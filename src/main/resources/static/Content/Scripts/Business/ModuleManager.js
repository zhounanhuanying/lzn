//********************************************************************************
//** 作者： 谢磊
//** 创建时间：2018-11-15
//** 修改人：谢磊
//** 修改时间：2018-11-15
//** 描述：系统模块前端js操作
//*********************************************************************************/
var moduleManager = {};

//刷新
function reloadClick() {
    moduleManager.queryData();

}
//增加
function addClick() {
    $("#ModuleForm").form('clear');
    $(".btnSaveModule").attr("data-type", 1);
    $("#ModuleAdd").dialog('open');
    $("#ModuleAdd").prev("div").children(":first").text("添加模块信息");
    $('#PARENTID').combotree('setValue', moduleManager.moduleId);
    $("#ModuleForm").find("input[name='ENABLED']").eq(0).prop("checked", true);
}

//删除
function deleteClick() {
    moduleManager.deleteData();
}

//编辑、修改
function updateClick() {
    var rows = $('#dgModuleList').datagrid('getChecked');
    if (rows.length != 1) {
        $.messager.alert('提示', "请选择要编辑的数据，并且只能选中一行");
        return false;
    }
    $(".btnSaveModule").attr("data-type", 0);
    $("#ModuleAdd").dialog('open');
    $("#ModuleAdd").prev("div").children(":first").text("编辑模块信息");
    $('#ModuleForm').form('load', rows[0]);
    if (rows[0].PARENTID == null) {
        $('#PARENTID').combotree('setValue', "xtmk");
    }
}


//工具栏按钮加载
var toolbar = LoadButtonData();

