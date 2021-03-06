/*******************************************************************************
 * Copyright (c) 2013 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package com.mycorp.examples.timeservice.internal.provider.rest.consumer;

import java.io.NotSerializableException;
import java.util.Map;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.ecf.remoteservice.client.IRemoteCallable;
import org.eclipse.ecf.remoteservice.client.IRemoteResponseDeserializer;
import org.eclipse.ecf.remoteservice.rest.RestCallableFactory;
import org.eclipse.ecf.remoteservice.rest.client.HttpGetRequestType;
import org.eclipse.ecf.remoteservice.rest.client.RestClientContainer;
import org.eclipse.ecf.remoteservice.rest.identity.RestID;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycorp.examples.timeservice.ITimeService;
import com.mycorp.examples.timeservice.provider.rest.common.TimeServiceRestNamespace;

public class TimeServiceRestClientContainer extends RestClientContainer {

	public static final String TIMESERVICE_CONSUMER_CONFIG_NAME = "com.mycorp.examples.timeservice.rest.consumer";

	private IRemoteServiceRegistration reg;

	TimeServiceRestClientContainer() {
		// Create a random ID for the client container
		super((RestID) IDFactory.getDefault().createID(
				TimeServiceRestNamespace.NAME, "uuid:"
						+ java.util.UUID.randomUUID().toString()));
		// This sets up the JSON deserialization of the server's response.
		// See below for implementation of TimeServiceRestResponseDeserializer
		setResponseDeserializer(new TimeServiceRestResponseDeserializer());
	}

	@Override
	public void connect(ID targetID, IConnectContext connectContext1)
			throws ContainerConnectException {
		super.connect(targetID, connectContext1);
		// Create the IRemoteCallable to represent
		// access to the ITimeService method.  
		IRemoteCallable callable = RestCallableFactory.createCallable(
				"getCurrentTime", ITimeService.class.getName(), null,
				new HttpGetRequestType(), 30000);
		// Register the callable and associate it with the ITimeService class
		// name
		reg = registerCallables(new String[] { ITimeService.class.getName() },
				new IRemoteCallable[][] { { callable } }, null);
	}

	@Override
	public void disconnect() {
		super.disconnect();
		if (reg != null) {
			reg.unregister();
			reg = null;
		}
	}

	class TimeServiceRestResponseDeserializer implements
			IRemoteResponseDeserializer {
		public Object deserializeResponse(String endpoint, IRemoteCall call,
				IRemoteCallable callable,
				@SuppressWarnings("rawtypes") Map responseHeaders,
				byte[] responseBody) throws NotSerializableException {
			// We simply need to read the response body (json String),
			// And return the value of the "time" field
			try {
				return new JSONObject(new String(responseBody)).get("time");
			} catch (JSONException e1) {
				throw new NotSerializableException(
						TimeServiceRestResponseDeserializer.class.getName());
			}
		}

	}

	@Override
	public Namespace getConnectNamespace() {
		return IDFactory.getDefault().getNamespaceByName(
				TimeServiceRestNamespace.NAME);
	}
}
