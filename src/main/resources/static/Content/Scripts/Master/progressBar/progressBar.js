$(function () {
    layui.use(['form', 'layedit', 'laydate', 'element', 'upload', 'table'], function () {
        var upload = layui.upload, form = layui.form,
            element = layui.element, $ = layui.$, table = layui.table, laydate = layui.laydate;
//            element.init();
//            form.render();
        //日期初始化
        laydate.render({
            elem: '#date_start'
            , type: 'datetime'
        });
        //创建监听函数
        xhrOnProgress = function (fun) {
            xhrOnProgress.onprogress = fun; //绑定监听
            //使用闭包实现监听绑
//                    return xhrOnProgressReturn($);
            return function () {
                //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
                var xhr = $.ajaxSettings.xhr();
                //判断监听函数是否为函数
                if (typeof xhrOnProgress.onprogress !== 'function')
                    return xhr;
                //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
                if (xhrOnProgress.onprogress && xhr.upload) {
                    xhr.upload.onprogress = xhrOnProgress.onprogress;
                }
                return xhr;
            }
        };

    });
});


function initProgressBar(element, indexL) {
    element.progress('js_upload_progress', '0');//设置页面进度条
    indexL = layer.open({
        type: 1,
        title: '上传进度',
        closeBtn: 0, //不显示关闭按钮
        area: ['400px', '150px'],
        shadeClose: false, //开启遮罩关闭
        content: $("#uploadLoadingDiv").html(),
        offset: '100px'
    });
    return indexL;
    // showloading(true);
}

function progressBarCallback(element, $, indexL, percentageUrl, percentMapClearUrl) {
    var timer = setInterval(function () {
        $.ajax({
            url: percentageUrl
            , type: "get"
            , async: false
            , success: function (res) {
                var percentage = res.percentage;
                var counts = res.counts;
                var countTotal = res.countTotal;
                var errorCount = res.errorCount;
                if (null != percentage && null != counts && null != countTotal &&percentage!=0) {debugger;
                    // showloading(false);
                    element.progress('js_upload_progress', percentage + '%');//设置页面进度条
                    element.progress('js_upload_progress', counts + "/" + countTotal);//设置页面进度条
                }
                if (percentage == 100 &&errorCount==null) {
                    clearInterval(timer);
                    $.ajax({
                        url: percentMapClearUrl
                        , type: "get"
                        , async: false
                    });
                    layer.close(indexL);
                    location.reload(true);
                    parent.layer.msg("上传成功", {time: 2000, icon: 1}, function () {
                    })
                }
                if(errorCount!=null){
                    clearInterval(timer);
                    $.ajax({
                        url: percentMapClearUrl
                        , type: "get"
                        , async: false
                    });
                }
            }
        })
    }, 1000)
}
function showloading(t) {
    if (t==1) {//如果是true则显示loading
        // console.log(t);
        var loadingIndex = layer.load(2, { //icon支持传入0-2
            shade: [0.1, 'gray'], //0.5透明度的灰色背景
            content: '请稍等...',
            success: function (layero) {
                layero.find('.layui-layer-content').css({
                    'padding-top': '39px',
                    'width': '120px'
                });
            }
        });
    } else {
        console.log("关闭loading层:" + t);
        layer.closeAll('loading');
    }
}

