package main;
import java.sql.*;

public class MainClass {

    public static void main(String[] args)
    {
        Connection c = null;
        Statement stmnt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Health.db");
            c.setAutoCommit(false);

            stmnt = c.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS SAVE_DATA " +
                    "(SAVE_ID INT PRIMARY KEY NOT NULL," +
                    " HEALTH INT NOT NULL)";
            stmnt.execute(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        new Game(c);
    }
}
