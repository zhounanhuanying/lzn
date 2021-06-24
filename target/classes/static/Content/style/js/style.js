function changLiSytle() {
    var winWidth=$(window).width();
    var winHeight=$(window).height();
    var attencepanelW=(winWidth-20);
    var attencepanelH=(winHeight-20);
    // 课表宽度、高度
    var schooltimetableW=(attencepanelW-145);
    $(".school-timetable").css({width:schooltimetableW});

    if(winWidth>1366){
        var schooltimetablecontentH=(attencepanelH-129);
        var liheightH=(schooltimetablecontentH-70)/6;
    }
    else{
        var schooltimetablecontentH=(attencepanelH-101);
    }
    $(".school-timetable-content").css({height:schooltimetablecontentH});
    $(".li-height").css({height:liheightH});
    $(".course-period").css({height:liheightH});
    $(".course-period>li:first-child").css("line-height",liheightH+"px");

    // var liwidthW=(schooltimetableW-170)/5;
    // $(".li-width").css("width",liwidthW);
}
window.onload=function(){
    var winWidth=$(window).width();
    var winHeight=$(window).height();
    var attencepanelW=(winWidth-20);
    var attencepanelH=(winHeight-20);
    $(".attence-panel").css({width:attencepanelW,height:attencepanelH});

    // 选择列高度
    var chooseulH=(winHeight-20);
    $(".choose-ul").css({height:chooseulH});


    // 考勤详情
    // var attencephotoH=(winHeight-272);
    // $(".attence-photo").css({height:attencephotoH});


    $(".layui-nav-child dd").click(function(){
        if($(this).hasClass("layui-this")){
            $(this).removeClass("layui-this");
            $(this).addClass("active").siblings().removeClass("active");
        }
        else{}
    })


    // 考勤详情

    // $(".signature button").click(function(){
    //     $(this).fadeOut();
    //     $(this).prev().fadeOut();
    //     $(".signature img").fadeIn(5000);
    // })





    // 滚动条
    setNiceScroll(".choose-ul");
    setNiceScroll(".school-timetable-content");



    // 班级排名
    //ClassRank();

    // 课程排名
    course();

    // 节次分布
    Sectiondistribution();

    // 年级考勤
    //Studentattendance();

    // 考勤异常统计
    //unusual();


    // 课堂专注度统计
    Concentrationdegree();
}
// $(function(){
// 	var winWidth=$(window).width();
// 	var winHeight=$(window).height();
// 	var attencepanelW=(winWidth-20);
// 	var attencepanelH=(winHeight-20);
// 	$(".attence-panel").css({width:attencepanelW,height:attencepanelH});
//
// 	// 选择列高度
// 	var chooseulH=(winHeight-20);
// 	$(".choose-ul").css({height:chooseulH});
//
//
// 	// 课表宽度、高度
// 	var schooltimetableW=(attencepanelW-165);
// 	$(".school-timetable").css({width:schooltimetableW});
//
// 	if(winWidth>1366){
//         var schooltimetablecontentH=(attencepanelH-76);
//         var liheightH=(schooltimetablecontentH-70)/6;
// 	}
//     else{
//         var schooltimetablecontentH=(attencepanelH-48);
// 	}
//     $(".school-timetable-content").css({height:schooltimetablecontentH});
// 	$(".li-height").css({height:liheightH});
// 	$(".course-period").css({height:liheightH});
// 	$(".course-period>li:first-child").css("line-height",liheightH+"px");
//
// 	var liwidthW=(schooltimetableW-100)/5;
// 	$(".li-width").css({width:liwidthW});
//
//
//
//
// 	// 考勤详情
// 	var attencephotoH=(winHeight-272);
// 	$(".attence-photo").css({height:attencephotoH});
//
//
// 	$(".layui-nav-child dd").click(function(){
// 		if($(this).hasClass("layui-this")){
// 			$(this).removeClass("layui-this");
// 			$(this).addClass("active").siblings().removeClass("active");
// 		}
// 		else{}
// 	})
//
//
// 	// 考勤详情
//
// 	$(".signature button").click(function(){
// 		$(this).fadeOut();
// 		$(this).prev().fadeOut();
// 		$(".signature img").fadeIn(5000);
// 	})
//
//
//
//
//
// 	// 滚动条
// 	setNiceScroll(".choose-ul");
//     setNiceScroll(".school-timetable-content");
//
//
//
// 	// 班级排名
// 	ClassRank();
//
// 	// 课程排名
// 	course();
//
// 	// 节次分布
// 	Sectiondistribution();
//
// 	// 年级考勤
// 	Studentattendance();
//
// 	// 考勤异常统计
// 	unusual();
//
//
// 	// 课堂专注度统计
// 	Concentrationdegree();
//
// })

