import java.util.ArrayList;
import java.util.UUID;


public interface Referendum {
    void setQuestions(ArrayList questions);
    ArrayList getQuestions();
    UUID getRefID();
    void createReferendum(UUID refId,  ArrayList<Question> questions, VoteCollector collector);
    void publishResults(UUID refId);
}
