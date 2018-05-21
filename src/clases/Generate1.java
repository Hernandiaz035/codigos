package clases;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;

public class Generate1 {
	//private Queue<CodeInt> codes;
	private PriorityQueue<CodeInt> codes;
	//private ArrayList<String> codesString;
	private Queue<String> codesString;
	
	CodeInt firstCode;
	
	private ArrayList<String> opciones;
	private int digQty; // 'cifras' del codigo
	private long codQty; // cantidad de codigos requeridos
	
	private long delta; // separacion entre codigos

	private long n; // Cant posible de codigos
	private int l; // Cant de opciones
	
	Random rand;
	
	public Generate1(String[] opciones, int digQty, int codQty) {
		super();
		this.opciones = new ArrayList<String>(Arrays.asList(opciones));
		this.l = opciones.length;
		
		this.digQty = digQty;
		this.codQty = codQty;
		
		//codes = new LinkedList<CodeInt>();
		codes = new PriorityQueue<CodeInt>(CodeInt.compCodeInt);
		
		rand = new Random();
		
		codesString = new LinkedList<String>();

		n = (long) Math.pow((double)l, (double)digQty);
		//n = CodeInt.permutation(l, digQty);
		
		this.delta = (long) (this.n / ((long)this.codQty + 1L));
		
		Integer[] a = new Integer[digQty];
		for (int i = 0; i < a.length ; i++)
			a[i] = 0;
		firstCode = new CodeInt(a, l);
		long iniPos = (delta/2);// + rand.nextLong(delta / 2L);
		firstCode.add(iniPos);
		
		if (codQty > n)
			System.out.println("Unreacheable Quantity!!!");
	}
	
	public void generateCodes(boolean randomize) throws CloneNotSupportedException {
		codes.add(firstCode);
		CodeInt tmp = (CodeInt) firstCode.clone();
		long acum = 0;
		while(codes.size() < codQty) {
			if (delta >= l)
				acum = delta + rand.nextInt(l) - (l/2);
			else
				acum = delta;
			tmp.add(acum);
			if (randomize)
				tmp.randPriority();
			//codes.add(new CodeInt(tmp,acum));
			//codes.add(new CodeInt(tmp, 0L));
			codes.add((CodeInt) tmp.clone());
		}
	}
	
	public void codesToString() {
		String tmpString;
		CodeInt tmpCode;
		Integer[] idxs;
		while(!codes.isEmpty()) {
			tmpCode = codes.poll();
			idxs = tmpCode.getInfo();
			tmpString = "";
			for(int i = 0 ; i < digQty ; i++)
				tmpString += opciones.get(idxs[i]);
			this.codesString.add(tmpString);
		}
	}
	
	public void shuffleChar() {
		Collections.shuffle(opciones);
	}
	
