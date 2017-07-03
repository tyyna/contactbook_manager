package fi.muni.cz.contacts;

import org.apache.derby.jdbc.EmbeddedDataSource;
//import javafx.util.Pair;
import org.assertj.core.util.ArrayWrapperList;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;

/**
 * @author Vratislav Bendel
 * @version 3/20/17
 */

public class DBUtils {

    private static final String databaseUriString = "";
    
    public static DataSource getMemoryEmbeddedDataSource() {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:contactbook-DB");
        ds.setCreateDatabase("create");
        return ds;
    }
    
    public static DataSource prepareEmbeddedDatabaseHome() {
        Path home = Paths.get(System.getProperty("user.home"));
        Path databaseName = home.resolve("ContactBookDatabase");
        
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName(databaseName.toString());
        ds.setCreateDatabase("create");
        return ds;
           
    }
    
    public static DataSource prepareEmbeddedDatabaseUri() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName(databaseUriString);
        ds.setCreateDatabase("create");
        return ds;
           
    }
        
    public static void executeSqlScript(DataSource ds, String filePath) throws SQLException {
        //Connection conn = null;
        List<String> commands = getLinesFromFile(filePath);
        try (Connection conn = ds.getConnection()){
            try (Statement st = conn.createStatement()) {
                for (String statement : commands) {
                    if (!statement.isEmpty()) {
                        st.execute(statement);
                    }
                }
            }
        }
    }

    public static void executeSqlScript(DataSource ds, URL scriptUrl) throws SQLException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for (String sqlStatement : readSqlStatements(scriptUrl)) {
                if (!sqlStatement.trim().isEmpty()) {
                    conn.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        } finally {
            conn.rollback();
            conn.close();
        }
}

    private static String[] readSqlStatements(URL url) {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            while (true) {
                int count = reader.read(buffer);
                if (count < 0) {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read " + url, ex);
        }
    }

    private static List<String> getLinesFromFile(String filePath) {
        List<String> result = new ArrayList<>();
        try (InputStream fis = new FileInputStream(filePath)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }
        catch (IOException ex) {
            System.err.print(ex.getMessage());
        }


        return result;
    }
}
