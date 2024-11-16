import java.sql.*;

public class Books {
    // Database connection details
    final String URL = "jdbc:mysql://localhost:3306/BookStore";
    final String NAME = "root";
    final String PASS = "admin123"; 

    // Connection object
    private Connection con;

    // Constructor to establish the connection
    public Books(){
        try {
            con = DriverManager.getConnection(URL, NAME, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for books by name, author, or genre
    public void searchBook(String bookName) {
        try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM books WHERE bookName LIKE ? OR author LIKE ? OR genre LIKE ?")){
            pstmt.setString(1,"%" + bookName + "%");
            pstmt.setString(2,"%" + bookName + "%");
            pstmt.setString(3,"%" + bookName + "%");

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();

            // Flag to check if any books were found
            boolean foundBooks = false;
            System.out.printf("%-5s %-30s %-20s %-15s %-10s %-10s%n", "ID", "Name", "Author", "Genre", "Price", "Quantity");
            while (rs.next()){
                foundBooks = true;
                int bookID =  rs.getInt("bookID");
                String name = rs.getString("bookName");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int  price = rs.getInt("price");
                int quantity = rs.getInt("quantity");

                // Print the book details
                System.out.printf("%-5d %-30s %-20s %-15s %-10d %-10d%n", bookID, name, author, genre, price, quantity);
            }
            // If no books were found, print a message
            if (!foundBooks){
                System.out.println("No Book found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}