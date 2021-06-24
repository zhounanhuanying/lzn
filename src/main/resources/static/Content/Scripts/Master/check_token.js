// /* 定时器 间隔30秒检测是否长时间未操作页面 */
// window.setInterval(checkTimeout, 30000);
//获取储存的token
var token = localStorage.getItem("access_token");
//jquery全局配置
$.ajaxSetup({
    cache: false,
    crossDomain: true,
    headers :{'access_token':token},
    complete: function (xhr,request) {
       //var  codeText=xhr.responseText;
        var  codeText=xhr.responseJSON;

       // console.log(codeText.substring(3,9))
     if(codeText!=null){
         //console.log(codeText[0])
         //var  dataCode=JSON.parse(codeText);
         var  dataCode=codeText
         //var  dataCode=eval("("+codeText+")");
         if(dataCode.code=="1001") {
             // layui.use('layer',function () {
             //     layer.alert('登陆超时,请重新登陆', {
             //         icon: 5,
             //         title: "提示"
             //     },function() {
                     // window.parent.location.reload(); //刷新父页面
              //token过期，跳转到登录页面
                     window.top.location.href="/login";
                     // window.top.location.href= "/login.html";
                 // });
             // });

         }else if(dataCode.code=="1002"){
             // layui.use('layer',function () {
             //     layer.alert('token格式错误,请重新登陆', {
             //         icon: 5,
             //         title: "提示"
             //     },function() {
                     // window.parent.location.reload(); //刷新父页面
             //token过期，跳转到登录页面
                     window.top.location.href="/login";
                     // window.top.location.href= "/login.html";
                 // });
             // });
         }else if(dataCode.code=="1003"){
             // layui.use('layer',function () {
             //     layer.alert('token格式错误,请重新登陆', {
             //         icon: 5,
             //         title: "提示"
             //     },function() {
             // window.parent.location.reload(); //刷新父页面

             //token过期，跳转到登录页面
             window.top.location.href="/login";
             // window.top.location.href= "/login.html";
             // });
             // });
         }else if(dataCode.code=="1004"){
             // layui.use('layer',function () {
             //     layer.alert('token格式错误,请重新登陆', {
             //         icon: 5,
             //         title: "提示"
             //     },function() {
             // window.parent.location.reload(); //刷新父页面
             //token过期，跳转到登录页面
             window.top.location.href="/login";
             // window.top.location.href= "/login.html";
             // });
             // });
         }
     }
    }
});

