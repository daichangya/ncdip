package nc.ui.dip.authorize.maintain;


import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.CorpRoleAuthRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.contresult.ContResultDlg;
import nc.ui.dip.contwhquery.ContWHQueryDlg;
import nc.ui.dip.datalook.DefBillTableCellRender;
import nc.ui.dip.datarec.DatarecDlg;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.dip.dlg.FastSetAskDLG;
import nc.ui.dip.dlg.design.DesignDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.ITreeManageController;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.authorize.maintain.DipADContdatawhHVO;
import nc.vo.dip.authorize.maintain.MyBillVO;
import nc.vo.dip.contdatawh.ContDataMapUtilVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipDesignVO;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{

	ContDataWHClientUI ui = (ContDataWHClientUI)this.getBillUI();
//	public HashMap contDataMap = new HashMap(); //����ϵͳ
//	public HashMap exteDataMap = new HashMap(); //������ϵͳ
	StringBuffer extesysfield = new StringBuffer();//������ϵͳ�ֶ�
	StringBuffer contsysfield = new StringBuffer();//����ϵͳ�ֶ�
	StringBuffer contsysfieldname = new StringBuffer();//����ϵͳ�����ֶ�
	StringBuffer extesysfieldname = new StringBuffer();
	String exteField = "";//������ϵͳ�ֶ�
	String exteFieldPk = "";//������ϵͳ����
	String contField = ""; // ����ϵͳ�ֶ�
	String contFieldPk = "";//����ϵͳ����
	String extsql="";//������ϵͳ��sql
	Integer selectrow=0;
	StringBuffer contsysfieldAll = new StringBuffer();//Ӣ������+��������
	StringBuffer extesysfieldAll = new StringBuffer();
	//excelʹ��
	StringBuffer fieldsAll = new StringBuffer();
	StringBuffer fieldNamesAll = new StringBuffer();
	DipDesignVO[] hlistVO;
	DipDesignVO[] blistVO;
	List ll=new ArrayList();//�����ɾ�����ݶ���ά�����еĴ�����Ϣ��pk������

//	List list=new ArrayList();
	public MyEventHandler(BillTreeManageUI billUI, ICardController control){
		super(billUI,control);		
	}

	IQueryField queryfield=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());

	//���밴ť LX 2011-5-20
	@Override
	protected void onBoImport() throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ㣡");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������������"); 
			return;
		}
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(jfileChooser.SAVE_DIALOG);
		if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		if(!path.endsWith(".xls")){
			getSelfUI().showErrorMessage("��ѡ��EXCEL�ļ����룡");
			return;
		}
		VOTreeNode node=getSelectNode();
		String pk=(String) node.getNodeID();
		DipADContdataVO cvo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pk);
		FileInputStream fin=new FileInputStream(path);
		TxtDataVO tvo=new TxtDataVO();
		try{
			HSSFWorkbook book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(0);
			if(sheet == null){
				getSelfUI().showErrorMessage("�����ļ���ʽ����ȷ��");
			}
			tvo.setSheetName(book.getSheetName(0));
			tvo.setStartRow(sheet.getFirstRowNum());
			tvo.setStartCol(sheet.getLeftCol());
			tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//ȥ��������
			tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
			tvo.setFirstDataRow(sheet.getFirstRowNum());
			tvo.setFirstDataCol(sheet.getLeftCol());

			HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
				titlemap.put((String)getCellValue(titleRow.getCell(i)).toString(),Short.toString(i));
			}
			tvo.setTitlemap(titlemap);
			int titlesize=titlemap.size();
			for(int i=1;i<=tvo.getRowCount();i++){
				HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
				for(short j=0;j<titlesize;j++){
					tvo.setCellData(i-1, j, getCellValue(row.getCell(j)));
				}
			}
		}finally{
			fin.close();
		}
		MyImpDlg dlg=new MyImpDlg(getSelfUI(),new UFBoolean(true),tvo.getTitlemap() );
		dlg.show();
		Object[] ob=dlg.getRet();
		StringBuffer su=new StringBuffer();//�ɹ�
		StringBuffer fa=new StringBuffer();//ʧ��
		StringBuffer nuc=new StringBuffer();//û�ж�Ӧ����
		StringBuffer nue=new StringBuffer();//û�ж�Ӧ���ݱ�����ϵͳ
		StringBuffer cz=new StringBuffer();//���ڶ��յ�
		if(ob==null||ob[0]==null||ob[1]==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ������ֶΣ�");
			return;
		}else{
			//�ⲿϵͳ��NCϵͳ�����ֶ�
			String contimpcol=(String) ob[0];
			String exteimpcol=(String) ob[1];
			String c=tvo.getTitlemap().get(contimpcol);
			String e=tvo.getTitlemap().get(exteimpcol);

			String memorytable=cvo.getMiddletab();//�洢����
			//�����м��
			String sql = "create table "+memorytable+"(" +
			"pk char(20) not null primary key," +
			"contpk varchar2(200)," +
			"extepk char(20),ts char(19),dr smallint)";


			if(!isTableExist(memorytable)){
				queryfield.exesql(sql);
			}

			//����ϵͳ���������������ֶ�
			String conttable=cvo.getContabname();
			String contcol=cvo.getContcolname();

			//������ϵͳ���������������ֶ�
			String extetabname=cvo.getExtetabname();
			String extecolname=cvo.getExtecolname();
			
			DipDatadefinitBVO dzbvo1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, cvo.getContcolcode());
			DipDatadefinitBVO dzbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dzbvo1.getQuotetable());
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, dzbvo.getPk_datadefinit_h());
			List list=null;
			for(int i=0;i<tvo.getDataSize();i++){
				//System.out.println("i====="+i);
				//System.out.println("cccc==================="+tvo.getRowData(i).getAttributeValue(c));
				String cs=(String) (tvo.getRowData(i).getAttributeValue(c)+"");
				//System.out.println("eeeeeee==================="+tvo.getRowData(i).getAttributeValue(c));
				String es=(String) (tvo.getRowData(i).getAttributeValue(e)+"");
				
				
				String sql1="select "+dzbvo.getEname()+" from "+hvo.getMemorytable()+" where "+dzbvo.getEname()+"='"+cs+"'";
				list=queryfield.queryfieldList(sql1);
				if(list==null||list.size()<=0){
					nuc.append((i+1)+",");
					continue;
				}
				String sql2="select "+extecolname+" from "+extetabname+" where "+extecolname+"='"+es+"'";
				list=queryfield.queryfieldList(sql2);
				if(list==null||list.size()<=0){
					nue.append((i+1)+",");
					continue;
				}
				String sql3="select pk from "+memorytable+" where contpk='"+cs+"'";//+/*"' and extepk='"+es+"'"*/;
				list=queryfield.queryfieldList(sql3);
				if(list!=null&&list.size()>0){
					cz.append((i+1)+",");
					continue;
				}
				RowDataVO vo = new RowDataVO();
				vo.setTableName(memorytable);
				vo.setPKField("pk");
				vo.setAttributeValue("contpk", cs);
				vo.setAttributeValue("extepk", es);
				vo.setAttributeValue("pk", queryfield.getOID());
				String ss=HYPubBO_Client.insert(vo);
				if(ss!=null&&ss.length()>0){
					su.append((i+1)+",");
				}else{
					fa.append((i+1)+",");
				}
			}

		}
		String msg="";
		if(su!=null&&su.length()>0){
			msg=msg+"����ɹ����ݡ�"+su.substring(0,su.length()-1)+"��;";
		}
		if(fa!=null&&fa.length()>0){
			msg=msg+"����ʧ�����ݡ�"+fa.substring(0,fa.length()-1)+"��;";
		}
		if(nuc!=null&&nuc.length()>0){
			msg=msg+"û���ҵ���Ӧ����ϵͳ���ݡ�"+nuc.substring(0,nuc.length()-1)+"��;";
		}
		if(nue!=null&&nue.length()>0){
			msg=msg+"û���ҵ���Ӧ������ϵͳ���ݡ�"+nue.substring(0,nue.length()-1)+"��;";
		}
		if(cz!=null&&cz.length()>0){
			msg=msg+"�Ѿ����ڶ��չ�ϵ���ݡ�"+cz.substring(0,cz.length()-1)+"��;";
		}

		MessageDialog.showHintDlg(getSelfUI(), "����Ȩ��ά������",msg);
