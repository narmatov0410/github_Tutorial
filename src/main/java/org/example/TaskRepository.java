package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskRepository {
    private Connection connection;

    public TaskRepository(Connection connection) {
        this.connection = connection;

    }

    public void saveTask(TaskDTO dto) {
        String insertSQL = "INSERT INTO task (title, content, status) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, dto.getTitle());
            preparedStatement.setString(2, dto.getContent());
            preparedStatement.setString(3, TaskStatus.ACTIVE.name());

            preparedStatement.executeUpdate();
            System.out.println("Task created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
