package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.redis.RedisKeys;
import org.visitor.appportal.redis.support.AvroObjectWrapper;
import org.visitor.appportal.redis.support.StringByteRedisTemplate;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/domain/redis/")
public class RedisController {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AvroObjectWrapper avroObjectWrapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private StringByteRedisTemplate userStringByteRedisTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;	

    @RequestMapping(value = "show", method = { GET, POST })
    public String show() {
    	return "domain/common/redis/show";
    }
	
	@RequestMapping(value = "ajax/value", method = GET)
	@ResponseBody
	public String ajaxValue(@RequestParam(value = "key", required = true) String key) {
		
		String value = (String)stringRedisTemplate.opsForValue().get(key);
		if (StringUtils.isEmpty(value)) {
			return "";
		}
		return value;
	}
    
	@RequestMapping(value = "ajax/string", method = GET)
	@ResponseBody
	public String ajaxString(@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "para", required = true) String para) {
		
		String value = (String)stringRedisTemplate.opsForHash().get(key, para);
		if (StringUtils.isEmpty(value)) {
			return "";
		}
		return value;
	}
	
	@RequestMapping(value = "ajax/compressstring", method = GET)
	@ResponseBody
	public String ajaxCompressString(@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "para", required = true) String para) {
		if(StringUtils.equalsIgnoreCase(key, "h-userpreference")) {
			String prefkey = RedisKeys.getUserPrerenceKey(para);
			byte[] value = userStringByteRedisTemplate.opsForValue().get(prefkey);		
			if(null != value && value.length > 0) {
				try {
					return JSON.toJSONString(this.avroObjectWrapper.convert2UserPreference(key, value));
				}catch (Exception e) {
					log.error(key, e);
					return "";
				}
			}
			return "";
		}
		String value = (String)compressStringRedisTemplate.opsForHash().get(key, para);
		if (StringUtils.isEmpty(value)) {
			return "";
		} else {
		}
		return value;
	}
	
	@RequestMapping(value = "ajax/list", method = GET)
	@ResponseBody
	public List<String> ajaxList(@RequestParam(value = "key", required = true) String key) {
		
		Map<Object, Object> entries = compressStringRedisTemplate.opsForHash().entries(key);
		List<String> list = new ArrayList<String>();
		if(null != entries) {
			for(Object obj : entries.values()) {
				list.add((String)obj);
			}
		}
		return list;
	}
	
	@RequestMapping(value = "ajax/set", method = GET)
	@ResponseBody
	public String ajaxSet(@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Set<String> pids = stringRedisTemplate.opsForZSet().reverseRange(key, (page - 1) * size, page * size - 1);
		return pids.toString();
	}
	
	@RequestMapping(value = "ajax/count", method = GET)
	@ResponseBody
	public String ajaxCount(@RequestParam(value = "key", required = true) String key) {
		Long size = stringRedisTemplate.opsForHash().size(key);
		if (size != null) {
			return size.toString();
		} else {
			return "0";
		}
	}
    
}
