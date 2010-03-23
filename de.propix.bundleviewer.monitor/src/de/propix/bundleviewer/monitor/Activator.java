package de.propix.bundleviewer.monitor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	Thread monitorThread; 
	
	MonitorCommand command;
	
	public void start(BundleContext context) throws Exception {
		command = new MonitorCommand(context);
		monitorThread = new Thread(command);
		monitorThread.start();
	}

	public void stop(BundleContext context) throws Exception {
		command.stop();
		monitorThread.interrupt();
		monitorThread = null;
	}

}