//		StringBuffer su=new StringBuffer();//�ɹ�
//		StringBuffer fa=new StringBuffer();//ʧ��
//		StringBuffer nuc=new StringBuffer();//û�ж�Ӧ����
//		StringBuffer nue=new StringBuffer();//û�ж�Ӧ���ݱ�����ϵͳ
//		StringBuffer cz=new StringBuffer();//���ڶ��յ�
//		MessageDialog.showHintDlg(getSelfUI(), "���ݶ���ά������","����ɹ����ݡ�"+su+"��;����ʧ�����ݡ�"+fa+"��;û���ҵ���Ӧ����ϵͳ���ݡ�"+nuc+"��;û���ҵ���Ӧ������ϵͳ���ݡ�"+nue+"��;�Ѿ����ڶ��չ�ϵ���ݡ�"+cz+"��");


	}

	private Object getCellValue(HSSFCell cell) {
		if(cell==null){
			return null;
		}
		Object rst = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			// �õ�Boolean����ķ���
			System.out.print(cell.getBooleanCellValue() + " ");
			rst = new UFBoolean(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			String value = Double.toString(cell.getNumericCellValue());
			//String ssssss=cell.getCellFormula();
			//String ssss=cell.getStringCellValue();
			if (value.contains(".")&&!value.contains("E")) {
				try {
					String temp = value.replace(".", "-");
					int lastNo = Integer.parseInt(temp.split("-")[1]);
					if (lastNo == 0) {
						rst = Integer.parseInt(temp.split("-")[0]);
					} else {
//						String str=cell.getNumericCellValue()+"";
//						str=str.trim();
//						if(str.indexOf("E")>0){
//						int str.split("E")[1];

//						}

						rst = new UFDouble(cell.getNumericCellValue());

					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					rst = new UFDouble(cell.getNumericCellValue());
				}
			}else if(value.contains(".")&& value.contains("E")){
				try{
					String sub=value.substring(0, value.length()-2);
					String temp=sub.replace(".", "-");
					int lastNo=Integer.parseInt(temp.split("-")[1]);
					if(lastNo==0){
						rst = Integer.parseInt(temp.split("-")[0]);
					}else{
						rst = new UFDouble(cell.getNumericCellValue());
						if(rst.toString().contains(".")){
							String str=rst.toString().substring(0, rst.toString().lastIndexOf("."));
							rst=Integer.parseInt(str.toString());
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					rst = new UFDouble(cell.getNumericCellValue());
				}
			}


//			if(value.contains("E")){
//			String str[]=value.split("E");
//			float s=(float) Double.parseDouble(str[0]);
//			float k=0;
//			String st=new String();
//			for(int i=1;i<=Integer.parseInt(str[1]);i++){
//			st=s*10+"";
//			System.out.println("k=="+k);
//			s=k;
//			System.out.println("s=="+s);
//			}
//			rst=new UFDouble(s);
//			}

			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// ��ȡ��ʽ
			rst = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_STRING:
			// ��ȡString
			rst = cell.getRichStringCellValue();
			try {
				rst = new UFDate(rst.toString());
			} catch (Exception e) {
				rst = cell.getRichStringCellValue().toString();
			}
			System.out.print(cell.getRichStringCellValue().toString() + " ");
			break;
		}
		return rst;
	}


	protected ITreeManageController getUITreeManageController() {
		return (ITreeManageController) getUIController();
	}
	/**
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private ContDataWHClientUI getSelfUI() {
		return (ContDataWHClientUI) getBillUI();
	}
	/**
	 * ȡ��ѡ�еĽڵ����
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}


	@Override
	protected void onBoEdit() throws Exception {

		//getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�����޸Ĳ�����"); 
			return;
		}
		super.onBoEdit();
		//ui.updateButtonUI();

	}
	/*
	 * ���ӱ��水ť��ʱ���ж�
	 * 2011-5-19
	 * */

	@Override
	protected void onContSave(int intBtn) throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ㣡");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������������"); 
			return;
		}

		int headcount = ui.getBillListPanel().getHeadTable().getSelectedRow();
		//������ϵͳ����
		String headpk = ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField)==null?"":ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField).toString();
//		int bodycount = ui.getBillListPanel().getBodyBillModel().getRowCount();
//		StringBuffer strPk = new StringBuffer();
//		StringBuffer deletePk = new StringBuffer();
		/*//����ϵͳѡ������
		for(int i = 0;i<bodycount;i++){
			Object selectcont = ui.getBillListPanel().getBodyBillModel().getValueAt(i, "selectcont");
			UFBoolean flag = new UFBoolean(selectcont==null?"false":selectcont.toString());
			//ѡ��
			if(flag.booleanValue()){
				String bodypk = ui.getBillListPanel().getBodyBillModel().getValueAt(i, contField)==null?"":ui.getBillListPanel().getBodyBillModel().getValueAt(i, contField).toString();
				strPk.append(bodypk+",");
			}
			else{
				String bodypk = ui.getBillListPanel().getBodyBillModel().getValueAt(i, contField)==null?"":ui.getBillListPanel().getBodyBillModel().getValueAt(i, contField).toString();
				deletePk.append("'"+bodypk+"',");
			}
		}*/
		DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);

		String tablename = contdataVO.getMiddletab();
//		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		String sql;
		Map addandremov=ui.getBodySplitPane().getRemoveOrAddMap();
		if(addandremov!=null&&addandremov.size()>0){
			Iterator<String> it=addandremov.keySet().iterator();
			while(it.hasNext()){
				String key=it.next();
				if(addandremov.get(key)==IContrastUtil.WH_ADD){
					sql = "insert into "+tablename+" (pk,contpk,extepk) values('"+queryfield.getOID()+"','"+key+"','"+headpk+"')";
				}else{
					sql="delete from "+tablename+" where contpk ='"+key+"' and extepk='"+headpk+"' ";
				}
				queryfield.exesql(sql);
			}
		}
		ui.getBodySplitPane().getRemoveOrAddMap().clear();
//		ui.showWarningMessage("������ɣ�");
		isEditBtn(false);
		ui.getBodySplitPane().initialize(true);
		afterExtQuery(extsql);

		getSelfUI().getBillListPanel().getHeadTable().changeSelection(selectrow, 0, false, false);
		getSelfUI().setTreeEnable(true);
		getBillUI().setBillOperate(IBillOperate.OP_INIT);

		ui.getBodySplitPane().getLeftValueSP().getBodyItem("isselect").setEdit(false);
		ui.getBodySplitPane().getRightPane().getBodyItem("isselect").setEdit(false);
	}

	@Override

	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		ui.getBodySplitPane().initialize(true);
		afterExtQuery(extsql);
		getSelfUI().getBillListPanel().getHeadTable().changeSelection(selectrow, 0, false, false);
		isEditBtn(false);
		getSelfUI().getBodySplitPane().getRemoveOrAddMap().clear();
		getSelfUI().setTreeEnable(true);
		ui.getBodySplitPane().getLeftValueSP().getBodyItem("isselect").setEdit(false);
		ui.getBodySplitPane().getRightPane().getBodyItem("isselect").setEdit(false);
	}

	/**
	 * �ж�ϵͳ�����Ƿ�����׼�������Ĵ˱�
	 * �ڵ��������ťʱ�жϣ�
	 * 1.����д˱���ѯ�����Ƿ������ݣ���������ݣ�������ʾ��û�У���ֱ��ɾ�������´���
	 * 2.û�д˱���ֱ�Ӵ���
	 */
	public boolean isTableExist(String tableName){

		if(tableName==null){
			return false;
		}
		boolean isExist=false;//Ĭ��û�д˱�
		IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql="select 1 from user_tables where table_name='"+tableName.toUpperCase()+"';";	
		try{
			ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
			if(al.size()>=1){
				isExist=true;//�д˱�			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}
	@Override
	protected void onBoDelete() throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫɾ���Ľڵ㣡");
			return;
		}
		String fpk=(String) tempNode.getParentnodeID();
		if(fpk==null || fpk.trim().equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻����ɾ��������");
			return ;
		}

		if (getSelfUI().isListPanelSelected() && getSelfUI().getBillListPanel().getHeadTable().getSelectedRow() < 0) {
			getSelfUI().showErrorMessage("��ѡ���ұߴ����б�����Ҫɾ���ļ�¼��");
			return;
		}

		CircularlyAccessibleValueObject billvo = getBufferData().getCurrentVO().getParentVO();
		super.onBoDelete();
		//����õ������ӱ���ʱ���Զ�ά�����ṹ����ִ�����²���
		if (getUITreeManageController().isAutoManageTree()) {
			getSelfUI().getBillTreeData().deleteNodeFromTree(billvo);
		}
		super.onBoRefresh();
	}


	public void onTreeSelected(VOTreeNode arg0){
		try {
			VOTreeNode tempNode = getSelectNode();
			String pk=null;
			if(tempNode !=null){
				pk=(String) tempNode.getParentnodeID();
			}
			if(pk==null){
				getButtonManager().getButton(IBtnDefine.CONTSYSQUERY).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.BCONTSYSQUERY).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.CONTSAVE).setEnabled(false);
				getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
				getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.VALIDATECHECK).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
				getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				getButtonManager().getButton(IBtnDefine.DATACHECK).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.autoContData).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.FAST_SET).setEnabled(false);
				ui.updateButtons();
				return ;
			}else{
				DipADContdatawhHVO datahvo=(DipADContdatawhHVO) tempNode.getData();
				if(datahvo!=null&&datahvo.getIsfolder()!=null&&datahvo.getIsfolder().booleanValue()){
					getButtonManager().getButton(IBtnDefine.CONTSYSQUERY).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.BCONTSYSQUERY).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.CONTSAVE).setEnabled(false);
					getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
					getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.VALIDATECHECK).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
					getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.DATACHECK).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.autoContData).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.edit).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.FAST_SET).setEnabled(false);
					ui.updateButtons();
					return;
				}else{
					getButtonManager().getButton(IBtnDefine.CONTSYSQUERY).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.BCONTSYSQUERY).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.edit).setEnabled(false);
					getButtonManager().getButton(IBtnDefine.CONTSAVE).setEnabled(false);
					getButtonManager().getButton(IBillButton.ImportBill).setEnabled(true);
					getButtonManager().getButton(IBillButton.ExportBill).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.VALIDATECHECK).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.SET).setEnabled(true);
					getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.DATACHECK).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.autoContData).setEnabled(true);
					getButtonManager().getButton(IBtnDefine.FAST_SET).setEnabled(true);
					ui.updateButtons();
				}
				
				
			}

			SuperVO vo=(SuperVO) arg0.getData();

