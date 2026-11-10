<img src="https://user-images.githubusercontent.com/100547978/180348593-2bcef817-9937-4c72-9061-71889689ce3d.png" width="700" height="380">  

# 💬 알고리즘 매칭 채팅  
https://parkrc1201log.tistory.com/24  
--------  
## 📌 Project Overview

**너만오면GO**는 Spring Boot와 WebSocket 기반의 **일회성 실시간 매칭 채팅** 팀 프로젝트입니다. 나이, 선호 성별, 위치를 평가해 대화 상대를 연결하며 채팅 기록은 데이터베이스에 저장하지 않습니다.

| 구분 | 기술 및 특성 |
| --- | --- |
| Backend | Java 8, Spring Boot 2.7, Spring WebSocket |
| Frontend | Thymeleaf, JavaScript, jQuery |
| 위치 | Kakao Maps JavaScript SDK, Haversine 거리 계산 |
| 매칭 | **점수 기반 후보 선정 알고리즘** |
| 상태 관리 | 서버 메모리의 WebSocketSession 매핑 |
| 데이터베이스 | 사용하지 않음 (일회성 채팅) |

## 🚀 Key Features & Architecture

### 1) 핵심: 점수 기반 매칭 알고리즘으로 리팩토링

**Why?** 기존 `MsgWebSocketHandler`는 나이, 성별, 거리 조건별로 여러 `HashMap`을 만들고 중첩된 `if-else`를 반복했습니다. WebSocket 처리와 매칭 정책이 결합되어 있어 기능 확장과 독립 테스트가 어려웠습니다.

**How?** AI와 함께 기존 구조를 분석하고 PRD의 입력·처리·결과 흐름 및 제약사항을 정리한 뒤, **적격 후보 필터링 → 후보별 점수 계산 → 최고 점수 선택**으로 재설계했습니다.

| 평가 기준 | 상세 조건 | 부여 점수 |
| --- | --- | ---: |
| 나이 | 동갑 / 1살 차 / 2살 차 | 300 / 200 / 100 |
| 성별 선호 | 상호 일치 / 한쪽만 일치 / 모두 불일치 | 20 / 10 / 0 |
| 거리 | 1km 이하 / 2km 이하 / 5km 이하 | 3 / 2 / 1 |

- 나이 차이가 **2살 초과**하거나 거리가 **5km 초과**하면 후보에서 제외합니다.
- 점수 가중치에 따라 **나이 > 성별 선호 > 거리** 순서가 유지됩니다.
- 최고점 후보를 선택하고 동점이면 사용자 ID의 사전순으로 결정합니다.
- 이전의 조건별 탐색 규칙을 개선한 것으로, 과거 버전과 매칭 결과가 동일하지 않을 수 있습니다.

```java
// MatchingPolicy.java - 점수 계산 핵심 코드
int ageDiff = Math.abs(me.age - other.age);
if (ageDiff > 2) return -1;

double km = distanceKm.applyAsDouble(me, other);
if (!Double.isFinite(km) || km < 0 || km > 5) return -1;

int preference = me.preferredSex.equals(other.sex)
              && other.preferredSex.equals(me.sex) ? 2
              : (me.preferredSex.equals(other.sex) ? 1 : 0);
int distance = km <= 1 ? 3 : km <= 2 ? 2 : 1;

return (3 - ageDiff) * 100 + preference * 10 + distance;
```

### 2) WebSocket 통신과 매칭 정책의 책임 분리

**Why?** 연결 이벤트, 데이터 모델, 후보 평가, 사용자 상태 관리까지 한 클래스에서 처리하면 수정 영향 범위가 넓어집니다.

**How?** 기능을 역할별 클래스에 분배하여 핵심 매칭 정책을 WebSocket 연결 없이 테스트할 수 있게 구성했습니다.

