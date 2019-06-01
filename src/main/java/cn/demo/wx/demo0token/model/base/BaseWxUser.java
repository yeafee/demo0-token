package cn.demo.wx.demo0token.model.base;

import com.jfinal.plugin.activerecord.Model;

public class BaseWxUser<M extends BaseWxUser<M>> extends Model<M>  {

	public BaseWxUser<M> setOpenid(String openid) {
		set("openid", openid);
		return this;
	}
	
	public String getOpenid() {
		return getStr("openid");
	}
		
	public BaseWxUser<M> setUnionid(String unionid) {
		set("unionid", unionid);
		return this;
	}
	
	public String getUnionid() {
		return getStr("unionid");
	}
	public BaseWxUser<M> setNickname(String nickname) {
		set("nickname", nickname);
		return this;
	}
	
	public String getNickname() {
		return getStr("nickname");
	}
	
	public BaseWxUser<M> setHeadimage(String headimage) {
		set("headimage", headimage);
		return this;
	}
	
	public String getHeadimage() {
		return getStr("headimage");
	}
	
	public BaseWxUser<M> setCreated(Integer created) {
		set("created", created);
		return this;
	}
	
	public String getCreated() {
		return getStr("created");
	}
}
