import java.util.Arrays;


public class Vote {
    private boolean valid;
    private String voterID;
    private String voterPIN;
    private long timestamp;
    private String questionID;
    private String[] ballot;


    private boolean pinVerified;


    public Vote(String voterID, String voterPIN, long timestamp) {
        this.voterID = voterID;
        this.voterPIN = voterPIN;
        this.timestamp = timestamp;
    }

    public Vote(String voterID, String voterPIN, long timestamp, String questionID, String[] ballot) {
        this.voterID = voterID;
        this.voterPIN = voterPIN;
        this.timestamp = timestamp;

        this.questionID = questionID;
        this.ballot = ballot;
        this.valid = true;
        this.pinVerified =false;
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

    public boolean isPinVerified() {
        return pinVerified;
    }

    public void setPinVerification(boolean pinVerified) {
        this.pinVerified = pinVerified;
    }



    public String toJSONString() {
        return "Vote{" +
                "voterID='" + voterID + '\'' +
                ", voterPIN='" + voterPIN + '\'' +
                ", timestamp=" + timestamp +
                ", questionID='" + questionID + '\'' +
                ", ballot=" + Arrays.toString(ballot).replace('[', ' ').replace(']', ' ').replace(',', ' ').trim() +
                '}';
    }
    @Override
    public String toString() {
        return "" + voterID + " "
                + voterPIN + " "
                + timestamp + " "
                + questionID + " "
                + Arrays.toString(ballot).replace('[', ' ')
                .replace(']', ' ').replace(',', ' ').trim();
    }
    public String ballotToString(){
        String ballotString = "";
        for (int i=0; i<ballot.length;i++){
            ballotString += ballot[i];
        }

        return ballotString;
    }
}