| 구성 | 책임 |
| --- | --- |
| `MsgWebSocketHandler` | 메시지 수신·전달, 접속 종료 이벤트 |
| `MatchingService` | 접속 세션과 양방향 매칭 상태 관리 |
| `MatchingPolicy` | 후보의 점수 계산과 최고점 선정 |
| `ChatUser` | 나이, 성별, 위치 등을 담는 불변 모델 |

`MatchingService`의 상태 변경 시 `synchronized`를 적용하여 **단일 JVM 내부**의 동시 매칭 경쟁을 줄이도록 구성했습니다. 분산 서버 사이의 동기화를 구현한 것은 아닙니다.

### 3) DB 없는 실시간 채팅

- **Why?** 채팅 내역을 남기지 않는 일회성 대화라는 프로젝트 취지를 유지하기 위함입니다.
- **How?** 사용자와 상대 WebSocket 세션을 서버 메모리에서 연결하고 메시지를 중계합니다.
- 연결 종료 시 상대 매칭 상태를 해제하며, 서버 재시작 시 연결 및 상태는 유지되지 않습니다.

### 4) 위치 기반 매칭 및 프론트엔드 개선

- **위치:** Kakao Maps의 `locationX`(경도), `locationY`(위도)를 구분하고 Haversine 공식으로 거리를 계산합니다.
- **JSON:** 문자열을 쉼표로 분리하던 방식에서 `JSON.parse()`로 변경했습니다.
- **출력:** 사용자 채팅은 HTML 문자열 결합 대신 jQuery `.text()`로 출력합니다.
- **통신:** WebSocket URL은 하드코딩된 IP가 아닌 현재 페이지의 호스트 및 `ws/wss` 스킴을 사용합니다.

## 🔄 Matching Flow

```text
[사용자 프로필 입력]
  나이 / 본인 성별 / 선호 성별 / 위치
                ↓
[WebSocket 등록 및 후보 검색]
  연결 중이면서 매칭되지 않은 사용자
                ↓
[후보 필터링]
  나이 차이 ≤ 2살 / 거리 ≤ 5km
                ↓
[점수 기반 평가]
  나이 점수 + 성별 선호 점수 + 거리 점수
                ↓
[최고 점수 상대 선택]
  동점은 사용자 ID 순서
                ↓
[양방향 세션 매칭 및 메시지 전송]
```

## 📂 Directory Structure

```text
src/
├── main/
│   ├── java/RandomChatting/
│   │   ├── config/
│   │   │   ├── MsgWebSocketHandler.java
│   │   │   ├── WebSocketConfig.java
│   │   │   └── KakaoMapViewConfig.java
│   │   ├── model/ChatUser.java
│   │   └── service/
│   │       ├── MatchingService.java
│   │       └── MatchingPolicy.java
│   └── resources/
│       ├── application.properties
│       ├── templates/
│       └── static/js/
└── test/java/RandomChatting/service/MatchingPolicyTest.java
```

## 🛠 Installation & Setup

Java 8, Gradle Wrapper 기준입니다.

```bash
git clone https://github.com/ParkRaechan/Java_practice_teampj-RandomChatting-.git
cd Java_practice_teampj-RandomChatting-

# Linux / macOS
export KAKAO_JAVASCRIPT_KEY="YOUR_KAKAO_JAVASCRIPT_KEY"
./gradlew bootRun

# Windows PowerShell
# $env:KAKAO_JAVASCRIPT_KEY="YOUR_KAKAO_JAVASCRIPT_KEY"
# .\gradlew.bat bootRun
```

## 📖 References  
- [ 알고리즘 리팩토링 ](https://parkrc1201log.tistory.com/24)

<details>
<summary>웹사이트 프론트</summary>
  
![image](https://user-images.githubusercontent.com/100547978/180403606-7e34ac28-fc85-4df3-922c-ed95d2fa18fe.png)  
  
![image](https://user-images.githubusercontent.com/100547978/180403558-2d98d8d3-5af8-47c0-997d-19743d2f1568.png)  
</details>
