package com.bfdb.service.impl;

import com.bfdb.config.Constant;
import com.bfdb.entity.DataDictionary;
import com.bfdb.entity.SysServer;
import com.bfdb.entity.websocket.SysSubscriptionAlarm;
import com.bfdb.mapper.DataDictionaryMapper;
import com.bfdb.mapper.SubscriptionMapper;
import com.bfdb.mapper.SysServerMapper;
import com.bfdb.service.SysServerService;
import com.bfdb.untils.HttpUtil;
import com.bfdb.untils.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysServerServiceImpl implements SysServerService {


    @Autowired
    private SysServerMapper sysServerMapper;
    @Autowired
    private SubscriptionMapper subscriptionMapper;


    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    /**
     * 新增终端服务器信息
     *
     * @param sysServer
     * @return
     */
    @Override
    public int insertSysServer(SysServer sysServer) {
        int i2 = 0;
        if (sysServer != null && !"".equals( sysServer )) {
            String serverIP = sysServer.getServerIp();
            Map<String, Object> sysServerQueryMap = new HashMap<>();
            //验证ip是否正确
            boolean isIP = HttpUtil.isIP( serverIP );
            if (isIP) {
                sysServerQueryMap.clear();
                sysServerQueryMap.put( "serverIp", serverIP );
                sysServerQueryMap.put( "page", 0 );
                sysServerQueryMap.put( "limit", 10 );
                List<SysServer> sysServerList = sysServerMapper.getSysServerList( sysServerQueryMap );
                if (sysServerList.size() != 0) {
                    return -3;
                }
                //新增服务器信息
                i2 = sysServerMapper.insertSysServer( sysServer );
            } else {
                return -2;
            }
        }
        return i2;
    }

    /**
     * 查询服务器信息
     *
     * @param params
     * @return
     */
    @Override
    public List<SysServer> getSysServerList(Map<String, Object> params) {
        List<SysServer> sysServerList = sysServerMapper.getSysServerList( params );
        //查询所在部门或院系信息
        List<DataDictionary> departmentsList = dataDictionaryMapper.setlectDataDictionaryList( Constant.DEPARTMENT );
        //查询学历
        List<DataDictionary> studentLevelList = dataDictionaryMapper.setlectDataDictionaryList( Constant.EDUCATION );
        //查询学级
        List<DataDictionary> gradeList = dataDictionaryMapper.setlectDataDictionaryList( Constant.GRADE );
        //查询人员信息
        List<DataDictionary> identicationInfoList = dataDictionaryMapper.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
        /**
         *判断是否为空
         * 以下代码，是为了从字典表中查出相关数据，进行对比显现
         */
        if (ListUtils.isNotNullAndEmptyList( sysServerList )) {
            for (SysServer sysServer : sysServerList) {
                //判空 学历
                if (StringUtils.isNotBlank( sysServer.getStudentLevel() )) {
                    if ((ListUtils.isNotNullAndEmptyList( studentLevelList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : studentLevelList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( sysServer.getStudentLevel() )) {
                            for (DataDictionary dataDictionary : studentLevelList) {
                                if (dataDictionary.getDicCode().equals( sysServer.getStudentLevel() )) {
                                    sysServer.setStudentLevel( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            sysServer.setStudentLevel( "" );
                        }
                    }

                }
                //判空 学级
                if (StringUtils.isNotBlank( sysServer.getGrade() )) {
                    if ((ListUtils.isNotNullAndEmptyList( gradeList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : gradeList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( sysServer.getGrade() )) {
                            for (DataDictionary dataDictionary : gradeList) {
                                if (dataDictionary.getDicCode().equals( sysServer.getGrade() )) {
                                    sysServer.setGrade( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            sysServer.setGrade( "" );
                        }
                    }

                }
                //判空 所在部门或院系
                if (StringUtils.isNotBlank( sysServer.getDepartments() )) {
                    if ((ListUtils.isNotNullAndEmptyList( departmentsList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : departmentsList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( sysServer.getDepartments() )) {
                            for (DataDictionary dataDictionary : departmentsList) {
                                if (dataDictionary.getDicCode().equals( sysServer.getDepartments() )) {
                                    sysServer.setDepartments( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            sysServer.setDepartments( "" );
                        }
                    }

                }
                //判空 人员类型
                if (StringUtils.isNotBlank( sysServer.getIdenticationInfo() )) {
                    if ((ListUtils.isNotNullAndEmptyList( identicationInfoList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : identicationInfoList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( sysServer.getIdenticationInfo() )) {
                            for (DataDictionary dataDictionary : identicationInfoList) {
                                if (dataDictionary.getDicCode().equals( sysServer.getIdenticationInfo() )) {
                                    sysServer.setIdenticationInfo( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            sysServer.setIdenticationInfo( "" );
                        }
                    }

                }
                //判空 终端采集自动
                if (StringUtils.isNotBlank( sysServer.getVerificationStatus() )) {
                    if ("1".equals( sysServer.getVerificationStatus() )) {
                        sysServer.setVerificationStatuss( "是" );
                    } else {
                        sysServer.setVerificationStatuss( "否" );
                    }

                }
            }
        }
        return sysServerList;
    }

    /**
     * 修改服务器信息
     *
     * @param sysServer
     * @return
     */
    @Override
    public int updateSysServer(SysServer sysServer) {
        if (!(Constant.IDENTICATIONINFO_2.equals( sysServer.getIdenticationInfo() )) && !(Constant.IDENTICATIONINFO_3.equals( sysServer.getIdenticationInfo() ))) {
            //判断人员类型是学生或者留学生赋为空值
            sysServer.setStudentLevel( "" );
            sysServer.setGrade( "" );
        }
        int i2 = 0;
        if (sysServer != null && !"".equals( sysServer )) {
            String serverIP = sysServer.getServerIp();
            Map<String, Object> sysServerQueryMap = new HashMap<>();
            //验证ip是否正确
            boolean isIP = HttpUtil.isIP( serverIP );
            if (isIP) {
                sysServerQueryMap.clear();
                sysServerQueryMap.put( "serverIp", serverIP );
                sysServerQueryMap.put( "page", 0 );
                sysServerQueryMap.put( "limit", 10 );
                List<SysServer> sysServerList = sysServerMapper.getSysServerList( sysServerQueryMap );

                if(sysServerList.size() != 0){
                    if (sysServerList.size() == 1) {
                        if(sysServerList.get(0).getServerId() != sysServer.getServerId()) {
                            return -3;
                        }
                    }else{
                        return -3;
                    }
                }
                //修改服务器信息
                i2 = sysServerMapper.updateSysServer( sysServer );
            } else {
                return -2;
            }
        }
        return i2;
    }

    /**
     * 查询服务器条数
     *
     * @param params
     * @return
     */
    @Override
    public int dataCount(Map<String, Object> params) {
        return sysServerMapper.dataCount( params );
    }

    /**
     * 删除服务器
     *
     * @param sysServers
     * @return
     */
    @Override
    public int deleteSysServerList(List<SysServer> sysServers) {
        int j = 0;
        for (SysServer server : sysServers) {
            //根据服务器id查询订阅信息
            SysSubscriptionAlarm subscription = subscriptionMapper.getSubscription( server.getServerId() );
            if (subscription != null) {
                return -2;
            }
            //删除服务器
            j = sysServerMapper.deleteSysServerById( server.getServerId() );
        }
        return j;
    }

    /**
     * 查询是否开启页面显示的人证核验终端的集合
     *
     * @return
     */
    @Override
    public List<SysServer> getSysServerByVerificationStatusList() {
        return sysServerMapper.getSysServerByVerificationStatusList();
    }

    @Override
    public List<SysServer> getSysServerListSuccess() {
        return sysServerMapper.getSysServerListSuccess();
    }

}
