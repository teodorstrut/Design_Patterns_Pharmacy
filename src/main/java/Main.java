import Controller.Controller;
import Exceptions.RepoException;

public class Main {
    public static void main(String[] args) throws RepoException {
        System.out.println("here we go!");
        Controller c = new Controller();
        c.run();
    }
}
