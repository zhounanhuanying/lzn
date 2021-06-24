package com.bfdb.shirorelam;

import com.bfdb.entity.CommonAddress;
import com.bfdb.service.CommonUrlService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * shiro的配置类
 */
@Configuration
public class ShiroConfiguration {


    //配置自定义的权限登录器
    @Bean(name = "myRealm")
    public MyRealm myRealm() {
        return new MyRealm();
    }


    //authc:所有url都必须认证通过才可以访问; anon:所有url都可以匿名访问
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager, CommonUrlService commonUrlService) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager( manager );
        //配置登录的url和登录成功的url
//        bean.setLoginUrl("/login");
//       bean.setSuccessUrl("/home");
        //无权限
        shiroFilterFactoryBean.setUnauthorizedUrl( "/unauthorized" );
        //配置访问权限  URL过滤
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/", "anon" );        //表示可以匿名访问
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/templates/**", "anon");
        filterChainDefinitionMap.put("/Content/**", "anon");
        filterChainDefinitionMap.put("/lib/**", "anon");
        filterChainDefinitionMap.put( "/logout", "anon" );
        filterChainDefinitionMap.put("/test", "anon");
        filterChainDefinitionMap.put("/configuration/initConfigurationWhetherPath", "anon" );
        filterChainDefinitionMap.put("/configuration/testConnectionJDBC", "anon" );
        filterChainDefinitionMap.put("/configuration/initConfiguration", "anon" );
        try {
            //从数据库查询需要过滤的url的集合
            List<CommonAddress> urlFilterList = commonUrlService.selectByList();
            for (CommonAddress filter : urlFilterList) {
                filterChainDefinitionMap.put( filter.getCommonUrl(), filter.getFilter() );
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            shiroFilterFactoryBean.setFilterChainDefinitionMap( filterChainDefinitionMap );
        }
        return shiroFilterFactoryBean;
    }

    //配置核心安全事务管理器
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("myRealm") MyRealm myRealm) {
        System.err.println( "--------------shiro已经加载----------------" );
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //加密配置
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置加密算法名称
        matcher.setHashAlgorithmName( "md5" );
        //设置加密次数
        matcher.setHashIterations( 1 );
        myRealm().setCredentialsMatcher( matcher );
        //设置Realm
        securityManager.setRealm( myRealm() );
        //ThreadContext.bind(manager); 本地运行
        securityManager.setRealm( myRealm );
        return securityManager;
    }

    /**
     * 管理shiro bean生命周期
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
//        creator.setProxyTargetClass( true );
//        return creator;
//    }
}
