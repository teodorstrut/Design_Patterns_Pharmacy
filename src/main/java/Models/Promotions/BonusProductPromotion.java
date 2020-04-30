package Models.Promotions;

import Models.Product;
import Services.ShoppingCart;

public class BonusProductPromotion implements Promotion {
    private int productId, ratioToOne;

    public BonusProductPromotion(int productId, int ratioToOne) {
        this.productId = productId;
        this.ratioToOne = ratioToOne;
    }

    public ShoppingCart applyPromotion(ShoppingCart shoppingCart) {
        for (Product p : shoppingCart.getProducts()) {
            if (p.getId() == productId) {
                int freeProducts = p.getQuantity() / (ratioToOne + 1);
                shoppingCart.updateTotalPrice(shoppingCart.getTotalPrice() - p.getPrice() * freeProducts);
                break;
            }
        }
        return shoppingCart;
    }
}
