package FilterPattern;


import backup.RecordVote;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.UUID;

public class RawVoteRecorder {
    private UUID refID;
    private DB db;


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

        public boolean recordVote(String vote){
            HTreeMap<Integer, String> map = db.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
            map.put(vote.hashCode() ,vote);
            db.commit();
            return true;
        }



}
