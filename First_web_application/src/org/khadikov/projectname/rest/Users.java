package org.khadikov.projectname.rest;

import org.khadikov.projectname.annotations.Path;
import org.khadikov.projectname.annotations.Restful;
import org.khadikov.projectname.dto.User;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Goncharov
 */

@Restful("/users")
public class Users {
    private List<User> users;

    public Users() {
        this.users = new ArrayList<User>();

        this.users.add(new User(1, "Arsen", "arsen.khadikov@gmail.com"));
        this.users.add(new User(2, "Vova", "vova.shvets@gmail.com"));
        this.users.add(new User(3, "Vlad", "vlad.bondar@gmail.com"));
        this.users.add(new User(4, "Tyoma", "tyoma.polyakov@gmail.com"));
    }

    @Path("")
    public List<User> get_all_users() {
        return users;
    }
    @Path("/:id")
    public User get_user_by_id(String id){
        for(int i=0;i<users.size();i++){
            if(Integer.parseInt(id)==users.get(i).getId())
                return users.get(i);
        }
        return null;
    }

    @Path("")
    public void add_user(String name,String email){
        int id=users.get(users.size() - 1).getId();
        User user=new User(id,name,email);
        users.add(user);
    }
    @Path("/:id")
    public boolean update_user_info(String id,String name,String email){
        for(int i=0;i<users.size();i++){
            if(Integer.parseInt(id)==users.get(i).getId()) {
                users.get(i).setEmail(email);
                users.get(i).setName(name);
                return true;
            }
        }
        return false;
    }

    @Path("/:id")
    public boolean delete_user(String id){
        for(int i=0;i<users.size();i++){
            if(Integer.parseInt(id)==users.get(i).getId()) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }
    public String toString(){
        final StringBuilder sb= new StringBuilder("");
        for(int i=0;i<users.size();i++){
            sb.append(users.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
