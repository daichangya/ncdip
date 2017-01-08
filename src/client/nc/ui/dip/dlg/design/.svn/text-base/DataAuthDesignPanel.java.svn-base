package nc.ui.dip.dlg.design;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
import javax.swing.WindowConstants;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillModelDecimalListener;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.FunctionUtil;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipAuthDesignVO;
import nc.vo.dip.datalook.VDipAuthDesignVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
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
public class DataAuthDesignPanel extends UIPanel implements ActionListener,MouseListener,IBillModelDecimalListener{
//	private UITabbedPane jPanel1;
	private JPanel jPanel3;
	private JComboBox jComboBox2;
	private JComboBox jComboBox1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JButton jButton4LL;//按钮"<<",向左全移动
	private JButton jButton3L;//按钮"<",向左移动
	private JButton jButton2RR;//按钮“>>”,向右全移动
	private JButton jButton1R;//按钮“>”，向右移动
	private JButton jButtonCopy;//按钮“>”，向右移动
	private JButton upbtn;
	private JButton dobtn;
	private String[] pks;
	private Map<String,String> map;
	// "数据显示设置", "查询模板设置","导入设置","导出设置" 
	private String type_dr="导入设置";
	private String type_dc="导出设置";
	private String type_xs="数据显示设置";
	private String type_cx="查询模板设置";
	private String type_mxs="手机数据显示设置";
	private String type_mcx="手机查询模板设置";
	private Map<String,VDipAuthDesignVO[]> dmap;
	private String[] pkfields;
	//liyunzhe add 2012-09-20
	private boolean isAuthorizeBrow=false;//表示是否是授权浏览标识，如果是就为true，就会授权浏览节点的设置按钮弹出框中业务表下拉选项增加一个常量下拉框，常量显示的数据是常量节点和消息节点标志
	private String type_cl="常量";
	private String type_clcombox[];
	private ComboBoxModel jComboBox1Model=null;
	private ComboBoxModel jComboBox1Model_cl=null;
	private VDipAuthDesignVO[] importVOS;
	private String notsavekey="";
	private Integer notetype=null;//节点编码
	private ArrayList<String> notsaveList = new ArrayList<String>();
	private String ismobile = null;
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new DataAuthDesignPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public DataAuthDesignPanel() {
		super();
		initGUI();
	}
	public DataAuthDesignPanel(String[] pks,String[] pkfields) {
		super();
		this.pks=pks;
		this.pkfields=pkfields;
		initGUI();
		initjp(0,0);
	}
	public DataAuthDesignPanel(String[] pks,String[] pkfields,boolean isAuthorizeBrow,String ismobile) {
		super();
		this.pks=pks;
		this.pkfields=pkfields;
		this.isAuthorizeBrow=isAuthorizeBrow;
		this.ismobile = ismobile;
		initGUI();
		initjp(0,0);
	}
	
	public DataAuthDesignPanel(String[] pks,String[] pkfields,Integer notetype) {
		super();
		this.pks=pks;
		this.pkfields=pkfields;
		this.notetype=notetype;
		initGUI();
		initjp(0,0);
	}
	
