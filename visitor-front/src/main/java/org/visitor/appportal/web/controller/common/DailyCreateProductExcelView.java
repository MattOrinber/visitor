/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;

/**
 * @author mengw
 * 
 */
@Component("DailyCreateProductExcelView")
public class DailyCreateProductExcelView extends AbstractExcelView {
	private static final SimpleDateFormat fileSdf = new SimpleDateFormat("yyyyMMdd");
	private String fileName = "ProductDailyCreate" + fileSdf.format(new Date()) + ".xls";

	/**
	 * 
	 */
	public DailyCreateProductExcelView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		// 产生Excel表头
		HSSFSheet sheet = workbook.createSheet("产品列表");
		HSSFRow header = sheet.createRow(0); // 第0行
		// 产生标题列
		header.createCell(0).setCellValue("产品ID");
		header.createCell(1).setCellValue("产品名称");
		header.createCell(2).setCellValue("产品分类");
		header.createCell(3).setCellValue("分类ID");
		header.createCell(4).setCellValue("所属频道及频道ID");
		header.createCell(5).setCellValue("产品来源");
		header.createCell(6).setCellValue("CP");
		header.createCell(7).setCellValue("上架时间");
		header.createCell(8).setCellValue("创建人");

		@SuppressWarnings("unchecked")
		List<ProductList> list = (List<ProductList>) model.get("data");
		// 填充数据
		int rowNum = 1;
		for (Iterator<ProductList> it = list.iterator(); it.hasNext();) {
			ProductList product = it.next();
			HSSFRow row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(product.getProductId());
			row.createCell(1).setCellValue(product.getName());
			row.createCell(2).setCellValue(product.getCategory().getName());
			row.createCell(3).setCellValue(product.getCategoryId());
			row.createCell(4).setCellValue(getProductFolder(product.getFolders()));
			row.createCell(5).setCellValue(product.getProductSource() == null ? "" : 
				product.getProductSource().getName());
			row.createCell(6).setCellValue(product.getOperator().getName());
			row.createCell(7).setCellValue(product.getPublishDate() == null ? "" : 
				sdf.format(product.getPublishDate()));
			row.createCell(8).setCellValue(product.getCreateBy());
		}
	}

	private String getProductFolder(List<Folder> folders) {
		StringBuffer sb = new StringBuffer();
		if (null != folders) {
			for (Folder f : folders) {
				sb.append(f.getFolderId()).append("  ").append(f.getName())
						.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
