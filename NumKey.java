package calcurator2022;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextField;

public class NumKey extends AbstractAction {
	private static final long serialVersionUID = 1L;
	JTextField field;
	NumKey(String key,JTextField field){
		putValue(Action.NAME,key);
		this.field=field;
	}
	
	public void actionPerformed(ActionEvent e) {
		Double field_value;
		String input =(String) getValue(Action.NAME);
		if (input.equals("AC")) {
			field.setText("0");
			Calc.stack.clear();
			Calc.num =0;
			Calc.repeat_ope = null;
			Calc.peri =0;
			Calc.repeat=0;
			Calc.status = null;
		}
		else if(field.getText().equals("error")) {
			return;
		}
		else if(input.equals("+")) {
			if(Calc.stack.size() == 0 ){//突然の＋は無視
				return;
			}
			else if (Calc.stack.size() == 1) {
				Calc.num=0;
				Calc.peri =0;
				Calc.repeat=0;
				Calc.status="plus";
				return;
			}
			this.checkstatus(Calc.stack.removeFirst(), Double.toString(Calc.stack.removeFirst()));
			this.checkwait();
			Calc.status = "plus";
			Calc.num =0;
			Calc.peri =0;
			Calc.repeat=0;
		}
		else if (input.equals("-")) {
			if(Calc.stack.size() ==0) {//突然の-は次に入力される数値を-1倍
				Calc.minus =1;
				return;
			}
			else if (Calc.stack.size() == 1) {
				Calc.num=0;
				Calc.peri =0;
				Calc.repeat=0;
				Calc.status="minus";
				return;
			}
			this.checkstatus(Calc.stack.removeFirst(), Double.toString(Calc.stack.removeFirst()));
			this.checkwait();
			Calc.status = "minus";
			Calc.num =0;
			Calc.peri =0;
			Calc.repeat=0;
		}
		else if(input.equals("*")) {
			if(Calc.stack.size() == 0) {//突然の*は無視
				return;
			}
			else if (Calc.stack.size() == 1) {
				Calc.num=0;
				Calc.peri =0;
				Calc.repeat=0;
				Calc.status="times";
				return;
			}
			if(Calc.status == "times" || Calc.status == "divide" || Calc.status == "pow") {
				field_value = Calc.stack.removeFirst();
				this.checkstatus(field_value,Double.toString(Calc.stack.removeFirst()));
				Calc.status = "times";
			}
			else{
				Calc.wait = Calc.status;
				Calc.status = "times";
			}
			Calc.num =0;
			Calc.peri =0;
			Calc.repeat=0;
		}
		else if (input.equals("/")) {
			if(Calc.stack.size() == 0) {//突然の/は無視
				return;
			}
			else if (Calc.stack.size() == 1) {
				Calc.num=0;
				Calc.peri =0;
				Calc.repeat=0;
				Calc.status="divide";
				return;
			}
			if(Calc.status == "times" || Calc.status == "divide" || Calc.status == "pow") {
				field_value = Calc.stack.removeFirst();
				this.checkstatus(field_value,Double.toString(Calc.stack.removeFirst()));
				Calc.status = "divide";
			}
			else{
				Calc.wait = Calc.status;
				Calc.status = "divide";
			}
			Calc.num =0;
			Calc.peri =0;
			Calc.repeat=0;
		}
		else if(input.equals("^")) {
			if(Calc.stack.size() == 0) {//突然の^は無視
				return;
			}
			else if (Calc.stack.size() == 1) {
				Calc.num=0;
				Calc.peri =0;
				Calc.repeat=0;
				Calc.status="pow";
				return;
			}
			if(Calc.status == "times" || Calc.status == "divide" ||Calc.status == "pow") {
				field_value = Calc.stack.removeFirst();
				this.checkstatus(field_value,Double.toString(Calc.stack.removeFirst()));
				Calc.status = "pow";
			}
			else{
				Calc.wait = Calc.status;
				Calc.status = "pow";
			}
			Calc.num =0;
			Calc.peri =0;
			Calc.repeat=0;
		}
		else if (input.equals("%")) {
			if(Calc.stack.size() == 0) {
				return;
			}
			else {
				BigDecimal B_value = BigDecimal.valueOf(Calc.stack.removeFirst());
				BigDecimal val = BigDecimal.valueOf(100);
				B_value = B_value.divide(val,9,java.math.RoundingMode.HALF_UP);
				if(B_value.compareTo(BigDecimal.valueOf(Math.pow(10, -8))) == 1 && B_value.compareTo(BigDecimal.valueOf(0)) == 1) {
					field.setText(B_value.toString());
					this.setText(B_value.doubleValue());
					//Calc.status = null;
					Calc.stack.addFirst(B_value.doubleValue());
				}
				else if (B_value.compareTo(BigDecimal.valueOf(Math.pow(10, -8))) == -1 && B_value.compareTo(BigDecimal.valueOf(0)) == -1) {
					field.setText(B_value.toString());
					this.setText(B_value.doubleValue());
					//Calc.status = null;
					Calc.stack.addFirst(B_value.doubleValue());
				}
				else {
					Calc.stack.addFirst(0.0);
					//Calc.status =null;
					field.setText("0");
				}
			}
		}
		else if (input.equals(".")) {
			if(Calc.stack.size() != 0) {
				if(Calc.num == 0 && Calc.status != null) {
					Calc.stack.addFirst(0.0);
					field.setText("0.");
					Calc.peri =1;
				}
				else {
					field_value = Calc.stack.getFirst();
					if(this.judgeInteger(field_value) == 0){
						Calc.peri = 1;
						field.setText(field_value.intValue() + ".");
					}
				}
			}
			else {
				Calc.stack.addFirst(0.0);
				field.setText("0.");
				Calc.status = null;
				Calc.peri = 1;
			}	
		}
		else if(input.equals("=")) {//連打で直前の演算を繰り返す
			if(Calc.repeat== 0) {
				if(Calc.stack.size() == 1 && Calc.status == null) {
					return;
				}
				Calc.repeat_ope = Calc.status;
				Calc.repeat_val = Calc.stack.removeFirst();
			}
			else{
				Calc.status = Calc.repeat_ope;
			}
			if(Calc.stack.size() == 0) {
				field_value = Calc.repeat_val;
			}
			else{
				field_value = Calc.stack.removeFirst();
			}
			this.checkstatus(Calc.repeat_val, Double.toString(field_value));
			if(Calc.stack.size() != 1) {this.checkwait();}
			Calc.repeat=1;
			Calc.num = 0;
			Calc.peri=0;
		}
		else if(input.equals("+/-")) {
			if(Calc.stack.isEmpty()) {
				field_value = (double) 0;
			}
			else {
				field_value = Calc.stack.removeFirst()*-1;
			}
			Calc.peri = 0;
			Calc.stack.addFirst(field_value);
			this.setText(field_value);
		}
		else {//数字入力　適切な値をpushする
			if(Calc.num == 0 ) {//1桁目の数字
				if(Calc.peri != 0) {
					if(Calc.stack.getFirst() >= 0) {
						field_value = Calc.stack.removeFirst() + Double.parseDouble(input)/Math.pow(10, Calc.peri);
					}
					else {
						field_value = Calc.stack.removeFirst() - Double.parseDouble(input)/Math.pow(10, Calc.peri);
					}
					if(Calc.minus ==1) {
						field_value = field_value * -1;
						Calc.minus =0;
					}
					Calc.stack.addFirst(field_value);
					Calc.peri++;
					field.setText(String.format("%." + Integer.toString(Calc.peri-1) +"f", field_value));
					Calc.num=1;
				}
				else{
					if(Calc.repeat == 1) {//=の後に次の計算を始めるとき
						Calc.stack.clear();
						Calc.peri =0;
						Calc.repeat=2;
						Calc.status = null;
					}
					field_value = Double.parseDouble(input);
					if(Calc.minus ==1) {
						field_value = field_value * -1;
						input = "-" + input;
						Calc.minus =0;
					}
					field.setText(input);
					Calc.stack.addFirst(field_value);
					Calc.num = 1;
				}
			}
			else {//２桁目以降の数字
				if(Calc.peri != 0) {
					field_value = Calc.stack.removeFirst();
					if(field_value >= 0) {
						field_value = field_value + Double.parseDouble(input)/Math.pow(10, Calc.peri);
					}
					else {
						field_value = field_value - Double.parseDouble(input)/Math.pow(10, Calc.peri);
					}
					Calc.stack.addFirst(field_value);
					Calc.peri++;
					field.setText(String.format("%." + Integer.toString(Calc.peri-1) +"f", field_value));
				}
				else {
					field_value = Double.parseDouble(Calc.stack.removeFirst().intValue() +input);
					Calc.stack.addFirst(field_value);
					field.setText(Double.toString(Calc.stack.getFirst().intValue()));
					Double Field = field_value;
					if(this.judgeInteger(field_value) == 0) {field.setText(Integer.toString(Field.intValue()));}
				}
			}
		}
	}
	
