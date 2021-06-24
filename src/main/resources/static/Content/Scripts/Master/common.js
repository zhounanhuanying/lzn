// JavaScript Document

var common = {};


//通用post请求方法
common.AjaxPost = function (url, data, successCallback, failCallback) {
    $.ajax({
        url: url,
        type: "post",
        data: data,
        success: function (result) {
            successCallback(result);
        },
        error: function (result) {
            if (failCallback) {
                failCallback(result);
            }
        }
    });
}
//通用get请求方法
common.AjaxGet = function (url, data, successCallback, failCallback) {
    $.ajax({
        url: url,
        type: "get",
        data: data,
        success: function (result) {
            successCallback(result);
        },
        error: function (result) {
            if (failCallback) {
                failCallback(result);
            }
        }
    });
}
//通用post同步或者异步请求方法
common.AjaxPostAsync = function (url, data, successCallback, async) {
    $.ajax({
        url: url,
        type: "post",
        data: data,
        async: async,
        success: function (result) {
            successCallback(result);
        }
    });
}
/**************************************获得指定ztree选中节点下的所有节点的id************************************/
common.GetChildNodes = function (ztree, treeNode) {
    var childNodes = ztree.transformToArray(treeNode);
    var nodes = new Array();
    for (i = 0; i < childNodes.length; i++) {
        nodes.push(childNodes[i].id);
    }
    return nodes.join("^");
}

/**************************************获得地址栏参数************************************/
common.GetQueryString = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]); return null;
}

/**************************************合并2个json对象************************************/
common.MergeJsonObject = function (jsonbject1, jsonbject2) {
    var resultJsonObject = {};
    if (jsonbject1) {
        for (var attr in jsonbject1) {
            resultJsonObject[attr] = jsonbject1[attr];
        }
    }
    if (jsonbject2) {
        for (var attr in jsonbject2) {
            resultJsonObject[attr] = jsonbject2[attr];
        }
    }
    return resultJsonObject;
};
/**************************************合并2个数组对象************************************/
common.MergeArray = function (jsonbject1, jsonbject2) {
    var resultJsonObject = [];
    if (jsonbject1) {
        for (var i = 0; i < jsonbject1.length; i++) {
            resultJsonObject.push(jsonbject1[i]);
        }
    }
    if (jsonbject2) {
        for (var i = 0; i < jsonbject2.length; i++) {
            resultJsonObject.push(jsonbject2[i]);
        }
    }
    return resultJsonObject;
};
/**************************************数组对象转换为树************************************/
common.Array2Tree = function (arr) {
    var top = [], arrObj = {};
    arr.forEach(function (item) {
        var id = item.id, parentId = item.pId, parent, own;
        if (parentId == "-1" || parentId == "0") {
            top.push(item);
        }
        item.text = item.name;
        item.children = [];
        own = arrObj[id];
        if (own) {
            Object.keys(own).forEach(function (key) {
                item[key] = own[key];
            });
        }
        arrObj[id] = item;

        parent = arrObj[parentId];
        if (!parent) {
            parent = { 'id': parentId, 'children': [] };
            arrObj[parentId] = parent;
        }
        parent.children.push(item);

    });
    return top;
}


/**************************************时间格式化处理************************************/
common.DateFormater = function (dt, formater) { //author: meizz  
    if (dt) {
        var date = new Date(dt);
        var o = {
            "M+": date.getMonth() + 1,                 //月份   
            "d+": date.getDate(),                    //日   
            "h+": date.getHours(),                   //小时   
            "m+": date.getMinutes(),                 //分   
            "s+": date.getSeconds(),                 //秒   
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度   
            "S": date.getMilliseconds()             //毫秒   
        };
        if (/(y+)/.test(formater))
            formater = formater.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(formater))
                formater = formater.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return formater;
    }
    return "";
}
/**************************************指定的日期加一天************************************/
common.dateAdd = function (startDate, day) {
    startDate = new Date(startDate);
    startDate = +startDate + 1000 * 60 * 60 * 24;
    startDate = new Date(startDate);
    var nextStartDate = startDate.getFullYear() + "-" + (startDate.getMonth()) + "-" + (startDate.getDate() + day);
    return nextStartDate;
}

/**************************************截取字符串************************************/
common.cutStr = function (str, length) {
    if (!str) {
        return "";
    }
    if (str.length > length) {
        return str.substr(0, length) + "...";
    }
    return str;
}



