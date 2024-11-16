import java.sql.*;
import java.util.Scanner;

public class Login {
    // Database connection details  
    final String URL = "jdbc:mysql://localhost:3306/BookStore";
    final String NAME = "root";
    final String PASS = "admin123";
    // Temporary variables to store user information
    String tempUsername;
    static int tempUserID;
    boolean flagLogin;
    private Connection con;
    Scanner sc = new Scanner(System.in);
    User user = new User();

    // Constructor to establish the connection
    public Login() {
        try {
            con = DriverManager.getConnection(URL, NAME, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display the first menu
    public void firstMenu(){
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("MENU");
            System.out.println("1. Login");
            System.out.println("2. New Account");
            System.out.println("3. Delete Account");
            System.out.println("4. Quit");
            System.out.print("Enter your choice - ");

            int choice1 = sc.nextInt();
            sc.nextLine();

            switch (choice1){
                case 1:
                    authenticateUser();
                    if (flagLogin){
                        user.afterLoginMenu();
                    }
                    break;
                case 2:
                    newAccount();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    return;
            }
        }
    }

    // Method to authenticate the user
    public void authenticateUser() {
        try (PreparedStatement pstmt = con.prepareStatement("SELECT password, userID FROM user WHERE email = ?")) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Username - ");
            String username = sc.nextLine();
            System.out.print("Password - ");
            String pass = sc.nextLine();
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {

                String gotPass = rs.getString("password");
                int gotUserID = rs.getInt("userID");
                if (gotPass.equals(pass)) {
                    System.out.println("Login Successful");
                    flagLogin = true;
                    tempUsername = username;
                    tempUserID = gotUserID;

                } else {
                    System.out.println("Invalid Password");

                }
            }else{
                System.out.println("No user found");
                flagLogin = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get the temporary user ID
    public static int getTempUserID() {
        return tempUserID;
    }
    // Method to delete the user account
    public void deleteAccount() {
        System.out.print("Enter Email - ");
        String email = sc.nextLine();
        try(PreparedStatement pstmt = con.prepareStatement("DELETE FROM user WHERE email=?")){
            pstmt.setString(1,email);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Data deleted");
            } else {
                System.out.println("Not deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to create a new account
    public void newAccount() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Phone: ");
        String phoneno = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO user (name, email, phoneno, password) values (?,?,?,?)")){
            pstmt.setString(1, name);
            pstmt.setString(2,email);
            pstmt.setString(3,phoneno);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            System.out.println("User added");
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}