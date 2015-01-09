package org.visitor.appportal.web.mongo;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.visitor.appportal.visitor.beans.mongo.BasicMongoBean;
import org.visitor.appportal.visitor.beans.mongo.ProductMongoBean;
import org.visitor.appportal.visitor.beans.mongo.UserMongoBean;
import org.visitor.appportal.web.utils.MixAndMatchUtils;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoTemplate {
	private static final Logger log = LoggerFactory.getLogger(MongoTemplate.class);
	
	private static MongoTemplate instance = null;
	private static Integer BlockingQueueSize = 0;
	private static String mongo_host = "";
	private static String mongo_port = "";
	private static BlockingQueue<MongoOperation> mongoExecutionQueue = null;
	private static MongoClient mongoClient = null;
	
	private static final String visitor_db = "visitor_db";
	private static final String user_collection = "user_collection";
	private static final String product_collection = "product_collection";
	private static final String other_collection = "other_collection";
	
	public static MongoTemplate getInstance() {
		if (instance == null) {
			instance = new MongoTemplate();
			mongo_host = MixAndMatchUtils.getMongoConfig("mongo.host");
			mongo_port = MixAndMatchUtils.getMongoConfig("mongo.port");
			BlockingQueueSize = Integer.valueOf(MixAndMatchUtils.getMongoConfig("mongo.blockingQueueSize"));
			if (BlockingQueueSize > 0) {
				mongoExecutionQueue = new LinkedBlockingQueue<MongoOperation>(BlockingQueueSize);
			} else {
				if (log.isInfoEnabled()) {
					log.info("blocking queue list size definition from mongoUtils.properties not right! use default size (20)");
				}
				BlockingQueueSize = 20;
				mongoExecutionQueue = new LinkedBlockingQueue<MongoOperation>(BlockingQueueSize);
			}
			
			if (StringUtils.isEmpty(mongo_host)) {
				if (log.isInfoEnabled()) {
					log.info("mongo host not configured");
				}
			} else if (StringUtils.isEmpty(mongo_port)) {
				if (log.isInfoEnabled()) {
					log.info("mongo port not configured");
				}
			} else {
				try {
					mongoClient = new MongoClient( mongo_host , Integer.valueOf(mongo_port));
					
					ExecutorService service = Executors.newCachedThreadPool();
					MongoExecutor consumer = new MongoExecutor();
					
					service.submit(consumer);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return instance;
	}
	
	public void insert(Object obj, int type) {
		MongoOperation moTemp = new MongoOperation();
		moTemp.setOperationType(type);
		moTemp.setSaveObject(obj);
		moTemp.setFetchDB(visitor_db);
		if (obj.getClass() == UserMongoBean.class) {
			moTemp.setFetchCollection(user_collection);
		} else if (obj.getClass() == ProductMongoBean.class){
			moTemp.setFetchCollection(product_collection);
		} else {
			moTemp.setFetchCollection(other_collection);
		}
		
		try {
			mongoExecutionQueue.put(moTemp);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void executeMongoOperation() {
		try {
			MongoOperation moTemp = mongoExecutionQueue.take();
			
			if (moTemp.getOperationType() == MongoOperationType.first_time_save) {
				DB db = mongoClient.getDB(moTemp.getFetchDB());
				DBCollection coll = db.getCollection(moTemp.getFetchCollection());
				
				BasicMongoBean basicMongoBean = (BasicMongoBean)moTemp.getSaveObject();
				basicMongoBean.convertToBSONObject();
				
				coll.insert(basicMongoBean);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
