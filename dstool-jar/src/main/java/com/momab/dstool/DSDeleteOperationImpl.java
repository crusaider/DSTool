package com.momab.dstool;

import java.io.IOException;
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
class DSDeleteOperationImpl extends AbstractDSOperationImpl implements
		DSOperation {

	private final String entityKind;
	
	DSDeleteOperationImpl(String host, String entityKind,
			int port, String username,
			String password)  {
		super(host, port, username, password);
		this.entityKind = entityKind;
	}

	@Override
	public boolean Run(ProgressCallback callback) throws IOException {
		
		sendProgressMessage(callback, "Installing ");
		
		super.Install();
		
		sendProgressMessage(callback, "Preparing query ");

		
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// Use class Query to assemble a query
		Query q = new Query(this.entityKind).setKeysOnly();

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		int entityCount = 0;
		
		for (Entity e : pq.asIterable()) {
			datastore.delete(e.getKey());
			entityCount++;
			sendProgressMessage(callback, ".");
		}
		
		sendProgressMessage(callback, String.format(" %d entities deleted ", entityCount));
		
		super.UnInstall();

		sendProgressMessage(callback, "Done!");

		return true;
	}
}
