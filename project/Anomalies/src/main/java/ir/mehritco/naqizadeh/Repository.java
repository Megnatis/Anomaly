package ir.mehritco.naqizadeh;

import java.sql.*;
import java.util.ArrayList;

public class Repository {

    public Connection connection ;
    public Repository(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Error in Class DataBase : " + e.getMessage());
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\ProjectSpace\\Applications\\payanname\\Repository\\Anomalies.db");
        }catch (SQLException ex){
            System.out.println("In connection or url we have an Error/Ex : " + ex.getMessage());
        }
    }
    private Connection getConnection() throws SQLException {
        if(connection.isClosed()){
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\ProjectSpace\\Applications\\payanname\\Repository\\Anomalies.db");
        }
        return connection;
    }

    public void storeRawData(ArrayList<Notification> rawData) throws SQLException {
        removeOldData("Notification");
        String insertData = """
                INSERT INTO Notification (id , title , fatitle , jalalidate , notifTime) Values (?,?,?,?,?);
                """;
        connection =  getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(insertData);
        for (Notification notif: rawData) {
            preparedStatement.setLong(1,notif.getId());
            preparedStatement.setString(2 , notif.getTitle());
            preparedStatement.setString(3 , notif.getFaTitle());
            preparedStatement.setString(4 , notif.getJalaliDate());
            preparedStatement.setLong(5 , notif.getNotifTime().getTime());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.commit();
        connection.close();
    }

    public void storeNormalized(ArrayList<Normalized> normalizeds) throws SQLException{
        removeOldData("Normalized");
        String insertData = """
                INSERT INTO Normalized (id , sen , lem , tree ) Values (?,?,?,?);
                """;
        connection =  getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(insertData);
        for (Normalized normalized: normalizeds) {
            preparedStatement.setLong(1,normalized.getId());
            preparedStatement.setString(2 , normalized.getSen());
            preparedStatement.setString(3 , normalized.getLem());
            preparedStatement.setString(4 , normalized.getTree());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.commit();
        connection.close();
    }
    public void storeCategorized(ArrayList<Normalized> normalizeds) throws SQLException{
        removeOldData("Categorized");
        String insertData = """
                INSERT INTO Categorized (id , sen , lem , tree ) Values (?,?,?,?);
                """;
        connection =  getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(insertData);
        for (Normalized normalized: normalizeds) {
            preparedStatement.setLong(1,normalized.getId());
            preparedStatement.setString(2 , normalized.getSen());
            preparedStatement.setString(3 , normalized.getLem());
            preparedStatement.setString(4 , normalized.getTree());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.commit();
        connection.close();
    }
    public ArrayList<Normalized> getNormalized() throws SQLException{
        ArrayList<Normalized> normalizeds = new ArrayList<Normalized>();
       connection = getConnection();
       String select = "SELECT * FROM Normalized;";
       Statement statement = connection.createStatement();
       ResultSet resultSet = statement.executeQuery(select);
       while (resultSet.next()){
           Normalized normalized = new Normalized();
           normalized.setId(resultSet.getLong("id"));
           normalized.setLem(resultSet.getString("lem"));
           normalized.setSen(resultSet.getString("sen"));
           normalized.setTree(resultSet.getString("tree"));
           normalizeds.add(normalized);
       }
       return normalizeds;
    }

    public void removeOldData(String tablename){
        try {
            connection =  getConnection();
            String query = "DELETE FROM " + tablename;
            connection.createStatement().execute(query);
            System.out.println("Removed old data from table : " + tablename);
        }catch (SQLException ex){
            System.err.println("Can't Remove old Data from table : " + tablename + " The error/Ex said : " + ex.getMessage());
        }
    }

}
