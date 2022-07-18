var id = sessionStorage.getItem("mid");
var yy = sessionStorage.getItem("ysex");
var tt = sessionStorage.getItem("tsex");
$("#id").html(id);
$("#yy").html(yy);
$("#tt").html(tt);


/////////////////////////////////소켓 설정 구간////////////

// 1. js 웹소켓 객체 생성                      // 세션 만으로 회원 구분 X ---> 경로에 회원아이디 추가(식별용)
let msgwebsocket = new WebSocket("ws://localhost:8282/ws/message/"+id);//
// 2. 웹소켓객체에 구현된 메소드 저장한다.
msgwebsocket.onopen = onOpen2;
msgwebsocket.onclose = onClose2;
msgwebsocket.onmessage = onMessage2;
// 3. 각 메소드 구현  [ open close onMessage ]
function onOpen2(){
    //정보모으기
    let from = id;
    let ysex = yy;
    let tsex = tt;
    let inchat = "";
    let jsonmsg = {
        "from" : from ,
        "ysex" : ysex ,
        "tsex" : tsex ,
        "inchat" : inchat
    }
    send(  jsonmsg  );
}
function onClose2(){ }
function onMessage2(msg){
    let data = msg.data; // 받은 메시지의 내용
    let opponent_1 = data.split(",")[0];//보낸사람 데이터 통째로 가져오기
    let opponent = opponent_1.split(":")[1]; // 보낸사람만 가져오기

    let message_1 = data.split(",")[2];//메세지내용 데이터 통째로 가져오기
    let message = message_1.split(":")[1]; // 메시지내용만 가져오기
    //let message = qq3.substring(0,qq3.length-1);//}빼기
    let sexual_1_1 = data.split(",")[3];//성별 데이터 통째로 가져오기
    let sexual_1 = sexual_1_1.split(":")[1];// 성별만 가져오기
    //let sexual = sexual_2.substring(0,sexual_2.length-1);//}빼기
    //let sexual_2_1 = data.split(",")[4];//성별 데이터 통째로 가져오기
    //let sexual_2_2 = sexual_2_1.split(":")[1];// 성별만 가져오기
    //let sexual_2 = sexual_2_2.substring(0,sexual_2_2.length-1);//}빼기

    let html = "";

    html = opponent+":"+ message+",상대성별-"+sexual_1;
    alert(html);

}







////////////////////////////전송 구간////////////

//send함수
function send( jsonmsg ){
    // json형식의 문자열 전송
    msgwebsocket.send(  JSON.stringify(jsonmsg) );
}

let index =0;
// 전송 버튼을 클릭했을때
$("#sendbtn").click( function(){
    //정보모으기
    if(index==0){

        let from = id;
        let to = $("#input2").val();
        let msg = $("#input3").val();
        let ysex = yy;
        let tsex = tt;
        let inchat = "";
        let jsonmsg = {
            "from" : from ,
            "to" : to ,
            "msg" : msg ,
            "ysex" : ysex ,
            "tsex" : tsex ,
            "inchat" : inchat
        }
        index++;
        send(  jsonmsg  );
    }else{
        let from = id;
        let to = $("#input2").val();
        let msg = $("#input3").val();
        let ysex = yy;
        let tsex = tt;
        let inchat = "1";
        let jsonmsg = {
            "from" : from ,
            "to" : to ,
            "msg" : msg ,
            "ysex" : ysex ,
            "tsex" : tsex ,
            "inchat" : inchat
        }
        send(  jsonmsg  );
    }
});
