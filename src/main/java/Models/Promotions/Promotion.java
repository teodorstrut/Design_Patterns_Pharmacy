package Models.Promotions;

import Services.ShoppingCart;

public interface Promotion {
    ShoppingCart applyPromotion(ShoppingCart shoppingCart);
}
