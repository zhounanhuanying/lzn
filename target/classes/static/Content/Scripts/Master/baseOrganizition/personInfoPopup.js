var personInfoPopup = {
    setting: null,
    layer: null,
    form: null,
    table: null,
    util: null,
    admin: null,
    xmSelect: null,
    laydate: null,
    element: null,
    editCertificateNum: 0,
    editContactNum: 0,
    //编辑人员信息
    editPerisonInfo: {},
    //编辑人员组织弹窗关闭标识
    editOrgIndex: null,
    editProfessionIndex: null,
    //所有枚举数据(dsrp)
    StaticEnumData: null,
    //组织下教师辅导员数据集合
    teacherList: null,
    //组织信息存放集合
    orgList: "",
    //组织信息存放集合（回显专用）
    orgListEcho: "",
    //组织信息拼接字符串
    orgStr: "",
    //组织信息拼接集合
    orgStrList: [],
    isAdd: null,
    //所有园区集合
    allRegion: null,
    pageIndex: 1,
    //证件框id暂存集合
    idCardList: [],
    //联系方式框id暂存集合
    phoneList: [],
    //手机号正则
    phoneReg: /^1[3456789]\d{9}$/,
    //邮箱正则
    emailReg: /^\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}$/,
    //身份证正则
    idCardReg: /(^\d{15}$)|(^\d{17}(\d|X)$)/,
    //主控园区选中ID
    regionId: "",
    //所属园区id
    masterRegion: "",
    //证件类型集合（防止证件添加重复）
    cardTypeArr: [],
    personObj: null,
    //人员列表数据公用变量
    personListData: null,
    exportNum: 0,
    yeshu: 0,
    loadingpopup: null,
    //上传文件标识
    //uploadFileFlag:true,
    //导出id列表
    exportIdList: null,
    //进度条窗口下标
    progressIndex: null,
    //进度条定时器
    timer: null,
    resultFileUrl: null,
    //人员修改身份证号
    editIdCard: "",
    //新增修改弹窗索引
    popupIndex:null,
    //账户信息是否校验通过
    checkLoginName:false,
    checkLoginPwd:false,
    checkIDCardNum:false,
    checkPwdFormat:false,

    //存储添加证件之前的证件集合数组
    idCardListCopy:[],
    //标记对应的证件框是否存在
    idCard1:false,
    idCard2:false,
    idCard3:false,
    idCard4:false,
    idCard5:false,
    idCard6:false,
    //存放当前存在的证件类型集合map
    map: null,
    //记录证件下拉框 改变之前的值
    backOffCarVal:'',
    BeforeSwitchCardTypeArr:[]
}

$(function () {
    layui.config({
        base: '../../Content/Scripts/Master/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['layer', 'form', 'table', 'util', 'admin', 'xmSelect', 'laydate', 'treeTable', 'element'], function () {
        $ = layui.jquery;
        personInfoPopup.layer = layui.layer;
        personInfoPopup.form = layui.form;
        personInfoPopup.table = layui.table;
        personInfoPopup.util = layui.util;
        personInfoPopup.admin = layui.admin;
        personInfoPopup.xmSelect = layui.xmSelect;
        personInfoPopup.laydate = layui.laydate;
        personInfoPopup.treeTable = layui.treeTable;
        personInfoPopup.element = layui.element;


        /*获取当前选中园区ID*/
        personInfoPopup.regionId = personInfoPopup.setter.getCookieValue("regionWitchDataId");  // cookie中当前园区ID
        if (personInfoPopup.regionId == '') {
            layer.msg('请选择当前园区', {icon: 2, anim: 6});
        }

        //学生职业 院系的select选择监听
        personInfoPopup.form.on('select(Faculty)', function (data) {
            var orgCode = $('#editFacultyName option:selected').val();
            personInfoPopup.orgClassChange(orgCode);
        });

        $(".layui-tab").on("click", function (e) {
            if ($(e.target).is(".layui-tab-close")) {
                console.log($(e.target).parent().attr("lay-id"))// 输出哪个tab被点击，没有值时返回undefined
            }
        })
        var i = 0;   //切换开关
        personInfoPopup.map = new Map();        //初始化证件框map
        //证件select选择监听
        personInfoPopup.form.on('select(IDCard)', function (data) {
            if(data.value == personInfoPopup.backOffCarVal){
                return
            }
            for (var j = 0; j < personInfoPopup.BeforeSwitchCardTypeArr.length; j++) {
                if(data.value == personInfoPopup.BeforeSwitchCardTypeArr[j]){
                    layer.msg("该证件已存在!");
                    $('#'+data.elem.id).val(personInfoPopup.backOffCarVal);
                    personInfoPopup.form.render('select');
                }
            }
        });

    });
})

/**
 * ztree事件
 */
personInfoPopup.setting = {
    edit: {
        enable: false,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    view: {
        selectedMulti: false,
        dblClickExpand: true
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
    check: {
        chkStyle: "checkbox",
        enable: true,
        chkboxType: { "Y": "", "N": "" }
    },
    callback: {}
};


//校验账户名称唯一性
personInfoPopup.verifyLoginName = function () {
    var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
    var loginName = $("#loginName").val();
    if(!uPattern.test(loginName)){
        layer.msg("账号格式非法（4-16位字母、数字、下划线）", {icon: 2, time: 2000});
        return;
    }

    var personId = personInfoPopup.isAdd ? null : personInfoPopup.personObj.id;
    var params = {
        vData:loginName,
        type: 2,            //账户
        papeworkType: null,
        personId: personId
    }
    //提交ajax请求到后台验证
    personInfoPopup.admin.ajax({
        url: conf.dsrpBaseServer + "person/verifyOnly",
        type: "get",
        data: params,
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code != 0) {
                layer.msg("校验账号失败!", {icon: 2, time: 3000});
                return;
            } else {
                if(!data.data){
                    personInfoPopup.checkLoginName = false;
                    layer.msg("该账号已存在!", {icon: 2, time: 3000});
                    return;
                }else {
                    personInfoPopup.checkLoginName = true;
                }
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    })
}

/**
 * 校验证件号码唯一性
 */
personInfoPopup.verifyUniqueIDCard = function(id){
    var cardNumber = $("#" + id).val();   //获取证件号码
    var index = id.substring(id.length - 1);    //截取证件id尾部id标识，就是类型索引
    var type = $("#editPersonCardType" + index).val();      //利用类型索引获取类型值
    var personId = personInfoPopup.isAdd ? null : personInfoPopup.personObj.id;
    var typeName = "";
    //针对身份证进行正则校验
    if (type == 1) {
        typeName = "身份证";
        var r = personInfoPopup.idCardReg.test(cardNumber);
        if (!r || $("#editPersonIdCard" + index) == "") {
            personInfoPopup.checkIDCardNum = false;
            layer.msg("身份证号码格式错误，长度应为15位或18位", {icon: 2, time: 3000});
            return;
        }else {
            personInfoPopup.checkIDCardNum = true;
        }
    }else {
        var s = /^[a-zA-Z0-9]{5,15}$/;
        var reg = s.test(cardNumber);
        //根据类型动态切换证件名称，相当于if else.....
        type == 2 ? typeName = "护照" : type == 3 ? typeName = "军官证" : type == 4 ? typeName = "工作证" : type == 5 ? typeName = "学生证" : typeName = "其他证件";
        if(!reg || $("#editPersonIdCard" + index) == ""){
            personInfoPopup.checkIDCardNum = false;
            layer.msg(typeName + "格式错误，长度应为5-15位", {icon: 2, time: 3000});
            return;
        }else {
            personInfoPopup.checkIDCardNum = true;
        }
    }

    var params = {
        vData: cardNumber,
        type: 1,            //身份证
        papeworkType: type,
        personId: personId
    }
    //正则校验通过后，开始提交后台进行最后校验
    personInfoPopup.admin.ajax({
        url: conf.dsrpBaseServer + "person/verifyOnly",
        type: "get",
        data: params,
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code == 0) {
                if(!data.data){
                    personInfoPopup.checkIDCardNum = false;
                    layer.msg(typeName+"重复", {icon: 2, time: 5000});
                    return;
                }else {
                    personInfoPopup.checkIDCardNum = true;
                }
            }
        },
        error: function (data) {
            layer.msg("证件验证失败", {icon: 2, time: 5000});
        }
    })
}

//校验密码和确认密码是否一致
personInfoPopup.verifyTwicePassword = function () {
    var password = $("#password").val();
    var confirmPwd = $("#confirmPwd").val();
    if(password !== confirmPwd){
        personInfoPopup.checkLoginPwd = false;
        layer.msg("两次输入密码不一致!", {icon: 2, time: 3000});
        return;
    }else {
        personInfoPopup.checkLoginPwd = true;
        return;
    }
}

//校验密码格式是否正确
personInfoPopup.verifyPwdFormat = function(){
    var uPattern = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/;
    var password = $("#password").val();
    var confirmPwd = $("#confirmPwd").val();
    if(!uPattern.test(password)){
        personInfoPopup.checkPwdFormat = false;
        layer.msg("密码格式非法（6-20位，包含数字、英文）", {icon: 2, time: 2000});
        return;
    }else {
        personInfoPopup.checkPwdFormat = true;
    }
}


var baseHtml = "    <form id=\"editPersonInfoEditForm\" lay-filter=\"editPersonInfoEditForm\" class=\"layui-form\"\n" +
    "          style=\"padding: 10px;background: #f6f6f6;\">\n" +
    "        <input name=\"planId\" type=\"hidden\"/>\n" +
    "        <div style=\"height:475px;overflow-y:auto;\">\n" +
    "            <div id=\"card-acc\" class=\"layui-card\" style=\"display: none\">\n" +
    "                <div class=\"layui-card-header\">账户信息</div>\n" +
    "                <div class=\"layui-card-body\">\n" +
    "                    <table class=\"addPersonInfo-table\">\n" +
    "                        <colgroup>\n" +
    "                            <col width=\"15%\">\n" +
    "                            <col width=\"30%\">\n" +
    "                            <col width=\"15%\">\n" +
    "                            <col width=\"30%\">\n" +
    "                            <col>\n" +
    "                        </colgroup>\n" +
    "                        <tbody>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">用户名称</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <input id=\"loginName\" lay-verify=\"required\" onblur=\"personInfoPopup.verifyLoginName()\"  class=\"layui-input\"/>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">密码</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <input type=\"password\" id=\"password\" onblur=\"personInfoPopup.verifyPwdFormat()\" lay-verify=\"required\" \n" +
    "                                           class=\"layui-input\"/></div>\n" +
    "                            </td>\n" +
    "                            <td></td>\n" +
    "                        </tr>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">确认密码</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <div class=\"layui-input-block\">\n" +
    "                                        <input type=\"password\" id=\"confirmPwd\" lay-verify=\"required\" \n" +
    "                                               class=\"layui-input\"/>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        </tbody>\n" +
    "                    </table>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"layui-card\">\n" +
    "                <div class=\"layui-card-header\">基本信息</div>\n" +
    "                <div class=\"layui-card-body \">\n" +
    "                    <table class=\"addPersonInfo-table\">\n" +
    "                        <colgroup>\n" +
    "                            <col width=\"15%\">\n" +
    "                            <col width=\"35%\">\n" +
    "                            <col width=\"15%\">\n" +
    "                            <col width=\"25%\">\n" +
    "                            <col>\n" +
    "                        </colgroup>\n" +
    "                        <tbody>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">姓名:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\"><input name=\"\" class=\"layui-input\" placeholder=\"\"\n" +
    "                                                                      id=\"editPersonName\" lay-verify='required' value=\"\"/></div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label\">所属园区:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <input name=\"\"\n" +
    "                                           class=\"layui-input\" lay-verType=\"tips\" lay-verify=\"nonempty\"\n" +
    "                                           required\n" +
    "                                           id=\"editPersonRegionPopwin\">\n" +
    "                                    <!--<select id=\"editPersonRegionPopwin\" lay-search=\"\">\n" +
    "                                    </select>-->\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td rowspan=\"3\">\n" +
    "                                <div class=\"upload-img\">\n" +
    "                                    <div class=\"uploadImg\">\n" +
    "                                        <img id=\"fileUploadImg\" onclick=\"personInfoPopup.uploadEvent()\"></img>\n" +
    "                                        <i class=\"layui-icon layui-icon-addition\"></i>\n" +
    "                                    </div>\n" +
    "                                    <input type=\"file\" class=\"fileUpLoad\" id=\"fileUpLoadIpt\" style=\"display: none\"\n" +
    "                                           onchange=\"personInfoPopup.ImageChange(this)\">\n" +
    "                                    <p>上传图片</p>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label\">国籍:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editPersonNationality\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label\">籍贯:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\"><input id=\"editPersonPlace\" name=\"\" class=\"layui-input\"\n" +
    "                                                                      placeholder=\"\"/></div>\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label\">民族:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editPersonNation\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label\">血型:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editPersonBooldType\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                                <!--<div class=\"layui-input-block\"><input id=\"editPersonBloodType\" name=\"\" class=\"layui-input\" placeholder=\"\"/></div>-->\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label\">性别:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editPersonGender\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label\">政治面貌:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editPersonPoliticalStatus\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label\">宗教信仰:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editPersonReligion\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label\">单位/部门:</label></td>\n" +
    "                            <td style=\"width:57%\">\n" +
    "                                <div class=\"layui-input-block layui-input-icon\">\n" +
    "                                    <input id=\"editOrgipt\" readonly=“readonly” name=\"\" class=\"layui-input\"\n" +
    "                                           placeholder=\"\"/>\n" +
    "                                    <span class=\"icon-input iconfont icon-xuanze1 color-blue\" lay-tips=\"选择\"\n" +
    "                                          lay-direction=\"3\" id=\"unitSelect\"\n" +
    "                                          onclick=\"personInfoPopup.orgChoose()\"></span>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        </tbody>\n" +
    "                    </table>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"layui-card\">\n" +
    "                <div class=\"layui-card-header\">添加证件信息\n" +
    "                    <i class=\"layui-icon layui-icon-add-circle color-blue\"\n" +
    "                       onclick=\"personInfoPopup.addCertificateCard()\"></i>\n" +
    "                </div>\n" +
    "                <div class=\"layui-card-body\" name=\"radioCard\" id=\"editCertificateCard\">\n" +
    "                </div>\n" +
    "            <div class=\"layui-card\">\n" +
    "                <div class=\"layui-card-header\">添加通讯录\n" +
    "                    <i class=\"layui-icon layui-icon-add-circle color-blue\"\n" +
    "                       onclick=\"personInfoPopup.addContactCard()\"></i>\n" +
    "                </div>\n" +
    "                <div class=\"layui-card-body\" id=\"editContactCard\">\n" +
    "                    <table class=\"addPersonInfo-table\" style=\"display: none\">\n" +
    "                        <colgroup>\n" +
    "                            <col width=\"15%\">\n" +
    "                            <col width=\"30%\">\n" +
    "                            <col width=\"15%\">\n" +
    "                            <col width=\"30%\">\n" +
    "                            <col>\n" +
    "                        </colgroup>\n" +
    "                        <tbody>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">联系方式:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editAddressTypeSel\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">联系类别:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\">\n" +
    "                                    <select id=\"editAddressChildTypeSel\" lay-search=\"\">\n" +
    "                                    </select>\n" +
    "                                </div>\n" +
    "                            </td>\n" +
    "                            <td></td>\n" +
    "                        </tr>\n" +
    "                        <tr>\n" +
    "                            <td><label class=\"layui-form-label layui-form-required\">联系电话:</label></td>\n" +
    "                            <td>\n" +
    "                                <div class=\"layui-input-block\"><input id=\"editPersonPhone\" name=\"\" class=\"layui-input\"\n" +
    "                                                                      placeholder=\"\"/></div>\n" +
    "                            </td>\n" +
    "                        </tr>\n" +
    "                        </tbody>\n" +
    "                    </table>\n" +
    "                </div>\n" +
    "\n" +
    "            </div>\n" +
    "            <div class=\"layui-card\">\n" +
    "                <div class=\"layui-card-header\">添加职业信息\n" +
    "                    <i class=\"layui-icon layui-icon-add-circle color-blue\" id=\"addProfessional\"\n" +
    "                       onclick=\"personInfoPopup.addProfession()\"></i>\n" +
    "                </div>\n" +
    "                <div class=\"layui-card-body\">\n" +
    "                    <div id=\"professioTab\" class=\"layui-tab layui-tab-card\" lay-filter=\"demo\" lay-allowclose=\"true\">\n" +
    "                        <ul class=\"layui-tab-title layui-tab-title-colxs\">\n" +
    "                        </ul>\n" +
    "                        <div class=\"layui-tab-content\">\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"model-form-footer\">\n" +
    "            <button class=\"layui-btn\" type=\"button\" lay-filter=\"planEditSubmit\" lay-submit\n" +
    "                    onclick=\"personInfoPopup.editPersonInfo()\">确定\n" +
    "            </button>\n" +
    "            <button class=\"layui-btn layui-btn-primary\" type=\"button\" ew-event=\"closeDialog\">取消</button>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </form>";


