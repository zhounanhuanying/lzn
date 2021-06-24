//var basePath = [[${#httpServletRequest.getScheme() + "://" + #httpServletRequest.getServerName() + ":" + #httpServletRequest.getServerPort() + #httpServletRequest.getContextPath()}]];
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
var access_token = window.localStorage.getItem("access_token"); //获取指定key本地存储的值
$(function () {
    layui.use(['layer', 'form', 'table', 'util',  'admin', 'xmSelect', 'laydate', 'contextMenu',], function () {

        const $ = layui.jquery;
        orgManage.contextMenu = layui.contextMenu;
        orgManage.form = layui.form;
        orgManage.admin = layui.admin;

        orgManage.layer = layui.layer;
        orgManage.table = layui.table;
        orgManage.table2 = layui.table;
        orgManage.table3 = layui.table;
        orgManage.treeTable = layui.treeTable;
        orgManage.util = layui.util;
        orgManage.xmSelect = layui.xmSelect;
        orgManage.laydate = layui.laydate;
        // orgManage.getOrgInfoZtree();

        orgManage.regionId = orgManage.getCookieValue('regionWitchDataId');
        if (orgManage.regionId == '') {
            layer.msg('请选择当前园区', {icon: 2, anim: 6});
        }

        var input_timeout;
        // 设置搜索时间间隔，防止频繁出发 input事件
        var input_time_interval = 100;
        // 搞一个锁 input事件,加锁
        var input_Lock = false;

        //对比开始
        $("#query_ipt").on('compositionstart', function () {
            input_Lock = true;
        });

        //对比结束
        $("#query_ipt").on('compositionend', function () {
            input_Lock = false;
            if (!input_Lock) {
                clearTimeout(input_timeout);
                input_timeout = setTimeout(onInput, input_time_interval);
            }
        });
        $("#query_ipt").on('input', function () {
            if (!input_Lock) {
                clearTimeout(input_timeout);
                input_timeout = setTimeout(onInput, input_time_interval);
            }
        });

        function onInput() {
            var input = $("#query_ipt").val();
            if(input == null || input == ''){
                getOrgInfoZtree();
            }
        }
        //获取组织树
        getOrgInfoZtree();
        //初始化goJS结构图组件
        // orgManage.initGoJS();
        //渲染属于当前节点下的人员数据表格
        orgManage.renderPersonGridBelong2CurrentOrg();
        //表格工具条点击事件
        orgManage.table.on('tool(personInfoTab)', function (obj) {
            //删除人员
            if (obj.event === 'del') {
                orgManage.layer.confirm('请确认是否需要删除该人员信息?', function (index) {
                    orgManage.deletePersonById(obj.data.personId, orgManage.orgId);
                });
            }
        });


        //监听人员选择数据表格复选框
        orgManage.table2.on('checkbox(selectPersonInfoTabDemo)', function (obj) {
            console.log(obj)
        });
    });
})


/**
 * 功能开关封装
 * @param handler 开关标志
 * @param selector 选择器
 */
function switchHandler(handler,selector){
    handler == 1 ? $(selector).show() : $(selector).hide();
}

$(function () {
    getOrgInfoZtree();
})
/**
 * 加载组织架构图
 */
function getOrgInfoZtree() {
    var orgName = $("#query_ipt").val();
    $.ajax({
        url:  "baseOrganizition/getOrganizationTree",
        headers: {
            "access_token": access_token//此处放置请求到的用户token
        },
        type: "GET",
        //data: {regionId: orgManage.regionId, orgName: orgName},
        dataType: "json",
        async: true,
        success: function (data) {
            //if (data.code != "200") {
                //layer.alert("获取组织架构错误,信息：" + data.msg + "错误请联系管理员！", {icon: 2});
                //return;
            //} else {

            console.log(JSON.stringify(data))
            alert(JSON.stringify(data))
                if (data.data.length == 0 || data.data == null) {
                    $("#orgTree").hide();
                } else {
                    $("#orgTree").show();
                    //zTree初始化
                    var zTree = $.fn.zTree.init($("#orgTree"), orgManage.setting,  data.data);
                    zTree.expandAll(true);
                    for (let i = 0; i < data.data.length; i++) {
                        if (data.data[i].pid != '0') {
                            orgManage.orgId = data.data[i].id;
                            orgManage.orgName = data.data[i].orgName=orgName;
                                                      // alert(orgManage.orgId);
                            var node = zTree.getNodeByParam("id", orgManage.orgId);
                            zTree.selectNode(node, true);   //将指定ID的节点选中
                            break;
                        }
                    }
                    orgManage.getPersonInfoData(orgManage.orgId);
                    orgManage.orgList = data.data;

                }
            //}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("获取组织架构错误" + XMLHttpRequest.status + "错误请联系管理员！", {icon: 2});
        }
    })
};