//			MyBillVO billvo =new MyBillVO();
//
//			//������VO
//			billvo.setParentVO(vo);
//
//			// ��ʾ����
//			getBufferData().addVOToBuffer(billvo);

			//�б���棬���±�ͷ�ֶ�����
			boolean suc=false;
			DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
			if(contdataVO==null){
				getSelfUI().showErrorMessage("������Ȩ����ڵ���ɾ����");
				return;
			}
			
			extesysfield = new StringBuffer();
			contsysfield = new StringBuffer();

			contsysfieldAll = new StringBuffer();//Ӣ������+��������
			extesysfieldAll = new StringBuffer();
			contsysfieldname = new StringBuffer();//����ϵͳ�����ֶ�
			extesysfieldname = new StringBuffer();
			fieldsAll = new StringBuffer();
			fieldNamesAll = new StringBuffer();

			contsysfieldname=new StringBuffer();

			//��ѯ����ϵͳ�ֶ�
			//�޸Ľ����ϵı�ͷ�ֶ�,����ϵͳ
			ui.getBodySplitPane().getLeftValueSP().updateUI();
			hlistVO=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, "pk_datadefinit_h='"+contdataVO.getExtetabcode()/*.getContabcode()*/+"' and nvl(dr,0)=0 and designtype=0");
			ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().clearBodyData();
			ui.getBodySplitPane().getRightPane().getBodyBillModel().clearBodyData();
			for(int i=1;i<=60;i++){
				if(hlistVO!=null && hlistVO.length>0&&i<=hlistVO.length){
					DipDesignVO bvo = hlistVO[i-1];
					String cname = bvo.getCname();
					if(contdataVO.getExtecolname().toUpperCase().equals(bvo.getEname().toUpperCase())){
						exteField = "vdef"+String.valueOf(i);
						exteFieldPk = bvo.getEname();
					}
					extesysfieldAll.append(" t2."+bvo.getEname()+",");	
					extesysfield.append(""+bvo.getEname()+",");
					extesysfieldname.append(""+cname+",");
					fieldsAll.append(""+bvo.getEname()+",");
					fieldNamesAll.append(""+cname+",");

					ui.getBillListPanel().getHeadItem("vdef"+String.valueOf(i)).setName(cname);
					ui.getBillListPanel().getHeadItem("vdef"+String.valueOf(i)).setShow(true);
					ui.getBillListPanel().getHeadItem("vdef"+String.valueOf(i)).setWidth(bvo.getDesignlen());
					String key="vdef"+String.valueOf(i);
					if(bvo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(bvo.getDatatype().toUpperCase())>=0)){
						ui.getBillListPanel().getHeadItem(key).setDataType(BillItem.INTEGER);
					}else{
						ui.getBillListPanel().getHeadItem(key).setDataType(BillItem.STRING);
					}
					if(bvo.getDesigplen()!=null&&bvo.getDesigplen()>0){
						ui.getBillListPanel().getHeadItem(key).setDataType(BillItem.DECIMAL);
						ui.getBillListPanel().getHeadItem(key).setDecimalDigits(bvo.getDesigplen());
					}
					
				}else{
					ui.getBillListPanel().getHeadItem("vdef"+String.valueOf(i)).setShow(false);
				}
			}
			ui.getBillListPanel().setListData(ui.getBillListPanel().getBillListData());
			ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());

			ui.getBillListPanel().getHeadTable().setSortEnabled(true);
			DipDatadefinitBVO dzbvo1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contdataVO.getContcolcode());
			DipDatadefinitBVO dzbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dzbvo1.getQuotetable());
			

			blistVO=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, "pk_datadefinit_h='"+dzbvo.getPk_datadefinit_h()/*.getExtetabcode()*/+"' and nvl(dr,0)=0 and designtype=0");
			ui.getBillListPanel().getHeadBillModel().clearBodyData();
			for(int i=1;i<=60;i++){
				if(blistVO!=null && blistVO.length>0&&i<=blistVO.length){
					DipDesignVO bvo = blistVO[i-1];
					String cname = bvo.getCname();
					if(dzbvo.getCname()!=null&&dzbvo.getEname().toUpperCase().equals(bvo.getEname().toUpperCase())){
						contField = "vdef"+String.valueOf(i);
						contFieldPk = bvo.getEname();
					}

					contsysfield.append(""+bvo.getEname()+",");
					contsysfieldname.append(""+cname+",");
					contsysfieldAll.append(" t1."+bvo.getEname()+",");
					fieldsAll.append(""+bvo.getEname()+",");
					fieldNamesAll.append(""+cname+",");

					ui.getBodySplitPane().getLeftValueSP().getBodyItem("vdef"+String.valueOf(i)).setName(cname);
					ui.getBodySplitPane().getLeftValueSP().showBodyTableCol("vdef"+String.valueOf(i));
					ui.getBodySplitPane().getLeftValueSP().getBodyItem("vdef"+String.valueOf(i)).setWidth(bvo.getDesignlen());
					
					ui.getBodySplitPane().getRightPane().getBodyItem("vdef"+String.valueOf(i)).setName(cname);
					ui.getBodySplitPane().getRightPane().showBodyTableCol("vdef"+String.valueOf(i));
					ui.getBodySplitPane().getRightPane().getBodyItem("vdef"+String.valueOf(i)).setWidth(bvo.getDesignlen());
					
					String key="vdef"+String.valueOf(i);
					if(bvo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(bvo.getDatatype().toUpperCase())>=0)){
						ui.getBodySplitPane().getLeftValueSP().getBodyItem(key).setDataType(BillItem.INTEGER);
						ui.getBodySplitPane().getRightPane().getBodyItem(key).setDataType(BillItem.INTEGER);
					}else{
						ui.getBodySplitPane().getLeftValueSP().getBodyItem(key).setDataType(BillItem.STRING);
						ui.getBodySplitPane().getRightPane().getBodyItem(key).setDataType(BillItem.STRING);
					}
					if(bvo.getDesigplen()!=null&&bvo.getDesigplen()>0){
						ui.getBodySplitPane().getLeftValueSP().getBodyItem(key).setDataType(BillItem.DECIMAL);
						ui.getBodySplitPane().getRightPane().getBodyItem(key).setDecimalDigits(bvo.getDesigplen());
						
						ui.getBodySplitPane().getLeftValueSP().getBodyItem(key).setDataType(BillItem.DECIMAL);
						ui.getBodySplitPane().getRightPane().getBodyItem(key).setDecimalDigits(bvo.getDesigplen());
					}
					
					
				}else{
					ui.getBodySplitPane().getLeftValueSP().hideBodyTableCol("vdef"+String.valueOf(i));
					ui.getBodySplitPane().getRightPane().hideBodyTableCol("vdef"+String.valueOf(i));

				}
			}
			
			getSelfUI().getBodySplitPane().getLeftValueSP().setListData(getSelfUI().getBodySplitPane().getLeftValueSP().getBillListData());
			getSelfUI().getBodySplitPane().getRightPane().setListData(getSelfUI().getBodySplitPane().getRightPane().getBillListData());

			if(blistVO!=null&&blistVO.length>0){
				for(int i=0;i<blistVO.length;i++){
					BillItem it=ui.getBodySplitPane().getRightPane().getBodyItem("vdef"+(i+1));
					DipDesignVO lvo=blistVO[i];
					String ss=lvo.getContype();
					if(it!=null){
						if(ss!=null){
							int l;
							if(ss.equals("��")){
								l=SwingConstants.LEFT;
							}else if(ss.equals("��")){
								l=SwingConstants.RIGHT;
							}else {
								l=SwingConstants.CENTER;
							}
							ui.getBodySplitPane().getRightPane().getBodyTable().getColumnModel().getColumn(i+1).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),l));
							ui.getBodySplitPane().getLeftValueSP().getBodyTable().getColumnModel().getColumn(i+1).setCellRenderer(new DefBillTableCellRender(ui.getBodySplitPane().getLeftValueSP().getBodyItem("vdef"+(i+1)),new BillRendererVO(),l));
						}
					}
					
				}
			}
			ui.getBodySplitPane().setPkField(contField);
			ui.getBodySplitPane().getRightPane().getBodyTable().setSortEnabled(false);
			ui.getBodySplitPane().getLeftValueSP().getBodyTable().setSortEnabled(false);

			ui.getBodySplitPane().getRightPane().getBodyBillModel().clearBodyData();
		} catch (UifException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		} 

	}
	
	public void iniSelect(){
		try{
			String node=getSelfUI().selectnode;
			if(node==null||node.length()<=0){
				return;
			}
			String pk1=(String) getSelectNode().getParentnodeID();
			if(pk1==null||pk1.length()<=0){
				return ;
			}
				String sql = "";
				int headcount = ui.getBillListPanel().getHeadTable().getSelectedRow();
				//������ϵͳ����
				String headpk = ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField)==null?"":ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField).toString();
				DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
				String tablename = contdataVO.getMiddletab();
				DipDatadefinitHVO hvo = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, contdataVO.getContabcode());
//				IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
				String sqlcount="select count(*) from "+hvo.getMemorytable()+" where 1=1 and nvl(dr,0) =0 and "+contdataVO.getContcolname()+" not in (select contpk from "+tablename+"  ) ";
				List lis=queryfield.queryfieldList(sqlcount);
				if(lis==null||lis.size()<=0){
					getSelfUI().showErrorMessage("û�з��ϲ�ѯ�����Ľ������");
					return ;
				}else{
					Object obj=lis.get(0);
					if(obj==null){
						getSelfUI().showErrorMessage("û�з��ϲ�ѯ�����Ľ������");
						return ;
					}
					Integer count=Integer.parseInt(obj.toString());
					if(count>10000){
						getSelfUI().showErrorMessage("������̫���������ò�ѯ����!");
						return;
					}
				}
				//��ѯ���ձ��е�ֵ
				sql = "select ";
				if(!"".equals(contsysfield.toString()))
					sql = sql+""+contsysfield.toString().subSequence(0, contsysfield.toString().length()-1)+" from "+hvo.getMemorytable()+" where 1=1 and nvl(dr,0) =0 and "+contdataVO.getContcolname()+" not in (select contpk from "+tablename+" ) ";
				else{
					return ;
				}
				List listValue = queryfield.queryListMapSingleSql(sql);
				String contField [] = contsysfield.toString().split(",");
				ui.getBodySplitPane().getRightPane().getBodyBillModel().clearBodyData();
				if(listValue!=null && listValue.size()>0){
					if(blistVO!=null&&blistVO.length>0){
						for(int i=0;i<blistVO.length;i++){
							BillItem it=ui.getBodySplitPane().getRightPane().getBodyItem("vdef"+(i+1));
							DipDesignVO lvo=blistVO[i];
							String ss=lvo.getContype();
							if(it!=null){
								if(ss!=null){
									int l;
									if(ss.equals("��")){
										l=SwingConstants.LEFT;
									}else if(ss.equals("��")){
										l=SwingConstants.RIGHT;
									}else {
										l=SwingConstants.CENTER;
									}
									ui.getBodySplitPane().getRightPane().getBodyTable().getColumnModel().getColumn(i+1).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),l));
								}
							}
							
						}
						for(int i = 0;i<listValue.size();i++){
							HashMap mapValue = (HashMap)listValue.get(i);
							ui.getBodySplitPane().getRightPane().getBodyBillModel().addLine();
							for(int j = 0;j<contField.length;j++){
								ui.getBodySplitPane().getRightPane().getBodyBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
								ui.getBodySplitPane().getRightPane().getBodyTable().getCellRenderer(i, j).getTableCellRendererComponent(ui.getBodySplitPane().getRightPane().getBodyTable(), mapValue.get(contField[j].toUpperCase()), false, false, i, j);
							}
						}
					}
				}
