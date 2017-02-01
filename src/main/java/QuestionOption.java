
public class QuestionOption {
    private int qid;
    private int optionId;
    private String optionString;

    public QuestionOption(int qid, int optionId, String optionString) {
        this.qid = qid;
        this.optionId = optionId;
        this.optionString = optionString;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionString() {
        return optionString;
    }

    public void setOptionString(String optionString) {
        this.optionString = optionString;
    }
}