/**
 *
 * 公共弹窗入口
 * @param opt --操作: 1,edit->修改 2,add->新增
 * @param isNeedAccount --是否需要账户信息表单: 1,true->需要 2,false->不需要
 * @param personId --参数对象: 需要回显的人员ID
 * @param fn --参数对象: 回调方法
 * @param StaticEnumData --参数对象: 下拉框数据
 */
personInfoPopup.personInfoPopupWindow = function (opt, isNeedAccount, personId, fn, StaticEnumData) {
    // 存放回调方法
    personInfoPopup.callbackFn = fn;
    personInfoPopup.isNeedAccount = isNeedAccount;
    // 获取人员下拉框数据
    if(StaticEnumData){
        personInfoPopup.StaticEnumData = StaticEnumData;
        for (var i = 0; i < personInfoPopup.StaticEnumData.cardType.length; i++) {
            personInfoPopup.cardTypeArr.push(personInfoPopup.StaticEnumData.cardType[i].id)
        }
    }else{
        personInfoPopup.getAllStaticDataCk();
    }
    if (opt === 'edit') {
        personInfoPopup.editPerisonInfoById(personId, isNeedAccount);
    }
    if (opt === 'add') {
        personInfoPopup.addPersonInfo(isNeedAccount);
    }
}

//封装一个添加弹窗的方法
personInfoPopup.addPersonInfo = function (isNeedAccount) {
    personInfoPopup.editPerisonInfo.organizationList = [];
    personInfoPopup.isAdd = true;
    personInfoPopup.editCertificateNum = 0;
    personInfoPopup.editContactNum = 0;
    personInfoPopup.popupIndex = personInfoPopup.admin.open({
        type: 1,
        title: '人员信息新增',
        area: ['800px', '600px'],
        id: 'createPerson',
        content: baseHtml,
        success: function () {
            //每次弹窗，清空证件信息map
            personInfoPopup.map = new Map();
            //头像初始化
            personInfoPopup.editPerisonInfo.photoFileName = "";
            //编辑证件号元素数组
            personInfoPopup.idCardList = [];
            //编辑联系方式元素数组
            personInfoPopup.phoneList = [];
            //人员基本数据下拉框渲染
            personInfoPopup.setSelData(personInfoPopup.StaticEnumData, "add");
            //人员职业下拉框渲染
            personInfoPopup.TeacherEnumSetData();
            //递归获取所属部门Str
            personInfoPopup.orgStrList.length = 0;
            personInfoPopup.orgStr = "";
            //获取人员对应联系方式类型
            //personInfoPopup.getTelType();
            //获取所有园区
            //selectPage园区下拉框渲染
            personInfoPopup.setEditRegionSel(personInfoPopup.regionId);
            //personInfoPopup.getAllRegion("add");
            //联系方式默认框
            personInfoPopup.contactEchoAdd();
            //证件默认框
            personInfoPopup.paperWorkEchoAdd();
            //渲染select控件
            personInfoPopup.form.render('select', 'editPersonInfoEditForm');
            //add 添加账户信息模块
            isNeedAccount ? $("#card-acc").show() : $("#card-acc").remove();

        }
    });
}


//封装一个编辑人员的方法
personInfoPopup.editPerisonInfoById = function (personId, isNeedAccount) {
    // var loadIndex = personInfoPopup.layer.load(2);
    personInfoPopup.isAdd = false;
    // personInfoPopup.personObj = obj;
    personInfoPopup.popupIndex = personInfoPopup.admin.open({
        type: 1,
        title: '人员信息编辑',
        area: ['800px', '600px'],
        content: baseHtml,
        success: function (layero) {
            personInfoPopup.admin.ajax({
                type: "get",
                url: conf.dsrpBaseServer + "person/findPersonById",
                data: { personId: personId},
                async: true,
                dataType: "json",
                success: function (data) {
                    if (data.code != 0) {
                        layer.msg("人员信息查询失败", {icon: 2, time: 5000});
                        return;
                    } else {
                        personInfoPopup.personObj = data.data;
                        // 人员ID
                        personInfoPopup.personId =data.data.id;
                        info = data.data;
                        //编辑证件号元素数组
                        personInfoPopup.idCardList = [];
                        //编辑联系方式元素数组
                        personInfoPopup.phoneList = [];
                        //获取所有枚举信息
                        personInfoPopup.setSelData(personInfoPopup.StaticEnumData);
                        //园区下拉赋值
                        personInfoPopup.setEditRegionSel(info.regionId);
                        //$("#editPersonRegionPopwin").val(info.regionId);
                        //获取枚举数据
                        personInfoPopup.TeacherEnumSetData();
                        if (info.photoFileName != null && info.photoFileName != '') {
                            $("#fileUploadImg").attr("src", conf.dsrpBaseServer + info.photoFileName);
                        }
                        $("#editPersonName").val(info.name);
                        $("#editPersonPlace").val(info.nativePlace);
                        $("#editPersonBloodType").val(info.bloodType);
                        $("#editPersonIdCard").val(info.idCard);
                        $("#editPersonPhone").val(info.phone);
                        // 回显组织机构全名称
                        if(info.fullOraName && info.fullOraName.length >0){
                            $("#editOrgipt").val(info.fullOraName[0]);
                        }
                        // 组织机构数据 用于树的回显
                        personInfoPopup.editPerisonInfo.organizationList = info.organizationList;

                        //获取人员对应职业信息
                        var professionMap = new Map;
                        professionMap.set("studentInfo", info.studentInfo);
                        professionMap.set("techerInfo", info.teacherInfo);
                        professionMap.set("securityInfo", info.securityInfo);
                        professionMap.set("staffInfo", info.staffInfo);
                        personInfoPopup.getProfessionById(professionMap, 2);
                        //获取人员对应联系方式类型
                        personInfoPopup.getTelType(info.addressBookList);
                        //证件回显
                        personInfoPopup.paperWorkEcho(info.papeWorkList);
                        //渲染select控件
                        personInfoPopup.form.render('select');
                        personInfoPopup.form.render('radio');
                        isNeedAccount ? $("#card-acc").show() : $("#card-acc").remove();
                    }
                },
                error: function (data) {
                    layer.msg("人员信息请求失败", {icon: 2, time: 5000});
                }
            })


        }
    });
}

//回显人员数据
personInfoPopup.echoPerisonInfoById = function (personId, isNeedAccount) {
    // 清空表单
    $('#editOrgipt').val('');
    $('#editContactCard').empty();
    $('#editCertificateCard').empty();
    $('#professioTab').children().eq(0).empty();
    $('#professioTab').children().eq(1).empty();
    $("#fileUploadImg").removeAttr("src");

    // var loadIndex = personInfoPopup.layer.load(2);
    personInfoPopup.isAdd = false;
    // personInfoPopup.personObj = obj;
    personInfoPopup.admin.ajax({
        type: "get",
        url: conf.dsrpBaseServer + "person/findPersonById",
        data: { personId: personId},
        async: true,
        dataType: "json",
        success: function (data) {
            if (data.code != 0) {
                layer.msg("人员信息查询失败", {icon: 2, time: 5000});
                return;
            } else {
                personInfoPopup.personObj = data.data;
                // 人员ID
                personInfoPopup.personId =data.data.id;
                info = data.data;
                //编辑证件号元素数组
                personInfoPopup.idCardList = [];
                //编辑联系方式元素数组
                personInfoPopup.phoneList = [];
                //获取所有枚举信息
                personInfoPopup.setSelData(personInfoPopup.StaticEnumData);
                //园区下拉赋值
                // personInfoPopup.setEditRegionSel(info.regionId);
                $('#editPersonRegionPopwin').val(info.regionId);
                $('#editPersonRegionPopwin').selectPageRefresh();
                //获取枚举数据
                personInfoPopup.TeacherEnumSetData();
                if (info.photoFileName != null && info.photoFileName != '') {
                    $("#fileUploadImg").attr("src", conf.dsrpBaseServer + info.photoFileName);
                }
                $("#editPersonName").val(info.name);
                $("#editPersonPlace").val(info.nativePlace);
                $("#editPersonBloodType").val(info.bloodType);
                $("#editPersonIdCard").val(info.idCard);
                $("#editPersonPhone").val(info.phone);
                // 回显组织机构全名称
                if(info.fullOraName){
                    $("#editOrgipt").val(info.fullOraName);
                }
                // 组织机构数据 用于树的回显
                personInfoPopup.editPerisonInfo.organizationList = info.organizationList;

                //获取人员对应职业信息
                var professionMap = new Map;
                professionMap.set("studentInfo", info.studentInfo);
                professionMap.set("techerInfo", info.teacherInfo);
                professionMap.set("securityInfo", info.securityInfo);
                professionMap.set("staffInfo", info.staffInfo);
                personInfoPopup.getProfessionById(professionMap, 2);
                //获取人员对应联系方式类型
                personInfoPopup.getTelType(info.addressBookList);
                //证件回显
                personInfoPopup.paperWorkEcho(info.papeWorkList);
                //渲染select控件
                personInfoPopup.form.render('select');
                personInfoPopup.form.render('radio');
            }
        },
        error: function (data) {
            layer.msg("人员信息请求失败", {icon: 2, time: 5000});
        }
    });
}

/*院系下拉选择联动班级*/
personInfoPopup.orgClassChange = function (orgCode) {
    $('#editClass option').remove();
    for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
        //班级下拉赋值
        if (orgCode == personInfoPopup.orgList.data[i].pid) {
            var selhtml = "<option value=" + personInfoPopup.orgList.data[i].id + ">" + personInfoPopup.orgList.data[i].orgName + "</option>"
            $("#editClass").append(selhtml);
        }
    }
    personInfoPopup.form.render('select');
}