// 滚动条
function setNiceScroll(selector) {
    $(selector).niceScroll({
        cursorcolor: "#ccc",
        cursoropacitymax: 1,
        touchbehavior: false,
        cursorwidth: "3px",
        cursorborder: "0",
        cursorborderradius: "5px",
        zindex: 10000

    })
}



// 班级排名
function ClassRank(grade,percent,array){
    var chart1 = echarts.init(document.getElementById('chart1'));
    //var array1=[1,1,1,1,1,1,1,1,1,1];
    //var classname=[123,621,521,306,405,203,201,103,102,101];
    //var percentage=[0.3,0.35,0.4,0.5,0.6,0.65,0.7,0.8,0.9,0.92];
    var classname=grade;
    var percentage=percent;
    var array1=array;
    //alert(JSON.stringify(classname));
    //alert(JSON.stringify(percentage));
    //alert(JSON.stringify(array1));
    option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
        },
        // legend: {
        //     data: ['进', '出'],
        //     top:10,
        //     right:10,
        //     textStyle:{    //图例文字的样式
        //      color:'#fff',
        //      fontSize:12,
        //      fontFamily:'微软雅黑'
        //  },
        //  itemWidth:15,
        // 	itemHeight:15
        // },
        grid: {
            left: '3%',
            right: '12%',
            bottom: '3%',
            top:'30',
            containLabel: true
        },
        xAxis:  {
            type: 'value',
            axisLabel: {
                show: false,
                textStyle: {
                    color: '#fff',
                    fontSize:16,
                }
            },
            axisLine:{
                show:false,
            },
            splitLine:{
                show:false,  //不显示分割线
            },
            axisTick: {
                show: false
            }
        },
        yAxis: {
            type: 'category',
            data: classname,
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#fff',
                    fontSize:"14",
                },
                interval: 0
            },
            axisLine:{
                show:false,
            },
            axisTick: {
                show: false
            },
            // min:0,
            // max:5,
            // splitNumber:8
        },
        series: [
            {
                name: '总人次',
                type: 'bar',
                barWidth : "60%",
                barGap: '-100%',
                // label: {
                //     normal: {
                //         textStyle: {
                //             color: '#fff',
                //             fontSize:16
                //         },
                //         show: true,
                //         position: 'right',
                //         formatter: '{c}'
                //     }
                // },
                //列总人数显示时使用
                // itemStyle: {
                //     normal: {
                //         // color: 'rgba(25, 63, 110, 0.54)',
                //         color: '#fc3028',
                //         // borderWidth: 1,
                //         borderWidth: 1,
                //         borderColor: 'rgba(25, 63, 110, 0.54)'
                //
                //     }
                // },
                // z: 1,
                // data: array1
            },
            {
                type: 'bar',
                barWidth : "60%",
                barGap: '-100%',
                stack: '总量',
                itemStyle:{
                    normal:{
                        color:{
                            show: true,
                            type: 'bar',
                            colorStops: [{
                                offset: 0,
                                // color: '#00FBFF' // 0% 处的颜色
                                color: '#fc3028' // 0% 处的颜色
                            }, {
                                offset: 1,
                                color: '#fc3028' // 100% 处的颜色
                                // color: '#39A7FC' // 100% 处的颜色
                            }],
                        }
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'right',
                        textStyle: {
                            color: '#fff', //百分比颜色
                        },
                        formatter: function(data) {
                            return (percentage[data.dataIndex] * 100).toFixed(1) + '%';
                        },
                    }
                },
                z:2,
                data: percentage
            }
        ]
    };
    chart1.setOption(option);
}


