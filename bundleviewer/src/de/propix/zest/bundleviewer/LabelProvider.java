package de.propix.zest.bundleviewer;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

import de.propix.zest.bundleviewer.model.BundleModel;
import de.propix.zest.bundleviewer.model.ServiceModel;


public class LabelProvider extends org.eclipse.jface.viewers.LabelProvider
		implements ISelfStyleProvider {

	Image classImage = new Image(Display.getDefault(), LabelProvider.class
			.getResourceAsStream("class_obj.gif"));

	Image bundleImage = new Image(Display.getDefault(), LabelProvider.class
			.getResourceAsStream("package_obj.gif"));
	
	@Override
	public String getText(Object element) {
		if (element instanceof BundleModel) {
			BundleModel bundle = (BundleModel) element;
			return bundle.getName();
		}
		if (element instanceof ServiceModel) {
			ServiceModel service = (ServiceModel) element;
			return service.getInterfaceName();
		}

		if (element instanceof EntityConnectionData) {
			return "";
		}
		throw new RuntimeException("Wrong type: "
				+ element.getClass().toString());
	}

	public void selfStyleConnection(Object element, GraphConnection connection) {
		if (element instanceof EntityConnectionData) {
			EntityConnectionData connectionData = (EntityConnectionData) element;
			ServiceModel service = (ServiceModel) connectionData.dest;
			BundleModel bundle = (BundleModel) connectionData.source;
			if (service.getBundle() != null && !service.getBundle().equals(bundle)) {
				connection.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
			} else {
				connection.setLineWidth(2);
			}
		}
	}

	public void selfStyleNode(Object element, GraphNode node) {
		if (element instanceof ServiceModel) {
	//		node.setImage(classImage);
		}
		if (element instanceof BundleModel) {
			node.setImage(bundleImage);
		}
	}

}
