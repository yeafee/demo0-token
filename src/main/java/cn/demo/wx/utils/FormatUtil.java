package cn.demo.wx.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.StrKit;


/**
 * @author Andy 
 * @date   2016年3月30日
 */

public class FormatUtil {
	
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
	public static boolean isMobile(HttpServletRequest request) {
		boolean mobile = false;
		String userAgent = request.getHeader("User-Agent");
		if (StrKit.notBlank(userAgent)) {
			Pattern p = Pattern.compile("Android|iPhone|SymbianOS|Windows Phone|iPod");
			Matcher m = p.matcher(userAgent);
			mobile = m.find();
		}
		return mobile;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}
	
	/**
	 * 把Map所有元素排序，并按照"参数=参数值"的模式用"&"字符拼接成字符串
	 * 
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createParaString(Map<String, String> params) {
		java.util.List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			// String value = URLEncoder.encode(params.get(key));

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}
	
	/**
	 * 微信内嵌浏览器
	 * @param request
	 * @return
	 */
	public static boolean isWechat(HttpServletRequest request) {
		boolean wechat = false;
		String userAgent = request.getHeader("User-Agent");
		if(StringUtils.isNotEmpty(userAgent)){
			wechat = userAgent.contains("MicroMessenger/");
		}
		return wechat;
	}
	/**
	 * Ajax请求
	 * @param request
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null   && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}
	
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("0:0:0:0:0:0:0:1".equals(ip)){
            	try {
					ip = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
            }
        }
        String[] ipArg = ip.split(",");
        return ipArg[0];
	}
	
	public static String getReferer(HttpServletRequest request){
		return (StringUtils.isBlank( request.getHeader("referer")) ? ( StringUtils.isBlank(request.getHeader("Referer")) ? request.getHeader("HTTP_REFERER") :request.getHeader("Referer") ) : request.getHeader("referer")).split("#")[0];
	}
	
	
	/**
	 * @param date 日期
	 * @param type 0,yyyy-MM-dd;1,yyyy年MM月dd日;2,yyyy/MM/dd;3,yyyyMMdd
	 * @return
	 */
	public static String dateToString(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return date == null ? null : sdf.format(date); 
	}
	
	public static String getBaseUrl(HttpServletRequest request){
		return request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath();
	}
	
	private static final boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
	
}
