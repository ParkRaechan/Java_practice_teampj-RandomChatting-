function getLocation() {
  if (navigator.geolocation) { // GPS를 지원하면
    navigator.geolocation.getCurrentPosition(function(position) {
      $("#your_coordinate").html("위도:"+position.coords.latitude + ",경도:" + position.coords.longitude);
      //alert(position.coords.latitude + ' ' + position.coords.longitude);
    }, function(error) {
      console.error(error);
    }, {
      enableHighAccuracy: false,
      maximumAge: 0,
      timeout: Infinity
    });
  } else {
    alert('GPS를 지원하지 않습니다');
  }
}


//ip받아오기
function getyourip() {
		$.getJSON("https://api.ipify.org?format=jsonp&callback=?",
			function(json) {
			    y_ip = json.ip;
			    $("#yuor_ip").html(json.ip);
                chat();
				//document.write(json.ip);
			}
		);
}

var y_loc = "";
var y_ip = "";

// js 열리면 실행되는 메소드
$(document).ready( function(){
    //좌표받아오기
    getLocation();
    //아이피 받아오기
    getyourip();


});

    //채팅 메소드 시작
function chat(){
    // 1. 익명 닉네임 만들기
    let username ="익명"+y_ip;
    // 3. 클릭이벤트 메소드 정의
    $("#msgbtn").click(  function(){
            send();
      });
    // 2. JS에서 제공하는 websocket 클래스로 websocket 객체 선언
        // 1. [ /ws/chat ] 해당 ( spring :  webSocketHandler path )URL 로 소켓 연결
        // 2. 현재 js가 새로고침[F5] 되면 소켓도 초기화
    let websocket = new WebSocket("ws://localhost:8282/ws/chat");
    websocket.onmessage = onMessage;    // 아래에서 구현한 메소드를 웹소켓 객체에 대입
    websocket.onopen = onOpen;              // 아래에서 구현한 메소드를 웹소켓 객체에 대입
    websocket.onclose = onClose;            // 아래에서 구현한 메소드를 웹소켓 객체에 대입
    // 3. 소켓 연결이 종료 되었을때
    function onClose( ) {
         websocket.send( username +": 님이 나가셨습니다.");
    }
    // 4. 소켓 연결이 되었을때
    function onOpen() {
        websocket.send( username +": 님이 입장했습니다");
    }
    // 5. 메시지 전송
    function send(){
        let msg = $("#msg").val();  // 채팅 입력창에 입력한 데이터 호출
        websocket.send( username+":"+msg);
        $("#msg").val("");
    }
    // 6. 메시지를 받았을때
    function onMessage( msg ) {
        let data = msg.data; // 받은 메시지의 내용
        let sessionid = data.split(":")[0]; // 보낸사람
        let message = data.split(":")[1]; // 메시지내용

        let html = "";
        // 1. 본인 보낸 메시지 이면
        if( sessionid == username ){
            html += ' <div class="alert alert-primary">';
            html += ' <span>'+sessionid+":"+message;
            html += '</span> ';
            html += ' </div>';

        }else{ // 본인 보낸 메시지가 아니면
            html += ' <div class="alert alert-warning">';
            html += ' <span>'+sessionid+":"+message;
            html += '</span> ';
            html += ' </div>';
        }
        $("#contentbox").append(html); // html 추가
        // 스크롤 최하단으로 이동
        $("#contentbox").scrollTop(  $("#contentbox")[0].scrollHeight );
                // $("#contentbox")[0].scrollHeight : 스크롤의 전체길이
                //  $("#contentbox").scrollTop( ) : 스크롤의 막대의 상단 위치
    }
}