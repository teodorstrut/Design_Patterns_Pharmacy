package Observers;

import Models.Product;
import Services.ShoppingCart;

public class ShoppingCartObserver {
    private double balance;
    private ShoppingCart shoppingCart;

    public ShoppingCartObserver(ShoppingCart shoppingCart) {
        this.balance = 0;
        this.shoppingCart = shoppingCart;
    }

    public void sendBalanceChanged(double newBalance) {
        this.balance = newBalance;
        this.shoppingCart.updateCurrentBalance(newBalance);
    }

    public void sendProductDeleted(Product p) {
        this.shoppingCart.RemoveProduct(p.getId());
    }

}