/**
 * ztree事件
 * @type {{view: {dblClickExpand: boolean, selectedMulti: boolean}, data: {keep: {parent: boolean, leaf: boolean}, simpleData: {idKey: string, enable: boolean, pIdKey: string, rootPId: string}, key: {name: string}}, edit: {enable: boolean, showRenameBtn: boolean, showRemoveBtn: boolean}, callback: {onClick: onClick}}}
 */
orgManage.setting = {
    edit: {
        enable: false,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    view: {
        selectedMulti: false,
        dblClickExpand: false
    },
    data: {
        keep: {
            parent: true,
            leaf: true
        },
        key: {
            name: "orgName"
        },
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: ""
        }
    },
    callback: {
        onClick: clickTreeNodeArea,         //点击节点加载人员信息
        onRightClick: onRightClick,         //右键节点 crud
        onDblClick: flashStructureDiagramByCurrentNode,  //双击节点，根据当前组织刷新组织结构图(go js)
        beforeDrag: function zTreeBeforeDrag(){return false}            //设置节点不可拖拽
    }
};

/**
 * 点击节点，获取选中节点下的所有人员信息
 * @param event
 * @param treeId
 * @param treeNode
 */
function clickTreeNodeArea(event, treeId, treeNode) {
    orgManage.orgId = treeNode.id;
    orgManage.orgName = treeNode.orgName;
    orgManage.currentCheckNode = treeNode.id;       //当前选中的id
    orgManage.getPersonInfoData(treeNode.id);
}


