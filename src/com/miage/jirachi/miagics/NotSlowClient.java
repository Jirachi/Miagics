package com.miage.jirachi.miagics;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.KryoNetException;

public class NotSlowClient extends Client {
	public NotSlowClient(int a, int b) {
		super(a,b);
	}
	
	public void run () {
		while (true) {
			try {
				update(2);
			} catch (IOException ex) {
				close();
			} catch (KryoNetException ex) {
				close();
				throw ex;
			}
		}

	}
}
