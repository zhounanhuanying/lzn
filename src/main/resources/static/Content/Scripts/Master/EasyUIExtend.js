var EasyUIExtend = function () {
    var easyUI = this;
    this.pageIndex = 1;  //当前页
    this.pageSize = 10;  //显示条数
    this.singleSelect = false; //是否单选
    this.sort = "";      //排序字段
    this.orderBy = 1;    //排序，0升序，1降序
    this.queryData = {};  //查询条件对象

    //***********************************************************
    //dtName,table的id
    //columns,table的列模型
    //toolbar,工具栏
    //url,请求的url地址
    //method,请求方式
    //**********************************************************
    easyUI.InitDataGrid = function (dtName, url, method, columns, toolbar, queryParam,callback) {
        this.url = url;
        this.method = method;
        this.dtName = dtName;
        this.queryData = queryParam;
        var param = { PageIndex: easyUI.pageIndex, PageSize: easyUI.pageSize, Sort: easyUI.sort, OrderBy: easyUI.orderBy };
        var queryData = easyUI.MergeJsonObject(param, queryParam);
        //if (parent.loding) {
        //    parent.loding.showLoding();
        //}
        $.ajax({
            url: url,
            type: "post",
            data: queryData,
            async: false,
            success: function (jsonResult) {
                var returnData = JSON.parse(jsonResult);
                $('#' + dtName).datagrid({
                    striped: true,
                    loadMsg: '正在加载……',
                    data: returnData.data,
                    remoteSort: false,
                    fitColumns:false,
                    pageNumber: easyUI.pageIndex, //重点:传入当前页数
                    pageSize: easyUI.pageSize,
                    pagination: true, //分页控件
                    rownumbers: false, //行号
                    singleSelect: easyUI.singleSelect, //是否单选
                    checkOnSelect: true,
                    nowrap: true,//允许换行
                    //sortName: "fff",
                    //sortOrder: 'asc', //初始排序方式
                    emptyMsg: '<div id="datanull"  style="text-align:center; margin-top:30px; height:439px;"><img id="imgsrc" src="../../Content/style/images/management/no-content.png" style="vertical-align:middle;" /></div>',
                    onMouseOverRow: function (e, rowIndex,
                                              rowData) { },
                    onMouseOutRow: function (e, rowIndex,
                                             rowData) { },
                    onDblClickRow: function (rowIndex, rowData) {
                        callback(rowData);
                    },
                    onClickRow: function (rowIndex, field, value) {
                        $("#" + dtName).datagrid("unselectAll");
                        $("#" + dtName).datagrid("selectRow", rowIndex);
                    },
                    columns: columns,
                    toolbar: toolbar,
                });
                $('#' + dtName).datagrid('getPager').pagination({//分页栏下方文字显示
                    // showPageList: false,
                    pageNumber: easyUI.pageIndex,
                    pageSize: easyUI.pageSize, //每页显示的记录条数，默认为10
                    //pageList: [30], //可以设置每页记录条数的列表
                    beforePageText: '第', //页数文本框前显示的汉字
                    afterPageText: '页    共 {pages} 页',
                    displayMsg: '当前显示{from}-{to}条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共{total}条',
                    onChangePageSize: function () {

                    },
                    onSelectPage: function (pageNumber, pageSize) {
                        //下一页
                        easyUI.pageIndex = pageNumber;
                        easyUI.pageSize = pageSize;
                        easyUI.QueryList(easyUI.queryData);

                    }
                });
                //if (parent.loding) {
                //    parent.loding.hideLoding();
                //}
            },
            error: function (ex) {
                //if (parent.loding) {
                //    parent.loding.hideLoding();
                //}
            }
        })

    }
    //通用查询方法,传递参数为json对象
    easyUI.QueryList = function (queryParam) {
        easyUI.queryData = queryParam;
        var param = { pageIndex: easyUI.pageIndex, pageSize: easyUI.pageSize, sort: easyUI.sort, orderBy: easyUI.orderBy };
        var queryData = easyUI.MergeJsonObject(queryParam, param);
        if (parent.loding) {
            parent.loding.showLoding();
        }
        $.ajax({
            url: easyUI.url,
            type: easyUI.method,
            data: queryData,
            async: false,
            success: function (jsonResult) {
                var returnData = JSON.parse(jsonResult);
                if (returnData.status) {
                    $("#" + easyUI.dtName).datagrid("loadData", returnData.data);
                    $("#" + easyUI.dtName).datagrid("scrollTo", 0);//重新加载数据之后滚动条返回第一行
                    var pager = $("#" + easyUI.dtName).datagrid("getPager");
                    pager.pagination('refresh', {
                        total: returnData.data.length,
                        pageNumber: easyUI.pageIndex
                    });
                } else {
                    $.messager.alert('提示', returnData.msg);
                }
                if (parent.loding) {
                    parent.loding.hideLoding();
                }
            },
            error: function () {
                if (parent.loding) {
                    parent.loding.hideLoding();
                }
            }
        });
    }
    ///合并2个json对象
    easyUI.MergeJsonObject = function (jsonbject1, jsonbject2) {
        var resultJsonObject = {};
        for (var attr in jsonbject1) {
            resultJsonObject[attr] = jsonbject1[attr];
        }
        for (var attr in jsonbject2) {
            // if (!resultJsonObject.hasOwnProperty(attr)) {
            resultJsonObject[attr] = jsonbject2[attr];
            //   }
        }
        return resultJsonObject;
    };
    return easyUI;
}