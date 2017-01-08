package nc.ui.dip.credence;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;

public class FzhsListener implements ActionListener {
	public static final int TYPE_HEAD=0;
	public static final int TYPE_BODY=1;
	public static final int TYPE_TAIL=2;
	

	FzhsDlg billUI;

	String rskey = "";

	String deffilename = "";

	String savefieldname = "";
	UIRefPane rf;

	String pk_credence_h;
	String pk_datadef;

	/**
	 * @param ui
	 *            当前界面的ui
	 * @param deffilename
	 *            界面上显示的配置伪参照字段的那个字段名
	 * @param savefieldame
	 *            实际数据库保存字段的字段名
	 */
	public FzhsListener(Container ui, String deffilename,
			String savefieldname, UIRefPane rf,String pk_credence_h,String pk_datadef) {
		this.pk_credence_h=pk_credence_h;
		this.pk_datadef=pk_datadef;
		this.billUI = (FzhsDlg) ui;
		this.deffilename = deffilename;
		this.savefieldname = savefieldname;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
		int row=billUI.getUIScrollPane().getBillTable().getSelectedRow();
		/*List list=billUI.list;
		Map map=(Map) list.get(row);
		String tablename=map.get("tablename".toUpperCase()).toString();
		String sql=" select column_name,data_type from user_tab_cols where table_name='"+tablename+"'";
		List<Map> va=((IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName())).queryListMapSingleSql(sql);
		DefitemVO[] defvo;
		int i=0;
		if(!SJUtil.isNull(va)&&va.size()>0){
			for (Map bvoi : va) {
				defvo[i] = new DefitemVO();
				defvo[i].setAttrname(bvoi.get("column_name".toUpperCase()).toString());
				//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(false));
				defvo[i].setItemname(bvoi.get("column_name".toUpperCase()).toString());
				defvo[i].setPrimaryKey(bvoi.get("column_name".toUpperCase()).toString());
				// TODO wyd 这个type值，要编辑
				defvo[i].setItemtype(0);
				i++;
			}
		}*/
		DipDatadefinitBVO[] bvos = null;
		DipDatadefinitBVO[] hvos=null;
		String pk_sys="";
		//表头标志
		try {
				DipDatadefinitHVO hvo=(DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class,pk_datadef);
				if(hvo!=null){
					    hvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk_datadef+"'");
					    pk_sys=hvo.getPk_sysregister_h();
				}
//			}
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
		dlg.setFormula(billUI.getUIScrollPane().getBodyValueAt(row, "gs")==null?"":billUI.getUIScrollPane().getBodyValueAt(row, "gs").toString());
		dlg.setBillItems(defvo);
		dlg.showModal();
		String tmpString = dlg.getFormula();
		rskey = tmpString;
				billUI.getUIScrollPane().setBodyValueAt(rskey, row, "gs");//.getBodyValueAt(savefieldname).setValue(rskey);
				rf.setText(rskey);
		dlg.destroy();

	}

}