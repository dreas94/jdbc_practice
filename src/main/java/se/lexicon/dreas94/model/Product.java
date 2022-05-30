package se.lexicon.dreas94.model;

import java.util.Objects;

public class Product
{
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price)
    {
        setId(id);
        setName(name);
        setPrice(price);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    @Override
    public String toString()
    {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
