package info.dourok.tools;

import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

public class BootAnimationView extends View implements Runnable{
	BootAnimation bootAnimation;
	Bitmap cBitmap;
	int cLeft;
	int cTop;
	Paint framePainter;
	private Paint background;
	Thread rThread;
	boolean stop;
	public BootAnimationView(Context context) {
		super(context);
		framePainter = new Paint();
		background = new Paint();
		background.setColor(0xFFFFFF);
		background.setStyle(Style.FILL);
		rThread = new Thread(this);
	}
	public void setBootAnimation(BootAnimation animation) throws ZipException, IOException{
		cTop =(getHeight() - animation.getHeight()) /2;
		cLeft = (getWidth()  - animation.getWidth())/2;
		bootAnimation = animation;
		bootAnimation.parseDesc();
	}
	
	public void start(){
		stop = false;
		rThread.start();
	}
	public void stop(){
		stop=true;
	}
//	int ii=0;
	@Override
	public void draw(Canvas canvas) {
//		Log.v(Main.BA_TAG, "Draw count: "+ii++);
		
		canvas.drawRect(0,0,getWidth(),getHeight(), background);
		if(cBitmap!=null){
			Matrix matrix = new Matrix();
			float ws = ((float)getWidth())/cBitmap.getWidth(), hs=((float)getHeight())/cBitmap.getHeight();
			if(ws>hs){
				ws = hs;
			}
			matrix.postScale(ws,ws);
			cTop =(int) ((getHeight() - ws*cBitmap.getHeight()) /2);
			cLeft = (int) ((getWidth()  - ws*cBitmap.getWidth())/2);
			matrix.postTranslate(cLeft, cTop);
			canvas.drawBitmap(cBitmap, matrix, framePainter);
//			cTop =(getHeight() - cBitmap.getHeight()) /2;
//			cLeft = (getWidth()  - cBitmap.getWidth())/2;
//			canvas.drawBitmap(cBitmap, cLeft, cTop, framePainter);
		}
//		super.draw(canvas);
	}
	@Override
	public void run() {
		long frameDuration = 1000/bootAnimation.getFps();
		long lastframe = System.currentTimeMillis();
		ZipFile zFile = bootAnimation.getzFile();
		first:for(BootAnimation.Part part: bootAnimation.getParts()){
			for(int i=0 ; part.count==0||i<part.count;i++){
				for(BootAnimation.ComparableZipEntry cZipEntry:part.frameEntry){
					if(stop)
						break first;
					try {
						Log.v(Main.BA_TAG, cZipEntry.getZipEntry().getName());
						cBitmap= BitmapFactory.decodeStream(zFile.getInputStream(cZipEntry.getZipEntry()));
						postInvalidate();
						long now = System.currentTimeMillis();
						long delay = frameDuration -(now -lastframe);
						lastframe = now;
						if(delay>0)
							Thread.sleep(delay);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(part.pause*frameDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
