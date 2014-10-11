/**
 * 
 */
package org.visitor.appportal.redis.support;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author mengw
 *
 */
public class ByteSerializationRedisSerializer implements RedisSerializer<byte[]> {

	/**
	 * 
	 */
	public ByteSerializationRedisSerializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] serialize(byte[] t) throws SerializationException {
		return t;
	}

	@Override
	public byte[] deserialize(byte[] bytes) throws SerializationException {
		return bytes;
	}

}
