package cn.demo.wx.demo0token.interceptor;

import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;

import cn.demo.wx.demo0token.model.WxUser;
import cn.demo.wx.utils.FormatUtil;

public class WechatInterceptor implements Interceptor {

	private static final Logger log = LoggerFactory.getLogger(WechatInterceptor.class);

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		
		String queryString = request.getQueryString();
		String targetUrl = request.getRequestURL() + (StrKit.notBlank(queryString) ? "?" + queryString : "");
		String redirect_uri = request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath() + "/wechatOAuth/wechatLoginCallBack";
		log.info("进入微信登陆拦截器 被拦截的用户目标地址:" +targetUrl);
		if (checkReq(controller)) {
			inv.invoke();
		} else {
			String redirectUrl="";
			//redirectUrl=buildAuthUrl(targetUrl);
			String state = UUID.randomUUID().toString().replaceAll("-", "");
			redirectUrl = buildSnsAuthUrl(redirect_uri,state);
			CacheKit.put(WxUser.CACHE_STATE,state,targetUrl);
			log.info("离开微信登陆拦截器 重定向地址:" + redirectUrl);
			controller.redirect(redirectUrl);
		}
	}
	
	
	private String buildSnsAuthUrl(String url,String state) {
		try {
			ApiConfig config = ApiConfigKit.getApiConfig();
			return SnsAccessTokenApi.getAuthorizeURL(config.getAppId(), URLEncoder.encode(url, "utf-8"), state, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private boolean checkReq(Controller controller){
		HttpServletRequest request=controller.getRequest();
		if(!FormatUtil.isWechat(request)) {
			return true;
		}
		if (FormatUtil.isAjax(request)) {
			return true;
		}

		String cookieOpenId = (String)controller.getCookie(WxUser.OPENID);
		log.debug("check cookieId:"+cookieOpenId);
		if(StrKit.notBlank(cookieOpenId)) {
			log.debug("check notblank:"+cookieOpenId);
			return true;
		}
		return false;
	}

}
