package service;

import model.Core;

import java.sql.*;

import java.util.List;

public abstract class JDBCGeneric {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/DBProjectManagementSystem";

    private static final String USER = "mysql";
    private static final String PASSWORD = "mysql";

    protected Connection connect() throws SQLException{

        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        return connection;

    }

}
