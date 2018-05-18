package clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CodeInt implements Comparable<CodeInt> , Cloneable{
	
	private Integer[] info;
	
	private int digQty;
	private int l;
	
	public static void main(String[] args) {
		int l = 5;
		Integer[] a = new Integer[l];
		for (int i = 0; i < a.length ; i++)
			a[i] = i;
		CodeInt c1 = new CodeInt(a.clone(), l);
		
		for (int i = 0; i < a.length ; i++)
			a[i] = l - i - 1;
		CodeInt c2 = new CodeInt(a.clone(), l);
		System.out.println(c1);
		System.out.println(c2);
		
		System.out.println(c2.compareTo(c1));
		System.out.println(c1.compareTo(c2));
		System.out.println(c1.compareTo(c1));
		System.out.println(c2.compareTo(c2));
		System.out.println(c1.isPermutation());
		c1.add(1);
		System.out.println(c1);
		System.out.println(c1.isPermutation());
		
		for (int i = 0; i < a.length ; i++)
			a[i] = 0;
		CodeInt c3 = new CodeInt(a.clone(), 20);
		c3.add(8958); // Prueba con Base 20
		System.out.println(c3);
		
		System.out.println(permutation(25, 4));
		
		char[] chars = new char[1024];
		Arrays.fill(chars, 'A');
		String text = new String(chars);
		
		try {
			long start = System.nanoTime();
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter("./testBufered.txt"));
			for(int i = 0 ; i < 100*1024 ; i++)
				bw.write(text);
			bw.close();
			long time = System.nanoTime() - start;
			System.out.println("Wrote " + chars.length*1000L*102400L/time+" MB/s.");
			
			
			
			start = System.nanoTime();
			FileWriter writer = new FileWriter("./testNormal.txt");
			for(int i = 0 ; i < 100*1024 ; i++)
				writer.write(text);
			writer.close();
			time = System.nanoTime() - start;
			System.out.println("Wrote " + chars.length*1000L*102400L/time+" MB/s.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Integer[] getInfo() {
		return info;
	}


	public void setInfo(Integer[] info) {
		this.info = info;
	}


	public int getDigQty() {
		return digQty;
	}


	public void setDigQty(int digQty) {
		this.digQty = digQty;
	}


	public int getL() {
		return l;
	}


	public void setL(int l) {
		this.l = l;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		CodeInt newClone = new CodeInt(info.clone(), l);
		
		return newClone;
	}


	@Override
	public String toString() {
		return Arrays.toString(info);
	}

	@Override
	public int compareTo(CodeInt o) {
		// TODO Auto-generated method stub
		int i = 0;
		while(i < this.digQty) {
			int comparison = this.info[i].compareTo(o.info[i]);
			if(comparison > 0)
				return 1;
			else if (comparison < 0)
				return -1;
			i++;
		}
		return 0;
	}
	
	public CodeInt(Integer[] info, int l) {
		super();
		this.info = info;
		this.digQty = info.length;
		this.l = l;
		
		this.add(0);
	}

	public void add(Integer b) {
		// Este metodo suma en la base que corresponda a traves de la cant de opciones
		Integer newVal;
		for (int i = 1; i <= digQty ; i++) {
			newVal = Math.floorMod(this.info[digQty - i] + b , l);
			
			b = Math.floorDiv((this.info[digQty - i] + b)  , l);
			this.info[digQty - i] = newVal;
		}
	}
	
	public void add(Long b) {
		// Este metodo suma en la base que corresponda a traves de la cant de opciones
		Long newVal;
		for (int i = 1; i <= digQty ; i++) {
			newVal = Math.floorMod(this.info[digQty - i] + b , l);
			
			b = Math.floorDiv((this.info[digQty - i] + b)  , l);
			this.info[digQty - i] = newVal.intValue();
		}
	}
	
	/*public static void add(Long b, boolean t) {
		// Este metodo suma en la base que corresponda a traves de la cant de opciones
		Long newVal;
		for (int i = 1; i <= digQty ; i++) {
			newVal = Math.floorMod(this.info[digQty - i] + b , l);
			
			b = Math.floorDiv((this.info[digQty - i] + b)  , l);
			this.info[digQty - i] = newVal.intValue();
		}
	}*/
	
	public boolean isPermutation() {
		int i = 0;
		while (i < this.info.length - 1) {
			for (int j = i + 1 ; j < this.info.length ; j++)
				if (this.info[i].compareTo(this.info[j]) == 0)
					return false;
			i++;
		}
		return true;
	}
	
	public int nextPermutation() { // Puede requerir Muchos procesamiento
		int i = 0;
		while (!this.isPermutation()) {
			this.add(1);
			i++;
		}
		return i;
	}
	
	public static long factorial(long x) {
		if (x <= 1)
			return 1;
		return x * factorial(x - 1);
	}
	
	public static long permutation(long n , long r) {
		long result = 1;
		for (int i = (int) (n - r + 1) ; i <= n ; i++)
			result *= (long) i;
		return result;
	}
}
