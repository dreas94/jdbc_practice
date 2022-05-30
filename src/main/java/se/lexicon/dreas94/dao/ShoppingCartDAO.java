package se.lexicon.dreas94.dao;

import se.lexicon.dreas94.exception.ObjectNotFoundException;
import se.lexicon.dreas94.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartDAO
{
    ShoppingCart save(ShoppingCart cart);
    Optional<ShoppingCart> findById(int id);
    List<ShoppingCart> findAll();
    List<ShoppingCart> findByOrderStatus(String status);
    List<ShoppingCart> findByReference(String customer);
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