//				ui.getBodySplitPane().getRightPane().getBodyBillModel().setEditRow(1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵ�ϵͳ�ڵ㣡");
			return ;
		}
		String ss=(String)tempNode.getParentnodeID();
		if(ss!=null&&ss.length()>0){
			getSelfUI().showErrorMessage("����ϵͳ�ڵ㲻�������Ӳ�����");
			return ;
		}
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("fpk", tempNode.getNodeID());
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		VOTreeNode treeNode=getSelectNode();

		super.onBoLineAdd();
		int row=getSelfUI().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt(treeNode.getNodeID(),row , "fpk");
	}
	
	@Override
	protected void onBoFastSet() throws Exception {
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showWarningMessage("��ѡ��ڵ㣡");
			return;
		}
		String fpk=(String) node.getParentnodeID();
		String kk=(String) node.getNodeID();
		DipADContdataVO dfvo= (DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, kk);
		if(fpk==null||fpk.trim().equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�ܱ༭��");
			return;
		}
		int headcount = ui.getBillListPanel().getHeadTable().getSelectedRow();
		String headpk = "";
		if(headcount>=0){
			headpk = ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField)==null?"1":ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField).toString();
		}
		FastSetAskDLG adlg=new FastSetAskDLG(getSelfUI(),dfvo,headpk);
		adlg.showModal();
		if(adlg.isOK){
			String returnMessage = adlg.getReturnMessage();
			if(null != returnMessage && !"".equals(returnMessage)){
				MessageDialog.showHintDlg(ui, "��ʾ", returnMessage);
			}
		}
	}
	//��������
	@Override
	protected void onDataClear(int intBtn) throws Exception {
		super.onDataClear(intBtn);

		VOTreeNode node=getSelectNode();
		//String fnode=node.getParentnodeID().toString();
		if(SJUtil.isNull(node)){
			getSelfUI().showWarningMessage("��ѡ��ڵ㣡");
			return;
		}
		//String pk=((ContDataWHClientUI)getBillUI()).selectnode;
		String fpk=(String) node.getParentnodeID();
		String kk=(String) node.getNodeID();
		DipADContdataVO dfvo= (DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, kk);


		if(fpk==null||fpk.trim().equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�ܱ༭��");
			return;

		}
		String tabname=dfvo.getMiddletab();	
		String extecolname=dfvo.getExtecolname();//������ϵͳ��������ΨһԼ��
		String extetablename=dfvo.getExtetabname();//������ϵͳ��
		AskDLG adlg=new AskDLG(getSelfUI(),"��ʾ","ѡ��ɾ������?",new String[]{"������е���������","�����ѯ��Χ�ڵ�����","������еĴ�������"});
		adlg.showModal();

		if(adlg.isOK){
			int k=adlg.getRes();
			if(k==0){
				//��ձ��еĶ��������ݡ�
				int flag = MessageDialog.showOkCancelDlg(ui, "����", "ȷ���Ƿ�����");
				if(flag==2){
					return;
				}

				String sql="delete from "+tabname;
//				IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				queryfield.exesql(sql);
			}
			if(k==1){
				//ɾ����ѯ��Χ�ڵ����ݡ����ݲ�ѯ������nc���������м����ȥɾ����ѯ����������nc������Ӧ�ļ�¼��
				String sql="select " +extecolname+" from "+extetablename+" where "+" 1=1 "+tj;
//				IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				List list=queryfield.queryfieldList(sql);
				if(list.size()>500){
					List<String> sqlList=new ArrayList<String>();
					int number=0;
					StringBuffer sbList=new StringBuffer("delete from "+tabname+" where extepk in (");
					for(int i=0;i<list.size();i++){
						number++;      
						sbList.append("'");
						sbList.append(list.get(i).toString());
						sbList.append("'");
						boolean boo=false;
						if(number==500){
							boo=true;
							sbList.append(")");
							number=0;
							sqlList.add(sbList.toString());
							sbList=new StringBuffer("delete from "+tabname+" where extepk in (");
						}else{
							if(i!=list.size()-1){
								sbList.append(",");	
							}
							boo=false;
						}
						if(i==list.size()-1&&!boo){
							sbList.append(")");
							sqlList.add(sbList.toString());
						}
					}
					boolean flag=queryfield.exectEverySql(sqlList);
					if(!flag){
						getSelfUI().showWarningMessage("ɾ���ɹ���");
					}else{
						getSelfUI().showErrorMessage("ɾ��ʧ�ܣ�");
					}
				}else{
					StringBuffer sb=new StringBuffer("delete from "+tabname+" where extepk in (");
					for(int i=0;i<list.size();i++){
						sb.append("'");
						sb.append(list.get(i).toString());
						sb.append("'");
						if(i!=list.size()-1){
							sb.append(",");
						}else{
							sb.append(")");	
						}

					}

					boolean flag=queryfield.exectEverySql(sb.toString());
					if(!flag){
						getSelfUI().showWarningMessage("ɾ���ɹ���");
					}else{
						getSelfUI().showErrorMessage("ɾ��ʧ�ܣ�");
					}

				}


			}
			if(k==2){
				//String middletable="";
				//ɾ����������
				List<String> sql=new ArrayList<String>();
				if(ll!=null&&ll.size()>0){
					for(int m=0;m<ll.size();m++){
						String sql1="delete from "+tabname+" where pk ='"+ll.get(m).toString()+"'";
						sql.add(sql1);
					}
					ll.clear();
					if(sql!=null&&sql.size()>0){
						boolean flag=queryfield.exectEverySql(sql);
						if(!flag){
							getSelfUI().showWarningMessage("�ɹ�ɾ��"+sql.size()+"����¼��");
						}
					}
				}else{
					getSelfUI().showWarningMessage("û�д����¼��");
					return;
				}
			}

		}else{
			return;
		}





	}

	String tj="";//2011-6-10 ��ѯ����

	//������ϵͳ��ѯ����
	@Override
	protected void onBContsysQuery(int intBtn) throws Exception{
		String node=getSelfUI().selectnode;
		if(node==null||node.length()<=0){
			getSelfUI().showErrorMessage("��ѡ���ѯ�ڵ㣡");
			return;
		}
		String pk=(String) getSelectNode().getParentnodeID();
		if(pk==null||pk.length()<=0){
			getSelfUI().showErrorMessage("���ڵ㲻�ܲ�����");
			return ;
		}
		super.onBContsysQuery(intBtn);
		HashMap map = new HashMap();


		map.put("pk_contdata_h", ui.selectnode);
		map.put("keyS", "0");
		map.put("isad", "y");
		ContWHQueryDlg dlg = new ContWHQueryDlg(this.getBillUI(),new UFBoolean(true),map);
		dlg.show();
		if(dlg.getIsOnboSave()==1){
			String sql = "";
			DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
			DipDatadefinitHVO hvo = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, contdataVO.getExtetabcode());

//			IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
			String where=" and pk_role_corp_alloc in (select pk_role from dip_rolegroup_b where pk_role_group in ((select pk_fp from dip_adcontdata_b where nvl(dr,0)=0 and pk_contdata_h='"+ui.selectnode+"')) and nvl(dr, 0) = 0) "
				+" and pk_role_group in (select pk_fp from dip_adcontdata_b where nvl(dr,0)=0 and pk_contdata_h='"+ui.selectnode+"')";
			if(dlg.getReturnSql()!=null&&dlg.getReturnSql().trim().length()>0){
				where = where+dlg.getReturnSql();
			}
			String sqlcount="select count(*) from "+hvo.getMemorytable()+ " where 1=1 "+where;
			List lis=null;
			try{
				lis=queryfield.queryfieldList(sqlcount);
			}catch(SQLException ex){
//				System.out.println("["+ex.getSQLState()+"]");
//				System.out.println("["+ex.getErrorCode()+"]");
//				System.out.println("["+ex.getMessage()+"]");
				if(ex.getSQLState().equals("42000")){
					if(ex.getErrorCode()==207){
						String msg=ex.getMessage().substring(12);
						throw new Exception(msg.substring(0,msg.indexOf("\""))+" �ֶ��ڱ��в�����");
					}else if(ex.getErrorCode()==936){
						throw new Exception("��ѯ�����������飡");
					}else if(ex.getErrorCode()==208){
						throw new Exception("�����ͼ������");
					}else{
						throw ex;
					}
				}else {
					throw ex;
				}
			}
			if(lis==null||lis.size()<=0){
				getSelfUI().showErrorMessage("û�з��ϲ�ѯ�����Ľ������");
				return ;
			}else{
				Object obj=lis.get(0);
				if(obj==null){
					getSelfUI().showErrorMessage("û�з��ϲ�ѯ�����Ľ������");
					return ;
				}
				Integer count=Integer.parseInt(obj.toString());
				if(count>10000){
					getSelfUI().showErrorMessage("������̫���������ò�ѯ����!");
					return;
				}
			}
			//��ѯ���ձ��е�ֵ
			sql = "select ";
			if(!"".equals(extesysfield.toString()))
				sql = sql+""+extesysfield.toString().substring(0, extesysfield.toString().length()-1)+" from "+hvo.getMemorytable()+" where 1=1 "+where;
			else
				return ;
			extsql=sql;

			//2011-6-10
			tj=where;
			afterExtQuery(sql);
			
		}
	}
	
	public void afterExtQuery(String sql){
//		IQueryField queryfield=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List listValue = null;
		try {
			listValue = queryfield.queryListMapSingleSql(sql);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String contField [] = extesysfield.toString().split(",");
		ui.getBillListPanel().getHeadBillModel().clearBodyData();
		ui.getBillListPanel().getBodyBillModel().clearBodyData();
		
		if(listValue!=null && listValue.size()>0){
			MyBillVO[] mybillvos=new MyBillVO[listValue.size()];
			if(hlistVO!=null&&hlistVO.length>0){
				for(int i=0;i<hlistVO.length;i++){
					BillItem it=ui.getBillListPanel().getHeadItem("vdef"+(i+1));
					DipDesignVO lvo=hlistVO[i];
					String ss=lvo.getContype();
					if(it!=null){
						if(ss!=null){
							int l;
							if(ss.equals("��")){
								l=SwingConstants.LEFT;
							}else if(ss.equals("��")){
								l=SwingConstants.RIGHT;
							}else {
								l=SwingConstants.CENTER;
							}
							ui.getBillListPanel().getHeadTable().getColumnModel().getColumn(i).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),l));
						}
					}
					
					
				}
				for(int i = 0;i<listValue.size();i++){
					mybillvos[i]=new MyBillVO();
					DipADContdatawhHVO hvo=new DipADContdatawhHVO();
					HashMap mapValue = (HashMap)listValue.get(i);
					ui.getBillListPanel().getHeadBillModel().addLine();
					for(int j = 0;j<contField.length;j++){
						ui.getBillListPanel().getHeadBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
						ui.getBillListPanel().getHeadTable().getCellRenderer(i, j).getTableCellRendererComponent(ui.getBillListPanel().getHeadTable(), mapValue.get(contField[j].toUpperCase()), false, false, i, j);
						hvo.setAttributeValue("vdef"+(i+1), mapValue.get(contField[j].toUpperCase()));
					}
					mybillvos[i].setParentVO(hvo);
				}
			}
			getBufferData().clear();
			getBufferData().addVOsToBuffer(mybillvos);
		}
