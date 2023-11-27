package org.example;

import java.sql.*;
import java.util.Scanner;

import static org.example.DataBaseUtil.createTableIfNotExists;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/jdbc_user";
    private static final String DB_USER = "jdbc_user";
    private static final String DB_PASSWORD = "444444";

    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            createTableIfNotExists(connection);
            TaskRepository taskRepository = new TaskRepository(connection);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n***** Menu *****");
                System.out.println("1-Create");
                System.out.println("2-Active Task List");
                System.out.println("3-Finished Task List");
                System.out.println("4-Update (by id)");
                System.out.println("5-Delete by id");
                System.out.println("6-Mark as Done");
                System.out.println("0-Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> createTask(scanner, taskRepository);
                    case 2 -> listTasksByStatus(TaskStatus.ACTIVE, connection);
                    case 3 -> listTasksByStatus(TaskStatus.DONE, connection);
                    case 4 -> updateTask(scanner, connection);
                    case 5 -> deleteTask(scanner, connection);
                    case 6 -> markAsDone(scanner, connection);
                    case 0 -> {
                        connection.close();
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
} catch (SQLException e) {
            throw new RuntimeException("Bog`lanishda hatolik yuz berdi! ", e);
        }
    }

    public static void createTask(Scanner scanner, TaskRepository taskRepository)  {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter content: ");
        String content = scanner.nextLine();

        TaskDTO dto = new TaskDTO();
        dto.setTitle(title);
        dto.setContent(content);

        taskRepository.saveTask(dto);
    }

    private static void listTasksByStatus(TaskStatus status, Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM task WHERE status = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, status.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                displayTasks(resultSet);
            }
        }
    }

    private static void displayTasks(ResultSet resultSet) throws SQLException {
        System.out.println("ID\tTitle\t\tContent\t\tStatus\tCreated Date\tFinished Date");
        while (resultSet.next()) {
            System.out.printf("%d\t%s\t\t%s\t\t%s\t%s\t%s\n",
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("content"),
                    resultSet.getString("status"),
                    resultSet.getTimestamp("created_date"),

                    resultSet.getTimestamp("finished_date"));
        }
    }

    private static void updateTask(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter Task Id: ");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Content: ");
        String content = scanner.nextLine();

        String updateSQL = "UPDATE task SET title = ?, content = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.setInt(3, taskId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task updated successfully.");
            } else {
                System.out.println("Task not found with the given ID.");
            }
        }
    }

    private static void deleteTask(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter Delete Task Id: ");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String deleteSQL = "DELETE FROM task WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, taskId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Task not found with the given ID.");
            }
        }
    }

    private static void markAsDone(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter Task Id To Mark it as Done: ");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String updateStatusSQL = "UPDATE task SET status = ?, finished_date = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatusSQL)) {
            preparedStatement.setString(1, TaskStatus.DONE.name());
            preparedStatement.setInt(2, taskId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task marked as done successfully.");
            } else {
                System.out.println("Task not found with the given ID.");
            }
        }
    }
}


