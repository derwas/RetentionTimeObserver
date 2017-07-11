/**
 * @copyright 	National University of Ireland, Galway
 * @author      Wassim Derguech <wassim.derguech @ gmail.com>
 * @version     1.0
 * @since       2015-10-13          
 */  
package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import models.ObservedAlarm;
import models.Rule;
import play.*;
import play.mvc.*;
//import utils.MyComparableAlarms;
import play.mvc.With;

public class ObservedAlarms extends Controller {

public static void index() {
	int active =0;
	int resolved = 0;
	int expired = 0 ;
        List<ObservedAlarm> totalAlarms = ObservedAlarm.findAll();
        //Logger.info("Size of alerts ", totalAlarms.size());
        List <ObservedAlarm> allAlarms = new ArrayList<ObservedAlarm>();
        
        Iterator it = totalAlarms.iterator();
        
        while (it.hasNext()){
        	ObservedAlarm one = (ObservedAlarm) it.next();
        	if(!(one.Expired||one.Resolved) ){
        		allAlarms.add(one);
        	}
        	if(one.Expired){
        		expired++;
        	}    
        	if(one.Resolved){
        		resolved ++;
        	}  
        }
        
        //Collections.sort(allAlarms,new MyComparableAlarms());
    	active = totalAlarms.size() - expired - resolved;
    	String analytics = Play.configuration.getProperty("analytics");

        render(allAlarms,active,resolved,expired,analytics);    }
	
public static void expiredAlarms() {
	
	int active =0;
	int resolved = 0;
	int expired = 0 ;
	
    List<ObservedAlarm> theObservedAlarms = ObservedAlarm.findAll();
    List <ObservedAlarm> allAlarms = new ArrayList<ObservedAlarm>();
    
    Iterator it = theObservedAlarms.iterator();
    while (it.hasNext()){
    	ObservedAlarm one = (ObservedAlarm) it.next();
    	if(one.Expired){
    		allAlarms.add(one);
    		expired++;
    	}    
    	if(one.Resolved){
    		resolved ++;
    	}  
    }
    
    //Collections.sort(allAlarms,new MyComparableAlarms());
	active = theObservedAlarms.size() - expired - resolved;

    render(allAlarms,active,resolved,expired);
}
	
public static void resolvedAlarms() {
	int active =0;
	int resolved = 0;
	int expired = 0 ;
	
	
    List<ObservedAlarm> AllObservedAlarms = ObservedAlarm.findAll();
    List <ObservedAlarm> allAlarms = new ArrayList<ObservedAlarm>();
    
    Iterator it = AllObservedAlarms.iterator();
    while (it.hasNext()){
    	ObservedAlarm one = (ObservedAlarm) it.next();
    	if(one.Resolved){
    		allAlarms.add(one);
    		resolved ++;
    	}        	
    	if(one.Expired){
    		expired++;
    	}
    }
	active = AllObservedAlarms.size() - expired - resolved;

    
    //Collections.sort(allAlarms,new MyComparableAlarms());
    render(allAlarms,active,resolved,expired);
}


}
