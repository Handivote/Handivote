package FilterPattern;



import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;


import java.util.UUID;

public class RawVoteRecorder {
    private UUID refID;
    private DB db;
    private  Vote vote;


    public RawVoteRecorder(UUID refID) {
        this.refID = refID;
        this.db = setupDB(refID);
    }

    public void closeDB(){
            db.close();
    }

    private  DB  setupDB(UUID refId){
        db = DBMaker.fileDB(refId + ".raw").fileMmapEnable().make();
        return db;
    }

    public boolean recordVote(Vote vote){
        HTreeMap<Integer, String> map = db.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
        map.put(vote.hashCode() ,vote.toString());
        db.commit();
        return true;
    }
}
