package Repos;

import java.sql.SQLException;

public class RepoProductSingleton {

    private static RepoProductSingleton singleton = null;
    public ProductRepo productRepo;

    private RepoProductSingleton() throws SQLException {
        productRepo = new ProductRepo();
    }

    public static RepoProductSingleton getInstance() {
        if (singleton == null) {
            try {
                singleton = new RepoProductSingleton();
                return singleton;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return singleton;
    }
}