common.tabNavallwidth = function () {
    var topWindow = $(window.parent.document);
    var taballwidth = 0,
        $tabNav = topWindow.find(".breadcrumb"),
        $tabNavWp = topWindow.find(".breadcrumbs"),
        $tabNavitem = topWindow.find(".breadcrumb li"),
        $tabNavmore = $(".Hui-tabNav-more");
    if (!$tabNav[0]) { return }
    $tabNavitem.each(function (index, element) {
        taballwidth += Number(parseFloat($(this).width() + 60));
    });
    $tabNav.width(taballwidth + 25);
    var w = $tabNavWp.width();
    if (taballwidth + 25 > w) {
        $tabNavmore.show();
    }
    else {
        $tabNavmore.hide();
        $tabNav.css({ left: 0 });
    }
}
//打开新框架
/*
href,要打开页面的地址
titlename,打开标签的标题
createNew，如果存在，是否创建新的页签
IsNewLable,是否为标签打开
*/

common.openIframe = function (href, titleName, createNew) {
    var dataName = GetMenuId(titleName);
    window.parent.document.getElementById("dataNameUrlParam").value = dataName;
    if (parent.loding) {
        parent.loding.showLoding();
    }
    if (createNew) {
        common.createIframe(href, titleName);
    } else {
        var url = href.split('?')[0];
        var bStopIndex = 0;
        var bStop = false;
        var topWindow = $(window.parent.document);
        var show_navLi = topWindow.find("#min_title_list li,#dropdown_menu li");
        show_navLi.each(function () {
            if ($(this).find('span').attr("data-href") == url && $(this).find('span').text() == titleName) {
                bStopIndex = show_navLi.index($(this));
                bStop = true;
                return false;
            }
        });
        if (!bStop) {
            common.createIframe(href, titleName);
            common.min_titleList();
        }
        else {
            show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
            var iframe_box = topWindow.find("#iframe_box");
            iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr({ "src": href, "data-href": url });
            iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").load(function () {
                parent.loding.hideLoding();
            });
        }

    }
}

//创建新标签
common.createIframe = function (href, titleName) {
    var url = href.split('?')[0];
    var topWindow = $(window.parent.document);
    var show_nav = topWindow.find('#min_title_list');
    var iframe_box = topWindow.find('#iframe_box');
    if (!window.parent.MultiTabs) {
        //下面代码是为了防止打开多个页签
        var navLi = show_nav.children();
        if (navLi.length > 1) {
            console.info(navLi);
            navLi.each(function (i, item) {
                if (!$(item).hasClass("home")) {
                    $(item).remove();
                }
            })
        }
    }

    var id_name = $(".show_iframe").each(function (i) { $(this).attr('id', "Sort_link_" + i); $(this).addClass("selected") });
    show_nav.find('li').removeClass("active");
    var iframe_box = topWindow.find('#iframe_box');
    show_nav.append('<li class="active name"><span data-href="' + url + '">' + titleName + '</span><em class="close_icon"></em></li>');
    common.tabNavallwidth();
    var iframeBox = iframe_box.find('.show_iframe');
    if (iframeBox.length === 0) {
        window.location.href = href;
        return;
    }
    iframeBox.hide().addClass("selected");
    iframe_box.append('<div class="show_iframe" date-id="' + id_name + '"><div class="loading"></div><iframe class="simei_iframe" frameborder="0" src=' + href + ' data-href=' + url + '></iframe></div>');
    iframe_box.find(".simei_iframe[src='" + href + "']").parent().addClass("selected").siblings().removeClass("selected");
    var showBox = iframe_box.find('.show_iframe:visible');
    showBox.find('iframe').attr("src", href).load(function () {
        parent.loding.hideLoding();
    });
}
common.min_titleList = function () {
    var topWindow = $(window.parent.document);
    var show_nav = topWindow.find("#min_title_list,#dropdown_menu");
    var aLi = show_nav.find("li");
}
//检测编号是否存在中文和特殊字符
common.CheckCode = function (str) {
    var res = /^[a-z0-9_A-Z]{1,18}$/;
    if (res.test(str)) {
        return true;
    } else {
        return false;
    }
}

