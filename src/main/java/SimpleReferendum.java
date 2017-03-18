import java.util.ArrayList;
import java.util.UUID;

public class SimpleReferendum implements Referendum {
    private UUID refID;

    @Override
    public UUID getRefID() {
        return refID;
    }

    @Override
    public void createReferendum(UUID refID,  ArrayList<Question> questions, VoteCollector collector) {
        this.refID = refID;
        collector.collectVotes(refID);
        PINValidator pinValidator = new PINValidator(refID);
        pinValidator.validateVoterPIN();
    }

    @Override
    public void publishResults(UUID refID) {
        VoteCounter vc = new VoteCounter(refID);
        vc.calculateResults();

    }

}
