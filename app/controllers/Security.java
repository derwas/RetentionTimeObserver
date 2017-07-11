package controllers;

public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
    	if (username.equals("admin") && password.equals("waternomics")) {
        return true;
    	}
    	else 
    	{
    		return false;
    	}
    }

    public static void logout() throws Throwable {
        session.clear();
        response.removeCookie("rememberme");
        flash.success("secure.logout");
        Application.index();
    }
    
   
}