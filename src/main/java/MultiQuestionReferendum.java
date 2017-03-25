import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;


public class MultiQuestionReferendum implements Referendum {
    private static Logger LOGGER = LoggerFactory.getLogger(MultiQuestionReferendum.class);
    private UUID refID;
    private ArrayList<Question> questions;

    @Override
    public void setQuestions(ArrayList questions) {
        this.questions = questions;
    }

    @Override
    public ArrayList getQuestions() {
        return questions;
    }

    @Override
    public UUID getRefID() {
        return refID;
    }

    @Override
    public void createReferendum(UUID refID, ArrayList<Question> questions, VoteCollector collector) {
        //todo sort out questions problem  - multi
        this.refID = refID;
        collector.collectVotes(refID);
        Validator validator = new Validator(refID, getQuestions());
        validator.validateVoterPIN();
    }



    @Override
    public void publishResults(UUID refID) {
        VoteCounter vc = new VoteCounter(refID, questions);
        vc.multiQuestionCalculateResults(questions);
    }

}
