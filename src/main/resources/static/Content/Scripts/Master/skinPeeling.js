
/**
* 动态加载CSS
* @param {string} url 样式地址
*/
var dynamicLoadCss = function (url) {
    var head = document.getElementsByTagName('head')[0];
    var link = document.createElement('link');
    link.type = 'text/css';
    link.rel = 'stylesheet';
    link.href = url;
    $(head).append(link);
}
/**
*换肤方法
*/
var skinPeeling = function () {
    var locationStr = location.pathname.toLowerCase();
    var isLogin = false;
    var url = "/BasicConfiguration/GetBasicConfig";
    if (locationStr.indexOf("/login/loginindex") > -1) {
        isLogin = true;
        url = "/BasicConfiguration/GetBasicConfig";
    }
    var skinFile = "/Content/style/skin/dpsim/skin.css";
    var loginSkinFile = "/Content/style/skin/dpsim/login.css";
    $.ajax({
        type: "post",
        url: url,
        async: false,
        success: function (result) {           
            var resultData = JSON.parse(result);
            if (resultData.status) {
                if (resultData.data) {
                    var skin = resultData.data.Skin;
                    if (skin && skin != "") {
                        skinFile = "/Content/style/skin/" + skin + "/skin.css";
                        loginSkinFile = "/Content/style/skin/" + skin + "/login.css";
                    }
                }
            } else {
                $.messager.alert('错误', resultData.msg);
            }
        }
    })
    if (isLogin) {
        dynamicLoadCss(loginSkinFile);
    }
    else {
        dynamicLoadCss(skinFile);
    }
}
skinPeeling();