//		if(listValue!=null && listValue.size()>0){
//
//
//
//			for(int i = 0;i<listValue.size();i++){
//				HashMap mapValue = (HashMap)listValue.get(i);
//				ui.getBillListPanel().getHeadBillModel().addLine();
//				for(int j = 0;j<contField.length;j++){
//
//
//					ui.getBillListPanel().getHeadBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
//				}
//
//
//			}
//		}
//
//		ui.getBillListPanel().setListData(ui.getBillListPanel().getBillListData());

		getButtonManager().getButton(IBtnDefine.edit).setEnabled(true);
		ui.updateButtons();
	}

	//�ұ߽������ݲ�ѯ������ϵͳ��ѯ����
	@Override
	protected void onContsysQuery(int intBtn)throws Exception {

		String node=getSelfUI().selectnode;
		if(node==null||node.length()<=0){
			getSelfUI().showErrorMessage("��ѡ���ѯ�ڵ㣡");
			return;
		}
		String pk1=(String) getSelectNode().getParentnodeID();
		if(pk1==null||pk1.length()<=0){
			getSelfUI().showErrorMessage("���ڵ㲻�ܲ�����");
			return ;
		}
		super.onContsysQuery(intBtn);
		HashMap map = new HashMap();


		map.put("pk_contdata_h", ui.selectnode);
		map.put("keyS", "1");
		map.put("isad", "Y");
		ContWHQueryDlg dlg = new ContWHQueryDlg(this.getBillUI(),new UFBoolean(true),map);
		dlg.show();
		if(dlg.getIsOnboSave()==1){

			String sql = "";

			int headcount = ui.getBillListPanel().getHeadTable().getSelectedRow();
			//������ϵͳ����
			String headpk = ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField)==null?"":ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField).toString();

			DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
			String tablename = contdataVO.getMiddletab();
			
			DipDatadefinitBVO dzbvo1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contdataVO.getContcolcode());
			DipDatadefinitBVO dzbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dzbvo1.getQuotetable());
			DipDatadefinitHVO hvo = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, dzbvo.getPk_datadefinit_h());

//			IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
			boolean moreflag=true;//�Ƿ����ö�Զ��ϵ,
			String sqlcount="";
			String where="";
			if(moreflag){
				 where=" and "+dzbvo.getEname()+" not in (select contpk from "+tablename+" where extepk='"+headpk+"' ) ";
			}else{
				where=" and "+dzbvo.getEname()+" not in (select contpk from "+tablename+") ";
			}
			sqlcount="select count(*) from "+hvo.getMemorytable()+" where 1=1 and nvl(dr,0) =0  "+where+dlg.getReturnSql();
			List lis=null;
			try{
				lis=queryfield.queryfieldList(sqlcount);
			}catch(SQLException ex){
//				System.out.println("["+ex.getSQLState()+"]");
//				System.out.println("["+ex.getErrorCode()+"]");
//				System.out.println("["+ex.getMessage()+"]");
				if(ex.getSQLState().equals("42000")){
					if(ex.getErrorCode()==207){
						String msg=ex.getMessage().substring(12);
						throw new Exception(msg.substring(0,msg.indexOf("\""))+" �ֶ��ڱ��в�����");
					}else if(ex.getErrorCode()==936){
						throw new Exception("��ѯ�����������飡");
					}else if(ex.getErrorCode()==208){
						throw new Exception("�����ͼ������");
					}else{
						throw ex;
					}
				}else {
					throw ex;
				}
			}
			if(lis==null||lis.size()<=0){
				getSelfUI().showErrorMessage("û�з��ϲ�ѯ�����Ľ������");
				return ;
			}else{
				Object obj=lis.get(0);
				if(obj==null||obj.toString().equals("0")){
					getSelfUI().showErrorMessage("û�з��ϲ�ѯ�����Ľ������");
					return ;
				}
				Integer count=Integer.parseInt(obj.toString());
				if(count>10000){
					getSelfUI().showErrorMessage("������̫���������ò�ѯ����!");
					return;
				}
			}
			sql = "select ";
			if(!"".equals(contsysfield.toString())){
				sql = sql+""+contsysfield.toString().subSequence(0, contsysfield.toString().length()-1)+" from "+hvo.getMemorytable()+" where 1=1 and nvl(dr,0) =0 and "+dzbvo.getEname()+" in (select contpk from "+tablename+" where extepk='"+headpk+"' ) ";}

			//��ѯ���ձ��е�ֵ
			sql = "select ";
			if(!"".equals(contsysfield.toString()))
				sql = sql+""+contsysfield.toString().subSequence(0, contsysfield.toString().length()-1)+" from "+hvo.getMemorytable()+" where 1=1 and nvl(dr,0) =0  "+where+dlg.getReturnSql();
			else
				return ;

			List listValue = queryfield.queryListMapSingleSql(sql);
			String contField [] = contsysfield.toString().split(",");
			ui.getBodySplitPane().getRightPane().getBodyBillModel().clearBodyData();
			if(listValue!=null && listValue.size()>0){
				if(blistVO!=null&&blistVO.length>0){
					for(int i=0;i<blistVO.length;i++){
						BillItem it=ui.getBodySplitPane().getRightPane().getBodyItem("vdef"+(i+1));
						DipDesignVO lvo=blistVO[i];
						String ss=lvo.getContype();
						if(it!=null){
							if(ss!=null){
								int l;
								if(ss.equals("��")){
									l=SwingConstants.LEFT;
								}else if(ss.equals("��")){
									l=SwingConstants.RIGHT;
								}else {
									l=SwingConstants.CENTER;
								}
								ui.getBodySplitPane().getRightPane().getBodyTable().getColumnModel().getColumn(i+1).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),l));
							}
						}
						
					}
					for(int i = 0;i<listValue.size();i++){
						HashMap mapValue = (HashMap)listValue.get(i);
						ui.getBodySplitPane().getRightPane().getBodyBillModel().addLine();
						for(int j = 0;j<contField.length;j++){
							ui.getBodySplitPane().getRightPane().getBodyBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
							ui.getBodySplitPane().getRightPane().getBodyTable().getCellRenderer(i, j+1).getTableCellRendererComponent(ui.getBodySplitPane().getRightPane().getBodyTable(), mapValue.get(contField[j].toUpperCase()), false, false, i, j+1);
						}
					}
				}
			}
			/*ui.getBillListPanel().getBodyBillModel().clearBodyData();
			if(listValue!=null && listValue.size()>0){

				for(int i = 0;i<listValue.size();i++){
					HashMap mapValue = (HashMap)listValue.get(i);
					ui.getBillListPanel().getBodyBillModel().addLine();
					for(int j = 0;j<contField.length;j++){
						ui.getBillListPanel().getBodyBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));

						//���ø�ѡ���Ƿ�ѡ��

						if(contFieldPk.equals(contField[j])){
							String contpk = mapValue.get(contField[j].toUpperCase()).toString();//����ϵͳ����

							sql = "select pk from "+tablename+" where contpk='"+contpk+"' and extepk='"+headpk+"' ";
							String pk = queryfield.queryfield(sql);

							if(!"".equals(pk))
								ui.getBillListPanel().getBodyBillModel().setValueAt(new UFBoolean(true), i, "selectcont");
						}
					}
				}
			}
			ui.getBillListPanel().getBodyBillModel().setEditRow(1);
			ui.getBillListPanel().getBodyItem("selectcont").setEnabled(true);*/
		}

	}

	/**
	 * ��ͷ����������
	 * */
	public void onConteQuery()throws Exception{

		

		getButtonManager().getButton(IBtnDefine.BCONTSYSQUERY).setEnabled(true);
		getButtonManager().getButton(IBtnDefine.edit).setEnabled(true);
		getButtonManager().getButton(IBtnDefine.CONTSYSQUERY).setEnabled(false);
		getButtonManager().getButton(IBtnDefine.CONTSAVE).setEnabled(false);
		ui.updateButtons();
		String sql = "";
		int headcount = ui.getBillListPanel().getHeadTable().getSelectedRow();
		//������ϵͳ����
		String headpk = ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField)==null?"":ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField).toString();
