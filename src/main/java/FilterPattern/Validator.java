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
    private DB cardsDB;

    public Validator(UUID refID) {
        this.refID = refID;
        setupDB(refID);
    }

    private  void setupDB(UUID refID) {
        voteDB = DBMaker.fileDB(refID + ".raw").fileMmapEnable().make();
        cardsDB = DBMaker.fileDB(refID + ".register").fileMmapEnable().make();

    }

    public void validateVoterPIN() {
        HTreeMap<String, String> voteMap = voteDB.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        HTreeMap<String, String> cardsMap = cardsDB.hashMap("numsMap", Serializer.STRING, Serializer.STRING).createOrOpen();
        Set<String> keys = voteMap.getKeys();
        Object[] arr = keys.toArray();
        for (int i = 0; i < keys.size(); i++) {
            String strVote = voteMap.get(arr[i]);
            String[] parts = strVote.split(" ");
            String voterID = cardsMap.get(parts[0]);
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));
            // get vote from storage, if not checked, check and return to store, else do nothing,
            if (!vote.isPinChecked()) {
                System.out.println(vote.getVoterPIN() + ":" + cardsMap.get(vote.getVoterID())); //todo fix this
                if (vote.getVoterPIN().equals(cardsMap.get(vote.getVoterID())) ) {
                    vote.resetVoterPIN();
                    System.out.println(vote.toJSONString() + " validated");
                } else {
                    vote.setValid(false);
                    System.out.println(vote.toJSONString() + " \n processed bad vote");
                }
                vote.setPinChecked(true);
                //voteMap.put(vote.getVoterID(), vote.toString());
                //Object foo = map.remove(arr[i]);
                //System.out.println("foo : " + foo.toString());
            }
        }
        voteDB.commit();
        voteDB.close();
        //setupDB(refID);
        //HTreeMap<Integer, String> map2 = voteDB.hashMap("map", Serializer.INTEGER, Serializer.STRING).createOrOpen();
        //System.out.println(map2.values());
    }


}
