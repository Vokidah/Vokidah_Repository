package org.khadikov.projectname.rest;

import org.khadikov.projectname.annotations.*;
import org.khadikov.projectname.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Restful("/users")
public class Users {
    private List<User> users = new ArrayList<User>();
    private Map<String, User> usersByEmail = new HashMap<String, User>();
    private Map<Integer, User> usersByIdx = new HashMap<Integer, User>();

    public Users() {
        this.users.add(new User(1, "Arsen", "arsen.khadikov@gmail.com"));
        this.users.add(new User(2, "Vova", "vova.shvets@gmail.com"));
        this.users.add(new User(3, "Vlad", "vlad.bondar@gmail.com"));
        this.users.add(new User(4, "Tyoma", "tyoma.polyakov@gmail.com"));

        for (User user : users) {
            this.usersByEmail.put(user.getEmail(), user);
            this.usersByIdx.put(user.getId(), user);
        }
    }

    public int size() {
        return this.users.size();
    }

    @Get()
    public List<User> getAllUsers() {
        return this.users;
    }

    @Get("/:id")
    public User getUserById(String id) {

        Integer userId = Integer.valueOf(id);

        if (usersByIdx.containsKey(userId)) {
            return usersByIdx.get(userId);
        }

        return null;
    }

    @Post()
    public boolean addUser(@Body User user) throws NullPointerException {
        autoGenerateIdFor(user);

        boolean result = false;

        if (!usersByEmail.containsKey(user.getEmail())) {
            result = this.users.add(user);
        }

        return result;
    }


    @Put("/:id")
    public boolean updateUserInfo(@Body User user) {
        for (User user1 : users) {

            if (user.getId() == user1.getId()) {
                user1.setEmail(user.getEmail());
                user1.setName(user.getName());

                return true;
            }
        }

        return false;
    }

    @Delete("/:id")
    public boolean deleteUser(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (Integer.parseInt(id) == users.get(i).getId()) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }


    private void autoGenerateIdFor(User user) {
        if (users.size() != 0) {
            user.setId(users.get(users.size() - 1).getId() + 1);
        } else {
            user.setId(1);
        }
    }

}
