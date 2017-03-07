package FilterPattern;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SimpleReferendum implements Referendum {

    @Override
    public void createReferendum(UUID refID, Date startDate, Date endDate, ArrayList<Question> questions, VoteCollector collector) {
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
