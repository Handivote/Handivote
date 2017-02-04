package FilterPattern;

import java.util.Date;

/**
 * Created by p on 04/02/17.
 */
public class Vote {
    private String voterID;
    private String voterPIN;
    private Date timestamp;
    private String QuestionID;
    private String [] ballot;


    public Vote(String voterID, String voterPIN, Date timestamp, String questionID, String[] ballot) {
        this.voterID = voterID;
        this.voterPIN = voterPIN;
        this.timestamp = timestamp;
        QuestionID = questionID;
        this.ballot = ballot;
    }

    public String getVoterID() {
        return voterID;
    }

    public String getVoterPIN() {
        return voterPIN;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getQuestionID() {
        return QuestionID;
    }

    public String[] getBallot() {
        return ballot;
    }

}
