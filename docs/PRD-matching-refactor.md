# 너만오면GO 매칭 리팩토링 PRD

## Source of requirements
Based on the original MsgWebSocketHandler and the user's retrospective refactor document. This is a reconstructed specification, not an independently supplied original PRD.

## Goals
- Preserve ephemeral, database-free WebSocket chat.
- Separate protocol handling, session lifecycle, and candidate scoring.
- Prevent a session from being paired with two different users concurrently.
- Avoid duplicative nested maps and JSON creation.
- Preserve the browser message fields (from, msg, inchat, yname, yage, ysex, tsex, locationX, locationY).

## Matching policy (deliberate behavior change)
- Age difference 0..2 years only, favor smaller difference.
- Reciprocal gender preference outranks one-sided preference, which outranks fallback.
- Candidate distance must be <=5km. Buckets: <=1km, <=2km, <=5km.
- Score is (3-ageDiff)*100 + preferenceScore*10 + distanceBucket.
- Ties resolve by lexicographically smallest session ID.
- The legacy handler used age 1/2-year expansions and distance <1/<100 km, and sometimes made early exits. This PR standardizes those rules based on the previous refactor notes; results are not guaranteed identical.

## Integration
- Kakao coordinates use locationX longitude and locationY latitude.
- Profile arrives on WebSocket open; first chat message doubles as a match request and is forwarded to selected peer.
- Unmatched requests return '찾는 사람이 없습니다'; disconnected peers receive '상대방이 나갔습니다.'.
- Browser parses JSON with JSON.parse and inserts messages using text nodes (not HTML).
- WebSocket uses page origin rather than an IP and hardcoded port; deployment proxy must expose /ws/message/*.

## Verification
- JUnit tests cover age/preference/tie/candidate exclusion/distance basics.
- Additional browser E2E and concurrent-session load testing are required before merge.
- Verify deployment reverse proxy, old client behavior, and error/disconnect recovery.
- No historical chat persistence or login requirement added.
