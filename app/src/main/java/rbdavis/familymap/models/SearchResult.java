package rbdavis.familymap.models;

public class SearchResult {

    public static final int PERSON_RESULT = 0;
    public static final int EVENT_RESULT = 1;

    private int resultType;
    private String id;
    private String topLine;
    private String botLine;

    public SearchResult() {
    }

    public SearchResult(int resultType, String id, String topLine, String botLine) {
        this.resultType = resultType;
        this.id = id;
        this.topLine = topLine;
        this.botLine = botLine;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopLine() {
        return topLine;
    }

    public void setTopLine(String topLine) {
        this.topLine = topLine;
    }

    public String getBotLine() {
        return botLine;
    }

    public void setBotLine(String botLine) {
        this.botLine = botLine;
    }
}
