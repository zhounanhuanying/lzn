//********************************************************************************
//** 作者： 陈剑辉
//** 创建时间：2019-01-08
//** 描述：组织机构前端js操作
//*********************************************************************************/
var orgManage = {
    setting: null,
    pid: null,
    admin: null,
    myDiagram: null,
    addOrUpdate: false,
    layer: null,
    treeTable: null,
    table: null,
    table2: null,
    table3: null,
    chooseDialog: null,
    form: null,
    orgId: '',
    orgName:'',
    orgList: null,
    delIds: "",
    regionId: "",
    parkId:"",
    parentNode: null,
    /*当前选中的节点*/
    currentCheckNode: null,
    /**是否首次加载*/
    initFirst: true,
    /**弹窗初次加载*/
    initPopupFirst: true,
    /**记录存储选择人员的id*/
    associationInfoList: [],
    /**标记右键节点类型，用于判断父节点类型 默认基本组织*/
    clickNodeType:null,
    isFirstNode:false,
    delBtnSwitch:true,
};
var parentid = "";
var OrganTypeTree;//组织机构类型
var OrganazationManager = {};
var access_token = window.localStorage.getItem("access_token"); //获取指定key本地存储的值
//刷新
function reloadClick() {
    OrganazationManager.Query();
}
//增加
function addClick() {
    OrganazationManager.currentEditOrg = null;

    $("#OrganizationInfo").dialog({ "title": "添加组织" });
    OrganazationManager.Open();
    OrganazationManager.LoadComboxOrganTree();
    $('#CATEGORY').combotree('loadData', OrganTypeTree);
    if (parentid.length !== 0 && parentid !== "00000000-0000-0000-0000-000000000000") {
        $("#PARENTID").combotree("setValue", parentid);
    }
}

//删除
function deleteClick() {
    OrganazationManager.DeleteOrganazation();
}

//导入院系信息 王院峰 2020-02-26
function CollegeImportClick() {
    $("#baseCollegeImport").dialog('open');
}

//导入班级信息 王院峰 2020-02-26
function BanjiImportClick() {
    $("#BanjiImport").dialog('open');
}



//编辑、修改
function updateClick() {
    var rows = $('#dgOrganizationList').datagrid('getChecked');
    if (rows.length !== 1) {
        $.messager.alert('提示', "请选择要编辑的数据，并且只能选中一行");
        return false;
    }
    OrganazationManager.LoadParkList();
    $("#OrganizationInfo").dialog({ "title": "编辑组织" });
    OrganazationManager.Open();
    OrganazationManager.LoadComboxOrganTree();
    $('#CATEGORY').combotree('loadData', OrganTypeTree);
    OrganazationManager.GetOrganizationById(rows[0].ORGANIZATIONID);
}

//修改类型
function updateTypeClick() {
    OrganazationManager.EditOrganType();
}

//工具栏按钮加载
// var toolbar = LoadButtonData();

