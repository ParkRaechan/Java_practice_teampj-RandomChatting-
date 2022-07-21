

//아이디 랜덤 생성
let rand =  Math.floor( Math.random()*1001 );
let mid =  rand;            //랜덤 아이디 배포


let sex_what1 = "";         //자신 성별 선택지
let sex_what2 = "";         //상대 성별 선택지
let yname_what = "";         //자신 이름
let yage_what = "";         //자신 나이

let pass = [ false , false , false , false, false ]; // 배열 = [  ]
//자신 성별 선택 유무   //상대 성별 선택 유무   //지역 설정 유무  //나이 설정 유무

//자기성별선택시
const showValue1 = (target) => {
  if(target.value==""){
    pass[0] = false;
    alert("다시 선택해주시길 바랍니다.");
  }else if(target.value=="남"){
    pass[0] = true;
    pass[1] = true;
    sex_what1 = "남";
    sex_what2 = "여";
    $("#target_sex").val("여").prop("seleted" ,true);
    alert(target.options[target.selectedIndex].text);
  }else if(target.value=="여"){
    pass[0] = true;
    pass[1] = true;
    sex_what1 = "여";
    sex_what2 = "남";
    $("#target_sex").val("남").prop("seleted" ,true);
    alert(target.options[target.selectedIndex].text);
  }
}
//상대성별선택시
const showValue2 = (target) => {
  if(target.value==""){
    pass[1] = false;
    alert("다시 선택해주시길 바랍니다.");
  }else if(target.value=="남"){
   pass[0] = true;
   pass[1] = true;
   sex_what1 = "여";
   sex_what2 = "남";
   $("#your_sex").val("여").prop("seleted" ,true);
   alert(target.options[target.selectedIndex].text);
 }else if(target.value=="여"){
   pass[0] = true;
   pass[1] = true;
   sex_what1 = "남";
   sex_what2 = "여";
   $("#your_sex").val("남").prop("seleted" ,true);
   alert(target.options[target.selectedIndex].text);
 }

}

$("#yage").keyup(function(){
    let key1 = $("#yage").val();
    if(key1==""){
    pass[2] = false;
        alert("나이를 입력해주세요!");
    }else{
        var num_check=/^[0-9]*$/;
        if(num_check.test(key1)){
            yage_what = key1;
            pass[2] = true;
        }else{
            alert("숫자로 입력해주세요!");
            pass[2] = false;
        }
    }
});

$("#yname").keyup(function(){
    let key2 = $("#yname").val();
    if(key2==""){
        pass[4] = false;
        alert("닉네임을 입력해주세요!");
    }else{
         yname_what = key2;
         pass[4] = true;
    }
});

function confirm(){
    let check = true;
    for(let i=0 ; i<pass.length ; i++){
        if(pass[i]==false){
            check = false;
        }
    }
    if(check){
        sessionStorage.setItem("mid", mid );//아이디 세션에 저장
        sessionStorage.setItem("yname", yname_what );//닉네임 세션에 저장
        sessionStorage.setItem("yage", yage_what );//나이 세션에 저장
        sessionStorage.setItem("locationX", locationX );
        sessionStorage.setItem("locationY", locationY );
        sessionStorage.setItem("tsex", sex_what2 ); //상대성별 세션에 저장
        sessionStorage.setItem("ysex", sex_what1 ); //자신성별 세션에 저장
        location.href="/chatting";
    }else{
        alert("전부 입력 바람");
    }
}







///////////////////////////////             지도관련               ////////////////////////////////



//////전역변수////////
let locationX=126.83865508614478; // 기본 X좌표, 수시로 변경
let locationY=37.315657294383875; // 기본 Y좌표, 수시로 변경
var positions=[]; // 유저의 좌표들
//////전역변수 끝//////


///////아래는 다음 주소 api////////
function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("sample5_address").value = addr;
                // 주소로 상세 정보를 검색
                geocoder.addressSearch(data.address, function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === daum.maps.services.Status.OK) {

                        var result = results[0]; //첫번째 결과의 값을 활용

                        // 해당 주소에 대한 좌표를 받아서
                        var coords = new daum.maps.LatLng(result.y, result.x);

                            locationX=result.x; // 전역변수에 x값 넣고
                            locationY=result.y; // y값도 넣기
                            pass[3] = true;
                            //console.log(locationX);
                            //console.log(locationY);
                        // 지도를 보여준다.
                        mapContainer.style.display = "block";
                        map.relayout();
                        // 지도 중심을 변경한다.
                        map.setCenter(coords);
//                        // 마커를 결과값으로 받은 위치로 옮긴다.
//                        marker.setPosition(coords)

                    }
                });
            }
        }).open();
    }
///////다음 주소 api 끝, 아래는 카카오 api////////
var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.315657294383875, 126.83865508614478), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };
var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다


var geocoder = new daum.maps.services.Geocoder();

