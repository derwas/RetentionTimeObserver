package controllers;

import java.util.Iterator;
import java.util.List;

import models.Rule;
import models.Sensor;

import org.json.JSONObject;

import play.Logger;
import play.db.jpa.NoTransaction;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Sensors extends Controller{
	
	
	public static void index() {
        List<Sensor> allSensors = Sensor.findAll();
        render(allSensors);
    }
	
	public static Sensor getSensorByID(String ID){
		Iterator it = Sensor.findAll().iterator();
		boolean found=false;
		
		Sensor sr;
		while(it.hasNext()){
			 sr = (Sensor) it.next();
			if (sr.sensorID.equals(ID)){
				found = true;
				return sr;
			}
		}

			return null;
		
	}
	
	public static void updateSensor(String id, double reading, long time){
		
		Logger.info("Sensor Update [id="+id+"]");

		
		Iterator it = Sensor.findAll().iterator();
		boolean found=false;
		
		Sensor sr;
		while(it.hasNext()){
			 sr = (Sensor) it.next();
			if (sr.sensorID.equals(id)){
				found = true;
				if (reading>0){
					sr.lastTime = time;
				}
			}
		}
		if(!found){
			new Sensor (id,reading,time).save();
			Logger.info("New sensor added to the system![id="+id+"]");
		}
	}
}
