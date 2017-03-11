package FilterPattern;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        HashMap<String,Integer> ballotCount = new HashMap<String,Integer>();
        String badVote = "Invalid Option";
        ballotCount.put("1",0);
        ballotCount.put("2",0);
        ballotCount.put("3",0);
        ballotCount.put("4",0);
        ballotCount.put(badVote, 0);
        for(String key : voteMap.getKeys()){
            String[] parts = voteMap.get(key).split(" ");
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));

            for(String  opt : vote.getBallot()) {
                if ( Integer.parseInt(opt)>0  && (Integer.parseInt(opt) <5)) {
                    Integer i = ballotCount.get(opt);
                    System.out.println(" putting " + opt  + ": " + String.valueOf(i+1));
                    ballotCount.put(opt, i + 1);

                }else{
                    ballotCount.put(badVote, ballotCount.get(badVote)+1 );
                }

            }

            for(int i =0; i< ballotCount.keySet().size(); i++){
             System.out.println("Option " + i + ": " + ballotCount.get(String.valueOf(i)));
            }

        }
        System.out.println(badVote +" " + ballotCount.get(badVote));

    }
    private void writeToFile(String qID, String optID) {
        File file = new File("./" + refID.toString() + "Question_" + qID + optID + ".txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);

//        for (String s : ) {
//            try {
//                bw.write(s + System.getProperty("line.separator"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
