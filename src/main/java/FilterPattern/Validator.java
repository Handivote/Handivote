package FilterPattern;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Set;
import java.util.UUID;

public class Validator {

    private UUID refID;
    private DB voteDB;
    private DB numsDB;

    public Validator(UUID refID) {
        this.refID = refID;
        this.voteDB = setupDB(refID);
    }

    private DB setupDB(UUID refID) {
        voteDB = DBMaker.fileDB(refID + ".raw").fileMmapEnable().make();
        numsDB = DBMaker.fileDB(refID + ".nums").fileMmapEnable().make();
        return voteDB;
    }
    public void validateVotes(){
        HTreeMap<Long, String> voteMap = voteDB.hashMap("map", Serializer.LONG, Serializer.STRING).createOrOpen();
        HTreeMap<Integer, String> numsMap = numsDB.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
        Set<Long> keys = voteMap.getKeys();
        Object[] arr = keys.toArray();
        for ( int i=0; i<keys.size (); i++){
            String strVote = voteMap.get(arr[i]);
            String[] parts = strVote.split(" ");
            String voterID = numsMap.get(parts[0]);
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));
            if(vote.getVoterPIN().equals(numsMap.get(vote.getVoterID()))){
                System.out.println(vote.toJSONString() + " validated");

            }
            else{
                System.out.println( " process bad vote");
            }
            //Object foo = map.remove(arr[i]);
            //System.out.println("foo : " + foo.toString());
        }
        //voteDB.commit();
        voteDB.close();
        //setupDB(refID);
        //HTreeMap<Integer, String> map2 = voteDB.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
        //System.out.println(map2.values());
    }

    //TODO  check card number from vote against .nums map

}