//初始化图形组件
// function initGoJS () {
//     //gojs画笔
//     var goJS = go.GraphObject.make;
//     //构建画布
//     myDiagram = goJS(go.Diagram, "OrgDiagramDiv", {
//         contentAlignment: go.Spot.Center,//内容的对齐方式
//         validCycle: go.Diagram.CycleDestinationTree,//确保用户只能创建树
//         maxSelectionCount: 1,//用户可以选择一次只有一个部分
//         layout: goJS(go.TreeLayout, {//画布里的元素布局方式
//             //获取或设置结果树的样式。 必须是TreeLayout.StyleLayered，TreeLayout.StyleAlternating， TreeLayout.StyleLastParents或TreeLayout.StyleRootOnly。
//             treeStyle: go.TreeLayout.StyleLastParents,
//             //获取或设置如何安排树应该布局单独的树。 必须是TreeLayout.ArrangementVertical，TreeLayout.ArrangementHorizo​​ntal或TreeLayout.ArrangementFixedRoots。 默认值为TreeLayout.ArrangementVertical
//             arrangement: go.TreeLayout.ArrangementHorizontal,
//             // 获取或设置树生长的默认方向。默认值为0; 该值必须为以下值之一：0，90，180，270。这些值以度为单位，其中0沿着正X轴，其中90沿着正Y轴
//             angle: 90,
//             //层之间的距离-父节点与其子节点之间的距离。
//             layerSpacing: 35,
//             //获取或设置树生长的替代方向。默认值为0; 该值必须为以下值之一：0，90，180，270。这些值以度为单位，其中0沿着正X轴，其中90沿着正Y轴
//             alternateAngle: 0,
//             //获取或设置父节点与其子节点之间的替代距离。默认值为50。这是父节点与其第一行子节点之间的距离，以防它的子节点有多行。所述rowSpacing属性确定子节点行之间的距离。负值可能导致子节点与父节点重叠
//             alternateLayerSpacing: 35,
//             //获取或设置父项相对于其子项的替代对齐方式。 必须是其名称以“Alignment”开头的TreeLayout的静态常量。默认值为TreeLayout.AlignmentCenterChildren。
//             alternateAlignment: go.TreeLayout.AlignmentStart,
//             //获取或设置第一个子节点的替代缩进。默认值为零。该值应为非负数。当对齐方式 为AlignmentStart或AlignmentEnd时，此属性才有意义。如果由于某种原因想要在子行的开头保留空间，则使用正值非常有用。例如，如果您想假装父节点是无限深，您可以将其设置为父节点的宽度
//             alternateNodeIndent: 10,
//             //获取或设置此节点宽度的分数被添加到nodeIndent 以确定子节点开始处的任何间距。默认值为0.0 - 唯一的缩进由nodeIndent指定。当值为1.0时，子节点将缩进超过父节点的宽度。当对齐方式 为AlignmentStart或AlignmentEnd时，此属性才有意义
//             alternateNodeIndentPastParent: 1.0,
//             //获取或设置子节点之间的替代距离。 默认值为20。负值导致兄弟节点重叠
//             alternateNodeSpacing: 10,
//             //获取或设置节点深度的替代分数，其中子层的开始与父层的交叠。默认值为0.0 - 仅当layerSpacing为负时，图层之间才有重叠。值为1.0和零layerSpacing将导致子节点与父节点完全重叠。大于零的值可能仍然导致层之间的重叠，除非layerSpacing的值足够大。如果layerSpacing为负，则值为零可能仍允许图层之间重叠
//             alternateLayerSpacingParentOverlap: 1.0,
//             //获取或设置此节点的端口获取作为其FromSpot的备用点。默认值为Spot.Default。Spot.Default 的值将使TreeLayout根据父节点的TreeVertex.angle分配一个FromSpot。如果值不是NoSpot，它只是被分配。当path是PathSource时，将设置端口的ToSpot而不是FromSpot
//             alternatePortSpot: new go.Spot(0.01, 1, 10, 0),
//             //获取或设置子节点的端口获取作为其ToSpot的备用点默认值为Spot.Default。Spot.Default 的值将使TreeLayout根据父节点的TreeVertex.angle分配ToSpot。如果值不是NoSpot，它只是被分配。当path是PathSource时，将设置端口的FromSpot而不是ToSpot
//             alternateChildPortSpot: go.Spot.Left
//         }),
//         "undoManager.isEnabled": true
//     });
//
//     function textStyle() {
//         return {font: "20pt sans-serif", stroke: "white"};
//     }
//
//     // 定义节点模板
//     myDiagram.nodeTemplate =
//         goJS(go.Node, "Auto",
//             // 进行排序,有节点。文本是data.name
//             new go.Binding("text", "name"),
//             // 绑定的一部分。layerName控制节点的层
//             new go.Binding("layerName", "isSelected", function (sel) {
//                 return sel ? "Foreground" : "";
//             }).ofObject(),
//             // 定义节点的外部形状
//             goJS(go.Shape, "RoundedRectangle", {
//                 name: "SHAPE",
//                 //fill: graygrad, stroke: "black",
//                 portId: "", fromLinkable: true, toLinkable: true, cursor: "pointer", fill: "#00A9C9"
//             }),
//             // 定义文本的面板显示
//             goJS(go.Panel, "Table", {
//                     maxSize: new go.Size(150, 999),
//                     margin: new go.Margin(3, 3, 0, 3),
//                     defaultAlignment: go.Spot.Left
//                 },
//                 goJS(go.RowColumnDefinition, {column: 2, width: 4}),
//                 goJS(go.TextBlock,  // the name
//                     {
//                         row: 0, column: 0, columnSpan: 5,
//                         font: "bold 15pt sans-serif",
//                         editable: false, isMultiline: false,
//                         stroke: "white", minSize: new go.Size(10, 14)
//                     },
//                     new go.Binding("text", "name").makeTwoWay()),
//                 goJS("TreeExpanderButton",
//                     {row: 4, columnSpan: 99, alignment: go.Spot.Center})
//             )
//         );
//     myDiagram.addDiagramListener("ObjectSingleClicked",
//         function (e) {
//             var part = e.subject.part;
//         });
//     // 定义模板的联系
//     myDiagram.linkTemplate =
//         goJS(go.Link, go.Link.Orthogonal,
//             {corner: 5, relinkableFrom: true, relinkableTo: true},
//             goJS(go.Shape, {strokeWidth: 2}));
//     //首次加载默认加载全部
//     orgManage.loadOrganizationData4GoJsPlugin('');
// }

/**
 * 双击组织树节点，刷新右侧组织结构图，只支持父节点生效
 * @param event
 * @param treeId
 * @param treeNode
 */
function flashStructureDiagramByCurrentNode(event, treeId, treeNode) {
    if (treeNode.isParent) {
        orgManage.loadOrganizationData4GoJsPlugin(treeNode.id);
        console.log(treeNode.id);
    }
    return;
}

/**
 * 加载go js插件需要的组织节点信息，绘制组织结构图
 * @param orgId
 */
orgManage.loadOrganizationData4GoJsPlugin = function (orgId) {
    $.ajax({
        url: "baseOrganizition/getOrganizationTree",
        data: {orgId: orgId, regionId: orgManage.regionId},
        type: "GET",
        dataType: "json",
        async: true,
        success: function (data) {
            /*if (data.code != 0) {
                return;
            } else {*/
            debugger
                var gojsData = {
                    "class": "go.GraphLinksModel",
                    "nodeDataArray": data.data.nodeList,
                    "linkDataArray": data.data.linkList
                }
                myDiagram.model = go.Model.fromJson(gojsData);
            //}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("加载组织架构图错误:" + XMLHttpRequest.responseText + ",请联系管理员(错误码:" + XMLHttpRequest.status + ")");
        }
    })
}

