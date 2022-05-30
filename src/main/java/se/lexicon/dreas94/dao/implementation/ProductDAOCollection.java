package se.lexicon.dreas94.dao.implementation;

import se.lexicon.dreas94.dao.ProductDAO;
import se.lexicon.dreas94.db.MySQLConnection;
import se.lexicon.dreas94.exception.DBConnectionException;
import se.lexicon.dreas94.exception.ObjectNotFoundException;
import se.lexicon.dreas94.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOCollection implements ProductDAO
{
    /**
     * Saves a Product in SQL
     * @param product Product
     * @return product Product
     */
    @Override
    public Product save(Product product)
    {
        String insertQuery = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet keySet = null;

        if(product.getId() == 0)
        {
            insertQuery = "insert into product (name, price) values (?,?)";
        }
        else if(product.getId() < 0)
        {
            insertQuery = "update product set name = ?, price = ? where id = ?";
        }

        try
        {
            connection = MySQLConnection.getConnection();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            if(product.getId() > 0) preparedStatement.setInt(3, product.getId());

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());

            int result = preparedStatement.executeUpdate();
            keySet = preparedStatement.getGeneratedKeys();

            System.out.println("Save operation is done successfully");

            if(keySet.next())
            {
                int generatedId = keySet.getInt(1);
                product.setId(generatedId);
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

        return product;
    }

    /**
     * Finds the data for a saved Product in SQL.
     * @param id int
     * @return Optional.ofNullable(result) Optional<Product>
     */
    @Override
    public Optional<Product> findById(int id)
    {
        Product result = null;
        String selectQuery = "select * from product where id = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                result = new Product(resultSet.getInt("id"), resultSet.getString("name"),resultSet.getDouble("price"));
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
     * @return List<Product>
     */
    @Override
    public List<Product> findAll()
    {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "select * from product";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                productList.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")));
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
        return productList;
    }

    /**
     * @param name String
     * @return List<Product>
     */
    @Override
    public List<Product> findByName(String name)
    {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "select * from product where name = ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                productList.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")));
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
        return productList;
    }

    /**
     * @param low int
     * @param high int
     * @return List<Product>
     */
    @Override
    public List<Product> findByPriceBetween(int low, int high)
    {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "select * from product where between ? and ?";
        ResultSet resultSet = null;

        try(Connection connection= MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery))
        {
            preparedStatement.setInt(1, low);
            preparedStatement.setInt(1, high);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                productList.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")));
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
        return productList;
    }

    /**
     * @param id int
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws ObjectNotFoundException if result is a illegal value (0)
     */
    @Override
    public int delete(int id) throws ObjectNotFoundException
    {
        String deleteQuery = "delete from product where id = ?";
        int result = 0;
        try(Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery))
        {
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();

            if (result == 0) throw new ObjectNotFoundException("row for product by id " + id + " dose not exist");

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