//		if(headpk==null||headpk.trim().equals("")){
//			this.getSelfUI().showWarningMessage("��ͷ��������Ϊ�գ��������ã�");
//			return;
//		}
		DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
		String tablename = contdataVO.getMiddletab();

		DipDatadefinitBVO dzbvo1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contdataVO.getContcolcode());
		DipDatadefinitBVO dzbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dzbvo1.getQuotetable());
		DipDatadefinitHVO hvo = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, dzbvo.getPk_datadefinit_h());

//		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());

		//��ѯ���ձ��е�ֵ����ѯ��߱����Ѿ����յ�ֵ
		sql = "select ";
		if(!"".equals(contsysfield.toString())){
			sql = sql+""+contsysfield.toString().subSequence(0, contsysfield.toString().length()-1)+" from "+hvo.getMemorytable()+" where 1=1 and nvl(dr,0) =0 and "+/*contdataVO.getContcolname()*/dzbvo.getEname()+" in (select contpk from "+tablename+" where extepk='"+headpk+"' ) ";}
		else{
			return ;}

		List listValue = queryfield.queryListMapSingleSql(sql);
		String contField [] = contsysfield.toString().split(",");
		ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().clearBodyData();
		ui.getBodySplitPane().getRightPane().getBodyBillModel().clearBodyData();
		if(listValue!=null && listValue.size()>0){
			if(blistVO!=null&&blistVO.length>0){
				for(int i=0;i<blistVO.length;i++){
					BillItem it=ui.getBodySplitPane().getLeftValueSP().getBodyItem("vdef"+(i+1));
					DipDesignVO lvo=blistVO[i];
					String ss=lvo.getContype();
					if(it!=null){
						if(ss!=null){
							int l;
							if(ss.equals("��")){
								l=SwingConstants.LEFT;
							}else if(ss.equals("��")){
								l=SwingConstants.RIGHT;
							}else {
								l=SwingConstants.CENTER;
							}
							ui.getBodySplitPane().getLeftValueSP().getBodyTable().getColumnModel().getColumn(i+1).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),l));
						}
					}
					
				}
				for(int i = 0;i<listValue.size();i++){
					HashMap mapValue = (HashMap)listValue.get(i);
					ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().addLine();
					for(int j = 0;j<contField.length;j++){
						ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
						ui.getBodySplitPane().getLeftValueSP().getBodyTable().getCellRenderer(i, j+1).getTableCellRendererComponent(ui.getBodySplitPane().getLeftValueSP().getBodyTable(), mapValue.get(contField[j].toUpperCase()), false, false, i, j);
					}
				}
			}
		}
		
//		if(listValue!=null && listValue.size()>0){
//
//			for(int i = 0;i<listValue.size();i++){
//				HashMap mapValue = (HashMap)listValue.get(i);
//				ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().addLine();
//				for(int j = 0;j<contField.length;j++){
//					ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
//				}
//			}
//		}
//
//		ui.getBodySplitPane().getLeftValueSP().getBodyBillModel().setEditRow(1);
	}
	/**
	 * ��������
	 * */
	@Override
	protected void onBoExport() throws Exception {

//		super.onBoExport();

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return;
		}
		String fpk=(String) node.getParentnodeID();
		if(SJUtil.isNull(fpk)||fpk.length()==0){
			this.getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�ɱ༭��");
			return;
		}

		AskDLG askDlg=new AskDLG(getSelfUI(),"����","ѡ�����ݶ���ά����������?",new String[]{"������ǰ��ѯ��Χ��","�����ñ�ȫ������"});

		askDlg.showModal();
		int ret=askDlg.getRes();
		if(ret<0){
			return;
		}
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(jfileChooser.SAVE_DIALOG);
		if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path=null;
		path = jfileChooser.getSelectedFile().toString();
		DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
		if(contdataVO==null){
			getSelfUI().showErrorMessage("û���ҵ����ݶ��ն��壡");
			return;
		}
		DipDatadefinitBVO dzbvo1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contdataVO.getContcolcode());
		DipDatadefinitBVO dzbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dzbvo1.getQuotetable());
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, dzbvo.getPk_datadefinit_h());
//		DipDatadefinitBVO bvo=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, strWhere)
		DipDesignVO[] cvos=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, "pk_datadefinit_h='"+dzbvo.getPk_datadefinit_h()+"' and designtype=3 order by disno");
		DipDesignVO[] evos=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, "pk_datadefinit_h='"+contdataVO.getExtetabcode()+"' and designtype=3 order by disno");
		if(cvos==null||cvos.length<=0||evos==null||evos.length<=0){
			getSelfUI().showErrorMessage("�붨�嵼����ʽ��");
			return;
		}
		//���ս���洢��
		String tablename = contdataVO.getMiddletab();
		//������ϵͳ����
		String extetablecode = contdataVO.getExtetabname();
		//����ϵͳ����
//		String conttablecode = contdataVO.getContabname();
		String conttablecode = hvo.getMemorytable();
		String contdatac=dzbvo.getEname();
