/**
 * Created by HP on 14.06.2015.
 */
public class User {
    int id;
    String name;
    String email;
    public String getName(){return name;}
    public int getId(){return id;}
    public String getEmail(){return email;}
    public User(){}
    public User(int i,String n,String e){id=i;name=n;email=e;}
}