/**
 * 节点点击事件，crud,右键空白处，新增最高父节点下直系子节点
 * @param event
 * @param treeId
 * @param treeNode
 */
function onRightClick(event, treeId, treeNode) {
    zTree = $.fn.zTree.getZTreeObj("orgTree");
    rightNode = treeNode;
    //直接右键在空白处新增一个根节点
    if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
        if (event) { orgManage.addParentNode(event, '0'); }
    } else if (treeNode && !treeNode.noR) {
        zTree.selectNode(treeNode);
        orgManage.buildindFloorClickId = treeNode.id;
        if (event){orgManage.expandOperationPanel4OrgNode(treeNode.id, treeNode.pid, treeNode.orgName, treeNode, event);}
    }
}


orgManage.getChildren = function (ids, treeNode) {
    alert(JSON.stringify(treeNode));
    ids.push(treeNode.id);
    if (treeNode.isParent) {
        for (var obj in treeNode.children) {
            orgManage.getChildren(ids, treeNode.children[obj]);
        }
    }
    return ids;
}

/**
 * 园区下没有组织时，点击新增按钮，新增父节点
 */
// orgManage.addFirstOrg = function(){
//     orgManage.isFirstNode = true;
//     orgManage.addOrUpdate = true;
//     orgManage.pid = '0';     //首次添加设置pid为根节点
//     orgManage.admin.open({
//         type: 1,
//         title: '添加组织信息',
//         area: ['400px', '400px'],
//         content: $('#addOrganizationBox').html(),
//         success: function (layero) {
//             orgManage.getOrgType(true);
//             $("#parent_name").attr("disabled", "disabled");
//             orgManage.form.render('select', 'addOrganizationEditForm');
//             orgManage.form.render('radio', 'addOrganizationEditForm');
//             // 禁止弹窗出现滚动条
//             $(layero).children('.layui-layer-content').css('overflow', 'visible');
//         }
//     });
// }


/**
 * 新增一个当前园区下的最高组织父节点
 * @param e
 * @param data
 */
orgManage.addParentNode = function (e, data) {
    var items = [{
        name: '新增',
        click: function (index) {
            orgManage.addOrUpdate = true;
            orgManage.pid = data;
            orgManage.admin.open({
                type: 1,
                title: '添加组织信息',
                area: ['400px', '400px'],
                content: $('#addOrganizationBox').html(),
                success: function (layero) {
                    orgManage.getOrgType(true);
                    $("#parent_name").attr("disabled", "disabled");
                    //打开弹窗后，加载组织类型
                    orgManage.form.render('select', 'addOrganizationEditForm');
                    orgManage.form.render('radio', 'addOrganizationEditForm');
                    // 禁止弹窗出现滚动条
                    $(layero).children('.layui-layer-content').css('overflow', 'visible');
                }
            });
        }
    }];
    if(e){
        orgManage.contextMenu.setEvents(items, e);
        orgManage.contextMenu.show(items, e.clientX, e.clientY, e);
    }
}



/**
 * 右键组织节点，弹出选项卡片 item : {新增,删除,修改}
 * @param id
 * @param pid
 * @param orgName
 * @param treeNode
 * @param e
 */
