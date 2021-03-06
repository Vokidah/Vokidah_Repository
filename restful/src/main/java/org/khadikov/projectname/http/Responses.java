package org.khadikov.projectname.http;

/**
 * @author Eugene Goncharov
 */
public enum Responses {
    NOT_FOUND(404, "NOT FOUND"),CANT_ADD(500,"CANT ADD");

    int code;
    String message;

    Responses(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