//		�ҳ�����Ҫ����ֶ�
		String c="";//����ϵͳ�������ֶ�
		String e="";//������ϵͳ�����ֶ�
		String cc="";//����ϵͳ�����ֶε�����
		String ec="";//������ϵͳ�����ֶε�����
		String fileallc="";//����ϵͳ�����ֶεı���
		String filealle="";//������ϵͳ�����ֶεı���
		String ck="";//����ϵͳ�����ֶε����пհ���
		String ek="";//������ϵͳ�����ֶε����пհ���
		for(DipDesignVO bvoc:cvos){
			c=c+"t2."+bvoc.getEname()+" t2"+bvoc.getEname()+",";
			fileallc=fileallc+"t2"+bvoc.getEname()+",";
			cc=cc+bvoc.getCname()+",";
			ck=ck+" null t2"+bvoc.getEname()+",";
		}
		for(DipDesignVO bvoc:evos){
			e=e+"t1."+bvoc.getEname()+" t1"+bvoc.getEname()+",";
			filealle=filealle+"t1"+bvoc.getEname()+",";
			ec=ec+bvoc.getCname()+",";
			ek=ek+" null t1"+bvoc.getEname()+",";
		}
		String condsql=tj;
		String sql="";
		if(askDlg.getRes()==0){			
			sql = " select "+c+""+e.substring(0,e.length()-1)+
			" from "+tablename+" t inner join "+conttablecode+" t2 on t.contpk=t2."+contdatac+" inner join "+extetablecode+" t1" +
			" on t.extepk=t1."+contdataVO.getExtecolname()+" "+condsql
			+" union all  select "+ck+e.substring(0,e.length()-1)+" from "+contdataVO.getExtetabname() +" t1 where "+contdataVO.getExtecolname()+" not in (select extepk from "+tablename+")" +
					condsql;
		}else{
			sql = " select "+c+""+e.substring(0,e.length()-1)+
			" from "+tablename+" t inner join "+conttablecode+" t2 on t.contpk=t2."+contdatac+" inner join "+extetablecode+" t1" +
			" on t.extepk=t1."+contdataVO.getExtecolname()+" ";
		}
			if(askDlg.getJcs()!=null){
				String s[]=askDlg.getJcs();
				if(s[0]!=null&&"1".equals(s[0])){
					sql=sql+" ";
				}
				if(s[1]!=null&&"2".equals(s[1])){
					
					String un1=" union  select "+c+ek.substring(0,ek.length()-1)+" from "+conttablecode +" t2 where "+contdatac+" not in (select contpk from "+tablename+")";
					sql=sql+un1;
				}
				if(s[2]!=null&&"3".equals(s[2])){
					String un2=" union  select "+ck+e.substring(0,e.length()-1)+" from "+contdataVO.getExtetabname() +" t1 where "+contdataVO.getExtecolname()+" not in (select extepk from "+tablename+")";
					sql=sql+un2;
				}
			
			
			
			
		}
		try {
			//2011-7-6 ��left join �ĳ��� inner join
//			IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
			String sqls="select count(*) from ( "+sql+" )";
			 String str=queryfield.queryfield(sqls);
			 if(Integer.parseInt(str)>65536){
					getSelfUI().showErrorMessage("����������̫�󣬲����������");
					return;
				}
			 List list = queryfield.queryListMapSingleSql(sql);

			
			ExpExcelVO[] vosW = null;

			if(list!=null && list.size()>0){
				List<ExpExcelVO> listW = new ArrayList<ExpExcelVO>();
				for(int i = 0;i<list.size();i++){
					HashMap map = (HashMap)list.get(i);
					ExpExcelVO	vo = new ExpExcelVO();
					String fieldsAlls [] = (fileallc+(filealle.substring(0,filealle.length()-1))).split(",");

					for(int j = 0;j<fieldsAlls.length;j++){
						vo.setAttributeValue("line"+Integer.valueOf(j+1), map.get(fieldsAlls[j].toUpperCase())==null?"":map.get(fieldsAlls[j].toUpperCase()));
					}
					listW.add(vo);
				}

				vosW = listW.toArray(new ExpExcelVO[0]);
				String filePath = path.endsWith(".xls")?path:(path+".xls");
				File file = new File(filePath);	
				if(!file.exists())
					file.createNewFile();	
				String fielPathTemp = path+"-1.xls";
				String fileName = (cc+(ec.substring(0,ec.length()-1)));
				ExpToExcel toexcle = new ExpToExcel(filePath,"���ݶ��ս��",fileName,vosW,fielPathTemp);

				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������!");
			}else{
				getSelfUI().showErrorMessage("û�з������������ݣ�");
				return;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			getSelfUI().showErrorMessage("��������"+ex.getMessage());
		}


	}

	//���ݶ��ս��ά��
	protected void contResut(int intBtn) throws Exception{
		int bodycount = ui.getBillListPanel().getBodyTable().getSelectedRow();
		HashMap<String, String> tableMap = new HashMap<String, String>();
		DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
		String tablename = contdataVO.getMiddletab();

		DipDatadefinitHVO hvo = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, contdataVO.getContabcode());
		if(hvo==null)
			return ;
		String contTablename = hvo.getMemorytable();
		String contpk = contdataVO.getContcolname();
		//������ϵͳ����
		String bodypk = ui.getBillListPanel().getBodyBillModel().getValueAt(bodycount, exteField)==null?"":ui.getBillListPanel().getBodyBillModel().getValueAt(bodycount, exteField).toString();
		if(bodypk==null||bodypk.length()<=0){
			getSelfUI().showErrorMessage("��ѡ�񱻶������ݣ�");
			return ;
		}
		tableMap.put("bodypk", bodypk);
		tableMap.put("tablename", tablename);//�洢����
		tableMap.put("contField", contsysfield.toString()); //����ϵͳ��ʾ�ֶ�
		tableMap.put("contsysfieldname", contsysfieldname.toString());//����ϵͳ�����ֶ�
		tableMap.put("contTablename", contTablename); //����ϵͳ����
		tableMap.put("contpk", contpk); //����ϵͳ�������ֶ�
		tableMap.put("contFieldvdef", contField);
		ContResultDlg dlg = new ContResultDlg(this.getBillUI(),new UFBoolean(true),tableMap);
		dlg.show();
	}

	/**
	 * �޸�
	 * */
	protected void edit(int intBtn) throws Exception{
		int headcount = ui.getBillListPanel().getHeadTable().getSelectedRow();
		if(headcount==-1){
			getSelfUI().showErrorMessage("��ѡ�񱻶������ݣ�");
			return ;
		}
		String headpk = ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField)==null?"":ui.getBillListPanel().getHeadBillModel().getValueAt(headcount, exteField).toString();
		if(headpk==null||headpk.trim().equals("")){
			this.getSelfUI().showWarningMessage("��ͷ��������Ϊ�գ��������ã�");
			return;
		}
		selectrow=getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		getSelfUI().getBodySplitPane().initialize(false);
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		afterExtQuery(extsql+" and "+exteFieldPk+"='"+headpk+"'");
		getSelfUI().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
		isEditBtn(true);
		getSelfUI().setTreeEnable(false);//.getUITree().setEnabled(false);
		ui.getBodySplitPane().getLeftValueSP().getBodyItem("isselect").setEdit(true);
		ui.getBodySplitPane().getRightPane().getBodyItem("isselect").setEdit(true);
		
	}

	/**
	 * @description У����
	 * @author cl
	 * @date 2011-6-10
	 */
	protected void validateCheck(int intBtn)throws Exception{
		VOTreeNode treeNode=getSelectNode();
		if(SJUtil.isNull(treeNode)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ�!");
			return;
		}
		DipADContdatawhHVO hvo=(DipADContdatawhHVO) treeNode.getData();
		HashMap map=new HashMap();
		map.put("pk", getSelfUI().selectnode);
		map.put("type", "���ݶ���ά��");
		map.put("code", hvo.getWbbm());
		map.put("name", hvo.getSysname());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map);

		dlg.show();
	}

	@Override
	protected void onBoSet() {
		String node=getSelfUI().selectnode;
		if(node==null||node.length()<=0){
			getSelfUI().showErrorMessage("��ѡ��༭�ڵ㣡");
			return;
		}
		String pk=(String) getSelectNode().getParentnodeID();
		if(pk==null||pk.length()<=0){
			getSelfUI().showErrorMessage("���ڵ㲻�ܲ�����");
			return ;
		}else{
			try {
				DipADContdataVO hvo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, node);
				if(hvo==null){
					getSelfUI().showErrorMessage("û���ҵ���Ӧ�����ݶ��ն���");
					return;
				}else{
					DipDatadefinitBVO dzbvo1=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, hvo.getContcolcode());
					DipDatadefinitBVO dzbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dzbvo1.getQuotetable());
					String t1=hvo.getContabcode();
					t1=dzbvo.getPk_datadefinit_h();
					String t2=hvo.getExtetabcode();//��ɫȨ�ޱ���ͷ���ݶ�����������
					String f1=hvo.getContcolcode();//�����ձ��������ݶ�����������
					f1=dzbvo.getPrimaryKey();
					String f2=hvo.getExtecolcode();
					DesignDLG dlg=new DesignDLG(getSelfUI(),new String[]{t2,t1},null,DesignDLG.DATAAUTHORIZEWH);
//					DesignDLG dlg=new DesignDLG(getSelfUI(),new String[]{t2,t1},new String[]{f2,f1});
					dlg.showModal();
				}
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onBoRefresh() throws Exception {

		getBillTreeManageUI().clearBody();
		getBillTreeManageUI().createBillTree(getBillTreeManageUI().getCreateTreeData());
		getBillTreeManageUI().afterInit();
		getBillTreeManageUI().setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);

		//2011-7-4 
		getBillUI().setListHeadData(null);
		getSelfUI().getButtonManager().getButton(IBtnDefine.edit).setEnabled(false);
		getSelfUI().updateButtonUI();
	}

	@Override
	protected void onDataCheck() throws Exception {

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return;
		}
		String fpk=(String) node.getParentnodeID();
		if(SJUtil.isNull(fpk)||fpk.length()==0){
			this.getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�ɱ༭��");
			return;
		}
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("����ת�������Ժ�...");
				dialog.setStartText("����ת�������Ժ�...");
							
				try {
					dialog.start();		
					VOTreeNode node=getSelectNode();
					String pk=(String) node.getNodeID();
					DipADContdataVO cvo=(DipADContdataVO) HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, pk);
					ContDataMapUtilVO contvo=new ContDataMapUtilVO();
					String middletable=cvo.getMiddletab();
					String extecolname=cvo.getExtecolname();
					String exttabname=cvo.getExtetabname();
					String contabname=cvo.getContabname();
					String contcolname=cvo.getContcolname();
					contvo.setMiddletablename(middletable);
					contvo.setExtecolname(extecolname);
					contvo.setExtetabname(exttabname);
					contvo.setContabname(contabname);
					contvo.setContcolname(contcolname);
					ll.clear();
					List list=new ArrayList();
					//SuperVO[] datacheck=HYPubBO_Client.queryByCondition(DataCheckVO.class, "type='���ݶ���ά��' order by code");
			
					//SuperVO[] datacheck1=HYPubBO_Client.queryByCondition(DataverifyHVO.class, "pk_datachange_h='"+pk+"' and nvl(dr,0)=0");
					String ssql="select hh.pk_dataverify_h from dip_dataverify_h hh where hh.pk_datachange_h='"+pk+"' and nvl(dr,0)=0";
					String pk_dataverify_h=queryfield.queryfield(ssql);
					SuperVO[] datacheck1=null;
					if(pk_dataverify_h!=null){
						datacheck1=HYPubBO_Client.queryByCondition(DataverifyBVO.class, "pk_dataverify_h='"+pk_dataverify_h+"' and nvl(dr,0)=0 order by code");	
					}
					if(datacheck1==null||datacheck1.length==0){
						getSelfUI().showErrorMessage("û��ע�����ݶ���ά����У���࣡");
						return;
					}
					for(int i=0;i<datacheck1.length;i++){
						String checkname=((DataverifyBVO)datacheck1[i]).getVdef2();//�������ơ�
						String checkclass=((DataverifyBVO)datacheck1[i]).getName();//������ơ�
						contvo.setCheckname(checkname);
						contvo.setCheckclassname(checkclass);
						Class cls = Class.forName(checkclass);
						Object inst = cls.newInstance();
						if (inst instanceof ICheckPlugin) {
							ICheckPlugin m = (ICheckPlugin) inst;
							CheckMessage cmsg=m.doCheck(contvo);
							if(cmsg.getMap()!=null){
								List list1=(List) cmsg.getMap().get(checkclass);
								if(list1!=null&&list1.size()>0){
									for(int j=0;j<list1.size();j++){
										list.add(list1.get(j));
			
									}
								}	
							}

							if(cmsg.getMessage()!=null&&cmsg.getMessage().length()>0){
								//�����еĴ�����Ϣд�����ݴ�����־�С��������ϸ��־��
								VOTreeNode treenode= (VOTreeNode) node.getParent();
								String sysname= treenode.getUserObject().toString().split(" ")[1];
								//String sysname=node.getData().getAttributeValue("sysname").toString();
								//ContdatawhHVO hvo=(ContdatawhHVO)node.getData();
								String active=(String) node.getData().getAttributeValue("sysname");
								//hvo.getSysname();
								String code=node.getData().getAttributeValue("wbbm").toString();
			
								ILogAndTabMonitorSys ilt=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
								DateprocessVO vo=new DateprocessVO();
								//vo.set
								vo.setActivecode(code);
								vo.setSysname(sysname);
								//vo.setTs(new UFDateTime(new Date()).toString());
								vo.setSdate(new UFDateTime(new Date()).toString());
								vo.setContent(cmsg.getMessage());
								vo.setActivetype("���ݶ���ά��");
								vo.setActive(active);
								vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
								ilt.writToDataLog_RequiresNew(vo);
			
							}
						}
			
					}
			
					if(list!=null&&list.size()>0){
						HashMap<String,String> map=new HashMap<String,String>();
						for(int i=0;i<list.size();i++){
							if(map.get(list.get(i).toString())==null){
								map.put(list.get(i).toString(), "ok");
								ll.add(list.get(i).toString());
							}
						}
						MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "��" +ll.size()+"�������¼��");
					}else{
						MessageDialog.showHintDlg(getSelfUI(), "У����", "û�д����¼��");
					}
				} catch (Throwable er) {
					er.printStackTrace();
				} finally {
					dialog.end();
				}
			}			
		}.start();
	}
	@Override
	protected void onAutoContData() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�����Զ�ά��������"); 
			return;
		}
		
		DipADContdataVO contdataVO = (DipADContdataVO)HYPubBO_Client.queryByPrimaryKey(DipADContdataVO.class, ui.selectnode);
		if(contdataVO==null){
			getSelfUI().showErrorMessage("������Ȩ����ڵ���ɾ����");
			return;
		}
		DipDatadefinitBVO contcolbvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contdataVO.getContcolcode());
		DipDatadefinitBVO quotetableDataDefBvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contcolbvo.getQuotetable());//�����ֶε�bvo��
		String quoteename= quotetableDataDefBvo.getEname()==null?"":quotetableDataDefBvo.getEname();//�����ֶ�����
		String quoteTableName="";//���ñ�����
		DipDatadefinitHVO hvo = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, contdataVO.getExtetabcode());
		String quoteTable="select * from Dip_Datadefinit_h hh where hh.pk_datadefinit_h in( select bb.pk_datadefinit_h from dip_datadefinit_b bb where bb.pk_datadefinit_b='"+contcolbvo.getQuotetable()+"' and nvl(dr,0)=0) and nvl(dr,0)=0 ";
		List listQuoteTableHVO=queryfield.queryVOBySql(quoteTable, new DipDatadefinitHVO());
		if(listQuoteTableHVO!=null&&listQuoteTableHVO.size()>0){
			if(listQuoteTableHVO.get(0)!=null&&listQuoteTableHVO.get(0) instanceof DipDatadefinitHVO){
				 quoteTableName=((DipDatadefinitHVO)listQuoteTableHVO.get(0)).getMemorytable();
			}
		}
		if(quoteename.equals("")){
			getSelfUI().showErrorMessage("�����ֶβ�����");
			return;
		}
		if(quoteTableName.equals("")){
			getSelfUI().showErrorMessage("���ñ�����");
			return;
		}
		
		if(quoteTableName.equals("bd_corp".toUpperCase())&&quoteename.equals("pk_corp".toUpperCase())){
			
		}else if(quoteTableName.equals("sm_user".toUpperCase())&&quoteename.equals("cuserid".toUpperCase())){
			
		}else{
			getSelfUI().showErrorMessage("�Զ�ά��������Ȩ�޶������ñ������bd_corp����sm_user,�Զ�ά������ʧ��");
			return;
		}
		String quoteCountData="select count ("+quoteename+") from "+ quoteTableName +" where nvl(dr,0)=0";
		List listCountData=queryfield.queryfieldList(quoteCountData);
		if(listCountData==null||listCountData.size()<=0||listCountData.get(0)==null||Integer.parseInt(listCountData.get(0).toString())<=0){
			getSelfUI().showErrorMessage("��ѯ���ñ�����Զ�ά������ʧ��");
			return;
		}
		
		UIRefPane refpanel=new UIRefPane();
		nc.ui.bd.ref.CorpRoleAuthRefModel  refmodel=new CorpRoleAuthRefModel();
		refmodel.addWherePart(" and sm_role.pk_role in ((select pk_fp from dip_adcontdata_b where nvl(dr,0)=0 and pk_contdata_h='"+ui.selectnode+"' )) ");
		refpanel.setRefModel(refmodel);
		refpanel.connEtoC1();
		String pk_role=refpanel.getRefValue("sm_role.pk_role")==null?"":refpanel.getRefValue("sm_role.pk_role").toString();
		if(pk_role.equals("")){
			return;
		}
		DipDatadefinitBVO hvocon=(DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, contdataVO.getContabcode());//���ձ�����Ҫ��ʾ������
		
