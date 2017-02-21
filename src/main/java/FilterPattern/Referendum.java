package FilterPattern;


import java.util.Date;
import java.util.UUID;

public class Referendum implements ReferendumFactory{

    public Referendum() {
    }

    @Override
    public UUID createReferendum(Date startDate, Date endDate, Question question, VoteCollector collector) {
        return null;
    }

    @Override
    public void publishResults(UUID refId) {

    }
}
