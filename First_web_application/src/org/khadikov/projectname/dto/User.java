package org.khadikov.projectname.dto;


import org.khadikov.projectname.annotations.Body;

/**
 * Created by HP on 14.06.2015.
 */

@Body
public class User {
    // You should double check what private/protected/public and package-local means
    private int id;
    private String name;
    private String email;

    public User() {
    }

    // This is a bad style for programming – you should not name variables with just letters
    // public User(int i, String n, String e) {

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Please take a look why do we need equals/hash-code and toString methods
    // http://www.ideyatech.com/2011/04/effective-java-equals-and-hashcode/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        final User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return !(email != null ? !email.equals(user.email) : user.email != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\"" + ":").append(id);
        sb.append("," + "\"name\"" + ":").append("\"" + name + "\"");
        sb.append("," + "\"email\"" + ":").append("\"" + email + "\"");
        sb.append('}');
        return sb.toString();
    }
}
