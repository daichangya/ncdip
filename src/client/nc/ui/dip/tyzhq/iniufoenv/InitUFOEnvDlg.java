package nc.ui.dip.tyzhq.iniufoenv;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JPanel;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.fi.uforeport.NCFuncForUFO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.util.dip.sj.SJUtil;
/**
 * ������ǵ���һ���Ի�����Ҫ��������getRet()���������ͨ��������������Է���ѡ����ǵڼ��ַ�ʽ��ע�⹹�졣
 * ����ֱ����������࣬Ȼ����Կ������еĽ��
 */
public class InitUFOEnvDlg extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private InitUFOJPanel mipanel=null;
	
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle="��ʽ������ʼ��";
	
	/**
	*��Ȩ��Ϣ���̼ѿƼ�
	*���ߣ�   ���޶�
	*�汾��   
	*������   ����һ��ѯ�ʵĵ�ѡ�Ŀ�dialog
	*�������ڣ�2011-04-14
	*����
	*@param   owner	��ǰ��ClientUI
	*@param   title	������ı���
	*@param   message	ѯ����Ϣ
	*@param   ss����   ��ѡ�Ŀ���ѡ��
	 * @throws Exception 
	*/
	public InitUFOEnvDlg(Container owner) {
		super(owner);
		setSize(500, 300);// ����DLG��С
		setLocation(400, 200);
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}

	
	public InitUFOJPanel getMipanel() {
		if(SJUtil.isNull(mipanel)){
			mipanel=new InitUFOJPanel();
		}
		return mipanel;
	}
	/*public void setMipanel(JPanel mipanel) {
		this.mipanel = mipanel;
	}*/
	/**
	*��Ȩ��Ϣ���̼ѿƼ�
	*���ߣ�   ���޶�
	*�汾��   
	*������   ����ѡ����ǵڼ��ַ�ʽ
	*�������ڣ�2011-04-14
	*����
	*@return ����ѡ����ǵڼ��ַ�ʽ
	 * @throws Exception 
	*/
	public int getRes(){
		if(!isOK){
			return -1;
		}
		return 0;
	}
	/**
	 * ����DLG����
	 */
	@Override
	public String getTitle() {
		return thetitle;
	}
	/**
	 * ����DLGPanel
	 * @return
	 */
	private UIPanel getPanelBackground() {
		if (panelBackground == null) {
			try {
				panelBackground = new UIPanel();
				panelBackground.setName("PanelBackground");
				panelBackground.setLayout(new BorderLayout());
//				panelBackground.add(getNothPanel(),"North");
				panelBackground.add(getMipanel(), "Center");
				panelBackground.add(getPanelButton(), "South");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return panelBackground;
	}
	/**
	 * ��ť[ȷ��]
	 * 
	 * @return
	 */
	private UIButton getBoOK() {
		if (boOK == null) {
			try {
				boOK = new UIButton();
				boOK.setName("m_btOK");
				boOK.setText("ȷ��");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boOK;
	}
	/**
	 * ��ť[ȡ��]
	 * 
	 * @return
	 */
	private UIButton getBoCancel() {
		if (boCancel == null) {
			try {
				boCancel = new UIButton();
				boCancel.setName("m_btCancel");
				boCancel.setText("ȡ��");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boCancel;
	}

	/**
	 * DLG��ť��
	 * 
	 * @return
	 */
	private UIPanel getPanelButton() {
		if (panelButtons == null) {
			try {
				panelButtons = new UIPanel();
				panelButtons.setName("PanelButton");
				panelButtons.setLayout(new FlowLayout());
				getPanelButton().add(getBoOK(), getBoOK().getName());
				getPanelButton().add(getBoCancel(), getBoCancel().getName());

			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return panelButtons;
	}
	private void initConnections() throws java.lang.Exception {
		getBoOK().addActionListener(eventHandler);
		getBoCancel().addActionListener(eventHandler);
	}
	/**
	 * ��ť�����ڲ���
	 * 
	 * @author jieely 2008-8-21
	 */
	class EventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK())
				onOK();
			if (e.getSource() == getBoCancel())
				closeCancel();
		};

	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public void onOK() {
		String[] env=getMipanel().getCalenvFromPanel();
		if(env==null){
			return ;
		}
		for(int i=0;i<env.length-1;i++){
			if(env[i]==null||env[i].length()<=0){
				MessageDialog.showErrorDlg(this,"���ݲ���Ϊ��","���ݲ���Ϊ��");
				return;
//				 String[] calEnv = {"ncdip", //����Դ����
//				  /*ClientEnvironment.getInstance().getAccount().getAccountCode()*/"02", //����
//				  ClientEnvironment.getInstance().getCorporation().getUnitcode(), //��λ
//				  "2011-04-01", //����
//				  ClientEnvironment.getInstance().getUser().getUserCode(), //�û�
//				  "1", //����
//					"2011-04-01",
//			ClientEnvironment.getInstance().getLanguage()//��������
//			    };
			}
		}
		boolean ischeckpass=false;
		try {
			ischeckpass=iqf.checkEnv(env);
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this,"У��",e.getMessage());
		}
		if(ischeckpass){
			ClientEnvDef.setCalenv(env);
			NCFuncForUFO ss=new NCFuncForUFO();
			ss.setCalEnv(env);
			ClientEnvDef.setNCFuncForUFO(ss);
			isOK=true;
			this.close();
		}
	}
	/*public DipWarningsetDayTimeVO getDTVO(){
		if(isOK){
			return ((DayTimePanel)getMipanel()).getDTVO();
		}else{
			return null;
		}
	}*/
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		InitUFOEnvDlg a=new InitUFOEnvDlg(jp);
		a.showModal();
//		System.out.println(a.getRes()+"");
	}

}

