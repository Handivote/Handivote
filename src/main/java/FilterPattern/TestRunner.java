package FilterPattern;


import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Set;
import java.util.UUID;

public class TestRunner {
    private static UUID refID;



    public static void main(String[] args){
        int numCards = 500;
        GenerateCardNumbers gcn = new GenerateCardNumbers("password1", "wordpass2", numCards, 4);
        refID = UUID.randomUUID();
        CollectMockEmails mockEmails = new CollectMockEmails();
        mockEmails.collectVotes(refID);
        mockEmails.stopServer();
        System.out.println("done");
        DB db = DBMaker.fileDB(refID + ".raw").make();
        HTreeMap<Integer, String> map = db.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
        Set<Integer> keys = map.getKeys();
        Object[] arr = keys.toArray();
        for ( int i=0; i<keys.size (); i++){
            System.out.println(map.get(arr[i]));
        }

    }
}