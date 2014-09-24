package com.momab.dstool;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * @author Jonas Andreasson
 * 
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 *
 */
class DSUploadOperationImpl extends AbstractDSOperationImpl implements
		DSOperation {

	private final InputStream in;

	DSUploadOperationImpl(String host, InputStream in,
			int port, String username, String password) {
		super(host, port, username, password);
		this.in = in;
	}

	@Override
	public boolean Run(final ProgressCallback callback) throws IOException, ClassNotFoundException {

		sendProgressMessage(callback, "Installing ");

		super.Install();

		ObjectInputStream objectStream; 
		int entityCount = 0;
		
		try {

			// Get the Datastore Service
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();

			objectStream = new ObjectInputStream(this.in);

			

			Object entity;

			while (true) {

				try {
					entity = objectStream.readObject();
				} catch (EOFException e) {
					break;
				}

				if (!(entity instanceof Entity)) {
					throw new InvalidClassException(
							"Could not find Entity object in input stream");
				}
	
				datastore.put((Entity) entity);
				
				entityCount++;
				sendProgressMessage(callback, ".");
			}

			sendProgressMessage(callback,
					String.format(" %d entities uploaded ", entityCount));

			return true;

		} finally {
			super.UnInstall();
			sendProgressMessage(callback, "Done!");
		}

	}
}
