package nc.ui.dip.tyzhq.iniufoenv;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ParamInputPanel extends javax.swing.JPanel {
	private JLabel jLabel1;
	private JTextField unitcodeField;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ParamInputPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ParamInputPanel() {
		super();
		initGUI();
	}
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("²ÎÊýÂ¼Èë");
				jLabel1.setBounds(52, 42, 72, 15);
			}
			{
				unitcodeField = new JTextField();
				this.add(unitcodeField);
				unitcodeField.setBounds(163, 107, 171, 22);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String getCalenvFromPanel(){
		return unitcodeField.getText();
	}
}
