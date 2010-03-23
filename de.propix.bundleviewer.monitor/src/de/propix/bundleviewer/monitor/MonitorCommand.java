package de.propix.bundleviewer.monitor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.propix.zest.bundleviewer.model.BundleModel;
import de.propix.zest.bundleviewer.model.Node;
import de.propix.zest.bundleviewer.model.ServiceModel;

public class MonitorCommand implements Runnable {

	final ServerSocket serverSocket;

	boolean listing = true;

	final BundleContext context;

	public MonitorCommand(BundleContext context) throws IOException {
		this.context = context;
		this.serverSocket = new ServerSocket(4444);
	}

	@Override
	public void run() {
		try {
			while (listing) {
				Socket socket = serverSocket.accept();

				InputStream in = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();

				ObjectInputStream objectInputStream = new ObjectInputStream(in);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						outputStream);

				MonitorRequest request = (MonitorRequest) objectInputStream
						.readObject();

				MonitorResponse response = new MonitorResponse();

				List<Node> nodes = getNodesForPackage(request.packageName);
				response.nodes = nodes;

				objectOutputStream.writeObject(response);

				objectInputStream.close();
				objectOutputStream.close();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (!serverSocket.isClosed())
					serverSocket.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private List<Node> getNodesForPackage(String packageName) {
		List<Node> nodes = new ArrayList<Node>();

		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			if (bundle.getSymbolicName().startsWith(packageName)) {
				BundleModel bundleModel = new BundleModel();
				bundleModel.setName(bundle.getSymbolicName());
				nodes.add(bundleModel);
				ServiceReference[] registeredServices = bundle
						.getRegisteredServices();
				if (registeredServices != null) {
					for (ServiceReference serviceReference : registeredServices) {
						String[] property = (String[]) serviceReference
								.getProperty("objectClass");
						ServiceModel serviceModel = findService(nodes,
								property[0]);
						serviceModel.setBundle(bundleModel);
						serviceModel.setInterfaceName(property[0].toString());
						bundleModel.addService(serviceModel);
						nodes.add(serviceModel);
					}
				}

				ServiceReference[] servicesInUse = bundle.getServicesInUse();
				if (servicesInUse != null) {
					for (ServiceReference serviceReference : servicesInUse) {
						String[] property = (String[]) serviceReference
								.getProperty("objectClass");
						ServiceModel serviceModel = findService(nodes,
								property[0]);
						bundleModel.useService(serviceModel);
					}
				}
			}
		}
		return nodes;
	}

	private ServiceModel findService(List<Node> nodes, String name) {
		for (Node node : nodes) {
			if (node instanceof ServiceModel) {
				ServiceModel service = (ServiceModel) node;
				if (service.getInterfaceName().endsWith(name)) {
					return service;
				}
			}
		}
		ServiceModel serviceModel = new ServiceModel();
		serviceModel.setInterfaceName(name);
		return serviceModel;
	}

	public void stop() {
		listing = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
