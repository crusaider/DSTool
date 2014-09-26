package com.momab.dstool;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jonas Andreasson
 *
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 *
 */
public class SimplexOperationBuilder {

    private Endpoint endpoint;
    private String entityKind;
    private OutputStream outputStream = null;
    private InputStream inStream = null;

    public SimplexOperationBuilder(Endpoint endpoint, String entityKind) {
        this.endpoint = endpoint;
        this.entityKind = entityKind;
    }

    public SimplexOperationBuilder writeTo(OutputStream stream) {
        this.outputStream = stream;
        return this;
    }

    public SimplexOperationBuilder readFrom(InputStream stream) {
        this.inStream = stream;
        return this;
    }

    public Operation buildDownload() {
        if (outputStream == null) {
            throw new IllegalArgumentException("No output stream supplied");
        }

        return new DownloadOperationImpl(endpoint, entityKind, outputStream );
	}

	public Operation buildUpload() {
        if (inStream == null) {
            throw new IllegalArgumentException("No input stream supplied");
        }

        return new UploadOperationImpl(endpoint, inStream);
    }

    public Operation buildDelete() {
        return new DeleteOperationImpl(endpoint, entityKind);
    }
}
