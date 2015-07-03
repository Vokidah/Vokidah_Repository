package org.khadikov.projectname.parser;

import org.khadikov.projectname.http.RequestType;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {
    private String id;
    private Map<String, String> handler = new HashMap<String, String>();
    private final String ID = "(^/[0-9]+$)|(^/[0-9]+/$)";
    private final String BOTH = "(^/$)|(^/[0-9]+$)|(^/[0-9]+/$)";

    public void setId(String id) {
        this.id = id;
        if (id.equals(""))
            handler.put("", id);
        else
            handler.put("/:id", id);
    }

    public String getID() {
        return this.id;
    }

    public Map<String, String> getHandler() {
        return this.handler;
    }

    public boolean Check_our_String(RequestType requestType, String URI, String path) {
        this.handler.put("/path", path);
        if (!URI.substring(0, path.length()).equals(path)) {
            return false;
        } else {
            if (URI.equals(path) || URI.equals(path + "/")) {
                this.setId("");
                return true;
            } else {
                if (requestType.equals(RequestType.GET)) {
                    return get_Mathcer_by_Request_Type(BOTH, URI, path);
                } else if (requestType.equals(RequestType.POST)) {
                    return false;
                }
                if (requestType.equals(RequestType.PUT) || requestType.equals(RequestType.DELETE)) {
                    return get_Mathcer_by_Request_Type(ID, URI, path);
                }
                return false;
            }
        }
    }

    public boolean get_Mathcer_by_Request_Type(String possible_inputs, String URI, String path) {
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
