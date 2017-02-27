package FilterPattern;


import java.util.UUID;

public class TestRunner {
    private static UUID refID;



    public static void main(String[] args){
        int numCards = 100;
        refID = UUID.randomUUID();
        GenerateCardNumbers gcn = new GenerateCardNumbers("password1", "wordpass2", numCards, 4);
        StoreCardNumbers scn = new StoreCardNumbers(refID, gcn.numsList);
        if(scn.storeCardNumbers());
        MockEmailCollector mockEmails = new MockEmailCollector();
        mockEmails.collectVotes(refID);
        mockEmails.stopServer();
        System.out.println("done");
        //DB db = DBMaker.fileDB(refID + ".raw").make();

        Validator validator = new Validator(refID);
        validator.validateVotes();

    }
}