// 课程排名

function course(){
    var chart2 = echarts.init(document.getElementById('chart2'));
    var bigfonts = 12;
    var nsum = 800;
    var fontS = 12;
    var dataAxis = [];
    var radius = "70%";
    data = [{
        "color": "#fff",
        "text": "数学"
    },
        {
            "color": "#fff",
            "text": "计算机"
        },
        {
            "color": "#fff",
            "text": "体育"
        },
        {
            "color": "#fff",
            "text": "表演"
        },
        {
            "color": "#fff",
            "text": "机电"
        },
        {
            "color": "#fff",
            "text": "编程"
        },
        {
            "color": "#fff",
            "text": "会计"
        },
        {
            "color": "#fff",
            "text": "金融"
        },
        {
            "color": "#fff",
            "text": "英语"
        },
        {
            "color": "#fff",
            "text": "语文"
        }
    ]


    option = {
        radar: [{
            indicator: data,
            radius: radius,
            startAngle: 60,
            splitNumber: 4,
            shape: 'circle',

            splitArea: {
                areaStyle: {
                    color: 'rgba(6,222,249,0.06)',
                    shadowBlur: 10
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#0095B0'
                }
            },
            splitLine: {
                lineStyle: {
                    color: '#0095B0'
                }
            }
        },

        ],
        series: [{
            name: '雷达图',
            type: 'radar',
            symbolSize: 0,
            areaStyle: {
                normal: {
                    color: 'rgba(6, 222, 249,0.3)',
                },

                emphasis: {
                    color: 'rgba(6, 222, 249,0.5)',
                }
            },
            lineStyle: {
                normal: {
                    color: '#f9d400',
                    type: 'solid',
                    width: 1
                },
                emphasis: {}
            },
            data: [{
                value: [350,130,140,150,130,115,125,105,85,300],
                label: {
                    show: 'true'
                }
            }]
        }, ]
    }
    chart2.setOption(option);
}


// 节次分布

function Sectiondistribution(){
    var chart3 = echarts.init(document.getElementById('chart3'));
    var colorList = ["#FFD951", "#FE292D", "#4BC4F9", "#F0924A", "#3165F0"];
    option = {
        tooltip: {
            trigger: 'item',
            formatter: "{b}: {c} ({d}%)"
        },
        grid: {
            top: 60,
            // containLabel: true
        },
        series: [{
            type: 'pie',
            radius: ['55%', '70%'],
            // roseType: 'radius',
            clockwise: false,
            z: 5,
            itemStyle: {
                normal: {
                    color: function(params) {
                        // build a color map as your need.
                        return colorList[params.dataIndex]
                    },
                    shadowBlur: 20,
                    shadowColor: 'rgba(0, 0, 0, 0.3)'
                }
            },
            label: {
                normal: {
                    formatter: '{d|{b}}\n{hr|}\n{c|{c}人}',
                    rich: {
                        b: {
                            fontSize: 12,
                            color: '#fff',
                            align: 'left',
                            padding: 4
                        },
                        hr: {
                            //		                            borderColor: '#12EABE',
                            width: '100%',
                            borderWidth: 1,
                            height: 0
                        },
                        d: {
                            fontSize: 12,
                            color: '#fff',
                            align: 'left',
                            padding: 1
                        },
                        c: {
                            fontSize: 12,
                            color: '#fff',
                            align: 'center',
                            //		                            padding: 4
                        }
                    }
                }
            },

            labelLine: {
                normal: {
                    length: 22,
                    length2: 0,
                    lineStyle: {
                        width: 2
                    }
                }
            },
            data: [{
                value: 50,
                name: '第一节课'
            },
                {
                    value: 50,
                    name: '第三节课'
                },
                {
                    value: 50,
                    name: '第二节课'
                },
                {
                    value: 40,
                    name: '第六节课'
                },
                {
                    value: 30,
                    name: '第七节课'
                }
            ]
        }]
    };
    chart3.setOption(option);
}


