package pl.wojtek.rop;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import pl.wojtek.rop.db.HawkManager;

public class RopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        HawkManager.savaUser("Test", "1234");
    }
}
