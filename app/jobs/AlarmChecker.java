package jobs;

import java.util.Calendar;
import java.util.Iterator;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

import com.sun.media.sound.AlawCodec;

import models.ObservedAlarm;
import models.Rule;
import models.Sensor;

@Every("1h")
public class AlarmChecker extends Job  {

	public AlarmChecker(){
		Logger.info("Alarm checker started!");
	}
	public void doJob() throws Exception {
		Logger.info("Verify Active Alarms!");

		Iterator it = ObservedAlarm.findAll().iterator();

		while (it.hasNext()){
			ObservedAlarm al = (ObservedAlarm) it.next();
			if(!al.Resolved && ! al.Expired){
				if(al.expiresOn < System.currentTimeMillis()){
					al.setExpired();
					al.save();
					Rule rl = Rule.findByName(al.instanceOf);
					rl.activeAlarm = false;
					rl.save();
				}
			}
			
		}
	}
}