//		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
//		String where=" and pk_role_corp_alloc in (select pk_role_corp_alloc from v_dip_corproleauth where pk_role in ((select pk_fp from dip_adcontdata_b where nvl(dr,0)=0 and pk_contdata_h='"+ui.selectnode+"' and rownum=1 ))) ";
		String where=" and pk_role_corp_alloc in (select pk_role_corp_alloc from v_dip_corproleauth where pk_role='"+pk_role+"')";
		String sqlrolecount="select count(*) from "+hvo.getMemorytable()+ " where 1=1 "+where;
		List lis=null;
		try{
			lis=queryfield.queryfieldList(sqlrolecount);
		}catch(SQLException ex){
			ex.printStackTrace();
			if(ex.getSQLState().equals("42000")){
				if(ex.getErrorCode()==207){
					String msg=ex.getMessage().substring(12);
					throw new Exception(msg.substring(0,msg.indexOf("\""))+" �ֶ��ڱ��в�����");
				}else if(ex.getErrorCode()==936){
					throw new Exception("��ѯ�����������飡");
				}else if(ex.getErrorCode()==208){
					throw new Exception("�����ͼ������");
				}else{
					throw ex;
				}
			}else {
				throw ex;
			}
		}
		
		if(lis==null||lis.size()<=0||lis.get(0)==null||Integer.parseInt(lis.get(0).toString())<=0){
			getSelfUI().showErrorMessage("�����ձ���û�����ݣ��Զ�ά������ʧ�ܣ�");
			return ;
		}else{
			String midtable=contdataVO.getMiddletab();
			if(midtable==null||midtable.trim().equals("")){
				getSelfUI().showErrorMessage("�м����Ϊ�գ�");
				return ;
			}
//			String sqlvalue=" select pk_role_corp_alloc, pk_corp "+
//			  				" from V_DIP_CORPROLEAUTH "+where+
//			  				" and pk_corp in (select "+quoteename+" from "+quoteTableName+" where nvl(dr, 0) = 0) ";
			String sqlvalue="";
			//quoteename.equals("pk_corp")&&quoteTableName.equals("bd_corp")
			if(quoteTableName.equals("sm_user".toUpperCase())&&quoteename.equals("cuserid".toUpperCase())){
				sqlvalue=" select a.pk_role_corp_alloc, a. "+quoteename+
				" from (" +
				" select v.pk_role_corp_alloc, r."+quoteename+
				" from V_DIP_CORPROLEAUTH v,sm_user_role r where 1=1 "+where+
				" and  v.pk_role = r.pk_role and v.pk_corp = r.pk_corp "+
				" ) a"+
				" where not exists (select * "+
				"  from "+midtable+" b "+
				"  where b.contpk = a."+quoteename+
				"  and b.extepk = a.pk_role_corp_alloc )";
			}else{
				sqlvalue=" select a.pk_role_corp_alloc, a.pk_corp "+
				" from (" +
				" select pk_role_corp_alloc, pk_corp "+
				" from V_DIP_CORPROLEAUTH  where 1=1 "+where+
				" and pk_corp in (select "+quoteename+" from "+quoteTableName+" where nvl(dr, 0) = 0)"+
				" ) a"+
				" where not exists (select * "+
				"  from "+midtable+" b "+
				"  where b.contpk = a.pk_corp "+
				"  and b.extepk = a.pk_role_corp_alloc )";
			}
			List list=queryfield.queryListMapSingleSql(sqlvalue);
			if(list!=null&&list.size()>0){
				int count=0;
				for(int i=0;i<list.size();i++){
					if(list.get(i)!=null&&list.get(i) instanceof Map){
						Map map=(Map) list.get(i);
						if(map!=null&&map.get(quoteename.toUpperCase())!=null&&map.get("pk_role_corp_alloc".toUpperCase())!=null){
							String sql = "insert into "+midtable+" (pk,contpk,extepk) values('"+queryfield.getOID()+"','"+map.get(quoteename.toUpperCase())+"','"+map.get("pk_role_corp_alloc".toUpperCase())+"')";
							queryfield.exesql(sql);
							count++;
						}
						
					}
				}
				MessageDialog.showHintDlg(getSelfUI(), "�ɹ�", "�Զ�ά��"+count+"��������Ϣ���Զ�ά�����ݳɹ���");
//				getSelfUI().showWarningMessage("�Զ�ά��"+count+"��������Ϣ���Զ�ά�����ݳɹ���");
				
			}else{
				MessageDialog.showHintDlg(getSelfUI(), "�ɹ�", "������û��ά������");
				getSelfUI().showHintMessage("������û��ά������");
			}
		}
		super.onAutoContData();
	}
	
	
	public void isEditBtn(boolean isEdit){
		getButtonManager().getButton(IBtnDefine.CONTSAVE).setEnabled(isEdit);
		getButtonManager().getButton(IBillButton.Cancel).setEnabled(isEdit);
		getButtonManager().getButton(IBtnDefine.CONTSYSQUERY).setEnabled(isEdit);
		getButtonManager().getButton(IBtnDefine.BCONTSYSQUERY).setEnabled(!isEdit);
		getButtonManager().getButton(IBtnDefine.edit).setEnabled(!isEdit);
		getButtonManager().getButton(IBillButton.Refresh).setEnabled(!isEdit);
		getButtonManager().getButton(IBillButton.ImportBill).setEnabled(!isEdit);
		getButtonManager().getButton(IBillButton.ExportBill).setEnabled(!isEdit);
		getButtonManager().getButton(IBtnDefine.VALIDATECHECK).setEnabled(!isEdit);
		getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(!isEdit);
		getButtonManager().getButton(IBtnDefine.SET).setEnabled(!isEdit);
		getButtonManager().getButton(IBtnDefine.DATACHECK).setEnabled(!isEdit);
		getButtonManager().getButton(IBtnDefine.FAST_SET).setEnabled(!isEdit);
		
		try {
			getSelfUI().updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}