	public void plus(double field_value,String input) {
		BigDecimal val1 = BigDecimal.valueOf(field_value);
		BigDecimal val2 = BigDecimal.valueOf(Double.parseDouble(input));
		val1 = val1.add(val2);
		field.setText(val1.toString());
		this.setText(val1.doubleValue());
		Calc.status = null;
		Calc.stack.addFirst(val1.doubleValue());
	}
	public void minus(double field_value,String input){
		BigDecimal val1 = BigDecimal.valueOf(field_value);
		BigDecimal val2 = BigDecimal.valueOf(Double.parseDouble(input));
		val1 = val2.subtract(val1);
		field.setText(val1.toString());
		this.setText(val1.doubleValue());
		Calc.status = null;
		Calc.stack.addFirst(val1.doubleValue());
	}
	public void times(double field_value,String input) {
		BigDecimal val1 = BigDecimal.valueOf(field_value);
		BigDecimal val2 = BigDecimal.valueOf(Double.parseDouble(input));
		val1 = val2.multiply(val1);
		field.setText(val1.toString());
		this.setText(val1.doubleValue());
		Calc.status = null;
		Calc.stack.addFirst(val1.doubleValue());
	}
	public void divide(double field_value,String input){
		if(field_value == 0) {
			field.setText("error");
			Calc.stack.clear();
			Calc.num =0;
			Calc.repeat_ope = null;
			Calc.peri =0;
			return;
		}
		BigDecimal val1 = BigDecimal.valueOf(field_value);
		BigDecimal val2 = BigDecimal.valueOf(Double.parseDouble(input));
		val1 = val2.divide(val1,9,java.math.RoundingMode.HALF_UP);
		field.setText(val1.toString());
		this.setText(val1.doubleValue());
		Calc.status = null;
		Calc.stack.addFirst(val1.doubleValue());
	}

