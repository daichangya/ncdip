package nc.ui.dip.effectdef;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
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
public class CreJPanel extends javax.swing.JPanel implements ItemListener{
	private JRadioButton jrdbtn1;
	private JCheckBox jcbishb;
	private JRadioButton jrdbtn2;

	DipDatachangeHVO hvo;
	public CreJPanel(DipDatachangeHVO hvo) {
		super();
		this.hvo=hvo;
		initGUI();
		
		//给JCheckBox增加监听事件
		jcbishb.addItemListener(this);
	}
	private ButtonGroup btng=new ButtonGroup();
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(360, 300));
			this.add(getJcbishb());
			this.add(getJrdbtn1());
			this.add(getJrdbtn2());
			btng.add(getJrdbtn1());
			btng.add(getJrdbtn2());
			jcbishb.setSelected(hvo.getSfhb()==null || hvo.getSfhb()==-1?false:true);
			jrdbtn1.setSelected(hvo.getSfhb()!=null && hvo.getSfhb()==1?false:true);
			jrdbtn2.setSelected(hvo.getSfhb()!=null && hvo.getSfhb()==1?true:false);
			if(jcbishb.isSelected()){
				jrdbtn1.setEnabled(true);
				jrdbtn2.setEnabled(true);
			}else{
				jrdbtn1.setEnabled(false);
				jrdbtn2.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JCheckBox getJcbishb() {
		if(jcbishb==null){
			jcbishb=new JCheckBox();
			jcbishb.setText("\u662f\u5426\u542f\u7528\u5408\u5e76");
			jcbishb.setPreferredSize(new java.awt.Dimension(339, 39));
		}
		return jcbishb;
	}

	public void setJcbishb(JCheckBox jcbishb) {
		this.jcbishb = jcbishb;
	}

	public JRadioButton getJrdbtn1() {
		if(jrdbtn1==null){
			jrdbtn1 = new JRadioButton();
			jrdbtn1.setText("\u76f8\u540c\u516c\u53f8+\u51ed\u8bc1\u7c7b\u522b");
			jrdbtn1.setPreferredSize(new java.awt.Dimension(341, 34));
		}

		return jrdbtn1;
	}

	public void setJrdbtn1(JRadioButton jrdbtn1) {
		this.jrdbtn1 = jrdbtn1;
	}

	public JRadioButton getJrdbtn2() {
		if(jrdbtn2==null){
			jrdbtn2 = new JRadioButton();
			jrdbtn2.setText("\u76f8\u540c\u516c\u53f8+\u51ed\u8bc1\u7c7b\u522b+\u5236\u5355\u4eba");
			jrdbtn2.setPreferredSize(new java.awt.Dimension(341, 33));
		}
		return jrdbtn2;
	}

	public void setJrdbtn2(JRadioButton jrdbtn2) {
		this.jrdbtn2 = jrdbtn2;
	}

	//2011-7-19 JCheckBox监听事件
	public void itemStateChanged(ItemEvent e) {
		JCheckBox jcb=(JCheckBox) e.getItem();
		if(jcb.isSelected()){
			jrdbtn1.setEnabled(true);
			jrdbtn2.setEnabled(true);
		}else{
			jrdbtn1.setSelected(false);
			jrdbtn1.setEnabled(false);
			
			jrdbtn2.setSelected(false);
			jrdbtn2.setEnabled(false);
		}
	}
}
