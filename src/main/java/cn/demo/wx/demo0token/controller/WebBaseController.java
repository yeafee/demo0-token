package cn.demo.wx.demo0token.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import cn.demo.wx.demo0token.model.WxUser;
import net.sf.json.JSONObject;

public class WebBaseController extends Controller {

	protected Logger log = LoggerFactory.getLogger(getClass());
	protected String unionId() {
//		TODO 提交去除注释 
//		return "olMmnjp9ggqAYoEmxDZ4UCEtaOus";

		return getCookie(WxUser.UNIONID);
	}

	protected String openId() {
//		return "oNAYTt4Uz5ZGqGUrUnkxhPKXOiuQ";
		return getCookie(WxUser.OPENID);
	}

	protected boolean wechatUserLogin(String scope, String jsonUserInfo) {
		log.debug("handleWechatUserInfo scope:" + scope + "info:" + jsonUserInfo + "]");
		JSONObject userinfo = JSONObject.fromObject(jsonUserInfo);
		// 小程序注册用户传入注册信息有unionid，有unionid用户，补全其他openid等信息
		String unionId = userinfo.optString(WxUser.UNIONID, "");
		String openid = userinfo.optString(WxUser.OPENID);
		String cookieOpenid = getCookie(WxUser.OPENID);
		// scope.equals(WechatUtil.SNSAPI_USERINFO)
		// || scope.equals(WechatUtil.SNSAPI_LOGIN))
		setCookie(WxUser.OPENID, openid, Integer.MAX_VALUE);
		if (StrKit.notBlank(unionId)) {
			log.debug("handleWechatUserInfo setUnionId:" + unionId);
			setCookie(WxUser.UNIONID, unionId, Integer.MAX_VALUE);
		}

		return true;
	}
}
