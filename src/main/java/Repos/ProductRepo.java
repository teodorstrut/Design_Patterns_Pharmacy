package Repos;

import Exceptions.RepoException;
import Models.BaseObject;
import Models.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductRepo implements IRepo<Product> {
    private Connection connection;

    public ProductRepo() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:sqlserver://DESKTOP-IR26HHH\\SQLEXPRESS:61652;databaseName=DesignPatternsPharmacy;user=teo;password=123;"
        );
        Statement s = connection.createStatement();
    }

    public ArrayList<Product> getAll() throws RepoException {
        Statement s = null;
        try {
            s = connection.createStatement();
            ResultSet resultSet = s.executeQuery("select * from Product");
            ArrayList<Product> returnedList = new ArrayList<>();
            while (resultSet.next()) {
                returnedList.add(new Product(
                        resultSet.getInt(1),
                        resultSet.getDouble(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5)
                ));
            }

            return returnedList;
        } catch (SQLException e) {
            throw new RepoException("getting products failed:" + e.getMessage());
        }

    }

    public Product add(Product product) throws RepoException {
        try {
            Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "insert into Product(id, price, productName, description, quantity)" +
                            " values (?, ?, ?, ?, ?)");
            ps.setInt(1, product.getId());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getProductName());
            ps.setString(4, product.getDescription());
            ps.setInt(5, product.getQuantity());

            ps.execute();

            return product;
        } catch (SQLException e) {
            throw new RepoException("adding product failed:" + e.getMessage());
        }

    }

    public int delete(int productId) throws RepoException {
        try {
            Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("delete from Product where id = ?");
            ps.setInt(1, productId);
            ps.execute();

            return productId;
        } catch (SQLException e) {
            throw new RepoException("deleting product failed:" + e.getMessage());
        }
    }

    public Product update(Product product) throws RepoException {
        try {
            Statement s = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(
                    "update Product" +
                            " set price = ?, productName = ?, description = ?, quantity = ? " +
                            " where id = ?");

            ps.setDouble(1, product.getPrice());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getQuantity());
            ps.setInt(5, product.getId());

            ps.execute();

            return product;
        } catch (SQLException e) {
            throw new RepoException("updating product failed:" + e.getMessage());
        }
    }

    public int getProductStock(int productId) throws RepoException {
        try {
            Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "select quantity from Product where id = ?");
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RepoException("error getting quantity: " + e.getMessage());
        }
    }

    public Product getProductById(int productId) throws RepoException {
        try {
            Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "select * from Product where id = ?");
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new Product(
                    rs.getInt(1),
                    rs.getDouble(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5));
        } catch (SQLException e) {
            throw new RepoException("error getting product: " + e.getMessage());
        }
    }

    public ArrayList<Integer> getProductCount() throws RepoException {
        try {
            Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "select id from Product order by id ASC");

            ResultSet rs = ps.executeQuery();

            ArrayList<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
            return ids;
        } catch (SQLException e) {
            throw new RepoException("error getting product ids: " + e.getMessage());
        }
    }

    public ArrayList<Product> getProductsOrderedByQuantity() throws RepoException {
        try {
            Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "select * from Product order by quantity ASC");

            ResultSet rs = ps.executeQuery();

            ArrayList<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5)
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RepoException("error getting products: " + e.getMessage());
        }
    }
}
