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

    private Endpoint endpoint;
    private String entityKind;
    private OutputStream outputStream = null;
    private InputStream inStream = null;

    public DSOperationBuilder(Endpoint endpoint, String entityKind) {
        this.endpoint = endpoint;
        this.entityKind = entityKind;
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

        return new DSDownloadOperationImpl(endpoint, entityKind, outputStream );
	}

	public DSOperation buildUpload() {
        if (inStream == null) {
            throw new IllegalArgumentException("No input stream supplied");
        }

        return new DSUploadOperationImpl(endpoint, inStream);
    }

    public DSOperation buildDelete() {
        return new DSDeleteOperationImpl(endpoint, entityKind);
    }
}
