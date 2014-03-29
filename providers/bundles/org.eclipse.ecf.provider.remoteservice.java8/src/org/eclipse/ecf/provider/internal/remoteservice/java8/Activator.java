package org.eclipse.ecf.provider.internal.remoteservice.java8;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.util.AdapterManagerTracker;
import org.eclipse.ecf.core.util.ExtensionRegistryRunnable;
import org.eclipse.ecf.provider.remoteservice.generic.RemoteServiceContainerAdapterFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private List<IAdapterFactory> rscAdapterFactories;

	private static IAdapterManager getAdapterManager(BundleContext ctx) {
		AdapterManagerTracker t = new AdapterManagerTracker(ctx);
		t.open();
		IAdapterManager am = t.getAdapterManager();
		t.close();
		return am;
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		SafeRunner.run(new ExtensionRegistryRunnable(context) {
			protected void runWithoutRegistry() throws Exception {
				context.registerService(ContainerTypeDescription.class, new ContainerTypeDescription(J8GenericContainerInstantiator.JAVA8_SERVER_NAME, new J8GenericContainerInstantiator(), "ECF Generic Server", true, false), null); //$NON-NLS-1$
				context.registerService(ContainerTypeDescription.class, new ContainerTypeDescription(J8GenericContainerInstantiator.JAVA8_CLIENT_NAME, new J8GenericContainerInstantiator(), "ECF Generic Client", false, true), null); //$NON-NLS-1$
				context.registerService(ContainerTypeDescription.class, new ContainerTypeDescription(J8SSLGenericContainerInstantiator.JAVA8_SSL_CLIENT_NAME, new J8SSLGenericContainerInstantiator(), "ECF SSL Generic Server", true, false), null); //$NON-NLS-1$
				context.registerService(ContainerTypeDescription.class, new ContainerTypeDescription(J8SSLGenericContainerInstantiator.JAVA8_SSL_SERVER_NAME, new J8SSLGenericContainerInstantiator(), "ECF SSL Generic Client", false, true), null); //$NON-NLS-1$
				IAdapterManager am = getAdapterManager(context);
				if (am != null) {
					rscAdapterFactories = new ArrayList<IAdapterFactory>();
					IAdapterFactory af = new J8RemoteServiceContainerAdapterFactory();
					am.registerAdapters(af, J8SSLServerSOContainer.class);
					rscAdapterFactories.add(af);
					af = new RemoteServiceContainerAdapterFactory();
					am.registerAdapters(af, J8TCPServerSOContainer.class);
					rscAdapterFactories.add(af);
					af = new RemoteServiceContainerAdapterFactory();
					am.registerAdapters(af, J8SSLClientSOContainer.class);
					rscAdapterFactories.add(af);
					af = new RemoteServiceContainerAdapterFactory();
					am.registerAdapters(af, J8TCPClientSOContainer.class);
					rscAdapterFactories.add(af);
				}
			}
		});

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (rscAdapterFactories != null) {
			IAdapterManager am = getAdapterManager(context);
			if (am != null) {
				for (IAdapterFactory af : rscAdapterFactories)
					am.unregisterAdapters(af);
			}
		}
	}

}
