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
    private final String ID = "(^/$)|(^/[0-9]+$)|(^/[0-9]+/$)";

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

    public boolean CheckOurString(RequestType requestType, String URI, String path) {
        if (!URI.substring(0, path.length()).equals(path)) {
            return false;
        } else {
            if (URI.equals(path) || URI.equals(path + "/")) {
                this.setId("");
                return true;
            } else {
                if (requestType.equals(RequestType.GET)) {
                    return getMathcerByRequest_Type(ID, URI, path);
                } else if (requestType.equals(RequestType.POST)) {
                    return false;
                }
                if (requestType.equals(RequestType.PUT) || requestType.equals(RequestType.DELETE)) {
                    return getMathcerByRequest_Type(ID, URI, path);
                }
                return false;
            }
        }
    }

    public boolean getMathcerByRequest_Type(String possible_inputs, String URI, String path) {
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
