youtube();
function youtube(){
    $.ajax({
        url:"https://www.googleapis.com/youtube/v3/videos?key=구글api키기입&part=snippet&chart=mostPopular&maxResults=3&regionCode=kr",
        success:function(data){
            let items=data.items;
            let html='"<div><span class="chat_info">유튜브 실시간 급상승</span>&nbsp<span onclick="youtube()" style="color:yellow;">새로고침</span></div>"';
            for(let i=0; i<items.length; i++){
                html+='<div class="youtube_video" onclick="window.open(\'https://www.youtube.com/watch?v='+items[i].id+'\');">'+
                               '<img src="https://i.ytimg.com/vi/'+items[i].id+'/mqdefault.jpg"><br>'+
                               '<span>'+items[i].title+'</span>'+
                           '</div>';
            }
            $("#youtube").html(html);
        }
    });

}
