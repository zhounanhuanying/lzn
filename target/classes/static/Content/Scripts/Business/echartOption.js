
//饼状图
function echartPie(data, name, dataName, formatter) {
    var option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        //legend: {
        //    y: 'bottom',
        //    data: ['男', '女']
        //},
        grid: {
            top: '5%',
            left: '3%',
            right: '3%',
            bottom: '0%',
            containLabel: true,
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        calculable: true,
        series: [

            {
                name: name,
                type: 'pie',
                radius: [40, 65],
                center: ['50%', '50%'],
                data: [{
                    value: data[0],
                    name: dataName[0],
                    itemStyle: {

                        color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                            offset: 1,
                            color: '#2278f8'
                        }, {
                            offset: 0,
                            color: '#8dc5ff'
                        }])
                    },
                    label: {
                        color: "#666",
                        fontSize: 14,
                        formatter: formatter[0],
                        rich: {
                            a: {
                                color: "#fff",
                                fontSize: 20,
                                lineHeight: 30
                            },
                        }
                    }
                },
                    {
                        value: data[0],
                        name: dataName[0],
                        itemStyle: {
                            color: "transparent"
                        }
                    }
                ]
            },
            {
                name: name,
                type: 'pie',
                radius: [50, 60],
                center: ['50%', '50%'],
                data: [
                    {
                        value: data[1],
                        name: dataName[1],
                    itemStyle: {
                        color: "transparent"
                    }
                },
                    {
                        value: data[1],
                        name: dataName[1],
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                                offset: 1,
                                color: '#fabf5f'
                            }, {
                                offset: 0,
                                color: '#fff2bc'
                            }])
                        },
                        label: {
                            color: "#666",
                            fontSize: 14,
                            formatter: formatter[1],
                            rich: {
                                a: {
                                    color: "#fff",
                                    fontSize: 20,
                                    lineHeight: 30
                                },
                            }
                        }
                    }
                ]
            }
        ]
    };
    return option;
}


//折线图
function echartLine(data, name, minNum, maxNum, intervalNum) {
    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                lineStyle: {
                    color: 'rgba(0,0,0,0)'
                }
            }

        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        grid: {
            top: '3%',
            left: '3%',
            right: '3%',
            bottom: '3%',
            containLabel: true,
        },
        xAxis: [{
            type: 'category',
            boundaryGap: false,
            axisLabel: { //坐标轴刻度标签的相关设置
                textStyle: {
                    color: '#666',
                },
            },
            axisTick: { show: false },
            splitLine: { show: false },
            splitArea: { show: false },
            axisLine: { show: false },
            axisTick: { show: false, },
            data: data[0],
        }],
        yAxis: [{
            type: 'value',
            min: minNum,
            max: maxNum,
            interval: intervalNum,
            splitNumber: 7,
            axisLine: {
                show: true,
                lineStyle: {
                    color: '#e9f2f7'
                }
            },
            axisLabel: {
                textStyle: {
                    color: '#666',

                },
            },
            axisTick: { show: false },
            splitLine: { show: true },
            splitArea: { show: false },
        }],
        series: [
            {
                name: name[0],
                type: 'line',
                smooth: true, //是否平滑曲线显示
                lineStyle: {
                    normal: {
                        color: "#acd4ff"   // 线条颜色
                    }
                },
                areaStyle: { //区域填充样式
                    normal: {
                        //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: 'rgba(161,204,254, 0.9)' },
                            { offset: 0.7, color: 'rgba(161,204,254, 0.2)' }
                        ], false),

                        shadowColor: 'rgba(203,229,255, 0.9)', //阴影颜色
                        shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                data: data[1]
            },
             {
                 name: name[1],
                 type: 'line',
                 smooth: true,
                 symbol: 'none',
                 itemStyle: {
                     normal: {
                         color: '#f868f6'
                     }
                 },
                 lineStyle: {
                     normal: {
                         width: 1
                     }
                 },
                 areaStyle: {
                     normal: {
                         color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                             offset: 0,
                             color: 'rgba(249, 155, 237,1)'
                         }, {
                             offset: 1,
                             color: 'rgba(249, 155, 237,.2)'
                         }])
                     }
                 },
                 label: {
                     normal: {
                         show: true,
                         position: 'top'
                     }
                 },
                 data: data[2]
             },
            {
                name: name[2],
                type: 'line',
                smooth: true,
                symbol: 'none',
                itemStyle: {
                    normal: {
                        color: '#fbae2c'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1
                    }
                },
                areaStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(254,213,148,1)'
                        }, {
                            offset: 1,
                            color: 'rgba(254,213,148,.2)'
                        }])
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                data: data[3]
            }
        ]
    };
    return option;
}


