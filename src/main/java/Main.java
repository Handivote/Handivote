import javax.mail.Message;
import java.util.ArrayList;

/**
 * Created by p on 16/01/17.
 */
public class Main {
    public static void main(String[] args) {

        //setup referendum
        // cards
        GenerateCardNumbers gcn = new GenerateCardNumbers("password1", "wordpass2", 100, 4);
        //ArrayList arrayList = gcn.numsList;

        //questions
        ArrayList <QuestionOption> qOpts = new ArrayList<>() ;
        QuestionOption questOpt1 = new QuestionOption(1,1, "yes");
        QuestionOption questOpt2 = new QuestionOption(1,2, "no");
        qOpts.add(questOpt1);
        qOpts.add(questOpt2);
        Question question = new Question(1, 0, "Test question", qOpts);
        CreateReferendum referendum = new CreateReferendum("./nums.txt", question);
        try {
            // Message[] msgs = EmailRetriever.readMessages();
           Message[] msgs = MockEmailRetriever.getMails();
            for (Message m : msgs){
                System.out.println (m.getContent().toString() + " " + m.getSubject().toString());
           }
       } catch (Exception e) {
            e.printStackTrace();
       }
    }
    //todo process votes - valid or invalid
    //todo calculate results
}
