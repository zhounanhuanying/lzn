var noticenum;
var noticedate;
var noticemanager = (function () {
    var notice = {};
    notice.GetAlarmNumber = function () {
        common.AjaxPost("/System/BasicConfiguration/GetStatisticalRulesConfig", {}, function (result) {
            var resultData = JSON.parse(result);
            if (resultData.status) {
                if (resultData.data != null) {
                    $(".laternum").html(resultData.data.AlarmMessageNumber);
                    $(".noreturnnum").html(resultData.data.AlarmMessageNumber);
                    $(".norecordnum").html(resultData.data.AlarmMessageNumber);
                    $(".noticedate").text(resultData.data.AlarmMessageDay);
                    noticenum = resultData.data.AlarmMessageNumber;
                    noticedate = resultData.data.AlarmMessageDay;
                    //通知异常人数
                    notice.GetNoticeCount();
                }
            }
        })
    }
    //通知异常人数
    notice.GetNoticeCount = function () {
        var query = {
            NoticeNum: noticenum,
            NoticeDate:noticedate
        };
        common.AjaxPost("/Apartment/Apartment/GetNoticeCount",
         query, function (result) {
             var resultData = JSON.parse(result);
             if (resultData.status) {
                 if (resultData.data != null) {
                     for (var i = 0; i < resultData.data.length; i++) {
                         if (resultData.data[i].RECORDTYPE == 1) {
                             $(".later").html(resultData.data[i].PERSONSUM);
                         }
                         if (resultData.data[i].RECORDTYPE == 2) {
                             $(".noreturn").html(resultData.data[i].PERSONSUM);
                         }
                         if (resultData.data[i].RECORDTYPE == 3) {
                             $(".norecord").html(resultData.data[i].PERSONSUM);
                         }
                         //var html = "";
                         //html += '<div class="prompt_info clearfix" data_type=' + resultData.data[i].RECORDTYPE + '><div class="tz_title">公寓通知</div>'
                         //    + '<a href="javascript:void(0);"><i class="fa fa-user icon_prompt label-danger"></i>'
                         //   + '近30天' + (resultData.data[i].RECORDTYPE == 1 ? "晚归" : (resultData.data[i].RECORDTYPE == 2 ? "未归" : "无进出记录")) + '次数大于<span>2</span>次的有<span>' + resultData.data[i].PERSONSUM + '</span>人，点击查看详细</a></div>';
                         //$("#Notice_list").append(html);
                     }
                 } 
                notice.initEvent();
             }
         });
    }
    //跳转异常人员列表页面
    notice.initEvent = function () {
        $(".prompt_info").click(function () {
            var recordcount = $(this).find("span").eq(1).text();
            var recorddate = $(this).find("span").eq(0).text();
            //console.info(recordcount);
            //var nowdate = new Date();
            ////获取系统当前时间
            //var y = nowdate.getFullYear();
            //var m = nowdate.getMonth()+1;
            //var d = nowdate.getDate();
            //var endtime = y + '-' + m + '-' + d;
            ////获取系统前一个月的时间
            //nowdate.setMonth(nowdate.getMonth() - 1);
            //var y = nowdate.getFullYear();
            //var m = nowdate.getMonth()+1;
            //var d = nowdate.getDate();
            //var starttime = y + '-' + m + '-' + d;
            var url = '/Apartment/Apartment/PersonAbnormalList?recordType=' + $(this).attr("data_type") + '&recordCount=' + recordcount + '&recorddate=' + recorddate;
            common.openIframe(url, "异常人员列表");
        })
    }
    return notice;
}());
$(function () {
    //获取配置数据中的异常次数
    noticemanager.GetAlarmNumber();
});