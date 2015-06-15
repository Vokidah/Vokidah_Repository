package org.hadikov.projectname.rest;

import org.hadikov.projectname.annotations.Path;
import org.hadikov.projectname.annotations.Restful;
import org.hadikov.projectname.dto.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
@Restful
public class Users {
    private List<User> users;

    public Users() {
        this.users = new ArrayList<User>();

        this.users.add(new User(1, "Arsen", "arsen.khadikov@gmail.com"));
        this.users.add(new User(2, "Vova", "vova.shvets@gmail.com"));
        this.users.add(new User(3, "Vlad", "vlad.bondar@gmail.com"));
        this.users.add(new User(4, "Tyoma", "tyoma.polyakov@gmail.com"));
    }

    @Path("/:id")
    public List<User> get() {
        return new ArrayList<User>();
    }
}
