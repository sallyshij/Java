package administration.application;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;


public class JDBC {

    Connection connection;
    Statement stmt;
    // This is the user id of the user who is currently logged in.
    int id;
    String title;
    String name;
     
    public int getIDFromTitle(String title) {
        String sqlStr = "SELECT iditem from item where title =\'" + title + "\'";
        int idItem = 0;
        try {
            ResultSet rset = stmt.executeQuery(sqlStr);
            if (rset.next()) {
                idItem = rset.getInt(1);
            }
        } catch (SQLException ex) {
            idItem = 0;
        }
        return idItem;
    }
    
    public int getIDFromName(String name) {
        String sqlStr = "SELECT idpatron from patron where name =\'" + name + "\'";
        int idPatron = 0;
        try {
            ResultSet rset = stmt.executeQuery(sqlStr);
            if (rset.next()) {
                idPatron = rset.getInt(1);
            }
        } catch (SQLException ex) {
                idPatron = 0;
        }
        return idPatron;
    }

    String newItemSQL = "insert into item(title, cost, kind) VALUES (?, ?, ?); ";
    PreparedStatement newItemStmt;
    public void createNewItem(String title, double cost, String kind) {
        connect();
        try {
            newItemStmt.setString(1, title);
            newItemStmt.setDouble(2, cost);
            newItemStmt.setString(3, kind);
            newItemStmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.print("Error in creatingNewItem");
        }
    }

    String newPatronSQL = "insert into patron(name, email) VALUES (?, ?); ";
    PreparedStatement newPatronStmt;
    public void createNewPatron(String name, String email) {
        connect();
        try {
            newPatronStmt.setString(1, name);
            newPatronStmt.setString(2, email);
            newPatronStmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.print("Error in creating new patron.");
        }
    }
    
    public String saveOverDue(PrintWriter output){
        String sqlStr = "SELECT item from borrow INNER JOIN item ON borrow.item=item.iditem WHERE datediff(curdate(),borrow.due)>14 AND item.kind='B';";
        String sqlStr1 ="SELECT item from borrow INNER JOIN item ON borrow.item=item.iditem WHERE datediff(curdate(),borrow.due)>7 AND item.kind='V';";
        String overdue = null;
        try{
            ResultSet rset = stmt.executeQuery(sqlStr);
            while(rset.next()) {
                // Read from the result set
                overdue = rset.getString(1);
            }
            ResultSet rset1 = stmt.executeQuery(sqlStr1);
            while(rset1.next()){
                //Read from the result set
                overdue = rset1.getString(1);
            }
        } catch (SQLException ex) {
            showMessageDialog("An SQL error took place." + ex.getMessage());
        }  
        return overdue;
    }
    
    public void calculateFines(){
        String sqlStra="UPDATE borrow INNER JOIN item ON borrow.item=item.iditem\n" +
                        "SET borrow.pending = datediff(curdate(),borrow.due)*0.5 - borrow.fines\n" +
                        "WHERE item.kind='B' AND borrow.due < curdate();";
        String sqlStrb="UPDATE borrow INNER JOIN item ON borrow.item=item.iditem\n" +
                        "SET borrow.pending = datediff(curdate(),borrow.due) - borrow.fines\n" +
                        "WHERE item.kind='V' AND borrow.due < curdate();";
        String sqlStr = "UPDATE borrow SET fines=fines+pending WHERE pending > 0;"; 
        String sqlStr1 = "UPDATE borrow SET pending=0.0 WHERE pending > 0;";
        String sqlStr2 = "UPDATE borrow SET pending=0.0 WHERE fines+pending = 5.0;";
        String sqlStr3 = "UPDATE borrow SET fines = 5.0 WHERE fines+pending = 5.0;";
        try{
            stmt.executeUpdate(sqlStra);
            stmt.executeUpdate(sqlStrb);
            stmt.executeUpdate(sqlStr);
            stmt.executeUpdate(sqlStr1);
            stmt.executeUpdate(sqlStr2);
            stmt.executeUpdate(sqlStr3);
        } catch (SQLException ex) {
            showMessageDialog("An SQL error took place." + ex.getMessage());
        }
    }
    
    public void remove(int id){
        try {
            String sqlStr = "delete from item where iditem =" + id;
            String sqlStr1 = "delete from borrow where item =" + id;
            stmt.execute(sqlStr);
            stmt.execute(sqlStr1);
        } catch (SQLException ex) {
            showMessageDialog("An SQL error took place." + ex.getMessage());
        }
    }
    
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            showMessageDialog("Unable to load JDBC driver. Application will exit.");
            System.exit(0);
        }
        // Establish a connection
        try {
            String url = "jdbc:mysql://localhost:3306/library?user=root&password=cmsc250";
            connection = DriverManager.getConnection(url);
            stmt = connection.createStatement();
            newPatronStmt = connection.prepareStatement(newPatronSQL);
            newItemStmt = connection.prepareStatement(newItemSQL);
            
        } catch (SQLException ex) {
            showMessageDialog("Unable to connect to database. Application will exit.");
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    private void showMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
 
}


