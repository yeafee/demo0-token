package cn.demo.wx.demo0token.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;

import cn.demo.wx.demo0token.interceptor.WechatInterceptor;
import cn.demo.wx.demo0token.model.WxUser;

@Before(WechatInterceptor.class)
public class IndexController extends WebBaseController {
	static ApiConfig config = ApiConfigKit.getApiConfig();
	final static String appId = config.getAppId();
	final static String secret = config.getAppSecret();

	
	public void index() {
		String openid =openId();
		setAttr("openid", openid);
		render("index.html");
	}
	
	public void indexfork() {
		String openid =openId();
		renderJavascript("<script>alert('" + (StrKit.isBlank(openid) ? "no openid" : openid) + "')</script>");
	}
	
	@Clear
	public void token() {
		String token = CacheKit.get(WxUser.CACHE_WXTOKEN, appId);
		setAttr("token", token);
		setAttr("openid",openId());
		render("token.html");
	}

	public void userinfo() {
		String info="";
		ApiResult result = UserApi.getUserInfo(getPara("openid"));
        if(result.isSucceed()) {
        	info=result.getJson();
        }
        setAttr("info", info);
        render("user.html");
	}
	
	public void userData() {
		ApiResult result = UserApi.getUserInfo(getPara("openid"));
        renderJson(result);
	}

}
