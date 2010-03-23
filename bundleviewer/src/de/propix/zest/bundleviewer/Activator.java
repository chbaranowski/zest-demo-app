package de.propix.zest.bundleviewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.propix.zest.bundleviewer.model.BundleModel;
import de.propix.zest.bundleviewer.model.Node;
import de.propix.zest.bundleviewer.model.ServiceModel;

public class Activator implements BundleActivator
{

  BundleContext context;
  
  GraphViewer viewer;
  
  Shell shell;
  /*
   * (non-Javadoc)
   * 
   * @seeorg.osgi.framework.BundleActivator#start(org.osgi.framework.
   * BundleContext)
   */
  public void start(BundleContext context) throws Exception
  {
    this.context = context;
    List<Node> nodes = getNodes();

    final Display d = new Display();
    Shell shell = new Shell(d);
    shell.setText("Services and Bundles");
    shell.setLayout(new FillLayout(SWT.VERTICAL));
    shell.setSize(800, 800);
    
    Button reload = new Button(shell, SWT.PUSH);
    reload.setText("Reload");
    reload.addSelectionListener(new SelectionListener()
    {
      
      public void widgetSelected(SelectionEvent e)
      {
        List<Node> newNodes = getNodes();
        Shell shell = new Shell(d);
        shell.setText("Services and Bundles");
        shell.setLayout(new FillLayout(SWT.VERTICAL));
        shell.setSize(800, 800);
        viewer = new GraphViewer(shell, SWT.NONE);
        viewer.setContentProvider(new ContentProvider());
        viewer.setLabelProvider(new LabelProvider());
        viewer.setInput(newNodes);
        viewer.setLayoutAlgorithm(new VerticalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

        shell.open();
        while (!shell.isDisposed())
        {
          while (!d.readAndDispatch())
          {
            d.sleep();
          }
        }

      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
      }
      
    });
    
    viewer = new GraphViewer(shell, SWT.NONE);
    viewer.setContentProvider(new ContentProvider());
    viewer.setLabelProvider(new LabelProvider());
    viewer.setInput(nodes);
    viewer.setLayoutAlgorithm(new VerticalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

    shell.open();
    while (!shell.isDisposed())
    {
      while (!d.readAndDispatch())
      {
        d.sleep();
      }
    }
  }

  private List<Node> getNodes()
  {
    List<Node> nodes = new ArrayList<Node>();

    Bundle[] bundles = context.getBundles();
    for (Bundle bundle : bundles)
    {
      if (bundle.getSymbolicName().startsWith("de"))
      {
        BundleModel bundleModel = new BundleModel();
        bundleModel.setName(bundle.getSymbolicName());
        nodes.add(bundleModel);
        ServiceReference[] registeredServices = bundle.getRegisteredServices();
        if (registeredServices != null)
        {
          for (ServiceReference serviceReference : registeredServices)
          {
            String[] property = (String[]) serviceReference.getProperty("objectClass");
            ServiceModel serviceModel = findService(nodes, property[0]);
            serviceModel.setBundle(bundleModel);
            serviceModel.setInterfaceName(property[0].toString());
            bundleModel.addService(serviceModel);
            nodes.add(serviceModel);
          }
        }
        
        ServiceReference[] servicesInUse = bundle.getServicesInUse();
        if(servicesInUse != null)
        {
        	for (ServiceReference serviceReference : servicesInUse) {
        		String[] property = (String[]) serviceReference.getProperty("objectClass");
				ServiceModel serviceModel = findService(nodes, property[0]);
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
			if(service.getInterfaceName().endsWith(name))
			{
				return service;
			}
		}
	  }
	  ServiceModel serviceModel = new ServiceModel();
	  serviceModel.setInterfaceName(name);
	  return serviceModel;
}

/*
   * (non-Javadoc)
   * 
   * @seeorg.osgi.framework.BundleActivator#stop(org.osgi.framework.
   * BundleContext)
   */
  public void stop(BundleContext context) throws Exception
  {
    context = null;
  }

}
