package FilterPattern;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SimpleReferendum implements Referendum {

    @Override
    public void createReferendum(UUID refId, Date startDate, Date endDate, ArrayList<Question> questions, VoteCollector collector) {
        collector.collectVotes(refId);
        System.out.println(" seems to be working");
    }

    @Override
    public void publishResults(UUID refId) {

    }
}
