/*******************************************************************************
 *  Copyright (c)2010 REMAIN B.V. The Netherlands. (http://www.remainsoftware.com).
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Ahmed Aadel - initial API and implementation     
 *******************************************************************************/

package org.eclipse.ecf.provider.zookeeper.util;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.provider.zookeeper.node.internal.INode;

/**
 * @author Ahmed Aadel
 * @since 0.1
 */
public class Geo {

	/**
	 * @param path
	 *            to be checked whether it's comes form local ZooKeeper server.
	 *            As local nodes need not to be discovered.
	 * @return <code>true</code> if local, <code>false</code> otherwise.
	 */
	public static boolean isLocal(String path) {
		Assert.isNotNull(path);
		Assert.isTrue(path.length() > INode.ROOT.length());
		String[] parts = path.split(INode._URI_);
		String host = parts[INode.URI_POSITION];
		return Geo.getHost().equals(host);
	}

	public static URI getLocation() {
		try {
			return URI.create(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHost() {
		String host;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			host = "localhost"; //$NON-NLS-1$
		}
		return host;
	}

	public static String getNodeHost() {
		String host;
		try {
			host = InetAddress.getLocalHost().toString();
			host = host.replace("/", "-");//$NON-NLS-1$ //$NON-NLS-2$
		} catch (UnknownHostException e) {
			host = "localhost"; //$NON-NLS-1$
		}

		return host;
	}

}