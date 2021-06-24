$(function () { $("[data-toggle='tooltip']").tooltip(); });
var iframe;
//是否为首次登录
var iSFirstLogin = false;
//设置框架
$(function () {

    $("#sanJiao").bind('click', function (event) {
        //冒泡阻止事件
        event.stopPropagation();
    });
    loding.showLoding();
    GetBasicConfigData();
    common.AjaxPostAsync("/ModuleManager/GetModuleTreeList", { PremissionFilter: true, Sort: "SortNo", OrderBy: 1 },
        function (result) {
            var resultData = JSON.parse(result);
            if (resultData.status) {
                if (resultData.data != null) {
                    //左侧模块导航需要设置pId为0，否则不显示
                    $.each(resultData.data, function (index, module) {
                        if (module.pId == "") {
                            module.pId = 0;
                        }
                    })
                }
                showFrame(resultData.data);

            } else {
                layer.alert(resultData.msg, { icon: 6 });
            }
        }, false);

    //获取当前用户的登录日志
    GetLastestLoginLog();
    $("body").find('iframe').on("load", function () {
        loding.hideLoding();
    });
    //修改密码
    $("#btnUpdatePwd").click(function () {
        UpdatePwd();
    })
    $('#btnExit').on('click', function () {
        layer.confirm('是否确定退出系统？', {
            btn: ['是', '否'],//按钮
            icon: 2,
        },
      function (index) {
          layer.close(index);
          loding.showTextLoding("正在退出当前用户...");
          common.AjaxPost("/Login/LoginOut", {},
            function (result) {
                if (result == "true") {
                    location.href = "/Login/LoginIndex";
                }
                else {
                    layer.alert('退出异常', { icon: 6 });
                }
                loding.hideLoding();
            }, function () {
                layer.alert('退出异常', { icon: 6 });
                loding.hideLoding();
            });
      });
    });
    $("body").on("mouseleave", ".dropdown-menu", function () {
        $(this).parent().removeClass("open");
    })
    $("body").on("mouseleave", ".administrator", function () {
      //  $(this).removeClass("open");
        $("body").removeClass("big-page");
        $(this).find('i.glyph-icon').attr("class", "glyph-icon fa fa-caret-down");
    })
    $("body").on("mouseleave", ".dropdown-op", function () {
        $(this).removeClass("open");
    })
    $("#btnShowHelpList").click(function () {
        $(".tooltip").remove();
    })
    $(document).click(function (e) {
        e.stopPropagation();
        $(".about-frame").hide();
    })
    $("#btnAbout").click(function (e) {
        //layer.open({
        //    type: 1,
        //    title: "关于系统",
        //    area: ['400px', '250px'],
        //    shadeClose: true, //点击遮罩关闭
        //    content: $('#AboutUs'),
        //    btn: ['确定'],//按钮

        //});
        $('#AboutUs').show();
        e.stopPropagation();
    })
    $(".about-frame .about-close").click(function (e) {
        $('#AboutUs').hide();
        e.stopPropagation();
    })
    $("#btnMyInfo").click(function () {
        var userid = $(this).attr("data-id");
        common.AjaxPost("/UserManager/GetUserByUserId", { userId: userid },
          function (result) {
              var resultData = JSON.parse(result);
              if (resultData.status) {
                  var user = resultData.data[0];
                  $("#spanAccount").text(user.ACCOUNT);
                  $("#spanRealName").text(user.REALNAME);
                  $("#spanGender").text(user.GENDER == 1 ? "男" : "女");
                  $("#spanMobile").text(user.MOBILE);
                  $("#spanPhone").text(user.TELEPHONE);
                  $("#spanBirthday").text(common.DateFormater(user.BIRTHDAY, "yyyy-MM-dd"));
                  $("#spanEmail").text(user.EMAIL);
                  $("#spanQQ").text(user.QICQ);
                  $("#spanDuty").text(user.DUTY);
                  $("#spanTitle").text(user.TITLE);
                  $("#spanOrgan").text(user.OrganName);
                  layer.open({
                      type: 1,
                      title: "个人信息",
                      area: ['600px', '400px'],
                      shadeClose: true, //点击遮罩关闭
                      content: $('#MyInfo'),
                      btn: ['确定'],//按钮

                  });
              }
          });
    })
    $("#btnStatisticsRule").click(function () {
        common.openIframe("/Apartment/Apartment/CalculationRules", "数据统计帮助", false, true);
    })

    //判断用户是否首次登录
    if (iSFirstLogin != true)
        return;
    layer.confirm("您是首次登录，请问您是否需要修改原始密码？", function (r) {
        if (r) {
            layer.closeAll();
            UpdatePwd();
        }
    });


});
//在父级显示Loding层方法组
var loding = {};
loding.showLoding = function () {
    var loadText = "正在加载";
    $("html").busyLoad("show", {
        background: "rgba(0,0,0, 0.4)",
        image: "/Content/Images/loding.gif",
        maxSize: "300px",
        minSize: "200px",
        text: loadText,
        textPosition: "bottom"
    });
    $(".busy-load-container").css("z-index", 9999);
}
loding.showTextLoding = function (loadText) {
    $("html").busyLoad("show", {
        background: "rgba(0,0,0, 0.4)",
        image: "/Content/Images/loding.gif",
        maxSize: "300px",
        minSize: "200px",
        text: loadText,
        textPosition: "bottom"
    });
    $(".busy-load-container").css("z-index", 9999);
}
loding.hideLoding = function () {
    $("html").busyLoad("hide");
}
///展示框架左侧导航
var showFrame = function (data) {
    iframe = $("#Sellerber").frame({
        float: 'left',//设置菜单栏目方向属性
        minStatue: true,//切换模式
        hheight: 60,//设置顶部高度 高度为0时自动样式切换（达到另外一种界面效果）
        bheight: 0,//设置底部高度
        mwidth: 160,//菜单栏宽度（最好不要改动该数值，一般200的宽度值最好）
        switchwidth: 50,//切换菜单栏目宽度
        color_btn: '.skin_select',//设置颜色
        menu_nav: '.menu_list',//设置栏目属性
        column_list: '.column_list',//设置栏目名称
        time: '.date_time',//设置时间属性(不填写不显示)
        logo_img: '~/Content/Images/logo_01.png',//logo地址链接（当header为0时显示该属性）
        Sellerber_content: '.Sellerber_content',//右侧名称
        Sellerber_menu: '.list_content', //左侧栏目
        header: '.Sellerber_header',//顶部栏目
        prompt: '.prompt_style',
        prompt_btn: '#promptbtn',//点击事件
        data: data,//数据
        mouIconOpen: "fa fa-angle-down",// 菜单(展开)图标
        mouIconClose: "fa fa-angle-up", // 菜单（隐藏）图标
        Rightclick: true,//是否禁用右键
        showMenu: ShowMenu,   //是否显示菜单导航
        multiTabs: MultiTabs//是否打开多个页签

    });
}
//修改密码
function UpdateUserPwd() {
    var oldPwd = $("#txtOldPwd").val();
    var newPwd = $("#txtNewPwd").val();
    var rePwd = $("#txtRePwd").val();
    var userId = $("#hfUserId").val();
    if (oldPwd == "") {
        layer.msg("请输入原密码！");
        return false;
    }
    if (newPwd == "") {
        layer.msg("请输入新密码！");
        return false;
    }
    if (rePwd == "") {
        layer.msg("请输入确认密码！");
        return false;
    }
    if (newPwd.length < 6 || newPwd.length > 20) {
        layer.msg("新密码长度为6到20个字符之间！");
        return false;
    }
    if (newPwd != rePwd) {
        layer.msg("新密码跟确认密码不一致，请重新输入！");
        return false;
    }
    common.AjaxPost("/UserManager/UpdateUserPwd", { userId: userId, newPwd: newPwd, oldPwd: oldPwd }, function (result) {
        var resultData = JSON.parse(result);
        if (resultData.status) {
            layer.closeAll();
        }
        layer.msg(resultData.msg);
    })
}
var ShowMenu = true;
var MultiTabs = true;
//配置项数据
function GetBasicConfigData() {
    var url = "/BasicConfiguration/GetBasicConfig";
    common.AjaxPostAsync(url, null, function (reslut) {
        var resultData = JSON.parse(reslut);
        if (resultData.status) {
            if (resultData.data) {
                var systemMenuIsHide = resultData.data.SystemMenuIsHide;
                var copyRight = resultData.data.RightsReserv;
                var skin = resultData.data.Skin;
                //配置主页
                $('#iframeHome').attr('src', resultData.data.DefaultPage);
                if (copyRight && copyRight != "") {
                    $("#spanCopyRight").text("版权所有：" + copyRight);
                }
                //顶部告警配置
                if (resultData.data.IsAlarm == true) {
                    $("#IsAlarm").show();
                } else {
                    $("#IsAlarm").hide();

                }

                ////导航标签
                if (resultData.data.IsShowLabel == true) {
                    $("#breadcrumbs").css("display", "block");
                    $("#iframe_box").css("top", "41px");
                } else {
                    $("#breadcrumbs").css("display", "none");
                    $("#iframe_box").css("top", "0px");
                }

                if (!systemMenuIsHide) {
                    ShowMenu = true;
                } else {
                    ShowMenu = false;
                }
                MultiTabs = resultData.data.MultiTabs;
            }
        } else {
            $.messager.alert('错误', resultData.msg);
        }
    }, false);
}
//获取当前用户登录日志信息
function GetLastestLoginLog() {
    common.AjaxPostAsync("/LogManager/GetLastestLoginLog", "", function (result) {
        var resultData = JSON.parse(result);
        if (resultData.data != null && resultData.data.length == 1) {
            //获取当前系统时间
            var datatime = new Date();
            var systemTime = common.DateFormater(datatime, "yyyy-MM-dd hh:mm:ss");
            //取到日志时间并增加2秒
            var datetm = common.DateFormater(resultData.data[0].CREATEDATE, "yyyy-MM-dd hh:mm:ss");
            datetm = new Date(Date.parse(datetm.replace(/-/g, "/")));
            datetm = datetm.getTime() + 1000 * 2;
            datetm = common.DateFormater(datetm, "yyyy-MM-dd hh:mm:ss");
            //判断日志时间是否大于当前系统时间
            if (datetm > systemTime) {
                iSFirstLogin = true;//true为首次登录。
            }
        }
    }, false);
}
//修改密码
function UpdatePwd() {
    $("#txtOldPwd").val("");
    $("#txtNewPwd").val("");
    $("#txtRePwd").val("");
    layer.open({
        type: 1,
        title: "修改密码",
        area: ['380px', '300px'],
        shadeClose: true, //点击遮罩关闭
        content: $('#PwdDiv'),
        btn: ['保存', '取消'],//按钮
        yes: function () {
            UpdateUserPwd();
        },
        btn2: function () {
        }
    });
}
