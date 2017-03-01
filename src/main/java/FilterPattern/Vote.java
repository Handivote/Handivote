package FilterPattern;

import java.util.Arrays;


public class Vote {
    private boolean valid;
    private String voterID;
    private String voterPIN;
    private long timestamp;
    private String questionID;
    private String [] ballot;


    private boolean pinChecked;


    public Vote(String voterID, String voterPIN, long timestamp, String questionID, String[] ballot) {
        this.voterID = voterID;
        this.voterPIN = voterPIN;
        this.timestamp = timestamp;
        this.questionID = questionID;
        this.ballot = ballot;
        this.valid = true;
        this.pinChecked =false;
    }

    public boolean isValid() {
        return valid;
    }

    protected void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getVoterID() {
        return voterID;
    }

    public String getVoterPIN() {
        return voterPIN;
    }

    protected void resetVoterPIN(){
        voterPIN = "";
    };

    public long getTimestamp() {
        return timestamp;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String[] getBallot() {
        return ballot;
    }

    public boolean isPinChecked() {
        return pinChecked;
    }

    public void setPinChecked(boolean pinChecked) {
        this.pinChecked = pinChecked;
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
