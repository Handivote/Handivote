package FilterPattern;

import java.util.Date;
import java.util.UUID;


public interface ReferendumFactory {
    UUID createReferendum(Date startDate, Date endDate, Question question, VoteCollector collector);
    void publishResults(UUID refId);
}
