package nc.ui.dip.credence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.BusinessException;

public class FzhsCredenceListener implements ActionListener {
	public static final int TYPE_HEAD=0;
	public static final int TYPE_BODY=1;
	public static final int TYPE_TAIL=2;
	

	CredenceClientUI billUI;

	String rskey = "";

	String deffilename = "";

	String savefieldname = "";
	UIRefPane rf;

	String pk_glorgbook;

	/**
	 * @param ui
	 *            当前界面的ui
	 * @param deffilename
	 *            界面上显示的配置伪参照字段的那个字段名
	 * @param savefieldame
	 *            实际数据库保存字段的字段名
	 */
	public FzhsCredenceListener(CredenceClientUI ui, String deffilename,
			String savefieldname, String pk_glorgbook,UIRefPane rf) {
		this.billUI = ui;
		this.deffilename = deffilename;
		this.savefieldname = savefieldname;
		this.pk_glorgbook = pk_glorgbook;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
		int row=billUI.getBillCardPanel().getBillTable().getSelectedRow();
		Object ob=billUI.getBillCardPanel().getBodyValueAt(row, "subjects");
		if(ob==null){
			return;
		}
		String sub=ob.toString();
		String sql=" select bdname,tablename,tablepkname from bd_bdinfo where pk_corp='0001' and refsystem='gl' and pk_bdinfo in "+
 "(select pk_bdinfo from bd_subjass where pk_accsubj=(select pk_accsubj from bd_accsubj where subjcode='"+sub+"' and pk_glorgbook='"+pk_glorgbook+"' ) and dr=0)";
		IQueryField ifq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List list=null;
		try {
			list=ifq.queryListMapSingleSql(sql);
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
		if(list==null||list.size()<=0){
			return;
		}

		String pk_credence_h=billUI.getPk_credence_h();
		String pk_datadef=billUI.getPk_datadef();
		FzhsDlg dlg=new FzhsDlg(list,pk_credence_h,pk_datadef,billUI.getBillCardPanel().getBodyValueAt(row, "assistant"));
		dlg.showModal();
		billUI.getBillCardPanel().setBodyValueAt(dlg.getFO(), row, "def_assistant");
		billUI.getBillCardPanel().setBodyValueAt(dlg.getFO(), row, "assistant");
		/*
		String pk_credence_h=billUI.getPk_credence_h();
		String pk_datadef=billUI.getPk_datadef();
		DipDatadefinitBVO[] bvos = null;
		DipDatadefinitBVO[] hvos=null;
		String pk_sys="";
		//表头标志
//		boolean flag=true;
		try {
				DipDatadefinitHVO hvo=(DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class,pk_datadef);
				if(hvo!=null){
					
					    hvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk_datadef+"'");
					    pk_sys=hvo.getPk_sysregister_h();
				}
		} catch (UifException ex) {
			ex.printStackTrace();
		}
		
		DefitemVO[] defvo = new DefitemVO[(SJUtil.isNull(bvos)?0:bvos.length)+(SJUtil.isNull(hvos)?0:hvos.length)];
		int i = 0;
		if(!SJUtil.isNull(bvos)){
			for (DipDatadefinitBVO bvoi : bvos) {
				defvo[i] = new DefitemVO();
				defvo[i].setAttrname(bvoi.getEname());
				//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(false));
				defvo[i].setItemname(bvoi.getCname());
				defvo[i].setPrimaryKey(bvoi.getPrimaryKey());
				// TODO wyd 这个type值，要编辑
				defvo[i].setItemtype(0);
				i++;
			}
		}
		if(!SJUtil.isNull(hvos)){
			for(DipDatadefinitBVO hvoi:hvos){
				defvo[i] = new DefitemVO();
				defvo[i].setAttrname(hvoi.getEname());
				//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(true));
				defvo[i].setItemname(hvoi.getCname());
				defvo[i].setPrimaryKey(hvoi.getPrimaryKey());
				// TODO wyd 这个type值，要编辑
				defvo[i].setItemtype(0);
				i++;
			}
		}
		DapFormuDefUI dlg = new DapFormuDefUI(billUI,pk_datadef,pk_sys);
			dlg.setFormula(rf.getText());
		dlg.setBillItems(defvo);
		dlg.showModal();
		String tmpString = dlg.getFormula();
		rskey = tmpString;
			int ro1=billUI.getBillCardWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			billUI.getBillCardPanel().setBodyValueAt(rskey, ro1, savefieldname);
			rf.setText(rskey);
		dlg.destroy();*/

	}

}