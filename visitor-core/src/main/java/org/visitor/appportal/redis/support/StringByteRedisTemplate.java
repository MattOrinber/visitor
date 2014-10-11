/**
 * 
 */
package org.visitor.appportal.redis.support;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author mengw
 *
 */
public class StringByteRedisTemplate extends RedisTemplate<String, byte[]> {

	public StringByteRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<byte[]> byteRedisSerializer = new ByteSerializationRedisSerializer();
		
		//Key类型为string，值类型为byte[]
		setKeySerializer(stringSerializer);
		setValueSerializer(byteRedisSerializer);
		
		setHashKeySerializer(stringSerializer);
		setHashValueSerializer(byteRedisSerializer);
	}
	
	public StringByteRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}
}
