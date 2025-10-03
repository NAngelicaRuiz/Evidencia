public class Administrator {
    private String id;
    private String password;

    public Administrator(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public boolean authenticate(String inputId, String inputPassword) {
        return id.equals(inputId) && password.equals(inputPassword);
    }
}