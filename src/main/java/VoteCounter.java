import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger LOGGER = LoggerFactory.getLogger(VoteCounter.class);
    private ArrayList<Question> questions;

    public VoteCounter(UUID refID, ArrayList<Question> questions) {
        this.refID = refID;
        this.questions = questions;

    }
    public void multiQuestionCalculateResults(ArrayList<Question> questions) {
        HTreeMap<String, String> voteMap = getVoteStore();
        HashMap<String,Integer> ballotCount = new HashMap<String,Integer>();
        for(String key : voteMap.getKeys()) {
            String[] parts = voteMap.get(key).split(" ");
            //System.out.println(Arrays.toString(parts));
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));
            //System.out.println(vote.toString());
        }

    }
    public String validateVoteOptions(Vote vote) {
        String str = vote.ballotToString();
        for (Question question : questions) {
            ArrayList<QuestionOption> qOpt = question.getOptions();
            for (QuestionOption questionOption : qOpt) {
                if ((str.toLowerCase()).equals(questionOption.getOptionString())) {
                    return questionOption.getOptionId();
                } else {
                    return str;
                }
            }
        }
        return str;
    }



    public void calculateResults() {

        //System.out.println("results");
        HTreeMap<String, String> voteMap = getVoteStore();
        HashMap<String,Integer> ballotCount = new HashMap<String,Integer>();
        String badVote = "Invalid Option";
        ballotCount.put("1",0);
        ballotCount.put("2",0);
        ballotCount.put("3",0);
        ballotCount.put("4",0);
        ballotCount.put(badVote, 0);
        Integer check;
        for(String key : voteMap.getKeys()){
            String[] parts = voteMap.get(key).split(" ");
            Vote vote = new Vote(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4].split(" "));
            //System.out.println(validateVoteOptions(vote)); //todo votecollector fix for longer options
            publishResults(parts[0], parts[4]);
            for(String  opt : vote.getBallot()) {

                //todo check for yes, no, or custom options and invalid options
                // todo
                try {
                     check = new Integer(opt);
                }
                catch (Exception e){
                    check = -1;
                    LOGGER.warn(e.toString());
                }
                if (!(check >0 && check <5)) {
                    ballotCount.put(badVote, ballotCount.get(badVote)+1 );

                }else{
                    Integer i = ballotCount.get(check.toString());
                    System.out.println(" putting " + opt  + " check: " + check + ": " + String.valueOf(i+1));
                    ballotCount.put(check.toString(), i + 1);
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

    @NotNull
    private HTreeMap<String, String> getVoteStore() {
        voteDB = DBMaker.fileDB(refID + ".raw").fileMmapEnable().make();
        LOGGER.info("Opened " + refID + " @" + System.currentTimeMillis());
        return voteDB.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
    }

    private void publishResults(String voterID, String ballot) {

        switch (ballot){
            case "1":
                option1.add(voterID);
                break;
            case "2":
                option2.add(voterID);
                break;
            case "3":
                option3.add(voterID);
                break;
            case "4":
                option4.add(voterID);
                break;
            default:
                badVotes.add(voterID);
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
