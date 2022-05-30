package se.lexicon.dreas94.exception;

public class DBConnectionException extends Exception {
    private final String jdbcURL;

    public DBConnectionException(String message, String jdbcURL)
    {
        super(message);
        this.jdbcURL = jdbcURL;
    }

    public String getJdbcURL()
    {
        return jdbcURL;
    }
}

