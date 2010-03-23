package de.propix.zest.bundleviewer;

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
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import de.propix.zest.bundleviewer.model.Node;

public class Viewer {

	static GraphViewer viewer = null;

	static Shell shell;

	public static void run() {
		final Display d = new Display();
		shell = new Shell(d);
		shell.setText("Services and Bundles");

		Button reload = new Button(shell, SWT.PUSH);
		reload.setText("Reload");
		reload.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				List<Node> newNodes = MonitorClient.getNodesForPackage("de");
				Shell shell = new Shell(d);
				shell.setText("Services and Bundles");
				shell.setLayout(new FillLayout(SWT.VERTICAL));
				shell.setSize(1200, 1200);
				viewer = new GraphViewer(shell, SWT.NONE);
				viewer.setContentProvider(new ContentProvider());
				viewer.setLabelProvider(new LabelProvider());
				viewer.setInput(newNodes);
				viewer.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

				shell.open();
				while (!shell.isDisposed()) {
					while (!d.readAndDispatch()) {
						d.sleep();
					}
				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}

	}

	public static void main(String[] args) {
		run();
	}

}
