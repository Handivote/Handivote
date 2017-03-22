import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class ReferendumFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(ReferendumFactory.class);

    private VoteCollector setCollector(UUID refID, String voteCollectorType, int numberOfQuestions) {
        VoteCollector voteCollector;
        LOGGER.info("Collector type: " + voteCollectorType);
        switch (voteCollectorType) {
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
                voteCollector = new SimpleTestCollector(numberOfQuestions);
                break;
            case "MultiTest":
                voteCollector = new MultiTestCollector(numberOfQuestions);
                break;
            default:
                IllegalArgumentException exception = new IllegalArgumentException("Invalid Collector type: " + voteCollectorType);
                LOGGER.error(exception.getMessage(), exception);
                throw exception;
        }
        return voteCollector;

    }

    Referendum buildReferendum(Properties properties) {
        UUID refID = UUID.randomUUID();
        int numberOfCards = Integer.parseInt(properties.getProperty("numberOfCards"));
        int pinDigits = Integer.parseInt(properties.getProperty("digits"));
        String password1 = properties.getProperty("password1");
        String password2 = properties.getProperty("password2");
        // load card numbers
        registerVoters(refID, numberOfCards, pinDigits, password1, password2);


        String refType = properties.getProperty("refType");

        ArrayList<Question> questions = new ArrayList<Question>();
        for (int i = 0; i < new Integer(properties.getProperty("numberOfQuestions")); i++) {
            ArrayList<QuestionOption> options = new ArrayList<>();
            for (int j = 1; j <= new Integer(properties.getProperty("numberOfOptions")); j++) {
                options.add(new QuestionOption("" + j, "" + j, properties.getProperty("questionOption" + j)));
            }
            for (int n = 1; n <= Integer.parseInt(properties.getProperty("numberOfQuestions")); n++) {
                Question question = new Question(1, 0, properties.getProperty("question" + String.valueOf(n)), options);
                questions.add(question);
            }

        }

        Referendum referendum;
        int numberOfQuestions = Integer.parseInt(properties.getProperty("numberOfQuestions"));
        VoteCollector voteCollector = setCollector(refID, properties.getProperty("voteMethod"), numberOfQuestions);
        switch (refType) {
            case "simple":
                referendum = new SimpleReferendum();
                referendum.createReferendum(refID, questions, voteCollector);
                break;
            case "multi":
                referendum = new MultiQuestionReferendum();
                referendum.createReferendum(refID, questions, voteCollector);
                break;

            default:
                throw new IllegalArgumentException("Invalid Referendum type: " + refType);

        }
        LOGGER.info("Created Referendum :"+ referendum.getRefID() + " Type: " + refType);
        return referendum;
    }

    private void registerVoters(UUID refID, int numberOfCards, int pinDigits, String password1, String password2) {
        GenerateCardNumbers gcn = new GenerateCardNumbers(refID, password1, password2, numberOfCards, pinDigits);
        StoreCardNumbers scn = new StoreCardNumbers(refID, gcn.getNumsList());
        //todo load nums from file
        //StoreCardNumbers scn = null;
        // try {
        //     scn = new StoreCardNumbers(refID, "/src/main/resources/emailnums.text");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        scn.storeCardNumbers();
    }

}
