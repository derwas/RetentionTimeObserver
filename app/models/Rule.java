/**
 * @copyright 	National University of Ireland, Galway
 * @author      Wassim Derguech <wassim.derguech @ gmail.com>
 * @version     1.0
 * @since       2015-10-13          
 */  

package models;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import play.Play;
import play.Logger;

import javax.persistence.Entity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import controllers.Sensors;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;


@Entity
public class Rule extends Model {
	
	@Required
	@Unique
	public String ruleName;  //a name that the administrator needs to give to the observation rule
	public String sensorID; //the sensor that needs to be observed with regard to this rule
	public double minVolume; // the minimum amount of water that needs to flow before sending an alarm
	public long alarmingPeriod; // period after which an alarm is created 
	public String alarminPeriodAsString="";
	//	If Volume (litres)	less than	69.88	Recorded at MWS2 in 48 hour period	Alarm for Control & Subsidary Fountains
	public String Action; // what action to take if the retention rate is longer than the alarmingPeriod (e.g., open the tap)
	public String ActionTitle = "Control and Subsidary Fountains";
	//public String Item; //what item or water outlet needs to be manipulated for the task 
	public boolean activeAlarm = false;// this valiable - true if an alarm has been created our f this rule.
	public boolean active = false; //enable or disable the rule
	public long expiresAfter = 0;// after how much time does this an observed alert expires.
	public String expiresAfterAsString="";
	public String Description="";
	public String location="";
	
	public Rule(String ruleName, String sensorID, double minVolume,long alarmingPeriod,
			String action, 
			long expiresAfter, String location) {
		super();
		this.ruleName = ruleName;
		this.sensorID = sensorID;
		this.minVolume = minVolume;
		this.alarmingPeriod = alarmingPeriod;
		this.alarminPeriodAsString = utils.DaysChecking.durationAsString(alarmingPeriod);
		Action = action;
		this.activeAlarm = false;
		//Item = item;
		this.expiresAfter = expiresAfter;
		this.expiresAfterAsString = utils.DaysChecking.durationAsString(expiresAfter);
		this.location=location;
		this.generateDescription();

		new Sensor(sensorID,0,System.currentTimeMillis()).save();
	}


	public static Rule findByName(String name) {
		 List<Rule> allRules = Rule.findAll();
	
		 Iterator it = allRules.iterator();
		 Rule oneRule ;
		 while (it.hasNext()){
			 oneRule = (Rule) it.next();
			 
			 if (oneRule.ruleName.equals(name)){
				 return oneRule;
			 }
			 
		 }
		return null;
	}



	public boolean isActive() {
		return active;
	}



	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String generateDescription(){
		this.Description = "Create an alarm when the volume (litres) is less than "+this.minVolume
				+", recorded by the sensor "+this.ruleName+" / "+this.sensorID
				+" ("+this.location+") "
				+ " in a period of "+ utils.DaysChecking.durationAsString(this.alarmingPeriod)
				+". This alarm expires after "+utils.DaysChecking.durationAsString(this.expiresAfter)+".";
		this.expiresAfterAsString = utils.DaysChecking.durationAsString(this.expiresAfter);
		this.alarminPeriodAsString = utils.DaysChecking.durationAsString(alarmingPeriod);
		return this.Description;
	}

	
	public String verifyRule() throws Exception{
		//This methods verifies the rule and retunrs true if there is NO retention rate observation problem and retunrs false if the is a probelm!
		String result ="";
		//Logger.info("Rule verification: "+this.ruleName);

		//if(!this.activeAlarm){
		  String interval = utils.DaysChecking.getInterval(this.alarmingPeriod);
		  String json ="";
		  //Logger.info("Interval = "+interval+"\n");	
			  
			  //Logger.info("Datasource: "+dataSRC.get(i));
			  String query = "{"
				  			+"  \"queryType\": \"groupBy\",\n"
				  			+"  \"dataSource\": \""+Play.configuration.getProperty("DataSource")+"\",\n"
							+"  \"intervals\": [ \""+interval+"\" ],\n"
							+"  \"granularity\": \"all\",\n"
							//+"  \"dimensions\" : [\"dDay\", \"dMonth\", \"dYear\"],\n"
							+"  \"filter\": {\"type\": \"selector\",\"dimension\": \"dSensor\", \"value\": \""+this.sensorID+"\"},\n"
							+"  \"aggregations\": [\n"
							+"     {\"type\": \"doubleSum\", \"fieldName\": \"mValue\", \"name\": \"consumption\"}\n"
							+"  ]\n"
/*							+"   \"having\": {\n"
							+"     \"type\": \"lessThan\",\n"
							+"     \"aggregation\": \"consumption\",\n"
							+"     \"value\": "+this.minVolume+"\n"
							+"   }\n"*/
							+"}\n";
		  
		 
		//Logger.info("Qeury = "+query+"\n");
		
			  CloseableHttpClient httpClient = HttpClientBuilder.create().build();

			  String postUrl=Play.configuration.getProperty("DRUID");// put in your url
			  HttpPost post = new HttpPost(postUrl);
			  StringEntity  postingString =new StringEntity(query);//convert your pojo to   json
			  post.setEntity(postingString);
			  post.setHeader("Content-type", "application/json");
			  HttpResponse  response = httpClient.execute(post);
			   json = EntityUtils.toString(response.getEntity());
		Logger.info("Result = "+json);

			List <jsonReading> rds = new ArrayList<jsonReading>();

		  	Gson gson= new Gson();
			rds = gson.fromJson(json, new TypeToken<List<jsonReading>>(){}.getType()) ;

			
			double consumption = 0;
			Iterator it = rds.iterator();
			while (it.hasNext()) {
				jsonReading jr = (jsonReading) it.next();
				consumption = Double.parseDouble(jr.event.consumption);
				
				Sensor s = Sensor.findByID(this.sensorID);
				
				if (s !=null){
					s.updateSensor(consumption, System.currentTimeMillis());
				}else
				{
					new Sensor (this.sensorID,consumption,System.currentTimeMillis()).save();
				}

					
				if (consumption * 1000 < this.minVolume){
					Calendar cl = Calendar.getInstance();
					int mm = ((int)cl.get(Calendar.MONTH))+1;
					result="Attention Required: Sensor "+this.ruleName+" / "+this.sensorID+" ("+this.location+") "+" recorded a consumption of " 
							+ String.format( "%.2f", consumption*1000) +" Litre(s) (less than "+this.minVolume+" Liter(s)) in a period of "
							+utils.DaysChecking.durationHoursAsString(this.alarmingPeriod) +" up to 00:00 am on "
							+cl.get(Calendar.DAY_OF_MONTH)+"/"+mm+"/"+cl.get(Calendar.YEAR)+".";
					
					Logger.info(result);
				}

			}
		//}
		//Logger.info("Result = "+json+"\n");
		return result;
	}
	
	

}