//写cookies 
common.setCookie = function (name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

//读取cookies 
common.getCookie = function (name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

    if (arr = document.cookie.match(reg))

        return unescape(arr[2]);
    else
        return null;
}

//删除cookies 
common.delCookie = function (name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

//取差集
common.getArrDifference = function (arr1, arr2) {
    return arr1.concat(arr2).filter(function (v, i, arr) {
        return arr.indexOf(v) === arr.lastIndexOf(v);
    });
}

//前面补0，num为要补的数字，n为需要的字符串长度
common.prefixInteger = function (num, n) {
    return (Array(n).join(0) + num).slice(-n);
}

//去重方法
common.uniq = function (array) {
    var temp = []; //一个新的临时数组
    for (var i = 0; i < array.length; i++) {
        if (temp.indexOf(array[i]) == -1) {
            temp.push(array[i]);
        }
    }
    return temp;
}


//easyui 日期格式化
function myformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}
function myparser(s) {
    if (!s) return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var d = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    } else {
        return new Date();
    }
}
////ZTREE树查询方法---------------------------------------------开始
//获取节点result:返回结果集，node：子节点，nodes：全部节点
common.getParentNodes = function (result, node, nodes) {
    if (!includes(result, node)) {
        result.push(node);
    }
    if (node.pId !== "-1") {
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].id === node.pId) {
                common.getParentNodes(result, nodes[i], nodes);
            }
        }
    }
}
//判断是否存在相同数组
function includes(arr, obj) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].id === obj.id) {
            return true;
        }
    }
    return false;
}
//排序函数
function sortNumber(a, b) {
    return b.sortno - a.sortno;
}
////ZTREE树查询方法---------------------------------------------结束
document.onkeydown = function (e) {
    var event = e || window.event;
    var type;
    var stat;
    var keyCode = event.keyCode || event.which;
    if (event.srcElement) {
        type = event.srcElement.type;
        stat = event.srcElement.readOnly;
    } else if (event.target) {
        type = event.target.type;
        stat = event.target.readOnly;
    }
    if (keyCode == 8) {
        if ((type != "text" && stat == true) || (type != "textarea" && stat == true) || (type != "select" && stat == true)) {
            if (event.preventDefault) {
                //preventDefault()方法阻止元素发生默认的行为
                event.preventDefault();
            }
            if (event.returnValue) {
                //IE浏览器下用window.event.returnValue = false;实现阻止元素发生默认的行为
                event.returnValue = false;
            }
        }
    }
    if (keyCode === 122) {
        if (event.preventDefault) {
            //preventDefault()方法阻止元素发生默认的行为
            event.preventDefault();
        }
        if (event.returnValue) {
            //IE浏览器下用window.event.returnValue = false;实现阻止元素发生默认的行为
            event.returnValue = false;
        }
    }
}

//给日历控件添加了清空按钮
if ($.fn.datebox) {
    var buttons = $.extend([], $.fn.datebox.defaults.buttons);
    buttons.splice(1, 0,
    {
        text: '清空',//按钮文本
        handler: function (target) {
            $("#" + target.id + "").datebox('setValue', "");//根据ID清空
            $("#" + target.id + "").datebox('hidePanel', "");
            try {
                eval("buttons_Clear('" + target.id + "')");
            } catch (e) { }

        }
    });
    $.fn.datebox.defaults.buttons = buttons;
    $.fn.datebox.defaults.editable = false;
}
/////////easy ui 弹窗移动范围限制

var easyuiPanelOnMove = function (left, top) {
    var l = left;
    var t = top;
    if (l < 1) {
        l = 1;
    }
    if (t < 1) {
        t = 1;
    }
    var width = parseInt($(this).parent().css('width')) + 14;
    var height = parseInt($(this).parent().css('height')) + 14;
    var right = l + width;
    var buttom = t + height;
    var browserWidth = $(window).width();
    var browserHeight = $(window).height();
    if (right > browserWidth) {
        l = browserWidth - width;
    }
    if (buttom > browserHeight) {
        t = browserHeight - height;
    }
    $(this).parent().css({/* 修正面板位置 */
        left: l,
        top: t
    });
};
if ($.fn.dialog) {
    $.fn.dialog.defaults.onMove = easyuiPanelOnMove;
}
if ($.fn.window) {
    $.fn.window.defaults.onMove = easyuiPanelOnMove;
}
if ($.fn.panel) {
    $.fn.panel.defaults.onMove = easyuiPanelOnMove;
}


//参数 标签名字
function GetMenuId(title) {
    var dataName = "";
    common.AjaxPostAsync("/ModuleManager/GetModuleTreeList", { PremissionFilter: true, Sort: "SortNo", OrderBy: 1 },
    function (result) {
        var resultData = JSON.parse(result);
        if (resultData.status) {
            if (resultData.data != null) {
                for (var i = 0; i < resultData.data.length; i++) {
                    if (resultData.data[i].name == title) {
                        dataName = resultData.data[i].id;
                        break;
                    }
                }
            }
        }
    }, false);

    return dataName;
}


