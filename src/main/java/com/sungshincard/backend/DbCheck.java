package com.sungshincard.backend;

import java.sql.*;

public class DbCheck {
    public static void main(String[] args) {
        String url = "jdbc:h2:file:./src/main/resources/db/pokemon_card"; // Based on usual H2 path
        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM card_master");
            if (rs.next()) {
                System.out.println("Card count: " + rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
