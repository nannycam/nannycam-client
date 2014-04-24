package com.example.nannycam;

import java.net.Socket;

import android.app.Application;

public class App extends Application {
	
	public Socket socket;
	
	public Socket getSocket(){
		return socket;
	}
}
