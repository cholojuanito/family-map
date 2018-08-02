package rbdavis.familymap.tasks;

import android.os.AsyncTask;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;

import static rbdavis.shared.utils.Constants.REG_SUCCESS;
import static rbdavis.shared.utils.Constants.SUCCESS;

public class RegisterUserTask extends AsyncTask<RegisterRequest, String, String> {

    public interface Callback {
        void onRegistrationComplete(String message);
    }

    private static Callback callback;

    public RegisterUserTask(Callback c) {
        callback = c;
    }

    @Override
    protected String doInBackground(RegisterRequest... requests) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        String responseStr;
        LoginOrRegisterResponse response = ServerProxy.getInstance().register(requests[0]);

        if (response.getMessage().equals(SUCCESS)) {
            responseStr = REG_SUCCESS;
        }
        else {
            responseStr = response.getMessage();
        }

        return responseStr;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onRegistrationComplete(result);
    }

}
