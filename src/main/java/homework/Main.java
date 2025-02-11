package homework;

import java.sql.*;
import java.util.Scanner;


public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/apart?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "password1";

    static Connection connection;

    public static void main(String[] args) {
        try {

            // Establish the connection
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            if (connection != null) {
                System.out.println("Connected to the database successfully");
            }

            createTable();
            showUserInterface();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS apart (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "district VARCHAR(50) NOT NULL, " +
                "address VARCHAR(100) NOT NULL, " +
                "area DECIMAL(5,2) NOT NULL, " +
                "rooms INT NOT NULL, " +
                "price DECIMAL(10,2) NOT NULL" +
                ")";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(createTableSQL);
    }

    private static void showUserInterface() {
        try {
            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Show all apartments");
                System.out.println("2. Search by address");
                System.out.println("3. Search by room count");
                System.out.println("4. Fill the table with 5 sample entries");
                System.out.println("5. Clear all entries");
                System.out.println("0. Exit");

                int choice = readIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        showAllApartments();
                        break;
                    case 2:
                        searchByAddress();
                        break;
                    case 3:
                        searchByRoomCount();
                        break;
                    case 4:
                        fillTableWithSampleEntries();
                        break;
                    case 5:
                        clearEntries();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void fillTableWithSampleEntries() throws SQLException {
        String insertSQL = "INSERT INTO apart (district, address, area, rooms, price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        String[] districts = {"Downtown", "Suburbs", "West End", "East Side", "City Center"};
        String[] addresses = {"123 Main St", "456 Elm St", "789 Maple Ave", "101 Oak Rd", "202 Pine St"};
        double[] areas = {120.5, 80.0, 150.2, 100.0, 200.0};
        int[] rooms = {3, 2, 4, 2, 5};
        double[] prices = {100000.0, 150000.0, 250000.0, 180000.0, 350000.0};

        for (int i = 0; i < districts.length; i++) {
            preparedStatement.setString(1, districts[i]);
            preparedStatement.setString(2, addresses[i]);
            preparedStatement.setDouble(3, areas[i]);
            preparedStatement.setInt(4, rooms[i]);
            preparedStatement.setDouble(5, prices[i]);
            preparedStatement.executeUpdate();
        }

        System.out.println("Filled the table 'apart' with 5 sample entries.");
    }

    private static void clearEntries() throws SQLException {
        String clearSQL = "DELETE FROM apart";
        Statement statement = connection.createStatement();
        int deletedRows = statement.executeUpdate(clearSQL);
        String resetPrimaryKeySQL = "ALTER TABLE apart AUTO_INCREMENT = 1";
        statement.executeUpdate(resetPrimaryKeySQL);
        System.out.println("Deleted " + deletedRows + " entries from the table 'apart'.");
    }

    private static int readIntInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextInt();
    }

    private static String readStringInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
    }

    private static void showAllApartments() throws SQLException {
        String selectAllSQL = "SELECT * FROM apart";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectAllSQL);

        System.out.println("\nAll Apartments:");
        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id"));
            System.out.println("District: " + resultSet.getString("district"));
            System.out.println("Address: " + resultSet.getString("address"));
            System.out.println("Area: " + resultSet.getDouble("area"));
            System.out.println("Rooms: " + resultSet.getInt("rooms"));
            System.out.println("Price: " + resultSet.getDouble("price"));
            System.out.println("----------------------");
        }
    }

    private static void searchByAddress() throws SQLException {
        String searchAddress = readStringInput("Enter the address to search for: ");

        String searchSQL = "SELECT * FROM apart WHERE address LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);
        preparedStatement.setString(1, "%" + searchAddress + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("\nApartments with address containing '" + searchAddress + "':");
        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id"));
            System.out.println("District: " + resultSet.getString("district"));
            System.out.println("Address: " + resultSet.getString("address"));
            System.out.println("Area: " + resultSet.getDouble("area"));
            System.out.println("Rooms: " + resultSet.getInt("rooms"));
            System.out.println("Price: " + resultSet.getDouble("price"));
            System.out.println("----------------------");
        }
    }

    private static void searchByRoomCount() throws SQLException {
        int roomCount = readIntInput("Enter the room count to search for: ");

        String searchSQL = "SELECT * FROM apart WHERE rooms = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);
        preparedStatement.setInt(1, roomCount);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("\nApartments with " + roomCount + " rooms:");
        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id"));
            System.out.println("District: " + resultSet.getString("district"));
            System.out.println("Address: " + resultSet.getString("address"));
            System.out.println("Area: " + resultSet.getDouble("area"));
            System.out.println("Rooms: " + resultSet.getInt("rooms"));
            System.out.println("Price: " + resultSet.getDouble("price"));
            System.out.println("----------------------");
        }
    }
}