
package com.momab.dstool;

/**
 *
 * @author Jonas
 */
public class Endpoint {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private Endpoint(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static class Builder {

        private final String host;
        private int port = 80;
        private String username;
        private String password;

        public Builder(String host) {
            this.host = host;
        }

        public Builder asUser( String username, String password ) {
            this.username = username;
            this.password = password;
            return this;
        }
        
        public Builder usingPort(int port) {
            this.port = port;
            return this;
        }
        
        public Endpoint build() {
            return new Endpoint(this);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
