package com.database;


import java.sql.*;
import java.util.Scanner;

public class HotelManagement {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Arjun@0311";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("\n--- Hotel Management System ---");
                System.out.println("1. Reserve a room");
                System.out.println("2. View reservations");
                System.out.println("3. Search reservation by ID");
                System.out.println("4. Update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        reserveRoom(conn, sc);
                        break;
                    case 2:
                        viewReservations(conn);
                        break;
                    case 3:
                        searchReservationById(conn, sc);
                        break;
                    case 4:
                        updateReservation(conn, sc);
                        break;
                    case 5:
                        deleteReservation(conn, sc);
                        break;
                    case 6:
                        System.out.println("Exiting... Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void reserveRoom(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = sc.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = sc.nextInt();
            sc.nextLine(); 
            System.out.print("Enter contact number: ");
            String contactNumber = sc.nextLine();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, guestName);
                pstmt.setInt(2, roomNumber);
                pstmt.setString(3, contactNumber);
                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Room reserved successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection conn) {
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Current Reservations ---");
            System.out.printf("| %-14s | %-15s | %-13s | %-20s | %-19s |\n",
                    "Reservation ID", "Guest Name", "Room Number", "Contact Number", "Reservation Date");
            System.out.println("----------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getInt("room_number"),
                        rs.getString("contact_number"),
                        rs.getTimestamp("reservation_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchReservationById(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter reservation ID: ");
            int id = sc.nextInt();
            String sql = "SELECT * FROM reservations WHERE reservation_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("Reservation found:");
                    System.out.println("Guest Name: " + rs.getString("guest_name"));
                    System.out.println("Room Number: " + rs.getInt("room_number"));
                    System.out.println("Contact Number: " + rs.getString("contact_number"));
                    System.out.println("Reservation Date: " + rs.getTimestamp("reservation_date"));
                } else {
                    System.out.println("Reservation not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter reservation ID to update: ");
            int id = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter new guest name: ");
            String guestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int roomNumber = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new contact number: ");
            String contactNumber = sc.nextLine();

            String sql = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, guestName);
                pstmt.setInt(2, roomNumber);
                pstmt.setString(3, contactNumber);
                pstmt.setInt(4, id);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation ID not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int id = sc.nextInt();

            String sql = "DELETE FROM reservations WHERE reservation_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation ID not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
}