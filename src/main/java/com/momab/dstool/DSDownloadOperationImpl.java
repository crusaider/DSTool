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
class DSDownloadOperationImpl extends AbstractDSOperationImpl implements
		DSOperation {

	private final OutputStream out;
	private final String entityKind;
	
	DSDownloadOperationImpl(String host, String entityKind,
			OutputStream outputStream, int port, String username,
			String password)  {
		super(host, port, username, password);
		this.entityKind = entityKind;
		this.out = outputStream;
	}

	@Override
	public boolean Run(ProgressCallback callback) throws IOException {
		
		sendProgressMessage(callback, "Installing ");
		
		super.Install();
		
		sendProgressMessage(callback, "Preparing query ");

		
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
			sendProgressMessage(callback, ".");
		}
		
		sendProgressMessage(callback, String.format(" %d entities downloaded ", entityCount));
		
		super.UnInstall();

		sendProgressMessage(callback, "Done!");

		return true;
	}
}
