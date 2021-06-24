var loginInfo = (function () {
    var loginObject = {};
    loginObject.init = function (_txt_userName, _txt_userPwd, _btn_logIn, _btn_unSet) {
        var nameObj = $("#" + _txt_userName);
        var pwdObj = $("#" + _txt_userPwd);

        //登录按钮事件
        $("#" + _btn_logIn).on("click", function () {
            var Name = nameObj.val() || "";
            var Pwd = pwdObj.val() || "";

            var loginInfo = {
                UserName: Name,
                PassWord: Pwd
            };
            if (Name == "" || Pwd == "") {
                // $.messager.alert("提醒", "用户名或密码不能为空");
                layer.alert("用户名或密码不能为空!");
                return false;
            }
            else {
                loginObject.checkLogin(loginInfo);
            }
        });

        //重置按钮事件
        $("#" + _btn_unSet).on("click", function () {
            nameObj.val("");
            pwdObj.val("");
        });
    }

    /*登录方法*/
    loginObject.checkLogin = function (json) {
        loginObject.showLoging("正在验证登录信息..");
        common.AjaxPost("/Login/CheckLogin", json, function (result) {
            var resultData = JSON.parse(result);
            if (resultData.status === true) {
                $(".busy-load-text").text("验证成功,正在登录...");
                var datatime = new Date();
                var systemTime = common.DateFormater(datatime, "yyyy-MM-dd hh:mm:ss");
                var paramtime = {
                    systime: systemTime
                }
                common.AjaxPostAsync("/Login/InsertLoginLog", paramtime, function () {

                }, false);
                window.location = "/Home/Index";
            } else {
                loginObject.hideLoding();//隐藏遮罩层
                //$.messager.alert("提醒", resultData.msg);
                layer.alert(resultData.msg);
                $("#unSet").click();
            }
        });
    }
    //显示遮罩层
    loginObject.showLoging = function (loadText) {
        var loadText = loadText || "正在登录";
        $("body").busyLoad("show", {
            background: "rgba(0,0,0, 0.4)",
            image: "/Content/Images/loding.gif",
            maxSize: "300px",
            minSize: "200px",
            text: loadText,
            textPosition: "bottom"
        });
        $(".busy-load-container").css("z-index", 9999);
    }
    //隐藏遮罩层
    loginObject.hideLoding = function () {
        $("body").busyLoad("hide");
    }
    return loginObject;
})();

$(function () {
    //屏幕的宽高
    winHeight = $(window).height();
    winWidth = $(window).width();
    $('html').css({ height: winHeight });
    loginInfo.init("UserName", "UserPwd", "checkLogin", "unSet");

    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            if ($(".layui-layer-shade").length == 0) {
                $("#checkLogin").click();
            }
            layer.closeAll();//回车关闭layer
            return false;
        }
    });
});