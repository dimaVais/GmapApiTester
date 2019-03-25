import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.*;
import java.util.ArrayList;

public class SqlDataGetter
{
    private Connection con;
    private ArrayList<Triple<String,String,String>> allDbData = new ArrayList<Triple<String,String,String>>();

    private static String connUrl = "jdbc:mysql://remotemysql.com:3306";
    private static String dbUserName = "KfI7Tv6sc6";
    private static String dbPass = "OeZzYIn4Va";
    private static String dbSchemAndTablePlaces = "KfI7Tv6sc6.Places";
    private static String dbSchemAndTableResulr = "KfI7Tv6sc6.test_result";
    private static String forNameSql = "com.mysql.jdbc.Driver";
    private static String selectStatPlaces = "SELECT * FROM "+dbSchemAndTablePlaces+";";
    private static String insertStatResult = "INSERT " +dbSchemAndTableResulr+" VALUES ";
    private static String urlColumnName = "url";
    private static String searchColumnName = "search";
    private static String resultColumn = "result";

    //Connects to the Remote SQL DB, with set user and password.
    public void connectToDB()
    {
        Reports.reporter.info(Reports.FUNCTION_START_MSG + new Throwable().getStackTrace()[0].getMethodName());
        try {
            Class.forName(forNameSql).newInstance();
            con = DriverManager.getConnection(connUrl, dbUserName, dbPass);
            Reports.reporter.info(Reports.CONNECTION_DONE_WELL_MSG  + connUrl);
        }
        catch (Exception ex) {
            Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);
            closeDbObjects();
        }
        Reports.reporter.info(Reports.FUNCTION_COMPETE_MSG + new Throwable().getStackTrace()[0].getMethodName());
    }

    //Reads the table of URL, search creteria and expected result from the remote SQL DB.
    public void readDBTable()
    {
        Reports.reporter.info(Reports.FUNCTION_START_MSG + new Throwable().getStackTrace()[0].getMethodName());
        Statement  stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            String statementToExecute = selectStatPlaces;
            rs = stmt.executeQuery(statementToExecute);
            while (rs.next()) {
                String url = rs.getString(urlColumnName);
                Reports.reporter.pass(Reports.DATA_GET_FROM_DB_MSG+url);
                String srch = rs.getString(searchColumnName);
                Reports.reporter.pass(Reports.DATA_GET_FROM_DB_MSG+srch);
                String result = rs.getString(resultColumn);
                Reports.reporter.pass(Reports.DATA_GET_FROM_DB_MSG+result);
                this.allDbData.add(Triple.of(url, srch, result));
            }
            Reports.reporter.info(Reports.FUNCTION_COMPETE_MSG + new Throwable().getStackTrace()[0].getMethodName());
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
        finally{closeDbObjects(rs,stmt);}
    }

    //Writes test status into table at rhe remote SQL DB.
    public void writeStatusToDb(String res, String status)
    {
        Reports.reporter.info(Reports.FUNCTION_START_MSG + new Throwable().getStackTrace()[0].getMethodName());
        Statement  stmt = null;
        try {
            stmt = con.createStatement();
            String statementToExecute = insertStatResult+insertStrGenerator(res,status);
            stmt.executeUpdate(statementToExecute);
            Reports.reporter.pass(Reports.DATA_INSERT_MSG+res+"_"+status);
            Reports.reporter.info(Reports.FUNCTION_COMPETE_MSG + new Throwable().getStackTrace()[0].getMethodName());
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
        finally{closeDbObjects(stmt);}
    }

    //Sets part of the SQL query for status write to the remote SQL DB.
    private String insertStrGenerator(String res, String status)
    {
        return "(\'"+res+"\',\'"+status+"\');";
    }


    // Close SQL objects for connection, statement and result set.
    private void closeDbObjects( ResultSet rs, Statement stmt)
    {
        try{
           if(rs!=null){ rs.close();}
           if(stmt!=null){ stmt.close();}
           this.con.close();
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
    }

    // Close SQL objects for connection and statement.
    private void closeDbObjects(Statement stmt)
    {
        try{
            if(stmt!=null){ stmt.close();}
            this.con.close();
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
    }

    // Close SQL objects for connection only.
    private void closeDbObjects()
    {
        try{
            this.con.close();
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
    }

    //Getter for result collection
    public ArrayList<Triple<String, String, String>> getAllDbData() {
        return allDbData;
    }

    //Setter for result collection
    public void setAllDbData(ArrayList<Triple<String, String, String>> allDbData) {
        this.allDbData = allDbData;
    }
}
