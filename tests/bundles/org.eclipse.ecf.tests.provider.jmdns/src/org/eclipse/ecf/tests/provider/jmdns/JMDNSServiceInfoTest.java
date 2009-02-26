/*******************************************************************************
 * Copyright (c) 2009 Markus Alexander Kuppe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev_eclipse.org <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.tests.provider.jmdns;

import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.provider.jmdns.identity.JMDNSNamespace;
import org.eclipse.ecf.tests.discovery.DiscoveryTestHelper;
import org.eclipse.ecf.tests.discovery.ServiceInfoTest;

public class JMDNSServiceInfoTest extends ServiceInfoTest {

	public JMDNSServiceInfoTest() {
		super();
		uri = DiscoveryTestHelper.createDefaultURI();
		priority = DiscoveryTestHelper.PRIORITY;
		weight = DiscoveryTestHelper.WEIGHT;
		serviceProperties = new ServiceProperties();
		serviceProperties.setProperty("foobar", new String("foobar"));
		Namespace namespace = IDFactory.getDefault().getNamespaceByName(
				JMDNSNamespace.NAME);
		try {
			serviceID = (IServiceID) IDFactory.getDefault().createID(namespace,
					new Object[] {DiscoveryTestHelper.SERVICE_TYPE, DiscoveryTestHelper.getHost()});
		} catch (IDCreateException e) {
			fail(e.getMessage());
		}

		serviceInfo = new ServiceInfo(uri, serviceID, priority, weight,
				serviceProperties);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.discovery.ServiceInfoTest#getServiceInfo(org.eclipse.ecf.discovery.IServiceInfo)
	 */
	protected IServiceInfo getServiceInfo(IServiceInfo aServiceInfo) {
		return aServiceInfo;
	}

}