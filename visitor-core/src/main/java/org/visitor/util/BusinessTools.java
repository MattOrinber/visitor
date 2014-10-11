/**
 * 
 */
package org.visitor.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import org.visitor.appportal.domain.Category;
import org.visitor.util.AppStringUtils;

/**
 * @author mengw
 *
 */
public abstract class BusinessTools {

	/**
	 * 根据高度，宽度，平台，及分辨率列表以获取最佳的分辨率
	 * @param width
	 * @param height
	 * @param platformVersion
	 * @param resolutionList
	 * @return
	 */
	public static Category getBestResolution(Integer width, Integer height,
			Category platformVersion, List<Category> resolutionList) {
		Map<Integer, List<Category>> map = new HashMap<Integer, List<Category>>();
		for(Category category : resolutionList) {
			if(null != category && StringUtils.isNotBlank(category.getName())) {
				String[] array = AppStringUtils.getScreenWidthAndHeight(category.getName());
				if(array.length == 2) {
					//Find the widht match 
					if(StringUtils.isNumeric(array[0])) {
						Integer currentWidth = Integer.valueOf(array[0]).intValue();
						if(currentWidth.intValue() >= width.intValue()) {
							category.setEnName(array[1]);//Just for later use.
							if(map.containsKey(currentWidth)) {
								map.get(currentWidth).add(category);
							} else {
								List<Category> catList = new ArrayList<Category>();
								catList.add(category);
								map.put(currentWidth, catList);
							}
						}
					}
				}
			}
		}
		if(map.size() > 0) {
			//Find the min value for the width is larger that specified.
			Integer[] sorted = new Integer[map.size()];//(String[])map.keySet().toArray();
			sorted = map.keySet().toArray(sorted);
			Arrays.sort(sorted);
			List<Category> list = map.get(sorted[0]);
			if(list.size() == 1) {
				return list.get(0);
			} else {
				Map<Integer, Category> heightMap = new HashMap<Integer, Category>();
				for(Category cat : list) {
					if(StringUtils.isNumeric(cat.getEnName())) {
						Integer currentHeight = Integer.valueOf(cat.getEnName());
						if(currentHeight.intValue() >= height.intValue()) {
							heightMap.put(currentHeight, cat);
						}
					}
				}
				if(heightMap.size() > 0) {
					sorted = new Integer[heightMap.size()];
					sorted = heightMap.keySet().toArray(sorted);
					
					Arrays.sort(sorted);
					
					return heightMap.get(sorted[0]);
				} else {//This height is too big to find a suitable one.
					Integer[] names = new Integer[list.size()];
					for(int i=0;i<list.size();i++) {
						heightMap.put(Integer.valueOf(list.get(i).getEnName()), list.get(i));
					}
					names = heightMap.keySet().toArray(names);
					Arrays.sort(names);
					return heightMap.get(names[names.length - 1]);
				}
			}
		} else {//This height is too big to find a suitable one.
			Map<Long, Category> areaMap = new HashMap<Long, Category>();
			for(Category category : resolutionList) {
				Long size = AppStringUtils.getScreenSize(category.getName());
				if(null != size) {
					areaMap.put(size, category);
				}
			}
			if(areaMap.size() > 0) {
				Long[] areas = new Long[areaMap.size()];
				areas = areaMap.keySet().toArray(areas);
				Arrays.sort(areas);
				return areaMap.get(areas[areas.length - 1]);
			}
			return null;
		}
	}
}