// 年级考勤

function Studentattendance(d){
    var chart4 = echarts.init(document.getElementById('chart4'));
    // data = [{
    //     name: "高一",
    //     value: 500
    // },
    // {
    //     name: "高二",
    //     value: 20
    // },
    // {
    //     name: "高三",
    //     value: 40
    // }];
    var data=d;
    //alert(JSON.stringify(data));
    arrName = getArrayValue(data, "name");
    arrValue = getArrayValue(data, "value");
    sumValue = eval(arrValue.join('+'));
    objData = array2obj(data, "name");
    optionData = getData(data);
    function getArrayValue(array, key) {
        var key = key || "value";
        var res = [];
        if (array) {
            array.forEach(function(t) {
                res.push(t[key]);
            });
        }
        return res;
    }

    function array2obj(array,key) {
        var resObj = {};
        for(var i=0;i<array.length;i++){
            resObj[array[i][key]] = array[i];
        }
        return resObj;
    }

    function getData(data) {
        var res = {
            series: [],
            yAxis: []
        };
        for (var i = 0; i < data.length; i++) {
            res.series.push({
                name: '',
                type: 'pie',
                clockWise: false, //顺时加载
                hoverAnimation: false, //鼠标移入变大
                radius: [80 - i * 30 + '%', 68 - i * 30 + '%'],
                center: ["30%", "55%"],
                label: {
                    show: false
                },
                itemStyle: {
                    label: {
                        show: false,
                    },
                    labelLine: {
                        show: false
                    },
                    borderWidth: 5,
                },
                data: [{
                    value: data[i].value,
                    name: data[i].name
                }, {
                    value: sumValue - data[i].value,
                    name: '',
                    itemStyle: {
                        color: "rgba(0,0,0,0)",
                        borderWidth: 0
                    },
                    tooltip: {
                        show: false
                    },
                    hoverAnimation: false
                }]
            });
            res.series.push({
                name: '',
                type: 'pie',
                silent: true,
                z: 1,
                clockWise: false, //顺时加载
                hoverAnimation: false, //鼠标移入变大
                radius: [80 - i * 30 + '%', 68 - i * 30 + '%'],
                center: ["30%", "55%"],
                label: {
                    show: false
                },
                itemStyle: {
                    label: {
                        show: false,
                    },
                    labelLine: {
                        show: false
                    },
                    borderWidth: 5,
                },
                data: [{
                    value: 7.5,
                    itemStyle: {
                        color: "#244499",
                        borderWidth: 0
                    },
                    tooltip: {
                        show: false
                    },
                    hoverAnimation: false
                }, {
                    value: 2.5,
                    name: '',
                    itemStyle: {
                        color: "rgba(0,0,0,0)",
                        borderWidth: 0
                    },
                    tooltip: {
                        show: false
                    },
                    hoverAnimation: false
                }]
            });
            res.yAxis.push(data[i].name+"："+data[i].value+ "人次");
        }
        return res;
    }

    option = {
        color: ['#FFC300', '#00E474', '#009CFF'],
        grid: {
            top: '10%',
            bottom: '48%',
            left: "30%",
            containLabel: false
        },
        yAxis: [{
            type: 'category',
            inverse: true,
            axisLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                interval: 0,
                inside: true,
                textStyle: {
                    color: "#fff",
                    fontSize: 14,
                },
                show: true
            },
            data: optionData.yAxis
        }],
        xAxis: [{
            show: false
        }],
        series: optionData.series
    };
    chart4.setOption(option);
}



// 考勤异常统计