//柱状图
function echartBar(xData, data, name, minNum, maxNum, intervalNum, ratioName, ratioData) {
    var legendData = [];
    //颜色匹配
    var color = [
        [{
            "offset": 0,
            "color": "rgba(91,154,250,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(91,154,250,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(91,154,250,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(83,229,218,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(83,229,218,0.5)"
        },
        {
            "offset": 1,
            "color": "rgba(83,229,218,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(250,192,96,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(250,192,96,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(250,192,96,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(242, 199, 229, 1)"
        }, {
            "offset": 0.5,
            "color": "rgba(242, 199, 229, 0.5)"
        }, {
            "offset": 1,
            "color": "rgba(242, 199, 229, 0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(113,102,158,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(113,102,158,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(113,102,158,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(120,116,100,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(120,116,100,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(120,116,100,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(171,112,124,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(171,112,124,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(171,112,124,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(43,130,29,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(43,130,29,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(43,130,29,0.1)"
        }],
        [{
            "offset": 0,
            "color": "rgba(255,238,81,1)"
        }, {
            "offset": 0.5,
            "color": "rgba(255,238,81,0.5)"
        }, {
            "offset": 1,
            "color": "rgba(255,238,81,0.1)"
        }]
    ]

    var dataArry = [];
    for (var j = 0; j < data.length; j++) {

        legendData.push({
            "name": name[j],
            "icon": "circle",
            "textStyle": {
                "color": "#7d838b"
            }
        });

        dataArry.push({
            "name": name[j],
            "type": "bar",
            "data": data[j],
            "barWidth": "auto",
            "itemStyle": {
                "normal": {
                    "color": {
                        "type": "linear",
                        "x": 0,
                        "y": 0,
                        "x2": 0,
                        "y2": 1,
                        "colorStops": color[j],
                        "globalCoord": false
                    }
                }
            },
            "barGap": "0"
        })
    }
    var option = {
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        grid: {
            top: '5%',
            left: '3%',
            right: '3%',
            bottom: '0%',
            containLabel: true,
        },
        "color": "#384757",
        "tooltip": {
            "trigger": "axis",
            "axisPointer": {
                "type": "cross",
                "crossStyle": {
                    "color": "#384757"
                }
            }
        },
        "legend": {
            "data": legendData
        },
        "xAxis": [
          {
              "type": "category",
              "data": xData,
              "axisPointer": {
                  "type": "shadow"
              },
              axisLabel: { //坐标轴刻度标签的相关设置
                  textStyle: {
                      color: '#666',
                  },
              },
              axisTick: { show: false },
              splitLine: { show: false },
              splitArea: { show: false },
              axisLine: { show: false },
              axisTick: { show: false, },
          }
        ],
        "yAxis": [
          {
              "type": "value",
              "name": "",
              "min": minNum,
              "max": maxNum,
              "interval": intervalNum,
              axisLabel: { //坐标轴刻度标签的相关设置
                  show: true,
                  textStyle: {
                      color: '#666',
                  },
              },
              axisLine: {
                  show: true,
                  lineStyle: {
                      color: '#e9f2f7'
                  }
              },
              axisTick: { show: false },
              splitLine: { show: false },
              splitArea: { show: false },
          }
          //,
          //{
          //    "type": "value",
          //    "name": name[1],
          //    "show": false,
          //}
        ],
        "series": dataArry
    };

    return option;
}



