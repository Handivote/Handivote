package FilterPattern;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SimpleReferendum implements Referendum {

    @Override
    public UUID createReferendum(UUID refId, Date startDate, Date endDate, ArrayList<Question> questions, VoteCollector collector) {
        return null;
    }

    @Override
    public void publishResults(UUID refId) {

    }
}
