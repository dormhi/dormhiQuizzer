public abstract class User {   //main abstract
    private String id;       // ID for everyone (Student Number, Teacher ID, or Admin Code)
    private String fullName;
    private String username;
    private String password;

    public User(String id, String fullName, String username, String password) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String inputPassword) {  //pasword line
        return this.password.equals(inputPassword);
    }
}