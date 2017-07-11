/**
 * @copyright 	National University of Ireland, Galway
 * @author      Wassim Derguech <wassim.derguech @ gmail.com>
 * @version     1.0
 * @since       2015-10-13          
 */  
package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Entity;

import org.json.JSONObject;

import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.WS.HttpResponse;

@Entity
public class ObservedAlarm  extends Model{
	
	public String instanceOf;
	
	@Required
	public long occurredAt;
	public Date occurredAtDate;
	public long expiresOn;
	public Date expiresOnDate;
	
	public String occurredIn;
	
	public String sensorID;
	
	public String resolvedBy;
	public Long resolvedAt;
	public Date resolvedAtDate;
	public String Resolution;
	public boolean Resolved = false;
	public boolean Expired = false;
	public String responseMsg;
	public String warningMessage = "";
	
	public ObservedAlarm (String instanceOf, long occurredAt, String sensorID, String warningMsg){
		super();
		this.instanceOf = instanceOf;
		this.occurredAt = occurredAt;
		this.occurredAtDate=new Date(occurredAt);
		this.sensorID = sensorID;
		//Sensor sensor = controllers.Sensors.getSensorByID(this.sensorID);
		//this.occurredIn = sensor.location;
		this.Resolved=false;
		Rule occ = Rule.findByName(instanceOf);
		this.expiresOn = occurredAt + occ.expiresAfter;
		this.expiresOnDate = new Date (this.expiresOn);
		String action = "Resolve this now";
		String label = occ.Action; 
		String level = "Medium";
		this.warningMessage=warningMsg;
		//Logger.info("Alert detected in: "+occurredIn);
		this.Resolution= "Task_"+UUID.randomUUID();
		JSONObject notification = createNotification(sensorID, occ.ActionTitle, label, action, level);
		Logger.info("Notification created!");

			sendNotification(notification);
			String emailtext = "\n"
					+"An alert has been relating to an issue with the retention of drinking water in the NUIG Engineering Building System.\n\n"
					+ this.warningMessage +"\n"
					+"Suggested preventative action:"
					+"\t"+label+"\n\n"
					+"Please note that this alert will remain active until "+this.expiresOnDate+" unless it has been resolved before that date.\n\n"
					+"Regards,\nThe Waternomics Team\n\n"
					+"--\nThis message was sent from the Waternomics.eu platform.";
			sendEmailNotification("[Waternomics] " + occ.ActionTitle, emailtext);


	} 
	
	public void resolve(long time, String by){
		this.resolvedAt = time;
		this.resolvedBy = by;
		this.resolvedAtDate = new Date(time);
		this.Resolved = true;
	}
	
	public void sendNotification(JSONObject notification){
		String type = "application/x-www-form-urlencoded";
		URL u;
		HttpURLConnection conn = null;
		String msg = "notification="+notification.toString();
		Logger.info("Msg to send: "+ msg);

		try {
			u = new URL(Play.configuration.getProperty("NotificationService"));
			conn = (HttpURLConnection) u.openConnection();

		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty( "Content-Type", type );
		conn.setRequestProperty( "Content-Length", String.valueOf(msg.length()));
		OutputStream os = conn.getOutputStream();
		os.write(msg.getBytes());
		os.flush();
		os.close();
		
		//Get Response	
	      InputStream is = conn.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      Logger.info(response.toString());

	    } catch (Exception e) {

	      e.printStackTrace();

	    } finally {

	      if(conn != null) {
	        conn.disconnect(); 
	      }
	    }
	      
	}
	

		public void sendEmailNotification(String subject, String text) {

			final String username = Play.configuration.getProperty("email");
			final String password = Play.configuration.getProperty("passwd");

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", Play.configuration.getProperty("smtp.host"));
			props.put("mail.smtp.port", Play.configuration.getProperty("smtp.port"));

			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  });

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				
				String to = Play.configuration.getProperty("emailTo");
			    StringTokenizer st = new StringTokenizer(to,";");
		    	//Logger.info("======================>"+st.toString());

			    while(st.hasMoreElements()) {
			    	String s = (String) st.nextElement();
			    	Logger.info("======================>"+s);
			    	message.addRecipients(Message.RecipientType.TO,
							InternetAddress.parse(s));
			    	}
			    
				
				message.setSubject(subject);
				message.setText(text);

				Transport.send(message);

				Logger.info("Notification sent by Email");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
	

	public void setExpired() {
		this.Expired = true;	
	}
	public static  JSONObject createNotification(String sensorID, 
			String title,
			  String label, 
			  String action,
			  String level ){
		JSONObject result = new JSONObject();
		
		result.put("applicationId", Play.configuration.getProperty("ApplicationID"));
		result.put( "userId", Play.configuration.getProperty("UserID"));
		result.put( "sensorId",sensorID);
		result.put( "title", title);
		result.put("description", label);
		//result.put("imageURL", "");
		result.put("criticality",level);
		result.put("actionText",action);
		result.put("snoozeText", "Remind me in 2 hours");
		result.put("noActionText", "Already resolved");
		
		return result;
	}
	
}