function unusual(nianji,shuju){
    var chart5 = echarts.init(document.getElementById('chart5'));
    var gradelist;//年级列表
    var dayDate;//年级列表
    option = {
        color: ['#08D3E4', '#FB0000', '#FD01BF','#F2C929'],
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            icon:"circle",
            itemWidth:10,
            itemHeight:10,
            bottom:0,
            textStyle:{
                color:"#fff"
            },
            data:nianji/*['高三101','高三102','高三103','高三104']*/
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            backgroundColor:"rgba(0, 0, 0, 0.05)",
            show: true,
            borderWidth:0,
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['周一','周二','周三','周四','周五','周六','周日'],
            axisLine:{
                lineStyle:{
                    color:'#043F70'
                },
            },
            axisLabel: {
                textStyle: {
                    fontSize: 10,
                    color: '#fff'
                },
            },
        },
        yAxis: {
            type: 'value',
            name: '人数/人',
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#043F70',
                }
            },
            axisLine:{
                lineStyle:{
                    color:'#043F70'
                },
            },
            axisLabel: {
                textStyle: {
                    fontSize: 10,
                    color: '#fff'
                },
            },

        },
        series:shuju /*[
		    {
		        name:'高三101',
		        type:'line',
		        stack: '总量',
		        data:[120, 132, 101, 134, 90, 230, 210]
		    },
		    {
		        name:'高三102',
		        type:'line',
		        stack: '总量',
		        data:[220, 182, 191, 234, 290, 330, 310]
		    },
		    {
		        name:'高三103',
		        type:'line',
		        stack: '总量',
		        data:[150, 232, 201, 154, 190, 330, 410]
		    },
		    {
		        name:'高三104',
		        type:'line',
		        stack: '总量',
		        data:[320, 332, 301, 334, 390, 330, 220]
		    }
		]*/
    };
    chart5.setOption(option);
}


// 课堂专注度统计

function Concentrationdegree(){
    var chart6 = echarts.init(document.getElementById('chart6'));
    // 指定图表的配置项和数据
    var data1 = [20, 30, 20, 30, 20, 30, 20, 30, 20, 30];
    var data2 = [9, 30, 9, 60, 70, 20, 59, 20, 49, 20];
    var datacity = ['101班', '102班', '103班', '104班', '105班', '106班', '107班'];
    var option = {
        color: ['#F4E183', '#40AFE7'],
        tooltip: {
            trigger: 'axis',
        },
        legend: {
            icon:"circle",
            itemWidth:10,
            itemHeight:10,
            bottom:0,
            textStyle: {
                fontSize: 10,
                color: '#fff'
            },
            data: ['专注人员', '非专注人员'],
        },
        grid: { //图表的位置
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '10%',
            backgroundColor:"rgba(0, 0, 0, 0.05)",
            show: true,
            borderWidth:0,
            containLabel: true
        },
        yAxis: [{
            type: 'value',
            axisLabel: {
                show: true,
                interval: 'auto',
                formatter: '{value} ',
                textStyle: {
                    fontSize: 10,
                    color: '#fff'
                },
            },
            splitLine: {
                show: true,
                lineStyle: {
                    color:'#043F70'
                }
            },
            axisLine:{
                lineStyle:{
                    color:'#043F70'
                },
            },
            show: true

        }],
        xAxis: [{
            type: 'category',
            axisLabel: {
                interval: 0,
                show: true,
                splitNumber: 15,
                textStyle: {
                    fontSize: 10,
                    color: '#fff'
                },

            },
            axisLine:{
                lineStyle:{
                    color:'#043F70'
                },
            },
            data: datacity,
        }],
        series: [{
            name: '专注人员',
            type: 'bar',
            stack: 'sum',
            barWidth: '20px',
            data: data1

        },
            {
                name: '非专注人员',
                type: 'bar',
                barWidth: '20px',
                stack: 'sum',
                data: data2,

            }
        ]
    };
    chart6.setOption(option);
}