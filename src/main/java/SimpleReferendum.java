import java.util.ArrayList;
import java.util.UUID;

public class SimpleReferendum implements Referendum {
    private UUID refID;
    private ArrayList questions;

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
    public void createReferendum(UUID refID,  ArrayList<Question> questions, VoteCollector collector) {
        this.refID = refID;
        setQuestions(questions);
        collector.collectVotes(refID);
        Validator validator = new Validator(refID, getQuestions());
        validator.validateVoterPIN();
    }

    @Override
    public void publishResults(UUID refID)
    {
        VoteCounter vc = new VoteCounter(refID, questions);
        vc.calculateResults();

    }

}
