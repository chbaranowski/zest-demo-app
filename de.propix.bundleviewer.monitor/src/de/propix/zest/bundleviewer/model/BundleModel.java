package de.propix.zest.bundleviewer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BundleModel implements Node, Serializable {
	
	private String name;

	private List<ServiceModel> providedServices = new ArrayList<ServiceModel>();
	
	private List<ServiceModel> useServices = new ArrayList<ServiceModel>();
	
	public void useService(ServiceModel service){
		this.getUseServices().add(service);
	}
		
	public boolean addService(ServiceModel e) {
		return getProvidedServices().add(e);
	}

	public boolean removeService(ServiceModel o) {
		return getProvidedServices().remove(o);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<ServiceModel> getProvidedServices() {
		return providedServices;
	}

	public List<ServiceModel> getUseServices() {
		return useServices;
	}
	
}
