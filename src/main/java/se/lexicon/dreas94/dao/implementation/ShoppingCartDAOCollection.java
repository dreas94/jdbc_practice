package se.lexicon.dreas94.dao.implementation;

import se.lexicon.dreas94.dao.ShoppingCartDAO;
import se.lexicon.dreas94.db.MySQLConnection;
import se.lexicon.dreas94.exception.DBConnectionException;
import se.lexicon.dreas94.exception.ObjectNotFoundException;
import se.lexicon.dreas94.model.ShoppingCart;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartDAOCollection implements ShoppingCartDAO
{
    /**
     * Saves a ShoppingCart in SQL
     * @param cart ShoppingCart
     * @return ShoppingCart cart
     */
    @Override
    public ShoppingCart save(ShoppingCart cart)
    {
        String insertQuery = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet keySet = null;

        if(cart.getId() == 0)
        {
            insertQuery = "insert into cart (last_update, order_status, delivery_address, customer_reference, payment_approved) values (?,?,?,?,?)";
        }
        else if(cart.getId() < 0)
        {
            insertQuery = "update cart set last_update = ?, order_status = ?, delivery_address = ?, customer_reference = ?, payment_approved = ? where id ?";
        }

        try
        {
            connection = MySQLConnection.getConnection();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            if(cart.getId() > 0) preparedStatement.setInt(6, cart.getId());

            preparedStatement.setString(1, cart.getLastUpdate().toString());
            preparedStatement.setString(2, cart.getOrderStatus());
            preparedStatement.setString(3, cart.getDeliveryAddress());
            preparedStatement.setString(4, cart.getCustomerReference());
            preparedStatement.setBoolean(5, cart.isPaymentApproved());

            int result = preparedStatement.executeUpdate();
            keySet = preparedStatement.getGeneratedKeys();

            System.out.println("Save operation is done successfully");

            if(keySet.next())
            {
                int generatedId = keySet.getInt(1);
                cart.setId(generatedId);
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

        return cart;
    }

    /**
     * @param id int
     * @return Optional.ofNullable(result) Optional<ShoppingCart>
     */
    @Override
    public Optional<ShoppingCart> findById(int id)
    {
        ShoppingCart result = null;
        String selectQuery = "select * from cart where id = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                result = new ShoppingCart(
                        resultSet.getInt("id"),
                        LocalDateTime.parse(resultSet.getString("last_update")),
                        resultSet.getString("order_status"),
                        resultSet.getString("delivery_address"),
                        resultSet.getString("customer_reference"),
                        resultSet.getBoolean("payment_approved"));
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
     * @return cartList List<ShoppingCart>
     */
    @Override
    public List<ShoppingCart> findAll()
    {
        List<ShoppingCart> cartList = new ArrayList<>();
        String selectQuery = "select * from cart";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                cartList.add(new ShoppingCart(
                        resultSet.getInt("id"),
                        LocalDateTime.parse(resultSet.getString("last_update")),
                        resultSet.getString("order_status"),
                        resultSet.getString("delivery_address"),
                        resultSet.getString("customer_reference"),
                        resultSet.getBoolean("payment_approved")));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        } finally
        {
            closeAll(resultSet);
        }
        return cartList;
    }

    /**
     * @param status String
     * @return cartList List<ShoppingCart>
     */
    @Override
    public List<ShoppingCart> findByOrderStatus(String status)
    {
        List<ShoppingCart> cartList = new ArrayList<>();
        String selectQuery = "select * from cart where order_status = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {
            preparedStatement.setString(1, status);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                cartList.add(new ShoppingCart(
                        resultSet.getInt("id"),
                        LocalDateTime.parse(resultSet.getString("last_update")),
                        resultSet.getString("order_status"),
                        resultSet.getString("delivery_address"),
                        resultSet.getString("customer_reference"),
                        resultSet.getBoolean("payment_approved")));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        } finally
        {
            closeAll(resultSet);
        }
        return cartList;
    }

    /**
     * @param customer String
     * @return cartList List<ShoppingCart>
     */
    @Override
    public List<ShoppingCart> findByReference(String customer)
    {
        List<ShoppingCart> cartList = new ArrayList<>();
        String selectQuery = "select * from cart where customer_reference = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {
            preparedStatement.setString(1, customer);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                cartList.add(new ShoppingCart(
                        resultSet.getInt("id"),
                        LocalDateTime.parse(resultSet.getString("last_update")),
                        resultSet.getString("order_status"),
                        resultSet.getString("delivery_address"),
                        resultSet.getString("customer_reference"),
                        resultSet.getBoolean("payment_approved")));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : " + e.getMessage());
        } catch (DBConnectionException e)
        {
            System.out.println("Database URL " + e.getJdbcURL() + " is not available.");
            System.out.println("Message: " + e.getMessage());
        } finally
        {
            closeAll(resultSet);
        }
        return cartList;
    }

    /**
     * @param id int
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws ObjectNotFoundException if result is a illegal value (0)
     */
    @Override
    public int delete(int id) throws ObjectNotFoundException
    {
        String deleteQuery = "delete from cart where id = ?";
        int result = 0;
        try(Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery))
        {
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();

            if (result == 0) throw new ObjectNotFoundException("row for cart by id " + id + " dose not exist");

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
