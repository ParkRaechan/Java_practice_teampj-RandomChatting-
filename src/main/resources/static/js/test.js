
//아이디 랜덤 생성
let rand =  Math.floor( Math.random()*1001 );
alert(rand);

let mid =  rand;            //랜덤 아이디 뿌리기
let sex_absence1 = false;    //자신 성별 선택 유무
let sex_what1 = "";         //자신 성별 선택지
//자기성별선택시
const showValue1 = (target) => {
  if(target.value==""){
    sex_absence1 = false;
    alert("다시 선택해주시길 바랍니다.");
  }else{
    sex_absence1 = true;
    sex_what1 = target.value;
    alert(target.options[target.selectedIndex].text);
  }
}
let from = mid;
let to = $("#input2").val();
let msg = $("#input3").val();
let ysex = sex_what1;
let tsex = sex_what2;
let jsonmsg = {
    "from" : from ,
    "to" : to ,
    "msg" : msg ,
    "ysex" : ysex ,
    "tsex" : tsex
}
if(sex_absence1==true){
    if(sex_absence2==true){
        send(  jsonmsg  );
    }else{alert("상대의 성별 선택 바람");}
}else{
    alert("당신의 성별 선택 바람");
}
let sex_absence2 = false;    //상대 성별 선택 유무
let sex_what2 = "";         //상대 성별 선택지
//상대성별선택시
const showValue2 = (target) => {
  if(target.value==""){
    sex_absence2 = false;
    alert("다시 선택해주시길 바랍니다.");
  }else{
    sex_absence2 = true;
    sex_what2 = target.value;
    alert(target.options[target.selectedIndex].text);
  }
}
// 전송 버튼을 클릭했을때
$("#sendbtn").click( function(){


    // 1. js 웹소켓 객체 생성                      // 세션 만으로 회원 구분 X ---> 경로에 회원아이디 추가(식별용)
    let msgwebsocket = new WebSocket("ws://localhost:8282/ws/message/"+mid+"/"+ysex+"/"+tsex);
    // 2. 웹소켓객체에 구현된 메소드 저장한다.
    msgwebsocket.onopen = onOpen2;
    msgwebsocket.onclose = onClose2;
    msgwebsocket.onmessage = onMessage2;
    // 3. 각 메소드 구현  [ open close onMessage ]
    function onOpen2(){  }
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
        let sexual_2_1 = data.split(",")[4];//성별 데이터 통째로 가져오기
        let sexual_2_2 = sexual_2_1.split(":")[1];// 성별만 가져오기
        let sexual_2 = sexual_2_2.substring(0,sexual_2_2.length-1);//}빼기

        let html = "";
        // 1. 본인 보낸 메시지 이면

        html = opponent+":"+ message+",상대-"+sexual_1+",나-"+sexual_2;
        alert(html);
    }
    function send( jsonmsg ){
        // json형식의 문자열 전송
        msgwebsocket.send(  JSON.stringify(jsonmsg) );
    }

});
