import java.sql.*;

public class Order {
    // Database connection details
    final String URL = "jdbc:mysql://localhost:3306/BookStore";
    final String NAME = "root";
    final String PASS = "admin123";
    private Connection con;

    // Constructor to establish the connection
    public Order(){
        try{
            con = DriverManager.getConnection(URL, NAME, PASS);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to buy a book
    public void buyBook(int buyBook) {
        int amount = 0;
        try (PreparedStatement pstmt = con.prepareStatement("SELECT * from Books WHERE  bookID = ?")) {
            pstmt.setInt(1, buyBook);

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();
            int quantity = 0;
            while (rs.next()) {
                quantity = rs.getInt("quantity");
                amount = rs.getInt("price");
            }
            // If the book is in stock, update the quantity and print the order details
                if (quantity > 0){
                --quantity;
                try (PreparedStatement pstmt2 = con.prepareStatement("UPDATE books SET quantity = ? WHERE bookID = ?")){
                    pstmt2.setInt(1,quantity);
                    pstmt2.setInt(2, buyBook);
                    pstmt2.executeUpdate();
                }
                System.out.println("\nYour order has been placed");
                System.out.println("Complete the Payment to get the confirmation");
                System.out.println("Thank you for shopping with us.\n");
                System.out.println("Payment Details - 9975761509 (UPI)\n");
                System.out.println("Total amount - " + amount);


            }else{
                System.out.println("We are out of stock, but your request is noted :)");
            }
            // Add the order to the history
            addToHistory(buyBook,amount);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to add an order to the history
    public void addToHistory(int bookID, int amount) {
        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO Orders (bookID, buyer, amount, status) VALUES (?,?,?,?)")){
            pstmt.setInt(1,bookID);
            pstmt.setInt(2, Login.getTempUserID());
            pstmt.setInt(3,amount);
            pstmt.setString(4, "PENDING");

            pstmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to display the order history
    public void orderHistory() {

        try (PreparedStatement pstmt = con.prepareStatement("SELECT Orders.orderID,Books.bookName,Orders.amount,Orders.status FROM Orders INNER JOIN Books ON Orders.bookID = Books.bookID WHERE Orders.buyer = ?")){
            pstmt.setInt(1, Login.getTempUserID());

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();

            // Flag to check if any orders were found
            boolean foundUser = false;
            System.out.printf("%-5s %-30s %-20s %-15s%n", "ID","Book Name", "Amount", "Status");

            while(rs.next()){
                foundUser = true;
                int orderID = rs.getInt("Orders.orderID");
                String bookName = rs.getString("Books.bookName");
                int amount = rs.getInt("Orders.amount");
                String status = rs.getString("Orders.status");

                // Print the order details
                System.out.printf("%-5d %-30s %-20d %-15s%n", orderID, bookName, amount, status);
            }
            // If no orders were found, print a message
            if (!foundUser){
                System.out.println("No books bought till now");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
