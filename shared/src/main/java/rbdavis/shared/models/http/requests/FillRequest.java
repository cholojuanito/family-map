package rbdavis.shared.models.http.requests;

/**
 * A specific {@code Request} mimics a HTTP request and carries information
 * from the client that made the API call to the {@code FillHandler} then
 * to the {@code FillService}.
 * <p>
 * It contains the username of the current user and how many generations
 * they want to have generated. Default is 4.
 *
 * @author Tanner Davis
 * @version 0.1
 * @since v0.1
 */

public class FillRequest {
    private String userName;
    private int numGenerations;

    public FillRequest(String userName) {
        this.userName = userName;
        this.numGenerations = 4;
    }

    public FillRequest(String userName, int numGenerations) {
        this.userName = userName;
        this.numGenerations = numGenerations;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
    }
}
