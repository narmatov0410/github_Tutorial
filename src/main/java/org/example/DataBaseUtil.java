package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtil {
    public static void createTableIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS task (" +
                    "id SERIAL PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "content TEXT," +
                    "status VARCHAR(20) NOT NULL," +
                    "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "finished_date TIMESTAMP)";
            statement.execute(createTableSQL);
        }
    }

}
