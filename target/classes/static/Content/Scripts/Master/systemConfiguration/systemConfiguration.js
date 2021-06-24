
layui.use(["form", "element", "slider", "layer", "laydate"], function () {
    var form = layui.form;
    var element = layui.element;
    var layer = layui.layer;
    var slider = layui.slider;
    var laydate = layui.laydate;
    //监听指定开关
    form.on('switch(switchTest)', function (data) {
        if (this.checked === false) {
            layer.tips('温馨提示：关闭之后将不再接受告警推送的图片数据', data.othis)
        }
    });
});