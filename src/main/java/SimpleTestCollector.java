import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class SimpleTestCollector implements VoteCollector {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleTestCollector.class);
    private int numberOfVotes;
    private ArrayList questions;

    public SimpleTestCollector(ArrayList<Question> questions) {
        this.questions = questions;

    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }


    @Override
    public void collectVotes(UUID refID) {
        VoteRecorder voteRecorder = new VoteRecorder(refID, questions);
         ArrayList<String[]> ballots = null;
         MockEmailGenerator mockEmailGenerator = new MockEmailGenerator();
        try {
            ballots = mockEmailGenerator.createMockEmailList(refID.toString() + "_nums.txt");

            for (int i=0; i<ballots.size(); i++){
                long timestamp = new Date().getTime();
                String [] content = ballots.get(i);
                // System.out.println("content: " + ballots.get(i));
                //System.out.println("content :" + content[1]);
                String [] id = content[1].trim().split(" ");
                //String ballot = content.substring(4);
                Vote vote = new Vote(id[0], id[1], timestamp, "1", content[2].split(""));
                voteRecorder.recordVote(vote);
                //System.out.println(" id: " + id[0]+ "  recorded");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            voteRecorder.closeDB();
        }
    }





    @Override
    public void sendAck(String recipient) {

    }
}
