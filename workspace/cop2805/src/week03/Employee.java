package week03;

import java.text.DecimalFormat;

public class Employee {
private String m_first;
private String m_last;
private double m_salary;
private DecimalFormat m_decimalFormatter;
private static final String MONEY_PATTERN = "###,##0.00";

		public Employee() {
			
			this("New First", "New Last");
		}
		
		public Employee(String first, String last) {
			
		}
		
		public Employee(String first, String last, double salary) {
			
		}
}
