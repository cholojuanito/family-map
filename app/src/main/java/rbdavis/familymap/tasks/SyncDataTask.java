package rbdavis.familymap.tasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rbdavis.familymap.models.App;
import rbdavis.familymap.models.MapMarkerColor;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.responses.EventsResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;

import static rbdavis.shared.utils.Constants.SUCCESS;

public class SyncDataTask extends AsyncTask<String, String, String> {
    public interface Callback {
        void onSyncError(String... messages);
        void onSyncProgressUpdate(String... messages);
        void onSyncComplete(String... messages);
    }

    private static Callback callback;

    public SyncDataTask(Callback c) {
        callback = c;
    }

    @Override
    protected String doInBackground(String... tokens) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        // TODO put with constants
        String responseStr = "Finished!";
        //rest();

        String progressUpdate = "Fetching ancestors' data...";
        publishProgress(progressUpdate);
        //rest();

        PeopleRequest peopleRequest = new PeopleRequest(tokens[0]);
        PeopleResponse peopleResponse = ServerProxy.getInstance().getAllPeople(peopleRequest);
        if (peopleResponse.getMessage().equals(SUCCESS)) {

            App.getInstance().setPeople(peopleResponse);
        }
        else {
            responseStr = peopleResponse.getMessage();
            callback.onSyncError(responseStr);
            //TODO Terminate early
        }

        progressUpdate = "Fetching life events...";
        publishProgress(progressUpdate);
        //rest();

        /*------------------------------------------------------------------------------------------*/

        EventsRequest eventsRequest = new EventsRequest(tokens[0]);
        EventsResponse eventsResponse = ServerProxy.getInstance().getAllEvents(eventsRequest);
        if (eventsResponse.getMessage().equals(SUCCESS)) {

            App.getInstance().setEvents(eventsResponse);
        }
        else {
            responseStr = eventsResponse.getMessage();
            callback.onSyncError(responseStr);
            //TODO Terminate early
        }

        //rest();

        /*-------------------------------------------------------------------------------------------*/

        progressUpdate = "Organizing data...";
        publishProgress(progressUpdate);

        organizeAppData();

        //rest();

        progressUpdate = responseStr;
        publishProgress(progressUpdate);
        //rest();

        return responseStr;
    }


    private void organizeAppData() {
        App model = App.getInstance();

        model.setPersonalEvents();
        model.setEventTypeColors();
        model.setFocusedPersonId(model.getUserPersonId());
        model.setPaternalAncestors();
        model.setMaternalAncestors();
        model.setSearchableList();
    }

    private void rest() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled(String message) {
        callback.onSyncError(message);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        callback.onSyncProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onSyncComplete(result);
    }
}
