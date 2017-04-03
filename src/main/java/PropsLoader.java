import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropsLoader {
    static Properties getProperties(String fName) {
        Properties properties = new Properties();

        File file = new File(fName);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return properties;
    }
}
