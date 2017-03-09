package FilterPattern;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class VoteCounter {
    private UUID refID;
    private DB voteDB;


    public VoteCounter(UUID refID) {
        this.refID = refID;

    }

    public void calculateResults() {

        System.out.println("results");
        voteDB = DBMaker.fileDB(refID + ".raw").fileMmapEnable().make();
        HTreeMap<String, String> voteMap = voteDB.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        
        for(String key : voteMap.getKeys()){
            String[] parts = voteMap.get(key).split(" ");
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));
            HashMap<String,Integer> ballotCount = new HashMap<String,Integer>(50,10);
            for(String  str : vote.getBallot()) {
                System.out.println(ballotCount.get(str));
                Integer i = ballotCount.get(str);
                if(i == null) ballotCount.put(str, 1);
                else ballotCount.put(str, i + 1);
            }
            System.out.println(ballotCount.values());
            //for (int i=0; i <(vote.getBallot().length); i++){

          //      System.out.println(vote.getBallot()[0]);//todo get winner and publish result in format - (get format)from super
      //      }

        }


    }
}
