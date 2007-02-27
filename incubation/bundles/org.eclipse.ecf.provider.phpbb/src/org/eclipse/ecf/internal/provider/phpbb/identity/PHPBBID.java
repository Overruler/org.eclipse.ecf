/*******************************************************************************
 * Copyright (c) 2005, 2006 Erkki Lindpere and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Erkki Lindpere - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.internal.provider.phpbb.identity;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.ecf.core.identity.BaseID;

public class PHPBBID extends BaseID {

	private static final long serialVersionUID = 7679927919437059784L;

	protected URI uri;

	public PHPBBID(PHPBBNamespace namespace, URI uri) throws URISyntaxException {
		super(namespace);
		this.uri = uri;
	}

	@Override
	protected int namespaceCompareTo(BaseID o) {
		if (!(o instanceof PHPBBID)) {
			throw new ClassCastException("Uncomparable types.");
		}
		return uri.compareTo(((PHPBBID) o).uri);
	}

	@Override
	protected boolean namespaceEquals(BaseID o) {
		if (!(o instanceof PHPBBID)) {
			return false;
		}
		return uri.equals(((PHPBBID) o).uri);
	}

	@Override
	protected String namespaceGetName() {
		return uri.toString();
	}

	@Override
	protected int namespaceHashCode() {
		return (int) uri.hashCode();
	}

	@Override
	protected String namespaceToExternalForm() {
		return uri.toString();
	}

}
