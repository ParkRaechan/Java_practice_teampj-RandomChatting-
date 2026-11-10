// Existing browser contract retained: profile on open; a chat message initiates matching.
const id = sessionStorage.getItem("mid");
const yname = sessionStorage.getItem("yname");
const yage = sessionStorage.getItem("yage");
const ysex = sessionStorage.getItem("ysex");
const tsex = sessionStorage.getItem("tsex");
const locationX = sessionStorage.getItem("locationX");
const locationY = sessionStorage.getItem("locationY");

$("#yname").text(yname);
$("#yage").text(yage);
$("#ysex").text(ysex);

const scheme = window.location.protocol === "https:" ? "wss:" : "ws:";
const msgwebsocket = new WebSocket(scheme + "//" + window.location.host + "/ws/message/" + encodeURIComponent(id || ""));
msgwebsocket.onopen = function () {
    send({from:id, ysex:ysex, tsex:tsex, inchat:"", locationX:locationX,
          locationY:locationY, yname:yname, yage:yage});
};
msgwebsocket.onclose = function () { appendStatus("연결이 종료되었습니다."); };
msgwebsocket.onmessage = function (event) {
    let data;
    try { data = JSON.parse(event.data); }
    catch (e) { appendStatus(event.data); return; }
    if (data && typeof data.msg === "string") {
        appendBubble(data.msg, false);
    }
};

function appendStatus(value) {
    $("<h5>").css("color", "blue").text(value).appendTo("#chat");
}
function appendBubble(value, mine) {
    const outer = $("<div>").addClass("d-flex mb-4")
        .addClass(mine ? "justify-content-end" : "justify-content-start");
    $("<div>").addClass(mine ? "msg_cotainer_send" : "msg_cotainer")
        .text(value).appendTo(outer);
    $("#chat").append(outer);
}
function send(value) {
    if (msgwebsocket.readyState === WebSocket.OPEN)
        msgwebsocket.send(JSON.stringify(value));
}
function toSend() {
    const msg = $("#input3").val();
    if (!msg || !msg.trim() || msgwebsocket.readyState !== WebSocket.OPEN) return;
    send({from:id, msg:msg, inchat:"", yname:yname, yage:yage});
    appendBubble(msg, true);
    $("#input3").val("");
}
function enterKey() {
    if (window.event && window.event.keyCode === 13) toSend();
}
$("#sendbtn").click(toSend);
