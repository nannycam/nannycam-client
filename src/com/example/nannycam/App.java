package com.example.nannycam;

import java.net.Socket;

import android.app.Application;
import android.content.Context;

public class App extends Application {
	
	private Socket socket = new Socket();
	private String response = "NULL";
	private Context context;
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket s) {
		socket = s;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	

}
