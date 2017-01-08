package nc.ui.dip.effectdef;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.datarec.formatedlg.CopyFormatPanl;
import nc.ui.pub.beans.UIRefPane;
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
public class DataPXPane extends javax.swing.JPanel {
	private JLabel jLabel1;

	DipDatachangeHVO hvo;
	public DataPXPane(DipDatachangeHVO hvo) {
		super();

		this.hvo=hvo;
		initGUI();
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(400, 207));
			this.setLayout(null);
//			JLabel lab=new JLabel("提示：在做数据输出时，排序条件进行排序输出。 ");
//			lab.setBounds(40, 50, 300, 20);
//			this.add(lab);
		/*	{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("排序字段");
				jLabel1.setBounds(70, 100, 56, 25);
			}*/
			/*add(getOrgnizeRefPnl());
			getOrgnizeRefPnl().setBounds(150,100, 170, 25);
			if(hvo.getPxzd()!=null){
				getOrgnizeRefPnl().setPK(hvo.getPxzd());
				
			}*/
			add(getISpxCheckBox());
			if(hvo.getPxzd()!=null&&hvo.getPxzd().equals("Y")){
				getISpxCheckBox().setSelected(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	JCheckBox jc;
	public JCheckBox getISpxCheckBox(){
		if(jc==null){
			jc=new JCheckBox("凭证制单日期");
			jc.setBounds(100, 70, 200, 25);
		}
		return jc;
	}
	private UIRefPane refpnlOrgize = null;
	 public UIRefPane getOrgnizeRefPnl(){
			if (refpnlOrgize == null){
				refpnlOrgize = new UIRefPane(this);
				refpnlOrgize.setPreferredSize(new Dimension(170,22));
				refpnlOrgize.setRefInputType(1 /** 名称*/);
//				<nc.ui.bd.ref.DataDefinitbRefModel>
				DataDefinitbRefModel model = new DataDefinitbRefModel();
				refpnlOrgize.setRefModel(model);
				model.setWherePart("nvl(dip_datadefinit_h.dr,0)=0 and nvl(dip_datadefinit_b.dr,0)=0 and dip_datadefinit_b.pk_datadefinit_h='"+hvo.getBusidata()+"'");
//				ActionListener ac=refpnlOrgize.getUIButton().getActionListeners()[0];
//				refpnlOrgize.getUIButton().removeActionListener(refpnlOrgize.getUIButton().getActionListeners()[0]);
//				refpnlOrgize.getUIButton().addActionListener(ac);
//				refpnlOrgize.getUIButton().addActionListener(this);
//				refpnlOrgize.addValueChangedListener(headValueChangeListener);
				
			}
			return refpnlOrgize;
		}

}
