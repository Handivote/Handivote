package FilterPattern;



import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;


import java.util.UUID;

public class RawVoteRecorder {
    private UUID refID;
    private DB db;
    private Vote vote;


    public RawVoteRecorder(UUID refID) {
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
        map.put(vote.getVoterID(), vote.toString());
        System.out.println("recorded: " + vote.toString()+ " " + map.size());
        db.commit();
        return true;
    }
}
