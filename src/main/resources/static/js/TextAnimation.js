function switchTextUpAndDown(fatherId,newText,time,letterSpacing) {
    let helperEle=$(".textAnimationHelper");
    let fatherEle=$("#"+fatherId);

    //判断是否正在运行动画
    if(!isNaN(helperEle.attr("runningAnimationNum"))&&parseInt(helperEle.attr("runningAnimationNum"))!==0){
        fatherEle.finish();
        helperEle.nextAll().finish();
    }

    let nl=newText.length;
    let ol=helperEle.nextAll().length;
    helperEle.attr("runningAnimationNum",nl+ol+1);
    let cur=helperEle;
    let left=cur.next().css("left");
    let top=parseInt(cur.next().css("top"));
    left=parseInt(left);
    let parentWidth=parseInt(fatherEle.css("width"));
    parentWidth+=(nl-ol)*letterSpacing;
    for(let i=0;i<nl;i++) {
        cur=cur.next();
        let curLeft=left+i*letterSpacing;
        let curTop=top;
        if(cur.text()==newText.charAt(i)&&i<ol) {
            curTop-=10;
        }
        else {
            curTop-=20;
        }
        fatherEle.append("<span style='position: absolute;opacity: 0;top: "+curTop+"px;left: "+curLeft+"px;\'>"+newText.charAt(i)+"</span>");
    }
    cur=helperEle;
    fatherEle.animate({width:parentWidth+"px"},time,function(e){changeNumberAttribute(helperEle,"runningAnimationNum",-1);});
    for(let i=0;i<ol+nl;i++) {
        cur=cur.next();
        if(i<ol)
            cur.animate({top:"40px",opacity:"0"},time,function(e){$(this).remove();changeNumberAttribute(helperEle,"runningAnimationNum",-1);});
        else
            cur.animate({top:"10px",opacity:"1"},time,function(e){changeNumberAttribute(helperEle,"runningAnimationNum",-1);});
    }
}
function changeNumberAttribute(ele,attribute,increment) {
    if(ele.attr(attribute)==null){
        ele.attr(attribute,increment);
    }else{
        let num=parseInt(ele.attr(attribute));
        num+=increment;
        ele.attr(attribute,num);
    }
}