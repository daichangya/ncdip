package nc.ui.dip.credence;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.BusinessException;

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
public class WbJPanel extends javax.swing.JPanel implements ActionListener {
	private JScrollPane jScrollPane1;
	private JList jList1;
	private JButton jButton3;
	private JTable jTable1;
    private String pk_sys;
    private Map<String,DipDatadefinitHVO> map;
    String[] liststr;
    String[][] value;
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
    
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new WbJPanel("0001AA1000000001KWMG"));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public WbJPanel() {
		super();
		initGUI();
	}
	public WbJPanel(String pk_sys) {
		super();
		setPk_sys(pk_sys);
		initMap();
		initGUI();
		
	}
	private void initMap(){
		map=new HashMap<String, DipDatadefinitHVO>();
		try {
			DipDatadefinitHVO[] hvos=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_sysregister_h='"+getPk_sys()+"' and nvl(dr,0)=0 and datatype is not null and iscreatetab='Y'");
			if(hvos!=null&&hvos.length>0){
				for(DipDatadefinitHVO hvo:hvos){
					map.put(hvo.syscode+hvo.getSysname(), hvo);
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
	}
	private void initGUI() {
		try {
			this.setLayout(null);
			setPreferredSize(new Dimension(400, 300));
			jScrollPane1 = new JScrollPane();
			this.add(jScrollPane1);
			jScrollPane1.setBounds(170, 12, 218, 272);
			jScrollPane1.setViewportView(getJtable());
			this.add(getJlist1());
			this.add(getJbutton());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public JButton getJbutton(){
		if(jButton3==null){
			jButton3 = new JButton();
			jButton3.addActionListener(this);
			jButton3.setText("\u9009\u62e9");
			jButton3.setBounds(108, 66, 66, 29);
		}
		return jButton3;
	}
/*	//显示当前系统的所有表
	public void viewList(){
		DipSysregisterHVO hvo=null;
		DipDatadefinitHVO dvo=null;
		try {
			 hvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_sys);
		} catch (UifException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String pk=hvo.getPk_sysregister_h();
		try {
			dvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk_sys);
		} catch (UifException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String tab=dvo.getSysname();
		
	}*/
	public JList getJlist1(){
		if(jList1==null){
			jList1 = new JList();
			if(map!=null&&map.size()>0){
				
				liststr=new String[map.size()];
				Iterator it=map.keySet().iterator();
				int i=0;
				while(it.hasNext()){
					String key=(String) it.next();
					liststr[i]=key;
					i++;
				}
				ListModel jList1Model = 
					new DefaultComboBoxModel(liststr);
				jList1.setModel(jList1Model);
			}
			jList1.setBounds(17, 12, 91, 265);
		}
		return jList1;
	}
	public JTable getJtable(){
		if(jTable1==null){
			/*TableModel jTable1Model = 
				new DefaultTableModel(
						new String[][] { { "One", "Two" }, { "Three", "Four" } },
						new String[] { "Column 1", "Column 2" });*/
			jTable1 = new JTable();
//			jTable1.setModel(jTable1Model);
			jTable1.setPreferredSize(new java.awt.Dimension(215, 31));
		}
		return jTable1;
	}
	
    
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==getJbutton()){
			String ss= (String) getJlist1().getSelectedValue();
			if(ss!=null){
				DipDatadefinitHVO hvo=map.get(ss);
				try {
					DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+hvo.getPrimaryKey()+"' and nvl(dr,0)=0" );
					if(bvos!=null&&bvos.length>0){
						String[] title=new String[bvos.length];
						String[] etitle=new String[bvos.length];
						int i=0;
						StringBuffer sb=new StringBuffer();
						for(DipDatadefinitBVO bvo:bvos){
							title[i]=bvo.getCname();
							etitle[i]=bvo.getEname();
							i++;
							sb.append(bvo.getEname()+",");
						}
						String sql="select "+sb.toString().substring(0,sb.length()-1)+" from "+hvo.getMemorytable()+" where nvl(dr,0)=0";
						IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
						try {
							List list=iqf.queryListMapSingleSql(sql);
							if(list!=null&&list.size()>0){
								value=new String[list.size()][title.length];
								for(int j = 0;j<list.size();j++){
									HashMap mapValue = (HashMap)list.get(j);
									for(int k=0;k<title.length;k++){
										if(mapValue.get(etitle[k].toUpperCase())!=null){
											value[j][k]=mapValue.get(etitle[k].toUpperCase()).toString();
										}
									}
								}
							}
							TableModel jTable1Model = 
								new DefaultTableModel(
										value,
										title);
							getJtable().setModel(jTable1Model);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (DbException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				} catch (UifException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}

	public String getPk_sys() {
		return pk_sys;
	}

	public void setPk_sys(String pk_sys) {
		this.pk_sys = pk_sys;
	}

}
