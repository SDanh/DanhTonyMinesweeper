package danhs.uw.tacoma.edu.danhtonyminesweeper.authenticate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.regex.Pattern;

import danhs.uw.tacoma.edu.danhtonyminesweeper.R;

public class LoginFragment extends Fragment {

    private LoginInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText userIdText = (EditText) v.findViewById(R.id.userid_edit);
        final EditText pwdText = (EditText) v.findViewById(R.id.pwd_edit);

        //sign in button
        Button signInButton = (Button) v.findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();

                boolean isValid = validateEmailAndPassword(userIdText, pwdText, v);

                if(isValid) {
                    ((SignInActivity) getActivity()).login(userId, pwd);
                }
            }
        });

        //register button
        Button registerButton = (Button) v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();

                boolean isValid = validateEmailAndPassword(userIdText, pwdText, v);

                if(isValid) {
                    ((SignInActivity) getActivity()).register(userId, pwd);
                }

                //Toast.makeText(v.getContext(), "SIGNED UP"
                //        , Toast.LENGTH_LONG)
                //        .show();
                //((SignInActivity) getActivity()).login("TEMP", "TEMP");
            }
        });
        return v;
    }

    private boolean validateEmailAndPassword(EditText emailEditText, EditText passwordEditText, View v) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            //Toast.makeText(v.getContext(), "Enter userid"
            Toast.makeText(v.getContext(), R.string.missing_email
                    , Toast.LENGTH_SHORT)
                    .show();
            emailEditText.requestFocus();
            isValid = false;
            //return;
        }
        else if (!email.contains("@")) {
            Toast.makeText(v.getContext(), R.string.invalid_email
                    , Toast.LENGTH_SHORT)
                    .show();
            emailEditText.requestFocus();
            isValid = false;
            //return;
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(v.getContext(), R.string.missing_password
                    , Toast.LENGTH_SHORT)
                    .show();
            passwordEditText.requestFocus();
            isValid = false;
            //return;
        }
        else if (password.length() < 6) {
            Toast.makeText(v.getContext()
                    , R.string.invalid_password
                    , Toast.LENGTH_SHORT)
                    .show();
            passwordEditText.requestFocus();
            isValid = false;
            //return;
        }
        return isValid;
    }





    /**
     * Email validation pattern.
     */
    public final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * Validates if the given input is a valid email address.
     *
     * @param email        The email to validate.
     * @return {@code true} if the input is a valid email. {@code false} otherwise.
     */
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     *
     * @param password Must be at least minimum length. (length 6)
     * @return {@code true} if password is valid. {@code false} otherwise
     */
    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginInteractionListener) {
            mListener = (LoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * interface for the implementation of interaction listener for login fragment
     */
    public interface LoginInteractionListener {
        // TODO: Update argument type and name

        public void login(String userId, String pwd);
        public void register(String userId, String pwd);
    }







}
