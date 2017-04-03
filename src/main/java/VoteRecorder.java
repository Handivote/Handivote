import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class VoteRecorder implements Runnable {
    private UUID refID;
    private DB db;
    private Vote vote;
    private ArrayList<Question> questions;
    public static final Boolean TESTING = true;
    private static Logger LOGGER = LoggerFactory.getLogger(VoteRecorder.class);
    private int voteCount;


    public VoteRecorder(UUID refID, ArrayList questions) {
        this.refID = refID;
        this.db = setupDB(refID);
        this.questions = questions;
        voteCount =0;
    }

    public VoteRecorder(UUID refID) {
        this.refID = refID;
        this.db = setupDB(refID);
        this.questions = questions;
        voteCount=0;
    }
    public void closeDB(){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        map.close();
        db.close();
    }

    public  DB  setupDB(UUID refId){
        LOGGER.info("Setting up :" + refId.toString());
        db = DBMaker.fileDB(refId + ".raw")
                .fileMmapEnable()
                .transactionEnable()
                .make();
        return db;
    }
    public  void  validateVoteOptions(Vote vote){
        for( Question question  : questions) {
            ArrayList<QuestionOption> qOpt = question.getOptions();
            for (QuestionOption questionOption : qOpt) {
                if((vote.ballotToString().toLowerCase()).equals(questionOption.getOptionString())){
                    LOGGER.warn("Voter: "+ vote.getVoterID() + ", vote = "  + vote.ballotToString().toLowerCase()
                            + " matches option:  " + questionOption.getOptionId()
                            + ": " + questionOption.getOptionString());
                }
            }
        }
    }

    public  boolean recordVote(Vote vote){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();

        if (map.containsKey(vote.getVoterID())){
            String [] parts = map.get(vote.getVoterID()).split(" ");
            String[] storedBallot = parts[4].split(" ");
            Vote storedVote = new Vote(parts[0],parts[1],Long.parseLong(parts[2]),parts[3],storedBallot);
            if(!Arrays.equals(vote.getBallot(), storedVote.getBallot())){
                vote.setValid(false);
                vote.setPinVerification(true);
                map.put(vote.getVoterID(), vote.toString());
                LOGGER.warn(" Recorded bad vote : " + vote.toString()+ " @" + System.currentTimeMillis());
            }
        }
        map.put(vote.getVoterID(), vote.toString());
        db.commit();
        LOGGER.info("Added vote:  " + vote.getVoterID()+ " at " + System.currentTimeMillis() );
        voteCount++;
        System.out.println(voteCount);
        return true;
    }

    @Override
    public void run() {

    }
}
