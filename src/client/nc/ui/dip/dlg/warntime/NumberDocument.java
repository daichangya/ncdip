package nc.ui.dip.dlg.warntime;

import javax.swing.*;
import javax.swing.text.*;

import nc.util.dip.sj.SJUtil;

//JTextField   jtf=new   JTextField(); 
//jtf.setDocument(new   NumberDocument(jtf)); 

public class NumberDocument extends PlainDocument {
	JTextField jtf = null;

	String temp = "h";

	/*
	 * public NumberDocument(JTextField jtf) { this.jtf=jtf; }
	 */
	public NumberDocument(JTextField jtf, String temp) {
		this.jtf = jtf;
		if (SJUtil.isNull(temp)) {
			this.temp = "h";
		} else if (temp.equals("m")) {
			this.temp = "m";
		} else if (temp.equals("s")) {
			this.temp = "s";
		} else {
			this.temp = "h";
		}
	}

	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {

		boolean isfit = true;
		if (offs > 1) {
			isfit = false;
		}
		char[] source = str.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (!Character.isDigit(source[i])) {
				isfit = false;
			}
		}
		if (!isfit) {
			return;
		}
		int h = Integer.parseInt(str);
		/*if (offs == 0) {
			if (this.temp.equals("h")) {
				if (h > 1) {
					isfit = false;
				}
			} else {
				if (h > 6) {
					isfit = false;
				}
			}
		} else {*/
			String ss=jtf.getText()+str;
			h=Integer.parseInt(ss);
			if (this.temp.equals("h")) {
				if (h > 24) {
					isfit = false;
				}
			} else {
				if (h > 60) {
					isfit = false;
				}
			}
//		}
		if (isfit) {
			super.insertString(offs, str, a);
		}

	}
}
