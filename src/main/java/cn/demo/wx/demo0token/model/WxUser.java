package cn.demo.wx.demo0token.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.demo.wx.demo0token.model.base.BaseWxUser;
import net.sf.json.JSONObject;

public class WxUser extends BaseWxUser<WxUser> {

	public static final WxUser dao = new WxUser().dao();
	Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String OPENID="openid";
	public static final String UNIONID="unionid";
	public static final String CACHE_WXTOKEN="wxtokencache";
	public static final String CACHE_STATE="wxstatecache";
	public void addJsonUser(String jsonuser) {
		log.info("addJsonUser:"+jsonuser);
		JSONObject userinfo = JSONObject.fromObject(jsonuser);
		WxUser user = findOpenidUser(userinfo.optString("openid", ""));
		boolean flag = false;
		if(null==user) {
			user = new WxUser();
			flag=true;
		}
		user.setOpenid(userinfo.optString("openid",""));
		user.setUnionid(userinfo.optString("unionid", ""));
		user.setNickname(userinfo.optString("nickname",""));
		user.setHeadimage(userinfo.optString("headimgurl",""));
		if(flag) {
			user.save();
		}else {
			user.update();
		}
	}
	
	public WxUser findOpenidUser(String openid) {
		return findById(openid);
	}
}
