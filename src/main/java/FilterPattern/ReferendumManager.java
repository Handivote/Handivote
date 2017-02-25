package FilterPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class ReferendumManager {
    private static ArrayList <String> props;

    public static void main(String[] args) {
        String fName = "";
        if(args.length >0){
            fName = args[0];
        }
        else{
            fName = "test.properties";
        }

        readProperties(fName);
        ReferendumFactory rf = new ReferendumFactory();
        Referendum referendum = rf.createReferendum("simple");


    }

    private static void readProperties(String fName) {
        try {
            File file = new File(fName);
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
