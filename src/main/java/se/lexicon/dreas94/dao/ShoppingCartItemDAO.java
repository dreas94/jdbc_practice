package se.lexicon.dreas94.dao;

import se.lexicon.dreas94.exception.ObjectNotFoundException;
import se.lexicon.dreas94.model.ShoppingCartItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartItemDAO
{
    ShoppingCartItem save(ShoppingCartItem cartItem);
    Optional<ShoppingCartItem> findById(int id);
    List<ShoppingCartItem> findAll();
    List<ShoppingCartItem> findByCartId(int cartId);
    List<ShoppingCartItem> findByProductId(int productId);
    int delete(int id) throws ObjectNotFoundException;

    default void closeAll(AutoCloseable... closeables)
    {
        if (closeables != null)
        {
            System.out.println("Close connections and statements");
            for (AutoCloseable autoCloseable : closeables)
            {
                try
                {
                    autoCloseable.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
