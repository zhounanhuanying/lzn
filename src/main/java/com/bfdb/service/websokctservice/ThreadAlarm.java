package com.bfdb.service.websokctservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfdb.config.CustomWebSocket;
import com.bfdb.entity.PersonDataOperationTable;
import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;
import com.bfdb.entity.SysServer;
import com.bfdb.entity.vo.GUIShowInfoVo;
import com.bfdb.entity.vo.ShowInfoList;
import com.bfdb.entity.websocket.CardInfo;
import com.bfdb.entity.websocket.DrivePropertiesBean;
import com.bfdb.mapper.SubscriptionMapper;
import com.bfdb.service.PersonDataOperationTableService;
import com.bfdb.service.PersonFaceInfomationTableService;
import com.bfdb.service.PersonTableService;
import com.bfdb.untils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实时获取订阅的消息推送到前端，渲染
 */
public class ThreadAlarm implements Runnable {
    private static final String SOAP_BEGIN = "{";
    private static final String SOAP_END = "}";
    private Socket socket;
    private String result = "";

    private DrivePropertiesBean drivePropertiesBean;

    private PersonTableService personTableService;

    private PersonFaceInfomationTableService personFaceInfomationTableService;

    private SubscriptionMapper subscriptionMapper;
    private PersonDataOperationTableService personDataOperationTableService;
    @Autowired
    private CustomWebSocket webSocketServlet;


    public ThreadAlarm(DrivePropertiesBean drivePropertiesBean, Socket socket, PersonTableService personTableService,
                       PersonFaceInfomationTableService personFaceInfomationTableService, SubscriptionMapper subscriptionMapper, PersonDataOperationTableService personDataOperationTableService) {
        this.drivePropertiesBean = drivePropertiesBean;
        this.socket = socket;
        this.personTableService = personTableService;
        this.personFaceInfomationTableService = personFaceInfomationTableService;
        this.subscriptionMapper = subscriptionMapper;
        this.personDataOperationTableService = personDataOperationTableService;
    }


