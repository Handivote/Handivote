package FilterPattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class ReferendumFactory {

     void readProperties(Properties properties) {
         String pattern = properties.getProperty("dates.pattern");
         String startDateStr = properties.getProperty("startDate");
         String endDateStr = properties.getProperty("endDate");
         String refType = properties.getProperty("refType");
         Question[] questions;
         for ( int i =0; i < new Integer(properties.getProperty("numberOfQuestions"));i++){
             ArrayList<QuestionOption> options = new ArrayList<>();
             for(int j = 1; j<= new Integer(properties.getProperty("numberOfOptions")); j++){
                 options.add(new QuestionOption(""+ j ,"" + j,  properties.getProperty("questionOption"+ j)));
             }
             Question question = new Question(1, 0, properties.getProperty("question1"), options );
             //questions[i]=
             //System.out.println(question.toString());
         }
         Date startDate = setDates(pattern, startDateStr);
         Date endDate = setDates(pattern, endDateStr);
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

    public Referendum createReferendum(Properties properties){
        String pattern = properties.getProperty("dates.pattern");
        String startDateStr = properties.getProperty("startDate");
        String endDateStr = properties.getProperty("endDate");
        Date startDate = setDates(pattern, startDateStr);
        Date endDate = setDates(pattern, endDateStr);

        String refType = properties.getProperty("refType");
        Question[] questions;
        for ( int i =0; i < new Integer(properties.getProperty("numberOfQuestions"));i++){
            ArrayList<QuestionOption> options = new ArrayList<>();
            for(int j = 1; j<= new Integer(properties.getProperty("numberOfOptions")); j++){
                options.add(new QuestionOption(""+ j ,"" + j,  properties.getProperty("questionOption"+ j)));
            }
            Question question = new Question(1, 0, properties.getProperty("question1"), options );

            System.out.println(question.toString());
        }

        switch (refType){
            case "simple":
                SimpleReferendum simpleReferendum = new SimpleReferendum();
                break;
            default:
                throw new IllegalArgumentException("Invalid Referendum type: " + refType);

        }


        return null;
    }

}
