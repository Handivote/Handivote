import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VoteCounter {
    private UUID refID;
    private DB voteDB;
    private ArrayList <String> option1 = new ArrayList<>();
    private ArrayList <String> option2 = new ArrayList<>();
    private ArrayList <String> option3 = new ArrayList<>();
    private ArrayList <String> option4 = new ArrayList<>();
    private ArrayList <String> badVotes = new ArrayList<>();


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
            publishResults(parts[0], parts[4]);
            for(String  opt : vote.getBallot()) {
                //todo check for yes, no, or custom options and invalid options
                // todo
                if ( Integer.parseInt(opt)>0  && (Integer.parseInt(opt) <5)) {
                    Integer i = ballotCount.get(opt);
                    //System.out.println(" putting " + opt  + ": " + String.valueOf(i+1));
                    ballotCount.put(opt, i + 1);

                }else{
                    ballotCount.put(badVote, ballotCount.get(badVote)+1 );
                }
            }
        }
        System.out.println(badVote +" " + ballotCount.get(badVote));
        writeToFile("1", option1);
        writeToFile("2", option2);
        writeToFile("3", option3);
        writeToFile("4", option4);
        writeToFile("badVotes", badVotes);
    }

    private void publishResults(String voterID, String ballot) {

        switch (ballot){
            case "1":
                option1.add(voterID+ " : " + ballot);
                break;
            case "2":
                option2.add(voterID + " : " + ballot);
                break;
            case "3":
                option3.add(voterID + " : " + ballot);
                break;
            case "4":
                option4.add(voterID + " : " + ballot);
                break;
            default:
                badVotes.add(voterID + " : " + ballot);
                break;
        }


    }
    private void writeToFile(String opt, ArrayList<String> voteList) {
        File file = new File("./" + refID.toString() + "_results_option_" + opt + ".txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);

        for (String s : voteList) {
            try {
                System.out.println(s);
                bw.write(s + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
