package de.propix.zest.bundleviewer;

import java.util.ArrayList;
import java.util.List;

import de.propix.zest.bundleviewer.model.BundleModel;
import de.propix.zest.bundleviewer.model.Node;
import de.propix.zest.bundleviewer.model.ServiceModel;

public class Content {
	
	private final List<Node> nodes = new ArrayList<Node>();
	
	public Content() {
		
		BundleModel coreBundle = new BundleModel();
		coreBundle.setName("de.propix.runtimetester.core");
		
		ServiceModel testerService = new ServiceModel();
		testerService.setInterfaceName("de.propix.runtimetester.core.runner.Tester");
		testerService.setBundle(coreBundle);
		
		coreBundle.addService(testerService);
		
		getNodes().add(coreBundle);
		getNodes().add(testerService);
		
		BundleModel webBundle = new BundleModel();
		webBundle.setName("de.propix.runtimetester.web");
		webBundle.useService(testerService);
		getNodes().add(webBundle);
		
		
		ServiceModel integratedTestService = new ServiceModel();
		integratedTestService.setInterfaceName("de.propix.runtimetester.core.IntegratedTestService");
		getNodes().add(integratedTestService);
		
		BundleModel userBundle = new BundleModel();
		userBundle.setName("de.propix.runtimetester.example.component.user");
		getNodes().add(userBundle);
		
		integratedTestService.setBundle(userBundle);
		
		userBundle.addService(integratedTestService);
		
		coreBundle.useService(integratedTestService);
		
	}

	public List<Node> getNodes() {
		return nodes;
	}
	
}
