
$(document).ready( function(){
    let rand =  Math.floor( Math.random()*1001 );
    alert(rand);

    let mid =  rand;
    // 문의하기 버튼을 클릭했을때
    $("#sendbtn").click( function(){

        let from = mid;
        let to = $("#input2").val();
        let msg = $("#input3").val();
        let jsonmsg = {
            "from" : from ,
            "to" : to ,
            "msg" : msg
        }
        send(  jsonmsg  );
    });
    // 1. js 웹소켓 객체 생성                      // 세션 만으로 회원 구분 X ---> 경로에 회원아이디 추가(식별용)
    let msgwebsocket = new WebSocket("ws://localhost:8282/ws/message/"+mid);
    // 2. 웹소켓객체에 구현된 메소드 저장한다.
    msgwebsocket.onopen = onOpen2;
    msgwebsocket.onclose = onClose2;
    msgwebsocket.onmessage = onMessage2;
    // 3. 각 메소드 구현  [ open close onMessage ]
    function onOpen2(){  }
    function onClose2(){ }
    function onMessage2(){ alert("메시지왔다.");  }
    function send( jsonmsg ){
        // json형식의 문자열 전송
        msgwebsocket.send(  JSON.stringify(jsonmsg) );
    }
});

