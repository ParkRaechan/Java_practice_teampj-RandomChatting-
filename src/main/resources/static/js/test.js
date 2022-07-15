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
                //chat();
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
