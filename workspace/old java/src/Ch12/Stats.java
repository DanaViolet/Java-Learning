package Ch12;

public class Stats {

	public Stats(){

	}

	public double Profit(int qty, double buy, double sell){
		double profit;
		profit = (sell - buy)*(double)qty;
		return profit;
	}
	

}
