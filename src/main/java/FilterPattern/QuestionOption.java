package FilterPattern;

public class QuestionOption {
    private String qid;
    private String optionId;
    private String optionString;




    public QuestionOption(String qid, String optionId, String optionString) {
        this.qid = qid;
        this.optionId = optionId;
        this.optionString = optionString;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionString() {
        return optionString;
    }

    public void setOptionString(String optionString) {
        this.optionString = optionString;
    }

    @Override
    public String toString() {
        return "QuestionOption{" +
                "qid='" + qid + '\'' +
                ", optionId='" + optionId + '\'' +
                ", optionString='" + optionString + '\'' +
                '}';
    }
}
