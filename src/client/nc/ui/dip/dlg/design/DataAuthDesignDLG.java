package nc.ui.dip.dlg.design;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datalook.DipAuthDesignVO;
import nc.vo.dip.datalook.VDipAuthDesignVO;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
/**
 * ������ǵ���һ���Ի�����Ҫ��������getRet()���������ͨ��������������Է���ѡ����ǵڼ��ַ�ʽ��ע�⹹�졣
 * ����ֱ����������࣬Ȼ����Կ������еĽ��
 */
public class DataAuthDesignDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	public  static final Integer DATAAUTHORIZEWH= 1;//����Ȩ��ά���ڵ�
	private UIButton boOK = null;
	private UIButton boQK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private DataAuthDesignPanel mipanel=null;
	private DipWarningsetDayTimeVO dtvo;
	private String[] pks;
	private String[] pkfields;
	
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle="����";
//	liyunzhe add 2012-09-20
	private boolean isAuthorizeBrow=true;//��ʾ�Ƿ�����Ȩ�����ʶ������Ǿ�Ϊtrue���ͻ���Ȩ����ڵ�����ð�ť��������ҵ�������ѡ������һ�����������򣬳�����ʾ�������ǳ����ڵ����Ϣ�ڵ��־
	private Integer nodetype=null;//�ڵ��ʶ����ʾ���ĸ��ڵ�����,1��ʾ����Ȩ��ά��
	private String ismobile = null;
	/**
	*��Ȩ��Ϣ���̼ѿƼ�
	*���ߣ�   ���޶�
	*�汾��   
	*������   ����һ��ѯ�ʵĵ�ѡ�Ŀ�dialog
	*�������ڣ�2011-04-14
	*����
	*@param   owner	��ǰ��ClientUI
	*@param   pks	������
	 * @throws Exception 
	*/
	public DataAuthDesignDLG(Container owner,String[] pks) {
		super(owner);
		this.pks=pks;
		setSize(1200, 475);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
		this.pkfields=null;
		this.nodetype=null;
	}
	public DataAuthDesignDLG(Container owner,String[] pks,String[] pkfields){
		super(owner);
		this.pks=pks;//���ݶ���������������һ���Ǳ�ͷ�����������ڶ����Ǳ������ݶ�������������
		this.pkfields=pkfields;//���ݶ����ӱ������������ֶζ������ݶ����Ӧ���ӱ�ġ������ֶΡ����������ϱ�pks��Ӧ
		this.nodetype=null;
		setSize(1200, 475);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	
	public DataAuthDesignDLG(Container owner,String[] pks,String[] pkfields,boolean isAuthorizeBrow,String ismobile){
		super(owner);
		this.pks=pks;
		this.pkfields=pkfields;
		this.isAuthorizeBrow=isAuthorizeBrow;
		this.nodetype=null;
		this.ismobile = ismobile;
		setSize(1200, 475);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	/**
	 * type,�ڵ�����
	 * 1��ʾ����Ȩ��ά���ڵ�
	 */
	public DataAuthDesignDLG(Container owner,String[] pks,String[] pkfields,Integer nodetype){
		super(owner);
		this.pks=pks;
		this.pkfields=pkfields;
		this.nodetype=nodetype;
		setSize(1200, 475);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	
	public DataAuthDesignDLG(Container owner,DipWarningsetDayTimeVO dtvo){
		super(owner);
		this.dtvo=dtvo;
		setSize(1200, 475);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
		this.pkfields=null;
	}
	
	public DataAuthDesignPanel getMipanel() {
		if(SJUtil.isNull(mipanel)){
			if(isAuthorizeBrow){            
				mipanel=new DataAuthDesignPanel(pks,pkfields,isAuthorizeBrow,ismobile);
			}else if(this.nodetype!=null){
				mipanel=new DataAuthDesignPanel(pks,pkfields,nodetype);
			}else{
				mipanel=new DataAuthDesignPanel(pks,pkfields);
			}
		}
		return mipanel;
	}
	public void setMipanel(DataAuthDesignPanel mipanel) {
		this.mipanel = mipanel;
	}
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
	 * ��ť[���]
	 * 
	 * @return
	 */
	private UIButton getBoQK() {
		if (boQK == null) {
			try {
				boQK = new UIButton();
				boQK.setName("m_btQK");
				boQK.setText("���");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boQK;
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
				getPanelButton().add(getBoQK(), getBoQK().getName());

			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return panelButtons;
	}
	private void initConnections() throws java.lang.Exception {
		getBoOK().addActionListener(eventHandler);
		getBoQK().addActionListener(eventHandler);
		getBoCancel().addActionListener(eventHandler);
	}
	/**
	 * ��ť�����ڲ���
	 * 
	 * @author jieely 2008-8-21
	 */
	class EventHandler implements java.awt.event.ActionListener {
		final DataAuthDesignDLG this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				Map<String, VDipAuthDesignVO[]> desmap = ((DataAuthDesignPanel) this$0.getMipanel()).getDMAP();
				ArrayList<String> notsaveList = ((DataAuthDesignPanel) this$0.getMipanel()).getNotsaveList();
				if(desmap!=null){
					if(pks!=null&&pks.length>0){
						String keys="";
						for(int i=0;i<pks.length;i++){
							keys=keys+"'"+pks[i]+"',";
						}
						keys=keys.substring(0,keys.length()-1);
						try {
							HYPubBO_Client.deleteByWhereClause(DipAuthDesignVO.class, "pk_datadefinit_h in("+keys+")");
							Iterator it=desmap.keySet().iterator();
							while (it.hasNext()){
								String key=it.next().toString();
								if(key.endsWith("r")&&(!notsaveList.contains(key))){
									VDipAuthDesignVO[] vos=desmap.get(key);
									if(vos!=null&&vos.length>0){
										DipAuthDesignVO[] voi=new DipAuthDesignVO[vos.length];
										for(int i=0;i<vos.length;i++){
											VDipAuthDesignVO vosi=vos[i];
											voi[i]=new DipAuthDesignVO();
											voi[i].setCname(vosi.getCname());
											voi[i].setEname(vosi.getEname());
											int index=(key.indexOf("-")+1);
											voi[i].setDesigntype(Integer.parseInt(key.substring(index,index+1)));
											voi[i].setIsdisplay(vosi.getIsdisplay());
											voi[i].setPk_datadefinit_b(vosi.getPk_datadefinit_b());
											voi[i].setPk_datadefinit_h(vosi.getPk_datadefinit_h());
											voi[i].setStatus(vosi.getStatus());
											voi[i].setDisno(vosi.getDisno());
											voi[i].setDesignlen(vosi.getDesignlen());
											voi[i].setDesigplen(vosi.getDesigplen());
											voi[i].setContype(vosi.getContype());
											voi[i].setIspk(vosi.getIspk());
											voi[i].setDatatype(vosi.getDatatype());
											voi[i].setConspk(vosi.getConspk());
											voi[i].setConsvalue(vosi.getConsvalue());
											voi[i].setIslock(vosi.getIslock());
											voi[i].setIs_list(vosi.getIs_list());
											voi[i].setIs_single_list(vosi.getIs_single_list());
										}
										HYPubBO_Client.insertAry(voi);
									}
								}
							}
						} catch (UifException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}else if(e.getSource()==getBoQK()){
				onBoQk();
			}
		};
		EventHandler() {
			this$0 = DataAuthDesignDLG.this;
			// super();
		}

	}
	public void onBoQk(){
		getMipanel().onBoQk();
	}
	public void onOK() {
		isOK=true;
		this.close();
	}
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		DataAuthDesignDLG a=new DataAuthDesignDLG(jp,new String[]{""});
		a.showModal();
//		System.out.println(a.getRes()+"");
	}
}
