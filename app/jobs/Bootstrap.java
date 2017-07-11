package jobs;

import models.Sensor;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job{
public void doJob() throws Exception {
		
		// Flush data from all tables
		//Fixtures.deleteDatabase();
		//Fixtures.deleteAllModels();
		
		//Logger.info("Number of sensors available n the system : "+Sensor.findAll().size());
		Logger.info("Application started...");
		
		//models.KafkaServer server = new models.KafkaServer("Main Readings", Play.configuration.getProperty("kafkaHost"),
		//		Play.configuration.getProperty("kafkaPort"), 
		//		Play.configuration.getProperty("zookeeperHost"),
		//		Play.configuration.getProperty("zookeeperPort"),
		//		"retention-time-observer");
		
				
		//models.KafkaTopic readFromTopic = new models.KafkaTopic(Play.configuration.getProperty("kafkaReadTopic"), server);
		//models.KafkaTopic writeToTopic = new models.KafkaTopic(Play.configuration.getProperty("kafkaWriteTopic"), server);

		
		//new KafkaConsumer(readFromTopic).now();
		//new TopicListener()
                //.startTopicListener(readFromTopic);
		
		new RuleChecker().now();
		new AlarmChecker().now();
		
		Sensor.deleteAll();
		//new TopicPublisher(writeToTopic);
		
}
}
