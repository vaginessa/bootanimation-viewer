/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.dourok.bootanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;

/**
 *
 * @author douo
 */
public class BootAnimationPlayer extends Activity {

    BootAnimationView animationView;
    BootAnimation bootAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bootAnimation = BootAnimationPropertiesActivity.bootAnimation;
        //(BootAnimation) getIntent().getSerializableExtra(Main.BOOTANIMATION_TAG);
        animationView = new BootAnimationView(this);
        try {
            animationView.setBootAnimation(bootAnimation);
            setContentView(animationView);
        } catch (ZipException ex) {
            Logger.getLogger(BootAnimationPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BootAnimationPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        animationView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationView.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        animationView.stop();
    }
//    @Override
//    public void onBackPressed() {
//        finish();
//    }
}
