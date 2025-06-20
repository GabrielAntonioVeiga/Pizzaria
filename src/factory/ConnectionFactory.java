package factory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {
    private static final String PROPERTIES_FILE = "/db.properties";

    public static Connection getConnection() throws Exception {
        Properties props = new Properties();
        InputStream input = ConnectionFactory.class.getResourceAsStream("/db.properties");
        if (input == null) {
            throw new Exception("Arquivo de configuração não encontrado: " + PROPERTIES_FILE);
        }
        props.load(input);

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}
