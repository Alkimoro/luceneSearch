function showMsgPanel(msg){
    $("body").append(
        "<div id='msgPanel' style='position: fixed;width: 100%;height: 100%;background-color: rgba(0,0,0,0.4);z-index: 9999;top: 0;left: 0;'>" +
            "<div style='position: fixed;left: 50%;top: 30%;opacity: 0;width: 500px;height: 130px;transform: translate(-50%, -50%);background-color:rgb(255,255,255);border-radius:5px;'>" +
                "<div class='showMsgPanelContent matscrollbar'>"+msg+"</div>"+
                "<div class='showMsgPanelButton' onclick='closeMsgPanel()'>确认</div>"+
            "</div>"+
        "</div>");
    $("#msgPanel div:first").animate({top:"50%",opacity:1},200);
}
function closeMsgPanel() {
    $("#msgPanel div:first").animate({top:"30%",opacity:0},200,function () {
        $("body #msgPanel").remove();
    });
}