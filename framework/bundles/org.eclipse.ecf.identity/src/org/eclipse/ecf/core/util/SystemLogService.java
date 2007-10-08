/****************************************************************************
 * Copyright (c) 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 *
 */
public class SystemLogService implements LogService {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("Z yyyy.MM.dd HH:mm:ss:S");

	private final String pluginName;

	public SystemLogService(String pluginName) {
		this.pluginName = pluginName;
	}

	private static final String getLogCode(int level) {
		switch (level) {
			case LogService.LOG_INFO :
				return "INFO";
			case LogService.LOG_ERROR :
				return "ERROR";
			case LogService.LOG_DEBUG :
				return "DEBUG";
			case LogService.LOG_WARNING :
				return "WARNING";
			default :
				return "UNKNOWN";
		}
	}

	private final void doLog(ServiceReference sr, int level, String message, Throwable t) {
		final StringBuffer buf = new StringBuffer("[log;");
		buf.append(dateFormat.format(new Date())).append(";");
		buf.append(getLogCode(level)).append(";");
		if (sr != null)
			buf.append(sr.getBundle().getSymbolicName()).append(";");
		else
			buf.append(pluginName).append(";");
		buf.append(message).append("]");
		if (t != null) {
			System.err.println(message);
			t.printStackTrace(System.err);
		} else
			System.out.println(message);
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.log.LogService#log(int, java.lang.String)
	 */
	public void log(int level, String message) {
		log(null, level, message, null);
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.log.LogService#log(int, java.lang.String, java.lang.Throwable)
	 */
	public void log(int level, String message, Throwable exception) {
		doLog(null, level, message, exception);
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.log.LogService#log(org.osgi.framework.ServiceReference, int, java.lang.String)
	 */
	public void log(ServiceReference sr, int level, String message) {
		log(sr, level, message, null);
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.log.LogService#log(org.osgi.framework.ServiceReference, int, java.lang.String, java.lang.Throwable)
	 */
	public void log(ServiceReference sr, int level, String message, Throwable exception) {
		doLog(sr, level, message, exception);
	}

}
