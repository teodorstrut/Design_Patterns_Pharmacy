package Controller;

import Exceptions.NegativeQuantityException;
import Exceptions.ProductServiceException;
import Exceptions.ShoppingCartException;
import Models.Product;
import Models.Promotions.BonusProductPromotion;
import Models.Promotions.PricePromotion;
import Observers.ShoppingCartObserver;
import Repos.RepoProductSingleton;
import Services.ProductService;
import Services.ShoppingCart;

import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    private ShoppingCart shoppingCart;
    private ShoppingCartObserver shoppingCartObserver;
    private RepoProductSingleton repoProductSingleton;
    private ProductService productService;
    private Scanner scanner;
    private double balance;

    public Controller() {
        shoppingCart = new ShoppingCart();
        shoppingCartObserver = new ShoppingCartObserver(shoppingCart);
        repoProductSingleton = RepoProductSingleton.getInstance();
        try {
            productService = new ProductService();
        } catch (ProductServiceException e) {
            System.out.println("failed creating controller: " + e.getMessage());
        }
        scanner = new Scanner(System.in).useDelimiter("\n");
    }

    public void run() {
        boolean running = true;
        System.out.println("----------------------------------------");
        System.out.println("Welcome to the ultimate online Pharmacy!");
        System.out.println("----------------------------------------");
        while (running) {
            this.printMainMenu();
            System.out.print("Enter an option: ");
            int num = scanner.nextInt();
            switch (num) {
                case 0:
                    running = false;
                    break;
                case 1:
                    System.out.println("Your balance: " + this.balance);
                    break;
                case 2:
                    this.UIAddBalance();
                    break;
                case 3:
                    this.UIManageCart();
                    break;
                case 4:
                    this.UIManageShop();
                    break;
                default:
                    break;
            }
        }

    }

    private void UIAddBalance() {
        System.out.print("Enter desired amount: ");
        double amount = scanner.nextDouble();
        this.balance += amount;
        this.shoppingCartObserver.sendBalanceChanged(this.balance);
    }

    private void UIManageShop() {
        boolean wentBack = false;
        while (!wentBack) {
            this.printManageMenu();
            System.out.println("Please select an option:");
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    wentBack = true;
                    break;
                case 1:
                    System.out.println("Enter the product's name: ");
                    String productName = scanner.next();
                    System.out.println("Enter the product's description: ");
                    String description = scanner.next();
                    System.out.println("Enter the price: ");
                    double price = scanner.nextDouble();
                    System.out.println("Enter the initial quantity: ");
                    int quantity = scanner.nextInt();
                    try {
                        this.productService.addProduct(price, productName, description, quantity);
                    } catch (ProductServiceException e) {
                        System.out.println("Failed adding product: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Enter the product's id: ");
                    int productId = scanner.nextInt();
                    try {
                        this.productService.deleteProduct(productId);
                    } catch (ProductServiceException e) {
                        System.out.println("Failed removing product: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        ArrayList<Product> products = this.productService.getProductsByAscendingStock();
                        for (Product p : products) {
                            System.out.println(p.toString());
                        }
                    } catch (ProductServiceException e) {
                        System.out.println("Failed getting products: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Enter the product's id: ");
                    int id = scanner.nextInt();
                    System.out.println("Enter the product's additional stocks: ");
                    int stock = scanner.nextInt();
                    try {
                        this.productService.addProductStock(id, stock);
                    } catch (ProductServiceException e) {
                        System.out.println("Failed updating stock for this product: " + e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void UIManageCart() {
        boolean wentBack = false;
        while (!wentBack) {
            this.printShopMenu();
            System.out.println("Please select an option:");
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    wentBack = true;
                    break;
                case 1:
                    System.out.println("Available products:");
                    try {
                        ArrayList<Product> products = this.productService.getProductsByAscendingStock();
                        for (Product p : products) {
                            System.out.println(p);
                        }
                    } catch (ProductServiceException e) {
                        System.out.println("Could not get products: " + e.getMessage());
                    }
                    break;
                case 2:
                    ArrayList<Product> myProducts = this.shoppingCart.getProducts();
                    System.out.println("Your current products in the cart:");
                    for (Product p : myProducts) {
                        System.out.println(p);
                    }
                    break;
                case 3:
                    System.out.println("Enter the product's id:");
                    int productId = scanner.nextInt();
                    System.out.println("Enter the desired quantity:");
                    int quantity = scanner.nextInt();
                    try {
                        double newPrice = 0;
                        if (quantity == 1) {
                            newPrice = this.shoppingCart.AddProduct(productId);
                        } else if (quantity > 1) {
                            newPrice = this.shoppingCart.AddProduct(productId, quantity);
                        }
                        System.out.println("your new total price: " + newPrice);
                    } catch (ShoppingCartException e) {
                        System.out.println("Adding the product failed: " + e.getMessage());
                    } catch (NegativeQuantityException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Enter the product's id:");
                    int pId = scanner.nextInt();
                    System.out.println("Enter the desired quantity:");
                    int q = scanner.nextInt();
                    try {
                        double newPrice = 0;
                        if (q == 1) {
                            newPrice = this.shoppingCart.RemoveProduct(pId);
                        } else if (q > 1) {
                            newPrice = this.shoppingCart.RemoveProduct(pId, q);
                        }
                        System.out.println("your new total price: " + newPrice);
                    } catch (NegativeQuantityException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    this.UIApplyPromotions();
                    break;
                case 6:
                    try {
                        this.shoppingCart.Purchase();
                    } catch (ShoppingCartException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void UIApplyPromotions() {
        boolean wentBack = false;
        while (!wentBack) {
            this.printPromotionsSection();
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    wentBack = true;
                    break;
                case 1:
                    new PricePromotion(1).applyPromotion(shoppingCart);
                    System.out.println("Your new Total: " + shoppingCart.getTotalPrice());
                    break;
                case 2:
                    new BonusProductPromotion(1, 2).applyPromotion(shoppingCart);
                    System.out.println("Your new Total: " + shoppingCart.getTotalPrice());
                    break;
                case 3:
                    new BonusProductPromotion(1, 2)
                            .applyPromotion(new PricePromotion(10)
                                    .applyPromotion(shoppingCart));
                    System.out.println("Your new Total: " + shoppingCart.getTotalPrice());
                    break;
                default:
                    break;
            }

        }
    }

    private void printMainMenu() {
        System.out.println(" ---v---Main Menu---v---");
        System.out.println("Please select an option:");
        System.out.println("0: Exit");
        System.out.println("1: Check balance");
        System.out.println("2: Add balance");
        System.out.println("3: Shop");
        System.out.println("4: Manage products(admins only :P)");
    }

    private void printShopMenu() {
        System.out.println(" ---v---Shop Menu---v---");
        System.out.println("Please select an option:");
        System.out.println("0: Back");
        System.out.println("1: Show available products!");
        System.out.println("2: Show current cart");
        System.out.println("3: Add a product to the cart");
        System.out.println("4: Remove a product from the cart");
        System.out.println("5: Apply promotions!");
        System.out.println("6: Checkout");
    }

    private void printManageMenu() {
        System.out.println(" ---v---Manage Menu---v---");
        System.out.println("Please select an option:");
        System.out.println("0: Back");
        System.out.println("1: Add a new Product");
        System.out.println("2: Remove a registered product");
        System.out.println("3: Show stocks");
        System.out.println("4: Add stocks to a product");
    }

    private void printPromotionsSection() {
        System.out.println(" ---v---Manage Menu---v---");
        System.out.println("Please choose an available promotion:");
        System.out.println("0: back");
        System.out.println("1: 10% off");
        System.out.println("2: 2 masks bought, 1 mask free");
        System.out.println("3: all of the above!");
    }
}
