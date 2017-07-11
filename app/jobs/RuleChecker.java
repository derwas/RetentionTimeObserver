package jobs;

import java.util.Calendar;
import java.util.Iterator;

import play.Logger;
import play.jobs.Job;
import play.jobs.On;

import com.sun.media.sound.AlawCodec;

import models.ObservedAlarm;
import models.Rule;
import models.Sensor;

@On("0 0 8 * * ?")
public class RuleChecker extends Job  {

	public RuleChecker(){
		Logger.info("Rule checker started!");
	}
	public void doJob() throws Exception {
		Logger.info("Rule verification!");

		Iterator it = Rule.findAll().iterator();

		while (it.hasNext()){
			Rule rl = (Rule) it.next();
			//rl.generateDescription();
			//rl.save();
			if(rl.active){
				Logger.info("Rule \""+rl.ruleName+"\"");
				if(rl.activeAlarm){
					Logger.info("There is already an active alarm associate to this rule! No further verifications required.");
				}
				else{
					Logger.info("There is no active alarm associate to this rule! Rule is being verified ...");
				}
				String test = rl.verifyRule();
				if (test.contains("Attention Required") ){
					if(!rl.activeAlarm){
						new ObservedAlarm(rl.ruleName, System.currentTimeMillis(), rl.sensorID,test).save();
						rl.activeAlarm = true;
						rl.save();
						Logger.info("Rule violated: new alarm created!");
					}
				}
				else{
					Logger.info("Everything is OK!");
				}
			}
			
		}
	}
}
