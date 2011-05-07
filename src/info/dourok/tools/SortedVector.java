package info.dourok.tools;

import java.util.Random;
import java.util.Vector;


public class SortedVector<E extends Comparable<E>> extends Vector<E>{
	public static  boolean ORDER = true;
	public static boolean DEORDER = false;
	private boolean order;
	public SortedVector(boolean d) {
		super();
		order = d;
		
	}
	public synchronized boolean add(E obj) {
		int i = size()-1;
		while(i>=0){
			boolean t = obj.compareTo(get(i))>0;
			if(t^order){
				i--;
			}else{
				break;
			}
		}
		add(i+1,obj);
		return true;
	};
	public static void main(String[] args) {
		SortedVector<Integer> s = new SortedVector<Integer>(ORDER);
		Random rand= new Random();
		for(int i=1;i<=10;i++){
			int j=rand.nextInt(100);
			System.out.print(j+"\t");
			s.add(j);
		}
		System.out.println();
		for(Integer i : s){
			System.out.print(i+"\t");
		}
		
	}
}