/*获取园区下的教师列表*/
personInfoPopup.getTeacherListByOrgCode = function (regionId) {
    personInfoPopup.admin.ajax({
        url: conf.dsrpBaseServer + "person/teacherList",
        type: "get",
        data: {regionId: regionId},
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code != 0) {
                return;
            } else {
                personInfoPopup.teacherList = data.data;
                //personManage.teacherList = data.data;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("获取园区下教师错误" + XMLHttpRequest.status + "错误请联系管理员！", {icon: 2});
        }
    })

}

/*递归获组织机构名称*/
/*personInfoPopup.getOrgStr = function (pid) {
    if (pid != '11111111-1111-1111-1111-111111111111') {
        var pid;
        for (var i = 0; i < personInfoPopup.orgListEcho.data.length; i++) {
            if (personInfoPopup.orgListEcho.data[i].id == pid) {
                //personInfoPopup.orgStr+=;
                personInfoPopup.orgStrList.push(personInfoPopup.orgListEcho.data[i].orgName)
                pid = personInfoPopup.orgListEcho.data[i].pid;
                break;
            }
        }
        return personInfoPopup.getOrgStr(pid);
    }
    return;
}*/

/*人员编辑多证件 按顺序回显*/
personInfoPopup.paperWorkEcho = function (papeWorkList) {
    if (papeWorkList.length > 0) {
        for (var k = 0; k < papeWorkList.length; k++) {
            for (var i = 1; i <= papeWorkList.length; i++) {
                if(papeWorkList[i-1].rank == k ){
                    if (papeWorkList[i - 1].type == 1) {
                        personInfoPopup.editIdCard = papeWorkList[i - 1].paperwork;
                    }
                    var html = '<div class="layui-card-body" id="editCertificateCard' + i + '">';
                    html += '<table class="addPersonInfo-table">';
                    html += '<colgroup>';
                    html += '<col width="15%">';
                    html += '<col width="30%">';
                    html += '<col width="15%">';
                    html += '<col width="30%">';
                    html += '<col>';
                    html += '</colgroup>';
                    html += '<tbody>';
                    html += ' <tr>';
                    html += '<td><label class="layui-form-label layui-form-required">证件类型:</label></td>';
                    html += '<td>';
                    html += '<div class="layui-input-block" onclick="personInfoPopup.BeforeSwitch(this)">';
                    html += '<select  id="editPersonCardType' + i + '" lay-filter="IDCard">';
                    html += '</select>';
                    html += '</div>';
                    html += '</td>';
                    html += '<td><label class="layui-form-label layui-form-required">证件号:</label></td>';
                    html += '<td>';
                    html += '<div class="layui-input-block"><input id="editPersonIdCard' + i + '" name="" class="layui-input" placeholder="" onchange="personInfoPopup.verifyUniqueIDCard(\'editPersonIdCard\' + personInfoPopup.editCertificateNum)"/></div>';
                    html += '</td>';
                    //编辑首个证件不可删除
                    if (i == 1) {
                        html += ' <td><i  id="editPersonDelCardType' + i + '" class="layui-icon layui-icon-close-fill color-red" onclick="personInfoPopup.removeCard(this)" style="display: none"></i></td>';
                    } else {
                        html += ' <td><i  id="editPersonDelCardType' + i + '" class="layui-icon layui-icon-close-fill color-red" onclick="personInfoPopup.removeCard(this)"></i></td>';
                    }
                    html += ' </tr>';
                    html += '<tr>';
                    html += '<td><label class="layui-form-label">是否默认:</label></td>';
                    html += '<td>';
                    html += '<div class="layui-input-block">';
                    if (papeWorkList[i - 1].primaryKey) {
                        html += '<input  type="radio" name="yes"  title="是"  value="' + i + '" checked="checked">';
                    } else {
                        html += '<input  type="radio" name="yes"  title="是"  value="' + i + '">';
                    }

                    html += '</div>';
                    html += '</td>';
                    html += '</tr>';
                    html += '</tbody>';
                    html += '</table>';
                    html += '</div>';
                    $("#editCertificateCard").append(html);
                    $("#editPersonDelCardType" + i).val(papeWorkList[i - 1].id);
                    $("#editPersonIdCard" + i).val(papeWorkList[i - 1].paperwork);
                    for (var j = 0; j < personInfoPopup.StaticEnumData.cardType.length; j++) {
                        selhtml = "<option value=" + personInfoPopup.StaticEnumData.cardType[j].id + ">" + personInfoPopup.StaticEnumData.cardType[j].name + "</option>"
                        $("#editPersonCardType" + i).append(selhtml);
                    }
                    $("#editPersonCardType" + i).val(papeWorkList[i - 1].type)
                    personInfoPopup.idCardList.push("editCertificateCard" + i);
                    break;
                }
            }
        }

        personInfoPopup.editCertificateNum = papeWorkList.length;
    }
    personInfoPopup.form.render('select');
}

/*新增人员默认证件框*/
personInfoPopup.paperWorkEchoAdd = function () {
    personInfoPopup.editCertificateNum = 0;
    personInfoPopup.editCertificateNum++;
    var html = '<div class="layui-card-body" id="editCertificateCard' + personInfoPopup.editCertificateNum + '">';
    html += '<table class="addPersonInfo-table">';
    html += '<colgroup>';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col>';
    html += '</colgroup>';
    html += '<tbody>';
    html += ' <tr>';
    html += '<td><label class="layui-form-label layui-form-required">证件类型:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block" onclick="personInfoPopup.BeforeSwitch(this)">';
    html += '<select  id="editPersonCardType' + personInfoPopup.editCertificateNum + '" data-method="'+personInfoPopup.editCertificateNum+'"  lay-filter="IDCard">';

    html += '</select>';
    html += '</div>';
    html += '</td>';
    html += '<td><label class="layui-form-label layui-form-required">证件号:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block"><input id="editPersonIdCard' + personInfoPopup.editCertificateNum + '" name="" class="layui-input" placeholder="" onchange="personInfoPopup.verifyUniqueIDCard(\'editPersonIdCard\' + personInfoPopup.editCertificateNum)"/></div>';
    html += '</td>';
    html += ' <td><i  id="editPersonDelCardType' + personInfoPopup.editCertificateNum + '" class="layui-icon layui-icon-close-fill color-red" onclick="personInfoPopup.removeCard(this)" style="display:none;"></i></td>';
    html += ' </tr>';
    html += '<tr>';
    html += '<td><label class="layui-form-label">是否默认:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block">';
    html += '<input  id="paperworkRadio' + personInfoPopup.editCertificateNum + '" type="radio" name="yes" value="' + personInfoPopup.editCertificateNum + '" lay-filter="ChoiceRadio" checked="checked">';
    html += '</div>';
    html += '</td>';
    html += '</tr>';
    html += '</tbody>';
    html += '</table>';
    html += '</div>';
    $("#editCertificateCard").append(html);
    personInfoPopup.map['idCard1'] = 1;
    personInfoPopup.idCard1 = true;
    personInfoPopup.idCardList.push("editCertificateCard" + personInfoPopup.editCertificateNum);
    for (var j = 0; j < personInfoPopup.StaticEnumData.cardType.length; j++) {
        selhtml = "<option value=" + personInfoPopup.StaticEnumData.cardType[j].id + ">" + personInfoPopup.StaticEnumData.cardType[j].name + "</option>"
        $("#editPersonCardType" + personInfoPopup.editCertificateNum).append(selhtml);
    }
    personInfoPopup.form.render();  // 加入这一句
    // personInfoPopup.form.render('select');
}