orgManage.expandOperationPanel4OrgNode = function (id, pid, orgName, treeNode, e) {
    orgManage.clickNodeType = treeNode.orgType;
    //alert(treeNode.orgType);
    var items = [{
        name: '新增',
        click: function (index) {
            orgManage.addOrUpdate = true;
            orgManage.pid = id;
            orgManage.admin.open({
                type: 1,
                title: '添加组织信息',
                area: ['400px', '400px'],
                content: $('#addOrganizationBox').html(),
                success: function (layero) {
                    orgManage.getOrgType(true);
                    $("#parent_name").val(orgName);
                    orgManage.form.render('select', 'addOrganizationEditForm');
                    orgManage.form.render('radio', 'addOrganizationEditForm');
                    // 禁止弹窗出现滚动条
                    $(layero).children('.layui-layer-content').css('overflow', 'visible');
                }
            });
        }
    }, {
        name: '删除',
        click: function (index) {
            layer.confirm('请确定是否删除该组织?', {icon: 3, title: '提示'}, function (index) {
                orgManage.delIds = "";
                var ids = [];
                ids = orgManage.getChildren(ids, treeNode);
                for (var i = 0; i < ids.length; i++) {
                    orgManage.delIds += ids[i] + ",";
                }
                orgManage.delIds = orgManage.delIds.substring(0, orgManage.delIds.length - 1);
                orgManage.admin.ajax({
                    type: "post",
                    url: conf.dsrpBaseServer + "organization/deleteOrganizationNode",
                    data: {orgId: orgManage.delIds,source:"dsrp"},
                    dataType: "json",
                    success: function (data) {
                        getOrgInfoZtree();
                        orgManage.loadOrganizationData4GoJsPlugin("");
                    },
                    error: function (data) {
                        buildingFloorManage.layer.msg("删除失败", {icon: 2, time: 3000});
                    }
                });
                layer.close(index);
            });
        }
    }, {
        name: '修改',
        click: function (index) {
            orgManage.id = id;
            orgManage.addOrUpdate = false;
            orgManage.admin.open({
                type: 1,
                title: '修改组织信息',
                area: ['400px', '400px'],
                content: $('#updOrganizationBox').html(),
                success: function (layero) {
                    orgManage.getOrgType(false);
                    orgManage.getOrgItemInfo(id);
                    $("#name_upd").val(orgName);
                    // $("#org_sel_upd").val(orgManage.itemInfo.orgType);
                    $("#org_content_upd").val(orgManage.itemInfo.orgContent);
                    orgManage.form.render('select', 'addOrganizationEditForm');
                    orgManage.form.render('radio', 'addOrganizationEditForm');
                    // 禁止弹窗出现滚动条
                    $(layero).children('.layui-layer-content').css('overflow', 'visible');
                }
            });
        }
    }];
    orgManage.contextMenu.setEvents(items, e);
    orgManage.contextMenu.show(items, e.clientX, e.clientY, e);
    orgManage.loadOrganizationData4GoJsPlugin("");
};


/**
 * 获取组织类型字典数据
 */
orgManage.getOrgType = function(isAdd){
    orgManage.admin.ajax({
        type: "get",
        url: conf.dsrpBaseServer + "organization/getOrgTypeList",
        data: {},
        contentType: "application/json",
        dataType: "json",
        // async:true,
        success: function (data) {
            console.log(JSON.stringify(data));
            //请求成功后，给弹窗赋值
            var orgType = data.data;
            var _html = '';
            for (var i = 0; i < orgType.length;i++){
                _html += "<option value="+orgType[i].codeKey+">"+orgType[i].codeValue+"</option>";
            }
            console.log(_html);
            if (isAdd){
                $("#org_sel").append(_html);
                orgManage.form.render('select', 'addOrganizationEditForm');
            }else{
                $("#org_sel_upd").append(_html);
                $("#org_sel_upd").val(orgManage.itemInfo.orgType);
                orgManage.form.render('select', 'addOrganizationEditForm');
            }

        },
        error: function (data) {
            buildingFloorManage.layer.msg("新增失败", {icon: 2, time: 5000});
        }
    });
}


/**
 * 创建/编辑组织节点信息
 */
orgManage.createOrEditOrgNode = function () {
    var orgName = '';
    var orgDesc = '';
    if(orgManage.addOrUpdate){
        orgName = $("#name").val();
        orgDesc = $("#org_content").val();
    }else {
        orgName = $("#name_upd").val();
        orgDesc = $("#org_content_upd").val();
    }

    if(orgName == null || orgName == ''){
        orgManage.layer.msg('组织名称为必填项!', {icon:2, time:2000});
        return;
    }
    if (orgManage.addOrUpdate) {
        var mapInfo = {};
        var name = $("#name").val().toString();
        var type = $("#org_sel").prop('selectedIndex');
        var content = $("#org_content").val().toString();

        mapInfo.pid = orgManage.pid;
        mapInfo.orgName = name;
        mapInfo.orgType = type;
        mapInfo.orgContent = content;
        mapInfo.regionId = orgManage.regionId;
        mapInfo.source = "dsrp";
        orgManage.admin.ajax({
            type: "post",
            url: conf.dsrpBaseServer + "organization/createOrganizationNode",
            data: JSON.stringify(mapInfo),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                orgManage.isFirstNode = false;
                getOrgInfoZtree();
                orgManage.loadOrganizationData4GoJsPlugin("");
            },
            error: function (data) {
                buildingFloorManage.layer.msg("新增失败", {icon: 2, time: 5000});
            }
        });
        layer.closeAll();
    } else {
        var mapInfo = {};
        var name = $("#name_upd").val().toString();
        var type = $("#org_sel_upd").val();
        var content = $("#org_content_upd").val().toString();
        console.log("右键点击的节点类型"+orgManage.clickNodeType);
        var map = {"id": orgManage.id, "orgName": name, "orgType": type, "orgContent": content,"source":"dsrp"};
        mapInfo.id = orgManage.id;
        mapInfo.orgName = name;
        mapInfo.orgType = type;
        mapInfo.orgContent = content;
        mapInfo.source = "dsrp"
        console.log("修改选择的类型：" + mapInfo.orgType);
        orgManage.admin.ajax({
            type: "post",
            url: conf.dsrpBaseServer + "organization/updateOrganizationNode",
            data: JSON.stringify(mapInfo),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                if (data.code != 0) {
                    layer.msg("修改失败,信息：" + data.msg + "错误请联系管理员！", {icon: 2, time: 5000});
                    return;
                } else {
                    orgManage.isFirstNode = false;
                    getOrgInfoZtree();
                    orgManage.loadOrganizationData4GoJsPlugin("");
                    layer.msg("修改成功", {icon: 1, time: 5000});
                }
            },
            error: function (data) {
                layer.msg("修改失败", {icon: 2, time: 5000});
            }
        });
        layer.closeAll();
    }
}

