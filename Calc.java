package calcurator2022;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calc extends JFrame{
	private static final long serialVersionUID = 1L;
	JTextField field = new JTextField();
	static String status=null;
	static Deque<Double> stack =new ArrayDeque<Double>();
	static String wait=null;
	static int num=0;
	static int repeat=0;
	static String repeat_ope =null;
	static Double repeat_val =0.0;
	static int peri =0;
	static int minus =0;
	public Calc() {
		super("Calcurator");
		JPanel pane = (JPanel)getContentPane();
		pane.add(field,BorderLayout.NORTH);
		GridLayout layout = new GridLayout(5,4);
		JPanel keyPanel = new JPanel(layout);
		pane.add(keyPanel, BorderLayout.CENTER);
		String[] keys = {"AC","+/-","%","/",
						 "7","8","9","*",
						 "4","5","6","-",
						 "1","2","3","+",
						 "0",".","=","^"};
		for(int i = 0;i < keys.length; i++) {
			keyPanel.add(new JButton(new NumKey(keys[i],field)));
		}
	}
	
	public static void main(String[] args) {
		Calc calc = new Calc();
		calc.setDefaultCloseOperation(EXIT_ON_CLOSE);
		calc.setSize(250,300);
		calc.setVisible(true);
	}
}
