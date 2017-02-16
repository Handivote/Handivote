package FilterPattern;

import jdk.nashorn.api.scripting.JSObject;

import java.util.Arrays;


public class Vote {
    private String voterID;
    private String voterPIN;
    private long timestamp;
    private String questionID;
    private String [] ballot;


    public Vote(String voterID, String voterPIN, long timestamp, String questionID, String[] ballot) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String[] getBallot() {
        return ballot;
    }


    public String toJSONString() {
        return "Vote{" +
                "voterID='" + voterID + '\'' +
                ", voterPIN='" + voterPIN + '\'' +
                ", timestamp=" + timestamp +
                ", questionID='" + questionID + '\'' +
                ", ballot=" + Arrays.toString(ballot) +
                '}';
    }
    @Override
    public String toString() {
        return "" + voterID + " " + voterPIN + " " + timestamp + " " + questionID + " " + Arrays.toString(ballot);
    }
}
