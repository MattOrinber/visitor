package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.visitor.appportal.service.newsite.S3Service;
import org.visitor.appportal.service.newsite.VisitorContainerService;
import org.visitor.appportal.service.newsite.redis.ContainerRedisService;
import org.visitor.appportal.visitor.beans.ContainerTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.Container;
import org.visitor.appportal.web.utils.FloopyInfo;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.RegisterInfo;

import com.amazonaws.services.s3.model.ObjectMetadata;

@Controller
@RequestMapping("/container/")
public class ContainerController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(ContainerController.class);
	
	@Autowired
	private VisitorContainerService visitorContainerService;
	@Autowired
	private ContainerRedisService containerRedisService;
	@Autowired
	private S3Service s3Service;
	
	@RequestMapping(value = "addOne", method = POST)
	public void addOne(HttpServletRequest request,
			HttpServletResponse response) {
		ContainerTemp ct = super.getContainerJson(request);
		
		Container newCon = new Container();
		newCon.setContainerName(ct.getNameStr());
		newCon.setContainerType(Integer.valueOf(ct.getTypeStr()));
		newCon.setContainerProductkey(ct.getValueStr());
		
		visitorContainerService.saveContainer(newCon);
		containerRedisService.saveContainerToRedis(newCon);
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping(value = "updateSingular", method = POST)
	public void updateSingular(HttpServletRequest request,
			HttpServletResponse response) {
		ContainerTemp ct = super.getContainerJson(request);
		
		Container newCon = visitorContainerService.getContainerByName(ct.getNameStr());
		newCon.setContainerType(Integer.valueOf(ct.getTypeStr()));
		newCon.setContainerProductkey(ct.getValueStr());
		
		visitorContainerService.saveContainer(newCon);
		containerRedisService.saveContainerToRedis(newCon);
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping(value = "picture")
	public void containerPictureCreate(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value = "containerPicture", required = true) MultipartFile fileContainerPic, 
			@RequestParam(value = "cid", required = true) String cNameStr) {
		Container con = containerRedisService.getContainerFromRedisByName(cNameStr);
		
		Integer result = 0;
		String resultDesc = "";
		ResultJson resultJ = new ResultJson();
		
		if (fileContainerPic != null && !fileContainerPic.isEmpty()) {
			
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(fileContainerPic.getSize());
			meta.setContentType(fileContainerPic.getContentType());
			try {
				String awsBucketName = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
				String imgDomain = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain);
				
				if (log.isInfoEnabled()) {
					if (con == null) {
						log.info("container null");
					} else {
						log.info("container can be used");
					}
				}
				
				String fileOriUrl = "container/icon-"+con.getContainerId()+"/"+fileContainerPic.getOriginalFilename();
				
				s3Service.createNewFile(fileOriUrl, fileContainerPic.getInputStream(), awsBucketName, meta);
				
				String finalFileUrl = imgDomain + awsBucketName + "/" + fileOriUrl;
				con.setContainerPicpaths(finalFileUrl);
				
				visitorContainerService.saveContainer(con);
				containerRedisService.saveContainerToRedis(con);
				
				result = 0;
				resultDesc = RegisterInfo.USER_ICON_SET_SUCCESS;
				resultJ.setResult(result);
				resultJ.setResultDesc(resultDesc);
				resultJ.setImageUrl(finalFileUrl);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		super.sendJSONResponse(resultJ, response);
	}
}
