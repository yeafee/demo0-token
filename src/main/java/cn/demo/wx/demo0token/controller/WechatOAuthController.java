package cn.demo.wx.demo0token.controller;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;

import cn.demo.wx.demo0token.model.WxUser;

public class WechatOAuthController extends WebBaseController {

	static ApiConfig config = ApiConfigKit.getApiConfig();
	final static String appId = config.getAppId();
	final static String secret = config.getAppSecret();

	/*
	 * 微信登陆回调
	 * /oauth2/wechatLoginCallBack?code=011ZCuYr1uMmPp0jUEYr1ZxyYr1ZCuYg&state=
	 * d29c16ad-603c-49a1-ad36-df096cb2042b
	 */
	public void wechatLoginCallBack() {
		log.info("公众号登录回调wxLoginCallBack");
		String code = getPara("code");
		String state = getPara("state");
		String scope = "";
		if (StrKit.isBlank(code)) {
			log.error("wxLoginCallBack error:no code");
			renderText("code is null");
		}
		String targeturl = "";
		targeturl=generateTargetUrl(state);

		try {

			SnsAccessToken accessToken = SnsAccessTokenApi.getSnsAccessToken(appId, secret, code);
			scope = accessToken.getScope();
			log.debug("accessToken:" +scope+":"+ accessToken.getJson());
			if (!accessToken.isAvailable()) {
				log.error("accessToken.isAvailable error:" + accessToken.getJson());
			}

			ApiResult apiResult = SnsApi.getUserInfo(accessToken.getAccessToken(), accessToken.getOpenid());
			log.debug("getUserInfo:" + apiResult.getJson());

			if (!apiResult.isSucceed()) {
				log.error("getUserInfo error:" + apiResult.getJson());
			}
			//WxUser.dao.addJsonUser(apiResult.getJson());
			super.wechatUserLogin(scope, apiResult.getJson());
			

		} catch (Exception e) {
			log.error("微信回调登陆失败", e);
			e.printStackTrace();
			return;
		}
		redirect(StrKit.notBlank(targeturl) ? targeturl : "/");

	}

	private String generateTargetUrl(String state) {
		log.debug("获得的state:" + state);
		String targetUrl = CacheKit.get(WxUser.CACHE_STATE,state);
		log.info("获得的targetUrl:" + targetUrl);
		return StrKit.isBlank(targetUrl) ? "/" : targetUrl;
	}
}
