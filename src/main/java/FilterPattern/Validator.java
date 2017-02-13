package FilterPattern;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Set;
import java.util.UUID;

public class Validator {

    private UUID refID;
    private DB db;

    public Validator(UUID refID) {
        this.refID = refID;
        this.db = setupDB(refID);
    }

    private DB setupDB(UUID refID) {
        db = DBMaker.fileDB(refID + ".raw").fileMmapEnable().make();
        return db;
    }
    public void validateVotes(){
        HTreeMap<Integer, String> map = db.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
        Set<Integer> keys = map.getKeys();
        Object[] arr = keys.toArray();
        for ( int i=0; i<keys.size (); i++){
            System.out.println(map.get(arr[i]));
        }
    }
    //TODO  check card number from vote against .nums map

}
