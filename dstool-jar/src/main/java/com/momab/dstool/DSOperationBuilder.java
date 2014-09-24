package com.momab.dstool;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jonas Andreasson
 * 
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 *
 */
public class DSOperationBuilder {
	String host;
	String entityKind;
	String username = null;
	String password = null;
	OutputStream outputStream = null;
	InputStream inStream = null;
	int port = 80;

	public DSOperationBuilder(String host, String entityKind) {
		this.host = host;
		this.entityKind = entityKind;
	}

	public DSOperationBuilder asUser(String userName, String password) {
		this.username = userName;
		this.password = password;
		return this;
	}

	public DSOperationBuilder usingPort(int port) {
		this.port = port;
		return this;
	}

	public DSOperationBuilder writeTo(OutputStream stream) {
		this.outputStream = stream;
		return this;
	}

	public DSOperationBuilder readFrom(InputStream stream) {
		this.inStream = stream;
		return this;
	}

	public DSOperation buildDownload() {
		if (outputStream == null) {
			throw new IllegalArgumentException("No output stream supplied");
		}

		return new DSDownloadOperationImpl(host, entityKind, outputStream,
				port, username, password);
	}

	public DSOperation buildUpload() {
		if (inStream == null) {
			throw new IllegalArgumentException("No input stream supplied");
		}

		return new DSUploadOperationImpl(host, inStream, port, username,
				password);
	}

	public DSOperation buildDelete() {
		return new DSDeleteOperationImpl(host, entityKind, port, username,
				password);
	}
}