OrganazationManager = (function () {
    var organazation = {};
    organazation.comboxOrganTree = {};
    organazation.currentEditOrg = null;
    organazation.orgTypeRelation = [];
    var datagrid = new EasyUIExtend();
    var Nodes = [];
    var Organazationtypes = [];
    var checkdSelect = [{
        CODEKEY: "-1",
        CODEVALUE: "全部"
    }];
    var column = [[
        { field: 'ORGANIZATIONID', checkbox: true },
        { field: 'FULLNAME', title: '组织机构全称', width: " 17%" },
        { field: 'SHORTNAME', title: '简称', width: " 10%" },
        {
            field: 'CATEGORY', title: '类型', width: "10%",
            formatter: function (value, row, index) {
                return GetAreaTypeName(value);
            }
        },
        {
            field: 'CREATEDATE', title: '创建时间', width: "18%",
            formatter: function (value, row, index) {
                return common.DateFormater(value, "yyyy-MM-dd hh:mm:ss");
            }
        },
        { field: 'DESCRIPTION', title: '描述', width: " 35%" },
        { field: 'SORTNO', title: '排序', width: "5%", halign: 'center', align: 'center' }
    ]];

    //var toolbar = [{
    //    text: '刷新',
    //    iconCls: 'icon-reload',
    //    handler: function () {
    //        Query();
    //    }
    //}, {
    //    text: '添加',
    //    iconCls: 'icon-add',
    //    handler: function () {
    //        $("#OrganizationInfo").dialog({ "title": "添加组织" });
    //        Open();
    //        LoadComboxOrganTree();
    //        $('#CATEGORY').combotree('loadData', OrganTypeTree);
    //        if (parentid.length !== 0 && parentid !== "00000000-0000-0000-0000-000000000000") {
    //            $("#PARENTID").combotree("setValue", parentid);
    //        }
    //    }
    //}, {
    //    text: '编辑',
    //    iconCls: 'icon-edit',
    //    handler: function () {
    //        var rows = $('#dgOrganizationList').datagrid('getChecked');
    //        if (rows.length !== 1) {
    //            $.messager.alert('提示', "请选择要编辑的数据，并且只能选中一行");
    //            return false;
    //        }
    //        $("#OrganizationInfo").dialog({ "title": "编辑组织" });
    //        Open();
    //        LoadComboxOrganTree();
    //        $('#CATEGORY').combotree('loadData', OrganTypeTree);

    //        GetOrganizationById(rows[0].ORGANIZATIONID);
    //    }
    //}, {
    //    text: '删除',
    //    iconCls: 'icon-cancel',
    //    handler: DeleteOrganazation
    //}, {
    //    text: '修改类型',
    //    iconCls: 'icon-edit',
    //    handler: EditOrganType
    //}];
    /*organazation.EditOrganType = function () {
        var rows = $('#dgOrganizationList').datagrid('getChecked');
        if (rows.length == 0 || rows.length > 1) {
            $.messager.alert('提示', "请选择要修改类型的数据,并且只能选择一行修改!");
            return false;
        }
        $("#EditOrganTypeInfo").dialog("open");
        //var typeName = "";
        ////1：院系，2：班级，3：部门，4：校区，5：学校
        //switch (rows[0].CATEGORY) {
        //    case 1:
        //        typeName = "院系";
        //        break;
        //    case 2:
        //        typeName = "班级";
        //        break;
        //    case 3:
        //        typeName = "部门";
        //        break;
        //    case 4:
        //        typeName = "校区";
        //        break;
        //    case 5:
        //        typeName = "学校";
        //        break;
        //}
        //$('#OrganType').combotree('setValue', typeName);
        $('#OrganType').combotree('loadData', OrganTypeTree);
    }
    function SaveType() {
        var rows = $('#dgOrganizationList').datagrid('getChecked');
        if (rows.length === 0) {
            $.messager.alert('提示', "请选择要修改类型的数据!");
            return false;
        }
        var category = $("#OrganType").combobox("getValue");
        if (category.length === 0) {
            $.messager.alert('提示', "请选择组织机构类型！");
            return false;
        }
        $.messager.confirm("提示", "确定修改组织机构类型？", function (isDelete) {
            if (isDelete === false) {
                return;
            }
            var id = [];
            for (var i = 0; i < rows.length; i++) {
                id.push(rows[i].ORGANIZATIONID);
            }

            // BEGIN: 2019-07-01 Add by hjs
            // 显示加载遮罩层
            if (parent.loding) {
                parent.loding.showLoding();
            }
            // END: 2019-07-01 Add by hjs

            common.AjaxPost("/Basicdata/OrganazationManager/UpdateOrganType",
                 {
                     OrganIDs: id.toString(),
                     Category: category
                 },
                  function (result) {
                      var resultData = JSON.parse(result);stringify
                      if (resultData.status === true) {
                          datagrid.QueryList({ PARENTID: parentid });//刷新数据表格
                          //    LoadOrganazationTree();//重新加载树
                          $("#EditOrganTypeInfo").dialog("close");
                          $("#OrganType").combotree("setValue", "");
                          $.messager.alert('提示', resultData.msg);
                      } else {
                          $.messager.alert('错误', resultData.msg, "error");
                      }

                      // BEGIN: 2019-07-01 Add by hjs
                      // 隐藏加载遮罩层
                      if (parent.loding) {
                          parent.loding.hideLoding();
                      }
                      // END: 2019-07-01 Add by hjs
                  });
        });

    }
*/
    //加载组织机构列表
    var LoadDataGrid = function () {
        datagrid.InitDataGrid("dgOrganizationList",
            "/Basicdata/OrganazationManager/OrganizationPageList",
            "post", column, toolbar);
    };
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
            onClick: zTreeOnClick
        }
    }

    function zTreeOnClick(event, treeId, treeNode) {
        parentid = treeNode.id;
        alert(parentid);
        organazation.Query();
    }

    // 根据 key 获取 value
    function getValueByKey(dic, key) {
        if (!key) {
            return "";
        }

        if (String(key) in dic) {
            return dic[String(key)];
        } else {
            return "";
        }
    }

    //// 组织机构类型
    //// 这里可以改成从后台取数，然后将返回内容转换成以下结构对象或者数组即可
    //organazation.categoryType = {
    //    1: "院系",
    //    2: "班级",
    //    3: "部门",
    //    4: "校区",
    //    5: "学校"
    //};

    // 通过 key 从组织机构类型中获取类型值 value
    organazation.getOrgTypeValueByKey = function (dic, key) {
        return getValueByKey(dic, key);
    };

    //导入组织机构信息-选择Excel文件并检测 王院峰 2020-02-26
    organazation.CheckCompressFile = function (file) {
        var fileSize = 0;
        if (isIE() && !file.files) {
            var filePath = file.value;
            var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
            var file = fileSystem.GetFile(filePath);
            fileSize = file.Size;
        } else {
            fileSize = file.files[0].size;
        }
        var size = fileSize / 1024;
        if (size > (1024 * 200)) {
            $.messager.alert('提示', "上传文件不能大于200M!");
            file.value = "";
            return false;
        }
        return true;
    }

    /////导入信息   2020-02-26   王院峰/////////////
