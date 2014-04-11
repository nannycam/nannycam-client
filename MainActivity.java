package com.example.nannycam_take2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
   private TextView tv_input;
   private Socket socket;
   private static final int REDIRECTED_SERVERPORT = 8080;
	
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
		tv_input = (TextView) findViewById(R.id.ipInput);
		try {
		     InetAddress serverAddr = InetAddress.getByName((String) tv_input.getText());
		     socket = new Socket(serverAddr, REDIRECTED_SERVERPORT);
		  } catch (UnknownHostException e1) {
		     e1.printStackTrace();
		  } catch (IOException e1) {
		     e1.printStackTrace();
		  }
			
			
		try {
		      TextView tv_cam = (TextView) findViewById(R.id.camText);
		      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      tv_cam.setText(br.readLine());
		} catch (UnknownHostException e) {
		   tv_input.setText("Error1");
		   e.printStackTrace();
		} catch (IOException e) {
		   tv_input.setText("Error2");
		   e.printStackTrace();
		} catch (Exception e) {
		   tv_input.setText("Error3");
		   e.printStackTrace();
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
	}

}
