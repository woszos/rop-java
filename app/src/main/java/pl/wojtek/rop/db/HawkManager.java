package pl.wojtek.rop.db;

import com.orhanobut.hawk.Hawk;

import pl.wojtek.rop.User;
import pl.wojtek.rop.railway.Failure;
import pl.wojtek.rop.railway.Result;
import pl.wojtek.rop.railway.Success;

public class HawkManager {

    private static final String USER_KEY = "USER_KEY";

    public static boolean isSavedUser() {
        return Hawk.contains(USER_KEY);
    }

    public static boolean savaUser(String name, String password) {
        return Hawk.put(USER_KEY, new User(name, password));
    }

    public static Result<User, String> getUserForName(String userName) {
        User user = Hawk.get(USER_KEY, null);
        if (user != null && user.getName().equals(userName)) {
            return Success.withValue(user);
        }
        return Failure.withError("This user is not in db");
    }

}
