package com.example.nannycam;

import java.net.Socket;

import android.app.Application;

public class App extends Application {
	
	private Socket socket;
	private String response;
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	

}
