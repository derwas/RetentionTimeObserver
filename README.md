# Water Retention Time Observer

The Retention Time Observer is an application developped as part of the European project Waternomics. It aims to observe the time during which water has been residing in a pipe and creates an alarm when the time exceeds a certain time to ask a manager to flash the water and make sure it is fresh and safe to drink. 

The retention time observer has been implemented as part of WP3 tasks. It has been earlier introduced in D3.3. The application operates as follows (see Figure below): 

1 – After a successful login, the building manager defines the retention time rules. Optionally, he can enable/disable any of the existing rules.

2 – 3 – The application continuously queries the Waternomics Dataspace (see D3.1.1 and D3.1.2)  through its Query Service (see D3.2).

4-  If one of  the rules is violated, the application updates its logs and creates a notification with a recommended corrective action. The notification is later sent to the notification component (see D3.2).

5 –A notification is sent to the manager depending on his preferences (email, dashboard, smart watch, etc.)

6- Regularly the building  manager checks the depected alerts and indicate those that have been resolved.

![WRTO](https://gitlab.insight-centre.org/uploads/waternomics/waterretentiontimeobserver/9b0816ac94/WRTO.png)

# Requirements
These are the following requirements:
* Play Framework 1.2.5
* Play requires Java 1.6.
To check that you have the latest JDK, please run:

>$java -version

To check the installed java versions on the machine, please run:

>$ls /usr/lib/jvm/

To change the active java version, please run:

>$export JAVA_HOME=/usr/lib/jvm/[your java version]/

>$export PATH=${JAVA_HOME}/bin:${PATH}


# Start and Stop the application
### Start
The application is play framework app to start it use the following command:

>$cd [location of the app]

>$[Path to Play Installation/play-1.2.5.3]/play run &> logger.log &

Please note that an accompanied start script is available. It can be used instead of this last command:

>$./start.sh

### Stop
The application runs in the background using port 8010.
Stopping the application can be done by killing the process id using that port.

To stop this application use the following command:

>$sudo netstat -tunlp | grep 8010

>$sudo kill -9 [Process ID]

# Screenshots

![WRTO1](https://gitlab.insight-centre.org/uploads/waternomics/waterretentiontimeobserver/4e73554bea/WRTO1.png)


![WRTO2](https://gitlab.insight-centre.org/uploads/waternomics/waterretentiontimeobserver/a0f4363898/WRTO2.png)


![WRTO3](https://gitlab.insight-centre.org/uploads/waternomics/waterretentiontimeobserver/f1e9dd811e/WRTO3.png)