/**
 * 修改节点回显数据
 * @param id
 */
orgManage.getOrgItemInfo = function (id) {
    orgManage.admin.ajax({
        type: "post",
        url: conf.dsrpBaseServer + "organization/getOrgItemInfoById",
        data: {itemId: id},
        async: false,
        dataType: "json",
        success: function (data) {
            if (data.code != 0) {
                layer.msg("获取组织信息失败,信息：" + data.msg, {icon: 2, time: 5000});
                return;
            } else {
                orgManage.itemInfo = data.data;
            }
        },
        error: function (data) {
            layer.msg("上传失败", {icon: 2, time: 5000});
        }
    });
}

/**
 * 加载组织人员列表信息
 */
orgManage.renderPersonGridBelong2CurrentOrg = function () {
    orgManage.table.render({
        id: 'personTab',
        elem: '#personInfoTab',
        data: [],
        page: true,
        limit: 5,
        limits: [5,10,15,20,25],
        cellMinWidth: 100,
        height: '260',
        cols: [[
            {type: 'checkbox'},
            {field: 'personName', title: '人员姓名', align: 'center'},
            {
                field: 'gender', title: '性别',
                templet: function (d) {
                    return d.gender == 1 ? '男' : d.gender == 2 ? '女' : d.gender == 3 ? '未知' : '--';
                },
                align: 'center'
            },
            {field: 'telephone', title: '联系方式', align: 'center'},
            {field: 'residentialAddress', title: '籍贯', align: 'center'},
            {
                field: 'identityNo',
                title: '证件号',
                align: 'center',
                templet: function (e) {
                    return (e.idCard == null || e.idCard == "") ? '--' : util.ShowEncryption(e.idCard);
                }
            },
            {
                title: '操作',
                toolbar: ['<p>',
                    '<button type="button" lay-event="del" id="del" class="layui-btn layui-btn-danger layui-btn-xs icon-btn" >删除</button>&nbsp;',
                    '</p>'].join(''),
            },
        ]],
        done: function (res, pageIndex, count, code) {

        }
    });
}


/**
 * 根据当前选中的组织节点id查询归属于当前组织下的所有人员信息
 * @param orgId
 * @returns {boolean}
 */
orgManage.getPersonInfoData = function (orgId) {
    var tempId = "";
    var zTree = $.fn.zTree.getZTreeObj("orgTree");
    var node = zTree.getSelectedNodes();
    if(node != null && node.length != 0){
        tempId = node[0].id;
    }
    //园区id
    //var parkId = orgManage.parkId;
    var searchPersonName = $("#personName").val();
    // orgManage.table.reload("personTab", {
    //     url: "personTable/getPersonByPersonId",
    //     headers: {
    //         "access_token": access_token//此处放置请求到的用户token
    //     },
    //     page: {
    //         curr: 1//从第一页开始
    //     },
    //     where: {
    //         //parkId: parkId,
    //         organizitionId: tempId,
    //         personName: searchPersonName
    //     }
    // });
    return false;
}

/**
 * 根据personId删除当前记录，
 * 1，如果删除的是叶子节点，直接删除，
 * 2，如果是父节点，需要进行级联删除，需要遍历所有子节点，在所有子节点中如果存在该人员信息，全部删除，
 * @param personId
 * @param orgId
 */
