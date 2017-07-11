package models;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;

import org.json.JSONObject;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class Sensor extends Model{
	@Required
	public String sensorID;
	public double reading;
	public long lastTime;
	public String location="Waternomics";
	public Date lastTimeAsDate;

	

	public Sensor(String id, double reading, long lastTime) {

			this.sensorID = id;
			this.reading = reading;
			this.lastTime = lastTime;
			this.lastTimeAsDate = new Date(lastTime);
			//this.location = location;
	
	}


	public static Sensor getSensorByID(String sensorID){
		Iterator it = Sensor.findAll().iterator();
		Sensor sr;
		while (it.hasNext()){
			sr = (Sensor) it.next();
			if (sr.sensorID.equals(sensorID)){
				return sr;
			}
		}
		return null;
	}

	public static Sensor findByID(String sensorID) {
		 List<Sensor> allSensors = Sensor.findAll();
	
		 Iterator it = allSensors.iterator();
		 Sensor oneSensor ;
		 while (it.hasNext()){
			 oneSensor = (Sensor) it.next();
			 
			 if (oneSensor.sensorID.equals(sensorID)){
				 return oneSensor;
			 }
			 
		 }
		return null;
	}
	public void updateSensor(double reading, long time){
		Logger.info("Updating Sensor Reading: "+ this.sensorID);
		this.lastTime = time;
		this.lastTimeAsDate = new Date (time);
		this.reading = reading;
		this.save();
		
	}
	
	
}
