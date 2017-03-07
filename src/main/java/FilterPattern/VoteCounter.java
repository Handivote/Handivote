package FilterPattern;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

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
            for (int i=0; i <(vote.getBallot().length); i++){
                System.out.println(vote.getBallot()[i]);//todo get winner and publish result in format - (get format)from super
            }

        }


    }
}
