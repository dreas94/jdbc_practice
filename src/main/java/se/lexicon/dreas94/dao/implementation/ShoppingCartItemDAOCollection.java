package se.lexicon.dreas94.dao.implementation;

import se.lexicon.dreas94.dao.ShoppingCartItemDAO;
import se.lexicon.dreas94.db.MySQLConnection;
import se.lexicon.dreas94.exception.DBConnectionException;
import se.lexicon.dreas94.exception.ObjectNotFoundException;
import se.lexicon.dreas94.model.Product;
import se.lexicon.dreas94.model.ShoppingCart;
import se.lexicon.dreas94.model.ShoppingCartItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartItemDAOCollection implements ShoppingCartItemDAO
{
    /**
     * @param cartItem ShoppingCartItem
     * @return cartItem ShoppingCartItem
     */
    @Override
    public ShoppingCartItem save(ShoppingCartItem cartItem)
    {
        String insertQuery = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet keySet = null;

        if(cartItem.getId() == 0)
        {
            insertQuery = "insert into cartItem (amount, total_price, product_id, cart_id) values (?,?,?,?)";
        }
        else if(cartItem.getId() < 0)
        {
            insertQuery = "update cartItem set amount = ?, total_price = ?, product_id = ?, cart_id = ?, payment_approved = ? where id ?";
        }

        try
        {
            connection = MySQLConnection.getConnection();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            if(cartItem.getId() > 0) preparedStatement.setInt(6, cartItem.getId());

            preparedStatement.setInt(1, cartItem.getAmount());
            preparedStatement.setDouble(2, cartItem.getTotalPrice());
            preparedStatement.setInt(3, cartItem.getItem().getId());
            preparedStatement.setInt(4, cartItem.getCart().getId());

            int result = preparedStatement.executeUpdate();
            keySet = preparedStatement.getGeneratedKeys();

            System.out.println("Save operation is done successfully");

            if(keySet.next())
            {
                int generatedId = keySet.getInt(1);
                cartItem.setId(generatedId);
            }

            System.out.println("result = " + result);
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        }
        catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        } finally
        {
            closeAll(keySet, preparedStatement, connection);
        }

        return cartItem;
    }

    /**
     * @param id int
     * @return Optional<ShoppingCartItem>
     */
    @Override
    public Optional<ShoppingCartItem> findById(int id)
    {
        ShoppingCartItem result = null;
        String selectQuery = "select * from cartItem where id = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            ProductDAOCollection productDAOCollection = new ProductDAOCollection();
            ShoppingCartDAOCollection cartDAOCollection = new ShoppingCartDAOCollection();

            if(resultSet.next())
            {
                result = new ShoppingCartItem(
                        resultSet.getInt("id"),
                        resultSet.getInt("amount"),
                        resultSet.getDouble("total_price"),
                        productDAOCollection.findById(resultSet.getInt("product_id")).orElse(null),
                        cartDAOCollection.findById(resultSet.getInt("cart_id")).orElse(null));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        }
        finally
        {
            closeAll(resultSet);
        }

        return Optional.ofNullable(result);
    }

    /**
     * @return List<ShoppingCartItem>
     */
    @Override
    public List<ShoppingCartItem> findAll()
    {
        List<ShoppingCartItem> cartItemList = new ArrayList<>();
        String selectQuery = "select * from cart";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {
            resultSet = preparedStatement.executeQuery();
            ProductDAOCollection productDAOCollection = new ProductDAOCollection();
            ShoppingCartDAOCollection cartDAOCollection = new ShoppingCartDAOCollection();

            while(resultSet.next())
            {
                cartItemList.add(new ShoppingCartItem(
                        resultSet.getInt("id"),
                        resultSet.getInt("amount"),
                        resultSet.getDouble("total_price"),
                        productDAOCollection.findById(resultSet.getInt("product_id")).orElse(null),
                        cartDAOCollection.findById(resultSet.getInt("cart_id")).orElse(null)));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        }
        finally
        {
            closeAll(resultSet);
        }
        return cartItemList;
    }

    /**
     * @param cartId int
     * @return List<ShoppingCartItem>
     */
    @Override
    public List<ShoppingCartItem> findByCartId(int cartId)
    {
        List<ShoppingCartItem> cartItemList = new ArrayList<>();
        String selectQuery = "select * from cartItem where cart_id = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            preparedStatement.setInt(1, cartId);
            resultSet = preparedStatement.executeQuery();
            ProductDAOCollection productDAOCollection = new ProductDAOCollection();
            ShoppingCartDAOCollection cartDAOCollection = new ShoppingCartDAOCollection();

            while(resultSet.next())
            {
                cartItemList.add(new ShoppingCartItem(
                        resultSet.getInt("id"),
                        resultSet.getInt("amount"),
                        resultSet.getDouble("total_price"),
                        productDAOCollection.findById(resultSet.getInt("product_id")).orElse(null),
                        cartDAOCollection.findById(resultSet.getInt("cart_id")).orElse(null)));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        }
        finally
        {
            closeAll(resultSet);
        }
        return cartItemList;
    }

    /**
     * @param productId int
     * @return List<ShoppingCartItem>
     */
    @Override
    public List<ShoppingCartItem> findByProductId(int productId)
    {
        List<ShoppingCartItem> cartItemList = new ArrayList<>();
        String selectQuery = "select * from cartItem where product_id = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            preparedStatement.setInt(1, productId);
            resultSet = preparedStatement.executeQuery();
            ProductDAOCollection productDAOCollection = new ProductDAOCollection();
            ShoppingCartDAOCollection cartDAOCollection = new ShoppingCartDAOCollection();

            while(resultSet.next())
            {
                cartItemList.add(new ShoppingCartItem(
                        resultSet.getInt("id"),
                        resultSet.getInt("amount"),
                        resultSet.getDouble("total_price"),
                        productDAOCollection.findById(resultSet.getInt("product_id")).orElse(null),
                        cartDAOCollection.findById(resultSet.getInt("cart_id")).orElse(null)));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        }
        finally
        {
            closeAll(resultSet);
        }
        return cartItemList;
    }

    /**
     * @param id int
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws ObjectNotFoundException if result is a illegal value (0)
     */
    @Override
    public int delete(int id) throws ObjectNotFoundException
    {
        String deleteQuery = "delete from cartItem where id = ?";
        int result = 0;
        try(Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery))
        {
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();

            if (result == 0) throw new ObjectNotFoundException("row for cartItem by id " + id + " dose not exist");

            System.out.println("result = " + result);
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        }
        return result;
    }
}
