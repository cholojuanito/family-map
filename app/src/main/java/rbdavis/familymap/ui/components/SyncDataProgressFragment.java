package rbdavis.familymap.ui.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import rbdavis.familymap.R;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.familymap.tasks.SyncDataTask;

public class SyncDataProgressFragment extends Fragment implements SyncDataTask.Callback {

    private static Callback callback;

    public interface Callback {
        void onSyncError(String... messages);
        void onSynced(String... messages);
    }

    private ProgressBar progressCircle;
    private TextView progressText;

    public static SyncDataProgressFragment newInstance(Callback callback) {
        SyncDataProgressFragment.callback = callback;
        return new SyncDataProgressFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_sync_data, container, false);

        progressCircle = (ProgressBar) v.findViewById(R.id.progressCircle);
        progressText = (TextView) v.findViewById(R.id.progressText);

        String content = "Syncing data...";
        progressText.setText(content);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO Run tests on how to handle this part well
        syncData(ServerProxy.getInstance().getToken());
    }

    private void syncData(String authToken) {
        try {
            SyncDataTask task = new SyncDataTask(this);
            task.execute(authToken);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSyncError(String... messages) {
        callback.onSyncError();
    }

    @Override
    public void onSyncProgressUpdate(String... messages) {
        progressText.setText(messages[0]);
    }

    @Override
    public void onSyncComplete(String... messages) {
        callback.onSynced();
    }
}
