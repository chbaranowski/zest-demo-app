package de.propix.zest.bundleviewer.model;

import java.io.Serializable;

public class ServiceModel implements Node, Serializable {

	private String interfaceName;

	private BundleModel bundle;
	
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setBundle(BundleModel bundle) {
		this.bundle = bundle;
	}

	public BundleModel getBundle() {
		return bundle;
	}
	
}