moduleManager = (function () {
    var manager = {};
    manager.moduleId = "";
    manager.moduleName = "";
    manager.moduleList = [];
    manager.moduleTree = {};
    var checkdSelect = [{
        CODEKEY: "",
        CODEVALUE: "全部"
    }]
    //列模型
    var column = [[
            { field: '', title: '全选', width: "5%", checkbox: true, },
      { field: 'CODE', title: '模块代码', width: "5%" },
       { field: 'FULLNAME', title: '模块名称', width: " 10%" },
       //{ field: 'PARENTID', title: '上级模块', width: "10%" },
       { field: 'MODULETYPENAME', title: '模块类型', width: "10%" },
       { field: 'ICON', title: 'Icon图标', width: "12%" },
       { field: 'TARGET', title: '连接目标', width: "10%" },
       { field: 'LEVELNO', title: '分别层次', width: "10%" },
       { field: 'LOCATION', title: '访问地址', width: "20%" },

     {
         field: 'DESCRIPTION', title: '模块描述', align: 'left', width: "15%"
         //, formatter: function (value) {
         //    //return "<span title='" + value + "'>" + value + "</span>";
         //}
     }, { field: 'SORTNO', title: '显示顺序', width: "5%" }
     , {
         field: 'ENABLED', title: '是否有效', width: "8%", formatter: function (value, rowData) {
             if (value == "1") {
                 return "有效";
             } else {
                 return "无效";
             }
         }
     }
     //, {
     //    field: 'ModuleID', title: '操作', align: 'left', width: "10%", formatter: function (value, rowData) {
     //        return "<a  href='#'>编辑</a>";
     //    }
     //}
    ]];
    //工具条
    //var toolbar = [{
    //    text: '刷新',
    //    iconCls: 'icon-reload',
    //    handler: function () {
    //        manager.queryData();
    //    }
    //}, {
    //    text: '添加',
    //    iconCls: 'icon-add',
    //    handler: function () {
    //        $("#ModuleForm").form('clear');
    //        $(".btnSaveModule").attr("data-type", 1);
    //        $("#ModuleAdd").dialog('open');
    //        $("#ModuleAdd").prev("div").children(":first").text("添加模块信息");
    //        $('#PARENTID').combotree('setValue', manager.moduleId);
    //        $("#ModuleForm").find("input[name='ENABLED']").eq(0).prop("checked", true);
    //    }
    //}, {
    //    text: '编辑',
    //    iconCls: 'icon-edit',
    //    handler: function () {
    //        var rows = $('#dgModuleList').datagrid('getChecked');
    //        if (rows.length != 1) {
    //            $.messager.alert('提示', "请选择要编辑的数据，并且只能选中一行");
    //            return false;
    //        }
    //        $(".btnSaveModule").attr("data-type", 0);
    //        $("#ModuleAdd").dialog('open');
    //        $("#ModuleAdd").prev("div").children(":first").text("编辑模块信息");
    //        $('#ModuleForm').form('load', rows[0]);
    //        if (rows[0].PARENTID == null) {
    //            $('#PARENTID').combotree('setValue', "xtmk");
    //        }
    //    }
    //}, {
    //    text: '删除',
    //    iconCls: 'icon-cancel',
    //    handler: function () {
    //        manager.deleteData();
    //    }
    //}
    //];
    var setting = {
        data: {//表示tree的数据格式
            simpleData: {
                enable: true,//表示使用简单数据模式
                idKey: "id",//设置之后id为在简单数据模式中的父子节点关联的桥梁
                pidKey: "pid",//设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
                rootPId: "-1"
            },
        },
        view: {
            dblClickExpand: true,
            showIcon: true,//设置是否显示节点图标
            showLine: true,//设置是否显示节点与节点之间的连线
            showTitle: true,  //设置是否显示节点的title提示信息
        },
        callback: {
            onClick: function (event, treeId, treeNode, clickFlag) {
                event.preventDefault();
                manager.moduleId = treeNode.id;
                manager.moduleName = treeNode.name;
                manager.queryData();
            }
        }
    }
    manager.datagrid = new EasyUIExtend();
    manager.initDataGrid = function () {
        manager.datagrid.sort = "SORTNO";
        //初始化表格数据
        manager.datagrid.InitDataGrid("dgModuleList", "/ModuleManager/GetModuleList", "post", column, toolbar, { ShowDisable: true });
    }
    //查询数据
    manager.queryData = function () {
        var data = {};
        data.ShowDisable = true;
        data.ModuleName = $("#txtModuleName").val();
        data.ModuleCode = $("#txtModuleCode").val();
        // var treeObj = $.fn.zTree.getZTreeObj("moduleTree");
        var nodes = manager.moduleTree.getSelectedNodes();
        if (nodes != null && nodes.length > 0) {
            data.ParentId = nodes[0].id;
        }
        //如果是根节点，则显示所有模块
        if (data.ParentId == "xtmk") {
            data.ParentId = "-1";
        }
        data.ModuleType = $("#selModuleType").combobox("getValue");
        data.Enable = $("#selEnable").combobox("getValue");
        manager.datagrid.pageIndex = 1;
        manager.datagrid.QueryList(data);
    }
    //保存数据
    manager.saveData = function () {
        var saveType = $(".btnSaveModule").attr("data-type");
        var saveUrl = "/ModuleManager/UpdateModule";
        if (saveType == "1") {
            saveUrl = "/ModuleManager/AddModule";
        }


        if ($("#ModuleForm").form('validate')) {
            var formData = $('#ModuleForm').serialize();
            $.ajax({
                type: "post",
                url: saveUrl,
                data: formData,
                success: function (resultJson) {
                    var result = JSON.parse(resultJson);
                    $.messager.alert('提示', result.msg);
                    if (result.status) {
                        $("#ModuleAdd").dialog('close');
                        manager.loadModuleTree();
                        manager.queryData();
                    }
                }
            })
        } else {
            $.messager.alert('提示', "录入信息有误！");
        }
    }

    //删除数据
    manager.deleteData = function () {
        var rows = $('#dgModuleList').datagrid('getChecked');
        if (rows.length != 1) {
            $.messager.alert('提示', "请选择要删除的数据，并且只能选中一行");
            return false;
        }
        //判断当前模块是否有子节点
        var hasChild = false;
        manager.moduleList.forEach(function (module) {
            if (module.pId == rows[0].MODULEID) {
                hasChild = true;
            }
        })
        if (hasChild) {
            $.messager.alert('提示', "当前选中的数据有子级节点，无法删除！");
            return false;
        }

        $.messager.confirm("操作提示", "您确定要删除选中的记录吗？", function (r) {
            if (r) {
                //var idArr = new Array();
                //for (var i = 0; i < rows.length; i++) {
                //    idArr.push(rows[i].MODULEID);
                //}
                //var ModuleIds = idArr.join("^");
                $.ajax({
                    type: "post",
                    url: "/ModuleManager/DeleteModules",
                    data: { ids: rows[0].MODULEID },
                    success: function (resultJson) {
                        var result = JSON.parse(resultJson);
                        $.messager.alert('提示', result.msg);
                        if (result.status) {
                            $("#ModuleAdd").dialog('close');
                            manager.loadModuleTree();
                            manager.queryData();
                        }
                    }
                })
            }
        });
    }

    ///加载模块树
    manager.loadModuleTree = function (data) {
        var showBase = { ShowBaseNode: true, ShowDisable: true, Sort: "SortNo", OrderBy: 1 }; //显示根节点，显示禁用的模块
        var mergeData = common.MergeJsonObject(showBase, data);
        common.AjaxPost("/ModuleManager/GetModuleTreeList", mergeData,
             function (result) {
                 var resultData = JSON.parse(result);
                 if (resultData.status) {
                     var treeObj = $.fn.zTree.init($("#moduleTree"), setting, resultData.data);
                     treeObj.expandAll(true);
                     manager.moduleTree = treeObj;
                     manager.moduleList = resultData.data;
                     var moduleTreeData = common.Array2Tree(resultData.data);
                     $('#PARENTID').combotree('loadData', moduleTreeData);
                 } else {
                     $.messager.alert("提示", resultData.msg);
                 }
             });
    }
    //加载区域类型下拉框
    manager.loadModuleType = function () {

        common.AjaxPost("/SysCodeManager/GetCodeList",
          { TYPEKEY: "SYS-MODULE-TYPE" }, function (result) {
              var resultData = JSON.parse(result);
              if (resultData.status) {
                  $('#MODULTYPE').combobox({
                      data: resultData.data,
                      valueField: "CODEKEY",
                      textField: "CODEVALUE"
                  });
                  var newData = common.MergeArray(checkdSelect, resultData.data);//添加默认选项
                  $('#selModuleType').combobox({
                      data: newData,
                      valueField: "CODEKEY",
                      textField: "CODEVALUE"
                  });
                  $("#selModuleType").combobox('setValue', checkdSelect.CODEKEY);//选中默认选项
              } else {
                  $.messager.alert("提示", resultData.msg);
              }
          });
    }
    //清空筛选条件
    manager.clearFilter = function () {
        $("#selModuleType").combobox('setValue', checkdSelect.CODEKEY);//选中默认选项
        $("#selEnable").combobox("setValue", "")
        $("#txtModuleName").val("");
        manager.moduleTree.cancelSelectedNode();
    }
    return manager;
}())



