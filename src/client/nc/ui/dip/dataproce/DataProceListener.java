package nc.ui.dip.dataproce;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;

public class DataProceListener implements ActionListener{
	private DataProceUI dataui;
	private DipDatadefinitBVO[] datedefvo;
	
	public DataProceListener(DataProceUI dataui,DipDatadefinitBVO[] datedefvo){
		this.dataui=dataui;
		this.datedefvo=datedefvo;
	}

	public void actionPerformed(ActionEvent arg0) {

		JGFormuDefUI defui=new JGFormuDefUI(dataui);
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
			UIRefPane ref1=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procetype").getComponent());//加工类型
			if(ref1.getText()!=null&&"数据清洗".equals(ref1.getText().toString())){
				UIRefPane ref2 =(UIRefPane)(dataui.getBillCardPanel().getHeadItem("firstdata").getComponent());//原始数据
				 String table=ref2.getText();
					UIRefPane ref3=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("refprocecond").getComponent());//加工条件
//					UIRefPane ref4=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procecond").getComponent());//加工条件
					String where=defui.getFormulaTArea().getText();
					String sql="delete from "+table+" where "+where;
//				    System.out.println("sql="+sql);
					ref3.setText(sql);
					dataui.getBillCardPanel().getHeadItem("procecond").setValue(sql);//.setText(sql);
			}
			if(ref1.getText()!=null&&"数据转换".equals(ref1.getText().toString())){
				UIRefPane ref2 =(UIRefPane)(dataui.getBillCardPanel().getHeadItem("firstdata").getComponent());//原始数据
				String newtable=null;
				if(dataui.getBillCardPanel().getHeadItem("procetab").getComponent()!=null){
					UIRefPane ref3 =(UIRefPane)dataui.getBillCardPanel().getHeadItem("procetab").getComponent();
					newtable=ref3.getText();
				}
				String table=ref2.getText();
					UIRefPane ref3=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("refprocecond").getComponent());//加工条件
//					UIRefPane ref4=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procecond").getComponent());//加工条件
					String where=defui.getFormulaTArea().getText();
					String sql=null;
					if(newtable!=null){
						sql="insert into  "+newtable+" select * from "+table+" where "+where;
					}else{
						sql=where;
					}
					
					//insert into newtable select * from table where 
//				    System.out.println("sql="+sql);
					ref3.setText(sql);
					dataui.getBillCardPanel().getHeadItem("procecond").setValue(sql);
			}
			
			
			if(ref1.getText()!=null&&"数据预置".equals(ref1.getText().toString())){
				UIRefPane ref2 =(UIRefPane)(dataui.getBillCardPanel().getHeadItem("firstdata").getComponent());//原始数据
				String table=ref2.getText();
				UIRefPane ref3=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("refprocecond").getComponent());//加工条件
//				UIRefPane ref4=(UIRefPane)(dataui.getBillCardPanel().getHeadItem("procecond").getComponent());//加工条件
				String set=defui.getFormulaTArea().getText();
				String sql="update " + table+" set "+ set;
				ref3.setText(sql);
				dataui.getBillCardPanel().getHeadItem("procecond").setValue(sql);
			}
			defui.flag=0;
		}
		
		
	}
	
	
	

}