/*获取指定园区下 组织机构树数据*/
personInfoPopup.getOrgData = function (regionId) {
    personInfoPopup.admin.ajax({
        url: conf.dsrpBaseServer + "person/organzationTree",
        type: "get",
        data: {regionId: regionId},
        async: false,
        success: function (data) {
            if (data.code != 0) {
                layer.alert("获取组织架构错误" + data.msg + "错误请联系管理员！", {icon: 2});
                return;
            } else {
                for (var ii = 0; ii < data.data.length; ii++) {
                    if (data.data[ii].orgType == 0) {
                        data.data[ii].nocheck = true
                    }
                }
                personInfoPopup.orgList = data;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("获取组织架构错误" + XMLHttpRequest.status + "错误请联系管理员！", {icon: 2});
        }
    })
}

var orgHtml = "<form id=\"unitForm\" lay-filter=\"unitForm\" class=\"layui-form\" style=\"padding:10px\">\n" +
    "        <input name=\"planId\" type=\"hidden\" id=\"edtiOrg\"/>\n" +
    "        <div class=\"layui-card\">\n" +
    "            <div class=\"layui-card-body\" style=\"height:455px;overflow-y: auto;border:1px solid #ddd; \">\n" +
    "                <ul id=\"editOrgZtree\" class=\"ztree\"></ul>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"model-form-footer\">\n" +
    "            <button class=\"layui-btn\" type=\"button\" lay-filter=\"planEditSubmit\" lay-submit\n" +
    "                    onclick=\"personInfoPopup.getCheckedAll()\">确定\n" +
    "            </button>\n" +
    "        </div>\n" +
    "    </form>";

/* 人员组织编辑弹窗*/
personInfoPopup.orgChoose = function () {
    personInfoPopup.editOrgIndex = personInfoPopup.admin.open({
        type: 1,
        title: '组织架构',
        area: ['400px', '600px'],
        content: orgHtml,
        success: function (layero) {
            //zTree初始化
            let orgZtree = $.fn.zTree.init($("#editOrgZtree"), personInfoPopup.setting, personInfoPopup.orgList.data);
            //默认展开所有节点
            orgZtree.expandAll(true);
            //回显
            let checkOrg = personInfoPopup.editPerisonInfo.organizationList;
            if(checkOrg && checkOrg.length > 0){
                checkOrg.forEach(function (item) {
                    var node = orgZtree.getNodeByParam("id", item.id);
                    if(node != null){
                        orgZtree.checkNode(node, true, true);
                    }
                })
            }
        }
    })
}

/*选择组织机构 提交  回显首个组织机构全名称 存储选中的组织机构ID*/
personInfoPopup.getCheckedAll = function () {

    var treeObj = $.fn.zTree.getZTreeObj("editOrgZtree");
    var nodes = treeObj.getCheckedNodes(true);
    // 获取选中的部门id
    var departmentIds = [];
    for (var i = 0; i < nodes.length; i++) {
        //departmentIds.push({"id":nodes[i].id});
        departmentIds.push(nodes[i].id);
    }
    //personInfoPopup.editPerisonInfo.organizationList = departmentIds;
    personInfoPopup.editPerisonInfo.orgList = departmentIds;
    //并将用户选择的信息展示在人员的组织机构输入框中
    var node = treeObj.getNodes();
    var nodes = treeObj.transformToArray(node);
    var orgname_str = "";
    if (departmentIds.length > 0) {
        orgname_str = personInfoPopup.ReverseOrganizationInfo(departmentIds[0], nodes, orgname_str);
    }
    $("#editOrgipt").val(orgname_str.substring(0, orgname_str.length - 1));
    // personInfoPopup.editPerisonInfo.department = msg;
    layer.close(personInfoPopup.editOrgIndex);
}

//依据组织机构ID逆向找根(递归)
personInfoPopup.ReverseOrganizationInfo = function (org_id, org_list, org_str) {
    if (org_id == "00000000-0000-0000-0000-000000000000" || org_id == "11111111-1111-1111-1111-111111111111") {
        return org_str;
    }
    else {
        for (var i = 0; i < org_list.length; i++) {
            if (org_list[i].id == org_id) {
                org_str = org_list[i].orgName + "/" + org_str;
                return personInfoPopup.ReverseOrganizationInfo(org_list[i].pid, org_list, org_str);
            }
        }
    }
    return org_str;
}

/*人员对应职业信息赋值*/
personInfoPopup.getProfessionById = function (professionMap, flag) {
    //保安信息
    if (professionMap.get("securityInfo") != null) {
        var securityInfo = professionMap.get("securityInfo");
        if (flag == 1) {
            //回显类型name
            for (var i = 0; i < personInfoPopup.StaticEnumData.securityTypeTypeList.length; i++) {
                if (personInfoPopup.StaticEnumData.securityTypeTypeList[i].code == securityInfo.securityTypeId) {
                    securityInfo.securityName = personInfoPopup.StaticEnumData.securityTypeTypeList[i].name;
                }
            }
            personInfoPopup.openProfessionDislogCk(securityInfo, "4");
            //$("#securityName").html(data.data.securityInfo.securityTypeId);
        } else {
            //lay-allowclose="true"
            personInfoPopup.openProfessionDislog("4");
            $("#editSecurityName").val(securityInfo.securityTypeId);
        }
    }
    //教师信息
    if (professionMap.get("techerInfo") != null) {
        var techerInfo = professionMap.get("techerInfo");
        if (flag == 1) {
            //回显教师等级name
            for (var i = 0; i < personInfoPopup.StaticEnumData.levelList.length; i++) {
                if (personInfoPopup.StaticEnumData.levelList[i].code == techerInfo.level) {
                    techerInfo.levelName = personInfoPopup.StaticEnumData.levelList[i].name;
                }
            }
            //回显教师在职状态name
            for (var i = 0; i < personInfoPopup.StaticEnumData.statusList.length; i++) {
                if (personInfoPopup.StaticEnumData.statusList[i].code == techerInfo.status) {
                    techerInfo.statusName = personInfoPopup.StaticEnumData.statusList[i].name;
                }
            }
            personInfoPopup.openProfessionDislogCk(techerInfo, "2");
        } else {
            personInfoPopup.openProfessionDislog("2");
            $("#editTecherStatus").val(techerInfo.status);
            $("#editTecherType").val(techerInfo.level);
            $("#editYunaxi").val(techerInfo.classId)

        }
    }
    //职工信息
    if (professionMap.get("staffInfo") != null) {
        var staffInfo = professionMap.get("staffInfo");
        if (flag == 1) {
            //回显院系name
            for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
                if (personInfoPopup.orgList.data[i].id == staffInfo.orgId) {
                    staffInfo.orgName = personInfoPopup.orgList.data[i].orgName;
                }
            }
            //回显在职状态name
            for (var i = 0; i < personInfoPopup.StaticEnumData.staffStatusList.length; i++) {
                if (personInfoPopup.StaticEnumData.staffStatusList[i].code == staffInfo.staffStatus) {
                    staffInfo.statusName = personInfoPopup.StaticEnumData.staffStatusList[i].name;
                }
            }
            //回显职工类型name
            for (var i = 0; i < personInfoPopup.StaticEnumData.staffTypeList.length; i++) {
                if (personInfoPopup.StaticEnumData.staffTypeList[i].code == staffInfo.staffType) {
                    staffInfo.staffTypeName = personInfoPopup.StaticEnumData.staffTypeList[i].name;
                }
            }
            personInfoPopup.openProfessionDislogCk(staffInfo, "3");
        } else {
            personInfoPopup.openProfessionDislog("3");
            $("#editOrgId").val(staffInfo.orgId);
            $("#editJobGradle").val(staffInfo.jobGrade);
            $("#editWorkNum").val(staffInfo.workNum);
            $("#editStartTime").val(staffInfo.startTime);
            $("#editStaffType").val(staffInfo.staffType);
            $("#editStaffStatus").val(staffInfo.staffStatus);
        }
    }
    //学生信息
    if (professionMap.get("studentInfo") != null) {
        var studentInfo = professionMap.get("studentInfo");
        if (flag == 1) {
            //回显院系name
            for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
                if (personInfoPopup.orgList.data[i].id == studentInfo.facultyGuid) {
                    studentInfo.facultyName = personInfoPopup.orgList.data[i].orgName;
                }
            }
            //回显班级name
            for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
                if (personInfoPopup.orgList.data[i].id == studentInfo.classGuid) {
                    studentInfo.className = personInfoPopup.orgList.data[i].orgName;
                }
            }
            if (studentInfo.className == "" || studentInfo.className == null) {
                studentInfo.className = "--";
            }
            //回显辅导员name
            //personInfoPopup.getTeacherListByOrgCode(data.data.studentInfo.classGuid);
            for (var i = 0; i < personInfoPopup.teacherList.length; i++) {
                if (personInfoPopup.teacherList[i].personId == studentInfo.staffGuid) {
                    studentInfo.staffName = personInfoPopup.teacherList[i].name;
                }
            }
            if (studentInfo.staffName == "" || studentInfo.staffName == null) {
                studentInfo.staffName = "--";
            }
            //回显导师name
            //personInfoPopup.getTeacherListByOrgCode(data.data.studentInfo.classGuid);
            for (var i = 0; i < personInfoPopup.teacherList.length; i++) {
                if (personInfoPopup.teacherList[i].personId == studentInfo.teacherGuid) {
                    studentInfo.teacherName = personInfoPopup.teacherList[i].name;
                }
            }
            if (studentInfo.teacherName == "" || studentInfo.teacherName == null) {
                studentInfo.teacherName = "--";
            }
            //回显学制name
            for (var i = 0; i < personInfoPopup.StaticEnumData.studentTypeList.length; i++) {
                if (personInfoPopup.StaticEnumData.studentTypeList[i].code == studentInfo.studentType) {
                    studentInfo.studentTypeName = personInfoPopup.StaticEnumData.studentTypeList[i].name;
                }
            }
            //回显状态name
            for (var i = 0; i < personInfoPopup.StaticEnumData.studentStatusList.length; i++) {
                if (personInfoPopup.StaticEnumData.studentStatusList[i].code == studentInfo.status) {
                    studentInfo.studentStatus = personInfoPopup.StaticEnumData.studentStatusList[i].name;
                }
            }
            personInfoPopup.openProfessionDislogCk(studentInfo, "1");
        } else {
            //personInfoPopup.openProfessionDislog(1);
            personInfoPopup.openProfessionDislog("1");
            $("#editStudentNum").val(studentInfo.studentNum);
            $("#editFacultyName").val(studentInfo.facultyGuid);
            for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
                if (studentInfo.facultyGuid == personInfoPopup.orgList.data[i].pid) {
                    selhtml = "<option value=" + personInfoPopup.orgList.data[i].id + ">" + personInfoPopup.orgList.data[i].orgName + "</option>"
                    $("#editClass").append(selhtml);
                }
            }
            $("#editClass").val(studentInfo.classGuid);
            if (studentInfo.inTime != null && studentInfo.inTime != "") {
                $("#editInTime").val(studentInfo.inTime);
            }
            $("#editSpecialty").val(studentInfo.specialty);
            $("#editStudentGradle").val(studentInfo.studentGrade);
            //personInfoPopup.getTeacherListByOrgCode(data.data.studentInfo.classGuid);
            $("#editStaffGuid").val(studentInfo.staffGuid);
            $("#editTeacherGuid").val(studentInfo.teacherGuid);
            $("#editEduYear").val(studentInfo.eduYear);
            $("#editStudentType").val(studentInfo.studentType);
            $("#editStatus").val(studentInfo.status);
        }
    }
    personInfoPopup.form.render('select');
};

/*获取人员下拉数据*/
personInfoPopup.getAllStaticDataCk = function () {
    if(!personInfoPopup.StaticEnumData){
        personInfoPopup.admin.ajax({
            url: conf.dsrpBaseServer + "person/personSelectData",
            type: "get",
            async: false,
            dataType: "json",
            success: function (data) {
                personInfoPopup.StaticEnumData = data.data;
                for (var i = 0; i < personInfoPopup.StaticEnumData.cardType.length; i++) {
                    personInfoPopup.cardTypeArr.push(personInfoPopup.StaticEnumData.cardType[i].id)
                }
                // personInfoPopup.form.render('select');
            },
            error: function (error) {

            }
        })
    }
}

//下拉框赋值枚举数据
personInfoPopup.setSelData = function (data, flag) {
    //国籍下拉框
    var html = '<select id=\"editPersonNationality\" lay-verify=\"\">';
    for (var i = 0; i < data.nationality.length; i++) {
        if (data.nationality[i].name == "中国") {
            html += "<option value=" + data.nationality[i].id + " selected>" + data.nationality[i].name + "</option>"
            continue;
        }
        html += "<option value=" + data.nationality[i].id + ">" + data.nationality[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonNationality").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonNationality").val(personInfoPopup.personObj.nationality);
    }
    //民族下拉框
    var html = '<select id=\"editPersonNation\" lay-verify=\"\">';
    html += "<option></option>"
    for (var i = 0; i < data.nation.length; i++) {
        html += "<option value=" + data.nation[i].id + ">" + data.nation[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonNation").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonNation").val(personInfoPopup.personObj.nation);
    }
    //血型下拉框
    var html = '<select id=\"editPersonBooldType\" lay-verify=\"\">';
    html += "<option></option>"
    for (var i = 0; i < data.bloodType.length; i++) {
        html += "<option value=" + data.bloodType[i].id + ">" + data.bloodType[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonBooldType").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonBooldType").val(personInfoPopup.personObj.bloodType);
    }
    //政治面貌下拉框
    var html = '<select id=\"editPersonPoliticalStatus\" lay-verify=\"\">';
    html += "<option></option>"
    for (var i = 0; i < data.politicalStatus.length; i++) {
        html += "<option value=" + data.politicalStatus[i].id + ">" + data.politicalStatus[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonPoliticalStatus").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonPoliticalStatus").val(personInfoPopup.personObj.politicalStatus);
    }
    //宗教信仰下拉框
    var html = '<select id=\"editPersonReligion\" lay-verify=\"\">';
    html += "<option></option>"
    for (var i = 0; i < data.religion.length; i++) {
        html += "<option value=" + data.religion[i].id + ">" + data.religion[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonReligion").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonReligion").val(personInfoPopup.personObj.religion);
    }
    //性别下拉框
    var html = '<select id=\"editPersonGender\" lay-verify=\"\">';
    for (var i = 0; i < data.gender.length; i++) {
        if (data.gender[i].name == "未知") {
            html += "<option value=" + data.gender[i].id + " selected>" + data.gender[i].name + "</option>"
            continue;
        }
        html += "<option value=" + data.gender[i].id + ">" + data.gender[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonGender").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonGender").val(personInfoPopup.personObj.gender);
    }
    //证件类型 CardType
    var html = '<select id=\"editPersonCardType\" lay-verify=\"\">';
    for (var i = 0; i < data.cardType.length; i++) {
        html += "<option value=" + data.cardType[i].id + ">" + data.cardType[i].name + "</option>"
    }
    html += "</select>";
    $("#editPersonCardType").html(html);
    if (!personInfoPopup.isAdd) {
        $("#editPersonCardType").val(personInfoPopup.personObj.cardType);
    }

}

/*当前园区 使用selectPage*/
personInfoPopup.setEditRegionSel = function (regionId) {
    //获取园区列表 下拉分页选择
    $('#editPersonRegionPopwin').selectPage({
        showField: 'regionName',//显示在列表中的数据字段,默认值name
        keyField: 'id',//数据value值,默认id
        headers: {"Authorization": personInfoPopup.setter.getHeader()},
        data: conf.dsrpBaseServer + 'mapconfig/findRegionInfoPage',//插件列表的数据源
        pageSize: 8,
        searchField: 'regionName',
        params: function () {
            return {'returnAllflag': 0};
        },
        eSelect: function (data) {
            personInfoPopup.regionId = data.id;
            // 所属园区改变 重新获取改变园区后的组织机构树的数据
            personInfoPopup.getOrgData(personInfoPopup.regionId);
            $("#editOrgipt").val('');
            personInfoPopup.editPerisonInfo.organizationList = [];
            // 所属园区改变 重新获取园区下的所有老师信息
            personInfoPopup.getTeacherListByOrgCode(personInfoPopup.regionId)
            // 清空导师、辅导员人员下拉 并重新赋值
            $("#editTeacherGuid").empty();
            $("#editStaffGuid").empty();
            if (personInfoPopup.teacherList != null && personInfoPopup.teacherList.length != 0) {
                for (var i = 0; i < personInfoPopup.teacherList.length; i++) {
                    selhtml = "<option value=" + personInfoPopup.teacherList[i].personId + ">" + personInfoPopup.teacherList[i].name + "</option>"
                    $("#editTeacherGuid").append(selhtml);
                    $("#editStaffGuid").append(selhtml);
                }
            }
            // 学生 ,班级 职工部门 重新渲染
            $('#editFacultyName option').remove();
            $('#editOrgId option').remove();
            $('#editClass option').remove();
            if (personInfoPopup.orgList.data && personInfoPopup.orgList.data.length != 0) {
                for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
                    selhtml = "<option value=" + personInfoPopup.orgList.data[i].id + ">" + personInfoPopup.orgList.data[i].orgName + "</option>"
                    $("#editFacultyName").append(selhtml);
                    $("#editOrgId").append(selhtml);
                }
                // 班级联动
                var orgCode = $('#editFacultyName option:selected').val();
                personInfoPopup.orgClassChange(orgCode);
            }
            personInfoPopup.form.render('select');
        },
        eClear: function () {
            personInfoPopup.regionId = null;
        },
        eAjaxSuccess: function (data) {//ajax请求模式，请求成功后的数据处理回调
            //personInfoPopup.allRegion = data.data.list;
            return data.data;
        }
    });
    $('#editPersonRegionPopwin').val(regionId);
    $('#editPersonRegionPopwin').selectPageRefresh();
    // 获取组织机构树的数据
    personInfoPopup.getOrgData(personInfoPopup.regionId);
    // 获取园区下的所有老师信息
    personInfoPopup.getTeacherListByOrgCode(personInfoPopup.regionId)
}

/*获取人员对应联系方式类型-编辑*/
personInfoPopup.getTelType = function (addressBookList) {
    if (addressBookList.length > 0) {
        for (var i = 0; i < addressBookList.length; i++) {
            var Type = addressBookList[i].contactType;
            $("#editAddressTypeSel").val(Type);
            var childType = addressBookList[i].childType;
            $("#editAddressChildTypeSel").val(childType);
        }
        //多联系方式回显
        personInfoPopup.contactEcho(addressBookList);
    }
}

