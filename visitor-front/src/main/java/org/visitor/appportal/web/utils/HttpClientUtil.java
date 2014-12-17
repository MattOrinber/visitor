package org.visitor.appportal.web.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	protected static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static String httpPost(String url, HashMap<String,String> map) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		if (!map.isEmpty()) {
			for(Map.Entry<String, String> entry : map.entrySet()) {
				urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}			
		}
    	try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse httpResponse = client.execute(post);
			StatusLine statusLine = httpResponse.getStatusLine();
	        int statusCode = statusLine.getStatusCode();
	        if(200 == statusCode) {
	        	return readHtmlContentFromEntity(httpResponse.getEntity());
			} 
        } catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if(null != post) {
				post.releaseConnection();
			}
		}
		return null;
	}
	
	public static String httpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            if(null != httpResponse) {
	            StatusLine statusLine = httpResponse.getStatusLine();
	            int statusCode = statusLine.getStatusCode();
	            if(200 == statusCode) {
	            	return readHtmlContentFromEntity(httpResponse.getEntity());
	            }
            }
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
        } finally {
            if(httpGet != null){
                httpGet.releaseConnection();
            }
        }		
		return null;
	}
	
	public static String httpGetJSON(String url) {
        HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            if(null != httpResponse) {
	            StatusLine statusLine = httpResponse.getStatusLine();
	            int statusCode = statusLine.getStatusCode();
	            if(200 == statusCode) {
	            	return readJsonStrContentFromEntity(httpResponse.getEntity());
	            }
            }
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
        } finally {
            if(httpGet != null){
                httpGet.releaseConnection();
            }
        }		
		return null;
	}

    /**
     * 从response返回的实体中读取页面代码
     * @param httpEntity Http实体
     * @return 页面代码
     * @throws IOException
     */
    private static String readHtmlContentFromEntity(HttpEntity httpEntity) throws IOException {
    	if(null != httpEntity) {
    		return IOUtils.toString(httpEntity.getContent(), ContentType.getOrDefault(httpEntity).getCharset().toString());
    	} 
    	return null;
    }
    
    private static String readJsonStrContentFromEntity(HttpEntity httpEntity) throws IOException {
    	if(null != httpEntity) {
    		return IOUtils.toString(httpEntity.getContent());
    	} 
    	return null;
    }
}
