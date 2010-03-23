package de.propix.zest.bundleviewer;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import de.propix.zest.bundleviewer.model.BundleModel;
import de.propix.zest.bundleviewer.model.ServiceModel;

public class ContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

	public Object[] getConnectedTo(Object entity) {
		if (entity instanceof BundleModel) {
			ArrayList<Object> connections = new ArrayList<Object>();
			BundleModel bundle = (BundleModel) entity;
			connections.addAll(bundle.getUseServices());
			connections.addAll(bundle.getProvidedServices());
			return connections.toArray();
		}
		if (entity instanceof ServiceModel) {
			ServiceModel service = (ServiceModel) entity;
			return new Object[]{};
		}
		throw new RuntimeException("Type not supported");
	}

}
