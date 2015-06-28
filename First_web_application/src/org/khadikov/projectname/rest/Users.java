package org.khadikov.projectname.rest;

import org.khadikov.projectname.annotations.*;
import org.khadikov.projectname.dto.User;

import java.util.ArrayList;
import java.util.List;


@Restful("/users")
public class Users {
    private List<User> users=new ArrayList<User>();

    public Users() {

        this.users.add(new User(1, "Arsen", "arsen.khadikov@gmail.com"));
        this.users.add(new User(2, "Vova", "vova.shvets@gmail.com"));
        this.users.add(new User(3, "Vlad", "vlad.bondar@gmail.com"));
        this.users.add(new User(4, "Tyoma", "tyoma.polyakov@gmail.com"));
    }
    public int size(){
        return this.users.size();
    }

    @Get()
    public List<User> get_all_users() {
        return this.users;
    }

    @Get("/:id")
    public User get_user_by_id(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (Integer.parseInt(id) == users.get(i).getId())
                return users.get(i);
        }
        return null;
    }

    @Post()
    public boolean add_user(User user) throws NullPointerException{
        if(users.size()!=0)
            user.setId(users.get(users.size() - 1).getId()+1);
        else
            user.setId(1);
        boolean check=true;
        for(int i=0;i<users.size();i++)
        {
            if(users.get(i).getEmail().equals(user.getEmail()) && users.get(i).getName().equals(user.getName()))
            {
                check=false;
            }
        }
        if(check) {
            this.users.add(user);
        }
        return check;
    }

    @Put("/:id")
    public boolean update_user_info(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (user.getId() == users.get(i).getId()) {
                users.get(i).setEmail(user.getEmail());
                users.get(i).setName(user.getName());
                return true;
            }
        }
        return false;
    }

    @Delete("/:id")
    public boolean delete_user(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (Integer.parseInt(id) == users.get(i).getId()) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }


}
