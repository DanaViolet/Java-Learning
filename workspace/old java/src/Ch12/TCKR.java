package Ch12;

public class TCKR extends Stats{
	
	public TCKR(){
		
	}
	
	public TCKR(String _stock,
				int _qty,
				double _buy,
				double _sell){
		
				stock = _stock;
				qty = _qty;
				buy = _buy;
				sell = _sell;
				pNext = null;
				
				profit = Profit(qty, buy, sell);
	}
	
	String stock;
	int qty;
	double buy;
	double sell;
	double profit;
	TCKR pNext;

}
