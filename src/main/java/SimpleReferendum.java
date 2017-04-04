import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("ALL")
public class SimpleReferendum implements Referendum {
    private UUID refID;
    private ArrayList questions;

    @Override
    public void setQuestions(ArrayList questions) {
        this.questions = questions;
    }

    @Override
    public ArrayList getQuestions() {
        return questions;
    }


    @Override
    public UUID getRefID() {
        return refID;
    }

    @Override
    public void createReferendum(UUID refID,  ArrayList<Question> questions, VoteCollector collector) {
        this.refID = refID;
        setQuestions(questions);
        collector.collectVotes(refID);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void publishResults(UUID refID)
    {
        //noinspection unchecked
        @SuppressWarnings("unchecked") ResultsGenerator vc = new ResultsGenerator(refID, questions);
        vc.calculateResults();

    }

}
