package nc.ui.dip.contdatawh.pane;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.contdatawh.ContdatawhBVO;
import nc.vo.pub.lang.UFBoolean;

public class SplitBodyPane extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3533690464167820201L;
	UISplitPane m_splitPane;
	UISplitPane m_splitPaneRight;
	BillListPanel m_leftValueSPl;
	BillListPanel m_rigthValueSPl;
	JPanel m_btnpane;
	

	private JButton jButton4;
	private JButton jButton3;
	private JButton jButton2;
	private JButton jButton1;
	//0是留，1是删
	private Map<String,Integer> addorremov;
	private String pkfield;
	public Map getRemoveOrAddMap(){
		return addorremov;
	}
	public SplitBodyPane(boolean isInit){
		super();
//		initialize();
		setSize(800, 400);
		setLayout(new java.awt.BorderLayout());
		initialize(isInit);
		addorremov=new Hashtable<String,Integer>();
	}
	public void initialize(boolean isInit ){
		try{
			this.add(getSplitPane(),"Center");
			getSplitPane().setLeftComponent(getLeftValueSP());
			getSplitPane().setDividerLocation(400);
			getSplitPane().setRightComponent(getSplitPaneRight());
			if(isInit){
				getSplitPane().remove(getSplitPaneRight());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}
	public void setPkField(String pkfield){
		this.pkfield=pkfield;
	}
	public UISplitPane getSplitPane()
	{
		if (m_splitPane == null){
			m_splitPane = new UISplitPane(1);
			m_splitPane.setOneTouchExpandable(true);
		}
		return m_splitPane;
	}
	public BillListPanel getLeftValueSP() {
		if (m_leftValueSPl == null) {
			m_leftValueSPl = new BillListPanel();
			m_leftValueSPl.loadTemplet("H4H3Hd", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
		}
		return m_leftValueSPl;
	}
	public BillListPanel getRightPane() {
		if (m_rigthValueSPl == null) {
			m_rigthValueSPl = new BillListPanel();
			m_rigthValueSPl.loadTemplet("H4H3Hd", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
		}
		return m_rigthValueSPl;
	}
	public UISplitPane getSplitPaneRight(){
		if(m_splitPaneRight==null){
			m_splitPaneRight=new UISplitPane(1);
			m_splitPaneRight.setDividerLocation(56);
			m_splitPaneRight.setLeftComponent(getButtonPane());
			m_splitPaneRight.setRightComponent(getRightPane());
		}
		return m_splitPaneRight;
	}
	public JPanel getButtonPane(){
		if(m_btnpane==null){
			m_btnpane=new JPanel();
			m_btnpane.setBounds(343, 111, 65, 191);
			m_btnpane.setLayout(null);

			jButton4 = new JButton();
			m_btnpane.add(jButton4);
			jButton4.setText("<<");
			jButton4.setBounds(3, 150, 50, 20);

			jButton3 = new JButton();
			m_btnpane.add(jButton3);
			jButton3.setText("<");
			jButton3.setBounds(3, 120, 50, 20);

			jButton2 = new JButton();
			m_btnpane.add(jButton2);
			jButton2.setText(">>");
			jButton2.setBounds(3, 90, 50, 20);

			jButton1 = new JButton();
			m_btnpane.add(jButton1);
			jButton1.setText(">");
			jButton1.setBounds(3, 60, 50, 20);

			jButton1.addActionListener(this);
			jButton2.addActionListener(this);
			jButton3.addActionListener(this);
			jButton4.addActionListener(this);
		}
		return m_btnpane;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jButton1)){
			int rowcount=getLeftValueSP().getBodyTable().getRowCount();
			if(rowcount>0){
				int k=0;
				for(int i=0;i<rowcount;i++){
					ContdatawhBVO bvo=(ContdatawhBVO) getLeftValueSP().getBodyBillModel().getBodyValueRowVO(k, ContdatawhBVO.class.getName());
					if(bvo!=null){
						if(bvo.getIsselect()!=null&&bvo.getIsselect().booleanValue()){
							getRightPane().getBodyBillModel().addLine();
							int rowc=getRightPane().getBodyTable().getRowCount();
							String key=bvo.getAttributeValue(pkfield).toString();
							addorremov.put(key, IContrastUtil.WH_REMOVE);
							bvo.setIsselect(new UFBoolean(false));
							getRightPane().getBodyBillModel().setBodyRowVO(bvo, rowc-1);
							getLeftValueSP().getBodyBillModel().delLine(new int[]{k});
							k--;
						}
					}
					k++;
				}
			}
			/*int row=getLeftValueSP().getBodyTable().getSelectedRow();
			if(row>=0){
				ContdatawhBVO bvo=(ContdatawhBVO) getLeftValueSP().getBodyBillModel().getBodyValueRowVO(row, ContdatawhBVO.class.getName());
				if(bvo!=null){
					getRightPane().getBodyBillModel().addLine();
					int rowc=getRightPane().getBodyTable().getRowCount();
					String key=bvo.getAttributeValue(pkfield).toString();
					addorremov.put(key, IContrastUtil.WH_REMOVE);
					getRightPane().getBodyBillModel().setBodyRowVO(bvo, rowc-1);
					getLeftValueSP().getBodyBillModel().delLine(new int[]{row});
				}
			}*/
		}else if(e.getSource().equals(jButton3)){
			int rowcount=getRightPane().getBodyTable().getRowCount();
			if(rowcount>0){
				int i=0;
				for(int k=0;k<rowcount;k++){
					ContdatawhBVO bvo=(ContdatawhBVO) getRightPane().getBodyBillModel().getBodyValueRowVO(i, ContdatawhBVO.class.getName());
					if(bvo!=null){
						if(bvo.getIsselect()!=null&&bvo.getIsselect().booleanValue()){
							if(bvo.getAttributeValue(pkfield)==null){
								MessageDialog.showErrorDlg(null, "错误", "界面主键字段值不能为空");
								return;
							}
							getLeftValueSP().getBodyBillModel().addLine();
							int rowc=getLeftValueSP().getBodyTable().getRowCount();
							String key = bvo.getAttributeValue(pkfield).toString();
							addorremov.put(key, IContrastUtil.WH_ADD);
							bvo.setIsselect(new UFBoolean(false));
							getLeftValueSP().getBodyBillModel().setBodyRowVO(bvo, rowc-1);
							getRightPane().getBodyBillModel().delLine(new int[]{i});
							i--;
						}
					}
					i++;
				}
			}
//			int row=getRightPane().getBodyTable().getSelectedRow();
//			if(row>=0){
//				ContdatawhBVO bvo=(ContdatawhBVO) getRightPane().getBodyBillModel().getBodyValueRowVO(row, ContdatawhBVO.class.getName());
//				if(bvo!=null){
//					getLeftValueSP().getBodyBillModel().addLine();
//					int rowc=getLeftValueSP().getBodyTable().getRowCount();
//					String key = bvo.getAttributeValue(pkfield).toString();
//					addorremov.put(key, IContrastUtil.WH_ADD);
//					getLeftValueSP().getBodyBillModel().setBodyRowVO(bvo, rowc-1);
//					getRightPane().getBodyBillModel().delLine(new int[]{row});
//				}
//			}
		}else if(e.getSource().equals(jButton2)){
			int row=getLeftValueSP().getBodyTable().getRowCount();
			int rowc=getRightPane().getBodyTable().getRowCount();
			if(row>0){
				for(int i=0;i<row;i++){
					ContdatawhBVO bvo=(ContdatawhBVO) getLeftValueSP().getBodyBillModel().getBodyValueRowVO(i, ContdatawhBVO.class.getName());
					if(bvo!=null){
						getRightPane().getBodyBillModel().addLine();
						String key = bvo.getAttributeValue(pkfield).toString();
						addorremov.put(key, IContrastUtil.WH_REMOVE);
						getRightPane().getBodyBillModel().setBodyRowVO(bvo, rowc+i);
					}
				}
				getLeftValueSP().getBodyBillModel().clearBodyData();
			}
		}else if(e.getSource().equals(jButton4)){
			int row=getRightPane().getBodyTable().getRowCount();
			int rowc=getLeftValueSP().getBodyTable().getRowCount();
			if(row>0){
				for(int i=0;i<row;i++){
					ContdatawhBVO bvo=(ContdatawhBVO) getRightPane().getBodyBillModel().getBodyValueRowVO(i, ContdatawhBVO.class.getName());
					if(bvo!=null){
						getLeftValueSP().getBodyBillModel().addLine();
						String key = bvo.getAttributeValue(pkfield).toString();
						addorremov.put(key, IContrastUtil.WH_ADD);
						getLeftValueSP().getBodyBillModel().setBodyRowVO(bvo, rowc+i);
					}
				}
				getRightPane().getBodyBillModel().clearBodyData();
			}
		}
	}
}
