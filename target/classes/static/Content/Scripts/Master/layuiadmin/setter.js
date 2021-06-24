 /** EasyWeb spa v3.1.8 date:2020-05-04 License By http://easyweb.vip */
layui.define(['table'], function (exports) {
    // 获取domain
    var cookieDomain = '';
    if(document.domain.indexOf('localhost') != -1 || !isNaN(document.domain.substring(0,document.domain.indexOf('.')))){
        cookieDomain = document.domain;
    } else {
        var locationUrl = window.location.href + "/a";
        cookieDomain ='.' + locationUrl.replace(/http:\/\/.*?([^\.]+\.(com\.cn|org\.cn|net\.cn|[^\.]+))\/.+/, "$1");
    }

    var setter = {
        tonkeServer: '', // 获取token地址
        logoutUrl: '',  // 退出登录地址
        headerName: 'Authorization',
        headerPrefix: 'Bearer ',
        pageTabs: false,   // 是否开启多标签
        cacheTab: true,  // 是否记忆Tab
        defaultTheme: '',  // 默认主题
        openTabCtxMenu: true,   // 是否开启Tab右键菜单
        maxTabNum: 20,  // 最多打开多少个tab
        viewPath: 'components', // 视图位置
        viewSuffix: '.html',  // 视图后缀
        reqPutToPost: true,  // req请求put方法变成post
        apiNoCache: true,  // ajax请求json数据不带版本号
        tableName: 'dlax',  // 存储表名
        accessToken: 'accessToken',
        expiresIn: 'expiresIn',




        /* 获取缓存的token */
        getCookieValue: function (cookieName) {
            cookieName = cookieName + '=';
            // var cache = layui.data(setter.tableName);
            // if (cache) return cache.token;
            var decodedCookie = decodeURIComponent(document.cookie);
            var ca = decodedCookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(cookieName) == 0) {
                    return c.substring(cookieName.length, c.length);
                }
            }
            return "";
        },

        /* 获取请求头jwt */
        getHeader: function () {
            var accessToken = setter.getCookieValue(setter.accessToken);
            if (accessToken) {
                var expiresIn = setter.getCookieValue(setter.expiresIn);
                var nowTimestamp = parseInt(new Date().getTime() / 1000)
                if (nowTimestamp >= expiresIn) {
                    setter.refreshToken(accessToken);
                    accessToken = setter.getCookieValue(setter.accessToken);
                }
                return setter.headerPrefix + accessToken;
            }
            return '';
        },

        /* 清除token */
        removeToken: function () {
            var keys = document.cookie.match(/[^ =;]+(?==)/g)
            if (keys) {
                for (var i = keys.length; i--;) {
                    document.cookie = keys[i] + '=;-1;path=/;domain='+cookieDomain; // 清除当前域名下的,例如：m.ratingdog.cn
                }
            }

        },

        /* 当前登录的用户 */
        getUser: function () {
            var cache = layui.data(setter.tableName);
            if (cache) return cache.loginUser;
        },
        /* 缓存user */
        putUser: function (user) {
            layui.data(setter.tableName, {key: 'loginUser', value: user});
        },
        /* 获取用户所有权限 */
        getUserAuths: function () {
            var auths = [], user = setter.getUser();
            var authorities = user ? user.authorities : [];
            for (var i = 0; i < authorities.length; i++) {
                auths.push(authorities[i].authority);
            }
            return auths;
        },
        /* ajax请求的header */
        getAjaxHeaders: function (url) {
            // 验证tonken是否过期
            var accessToken = setter.getCookieValue(setter.accessToken);
            var headers = [];
            if (accessToken) {
                var expiresIn = setter.getCookieValue(setter.expiresIn);
                var nowTimestamp = parseInt(new Date().getTime() / 1000)
                if (nowTimestamp >= expiresIn) {
                    setter.refreshToken(accessToken);
                    accessToken = setter.getCookieValue(setter.accessToken);
                }
                headers.push({name: 'Authorization', value: 'Bearer ' + accessToken});
            }
            return headers;
        },
        /* ajax请求结束后的处理，返回false阻止代码执行 */
        ajaxSuccessBefore: function (res, url, obj) {
            if (res.code === 401) {  // 登录过期退出到登录界面
                layui.layer.msg('登录过期', {icon: 2, anim: 6, time: 1500}, function () {
                    
                    var loginUrl = '';

                    if(isCas){
                        var u = window.location.href;
                        var u2 = u.split("?")[0];
                        loginUrl = casLoginpath + "login.html?returnUrl" + u2;
                        return;
                    }
                    
                    if(conf.active == 'dev'){
                        loginUrl = loginUri
                    }else {
                        // var parenUlr = '';
                        // var iframeUlr = window.location.href;
                        // if(iframeUlr.indexOf(document.referrer) || document.referrer == ''){
                        //     iframeUlr = '#' +window.location.href.replace('http:/','');
                        // }
                        var loginParamJson = setter.getCookieValue("loginParam");
                        if(loginParamJson!= null && loginParamJson != ''){
                            var loginParam = JSON.parse(loginParamJson);
                            loginUrl = conf.dsrpBaseServer + loginUri + conf.applicationName + '/'+loginParam[0].loginTypeName+'/login.html?returnUrl='+window.location.href;
                        }else{
                            loginUrl = conf.dsrpBaseServer + loginUri + conf.applicationName + '/default/login.html?returnUrl='+window.location.href;
                        }
                    }
                    setter.removeToken();
                    location.replace(loginUrl);
                });
                return false;
            }
            if (res.code === 403){
                // todo 需要优化
                
                if(url.indexOf("mapconfig/findPersonMainControlPark") >0 || url.indexOf("'purviewmanage/getpurviewinfo'") >0){
                    return location.replace('/static/assets/components/nopermission/nopermission.html');
                }

                layui.layer.msg(res.msg, {icon: 2, anim: 6, time: 1500}, function () {
                });
                return false;
            }
            return true;
        },
    };
    /* table全局设置 */
    // var token = setter.getCookieValue();
    // if (token && token.accessToken) {
    //     layui.table.set({
    //         headers: {'Authorization': 'Bearer ' + token.accessToken}
    //     });
    //     console.log(layui.table.headers)
    // }
    setter.tonkeServer = conf.dsrpBaseServer + 'dsrp/token';
    setter.logoutUrl = conf.dsrpBaseServer + 'dsrp/logout';
    setter.base_server = setter.baseServer;  // 兼容旧版
    exports('setter', setter);

});
