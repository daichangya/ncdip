package nc.ui.dip.datadefcheck;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.util.dip.sj.SJUtil;

//JTextField   jtf=new   JTextField(); 
//jtf.setDocument(new   NumberDocument(jtf)); 

public class StringDocument extends PlainDocument {
	JTextField jtf = null;

	String temp = "DIP_DD_";

	/*
	 * public NumberDocument(JTextField jtf) { this.jtf=jtf; }
	 */
	public StringDocument(JTextField jtf, String temp) {
		this.jtf = jtf;
		this.temp=temp;
	}

	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {

		boolean isfit = true;
		if(SJUtil.isNull(str)/*||jtf.getText().length()<temp.length()*/){
			isfit=false;
		}
		if(!SJUtil.isNull(jtf.getText())&&jtf.getText().length()>0&&offs<temp.length()){
			isfit=false;
		}
		char[] source = str.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (((source[i])+"").matches("[\u4E00-\u9FA5]")) {
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

	@Override
	protected void removeUpdate(DefaultDocumentEvent chng) {
		if(chng.getLength()==1){
			if(jtf.getText().length()<(temp.length()+1)){
				return;
			}
		}
			super.removeUpdate(chng);
	}

	@Override
	public void remove(int arg0, int arg1) throws BadLocationException {
		if(arg0<temp.length()||(arg0+arg1)<=temp.length()){
			return;
		}else{
			super.remove(arg0, arg1);
		}
	}
}
