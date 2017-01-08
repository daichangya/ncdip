package nc.ui.dip.exp;

//import nc.ui.jyprj.effectdef.MyEventHandler;S
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.exp.DataExpVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 
 * <P>
 * <B>��Ƭ�͵�����UI��</B>
 * </P>
 * <P>
 * ����˵��: <BR>
 * �������ṩ���ӡ���ѯ���޸ġ�ɾ�����ܡ�
 * 
 * ע��:
 * ��MyEventHandler���б�����дprocessNewBodyVO()����
 * ��ClientUICtrl���п����޸�getBodyCondition()����,Ϊ��ѯ����Ĭ������
 * ���Ҫ�ڵ��ݱ����ҳ�治ˢ�£������²�ѯ���ݣ�����ClientUICheckRuleGetter�̳�IRetCurrentDataAfterSave�ӿ�
 * 
 * @author �α�
 * @version 1.0
 * @date 2008-3-26
 */
public class MyExpClientUI extends AbstractClientUI {
	String hpk;

	/** �ֶ����� */
	private static final long serialVersionUID = 3899425417611651215L;
	public void initdef(String hpk,boolean flag){
		
		int rows=getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			DataExpVO[] bvo;
			try {
				bvo = (DataExpVO[])getVOFromUI().getChildrenVO();
				if(bvo[0].getPrimaryKey()==null||bvo[0].getPrimaryKey().length()<20){
					HYPubBO_Client.insertAry(bvo);
				}else{
					HYPubBO_Client.updateAry(bvo);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<rows;i++){
			getBillCardPanel().delLine();
			}
		}

			
			DipContdataVO dvo=null;
			try {
				 dvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, hpk);
			} catch (UifException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			UFBoolean issys;
			String charisys;
			String pk="";
			if(flag){
				issys=new UFBoolean(false);//�ⲿϵͳ
				charisys="N";
				pk=dvo.getContabcode();//����ϵͳ������
			}else{
				issys=new UFBoolean(true);//NCϵͳ
				charisys="Y";
				pk=dvo.getExtetabcode();//�ⲿϵͳ�����
			}
			
			//�������ֶ����˲���
			UIRefPane pane=(UIRefPane) getBillCardPanel().getBodyItem("exptab").getComponent();
			DataDefinitbRefModel model=new DataDefinitbRefModel();
			model.addWherePart(" and dip_datadefinit_b.pk_datadefinit_h='"+pk+"'  and nvl(dip_datadefinit_b.dr,0)=0");
			pane.setRefModel(model);
		
	
		//���ݶ��ն�������
		this.hpk=hpk;
		try {
			if(hpk!=null){
			DataExpVO[] vo=(DataExpVO[]) HYPubBO_Client.queryByCondition(DataExpVO.class, "pk_contdata_h='"+hpk+"' and flag='"+charisys+"'");
				if(SJUtil.isNull(vo)||vo.length==0){
					for(int i=1;i<=20;i++){
						getBillCardPanel().getBillModel().addLine();
						if(i==1){
							getBillCardPanel().setBodyValueAt("exppro", i-1, "exppro");
						}else{
							
							getBillCardPanel().setBodyValueAt("exppro"+i, i-1, "exppro");
						}
						getBillCardPanel().setBodyValueAt("�����ֶ�"+i, i-1, "exppro");
						getBillCardPanel().setBodyValueAt(issys, i-1, "flag");
						getBillCardPanel().setBodyValueAt(hpk, i-1, "pk_contdata_h");
					}
				}else{
					
					if(rows!=20){
						for(int i=1;i<=20;i++){
							getBillCardPanel().getBillModel().addLine();
						}
					}
					for(int i=0;i<vo.length;i++){
//						vo[i].setExppro("�����ֶ�"+(i+1));
						getBillCardPanel().getBillModel().setBodyRowVO(vo[i],i);
					}
			}
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

//	@Override
//	public void windowClosed(WindowEvent arg0) {
//		// TODO Auto-generated method stub
////		super.windowClosed(arg0);
//	}
	

	public String getRefBillType() {
		return null;
	}
	

	/**
	 * �޸Ĵ˷�����ʼ��ģ��ؼ�����
	 */
	protected void initSelfData() {
	
		
		//getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
	}
	public void onButtonAction(ButtonObject bo)throws Exception
	{
	    ((MyEventHandler)this.getCardEventHandler()).onButtonAction(bo);
	   
	}
	public void setDefaultData() throws Exception {
	}
//	public void init(MyBillVO vo,boolean isadd){
//		onButtonClicked(getButtonManager().getButton(IBillButton.Card));
//		CdSbodyVO hvo=null;
//		if(isadd){
//			//hvo=(CdSbodyVO) vo.getParentVO();
//			//hvo.setEffectname("Ӱ����������");
////			hvo.setCondition("��������д����");
////			hvo.setIsnotwarning("��");
////			hvo.setIsnottiming("��ʱ");
////			((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{hvo});
//
//		}else{
//			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
////((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO) vo.getParentVO()});
//		}
//
//		try {
//			updateBtnStateByCurrentVO();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(isadd){
//
//			onButtonClicked(getButtonManager().getButton(IBillButton.Add));
//			getBillCardPanel().getBillData().setHeaderValueVO(hvo);
//		}
//	}

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
		
		
		
		
	}
	
	
}