/*

    //院系导入 提交按钮 //
    var ImportCollege = function () {
        $("#baseCollegeImportSubmit").click(function () {

            var fileUrl = $("#picCompressFileUrl").val();
            if (fileUrl == "") {
                $.messager.alert('提示', "请先选择你要上传的Excel文件!");
                return;
            }
            else {
                var pos = fileUrl.lastIndexOf(".");//查找最后一个\的位置
                var fileExtendName = fileUrl.substr(pos + 1).toLowerCase(); //截取最后一个\位置到字符长度，也就是截取文件名
                if (fileExtendName == "" || (fileExtendName != "xls" && fileExtendName != "xlsx")) {
                    $.messager.alert('提示', "请先选择rar或者zip压缩文件进行上传!");
                    return;
                }
            }
            var form = document.getElementById("baseCollegeImportForm");
            // 用表单来初始化
            var formData = new FormData(form);
            $.ajax({
                url: '/Basicdata/OrganazationManager/ImportExcelCollege',
                type: 'POST',
                async: false,
                cache: false, //上传文件不需要缓存
                data: formData,
                processData: false, // 告诉jQuery不要去处理发送的数据
                contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                beforeSend: function () {
                    parent.loding.showLoding();
                },
                success: function (result) {
                    var resultData = JSON.parse(result);
                    if (resultData.status === true) {
                        $.messager.alert('提示', resultData.msg);
                        $("#baseCollegeImportForm").form('clear');
                        $("#baseCollegeImport").dialog("close");
                        //下载
                        //DownLoadResutl('/Basicdata/PersonManager/DownLoadPersonImportDataBaseResult');
                        parent.loding.hideLoding()
                        person.Query();
                    } else {
                        parent.loding.hideLoding()
                        $.messager.alert('错误', resultData.msg);
                    }
                    $("#baseCollegeImportSubmit").prop("disabled", "");
                },
                error: function (err) {
                    parent.loding.hideLoding()
                    $.messager.alert('错误', "系统错误请重试!");
                }
            });
            // }
        });
    }
    //班级导入 提交按钮 //
    var ImportBanji = function () {
        $("#baseBanjiImportSubmit").click(function () {
            var fileUrl = $("#picCompressFileUrl1").val();
            if (fileUrl == "") {
                $.messager.alert('提示', "请先选择你要上传的Excel文件!");
                return;
            }
            else {
                var pos = fileUrl.lastIndexOf(".");//查找最后一个\的位置
                var fileExtendName = fileUrl.substr(pos + 1).toLowerCase(); //截取最后一个\位置到字符长度，也就是截取文件名
                if (fileExtendName == "" || (fileExtendName != "xls" && fileExtendName != "xlsx")) {
                    $.messager.alert('提示', "请先选择xls或者xlsx压缩文件进行上传!");
                    return;
                }
            }
            var form = document.getElementById("baseBanjiImportForm");
            // 用表单来初始化
            var formData = new FormData(form);
            $.ajax({
                url: '/Basicdata/OrganazationManager/ImportExcelBanji',
                type: 'POST',
                async: false,
                cache: false, //上传文件不需要缓存
                data: formData,
                processData: false, // 告诉jQuery不要去处理发送的数据
                contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                beforeSend: function () {
                    parent.loding.showLoding();
                },
                success: function (result) {
                    var resultData = JSON.parse(result);
                    if (resultData.status === true) {
                        $.messager.alert('提示', resultData.msg);
                        $("#baseBanjiImportForm").form('clear');
                        $("#BanjiImport").dialog("close");
                        //下载
                        //DownLoadResutl('/Basicdata/PersonManager/DownLoadPersonImportDataBaseResult');
                        parent.loding.hideLoding()
                        person.Query();
                    } else {
                        parent.loding.hideLoding()
                        $.messager.alert('错误', resultData.msg);
                    }
                    $("#baseBanjiImportSubmit").prop("disabled", "");
                },
                error: function (err) {
                    parent.loding.hideLoding()
                    $.messager.alert('错误', "系统错误请重试!");
                }
            });
            // }
        });
    }
*/

    /*//添加组织机构
    var OrganazationAdd = function () {
        if (!InputCheck()) {//非空判断
            return;
        }
        // $("#PARENTID").val(parentid);

        // BEGIN: 2019-07-01 Add by hjs
        // 显示加载遮罩层
        if (parent.loding) {
            parent.loding.showLoding();
        }
        // END: 2019-07-01 Add by hjs

        common.AjaxPost("/Basicdata/OrganazationManager/OrganizationAdd",
            $("#OrganizationForm").serialize(),
            function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status === true) {
                    Close();//关闭窗口
                    datagrid.QueryList({ PARENTID: parentid });//刷新数据表格
                    LoadOrganazationTree();//重新加载树
                } else {
                    $.messager.alert('错误', resultData.msg);
                }

                // BEGIN: 2019-07-01 Add by hjs
                // 隐藏加载遮罩层
                if (parent.loding) {
                    parent.loding.hideLoding();
                }
                // END: 2019-07-01 Add by hjs
            });
    }
    //编辑组织机构
    var OrganazationEdit = function () {
        if (!InputCheck()) {//非空判断
            return;
        }

        // BEGIN: 2019-07-01 Add by hjs
        // 显示加载遮罩层
        if (parent.loding) {
            parent.loding.showLoding();
        }
        // END: 2019-07-01 Add by hjs
        
        common.AjaxPost("/Basicdata/OrganazationManager/OrganizationUpdate",
                $("#OrganizationForm").serialize(),
                function (result) {
                    var resultData = JSON.parse(result);
                    if (resultData.status === true) {
                        Close();
                        datagrid.QueryList({ PARENTID: parentid });//刷新数据表格
                        LoadOrganazationTree();//重新加载树
                    } else {
                        $.messager.alert('错误', resultData.msg);
                    }

                    // BEGIN: 2019-07-01 Add by hjs
                    // 隐藏加载遮罩层
                    if (parent.loding) {
                        parent.loding.hideLoding();
                    }
                    // END: 2019-07-01 Add by hjs
                });
    }
    ///根据组织机构ID获取组织机构信息
    organazation.GetOrganizationById = function (OrganizationID) {
        common.AjaxPost("/Basicdata/OrganazationManager/GetOrganizationById",
            { OrganizationID: OrganizationID },
            function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status === true)9o {

                    organazation.currentEditOrg = JSON.parse(JSON.stringify(resultData.data));

                    // 根据组织类型 key 绑定 value
                    //resultData.data.CATEGORY = organazation.getOrgTypeValueByKey(organazation.categoryType, resultData.data.CATEGORY);
                    try {
                        $('#OrganizationForm').form('load', resultData.data);//给form表单的控件赋值
                    } catch (e) {
                        //console.error(e.message);
                    }
                    //一级部门需要显示园区，否则显示上级部门
                    if (OrganazationManager.currentEditOrg.CATEGORY == "5") {
                        $(".tdPark").show();
                        $(".tdLastOrgan").hide();
                    } else {
                        $(".tdPark").hide();
                        $(".tdLastOrgan").show();
                    }
                    //$("#PARKID").combotree('setValue', OrganazationManager.currentEditOrg.PARKID);
                    //$("#CATEGORY").combotree('setValue', OrganazationManager.currentEditOrg.CATEGORY);
                    //$("#PARENTID").combotree('setValue', OrganazationManager.currentEditOrg.PARENTID);
                } else {
                    $.messager.alert('错误', resultData.msg);
                }
            });
    }
    //删除组织机构
    organazation.DeleteOrganazation = function () {
        var rows = $('#dgOrganizationList').datagrid('getChecked');
        if (rows.length <0) {
            $.messager.alert('提示', "请选择要删除的数据");
            return false;
        }
        $.messager.confirm("提示", "确定删除？", function (isDelete) {
            if (isDelete === true) {
                var ids = [];
                for (var i = 0; i < rows.length; i++) {
                    ids.push(rows[i].ORGANIZATIONID)
                }

                // BEGIN: 2019-07-01 Add by hjs
                // 显示加载遮罩层
                if (parent.loding) {
                    parent.loding.showLoding();
                }
                // END: 2019-07-01 Add by hjs

                common.AjaxPost("/Basicdata/OrganazationManager/DeleteOrganization",
                  { OrganizationIDs: ids.toString() },
                   function (result) {
                       var resultData = JSON.parse(result);
                       if (resultData.status === true) {
                           for (var i = 0; i < ids.length; i++) {
                               if (ids[i] === parentid) {
                                   parentid = "";
                               }
                           }
                           datagrid.QueryList({ PARENTID: parentid });//刷新数据表格
                           LoadOrganazationTree();//重新加载树
                       } else {
                           $.messager.alert('错误', resultData.msg);
                       }

                       // BEGIN: 2019-07-01 Add by hjs
                       // 隐藏加载遮罩层
                       if (parent.loding) {
                           parent.loding.hideLoding();
                       }
                       // END: 2019-07-01 Add by hjs
                   });
            }
        })


    }
    //加载组织机构类型下拉框
    var LoadSysCode = function () {
        common.AjaxPostAsync("/System/SysCodeManager/GetCodeList",
          { TYPEKEY: "BASE-ORGANAZATION-TYPE" }, function (result) {
              var resultData = JSON.parse(result);
              if (resultData.status === true) {
                  if (resultData.data !== null) {
                      Organazationtypes = resultData.data;//存储组织机构类型数据
                      var combotree = getOrganTypeNode(null);
                      OrganTypeTree = combotree;
                      LoadDataGrid();//加载组织机构数据
                      var newData = common.MergeArray(checkdSelect, resultData.data);//添加默认选项
                      $('#Query_CateGory').combobox({//
                          data: newData,
                          valueField: "CODEKEY",
                          textField: "CODEVALUE"
                      });
                      $("#Query_CateGory").combobox('setValue', checkdSelect[0].CODEKEY);//选中默认选项
                  }
              } else {
                  $.messager.alert('错误', resultData.msg);
              }
          },false);
    }

    function getOrganTypeNode(pid) {
        var options = [];
        for (var i = 0; i < Organazationtypes.length; i++) {
            if (Organazationtypes[i].PARENTID === pid) {
                var option = {
                    id: Organazationtypes[i].CODEKEY,
                    text: Organazationtypes[i].CODEVALUE,
                    children: []
                };
                option.children = getOrganTypeNode(Organazationtypes[i].CODEID);
                options.push(option);
            }
        }
        return options;
    }

    //根据条件查询组织机构数据
    organazation.Query = function () {
        var areatype = $("#Query_CateGory").combobox("getValue");
        if (areatype === "-1") {
            areatype = null;
        }
        var queryData = {
            PARENTID: parentid,
            CATEGORY: areatype,
            OrganName: $("#Query_FullName").val()
        };
        datagrid.pageIndex = 1;
        datagrid.QueryList(queryData);//刷新数据表格
    }
    //树节点查询
    var QueryTree = function () {
        var QueryConditions = $("#TreeQueryConditions").val();
        if (QueryConditions === "") {//判断查询条件
            $.fn.zTree.init($("#Organazation_ztree"), setting, Nodes).expandAll(true);
            return;
        }
        var data = [];
        for (var i = 0; i < Nodes.length; i++) {
            if (Nodes[i].name.indexOf(QueryConditions) !== -1) {
                common.getParentNodes(data, Nodes[i], Nodes);//获取父节点
            }
        }
        data.sort(sortNumber);//排序树
        $.fn.zTree.init($("#Organazation_ztree"), setting, data).expandAll(true);
    }*/
    //打开窗口
    organazation.Open = function () {
        $("#OrganizationForm").form('clear');
        $("#OrganizationInfo").dialog("open");
    }
    //关闭窗口
    var Close = function () {
        $("#OrganizationForm").form('clear');
        $("#OrganizationInfo").dialog("close");
    }
    //根据组织机构类型获取组织机构名称
    function GetAreaTypeName(areaType) {
        for (var i = 0; i < Organazationtypes.length; i++) {
            if (areaType === Organazationtypes[i].CODEKEY) {
                return Organazationtypes[i].CODEVALUE;
            }
        }
    }
    //清空树选中节点
    function cancelSelectedNode() {
        var treeObj = $.fn.zTree.getZTreeObj("Organazation_ztree");
        treeObj.cancelSelectedNode();
        parentid = "";
    }

    //非空判断
    function InputCheck() {
        var fullname = $("#FULLNAME").val();
        if (fullname.length === 0) {
            $.messager.alert('提示', "组织机构名称不能为空！");
            return false;
        }
        if (fullname.length > 20 || fullname.length < 2) {
            $.messager.alert('提示', "输入错误！组织机构名称长度在2~20之间");
            return false;
        }
        if ($("#CATEGORY").combobox("getValue").length === 0) {
            $.messager.alert('提示', "请选择组织机构类型");
            return false;
        }

        var orgCategory = $("#CATEGORY").combobox("getValue");
        if ("5" !== orgCategory) {
            if (Nodes.length !== 0 && $("#PARENTID").combobox("getValue").length === 0) {
                $.messager.alert('提示', "请选择上级组织机构");
                return false;
            }
        }

        var shortname = $("#SHORTNAME").val();
        if (shortname.length != 0 && shortname.length > 10) {
            $.messager.alert('提示', "输入错误！组织机构简称长度在2~10之间");
            return false;
        }
        var description = $("#DESCRIPTION").val();
        if (description.length !== 0 && description.length > 95) {
            $.messager.alert('提示', "描述的内容过长！不可超过95个文字");
            return false;
        }
        //if ($("#SERVERID").combobox("getValues").length == 0) {
        //    $.messager.alert('提示', "请选择服务器！");
        //    return false;
        //}

        // 上级组织不能是自己
        var currentOrgId = $("#ORGANIZATIONID").val();
        var parentOrgId = $("#PARENTID").combobox("getValue");
        if (currentOrgId && parentOrgId)    // 防止新建时，既没有 id，也没有 父 id
        {
            if (currentOrgId === parentOrgId) {
                $.messager.alert('提示', "上级组织不能是自己！请重新选择。");
                return false;
            }
        }

        return true;
    }
    //加载园区选择项
    organazation.LoadParkList = function () {
        common.AjaxPostAsync("/Basicdata/GridAreaManager/GetParkList",
          { TYPEKEY: "BASE_GRID_lEVEL" }, function (result) {
              var resultData = JSON.parse(result);
              if (resultData.status === true) {
                  if (resultData.data !== null) {
                      firstParkId = resultData.data[0].PARKID;
                      $('#PARKID').combobox({
                          data: resultData.data,
                          valueField: "PARKID",
                          textField: "PARKNAME"
                      });
                  }
              } else {
                  $.messager.alert('错误', resultData.msg);
              }
          },false);
    }
    //事件初始化
    function InitEvent() {
        //$("#PARENTID").combotree({
        //    onSelect: function (node) {
        //        var rows=$('#dgOrganizationList').datagrid('getChecked');
        //        if (rows[0].ORGANIZATIONID == node.id) {
        //            $(node.target).treegrid('unselect');
        //        }
        //    }
        //})
        $("#btn_Close").click(function () {
            Close();
        });
        $("#btn_Save").click(function () {
            var areaid = $("#ORGANIZATIONID");
            if (areaid.val() === "" || areaid.val() === undefined) {

                OrganazationAdd();
            } else {
                OrganazationEdit();
            }
        });
        $("#btn_Query").click(function () {
            //cancelSelectedNode();
            organazation.Query();
        })
        $("#btn_Clear").click(function () {
            $("#Query_FullName").textbox("setValue", "");
            $("#Query_CateGory").combobox('setValue', checkdSelect[0].CODEKEY);
            cancelSelectedNode();
            organazation.Query();
        })
        $("#btn_TreeQuery").click(function () {
            QueryTree();
        })
        $("#btn_CloseType").click(function () {
            $("#EditOrganTypeInfo").dialog("close");
            $("#OrganType").combotree("setValue", "");
        });
        $("#btn_SaveType").click(function () {
            SaveType();
        })

        // 组织机构类型下拉框 item 改变事件
        $("#CATEGORY").combobox({
            onChange: organazationCategoryChange
        });
        $(".tdPark").hide();      
    }

    // 私有方法: 组织类型改变方法
    var organazationCategoryChange = function () {
        var category = $("#CATEGORY").combotree("getValue");
        if (category == "5") {//一级部门需要选择园区，其它选择上级部门
            $(".tdPark").show();
            $(".tdLastOrgan").hide();
            organazation.LoadParkList();
        } else {
            $(".tdPark").hide();
            $(".tdLastOrgan").show();
        }
        getParentOrganazationByCategory(category);
    };

    // 私有方法: 根据组织类别获取父级组织
    var getParentOrganazationByCategory = function (category) {
        //console.log(category);

        if (!organazation.comboxOrganTree) {
            return;
        }

        // 这里由于编辑时，第一次会获取到的是文本，所以需要函数返回
        if (getValueByKey(organazation.categoryType, category) === "") {
            return;
        }

        // 获取选择类型对应的上级类型
        var temp = "";
        for (var i = 0; i < organazation.orgTypeRelation.length; i++) {
            if (organazation.orgTypeRelation[i].Key === category) {
                temp = organazation.orgTypeRelation[i];
                break;
            }
        }

        var typeArr = [];
        var types = "";
        if (temp && temp !== "") {
            // 有值，说明有上级类型，否则没有上级
            if (temp.Value.FullKey) {
                typeArr = temp.Value.FullKey.split(",");
            }
            else {
                organazation.getOrganazationByTypes({ "types": "-1" }); // 顶级类型节点
                return;
            }
        }
        else {
            return; // 理论上不会走这一步，因为每一种类型都会 organazation.orgTypeRelation 存在
        }

        // 拼接类型字符串
        if (typeArr.length > 0) {
            for (var i = 0; i < typeArr.length; i++) {
                types += typeArr[i] + ",";
            }
            types = types.substr(0, types.length - 1);
        }
        else {
            types = "-1";
        }

        organazation.getOrganazationByTypes({ "types": types });
    };

    // 根据类型获取组织
    organazation.getOrganazationByTypes = function (data) {
        common.AjaxPostAsync("/Basicdata/OrganazationManager/GetOrganazationByTypes",
            data,
            function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status === true) {
                    if (resultData.data !== null) {
                        var moduleTreeData = common.Array2Tree(resultData.data);
                        organazation.comboxOrganTree = moduleTreeData;

                        $("#PARENTID").combotree("clear");                      // 清除父级组织。
                        $('#PARENTID').combotree('loadData', moduleTreeData);   // 重新加载父级组织

                        if (organazation.currentEditOrg) {
                            if (hasParentId(moduleTreeData, organazation.currentEditOrg.PARENTID)) {
                                $("#PARENTID").combotree('setValue', organazation.currentEditOrg.PARENTID);
                            }
                        }
                    }
                } else {
                    $.messager.alert('错误', resultData.msg);
                }
            });
    }

    // 判断数组中是否存在此 Id（orgs 必须是数组，且包含 id 和 children 属性）
    function hasParentId(orgs, pid) {
        if (orgs) {
            for (var i = 0; i < orgs.length; i++) {
                if (orgs[i].id === pid) {
                    return true;
                }

                if (orgs[i].children && orgs[i].children.length > 0) {
                    return hasParentId(orgs[i].children, pid);
                }
            }
            return false;
        }
        return false;
    }
