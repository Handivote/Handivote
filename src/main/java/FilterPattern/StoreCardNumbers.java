package FilterPattern;


import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.UUID;

class StoreCardNumbers {

    private ArrayList numsList;
    private UUID refID;
    private DB db;

    StoreCardNumbers(UUID refID, ArrayList numsList) {
        this.refID = refID;
        this.db = setupDB(refID);
        this.numsList = numsList;

    }
    private DB setupDB(UUID refId){
        db = DBMaker.fileDB(refId + ".register").fileMmapEnable().make();
        return db;
    }
    void storeCardNumbers(){
        HTreeMap<String, String> map = db.hashMap("numsMap", Serializer.STRING, Serializer.STRING).createOrOpen();
        for (int i=0; i<numsList.size(); i++){
            String str = (String) numsList.get(i);
            String [] parts = str.split(" ");
            map.put(parts[0], parts[1]);
            System.out.println("added " +numsList.get(i));
        }
        db.commit();
        db.close();

    }
}