//柱状图横向
function ecahrtBarland(data, titlename, valdata) {
    var option = {
        grid: {
            top: '3%',
            left: '3%',
            right: '3%',
            bottom: '0%',
            containLabel: true,
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: {
            show: false
        },
        yAxis: [{
            show: true,
            data: titlename,
            inverse: true,
            axisLine: {
                show: false
            },
            splitLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                color: '#666',
                rich: {
                    lg: {
                        backgroundColor: '#339911',
                        color: '#666',
                        // padding: 5,
                        align: 'center',
                        width: 15,
                        height: 15
                    },
                }
            },


        }, {
            show: true,
            inverse: true,
            data: valdata,
            axisLabel: {
                textStyle: {
                    fontSize: 12,
                    color: '#666',
                },
            },
            axisLine: {
                show: false
            },
            splitLine: {
                show: false
            },
            axisTick: {
                show: false
            },

        }],
        series: [{
            name: '框',
            type: 'bar',
            yAxisIndex: 1,
            barGap: '-100%',
            data: [100, 100, 100, 100, 100],
            barWidth: 15,
            itemStyle: {
                normal: {
                    color: '#d3e5ff'
                }
            }
        }, {
            type: 'bar',
            yAxisIndex: 0,
            data: data,
            barWidth: 15,
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                        offset: 0,
                        color: '#a8d1ff'
                    }, {
                        offset: 1,
                        color: '#75abfb'
                    }])

                }
            },

            label: {
                normal: {
                    show: true,
                    position: 'inside',
                    formatter: '{c}%'
                }
            },
        }, ]
    };
    return option;
}


//雷达网状图

function echartRadar(data, name, maxNum) {
    option = {
        color: ["#666", "#666"],
        tooltip: {
            show: true,
            trigger: "item"
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        radar: {
            center: ["50%", "50%"],
            radius: "60%",
            startAngle: 80,
            splitNumber: 4,
            shape: "circle",
            splitArea: {
                areaStyle: {
                    color: ["transparent"]
                }
            },
            axisLabel: {
                show: false,
                fontSize: 14,
                color: "#666",
                fontStyle: "normal",
                fontWeight: "normal"
            },
            axisLine: {
                show: true,
                lineStyle: {
                    type: "solid",
                    color: "#dce5ee"
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                    type: "solid",
                    color: "#dce5ee"
                }
            },
            // shape: 'circle',
            name: {
                //  formatter: '{a|{value}}{abg|}\n{hr|}\n{b|1%}',
                borderRadius: 0,

                rich: {

                    a: {
                        color: '#666',
                        fontSize: '14',
                        padding: [0, 0, 0, 8],
                        height: 25,
                        borderRadius: 0
                    },

                    hr: {
                        width: '100%',
                        align: 'left',
                        height: 1
                    },
                    b: {
                        color: '#666',
                        lineHeight: 25,
                        padding: [0, 0, 0, 8],
                        width: '100%',
                        align: 'left',
                        borderRadius: 0
                    },
                    per: {
                        color: '#666',
                    }
                }

            },
            indicator: [
                {
                name: name[0],
                max: maxNum[0],

                },
                {
                    name: name[1],
                    max: maxNum[1],
                },
                {
                    name: name[2],
                    max: maxNum[2],
                },
                {
                    name: name[3],
                    max: maxNum[3],
                }
            ]
        },

        series: [{
            name: "",
            type: "radar",
            symbol: "circle",
            areaStyle: {
                normal: {
                    color: "rgba(89,229,219, .7)"
                }
            },
            itemStyle: {
                color: 'rgba(86,199,60, 1)',
                borderColor: 'rgba(89,229,219, 0.3)',
                borderWidth: 10,
            },

            data: [
               data
            ]
        }]


    };

    return option;
}