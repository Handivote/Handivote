import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class ReferendumFactory {




    private VoteCollector setCollector(UUID refID, String voteCollectorType){
        VoteCollector voteCollector;
        switch (voteCollectorType){
            case "mockEmail":
                voteCollector = new MockEmailCollector();
                break;
            case "Email":
                voteCollector = new EmailCollector();
                break;
            case "SMS":
                voteCollector = new SMSCollector();
                break;
            case "Test":
                voteCollector = new TestCollector();
                break;
            default:
                throw new IllegalArgumentException("Invalid Collector type: " + voteCollectorType);

        }
        return voteCollector;

    }

    Referendum buildReferendum(Properties properties){
        UUID refID = UUID.randomUUID();
        int numberOfCards= Integer.parseInt(properties.getProperty("numberOfCards"));
        int pinDigits= Integer.parseInt(properties.getProperty("digits"));
        String password1 = properties.getProperty("password1");
        String password2 = properties.getProperty("password2");
        // load card numbers
        registerVoters(refID, numberOfCards, pinDigits, password1, password2);



        String refType = properties.getProperty("refType");

        ArrayList<Question> questions = new ArrayList<Question>();
        for ( int i =0; i < new Integer(properties.getProperty("numberOfQuestions"));i++){
            ArrayList<QuestionOption> options = new ArrayList<>();
            for(int j = 1; j<= new Integer(properties.getProperty("numberOfOptions")); j++){
                options.add(new QuestionOption(""+ j ,"" + j,  properties.getProperty("questionOption"+ j)));
            }
            Question question = new Question(1, 0, properties.getProperty("question1"), options );
            questions.add(question);
        }

        Referendum referendum;
        VoteCollector voteCollector = setCollector(refID, properties.getProperty("voteMethod"));
        switch (refType){
            case "simple":
                referendum = new SimpleReferendum();
                referendum.createReferendum(refID, questions, voteCollector);
                break;

            default:
                throw new IllegalArgumentException("Invalid Referendum type: " + refType);

        }
        return referendum;
    }

    private void registerVoters(UUID refID, int numberOfCards, int pinDigits, String password1, String password2) {
        GenerateCardNumbers gcn = new GenerateCardNumbers(refID, password1, password2, numberOfCards, pinDigits);
        //StoreCardNumbers scn = new StoreCardNumbers(refID, gcn.getNumsList());
        //todo load nums from file
        StoreCardNumbers scn = null;
        try {
            scn = new StoreCardNumbers(refID, "emailnums.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        scn.storeCardNumbers();
    }

}
