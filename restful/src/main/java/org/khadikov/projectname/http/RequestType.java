package org.khadikov.projectname.http;

/**
 * @author Eugene Goncharov
 */
public enum RequestType {
    GET, POST, PUT, DELETE, Body;

    public String value() {
        return this.toString();
    }
}
