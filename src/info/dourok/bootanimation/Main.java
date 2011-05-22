package info.dourok.bootanimation;

import android.view.MotionEvent;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;

public class Main extends Activity {

    public static String BA_TAG = "bootanimation";
    public static  int BOOTANIMATION_FILE = 10086;

    public static String BOOTANIMATION_TAG="bamt";
    static ZipFileNameFilter zipFileNameFilter = new ZipFileNameFilter();
//    public static String START_PATH_TAG="sp";
//    private BootAnimationView animationPlayer;
//    private BootAnimation ba;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode == BOOTANIMATION_FILE){
            String result = data.getStringExtra(FileChooser.RESULT_FILE_TAG);
            Intent intent = new Intent(this,BootAnimationPropertiesActivity.class);
            intent.putExtra(FileChooser.RESULT_FILE_TAG, result);
            startActivity(intent);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Intent intent = new Intent(this, FileChooser.class);
            intent.putExtra(FileChooser.INIT_PATH_TAG, "/sdcard");
            intent.putExtra(FileChooser.FILE_FILTER_TAG, zipFileNameFilter);
            startActivityForResult(intent, BOOTANIMATION_FILE);
            return true;
        }
        return super.onTouchEvent(event);
    }
    
    static class ZipFileNameFilter implements FileFilter,Serializable{

        public boolean accept(File file) {
            String name = file.getName();
            boolean rslt = file.isDirectory()||(name.substring(name.length()-4,name.length()).equalsIgnoreCase(".zip"));
            Log.v(Main.BA_TAG, file.getAbsolutePath()+"   "+rslt);
            return rslt;
        }
        
    }
    

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}