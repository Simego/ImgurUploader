package com.thesimego.imguruploader.dao;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Simego
 */
public class Connection {

    private static java.sql.Connection connection = null;

    public static Statement openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:imguruploader.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS links (\n"
                    + "  id INTEGER NOT NULL ON CONFLICT ROLLBACK PRIMARY KEY AUTOINCREMENT, \n"
                    + "  link VARCHAR(64) NOT NULL ON CONFLICT ROLLBACK, \n"
                    + "  description VARCHAR(24), \n"
                    + "  date VARCHAR(24) NOT NULL ON CONFLICT ROLLBACK);"
            );

            return statement;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

}
