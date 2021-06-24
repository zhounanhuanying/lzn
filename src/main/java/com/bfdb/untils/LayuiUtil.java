package com.bfdb.untils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayuiUtil extends HashMap<String,Object>{

    public static LayuiUtil dataList(Integer count,List<?> data){
        LayuiUtil r = new LayuiUtil();
        r.put("code", 0);
        r.put("msg", "success");
        r.put("count", count);
        r.put("data", data);
        return r;
    }
    public static Map<String, Object> dataUpdate(Integer i){
        LayuiUtil r = new LayuiUtil();
        if(i>0){
            r.put("code", 0);
            r.put("msg", "success");
        }else{
            r.put("code","8018");
            r.put("msg","更新失败");
        }
        return r;
    }
    public static Map<String, Object> dataInsert(Integer i){
        LayuiUtil r = new LayuiUtil();
        if(i>0){
            r.put("code", 0);
            r.put("msg", "success");
        } else if(i==0){
            r.put("code",-1);
            r.put("msg","error");
        }else if(i==-2){
            r.put("code",-2);
            r.put("msg","error");
        }else if(i==-3){
            r.put("code",-3);
            r.put("msg","error");
        }else{
            r.put("code","404");
            r.put("msg","error");
        }
        return r;
    }
    public static Map<String, Object> dataDelete(Integer i){
        LayuiUtil r = new LayuiUtil();
        if(i>0){
            r.put("code", 0);
            r.put("msg", "success");
        }else if(i==-1){
            r.put("code",-1);
            r.put("msg","error");
        }else if(i==-2){
            r.put("code",-2);
            r.put("msg","error");
        }else if(i==-3){
            r.put("code",-3);
            r.put("msg","error");
        }else{
            r.put("code","8018");
            r.put("msg","error");
        }
        return r;
    }
    public static Map<String, Object> rePutParams(Map<String, Object> params){
        Integer limit = Integer.parseInt(params.get("limit").toString());
        Integer page = (Integer.parseInt(params.get("page").toString())-1)*limit;
        params.put("limit",limit);
        params.put("page",page);
        return params;
    }
    public static Map<String, Object> subscriptionResponse(Integer i){
        LayuiUtil r = new LayuiUtil();
        if(i>0){
            r.put("code", 0);
            r.put("msg", "success");
        }else if(i==-2){
            r.put("code",-2);
            r.put("msg","error");
        }
        return r;
    }
    public static Map<String, Object> importResponse(Integer i){
        LayuiUtil r = new LayuiUtil();
        if(i>0){
            r.put("code", 0);
            r.put("msg", "success");
        }else{
            r.put("code",-1);
            r.put("msg","error");
        }
        return r;
    }
}