/*多联系方式 按顺序回显*/
personInfoPopup.contactEcho = function (data) {
    if (data.length > 0) {
        for (var k = 0; k < data.length; k++) {
            for (var i = 1; i <= data.length; i++) {
                if (data[i - 1].rank == k) {

                    var html = '<div class="layui-card-body" id="editContactCard' + i + '">';
                    html += '<table class="addPersonInfo-table">';
                    html += '<colgroup>';
                    html += '<col width="15%">';
                    html += '<col width="30%">';
                    html += '<col width="15%">';
                    html += '<col width="30%">';
                    html += '<col>';
                    html += '</colgroup>';
                    html += '<tbody>';
                    html += '<tr>';
                    html += '<td><label class="layui-form-label layui-form-required">联系方式:</label></td>';
                    html += '<td>';
                    html += '<div class="layui-input-block">';
                    html += '<select id="editAddressTypeSel' + i + '">';
                    html += '</select>';
                    html += '</div>';
                    html += '</td>';
                    html += '<td><label class="layui-form-label layui-form-required">联系类别:</label></td>';
                    html += '<td>';
                    html += '<div class="layui-input-block">';
                    html += '<select id="editAddressChildTypeSel' + i + '">';
                    html += '</select>';
                    html += '</div>';
                    html += '</td>';
                    if (i == 1) {
                        html += '<td><i id="editPersonDelPhone' + i + '" class="layui-icon layui-icon-close-fill color-red" name="" onclick="personInfoPopup.delPhone(this)" style="display: none"></i></td>';
                    } else {
                        html += '<td><i id="editPersonDelPhone' + i + '" class="layui-icon layui-icon-close-fill color-red" name="" onclick="personInfoPopup.delPhone(this)" ></i></td>';
                    }
                    html += ' </tr>';
                    html += '<tr>';
                    html += '<td><label class="layui-form-label layui-form-required">联系电话:</label></td>';
                    html += '<td>';
                    html += '<div class="layui-input-block"><input id="editPersonPhone' + i + '" name="" class="layui-input" placeholder=""/></div>';
                    html += '</td>';
                    html += '</tr>';
                    html += '</tbody>';
                    html += '</table>';
                    html += '</div>';
                    $("#editContactCard").append(html);
                    //$("#editPersonCardType"+i).val(papeWorkList[i-1].type);

                    //联系方式
                    for (var j = 0; j < personInfoPopup.StaticEnumData.contactTypeList.length; j++) {
                        selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactTypeList[j].code + ">" + personInfoPopup.StaticEnumData.contactTypeList[j].name + "</option>"
                        $("#editAddressTypeSel" + i).append(selhtml);
                    }
                    $("#editPersonDelPhone" + i).val("editContactCard" + i);
                    $("#editPersonDelPhone" + i).attr("name", data[i - 1].personId);
                    $("#editAddressTypeSel" + i).val(data[i - 1].contactType);
                    //联系方式类型
                    for (var j = 0; j < personInfoPopup.StaticEnumData.contactChildTypeList.length; j++) {
                        selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactChildTypeList[j].code + ">" + personInfoPopup.StaticEnumData.contactChildTypeList[j].name + "</option>"
                        $("#editAddressChildTypeSel" + i).append(selhtml);
                    }
                    $("#editAddressChildTypeSel" + i).val(data[i - 1].childType)

                    $("#editPersonPhone" + i).val(data[i - 1].concretenessInfo)

                    personInfoPopup.phoneList.push("editContactCard" + i);
                    break;
                }
            }
            personInfoPopup.editContactNum = data.length;
        }
    }
}

/*新增默认联系方式框*/
personInfoPopup.contactEchoAdd = function () {
    personInfoPopup.editContactNum = 0;
    personInfoPopup.editContactNum++;
    var html = '<div class="layui-card-body" id="editContactCard' + personInfoPopup.editContactNum + '">';
    html += '<table class="addPersonInfo-table">';
    html += '<colgroup>';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col>';
    html += '</colgroup>';
    html += '<tbody>';
    html += '<tr>';
    html += '<td><label class="layui-form-label layui-form-required">联系方式:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block">';
    html += '<select disabled id="editAddressTypeSel' + personInfoPopup.editContactNum + '">';
    html += '</select>';
    html += '</div>';
    html += '</td>';
    html += '<td><label class="layui-form-label layui-form-required">联系类别:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block">';
    html += '<select id="editAddressChildTypeSel' + personInfoPopup.editContactNum + '">';
    html += '</select>';
    html += '</div>';
    html += '</td>';
    html += '<td><i id="editPersonDelPhone' + personInfoPopup.editContactNum + '" class="layui-icon layui-icon-close-fill color-red" onclick="personInfoPopup.delPhone(this)" style="display: none"></i></td>';
    html += ' </tr>';
    html += '<tr>';
    html += '<td><label class="layui-form-label layui-form-required">联系电话:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block"><input id="editPersonPhone' + personInfoPopup.editContactNum + '" name="" class="layui-input" placeholder=""/></div>';
    html += '</td>';
    html += '</tr>';
    html += '</tbody>';
    html += '</table>';
    html += '</div>';
    $("#editContactCard").append(html);
    personInfoPopup.phoneList.push("editContactCard" + personInfoPopup.editContactNum);
    //联系方式
    for (var j = 0; j < personInfoPopup.StaticEnumData.contactTypeList.length; j++) {
        selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactTypeList[j].code + ">" + personInfoPopup.StaticEnumData.contactTypeList[j].name + "</option>"
        $("#editAddressTypeSel" + personInfoPopup.editContactNum).append(selhtml);
    }
    //联系方式类型
    for (var j = 0; j < personInfoPopup.StaticEnumData.contactChildTypeList.length; j++) {
        selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactChildTypeList[j].code + ">" + personInfoPopup.StaticEnumData.contactChildTypeList[j].name + "</option>"
        $("#editAddressChildTypeSel" + personInfoPopup.editContactNum).append(selhtml);
    }
};



/*动态拼接证件修改框*/
personInfoPopup.addCertificateCard = function () {
    //modify by lzt添加之前要创建数组副本，校验使用，每次修改时也需要维护数组副本，删除时也需要维护数组副本
    personInfoPopup.idCardList.forEach(function (item) {
        personInfoPopup.idCardListCopy.push(item);
    })

    var curType = 0;
    if (personInfoPopup.cardTypeArr.length == personInfoPopup.idCardList.length) {
        layer.alert("证件类型已为最大值");
        return;
    }
    for (var i = 0; i < personInfoPopup.cardTypeArr.length; i++) {
        var iszai = false;
        for (var j = 0; j < personInfoPopup.idCardList.length; j++) {
            //var index = personInfoPopup.idCardList[j].indexOf(str);
            //var str = "asdf1";
            var search = personInfoPopup.idCardList[j].search(/\d/)
            var index = personInfoPopup.idCardList[j].substr(search);
            if (index == personInfoPopup.cardTypeArr[i]) {
                iszai = true;
            }
        }
        if (iszai) {
            continue;
        } else {
            personInfoPopup.editCertificateNum = personInfoPopup.cardTypeArr[i];
            break;
        }
    }
    //外层循环，已存在的证件框
    for (var i = 0; i < personInfoPopup.cardTypeArr.length; i++) {
        //证件类型（int）
        var isExts = false;
        //内层循环，全部证件类型
        for (var j = 0; j < personInfoPopup.idCardList.length; j++) {
            var index = personInfoPopup.idCardList[j].substr(personInfoPopup.idCardList[j].length - 1, 1);
            //判断改类型是否存在已存在集合内
            if (personInfoPopup.cardTypeArr[i] == $("#editPersonCardType" + index).val()) {
                isExts = true;
                break
            }
        }
        if (!isExts) {
            curType = personInfoPopup.cardTypeArr[i];
            break;
        }
    }
    var html = '<div class="layui-card-body" id="editCertificateCard' + personInfoPopup.editCertificateNum + '">';
    html += '<table class="addPersonInfo-table">';
    html += '<colgroup>';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col>';
    html += '</colgroup>';
    html += '<tbody>';
    html += ' <tr>';
    html += '<td><label class="layui-form-label layui-form-required">证件类型:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block" onclick="personInfoPopup.BeforeSwitch(this)">';
    html += '<select  id="editPersonCardType' + personInfoPopup.editCertificateNum + '" data-method="'+personInfoPopup.editCertificateNum+'" lay-filter="IDCard">';
    html += '</select>';
    html += '</div>';
    html += '</td>';
    html += '<td><label class="layui-form-label layui-form-required">证件号:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block"><input id="editPersonIdCard' + personInfoPopup.editCertificateNum + '" name="editPersonCardType' + personInfoPopup.editCertificateNum + '" class="layui-input" placeholder="" onchange="personInfoPopup.verifyUniqueIDCard(\'editPersonIdCard\' + personInfoPopup.editCertificateNum)"/></div>';
    html += '</td>';
    html += ' <td><i id="editPersonIdCardDel' + personInfoPopup.editCertificateNum + '"  class="layui-icon layui-icon-close-fill color-red" onclick="personInfoPopup.removeCard(this)" ></i></td>';
    html += ' </tr>';
    html += '<tr>';
    html += '<td><label class="layui-form-label">是否默认:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block">';
    html += '<input id="paperworkRadio' + personInfoPopup.editCertificateNum + '" type="radio" name="yes"  value="' + personInfoPopup.editCertificateNum + '" lay-filter="ChoiceRadio">';
    html += '</div>';
    html += '</td>';
    html += '</tr>';
    html += '</tbody>';
    html += '</table>';
    html += '</div>';
    $("#editCertificateCard").append(html);

    //证件类型 CardType
    personInfoPopup.idCardList.push('editCertificateCard' + personInfoPopup.editCertificateNum);
    //add by lzt 每次添加成功，就记录该类型的证件框已经存在 删除的时候也要记录对应类型的证件框已经删除
    if(personInfoPopup.editCertificateNum == 1){
        personInfoPopup.idCard1 = true;
    }else if(personInfoPopup.editCertificateNum == 2){
        personInfoPopup.idCard2 = true;
    }else if(personInfoPopup.editCertificateNum == 3){
        personInfoPopup.idCard3 = true;
    }else if(personInfoPopup.editCertificateNum == 4){
        personInfoPopup.idCard4 = true;
    }else if(personInfoPopup.editCertificateNum == 5){
        personInfoPopup.idCard5 = true;
    }else {
        personInfoPopup.idCard6 = true;
    }

    $("#editPersonIdCardDel" + personInfoPopup.editCertificateNum).val('editCertificateCard' + personInfoPopup.editCertificateNum);
    for (var i = 0; i < personInfoPopup.StaticEnumData.cardType.length; i++) {
        selhtml = "<option value=" + personInfoPopup.StaticEnumData.cardType[i].id + ">" + personInfoPopup.StaticEnumData.cardType[i].name + "</option>"
        if (curType == personInfoPopup.StaticEnumData.cardType[i].id) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.cardType[i].id + " selected>" + personInfoPopup.StaticEnumData.cardType[i].name + "</option>"
        }
        $("#editPersonCardType" + personInfoPopup.editCertificateNum).append(selhtml);
    }

    //personInfoPopup.map['idCard1'] = 1;
    //modify by lzt 每次添加时用每个对应的遍历记录对应的值
    if(personInfoPopup.editCertificateNum == 2){
        personInfoPopup.map['idCard2'] = 2;
    }else if (personInfoPopup.editCertificateNum == 3){
        personInfoPopup.map['idCard3'] = 3;
    }else if (personInfoPopup.editCertificateNum == 4){
        personInfoPopup.map['idCard4'] = 4;
    }else if (personInfoPopup.editCertificateNum == 5){
        personInfoPopup.map['idCard5'] = 5;
    }else {
        personInfoPopup.map['idCard6'] = 6;
    }
    //alert(JSON.stringify(personInfoPopup.map));

    personInfoPopup.form.render();
    personInfoPopup.form.render('select');
};

// 证件类型下拉框 点击的时候 记录下拉框改变之前的值  和所有证件类型的ID
personInfoPopup.BeforeSwitch = function(obj){
    personInfoPopup.backOffCarVal = $(obj).children(":first").val()
    personInfoPopup.BeforeSwitchCardTypeArr = [];
    let $lay = $('[lay-filter="IDCard"]');

    for (let i = 0; i < $lay.length; i++) {
        personInfoPopup.BeforeSwitchCardTypeArr.push($('#'+$lay[i].attributes.id.value).val())
    }
    console.log(personInfoPopup.BeforeSwitchCardTypeArr)
}

/*验证证件唯一性*/
personInfoPopup.verifyOnly = function (id) {
    var name = $("#" + id).val();
    var index = id.substring(id.length - 1);
    var type = $("#editPersonCardType" + index).val();
    if (type == 1) {
        var r = personInfoPopup.idCardReg.test(name);
        if (!r || $("#editPersonIdCard" + index) == "") {
            layer.msg("身份证格式错误", {icon: 2, time: 3000});
            return;
        }
    }
    personInfoPopup.admin.ajax({
        url: conf.dsrpBaseServer + "personInfo/verifyOnly",
        type: "post",
        data: {cardId: name, type: type},
        dataType: "json",
        async: true,
        success: function (data) {
            if (data.code != 200) {
                layer.msg(data.msg, {icon: 2, time: 5000});
                return;
            }
        },
        error: function (data) {
            layer.msg("证件验证失败", {icon: 2, time: 5000});
        }
    })


}

