package FilterPattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class ReferendumFactory {


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
    private VoteCollector setCollector(UUID refID, String voteCollectorType){
        VoteCollector voteCollector;
        switch (voteCollectorType){
            case "mockEmail":
                voteCollector = new MockEmailCollector();
                break;
            case "Email":
                voteCollector = new MockEmailCollector();
                break;
            default:
                throw new IllegalArgumentException("Invalid Collector type: " + voteCollectorType);

        }
        return voteCollector;

    }

    public Referendum buildReferendum(Properties properties){
        String pattern = properties.getProperty("dates.pattern");
        String startDateStr = properties.getProperty("startDate");
        String endDateStr = properties.getProperty("endDate");
        Date startDate = setDates(pattern, startDateStr);
        Date endDate = setDates(pattern, endDateStr);

        String refType = properties.getProperty("refType");

        ArrayList<Question> questions = new ArrayList<Question>();
        for ( int i =0; i < new Integer(properties.getProperty("numberOfQuestions"));i++){
            ArrayList<QuestionOption> options = new ArrayList<>();
            for(int j = 1; j<= new Integer(properties.getProperty("numberOfOptions")); j++){
                options.add(new QuestionOption(""+ j ,"" + j,  properties.getProperty("questionOption"+ j)));
            }
            Question question = new Question(1, 0, properties.getProperty("question1"), options );
            questions.add(question);

            System.out.println(questions.get(i).toString());
        }
        UUID refID = UUID.randomUUID();
        VoteCollector voteCollector = setCollector(refID, properties.getProperty("voteMethod"));
        switch (refType){
            case "simple":
                SimpleReferendum simpleReferendum = new SimpleReferendum();

                simpleReferendum.createReferendum(refID, startDate, endDate,questions, voteCollector);
                break;
            default:
                throw new IllegalArgumentException("Invalid Referendum type: " + refType);

        }


        return null;
    }

}
