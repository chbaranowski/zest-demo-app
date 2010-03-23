package com.propix.bundleviewer.monitor;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

import de.propix.bundleviewer.monitor.MonitorRequest;
import de.propix.bundleviewer.monitor.MonitorResponse;

public class MonitorClientTest {

	@Test
	public void testGetResponse() throws Exception {
		MonitorRequest request = new MonitorRequest();
		request.packageName = "de";
		
		Socket socket = new Socket("localhost", 4444);
		OutputStream out = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
		
		InputStream in = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(in);
		
		objectOutputStream.writeObject(request);
	
		MonitorResponse response = (MonitorResponse) objectInputStream.readObject();
		
		objectOutputStream.close();
		objectInputStream.close();
		
		assertEquals(1, response.nodes.size());
		
		socket.close();
	}
	
}
