package FilterPattern;


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

        

    }
}