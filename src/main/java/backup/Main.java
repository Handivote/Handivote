/*
package backup;

import javax.mail.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Main {
    private static final Boolean TESTING = true;
    private static final boolean MAIL = false;

    public static void main(String[] args) {

        //setup referendum
        // cards
        int numCards = 5000;
        GenerateCardNumbers gcn = new GenerateCardNumbers("password1", "wordpass2", numCards, 4);
        ArrayList <Details> voteDetails = new ArrayList<Details>();
        ArrayList <String> voteList = null;

        //questions
        long startTime = 0;
        ArrayList <QuestionOption> qOpts = new ArrayList<>() ;
        QuestionOption questOpt1 = new QuestionOption(1,1, "yes");
        QuestionOption questOpt2 = new QuestionOption(1,2, "no");
        qOpts.add(questOpt1);
        qOpts.add(questOpt2);
        Question question = new Question(1, 0, "Test question", qOpts);
        CreateReferendum referendum = new CreateReferendum("./nums.txt", question);
        UUID refId = referendum.getRefId();
        System.out.println(refId);
        if(MAIL)startTime = createJavaMail(voteDetails, startTime);
        else {
            MockEmailGenerator meg = new MockEmailGenerator();
            try {
                voteList = meg.createMockEmailList("./nums.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i =0 ; i<numCards; i++) {
                String[] arr = (String[]) voteList.toArray();
                System.out.println(arr[i].toString());
                //Details d = VoteParser.extractDetails(voteList.get(i));
                //voteDetails.add(d);
            }

        }

        RecordVote.processVotes(refId, voteDetails);
        //for (Details vote : voteDetails){
        //    boolean success = RecordVote.recordVote(refId, vote);
        //    System.out.println(success);
        // }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) );

    }

    private static long createJavaMail(ArrayList<Details> voteDetails, long startTime) {
        try {
            //Message[] msgs = EmailRetriever.readMessages();
            Message[] msgs = MockEmailRetriever.getMails();
            startTime = System.currentTimeMillis(); // time from arrival of msgs
            System.out.println(startTime);
            for (Message m : msgs){
                //if(VoteParser.validateMessageFormat(m.getFrom()[0].toString(), m.getSubject().toString())) {

                voteDetails.add(VoteParser.extractDetails(m.getContent().toString()));
                System.out.println(System.currentTimeMillis() - startTime);
                //}
                //else {
                //System.out.println(m.getFrom()[0].toString() + " " + m.getSubject().toString());
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            MockEmailRetriever.stopServer();
        }
        return startTime;
    }
    //todo fix process votes - valid or invalid
    //todo record votes
    //todo calculate results
}
*/