	public void writeCodes(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			//BufferedWriter writer= new BufferedWriter(new FileWriter(fileName) /*, 8*1024*/);

			double printRate = 0.05;
			
			int deltaPrint = (int) ((float)codesString.size() * printRate);
			
			deltaPrint = (deltaPrint > 0)?deltaPrint:1;

			long i = 0;
			
			int iPrint = 0;
			
			while(!codesString.isEmpty()) {
				writer.write(codesString.poll() + "\r\n");

				if (Math.floorMod(i++, deltaPrint) == 0)
					System.out.println("\tEscribiendo...\t" + ((float)iPrint++ * 100.0 * printRate) + "%");
			}
			System.out.println("\tEscribiendo...\t" + 100.0 + "  %");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void test() {
		String[] opciones = {"A" , "C" , "E" , "F" , "H", "J" , "K" , "L" , "N" , "P" , "Q" , "R" ,
				"T" , "V" , "W" , "X" , "Y" , "Z" , "1" , "2" , "3" , "4" , "7" , "8" , "9"};
		
		Generate1 g = new Generate1(opciones, 6, 1000000);
		
		System.out.println(g.codQty);
		System.out.println(g.n);
		System.out.println(g.delta);
		long start , end;
		
		start = System.currentTimeMillis();
		
		try {
			g.generateCodes(false);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println("Generated Codes");
		
		g.codesToString();
		
		g.writeCodes("C:\\Users\\Felipe\\Desktop\\test.txt");
		System.out.println("File Writen !!!");
		
		end = System.currentTimeMillis();
		
		System.out.println("Elapsed Time:\t" + (end - start));
	}
	
	/*public void printCodes() {
		for(int i = 0 ; i < codes.size() ; i++)
			//System.out.println(codes.get(i).toString());
			System.out.println(codes.get(i).toString());
	}*/

	public static void main(String[] args) {
		//Generate1.test();
		
		FileInputStream in = null;
		
		try {
			in = new FileInputStream("config.ini");
			Properties p = new Properties();
			p.load(in);
			
			int codQty = Integer.parseInt(p.getProperty("Cantidad" , "10").replaceAll(" " , "")),
				digQty = Integer.parseInt(p.getProperty("Digitos" , "6").replaceAll(" " , ""));
			
			String[] opciones = p.getProperty("Caracteres" , "A,B,C").replaceAll(" " , "").split(",");
			
			boolean shuffleChar = Boolean.parseBoolean(p.getProperty("ShuffleChar","False").replaceAll(" " , "").toLowerCase()),
					shuffleCodes = Boolean.parseBoolean(p.getProperty("ShuffleCodes","False").replaceAll(" " , "").toLowerCase());
			
			System.out.println("Cantidad de Codigos:\t" + codQty);
			System.out.println("Cantidad de Cifras:\t" + digQty);
			System.out.println("Opciones por Cifra:\t" + Arrays.toString(opciones));
			System.out.println("Shuffle para Opciones:\t" + shuffleChar);
			System.out.println("Shuffle para Codigos:\t" + shuffleCodes);
			System.out.print("\r\n");
			
			System.out.println("Enter Para Continuar...");
			System.in.read();
			
			Date dt = new Date();
			SimpleDateFormat dFormat =  new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
			
			File f = new File("./Codigos/");
			
			if(!f.exists())
				f.mkdirs();
			
			Generate1 g = new Generate1(opciones, digQty, codQty);
			
			System.out.println("Cantidad Posible de Codigos:\t" + g.n);
			System.out.println("Separacion entre Codigos:\t" + g.delta);

			System.out.println();
			
			long start , end , tempStart , tempEnd;
			
			System.out.println();
			System.out.println("INICIO DE PROCESAMIENTO");
			System.out.println();
			
			start = System.currentTimeMillis();
			tempStart = start;
			
			if (shuffleChar)
				g.shuffleChar();
			
			System.out.println("Generando Codigos...");
			tempStart  = System.currentTimeMillis();
			g.generateCodes(shuffleCodes);
			tempEnd = System.currentTimeMillis();
			System.out.println("[" + (tempEnd - tempStart) + " ms]");
			System.out.println();
			
			System.out.println("Codificando Codigos...");
			tempStart  = System.currentTimeMillis();
			g.codesToString();
			tempEnd = System.currentTimeMillis();
			System.out.println("[" + (tempEnd - tempStart) + " ms]");
			System.out.println();
			
			String fileName = "./Codigos/Codigos(" + dFormat.format(dt) + ").txt"; 
			System.out.println("Escribiendo Codigos...");
			tempStart  = System.currentTimeMillis();
			g.writeCodes(fileName);
			tempEnd = System.currentTimeMillis();
			System.out.println("[" + (tempEnd - tempStart) + " ms]");
			System.out.println();
			
			end = System.currentTimeMillis();
			
			System.out.println("Tiempo Total:\t" + (end - start) + " ms");
			System.out.println();
			System.out.println("PROCESO FINALIZADO");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		try {
			System.in.read();
			System.in.read();
		} catch (IOException e) {
			
		}
	}
}