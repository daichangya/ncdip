package nc.ui.dip.datachange.ref;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import nc.ui.dip.dataformatdef.MyEventHandler;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.IBillModelDecimalListener;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataformatdef.MyBillVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.messagelogo.MessagelogoVO;
import nc.vo.dip.view.VDipCrerefVO;
import nc.vo.pub.lang.UFBoolean;

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
public class DataRefPanel extends javax.swing.JPanel implements ActionListener,MouseListener,IBillModelDecimalListener{
	private JScrollPane jTabeP = null;
	private BillListPanel jpField;
	private String[] pks=null;//文件导入、开始标志、结束标志、。。。
	private String[] lx=null;//顺序，字段
	private DataformatdefHVO[] hvos;
	boolean esb=false;//是否是ESB接收格式
	private String pksys;// 系统主键
	private String vo;
	
	public boolean isEsb(){
		return esb;
	}
	public String[] getPks(){
		return pks;
	}
	public DataformatdefHVO[] getHvos(){
		return hvos;
	}
	public String[] getLx(){
		return lx;
	}

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	
	public DataRefPanel(String pksys,String vo) {
		super();
		this.pksys=pksys;
		this.vo=vo;
		initGUI();
	}

		private JScrollPane getTabbedPane() {
			if (jTabeP == null) {
				try {
					jTabeP = new JScrollPane();

					jTabeP.setBounds(12, 25, 920, 345);
					jTabeP.setName("FiledTabbedPane");
					jTabeP.setViewportView(getJpField());
					
				} catch (Exception ivjExc) {
					ivjExc.printStackTrace();
				}
			}
			return jTabeP;
		}
		/**
		 * @desc 业务数据的字段选择
		 * */
		public BillListPanel getJpField(){
			if(jpField==null){
				jpField=new BillListPanel();
				jpField.loadTemplet("H4H8He", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				jpField.getBodyTable().setSortEnabled(false);//.getBillTable().setSortEnabled(false);
				
				try {
					VDipCrerefVO[] vos=(VDipCrerefVO[]) HYPubBO_Client.queryByCondition(VDipCrerefVO.class, "fpk='"+pksys+"'");
					jpField.setBodyValueVO(vos);
					if(vo!=null){
						for(int i=0;i<vos.length;i++){
							if(vo.equals(vos[i].getPrimaryKey())){
								jpField.getBodyTable().changeSelection(i, 0, false, false);
								break;
							}
						}
					}
				} catch (UifException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return jpField;
		}
		/**
		 * @desc 格式的设置
		 * */
	private void initGUI() {

			this.setPreferredSize(new java.awt.Dimension(920, 388));
			this.setLayout(null);
			this.add(getTabbedPane());
	}
	
	

	public void actionPerformed(ActionEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClickedaaa");		
	}

	public void mouseEntered(MouseEvent e) {
		System.out.println("mouseEnteredaaa");		
	}

	public void mouseExited(MouseEvent e) {
		System.out.println("mouseExitedaaa");		
		
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("mousePressedaaa");		
		
	}

	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleasedaaa");		
		// TODO Auto-generated method stub
		
	}

	public int getDecimalFromItem(int row, BillItem item) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDecimalFromSource(int row, Object okValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDecimalType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTarget(BillItem item) {
		return false;
	}

}
