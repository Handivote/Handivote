package FilterPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class ReferendumManager {
    public static void main(String[] args) {
        ReferendumFactory rf = new ReferendumFactory();
        try {
            File file = new File("test.properties");
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            String pattern = properties.getProperty("dates.pattern");
            String startDate = properties.getProperty("startDate");
            String endDate = properties.getProperty("endDate");
            setDates(pattern, startDate, endDate);
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                System.out.println(key + ": " + value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setDates(String pattern, String startDate, String endDate) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date sDate = format.parse(startDate);
            Date eDate = format.parse(endDate);
            System.out.println(sDate + " Dates " + eDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
