package info.dourok.bootanimation;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.io.FileFilter;

public class FileChooser extends ListActivity {
    public static String INIT_PATH_TAG="sp";
    public static String FILE_FILTER_TAG="ff";
    public static String RESULT_FILE_TAG="rf";
    private String curPath;
    private File[] fileList;
    private FileArrayAdapter fileArrayAdapter;
    private FileFilter filter;

    public FileChooser() {
//        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Main.BA_TAG, "FileChooser onCreate");
        setContentView(R.layout.filechooser);
        fileList = new File[0];
        curPath = getIntent().getStringExtra(INIT_PATH_TAG);
        if(curPath==null){
            curPath="/sdcard";
        }
        filter = (FileFilter) getIntent().getSerializableExtra(FILE_FILTER_TAG);
        fill(new File(curPath));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(Main.BA_TAG, "FileChooser onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
         Log.v(Main.BA_TAG, "FileChooser onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(Main.BA_TAG, "FileChooser onStart");
    }

    
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(Main.BA_TAG, "FileChooser onStop");
    }
    
    
    
    
    
    private void fill(File f) {
        if (!f.isDirectory()) {
            f = f.getParentFile();
        }
        setTitle(curPath);
        fileList = f.listFiles(filter);
//        for (File ff : fileList) {
//            Log.v(getResources().getString(R.string.log_tag),ff.getName());
//        }
        sort(fileList, 0, fileList.length - 1);
//        Log.v(getResources().getString(R.string.log_tag),"-----------------------------------sorted-----------------------------------");
//         for (File ff : fileList) {
//            Log.v(getResources().getString(R.string.log_tag),ff.getName());
//        }
        fileArrayAdapter = new FileArrayAdapter(this, R.layout.file_view, fileList);
        setListAdapter(fileArrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == 0) {
            File f = new File(curPath).getParentFile();
            curPath = f.getAbsolutePath();
            fill(f);
            return;
        } else {
            position--;
        }
        File f = fileArrayAdapter.getItem(position);
        if (f.isDirectory()) {
            curPath = f.getAbsolutePath();
            fill(f);
        } else if (f.isFile()) {
            Intent intent = new Intent();
            intent.putExtra(RESULT_FILE_TAG, f.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    private int compare(File f1, File f2) {
        if (f1.isDirectory() == f2.isDirectory()) {
            return f1.getName().compareTo(f2.getName());
        }
        return f1.isDirectory() ? -1 : 1;
    }
    
    private void sort(File[] fs, int h, int t) {
        int p = partitions(fs, h, t);
        if (p == -1) {
            return;
        }
        if (p > h) {
            sort(fs, h, p - 1);
        }
        if (p < t) {
            sort(fs, p + 1, t);
        }

    }

    private int partitions(File[] fs, int h, int t) {
        // System.out.println(h+"   "+t);
        if (h == t) {
            return -1;
        }
        int p = h + (t - h) / 2;
        int b = h, s = t;
        File tmp;
        while (b < p) {
            if (compare(fs[b], fs[p]) > 0) {
                while (s > p) {
                    if (compare(fs[s], fs[p]) < 0) {
                        tmp = fs[b];
                        fs[b] = fs[s];
                        fs[s] = tmp;
                        break;
                    } else {
                        s--;
                    }
                }
                if (s == p) {
                    while (b < p) {
                        if (compare(fs[b], fs[p]) > 0) {
                            tmp = fs[b];
                            fs[b] = fs[p - 1];
                            fs[p - 1] = tmp;
                            fs[p - 1] = fs[p];
                            fs[p] = tmp;
                            p--;
                        } else {
                            b++;
                        }
                    }
                }
            } else {
                b++;
            }
        }
        while (s > p) {
            if (compare(fs[s], fs[p]) < 0) {
                tmp = fs[s];
                fs[s] = fs[p + 1];
                fs[p + 1] = tmp;
                fs[p + 1] = fs[p];
                fs[p] = tmp;
                p++;
            } else {
                s--;
            }
        }
        return p;
    }
}


class FileArrayAdapter extends ArrayAdapter<File> {

    int resource;
    File[] fileList;

    public FileArrayAdapter(Context context, int resource, File[] fileList) {
        super(context, resource, fileList);
        this.resource = resource;
        this.fileList = fileList;
    }

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name;
        String infos;
        if (position == 0) {
            name = "..";
            infos = "Parent";
        } else {
            position--;
            File file = getItem(position);
            name = file.getName();
            infos = file.isDirectory() ? "Folder" : ("File Size:" + file.length());
        }
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resource, null);
        }
        TextView tn = (TextView) v.findViewById(R.id.file_name);
        TextView ti = (TextView) v.findViewById(R.id.file_info);
        if (tn != null) {
            tn.setText(name);
        }
        if (ti != null) {
            ti.setText(infos);
        }
        return v;
    }
}

