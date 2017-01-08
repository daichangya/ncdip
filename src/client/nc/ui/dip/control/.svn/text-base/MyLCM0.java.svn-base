package nc.ui.dip.control;




import javax.swing.JFrame;

import nc.ui.pub.bill.BillListPanel;
import nc.vo.dip.warningset.MyBillVO;

public class MyLCM0 {
	ControlClientUI ui = new ControlClientUI();
	BillListPanel listpanel;
	String pk_spsq;
	MyBillVO[] mybillvo;
	public MyLCM0(){
		this.pk_spsq=pk_spsq;
		this.mybillvo=mybillvo;
		JFrame jf=new JFrame("联查内部转账授权记录");
		jf.add(ui);
		jf.setSize(1200,700);
		jf.setLocation(40, 40);
		jf.setVisible(true);
	}
//	private BillListPanel getUITablePane1rc() {
//		if (listpanel == null) {
//			listpanel = new BillListPanel();
//			
//			listpanel.loadTemplet("H4H2H5", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
//			listpanel.setSize(1200, 700);
//			listpanel.getHeadTable().addMouseListener(new MouseAdapter(){
//				@Override
//				public void mouseExited(MouseEvent mouseevent) {
//				}
//				@Override
//				public void mouseReleased(MouseEvent mouseevent) {
//					int row=listpanel.getHeadTable().getSelectedRow();
//					listpanel.setBodyValueVO(mybillvo[row].getChildrenVO());
//					listpanel.getBodyBillModel().execLoadFormula();
////					listpanel.updateUI();
//				}
//				public void mouseClicked(MouseEvent e) {
////					mouseClick(e);
//				}
//				public void mousePressed(MouseEvent mouseevent) {		
//					
//				}
//			});
//			/*ArapSpsqVO[] vos = null;
//			try {
//				vos =(ArapSpsqVO[]) HYPubBO_Client.queryByCondition(ArapSpsqVO.class, "(vdef1 = '"+pk_spsq+"' or pk_spsq = '"+pk_spsq+"') or (dr = 1 and (vdef1 = '"+pk_spsq+"' or pk_spsq = '"+pk_spsq+"'))");
//			} catch (UifException e) {
//				e.printStackTrace();
//			}
//			listpanel.setHeaderValueVO(vos);
//			listpanel.getHeadBillModel().execLoadFormula();
//			listpanel.getParentListPanel().setShowThMark(true);*/
//			/*DipWarningsetVO[] hvo=new DipWarningsetVO[mybillvo.length];
//			for(int i=0;i<hvo.length;i++){
//				hvo[i]=(DipWarningsetVO) mybillvo[i].getParentVO();
//			}
//			listpanel.setHeaderValueVO(hvo);
//			if(hvo.length>0){
//				listpanel.getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
//				listpanel.setBodyValueVO(mybillvo[0].getChildrenVO());
//			}*/
//			listpanel.getBodyBillModel().execLoadFormula();
//			listpanel.getHeadBillModel().execLoadFormula();
//		}
//		return listpanel;
//	}
 
}
