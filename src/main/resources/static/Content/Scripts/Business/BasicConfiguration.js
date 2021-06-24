//********************************************************************************
//** 作者： 陈剑辉
//** 创建时间：2019-01-08
//** 描述：基础配置前端js操作
//*********************************************************************************/
layui.use(["form", "element", "slider", "layer", "laydate"], function () {
    var form = layui.form;
    var element = layui.element;
    var layer = layui.layer;
    var slider = layui.slider;
    var laydate = layui.laydate;
    //监听指定开关
    form.on('switch(switchTest)', function (data) {
        if (this.checked === false) {
            layer.tips('温馨提示：关闭之后左侧的功能菜单默认隐藏', data.othis)
        }
    });
    form.on('switch(switchTest1)', function (data) {
        if (this.checked === false) {
            layer.tips('温馨提示：选择单个之后所有页面会在同一个页签中打开', data.othis)
        }
    });

    //监听折叠
    //element.on('collapse(test)', function (data) {
    //    layer.msg('展开状态：' + data.show);
    //});

    var date = new Date()
    var dateDefault = common.DateFormater(date, "hh:mm:ss");//当前时间

    var nextDate = new Date(date.getTime() + 60 * 60 * 1000); //当前时间的后一小时
    nextDate = common.DateFormater(nextDate, "hh:mm:ss");

    //设置步长 大数据配置 公寓昨日24小时内进出情况对比图 天数设置
    var slideTest1 = slider.render({
        elem: '#slideTest1'
        , step: 1 //步长
        // , value: [0, 23]
        //  , range: true
        , showstep: true //开启间隔点
        , max: 7
        // , min: 1
        // , showstep: true //开启间隔点
        , change: function (value) {
            $("#BottomTime").val(value);
            $(this.elem).next("label")[0].innerText = "近" + value + "天";
        }
    });

    // 共用规则 异常警告次数设置
    var slideTest2 = slider.render({
        elem: '#slideTest2'
        , step: 1 //步长
        , max: 10
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#AlarmMessageNumber").val(value);
            $(this.elem).next("label")[0].innerText = "大于" + value + "次告警";
        }
    });

    // 共用规则 异常告警天数设置
    var slideTest14 = slider.render({
        elem: '#slideTest14'
        , step: 1 //步长
        , max: 60
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#AlarmMessageDay").val(value);
            $(this.elem).next("label")[0].innerText = "统计近" + value + "天内的告警";
        }
    });

    // 公共规则 近一周 天数设置
    var slideTest3 = slider.render({
        elem: '#slideTest3'
        , step: 1 //步长
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#LeftBottomWeek").val(value);
            $(this.elem).next("label")[0].innerText = "近" + value + "天";
        }
    });

    // 公共规则 近30天公寓异常进出情况男女比例近30天 天数设置
    var slideTest4 = slider.render({
        elem: '#slideTest4'
        , step: 1 //步长
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#LeftCentrDays").val(value);
            $(this.elem).next("label")[0].innerText = "近" + value + "天";
        }
    });

    // 公共规则 公寓近30天异常>N次人员列表 天数设置
    var slideTest5 = slider.render({
        elem: '#slideTest5'
        , step: 1 //步长
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#RightBottomDays").val(value);
            $(this.elem).next("label")[0].innerText = "近" + value + "天";
        }
    });

    //公共规则 公寓近30天异常>N次人员列表 次数设置
    var slideTest6 = slider.render({
        elem: '#slideTest6'
        , step: 1 //步长
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#RightBottomNumber").val(value);
            $(this.elem).next("label")[0].innerText = "异常" + value + "次";
        }
    });

    slider.render({
        elem: '#slideTest7'
        , step: 10 //步长
        , showstep: true //开启间隔点
    });

    slider.render({
        elem: '#slideTest8'
        , step: 10 //步长
        , showstep: true //开启间隔点
    });

    slider.render({
        elem: '#slideTest9'
        , step: 10 //步长
        , showstep: true //开启间隔点
    });

    slider.render({
        elem: '#slideTest10'
        , step: 10 //步长
        , showstep: true //开启间隔点
    });

    //大数据配置 统计天数设置
    var slideTest11 = slider.render({
        elem: '#slideTest11',
        showstep: true, //开启间隔点
        change: function (value) {
            $("#TopRightWeek").val(value);
            $(this.elem).next("label")[0].innerText = "近" + value + "天";
        }
    });

    //大数据配置 公寓近30天异常人员 统计天数设置
    var slideTest12 = slider.render({
        elem: '#slideTest12',
        showstep: true, //开启间隔点
        change: function (value) {
            $("#CentreRightDays").val(value);
            $(this.elem).next("label")[0].innerText = "近" + value + "天";
        }
    });

    //大数据配置 次数设置
    var slideTest13 = slider.render({
        elem: '#slideTest13'
        , step: 1 //步长
        , showstep: true //开启间隔点
        , change: function (value) {
            $("#CentreRightNumber").val(value);
            $(this.elem).next("label")[0].innerText = "异常" + value + "次";
        }
    });
    //晚归时间选择器
    var NightReturnStartTime = laydate.render({
        elem: '#NightReturnStartTime'
        , type: 'time'
        , min: '00:00:00'
        , max: '23:59:59'
        , btns: ["clear", "confirm"]
        , done: function (value, date) {
            // setMinTime(value, NightReturnEndtTime, date);
        }

    });

    //晚归时间选择器
    var NightReturnEndtTime = laydate.render({
        elem: '#NightReturnEndtTime'
        , type: 'time'//时间类型
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            // SetMaxTime(value, NightReturnStartTime, date);
        }
    });

    //迟到时间选择器
    var BeLateStartTime = laydate.render({
        elem: '#BeLateStartTime'
        , type: 'time'
        , min: '00:00:00'
        , max: '23:59:59'
        , btns: ["clear", "confirm"]
        , value: dateDefault
        , done: function (value, date) {
            setMinTime(value, BeLateEndTime, date);
        }

    });

    //迟到时间选择器
    var BeLateEndTime = laydate.render({
        elem: '#BeLateEndTime'
        , type: 'time'//时间类型
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , value: nextDate
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            SetMaxTime(value, BeLateStartTime, date);
        }
    });

    //未到开始时间
    var NonStartTime = laydate.render({
        elem: '#NonStartTime'
        , type: 'time'
        , min: '00:00:00'
        , max: '23:59:59'
        , btns: ["clear", "confirm"]
        , value: dateDefault
        , done: function (value, date) {
            setMinTime(value, NonEndTime, date);
        }
    });

    //未到时间选择器
    var NonEndTime = laydate.render({
        elem: '#NonEndTime'
        , type: 'time'//时间类型
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , value: nextDate
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            SetMaxTime(value, NonStartTime, date);
        }
    });

    //早退开始时间
    var LeaveStartTiem = laydate.render({
        elem: '#LeaveStartTiem'
        , type: 'time'
        , min: '00:00:00'
        , max: '23:59:59'
        , btns: ["clear", "confirm"]
        , value: dateDefault
        , done: function (value, date) {
            setMinTime(value, LeaveEndTiem, date);
        }

    });

    //未到时间选择器
    var LeaveEndTiem = laydate.render({
        elem: '#LeaveEndTiem'
        , type: 'time'//时间类型
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , value: nextDate
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            SetMaxTime(value, LeaveStartTiem, date);
        }
    });

    //旷课开始时间
    var TruantStartTiem = laydate.render({
        elem: '#TruantStartTiem'
        , type: 'time'
        , min: '00:00:00'
        , max: '23:59:59'
        , btns: ["clear", "confirm"]
        , value: dateDefault
        , done: function (value, date) {
            setMinTime(value, TruantEndTiem, date);
        }

    });

    //旷课时间选择器
    var TruantEndTiem = laydate.render({
        elem: '#TruantEndTiem'
        , type: 'time'//时间类型
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , value: nextDate
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            SetMaxTime(value, TruantStartTiem, date);
        }
    });


    //未归时间选择器
    var NotReturnStartTime = laydate.render({
        elem: '#NotReturnStartTime'
        , type: 'time'
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            setMinTime(value, NotReturnEndTime, date);
        }
    });

    //未归时间选择器
    var NotReturnEndTime = laydate.render({
        elem: '#NotReturnEndTime'
        , type: 'time'
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            SetMaxTime(value, NotReturnStartTime, date);
        }
    });

    //无进出记录时间选择器
    var NoAccessRecordStartTiem = laydate.render({
        elem: '#NoAccessRecordStartTiem'
        , type: 'time'
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            setMinTime(value, NoAccessRecordEndTiem, date);
        }
    });

    //无进出记录时间选择器
    var NoAccessRecordEndTiem = laydate.render({
        elem: '#NoAccessRecordEndTiem'
        , type: 'time'
        , min: '00:00:00'//最大时间
        , max: '23:59:59'//最小时间
        , btns: ["clear", "confirm"]//时间控件按钮
        , done: function (value, date) {
            SetMaxTime(value, NoAccessRecordStartTiem, date);
        }
    });

    //设置最小时间
    function setMinTime(val, obj, date) {
        if (val !== "") {
            obj.config.min.hours = date.hours;
            obj.config.min.minutes = date.minutes;
            obj.config.min.seconds = date.seconds + 1;
        } else {
            obj.config.min.hours = 0;
            obj.config.min.minutes = 0;
            obj.config.min.seconds = 0;
        }
    }

    //设置最大时间
    function SetMaxTime(val, obj, date) {
        if (val !== "") {
            obj.config.max.hours = date.hours;
            obj.config.max.minutes = date.minutes;
            obj.config.max.seconds = date.seconds - 1;
        } else {
            obj.config.max.hours = 0;
            obj.config.max.minutes = 0;
            obj.config.max.seconds = 0;
        }
    }

    function DateCompare(time1, time2) {
        var Time = new Date();
        var start = time1.split(":");
        var end = time2.split(":");
        var startTime = Date.UTC(Time.getYear(), Time.getMonth(), Time.getDate(), start[0], start[1], start[2]);
        var endTime = Date.UTC(Time.getYear(), Time.getMonth(), Time.getDate(), end[0], end[1], end[2]);
        if (endTime < startTime) {
            layer.alert('提示', "截止时间不能大于起始时间");
            return false;
        }
        return true;
    }

    function DateTimeTransform(time) {
        var date = new Date();
        var arr = time.split(":");
        var dateTime = {
            year: date.getFullYear(),
            month: date.getMonth(),
            date: date.getDate(),
            hours: +arr[0],
            minutes: +arr[1],
            seconds: +arr[2]
        }
        return dateTime;
    }

    //配置项数据
    function GetConfigData(ConfigKey) {
        var ulr = "/BasicConfiguration/GetConfigByKey";
        common.AjaxPost(ulr, { ConfigKey: ConfigKey }, function (reslut) {
            var resultData = JSON.parse(reslut);
            if (resultData.status === true) {
                if (resultData.data === null) {
                    return;
                }
                switch (resultData.data.CONFIGKEY) {
                    case "Sys_ApartManagerConfig"://公寓统计规则
                        ApartManagerConfig(resultData.data);
                        break;
                    case "Sys_BigDataConfig"://大数据配置
                        BigDataConfig(resultData.data);
                        break;
                    case "Sys_BasicConfig"://基础配置
                        BasicConfig(resultData.data);
                        break;
                    case "Sys_StatisticalRulesConfig"://公用规则
                        StatisticalRulesConfig(resultData.data);
                        break;
                    case "Sys_StudentConfig"://学生考勤规则
                        StudentConfig(resultData.data);
                        break;
                    case "Sys_DeviceDriverConfig"://设备驱动配置
                        //default:
                        DeviceDriverConfig(resultData.data);
                        break;
                    case "Sys_VisAppointmentConfig"://访客预约配置
                        VisitorApponitConfig(resultData.data);
                        break;
                    case "Sys_FilePathConfig"://文件路径配置
                        FilePathConfig(resultData.data);
                        break;

                    case "Sys_MonitorVideoConfig"://视频风格
                        MonitorVideoConfig(resultData.data);
                        break;

                }
                form.render();
            } else {
                layer.alert('错误', resultData.msg);
            }
        });
    }

    //基本配置
    function BasicConfig(data) {
        var Basic = JSON.parse(data.CONFIGVALUE);
        $("#BasicConfigKey").val(data.CONFIGKEY);
        $("#BasicConfigID").val(data.CONFIGID);
        $("#SystemName").val(Basic.SystemName);
        $("#SystemIntroduce").val(Basic.SystemIntroduce);
        $("#SystemMenuIsHide").attr("checked", !Basic.SystemMenuIsHide);
        $("#IsAlarm").attr("checked", Basic.IsAlarm);
        $("#OpenDebugLog").attr("checked", Basic.OpenDebugLog);
        $("#IsShowLabel").attr("checked", Basic.IsShowLabel);
        $("#MultiTabs").attr("checked", Basic.MultiTabs);

        $("#Skin").val(Basic.Skin);
        $("#RightsReserv").val(Basic.RightsReserv);
        $("#DefaultPage").val(Basic.DefaultPage);
        $("#SysVersions").val(Basic.SysVersions);//系统版本

    }

    //大数据配置
    function BigDataConfig(data) {
        var BigData = JSON.parse(data.CONFIGVALUE);
        $("#BigDataConfigKey").val(data.CONFIGKEY);
        $("#BigDataConfigID").val(data.CONFIGID);
        $("#TopLeftTitle").val(BigData.TopLeftTitle);
        $("#block_TopLeftTitle").text(BigData.TopLeftTitle);
        for (var i = 0; i < BigData.TopLeftSubtitle.length; i++) {
            $("#TopLeftSubtitle").append(JoinHtml(BigData.TopLeftSubtitle[i], i));
        }
        $("#BottomTitle").val(BigData.BottomTitle);
        $("#block_BottomTitle").text(BigData.BottomTitle);
        slideTest1.setValue(BigData.BottomTime);
        $("#TopRightTitle").val(BigData.TopRightTitle);
        $("#block_TopRightTitle").text(BigData.TopRightTitle);
        slideTest11.setValue(BigData.TopRightWeek);
        $("#CentreRightTitle").val(BigData.CentreRightTitle);
        $("#block_CentreRightTitle").text(BigData.CentreRightTitle);
        slideTest12.setValue(BigData.CentreRightDays);
        slideTest13.setValue(BigData.CentreRightNumber);
        var inputs = $("#CentreRightTabtitle .layui-input-inline input");
        //for (var i = 0; i < BigData.CentreRightTabtitle.length; i++) {
        //    inputs[i].value = BigData.CentreRightTabtitle[i];
        //}
        $("#TopLeftSubtitleAdd").click(function () {
            if ($("#TopLeftSubtitle .layui-form-item").length > 6) {
                $.messager.alert('提示', "内容最大只能添加6个");
                return;
            }
            $("#TopLeftSubtitle").append(JoinHtml({ Title: "", Explain: "" }, 1));
            InputEvent();
        });
        InputEvent();
    }

    ///注册删除HMTL事件
    function InputEvent() {
        $(".layui_close[name=TopLeftSubtitleClose]").click(function () {
            $(this).parents(".layui-form-item").remove();
        })
        $("#TopLeftSubtitle input").change(function () {
            var value = $(this).val();
            if (value.length > 5) {
                layer.alert('输入内容限制5五个字');
                $(this).val(value.substring(0, 5));
            }
        })
    }

    ///拼接HTML
    function JoinHtml(data, add) {
        var div = ' <div class="layui-form-item">' +
            '<div class="layui-inline">' +
            '<label class="layui-form-label">内容标题：</label>' +
            '<div class="layui-input-inline">' +
            '<input type="text" name="Title" value="' + data.Title + '" class="layui-input">' +
            '</div>' +
            '</div>' +
            '<div class="layui-inline">' +
            '<label class="layui-form-label">内容说明：</label>' +
            '<div class="layui-input-inline">' +
            '<input type="text" name="Explain" value="' + data.Explain + '" class="layui-input">' +
            '</div>' +
            '</div>';
        if (add === 0) {
            div += '<div class="layui-inline">' +
                '<label class="layui-form-label layui_add" id="TopLeftSubtitleAdd" >+</label>' +
                '</div>';
            $("#TopLeftSubtitle").html("");
        } else {
            div += '<div class="layui-inline">' +
                '<label class="layui-form-label layui_close" name="TopLeftSubtitleClose" >x</label>' +
                '</div>';
        }
        div += '</div>';
        return div;
    }

    //统计规则
    function StatisticalRulesConfig(data) {
        var StatisticalRules = JSON.parse(data.CONFIGVALUE);
        $("#StatisticalRulesConfigKey").val(data.CONFIGKEY);
        $("#StatisticalRulesConfigID").val(data.CONFIGID);
        slideTest2.setValue(StatisticalRules.AlarmMessageNumber);
        slideTest14.setValue(StatisticalRules.AlarmMessageDay);
        $("#LeftBottomTItle").val(StatisticalRules.LeftBottomTItle);
        $("#block_LeftBottomTItle").text(StatisticalRules.LeftBottomTItle);
        slideTest3.setValue(StatisticalRules.LeftBottomWeek);
        $("#LeftCentrTitle").val(StatisticalRules.LeftCentrTitle);
        $("#block_LeftCentrTitle").text(StatisticalRules.LeftCentrTitle);
        slideTest4.setValue(StatisticalRules.LeftCentrDays);
        $("#RightBottomTitle").val(StatisticalRules.RightBottomTitle);
        $("#block_RightBottomTitle").text(StatisticalRules.RightBottomTitle);
        slideTest5.setValue(StatisticalRules.RightBottomDays);
        slideTest6.setValue(StatisticalRules.RightBottomNumber);
        //var inputs = $("#RightBottomTabtitle input");
        //for (var i = 0; i < StatisticalRules.RightBottomTabtitle.length; i++) {
        //    inputs[i].value = StatisticalRules.RightBottomTabtitle[i];
        //}

    }

    //公寓数据计算规则
    function ApartManagerConfig(data) {
        $("#Subtitle").html("");
        var ApartManager = JSON.parse(data.CONFIGVALUE);
        $("#ApartManagerConfigID").val(data.CONFIGID);
        $("#ApartManagerConfigKey").val(data.CONFIGKEY);
        $("#NightReturnStartTime").val(ApartManager.NightReturnStartTime);
        $("#NightReturnEndtTime").val(ApartManager.NightReturnEndtTime);
        $("#NotReturnStartTime").val(ApartManager.NotReturnStartTime);
        $("#NotReturnEndTime").val(ApartManager.NotReturnEndTime);
        $("#NoAccessRecordStartTiem").val(ApartManager.NoAccessRecordStartTiem);
        $("#NoAccessRecordEndTiem").val(ApartManager.NoAccessRecordEndTiem);
        $("#DataStatisticsHelp").val(ApartManager.DataStatisticsHelp);

        $("#NightEveryDay").attr("checked", ApartManager.NightEveryDay);//晚归是否隔天
        $("#NotEveryDay").attr("checked", ApartManager.NotEveryDay);//未归是否隔天
        $("#NoEveryDay").attr("checked", ApartManager.NoEveryDay);//无进出记录是否隔天
        for (var i = 0; i < ApartManager.Subtitle.length; i++) {
            SubtitleAdd(ApartManager.Subtitle[i].Title, ApartManager.Subtitle[i].Explain);
        }
        //晚归时间
        NightReturnStartTime.config.done(ApartManager.NightReturnStartTime, DateTimeTransform(ApartManager.NightReturnStartTime));//时间控制
        NightReturnEndtTime.config.done(ApartManager.NightReturnEndtTime, DateTimeTransform(ApartManager.NightReturnEndtTime));
        //未归时间
        NotReturnStartTime.config.done(ApartManager.NotReturnStartTime, DateTimeTransform(ApartManager.NotReturnStartTime));
        NotReturnEndTime.config.done(ApartManager.NotReturnEndTime, DateTimeTransform(ApartManager.NotReturnEndTime));
        //无进出记录时间
        NoAccessRecordStartTiem.config.done(ApartManager.NoAccessRecordStartTiem, DateTimeTransform(ApartManager.NoAccessRecordStartTiem));
        NoAccessRecordEndTiem.config.done(ApartManager.NoAccessRecordEndTiem, DateTimeTransform(ApartManager.NoAccessRecordEndTiem));
    }

    //学生统计规则
    function StudentConfig(data) {
        //隐藏存储值的ipunt文本框
        $('#StudentiRulesConfigID').val(data.CONFIGID);
        $('#StudentRulesConfigKey').val(data.CONFIGKEY);
        //判断是否有值
        if (data.CONFIGVALUE != "") {
            var ApartManager = JSON.parse(data.CONFIGVALUE);
            //迟到时间
            $('#BeLateStartTime').val(ApartManager.BeLate.BeLateStartTime);
            $('#BeLateEndTime').val(ApartManager.BeLate.BeLateEndTime);
            //早退时间
            $("#NonStartTime").val(ApartManager.NonArrival.NonStartTime);
            $("#NonEndTime").val(ApartManager.NonArrival.NonEndTime);
            //早退时间
            $("#LeaveStartTiem").val(ApartManager.LeaveEarly.LeaveStartTime);
            $("#LeaveEndTiem").val(ApartManager.LeaveEarly.LeaveEndTime);
            //旷课时间
            $("#TruantStartTiem").val(ApartManager.Truant.TruantStartTime);
            $("#TruantEndTiem").val(ApartManager.Truant.TruantEndTime);
        }
    }

    //设备驱动配置
    function DeviceDriverConfig(data) {
        $('#DeviceDriverConfigID').val(data.CONFIGID);
        $('#DeviceDriverConfigKey').val(data.CONFIGKEY);
        if (data.CONFIGVALUE != "") {
            var ApartManager = JSON.parse(data.CONFIGVALUE);
            $("#cameraManuName").val(ApartManager.Camera.CameraManuName),//相机厂商名称
                $("#cameraTerraceName").val(ApartManager.Camera.CameraTerraceName),//相机平台名称
                $("#cameraTerraIP").val(ApartManager.Camera.CameraTerraIP),//相机平台ip
                $("#cameraUserName").val(ApartManager.Camera.CameraUserName),//相机用户名
                $("#cameraLoginPwd").val(ApartManager.Camera.CameraLoginPwd),//相机登录密码
                $("#cameraDecoderModel").val(ApartManager.Camera.CameraDecoderModel),//相机解码型号

                $("#gateManuName").val(ApartManager.Gate.GateManuName),//人脸闸机厂商名称
                $("#gateTerraceName").val(ApartManager.Gate.GateTerraceName),//人脸闸机平台名称
                $("#gateTerraIP").val(ApartManager.Gate.GateTerraIP),//人脸闸机平台ip
                $("#gateUserName").val(ApartManager.Gate.GateUserName),//人脸闸机用户名
                $("#gateLoginPwd").val(ApartManager.Gate.GateLoginPwd),//人脸闸机登录密码
                $("#gateDecoderModel").val(ApartManager.Gate.GateDecoderModel)//人脸闸机解码型号
        }
    }

    //访客预约配置
    function VisitorApponitConfig(data) {
        $('#VisAppointmentConfigID').val(data.CONFIGID);
        $('#VisAppointmentConfigKey').val(data.CONFIGKEY);
        if (data.CONFIGVALUE != "" && data.CONFIGVALUE != null) {
            var ApartManager = JSON.parse(data.CONFIGVALUE);
            $("#IsAudit").attr("checked", ApartManager.IsAudit);
            $("#isIssue").attr("checked", ApartManager.isIssue);
            $("#serverID").val(ApartManager.serverID);
            $("#maxAppointNumber").val(ApartManager.maxAppointNumber);
        }

    }
    //文件路径配置
    function MonitorVideoConfig(data) {
        $('#MonitorVideoConfigID').val(data.CONFIGID);
        $('#MonitorVideoConfigKey').val(data.CONFIGKEY);
        if (data.CONFIGVALUE != "" && data.CONFIGVALUE != null) {
            var result = JSON.parse(data.CONFIGVALUE);
            //$("#filePath").val(result.filePath);
            var obj = document.getElementsByName("checkboxVideo");
            for (var i = 0; i < obj.length; i++) {
                if (obj[i].value == result.videoStyle) {
                    obj[i].checked = true;
                    form.render('checkbox');
                    break;
                }
            }
        }
    }

    //文件路径配置
    function FilePathConfig(data) {
        $('#FilePathConfigID').val(data.CONFIGID);
        $('#FilePathConfigKey').val(data.CONFIGKEY);
        if (data.CONFIGVALUE != "" && data.CONFIGVALUE != null) {
            var result = JSON.parse(data.CONFIGVALUE);
            $("#filePath").val(result.filePath);
        }
    }

    //保存提交方法
    function SaveConfig(data) {
        var ulr = "/BasicConfiguration/UpdateBasicConfiguration";
        common.AjaxPost(ulr, data, function (reslut) {
            var resultData = JSON.parse(reslut);
            if (resultData.status === true) {
                layer.alert('提示：' + resultData.msg);
                GetConfigData(data.CONFIGKEY);
                // skinPeeling();
            } else {
                layer.alert('错误', resultData.msg);
            }
        });
    }

    //保存基础配置
    function SaveBasicConfig() {
        var key = $("#BasicConfigKey").val();
        var islable = $("#IsShowLabel")[0].checked;
        var muTabs = $("#MultiTabs")[0].checked;
        if (!islable) {
            muTabs = false
        }
        var data = {
            CONFIGKEY: key,
            SystemName: $("#SystemName").val(),//园区名称
            SystemIntroduce: $("#SystemIntroduce").val(),//园区介绍
            SystemMenuIsHide: !$("#SystemMenuIsHide")[0].checked,//功能菜单是否隐藏
            IsAlarm: $("#IsAlarm")[0].checked,//黑名单告警
            OpenDebugLog: $("#OpenDebugLog")[0].checked,//开启调试日志
            IsShowLabel: islable,//是否显示导航菜单
            Skin: $("#Skin").val(),//皮肤
            RightsReserv: $("#RightsReserv").val(),//版权
            MultiTabs: muTabs,
            DefaultPage: $("#DefaultPage").val(),//首页默认地址
            SysVersions: $("#SysVersions").val()//系统版本
        };
        var sys_configuration = {};
        sys_configuration.CONFIGID = $("#BasicConfigID").val();
        sys_configuration.CONFIGVALUE = JSON.stringify(data);
        sys_configuration.CONFIGKEY = key;
        SaveConfig(sys_configuration);
    }


    function ComboboxBinding() {
        //厂家绑定
        common.AjaxPostAsync("/System/SysCodeManager/GetCodeList",
            { TYPEKEY: "SYS-MANUFACTURERS-TYPE" }, function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status === true) {
                    if (resultData.data != null) {
                        $.each(resultData.data, function (index, item) {
                            $('#cameraManuName').append(new Option(item.CODEVALUE, item.CODEKEY));// 下拉菜单里添加元素
                            $('#gateManuName').append(new Option(item.CODEVALUE, item.CODEKEY));// 下拉菜单里添加元素

                        });
                        layui.form.render("select");
                    }
                } else {
                    layer.alert('错误', resultData.msg);
                }
            }, false);
    }

    //保存大数据配置
    function SaveBigDataConfig() {
        var Titles = $("#TopLeftSubtitle .layui-form-item");
        var TopLeftSubtitle = [];
        for (var i = 0; i < Titles.length; i++) {
            var input = $(Titles[i]).find("input");
            TopLeftSubtitle.push({
                Title: input[0].value,
                Explain: input[1].value,
                ID: "Tltle_0" + i
            });
        }
        //var inputs = $("#CentreRightTabtitle .layui-input-inline input");
        //var CentreRightTabtitle = [];
        //for (var i = 0; i < inputs.length; i++) {
        //    CentreRightTabtitle.push(inputs[i].value);
        //}
        var Sys_BigDataConfig = {
            CONFIGKEY: $("#BigDataConfigKey").val(),
            TopLeftTitle: $("#TopLeftTitle").val(),
            TopLeftSubtitle: TopLeftSubtitle,
            BottomTitle: $("#BottomTitle").val(),
            BottomTime: $("#BottomTime").val(),
            TopRightTitle: $("#TopRightTitle").val(),
            TopRightWeek: $("#TopRightWeek").val(),
            CentreRightTitle: $("#CentreRightTitle").val(),
            CentreRightDays: $("#CentreRightDays").val(),
            CentreRightNumber: $("#CentreRightNumber").val()
            //   CentreRightTabtitle: CentreRightTabtitle
        };
        var data = {
            CONFIGID: $("#BigDataConfigID").val(),
            CONFIGVALUE: JSON.stringify(Sys_BigDataConfig),
            CONFIGKEY: Sys_BigDataConfig.CONFIGKEY
        };
        SaveConfig(data);
    }

    //保存统计规则配置
    function SaveStatisticalRulesConfig() {
        //var RightBottomTabtitle = [];
        //var inputs = $("#RightBottomTabtitle input");
        //for (var i = 0; i < inputs.length; i++) {
        //    RightBottomTabtitle.push(inputs[i].value);
        //}
        var Sys_StatisticalRulesConfig = {
            CONFIGKEY: $("#StatisticalRulesConfigKey").val(),
            Z: $("#AlarmMessageNumber").val(),
            AlarmMessageDay: $("#AlarmMessageDay").val(),
            LeftBottomTItle: $("#LeftBottomTItle").val(),
            LeftBottomWeek: $("#LeftBottomWeek").val(),
            LeftCentrTitle: $("#LeftCentrTitle").val(),
            LeftCentrDays: $("#LeftCentrDays").val(),
            RightBottomTitle: $("#RightBottomTitle").val(),
            RightBottomDays: $("#RightBottomDays").val(),
            RightBottomNumber: $("#RightBottomNumber").val()
            //  RightBottomTabtitle: RightBottomTabtitle
        }
        var data = {
            CONFIGID: $("#StatisticalRulesConfigID").val(),
            CONFIGVALUE: JSON.stringify(Sys_StatisticalRulesConfig),
            CONFIGKEY: Sys_StatisticalRulesConfig.CONFIGKEY
        };
        SaveConfig(data);
    }

    //设备驱动
    function SaveDeviceDriverConfig() {

        var CameraManuName = $("#cameraManuName").val();//相机厂商名称
        var CameraTerraceName = $("#cameraTerraceName").val();//相机平台名称
        var CameraTerraIP = $("#cameraTerraIP").val();//相机平台ip
        var CameraUserName = $("#cameraUserName").val();//相机用户名
        var CameraLoginPwd = $("#cameraLoginPwd").val();//相机登录密码
        var CameraDecoderModel = $("#cameraDecoderModel").val();//相机解码型号
        var Camera = { CameraManuName: CameraManuName, CameraTerraceName: CameraTerraceName, CameraTerraIP: CameraTerraIP, CameraUserName: CameraUserName, CameraLoginPwd: CameraLoginPwd, CameraDecoderModel: CameraDecoderModel }
        var GateTerraceName = $("#gateTerraceName").val();//人脸闸机平台名称
        var GateManuName = $("#gateManuName").val();//人脸闸机厂商名称
        var GateTerraIP = $("#gateTerraIP").val();//人脸闸机平台ip
        var GateUserName = $("#gateUserName").val();//人脸闸机用户名
        var GateLoginPwd = $("#gateLoginPwd").val();//人脸闸机登录密码
        var Gate = { GateTerraceName: GateTerraceName, GateManuName: GateManuName, GateTerraIP: GateTerraIP, GateUserName: GateUserName, GateLoginPwd: GateLoginPwd }
        var Sys_DeviceDriverConfig = {
            CONFIGKEY: $("#DeviceDriverConfigKey").val(),
            Camera: Camera,
            Gate: Gate
        }
        var data = {
            CONFIGID: $('#DeviceDriverConfigID').val(),
            CONFIGVALUE: JSON.stringify(Sys_DeviceDriverConfig),
            CONFIGKEY: Sys_DeviceDriverConfig.CONFIGKEY
        }
        SaveConfig(data);
    }

    //学生考勤规则
    function SaveStudentRulesConfig() {
        //迟到时间
        var BeLateStartTime = $('#BeLateStartTime').val();
        var BeLateEndTime = $('#BeLateEndTime').val();
        var BeLate = { BeLateStartTime: BeLateStartTime, BeLateEndTime: BeLateEndTime };
        //早退时间
        var NonStartTime = $("#NonStartTime").val();
        var NonEndTime = $("#NonEndTime").val();
        var NonArrival = { NonStartTime: NonStartTime, NonEndTime: NonEndTime };
        //早退时间
        var LeaveStartTime = $("#LeaveStartTiem").val();
        var LeaveEndTime = $("#LeaveEndTiem").val();
        var LeaveEarly = { LeaveStartTime: LeaveStartTime, LeaveEndTime: LeaveEndTime };
        //旷课时间
        TruantStartTime = $("#TruantStartTiem").val();
        TruantEndTime = $("#TruantEndTiem").val();
        var Truant = { TruantStartTime: TruantStartTime, TruantEndTime: TruantEndTime };
        var Sys_StudentConfig = {
            CONFIGKEY: $('#StudentRulesConfigKey').val(),
            //迟到时间
            BeLate: BeLate,
            //未到时间
            NonArrival: NonArrival,
            //早退时间
            LeaveEarly: LeaveEarly,
            //旷课时间
            Truant: Truant
        }
        var data = {
            CONFIGID: $('#StudentiRulesConfigID').val(),
            CONFIGVALUE: JSON.stringify(Sys_StudentConfig),
            CONFIGKEY: Sys_StudentConfig.CONFIGKEY
        }
        SaveConfig(data);
    }

    //公寓管理统计配置
    function SaveApartManagerConfig() {
        var div = $("#Subtitle").find(".layui-colla-item");
        var Subtitle = [];
        for (var i = 0; i < div.length; i++) {
            var pre = $(div[i]).find("pre");
            Subtitle.push({
                ID: "pre_" + i,
                Title: pre[0].innerText,
                Explain: pre[1].innerText
            })
        }

        var Sys_ApartManagerConfig = {
            CONFIGKEY: $("#ApartManagerConfigKey").val(),
            NightReturnStartTime: $("#NightReturnStartTime").val(),
            NightReturnEndtTime: $("#NightReturnEndtTime").val(),
            NotReturnStartTime: $("#NotReturnStartTime").val(),
            NotReturnEndTime: $("#NotReturnEndTime").val(),
            NoAccessRecordStartTiem: $("#NoAccessRecordStartTiem").val(),
            NoAccessRecordEndTiem: $("#NoAccessRecordEndTiem").val(),
            DataStatisticsHelp: $("#DataStatisticsHelp").val(),
            Subtitle: Subtitle,
            NightEveryDay: $("#NightEveryDay")[0].checked,//晚归是否隔天
            NotEveryDay: $("#NotEveryDay")[0].checked,//未归是否隔天
            NoEveryDay: $("#NoEveryDay")[0].checked//无进出记录是否隔天
        }
        var data = {
            CONFIGID: $("#ApartManagerConfigID").val(),
            CONFIGVALUE: JSON.stringify(Sys_ApartManagerConfig),
            CONFIGKEY: Sys_ApartManagerConfig.CONFIGKEY
        };
        SaveConfig(data);
    }

    //访客预约配置
    function SaveVisitorConfig() {
        var Sys_VisAppointmentConfig = {
            CONFIGKEY: $("#VisAppointmentConfigKey").val(),
            IsAudit: $("#IsAudit")[0].checked,
            isIssue: $("#isIssue")[0].checked,
            serverID: $("#serverID").val(),
            maxAppointNumber: $("#maxAppointNumber").val()
        }
        var data = {
            CONFIGID: $("#VisAppointmentConfigID").val(),
            CONFIGVALUE: JSON.stringify(Sys_VisAppointmentConfig),
            CONFIGKEY: Sys_VisAppointmentConfig.CONFIGKEY
        }
        SaveConfig(data);
    }

    //文件路径保存
    function SavefilePathConfig() {
        var Sys_FilePathConfig = {
            CONFIGKEY: $("#FilePathConfigKey").val(),
            filePath: $("#filePath").val()
        }
        var data = {
            CONFIGID: $("#FilePathConfigID").val(),
            CONFIGVALUE: JSON.stringify(Sys_FilePathConfig),
            CONFIGKEY: Sys_FilePathConfig.CONFIGKEY
        }
        SaveConfig(data);
    }

    //保存视频风格配置
    function SaveMonitorVideoConfig() {
        var obj = document.getElementsByName("checkboxVideo");
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) {
                var videoStyle = obj[i].value;
                break;
            }
        }
        var Sys_MonitorVideoConfig = {
            CONFIGKEY: $("#MonitorVideoConfigKey").val(),
            videoStyle: videoStyle
        }
        var data = {
            CONFIGID: $("#MonitorVideoConfigID").val(),
            CONFIGVALUE: JSON.stringify(Sys_MonitorVideoConfig),
            CONFIGKEY: Sys_MonitorVideoConfig.CONFIGKEY
        }
        SaveConfig(data);
    }

    //添加标题说明
    function SubtitleAdd(title, content) {
        var div = '<div class="layui-form-item">' +
            '<div class="layui-inline" style="width:93%">' +
            ' <div class="layui-collapse" lay-filter="test">' +
            '<div class="layui-colla-item">' +
            '<h2 class="layui-colla-title"><pre>' + title + '</pre></h2>' +
            '<div class="layui-colla-content layui-show">' +
            ' <pre>' + content + '</pre> </div></div> </div></div>' +
            '<div class="layui-inline">' +
            ' <label class="layui-form-label layui_close" name="Subtitle_close">x</label></div></div>';
        $("#Subtitle").append(div);
        element.init();//重新初始化
        $(".layui_close[name=Subtitle_close]").click(function () {
            $(this).parent().parent().remove();
        })

    }

    //服务器下拉框绑定
    function ComboboxDtive() {
        common.AjaxPostAsync("/System/EngIneManager/GetFaceServerList",
            null, function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status == true) {
                    $.each(resultData.data, function (key, val) {
                        var option1 = $("<option>").val(val.SERVERID).text(val.SERVERNAME);
                        $("#serverID").append(option1);
                        form.render('select');
                    });

                } else {
                    $.messager.alert('错误', resultData.msg);
                }
            }, false);
    }


    function InitEvent() {
        //视频风格
        $("#SaveMonitorVideoConfig").click(function () {
            SaveMonitorVideoConfig();
        })
        $("#SaveBasicConfig").click(function () {
            SaveBasicConfig();
        })
        $(".hd li").click(function () {
            GetConfigData(this.id);
        });
        $("#SaveBigDataConfig").click(function () {
            SaveBigDataConfig();
        })
        $("#SaveStatisticalRulesConfig").click(function () {
            SaveStatisticalRulesConfig();
        })
        $("#SubtitleAdd").click(function () {
            SubtitleAdd($("#SubtitleTitle").val(), $("#SubtitleExplain").val());
            $("#SubtitleTitle").val("");
            $("#SubtitleExplain").val("");
        });
        $("#SaveApartManagerConfig").click(function () {

            SaveApartManagerConfig();
        })
        //学生考勤规则
        $("#SaveStudentRulesConfig").click(function () {
            SaveStudentRulesConfig();
        })

        //设备驱动配置
        $("#SaveDeviceDriverConfig").click(function () {
            SaveDeviceDriverConfig();
        })
        //保存访客配置信息
        $("#SaveVisitorConfig").click(function () {

            SaveVisitorConfig();

        })
        //文件路径配置保存
        $("#SavefilePathConfig").click(function () {
            SavefilePathConfig();
        })
    }

    ///加载三级模块菜单
    function loadModuleData() {
        var param = {
            ModuleType: "3"
        }
        var showBase = { PremissionFilter: true, ShowBaseNode: true, ShowDisable: false, Sort: "SortNo", OrderBy: 1 }; //显示根节点，显示禁用的模块
        var mergeData = common.MergeJsonObject(showBase, param);
        common.AjaxPostAsync("/ModuleManager/GetModuleTreeList", mergeData,
            function (result) {
                var resultData = JSON.parse(result);
                if (resultData.status) {
                    var liList = $("#ulli li");
                    if (resultData.data.length == 1)// 默认显示基础配置
                    {
                        $("#Sys_BasicConfig").css('display', 'block');
                    } else {
                        for (var i = 0; i < resultData.data.length; i++) {
                            for (var z = 0; z < liList.length; z++) {
                                if (resultData.data[i].name == liList[z].innerText) {
                                    var $this = liList[z];
                                    $($this).css('display', 'block');
                                }
                            }
                        }
                    }
                } else {
                    $.messager.alert("提示", resultData.msg);
                }
            }, false);
    }


    var Init = function () {
        ComboboxDtive();
        ComboboxBinding();
        //根据权限显示登录角色下的三级菜单
        loadModuleData();
        //默认加载基础配置
        GetConfigData("Sys_BasicConfig");
        InitEvent();
        $(".split_screen_box").click(function () {
            var obj = $(this).children("input");
            if (obj.is(":checked")) {
                // 先把所有的checkbox 都设置为不选种
                $("input[name='checkboxVideo']").prop('checked', false);
                // 把自己设置为选中
                obj.prop('checked', true);
            }
            form.render('checkbox');
        })
    }
    // Init();//调用初始化
});
//默认系统时间