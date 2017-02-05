package FilterPattern;

import java.util.Date;


public class Vote {
    private String voterID;
    private String voterPIN;
    private Date timestamp;
    private String questionID;
    private String [] ballot;


    public Vote(String voterID, String voterPIN, Date timestamp, String questionID, String[] ballot) {
        this.voterID = voterID;
        this.voterPIN = voterPIN;
        this.timestamp = timestamp;
        this.questionID = questionID;
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
        return questionID;
    }

    public String[] getBallot() {
        return ballot;
    }

}
