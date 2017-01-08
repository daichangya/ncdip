package nc.ui.dip.datalookquery;

import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.bd.ref.DipDataRefModel;
import nc.ui.bd.ref.DipDayRefModel;
import nc.ui.bd.ref.DipMonthRefModel;
import nc.ui.bd.ref.DipYearRefModel;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.util.ClientEvnUtilVO;
import nc.vo.dip.util.QueryUtilVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;


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
public class DatalookQueryClientUI extends AbstractBdBillCardUI{
	 HashMap<String, String> fieldmap=new HashMap<String, String>();
	 public Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();//String 是ename，String[] 是 类型，值
	 public String returnSql = "";
	 String key = "";
	 public Boolean isAuth = false;
	 private HashMap<String, DipDatadefinitBVO> dataBMap = new HashMap<String, DipDatadefinitBVO>();
	 IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
	 private ArrayList<String> refFieldCodes = new ArrayList<String>();
	public DatalookQueryClientUI() {

	}
	
	
	 public DatalookQueryClientUI(String key){
		 this.key = key;
		 try {
			setDefaultData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 public DatalookQueryClientUI(String key,Boolean isAuth,HashMap<String, DipDatadefinitBVO> dataBMap){
		 this.key = key;
		 this.isAuth=isAuth;
		 this.dataBMap = dataBMap;
		 try {
			setDefaultData();
			((UIRefPane)getBillCardPanel().getBodyItem("getvalue").getComponent()).setAutoCheck(false);
			((UIRefPane)getBillCardPanel().getBodyItem("getvalue").getComponent()).setIsCustomDefined(true);
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
		return new DatalookQueryClientUICtrl();
	}


	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setDefaultData() throws Exception {
		String pk_datadefint_h=this.key;
		if(pk_datadefint_h!=null&&pk_datadefint_h.length()>0){
			ClientEvnUtilVO evo=null;
			if(isAuth){
				evo = ClientEnvDef.getQueryAuthMap(pk_datadefint_h);
			}else{
				evo = ClientEnvDef.getQueryMap(pk_datadefint_h);
			}
			if(evo!=null&&evo.getCnames()!=null&&evo.getCnames().length>0){
				QueryUtilVO[] uvo=evo.getVos();
				fieldmap=(HashMap<String, String>) evo.getMap();
				getBillCardPanel().getBillModel().clearBodyData();	
				for(int i=0;i<uvo.length;i++){
					getBillCardPanel().getBillModel().addLine();
					UFBoolean isref = uvo[i].getIsref();
					if(null != isref && isref.booleanValue()){
						refFieldCodes.add(uvo[i].getEname());
					}
					getBillCardPanel().getBillModel().setBodyRowVO(uvo[i], i);//.setValueAt(uvo[i].getCname(),i, "cname");
//					getBillCardPanel().getBillModel().setValueAt(uvo[i].getEname(),i, "ename");
//					getBillCardPanel().getBillModel().setValueAt(uvo[i].getCztype(),i, "cztype");
//					getBillCardPanel().getBillModel().setValueAt(uvo[i].getGetvalue(),i, "getvalue");
//					getBillCardPanel().getBillModel().setValueAt(uvo[i].getGetvalue(),i, "getvalue");
//					getBillCardPanel().getBillModel().setValueAt(uvo[i].getGetvalue(),i, "getvalue");
					if(null != uvo[i].getGetvalue() && !"".equals(uvo[i].getGetvalue())){
						getBillCardPanel().getBillModel().setCellEditable(i, "cztype", false);
						getBillCardPanel().getBillModel().setCellEditable(i, "getvalue", false);
					}else{
						getBillCardPanel().getBillModel().setCellEditable(i, "cztype", true);
						getBillCardPanel().getBillModel().setCellEditable(i, "getvalue", true);
					}
					if(null != uvo[i].getIslock() && uvo[i].getIslock().booleanValue()){
						BillItem[] bodyItems = getBillCardPanel().getBillModel().getBodyItems();
						for (BillItem billItem : bodyItems) {
							getBillCardPanel().getBillModel().setForeground(Color.blue, i, getBillCardPanel().getBillModel().getItemIndex(billItem.getKey()));
						}
					}
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
		this.setBillOperate(2);

		getBillCardPanel().getBillTable().setSortEnabled(false);
		/*
			String sql ="";
			
//			sql = "select ename,cname from dip_datadefinit_b " +
//			" where pk_datadefinit_h = '" + this.key + "' and nvl(dr,0) = 0 ";
			sql="select d.ename,d.cname from dip_design d where d.pk_datadefinit_h='"+this.key+"' and d.designtype=1";
			
			IUAPQueryBS querybs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<DipDesignVO> listVO = (ArrayList<DipDesignVO>)querybs.executeQuery(sql, new BeanListProcessor(DipDesignVO.class));
			if(listVO!=null && listVO.size()>0){
				getBillCardPanel().getBillModel().clearBodyData();	//清除数据
				String[] field=new String[listVO.size()];
				for(int i = 0;i<listVO.size();i++){
					DipDesignVO bvo = listVO.get(i);
					getBillCardPanel().getBillModel().addLine();
					getBillCardPanel().getBillModel().setValueAt(bvo.getCname(),i, "cname");
					getBillCardPanel().getBillModel().setValueAt(bvo.getEname(),i, "ename");
					getBillCardPanel().getBillModel().setCellEditable(i, "cztype", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "getvalue", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "cname", true);
					
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
					fieldmap.put(bvo.getCname(), bvo.getEname());
					field[i]=bvo.getCname();
					
//					if(MyEventHandler.map.keySet()!=null){
//						Iterator<String> it= MyEventHandler.map.keySet().iterator();
//						while(it.hasNext()){
//							String str=it.next();
//							if(bvo.getEname()!=null&&str!=null&&str.equals(bvo.getEname())){
//								String ss[]=MyEventHandler.map.get(str);
//								getBillCardPanel().getBillModel().setValueAt(ss[0],i, "cztype");
//								getBillCardPanel().getBillModel().setValueAt(ss[1],i, "getvalue");
//							}
//						}
//					}
					
				}
				UIComboBox m=(UIComboBox) getBillCardPanel().getBodyItem("cname").getComponent();
				ComboBoxModel jComboBox2Model = 
					new DefaultComboBoxModel(
							field);
				m.setModel(jComboBox2Model);
			}else{
				getBillCardPanel().getBillModel().clearBodyData();	//清除数据
			}
		this.setBillOperate(2);
	*/
//		liyunzhe 给查询模板增加缓存
		if(this.map!=null){
			int count=getBillCardPanel().getRowCount();
			for(int kk=0;kk<count;kk++){
				String ename=getBillCardPanel().getBillModel().getValueAt(kk, "ename")==null?"":getBillCardPanel().getBillModel().getValueAt(kk, "ename").toString();
				if(!ename.equals("")){
				 Map map=this.map.get(ename);
				 if(map!=null){
//					0 kh,1 cztype,2 getvalue,3 ope
					 getBillCardPanel().getBillModel().setValueAt(map.get("kh"), kk, "kh") ;
					 getBillCardPanel().getBillModel().setValueAt(map.get("cztype"), kk, "cztype") ;
					 getBillCardPanel().getBillModel().setValueAt(map.get("getvalue"), kk, "getvalue") ;
					 getBillCardPanel().getBillModel().setValueAt(map.get("ope"), kk, "ope") ;
				 }
				}
			}
		}
	
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
		if(e.getKey().equals("cztype")){
			if(e.getValue().equals("")){
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "getvalue");
			}
		}else if(e.getKey().equals("cname")){
			getBillCardPanel().setBodyValueAt(fieldmap.get(e.getValue()), e.getRow(), "ename");
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "cztype");
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "getvalue");
		}
		
		
//		0 kh,1 cztype,2 getvalue,3 ope
		String ename=getBillCardPanel().getBodyValueAt(e.getRow(), "ename")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "ename").toString().trim();
		Map map=this.map.get(ename);
		if(map==null){
			map=new HashMap<String,String>();
		}
		if(e.getKey().equals("cztype")){
//				getBillCardPanel().setBodyValueAt(null, e.getRow(), "getvalue");
				String value=getBillCardPanel().getBodyValueAt(e.getRow(), "cztype")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "cztype").toString();
					map.put("cztype", value);
					this.map.put(ename, map);
				
		}else if(e.getKey().equals("cname")){
			String ename1=fieldmap.get(e.getValue());
			getBillCardPanel().setBodyValueAt(ename1, e.getRow(), "ename");
			if(this.map.get(ename1)!=null){
				getBillCardPanel().setBodyValueAt(this.map.get(ename1).get("kh"), e.getRow(), "kh");
				getBillCardPanel().setBodyValueAt(this.map.get(ename1).get("cztype"), e.getRow(), "cztype");
				getBillCardPanel().setBodyValueAt(this.map.get(ename1).get("getvalue"), e.getRow(), "getvalue");
				getBillCardPanel().setBodyValueAt(this.map.get(ename1).get("ope"), e.getRow(), "ope");
			}else{
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "cztype");
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "getvalue");
			}
			if(refFieldCodes.contains(ename1)){
				getBillCardPanel().setBodyValueAt(new UFBoolean("Y"), e.getRow(), "isref");
			}else{
				getBillCardPanel().setBodyValueAt(new UFBoolean("N"), e.getRow(), "isref");
			}
			
		}else if(e.getKey().equals("getvalue")){
			String value=getBillCardPanel().getBodyValueAt(e.getRow(), "getvalue")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "getvalue").toString();
			map.put("getvalue", value);
			this.map.put(ename, map);
		}else if(e.getKey().equals("kh")){
			String value=getBillCardPanel().getBodyValueAt(e.getRow(), "kh")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "kh").toString();
				map.put("kh", value);
				this.map.put(ename, map);
		}else if(e.getKey().equals("ope")){
			String value=getBillCardPanel().getBodyValueAt(e.getRow(), "ope")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "ope").toString();
			map.put("ope", value);
			this.map.put(ename, map);
		}
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		Object bodyValueAt = getBillCardPanel().getBodyValueAt(e.getRow(),"isref");
		BillItem bodyItem = getBillCardPanel().getBodyItem("getvalue");
		if(null != bodyValueAt && new UFBoolean(bodyValueAt.toString()).booleanValue()){
			Object bodyValueAt2 = getBillCardPanel().getBodyValueAt(e.getRow(),"ename");
			DipDatadefinitBVO dipDatadefinitBVO = dataBMap.get(bodyValueAt2.toString());
			if(null != dipDatadefinitBVO){
				String type = dipDatadefinitBVO.getType();
				if("DATE".equals(type)){
					((UIRefPane)bodyItem.getComponent()).setIsCustomDefined(false);
					((UIRefPane)bodyItem.getComponent()).setRefNodeName("日历");
				}else{
					((UIRefPane)bodyItem.getComponent()).setRefNodeName("人员基本档案");
					String quotecolu = dipDatadefinitBVO.getQuotecolu();
					String memorytable;
					try {
						memorytable = queryfield.queryfield("SELECT memorytable FROM v_dip_jgyyzdtree WHERE pk_datadefinit_b='"
								+dipDatadefinitBVO.getQuotetable()
								+"'");
						if("ZMDM_OCALYEAR".equals(memorytable)){
							DipYearRefModel yearRefModel = new DipYearRefModel();
							((UIRefPane)bodyItem.getComponent()).setRefModel(yearRefModel);
						}else if("ZMDM_OCALMONTH2".equals(memorytable)){
							DipMonthRefModel monthRefModel = new DipMonthRefModel();
							((UIRefPane)bodyItem.getComponent()).setRefModel(monthRefModel);
						}else if("ZMDM_OCALDAY".equals(memorytable)){
							DipDayRefModel monthRefModel = new DipDayRefModel();
							((UIRefPane)bodyItem.getComponent()).setRefModel(monthRefModel);
						}else{
							DipDataRefModel model = new DipDataRefModel();
							model.setTableName(memorytable);
							model.setPkFieldCode(quotecolu);
							((UIRefPane)bodyItem.getComponent()).setRefModel(model);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}else{
			((UIRefPane)bodyItem.getComponent()).setRefModel(null);
			((UIRefPane)bodyItem.getComponent()).setRefNodeName(null);
		}
		boolean beforeEdit = super.beforeEdit(e);
		return beforeEdit;
	}
}
