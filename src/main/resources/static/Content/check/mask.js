//********************************************************************************
//** 作者： 刘周南
//** 创建时间：2020-08-04
//** 描述：适用于页面上的数据量大的情况， 遮罩层，防止重复操作  需要在后端设置cookies
//*********************************************************************************/

//下载监测timer
function initDownloadCheckTimer(){
    configDownloadCheckTimer = window.setInterval(function() {
        var cookieValue = document.cookie;
        //获取cookies的值
        var resultCookie = getdescookie(cookieValue,'configDownloadToken');
        console.log(resultCookie);
        //判断是否执行完毕
        if ("1"==resultCookie) {
            //关闭遮罩层
            showloading(false);
            //删除cookies
            finishDownload() ;
        }
    }, 1000);
}

/**
 * 获取cookie中的值
 * @param strcookie
 * @param matchcookie
 * @returns {string}
 */
function getdescookie(strcookie,matchcookie){
    var getMatchCookie;
    var arrCookie=strcookie.split(";");
    for(var i=0;i<arrCookie.length;i++){
        var arr=arrCookie[i].split("=");
        if(matchcookie == arr[0]){
            getMatchCookie = arr[1];
            break;
        }
    }
    return getMatchCookie;
}
/**
 * 删除cookies的操作
 */
function finishDownload() {
    //停止定时器
    window.clearInterval(configDownloadCheckTimer);
    //删除cookies
    delCookie('configDownloadToken');
}
//删除cookies
function delCookie(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookies(name);
    console.log(cval);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
//获取cookies
function getCookies(name){
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}
//layui 遮罩层
function showloading(t) {
    if (t) {//如果是true则显示loading
        // console.log(t);
        var loadingIndex = layer.load(2, { //icon支持传入0-2
            shade: [0.5, 'gray'], //0.5透明度的灰色背景
            content: '请稍等...',
            color:'red',
            success: function (layero) {
                layero.find('.layui-layer-content').css({
                    'padding-top': '39px',
                    'width': '60px'
                });
            }
        });
    } else {
        console.log("关闭loading层:" + t);
        layer.closeAll('loading');
    }
}
