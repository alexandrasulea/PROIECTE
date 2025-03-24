package Model;

public class Client {
    private int clientId;
    private String name;
    private String email;
    private String phone;

    /**
     * Constructor pentru inițializarea unui client cu ID.
     * @param clientId ID-ul clientului.
     * @param name Numele clientului.
     * @param email Adresa de email a clientului.
     * @param phone Numărul de telefon al clientului.
     */
    public Client(int clientId, String name, String email, String phone) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Client(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(int clientId) {
        this.clientId=clientId;
    }

    public int getId() {
        return clientId;
    }
    @Override
    public String toString() {
        return name;
    }
    /**
     * Constructor pentru inițializarea unui client fără ID.
     * @param name Numele clientului.
     * @param email Adresa de email a clientului.
     * @param phone Numărul de telefon al clientului.
     */
}
