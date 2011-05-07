package info.dourok.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.util.Log;

public class BootAnimation {
		private int fps;
		private int width;
		private int height;
		private Vector<Part> parts;
		private ZipFile zFile;
		public BootAnimation(File f) throws ZipException, IOException {
			zFile = new ZipFile(f);
			parts = new Vector<BootAnimation.Part>(3);
			
		}
		
	public void parseDesc() throws ZipException,IOException  {
		Scanner scanner =new Scanner(getZipEntryInputStream("desc.txt", zFile));
		try{
		while(scanner.hasNext()){
			String tmp = scanner.next();
			Log.v(Main.BA_TAG, "ParseDesc: "+tmp);
			if(tmp.equals("p")){
				Part part =new Part();
				part.count = scanner.nextInt();
				part.pause = scanner.nextInt();
				part.path = scanner.next();
				parts.add(part);
			}else if(tmp.equals("#")){
				scanner.nextLine();
			}else {
				width = Integer.parseInt(tmp);
				height = scanner.nextInt();
				fps = scanner.nextInt();}
		}
		}catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		Enumeration<? extends ZipEntry> entries =zFile.entries();
		
		while(entries.hasMoreElements()){
			ZipEntry entry =entries.nextElement();
			for(Part p: parts){
			if(entry.getName().startsWith(p.path)&&!entry.getName().endsWith("/")){
				p.frameEntry.add(new ComparableZipEntry(entry));
			}
			}
		}
	}
	
	public static InputStream getZipEntryInputStream(String entryName,ZipFile zFile) throws ZipException,IOException{
		ZipEntry zEntry = zFile.getEntry(entryName);
		if(zEntry == null)
			throw new ZipException();
		return zFile.getInputStream(zEntry);
	}
	public int getFps() {
		return fps;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector<Part> getParts() {
		return parts;
	}

	public ZipFile getzFile() {
		return zFile;
	}
	@Override
	public String toString() {
		String s = "";
		s+="width: "+width+"\nheight: "+height+"\nfps: "+fps+"\n";
		for(Part p:parts){
			s+=p;
		}
		return s;
	}
	class Part{
		int count;
		int pause;
		String path;
		SortedVector<ComparableZipEntry> frameEntry;
		public Part() {
			frameEntry = new SortedVector<ComparableZipEntry>(SortedVector.ORDER);
		}
		@Override
		public String toString() {
			String s = path+"\n"+"count:"+count +"\tpause:"+pause+"\n";
			for(ComparableZipEntry zipEntry : frameEntry){
				s+=zipEntry.getZipEntry().getName()+"\n";
			}
			return  s;
		}
	
	}
	class ComparableZipEntry implements Comparable<ComparableZipEntry>{
		private ZipEntry zipEntry;
		ComparableZipEntry(ZipEntry zipEntry) {
			this.zipEntry = zipEntry;
		}
		@Override
		public int compareTo(ComparableZipEntry another) {
			return zipEntry.getName().compareTo(another.zipEntry.getName());
			
		}
		public ZipEntry getZipEntry() {
			return zipEntry;
		}
		
	}
}
