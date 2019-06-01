package cn.demo.wx.demo0token.cache;

import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class CacheAccessTokenCache implements IAccessTokenCache {

	private final Cache cache;

    public CacheAccessTokenCache(String key) {
        this.cache = CacheKit.getCacheManager().getCache(key);
    }
	@Override
	public String get(String key) {
		Element ele= cache.get(key);
		return null==ele?"":String.valueOf(ele.getObjectValue());
	}

	@Override
	public void set(String key, String value) {
		cache.put(new Element(key, value));
	}

	@Override
	public void remove(String key) {
        cache.remove(key);
	}

}
