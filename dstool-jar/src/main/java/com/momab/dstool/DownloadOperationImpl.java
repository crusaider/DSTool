package com.momab.dstool;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * @author Jonas Andreasson
 *
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 *
 */
class DownloadOperationImpl extends AbstractOperationImpl implements
        Operation {

    private final OutputStream out;
    private final String entityKind;

    DownloadOperationImpl(Endpoint endpoint, String entityKind,
            OutputStream outputStream) {
        super(endpoint);
        this.entityKind = entityKind;
        this.out = outputStream;
    }

    @Override
    public boolean Run(ProgressCallback callback) throws IOException {

        CallbackWrapper wrappedCallback = new CallbackWrapper(callback);

        wrappedCallback.onInstall();

        super.Install();

        wrappedCallback.onBegin();

        // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Use class Query to assemble a query
        Query q = new Query(this.entityKind);

        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = datastore.prepare(q);

        ObjectOutputStream objectStream = new ObjectOutputStream(this.out);

        int entityCount = 0;

        for (Entity e : pq.asIterable()) {
            objectStream.writeObject(e);
            entityCount++;
            wrappedCallback.onEntityCompleted();
        }

        super.UnInstall();

        wrappedCallback.onDone(entityCount);

        return true;
    }
}
