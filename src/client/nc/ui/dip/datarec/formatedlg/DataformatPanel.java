package nc.ui.dip.datarec.formatedlg;

import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.bd.ref.DataFormateCopyTreeRefModel;
import nc.ui.bd.ref.MessageLogoRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillModelDecimalListener;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.consttab.DipConsttabVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.dip.messagelogo.MessagelogoVO;
import nc.vo.dip.messtype.MesstypeVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
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
public class DataformatPanel extends javax.swing.JPanel implements ActionListener,MouseListener,IBillModelDecimalListener{
	private CopyFormatPanl refpnlOrgize = null;
	private JPanel jPButton;
	private JComboBox jComboBox2;//顺序对照
	private JComboBox jComboBox1;//数据流类型
	private JComboBox jCombox3;//系统标志
	private JComboBox filetype;//文件类型
	private JTextField mergeCountTextField;//合并数
	private JTextField sendPageRunSysTextField;//合并分页参数
	private UIRefPane delayedTextField;//延时时间
	private JComboBox mergeStyleComboBox;//合并类型
	private JComboBox sendStyleComboBox;//发送类型

//	private JPanel jPanel2;
//	private JScrollPane jPanel2;
	private JLabel jLabel2;//对照方式
	private JLabel jLabel1;//数据流类型
	private JButton jButton4;
	private JButton jButton3;
	private JButton jButton2;
	private JButton jButton1;
	private JButton upBut;
	private JButton downBut;
	private DipDatarecHVO hvo=null;
	private UITabbedPane jTabeP = null;
	private UITabbedPane jTabePR = null;
	private BillCardPanel messField;
	private BillCardPanel jpField;
	private BillCardPanel jpFormat;
	private BillCardPanel rightJpFormat;
	private BillCardPanel rightXTJpFormat;
	private BillCardPanel rightBackJpFormat;
	private BillCardPanel rightXmlJpFormat;
	
	private Map<String,DataformatdefBVO[]> dmap=new HashMap<String, DataformatdefBVO[]>();//数据的一个map,这个map的key是i-l,i-k,value是 dip
//	dmap.put(j+"-r", bvos);//这个j是数据类型的index，bvos是显示右边已经定义的格式，DataformatdefBVO[] bvos
//	dmap.put(index+"-l", jlvos);//这个是index是左边table的标签index，jlvos是数据同步定义的业务表的所有字段加工后的DataformatdefBVO[]，
//	dmap.put("ms", msvos);//这个是消息标志
	private Map<String,DipDatarecSpecialTab[]> specialmap=new HashMap<String,DipDatarecSpecialTab[]>();
	public DataformatdefBVO[] ziduandefbvo=null;
	private String[] pks=null;//文件导入、开始标志、结束标志、。。。
	private String[] ipks=null;//文件导入，开始标志，等对应的pk
	private String[] lx=null;//顺序，字段
	private int flowtype=0;
	private DataformatdefHVO[] hvos;//同步定义的所有数据流类型对象
	private String mergecount="";
	private String sendPageRunSys="";
	private String mergeStyle="0";//合并类型
	private String sendStyle="0";//发送类型
	boolean esb=false;//是否是ESB接收格式
	String filetype_txt="txt";
	String filetype_xml="xml";
	String filetype_excel="Excel";
	/*liyunzhe add 2012-06-06 start */
	public static String XTBZ="系统标志";
	public static String ZDBZ="站点标志";
	public static String YWBZ="业务标志";
	public static String SPECIAL_XT="xt";
	public static String SPECIAL_XML="xml";
	public static String SPECIAL_BACK="back";

	
	public static final String MESSTYPE_CSKS_PK="0001AA100000000142YQ";
	public static final String MESSTYPE_CSSJ_PK="0001AA100000000142YS";
	public static final String MESSTYPE_CSJS_PK="0001ZZ10000000018K7M";
	public static final String MESSTYPE_HZBZ_PK="0001ZZ1000000001GFD7";
	public static final String MESSTYPE_ZDZQ_PK="0001AA1000000001HYWO";
	public static final String MESSTYPE_GSWJ_PK="0001AA1000000001HYWP";
	public static final String MESSTYPE_ZJB_PK="0001AA1000000001HYWQ";
	public static final String MESSTYPE_WEBSERVICEZQ_PK="0001AA1000000003M2VY";
	/* MESSTYPE_CSKS 和 MESSTYPE_CSKS_LX 只是初始化一下，在使用时候会现在数据库查询一下然后在赋值。*/
	public static String MESSTYPE_CSKS="传输开始标志";
	public static String MESSTYPE_CSSJ="传输数据标志";
	public static String MESSTYPE_CSJS="传输结束标志";
	public static String MESSTYPE_HZBZ="回执标志";
	public static String MESSTYPE_ZDZQ="主动抓取";
	public static String MESSTYPE_GSWJ="格式文件";
	public static String MESSTYPE_ZJB="中间表";
	public static String MESSTYPE_WEBSERVICEZQ="webservice抓取";
	public static String[] MESSTYPE_CSKS_LX={"顺序"};
	public static String[] MESSTYPE_CSSJ_LX={"顺序"};
	public static String[] MESSTYPE_CSJS_LX={"顺序"};
	public static String[] MESSTYPE_HZBZ_LX={"顺序"};
	public static String[] MESSTYPE_ZDZQ_LX={"顺序","字段"};
	public static String[] MESSTYPE_GSWJ_LX={"顺序","字段"};
	public static String[] MESSTYPE_ZJB_LX={"顺序"};
	public static String[] MESSTYPE_WEBSERVICEZQ_LX={"顺序","字段"};
	
	
	public static String BZT="标志头";
	public static String BZW="标志尾";
	public static String KSBZ="开始标志";
	public static String JSBZ="结束标志";
	public static String GDBZ="固定标志";
	public static String YWSJ="业务数据";
	public static String ZDY="自定义";
	
	
	
	public static String RIGHT_GSSJ="格式数据";
	public static String RIGHT_GDGS="固定格式";
	public static String RIGHT_HZGS="回执格式";
	public static String RIGHT_XMLGS="XML格式";
	
