/**
 * 
 */
package org.visitor.appportal.redis.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.visitor.app.portal.model.UserPreference;

/**
 * @author mengw
 *
 */
@Component
public class AvroObjectWrapper {
	protected MessagePack messagePack = new MessagePack();

	protected Logger log = LoggerFactory.getLogger(AvroObjectWrapper.class);

	/**
	 * 对象转化为字节数组
	 * @param object
	 * @param class1
	 * @return
	 */
	public byte[] objectToByte(Object object, Class<?> class1) {
		//字节输出流
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			messagePack.write(bao, object);
			return bao.toByteArray();
		} catch (Exception e) {
			log.error("objectToByte error:", e);
			return null;
		}
	}
	
	public UserPreference convert2UserPreference(String uid, byte[] bytes){

		if(bytes == null) return null;//非空验证
	    
		try {
			return messagePack.read(bytes, UserPreference.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
