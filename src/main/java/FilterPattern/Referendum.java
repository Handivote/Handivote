package FilterPattern;

import java.util.Date;
import java.util.UUID;


public interface Referendum {
    UUID createReferendum(Date startDate, Date endDate, Question[] questions, VoteCollector collector);
    void publishResults(UUID refId);
}
