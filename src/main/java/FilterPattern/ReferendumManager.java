package FilterPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReferendumManager {
    private static Referendum referendum;

    public static void main(String[] args) {
        ReferendumFactory rf = new ReferendumFactory();
        String fName = "";
        if (args.length > 0) {
            fName = args[0];
        } else {
            fName = "test.properties";
        }

        File file = new File(fName);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            System.out.println(properties.stringPropertyNames());
            referendum = rf.buildReferendum(properties);
            fileInput.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
