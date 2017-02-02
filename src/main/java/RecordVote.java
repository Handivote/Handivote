import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.UUID;


public class RecordVote {



    public static void processVotes( UUID refId, ArrayList<Details> voteDetails){
        DB db = setupDB(refId);
        for (Details vote : voteDetails){
            boolean success = RecordVote.recordVote(db, refId, vote);
            System.out.println(success);
        }
        db.commit();
        db.close();
    }
    private static DB  setupDB(UUID refId){
        DB db = DBMaker.fileDB(refId + ".db").fileMmapEnable().make();
        return db;
    }

    public static boolean recordVote(DB db, UUID refId, Details vote){

        String barcode = vote.card + vote.PIN;
        String voteOption = vote.option;


        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        map.put(barcode, voteOption);
        return true;
    }

}