/*

    // 获取组织类型上级类型/下级类型
    // findChild : true 下级类型， false : 上级类型
    organazation.getOrganazationTypeRelation = function () {
        common.AjaxPostAsync("/OrganazationManager/GetOrganazationTypeRelation",
            { findChild: false },
            function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status) {
                    var temp = resultData.data;
                    for (var i = 0; i < temp.length; i++) {
                        organazation.orgTypeRelation.push({
                            Key: temp[i].Key, Value: {
                                Id: temp[i].Id,
                                Pid: temp[i].Pid,
                                Key: temp[i].Key,
                                FullKey: temp[i].FullKey
                            }
                        });
                    }
                    //console.log(organazation.orgTypeRelation);
                }
            });
    };

*/

   /* //加载服务器下拉框
    organazation.LoadServerList = function () {
        common.AjaxPostAsync("/EngIneManager/GetFaceServerList",
           null, function (result) {
               var resultData = JSON.parse(result);
               if (resultData.status) {
                   $('#SERVERID').combobox({
                       data: resultData.data,
                       valueField: "SERVERID",
                       textField: "SERVERNAME"
                   });
               } else {
                   $.messager.alert("提示", resultData.msg);
               }
           }, false);
    }*/
    organazation.Init = function () {
        // 获取组织类型关系
        //organazation.getOrganazationTypeRelation();

        LoadOrganazationTree();//加载组织机构树

        //LoadSysCode();//加载组织机构类型
        InitEvent();
        //ImportCollege();
        //ImportBanji();

       // organazation.LoadServerList();
    }
    // Init();//调用初始化
    return organazation;
}());
//加载数据，解决数据表格无高度问题（在此加载方法可以解决）
$(function () {
    OrganazationManager.Init();
   
})

