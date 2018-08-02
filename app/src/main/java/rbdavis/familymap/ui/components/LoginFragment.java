package rbdavis.familymap.ui.components;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.security.InvalidParameterException;

import rbdavis.familymap.R;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.familymap.tasks.LoginUserTask;
import rbdavis.familymap.tasks.RegisterUserTask;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.utils.Constants;

import static rbdavis.shared.utils.Constants.*;

public class LoginFragment extends Fragment implements LoginUserTask.Callback, RegisterUserTask.Callback  {
    private static Callback callback;

    public interface Callback {
        void onLogin();
    }

    private EditText hostEditText;
    private EditText portEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;

    private Button loginButton;
    private Button registerButton;


    public LoginFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param callback A Callback
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(Callback callback) {
        LoginFragment.callback = callback;
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        if (savedInstanceState != null) {
            // Save their info?
            if (savedInstanceState.getString(Constants.SYNC_ERROR_KEY) != null) {
                showResponseToast(savedInstanceState.getString(Constants.SYNC_ERROR_KEY));
            }
        }

        hostEditText = (EditText) v.findViewById(R.id.edit_host);
        portEditText = (EditText) v.findViewById(R.id.edit_port);
        usernameEditText = (EditText) v.findViewById(R.id.edit_username);
        passwordEditText = (EditText) v.findViewById(R.id.edit_password);
        firstNameEditText = (EditText) v.findViewById(R.id.edit_first);
        lastNameEditText = (EditText) v.findViewById(R.id.edit_last);
        emailEditText = (EditText) v.findViewById(R.id.edit_email);
        maleRadio = (RadioButton) v.findViewById(R.id.radio_male);
        femaleRadio = (RadioButton) v.findViewById(R.id.radio_female);
        loginButton = (Button) v.findViewById(R.id.btn_login);
        registerButton = (Button) v.findViewById(R.id.btn_register);

        fillDefaults();

        initOnTextChangeListeners();

        initOnClickListeners();

        return v;
    }

    private void initOnTextChangeListeners() {
        hostEditText.addTextChangedListener(new LoginTextWatcher());
        portEditText.addTextChangedListener(new LoginTextWatcher());
        usernameEditText.addTextChangedListener(new LoginTextWatcher());
        passwordEditText.addTextChangedListener(new LoginTextWatcher());
        firstNameEditText.addTextChangedListener(new LoginTextWatcher());
        lastNameEditText.addTextChangedListener(new LoginTextWatcher());
        emailEditText.addTextChangedListener(new LoginTextWatcher());
    }

    private void initOnClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClick();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClick();
            }
        });
    }

    private void fillDefaults() {
        hostEditText.setText(getString(R.string.default_host));
        portEditText.setText(getString(R.string.default_port));
        usernameEditText.setText(getString(R.string.default_user));
        passwordEditText.setText(getString(R.string.default_pass));
        firstNameEditText.setText(getString(R.string.default_first));
        lastNameEditText.setText(getString(R.string.default_last));
        emailEditText.setText(getString(R.string.default_email));
    }

    private void onLoginButtonClick() {
        disableButtons();
        try {
            String host = hostEditText.getText().toString();
            String port = portEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            reviewAllLoginInfo(host, port, username, password);

            LoginRequest newRequest = new LoginRequest(username, password);

            LoginUserTask task = new LoginUserTask(this);
            task.execute(newRequest);
        }
        catch (InvalidParameterException e) {
            //TODO Log it
            showEmptyFieldToast(e.getMessage());
        }
    }

    private void onRegisterButtonClick() {
        disableButtons();
        try {

            String host = hostEditText.getText().toString();
            String port = portEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String first = firstNameEditText.getText().toString();
            String last = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String gender = maleRadio.isChecked() ? "m" : "f";
            reviewAllRegisterInfo(host, port, username, password, first, last, email);

            RegisterRequest newRequest = new RegisterRequest(username, password, email, first, last, gender);

            RegisterUserTask task = new RegisterUserTask(this);
            task.execute(newRequest);
        }
        catch (InvalidParameterException e) {
            //TODO Log it
            showEmptyFieldToast(e.getMessage());
        }

    }

    public void reviewAllLoginInfo(String host, String portStr, String userName, String password) throws InvalidParameterException {
        if (TextUtils.isEmpty(host)) {
            throw new InvalidParameterException(getString(R.string.host_hint));
        }
        else {
            ServerProxy.getInstance().setHostName(host);
        }

        if (TextUtils.isEmpty(portStr)) {
            throw new InvalidParameterException(getString(R.string.port_hint));
        }
        else {
            int port = Integer.parseInt(portStr);
            ServerProxy.getInstance().setPortNum(port);
        }

        if(TextUtils.isEmpty(userName)) {
            throw new InvalidParameterException(getString(R.string.username_hint));
        }

        if(TextUtils.isEmpty(password)) {
            throw new InvalidParameterException(getString(R.string.password_hint));
        }

    }

    public void reviewAllRegisterInfo(String host, String portStr, String userName, String password,
                                      String firstName, String lastName, String email) throws InvalidParameterException {

        reviewAllLoginInfo(host, portStr, userName, password);

        if(TextUtils.isEmpty(firstName)) {
            throw new InvalidParameterException(getString(R.string.first_hint));
        }

        if(TextUtils.isEmpty(lastName)) {
            throw new InvalidParameterException(getString(R.string.last_hint));
        }

        if(TextUtils.isEmpty(email)) {
            throw new InvalidParameterException(getString(R.string.email_hint));
        }

    }

    @Override
    public void onLoginComplete(String message) {
        showResponseToast(message);
        enableButtons();
        if (message.equals(LOGIN_SUCCESS)) {
            callback.onLogin();
        }
    }

    @Override
    public void onRegistrationComplete(String message) {
        showResponseToast(message);
        enableButtons();
        if (message.equals(REG_SUCCESS)) {
            callback.onLogin();
        }
    }

    private void enableRegister() {
        registerButton.setEnabled(true);
        registerButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
    }

    private void enableLogin() {
        loginButton.setEnabled(true);
        loginButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
    }

    private void disableRegister() {
        registerButton.setEnabled(false);
        registerButton.setTextColor(ContextCompat.getColor(getContext(),R.color.disabledButtonTextColor));
    }

    private void disableLogin() {
        loginButton.setEnabled(false);
        loginButton.setTextColor(ContextCompat.getColor(getContext(),R.color.disabledButtonTextColor));
    }

    private void enableButtons() {
        enableLogin();
        enableRegister();
    }

    private void disableButtons() {
        disableLogin();
        disableRegister();
    }

    private void showEmptyFieldToast(String fieldName) {
        String output = fieldName + " field cannot be empty";
        Toast toast = Toast.makeText(getContext(), output, Toast.LENGTH_LONG);
        // You can personalize it later
        toast.show();
    }

    private void showResponseToast(String reason) {
        Toast toast = Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT);
        // You can personalize it later
        toast.show();
    }

    private class LoginTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String host = hostEditText.getText().toString();
            String port = portEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String first = firstNameEditText.getText().toString();
            String last = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();

            try {
                LoginFragment.this.reviewAllLoginInfo(host, port, username, password);
                LoginFragment.this.enableLogin();
            }
            catch (InvalidParameterException e) {
                //TODO Log it
                LoginFragment.this.disableLogin();
            }

            try {
                LoginFragment.this.reviewAllRegisterInfo(host, port, username, password, first, last, email);
                LoginFragment.this.enableRegister();
            }
            catch (InvalidParameterException e) {
                //TODO Log it
                LoginFragment.this.disableRegister();
            }
        }
    }

}
