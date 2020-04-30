package Services;

import Exceptions.ProductServiceException;
import Exceptions.RepoException;
import Models.Product;
import Repos.RepoProductSingleton;

import java.util.ArrayList;

public class ProductService {
    private RepoProductSingleton repoProductSingleton;
    ArrayList<Integer> unavailableIds;

    public ProductService() throws ProductServiceException {
        this.repoProductSingleton = RepoProductSingleton.getInstance();
        try {
            this.unavailableIds = this.repoProductSingleton.productRepo.getProductCount();
        } catch (RepoException e) {
            throw new ProductServiceException("product service init failed: " + e.getMessage());
        }
    }

    public Product addProduct(double price, String productName, String description, int quantity) throws ProductServiceException {
        int newId = getAvailableId();
        Product newProduct = new Product(newId, price, productName, description, quantity);
        try {
            this.repoProductSingleton.productRepo.add(newProduct);
            return newProduct;
        } catch (RepoException e) {
            throw new ProductServiceException("failed adding new Product: " + e.getMessage());
        }
    }

    public int deleteProduct(int productId) throws ProductServiceException {
        try {
            this.repoProductSingleton.productRepo.delete(productId);
            return productId;
        } catch (RepoException e) {
            throw new ProductServiceException("failed removing the Product: " + e.getMessage());
        }
    }

    public void addProductStock(int productId, int newStock) throws ProductServiceException {
        try {
            Product p = this.repoProductSingleton.productRepo.getProductById(productId);
            p.setQuantity(newStock);
            this.repoProductSingleton.productRepo.update(p);
        } catch (RepoException e) {
            throw new ProductServiceException("failed updating stock: " + e.getMessage());
        }
    }

    public ArrayList<Product> getProductsByAscendingStock() throws ProductServiceException {
        try {
            return this.repoProductSingleton.productRepo.getProductsOrderedByQuantity();
        } catch (RepoException e) {
            throw new ProductServiceException("failed getting products: " + e.getMessage());
        }
    }

    private int getAvailableId() {
        for (int i = 0; i < unavailableIds.size() - 1; i++) {
            if (unavailableIds.get(i + 1) - unavailableIds.get(i) > 1) {
                this.unavailableIds.add(i + 1, unavailableIds.get(i) + 1);
                return unavailableIds.get(i) + 1;
            }
        }
        this.unavailableIds.add(unavailableIds.get(unavailableIds.size() - 1) + 1);
        return unavailableIds.get(unavailableIds.size() - 1) + 1;
    }
}
