package FilterPattern;

import java.util.ArrayList;

public class Question {
    private int qid;
    private int refID;
    private String questionString;
    private ArrayList<QuestionOption> options;

    public Question(int qid, int refID, String questionString, ArrayList<QuestionOption> options) {
        this.qid = qid;
        this.refID = refID;
        this.questionString = questionString;
        this.options = options;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getRefID() {
        return refID;
    }

    public void setRefID(int refID) {
        this.refID = refID;
    }

    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    public ArrayList<QuestionOption> getOptions() {
        return options;
    }



}
