package FilterPattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class TestCollector implements VoteCollector {
    private int numberOfVotes;


    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }


    @Override
    public void collectVotes(UUID refID) {
        VoteRecorder voteRecorder = new VoteRecorder(refID);
         ArrayList<String[]> ballots = null;
         MockEmailGenerator mockEmailGenerator = new MockEmailGenerator();
        try {
            ballots = mockEmailGenerator.createMockEmailList(refID.toString() + "_nums.txt");

            for (int i=0; i<ballots.size(); i++){
                long timestamp = new Date().getTime();
                String [] content = ballots.get(i);
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
    public void sendAck() {

    }
}
