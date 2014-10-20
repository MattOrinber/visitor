package org.visitor.appportal.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("dataReadService")
public class DataReadService {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
	
	public List<Map<String, String>> getRecommendSite(Integer serivceId) {
		String sql = "select site_id, name from site where status = 0";	
		
		List<Map<String, Object>>  results = jdbcTemplate.queryForList(sql);
		
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		
		if (results != null && results.size() > 0) {
			for (Map<String, Object> r : results) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("key", String.valueOf(r.get("site_id")));
				map.put("value", String.valueOf(r.get("name")));
				out.add(map);				
			}
		}
		return out;
	}
	
	public List<Map<String, String>> getRecommendSiteFolder(Integer siteId) {
		String sql = "select f.folder_id ,f.name from folder f ,product_site_folder psf " +
				"where f.folder_id = psf.folder_id and f.status = 0 and psf.site_id = ? group by psf.folder_id";	
		List<Map<String, Object>>  results = jdbcTemplate.queryForList(sql, siteId);
		
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		
		if (results != null && results.size() > 0) {
			for (Map<String, Object> r : results) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("key", String.valueOf(r.get("folder_id")));
				map.put("value", String.valueOf(r.get("name")));
				out.add(map);				
			}
		}
		return out;
	}
	
	public String getProductName(Long productId) {
		String sql = "select name from product_list where product_id = ?";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, productId);
		if (results != null && results.size() > 0) {
			return String.valueOf(results.get(0).get("name"));
		}
		return null;
	}
}