	public void pow(double field_value,String input) {
		if(field_value < 1 && Double.parseDouble(input) < 0 ) {
			Calc.status = null;
			field.setText("error");
			Calc.stack.clear();
			return;
		}
		field_value = Math.pow(Double.parseDouble(input),field_value);
		BigDecimal B_value = BigDecimal.valueOf(field_value);
		if(B_value.compareTo(BigDecimal.valueOf(Math.pow(10, -8))) == 1 && B_value.compareTo(BigDecimal.valueOf(0)) == 1) {
			field.setText(B_value.toString());
			this.setText(B_value.doubleValue());
			//Calc.status = null;
			Calc.stack.addFirst(B_value.doubleValue());
		}
		else if (B_value.compareTo(BigDecimal.valueOf(Math.pow(10, -8))) == -1 && B_value.compareTo(BigDecimal.valueOf(0)) == -1) {
			field.setText(B_value.toString());
			this.setText(B_value.doubleValue());
			//Calc.status = null;
			Calc.stack.addFirst(B_value.doubleValue());
		}
		else {
			Calc.stack.addFirst(0.0);
			//Calc.status =null;
			field.setText("0");
		}
		Calc.status = null;
	}
	
	
	public int judgeInteger(Double in){
		if(in - in.intValue() == 0.0){
			return 0;
		}
		else {
			return 1;
		}
	}
	
