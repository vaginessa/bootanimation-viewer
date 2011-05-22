/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.dourok.bootanimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

/**
 *
 * @author douo
 */
public class BootAnimationPropertiesActivity extends Activity {

    static BootAnimation bootAnimation;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.properties);
         Intent intent = getIntent();
        filePath = intent.getStringExtra(FileChooser.RESULT_FILE_TAG);
        fill();
    }
    private void fill() {
        try {
            bootAnimation = new BootAnimation(new File(filePath));
            bootAnimation.parseDesc();
//            		animationPlayer.setBootAnimation(ba);
        } catch (ZipException e) {
            Log.e(getResources().getString(R.string.log_tag), "Creating BootAnimation Error", e);
        } catch (IOException e) {
            Log.e(getResources().getString(R.string.log_tag), "Creating BootAnimation Error", e);
        }
        TextView tmpView = (TextView) findViewById(R.id.ba_name);
        tmpView.setText(bootAnimation.getName());
        tmpView = (TextView) findViewById(R.id.ba_width);
        tmpView.setText(Integer.toString(bootAnimation.getWidth()));
        tmpView = (TextView) findViewById(R.id.ba_height);
        tmpView.setText(Integer.toString(bootAnimation.getHeight()));
        tmpView = (TextView) findViewById(R.id.ba_fps);
        tmpView.setText(Integer.toString(bootAnimation.getFps()));
        tmpView = (TextView) findViewById(R.id.ba_part);
        tmpView.setText(Integer.toString(bootAnimation.getParts().size()));
        Button btnPreview = (Button) findViewById(R.id.btn_preview);
        btnPreview.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(BootAnimationPropertiesActivity.this, BootAnimationPlayer.class);
//                   intent.putExtra(Main.BOOTANIMATION_TAG, bootAnimation);
                startActivity(intent);
            }
        });
        Button othrPreview = (Button) findViewById(R.id.btn_other); 
        othrPreview.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(BootAnimationPropertiesActivity.this, FileChooser.class);
                intent.putExtra(FileChooser.INIT_PATH_TAG, filePath.subSequence(0, filePath.lastIndexOf('/')));
                intent.putExtra(FileChooser.FILE_FILTER_TAG, Main.zipFileNameFilter);
                startActivityForResult(intent, Main.BOOTANIMATION_FILE); 
//                   intent.putExtra(Main.BOOTANIMATION_TAG, bootAnimation);
//                startActivity(intent);
            }
        });
//        LinearLayout parts = (LinearLayout) findViewById(R.id.ba_parts);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode == Main.BOOTANIMATION_FILE){
            filePath = data.getStringExtra(FileChooser.RESULT_FILE_TAG);
            fill();
        }
    }
    
}
