import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MultiTestCollector  implements VoteCollector {
    private static Logger LOGGER = LoggerFactory.getLogger(MultiTestCollector.class);
    private int numberOfVotes;
    private int numberOfQuestions = 4;

    public MultiTestCollector(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }


    @Override
    public void collectVotes(UUID refID) {
        MultiQuestionVoteRecorder voteRecorder = new MultiQuestionVoteRecorder(refID);
        ArrayList<String[]> ballots = null;
        MockEmailGenerator mockEmailGenerator = new MockEmailGenerator(numberOfQuestions);
        try {
            ballots = mockEmailGenerator.createMockEmailList(refID.toString() + "_nums.txt");

            for (int i=0; i<ballots.size(); i++){
                long timestamp = new Date().getTime();
                String [] content = ballots.get(i);
                String [] id = content[1].trim().split(" ");
                Vote vote = new Vote(id[0], id[1], timestamp, "1", content[2].split(""));
                voteRecorder.recordVote(vote);
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
        finally{
            LOGGER.info("Closing :" + refID.toString());
            voteRecorder.closeDB();
        }
    }





    @Override
    public void sendAck(String recipient) {

    }
}


