package Exceptions;

import Services.ShoppingCart;

public class ShoppingCartException extends Throwable {
    public ShoppingCartException(String message) {
        super(message);
    }
}