/*删除动态拼接的证件框*/
personInfoPopup.removeCard = function (obj) {
    let divId = $(obj).parents('table').parent().attr("id");
    for (var i = 0; i < personInfoPopup.idCardList.length; i++) {
        if (personInfoPopup.idCardList[i] == divId) {
            var isE = $('input[name="yes"]:checked').val();
            var index = personInfoPopup.idCardList[i].substr(personInfoPopup.idCardList[i].length - 1, 1);
            //删除的证件刚好是默认的
            if (isE == index) {
                $("#editCertificateCard input[name=\"yes\"]").prop("checked", true);
                personInfoPopup.form.render();
            }
            $("#" + divId).remove();

            //遍历六个元素，找到对应的值，进行置位操作
            for(var key in personInfoPopup.map){
                if(index == personInfoPopup.map[key]){
                    delete personInfoPopup.map[key];
                }
            }
            //并将该类型对应的证件框是否存在置为false
            if(index == 1){
                personInfoPopup.idCard1 = false;
            }else if(index == 2){
                personInfoPopup.idCard2 = false;
            }else if(index == 3){
                personInfoPopup.idCard3 = false;
            }else if(index == 4){
                personInfoPopup.idCard4 = false;
            }else if(index == 5){
                personInfoPopup.idCard5 = false;
            }else {
                personInfoPopup.idCard6 = false;
            }

            personInfoPopup.idCardList.splice(i, 1);
        }
    }

}

/*动态拼接联系方式修改框*/
personInfoPopup.addContactCard = function () {
    personInfoPopup.editContactNum++;
    var html = '<div class="layui-card-body" id="editContactCard' + personInfoPopup.editContactNum + '">';
    html += '<table class="addPersonInfo-table">';
    html += '<colgroup>';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col width="15%">';
    html += '<col width="30%">';
    html += '<col>';
    html += '</colgroup>';
    html += '<tbody>';
    html += '<tr>';
    html += '<td><label class="layui-form-label layui-form-required">联系方式:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block">';
    html += '<select onchange="personInfoPopup.addressTypeChange(this)" id="editAddressTypeSel' + personInfoPopup.editContactNum + '">';
    html += '</select>';
    html += '</div>';
    html += '</td>';
    html += '<td><label class="layui-form-label layui-form-required">联系类别:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block">';
    html += '<select id="editAddressChildTypeSel' + personInfoPopup.editContactNum + '">';
    html += '</select>';
    html += '</div>';
    html += '</td>';
    html += '<td><i id="editPersonDdelPhone' + personInfoPopup.editContactNum + '" class="layui-icon layui-icon-close-fill color-red" onclick="personInfoPopup.delPhone(this)"></i></td>';
    html += ' </tr>';
    html += '<tr>';
    html += '<td><label class="layui-form-label layui-form-required">联系电话:</label></td>';
    html += '<td>';
    html += '<div class="layui-input-block"><input id="editPersonPhone' + personInfoPopup.editContactNum + '" name="" class="layui-input" placeholder=""/></div>';
    html += '</td>';
    html += '</tr>';
    html += '</tbody>';
    html += '</table>';
    html += '</div>';
    $("#editContactCard").append(html);
    personInfoPopup.phoneList.push("editContactCard" + personInfoPopup.editContactNum);
    $("#editPersonDdelPhone" + personInfoPopup.editContactNum).val("editContactCard" + personInfoPopup.editContactNum);
    //填充联系方式数据
    for (var i = 0; i < personInfoPopup.StaticEnumData.contactTypeList.length; i++) {
        selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactTypeList[i].code + ">" + personInfoPopup.StaticEnumData.contactTypeList[i].name + "</option>"
        $("#editAddressTypeSel" + personInfoPopup.editContactNum).append(selhtml);
    }
    //联系方式类型
    for (var j = 0; j < personInfoPopup.StaticEnumData.contactChildTypeList.length; j++) {
        selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactChildTypeList[j].code + ">" + personInfoPopup.StaticEnumData.contactChildTypeList[j].name + "</option>"
        $("#editAddressChildTypeSel" + personInfoPopup.editContactNum).append(selhtml);
    }
    personInfoPopup.form.render('select');
}

/*动态删除联系方式框*/
personInfoPopup.delPhone = function (obj) {
    // 删除证件信息Html元素
    for (var i = 0; i < personInfoPopup.phoneList.length; i++) {
        if (personInfoPopup.phoneList[i] == obj.value) {
            $("#" + obj.value).remove();
            personInfoPopup.phoneList.splice(i, 1);
        }
    }
}

var workHtml = "    <form id=\"addPersonInfoEditForm\" lay-filter=\"addPersonInfoEditForm\"\n" +
    "          class=\"layui-form model-form addPersonInfoEditForm\">\n" +
    "        <input name=\"planId\" type=\"hidden\"/>\n" +
    "        <div class=\"layui-form toolbar\">\n" +
    "            <div class=\"layui-form-item\">\n" +
    "                <label class=\"layui-form-label\">选择人员:</label>\n" +
    "                <div class=\"layui-input-block\">\n" +
    "                    <select id=\"editPersonProfession\" lay-search=\"\">\n" +
    "                        <option value=\"1\">学生</option>\n" +
    "                        <option value=\"2\">教师</option>\n" +
    "                        <option value=\"3\">职工</option>\n" +
    "                        <option value=\"4\">安保</option>\n" +
    "                    </select>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"model-form-footer\">\n" +
    "            <button class=\"layui-btn\" type=\"button\" lay-filter=\"\" lay-submit\n" +
    "                    onclick=\"personInfoPopup.addProfessionSubmit()\">添加\n" +
    "            </button>\n" +
    "        </div>\n" +
    "    </form>";

/*添加职业弹窗*/
personInfoPopup.addProfession = function () {
    personInfoPopup.editProfessionIndex = personInfoPopup.admin.open({
        type: 1,
        title: '人员信息',
        area: ['400px', '300px'],
        content: workHtml,
        success: function (layero) {
        }
    });
    personInfoPopup.form.render('select');
}

/*添加职业弹窗*/
personInfoPopup.addProfessionSubmit = function () {
    //下拉框选择职业
    var val = $("#editPersonProfession").val();
    //打开对应职业编辑窗口
    personInfoPopup.openProfessionDislog(val);
    layer.close(personInfoPopup.editProfessionIndex);
}

