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
            String startDateStr = properties.getProperty("startDate");
            String endDateStr = properties.getProperty("endDate");
            String refType = properties.getProperty("refType");
            Question[] questions;
            for ( int i =0; i < new Integer(properties.getProperty("numberOfQuestions"));i++){
                ArrayList <QuestionOption> options = new ArrayList<>();
                for(int j = 1; j<= new Integer(properties.getProperty("numberOfOptions")); j++){
                    options.add(new QuestionOption(""+ j ,"" + j,  properties.getProperty("questionOption"+ j)));
                }
                Question question = new Question(1, 0, properties.getProperty("question1"), options );
                //questions[i]=
                System.out.println(question.toString());
            }
            Date startDate = setDates(pattern, startDateStr);
            Date endDate = setDates(pattern, endDateStr);
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                //System.out.println(key + ": " + value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Date setDates(String pattern, String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
