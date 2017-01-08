package nc.ui.dip.effectdef;

import javax.swing.*;
import javax.swing.text.*;

import nc.util.dip.sj.SJUtil;

//JTextField   jtf=new   JTextField(); 
//jtf.setDocument(new   NumberDocument(jtf)); 

public class NumberDocument extends PlainDocument {
	JTextField jtf = null;



	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {

		boolean isfit = true;
		if(SJUtil.isNull(str)/*||jtf.getText().length()<temp.length()*/){
			isfit=false;
		}
		char[] source = str.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (!((source[i])+"").matches("[0-9]")&&!((source[i])+"").equals("")) {
				isfit = false;
			}
		}
		if (!isfit) {
			return;
		}
		if (isfit) {
			super.insertString(offs, str, a);
		}
		
	}


}