orgManage.deletePersonById = function (personId, orgId) {
    var isParent = "";
    var zTree = $.fn.zTree.getZTreeObj("orgTree");
    var node = zTree.getSelectedNodes();
    if(node != null && node.length != 0){
        isParent = node[0].isParent;
    }
    orgManage.admin.ajax({
        url: conf.dsrpBaseServer + "organization/deletePersonAssociationByOrgIdAndPersonId",
        type: "post",
        async: false,
        data: {personId: personId, orgId: orgId,isParent: isParent},
        dataType: "json",
        success: function (data) {
            if (data.code != 0) {
                layer.msg(data.msg, {icon: 2, time: 3000});
            } else {
                layer.msg(data.msg, {icon: 1, time: 3000});
                orgManage.getPersonInfoData();
                return;
            }
        },
        error: function (data) {
            layer.msg("删除失败", {icon: 2, time: 5000});
        }

    })
}

/**
 * 点击选择按钮，打开选择人员弹窗，并加载渲染数据
 */
orgManage.openChoosePersonDialog = function () {
    if (!orgManage.orgId) {
        return;
    }
    var zTree = $.fn.zTree.getZTreeObj("orgTree");
    var node = zTree.getSelectedNodes();

    if(node[0].orgType == 0){
        layer.msg("基本组织架构下不能绑定人员", {icon: 7, time: 3000});
        return;
    }
    orgManage.initChoosePersonList();       //渲染数据表格，
    orgManage.loadPersonNotInCurrentOrg(orgManage.regionId);        //加载数据
    orgManage.setEditRegionSel();          //分页加载下拉选
}

/*人员选择窗口*/
orgManage.initChoosePersonList = function () {
    orgManage.chooseDialog = orgManage.admin.open({
        type: 1,
        title: '选择人员信息',
        area: ['1200px', '600px'],
        content: $('#selectPersonInfoBox').html(),
        success: function (layero) {
            $(layero).children('.layui-layer-content').css('overflow', 'visible');
            orgManage.table2.render({
                elem: '#selectPersonInfoTab',
                id: 'idTest',
                page: true,
                limit: 8,
                limits: [8,20,30,40,50],
                cellMinWidth: 100,
                height: '410',
                cols: [[
                    {type: 'checkbox'},
                    {field: 'name', title: '姓名'},
                    {
                        field: 'gender', title: '性别',
                        templet: function (d) {
                            return d.gender == 1 ? '男' : d.gender == 2 ? '女' : d.gender == 3 ? '未知' : '--';
                        }
                    },
                    {field: 'phone', title: '联系方式'},
                    {field: 'nativePlace', title: '籍贯'},
                    {field: 'idCard', title: '证件号'},

                ]],
                done: function (res, pageIndex, count) {
                }
            });
            orgManage.form.render('checkbox');
        }
    });
}

/**
 * 根据当前选择节点id查询不在该节点下的所有人员信息
 * @param regionId
 * @returns {boolean}
 */
orgManage.loadPersonNotInCurrentOrg = function (regionId) {
    //获取选中的节点id
    var zTree = $.fn.zTree.getZTreeObj("orgTree");
    var node = zTree.getSelectedNodes();

    var searchPersonName = $("#searchPersonName").val();
    orgManage.table2.reload("idTest", {
        url: conf.dsrpBaseServer + "organization/getPersonListData",
        headers: {"Authorization": orgManage.getHeader()},
        page: {
            curr: 1
        },
        where: {
            regionId: orgManage.regionId,
            orgId: node[0].id,
            personName: searchPersonName,
        }
    });
    return false;
};

/**
 * 分页加载当前用户权限下的园区列表
 */
