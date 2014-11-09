package org.visitor.appportal.web.controller.common;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.visitor.beans.ResultJson;

@Controller
@RequestMapping("/publishinfo/")
public class PublishController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger("classLogger");

	@RequestMapping("timezone")
    public void timezone(HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		sendJSONResponse(resultJson, response);
	}
	
	@RequestMapping("language")
    public void language(HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		sendJSONResponse(resultJson, response);
	}
}
