package se.lexicon.dreas94.dao;

import se.lexicon.dreas94.exception.ObjectNotFoundException;
import se.lexicon.dreas94.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO
{
    Product save(Product product);
    Optional<Product> findById(int id);
    List<Product> findAll();
    List<Product> findByName(String name);
    List<Product> findByPriceBetween(int low, int high);
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
