import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class VoteRecorder {
    private UUID refID;
    private DB db;
    private Vote vote;
    private ArrayList<Question> questions;
    private static Logger LOGGER = LoggerFactory.getLogger(VoteRecorder.class);


    public VoteRecorder(UUID refID, ArrayList questions) {
        this.refID = refID;
        this.db = setupDB(refID);
        this.questions = questions;
    }

    public VoteRecorder(UUID refID) {
        this.refID = refID;
        this.db = setupDB(refID);
        this.questions = questions;
    }
    public void closeDB(){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        map.close();
        db.close();
    }

    public  DB  setupDB(UUID refId){
        LOGGER.info("Setting up :" + refId.toString());
        db = DBMaker.fileDB(refId + ".raw").fileMmapEnable().make();
        return db;
    }
    public void validateVoteOptions(Vote vote){
        for( Question question  : questions) {
            ArrayList<QuestionOption> qOpt = question.getOptions();
            for (QuestionOption questionOption : qOpt) {

                if((vote.ballotToString().toLowerCase()).equals(questionOption.getOptionString())){
                    System.out.println(vote.ballotToString().toLowerCase()+" ::: "+ questionOption.getOptionString());
                }
            }
        }
    }

    public boolean recordVote(Vote vote){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        // todo vaildate vote 'yes' ,'no' or user-defined option etc.


        if (vote.getBallot().length>1){
            validateVoteOptions(vote);
            //String ballot = vote.ballotToString(vote.getBallot());
            //System.out.println("recording: " + ballot);

        }

        if (map.containsKey(vote.getVoterID())){
            String [] parts = map.get(vote.getVoterID()).split(" ");
            System.out.println("record: " +  parts[4]);
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
        LOGGER.info("Added vote:  " + vote.toString() + " @" + System.currentTimeMillis() );
        db.commit();

        return true;
    }

}
