package com.example.nannycam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
   private EditText et_input;
   private static final int SERVER_PORT = 8080;
   App appState;
   Thread cThread;  	
   InetAddress serverAddr = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
    	appState = ((App)getApplication());
    	appState.setContext(getApplicationContext()); 
    	cThread = new Thread(new ClientThread());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void connect(View v) {
        try { 
        	serverAddr = InetAddress.getByName(((EditText)findViewById(R.id.ipInput)).toString());
            Log.d("ClientActivity", et_input.getText().toString());
            Log.d("ClientActivity", "C: Connecting...");
    	} catch (Exception e) {
        	/*new AlertDialog.Builder(MainActivity.this).setTitle("IP Address")
        	.setMessage("Invalid IP address!")
        	.setCancelable(false)
        	.show();*/
    		Log.e("ClientActivity", "C: Error", e);
    		serverAddr = null;
    	}
        
        if(serverAddr != null){
        
	        cThread.run();
	        Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
	        while(true){
	        	if(appState.getResponse().trim().compareTo("ok") == 0){
	                Log.d("ClientActivity", "C: Got ack. Waiting for login.");
	        		startActivity(loginScreen);
	        		break;
	        	}
	        }
        }
    }


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	};

	public class ClientThread implements Runnable {
        public void run() {
	            try{
	            	appState.setSocket(new Socket(serverAddr, SERVER_PORT));
	            }
	            catch(Exception e){
	            	new AlertDialog.Builder(MainActivity.this).setTitle("IP Address")
	            	.setMessage("Invalid IP address!")
	            	.setCancelable(false)
	            	.show();
	            }
            
            while (appState.getSocket().isConnected() && !appState.getSocket().isClosed()) {
                try {
                    Log.d("ClientActivity", "C: Waiting for ack.");
                    BufferedReader br = new BufferedReader(new InputStreamReader(appState.getSocket().getInputStream()));
                    if (br.readLine() != null)
                    	{
                    	appState.setResponse(br.readLine());
                    	Log.d("ClientActivity", appState.getResponse());
                    	}
                } catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
            }
            Log.d("ClientActivity", "C: Closed.");
        }
	}
}