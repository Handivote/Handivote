package FilterPattern;


import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.UUID;

public class StoreCardNumbers {

    private ArrayList numsList;
    private UUID refID;
    private DB db;

    public StoreCardNumbers(UUID refID, ArrayList numsList) {
        this.refID = refID;
        this.db = setupDB(refID);
        this.numsList = numsList;

    }
    private DB setupDB(UUID refId){
        db = DBMaker.fileDB(refId + ".nums").fileMmapEnable().make();
        return db;
    }
    public void storeCardNumbers(){
        HTreeMap<String, String> map = db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        for (int i=0; i<numsList.size(); i++){
            String str = (String) numsList.get(i);
            String [] parts = str.split(" ");
            map.put(parts[0], parts[1]);
            System.out.println("added " +numsList.get(i));
        }



    }
}