	public void checkstatus(double field_value,String input) {
		if(Calc.status == null) {
			return;
		}
		else if (Calc.status =="plus") {
			this.plus(field_value, input);
		}
		else if(Calc.status =="minus") {
			this.minus(field_value, input);
		}
		else if(Calc.status =="times") {
			if(Math.log10(Double.parseDouble(input)) + Math.log10(field_value) > 130) {
				field.setText("error");
				Calc.stack.clear();
				Calc.status = null;
			}
			else {
				this.times(field_value,input);
			}
		}			
		else if(Calc.status =="divide") {
			if(Math.log10(field_value) - Math.log10(Double.parseDouble(input))  > 130) {
				field.setText("error");
				Calc.stack.clear();
				Calc.status = null;
			}
			else {
				this.times(field_value,input);
			}
			this.divide(field_value, input);
		}
		else if(Calc.status == "pow") {
			if(field_value * Math.log10(Double.parseDouble(input)) > 130) {
				field.setText("error");
				Calc.stack.clear();
				Calc.status = null;
			}
			else {
				this.pow(field_value,input);
			}
		}
		if (Calc.stack.isEmpty() || Calc.stack.getFirst() > Math.pow(10,125)) {
			field.setText("error");
		}
		else if (Calc.stack.getFirst() < Math.pow(10,-8) && Calc.stack.getFirst() > 0.0) {
			Calc.stack.removeFirst();
			Calc.stack.addFirst(0.0);
			field.setText("0");
		}
		else if (Calc.stack.getFirst() > Math.pow(10,-8) && Calc.stack.getFirst() < 0.0) {
			Calc.stack.removeFirst();
			Calc.stack.addFirst(0.0);
			field.setText("0");
		}
	}
	public void checkwait() {
		if(Calc.wait == null) {
			return;
		}
		else if (Calc.wait =="plus") {
			this.plus(Calc.stack.removeFirst(), Double.toString(Calc.stack.removeFirst()));
		}
		else if(Calc.wait =="minus") {
			this.minus(Calc.stack.removeFirst(), Double.toString(Calc.stack.removeFirst()));
		}
		Calc.wait = null;
		if (Calc.stack.getFirst() > Math.pow(10,125)) {
			field.setText("error");
		}
		else if (Calc.stack.getFirst() < Math.pow(10,-8) && Calc.stack.getFirst() > 0.0) {
			Calc.stack.removeFirst();
			Calc.stack.addFirst(0.0);
			field.setText("0");
		}
		else if (Calc.stack.getFirst() > Math.pow(10,-8) && Calc.stack.getFirst() < 0.0) {
			Calc.stack.removeFirst();
			Calc.stack.addFirst(0.0);
			field.setText("0");
		}
	}
	public void zeroremove(double field_value,int digit) {
		if(!(Double.toString(field_value).contains("E-4")) && !(Double.toString(field_value).contains("E-5")) && !(Double.toString(field_value).contains("E-6"))) {
			if(String.format("%." + Integer.toString(9-digit) +"f", field_value).length() > Double.toString(field_value).length()) {
				field.setText(Double.toString(field_value));
			}
		}
		else {
			if(Double.toString(field_value).contains(".")) {
				while(field.getText().charAt(field.getText().length()-1) == '0') {
					field.setText(field.getText().substring(0, field.getText().length()-1));
				}
			}
		}
	}
	public void setText(double field_value) {
		Double Field = field_value;
;		int digit;
		digit = String.valueOf(Field.intValue()).length();
		if(Field.intValue() == 0) {digit--;}
		if(digit<10) {
			field.setText(String.format("%." + Integer.toString(9-digit) +"f", field_value));//四捨五入してない
			zeroremove(field_value,digit);
		}
		if(this.judgeInteger(field_value) == 0) {field.setText(Integer.toString(Field.intValue()));}
		if(digit >=10 && field.getText().substring(1,2) != ".") {
			field.setText(field.getText().substring(0,1) + "." + field.getText().substring(1,2) + "E+" +digit);//四捨五入してない
		}
		else if(digit >= 10) {
			field.setText(field.getText().substring(0,1) + "." + field.getText().substring(2,3) + "E+" +digit);//四捨五入してない
		}
	}
}
