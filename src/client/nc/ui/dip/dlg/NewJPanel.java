package nc.ui.dip.dlg;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
	private JLabel jLabel2;
	private JTextField jTextField2;
	private JTextField jTextField1;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new NewJPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public NewJPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setLayout(null);
			this.setPreferredSize(new java.awt.Dimension(400, 204));
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("\u6587\u4ef6\u5939\u7f16\u7801\uff1a");
				jLabel1.setBounds(60, 59, 85, 27);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("\u6587\u4ef6\u5939\u540d\u79f0\uff1a");
				jLabel2.setBounds(60, 116, 85, 27);
			}
			{
				jTextField1 = new JTextField();
				this.add(jTextField1);
				jTextField1.setBounds(151, 61, 199, 22);
			}
			{
				jTextField2 = new JTextField();
				this.add(jTextField2);
				jTextField2.setBounds(151, 116, 199, 22);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
