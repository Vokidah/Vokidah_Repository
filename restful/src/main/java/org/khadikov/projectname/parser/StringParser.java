package org.khadikov.projectname.parser;

import org.khadikov.projectname.http.RequestType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HP on 01.07.2015.
 */
public class StringParser {
    private String URI;
    private String path;
    private String id;

    public StringParser(String URI, String path) {
        this.URI = URI;
        this.path = path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public boolean Check_our_String(RequestType requestType) {
        if (!URI.substring(0, path.length()).equals(path)) {
            return false;
        } else {
            if (URI.equals(path) || URI.equals(path + "/")) {
                this.setId("");
                return true;
            } else {
                if (requestType.equals(RequestType.GET)) {
                    return get_Mathcer_by_Request_Type("(^/$)|(^/[0-9]+$)|(^/[0-9]+/$)");
                } else if (requestType.equals(RequestType.POST)) {
                    return false;
                }
                if (requestType.equals(RequestType.PUT) || requestType.equals(RequestType.DELETE)) {
                    return get_Mathcer_by_Request_Type("(^/[0-9]+$)|(^/[0-9]+/$)");
                }
                return false;
            }
        }
    }

    public boolean get_Mathcer_by_Request_Type(String possible_inputs) {
        Pattern pattern = Pattern.compile(possible_inputs);
        Matcher m = pattern.matcher(URI.substring(path.length(), URI.length()));
        if (m.matches()) {
            if (URI.charAt(URI.length() - 1) == '/') {
                this.setId(URI.substring(path.length() + 1, URI.length() - 1));
            } else {
                this.setId(URI.substring(path.length() + 1, URI.length()));
            }
        }
        return m.matches();
    }

}
