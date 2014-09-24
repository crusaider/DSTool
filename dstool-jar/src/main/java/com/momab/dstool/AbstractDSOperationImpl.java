package com.momab.dstool;

import java.io.IOException;

import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

import com.momab.dstool.DSOperation.ProgressCallback;

/**
 * @author Jonas Andreasson
 * 
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 *
 */
class AbstractDSOperationImpl {

	private final String host;
	private final int port;
	private final String username;
	private final String password;

	private RemoteApiInstaller installer;

	public AbstractDSOperationImpl(String host, int port, String username,
			String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	protected void Install() throws IOException {
		RemoteApiOptions options = new RemoteApiOptions().server(host, port)
				.credentials(username, password);

		installer = new RemoteApiInstaller();
		installer.install(options);
	}

	protected void UnInstall() {
		installer.uninstall();
	}
	
	protected void sendProgressMessage(ProgressCallback callback, String message ) {
		if ( callback != null ) {
			callback.Tick(message);
		}
	}
}
