
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GA {

	// metoda konwertujaca liczbe binarna na liczbe decimal
	public static int bin2dec(int[] tablica) {
		int decimal = 0;
	    int power = 0;
	    for(int i = 0 ; i < tablica.length ; i++){
	            int tmp = tablica[i]%10;
	            decimal += tmp*Math.pow(2, power);
	            power++;
	    }
	    return decimal;
	}
	
	// metoda losuj�ca liczby binarne do tablicy
	public static int[] binaryRandom(int[] tablica) {
		Random gen = new Random();
		for(int i=0;i<tablica.length;i++) {
			tablica[i]=gen.nextInt(2);
		}
	    return tablica;
	}
	
	//metoda obliczajaca wartosc funkcji dla okre�lonego chromosomu i bitow odpowiadajacym jednemu osobnikowi
	public static double obliczWarFun(int[] chromosom, int bityosobnika,int nr) {
		int[] geny = new int[chromosom.length/bityosobnika];
		int[] zastepcza= new int[bityosobnika];
		int k=0;
		for(int j=0;j<(chromosom.length/bityosobnika);j++) {
			for(int i=0; i<zastepcza.length;i++) {		
				zastepcza[i]=chromosom[k];
				k++;
			}
			geny[j]=bin2dec(zastepcza);
		}
		int m=8;
	    double ai=0;
		double bi=0;
		double x1,x2,x11,f=0;
		if(nr==1)
		{
			ai=-5;
			bi= 5;
			double delta=(bi-ai)/(Math.pow(2, m)-1);
			x1=(Math.round((ai+geny[0]*delta)*100000));
			x1/=100000;
			x2=(Math.round((ai+geny[1]*delta)*100000));
			x2/=100000;
			f=-20*Math.exp(-0.2*Math.sqrt(0.5*((x1*x1)+(x2*x2))))-Math.exp(0.5*(Math.cos(2*Math.PI*x1)+Math.cos(2*Math.PI*x2)))+Math.E+20;
		}else if(nr==2)
		{
			ai=-5;
			bi= 5;
			double delta=(bi-ai)/(Math.pow(2, m)-1);
			
			for(int i=0; i<geny.length; i++)
			{
				x1=(Math.round((ai+geny[i]*delta)*100000));
				x1/=100000;
				f=f+Math.pow(x1, 2);
			}
			
		}
		else if(nr==3)
		{
			 	ai = 1;
			 	bi = 0.5;
			 
			 	double delta=(bi-ai)/(Math.pow(2, m)-1);
		        for(int i=0; i<geny.length-1; i++)
		        {
		        	 
					  x1=(Math.round((ai+geny[i]*delta)*100000));
					  x1/=100000;
					  x11=(Math.round((ai+geny[i+1]*delta)*100000));
					  x11/=100000;
		             f=f+(100*(Math.pow(x11 - Math.pow(x1,2),2) + Math.pow((1-x1), 2))); 
		        
		        }
		}else if(nr==4)
		{
			ai=-4.5;
			bi= 4.5;
			double delta=(bi-ai)/(Math.pow(2, m)-1);
			x1=(Math.round((ai+geny[0]*delta)*100000));
			x1/=100000;
			x2=(Math.round((ai+geny[1]*delta)*100000));
			x2/=100000;
			f=Math.pow((1.5-x1+x1*x2),2)+Math.pow((2.25-x1+x1*x2*x2),2)+Math.pow((2.625-x1+x1*x2*x2*x2),2);
		}else if(nr==5)
		{
			ai=-2;
			bi= 2;
			double delta=(bi-ai)/(Math.pow(2, m)-1);
			x1=(Math.round((ai+geny[0]*delta)*100000));
			x1/=100000;
			x2=(Math.round((ai+geny[1]*delta)*100000));
			x2/=100000;
			f= (1 + (Math.pow(x1+x2+1,2)*(19-14*x1 + 3*Math.pow(x1,2)-14*x2 + 6*x1*x2 + 3*(Math.pow(x2,2)))))*(30 + (Math.pow(2*x1 - 3*x2,2))*(18-32*x1 + 12*Math.pow(x1,2)+ 48*x2 -36*x1*x2 + 27*Math.pow(x2,2)));
		}else if(nr==6)
		{
			ai=-10;
			bi= 10;
			double delta=(bi-ai)/(Math.pow(2, m)-1);
			x1=(Math.round((ai+geny[0]*delta)*100000));
			x1/=100000;
			x2=(Math.round((ai+geny[1]*delta)*100000));
			x2/=100000;
			f= Math.pow(x1 + 2*x2 - 7,2) + Math.pow(2*x1 + x2 - 5,2);
		}
		return f;
		
	}
	
	//metoda generuj�ca populacje osobnikow o podanej wielkosci n
	public static ArrayList<int[]> wygenerujOsobniki (int n,int a) {
		ArrayList<int[]> osobniki = new ArrayList<int[]>();
		if(a==1||a==4||a==5||a==6) 
		{
			for(int i=0;i<n;i++) {
				osobniki.add(binaryRandom(new int[16]));
			}
		}else
		if(a==2||a==3)
		{
			for(int i=0;i<n;i++) 
			{
				osobniki.add(binaryRandom(new int[80]));
			}
		}
		return osobniki;
	}
	
	//metoda s�u��ca dobieraniu najlepiej przystosowanych osobnik�w do populacji z pomoc� prawdopodobie�stwa
	public static ArrayList<int[]> metodaRuletki(ArrayList<int[]> a, int nr) {
		ArrayList<int[]> P2 = new ArrayList<int[]>();
		Random r = new Random();
		double[] warfun = new double[a.size()];
		double[] pom = new double[a.size()];
		double suma=0,suma2=0;
		double p=0,s=0;
		int i=0;
		
		for(i=0;i<a.size();i++) {
			warfun[i]=obliczWarFun(a.get(i),8,nr);
			pom[i]=warfun[i];
		}
		
		
		for(i=0;i<pom.length;i++) {
        	pom[i]=1/pom[i];
        	suma=suma+pom[i];
		}
		
		for(i=0;i<pom.length;i++) {
        	pom[i]=pom[i]/suma;
		}
	
        for(i=0;i<warfun.length;i++) {
        	p=r.nextDouble();
			for(int k=0;k<warfun.length;k++) {
			    s+=pom[k];
				if(s>p) {
	        		    P2.add(a.get(k));
	        		    s=0;
	        		    k=a.size();
						} 	     
      	        }
           }
        return P2;			      		
      }
	
	//metoda krzyzujaca osobnika n oraz m w podanej populacji
	public static ArrayList<int[]> krzyzowanie (int n, int m, ArrayList<int[]> populacja) {
		Random rand = new Random();
		int[] chromosom = populacja.get(n);
		int[] chromosom2 = populacja.get(m);
		int[] potomek = new int[chromosom.length];
		int[] potomek2 = new int[chromosom.length];

		int p1,p2;
		p1=rand.nextInt((chromosom.length/2)+1);
		p2=rand.nextInt((chromosom.length/2)+1)+(chromosom.length/2)+1;
		for(int j1=0;j1<chromosom.length;j1++) 
			{
				if(j1>p2)
				{
					potomek2[j1]=chromosom[j1];
					potomek[j1]=chromosom2[j1];
				}
				if(j1>p1)
				{ 
					potomek[j1]=chromosom[j1];
					potomek2[j1]=chromosom2[j1];
				}
				else 
				{
					potomek2[j1]=chromosom[j1];
					potomek[j1]=chromosom2[j1];
				}
			}
		populacja.set(n, potomek);
		populacja.set(m, potomek2);
		return populacja;
	}
	
	//metoda mutujaca osobnika w podanej populacji
	public static ArrayList<int[]> mutacja (int n, ArrayList<int[]> populacja){
		Random rand = new Random();
		int[] chromosom = populacja.get(n);
		for(int j2=0;j2<chromosom.length;j2++) 
		{
		if(rand.nextDouble()<0.1) 
		{
			if(chromosom[j2]==0)
			{
				chromosom[j2]=1;
			}
			else chromosom[j2]=0;
			
		}
		populacja.set(n, chromosom);
	}			
		return populacja;
	}
	
	//algorytm genetyczny, generuj�cy danej wielko�ci populacje i wykonuj�cy na niej ev liczb� ewaluacji
	public static ArrayList<int[]> algorytmGenetyczny(int wielkosc, int ev,int n){
		
		Random rand = new Random();
		double k=0.9; //prawdopodobie�stwo krzy�owania
		double m=0.02; //prawdopodobie�stwo mutacji
		ArrayList<int[]> populacja= wygenerujOsobniki(wielkosc,n); //inicjacja - generowanie populacji o danej wielko�ci
		double[] srednia = new double[600];
	
		for(int z=1; z<=20; z++)
		{
			populacja= wygenerujOsobniki(wielkosc,n);
			double min=0;
			
			for(int i=0;i<ev;i++) { //warunek zatrzymania
				populacja=metodaRuletki(populacja,n); //selekcja chromosom�w
				 min=obliczWarFun(populacja.get(0),8,n);
	
				for(int j=0;j<populacja.size();j++)
				{	
					if(rand.nextDouble()<=k)  //okre�lenie prawdopodobie�stwa - krzy�owanie
					{
						int r=rand.nextInt(populacja.size()); 
						populacja=krzyzowanie(j,r,populacja);  //zastosowanie operatora genetycznego - krzy�owanie
					}
					if(rand.nextDouble()<=m)  //okre�lenie prawdopodobie�stwa - mutacja
					{
						populacja=mutacja(j,populacja);  //zastosowanie operatora genetycznego - mutacja
					}
					if(obliczWarFun(populacja.get(j),8,n)<min)
					{
						min=obliczWarFun(populacja.get(j),8,n);  //najlepszy chromosom w danej ewaluacji
					}
				}	
				
				srednia[i]=srednia[i]+ min;
			}
			
			
		}
		
		for(int i=0; i<ev; i++)
		{
			System.out.println(srednia[i]/20);
		}
		return populacja; //wyprowadzenie populacji po ostatecznej ewaluacji
	}
	
	public static void main(String[] args) {
		System.out.println("Funkcje testowe:\n1. Ackley\n2. Sphere\n3. Rosenbrok\n4. Beale\n5. Goldstein-Price\n6. Booth\nWpisz numer funkcji, ktrrą chcesz zoptymalizować: ");
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		
		algorytmGenetyczny(600,600,n);
	}

}