package nc.ui.dip.effectdef;

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
public class NewJPanel extends javax.swing.JPanel {
	private JLabel jLabel1;
	private JLabel jlblbilltype;
	private JTextField jtfbilltype;
	private JLabel jlblsender;
	private JTextField jtfoperation;
	private JLabel jlblproc;
	private JTextField jtfsender;
	private JTextField jtfproc;
	private JTextField jtfisexchange;
	private JLabel jlbloperation;
	private JLabel jlblisexchange;
	private JTextField jtfreplace;
	private JLabel jlblreplace;
	private JTextField jTextField1;

	DipDatachangeHVO hvo;
	public NewJPanel(DipDatachangeHVO hvo) {
		super();

		this.hvo=hvo;
		initGUI();
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(400, 207));
			this.setLayout(null);
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("roottag");
				jLabel1.setBounds(12, 16, 56, 25);
			}
			{
				jTextField1 = new JTextField();
				this.add(jTextField1);
				jTextField1.setText("voucher");
				jTextField1.setBounds(87, 16, 100, 28);
			}
			{
				jlblbilltype = new JLabel();
				this.add(jlblbilltype);
				jlblbilltype.setText("billtype");
				jlblbilltype.setBounds(199, 13, 56, 30);
			}
			{
				jtfbilltype = new JTextField();
				this.add(jtfbilltype);
				jtfbilltype.setText("gl");
				jtfbilltype.setBounds(261, 16, 102, 25);
			}
			{
				jlblreplace = new JLabel();
				this.add(jlblreplace);
				jlblreplace.setText("replace");
				jlblreplace.setBounds(12, 61, 56, 24);
			}
			{
				jtfreplace = new JTextField();
				this.add(jtfreplace);
				jtfreplace.setText("Y");
				jtfreplace.setBounds(87, 64, 100, 28);
			}
			{
				jlblsender = new JLabel();
				this.add(jlblsender);
				jlblsender.setText("sender");
				jlblsender.setBounds(199, 64, 56, 28);
			}
			{
				jlblisexchange = new JLabel();
				this.add(jlblisexchange);
				jlblisexchange.setText("isexchange");
				jlblisexchange.setBounds(12, 113, 69, 29);
			}
			{
				jlblproc = new JLabel();
				this.add(jlblproc);
				jlblproc.setText("proc");
				jlblproc.setBounds(199, 116, 56, 22);
			}
			{
				jlbloperation = new JLabel();
				this.add(jlbloperation);
				jlbloperation.setText("operation");
				jlbloperation.setBounds(12, 148, 69, 31);
			}
			{
				jtfisexchange = new JTextField();
				this.add(jtfisexchange);
				jtfisexchange.setText("Y");
				jtfisexchange.setBounds(87, 114, 100, 27);
			}
			{
				jtfproc = new JTextField();
				this.add(jtfproc);
				jtfproc.setText("add");
				jtfproc.setBounds(261, 114, 102, 24);
			}
			{
				this.add(getJtfsender());
			}
			{
				jtfoperation = new JTextField();
				this.add(jtfoperation);
				jtfoperation.setText("req");
				jtfoperation.setBounds(87, 153, 100, 26);
			}
			getJtfsender().setText(hvo.getDef_str_1());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JTextField getJtfsender() {
		if(jtfsender==null){
			jtfsender = new JTextField();
			jtfsender.setBounds(261, 67, 102, 25);
			jtfsender.setText(hvo.getDef_str_1());
		}
		return jtfsender;
	}

	public void setJtfsender(JTextField jtfsender) {
		this.jtfsender = jtfsender;
	}

}
