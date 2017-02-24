package FilterPattern;


import java.util.Date;
import java.util.UUID;

public class SimpleReferendum implements Referendum {
    private UUID refId;
    private String fName;
    private Question question;

    public SimpleReferendum() {
        this.refId = UUID.randomUUID();
        this.fName = fName;
        this.question = question;
    }



    @Override
    public UUID createReferendum(Date startDate, Date endDate, Question[] question, VoteCollector collector) {
        return null;
    }

    @Override
    public void publishResults(UUID refId) {

    }
}
