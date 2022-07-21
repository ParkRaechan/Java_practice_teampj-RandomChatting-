
var id = sessionStorage.getItem("mid");
var yname = sessionStorage.getItem("yname");
var yage = sessionStorage.getItem("yage");
var ysex = sessionStorage.getItem("ysex");
var tsex = sessionStorage.getItem("tsex");
var locationX = sessionStorage.getItem("locationX");
var locationY = sessionStorage.getItem("locationY");
$("#id").html(id);
$("#yname").html(yname);
$("#yage").html(yage);
$("#ysex").html(ysex);
$("#tsex").html(tsex);
$("#locationX").html(locationX);
$("#locationY").html(locationY);

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
    let inchat = "";
    let jsonmsg = {
        "from" : from ,
        "ysex" : ysex ,
        "tsex" : tsex ,
        "inchat" : inchat ,
        "locationX" : locationX ,
        "locationY" : locationY ,
        "yname" : yname ,
        "yage" : yage
    }
    send(  jsonmsg  );
}
function onClose2(){ }
function onMessage2(msg){
    let data = msg.data; // 받은 메시지의 내용
//    let opponent_1 = data.split(",")[0];//보낸사람 데이터 통째로 가져오기
//    let opponent = opponent_1.split(":")[1]; // 보낸사람만 가져오기
//
//    let message_1 = data.split(",")[2];//메세지내용 데이터 통째로 가져오기
//    let message = message_1.split(":")[1]; // 메시지내용만 가져오기
//    //let message = qq3.substring(0,qq3.length-1);//}빼기
//    let sexual_1_1 = data.split(",")[3];//성별 데이터 통째로 가져오기
//    let sexual_1 = sexual_1_1.split(":")[1];// 성별만 가져오기
    //let sexual = sexual_2.substring(0,sexual_2.length-1);//}빼기
    //let sexual_2_1 = data.split(",")[4];//성별 데이터 통째로 가져오기
    //let sexual_2_2 = sexual_2_1.split(":")[1];// 성별만 가져오기
    //let sexual_2 = sexual_2_2.substring(0,sexual_2_2.length-1);//}빼기


    if(data.length==11){
        alert(data);
    }else if(data.length==20){
        alert(data);
    }else if(data.length==16){
        alert(data);
    }
    else{
        let opponent_1 = data.split(",")[3];//보낸사람 닉네임 데이터 통째로 가져오기
        let opponent = opponent_1.split(":")[1]; // 보낸사람 닉네임만 가져오기
        //let opponent = opponent_2.substring(0,opponent_2.length-1);//}빼기
        let message_1 = data.split(",")[1];//메세지내용 데이터 통째로 가져오기
        let message = message_1.split(":")[1]; // 메시지내용만 가져오기

        let html = "";
        html = opponent + ":" +message;

        alert(html);
        //alert(data);
    }


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
        let msg = $("#input3").val();
        let inchat = "";
        let jsonmsg = {
            "from" : from ,
            "msg" : msg ,
            //"ysex" : ysex ,
            //"tsex" : tsex ,
            "inchat" : inchat ,
            //"locationX" : locationX ,
            //"locationY" : locationY ,
            "yname" : yname ,
            "yage" : yage
        }
        //index++;
        send(  jsonmsg  );
    }
});

