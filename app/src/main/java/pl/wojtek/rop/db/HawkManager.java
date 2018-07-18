package pl.wojtek.rop.db;

import com.orhanobut.hawk.Hawk;

import pl.wojtek.rop.User;

public class HawkManager {

    private static final String USER_KEY = "USER_KEY";

    public static boolean isSavedUser() {
        return Hawk.contains(USER_KEY);
    }

    public static boolean savaUser(String name, String password) {
        return Hawk.put(USER_KEY, new User(name, password));
    }

    public static User getUserUser() {
        return Hawk.get(USER_KEY, null);
    }

}