    @Override
    public void run() {
        try {
            //输入流读取推送数据
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            //创建缓存空间
            CharBuffer charbuffer = CharBuffer.allocate( 8192 );
            while ((bufferedReader.read( charbuffer )) != -1) {
                charbuffer.flip();//将Buffer从写模式切换到读模式
                result += charbuffer.toString();
            }
            System.out.println("———————————————监听获取数据成功——————————————result—————————————————————");
            //根据业务需求获取需要的数据
            if (result.indexOf( SOAP_BEGIN ) != -1 && result.indexOf( SOAP_END ) != -1) {
                String substring = result.substring( result.indexOf( SOAP_BEGIN ) );
                //解析json
                JSONObject jsonObject = JSONObject.parseObject( substring );
                //获取身份证信息
                String jsonObjectString = jsonObject.getString( "CardInfoList" );
                //进行转换处理，便于更好的转换成json对象
                jsonObjectString = jsonObjectString.replace( "[", "" );
                jsonObjectString = jsonObjectString.replace( "]", "" );
                //最后获取身份证信息
                JSONObject ardInfo = JSON.parseObject( jsonObjectString );
                //获取当前人员照片信息
                String faceInfoString = jsonObject.getString( "FaceInfoList" );
                //判断第一个字符是否为“[”
                if (faceInfoString.startsWith( "[" )) {
                    faceInfoString = faceInfoString.substring( 1 );
                }
                //判断最后一个字符是否为“]”
                if (faceInfoString.endsWith( "]" )) {
                    faceInfoString = faceInfoString.substring( 0, faceInfoString.length() - 1 );
                }
//                System.out.println(faceInfoString);
                //将json字符串转化成json对象
                JSONObject faceInfo = JSON.parseObject( faceInfoString );
                //封装实体类
                CardInfo cardInfo = new CardInfo();
                cardInfo.setName( (String) ardInfo.get( "Name" ) );
                cardInfo.setGender( (Integer) ardInfo.get( "Gender" ) );//性别
                cardInfo.setEthnicity( (Integer) ardInfo.get( "Ethnicity" ) );//民族
                cardInfo.setBirthday( (String) ardInfo.get( "Birthday" ) );//出生日期
                cardInfo.setResidentialAddress( (String) ardInfo.get( "ResidentialAddress" ) );//住址
                cardInfo.setIdentityNo( (String) ardInfo.get( "IdentityNo" ) );//身份证号
                cardInfo.setIssuingAuthority( (String) ardInfo.get( "IssuingAuthority" ) );//签发地址
                cardInfo.setValidDateStart( (String) ardInfo.get( "ValidDateStart" ) );//起止时间
                cardInfo.setValidDateEnd( (String) ardInfo.get( "ValidDateEnd" ) );//截止时间
                //获取身份证人脸照片信息
                Map rMap = (Map) JSON.parse( String.valueOf( ardInfo ) );
                String idImage = rMap.get( "IDImage" ).toString();
                Map rMa = (Map) JSON.parse( idImage );
                //base64图片转换
//                String bigImageUrl = Base64Utils.generateImage( (String) rMa.get( "Data" ) );
                cardInfo.setSmallImage((String) rMa.get( "Data" ));//身份证图片路径信息
                //获取当前获取当前人员照片信息
                Map fmap = (Map) JSON.parse( String.valueOf( faceInfo ) );
                //获取当前获取当前人员全景照片信息
                String panoImage = fmap.get( "PanoImage" ).toString();
                Map famap = (Map) JSON.parse( panoImage );
                //base64图片转换
//                String faceInfoUrl = Base64Utils.generateImage( (String) famap.get( "Data" ) );
                cardInfo.setFaceInfoImage(  (String) famap.get( "Data" ) );//人员全景照片路径信息
                //获取人员人脸小图照片
                String faceImage = fmap.get( "FaceImage" ).toString();
                Map faceImageMap = (Map) JSON.parse( faceImage );
                //base64图片转换
//                String faceImageUrl = Base64Utils.generateImage( (String) faceImageMap.get( "Data" ) );
                cardInfo.setFaceImage(  (String) faceImageMap.get( "Data" ) );//人员小图照片路径信息
//                System.out.println(JSONObject.toJSON( cardInfo ));
                //实时保存到数据库
                //监听获取采集设备ip
                String hostAddress = socket.getInetAddress().getHostAddress();
                //根据人员身份证号查询人员信息
                PersonTable personTable = personTableService.selectPersonTableByIdentityNo( cardInfo.getIdentityNo() );
                //查询人员配置信息
                List<SysServer> sysServerList = subscriptionMapper.selectBySysServerList();
                PersonFaceInfomationTable person = null;
                PersonDataOperationTable personDataOperationTable = null;
                PersonTable personNewTable=null;
                //判断数据库是否有人员信息
                if (personTable == null) {
                    //判断是否有订阅的设备
                    if (ListUtils.isNotNullAndEmptyList( sysServerList )) {
                        //循环遍历订阅设备
                        for (SysServer sysServer : sysServerList) {
                            //判断是否开启自动提交
                            if ("1".equals( sysServer.getVerificationStatus() )) {
                                personNewTable = new PersonTable();
                                personNewTable.setPersonName( cardInfo.getName() );
                                personNewTable.setGender( cardInfo.getGender() );
                                personNewTable.setEthnicity( cardInfo.getEthnicity().toString() );
                                personNewTable.setBirthday( BirthdayUtils.extractYearMonthDayOfIdCard( cardInfo.getIdentityNo() ) );
                                personNewTable.setResidentialAddress( cardInfo.getResidentialAddress() );
                                personNewTable.setIdentityNo( cardInfo.getIdentityNo() );
                                personNewTable.setIdenticationInfo( sysServer.getIdenticationInfo() );
                                personNewTable.setDepartments( sysServer.getDepartments() );
                                personNewTable.setStudentLevel( sysServer.getStudentLevel() );
                                personNewTable.setGrade( sysServer.getGrade() );
                                int i = personTableService.insertPersonNewTable( personNewTable );
                                if (i != 0) {
                                    //判断人脸图片是否为空  身份证人脸照片信息
                                    if (StringUtils.isNotBlank( cardInfo.getSmallImage() )) {
                                        person = new PersonFaceInfomationTable();
                                        person.setFaceAddress( Base64Utils.generateImage(cardInfo.getSmallImage() ));
                                        person.setDataSource( "2" );
                                        person.setPersonId( personNewTable.getPersonId() );
                                        person.setIdentification( "1" );
                                        personFaceInfomationTableService.insertSelective( person );
                                    }
                                    //判断人证核验抓拍图片是否为空
                                    if (StringUtils.isNotBlank( cardInfo.getFaceInfoImage() )) {
                                        person = new PersonFaceInfomationTable();
                                        person.setFaceImage( Base64Utils.generateImage(cardInfo.getFaceInfoImage() ));
                                        person.setDataSource( "2" );
                                        person.setPersonId( personNewTable.getPersonId() );
                                        personFaceInfomationTableService.insertSelective( person );
                                    }
                                    //添加人员更新日志
                                    personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personNewTable );
                                    personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                                }
                            } else {
                                //从设备获取到IP
                                cardInfo.setServerIp( hostAddress );
                                //根据不同的设备IP区分终端设备
                                cardInfo.setBirthday( BirthdayUtils.extractYearMonthDayOfIdCard( cardInfo.getIdentityNo() ) );
                                //向前端页面发送消息
                                webSocketServlet.sendInfo( cardInfo );
//                                }
                            }
                        }

                    }

                } else {
                    //判断是否有订阅的设备   此判断不对页面采集操作，因为页面提交的时候已经有人脸照片了
                    if (ListUtils.isNotNullAndEmptyList( sysServerList )) {
                        //循环遍历订阅设备
                        for (SysServer sysServer : sysServerList) {
                            //判断是否开启自动提交
                            if ("1".equals( sysServer.getVerificationStatus() )) {
                                //查询人脸有多少张
                                int  faceAddresscount= personFaceInfomationTableService.faceAddresscount( personTable.getPersonId() );
                                //查询人脸相关照片是否有身份证照片
                                PersonFaceInfomationTable  faceAddress= personFaceInfomationTableService.faceAddres( personTable.getPersonId() );
                                //判断人脸照片条数不大于3时，更新补齐
                                if(faceAddresscount<3){
                                    //判断人脸相关照片是否有身份证照片
                                    if(faceAddress==null){
                                        //更新人员的姓名
                                        personNewTable= new PersonTable();
                                        personNewTable.setPersonName( cardInfo.getName() );
                                        personNewTable.setPersonId( personTable.getPersonId());
                                        personTableService.updateByPrimaryKeySelective(personNewTable);
                                        //判断人脸图片是否为空  身份证人脸照片信息  更新到数据库
                                        if (StringUtils.isNotBlank( cardInfo.getSmallImage() )) {
                                            person = new PersonFaceInfomationTable();
                                            person.setFaceAddress(Base64Utils.generateImage(cardInfo.getSmallImage()));
                                            person.setDataSource( "2" );
                                            person.setPersonId( personTable.getPersonId() );
                                            person.setIdentification( "1" );
                                            personFaceInfomationTableService.insertSelective( person );
                                        }
                                        //添加人员更新日志
                                        personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable( personTable );
                                        personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                                    }
                                }
                                //查询人证核验的照片有多少张
                                int  faceInfoImagecount= personFaceInfomationTableService.faceInfoImageCount( personTable.getPersonId() );
                                if(faceInfoImagecount==0){
                                    //更新人员的姓名
                                    personNewTable= new PersonTable();
                                    personNewTable.setPersonName( cardInfo.getName() );
                                    personNewTable.setPersonId( personTable.getPersonId());
                                    personTableService.updateByPrimaryKeySelective(personNewTable);
                                    //判断人证核验抓拍图片是否为空
                                    if (StringUtils.isNotBlank( cardInfo.getFaceInfoImage() )) {
                                        person = new PersonFaceInfomationTable();
                                        person.setFaceImage(Base64Utils.generateImage(cardInfo.getFaceInfoImage()) );
                                        person.setDataSource( "2" );
                                        person.setPersonId( personNewTable.getPersonId() );
                                        personFaceInfomationTableService.insertSelective( person );
                                    }
                                    //添加人员更新日志
                                    personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable( personTable );
                                    personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                                }

                            }
                        }
                    }

                    //请求参数实体类
                    GUIShowInfoVo guiShowInfoVo = new GUIShowInfoVo();
                    //结果码（可控制开门）0:不开门， 1:开门
                    guiShowInfoVo.setResultCode( drivePropertiesBean.getResultCode() );
                    //返回信息
                    guiShowInfoVo.setResultMsg( "人员信息重复" );
                    //经过时刻：范围[0, 18] 终端上报过人记录中的时间
                    guiShowInfoVo.setPassTime( drivePropertiesBean.getPassTime() );
                    //人机显示信息行数最大为2，当前最多显示两行
                    guiShowInfoVo.setShowInfoNum( drivePropertiesBean.getShowInfoNum() );
                    //组成一个数组
                    List<ShowInfoList> showInfoLists = new ArrayList<>();
                    ShowInfoList showInfo = new ShowInfoList();
                    //人员姓名
                    showInfo.setKey( cardInfo.getName() );
                    //身份证号
                    showInfo.setValue( cardInfo.getIdentityNo() );
                    showInfoLists.add( showInfo );
                    showInfo = new ShowInfoList();
                    //若需控制健康码显示，属性值填写“CodeStatus”，同时设备开启远程核验模式
                    showInfo.setKey( "CodeStatus" );
                    showInfo.setValue( "1" );
                    showInfoLists.add( showInfo );
                    guiShowInfoVo.setShowInfoList( showInfoLists );
                    //向设备推送人员信息重复提示
                    HttpResponseResult httpResponseResult = ReqContextUtils.doPostForJson( drivePropertiesBean.getSubscriptionUrlPre() + drivePropertiesBean.getShowInfoUrl() + drivePropertiesBean.getgUIShowInfoUrl(), JSONObject.toJSONString( guiShowInfoVo ) );
                    System.out.println( "人证核验设备返回的数据：" + httpResponseResult );
                    //从设备获取到IP
                    cardInfo.setServerIp( hostAddress );
                    //根据不同的设备IP区分终端设备
                    cardInfo.setBirthday( BirthdayUtils.extractYearMonthDayOfIdCard( cardInfo.getIdentityNo() ) );
                    //向前端页面发送消息
                    webSocketServlet.sendInfo( cardInfo );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
