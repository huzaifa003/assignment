package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String url
                = "jdbc:mysql://localhost:3306/"; // table details
        String username = "root"; // MySQL credentials
        String password = "";
//        String query
//                = "select *from students"; // query to be run
        Class.forName(
                "com.mysql.cj.jdbc.Driver"); // Driver name
        Connection con = DriverManager.getConnection(
                url, username, password);
        System.out.println(con);
        System.out.println(
                "Connection Established successfully");
//        Statement st = con.createStatement();
//        ResultSet rs
//                = st.executeQuery(query); // Execute query
//        rs.next();
//        String name
//                = rs.getString("name"); // Retrieve name from db
//
//        System.out.println(name); // Print result on console
//        st.close(); // close statement
        return con;
//        con.close(); // close connection
//        System.out.println("Connection Closed....");
    }

    public void closeConnection(Connection con) throws SQLException {
        con.close();
        System.out.println("Connection Closed Succesfully");
    }
}
