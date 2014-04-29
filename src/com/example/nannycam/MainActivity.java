package com.example.nannycam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
   private EditText et_input;
   private boolean connected = false;
   private static final int SERVER_PORT = 8080;
   App appState = ((App)this.getApplication());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
		et_input = (EditText) findViewById(R.id.ipInput);
		Log.d("et_input", et_input.getText().toString());
		Context context = getApplicationContext();
		CharSequence text = "Connected!  See log";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
        Thread cThread = new Thread(new ClientThread());
        cThread.start();
        Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
        while(true){
        	if(appState.getResponse().trim().compareTo("ok") == 0){
                Log.d("ClientActivity", "C: Got ack. Waiting for login.");
        		startActivity(loginScreen);
        		break;
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
		Socket socket = appState.getSocket();
        public void run() {  	
            try {
                InetAddress serverAddr = InetAddress.getByName(et_input.getText().toString());
                Log.d("ClientActivity", et_input.getText().toString());
                Log.d("ClientActivity", "C: Connecting...");
                try{
                	socket = new Socket(serverAddr, SERVER_PORT);
                    connected = true;
                }
                catch(IOException e){
                	Toast.makeText(getApplicationContext(), "Not a valid server",Toast.LENGTH_SHORT).show();
                	connected = false;
                }
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Waiting for ack.");
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        if (br.readLine() != null)
                        	{
                        	appState.setResponse(br.readLine());
                        	Log.d("ClientActivity", appState.getResponse());
                        	}
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
	}
}