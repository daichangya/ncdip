package nc.ui.dip.effectdef;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import nc.vo.dip.datachange.DipDatachangeHVO;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class JDJPanel extends javax.swing.JPanel implements ItemListener{
	//private JTextField jrdbtn1;
	private JCheckBox jcbishb;
	//private JTextField jrdbtn2;
	private JCheckBox jcbisqyq;
	private JCheckBox jcbisqyh;
	private JComboBox jcomb1;
	private JComboBox jcomb2;

	DipDatachangeHVO hvo;
	public JDJPanel(DipDatachangeHVO hvo) {
		super();
		this.hvo=hvo;
		initGUI();
		
		//给JCheckBox增加监听事件
		jcbishb.addItemListener(this);
		jcbisqyq.addItemListener(this);
		jcbisqyh.addItemListener(this);
	}
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(360, 300));
			this.add(getJcbishb());
			this.setLayout(null);

			this.add(getJcbisqyq());
			this.add(getJcbisqyh());
			this.add(getJcomb1());
			this.add(getJcomb2());
			
			jcbishb.setSelected(hvo.getDataprecision()==null||hvo.getDataprecision().equals("N")?false:true);
			//jrdbtn1.setText(hvo.getBeforeprecision()==null?"":(hvo.getBeforeprecision()+""));
			jcomb1.setSelectedItem(hvo.getBeforeprecision()==null?2:(hvo.getBeforeprecision()));
			if(hvo.getBeforeprecision()!=null&&!"".equals(hvo.getBeforeprecision())){
				jcomb1.setEnabled(true);
			}else{
				jcomb1.setEnabled(false);
			}
			if(hvo.getAfterprecision()!=null&&!"".equals(hvo.getAfterprecision())){
				jcomb2.setEnabled(true);
			}else{
				jcomb2.setEnabled(false);
			}
			if(hvo.getBeforeprecision()!=null&&!"".equals(hvo.getBeforeprecision())&&jcbishb.isSelected()){
				jcbisqyq.setEnabled(true);
				jcbisqyq.setSelected(true);
			}else{
				jcbisqyq.setEnabled(false);
				jcbisqyq.setSelected(false);
			}
			//.setSelected(hvo.getSfhb()!=null && hvo.getSfhb()==1?false:true);
			jcomb2.setSelectedItem(hvo.getAfterprecision()==null?2:(hvo.getAfterprecision()));//.setSelected(hvo.getSfhb()!=null && hvo.getSfhb()==1?true:false);
			if(hvo.getAfterprecision()!=null&&!"".equals(hvo.getAfterprecision())&&jcbishb.isSelected()){
				jcbisqyh.setEnabled(true);
				jcbisqyh.setSelected(true);
			}else{
				jcbisqyh.setEnabled(false);
				jcbisqyh.setSelected(false);
			}
			if(jcbishb.isSelected()){
				jcbisqyq.setEnabled(true);
				jcbisqyh.setEnabled(true);
			}else{
				jcbisqyq.setEnabled(false);
				jcbisqyh.setEnabled(false);
				jcomb1.setEnabled(false);
				jcomb2.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JCheckBox getJcbishb() {
		if(jcbishb==null){
			jcbishb=new JCheckBox();
			jcbishb.setText("是否启用数据精度");
			jcbishb.setBounds(30, 30, 200, 20);
//			jcbishb.setPreferredSize(new java.awt.Dimension(339, 39));
		}
		return jcbishb;
	}

	public void setJcbishb(JCheckBox jcbishb) {
		this.jcbishb = jcbishb;
	}

//	public JTextField getJrdbtn1() {
//		if(jrdbtn1==null){
//			jrdbtn1 = new JTextField();
//			jrdbtn1.setHorizontalAlignment(JTextField.RIGHT );
//			jrdbtn1.setBounds(240, 90, 100, 20);
//			jrdbtn1.setDocument(new NumberDocument());
//		}
//
//		return jrdbtn1;
//	}

//	public void setJrdbtn1(JTextField jrdbtn1) {
//		this.jrdbtn1 = jrdbtn1;
//	}

//	public JTextField getJrdbtn2() {
//		if(jrdbtn2==null){
//			jrdbtn2 = new JTextField();
//			jrdbtn2.setBounds(240, 150, 100, 20);
//			jrdbtn2.setHorizontalAlignment(JTextField.RIGHT );
//			jrdbtn2.setDocument(new NumberDocument());
//		}
//		return jrdbtn2;
//	}

//	public void setJrdbtn2(JTextField jrdbtn2) {
//		this.jrdbtn2 = jrdbtn2;
//	}

	//2011-7-19 JCheckBox监听事件
	public void itemStateChanged(ItemEvent e) {
		JCheckBox jcb=(JCheckBox) e.getItem();
		String str=jcb.getText();
		if(str.equals("是否启用数据精度")){
			if(jcbishb.isSelected()){
			 jcbisqyq.setEnabled(true);
			 jcbisqyh.setEnabled(true);				
			}else{
				jcbisqyq.setEnabled(false);
				 jcbisqyh.setEnabled(false);
				 jcomb1.setEnabled(false);
				 jcomb2.setEnabled(false);
			}
		}
		if(str.equals("转换前精度")){
			if(jcbisqyq.isSelected()&&jcbisqyq.isSelected()){
				jcomb1.setEnabled(true);
			}
			if(!jcbisqyq.isSelected()){
				jcomb1.setEnabled(false);
			}
			
			//jrdbtn1.setText(2+"");
		}
//		else{
//			jrdbtn1.setText("");
//			jrdbtn1.setEnabled(false);
//		}
		if(str.equals("转换后精度")){
			if(jcbisqyh.isSelected()&&jcbisqyh.isSelected()){
				jcomb2.setEnabled(true);	
			}
			if(!jcbisqyh.isSelected()){
				jcomb2.setEnabled(false);
			}
			
			//jrdbtn2.setText(2+"");
		}
		//else
//		{
//			jrdbtn2.setText("");
//			jrdbtn2.setEnabled(false);
//		}
//		if(jcb.isSelected()){
//			jrdbtn1.setEnabled(true);
//			jrdbtn2.setEnabled(true);
//			jrdbtn1.setText(2+"");
//			jrdbtn2.setText(2+"");
//		}else{
//			jrdbtn1.setText("");
//			jrdbtn1.setEnabled(false);
//			
//			jrdbtn2.setText("");
//			jrdbtn2.setEnabled(false);
//		}
	}
	public JCheckBox getJcbisqyh() {
		if(jcbisqyh==null){
			jcbisqyh=new JCheckBox();
			jcbisqyh.setText("转换后精度");
			jcbisqyh.setBounds(60, 150, 100, 20);
			
		}
		return jcbisqyh;
	}
	public JCheckBox getJcbisqyq() {
		if(jcbisqyq==null){
			jcbisqyq=new JCheckBox();
			jcbisqyq.setText("转换前精度");
			jcbisqyq.setBounds(60, 90, 100, 20);
			
		}
		return jcbisqyq;
	}
	public JComboBox getJcomb1() {
		if(jcomb1==null){
			Integer i[]={1,2,3,4,5,6,7,8};
			jcomb1=new JComboBox(i);
			jcomb1.setBounds(240, 90, 100, 20);
			
		}
		return jcomb1;
	}
	public JComboBox getJcomb2() {
		if(jcomb2==null){
			Integer i[]={1,2,3,4,5,6,7,8};
			jcomb2=new JComboBox(i);
			jcomb2.setBounds(240, 150, 100, 20);
			
		}
		return jcomb2;
	}
	
	
}