/*打开对应职业查看窗口*/
personInfoPopup.openProfessionDislogCk = function (data, type) {
    var editSecurityItemHtml = '<div><div><h1>zzq</h1></div><div><a href="https://www.baidu.com">tdos</a></div></div>';
    var teacherHtmlCk = ' <table class="layui-table addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="12%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="12%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">院系:</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="teacherClassId">' + data.classId + '</span></td>\n' +
        '                                            <td><label class="layui-form-label">等级:</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="teacherlevel">' + data.levelName + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">教师状态:</label></td>\n' +
        '                                            <td colspan="3"><span class="layui-input-block" id="teacherStatus">' + data.statusName + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    var studentHtmlCk = '<table class="layui-table addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="12%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="12%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">学号:</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="studentNum">' + data.studentNum + '</span></td>\n' +
        '                                            <td><label class="layui-form-label">院系：</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="facultyName">' + data.facultyName + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">班级:</label></td>\n' +
        '                                            <td><span class="layui-input-block"  id="className">' + data.className + '</span></td>\n' +
        '                                            <td><label class="layui-form-label">入学时间：</label></td>\n' +
        '                                            <td><span class="layui-input-block"  id="inTime" value="">' + (data.inTime==null? '':data.inTime) + '</td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">专业:</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="specialty">' + data.specialty + '</span></td>\n' +
        '                                            <td><label class="layui-form-label">年级：</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="StudentGradle">' + data.studentGrade + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label" >辅导员:</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="staffGuid">' + data.staffName + '</span></td>\n' +
        '                                            <td><label class="layui-form-label">导师：</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="teacherGuid">' + data.teacherName + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">培养层次:</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="eduYear"></span>' + data.eduYear + '</td>\n' +
        '                                            <td><label class="layui-form-label">学制：</label></td>\n' +
        '                                            <td><span class="layui-input-block" id="studentType">' + data.studentTypeName + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">学籍状态:</label></td>\n' +
        '                                            <td colspan="3"><span class="layui-input-block" id="status">' + data.studentStatus + '</span></td>\n' +
        '                                        </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    var staffHtmlCk = '  <table class="layui-table addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="12%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="12%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">部门:</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="orgId">' + data.orgName + '</span></td>\n' +
        '                                        <td><label class="layui-form-label">职位：</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="jobGradle">' + data.jobGrade + '</span></td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">入职时间:</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="startTime">' + (data.startTime==null? '':data.startTime) + '</span></td>\n' +
        '                                        <td><label class="layui-form-label">工号：</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="workNum">' + data.workNum + '</span></td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">职员类型:</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="staffType">' + data.staffTypeName + '</span></td>\n' +
        '                                        <td><label class="layui-form-label">职工状态:</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="staffStatus">' + data.statusName + '</span></td>\n' +
        '                                    </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    var secriutyHtmlCk = ' <table class="layui-table addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="12%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="12%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">职位:</label></td>\n' +
        '                                        <td><span class="layui-input-block" id="securityName">' + data.securityName + '</span></td>\n' +
        '                                    </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    switch (type) {
        //学生
        case "1":

            personInfoPopup.element.tabAdd('personInfoCk', {
                title: '学生', //用于演示
                content: studentHtmlCk,
                id: 1 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //切换到指定Tab项
                personInfoPopup.element.tabChange('personInfoCk', '1'); //切换到：用户管理
            //执行一个laydate实例
            personInfoPopup.laydate.render({
                elem: '#addInTime', //指定元素
                value: data.inTime,
                change: function (value, date) { //监听日期被切换
                    $("#addInTime").val(date);
                }
            });
            break;
        //教师
        case "2":
            personInfoPopup.element.tabAdd('personInfoCk', {
                title: '教师', //用于演示
                content: teacherHtmlCk,
                id: 2 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //切换到指定Tab项
                personInfoPopup.element.tabChange('personInfoCk', '2'); //切换到：用户管理
            break;
        //职工
        case "3":
            personInfoPopup.element.tabAdd('personInfoCk', {
                title: '职工', //用于演示
                content: staffHtmlCk,
                id: 3 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //切换到指定Tab项
                personInfoPopup.element.tabChange('personInfoCk', '3'); //切换到：用户管理
            break;
        //保安
        case "4":
            personInfoPopup.element.tabAdd('personInfoCk', {
                title: '安保', //用于演示
                content: secriutyHtmlCk,
                id: 4 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //切换到指定Tab项
                personInfoPopup.element.tabChange('personInfoCk', '4'); //切换到：用户管理
            break;
    }
    personInfoPopup.form.render('select');
}

/*打开对应职业编辑窗口 -新增 编辑*/
personInfoPopup.openProfessionDislog = function (type) {
    var teacherHtml = '<table id="teacherCardTab" class="addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">院系:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block"><input id="editYunaxi" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                            </td>\n' +
        '                                            <td><label class="layui-form-label">等级:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block">\n' +
        '                                                    <select id="editTecherType">\n' +
        '                                                    </select>\n' +
        '                                                </div>\n' +
        '                                            </td>\n' +
        '                                            <td></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">教师状态:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block">\n' +
        '                                                    <select id="editTecherStatus">\n' +
        '                                                    </select>\n' +
        '                                                </div>\n' +
        '                                            </td>\n' +
        '                                        </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    var studentHtml = '<table  id="studentCardTab" class="addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label" >学号:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block"><input id="editStudentNum" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                        </td>\n' +
        '                                        <td><label class="layui-form-label">院系:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block" >\n' +
        '                                                <select id="editFacultyName"  lay-filter="Faculty"> \n' +
        '                                                </select>\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                        <td></td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label" id="editClassName">班级:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block" >\n' +
        '                                                <select id="editClass" lay-filter="QMID">\n' +
        '                                                </select>\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                        <td><label class="layui-form-label">入学时间:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-inline">\n' +
        '                                                <input  type="text" class="layui-input icon-date" id="editInTime"  placeholder="yyyy-MM-dd">\n' +
        '                                               <!-- <input id="editInTime" type="text" class="layui-input" id="test1" placeholder="yyyy-MM-dd">\n' +
        '                                                <input id="editInTime" name="entranceDateSel" class="layui-input icon-date" placeholder=""/></div>-->\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">专业:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block"><input id="editSpecialty" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                        </td>\n' +
        '                                        <td><label class="layui-form-label">年级:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block"><input id="editStudentGradle" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                        </td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">辅导员:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block">\n' +
        '                                                <select id="editStaffGuid">\n' +
        '                                                </select>\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                        <td><label class="layui-form-label">导师:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block">\n' +
        '                                                <select id="editTeacherGuid">\n' +
        '                                                </select>\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">培养层次:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block"><input id="editEduYear" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                        </td>\n' +
        '                                        <td><label class="layui-form-label">学制:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block">\n' +
        '                                                <select id="editStudentType">\n' +
        '                                                </select>\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                    </tr>\n' +
        '                                    <tr>\n' +
        '                                        <td><label class="layui-form-label">学籍状态:</label></td>\n' +
        '                                        <td>\n' +
        '                                            <div class="layui-input-block">\n' +
        '                                                <select id="editStatus">\n' +
        '                                                </select>\n' +
        '                                            </div>\n' +
        '                                        </td>\n' +
        '                                    </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    var staffHtml = ' <table   id="staffCardTab" class="addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">部门:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block">\n' +
        '                                                    <select id="editOrgId">\n' +
        '                                                    </select>\n' +
        '                                                </div>\n' +
        '                                            </td>\n' +
        '                                            <td><label class="layui-form-label">职位:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block"><input id="editJobGradle" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                            </td>\n' +
        '                                            <td></td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">工号:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block"><input id="editWorkNum" name="" class="layui-input" placeholder=""/></div>\n' +
        '                                            </td>\n' +
        '                                            <td><label class="layui-form-label">入职时间:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block"><input id="editStartTime" name="entryDateSel" class="layui-input icon-date" placeholder=""/></div>\n' +
        '                                            </td>\n' +
        '                                        </tr>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">职员类型:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block">\n' +
        '                                                    <select id="editStaffType">\n' +
        '                                                    </select>\n' +
        '                                                </div>\n' +
        '                                            </td>\n' +
        '                                            <td><label class="layui-form-label">职工状态:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block">\n' +
        '                                                    <select id="editStaffStatus">\n' +
        '                                                    </select>\n' +
        '                                                </div>\n' +
        '                                            </td>\n' +
        '                                        </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    var secriutyHtml = ' <table   id="secriutyCardTab" class="addPersonInfo-table">\n' +
        '                                    <colgroup>\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col width="15%">\n' +
        '                                        <col width="30%">\n' +
        '                                        <col>\n' +
        '                                    </colgroup>\n' +
        '                                    <tbody>\n' +
        '                                        <tr>\n' +
        '                                            <td><label class="layui-form-label">职位:</label></td>\n' +
        '                                            <td>\n' +
        '                                                <div class="layui-input-block">\n' +
        '                                                    <select id="editSecurityName">\n' +
        '                                                    </select>\n' +
        '                                                </div>\n' +
        '                                            </td>\n' +
        '                                            <td></td>\n' +
        '                                            <td></td>\n' +
        '                                            <td></td>\n' +
        '                                        </tr>\n' +
        '                                    </tbody>\n' +
        '                                </table>';
    switch (type) {
        //学生
        case "1":
            if ($("#studentCardTab").length != 0) {
                layer.alert("学生职业已存在");
                return;
            }
            personInfoPopup.element.tabAdd('demo', {
                title: '学生', //用于演示
                content: studentHtml,
                id: 1 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //执行一个laydate实例
                personInfoPopup.laydate.render({
                    elem: '#editInTime', //指定元素
                    change: function (value, date) { //监听日期被切换
                    }
                });
            //切换到指定Tab项
            personInfoPopup.element.tabChange('demo', '1'); //切换
            personInfoPopup.TeacherEnumSetData();
            break;
        //教师
        case "2":
            if ($("#teacherCardTab").length != 0) {
                layer.alert("教师职业已存在");
                return;
            }
            personInfoPopup.element.tabAdd('demo', {
                title: '教师', //用于演示
                content: teacherHtml,
                id: 2 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //切换到指定Tab项
                personInfoPopup.element.tabChange('demo', '2'); //切换
            personInfoPopup.TeacherEnumSetData();
            //personInfoPopup.getAllRegion();
            break;
        //职工
        case "3":
            if ($("#staffCardTab").length != 0) {
                layer.alert("职工职业已存在");
                return;
            }
            personInfoPopup.element.tabAdd('demo', {
                title: '职工', //用于演示
                content: staffHtml,
                id: 3 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                personInfoPopup.laydate.render({
                    elem: '#editStartTime', //指定元素
                    change: function (value, date) { //监听日期被切换
                    }
                });
            //切换到指定Tab项
            personInfoPopup.element.tabChange('demo', '3'); //切换
            personInfoPopup.TeacherEnumSetData();
            //personInfoPopup.getAllRegion();
            break;
        //保安
        case "4":
            if ($("#secriutyCardTab").length != 0) {
                layer.alert("安保职业已存在");
                return;
            }
            personInfoPopup.element.tabAdd('demo', {
                title: '安保', //用于演示
                content: secriutyHtml,
                id: 4 //实际使用一般是规定好的id，这里以时间戳模拟下
            }),
                //切换到指定Tab项
                personInfoPopup.element.tabChange('demo', '4'); //切换
            personInfoPopup.TeacherEnumSetData();
            //personInfoPopup.getAllRegion();
            break;
    }
    personInfoPopup.form.render('select');
}

/*通讯录 职业下拉框渲染*/
personInfoPopup.TeacherEnumSetData = function () {
    //教师在职状态下拉赋值
    if (personInfoPopup.StaticEnumData.statusList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.statusList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.statusList[i].code + ">" + personInfoPopup.StaticEnumData.statusList[i].name + "</option>"
            $("#editTecherStatus").append(selhtml);
        }
    }

    //教师等级下拉赋值
    if (personInfoPopup.StaticEnumData.levelList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.levelList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.levelList[i].code + ">" + personInfoPopup.StaticEnumData.levelList[i].name + "</option>"
            $("#editTecherType").append(selhtml);
        }
    }
    //职工在职状态下拉赋值
    if (personInfoPopup.StaticEnumData.staffStatusList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.staffStatusList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.staffStatusList[i].code + ">" + personInfoPopup.StaticEnumData.staffStatusList[i].name + "</option>"
            $("#editStaffStatus").append(selhtml);
        }
    }
    //职工类型下拉赋值
    if (personInfoPopup.StaticEnumData.staffTypeList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.staffTypeList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.staffTypeList[i].code + ">" + personInfoPopup.StaticEnumData.staffTypeList[i].name + "</option>"
            $("#editStaffType").append(selhtml);
        }
    }
    //学生在校态下拉赋值
    if (personInfoPopup.StaticEnumData.studentStatusList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.studentStatusList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.studentStatusList[i].code + ">" + personInfoPopup.StaticEnumData.studentStatusList[i].name + "</option>"
            $("#editStatus").append(selhtml);
        }
    }
    //学生学制下拉赋值
    if (personInfoPopup.StaticEnumData.studentTypeList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.studentTypeList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.studentTypeList[i].code + ">" + personInfoPopup.StaticEnumData.studentTypeList[i].name + "</option>"
            $("#editStudentType").append(selhtml);
        }
    }
    //联系方式赋值
    if (personInfoPopup.StaticEnumData.contactTypeList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.contactTypeList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactTypeList[i].code + ">" + personInfoPopup.StaticEnumData.contactTypeList[i].name + "</option>"
            $("#editAddressTypeSel").append(selhtml);
        }
    }
    //联系方式类型赋值
    if (personInfoPopup.StaticEnumData.contactChildTypeList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.contactChildTypeList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.contactChildTypeList[i].code + ">" + personInfoPopup.StaticEnumData.contactChildTypeList[i].name + "</option>"
            $("#editAddressChildTypeSel").append(selhtml);
        }
    }
    //学生院系
    if (personInfoPopup.orgList.data && personInfoPopup.orgList.data.length != 0) {
        var selhtml = '';
        for (var i = 0; i < personInfoPopup.orgList.data.length; i++) {
            selhtml += "<option value=" + personInfoPopup.orgList.data[i].id + ">" + personInfoPopup.orgList.data[i].orgName + "</option>"
        }
        if($("#editFacultyName").find("option").length == 0){
            $("#editFacultyName").append(selhtml);
            // 班级联动
            var orgCode = $('#editFacultyName option:selected').val();
            personInfoPopup.orgClassChange(orgCode);
        }
        if($("#editOrgId").find("option").length == 0){
            //职工部门下拉赋值
            $("#editOrgId").append(selhtml);
        }

    }

    //安保人员赋值
    if (personInfoPopup.StaticEnumData.securityTypeTypeList.length != 0) {
        for (var i = 0; i < personInfoPopup.StaticEnumData.securityTypeTypeList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.StaticEnumData.securityTypeTypeList[i].code + ">" + personInfoPopup.StaticEnumData.securityTypeTypeList[i].name + "</option>"
            $("#editSecurityName").append(selhtml);
        }
    }
    //导师、辅导员人员下拉赋值
    if (personInfoPopup.teacherList != null && personInfoPopup.teacherList.length != 0) {
        for (var i = 0; i < personInfoPopup.teacherList.length; i++) {
            selhtml = "<option value=" + personInfoPopup.teacherList[i].personId + ">" + personInfoPopup.teacherList[i].name + "</option>"
            $("#editTeacherGuid").append(selhtml);
            $("#editStaffGuid").append(selhtml);
        }
    }
    personInfoPopup.form.render('select');
}

/*提交编辑人员信息*/
personInfoPopup.editPersonInfo = function () {
    //添加加载信息
    let loadix = personInfoPopup.layer.load(1, {shade: [0.1,'#fff']});

    // personInfoPopup.editPerisonInfo={};//对象
    personInfoPopup.editPerisonInfo.securityInfo = null;
    personInfoPopup.editPerisonInfo.teacherInfo = null;
    personInfoPopup.editPerisonInfo.staffInfo = null;
    personInfoPopup.editPerisonInfo.studentInfo = null;
    personInfoPopup.editPerisonInfo.loginName = null;
    personInfoPopup.editPerisonInfo.loginPass = null;
    if(personInfoPopup.isNeedAccount){
        personInfoPopup.editPerisonInfo.loginName = $('#loginName').val();
        personInfoPopup.editPerisonInfo.loginPass = $('#password').val();

        personInfoPopup.verifyLoginName();  //如果含有账户信息，校验账户信息
        if(!personInfoPopup.checkLoginName){
            personInfoPopup.layer.close(loadix); return;
        }
        personInfoPopup.verifyTwicePassword();  //校验两次密码是否一致
        if(!personInfoPopup.checkLoginPwd){
            personInfoPopup.layer.close(loadix); return;
        }
        personInfoPopup.verifyPwdFormat();      //校验密码格式
        if(!personInfoPopup.checkPwdFormat){
            personInfoPopup.layer.close(loadix); return;
        }
    }
    var papeWorkList = [];//证件集合
    var addressBookList = [];//联系方式集合
    var professionList = [];//职业集合
    if (personInfoPopup.personObj != null) {
        personInfoPopup.editPerisonInfo.personId = personInfoPopup.personObj.id;
    }
    personInfoPopup.editPerisonInfo.security = null;
    personInfoPopup.editPerisonInfo.techer = null;
    personInfoPopup.editPerisonInfo.staff = null;
    personInfoPopup.editPerisonInfo.student = null;
    //窗口内数据赋值
    //人员名字
    // if ($("#editPersonName").val() == "") {
    //     layer.msg("用户名不能为空", {icon: 2, time: 3000});
    //     personInfoPopup.layer.close(loadix); return;
    // }
    // 来源
    personInfoPopup.editPerisonInfo.sourcePlatform = 'dpsim';
    personInfoPopup.editPerisonInfo.name = $("#editPersonName").val();
    //宗教信仰
    personInfoPopup.editPerisonInfo.religion = $("#editPersonReligion").val();
    //国籍
    personInfoPopup.editPerisonInfo.nationality = $("#editPersonNationality").val();
    //籍贯
    personInfoPopup.editPerisonInfo.nativePlace = $("#editPersonPlace").val();
    //民族
    personInfoPopup.editPerisonInfo.nation = $("#editPersonNation").val();
    //血型
    personInfoPopup.editPerisonInfo.bloodType = $("#editPersonBooldType").val();
    //性别
    personInfoPopup.editPerisonInfo.gender = $("#editPersonGender").val();
    //政治面貌
    personInfoPopup.editPerisonInfo.politicalStatus = $("#editPersonPoliticalStatus").val();
    //所属园区(学校)
    personInfoPopup.editPerisonInfo.regionId = $("#editPersonRegionPopwin").val();
    //主控园区(学校)
    personInfoPopup.editPerisonInfo.masterRegion = personInfoPopup.regionId;
    //没有添加额外证件
    if (personInfoPopup.editCertificateNum == 0) {
        personInfoPopup.layer.msg("证件不能为空，至少一种", {icon: 2, time: 3000});
        personInfoPopup.layer.close(loadix); return;
    } else {
        if (personInfoPopup.idCardList.length == 0) {
            personInfoPopup.layer.msg("证件不能为空，至少一种", {icon: 2, time: 3000});
            personInfoPopup.layer.close(loadix); return;
        }
        for (var i = 0; i < personInfoPopup.idCardList.length; i++) {
            var index = personInfoPopup.idCardList[i].substr(personInfoPopup.idCardList[i].length - 1, 1);
            //获取新增的证件框数据
            var certificate = {};//对象
            //证件号
            var cardNum = $("#editPersonIdCard" + index).val();
            certificate.paperwork = cardNum;
            //证件类型（int）
            var cardType = $("#editPersonCardType" + index).val();
            certificate.type = cardType;
            //是否默认
            if (cardType == 1) {
                if ($("#editPersonIdCard" + index).val() == "") {
                    layer.msg("请输入证件号", {icon: 2, time: 3000});
                    personInfoPopup.layer.close(loadix); return;
                }
                //判断身份证框内值是否改变
                if ($("#editPersonIdCard" + index).val() != personInfoPopup.editIdCard) {
                    personInfoPopup.verifyUniqueIDCard("editPersonIdCard" + index);
                    //提交时再校验一次身份证
                    if(!personInfoPopup.checkIDCardNum){
                        personInfoPopup.layer.close(loadix); return;;
                    }
                    // var r = personInfoPopup.idCardReg.test($("#editPersonIdCard" + index).val());
                    // if (!r || $("#editPersonIdCard" + index) == "") {
                    //     layer.msg("身份证格式错误", {icon: 2, time: 3000});
                    //     personInfoPopup.layer.close(loadix); return;
                    // }
                }

            }else {
                //护照校验
                if(cardType == 2){
                    if ($("#editPersonIdCard" + index).val() == "") {
                        layer.msg("请输入护照证件号", {icon: 2, time: 3000});
                        personInfoPopup.layer.close(loadix); return;
                    }
                }
                if(cardType == 3){
                    if ($("#editPersonIdCard" + index).val() == "") {
                        layer.msg("请输入军官证件号", {icon: 2, time: 3000});
                        personInfoPopup.layer.close(loadix); return;
                    }
                }
                if(cardType == 4){
                    if ($("#editPersonIdCard" + index).val() == "") {
                        layer.msg("请输入工作证件号", {icon: 2, time: 3000});
                        personInfoPopup.layer.close(loadix); return;
                    }
                }
                if(cardType == 5){
                    if ($("#editPersonIdCard" + index).val() == "") {
                        layer.msg("请输入学生证件号", {icon: 2, time: 3000});
                        personInfoPopup.layer.close(loadix); return;
                    }
                }
                if(cardType == 6){
                    if ($("#editPersonIdCard" + index).val() == "") {
                        layer.msg("请输入其他证件号", {icon: 2, time: 3000});
                        personInfoPopup.layer.close(loadix); return;
                    }
                }
                personInfoPopup.verifyUniqueIDCard("editPersonIdCard" + index);
                if(!personInfoPopup.checkIDCardNum){
                    personInfoPopup.layer.close(loadix); return;;
                }
            }
            //var o = $("#paperworkRadio" + index).is("checked");
            //var isE = $('#paperworkRadio' + index).val();
            var isDefult = 0;
            var isE = $('input[name="yes"]:checked').val();
            //如果相等则是选中项
            if (isE == index) {
                // 把默认证件信息 加入人员的基本信息中
                personInfoPopup.editPerisonInfo.idCard = cardNum;
                personInfoPopup.editPerisonInfo.cardType = cardType;
                isDefult = 1;
            } else {
                isDefult = 0;
            }
            // 来源
            certificate.source = 'dpism';
            certificate.primaryKey = isDefult;
            papeWorkList.push(certificate);
        }
        personInfoPopup.editPerisonInfo.papeWorkList = papeWorkList;
    }

    //没有添加额外联系方式
    // personInfoPopup.phoneList
    if (personInfoPopup.editContactNum == 0) {
        personInfoPopup.layer.msg("联系方式不能为空，至少一种", {icon: 2, time: 3000});
        personInfoPopup.layer.close(loadix); return;
    } else {
        if (personInfoPopup.phoneList.length == 0) {
            personInfoPopup.layer.msg("联系方式不能为空，至少一种", {icon: 2, time: 3000});
            personInfoPopup.layer.close(loadix); return;
        }
        for (var i = 0; i < personInfoPopup.phoneList.length; i++) {
            //获取默认联系方式框内数据
            var index = personInfoPopup.phoneList[i].substr(personInfoPopup.phoneList[i].length - 1, 1);
            var contact = {};//对象

            var phoneNum = $("#editPersonPhone" + index).val();
            contact.concretenessInfo = phoneNum;
            //联系方式
            var type = $("#editAddressTypeSel" + index).val();
            contact.contactType = type;
            //联系方式类别
            var childType = $("#editAddressChildTypeSel" + index).val();
            //正则判断手机号、邮箱
            if ($("#editAddressTypeSel" + index).val() == 1) {
                if ($("#editPersonPhone" + index).val() == "") {
                    layer.msg("请输入手机号", {icon: 2, time: 3000});
                    personInfoPopup.layer.close(loadix); return;
                }
                var r = personInfoPopup.phoneReg.test($("#editPersonPhone" + index).val());
                if (!r || $("#editPersonPhone" + index) == "") {
                    layer.msg("手机号格式错误", {icon: 2, time: 3000});
                    personInfoPopup.layer.close(loadix); return;
                }
            } else if ($("#editAddressTypeSel" + index).val() == 2) {
                if ($("#editPersonIdCard" + index).val() == "") {
                    layer.msg("请输入邮箱号", {icon: 2, time: 3000});
                    personInfoPopup.layer.close(loadix); return;
                }
                var r = personInfoPopup.emailReg.test($("#editPersonPhone" + index).val());
                if (!r || $("#editPersonPhone" + index) == "") {
                    layer.msg("邮箱格式错误", {icon: 2, time: 3000});
                    personInfoPopup.layer.close(loadix); return;
                }
            }
            //为1则是主要方式，存入人员信息 dsrp
            if (type == 1) {
                personInfoPopup.editPerisonInfo.phone = phoneNum;
            }
            contact.childType = childType;
            //联系方式号码
            //certificate.type(cardType);
            addressBookList.push(contact);
        }
        personInfoPopup.editPerisonInfo.addressBookList = addressBookList;
    }
    if (personInfoPopup.editPerisonInfo.addressBookList.length == 0) {
        personInfoPopup.layer.msg("联系方式不能为空，至少一种", {icon: 2, time: 3000});
        personInfoPopup.layer.close(loadix); return;
    }
    if (personInfoPopup.editPerisonInfo.papeWorkList.length == 0) {
        personInfoPopup.layer.msg("证件不能为空，至少一种", {icon: 2, time: 3000});
        personInfoPopup.layer.close(loadix); return;
    }
    //保安数据
    if ($("#secriutyCardTab").length != 0) {
        var securityEntity = {};
        securityEntity.securityTypeId = $("#editSecurityName").val();
        // securityEntity.securityTypeName = $("#editSecurityName").find("option:selected").text();
        personInfoPopup.editPerisonInfo.securityInfo = securityEntity;
    }
    //教师数据
    if ($("#teacherCardTab").length != 0) {
        var techerEntity = {};
        techerEntity.status = $("#editTecherStatus").val(); //教师状态
        techerEntity.level = $("#editTecherType").val();  //等级
        techerEntity.classId = $("#editYunaxi").val();  //院系
        personInfoPopup.editPerisonInfo.teacherInfo = techerEntity;
    }

    //职工数据
    if ($("#staffCardTab").length != 0) {
        var staffEntity = {};
        staffEntity.orgId = $("#editOrgId").val();  // 部门
        staffEntity.jobGrade = $("#editJobGradle").val();  //职位
        staffEntity.startTime = $("#editStartTime").val(); //入职时间
        staffEntity.workNum = $("#editWorkNum").val();  // 工号
        staffEntity.staffType = $("#editStaffType").val();  //职工类型
        staffEntity.staffStatus = $("#editStaffStatus").val(); //职工状态
        personInfoPopup.editPerisonInfo.staffInfo = staffEntity;
    }
    //学生数据
    if ($("#studentCardTab").length != 0) {
        var studentEntity = {};
        studentEntity.studentNum = $("#editStudentNum").val();   // 学号
        studentEntity.facultyGuid = $("#editFacultyName").val();  // 院系id
        studentEntity.facultyName = $('#editFacultyName option:selected').text();  // 院系名称
        studentEntity.classGuid = $("#editClass").val();    // 班级id
        studentEntity.className = $('#editClass option:selected').text();     // 班级名称
        studentEntity.inTime = $("#editInTime").val();       // 入学时间
        studentEntity.specialty = $("#editSpecialty").val(); // 专业
        studentEntity.studentGrade = $("#editStudentGradle").val();  // 年级
        studentEntity.staffGuid = $("#editStaffGuid").val();    // 辅导员ID
        studentEntity.staffName = $('#editStaffGuid option:selected').text();    // 辅导员名称
        studentEntity.teacherGuid = $("#editTeacherGuid").val();    // 导师ID
        studentEntity.teacherName = $('#editTeacherGuid option:selected').text();    //导师名称
        studentEntity.eduYear = $("#editEduYear").val();    // 培养层次
        studentEntity.studentType = $("#editStudentType").val();  // 学制
        studentEntity.status = $("#editStatus").val();  // 学籍状态
        personInfoPopup.editPerisonInfo.studentInfo = studentEntity;
    }
    JSON.stringify(personInfoPopup.editPerisonInfo);
    //编辑
    if (!personInfoPopup.isAdd) {
        personInfoPopup.editPerisonInfo.id = personInfoPopup.personId;
        personInfoPopup.admin.ajax({
            type: "post",
            url: conf.dsrpBaseServer + "person/update",
            data: JSON.stringify(personInfoPopup.editPerisonInfo),
            contentType: "application/json",
            async: true,
            dataType: "json",
            success: function (data) {
                if (data.code != 0) {
                    layer.msg("修改失败", {icon: 2, time: 5000});
                    //layer.msg("上传成功失败" + data.msg + "错误请联系管理员！", {icon: 2, time: 5000});
                    personInfoPopup.layer.close(loadix); return;
                } else {
                    personInfoPopup.layer.close(loadix);
                    personInfoPopup.layer.close(personInfoPopup.popupIndex);
                    layer.msg("修改成功", {icon: 1, time: 5000});
                    personInfoPopup.callbackFn(data.data);
                }
            },
            error: function (data) {
                personInfoPopup.layer.close(loadix);
                layer.msg("修改失败", {icon: 2, time: 5000});
            }
        })
        //新增
    } else {
        personInfoPopup.admin.ajax({
            type: "post",
            url: conf.dsrpBaseServer + "person/add",
            data: JSON.stringify(personInfoPopup.editPerisonInfo),
            contentType: "application/json",
            async: true,
            dataType: "json",
            success: function (data) {
                if (data.code != 0) {
                    //layer.msg("上传成功失败" + data.msg + "错误请联系管理员！", {icon: 2, time: 5000});
                    layer.msg("新增失败", {icon: 2, time: 5000});
                    personInfoPopup.layer.close(loadix); return;
                } else {
                    personInfoPopup.layer.close(loadix);
                    personInfoPopup.layer.close(personInfoPopup.popupIndex);
                    layer.msg("新增成功", {icon: 1, time: 5000});
                    var resData = personInfoPopup.editPerisonInfo;
                    resData.id = data.data;
                    personInfoPopup.callbackFn(resData);
                }
            },
            error: function (data) {
                personInfoPopup.layer.close(loadix);
                layer.msg("新增失败", {icon: 2, time: 5000});
            }
        })
    }
}

/*上传文件事件触发*/
personInfoPopup.uploadEvent = function () {
    //$("#fileUpLoadIpt").click();
    document.querySelector("#fileUpLoadIpt").click();
}

/*选择图片回显*/
personInfoPopup.ImageChange = function (obj) {
    var self = obj;
    if (self.files && self.files[0]) {
        var file = self.files[0];
        obj.value = ""//及时清空
        var three = file.name.split(".");
        //获取截取的最后一个字符串，即为后缀名
        var last = three[three.length - 1];
        //添加需要判断的后缀名类型
        //var tp = "jpg,png,JPG,PNG,gif";
        var tp = "jpg,png,JPG,PNG,gif";
        if (tp.indexOf(last) < 0) {
            self.value = "";
            self.files[0] = null;
            layer.alert('上传图片格式错误，请上传jpg,png,gif格式图片');
            return;
        }
        if (file.type.indexOf("image") == -1) {
            self.value = "";
            self.files[0] = null;
            layer.alert('请上传图片文件');
            return;
        }
        if (file.size <= 1024 * 1024 * 20) {
            //普通图片上传
            var imgButId = $(obj).attr("id");
            var imgId = $("#fileUploadImg");
            if (imgId != "") {
                var img = document.getElementById(imgId);
                if (window.FileReader) {
                    var reader = new FileReader();
                    reader.onloadend = function (e) {
                        //img.src = e.target.result;
                    }
                }
                reader.readAsDataURL(file);
            }
            //上传文件
            var formData = new FormData();//这里需要实例化一个FormData来进行文件上传
            formData.append("file", file);
            formData.append("module", "person");
            formData.append("type", "icon");
            // Config.dir = formData.get("module");
            //Config.dir = formData.module;
            personInfoPopup.admin.ajax({
                type: "post",
                url: conf.dsrpBaseServer + 'upload/uploadFile',
                data: formData,
                accept: 'file',
                processData: false,
                contentType: false,
                dataType: "json",
                async: true,
                success: function (data) {
                    debugger
                    personInfoPopup.editPerisonInfo.photoFileName = data.msg.substring(1);
                    personInfoPopup.layer.msg("上传成功", {icon: 1, time: 2000});
                    //var split = data.data.split("/");
                    // var name = split[split.length - 1];
                    $("#fileUploadImg").attr("src", conf.dsrpBaseServer + personInfoPopup.editPerisonInfo.photoFileName);
                },
                error: function (data) {
                    layer.msg("上传失败", {icon: 2});
                    $("#" + imgId).attr("data-filepath", "");

                }
            });
            // Config.dir = formData.get("module");
            //Config.dir = formData.module;
        } else {
            var fszie = parseFloat(file.size / (1024 * 1024)).toFixed(1);
            self.value = "";
            self.files[0] = null;
            layer.alert('上传的文件不能超过20M，您的图片为：' + fszie + 'M');
            return;
        }
    }
}
/*联系方式改变 对应文本也改变*/
personInfoPopup.addressTypeChange = function (obj) {
    $(obj).parents('tr').next().find("label").text('联系' + $(obj).find("option:selected").text())
}











