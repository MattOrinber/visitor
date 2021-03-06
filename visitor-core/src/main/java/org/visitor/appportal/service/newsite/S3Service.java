package org.visitor.appportal.service.newsite;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.visitor.appportal.web.utils.MixAndMatchUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;

@Service("s3Service")
public class S3Service {
	private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

	public void createNewFile(String key, InputStream input, String bucketName, ObjectMetadata meta) throws Exception {
		AmazonS3Client s3 = initS3();
		
		List<Bucket> listB = s3.listBuckets();
		
		if (listB.size() > 0) {
			System.out.println(">>>>>>>>>>>>>>> has buckets");
			logger.info(">>>>>>>>>>>>>>> has buckets");
			for (Bucket b : listB) {
				logger.info("bucket name: >" + b.getName() + "<");
				System.out.println("bucket name: >" + b.getName() + "<");
			}
		} else {
			logger.info(">>>>>>>>>>>>>>> not have buckets");
			System.out.println(">>>>>>>>>>>>>>> not have buckets");
		}
		
		
		PutObjectRequest put = new PutObjectRequest(bucketName, key, input, meta);
		//DeleteObjectRequest delObj = new DeleteObjectRequest(bucketName, key);
		AccessControlList accessControlList = new AccessControlList();
		Grantee grantee = GroupGrantee.AllUsers;
		Permission permission = Permission.Read;
		accessControlList.grantPermission(grantee, permission);
		put.setAccessControlList(accessControlList);
		//s3.deleteObject(delObj);
		s3.putObject(put);
	}
	public void createNewFile(String key, File file, String bucketName) throws Exception {
		AmazonS3Client s3 = initS3();
		PutObjectRequest put = new PutObjectRequest(bucketName, key, file);
		AccessControlList accessControlList = new AccessControlList();
		Grantee grantee = GroupGrantee.AllUsers;
		Permission permission = Permission.Read;
		accessControlList.grantPermission(grantee, permission);
		put.setAccessControlList(accessControlList);
		s3.putObject(put);
	}

	public InputStream getFile(String bucketName, String url) {
		AmazonS3Client s3 = initS3();
		S3Object object = s3.getObject(MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain), url);
		InputStream fis = object.getObjectContent();
		return fis;
	}

	private AmazonS3Client initS3() {
		AWSCredentials credentials = new BasicAWSCredentials(
				MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.accessKey),
				MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.secretKey));
		AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(
				credentials); // // Manually start a session.

		GetSessionTokenRequest getSessionTokenRequest = new GetSessionTokenRequest();
		// Following duration can be set only if temporary credentials are
		// requested
		// by an IAM user. getSessionTokenRequest.setDurationSeconds(7200);
		// GetSessionToken
		// API Version 2006-03-01 17Amazon Simple Storage Service 开发人员指南
		// 使用 IAM 用户临时证书进行请求
		GetSessionTokenResult sessionTokenResult = stsClient
				.getSessionToken(getSessionTokenRequest);
		Credentials sessionCredentials = sessionTokenResult.getCredentials(); //
		// Package the temporary security credentials as // a
		// BasicSessionCredentials ob
		// ject, for an Amazon S3 client object to use.

		BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
				sessionCredentials.getAccessKeyId(),
				sessionCredentials.getSecretAccessKey(),
				sessionCredentials.getSessionToken());
		// The following will be part of your less trusted code. You provide
		// temporary security
		// credentials so it can send authenticated requests to Amazon S3.
		// Create Amazon S3 client by passing in the basicSessionCredentials
		// object.
		AmazonS3Client s3 = new AmazonS3Client(basicSessionCredentials); // Test.
		return s3;
	}
}
