package nc.ui.dip.message;





import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.jdbc.framework.exception.DbException;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dip.message.MsrVO;
import nc.vo.pub.BusinessException;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author 何冰
 * @version tempProject version
 */

public abstract class AbstractMyEventHandler extends CardEventHandler {

	public AbstractMyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch(intBtn){
			case IBtnDefine.TESTESBREC:
				onBoESBRec();
				break;
			case IBtnDefine.TESTESBSEND:
				onBoESBSend();
				break;
		}
	}

//测试Esb消息队列的发送
	private void onBoESBSend() {
		
		ITaskExecute it=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
		MsrVO vo=null;
		if(getBillCardPanelWrapper().getSelectedBodyVOs()!=null&&getBillCardPanelWrapper().getSelectedBodyVOs().length>=1&&getBillCardPanelWrapper().getSelectedBodyVOs()[0]!=null){
			vo=(MsrVO) getBillCardPanelWrapper().getSelectedBodyVOs()[0];
				
		}else{
			this.getBillUI().showErrorMessage("请选择一行！");
			return;
		}
		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		try {
			String sql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000005'";
			String canShuSend=iq.queryfield(sql);
			String sql1="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000006'";
			String canShuCount=iq.queryfield(sql1);
			if(canShuSend!=null&&canShuSend.length()>0){
				vo.setCanShuSend(canShuSend);
			}else{
				getBillUI().showErrorMessage("esb测试发送消息流参数没有值！");
				return;
			}
			
			if(canShuCount!=null&&canShuCount.length()>0){
				String regex="[0-9]*";
				boolean flag=canShuCount.matches(regex);
				if(!flag){
					getBillUI().showErrorMessage("esb测试发送消息流次数参数值有误！");
					return;
				}
				Integer iii=Integer.parseInt(canShuCount);
				if(!(iii>0)){
					getBillUI().showErrorMessage("esb测试发送消息流次数参数值错误！");
					return;
				}
				vo.setCnaShuCount(iii);
			}
			
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
		String str="";
		if(vo.getVdef10()!=null&&vo.getVdef10().booleanValue()){
			str=it.dosendTopic(vo);
		}else{
			str=it.dosend(vo);
		}
		MessageDialog.showHintDlg(this.getBillUI(), "发送", str);
		
//		//JY.CTFMS.SEND 集邮发送，财务接收 财务 expctfms 123456
//		//CTFMS.JY.SEND 财务发送，集邮接收 集邮 expjy 123456
//		String url="10.3.14.137:7322";
//		String strJmsUser="expjy";
//		String strJmsPwd="123456";
//		String strJmsQuName="JY.CTFMS.SEND";
//		try{
//			QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
//				url);
//
//			QueueConnection connection = factory.createQueueConnection(
//				strJmsUser, strJmsPwd);
//
//			QueueSession session = connection.createQueueSession(false,
//				javax.jms.Session.AUTO_ACKNOWLEDGE);
//
//			javax.jms.Queue queue = session.createQueue(strJmsQuName);
//			QueueSender sender = session.createSender(queue);
//
//			System.out.println(new StringBuffer(
//					"JMS发送信息开始开始:------------------------"));
//			String strMessage="";
//			connection.start();
//			TextMessage message = null;
//			BytesMessage bytesMessage = null;
//			for(int i=0;i<4;i++){
//				TextMessage t=new TibjmsTextMessage(("abcdef"+i).getBytes(),"abcdef".length());
//				sender.send(t);
//			}
//			getBillUI().showErrorMessage("发送完");
//			connection.close();
//		}catch(Exception e){
//			e.printStackTrace();
//			getBillUI().showErrorMessage(e.getMessage());
//		}

	
		
	}


//测试esb消息队列的接收
	private void onBoESBRec() {
		//JY.CTFMS.SEND 集邮发送，财务接收 财务 expctfms 123456
		//CTFMS.JY.SEND 财务发送，集邮接收 集邮 expjy 123456
		ITaskExecute it=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
		MsrVO vo=null;
		if(getBillCardPanelWrapper().getSelectedBodyVOs()!=null&&getBillCardPanelWrapper().getSelectedBodyVOs().length>=1&&getBillCardPanelWrapper().getSelectedBodyVOs()[0]!=null){
			vo=(MsrVO) getBillCardPanelWrapper().getSelectedBodyVOs()[0];
				
		}else{
			this.getBillUI().showErrorMessage("请选择一行！");
			return;
		}
		String str=it.dorec(vo);
		MessageDialog.showHintDlg(this.getBillUI(), "接收", str);
		
		
//		String url="10.3.14.137:7322";
//		String strJmsUser="expjy";
//		String strJmsPwd="123456";
//		String strJmsQuName="CTFMS.JY.SEND";
//		try{
//			QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
//				url);
//
//			QueueConnection connection = factory.createQueueConnection(
//				strJmsUser, strJmsPwd);
//
//			QueueSession session = connection.createQueueSession(false,
//				javax.jms.Session.AUTO_ACKNOWLEDGE);
//
//			javax.jms.Queue queue = session.createQueue(strJmsQuName);
//			QueueReceiver receiver = session.createReceiver(queue);
//
//			System.out.println(new StringBuffer(
//					"JMS接收信息开始开始:------------------------"));
//			String strMessage="";
//			connection.start();
//			TextMessage message = null;
//			BytesMessage bytesMessage = null;
//			String me="";
//			for(int i=0;i<5;i++){
//				javax.jms.Message m = receiver.receive(10 * 60 * 1000);
//				
//				if (m != null) {
//					if (m instanceof TextMessage) {
//						message = (TextMessage) m;
//						System.out.println(new StringBuffer("TextMsg message: ")
//								.append(message.getText()));
//						me=me+"\n"+i+":t:"+message.getText();
//					} else if (m instanceof BytesMessage) {
//						bytesMessage = (BytesMessage) m;
//
//						int imsgLen = (int) bytesMessage.getBodyLength();
//						byte[] byteArg = new byte[imsgLen];
//
//
//						strMessage = new String(byteArg);
//						strMessage = strMessage.replace("\n", "");
//						System.out.println(new StringBuffer("BytesMsg message: ")
//								.append(strMessage));
//						System.out.println(new StringBuffer("BytesMsg message: ")
//						.append(strMessage));
//						me=me+"\n"+i+":b:"+strMessage;
//					}
//					System.out.println(strMessage);
//				} else{
//					break;
//				}
//			}
//			getBillUI().showErrorMessage("成功！"+me);
//			connection.close();
//		}catch(Exception e){
//			e.printStackTrace();
//			getBillUI().showErrorMessage("接收失败！"+"\n"+e.getMessage()+"\n"+e.getStackTrace()[0]+"\n"+e.getStackTrace()[1]);
//		}
	}


}