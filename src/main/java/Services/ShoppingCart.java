package Services;

import Enums.ShoppingCartState;
import Exceptions.NegativeQuantityException;
import Exceptions.RepoException;
import Exceptions.ShoppingCartException;
import Models.Product;
import Repos.RepoProductSingleton;

import java.util.ArrayList;
import java.util.Collections;

public class ShoppingCart {
    public double totalPrice;
    public ArrayList<Product> products;
    public ShoppingCartState shoppingCartState;
    private double currentBalance;

    RepoProductSingleton repoSingleton;

    public ShoppingCart() {
        this.products = new ArrayList<Product>();
        this.totalPrice = 0;
        this.shoppingCartState = ShoppingCartState.available;
        this.currentBalance = 0;
        this.repoSingleton = RepoProductSingleton.getInstance();
    }

    public void updateCurrentBalance(double newBalance) {
        this.currentBalance = newBalance;
        if (this.currentBalance >= this.totalPrice) {
            if (this.shoppingCartState != ShoppingCartState.notEnoughProducts)
                this.shoppingCartState = ShoppingCartState.available;
        } else {
            this.shoppingCartState = ShoppingCartState.notEnoughFunds;
        }
    }

    public void updateTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        if (this.totalPrice > this.currentBalance) {
            if (this.shoppingCartState != ShoppingCartState.notEnoughProducts) {
                this.shoppingCartState = ShoppingCartState.notEnoughFunds;
            }
        } else {
            this.shoppingCartState = ShoppingCartState.available;
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void EmptyShoppingCart() {
        this.products = new ArrayList<Product>();
        this.totalPrice = 0;
        this.currentBalance = 0;
        this.shoppingCartState = ShoppingCartState.available;
    }

    public double AddProduct(int productId) throws ShoppingCartException {
        try {
            int availableQuantity = this.repoSingleton.productRepo.getProductStock(productId);

            if (availableQuantity - 1 < 0) {
                this.shoppingCartState = ShoppingCartState.notEnoughProducts;
            }
            boolean found = false;
            for (Product pr : this.products) {
                if (pr.getId() == productId) {
                    pr.setQuantity(pr.getQuantity() + 1);
                    found = true;
                }
            }
            if (!found) {
                Product cloneProduct = this.repoSingleton.productRepo.getProductById(productId);
                cloneProduct.setQuantity(1);
                this.products.add(cloneProduct);
                this.totalPrice += cloneProduct.getPrice();
            }
            if (this.totalPrice > this.currentBalance && this.shoppingCartState == ShoppingCartState.available) {
                this.shoppingCartState = ShoppingCartState.notEnoughFunds;
            }
            return this.totalPrice;

        } catch (RepoException e) {
            throw new ShoppingCartException("could not add the product!: " + e.getMessage());
        }

    }


    public double AddProduct(int productId, int quantity) throws NegativeQuantityException, ShoppingCartException {
        if (quantity > 0) {
            try {
                int availableQuantity = this.repoSingleton.productRepo.getProductStock(productId);

                if (availableQuantity - quantity < 0) {
                    this.shoppingCartState = ShoppingCartState.notEnoughProducts;
                }
                boolean found = false;
                for (Product pr : this.products) {
                    if (pr.getId() == productId) {
                        pr.setQuantity(pr.getQuantity() + quantity);
                        found = true;
                    }
                }
                if (!found) {
                    Product cloneProduct = this.repoSingleton.productRepo.getProductById(productId);
                    cloneProduct.setQuantity(quantity);
                    this.products.add(cloneProduct);
                    this.totalPrice += cloneProduct.getPrice() * quantity;
                }
                if (this.totalPrice > this.currentBalance && this.shoppingCartState == ShoppingCartState.available) {
                    this.shoppingCartState = ShoppingCartState.notEnoughFunds;
                }
                return this.totalPrice;
            } catch (RepoException e) {
                throw new ShoppingCartException("could not get quantity of products: " + e.getMessage());
            }

        } else {
            throw new NegativeQuantityException("Cannot add negative or zero quantity to shopping cart!");
        }
    }

    public double RemoveProduct(int productId) {
        for (Product p : this.products) {
            try {
                int actualQuantity = this.repoSingleton.productRepo.getProductStock(p.getId());
                if (productId == p.getId()) {
                    this.products.remove(p);
                    this.totalPrice -= p.getPrice() * p.getQuantity();
                }
                if (p.getQuantity() > actualQuantity) {
                    this.shoppingCartState = ShoppingCartState.notEnoughProducts;
                }
            } catch (RepoException e) {
                System.out.println("could not get stocks: " + e.getMessage());
            }
        }
        if (this.totalPrice > this.currentBalance && this.shoppingCartState == ShoppingCartState.available) {
            this.shoppingCartState = ShoppingCartState.notEnoughProducts;
        }
        return this.totalPrice;
    }

    public double RemoveProduct(int productId, int quantity) throws NegativeQuantityException {
        for (Product p : this.products) {
            try {
                int actualQuantity = this.repoSingleton.productRepo.getProductStock(p.getId());
                if (productId == p.getId()) {
                    if (p.getQuantity() > quantity) {
                        p.setQuantity(p.getQuantity() - quantity);
                    } else if (p.getQuantity() == quantity) {
                        this.products.remove(p);
                    } else {
                        throw new NegativeQuantityException("Not enough products of this type in the cart!");
                    }
                    this.totalPrice -= p.getPrice() * quantity;
                }
                if (p.getQuantity() > actualQuantity) {
                    this.shoppingCartState = ShoppingCartState.notEnoughProducts;
                }
            } catch (RepoException e) {
                System.out.println("could not get stocks: " + e.getMessage());
            }
        }
        if (this.totalPrice > this.currentBalance && this.shoppingCartState == ShoppingCartState.available) {
            this.shoppingCartState = ShoppingCartState.notEnoughProducts;
        }
        return this.totalPrice;
    }

    public boolean Purchase() throws ShoppingCartException {
        if (this.shoppingCartState == ShoppingCartState.notEnoughProducts) {
            throw new ShoppingCartException("there are not enough products!");
        } else if (this.shoppingCartState == ShoppingCartState.notEnoughFunds) {
            throw new ShoppingCartException("insufficient funds!");
        } else {
            for (Product p : this.products) {
                try {
                    int actualQuantity = this.repoSingleton.productRepo.getProductStock(p.getId());
                    p.setQuantity(actualQuantity - p.getQuantity());
                    this.repoSingleton.productRepo.update(p);
                    this.EmptyShoppingCart();
                } catch (RepoException e) {
                    throw new ShoppingCartException("purchase failed: " + e.getMessage());
                }
            }
            return true;
        }
    }
}
