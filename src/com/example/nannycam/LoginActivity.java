package com.example.nannycam;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
	App appState = ((App)this.getApplication());
	Socket socket = appState.getSocket();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
	}
	
    
    public void login(View v) {
    	String user = ((EditText)findViewById(R.id.uname)).toString();
    	String pass = ((EditText)findViewById(R.id.password)).toString();
    	
    	PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream());
	    	out.print("USER " + user);

	        while(true){
	        	if(appState.getResponse().trim().compareTo("ok") == 0){
	        		break;
	        	}
	        }
	        
	        out.print("PASS " + md5(pass));
	        
	        while(true){
	        	if(appState.getResponse().trim().compareTo("ok") == 0){
	        		//TODO: add transition to image screen
	        		break;
	        	}
	        }
		} catch (IOException e) {
        	Toast.makeText(getApplicationContext(), "Something went wrong",Toast.LENGTH_SHORT).show();
		}
    	
    }
    
    public static String md5(String input) {
        
        String md5 = null;
         
        if(null == input) return null;
         
        try {
             
        //Create MessageDigest object for MD5
        MessageDigest digest = MessageDigest.getInstance("MD5");
         
        //Update input string in message digest
        digest.update(input.getBytes(), 0, input.length());
 
        //Converts message digest value in base 16 (hex) 
        md5 = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (NoSuchAlgorithmException e) {
 
            e.printStackTrace();
        }
        return md5;
    }

}
