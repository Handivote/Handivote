import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Validator {
    private static Logger LOGGER = LoggerFactory.getLogger(Validator.class);
    private DB voteDB;
    private DB cardsDB;

    public Validator(UUID refID, ArrayList questions) {
        UUID refID1 = refID;
        ArrayList questions1 = questions;
        setupDB(refID);
    }

    private void setupDB(UUID refID) {
        voteDB = DBMaker.fileDB(refID + ".raw")
                .fileMmapEnable()
                .transactionEnable()
                .fileChannelEnable()
                .make();
        cardsDB = DBMaker.fileDB(refID + ".register")
                .transactionEnable()
                .fileMmapEnable()
                .make();

    }


    public void validateVoterPIN() {

        HTreeMap<String, String> voteMap = voteDB. hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();

        HTreeMap<String, String> cardsMap = cardsDB.hashMap("numsMap", Serializer.STRING, Serializer.STRING).createOrOpen();

        Set<String> keys = voteMap.getKeys();
        Object[] arr = keys.toArray();
        for (int i = 0; i < keys.size(); i++) {
            String strVote = voteMap.get(arr[i]);
            String[] parts = strVote.split(" ");
            String voterID = cardsMap.get(parts[0]);
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));
            // get vote from storage, if not checked, check and return to store, else do nothing,
            if (!vote.isPinVerified()) {

                LOGGER.info("Validating PIN of " + parts[0]);
                if (vote.getVoterPIN().equals(cardsMap.get(vote.getVoterID())) ) {
                    vote.resetVoterPIN();
                } else {
                    vote.setValid(false);
                    LOGGER.warn(vote.toJSONString() + " \n processed bad vote at: " + System.currentTimeMillis());
                }
                vote.setPinVerification(true);
                voteMap.put(vote.getVoterID(), vote.toString());
            }
        }
        voteDB.commit();//persist changes into disk
        voteDB.close();
    }


}
