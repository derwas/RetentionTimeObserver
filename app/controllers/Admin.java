package controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.ObservedAlarm;
import models.Rule;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Admin extends Controller {

	
	public static void createNew()  {
		render();
	}
	
	 public static void deleteRule(String name){
	        Rule tobedeleted = Rule.findByName(name);
	        tobedeleted.delete();
	        Rules.index();
	 }
	 
	 public static void save(String name, String sensorID,String location, double minVolume, String action, int pDays, int pHours, int pMinutes, int eDays, int eHours, int eMinutes){
		 long expires = (eMinutes* 60*1000L) + (eHours * 60 * 60 * 1000L) + (eDays *24 * 60 * 60 * 1000L);
		 long period = (pMinutes* 60*1000L) + (pHours * 60 * 60 * 1000L) + (pDays *24 * 60 * 60 * 1000L);

					
		 Rule rule = new Rule(name, sensorID, minVolume,period, action, expires, location );
		 rule.save();

		 Rules.index();
	 }
	 
	    public static void about() {
	        Set<Map.Entry<Object,Object>> parameters = Play.configuration.entrySet();
	        render(parameters);
	    }
	    
	 public static void deleteAll(){
	        List<ObservedAlarm> allObservedAlarms = ObservedAlarm.findAll();
	        Iterator it = allObservedAlarms.iterator();
	        while (it.hasNext()){
	        	ObservedAlarm occ = (ObservedAlarm) it.next();
	        	deleteAlarm(occ.getId().toString());
	        	Rule rl = Rule.findByName(occ.instanceOf);
	        	if (rl!=null){
     			rl.activeAlarm = false;
	        		rl.save();
     		}
	        }
		 
	        ObservedAlarms.index();
	 }

	 public static void deleteAlarm(String id){
	        List<ObservedAlarm> allObservedAlarms = ObservedAlarm.findAll();
	        Iterator it = allObservedAlarms.iterator();
	        while (it.hasNext()){
	        	ObservedAlarm occ = (ObservedAlarm) it.next();
	        	if(occ.id==Long.parseLong(id)){
	        		Rule rl = Rule.findByName(occ.instanceOf);
	        		if (rl!=null){
	        			rl.activeAlarm = false;
		        		rl.save();
	        		}
	        		occ.delete();
	        	}
	        }   
	        ObservedAlarms.index();
	 }

	 public static void resolve(String id){
	        List<ObservedAlarm> allObservedAlarms = ObservedAlarm.findAll();
	        Iterator it = allObservedAlarms.iterator();
	        while (it.hasNext()){
	        	ObservedAlarm occ = (ObservedAlarm) it.next();
	        	if(occ.id==Long.parseLong(id)){
	        		occ.resolve(System.currentTimeMillis(), Security.connected());
	        		occ.save();
	        		Rule rl = Rule.findByName(occ.instanceOf);
	        		if (rl!=null){
	        			rl.activeAlarm = false;
		        		rl.save();
	        		}
	        	}
	        }   
	        ObservedAlarms.index();
	 }
}