$(function () {
    //屏幕的宽高
    winHeight = $(window).height();
    winWidth = $(window).width();

    if (winWidth <= 1480) {
        var easyui_listh = (winHeight - 125);
    } else {
        var easyui_listh = (winHeight - 85);
    }
    $('.easyui_list').css({ height: easyui_listh })
    var ztree_lefth = (winHeight - 10);
    $('.ztree_left').css({ height: ztree_lefth })
    var list_ztreeh = (ztree_lefth - 77);
    $('.list_ztree').css({ height: list_ztreeh })
    $(".list_ztree").niceScroll({
        cursorcolor: "#efefef",
        cursoropacitymax: 1,
        touchbehavior: false,
        cursorwidth: "3px",
        cursorborder: "0",
        cursorborderradius: "5px"

    })
    $(".module_select").niceScroll({
        cursorcolor: "#efefef",
        cursoropacitymax: 1,
        touchbehavior: false,
        cursorwidth: "3px",
        cursorborder: "0",
        cursorborderradius: "5px"
    })



    //点击查询，查询数据
    $(".btnQuery").click(function () {
        moduleManager.queryData();
    })
    //点击清空
    $(".btnClear").click(function () {
        moduleManager.clearFilter();
    })
    $("#btnSearch").click(function () {
        var data = { "ModuleName": $("#txtSearch").val() };
        moduleManager.loadModuleTree(data);
    })

    //保存数据
    $(".btnSaveModule").click(function () {
        moduleManager.saveData();
    })
    $(".btnCloseForm").click(function () {
        $("#ModuleAdd").dialog('close');
    })
    //初始化模块树
    moduleManager.loadModuleTree();
    //加载模块类型
    moduleManager.loadModuleType();
    //初始化表格
    moduleManager.initDataGrid();



});
