	public static String RIGHT_XMLGS_ROOT="根标签";
	public static String RIGHT_XMLGS_ONT="一级标签";
	public static String RIGHT_XMLGS_TWO="二级标签";
	/*liyunzhe add 2012-06-06 end */
	boolean iszjb=false;
	public static RetMessage getStaticValue(){
		SuperVO[] messtypevos=null;
		try {
		  messtypevos=HYPubBO_Client.queryByCondition(MesstypeVO.class, MesstypeVO.PK_MESSTYPE+" in ('"+MESSTYPE_CSKS_PK+"',"+
					"'"+MESSTYPE_CSSJ_PK+"',"+
					"'"+MESSTYPE_CSJS_PK+"',"+
					"'"+MESSTYPE_HZBZ_PK+"',"+
					"'"+MESSTYPE_GSWJ_PK+"',"+
					"'"+MESSTYPE_ZDZQ_PK+"',"+
					"'"+MESSTYPE_ZJB_PK+"',"+
					"'"+MESSTYPE_WEBSERVICEZQ_PK+"') and nvl(dr,0)=0 "
					);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(messtypevos!=null&&messtypevos.length==8){
			for(int i=0;i<messtypevos.length;i++){
				String pk=messtypevos[i].getPrimaryKey();
				Object nameobj=messtypevos[i].getAttributeValue(MesstypeVO.NAME);
				Object princobj=messtypevos[i].getAttributeValue(MesstypeVO.PRINC);
				String name="";
				String princ[]=null;
				if(nameobj!=null&&nameobj.toString().trim().length()>0){
					name=nameobj.toString();
				}
				if(princobj!=null&&princobj.toString().length()>0){
					String str[]=princobj.toString().split("，");
					if(str!=null&&str.length>=1){
						princ=str;
					}
				}
				if(pk!=null){
					if(pk.equals(MESSTYPE_CSKS_PK)){
						MESSTYPE_CSKS=name;
						MESSTYPE_CSKS_LX=princ;
					}else if(pk.equals(MESSTYPE_CSSJ_PK)){
						MESSTYPE_CSSJ=name;
						MESSTYPE_CSSJ_LX=princ;
					}else if(pk.equals(MESSTYPE_CSJS_PK)){
						MESSTYPE_CSJS=name;
						MESSTYPE_CSJS_LX=princ;
					}else if(pk.equals(MESSTYPE_HZBZ_PK)){
						MESSTYPE_HZBZ=name;
						MESSTYPE_HZBZ_LX=princ;
					}else if(pk.equals(MESSTYPE_GSWJ_PK)){
						MESSTYPE_GSWJ=name;
						MESSTYPE_GSWJ_LX=princ;
					}else if(pk.equals(MESSTYPE_ZDZQ_PK)){
						MESSTYPE_ZDZQ=name;
						MESSTYPE_ZDZQ_LX=princ;
					}else if(pk.equals(MESSTYPE_ZJB_PK)){
						MESSTYPE_ZJB=name;
						MESSTYPE_ZJB_LX=princ;
					}else if(pk.equals(MESSTYPE_WEBSERVICEZQ_PK)){
						MESSTYPE_WEBSERVICEZQ=name;
						MESSTYPE_WEBSERVICEZQ_LX=princ;
					}else{
						return new RetMessage(false,"消息标志表数据错误！");
					}
				}
			}
		}else{
			return new RetMessage(false,"消息标志表数据错误！");
		}
		return new RetMessage(true,"");
	}
	
	public IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public boolean isEsb(){
		return esb;
	}
	public String[] getPks(){
		return pks;
	}
	public boolean isZjb(){
		return iszjb;
	}
	public boolean isinput(){
		if(hvo==null){
			return true;
		}else if(hvo.getIoflag().equals("输入")){
			return true;
		}else {
			return false;
		}
	}
	public DataformatdefHVO[] getHvos(){
		if(hvos!=null&&hvos.length>0){
			for(int i=0;i<hvos.length;i++){
				if(filetype!=null){
					hvos[i].setFiletype(filetype.getSelectedIndex());
				}
				hvos[i].setBeginsign(getFgbj1Pnl().getRefPK());
				hvos[i].setEndsign(getFgbj2Pnl().getRefPK());
				if(mergeStyleComboBox!=null&&mergeStyleComboBox.getSelectedIndex()!=0){
					hvos[i].setMergecount(Integer.parseInt(mergeCountTextField.getText()));
					hvos[i].setPagerunsys(Integer.parseInt(sendPageRunSysTextField.getText()));
					hvos[i].setMergemark(getMergemarkRefPanelPnl().getRefPK());
					hvos[i].setMergestyle(mergeStyleComboBox.getSelectedIndex()+"");
				}else{
					hvos[i].setMergecount(null);
					hvos[i].setMergemark(null);
					hvos[i].setMergestyle(null);
					hvos[i].setPagerunsys(null);
				}
				if(sendStyleComboBox!=null){
					hvos[i].setSendstyle(sendStyleComboBox.getSelectedIndex()+"");
				}
				if(delayedTextField!=null&&delayedMap!=null){
					if(delayedMap.get(ipks[i])!=null){
						hvos[i].setDelayed(delayedMap.get(ipks[i]));
					}else{
						hvos[i].setDelayed(null);
					}
					
				}
				
			}
		}
		return hvos;
	}
	public Map<String,DataformatdefBVO[]> getDmap(){
		return dmap;
	}
	public String[] getLx(){
		return lx;
	}

	public Map<String, DipDatarecSpecialTab[]> getSpecialmap() {
		return specialmap;
	}
	public void doCopy(String pk){
		try {
			DataformatdefBVO[] bvos=(DataformatdefBVO[]) HYPubBO_Client.queryByCondition(DataformatdefBVO.class, "pk_dataformatdef_h='"+pk+"' and nvl(dr,0)=0 order by code");
			List<DataformatdefBVO> list =new ArrayList<DataformatdefBVO>();
			if(bvos!=null&&bvos.length>0){
				//查询数据定义的附表里边，Ename在格式里边的那些字段
				DipDatadefinitBVO[] dbvos1=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,
						"pk_datadefinit_h='"+hvo.getMemorytable()+"' and ename in" +
								" (select datakind from dip_dataformatdef_b where pk_dataformatdef_h='"+
						pk+"' and vdef1 is not null ) ");
				int k=0;
				//循环的复制的那个表的格式定义的格式
				for(int i=0;i<bvos.length;i++){
					DataformatdefBVO bv=bvos[i];
					if(bv.getVdef2()==null){
						
					}else if(bv.getVdef2()!=null&&!bv.getVdef2().equals(DataformatPanel.YWSJ)){
						k++;
						bv.setCode(k);
						list.add(bv);
					}else{
						if(dbvos1!=null&&dbvos1.length>0){
							for(int j=0;j<dbvos1.length;j++){
								if(bv.getDatakind().equals(dbvos1[j].getEname())){
									k++;
									bv.setCode(k);
									bv.setVdef1(dbvos1[j].getPrimaryKey());
									list.add(bv);
									break;
								}
							}
						}
					}
				}
			}
			int index=getJComboBox1().getSelectedIndex();
			if(list!=null&&list.size()>0){
				dmap.put(index+"-r", list.toArray(new DataformatdefBVO[list.size()]));
			}else{
				dmap.put(index+"-r", null);
			}
			
			
			DipDatadefinitBVO[] dbvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+hvo.getMemorytable()+"' and ename not in (select datakind from dip_dataformatdef_b where pk_dataformatdef_h='"+
					pk+"' and vdef1 is not null) order by ename");
			if(dbvos!=null&&dbvos.length>0){
				DataformatdefBVO[] jlvos=new DataformatdefBVO[dbvos.length];
				for(int j=0;j<dbvos.length;j++){
					jlvos[j]=new DataformatdefBVO();
					jlvos[j].setDatakind(dbvos[j].getEname());
					jlvos[j].setVdef1(dbvos[j].getPrimaryKey());
					jlvos[j].setVdef2(DataformatPanel.YWSJ);
					jlvos[j].setVdef3(dbvos[j].getCname());
					jlvos[j].setDatakind(dbvos[j].getEname());
				}
				dmap.put(index+"-l", jlvos);
			}else{
				dmap.put(index+"-l", null);
			}
			String sql="select h.pk_datarec_h from dip_dataformatdef_h h where h.pk_dataformatdef_h='"+pk+"' and nvl(dr,0)=0 ";
			String pk_datarec_h=iq.queryfield(sql);
			if(pk_datarec_h!=null&&pk_datarec_h.trim().length()>0){
				specialmap=new HashMap<String, DipDatarecSpecialTab[]>();
				querySpecialValues(pk_datarec_h);
			}
//			DataformatdefHVO hvo=(DataformatdefHVO) HYPubBO_Client.queryByPrimaryKey(DataformatdefHVO.class, pk);
//			if(hvo.getBeginsign()!=null){
//				getFgbj1Pnl().setPK(hvo.getBeginsign());
//			}
//			hvos[i].setBeginsign(getFgbj1Pnl().getRefPK());
			initjp(index);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new DataformatPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public DataformatPanel() {
		super();
		initGUI();
	}
	public DataformatPanel(DipDatarecHVO hvo) {
		super();
		this.hvo=hvo;
		initGUI();
		initjp(0);
		
	}
	public void initjp(int pki){
		DataformatdefBVO[] vos=dmap.get(pki+"-"+"l");
		int rows=getJpField().getBillTable().getRowCount();
		if(rows>0){
			getJpField().getBillTable().changeSelection(0,0,true,true);
			for(int i=0;i<rows;i++){
				getJpField().delLine();
			}
		}
		 rows=getJpField().getBillTable().getRowCount();
			if(rows>0){
				getJpField().getBillTable().changeSelection(0,0,true,false);
				for(int i=0;i<rows;i++){
					getJpField().delLine();
				}
			}
		
		if(vos!=null&&vos.length>0){
			for(int i=0;i<vos.length;i++){
				getJpField().addLine();
			}
			getJpField().getBillModel().setBodyDataVO(vos);
			getJpField().getBillModel().setEnabled(false);
		}
		DataformatdefBVO[] vos1=dmap.get(pki+"-"+"r");
//		if(jComboBox2.getSelectedIndex()==1){
//			DataformatdefBVO[] vos2=dmap.get(pki+"-"+"r");
//			if(vos2!=null&&vos2[0]!=null){
//				 if(vos2[0].getCorreskind()!=null&&!vos2[0].getCorreskind().equals("")){
//					 dmap.put("ziduanmap", vos2);
//				 }
//			 }	
//		}
		 
		rows=getRightJpFormat().getBillTable().getRowCount();
		if(rows>0){
			getRightJpFormat().getBillTable().changeSelection(0,0,true,true);
			for(int i=0;i<rows;i++){
				getRightJpFormat().delLine();
			}
		}
		rows=getRightJpFormat().getBillTable().getRowCount();
		if(rows>0){
			getRightJpFormat().getBillTable().changeSelection(0,0,true,false);
			for(int i=0;i<rows;i++){
				getRightJpFormat().delLine();
			}
		}
		
	if(jComboBox2.getSelectedIndex()==0){
		if(vos1!=null&&vos1.length>0){
			for(int i=0;i<vos1.length;i++){
				getRightJpFormat().addLine();
			}
			getRightJpFormat().getBillModel().setBodyDataVO(vos1);
//			getjp2().getBillModel().setEnabled(false);
			getRightJpFormat().getBillModel().setEnabled(true);
//			getJpFormat().getBillModel().getItemByKey("disno").setEnabled(false);
//			getJpFormat().getBillModel().getItemByKey("cname").setEnabled(false);
//			getJpFormat().getBillModel().getItemByKey("ename").setEnabled(false);
//			getJpFormat().getBillModel().getItemByKey("isdisplay").setEnabled(true);//
		}
	}else{
		
		if(ziduandefbvo!=null&&ziduandefbvo.length>0){
			if(vos1!=null&&vos1.length>0){
				for(int i=0;i<vos1.length;i++){
					if(vos1[i].getCorreskind()==null||vos1[i].getCorreskind().length()<=0){
						for(int j=0;j<ziduandefbvo.length;j++){
							if(ziduandefbvo[j].getPk_datafornatdef_b().equals(vos1[i].getPk_datafornatdef_b())){
								if(ziduandefbvo[j].getCorreskind()!=null){
									vos1[i].setCorreskind(ziduandefbvo[j].getCorreskind());
								}
								break;
							}
						}
					}
				}
			}
		}
			if(vos1!=null&&vos1.length>0){
				for(int i=0;i<vos1.length;i++){
					getRightJpFormat().addLine();
				}
				getRightJpFormat().getBillModel().setBodyDataVO(vos1);
				getRightJpFormat().getBillModel().setEnabled(true);
			}
		}
	if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)||sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)){
		if(vos1!=null&&vos1.length>=5){
			if(DataformatPanel.BZT.equals(vos1[0].getVdef2())&&DataformatPanel.XTBZ.equals(vos1[1].getDatakind())&&DataformatPanel.ZDBZ.equals(vos1[2].getDatakind())&&
					DataformatPanel.YWBZ.equals(vos1[3].getDatakind())&&DataformatPanel.BZW.equals(vos1[vos1.length-1].getVdef2())){
				jCombox3.setSelectedIndex(0);
			}else{
				jCombox3.setSelectedIndex(1);
			}
		}else{
			jCombox3.setSelectedIndex(1);
		}	
	}
	
	
	 addSpecialTabValue();
	}
	private String sysSideBussinessCode[]=null;
	public String[] getSysSideBussinessCode(){
		return sysSideBussinessCode;
	}
	public void addSpecialTabValue(){
//		特殊标志添加
		 sysSideBussinessCode=IContrastUtil.getSysSideBussinessCode(hvo.getPk_xt(), hvo.getMemorytable());
		   if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
			   if(backcon.booleanValue()){
				   DipDatarecSpecialTab specialBack[]=specialmap.get(SPECIAL_BACK);
				   if(specialBack==null||specialBack.length==0){
					   DipDatarecSpecialTab specialB[]=new DipDatarecSpecialTab[3];
					   specialB[0]=new DipDatarecSpecialTab();
					   specialB[0].setNodenumber(1);
					   specialB[0].setName(DataformatPanel.XTBZ);
					   specialB[0].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialB[0].setIs_back(new UFBoolean(true));
					   specialB[0].setValue(sysSideBussinessCode[0]);
					   
					   specialB[1]=new DipDatarecSpecialTab();
					   specialB[1].setNodenumber(2);
					   specialB[1].setName(DataformatPanel.ZDBZ);
					   specialB[1].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialB[1].setIs_back(new UFBoolean(true));
					   specialB[1].setValue(sysSideBussinessCode[1]);
					   specialB[2]=new DipDatarecSpecialTab();
					   specialB[2].setNodenumber(3);
					   specialB[2].setName(DataformatPanel.YWBZ);
					   specialB[2].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialB[2].setIs_back(new UFBoolean(true));
					   specialB[1].setValue(sysSideBussinessCode[2]);
					   getRightBackFormat().getBillModel().setBodyDataVO(specialB);
					   specialmap.put(SPECIAL_BACK, specialB);
					   
				   }else{
					   for(int i=0;i<specialBack.length;i++){
						   if(specialBack[i].getNodenumber()==1&&specialBack[i].getName()!=null&&specialBack[i].getName().equals(DataformatPanel.XTBZ)){
							   if(specialBack[i].getValue()==null||specialBack[i].getValue().trim().equals("")){
								   specialBack[i].setValue(sysSideBussinessCode[0]);
							   }
						   }
						   if(specialBack[i].getNodenumber()==2&&specialBack[i].getName()!=null&&specialBack[i].getName().equals(DataformatPanel.ZDBZ)){
							   if(specialBack[i].getValue()==null||specialBack[i].getValue().trim().equals("")){
								   specialBack[i].setValue(sysSideBussinessCode[1]);
							   }
						   }
						   if(specialBack[i].getNodenumber()==3&&specialBack[i].getName()!=null&&specialBack[i].getName().equals(DataformatPanel.YWBZ)){
							   if(specialBack[i].getValue()==null||specialBack[i].getValue().trim().equals("")){
								   specialBack[i].setValue(sysSideBussinessCode[2]);
							   }
						   }
					   }
					   getRightBackFormat().getBillModel().setBodyDataVO(specialBack);
				   }
			   }
			  
			   
			   DipDatarecSpecialTab specialXTfixed[]=specialmap.get(SPECIAL_XT);
			   if(specialXTfixed==null||specialXTfixed.length==0){
				   DipDatarecSpecialTab specialXt[]=new DipDatarecSpecialTab[3];
				   specialXt[0]=new DipDatarecSpecialTab();
				   specialXt[0].setName(DataformatPanel.XTBZ);
				   specialXt[0].setNodenumber(1);
				   specialXt[0].setIs_xtfixed(new UFBoolean(true));
				   specialXt[0].setPk_datarec_h(hvo.getPk_datarec_h());
				   specialXt[0].setValue(sysSideBussinessCode[0]);
				   specialXt[1]=new DipDatarecSpecialTab();
				   specialXt[1].setName(DataformatPanel.ZDBZ);
				   specialXt[1].setNodenumber(2);
				   specialXt[1].setIs_xtfixed(new UFBoolean(true));
				   specialXt[1].setPk_datarec_h(hvo.getPk_datarec_h());
				   specialXt[1].setValue(sysSideBussinessCode[1]);
				   specialXt[2]=new DipDatarecSpecialTab();
				   specialXt[2].setNodenumber(3);
				   specialXt[2].setName(DataformatPanel.YWBZ);
				   specialXt[2].setIs_xtfixed(new UFBoolean(true));
				   specialXt[2].setPk_datarec_h(hvo.getPk_datarec_h());
				   specialXt[2].setValue(sysSideBussinessCode[2]);
				   getRightXTFormat().getBillModel().setBodyDataVO(specialXt);
				   specialmap.put(SPECIAL_XT, specialXt);
			   }else{
				   for(int i=0;i<specialXTfixed.length;i++){ 
					   if(specialXTfixed[i].getNodenumber()==1&&specialXTfixed[i].getName()!=null&&specialXTfixed[i].getName().equals(DataformatPanel.XTBZ)){
						   if(specialXTfixed[i].getValue()==null||specialXTfixed[i].getValue().trim().equals("")){
							   specialXTfixed[i].setValue(sysSideBussinessCode[0]);
						   }
					   }
					   if(specialXTfixed[i].getNodenumber()==2&&specialXTfixed[i].getName()!=null&&specialXTfixed[i].getName().equals(DataformatPanel.ZDBZ)){
						   if(specialXTfixed[i].getValue()==null||specialXTfixed[i].getValue().trim().equals("")){
							   specialXTfixed[i].setValue(sysSideBussinessCode[1]);
						   }
					   }
					   if(specialXTfixed[i].getNodenumber()==3&&specialXTfixed[i].getName()!=null&&specialXTfixed[i].getName().equals(DataformatPanel.YWBZ)){
						   if(specialXTfixed[i].getValue()==null||specialXTfixed[i].getValue().trim().equals("")){
							   specialXTfixed[i].setValue(sysSideBussinessCode[2]);
						   }
					   }
				  }
				   
				   getRightXTFormat().getBillModel().clearBodyData();
				   getRightXTFormat().getBillModel().setBodyDataVO(specialXTfixed);
				   getRightXmlFormat().updateValue();
			   }
		   }
		   
		   if(sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)&&hvos!=null&&hvos.length>0&&hvos[0]!=null){
			   if(filetype.getSelectedIndex()>=0&&filetype.getSelectedIndex()==1){
				   DipDatarecSpecialTab specialXml[]=specialmap.get(SPECIAL_XML);
				   if(specialXml==null||specialXml.length==0){
					   DipDatarecSpecialTab specialXml2[]=new DipDatarecSpecialTab[3];
					   specialXml2[0]=new DipDatarecSpecialTab();
					   specialXml2[0].setNodename(RIGHT_XMLGS_ROOT);
					   specialXml2[0].setNodenumber(1);
					   specialXml2[0].setIs_xml(new UFBoolean(true));
					   specialXml2[0].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialXml2[1]=new DipDatarecSpecialTab();
					   specialXml2[1].setNodename(RIGHT_XMLGS_ONT);
					   specialXml2[1].setNodenumber(2);
					   specialXml2[1].setIs_xml(new UFBoolean(true));
					   specialXml2[1].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialXml2[2]=new DipDatarecSpecialTab();
					   specialXml2[2].setNodenumber(3);
					   specialXml2[2].setNodename(RIGHT_XMLGS_TWO);
					   specialXml2[2].setIs_xml(new UFBoolean(true));
					   specialXml2[2].setPk_datarec_h(hvo.getPk_datarec_h());
					   getRightXTFormat().getBillModel().setBodyDataVO(specialXml2);
					   specialmap.put(SPECIAL_XML, specialXml2);
				   }else{
					   getRightXmlFormat().getBillModel().setBodyDataVO(specialXml);
				   }

			   }
			   DipDatarecSpecialTab specialXTfixed[]=specialmap.get(SPECIAL_XT);
			   if(specialXTfixed==null||specialXTfixed.length==0){
				   DipDatarecSpecialTab specialXt[]=new DipDatarecSpecialTab[3];
				   specialXt[0]=new DipDatarecSpecialTab();
				   specialXt[0].setName(DataformatPanel.XTBZ);
				   specialXt[0].setNodenumber(1);
				   specialXt[0].setIs_xtfixed(new UFBoolean(true));
				   specialXt[0].setPk_datarec_h(hvo.getPk_datarec_h());
				   specialXt[0].setValue(sysSideBussinessCode[0]);
				   specialXt[1]=new DipDatarecSpecialTab();
				   specialXt[1].setName(DataformatPanel.ZDBZ);
				   specialXt[1].setNodenumber(2);
				   specialXt[1].setIs_xtfixed(new UFBoolean(true));
				   specialXt[1].setPk_datarec_h(hvo.getPk_datarec_h());
				   specialXt[1].setValue(sysSideBussinessCode[1]);
				   specialXt[2]=new DipDatarecSpecialTab();
				   specialXt[2].setNodenumber(3);
				   specialXt[2].setName(DataformatPanel.YWBZ);
				   specialXt[2].setIs_xtfixed(new UFBoolean(true));
				   specialXt[2].setPk_datarec_h(hvo.getPk_datarec_h());
				   specialXt[2].setValue(sysSideBussinessCode[2]);
				   getRightXTFormat().getBillModel().setBodyDataVO(specialXt);
				   specialmap.put(SPECIAL_XT, specialXt);
			   }else{
				   for(int i=0;i<specialXTfixed.length;i++){ 
					   if(specialXTfixed[i].getNodenumber()==1&&specialXTfixed[i].getName()!=null&&specialXTfixed[i].getName().equals(DataformatPanel.XTBZ)){
						   if(specialXTfixed[i].getValue()==null||specialXTfixed[i].getValue().trim().equals("")){
							   specialXTfixed[i].setValue(sysSideBussinessCode[0]);
						   }
					   }
					   if(specialXTfixed[i].getNodenumber()==2&&specialXTfixed[i].getName()!=null&&specialXTfixed[i].getName().equals(DataformatPanel.ZDBZ)){
						   if(specialXTfixed[i].getValue()==null||specialXTfixed[i].getValue().trim().equals("")){
							   specialXTfixed[i].setValue(sysSideBussinessCode[1]);
						   }
					   }
					   if(specialXTfixed[i].getNodenumber()==3&&specialXTfixed[i].getName()!=null&&specialXTfixed[i].getName().equals(DataformatPanel.YWBZ)){
						   if(specialXTfixed[i].getValue()==null||specialXTfixed[i].getValue().trim().equals("")){
							   specialXTfixed[i].setValue(sysSideBussinessCode[2]);
						   }
					   }
				  }
				   getRightXTFormat().getBillModel().setBodyDataVO(specialXTfixed);
			   }
		   }
		   
	}
	
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
                		DataformatdefBVO[] vos=dmap.get(index+"-"+index2+"r");
                		int row=getRightJpFormat().getBillTable().getSelectedRow();
                		DefaultConstEnum o=(DefaultConstEnum) box.getSelectedItem();
                		if(o!=null){
//                			vos[row].setContype(o.getName());
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
            		DataformatdefBVO[] vos=dmap.get(index+"-"+index2+"r");
            		int row=getRightJpFormat().getBillTable().getSelectedRow();
                    
	            }
	        }
	    }
	 class HeadValueChangeListener implements ValueChangedListener {
	        // uirefpane + billitem
	        Hashtable<Component,BillItem> hashItem = new Hashtable<Component,BillItem>();

	        public void valueChanged(ValueChangedEvent e) {
	        	if(e.getSource().equals(delayedTextField)){
	        		String delayedText=delayedTextField.getText();
	        		if(delayedText!=null&&delayedText.trim().length()>0){
						String regex="\\d{1,9}";
						if(!delayedText.matches(regex)){
							delayedMap.put(ipks[jComboBox1.getSelectedIndex()], null);
						}else{
							delayedMap.put(ipks[jComboBox1.getSelectedIndex()], Integer.parseInt(delayedText));
						}
	        		}else{
	        			delayedMap.put(ipks[jComboBox1.getSelectedIndex()], null);
	        		}
	        		return;
	        	}
	        	
	        	
	        	
                JComponent editorComp = (JComponent) e.getSource();
                if (editorComp instanceof UIRefPane || editorComp instanceof UITextArea) {
                    BillItem item = hashItem.get(editorComp);
                    int index=jComboBox1.getSelectedIndex();
            		int index2=jComboBox2.getSelectedIndex();
            		DataformatdefBVO[] vos=dmap.get(index+"-r");
            		
            		Object value = null;
            		if (editorComp instanceof UIRefPane)
            			value = ((UIRefPane) editorComp).getText();
            		else if (editorComp instanceof UITextArea)
            			value = ((UITextArea) editorComp).getText();
            		 if(editorComp.getName().equals("correskind")){
            			int row=getRightJpFormat().getBillTable().getSelectedRow();
            			vos[row].setCorreskind(value.toString());//.setDesigplen(new Integer(value.toString()));
            		} if(editorComp.getName().equals("nodename")){
            			DipDatarecSpecialTab xmlvos[]=specialmap.get(SPECIAL_XML);
            			if(xmlvos==null||xmlvos.length==0){
            				xmlvos=new DipDatarecSpecialTab[3];
            				for(int i=0;i<xmlvos.length;i++){
            					xmlvos[i]=new DipDatarecSpecialTab();
            				}
            			}
            			int row=getRightXmlFormat().getBillTable().getSelectedRow();
            			xmlvos[row].setNodename(value.toString());
//            			getRightXmlFormat().getBillModel().getBodyValueVOs(xmlvos);
        				specialmap.put(SPECIAL_XML, xmlvos);
            		} if(editorComp.getName().equals("nodeproperty")){
            			DipDatarecSpecialTab xmlvos[]=specialmap.get(SPECIAL_XML);
            			if(xmlvos==null||xmlvos.length==0){
            				xmlvos=new DipDatarecSpecialTab[3];
            				for(int i=0;i<xmlvos.length;i++){
            					xmlvos[i]=new DipDatarecSpecialTab();
            				}
            			}
            			int row=getRightXmlFormat().getBillTable().getSelectedRow();
            			xmlvos[row].setNodeproperty(value.toString());
            			specialmap.put(SPECIAL_XML, xmlvos);
            		} if(editorComp.getName().equals("value")){
            			if(getRightTabbedPane().getSelectedIndex()==1){
            				DipDatarecSpecialTab xtvos[]=specialmap.get(SPECIAL_XT);
            				if(xtvos==null||xtvos.length==0){
            					xtvos=new DipDatarecSpecialTab[3];
                				for(int i=0;i<xtvos.length;i++){
                					xtvos[i]=new DipDatarecSpecialTab();
                				}
                			}
            				int row=getRightXTFormat().getBillTable().getSelectedRow();
            				int column=getRightXTFormat().getBillTable().getSelectedColumn();
            				getRightXTFormat().getBillTable().setValueAt(value.toString().toUpperCase(), row, column);
//            				getRightXTFormat().getBillModel().setValueAt(value.toString().toUpperCase(), row, "value");
            				xtvos[row].setValue(value.toString().toUpperCase());
//                			getRightXTFormat().getBillModel().getBodyValueVOs(xtvos);
            				specialmap.put(SPECIAL_XT, xtvos);
//            				initjp(jComboBox1.getSelectedIndex());
            				getRightXTFormat().updateValue();
            				getRightXTFormat().getBillTable().updateUI();
            				getRightTabbedPane().updateUI();
            			}
            			if(getRightTabbedPane().getSelectedIndex()==2){
	            			if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
	                				DipDatarecSpecialTab backvos[]=specialmap.get(SPECIAL_BACK);
	                				if(backvos==null||backvos.length==0){
	                					backvos=new DipDatarecSpecialTab[3];
	                    				for(int i=0;i<backvos.length;i++){
	                    					backvos[i]=new DipDatarecSpecialTab();
	                    				}
	                    			}
	                				int row=getRightBackFormat().getBillTable().getSelectedRow();
	                				int column=getRightBackFormat().getBillTable().getSelectedColumn();
	                				getRightBackFormat().getBillTable().setValueAt(value.toString().toUpperCase(), row, column);
	                				getRightBackFormat().updateUI();
	                				backvos[row].setValue(value.toString().toUpperCase());
	//                    			getRightBackFormat().getBillModel().getBodyValueVOs(backvos);
	                				specialmap.put(SPECIAL_BACK, backvos);
//	                				initjp(jComboBox1.getSelectedIndex());
	                				getRightTabbedPane().updateUI();
	                			
	            			}else{
	            				
	                				DipDatarecSpecialTab xmlkvos[]=specialmap.get(SPECIAL_XML);
	                				if(xmlkvos==null||xmlkvos.length==0){
	                					xmlkvos=new DipDatarecSpecialTab[3];
	                    				for(int i=0;i<xmlkvos.length;i++){
	                    					xmlkvos[i]=new DipDatarecSpecialTab();
	                    				}
	                    			}
	                				int row=getRightXmlFormat().getBillTable().getSelectedRow();
	                				xmlkvos[row].setValue(value.toString());
	//                    			getRightBackFormat().getBillModel().getBodyValueVOs(backvos);
	                				specialmap.put(SPECIAL_XML, xmlkvos);
	                			
	            			}
            			}
            		}
            		
            		/*else if(editorComp.getName().equals("contype")){
            			vos[row].setContype(value.toString());
            		}else{
            			vos[row].setDesignlen(new Integer(value.toString()));
            		}*/
	            }
	        }
	    }
	 private CopyFormatPanl getOrgnizeRefPnl(){
			if (refpnlOrgize == null){
				refpnlOrgize = new CopyFormatPanl(this);
				refpnlOrgize.setPreferredSize(new Dimension(150,22));
				refpnlOrgize.setRefInputType(1 /** 名称*/);
				DataFormateCopyTreeRefModel model = new DataFormateCopyTreeRefModel();
				refpnlOrgize.setRefModel(model);
				model.setWherePart( model.getWherePart()==null?"":model.getWherePart()+" and dip_datarec_h.sourcetype ='"+hvo.getSourcetype()+"'");
			}
			return refpnlOrgize;
		}
	 UIRefPane fgbj1=null;
	 UIRefPane fgbj2=null;
	 UIRefPane mergemarkRefPanel=null;
	 private UIRefPane getFgbj1Pnl(){
			if (fgbj1 == null){
				fgbj1 = new UIRefPane(this);
				fgbj1.setPreferredSize(new Dimension(150,22));
				fgbj1.setRefInputType(1 );
				MessageLogoRefModel model = new MessageLogoRefModel();
				fgbj1.setRefModel(model);
				
				
			}
			return fgbj1;
		}
	 private UIRefPane getFgbj2Pnl(){
			if (fgbj2 == null){
				fgbj2 = new UIRefPane(this);
				fgbj2.setPreferredSize(new Dimension(150,22));
				fgbj2.setRefInputType(1 /** 名称*/);
				MessageLogoRefModel model = new MessageLogoRefModel();
				fgbj2.setRefModel(model);
				
			}
			return fgbj2;
		}
		
	 private UIRefPane getMergemarkRefPanelPnl(){
			if (mergemarkRefPanel == null){
				mergemarkRefPanel = new UIRefPane(this);
				mergemarkRefPanel.setPreferredSize(new Dimension(150,22));
				mergemarkRefPanel.setRefInputType(1 );
				MessageLogoRefModel model = new MessageLogoRefModel();
				mergemarkRefPanel.setRefModel(model);
			}
			return mergemarkRefPanel;
		}
		private UITabbedPane getLeftTabbedPane() {
			if (jTabeP == null) {
				try {
					jTabeP = new UITabbedPane();
					if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
						jTabeP.setBounds(10+30, 85+28+10, 300, 350);
					}else{
						jTabeP.setBounds(10+30, 85, 300, 350);
						
					}
					jTabeP.setName("FiledTabbedPane");
					jTabeP.addTab("业务数据", getJpField());
					jTabeP.addTab("消息标志", getJpMessField());
					
				} catch (Exception ivjExc) {
					ivjExc.printStackTrace();
				}
			}
			return jTabeP;
		}
		private UITabbedPane getRightTabbedPane() {
			if (jTabePR == null) {
				try {
					jTabePR = new UITabbedPane();
//					370, 85, 400, 350
					if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
						jTabePR.setBounds(370+30, 85+28+10, 300, 350);
					}else{
						jTabePR.setBounds(370+30, 85, 300, 350);
						
					}
					jTabePR.setName("RightFiledTabbedPane");
					jTabePR.addTab(RIGHT_GSSJ, getRightJpFormat());
					
					if(sourcetype!=null&&sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
						jTabePR.addTab(RIGHT_GDGS, getRightXTFormat());
			            if(backcon.booleanValue()){
			            	jTabePR.addTab(RIGHT_HZGS, getRightBackFormat());
			            }
					}
					if(sourcetype!=null&&sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)){
						jTabePR.addTab(RIGHT_GDGS, getRightXTFormat());
						if(hvos[0].getFiletype()!=null&&hvos[0].getFiletype()==1){
							jTabePR.addTab(RIGHT_XMLGS, getRightXmlFormat());	
							getFgbj1Pnl().setValue(null);
							getFgbj1Pnl().setEnabled(false);
						}
					}
					
					jTabePR.addMouseListener(new MouseListener(){

						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							if(jTabePR.getSelectedIndex()==0){
								jCombox3.setEnabled(true);
							}else{
								jCombox3.setEnabled(false);
							}
						}

						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
						
					});
				} catch (Exception ivjExc) {
					ivjExc.printStackTrace();
				}
			}
			return jTabePR;
		}

		public BillCardPanel getRightJpFormat(){
			if(rightJpFormat==null){
				rightJpFormat=new BillCardPanel();
				rightJpFormat.loadTemplet("H4H8Hc", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				rightJpFormat.getBillTable().setSortEnabled(false);
				rightJpFormat.getBillModel().getItemByKey("code").setEnabled(false);
				rightJpFormat.getBillModel().getItemByKey("datakind").setEnabled(false);
				rightJpFormat.getBillModel().getItemByKey("correskind").setEnabled(false);
				rightJpFormat.getBillModel().getItemByKey("vdef2").setEnabled(false);
				rightJpFormat.getBillModel().getItemByKey("vdef3").setEnabled(false);
				
//				JCheckBox com=(JCheckBox) getjp2().getBillModel().getItemByKey("isdisplay").getComponent();
//				com.addActionListener(checkBoxItemListener);
//	            checkBoxItemListener.hashItem.put(com,  getjp2().getBillModel().getItemByKey("isdisplay"));
	            UIRefPane rf=(UIRefPane)rightJpFormat.getBillModel().getItemByKey("correskind").getComponent();
	            rf.addValueChangedListener(headValueChangeListener);
//	            UIRefPane rf1=(UIRefPane) getjp2().getBillModel().getItemByKey("desigplen").getComponent();
//	            rf1.addValueChangedListener(headValueChangeListener);
//	            UIComboBox rf2=(UIComboBox) getjp2().getBillModel().getItemByKey("contype").getComponent();
//	            rf2.addItemListener(comboBoxItemListener);
	            
//	            jpFormat.setBounds(370, 85, 400, 350);
			}
			return rightJpFormat;
		}
		public BillCardPanel getRightXTFormat(){
			if(rightXTJpFormat==null){
				rightXTJpFormat=new BillCardPanel();
				rightXTJpFormat.loadTemplet("H4H8Hc1", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				rightXTJpFormat.getBillTable().setSortEnabled(false);
//				rightXTJpFormat.getBodyPanel().hideTableCol("nodenumber");
				rightXTJpFormat.getBodyPanel().hideTableCol("nodename");
				rightXTJpFormat.getBodyPanel().hideTableCol("nodeproperty");
				
//				JCheckBox com=(JCheckBox) getjp2().getBillModel().getItemByKey("isdisplay").getComponent();
//				com.addActionListener(checkBoxItemListener);
//	            checkBoxItemListener.hashItem.put(com,  getjp2().getBillModel().getItemByKey("isdisplay"));
	            UIRefPane rf=(UIRefPane)rightXTJpFormat.getBillModel().getItemByKey("value").getComponent();
	            rf.addValueChangedListener(headValueChangeListener);
//	            UIRefPane rf1=(UIRefPane) getjp2().getBillModel().getItemByKey("desigplen").getComponent();
//	            rf1.addValueChangedListener(headValueChangeListener);
//	            UIComboBox rf2=(UIComboBox) getjp2().getBillModel().getItemByKey("contype").getComponent();
//	            rf2.addItemListener(comboBoxItemListener);
	            
//	            jpFormat.setBounds(370, 85, 400, 350);
			}
			return rightXTJpFormat;
		}
		public BillCardPanel getRightBackFormat(){
			if(rightBackJpFormat==null){
				rightBackJpFormat=new BillCardPanel();
				rightBackJpFormat.loadTemplet("H4H8Hc1", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				rightBackJpFormat.getBillTable().setSortEnabled(false);
//				rightBackJpFormat.getBodyPanel().hideTableCol("nodenumber");
				rightBackJpFormat.getBodyPanel().hideTableCol("nodename");
				rightBackJpFormat.getBodyPanel().hideTableCol("nodeproperty");
				
//				JCheckBox com=(JCheckBox) getjp2().getBillModel().getItemByKey("isdisplay").getComponent();
//				com.addActionListener(checkBoxItemListener);
//	            checkBoxItemListener.hashItem.put(com,  getjp2().getBillModel().getItemByKey("isdisplay"));
	            UIRefPane rf=(UIRefPane)rightBackJpFormat.getBillModel().getItemByKey("value").getComponent();
	            rf.addValueChangedListener(headValueChangeListener);
//	            UIRefPane rf1=(UIRefPane) getjp2().getBillModel().getItemByKey("desigplen").getComponent();
//	            rf1.addValueChangedListener(headValueChangeListener);
//	            UIComboBox rf2=(UIComboBox) getjp2().getBillModel().getItemByKey("contype").getComponent();
//	            rf2.addItemListener(comboBoxItemListener);
	            
//	            jpFormat.setBounds(370, 85, 400, 350);
			}
			return rightBackJpFormat;
		}
		public BillCardPanel getRightXmlFormat(){
			if(rightXmlJpFormat==null){
				rightXmlJpFormat=new BillCardPanel();
				rightXmlJpFormat.loadTemplet("H4H8Hc1", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				rightXmlJpFormat.getBillTable().setSortEnabled(false);
				
				rightXmlJpFormat.getBodyPanel().hideTableCol("name");
//				rightXmlJpFormat.getBodyPanel().hideTableCol("value");
//				JCheckBox com=(JCheckBox) getjp2().getBillModel().getItemByKey("isdisplay").getComponent();
//				com.addActionListener(checkBoxItemListener);
//	            checkBoxItemListener.hashItem.put(com,  getjp2().getBillModel().getItemByKey("isdisplay"));
	            UIRefPane rf=(UIRefPane)rightXmlJpFormat.getBillModel().getItemByKey("value").getComponent();
	            rf.addValueChangedListener(headValueChangeListener);
	            UIRefPane rf1=(UIRefPane)rightXmlJpFormat.getBillModel().getItemByKey("nodeproperty").getComponent();
	            rf1.addValueChangedListener(headValueChangeListener);
//	            UIRefPane rf1=(UIRefPane) getjp2().getBillModel().getItemByKey("desigplen").getComponent();
//	            rf1.addValueChangedListener(headValueChangeListener);
//	            UIComboBox rf2=(UIComboBox) getjp2().getBillModel().getItemByKey("contype").getComponent();
//	            rf2.addItemListener(comboBoxItemListener);
	            
//	            jpFormat.setBounds(370, 85, 400, 350);
			}
			return rightXmlJpFormat;
		}
		/**
		 * @desc 消息标志的字段
		 * */
		public BillCardPanel getJpMessField(){
			if(messField==null){
				messField=new BillCardPanel();
				messField.loadTemplet("H4H8Hc", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				messField.getBillTable().setSortEnabled(false);
				messField.getBodyPanel().hideTableCol("code");
				messField.getBodyPanel().hideTableCol("correskind");
			}
			return messField;
		}
		/**
		 * @desc 业务数据的字段选择
		 * */
		public BillCardPanel getJpField(){
			if(jpField==null){
				jpField=new BillCardPanel();
				jpField.loadTemplet("H4H8Hc", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				jpField.getBillTable().setSortEnabled(false);
				jpField.getBodyPanel().hideTableCol("code");
				jpField.getBodyPanel().hideTableCol("correskind");
			}
			return jpField;
		}
		/**
		 * @desc 格式的设置
		 * */
//	public BillCardPanel getJpFormat(){
//		if(jpFormat==null){
//			jpFormat=new BillCardPanel();
//			jpFormat.loadTemplet("H4H8Hc", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
//			jpFormat.getBillTable().setSortEnabled(false);
//			jpFormat.getBillModel().getItemByKey("code").setEnabled(false);
//			jpFormat.getBillModel().getItemByKey("datakind").setEnabled(false);
//			jpFormat.getBillModel().getItemByKey("correskind").setEnabled(false);
//			jpFormat.getBillModel().getItemByKey("vdef2").setEnabled(false);
//			jpFormat.getBillModel().getItemByKey("vdef3").setEnabled(false);
//			
////			JCheckBox com=(JCheckBox) getjp2().getBillModel().getItemByKey("isdisplay").getComponent();
////			com.addActionListener(checkBoxItemListener);
////            checkBoxItemListener.hashItem.put(com,  getjp2().getBillModel().getItemByKey("isdisplay"));
//            UIRefPane rf=(UIRefPane)jpFormat.getBillModel().getItemByKey("correskind").getComponent();
//            rf.addValueChangedListener(headValueChangeListener);
////            UIRefPane rf1=(UIRefPane) getjp2().getBillModel().getItemByKey("desigplen").getComponent();
////            rf1.addValueChangedListener(headValueChangeListener);
////            UIComboBox rf2=(UIComboBox) getjp2().getBillModel().getItemByKey("contype").getComponent();
////            rf2.addItemListener(comboBoxItemListener);
//            
//            jpFormat.setBounds(370, 85, 400, 350);
//		}
//		return jpFormat;
//	}
	/**
	 * @desc 格式的设置滚动框
	 * */
/*	public JPanel getJPFormat(){
		if(jPanel2==null){
			jPanel2=new JPanel();
			jPanel2.add(getJpFormat());
//			jPanel2.setViewportView(getJpFormat());
//			getJPFormat().getBounds();
//			jPanel2.setBounds(getJPFormat().getBounds());
			jPanel2.setBounds(413, 63, 500, 458);
		}
		return jPanel2;
	}*/
	private JPanel getJPButton() {
		if(jPButton == null) {
			jPButton = new JPanel();
			jPButton.setBounds(315+30, 111+28, 55, 300);
			jPButton.setLayout(null);
			{
				jButton4 = new JButton();
				jPButton.add(jButton4);
				jButton4.setText("<<");
				jButton4.setBounds(0, 150+28, 50, 20);
			}
			{
				jButton3 = new JButton();
				jPButton.add(jButton3);
				jButton3.setText("<");
				jButton3.setBounds(0, 105+28, 50, 20);
			}
			{
				jButton2 = new JButton();
				jPButton.add(jButton2);
				jButton2.setText(">>");
				jButton2.setBounds(0, 60+28, 50, 20);
			}
			{
				jButton1 = new JButton();
				jPButton.add(jButton1);
				jButton1.setText(">");
				jButton1.setBounds(0, 15+28, 50, 20);
			}
			{
				upBut=new JButton();
				jPButton.add(upBut);
				upBut.setText("上移");
				upBut.setBounds(0, 195+28, 50, 20);
				
				downBut=new JButton();
				jPButton.add(downBut);
				downBut.setText("下移");
				downBut.setBounds(0, 240+28, 50, 20);
			}
			jButton1.addActionListener(this);
			jButton2.addActionListener(this);
			jButton3.addActionListener(this);
			jButton4.addActionListener(this);
			upBut.addActionListener(this);
			downBut.addActionListener(this);
		}
		return jPButton;
	}
	private String type=null;
	private String sourcetype=null;
	private UFBoolean backcon=new UFBoolean(false);
	private void querySpecialValues(String recpk){
		DipDatarecSpecialTab[] specialXT=null;
		DipDatarecSpecialTab[] specialBack=null;
		DipDatarecSpecialTab[] specialXml=null;
		try {
			specialXT = (DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+recpk+"' and nvl(is_xtfixed,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
			specialBack=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+recpk+"' and nvl(is_back,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
			specialXml=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+recpk+"' and nvl(is_xml,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(specialXT!=null&&specialXT.length==3){
			specialmap.put(SPECIAL_XT, specialXT);
		}
		if(specialBack!=null&&specialBack.length>1){
			specialmap.put(SPECIAL_BACK, specialBack);
		}
		if(specialXml!=null&&specialXml.length>1){
			specialmap.put(SPECIAL_XML, specialXml);
		}
	}
	
	
	
	private void initGUI() {
		RetMessage ret=getStaticValue();
		if(!ret.getIssucc()){
			MessageDialog.showErrorDlg(this, "错误", ret.getMessage());
			return;
		}
		DataformatdefHVO dhvo=new DataformatdefHVO();

//		String type=null;
		if(!SJUtil.isNull(hvo)){
			//数据接收定义主表主键
			String recpk=hvo.getPk_datarec_h();
			 sourcetype=hvo.getSourcetype();//数据来源类型：由于是参照，存放的是主键
			UFBoolean trancon=hvo.getTrancon()==null?new UFBoolean(false):new UFBoolean(hvo.getTrancon());//传输标志控制
			 backcon=hvo.getBackcon()==null?new UFBoolean(false):new UFBoolean(hvo.getBackcon());//回执标志控制
			
			 
			try {
				DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, hvo.getPk_xt());

				//数据来源类型vo
				DipDataoriginVO ddvo=(DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype);
				type=ddvo.getName();//数据来源类型
				//格式定义的主键们，数据同步定义节点对应的所有格式主表vo
				DataformatdefHVO[] vos=(DataformatdefHVO[]) HYPubBO_Client.queryByCondition(DataformatdefHVO.class, "pk_datarec_h ='"+recpk+"' and nvl(dr,0)=0") ;
				querySpecialValues(recpk);
//				DipDatarecSpecialTab[] specialXT=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+recpk+"' and nvl(is_xtfixed,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
//				DipDatarecSpecialTab[] specialBack=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+recpk+"' and nvl(is_back,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
//				DipDatarecSpecialTab[] specialXml=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+recpk+"' and nvl(is_xml,'N')='Y' and nvl(dr,0)=0 order by  nodenumber");
//				if(specialXT!=null&&specialXT.length==3){
//					specialmap.put(SPECIAL_XT, specialXT);
//				}else{
//					
//				}
//				if(specialBack!=null&&specialBack.length>1){
//					specialmap.put(SPECIAL_BACK, specialBack);
//				}
//				if(specialXml!=null&&specialXml.length>1){
//					specialmap.put(SPECIAL_XML, specialXml);
//				}
				if(type==null){
					return;
				}
//				1	0001ZZ10000000018K7M	传输结束标志	0003	顺序						0	2011-06-17 16:31:32	Y
//				2	0001ZZ1000000001GFD7	回执标志	0004	顺序						0	2011-06-17 16:31:32	Y
//				3	0001AA100000000142YQ	传输开始标志	0001	顺序						0	2011-06-17 16:31:32	Y
//				4	0001AA100000000142YS	传输数据标志	0002	顺序						0	2011-06-17 16:31:32	Y
//				5	0001AA1000000001HYWO	主动抓取	0005	字段，顺序						0	2011-06-17 16:31:32	Y
//				6	0001AA1000000001HYWP	文件导入	0006	字段，顺序						0	2011-06-17 16:31:32	Y
//				7	0001AA1000000001HYWQ	中间表	0007	字段，顺序						0	2011-06-17 16:31:32	Y
//				8	0001AA1000000003M2VY	webservice抓取	0008	字段，顺序						0	2012-04-19 16:50:28	Y
				
				if(sourcetype !=null && sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
					esb=true;
					if(trancon.booleanValue()){
						if(backcon.booleanValue()){
							pks=new String[]{DataformatPanel.MESSTYPE_CSKS,DataformatPanel.MESSTYPE_CSSJ,DataformatPanel.MESSTYPE_CSJS,DataformatPanel.MESSTYPE_HZBZ};
							ipks=new String[]{DataformatPanel.MESSTYPE_CSKS_PK,DataformatPanel.MESSTYPE_CSSJ_PK,DataformatPanel.MESSTYPE_CSJS_PK,DataformatPanel.MESSTYPE_HZBZ_PK};
						}else{
							pks=new String[]{DataformatPanel.MESSTYPE_CSKS,DataformatPanel.MESSTYPE_CSSJ,DataformatPanel.MESSTYPE_CSJS};
							ipks=new String[]{DataformatPanel.MESSTYPE_CSKS_PK,DataformatPanel.MESSTYPE_CSSJ_PK,DataformatPanel.MESSTYPE_CSJS_PK};
						}
						if(backcon!=null&&backcon.booleanValue()){
							pks=new String[]{DataformatPanel.MESSTYPE_CSKS,DataformatPanel.MESSTYPE_CSSJ,DataformatPanel.MESSTYPE_CSJS,DataformatPanel.MESSTYPE_HZBZ};
							ipks=new String[]{DataformatPanel.MESSTYPE_CSKS_PK,DataformatPanel.MESSTYPE_CSSJ_PK,DataformatPanel.MESSTYPE_CSJS_PK,DataformatPanel.MESSTYPE_HZBZ_PK};
						}
						
					}else{
						if(backcon.booleanValue()){
							pks=new String[]{DataformatPanel.MESSTYPE_CSSJ,DataformatPanel.MESSTYPE_HZBZ};
							ipks=new String[]{DataformatPanel.MESSTYPE_CSSJ_PK,DataformatPanel.MESSTYPE_HZBZ_PK};
						}else{
							pks=new String[]{DataformatPanel.MESSTYPE_CSSJ};
							ipks=new String[]{DataformatPanel.MESSTYPE_CSSJ_PK};
						}
						if(backcon!=null&&backcon.booleanValue()){
							pks=new String[]{DataformatPanel.MESSTYPE_CSSJ,DataformatPanel.MESSTYPE_HZBZ};
							ipks=new String[]{DataformatPanel.MESSTYPE_CSSJ_PK,DataformatPanel.MESSTYPE_HZBZ_PK};
						}
					}
					lx=new String[]{"顺序"};
				}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)){
					pks=new String[]{DataformatPanel.MESSTYPE_GSWJ};
					ipks=new String[]{DataformatPanel.MESSTYPE_GSWJ_PK};
					lx=DataformatPanel.MESSTYPE_GSWJ_LX;
				}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_ZDZQ)){
					pks=new String[]{DataformatPanel.MESSTYPE_ZDZQ};
					ipks=new String[]{DataformatPanel.MESSTYPE_ZDZQ_PK};
					lx=DataformatPanel.MESSTYPE_ZDZQ_LX;
				}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_ZJB)){
					ipks=new String[]{DataformatPanel.MESSTYPE_ZJB_PK};
					pks=new String[]{DataformatPanel.MESSTYPE_ZJB};
					lx=DataformatPanel.MESSTYPE_ZJB_LX;
					iszjb=true;
				}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
					ipks=new String[]{DataformatPanel.MESSTYPE_WEBSERVICEZQ_PK};
					pks=new String[]{DataformatPanel.MESSTYPE_WEBSERVICEZQ};
					lx=DataformatPanel.MESSTYPE_WEBSERVICEZQ_LX;
				}
				int len=pks.length;
				for(int i=0;i<len;i++){
					dmap.put(i+"-l", null);
					dmap.put(i+"-r", null);
				}
				hvos=new DataformatdefHVO[pks.length];
				for(int i=0;i<pks.length;i++){
					hvos[i]=new DataformatdefHVO();
					hvos[i].setPk_datarec_h(hvo.getPrimaryKey());
					//hvos[i].setMessflowdef(0+"");
					
					if(vos!=null&&vos.length>0&&vos.length>i&&vos[i]!=null){
						hvos[i].setMessflowdef(vos[i].getMessflowdef()==null?0+"":vos[i].getMessflowdef());	//vos[i].getMessflowdef() 顺序0或者字段1
					}else{
						hvos[i].setMessflowdef(0+"");
					}
					
					hvos[i].setMessflowtype(ipks[i]);
					boolean iscontain=false;
					if(vos!=null&&vos.length>0){
						for(int k=0;k<vos.length;k++){
							if(vos[k].getMessflowtype().equals(ipks[i])){
								delayedMap.put(ipks[i], vos[k].getDelayed());
								DataformatdefBVO[] bvos=(DataformatdefBVO[]) HYPubBO_Client.queryByCondition(DataformatdefBVO.class, "pk_dataformatdef_h='"+vos[k].getPrimaryKey()+"' order by code");
								int index=0;
								for(int j=0;j<ipks.length;j++){
									if(ipks[j].equals(vos[k].getMessflowtype())){
										dmap.put(j+"-r", bvos);
										if(bvos!=null&&bvos.length>0){
										if(bvos[0].getCorreskind()!=null&&!bvos[0].getCorreskind().trim().equals("")){
											ziduandefbvo=(DataformatdefBVO[]) HYPubBO_Client.queryByCondition(DataformatdefBVO.class, "pk_dataformatdef_h='"+vos[k].getPrimaryKey()+"' order by code");
										}
										}
										index=j;
										hvos[index]=vos[k];
										flowtype=Integer.parseInt(hvos[index].getMessflowdef()==null?"0":hvos[index].getMessflowdef());
										mergecount=hvos[index].getMergecount()==null?"":hvos[index].getMergecount()+"";
										sendPageRunSys=hvos[index].getPagerunsys()==null?"":hvos[index].getPagerunsys()+"";
										mergeStyle=hvos[index].getMergestyle()==null?"0":hvos[index].getMergestyle();
										sendStyle=hvos[index].getSendstyle()==null?"0":hvos[index].getSendstyle();
										break;
									}
								}
								DipDatadefinitBVO[] dbvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+hvo.getMemorytable()+"' and pk_datadefinit_b not in (select vdef1 from dip_dataformatdef_b where pk_dataformatdef_h='"+vos[k].getPrimaryKey()+"' and vdef1 is not null)");
								if(dbvos!=null&&dbvos.length>0){
									DataformatdefBVO[] jlvos=new DataformatdefBVO[dbvos.length];
									for(int j=0;j<dbvos.length;j++){
										jlvos[j]=new DataformatdefBVO();
										jlvos[j].setDatakind(dbvos[j].getEname());
										jlvos[j].setVdef1(dbvos[j].getPrimaryKey());
										jlvos[j].setVdef2(DataformatPanel.YWSJ);
										jlvos[j].setVdef3(dbvos[j].getCname());
										jlvos[j].setDatakind(dbvos[j].getEname());
									}
									dmap.put(index+"-l", jlvos);
								}
							
								
								iscontain=true;
								break;
							}
						}
					}
					if(!iscontain){
						DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+hvo.getMemorytable()+"' ");
						if(bvos==null||bvos.length<=0){
							break;
						}else{
							DataformatdefBVO[] vofs=new DataformatdefBVO[bvos.length];
							for(int k=0;k<bvos.length;k++){
								vofs[k]=new DataformatdefBVO();
								vofs[k].setVdef1(bvos[k].getPk_datadefinit_b());
								vofs[k].setDatakind(bvos[k].getEname());
								vofs[k].setVdef3(bvos[k].getCname());
								vofs[k].setVdef2(DataformatPanel.YWSJ);
							}
							dmap.put(i+"-l", vofs);
						}
						if(isEsb()||sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)){
							String[] str=new String[]{DataformatPanel.KSBZ,DataformatPanel.XTBZ,DataformatPanel.ZDBZ,DataformatPanel.YWBZ,DataformatPanel.JSBZ};
							DataformatdefBVO[] gdbvos=new DataformatdefBVO[str.length];
							for(int k=0;k<str.length;k++){
								gdbvos[k]=new DataformatdefBVO();
								gdbvos[k].setCode(k+1);
								if(k==0){
									gdbvos[k].setVdef2(DataformatPanel.BZT);	
								}else if(k==4){
									gdbvos[k].setVdef2(DataformatPanel.BZW);
								}else{
									gdbvos[k].setVdef2(DataformatPanel.GDBZ);
								}
								if(k<4&&k>0){
									gdbvos[k].setDatakind(str[k]);
									gdbvos[k].setVdef3(str[k]);
								}
							}
							dmap.put(i+"-r", gdbvos);
						}
					}
				}
			} catch (UifException e1) {
				e1.printStackTrace();
			}
		}
		try {//初始化消息标志 
			MessagelogoVO[] logvos=(MessagelogoVO[]) HYPubBO_Client.queryByCondition(MessagelogoVO.class, "nvl(dr,0)=0 and ctype <>'分割标记' order by ctype");
			DipConsttabVO[] ctbvos=(DipConsttabVO[]) HYPubBO_Client.queryByCondition(DipConsttabVO.class, "nvl(dr,0)=0");
			if(logvos!=null&&logvos.length>0){
				List msvosl=new ArrayList<DataformatdefBVO>();
				for(int i=0;i<logvos.length;i++){
					DataformatdefBVO msvosi=new DataformatdefBVO();
					msvosi.setVdef2(logvos[i].getCtype());
					msvosi.setDatakind(logvos[i].getCdata());
					msvosi.setVdef3(logvos[i].getCname());
					msvosl.add(msvosi);
					getJpMessField().addLine();
				}
				if(ctbvos!=null&&ctbvos.length>0){
					for(int i=0;i<ctbvos.length;i++){
						DataformatdefBVO msvosi=new DataformatdefBVO();
						msvosi.setVdef2(DataformatPanel.ZDY);
						msvosi.setDatakind(ctbvos[i].getDescs());
						msvosi.setVdef3(ctbvos[i].getName());
						msvosi.setVdef1(ctbvos[i].getPrimaryKey());
						msvosl.add(msvosi);
						getJpMessField().addLine();
					}
				}
				if(isEsb()&&isinput()){
//					DataformatdefBVO msvosi=new DataformatdefBVO();
//					msvosi.setVdef2("固定标志");
//					msvosi.setDatakind("系统站点业务标志");
//					msvosi.setVdef3("系统站点业务标志");
//					msvosl.add(msvosi);
//					getJpMessField().addLine();
				}else{
					DataformatdefBVO msvosi=new DataformatdefBVO();
					msvosi.setVdef2(DataformatPanel.GDBZ);
					msvosi.setDatakind(DataformatPanel.XTBZ);
					msvosi.setVdef3(DataformatPanel.XTBZ);
					msvosl.add(msvosi);
					getJpMessField().addLine();
					DataformatdefBVO msvosi1=new DataformatdefBVO();
					msvosi1.setVdef2(DataformatPanel.GDBZ);
					msvosi1.setDatakind(DataformatPanel.ZDBZ);
					msvosi1.setVdef3(DataformatPanel.ZDBZ);
					msvosl.add(msvosi1);
					getJpMessField().addLine();
					DataformatdefBVO msvosi2=new DataformatdefBVO();
					msvosi2.setVdef2(DataformatPanel.GDBZ);
					msvosi2.setDatakind(DataformatPanel.YWBZ);
					msvosi2.setVdef3(DataformatPanel.YWBZ);
					msvosl.add(msvosi2);
					getJpMessField().addLine();
				}
				DataformatdefBVO[] msvos=(DataformatdefBVO[]) msvosl.toArray(new DataformatdefBVO[msvosl.size()]);

				getJpMessField().getBillModel().setBodyDataVO(msvos);
				getJpMessField().getBillModel().setEnabled(false);
				
				dmap.put("ms", msvos);
			}else{
				dmap.put("ms", null);
			}
		} catch (UifException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//JTextField ioflag=null;
		try {//初始化界面布局和界面参照
			this.setPreferredSize(new java.awt.Dimension(700, 388+28));
			this.setLayout(null);
			this.add(getLeftTabbedPane());
//				this.add(getJP1());
//				this.add(getJpFormat());
				this.add(getRightTabbedPane());
				this.add(getJPButton());
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("数据流类型");
				jLabel1.setBounds(25, 17, 60, 15);
				jLabel2 = new JLabel();
				//ioflag=new JTextField(hvo.getIoflag()==null?"  输入":"  "+hvo.getIoflag());
//				this.add(ioflag);
//				ioflag.setBounds(245, 15, 40, 20);
				//ioflag.setEditable(false);
				this.add(jLabel2);
				jLabel2.setText("对照方式");
				jLabel2.setBounds(295-12, 17, 60, 15);
				JLabel jLabel3=new JLabel();
				this.add(jLabel3);
				jLabel3.setText("格式复制");
				jLabel3.setBounds(530,17,60,15);
				
				this.add(getOrgnizeRefPnl());
				getOrgnizeRefPnl().setBounds(580,13, 142, 27);
			}
			{
				String strpks="";
				if(pks!=null&&pks.length>0){
					for(int i=0;i<pks.length;i++){
						strpks=strpks+"'"+pks[i]+"',";
					}
					strpks=strpks.substring(0,strpks.length()-1);
				}
				DipDatadefinitHVO  vos=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, hvo.getMemorytable());
				ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(pks);
				jComboBox1 = new JComboBox();
				if(pks!=null&&pks.length>1){
					jComboBox1.addActionListener(this);
					jComboBox1.addMouseListener(this);
					
				}
				this.add(jComboBox1);
				
				
				jComboBox1.setModel(jComboBox1Model);
				jComboBox1.setBounds(90, 13, 142, 22);
				
				filetype=new JComboBox();
				ComboBoxModel jftModel=null;
				if(pks!=null&&pks.length>0&&pks[0].equals(DataformatPanel.MESSTYPE_GSWJ)){
					jftModel = new DefaultComboBoxModel(new String[]{filetype_txt,filetype_xml,filetype_excel});
					this.add(filetype);
					filetype.setModel(jftModel);
					if(hvos[0].getFiletype()!=null){
						filetype.setSelectedIndex(hvos[0].getFiletype());
					}
					filetype.setBounds(90,42,142,22);
					filetype.addActionListener(this);
					
					JLabel jlf=new JLabel("文件类型");
					this.add(jlf);
					jlf.setBounds(25, 45, 60, 15);
					

					JLabel jlfg=new JLabel("分割标志");
					this.add(jlfg);
					jlfg.setBounds(295-12, 45, 90, 15);
					UIRefPane refp=getFgbj1Pnl();
					refp.getRefModel().addWherePart(" and ctype='分割标记'");
					this.add(refp);
					getFgbj1Pnl().setBounds(345,42, 142, 27);
					
					JLabel jlfg2=new JLabel("二级节点");
//					this.add(jlfg2);
//					jlfg2.setBounds(530, 45, 60, 15);
					
//					this.add(getFgbj2Pnl()); //(90,42,142,22);
//					getFgbj2Pnl().setBounds(580,42, 142, 27);
//					getFgbj2Pnl().setEnabled(false);
					
					if(hvos!=null&&hvos.length>0){
						DataformatdefHVO hvo=hvos[0];
						if(hvo.getFiletype()!=null){
							if(hvo.getFiletype()==0){
								getFgbj1Pnl().setPK(hvo.getBeginsign());
							}else if(hvo.getFiletype()==1){
//								getFgbj1Pnl().setPK(hvo.getBeginsign());
//								getFgbj2Pnl().setEnabled(true);
//								getFgbj2Pnl().setPK(hvo.getEndsign());
							}else if(hvo.getFiletype()==2){
								getFgbj1Pnl().setEnabled(false);
								getFgbj2Pnl().setEnabled(false);
							}
						}
						
					}
				}else if(pks!=null&&pks.length>0&&(pks[0].equals(DataformatPanel.MESSTYPE_CSKS)||pks[0].equals(DataformatPanel.MESSTYPE_CSSJ)||pks[0].equals(DataformatPanel.MESSTYPE_CSJS))){
					JLabel jlfg=new JLabel("分割标志");
					this.add(jlfg);
					jlfg.setBounds(25, 45, 60, 15);
					UIRefPane refp=getFgbj1Pnl();
					refp.getRefModel().addWherePart(" and ctype='分割标记'");
					this.add(refp);
					getFgbj1Pnl().setBounds(90,42,142,22);
					if(hvos!=null&&hvos.length>0){
						DataformatdefHVO hvo=hvos[0];
								getFgbj1Pnl().setPK(hvo.getBeginsign());
								getFgbj2Pnl().setEnabled(true);
								getFgbj2Pnl().setPK(hvo.getEndsign());
						
					}
					
					
//					JComboBox combox=new JComboBox();
//					String str[]={"接口平台格式","自定义格式"};
//					ComboBoxModel jftttModel=null;
//					jftttModel = new DefaultComboBoxModel(str);
//					combox.setModel(jftttModel);
////					combox.addItem(str);
//					JLabel jltfg=new JLabel(DataformatPanel.XTBZ);
//					this.add(combox);
//					this.add(jltfg);
//					jltfg.setBounds(295, 45, 90, 15);
//					combox.setBounds(345,42, 142, 22);
				}
				
				String str[]={"接口平台格式","自定义格式"};
				if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
					 jCombox3=new JComboBox();
					ComboBoxModel jftttModel=null;
					jftttModel = new DefaultComboBoxModel(str);
					jCombox3.setModel(jftttModel);
//					combox.addItem(str);
					JLabel jltfg=new JLabel(DataformatPanel.XTBZ);
					this.add(jCombox3);
					this.add(jltfg);
					jCombox3.addActionListener(this);
//					jCombox3.addMouseListener(this);
					jltfg.setBounds(295-12, 45, 90, 15);
					jCombox3.setBounds(345,42, 142, 22);
					addItem();
					mergeCountTextField.setText(mergecount);
					sendPageRunSysTextField.setText(sendPageRunSys);
					if(hvos!=null&&hvos.length>0){
						DataformatdefHVO hvo=hvos[0];
					    getMergemarkRefPanelPnl().setPK(hvo.getMergemark());
					}
					if(mergeStyle!=null&&mergeStyle.trim().length()>0){
						mergeStyleComboBox.setSelectedIndex(Integer.parseInt(mergeStyle));
					}
					if(sendStyle!=null&&sendStyle.trim().length()>0){
						sendStyleComboBox.setSelectedIndex(Integer.parseInt(sendStyle));
					}
//				}
//				else if(type.equals("中间表")||type.equals("主动抓取")||type.equals("webservice抓取")){
//					 jCombox3=new JComboBox();
//					ComboBoxModel jftttModel=null;
//					jftttModel = new DefaultComboBoxModel(str);
//					jCombox3.setModel(jftttModel);
////					combox.addItem(str);
//					JLabel jltfg=new JLabel(DataformatPanel.XTBZ);
//					this.add(jCombox3);
//					this.add(jltfg);
//					jCombox3.addActionListener(this);
//					jCombox3.addMouseListener(this);
//					jltfg.setBounds(25, 45, 60, 15);
//					jCombox3.setBounds(90,42,142,22);
				}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_GSWJ)){
					 jCombox3=new JComboBox();
					
					ComboBoxModel jftttModel=null;
					jftttModel = new DefaultComboBoxModel(str);
					jCombox3.setModel(jftttModel);
					JLabel jltfg=new JLabel(DataformatPanel.XTBZ);
					this.add(jCombox3);
					this.add(jltfg);
					jCombox3.addActionListener(this);
					jCombox3.addMouseListener(this);
					jltfg.setBounds(530, 45, 60, 15);
					jCombox3.setBounds(580,42, 142, 22);
					
				}
				
			}
			{
				ComboBoxModel jComboBox2Model = 
					new DefaultComboBoxModel(
							lx);
				jComboBox2 = new JComboBox();
				jComboBox2.addActionListener(this);
				jComboBox2.addMouseListener(this);
				jComboBox2.setModel(jComboBox2Model);
				jComboBox2.setBounds(345, 13, 142, 22);
//				if(isZjb()){
//					flowtype=1;
//					jComboBox2.setEnabled(false);
//				}
				jComboBox2.setSelectedIndex(flowtype);
				this.add(jComboBox2);
			}
			if(flowtype==1){
				rightJpFormat.getBillModel().getItemByKey("correskind").setEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Map <String,Integer> delayedMap=new HashMap<String, Integer>();
	public void displayDelayed(ActionEvent e){
			if(delayedTextField!=null&&delayedMap!=null){
				if(delayedMap.get(ipks[jComboBox1.getSelectedIndex()])!=null&&delayedMap.get(ipks[jComboBox1.getSelectedIndex()]).toString().trim().length()>0){
					delayedTextField.setText(delayedMap.get(ipks[jComboBox1.getSelectedIndex()]).toString());
				}else{
					delayedTextField.setText(null);
				}
			}
	}

	public void actionPerformed(ActionEvent e) {
//		 if(e.getSource()==(getOrgnizeRefPnl().getUIButton())){
////				getOrgnizeRefPnl().getRef().showModal();
//				if (getOrgnizeRefPnl().getReturnButtonCode() == UFRefManage.ID_OK) {
//					System.out.println("----------yes dedaole------------");
//				}else if(getOrgnizeRefPnl().getReturnButtonCode()==UFRefManage.ID_CANCEL){
//					return;
//			e.getSource().equals(jComboBox1)	}
//		}
		displayDelayed(e);
		if(e.getSource().equals(mergeStyleComboBox)){
			int mergeStyleComboBoxSelectIndex=mergeStyleComboBox.getSelectedIndex();
			if(mergeStyleComboBoxSelectIndex==0){
				getMergemarkRefPanelPnl().setEnabled(false);
				mergeCountTextField.setEnabled(false);
				sendPageRunSysTextField.setEnabled(false);
			}else{
				getMergemarkRefPanelPnl().setEnabled(true);
				mergeCountTextField.setEnabled(true);
				sendPageRunSysTextField.setEnabled(true);
			}
			return;
		}
		if(e.getSource().equals(delayedTextField)){
//			int delayedtime=0;
			String text=delayedTextField.getText();
			if(text!=null&&text.trim().length()>0){
				String regex="\\d{1,9}";
				if(!text.matches(regex)){
					delayedTextField.setText(null);
				}
			}
			
		}
		
		int index=jComboBox1.getSelectedIndex();
		int index2=0;
		if(jComboBox2!=null){
			index2=jComboBox2.getSelectedIndex();
		}
		int tabindex=getLeftTabbedPane().getSelectedIndex();
		int righttabindex=getRightTabbedPane().getSelectedIndex();
		if(righttabindex==0){
			
			if (e.getSource().equals(jButton1)){
				if(tabindex==0){
					int row=getJpField().getBillTable().getSelectedRow();
					if(row>=0){
						String key=index+"-"+"l";
						DataformatdefBVO[] vol=dmap.get(key);
						DataformatdefBVO vo=vol[row];
						if(vol.length==1){
							dmap.put(key, null);
						}else{
							DataformatdefBVO[] vol1=new DataformatdefBVO[vol.length-1];
							for(int i=0;i<vol.length-1;i++){
								if(i<row){
									vol1[i]=vol[i];
								}else{
									vol1[i]=vol[i+1];
								}
							}
							dmap.put(key, vol1);
						}
						key=index+"-"+"r";
						DataformatdefBVO[] vor=dmap.get(key);
						if(vor==null){
							vor=new DataformatdefBVO[1];
							vo.setCode(1);
							vor[0]=vo;
							dmap.put(key, vor);
						}else{
							DataformatdefBVO[] vor1=new DataformatdefBVO[vor.length+1];
							if((isEsb()&&isinput())||(jCombox3!=null&&jCombox3.getSelectedIndex()==0)){
//								if(isEsb()&&isinput()){
								DataformatdefBVO lvo=vor[vor.length-1];//结尾的那个vo
								for(int i=0;i<vor.length-1;i++){
									vor1[i]=vor[i];
								}
								vo.setCode(vor.length);
								vor1[vor.length-1]=vo;
								lvo.setCode(vor.length+1);
								vor1[vor.length]=lvo;
							}else{
								for(int i=0;i<vor.length;i++){
									vor1[i]=vor[i];
								}
								vo.setCode(vor.length+1);
								vor1[vor.length]=vo;
							}
							dmap.put(key, vor1);
						}
						initjp(index);
					}
				}else if(tabindex==1){
					int row=getJpMessField().getBillTable().getSelectedRow();
					if(row>=0){
						String key="ms";
						if(dmap.get(key)==null){
							return;
						}
						DataformatdefBVO[] vol=dmap.get(key);
						DataformatdefBVO vo=(DataformatdefBVO) vol[row].clone();
						key=index+"-"+"r";
						DataformatdefBVO[] vor=dmap.get(key);
//						if((isEsb()&&!isinput())||!isEsb()||jCombox3.getSelectedIndex()==1){
						if(jCombox3.getSelectedIndex()==1){
							if(vor==null||vor.length<=0){
								vor=new DataformatdefBVO[1];
								vo.setCode(1);
								vor[0]=vo;
								dmap.put(key, vor);
							}else{
								DataformatdefBVO[] vor1=new DataformatdefBVO[vor.length+1];
								for(int i=0;i<vor.length;i++){
									vor1[i]=vor[i];
								}
								vo.setCode(vor.length+1);
								vor1[vor.length]=vo;
								dmap.put(key, vor1);
							}
						}else{
							if(vo.getVdef2().equals(DataformatPanel.GDBZ)){
							}else if(vo.getVdef2().equals(DataformatPanel.BZT)){
								vo.setCode(1);
								vor[0]=vo;
								dmap.put(key, vor);
							}else if(vo.getVdef2().equals(DataformatPanel.BZW)){
								vo.setCode(vor.length);
								vor[vor.length-1]=vo;
								dmap.put(key, vor);
							}else{
								boolean isxt=false;
								if(vo.getVdef2()!=null&&vo.getVdef2().equals(DataformatPanel.GDBZ)){
//									isxt=true;
								}
								String[] str=new String[]{DataformatPanel.XTBZ,DataformatPanel.ZDBZ,DataformatPanel.YWBZ};
								if(vor==null||vor.length<=0){
									if(isxt){
										vor=new DataformatdefBVO[3];
										for(int i=0;i<3;i++){
											vor[i]=new DataformatdefBVO();
											vor[i].setCode(i+1);
											vor[i].setVdef2(DataformatPanel.GDBZ);
											vor[i].setVdef3(str[i]);
											vor[i].setDatakind(str[i]);
											dmap.put(key, vor);
										}
									}else{
										vor=new DataformatdefBVO[1];
										vo.setCode(1);
										vor[0]=vo;
										dmap.put(key, vor);
									}
								}else{
									if(isxt){
										DataformatdefBVO[] vor1=new DataformatdefBVO[vor.length+3];
										for(int i=0;i<vor1.length;i++){
											if(i==0){
												vor1[i]=vor[i];
											}else if(0<i&&i<4){
												vor1[i]=new DataformatdefBVO();
												vor1[i].setCode(i+1);
												vor1[i].setVdef2(DataformatPanel.GDBZ);
												vor1[i].setVdef3(str[i-1]);
												vor1[i].setDatakind(str[i-1]);
											}else{
												vor1[i]=vor[i-3];
												vor1[i].setCode(i+1);
											}
										}
										dmap.put(key, vor1);
									}else{
										DataformatdefBVO[] vor1=new DataformatdefBVO[vor.length+1];
										if(isEsb()&&isinput()){
											for(int i=0;i<vor.length-1;i++){
												vor1[i]=vor[i];
											}
											vo.setCode(vor.length);
											vor1[vor.length-1]=vo;
											vor[vor.length-1].setCode(vor.length+1);
											vor1[vor.length]=vor[vor.length-1];
										}else{
										for(int i=0;i<vor.length;i++){
											vor1[i]=vor[i];
										}
										vo.setCode(vor.length+1);
										vor1[vor.length]=vo;
										}
										dmap.put(key, vor1);
									}
								}
							}
						}
						initjp(index);
					}
				}
			}else if(e.getSource().equals(jButton2)){
				if(tabindex==0){
					String keyl=index+"-"+"l";
					if(dmap.get(keyl)!=null){
						DataformatdefBVO[] vol=dmap.get(keyl);
						dmap.put(keyl, null);
						String key=index+"-"+"r";
						if(dmap.get(key)==null){
							for(int i=0;i<vol.length;i++){
								vol[i].setCode(i+1);
							}
							dmap.put(key, vol);
						}else{
							DataformatdefBVO[] vor=dmap.get(key);
							DataformatdefBVO[] vor1=new DataformatdefBVO[vor.length+vol.length];
							if((isEsb()&&isinput())||(jCombox3!=null&&jCombox3.getSelectedIndex()==0)){
								for(int i=0;i<vor.length-1;i++){
									vor1[i]=vor[i];
								}
								vor[vor.length-1].setCode(vor1.length);
								vor1[vor1.length-1]=vor[vor.length-1];
								int len=vol.length;
								int j=0;
								for(int i=vor.length-1;i<vor1.length-1;i++){
									vor1[i]=vol[j];
									vor1[i].setCode(i+1);
									j++;
								}
							}else{
								for(int i=0;i<vor.length;i++){
									vor1[i]=vor[i];
									vor1[i].setCode(i+1);
								}
								int len=vor.length;
								for(int i=vor.length;i<vor1.length;i++){
									vor1[i]=vol[i-len];
									vor1[i].setCode(i+1);
								}
							}
							
							dmap.put(key, vor1);
						}
						initjp(index);
					}
				}
			}else if(e.getSource().equals(jButton3)){
				
					int row=getRightJpFormat().getBillTable().getSelectedRow();
					if(row>=0){
						String key=index+"-"+"r";
						DataformatdefBVO[] vor=dmap.get(key);
						if(isEsb()&&isinput()||(jCombox3!=null&&jCombox3.getSelectedIndex()==0)){
							if(row==0||row==1||row==2||row==3||row==vor.length-1){
								return;
							}
						}
						if(vor.length==1){
							dmap.put(key, null);
						}else{
							DataformatdefBVO[] vor1=new DataformatdefBVO[vor.length-1];
							for(int i=0;i<vor1.length;i++){
								if(i<row){
									vor1[i]=vor[i];
								}else{
									vor1[i]=vor[i+1];
								}
								vor1[i].setCode(i+1);
							}
							dmap.put(key, vor1);
						}
						DataformatdefBVO vo=vor[row];
						if(vo.getVdef2()!=null&&vo.getVdef2().equals(DataformatPanel.YWSJ)){
						vo.setCode(null);
						key=index+"-"+"l";
						DataformatdefBVO[] vol=dmap.get(key);
						if(vol==null){
							dmap.put(key,new DataformatdefBVO[]{vo});
						}else{
							DataformatdefBVO[] vol1=new DataformatdefBVO[vol.length+1];
							for(int i=0;i<vol.length;i++){
								vol1[i]=vol[i];
							}
							vol1[vol1.length-1]=vo;
							dmap.put(key, vol1);
						}
						}
						initjp(index);
					}
			}else if(e.getSource().equals(jButton4)){
				String key=index+"-"+"r";
				String key2=index+"-"+"l";
				if(dmap.get(key)!=null){
					DataformatdefBVO[] vor=dmap.get(key);
					DataformatdefBVO[] vol=dmap.get(key2);
	//				int start;
	//				DataformatdefBVO[] vol1=null;
					List<DataformatdefBVO> l=((vol==null||vol.length<=0)?new ArrayList<DataformatdefBVO>():new ArrayList<DataformatdefBVO>(Arrays.asList(vol)));
					if(vor!=null&&vor.length>0){
						for(int i=0;i<vor.length;i++){
							DataformatdefBVO bvo=vor[i];
							if(bvo.getVdef2()!=null&&bvo.getVdef2().equals(DataformatPanel.YWSJ)){
								bvo.setCode(null);
								l.add(bvo);
							}
						}
					}
					dmap.put(key, null);
					dmap.put(key2, l==null?null:(DataformatdefBVO[])l.toArray(new DataformatdefBVO[l.size()]));
					if((isEsb()&&isinput())||(jCombox3!=null&&jCombox3.getSelectedIndex()==0)){
						String[] str=new String[]{DataformatPanel.KSBZ,DataformatPanel.XTBZ,DataformatPanel.ZDBZ,DataformatPanel.YWBZ,DataformatPanel.JSBZ};
						DataformatdefBVO[] gdbvos=new DataformatdefBVO[str.length];
						for(int k=0;k<str.length;k++){
							gdbvos[k]=new DataformatdefBVO();
							gdbvos[k].setCode(k+1);
							gdbvos[k].setVdef2(DataformatPanel.GDBZ);
							if(k<4&&k>0){
								gdbvos[k].setDatakind(str[k]);
								gdbvos[k].setVdef3(str[k]);
							}
						}
						dmap.put(key, gdbvos);
					}
					initjp(index);
				}
			}else if(e.getSource().equals(jComboBox1)){
				initjp(index);
//				liyunzhe 在切换数据流类型时候，初始化复制参照 start 
			 	DataFormateCopyTreeRefModel model = new DataFormateCopyTreeRefModel();
				refpnlOrgize.setRefModel(model);
				model.setWherePart( model.getWherePart()==null?"":model.getWherePart()+" and dip_datarec_h.sourcetype ='"+hvo.getSourcetype()+"'");
			   getOrgnizeRefPnl().setRefModel(model);
//			 	liyunzhe 在切换数据流类型时候，初始化复制参照 end 
			}else if(e.getSource().equals(jComboBox2)){
				initjp(index);
				if(index2==1){
					getRightJpFormat().getBillModel().getItemByKey("correskind").setEnabled(true);
				}else{
					getRightJpFormat().getBillModel().getItemByKey("correskind").setEnabled(false);
					Iterator it=dmap.keySet().iterator();
					while(it.hasNext()){
						String key=(String) it.next();
						//if(key!=null&&!key.equals("ziduanmap")){
							DataformatdefBVO[] voi=dmap.get(key);
							if(voi!=null&&voi.length>0){
								for(int i=0;i<voi.length;i++){
									voi[i].setCorreskind(null);
								}
							}
							dmap.put(key, voi);
						//}
						
					}
				}
				for(int i=0;i<pks.length;i++){
					hvos[i].setMessflowdef(index2+"");
				}
				initjp(index);
			}else if(e.getSource().equals(upBut)){
				int row=getRightJpFormat().getBillTable().getSelectedRow();
				int brow=((isEsb()&&isinput())||(jCombox3!=null&&jCombox3.getSelectedIndex()==0))?5:1;
				DataformatdefBVO[] db=dmap.get(index+"-r");
				int erow=(db==null||db.length<=0)?0:(((isEsb()&&isinput())||(jCombox3!=null&&jCombox3.getSelectedIndex()==0))?(db.length-2):db.length-1);
				if(row>=brow&&row<=erow){
					DataformatdefBVO vo=(DataformatdefBVO) db[row].clone();
					vo.setCode(row);
					db[row]=db[row-1];
					db[row].setCode(row+1);
					db[row-1]=vo;
					dmap.put(index+"-r", db);
					initjp(index);
					this.getRightJpFormat().getBillTable().changeSelection(row-1,0,false,false);
				}
			}else if(e.getSource().equals(downBut)){
				int row=getRightJpFormat().getBillTable().getSelectedRow();
				int brow=(isEsb()&&isinput())?4:0;
				DataformatdefBVO[] db=dmap.get(index+"-r");
				int erow=(db==null||db.length<=0)?0:(((isEsb()&&isinput())||(jCombox3!=null&&jCombox3.getSelectedIndex()==0))?(db.length-3):db.length-2);
				
				if(row>=brow&&row<=erow){
					DataformatdefBVO vo=(DataformatdefBVO) db[row+1].clone();
					vo.setCode(row+1);
					db[row+1]=db[row];
					db[row].setCode(row+2);
					db[row]=vo;
					dmap.put(index+"-r", db);
					initjp(index);
					this.getRightJpFormat().getBillTable().changeSelection(row+1,0,false,false);
				}
	
			}else if(e.getSource().equals(jCombox3)){
				if(jCombox3.getSelectedIndex()==0){
					int w=getRightJpFormat().getRowCount();
					DataformatdefBVO bvos[]=null;
					
					if(w>0){
					 DataformatdefBVO ss[]=null;
					 ss=(DataformatdefBVO[]) getRightJpFormat().getBillModel().getBodyValueVOs(new DataformatdefBVO().getClass().getName());
					 
					 if(ss!=null&&ss.length>0){
						 
						 if(ss.length>=5){
								if(DataformatPanel.BZT.equals(ss[0].getVdef2())&&DataformatPanel.XTBZ.equals(ss[1].getDatakind())&&DataformatPanel.ZDBZ.equals(ss[2].getDatakind())&&
										DataformatPanel.YWBZ.equals(ss[3].getDatakind())&&DataformatPanel.BZW.equals(ss[ss.length-1].getVdef2())){
									return;
								}
						 }
						 
						 
						 List<DataformatdefBVO> list=new ArrayList<DataformatdefBVO>();
						 String[] str=new String[]{DataformatPanel.KSBZ,DataformatPanel.XTBZ,DataformatPanel.ZDBZ,DataformatPanel.YWBZ,DataformatPanel.JSBZ};
							DataformatdefBVO[] gdbvos=new DataformatdefBVO[str.length];
							for(int k=0;k<str.length;k++){
								gdbvos[k]=new DataformatdefBVO();
								gdbvos[k].setCode(k+1);
								if(k==0){
									gdbvos[k].setVdef2(DataformatPanel.BZT);	
								}else if(k==4){
									gdbvos[k].setVdef2(DataformatPanel.BZW);
								}else{
									gdbvos[k].setVdef2(DataformatPanel.GDBZ);
								}
								if(k<4&&k>0){
									gdbvos[k].setDatakind(str[k]);
									gdbvos[k].setVdef3(str[k]);
								}
							}
							
						 for(int i=0;i<ss.length;i++){
							 if(i==0){
								 list.add(gdbvos[0]);
								 list.add(gdbvos[1]);
								 list.add(gdbvos[2]);
								 list.add(gdbvos[3]);
							 }
							 ss[i].setCode(5+i);
							 list.add(ss[i]);
							 if(i==ss.length-1){
								 gdbvos[4].setCode(1+list.size());
								 list.add(gdbvos[4]);
							 }
							 
						 }
						 getRightJpFormat().getBillModel().setBodyDataVO(list.toArray(new DataformatdefBVO[0]));
						 dmap.put(jComboBox1.getSelectedIndex()+"-r", list.toArray(new DataformatdefBVO[0]));
					 }
					}else{
						 String[] str=new String[]{DataformatPanel.KSBZ,DataformatPanel.XTBZ,DataformatPanel.ZDBZ,DataformatPanel.YWBZ,DataformatPanel.JSBZ};
							DataformatdefBVO[] gdbvos=new DataformatdefBVO[str.length];
							for(int k=0;k<str.length;k++){
								gdbvos[k]=new DataformatdefBVO();
								gdbvos[k].setCode(k+1);
								if(k==0){
									gdbvos[k].setVdef2(DataformatPanel.BZT);	
								}else if(k==4){
									gdbvos[k].setVdef2(DataformatPanel.BZW);
								}else{
									gdbvos[k].setVdef2(DataformatPanel.GDBZ);
								}
								
								
								if(k<4&&k>0){
									gdbvos[k].setDatakind(str[k]);
									gdbvos[k].setVdef3(str[k]);
								}
							}
							getRightJpFormat().getBillModel().setBodyDataVO(gdbvos);
							 dmap.put(jComboBox1.getSelectedIndex()+"-r", gdbvos);
					}
				}else{
					 DataformatdefBVO ss[]=null;
					 ss=(DataformatdefBVO[]) getRightJpFormat().getBillModel().getBodyValueVOs(new DataformatdefBVO().getClass().getName());
					List<DataformatdefBVO> list=new ArrayList<DataformatdefBVO>();
					 if(ss!=null&&ss.length>=5){
						if(DataformatPanel.BZT.equals(ss[0].getVdef2())&&DataformatPanel.XTBZ.equals(ss[1].getDatakind())&&DataformatPanel.ZDBZ.equals(ss[2].getDatakind())&&
								DataformatPanel.YWBZ.equals(ss[3].getDatakind())&&DataformatPanel.BZW.equals(ss[ss.length-1].getVdef2())){
							int count=1;
							for(int i=4;i<ss.length;i++){
								   if(i!=ss.length-1){
									   ss[i].setCode(count);
									   list.add(ss[i]);
									   count++;
								   }
								}	
							
							if(list.size()>0){
								 getRightJpFormat().getBillModel().setBodyDataVO(list.toArray(new DataformatdefBVO[0]));
								 dmap.put(jComboBox1.getSelectedIndex()+"-r", list.toArray(new DataformatdefBVO[0]));
							 }else{
								 getRightJpFormat().getBillModel().setBodyDataVO(null);
								 dmap.put(jComboBox1.getSelectedIndex()+"-r", null);
							 }
						}
						
					}
					 
					 
				}
				
				
			}
	}
		if(e.getSource().equals(filetype)){
			int fileindex=filetype.getSelectedIndex();
			if(fileindex==0){
				getFgbj1Pnl().setEnabled(true);
				getFgbj2Pnl().setEnabled(false);
				getFgbj2Pnl().setPK(null);
				int k=getRightTabbedPane().getTabCount();
				if(k==3){
					getRightTabbedPane().remove(2);
				}
			}else if(fileindex==1){
				getFgbj1Pnl().setValue(null);
				getFgbj1Pnl().setEnabled(false);
//				getFgbj2Pnl().setEnabled(true);
				int k=getRightTabbedPane().getTabCount();
				if(k!=3){
					getRightTabbedPane().addTab("xml格式", getRightXmlFormat());
				}
				 DipDatarecSpecialTab specialXml[]=specialmap.get(SPECIAL_XML);
				   if(specialXml==null||specialXml.length==0){
					   DipDatarecSpecialTab specialXml2[]=new DipDatarecSpecialTab[3];
					   specialXml2[0]=new DipDatarecSpecialTab();
					   specialXml2[0].setNodename(RIGHT_XMLGS_ROOT);
					   specialXml2[0].setNodenumber(1);
					   specialXml2[0].setIs_xml(new UFBoolean(true));
					   specialXml2[0].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialXml2[1]=new DipDatarecSpecialTab();
					   specialXml2[1].setNodename(RIGHT_XMLGS_ONT);
					   specialXml2[1].setNodenumber(2);
					   specialXml2[1].setIs_xml(new UFBoolean(true));
					   specialXml2[1].setPk_datarec_h(hvo.getPk_datarec_h());
					   specialXml2[2]=new DipDatarecSpecialTab();
					   specialXml2[2].setNodenumber(3);
					   specialXml2[2].setNodename(RIGHT_XMLGS_TWO);
					   specialXml2[2].setIs_xml(new UFBoolean(true));
					   specialXml2[2].setPk_datarec_h(hvo.getPk_datarec_h());
					   getRightXmlFormat().getBillModel().setBodyDataVO(specialXml2);
					   specialmap.put(SPECIAL_XML, specialXml2);
				   }else{
					   getRightXmlFormat().getBillModel().setBodyDataVO(specialXml);
				   }
			}else if(fileindex==2){
				getFgbj1Pnl().setEnabled(false);
				getFgbj2Pnl().setEnabled(false);
				getFgbj1Pnl().setPK(null);
				getFgbj2Pnl().setPK(null);
				int k=getRightTabbedPane().getTabCount();
				if(k==3){
					getRightTabbedPane().remove(2);
				}
			}
			
			
		}
		
	
	}
	/**
	 * 增加合并数，和数据分隔符
	 *
	 */
	public void addItem(){
		
		{
			 //合并类型
			 JLabel mergeStyleLabel=new JLabel();
			 this.add(mergeStyleLabel);
			 mergeStyleLabel.setText("合并类型");
			 mergeStyleLabel.setBounds(530, 45, 60, 15);
			 mergeStyleComboBox=new JComboBox();
			 String strMergeStyleArray[]={"不合并","记录合并","完整合并"};
			 ComboBoxModel mergeStyleModel = 
					new DefaultComboBoxModel(strMergeStyleArray);
			 mergeStyleComboBox.setModel(mergeStyleModel);
			 mergeStyleComboBox.addActionListener(this);
			 this.add(mergeStyleComboBox);
			 mergeStyleComboBox.setBounds(580, 42, 142, 22);
			}
		
		
		
		
		{
		JLabel mergeMarkLabel=new JLabel();
		this.add(mergeMarkLabel);
		mergeMarkLabel.setText("合并分隔符");
		mergeMarkLabel.setBounds(25, 74, 90, 15);
		UIRefPane refp=getMergemarkRefPanelPnl();
		refp.getRefModel().addWherePart(" and ctype='分割标记'");
		this.add(refp);
		getMergemarkRefPanelPnl().setBounds(90, 70, 142, 22);
		}
		
		{
			JLabel mergeCountLabel=new JLabel();
			this.add(mergeCountLabel);
			mergeCountLabel.setText("记录合并数");
			mergeCountLabel.setBounds(295-12, 75, 90, 15);
			mergeCountTextField=new JTextField();
			mergeCountTextField.setPreferredSize(new Dimension(150,22));
			this.add(mergeCountTextField);
			mergeCountTextField.setBounds(345,72, 142, 22);
		}
		
		{
			
			 //分页参数
			 JLabel sendPageRunSysLabel=new JLabel();
			 this.add(sendPageRunSysLabel);
			 sendPageRunSysLabel.setText("分页参数");
			 sendPageRunSysLabel.setBounds(530, 74, 60, 15);
			 //295-12, 75, 90, 15
			 sendPageRunSysTextField=new JTextField();
			 this.add(sendPageRunSysTextField);
			 sendPageRunSysTextField.setBounds(580, 70, 142, 22);
			
//			
//		 //延时时间
//			JLabel delayedLabel=new JLabel();
//			this.add(delayedLabel);
//			delayedLabel.setText("延时(秒)");
//			delayedLabel.setBounds(530, 74, 60, 15);
//			delayedTextField=new UIRefPane();
//			delayedTextField.setButtonVisible(false);
//			delayedTextField.setPreferredSize(new Dimension(150,22));
//			delayedTextField.addValueChangedListener(headValueChangeListener);
//			this.add(delayedTextField);
//			delayedTextField.setBounds(580, 70, 142, 22);
		}
		
		
		{
			 //发送类型
			 JLabel sendStyleLabel=new JLabel();
			 this.add(sendStyleLabel);
			 sendStyleLabel.setText("发送类型");
			 sendStyleLabel.setBounds(25, 102, 90, 15);
			 sendStyleComboBox=new JComboBox();
			 String strSendStyleArray[]={"空数据不发送","空数据发送"};
			 ComboBoxModel mergeStyleModel = 
					new DefaultComboBoxModel(strSendStyleArray);
			 sendStyleComboBox.setModel(mergeStyleModel);
			 sendStyleComboBox.addActionListener(this);
			 this.add(sendStyleComboBox);
			 sendStyleComboBox.setBounds(90, 98, 142, 22);
		}
		
		{
			
			 //延时时间
				JLabel delayedLabel=new JLabel();
				this.add(delayedLabel);
				delayedLabel.setText("延时(秒)");
				delayedLabel.setBounds(295-12, 102, 90, 15);
				delayedTextField=new UIRefPane();
				delayedTextField.setButtonVisible(false);
				delayedTextField.setPreferredSize(new Dimension(150,22));
				delayedTextField.addValueChangedListener(headValueChangeListener);
				this.add(delayedTextField);
				delayedTextField.setBounds(345, 98, 142, 22);
//			 //分页参数
//			 JLabel sendPageRunSysLabel=new JLabel();
//			 this.add(sendPageRunSysLabel);
//			 sendPageRunSysLabel.setText("分页参数");
//			 sendPageRunSysLabel.setBounds(295-12, 102, 90, 15);
//			 //295-12, 75, 90, 15
//			 sendPageRunSysTextField=new JTextField();
//			 this.add(sendPageRunSysTextField);
//			 sendPageRunSysTextField.setBounds(345, 98, 142, 22);
//			 //345,72, 142, 22
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicke33333");	
//		if(e.getSource().equals(getRightTabbedPane())){
//			if(getRightTabbedPane().getSelectedIndex()==0){
//				jCombox3.setEnabled(true);
//			}else{
//				
//				jCombox3.setEnabled(false);
//			}
//			
//		}
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

	public UFBoolean getBackcon() {
		return backcon;
	}
	public boolean isTarget(BillItem item) {
		return false;
	}
	public JComboBox getJComboBox1() {
		return jComboBox1;
	}
	public DipDatarecHVO getHvo() {
		return hvo;
	}
	public void setHvo(DipDatarecHVO hvo) {
		this.hvo = hvo;
	}
	public String[] getIpks() {
		return ipks;
	}
	public void setIpks(String[] ipks) {
		this.ipks = ipks;
	}
	public void onBoDelete() {
		int index=jComboBox1.getSelectedIndex();
		for(int j=0;j<pks.length;j++){
//			int index2=jComboBox2.getSelectedIndex();
			String key=j+"-"+"r";
			String key2=j+"-"+"l";
			if(dmap.get(key)!=null){
				DataformatdefBVO[] vor=dmap.get(key);
				DataformatdefBVO[] vol=dmap.get(key2);
	//			int start;
	//			DataformatdefBVO[] vol1=null;
				List<DataformatdefBVO> l=((vol==null||vol.length<=0)?new ArrayList<DataformatdefBVO>():new ArrayList<DataformatdefBVO>(Arrays.asList(vol)));
				if(vor!=null&&vor.length>0){
					for(int i=0;i<vor.length;i++){
						DataformatdefBVO bvo=vor[i];
						if(bvo.getVdef2()!=null&&bvo.getVdef2().equals(DataformatPanel.YWSJ)){
							bvo.setCode(null);
							l.add(bvo);
						}
					}
				}
				dmap.put(key, null);
				dmap.put(key2, l==null?null:(DataformatdefBVO[])l.toArray(new DataformatdefBVO[l.size()]));
				if((isEsb()&&isinput())||jCombox3.getSelectedIndex()==0){
					String[] str=new String[]{DataformatPanel.KSBZ,DataformatPanel.XTBZ,DataformatPanel.ZDBZ,DataformatPanel.YWBZ,DataformatPanel.JSBZ};
					DataformatdefBVO[] gdbvos=new DataformatdefBVO[str.length];
					for(int k=0;k<str.length;k++){
						gdbvos[k]=new DataformatdefBVO();
						gdbvos[k].setCode(k+1);
						if(k==0){
							gdbvos[k].setVdef2(DataformatPanel.BZT);	
						}else if(k==4){
							gdbvos[k].setVdef2(DataformatPanel.BZW);
						}else{
							gdbvos[k].setVdef2(DataformatPanel.GDBZ);
						}
						if(k<4&&k>0){
							gdbvos[k].setDatakind(str[k]);
							gdbvos[k].setVdef3(str[k]);
						}
					}
					dmap.put(key, gdbvos);
				}
				
			}
		}
		if(specialmap!=null){
			for(int w=0;w<3;w++){
				DipDatarecSpecialTab specialtabvo[]=null;
				String type="";
				if(w==0){
					type=SPECIAL_XT;
					specialtabvo=specialmap.get(SPECIAL_XT);
				}else if(w==1){
					type=SPECIAL_XML;
					specialtabvo=specialmap.get(SPECIAL_XML);
				}else {
					type=SPECIAL_BACK;
					specialtabvo=specialmap.get(SPECIAL_BACK);
				}
				
				if(specialtabvo!=null&&specialtabvo.length>0){
					for(int i=0;i<specialtabvo.length;i++){
						specialtabvo[i].setValue(null);
						if(type.equals(SPECIAL_XML)){
							specialtabvo[i].setNodeproperty(null);
						}
					}
					specialmap.put(type, specialtabvo);
				}
			}
			
		}
		
		if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
			mergeStyleComboBox.setSelectedIndex(0);
			mergemarkRefPanel.setText(null);
			mergeCountTextField.setText(null);
			sendPageRunSysTextField.setText(null);
			sendStyleComboBox.setSelectedIndex(0);
			delayedMap=new HashMap<String, Integer>();
			delayedTextField.setText(null);
			getFgbj1Pnl().setText(null);
			getFgbj1Pnl().setValue(null);
		}
		initjp(index);
	
	}

	public boolean ischeckpass() {
		String shuru=hvo.getIoflag()==null?"输入":hvo.getIoflag();
		if(pks!=null&&pks.length>0&&pks[0].equals(MESSTYPE_GSWJ)){
			int fileindex=filetype.getSelectedIndex();
			if((shuru.equals("输入")||shuru.equals("输出"))&&fileindex==0){
				if(getFgbj1Pnl().getRefPK()==null||getFgbj1Pnl().getRefPK().length()<=0){
					MessageDialog.showErrorDlg(this,  "提示", "txt文件格式必须输入分割标记");
					return false;
				}
			}else if((shuru.equals("输出")||shuru.equals("输入"))&&fileindex==1){
				Object obj1=getRightXmlFormat().getBillModel().getValueAt(0, "value");
				Object obj2=getRightXmlFormat().getBillModel().getValueAt(1, "value");
				Object obj1propertys=getRightXmlFormat().getBillModel().getValueAt(0, "nodeproperty");
				Object obj2propertys=getRightXmlFormat().getBillModel().getValueAt(1, "nodeproperty");
				Object obj3propertys=getRightXmlFormat().getBillModel().getValueAt(2, "nodeproperty");
				if(!(obj1!=null&&obj1.toString().trim().length()>0&&obj2!=null&&obj2.toString().trim().length()>0)){
					MessageDialog.showErrorDlg(this,  "提示", "xml文件格式必须输入根标签和一级标签对应值");
					return false;
				}
				boolean flag=true;
				flag=checkxmlPropertys(obj1propertys);
				if(!flag){
					MessageDialog.showErrorDlg(this,  "提示", "xml属性必须是key=value，如果是多个参数用\",\"隔开");
					return false;
				}
				flag=checkxmlPropertys(obj2propertys);
				if(!flag){
					MessageDialog.showErrorDlg(this,  "提示", "xml属性必须是key=value，如果是多个参数用\",\"隔开");
					return false;
				}
				flag=checkxmlPropertys(obj3propertys);
				if(!flag){
					MessageDialog.showErrorDlg(this,  "提示", "xml属性必须是key=value，如果是多个参数用\",\"隔开");
					return false;
				}
				
//				if(getFgbj1Pnl().getRefPK()==null||getFgbj1Pnl().getRefPK().length()<=0||
//						getFgbj2Pnl().getRefPK()==null||getFgbj2Pnl().getRefPK().length()<=0){
//					MessageDialog.showErrorDlg(this,  "提示", "xml文件格式必须输入一级节点和二级节点标志");
//					return false;
//				}
			}
//		}else if(pks!=null&&pks.length>0&&(pks[0].equals(DataformatPanel.MESSTYPE_CSKS)||pks[0].equals(DataformatPanel.MESSTYPE_CSSJ)||pks[0].equals(DataformatPanel.MESSTYPE_CSJS))){
		}else if(sourcetype.equals(IContrastUtil.DATAORIGIN_XXZX)){
//			if(mergeStyleComboBox.getSelectedIndex()==0){
//				getMergemarkRefPanelPnl().setPK(null);
//				mergeCountTextField.setText(null);
//			}
				if(getFgbj1Pnl().getRefPK()==null||getFgbj1Pnl().getRefPK().length()<=0){
					MessageDialog.showErrorDlg(this,  "提示", "消息总线必须输入分割标记");
					return false;
				}
				if(getMergemarkRefPanelPnl().getRefPK()!=null&&getFgbj1Pnl().getRefPK()!=null){
					String mergedata=getMergemarkRefPanelPnl().getRefValue("dip_messagelogo.cdata")==null?"":getMergemarkRefPanelPnl().getRefValue("dip_messagelogo.cdata").toString();
					String fgbjdata=getFgbj1Pnl().getRefValue("dip_messagelogo.cdata")==null?"":getFgbj1Pnl().getRefValue("dip_messagelogo.cdata").toString();
					if(mergedata.equals(fgbjdata)){
						MessageDialog.showErrorDlg(this,  "提示", "消息总线分割标记不能和合并标记相同");
						return false;
					}
					if(mergedata.contains(fgbjdata)||fgbjdata.contains(mergedata)){
						MessageDialog.showErrorDlg(this,  "提示", "消息总线分割标记不能和合并标记相互包含");
						return false;
					}
				}
				int mergeCount=0;
				if(mergeStyleComboBox.getSelectedIndex()!=0){
					if(mergeCountTextField.getText()!=null&&getMergemarkRefPanelPnl().getRefPK()!=null){
						String text=mergeCountTextField.getText();
						String regex="\\d{1,5}";
						if(text.matches(regex)){
							mergeCount=Integer.parseInt(text);
							if(!(mergeCount>0&&mergeCount<10000)){
								MessageDialog.showErrorDlg(this,  "提示", "消息总线合并数必须是1-10000之间的数字");
								return false;
							}
						}else{
							MessageDialog.showErrorDlg(this,  "提示", "消息总线合并数必须是1-10000之间的数字");
							return false;
						}
					}else{
						MessageDialog.showErrorDlg(this,  "提示", "消息总线合并数和合并标志必须填写");
						return false;
					}
					
					
					if(sendPageRunSysTextField.getText()!=null&&getMergemarkRefPanelPnl().getRefPK()!=null){
						String text=sendPageRunSysTextField.getText();
						String regex="\\d{1,5}";
						if(text.matches(regex)){
							int count=Integer.parseInt(text);
							if(count*mergeCount<=0||count*mergeCount>99999){
								MessageDialog.showErrorDlg(this,  "提示", "消息总线合并数和分页参数乘积必须是1-100000之间的数字");
								return false;
							}
						}else{
							MessageDialog.showErrorDlg(this,  "提示", "消息总线合并数和分页参数乘积必须是1-100000之间的数字");
							return false;
						}
					}else{
						MessageDialog.showErrorDlg(this,  "提示", "消息总线分页参数和合并标志必须同时为空或者有值");
						return false;
					}
				}
//				if(delayedTextField.getText()!=null&&delayedTextField.getText().trim().length()>0){
//					String text=delayedTextField.getText();
//					String regex="\\d{1,9}";
//					if(!text.matches(regex)){
//						MessageDialog.showErrorDlg(this,  "提示", "延时时间必须是数字");
//						return false;
//					}
//				}
				
				
		}
		
		return true;
	}
	public static boolean checkxmlPropertys(Object obj1propertys){
		if(obj1propertys!=null&&obj1propertys.toString().trim().length()>0){
			String str=obj1propertys.toString().trim();
			if(str.contains(",")){
				String vars[]=str.split(",");
				if(vars!=null&&vars.length>1){
					for(int i=0;i<vars.length;i++){
						String var=vars[i];
						if(var!=null&&var.trim().length()>2&&var.contains("=")){
							String vartwo[]=var.split("=");
							if(!(vartwo!=null&&vartwo.length==2)){
								return false;
							}
						}else{
							return false;
						}
					}
				}else{
					return false;
				}
			}else{
				if(str.contains("=")&&str.length()>2){
					String vars[]=str.split("=");
					if(vars!=null&&vars.length==2){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
		}
		return true;
	}
}