orgManage.setEditRegionSel = function () {
    orgManage.admin.ajax({
        url: conf.dsrpBaseServer + "organization/getRegionInfo",
        type: 'get',
        datatype: 'json',
        data: {regionId:orgManage.regionId},
        async: false,
        success: function (data) {
            if (data.code == 0) {
                $("#editPersonRegion").val(data.data.regionName);
                //禁用输入
                $('#editPersonRegion').prop('disabled', true);
            } else {
                layer.msg("园区信息加载失败", {icon: 2, time: 3000})
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.msg("获取园区信息错误, " + XMLHttpRequest.responseText);
        }
    })
    // $('#editPersonRegion').selectPage({
    //     showField: 'regionName',
    //     keyField: 'id',
    //     headers: {"Authorization": orgManage.setter.getHeader()},
    //     data: orgManage.setter.baseServer + "mapconfig/findRegionInfoPage",
    //     pageSize: 8,
    //     searchField: 'regionName',
    //     params: function () {
    //         return {'returnAllflag': 0};
    //     },
    //     eSelect: function (data) {
    //         orgManage.regionId = data.id;
    //     },
    //     eClear: function () {
    //         orgManage.regionId = null;
    //     },
    //     eAjaxSuccess: function (data) {
    //         return data.data;
    //     }
    // });
    // if (orgManage.regionId == null || orgManage.regionId == "") {
    //     orgManage.regionId = orgManage.setter.getCookieValue("regionWitchDataId");  // cookie中当前园区ID
    //     $('#editPersonRegion').val(orgManage.regionId);
    // } else {
    //     $('#editPersonRegion').val(orgManage.regionId);
    // }
    // $('#editPersonRegion').selectPageRefresh();
}


/**
 * 封装公共方法：数据列表多选返回选择节点id Map数组
 * @param filter 表格filter -> Id
 * @param isDelete  是否是批量删除功能
 */
orgManage.getCheckData = function (filter, isDelete) {
    var checkStatus = orgManage.table2.checkStatus(filter);
    if (isDelete) {
        checkStatus = orgManage.table.checkStatus(filter);
    }
    var data = checkStatus.data;
    data.forEach(function (item) {
        var map = {};
        isDelete ? map.personId = item.personId : map.personId = item.id;
        map.orgId = orgManage.orgId;
        map.source = "dsrp";
        orgManage.associationInfoList.push(map);
    })
    console.log(JSON.stringify(orgManage.associationInfoList));
}


/**
 * 将选中的人员添加到当前选中的组织下
 */
orgManage.buildAssociationBetweenPersonAndOrg = function () {
    //获取选中数据
    orgManage.getCheckData('idTest', false);

    if(orgManage.associationInfoList.length == 0){
        layer.msg("请选择需要添加的人员信息!", {icon: 7, time: 3000});
        return;
    }
    orgManage.layer.confirm('请确定是否需要将该人员添加到【' + orgManage.orgName + '】下?', function (index) {
        orgManage.getCheckData('idTest', false);
        var orgId = orgManage.orgId;
        orgManage.admin.ajax({
            url: conf.dsrpBaseServer + "organization/buildAssociationBetweenPersonAndOrg",
            type: 'post',
            datatype: 'json',
            data: {associationInfoList: JSON.stringify(orgManage.associationInfoList)},
            async: false,
            success: function (data) {
                if (data.code == 0) {
                    layer.msg("关联成功", {icon: 1, time: 3000});
                    layer.closeAll();
                    //关联成功之后，务必清空数组，以便下次使用!!
                    orgManage.associationInfoList.splice(0, orgManage.associationInfoList.length);
                    orgManage.getPersonInfoData(orgManage.orgId);
                } else {
                    layer.msg("关联失败", {icon: 2, time: 3000});
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.msg("关联组织人员信息失败,信息：" + XMLHttpRequest.responseText + ",请联系管理员(错误码:" + XMLHttpRequest.status + ")");
            }
        })
        layer.close(index);
    });
    orgManage.associationInfoList.splice(0, orgManage.associationInfoList.length);
};


/**
 * 批量删除组织下关联的人员信息
 */
orgManage.batchDelPerson = function () {
    var isParent = "";
    var zTree = $.fn.zTree.getZTreeObj("orgTree");
    var node = zTree.getSelectedNodes();
    if(node != null && node.length != 0){
        isParent = node[0].isParent;
    }
    //获取选中的人员数据，存入orgManage.associationInfoList
    orgManage.getCheckData('personTab', true);

    if(orgManage.associationInfoList.length == 0){
        layer.msg("请选择需要删除的人员!", {icon: 7, time: 3000});
        return;
    }
    orgManage.layer.confirm('请确认是否需要删除该人员信息?', function (index) {
        orgManage.getCheckData('personTab', true);
        orgManage.admin.ajax({
            type: "post",
            url: conf.dsrpBaseServer + "organization/batchDeletePersonByIds",
            data: {associationInfoList: JSON.stringify(orgManage.associationInfoList),isParent: isParent},
            async: false,
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    layer.msg("批量删除成功!", {icon: 1, time: 3000});
                    orgManage.associationInfoList.splice(0, orgManage.associationInfoList.length);   //清空数组，否则会和人员选择弹窗出现冲突
                    orgManage.getPersonInfoData(orgManage.orgId);
                } else {
                    layer.msg("批量删除失败!", {icon: 2, time: 3000});
                }
            },
            error: function (data) {
                buildingFloorManage.layer.msg("删除失败", {icon: 2, time: 3000});
            }
        });
    });
    orgManage.associationInfoList.splice(0, orgManage.associationInfoList.length);   //清空数组，防止删除后数组依然非空，导致校验错误
}

