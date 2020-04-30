package Models.Promotions;

import Services.ShoppingCart;

public class PricePromotion implements Promotion {
    private int percentOff;

    public PricePromotion(int percentOff) {
        this.percentOff = percentOff;
    }

    public ShoppingCart applyPromotion(ShoppingCart shoppingCart) {
        double newPrice = shoppingCart.getTotalPrice();
        newPrice = newPrice - newPrice / this.percentOff;
        shoppingCart.updateTotalPrice(newPrice);
        return shoppingCart;
    }
}
