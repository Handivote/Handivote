package FilterPattern;

import java.util.ArrayList;
import java.util.UUID;


public interface Referendum {
    UUID getRefID();
    void createReferendum(UUID refId,  ArrayList<Question> questions, VoteCollector collector);
    void publishResults(UUID refId);
}
