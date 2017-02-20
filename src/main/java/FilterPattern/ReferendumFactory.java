package FilterPattern;

import java.util.Date;
import java.util.UUID;

/**
 * Created by p on 20/02/17.
 */
public interface ReferendumFactory {
    Referendum createReferendum(Date startDate, Date endDate, Question question);
    void publishResults(UUID refId);
}
