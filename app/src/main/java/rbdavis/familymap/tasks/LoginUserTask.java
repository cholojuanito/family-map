package rbdavis.familymap.tasks;

import android.os.AsyncTask;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;

import static rbdavis.shared.utils.Constants.LOGIN_SUCCESS;
import static rbdavis.shared.utils.Constants.SUCCESS;

public class LoginUserTask extends AsyncTask<LoginRequest, String, String> {

    public interface Callback {
        void onLoginComplete(String message);
    }

    private static Callback callback;

    public LoginUserTask(Callback c) {
        callback = c;
    }

    @Override
    protected String doInBackground(LoginRequest... requests) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        String responseStr;
        LoginOrRegisterResponse response = ServerProxy.getInstance().login(requests[0]);

        if (response.getMessage().equals(SUCCESS)) {
            responseStr = LOGIN_SUCCESS;
        }
        else {
            responseStr = response.getMessage();
        }

        return responseStr;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onLoginComplete(result);
    }

}