	public void initjp(int pki,int stat){
		VDipAuthDesignVO[] vos=dmap.get(pki+"-"+stat+"l");
		int rows=getjp1().getBillTable().getRowCount();
		if(rows>0){
			getjp1().getBillTable().changeSelection(0,0,true,true);
			for(int i=0;i<rows;i++){
				getjp1().delLine();
			}
		}
		 rows=getjp1().getBillTable().getRowCount();
			if(rows>0){
				getjp1().getBillTable().changeSelection(0,0,true,false);
				for(int i=0;i<rows;i++){
					getjp1().delLine();
				}
			}
		
		if(vos!=null&&vos.length>0){
			for(int i=0;i<vos.length;i++){
				getjp1().addLine();
			}
			getjp1().getBillModel().setBodyDataVO(vos);
			getjp1().getBillModel().setEnabled(false);
		}
		VDipAuthDesignVO[] vos1;
//		if(isAuthorizeBrow&&pki==1&&stat==2){
//			vos1=dmap.get(0+"-"+stat+"r");
//		}else{
			vos1=dmap.get(pki+"-"+stat+"r");
//		}

		rows=getjp2().getBillTable().getRowCount();
		if(rows>0){
			getjp2().getBillTable().changeSelection(0,0,true,true);
			for(int i=0;i<rows;i++){
				getjp2().delLine();
			}
		}
		rows=getjp2().getBillTable().getRowCount();
		if(rows>0){
			getjp2().getBillTable().changeSelection(0,0,true,false);
			for(int i=0;i<rows;i++){
				getjp2().delLine();
			}
		}
		if(vos1!=null&&vos1.length>0){
			for(int i=0;i<vos1.length;i++){
				getjp2().addLine();
			}
			getjp2().getBillModel().setBodyDataVO(vos1);
//			getjp2().getBillModel().setEnabled(false);
			getjp2().getBillModel().setEnabled(true);
			getjp2().getBillModel().getItemByKey("disno").setEnabled(false);
			getjp2().getBillModel().getItemByKey("cname").setEnabled(false);
			getjp2().getBillModel().getItemByKey("ename").setEnabled(false);
			getjp2().getBillModel().getItemByKey("isdisplay").setEnabled(true);//
		}
		
	}
/*	public UITabbedPane getJP1(){
		if(jPanel1==null){
			jPanel1=new UITabbedPane();
			
			jPanel1.setBounds(12, 63, 331, 458);
//			jPanel1.setViewportView(getjp1());
			jPanel1.addTab("设置选择", getjp1());
		}
		return jPanel1;
	}*/
	private BillCardPanel jp1;
	public BillCardPanel getjp1(){
		if(jp1==null){
			jp1=new BillCardPanel();
//			jp1.setBounds(12, 63, 329, 318);
			jp1.loadTemplet("H4H1Hk", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			jp1.getBillTable().setSortEnabled(false);
			jp1.setBounds(10, 50, 300, 350);
			jp1.getBodyPanel().getPmBody().removeAll();
		}
		return jp1;
	}
	
	private BillCardPanel jp2;
	CheckBoxActionListener checkBoxItemListener = new CheckBoxActionListener();
	HeadValueChangeListener headValueChangeListener = new HeadValueChangeListener();
	ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();
	class ComboBoxItemListener implements ItemListener {
        // combobox + item
        Hashtable<Component,BillItem> hashItem = new Hashtable<Component,BillItem>();

        public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComponent editorComp = (JComponent) e.getSource();
                    if (editorComp instanceof JComboBox && editorComp.hasFocus()) {
                        JComboBox box = (JComboBox) editorComp;
                        BillItem item = hashItem.get(box);
                        int index=jComboBox1.getSelectedIndex();
                		int index2=jComboBox2.getSelectedIndex();
                		VDipAuthDesignVO[] vos=dmap.get(index+"-"+index2+"r");
                		int row=getjp2().getBillTable().getSelectedRow();
                		DefaultConstEnum o=(DefaultConstEnum) box.getSelectedItem();
                		if(o!=null){
                			vos[row].setContype(o.getName());
                		}
//                		vos[row].setContype(box.getSelectedItem()==1?"左":(ItemEvent.SELECTED==2?"中":"右"));
                    }
            }
        }
        /**
         * 获取下拉框的显示字符串. 主要用于可编辑下拉框. 创建日期:(2003-6-23 9:24:52)
         * 
         * @param box
         *            the combobox
         * @return the display string of the combobox
         */
        String getJComboBoxText(final JComboBox box) {
            if (box == null)
                return null;
            String text = null;
            Object o;
            java.awt.Component editor;
            if ((o = box.getSelectedItem()) != null) {
                text = o.toString();
            }
            else if ((editor = box.getEditor().getEditorComponent()) instanceof javax.swing.text.JTextComponent) {
                text = ((javax.swing.text.JTextComponent) editor).getText();
            }
            return text;
        }
    }
	 class CheckBoxActionListener implements ActionListener {
	        // checkbox + item
	        Hashtable<Component,BillItem> hashItem = new Hashtable<Component,BillItem>();

	        public void actionPerformed(ActionEvent e) {
                JComponent editorComp = (JComponent) e.getSource();
                if (editorComp instanceof JCheckBox) {
                    JCheckBox box = (JCheckBox) editorComp;
                    BillItem item = hashItem.get(box);
                	int index=jComboBox1.getSelectedIndex();
            		int index2=jComboBox2.getSelectedIndex();
            		VDipAuthDesignVO[] vos=dmap.get(index+"-"+index2+"r");
            		int row=getjp2().getBillTable().getSelectedRow();
            		if(editorComp.getName().equals("isdisplay")){
            			vos[row].setIsdisplay(new UFBoolean(box.isSelected()));
            		}else if(editorComp.getName().equals("islock")){
            			vos[row].setIslock(new UFBoolean(box.isSelected()));
            		}else if(editorComp.getName().equals("is_single_list")){
            			UFBoolean is_single_list = new UFBoolean(box.isSelected());
            			if(null != is_single_list && is_single_list.booleanValue()){
	            			int k = 0;
	            			for (int i = 0; i < vos.length; i++) {
	            				UFBoolean bool = vos[i].getIs_single_list();
	            				if(null != bool && bool.booleanValue()){
	            					k++;
	            				}
							}
	            			if(k>=3){
	            				MessageDialog.showHintDlg(null, "提示", "是否列表列不能大于3列");
	            				vos[row].setIs_single_list(new UFBoolean(false));
	            				getjp2().getBillModel().setValueAt(null, row, "is_single_list");
	            			}else{
	            				vos[row].setIs_single_list(is_single_list);
	            				vos[row].setIs_list(is_single_list);
	            				getjp2().getBillModel().setValueAt(is_single_list, row, "is_list");
	            			}
            			}else{
            				vos[row].setIs_single_list(is_single_list);
            			}
            		}else if(editorComp.getName().equals("is_list")){
            			vos[row].setIs_list(new UFBoolean(box.isSelected()));
            		}
                    
	            }
	        }
	    }
	 class HeadValueChangeListener implements ValueChangedListener {
	        // uirefpane + billitem
	        Hashtable<Component,BillItem> hashItem = new Hashtable<Component,BillItem>();

	        public void valueChanged(ValueChangedEvent e) {
                JComponent editorComp = (JComponent) e.getSource();
                if (editorComp instanceof UIRefPane || editorComp instanceof UITextArea) {
                    BillItem item = hashItem.get(editorComp);
                    int index=jComboBox1.getSelectedIndex();
            		int index2=jComboBox2.getSelectedIndex();
            		VDipAuthDesignVO[] vos=dmap.get(index+"-"+index2+"r");
            		int row=getjp2().getBillTable().getSelectedRow();
            		Object value = null;
            		if (editorComp instanceof UIRefPane)
            			value = ((UIRefPane) editorComp).getText();
            		else if (editorComp instanceof UITextArea)
            			value = ((UITextArea) editorComp).getText();
            		if(editorComp.getName().equals("desigplen")){
            			if(value==null||value.toString().length()<=0){
            				vos[row].setDesigplen(null);
            			}else{
            				vos[row].setDesigplen(new Integer(value.toString()));
            			}
            		}else if(editorComp.getName().equals("designlen")){
            			vos[row].setDesignlen(new Integer(value.toString()));
            		}else if(editorComp.getName().equals("contype")){
            			vos[row].setContype(value.toString());
            		}else if(editorComp.getName().equals("consvalue")){
            			vos[row].setConsvalue(value.toString());
            		}else{
            			vos[row].setDesignlen(new Integer(value.toString()));
            		}
	            }
	        }
	    }
	public BillCardPanel getjp2(){
		if(jp2==null){
			jp2=new BillCardPanel();
			jp2.loadTemplet("H4H1H1", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;

			JCheckBox com=(JCheckBox) getjp2().getBillModel().getItemByKey("isdisplay").getComponent();
			com.addActionListener(checkBoxItemListener);
            checkBoxItemListener.hashItem.put(com,  getjp2().getBillModel().getItemByKey("isdisplay"));
            UIRefPane rf=(UIRefPane) getjp2().getBillModel().getItemByKey("designlen").getComponent();
            rf.addValueChangedListener(headValueChangeListener);
            UIRefPane rf1=(UIRefPane) getjp2().getBillModel().getItemByKey("desigplen").getComponent();
            rf1.addValueChangedListener(headValueChangeListener);
            UIComboBox rf2=(UIComboBox) getjp2().getBillModel().getItemByKey("contype").getComponent();
            rf2.addItemListener(comboBoxItemListener);
            UIRefPane rf3=(UIRefPane) getjp2().getBillModel().getItemByKey("consvalue").getComponent();
            rf3.addValueChangedListener(headValueChangeListener);
//            headValueChangeListener.hashItem.put(rf, getjp2().getBillModel().getItemByKey("designlen"));
//            registerKey(rf.getUITextField());
            JCheckBox lockCheck=(JCheckBox) getjp2().getBillModel().getItemByKey("islock").getComponent();
            lockCheck.addActionListener(checkBoxItemListener);
            
            JCheckBox singleListCheck=(JCheckBox) getjp2().getBillModel().getItemByKey("is_single_list").getComponent();
            singleListCheck.addActionListener(checkBoxItemListener);
            
            JCheckBox listCheck=(JCheckBox) getjp2().getBillModel().getItemByKey("is_list").getComponent();
            listCheck.addActionListener(checkBoxItemListener);
            jp2.getBillTable().setSortEnabled(false);

            jp2.setBounds(370, 50, 800, 350);
//            jp2.getBodyPanel().hideTableCol("consvalue");
            jp2.addBodyEditListener2(new BillEditListener2() {
				
				public boolean beforeEdit(BillEditEvent e) {
					if(e.getKey().equals("consvalue")){
						Object object = jp2.getBodyValueAt(e.getRow(), e.getKey());
						if(null != object && (object.toString().startsWith(FunctionUtil.SAVEREPLACEPK) 
								|| object.toString().startsWith(FunctionUtil.SHOWREPLACEPK)
								|| object.toString().startsWith(FunctionUtil.STRREPLACEPK))){
							return true;
						}
						return false;
					}
					return true;
				}
			});
            jp2.getBodyPanel().getPmBody().removeAll();
		}
		return jp2;
	}
	private void initGUI() {
		dmap=new HashMap<String, VDipAuthDesignVO[]>();
		if(pks!=null){
			for(int i=0;i<pks.length;i++){//左边下拉表表pks，循环
				int g = 4;
				if("Y".equals(ismobile)){
					g = 6;
				}
				for(int j=0;j<g;j++){//右边下拉选项循环，总共4个，一个是显示，导入，导出，
					try {
						if(pkfields==null||j>0){
							dmap.put((i+"-"+j+"l"),(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "pk_design is null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j +" order by ename"));
							
							if(isAuthorizeBrow&&i==0){
								importVOS=(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "pk_design is not null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j+" order by disno");
								dmap.put((i+"-"+j+"r"),importVOS);
								notsavekey=1+"-"+j+"r";
								dmap.put(notsavekey,importVOS);
								notsaveList.add(notsavekey);
							}else{
								dmap.put((i+"-"+j+"r"),(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "pk_design is not null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j+" order by disno"));
							}
						}else{
//							if(this.notetype!=null&&this.notetype==DesignDLG.DATAAUTHORIZEWH){
								dmap.put((i+"-"+j+"l"),(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "pk_design is null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j +" order by ename"));
//							}else{
//								dmap.put((i+"-"+j+"l"),(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "pk_design is null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j +" and pk_datadefinit_b <>'"+pkfields[i]+"' order by ename"));
//							}
//							VDipAuthDesignVO[] vos=(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "((pk_design is not null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j+") or (pk_datadefinit_b='"+pkfields[i]+"' and designtype="+j+")) order by disno");
							VDipAuthDesignVO[] vos=(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "((pk_design is not null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j+")) order by disno");
							if(vos!=null&&vos.length>0){
								for(int k=0;k<vos.length;k++){
									if(vos[k].getPk_datadefinit_b().equals(pkfields[i])){
										if(vos[k].getDisno()==null){
											if(vos[k].getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(vos[k].getDatatype().toUpperCase())>=0)){
												vos[k].setContype("右");
											}else{
												vos[k].setContype("左");
												vos[k].setDesigplen(null);
											}
										}
									}
									vos[k].setDisno(k+1);
								}
//								if(isAuthorizeBrow&&i==0&&j==2){
//									importVOS=new VDipAuthDesignVO[vos.length];
//									for(int w=0;w<vos.length;w++){
//										importVOS[w]=vos[w];
//									}
//									dmap.put((i+"-"+j+"r"),importVOS);
//									dmap.put((1+"-"+j+"r"),importVOS);
//								}else{
									dmap.put((i+"-"+j+"r"),vos );
//								}
							}
							
							if(isAuthorizeBrow&&i==0){
								importVOS=(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, "pk_design is not null and pk_datadefinit_h='"+pks[i]+"' and designtype="+j+" order by disno");
								dmap.put((i+"-"+j+"r"),importVOS);
								notsavekey=1+"-"+j+"r";
								dmap.put(notsavekey,importVOS);
								notsaveList.add(notsavekey);
							}
						}
					} catch (UifException e) {
						e.printStackTrace();
						dmap.put((i+"-"+j+"l"),null);
						dmap.put((i+"-"+j+"r"),null);
					}
				}
			}
			if(isAuthorizeBrow){//如果是数据权限浏览格式，增加两个导入格式下拉的常量页签
				try {
					VDipAuthDesignVO []VDipAuthDesignVOs=(VDipAuthDesignVO[]) HYPubBO_Client.queryByCondition(VDipAuthDesignVO.class, " designtype=8 and nvl(dr,0)=0 ");
					dmap.put((1+"-"+0+"l"),VDipAuthDesignVOs);
					dmap.put((1+"-"+1+"l"),VDipAuthDesignVOs);
					dmap.put((1+"-"+2+"l"),VDipAuthDesignVOs);
					dmap.put((1+"-"+3+"l"),VDipAuthDesignVOs);
					dmap.put((1+"-"+4+"l"),VDipAuthDesignVOs);
					dmap.put((1+"-"+5+"l"),VDipAuthDesignVOs);
//					dmap.put((1+"-"+2+"r"), dmap.get((0+"-"+2+"r")));
				} catch (UifException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dmap.put((2+"-"+1+"l"),null);
				}
			}
		}
		try {
			this.setPreferredSize(new java.awt.Dimension(880, 388));
			this.setLayout(null);
				this.add(getjp1());
				this.add(getjp2());
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("\u4e1a\u52a1\u8868");
				jLabel1.setBounds(50, 17, 48, 15);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("\u8bbe\u7f6e\u7c7b\u578b");
				jLabel2.setBounds(364, 17, 57, 15);
			}
			{
				String strpks="";
				if(pks!=null&&pks.length>0){
					for(int i=0;i<pks.length;i++){
						strpks=strpks+"'"+pks[i]+"',";
					}
					strpks=strpks.substring(0,strpks.length()-1);
				}
				map=new HashMap<String, String>();
				String[] bm=new String[pks.length];
				for(int i=0;i<pks.length;i++){
					DipADContdataVO contDataVO =(DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pks[i]);
					DipDatadefinitHVO  vos=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, contDataVO.getContabcode());
					if(vos!=null){
						map.put(vos.getSysname(),vos.getPrimaryKey());
						bm[i]=vos.getSysname();
					}
				}
				
				if(isAuthorizeBrow){
					type_clcombox=new String[pks.length+1];
					for(int i=0;i<bm.length;i++){
						type_clcombox[i]=bm[i];
					}
					type_clcombox[type_clcombox.length-1]=type_cl;
					jComboBox1Model_cl=new DefaultComboBoxModel(type_clcombox);
				}
				 jComboBox1Model = new DefaultComboBoxModel(bm);
				jComboBox1 = new JComboBox();
//				if(pks!=null&&pks.length>1){
					jComboBox1.addActionListener(this);
					jComboBox1.addMouseListener(this);
					
//				}
				this.add(jComboBox1);
				
				jComboBox1.setModel(jComboBox1Model_cl);
				jComboBox1.setBounds(110, 13, 142, 22);
			}
			{
				String[] strings = new String[] { type_xs, type_cx,type_dr,type_dc};
				if("Y".equals(ismobile)){
					strings = new String[] { type_xs, type_cx,type_dr,type_dc,type_mxs,type_mcx};
				}
				ComboBoxModel jComboBox2Model = 
					new DefaultComboBoxModel(
							strings);
				jComboBox2 = new JComboBox();
				jComboBox2.addActionListener(this);
				jComboBox2.addMouseListener(this);
				this.add(jComboBox2);
				this.add(getJPanel3());
				jComboBox2.setModel(jComboBox2Model);
				jComboBox2.setBounds(436, 13, 164, 22);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setBounds(315, 80, 55, 330);
			jPanel3.setLayout(null);
			{
				jButton4LL = new JButton();
				jPanel3.add(jButton4LL);
				jButton4LL.setText("<<");
				jButton4LL.setBounds(0, 195, 50, 20);
			}
			{
				jButton3L = new JButton();
				jPanel3.add(jButton3L);
				jButton3L.setText("<");
				jButton3L.setBounds(0, 150, 50, 20);
			}
			{
				jButton2RR = new JButton();
				jPanel3.add(jButton2RR);
				jButton2RR.setText(">>");
				jButton2RR.setBounds(0, 105, 50, 20);
			}
			{
				jButton1R = new JButton();
				jPanel3.add(jButton1R);
				jButton1R.setText(">");
				jButton1R.setBounds(0, 60, 50, 20);
			}
			{
				jButtonCopy = new JButton();
				jPanel3.add(jButtonCopy);
				jButtonCopy.setText("复制");
				jButtonCopy.setBounds(0, 15, 50, 20);
				jButtonCopy.setVisible(false);
			}
			upbtn = new JButton();
			jPanel3.add(upbtn);
			upbtn.setText("上移");
			upbtn.setBounds(0, 240, 50, 20);
			dobtn = new JButton();
			jPanel3.add(dobtn);
			dobtn.setText("下移");
			dobtn.setBounds(0, 290, 50, 20);
			
			jButton1R.addActionListener(this);
			jButtonCopy.addActionListener(this);
			jButton2RR.addActionListener(this);
			jButton3L.addActionListener(this);
			jButton4LL.addActionListener(this);
			upbtn.addActionListener(this);
			dobtn.addActionListener(this);
		}
		return jPanel3;
	}
	IUAPQueryBS ibs=null;
	public IUAPQueryBS getIBS(){
		if(ibs==null){
			ibs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return ibs;
	}
	public void onBoQk() {
		int i=jComboBox1.getSelectedIndex();
		int j=jComboBox2.getSelectedIndex();
		if(isAuthorizeBrow&&i==1){
//			dmap.put(notsavekey, null);
//			dmap.put(0+"-"+2+"r", null);
			return;
		}
		try {
			DipADContdataVO contDataVO =(DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pks[i]);
			if(pkfields==null||j>0||pkfields.length<(i+1)||pkfields[i]==null||pkfields[i].length()<=0){
				String sql="select  type datatype,ispk,'左' contype,case when deciplace is not null then 2 end desigplen,50 designlen,'' pk_design,pk_datadefinit_h,pk_datadefinit_b,cname,ename,'N' isdisplay,null disno,3 designtype,dr from dip_datadefinit_b where nvl(dr,0)=0 and pk_datadefinit_h='"+contDataVO.getContabcode()+"'";
				List<VDipAuthDesignVO> list=null;
				try {
					list=(List<VDipAuthDesignVO>) getIBS().executeQuery(sql, new BeanListProcessor(VDipAuthDesignVO.class));
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(list!=null){
				dmap.put((i+"-"+j+"l"),list.toArray(new VDipAuthDesignVO[list.size()]));
				dmap.put((i+"-"+j+"r"),null);
				}
			}else{
				String sql="select  type datatype,ispk,'左' contype,case when deciplace is not null then 2 end desigplen,50 designlen,'' pk_design,pk_datadefinit_h,pk_datadefinit_b,cname,ename,'N' isdisplay,null disno,3 designtype,dr from dip_datadefinit_b where nvl(dr,0)=0 and pk_datadefinit_h='"+contDataVO.getContabcode()+"'";
				List<VDipAuthDesignVO> list=null;
				try {
					list=(List<VDipAuthDesignVO>) getIBS().executeQuery(sql, new BeanListProcessor(VDipAuthDesignVO.class));
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(list!=null&&list.size()>0){
					VDipAuthDesignVO[] vol=new VDipAuthDesignVO[list.size()-1];
					int row=-1;
					boolean ispk=false;
					for(int k=0;k<list.size();k++){
						if(list.get(k).getPk_datadefinit_b().equals(pkfields[i])){
							row=k;
							ispk=true;
						}else{
							if(ispk){
								vol[k-1]=list.get(k);
							}else{
								vol[k]=list.get(k);
							}
						}
					}
					
					dmap.put((i+"-"+j+"l"),vol);
					VDipAuthDesignVO[] vos= new VDipAuthDesignVO[]{list.get(row)};
					if(vos!=null&&vos.length>0){
						for(int k=0;k<vos.length;k++){
							if(vos[k].getPk_datadefinit_b().equals(pkfields[i])){
								if(vos[k].getDisno()==null){
									if(vos[k].getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(vos[k].getDatatype().toUpperCase())>=0)){
										vos[k].setContype("右");
									}else{
										vos[k].setContype("左");
										vos[k].setDesigplen(null);
									}
								}
							}
							vos[k].setDisno(k+1);
						}
						dmap.put((i+"-"+j+"r"),vos );
					}
				
				}
			}
		} catch (UifException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(isAuthorizeBrow&&i==0){
			dmap.put(notsavekey, null);
		}
		initjp(i, j);
		
	}


	public void actionPerformed(ActionEvent e) {

		int index=jComboBox1.getSelectedIndex();
		int index2=jComboBox2.getSelectedIndex();
		if (e.getSource().equals(jButton1R)){
			int row=getjp1().getBillTable().getSelectedRow();
			if(row>=0){
				
				String key=index+"-"+index2+"l";
				VDipAuthDesignVO[] vol=dmap.get(key);
				VDipAuthDesignVO vo=vol[row];
				if(isAuthorizeBrow&&index==1){//常量导入设置
					if(getjp2().getBillTable().getSelectedRow()<0){
						MessageDialog.showErrorDlg(this, "错误", "右边必须选择替换行");
						return;
					}else{
						String keyr=index+"-"+index2+"r";
						String keyrtable=0+"-"+index2+"r";
						dmap.get(keyr)[getjp2().getBillTable().getSelectedRow()].setConsvalue(vo.getEname());
						dmap.get(keyr)[getjp2().getBillTable().getSelectedRow()].setConspk(vo.getPk_datadefinit_b());
						dmap.put(keyrtable, dmap.get(keyr));
						initjp(index, index2);
						return;
					}
				}
				if(vol.length==1){
					dmap.put(key, null);
				}else{
					VDipAuthDesignVO[] vol1=new VDipAuthDesignVO[vol.length-1];
					for(int i=0;i<vol.length-1;i++){
						if(i<row){
							vol1[i]=vol[i];
						}else{
							vol1[i]=vol[i+1];
						}
					}
					dmap.put(key, vol1);
				}
				key=index+"-"+index2+"r";
				VDipAuthDesignVO[] vor=dmap.get(key);
				if(vo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(vo.getDatatype().toUpperCase())>=0)){
					vo.setContype("右");
				}else{
					vo.setContype("左");
					vo.setDesigplen(null);
				}
				if(vor==null){
					vor=new VDipAuthDesignVO[1];
					vo.setDisno(1);
					vor[0]=vo;
					dmap.put(key, vor);
				}else{
					VDipAuthDesignVO[] vor1=new VDipAuthDesignVO[vor.length+1];
					for(int i=0;i<vor.length;i++){
						vor1[i]=vor[i];
					}
					vo.setDisno(vor.length+1);
					vor1[vor.length]=vo;
					dmap.put(key, vor1);
				}
				
				if(isAuthorizeBrow&&index==0){
					dmap.put(notsavekey, dmap.get(key));
				}
				initjp(index, index2);
			}
		}else if(e.getSource().equals(jButton2RR)){
			String keyl=index+"-"+index2+"l";
			if(isAuthorizeBrow&&index==1){
				return;
			}
			String key=index+"-"+index2+"r";
			if(dmap.get(keyl)!=null){
				VDipAuthDesignVO[] vol = null;
				int[] rows =getjp1().getBillTable().getSelectedRows();
				if(null != rows && rows.length>1){
					vol = new VDipAuthDesignVO[rows.length];
					ArrayList<Integer> rowList = new ArrayList<Integer>();
					for (int i = 0; i < rows.length; i++) {
						vol[i] = dmap.get(keyl)[rows[i]];
						rowList.add(rows[i]);
					}
					VDipAuthDesignVO[] VDipAuthDesignVOs = dmap.get(keyl);
					ArrayList<VDipAuthDesignVO> list = new ArrayList<VDipAuthDesignVO>();
					for (int i = 0; i < VDipAuthDesignVOs.length; i++) {
						if(!rowList.contains(i)){
							list.add(VDipAuthDesignVOs[i]);
						}
					}
					if(list.size()>0){
						dmap.put(keyl, list.toArray(new VDipAuthDesignVO[list.size()]));
					}else{
						dmap.put(keyl, null);
					}
				}else{
					vol=dmap.get(keyl);
					dmap.put(keyl, null);
				}
				if(dmap.get(key)==null||dmap.get(key).length==0){
					for(int i=0;i<vol.length;i++){
						VDipAuthDesignVO vo=vol[i];
						if(vo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(vo.getDatatype().toUpperCase())>=0)){
							vo.setContype("右");
						}else{
							vo.setContype("左");
							vo.setDesigplen(null);
						}
						vol[i].setDisno(i+1);
					}
					dmap.put(key, vol);
				}else{
					VDipAuthDesignVO[] vor=dmap.get(key);
					VDipAuthDesignVO[] vor1=new VDipAuthDesignVO[vor.length+vol.length];
					for(int i=0;i<vor.length;i++){
						
						vor1[i]=vor[i];
						vor1[i].setDisno(i+1);
					}
					int len=vor.length;
					for(int i=vor.length;i<vor1.length;i++){
						vor1[i]=vol[i-len];
						vor1[i].setDisno(i+1);
						VDipAuthDesignVO vo=vor1[i];
						if(vo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(vo.getDatatype().toUpperCase())>=0)){
							vo.setContype("右");
						}else{
							vo.setContype("左");
							vo.setDesigplen(null);
						}
					}
					dmap.put(key, vor1);
				}
				if(isAuthorizeBrow&&index==0){
					dmap.put(notsavekey, dmap.get(key));
				}
				initjp(index, index2);
			}
		}else if(e.getSource().equals(jButtonCopy)){
			String keyl=index+"-"+index2+"l";
			if(isAuthorizeBrow&&index==1){
				return;
			}
			String key=index+"-"+index2+"r";
			if(index2==4){
				dmap.put(keyl,dmap.get("0-0l"));
				dmap.put(key,dmap.get("0-0r"));
			}else if(index2==5){
				dmap.put(keyl,dmap.get("0-1l"));
				dmap.put(key,dmap.get("0-1r"));
			}
			initjp(index, index2);
		}else if(e.getSource().equals(jButton3L)){
			int row=getjp2().getBillTable().getSelectedRow();
			if(row>=0){
				String key=index+"-"+index2+"r";
				if(isAuthorizeBrow&&index==1){
					dmap.get(key)[row].setConsvalue(null);
					dmap.put(0+"-"+index2+"r", dmap.get(key));
					initjp(index, index2);
					return;
				}
				VDipAuthDesignVO[] vor=dmap.get(key);
				if(index2==0){
					/*在显示字段中去掉主键不能往左选择*/
//					if(pkfields!=null&&pkfields.length>index){
//						if(vor[row].getPk_datadefinit_b().equals(pkfields[index])){
//							return;
//						}
//					}
				}
				if(vor.length==1){
					dmap.put(key, null);
				}else{
					VDipAuthDesignVO[] vor1=new VDipAuthDesignVO[vor.length-1];
					for(int i=0;i<vor1.length;i++){
						if(i<row){
							vor1[i]=vor[i];
						}else{
							vor1[i]=vor[i+1];
						}
						vor1[i].setDisno(i+1);
					}
					dmap.put(key, vor1);
				}
				if(isAuthorizeBrow&&index==0){
					dmap.put(notsavekey, dmap.get(key));
				}
				VDipAuthDesignVO vo=vor[row];
				vo.setDisno(null);
				vo.setConspk(null);
				vo.setConsvalue(null);
				key=index+"-"+index2+"l";
				VDipAuthDesignVO[] vol=dmap.get(key);
				if(vol==null){
					dmap.put(key,new VDipAuthDesignVO[]{vo});
				}else{
					VDipAuthDesignVO[] vol1=new VDipAuthDesignVO[vol.length+1];
					for(int i=0;i<vol.length;i++){
						vol1[i]=vol[i];
					}
					vol1[vol1.length-1]=vo;
					dmap.put(key, vol1);
				}
				
				initjp(index, index2);
			}
		
		}else if(e.getSource().equals(jButton4LL)){
			if(index==1){
				return;
			}
			String key=index+"-"+index2+"r";
			String key2=index+"-"+index2+"l";
			if(dmap.get(key)!=null&&dmap.get(key).length>0){
				VDipAuthDesignVO[] vor=dmap.get(key);
				VDipAuthDesignVO[] vol=dmap.get(key2);
				if(isAuthorizeBrow){//导入
					if(index==1){
						for(int i=0;i<vor.length;i++){
							vor[i].setConsvalue(null);
						}
						dmap.put(key, vor);
						dmap.put(0+"-"+index2+"r", vor);
						initjp(index,index2);
						return;
					}
					if(index==0){
						dmap.put(1+"-"+index2+"r", null);
					}
				}
				int[] rows=getjp2().getBillTable().getSelectedRows();
				if(null != rows && rows.length>0){
					vor=new VDipAuthDesignVO[rows.length];
					ArrayList<Integer> rowList = new ArrayList<Integer>();
					for (int i = 0; i < rows.length; i++) {
						vor[i]=dmap.get(key)[rows[i]];
						rowList.add(rows[i]);
					}
					VDipAuthDesignVO[] VDipAuthDesignVOs = dmap.get(key);
					ArrayList<VDipAuthDesignVO> list = new ArrayList<VDipAuthDesignVO>();
					for (int i = 0; i < VDipAuthDesignVOs.length; i++) {
						if(!rowList.contains(i)){
							list.add(VDipAuthDesignVOs[i]);
						}
					}
					if(list.size()>0){
						dmap.put(key, list.toArray(new VDipAuthDesignVO[list.size()]));
					}else{
						dmap.put(key, null);
					}
				}else{
					dmap.put(key, null);
				}
				
				int start;
				VDipAuthDesignVO[] vol1=null;
				if(vol!=null){
					vol1=new VDipAuthDesignVO[vor.length+vol.length];
					start=vol.length;
				}else{
					vol1=new VDipAuthDesignVO[vor.length];
					start=0;
				}
				int j=0;
				for(int i=0;i<vol1.length;i++){
					if(i<start){
						vol1[i]=vol[i];
					}else{
						vol1[i]=vor[j];
						vol1[i].setDisno(null);
						vol1[i].setConsvalue(null);
						vol1[i].setConspk(null);
						j++;
					}
				}
				dmap.put(key2, vol1);
				/*去掉主键不能忘左边选择   start*/
//				if(index2==0&&(pkfields!=null&&pkfields.length>index)){
//					vol=dmap.get(key2);
//					VDipAuthDesignVO[] vo2=new VDipAuthDesignVO[vol.length-1];
//					VDipAuthDesignVO[] vo2l=new VDipAuthDesignVO[1];
//					boolean hasfindkey=false;
//					for(int i=0;i<vol.length;i++){
//						if(!vol[i].getPk_datadefinit_b().equals(pkfields[index])){
//							if(!hasfindkey){
//								vo2[i]=vol[i];
//							}else{
//								vo2[i-1]=vol[i];
//							}
//						}else {
//							hasfindkey=true;
//							vol[i].setDisno(1);
//							vo2l[0]=vol[i];
//						}
//					}
//					dmap.put(key2, vo2);
//					
//					dmap.put(key, vo2l);
//				}
				/*去掉主键不能忘左边选择   start*/
				initjp(index,index2);
			}
		}else if(e.getSource().equals(jComboBox1)){
			initjp(index,index2);
		}else if(e.getSource().equals(jComboBox2)){
			if(isAuthorizeBrow){
				jComboBox1.setModel(jComboBox1Model_cl);
				jComboBox1.setSelectedIndex(0);
				jComboBox1.addActionListener(this);
				jComboBox1.addMouseListener(this);
				index=0;
			}
			
			initjp(index,index2);
			getjp2().getBodyPanel().hideTableCol("is_single_list");
			getjp2().getBodyPanel().hideTableCol("is_list");
			if(index2==4 || index2==5){
				jButtonCopy.setVisible(true);
			}else{
				jButtonCopy.setVisible(false);
			}
			
			if(index2==0){
				//"desigplen"  designlen   contype
				getjp2().getBodyPanel().showTableCol("desigplen");//.getBodyItem("desigplen").setShow(true);
				getjp2().getBodyPanel().showTableCol("designlen");//.setShow(true);
				getjp2().getBodyPanel().showTableCol("contype");//.setShow(true);
				getjp2().getBodyPanel().showTableCol("islock");
			}else if(index2==4){
				//"desigplen"  designlen   contype
				getjp2().getBodyPanel().showTableCol("desigplen");//.getBodyItem("desigplen").setShow(true);
				getjp2().getBodyPanel().showTableCol("designlen");//.setShow(true);
				getjp2().getBodyPanel().hideTableCol("contype");//.setShow(true);
				getjp2().getBodyPanel().showTableCol("islock");
				getjp2().getBodyPanel().showTableCol("is_single_list");
				getjp2().getBodyPanel().showTableCol("is_list");
			}else{
				getjp2().getBodyPanel().hideTableCol("desigplen");
				getjp2().getBodyPanel().hideTableCol("designlen");
				getjp2().getBodyPanel().hideTableCol("contype");
				if(index2!=1 && index2!=5){
					getjp2().getBodyPanel().hideTableCol("islock");
				}else{
					getjp2().getBodyPanel().showTableCol("islock");
				}
			}
			
//			if(index2==2){
//				if(isAuthorizeBrow){
//					getjp2().getBodyPanel().showTableCol("consvalue");
//				}
//			}else{
//				getjp2().getBodyPanel().hideTableCol("consvalue");
//			}
			
		}else if(e.getSource().equals(upbtn)){
			int row=getjp2().getBillTable().getSelectedRow();
			int rowcount=getjp2().getBillTable().getRowCount();
			if(row<=0||rowcount==1){
				return;
			}else{
				String key=index+"-"+index2+"r";
				VDipAuthDesignVO[] vos=dmap.get(key);
				VDipAuthDesignVO upvo=(VDipAuthDesignVO) vos[row].clone();
				VDipAuthDesignVO downvo=(VDipAuthDesignVO) vos[row-1].clone();
				upvo.setDisno(row);
				downvo.setDisno(row+1);
				vos[row]=downvo;
				vos[row-1]=upvo;
				dmap.put(key, vos);
				if(isAuthorizeBrow){
					if(index==0){
						dmap.put(notsavekey, dmap.get(key));
					}
					if(index==1){
						dmap.put(0+"-"+index2+"r", dmap.get(key));
					}
				}
				
				initjp(index,index2);
				getjp2().getBillTable().changeSelection(row-1,0,false,false);
//				getjp2().getBillTable().changeSelection(row-1,1,true,false);
//				getjp2().getBillTable().changeSelection(row-1,2,true,false);
//				getjp2().getBillTable().changeSelection(row-1,3,true,false);
//				getjp2().getBillTable().changeSelection(row-1,4,true,false);
//				getjp2().getBillTable().changeSelection(row-1,5,true,false);
				
			}
		}else if(e.getSource().equals(dobtn)){
			int row=getjp2().getBillTable().getSelectedRow();
			int rowcount=getjp2().getBillTable().getRowCount();
			if(row<0||rowcount==(row+1)){
				return;
			}else{
				String key=index+"-"+index2+"r";
				VDipAuthDesignVO[] vos=dmap.get(key);
				VDipAuthDesignVO upvo=(VDipAuthDesignVO) vos[row+1].clone();
				VDipAuthDesignVO downvo=(VDipAuthDesignVO) vos[row].clone();
				upvo.setDisno(row+1);
				downvo.setDisno(row+2);
				vos[row+1]=downvo;
				vos[row]=upvo;
				dmap.put(key, vos);
				if(isAuthorizeBrow){
					if(index==0){
						dmap.put(notsavekey, dmap.get(key));
					}
					if(index==1){
						dmap.put(0+"-"+index2+"r", dmap.get(key));
					}
				}
				initjp(index,index2);
				getjp2().getBillTable().changeSelection(row+1,0,false,false);
			}
			
		}
	}
	public Map<String,VDipAuthDesignVO[]> getDMAP(){
		return dmap;
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
		// TODO Auto-generated method stub
		System.out.println("bbb");
		return false;
	}

	public String getNotsavekey() {
		return notsavekey;
	}

	public void setNotsavekey(String notsavekey) {
		this.notsavekey = notsavekey;
	}

	public ArrayList<String> getNotsaveList() {
		return notsaveList;
	}

	public void setNotsaveList(ArrayList<String> notsaveList) {
		this.notsaveList = notsaveList;
	}
	
	
}
