package nc.ui.dip.contwhquery;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dip.util.ClientEvnUtilVO;
import nc.vo.dip.util.QueryUtilVO;



/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 @SuppressWarnings("serial")
public class ContWHQueryClientUI extends AbstractBdBillCardUI{
	 HashMap<String, String> fieldmap=new HashMap<String, String>();
	 HashMap tableMap = null;
	 public String returnSql = "";
	 String key="";
	 String pk_h;
	public ContWHQueryClientUI() {

	}
	
	
	 public ContWHQueryClientUI(HashMap  map){
		 tableMap = map;
		 try {
			setDefaultData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	public void onButtonAction(ButtonObject bo)throws Exception
{
    ((MyEventHandler)this.getCardEventHandler()).onButtonAction(bo);
   
}
	protected CardEventHandler createEventHandler()
    {
        return new MyEventHandler(this, getUIControl());
    }


	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new ContWHQueryClientUICtrl();
	}


	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setDefaultData() throws Exception {
		
		String pk_contdata_h = tableMap.get("pk_contdata_h")==null?"":tableMap.get("pk_contdata_h").toString();
		boolean isad=tableMap.containsKey("isad")?true:false;
		key=pk_contdata_h;
		String keyS = tableMap.get("keyS")==null?"":tableMap.get("keyS").toString();
		if("".equals(tableMap.get("pk_contdata_h"))){
			
		}else{

			String sql ="";
			IUAPQueryBS querybs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//			DipDesignVO[] listVO  = null;
			String pk_datadefint_h="";
			//被对照系统
			if("0".equals(keyS)){
				sql="select extetabcode from "+(isad?"dip_adcontdata":"dip_contdata")+" where pk_contdata_h='"+pk_contdata_h+"'";
//				listVO=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, "pk_datadefinit_h=(select extetabcode from "+(isad?"dip_adcontdata":"dip_contdata")+" where pk_contdata_h='"+pk_contdata_h+"') and designtype=1");
//				
//				sql = "select ename,cname from dip_datadefinit_b " +
//				" where dip_datadefinit_b.pk_datadefinit_b in " +
//				" (select extesysfield from dip_contdata_b2 where dip_contdata_b2.pk_contdata_h='"+pk_contdata_h+"' and nvl(dr,0)=0) and nvl(dr,0) = 0 ";
//				listVO = (ArrayList<DipDesignVO>)querybs.executeQuery(sql, new BeanListProcessor(DipDesignVO.class));
			}
			//对照系统
			if("1".equals(keyS)){
				String swhere="";
				if(isad){
					sql="select pk_datadefinit_h from dip_datadefinit_b where pk_datadefinit_b=( select quotetable from dip_datadefinit_b where pk_datadefinit_b= (select contcolcode from dip_adcontdata  where pk_contdata_h='"+pk_contdata_h+"'))";
					swhere="pk_datadefinit_h=(select pk_datadefinit_h from dip_datadefinit_b where pk_datadefinit_b=( select quotetable from dip_datadefinit_b where pk_datadefinit_b= (select contcolcode from dip_adcontdata  where pk_contdata_h='"+pk_contdata_h+"'))) and designtype=1";
				}else {
					sql="select contabcode from dip_contdata where pk_contdata_h='"+pk_contdata_h+"'";
					swhere="pk_datadefinit_h=(select contabcode from dip_contdata where pk_contdata_h='"+pk_contdata_h+"') and designtype=1";
				}
//				listVO=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, swhere);
					
//				sql = "select ename,cname from dip_datadefinit_b " +
//				" where dip_datadefinit_b.pk_datadefinit_b in " +
//				" (select sysfield from dip_contdata_b where dip_contdata_b.pk_contdata_h='"+pk_contdata_h+"' and nvl(dr,0)=0) and nvl(dr,0) = 0 ";
//				listVO = (ArrayList<DipDatadefinitBVO>)querybs.executeQuery(sql, new BeanListProcessor(DipDatadefinitBVO.class));
			}
			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
			pk_datadefint_h=iqf.queryfield(sql);
			pk_h=pk_datadefint_h;
			if(pk_datadefint_h!=null&&pk_datadefint_h.length()>0){
				ClientEvnUtilVO evo=ClientEnvDef.getQueryMap(pk_datadefint_h);
				if(evo!=null&&evo.getCnames()!=null&&evo.getCnames().length>0){
					QueryUtilVO[] uvo=evo.getVos();
					fieldmap=(HashMap<String, String>) evo.getMap();
					getBillCardPanel().getBillModel().clearBodyData();	
					for(int i=0;i<uvo.length;i++){
						getBillCardPanel().getBillModel().addLine();
						
						getBillCardPanel().getBillModel().setBodyRowVO(uvo[i], i);//.setValueAt(uvo[i].getCname(),i, "cname");
//						getBillCardPanel().getBillModel().setValueAt(uvo[i].getEname(),i, "ename");
//						getBillCardPanel().getBillModel().setValueAt(uvo[i].getCztype(),i, "cztype");
//						getBillCardPanel().getBillModel().setValueAt(uvo[i].getGetvalue(),i, "getvalue");
//						getBillCardPanel().getBillModel().setValueAt(uvo[i].getGetvalue(),i, "getvalue");
//						getBillCardPanel().getBillModel().setValueAt(uvo[i].getGetvalue(),i, "getvalue");
						getBillCardPanel().getBillModel().setCellEditable(i, "cztype", true);
						getBillCardPanel().getBillModel().setCellEditable(i, "getvalue", true);
						getBillCardPanel().getBillModel().setCellEditable(i, "cname", true);
						getBillCardPanel().getBillModel().setCellEditable(i, "kh", true);
						getBillCardPanel().getBillModel().setCellEditable(i, "ope", true);
					}
					UIComboBox m=(UIComboBox) getBillCardPanel().getBodyItem("cname").getComponent();
					ComboBoxModel jComboBox2Model = 
						new DefaultComboBoxModel(
								evo.getCnames());
					m.setModel(jComboBox2Model);
				}
			}
			
//			liyunzhe 给查询模板增加缓存
			if(MyEventHandler.map!=null){
				int count=getBillCardPanel().getRowCount();
				for(int kk=0;kk<count;kk++){
					String ename=getBillCardPanel().getBillModel().getValueAt(kk, "ename")==null?"":getBillCardPanel().getBillModel().getValueAt(kk, "ename").toString();
					if(!ename.equals("")){
					 Map map=MyEventHandler.map.get(ename);
					 if(map!=null){
//						0 kh,1 cztype,2 getvalue,3 ope
						 getBillCardPanel().getBillModel().setValueAt(map.get("kh"), kk, "kh") ;
						 getBillCardPanel().getBillModel().setValueAt(map.get("cztype"), kk, "cztype") ;
						 getBillCardPanel().getBillModel().setValueAt(map.get("getvalue"), kk, "getvalue") ;
						 getBillCardPanel().getBillModel().setValueAt(map.get("ope"), kk, "ope") ;
					 }
					}
//					if(list.get(kk)!=null&&list.get(kk)[0].equals(bvo.getEname())){
//						
//						getBillCardPanel().getBillModel().setValueAt(list.get(kk)[1]==null?"":list.get(kk)[1],i, "cztype");
//						getBillCardPanel().getBillModel().setValueAt(list.get(kk)[2]==null?"":list.get(kk)[2],i, "getvalue");
//					}
				}
			}
			
			
			/*if(listVO!=null && listVO.length>0){
				getBillCardPanel().getBillModel().clearBodyData();	//清除数据
				String[] field=new String[listVO.length+1];
				for(int i = 0;i<listVO.length;i++){
					DipDesignVO bvo = listVO[i];
					getBillCardPanel().getBillModel().addLine();
					getBillCardPanel().getBillModel().setValueAt(bvo.getCname(),i, "cname");
					getBillCardPanel().getBillModel().setValueAt(bvo.getEname(),i, "ename");
					getBillCardPanel().getBillModel().setCellEditable(i, "cztype", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "getvalue", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "cname", true);
					fieldmap.put(bvo.getCname(), bvo.getEname());
					field[i+1]=bvo.getCname();
					
					
					//liyunzhe 给查询模板增加缓存
					if(MyEventHandler.map.get(key)!=null){
						List<String[]> list=MyEventHandler.map.get(key);
						   
						for(int kk=0;kk<list.size();kk++){
							if(list.get(kk)!=null&&list.get(kk)[0].equals(bvo.getEname())){
								getBillCardPanel().getBillModel().setValueAt(list.get(kk)[1]==null?"":list.get(kk)[1],i, "cztype");
								getBillCardPanel().getBillModel().setValueAt(list.get(kk)[2]==null?"":list.get(kk)[2],i, "getvalue");
							}
						}
					}
					
				}
				UIComboBox m=(UIComboBox) getBillCardPanel().getBodyItem("cname").getComponent();
				ComboBoxModel jComboBox2Model = 
					new DefaultComboBoxModel(
							field);
				m.setModel(jComboBox2Model);
			}else{
				getBillCardPanel().getBillModel().clearBodyData();
			}*/
		
			
			
			}
		this.setBillOperate(2);
		getBillCardPanel().getBillTable().setSortEnabled(false);
	}
	public void bodyRowChange(BillEditEvent arg0) {
		boolean save,cancel;
		save=this.getButtonManager().getButton(nc.ui.trade.button.IBillButton.Save).isEnabled();
		cancel=this.getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).isEnabled();
		if((this.getBillCardPanel().getRowCount()>0) & (save==false & cancel==true)){
			try {
				((MyEventHandler)this.getCardEventHandler()).onBoEdit();
			} catch (Exception e) {
				//		 TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	} 
	
	@Override
	public boolean onClosing() {
		// TODO Auto-generated method stub
//		return super.onClosing();
		
		return true;
	}


	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		//0 kh,1 cztype,2 getvalue,3 ope
		String ename=getBillCardPanel().getBodyValueAt(e.getRow(), "ename")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "ename").toString().trim();
		Map map=MyEventHandler.map.get(ename);
		if(map==null){
			map=new HashMap<String,String>();
		}
		if(e.getKey().equals("cztype")){
//				getBillCardPanel().setBodyValueAt(null, e.getRow(), "getvalue");
				String value=getBillCardPanel().getBodyValueAt(e.getRow(), "cztype")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "cztype").toString();
					map.put("cztype", value);
					MyEventHandler.map.put(ename, map);
				
		}else if(e.getKey().equals("cname")){
			String ename1=fieldmap.get(e.getValue());
			getBillCardPanel().setBodyValueAt(ename1, e.getRow(), "ename");
			if(MyEventHandler.map.get(ename1)!=null){
				getBillCardPanel().setBodyValueAt(MyEventHandler.map.get(ename1).get("kh"), e.getRow(), "kh");
				getBillCardPanel().setBodyValueAt(MyEventHandler.map.get(ename1).get("cztype"), e.getRow(), "cztype");
				getBillCardPanel().setBodyValueAt(MyEventHandler.map.get(ename1).get("getvalue"), e.getRow(), "getvalue");
				getBillCardPanel().setBodyValueAt(MyEventHandler.map.get(ename1).get("ope"), e.getRow(), "ope");
			}else{
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "cztype");
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "getvalue");	
			}
			
		}else if(e.getKey().equals("getvalue")){
			String value=getBillCardPanel().getBodyValueAt(e.getRow(), "getvalue")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "getvalue").toString();
			map.put("getvalue", value);
			MyEventHandler.map.put(ename, map);
		}else if(e.getKey().equals("kh")){
			String value=getBillCardPanel().getBodyValueAt(e.getRow(), "kh")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "kh").toString();
				map.put("kh", value);
				MyEventHandler.map.put(ename, map);
		}else if(e.getKey().equals("ope")){
			String value=getBillCardPanel().getBodyValueAt(e.getRow(), "ope")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "ope").toString();
			map.put("ope", value);
			MyEventHandler.map.put(ename, map);
		}
	}
}
