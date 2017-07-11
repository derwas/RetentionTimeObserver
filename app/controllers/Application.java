package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        Long noOfRules = Rule.count();
        Long noOfAlarms = ObservedAlarm.count();
        //Long noOfSensors = Sensor.count();

        render(noOfRules, noOfAlarms);
    }

    
}