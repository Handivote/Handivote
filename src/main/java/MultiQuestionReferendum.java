import java.util.ArrayList;
import java.util.UUID;


public class MultiQuestionReferendum implements Referendum {
    private UUID refID;
    private ArrayList<Question> questions;

    @Override
    public UUID getRefID() {
        return refID;
    }

    @Override
    public void createReferendum(UUID refID, ArrayList<Question> questions, VoteCollector collector) {
        //todo sort out questions problem  - multi
        this.refID = refID;
        collector.collectVotes(refID);
        Validator validator = new Validator(refID);
        validator.validateVoterPIN();
    }

    @Override
    public void publishResults(UUID refID) {
        VoteCounter vc = new VoteCounter(refID);
        vc.multQuestionCalculateResults(questions);
    }

}
