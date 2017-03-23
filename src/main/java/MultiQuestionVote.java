import java.util.ArrayList;

public class MultiQuestionVote extends Vote {
    private ArrayList ballot;


    public MultiQuestionVote(String voterID, String voterPIN, long timestamp, ArrayList ballot) {
        super(voterID, voterPIN, timestamp);
        this.ballot = ballot;

    }


    public ArrayList getMQVBallot() {
        return ballot;
    }

    public void setMQVBallot(ArrayList ballot) {
        this.ballot = ballot;
    }

    @Override
    public String toString() {
        return "MultiQuestionVote{" +
                "ballot=" + ballot +
                '}';
    }



}
