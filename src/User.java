import java.sql.*;
import java.util.Scanner;

public class User {
    // Database connection details
    final String URL = "jdbc:mysql://localhost:3306/BookStore";
    final String NAME = "root";
    final String PASS = "admin123";

    // Connection object
    private Connection con;

    Scanner sc = new Scanner(System.in);
    // Instance of Books class
    Books book = new Books();
    // Instance of Order class
    Order order = new Order();

    // Constructor to establish the connection
    public User(){
        try {
            con = DriverManager.getConnection(URL, NAME, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to display the menu after login
    public void afterLoginMenu() {
        boolean flag = true;

        do {
            System.out.println("1. Search Book");
            System.out.println("2. Buy a Book");
            System.out.println("3. Order History");
            System.out.println("4. Update Personal Details");
            System.out.println("5. Logout");
            System.out.print("Enter your option - ");

            int choice2 = sc.nextInt();
            sc.nextLine();

            switch (choice2) {
                case 1:
                    System.out.print("Enter the Book Name - ");
                    String bookName = sc.nextLine();
                    book.searchBook(bookName);
                    break;
                case 2:
                    System.out.print("Enter the Book ID - ");
                    int buyBook = sc.nextInt();
                    sc.nextLine();
                    order.buyBook(buyBook);
                    break;
                case 3:
                    order.orderHistory();
                    break;
                case 4:
                    updateInfoMenu();
                    break;
                case 5:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid input. Try again...");
            }
        }while (flag);
    }

    // Method to display the update information menu
    private void updateInfoMenu() {
        System.out.println("What do u want to update");
        System.out.println("1. Name");
        System.out.println("2. Phone No");
        System.out.println("3. Password");
        System.out.println("4. Email");
        System.out.println("Enter your choice - ");
        int ch = sc.nextInt();
        sc.nextLine();

        switch (ch){
            case 1:
                System.out.print("Enter new name: ");
                String newName = sc.nextLine();
                changeName(newName);
                break;

            case 2:
                System.out.print("Enter New Number: ");
                String newNumber = sc.nextLine();
                changePhoneNo(newNumber);
                break;
            case 3:
                System.out.print("Enter New Password:");
                String newPass = sc.nextLine();
                changePassword(newPass);
                break;
            case 4:
                System.out.print("Enter New Email:");
                String newEmail = sc.nextLine();
                changeEmail(newEmail);
                break;
            default:
                System.out.println("Invalid Input");
        }
    }

    // Method to change the user's password
    private void changePassword(String newPass) {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE user SET password = ? WHERE userID = ?")){
            pstmt.setString(1,newPass);
            pstmt.setInt(2, Login.getTempUserID());

            pstmt.executeUpdate();
            System.out.println("Password Changed Successfully");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to change the user's phone number
    private void changePhoneNo(String newNumber) {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE user SET phoneNO = ? WHERE userID = ?")){
            pstmt.setString(1, newNumber);
            pstmt.setInt(2,Login.getTempUserID());

            pstmt.executeUpdate();
            System.out.println("Number Changed Successfully");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to change the user's name
    private void changeName(String newName) {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE user SET name = ? WHERE userID = ?")){
            pstmt.setString(1, newName);
            pstmt.setInt(2,Login.getTempUserID());

            pstmt.executeUpdate();
            System.out.println("Name Changed Successfully");
        }catch (SQLException e){
            e.printStackTrace();
        }

        
    }

    // Method to change the user's email
    private void changeEmail(String newEmail) {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE user SET email = ? WHERE userID = ?")) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, Login.getTempUserID());

            pstmt.executeUpdate();
            System.out.println("Email Changed Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}