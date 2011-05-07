package info.dourok.tools;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
   
   
    public static String BA_TAG = "bootanimation";
    private BootAnimationView animationPlayer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        animationPlayer = new BootAnimationView(this);
        try {
			BootAnimation ba = new BootAnimation(new File("/data/local/ubuntubootanimation.zip"));
			animationPlayer.setBootAnimation(ba);
		} catch (ZipException e) {
			
			Log.e(BA_TAG, "Creating BootAnimation Error", e);
		} catch (IOException e) {
			Log.e(BA_TAG, "Creating BootAnimation Error", e);
		}
        setContentView(animationPlayer);
        animationPlayer.start();
    }
    @Override
    protected void onPause() {
    	animationPlayer.stop();
    	super.onPause();
    }
    
    @Override
    protected void onStop() {
    	animationPlayer.stop();
    	super.onStop();
    }
}