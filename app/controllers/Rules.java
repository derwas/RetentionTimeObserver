/**
 * @copyright 	National University of Ireland, Galway
 * @author      Wassim Derguech <wassim.derguech @ gmail.com>
 * @version     1.0
 * @since       2015-10-13          
 */  
package controllers;

import java.util.List;

import jobs.RuleChecker;
import models.ObservedAlarm;
import models.Rule;
import play.Logger;
import play.mvc.Controller;
import play.mvc.With;

public class Rules extends Controller {
	

	public static void index() {
        List<Rule> allRules = Rule.findAll();
        render(allRules);
    }
	public static void createNew() throws Exception {
		render();
	}
	


	 public static void setActive(String name) throws Exception{
	        Rule tobeActive = Rule.findByName(name);
	        tobeActive.setActive(true);
	        tobeActive.save();
	        String test = tobeActive.verifyRule();
			if (test.contains("Attention Required") ){
				if(!tobeActive.activeAlarm){
					new ObservedAlarm(tobeActive.ruleName, System.currentTimeMillis(), tobeActive.sensorID,test).save();
					tobeActive.activeAlarm = true;
					tobeActive.save();
					Logger.info("Rule violated: new alarm created!");
				}
			}
	        
	        index();
	 }
	 
	 public static void setNotActive(String name){
	        Rule tobeNotActive = Rule.findByName(name);
	        //Logger.info("Set " + name + " not active");
	        tobeNotActive.setActive(false);
	        tobeNotActive.save();
	        index();
	 }
}
