package pl.wojtek.rop.login;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.wojtek.rop.R;
import pl.wojtek.rop.User;
import pl.wojtek.rop.db.HawkManager;
import pl.wojtek.rop.railway.Failure;
import pl.wojtek.rop.railway.Result;
import pl.wojtek.rop.railway.Success;

public class LoginActivity extends AppCompatActivity {

    private final int MIN_USER_LETTER = 4;
    private final int PASSWORD_USER_LETTER = 4;

    private TextInputLayout userWrapper;
    private TextInputLayout passwordWrapper;

    private List<String> specialLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        grabUi();
        init();
    }

    private void init() {
        specialLetters = Arrays.asList(getResources().getStringArray(R.array.special_letter));
    }

    private void grabUi() {
        userWrapper = findViewById(R.id.userWrapper);
        passwordWrapper = findViewById(R.id.passwordWrapper);
    }

    public void onLogInClick(View view) {
        userNameValidate()
            .bind(isUserValidate -> passwordValidate()
            .bind(isValid -> getUserFromDb(userWrapper.getEditText().getText().toString())))
            .bind(user -> checkPassword(user.getPassword(), passwordWrapper.getEditText().getText().toString()))
            .result(user -> hiddenKeyboard(),
                    error -> Toast.makeText(view.getContext(), error, Toast.LENGTH_SHORT).show());
    }

    private Result<Boolean,String> checkPassword(String passwordFromDb, String writedPassword) {
        if (passwordFromDb.equals(writedPassword)) {
            return Success.withValue(true);
        }
        return Failure.withError("Wrong password");
    }

    private void hiddenKeyboard() {

    }

    private Result<User, String> getUserFromDb(String userName) {
        return HawkManager.getUserForName(userName);
    }

    private Result<Boolean, String> userNameValidate() {
        return Success.<String, String>withValue(userWrapper.getEditText().getText().toString())
                .bind(user -> textHaveNLetterOrMore(user, MIN_USER_LETTER))
                .bind(user -> textHaveOneBigLetter(user))
                .map(user -> true)
                .result(user -> userWrapper.setError(""), error -> userWrapper.setError(error));
    }

    private Result<Boolean, String> passwordValidate() {
        return Success.<String, String>withValue(passwordWrapper.getEditText().getText().toString())
                .aggregate(password -> textHaveNLetterOrMore(password, PASSWORD_USER_LETTER),
                        password -> textHaveOneBigLetter(password),
                        password -> passwordHaveSpecialLetter(password))
                .map(user -> true)
                .result(user -> passwordWrapper.setError(""), error -> passwordWrapper.setError(error));
    }

    private Result<String, String> passwordHaveSpecialLetter(String password) {
        Result<String, String> result = null;
        boolean isSpecialLetter = false;
        for (char letter : password.toCharArray()) {
            if (specialLetters.contains(String.valueOf(letter))) {
                isSpecialLetter = true;
                break;
            }
        }
        if (isSpecialLetter) {
            result = Success.withValue(password);
        } else {
            result = Failure.withError("Text have not one or more special letters");
        }
        return result;
    }

    private Result<String, String> textHaveOneBigLetter(String user) {
        Result<String, String> result = null;
        boolean isBigLetter = false;
        for (char letter : user.toCharArray()) {
            if (letter >= 'A' && letter <= 'Z') {
                isBigLetter = true;
                break;
            }
        }
        if (isBigLetter) {
            result = Success.withValue(user);
        } else {
            result = Failure.withError("Text have not one or more big letters");
        }
        return result;
    }

    private Result<String, String> textHaveNLetterOrMore(String user, int minUserLetter) {
        Result<String, String> result = null;
        if (user.length() >= minUserLetter) {
            result = Success.withValue(user);
        } else {
            result = Failure.withError("Text is too short");
        }
        return result;
    }

}
