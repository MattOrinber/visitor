package org.visitor.appportal.web.mongo;

public class MongoExecutor implements Runnable {
	private MongoTemplate mongoTemplate = MongoTemplate.getInstance();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{

            while(true){
            	
            	mongoTemplate.executeMongoOperation();
            	
            	Thread.sleep(10);
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
