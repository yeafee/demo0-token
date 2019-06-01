package cn.demo.wx.demo0token.config;

import com.jfinal.config.Routes;

import cn.demo.wx.demo0token.controller.IndexController;
import cn.demo.wx.demo0token.controller.WechatOAuthController;

public class TokenRoutes extends Routes {

	@Override
	public void config() {
		add("/", IndexController.class);
		add("wechatOAuth", WechatOAuthController.class);
	}
}
