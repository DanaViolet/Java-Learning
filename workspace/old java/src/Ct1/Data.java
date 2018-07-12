package Ct1;
import java.util.Arrays;

public class Data {
public int ary[];
public int sum;

	public Data(){
		ary = null;

	}

	public Data(int n){
		ary = new int[n];
		for (int i=0; i< n; i++){
			ary[i] = 0;
		}
	}

	public void InitIntegers(){
		for (int i=0; i< ary.length; i++){
			ary[i] = i;
		}
	}
	public void InitEven(){
		int k = 0;
		for (int i = 0; i< ary.length; i++){
			ary[i] = k;
			k+= 2;
		}
	}
	public void InitOdd(){
		int k = 1;
		for (int i = 0; i< ary.length; i++){
			ary[i] = k;
			k+= 2;
		}
		
	}
	public void InitFib(){
		ary[0] = 0;
		ary[1] = 1;
		for (int i = 2; i< ary.length; i++){
			ary[i] = ary[i-1] + ary[i-2];
		}
	}

	public void Puts()
	{
	System.out.println(Arrays.toString(ary));
	//System.out.println("The sum of the array is: " + Sum());
	//System.out.println("The average of the array is: " + Average());

	}

	public int Sum()
	{
		int sum = 0;

		for (int i = 0; i < ary.length; i++)
		{
			sum += ary[i];
		}
		return sum;
	}

	public int Search(int nbr){
		int index = -1;
		for (int i=0; i<ary.length; i++){
			index = i;
			if (ary[i] == nbr)
				return index;
		}
		return index;
	}
	public void InitFact(){
		
		ary[0] = 0;
		ary[1] = 1;
		for (int i = 2; i< ary.length; i++){
			ary[i] = Factorial(i);
		}
	}
	
	public int Factorial(int nbr){
		int fac = 1;
		for (int i = nbr; i>1; i--){
			fac *= i;
		}
		return fac;
	}
}
