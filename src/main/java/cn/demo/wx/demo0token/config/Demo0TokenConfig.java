package cn.demo.wx.demo0token.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.core.JFinal;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

import cn.demo.wx.demo0token.cache.CacheAccessTokenCache;
import cn.demo.wx.demo0token.model.WxUser;

public class Demo0TokenConfig extends JFinalConfig {
	
	public static final String CFG_FILE="demo-token.properties";
	/**
	 * 启动入口，运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 * 
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run configuration 中自动生成
	 * 一条启动配置项，可对该自动生成的配置再继续添加更多的配置项，例如 VM argument 可配置为：
	 * -XX:PermSize=64M -XX:MaxPermSize=256M
	 * 上述 VM 配置可以缓解热加载功能出现的异常
	 */
	public static void main(String[] args) {
		
		UndertowServer.start(Demo0TokenConfig.class,8080,true);

		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
//		 JFinal.start("src/main/webapp", 8123, "/", 5);
		
		/**
		 * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		//JFinal.start("src/main/webapp", 8080, "/");
	}
	public static Prop p=null;
    public void configConstant(Constants me) {
    	
    	p = PropKit.use(CFG_FILE);
    	ApiConfig apiconfig = new ApiConfig();
    	apiconfig.setAppId(p.get("wechat.appId"));
    	apiconfig.setAppSecret(p.get("wechat.appSecret"));
    	apiconfig.setToken(p.get("wechat.token"));
    	ApiConfigKit.putApiConfig(apiconfig);
//    	DOMAIN = p.get("domain");
//    	HOST = p.get("host");
        me.setDevMode(p.getBoolean("devMode", false));
		me.setJsonFactory(MixedJsonFactory.me());
		me.setMaxPostSize(10*Const.DEFAULT_MAX_POST_SIZE);
		me.setError404View("/static/404.html");
    }
    
    public void configRoute(Routes me) {
	    me.add(new TokenRoutes());
	    
    }
    
    /**
     * 配置模板引擎，通常情况只需配置共享的模板函数
     */
    public void configEngine(Engine me) {
    	me.setDevMode(JFinal.me().getConstants().getDevMode());

    }
    
    /**
     * 抽取成独立的方法，便于 _Generator 中重用该方法，减少代码冗余
     */
	public static DruidPlugin getDruidPlugin() {
		return new DruidPlugin(p.get("jdbc.url"), p.get("jdbc.username"), p.get("jdbc.password").trim());
	}
	
    public void configPlugin(Plugins me) {
	  
//    	DruidPlugin dp = new DruidPlugin(String.format(p.get("jdbc.url"), PathKit.getRootClassPath()),p.get("jdbc.username","").trim(), p.get("jdbc.password","").trim());
//    	//加载sqlite的驱动,其他操作和mysql的一样
//    	dp.setDriverClass("org.sqlite.JDBC");
//    	me.add(dp);
//    	ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
//	    me.add(arp);
//	    arp.addMapping("wx_user","openid", WxUser.class);
//        arp.setShowSql(JFinal.me().getConstants().getDevMode());
       
	    me.add(new EhCachePlugin());
	   

    }
    
    public void configInterceptor(Interceptors me) {
    }
    
    public void configHandler(Handlers me) {
       //me.add(new UrlSkipHandler(".*\\.(txt|js|css|jpeg|jpg|png|gif)$", false));
	   // me.add(new UrlSeoHandler());  // index、detail 两类 action 的 url seo
	   // me.add(new ContextPathHandler("ctx"));
    }
    
 
	public void afterJFinalStart() {
		ApiConfigKit.setAccessTokenCache(new CacheAccessTokenCache(WxUser.CACHE_WXTOKEN));
	}

}
