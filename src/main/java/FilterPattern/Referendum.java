package FilterPattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public interface Referendum {
    UUID createReferendum(UUID refId, Date startDate, Date endDate, ArrayList<Question> questions, VoteCollector collector);
    void publishResults(UUID refId);
}
