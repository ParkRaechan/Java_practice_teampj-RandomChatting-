
//아이디 랜덤 생성
let rand =  Math.floor( Math.random()*1001 );

let mid =  rand;            //랜덤 아이디 배포
sessionStorage.setItem("mid", mid );//아이디 세션에 저장
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
    sessionStorage.setItem("ysex", sex_what1 );
  }
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
    sessionStorage.setItem("tsex", sex_what2 );
  }
}




