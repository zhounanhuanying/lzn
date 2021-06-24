package com.bfdb.controller;

import com.alibaba.fastjson.JSON;
import com.bfdb.entity.SysServer;
import com.bfdb.service.SysServerService;
import com.bfdb.untils.LayuiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 终端服务器管理
 */
@RestController
@RequestMapping("sysServer")
public class SysServerController {

    @Autowired
    private SysServerService sysServerService;

    /**
     * 新增服务器信息
     *
     * @param sysServer
     * @return
     */
    @RequestMapping(value = "/insertSysServer", method = RequestMethod.POST)
    public Map<String, Object> insertSysServer(SysServer sysServer) {
        int i = sysServerService.insertSysServer( sysServer );
        return LayuiUtil.dataInsert( i );
    }


    /**
     * 查询服务器信息
     *
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSysServerList", method = RequestMethod.GET)
    public Map<String, Object> getSysServerList(@RequestParam Map<String, Object> params) {
        int totalCount = sysServerService.dataCount( params );
        Map<String, Object> rePutParams = LayuiUtil.rePutParams( params );
        List<SysServer> sysServerList = sysServerService.getSysServerList( rePutParams );
        return LayuiUtil.dataList( totalCount, sysServerList );
    }

    /**
     * 删除服务器
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteSysServerList")
    public Map<String, Object> deleteSysServerList(HttpServletRequest request) {
        String data = request.getParameter( "data" );
        List<SysServer> sysServers = JSON.parseArray( data, SysServer.class );
        int i = sysServerService.deleteSysServerList( sysServers );
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put( "code", i );
        returnMap.put( "msg", "success" );
        return returnMap;
    }

    /**
     * 修改服务器信息
     *
     * @param sysServer
     * @return
     */
    @RequestMapping(value = "/updateSysServer")
    public Map<String, Object> updateSysServer(SysServer sysServer) {
        int i = sysServerService.updateSysServer( sysServer );
        return LayuiUtil.dataInsert( i );
    }

}
