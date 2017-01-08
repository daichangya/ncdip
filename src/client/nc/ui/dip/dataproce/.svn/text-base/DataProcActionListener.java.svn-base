package nc.ui.dip.dataproce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import nc.ui.dip.procondition.ProconditionDlg;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.processstyle.ProcessstyleVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * 数据加工的监听类
 * */
public class DataProcActionListener implements ActionListener {
	DataProceUI ui;
	Map<String,ProcessstyleVO> map=new HashMap<String, ProcessstyleVO>();
	BillItem item;
	public DataProcActionListener(DataProceUI ui,BillItem item){
		this.item=item;
		this.ui=ui;
		try {
			ProcessstyleVO[] vo=(ProcessstyleVO[]) HYPubBO_Client.queryByCondition(ProcessstyleVO.class, "nvl(dr,0)=0");
			if(vo!=null&&vo.length>0){
				for(ProcessstyleVO voi:vo){
					map.put(voi.getPrimaryKey(), voi);
				}
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void actionPerformed(ActionEvent e) {
		String item=ui.getBillCardPanel().getHeadItem("def_str_2").getValueObject()==null?null:ui.getBillCardPanel().getHeadItem("def_str_2").getValueObject().toString();
		/*if(ui.getBillCardPanel().getHeadItem("firsttab").getValueObject()==null||ui.getBillCardPanel().getHeadItem("firsttab").getValueObject().toString().length()<=0){
			return;
		}*/
		if(item==null){
			return;
		}else{
			String str=map==null?null:(map.get(item)==null?null:((ProcessstyleVO)map.get(item)).getName());
			if(str==null){
				return;
			}else{
				Object pk=ui.getBillCardPanel().getHeadItem("firsttab").getValueObject();
				if("数据清洗".equals(str)||"数据转换".equals(str)||"数据预置".equals(str)){ 
					DipDatadefinitBVO[] datedefvo=null;
					if(!SJUtil.isNull(pk)){

						try {
							String sql="pk_datadefinit_h='"+pk.toString()+"'";
							datedefvo	=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, sql);

						} catch (UifException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}


					JGFormuDefUI defui=new JGFormuDefUI(ui);
					UIScrollPane ivjFieldScrollPane=defui.getFieldScrollPane();
					DefitemVO[] defvo = new DefitemVO[SJUtil.isNull(datedefvo)?0:datedefvo.length];
					if(!SJUtil.isNull(datedefvo)){
						for(int i=0;i<datedefvo.length;i++){
							defvo[i] = new DefitemVO();
							defvo[i].setAttrname(datedefvo[i].getEname());
							//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
							defvo[i].setHeadflag(new UFBoolean(true));
							defvo[i].setItemname(datedefvo[i].getCname());
							defvo[i].setPrimaryKey(datedefvo[i].getPrimaryKey());
							// TODO wyd 这个type值，要编辑
							defvo[i].setItemtype(0);
						}
					}
					defui.setBillItems(defvo);

					defui.showModal();
					if(defui.flag==1){
						UIRefPane ref1=(UIRefPane)(ui.getBillCardPanel().getHeadItem("procetype").getComponent());//加工类型
						if(ref1.getText()!=null&&"数据清洗".equals(ref1.getText().toString())){
							UIRefPane ref2 =(UIRefPane)(ui.getBillCardPanel().getHeadItem("firstdata").getComponent());//原始数据
							 String table=ref2.getText();
								UIRefPane ref3=(UIRefPane)(ui.getBillCardPanel().getHeadItem("refprocecond").getComponent());//加工条件
//								UIRefPane ref4=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procecond").getComponent());//加工条件
								String where=defui.getFormulaTArea().getText();
								String sql="delete from "+table+" where "+where;
//							    System.out.println("sql="+sql);
								ref3.setText(sql);
								ui.getBillCardPanel().getHeadItem("procecond").setValue(sql);//.setText(sql);
						}
						if(ref1.getText()!=null&&"数据转换".equals(ref1.getText().toString())){
							UIRefPane ref2 =(UIRefPane)(ui.getBillCardPanel().getHeadItem("firstdata").getComponent());//原始数据
							String newtable=null;
							if(ui.getBillCardPanel().getHeadItem("procetab").getComponent()!=null){
								UIRefPane ref3 =(UIRefPane)ui.getBillCardPanel().getHeadItem("procetab").getComponent();
								newtable=ref3.getText();
							}
							String table=ref2.getText();
								UIRefPane ref3=(UIRefPane)(ui.getBillCardPanel().getHeadItem("refprocecond").getComponent());//加工条件
//								UIRefPane ref4=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procecond").getComponent());//加工条件
								String where=defui.getFormulaTArea().getText();
								String sql=null;
								if(newtable!=null){
									sql="insert into  "+newtable+" select * from "+table+" where "+where;
								}else{
									sql=where;
								}
								
								//insert into newtable select * from table where 
//							    System.out.println("sql="+sql);
								ref3.setText(sql);
								ui.getBillCardPanel().getHeadItem("procecond").setValue(sql);
						}
						
						
						if(ref1.getText()!=null&&"数据预置".equals(ref1.getText().toString())){
							UIRefPane ref2 =(UIRefPane)(ui.getBillCardPanel().getHeadItem("firstdata").getComponent());//原始数据
							String table=ref2.getText();
							UIRefPane ref3=(UIRefPane)(ui.getBillCardPanel().getHeadItem("refprocecond").getComponent());//加工条件
//							UIRefPane ref4=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procecond").getComponent());//加工条件
							String set=defui.getFormulaTArea().getText();
							String sql="update " + table+" set "+ set;
							ref3.setText(sql);
							ui.getBillCardPanel().getHeadItem("procecond").setValue(sql);
						}
						defui.flag=0;
					}
					
				
				}else if("自定义".equals(str)){
					ui.transferFocus();
					ProFormuDefUI dlg = new ProFormuDefUI(ui,ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString());
//					String ss=ui.getLob()==null?"":ui.getLob().toString();
					dlg.setFormula(ui.getBillCardPanel().getHeadItem("procecond").getValueObject().toString());
//					dlg.setFormula(ss);
//					DipDatadefinitBVO[] hvos=null;
//					try {
//						hvos = (DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+ui.getBillCardPanel().getHeadItem("firsttab").getValueObject().toString()+"'");
//					} catch (UifException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					DefitemVO[] defvo = new DefitemVO[SJUtil.isNull(hvos)?0:hvos.length];
//					int i = 0;
//					if(!SJUtil.isNull(hvos)){
//						for (DipDatadefinitBVO bvoi :hvos) {
//							defvo[i] = new DefitemVO();
//							defvo[i].setAttrname(bvoi.getEname());
//							//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
//							defvo[i].setHeadflag(new UFBoolean(true));
//							defvo[i].setItemname(bvoi.getCname());
//							defvo[i].setPrimaryKey(bvoi.getPrimaryKey());
//							// TODO wyd 这个type值，要编辑
//							defvo[i].setItemtype(0);
//							i++;
//						}
//					}
//					dlg.setBillItems(defvo);
					dlg.showModal();
					if(dlg.OK==1){
						String tmpString = dlg.getFormula();
						ui.getBillCardPanel().getHeadItem("procecond").setValue(tmpString);
						ui.getBillCardPanel().getHeadItem("refprocecond").setValue("条件保存在下边");
						ui.setLob(tmpString);
//						ui.getBillCardPanel().getHeadItem("proclob").setValue(new CharClob(tmpString.toCharArray()));
					}
				}else{

					
					ui.transferFocus();

					String firsttab = ui.getBillCardPanel().getHeadItem("firsttab").getValueObject()==null ?"":ui.getBillCardPanel().getHeadItem("firsttab").getValueObject().toString();
					
					String procecond = "DIP_BAK_TS";//ui.getBillCardPanel().getHeadItem("procecond").getValueObject()==null ?"":ui.getBillCardPanel().getHeadItem("procecond").getValueObject().toString();
					
//					String procetype = ui.getBillCardPanel().getHeadItem("procetype").getValueObject()==null?"":ui.getBillCardPanel().getHeadItem("procetype").getValueObject().toString(); 
					
					HashMap tableMap = new HashMap();
					tableMap.put("firsttab", firsttab);
					tableMap.put("procecond", procecond);
					tableMap.put("procetype", str);
//					ProconditionClientUI ui = new ProconditionClientUI();
					ProconditionDlg dlg = new ProconditionDlg(ui, new UFBoolean(true), tableMap);
					dlg.show();
					if(dlg.onSave==1){
						String sql = dlg.getReturnSql();
						this.item.setValue(sql);
						this.ui.getBillCardPanel().getHeadItem("procecond").setValue(sql);
						ui.getBillCardPanel().setHeadItem("refprocecond", "条件保存在下边");
						/*BillItem item12 = ui.getBillCardPanel().getHeadItem("refprocecond");//加工条件
						if (item12 != null) {
							UIRefPane ref = (UIRefPane) item12.getComponent();
							if (ref != null) { 
								String sql = dlg.getReturnSql();
								ref.setText(sql);
//								ref.setValue(dlg.getReturnSql());
								ui.getBillCardPanel().getHeadItem("procecond").setValue(sql);
//								billUI.getBillCardPanel().getHeadItem("procecond").setComponent(ref);
							}
						}*/
						dlg.onSave=0;
					}
					
					dlg.destroy();
				}
			}
		}

	
	
	}

}
