package Models;

public class Client {
    private String name;
    private String address;
    private double balance;

    public Client(String name, String address, double balance) {
        this.name = name;
        this.address = address;
        this.balance = balance;
    }

    public Client(int id, String name, String address) {
        this.name = name;
        this.address = address;
        this.balance = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                '}';
    }
}
