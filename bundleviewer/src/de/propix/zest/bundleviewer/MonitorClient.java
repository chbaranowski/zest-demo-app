package de.propix.zest.bundleviewer;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import de.propix.bundleviewer.monitor.MonitorRequest;
import de.propix.bundleviewer.monitor.MonitorResponse;
import de.propix.zest.bundleviewer.model.Node;

public class MonitorClient {

	public static List<Node> getNodesForPackage(String packageName) {
		try {
			MonitorRequest request = new MonitorRequest();
			request.packageName = packageName;

			Socket socket = new Socket("localhost", 4444);
			OutputStream out = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);

			InputStream in = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(in);

			objectOutputStream.writeObject(request);

			MonitorResponse response = (MonitorResponse) objectInputStream
					.readObject();
			
			objectOutputStream.close();
			objectInputStream.close();
			
			socket.close();
			
			return response.nodes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
