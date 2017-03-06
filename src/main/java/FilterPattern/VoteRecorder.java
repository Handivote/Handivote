package FilterPattern;



import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;


import java.util.Arrays;
import java.util.UUID;

public class VoteRecorder {
    private UUID refID;
    private DB db;
    private Vote vote;


    public VoteRecorder(UUID refID) {
        this.refID = refID;
        this.db = setupDB(refID);
    }

    public void closeDB(){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        System.out.println(map.size());
        map.close();
        db.close();
    }

    private  DB  setupDB(UUID refId){
        db = DBMaker.fileDB(refId + ".raw").fileMmapEnable().make();
        return db;
    }

    public boolean recordVote(Vote vote){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        // vaildate vote

        map.put(vote.getVoterID(), vote.toString());
        //System.out.println("recorded: " + vote.toString()+ " " + map.size());
        if (map.containsKey(vote.getVoterID())){
            String [] parts = map.get(vote.getVoterID()).split(" ");
            System.out.println(parts[4]);
            String[] storedBallot = parts[4].split(" ");
            Vote storedVote = new Vote(parts[0],parts[1],Long.parseLong(parts[2]),parts[3],storedBallot);
            //System.out.println(Arrays.toString(storedVote.getBallot()));
            if(Arrays.equals(vote.getBallot(), storedVote.getBallot())){
                vote.setValid(false);
                vote.setPinChecked(true);
                map.put(vote.getVoterID(), vote.toString());
                //System.out.println("recorded bad vote : " + vote.toString()+ " ");
            }
        }
        db.commit();
        return true;
    }

}
