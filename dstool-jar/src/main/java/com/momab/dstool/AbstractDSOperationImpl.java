/**
 * This code is copyrighted under the MIT license. Please see LICENSE.TXT.
 */
package com.momab.dstool;

import java.io.IOException;

import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

import com.momab.dstool.DSOperation.ProgressCallback;

/**
 * Abstract base class for data store operations. Manages installation and
 * deinstalltion of the app enngine remote API and the properties necesasry for
 * establish a connection to the remote app.
 *
 * @author Jonas Andreasson
 */
class AbstractDSOperationImpl {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private RemoteApiInstaller installer;

    /**
     * Constructs the baseclass
     *
     * @param host The remote host to install the remote api against
     * @param port Port for communication with the remote host
     * @param username User name for atuhentication
     * @param password Password for authentication
     *
     */
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

    /**
     * A class that wraps a {@link ProgressCallback} object and ensures that
     * calls to the interface methods will happen only of the referenced object
     * is not null.
     */
    static class CallbackWrapper implements ProgressCallback {

        private final ProgressCallback sink;

        /**
         * Conctrucst a wrapper object
         *
         * @param sink The ProgressCallback object that should recieved the
         * wrapped calls. If sink is null, calls will not be executed.
         */
        public CallbackWrapper(ProgressCallback sink) {
            this.sink = sink;
        }

        @Override
        public void onInstall() {
            if (sink != null) {
                sink.onInstall();
            }
        }

        @Override
        public void onBegin() {
            if (sink != null) {
                sink.onBegin();
            }
        }

        @Override
        public void onEntityCompleted() {
            if (sink != null) {
                sink.onEntityCompleted();
            }
        }

        @Override
        public void onDone(Integer count) {
            if (sink != null) {
                sink.onDone(count);
            }
        }
    }
}
