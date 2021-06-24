
//********************************************************************************
//** 作者： 彭志浩
//** 创建时间：2019-01-16
//** 修改人：彭志浩
//** 修改时间：2019-01-16
//** 描述：动态加载模块下列表的工具栏
//*********************************************************************************/

function LoadButtonData() {
    var resultArr = [];
    var moduleId = "";
    if (window.parent.document.getElementById("dataNameUrlParam") == null) {
        moduleId = common.GetQueryString("dataNameUrlParam");
    }
    else {
        moduleId = window.parent.document.getElementById("dataNameUrlParam").value;
    }
    var query = {
        moduleId: moduleId,
    };
    var url = "/System/ButtonManager/GetButtonList";
    common.AjaxPostAsync(url, query, function (result) {
        resultBttonData = JSON.parse(result);
        var dataJsevent = "";
        if (resultBttonData.data == null)
            return;
        for (var i = 0; i < resultBttonData.data.length; i++) {
            resultArr.push({ text: resultBttonData.data[i].FULLNAME, iconCls: resultBttonData.data[i].ICON, handler: eval(resultBttonData.data[i].JSEVENT) });
            if (i < resultBttonData.data.length - 1) {
                resultArr.push('-');
            }
        }
    }, false);
    return resultArr;
}

//lix  考勤管理里面传递的有参数
var ButtonIsLoaded = false;
function LoadButtonDataByModuleID(moduleID) {
    var resultArr = [];
    var moduleId = null;
    var urlM = null;
    try {
        if (moduleID !== null && moduleID !== undefined && moduleID !== '') {
            moduleId = moduleID;
        }
        else {
            var element = window.parent.document.getElementById("dataNameUrlParam");
            moduleId = element.value;
        }
        ButtonIsLoaded = true;
    } catch (e) {
        ButtonIsLoaded = false;
    }
    var query = {
        moduleId: moduleId,
    };
    var url = "/System/ButtonManager/GetButtonList";
    common.AjaxPostAsync(url, query, function (result) {
        resultBttonData = JSON.parse(result);
        var dataJsevent = "";
        if (resultBttonData.data == null)
            return;
        for (var i = 0; i < resultBttonData.data.length; i++) {
            resultArr.push({ text: resultBttonData.data[i].FULLNAME, iconCls: resultBttonData.data[i].ICON, handler: eval(resultBttonData.data[i].JSEVENT) });
            if (i < resultBttonData.data.length - 1) {
                resultArr.push('-');
            }
        }
    }, false);
    return resultArr;
}
