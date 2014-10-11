/**
 * 
 */
package org.visitor.appportal.redis.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

/**
 * @author mengw
 *
 */
public class CompressStringRedisSerializer extends StringRedisSerializer {
	private final Charset charset;
	/**
	 * 
	 */
	public CompressStringRedisSerializer() {
		this(Charset.forName("UTF-8"));
	}

	/**
	 * @param charset
	 */
	public CompressStringRedisSerializer(Charset charset) {
		Assert.notNull(charset);
		this.charset = charset;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.keyvalue.redis.serializer.StringRedisSerializer#deserialize(byte[])
	 */
	@Override
	public String deserialize(byte[] bytes) {
		if(null != bytes) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				GzipCompressorInputStream gzip = new GzipCompressorInputStream(bis);
			    byte[] buf = new byte[1024];
			    int num = -1;
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    while((num = gzip.read(buf, 0, buf.length)) != -1) {
			    	baos.write(buf, 0, num);
			    }
			    bytes = baos.toByteArray();
			    baos.flush();
			    baos.close();
			    gzip.close();
			    bis.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return super.deserialize(bytes);
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.keyvalue.redis.serializer.StringRedisSerializer#serialize(java.lang.String)
	 */
	@Override
	public byte[] serialize(String string) {
		if(null != string) {
			byte[] data = string.getBytes(charset);
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(bos);
				gzip.write(data);
				gzip.close();
				data = bos.toByteArray();
				bos.close();
			}catch(Exception e){
				e.printStackTrace();
			}  			
			return data;
		}
		return super.serialize(string);
	}
}
