package nc.ui.dip.datarec.formatedlg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
 */
public class DataformatDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIButton boDelete=null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private DataformatPanel mipanel=null;
	private DipWarningsetDayTimeVO dtvo;
	DipDatarecHVO hvo=null;
	
	EventHandler eventHandler = new EventHandler();
	//当前框的标题
	String thetitle="设置";
	public DataformatDLG(Container owner,DipDatarecHVO hvo){
		super(owner);
		this.hvo=hvo;
		setSize(790-38, 500+28);// 设置DLG大小
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	

	public DataformatDLG(Container owner,DipWarningsetDayTimeVO dtvo){
		super(owner);
		this.dtvo=dtvo;
		setSize(790, 500);// 设置DLG大小
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	
	public DataformatPanel getMipanel() {
		if(SJUtil.isNull(mipanel)){
			mipanel=new DataformatPanel(hvo);
		}
		return mipanel;
	}
	public void setMipanel(DataformatPanel mipanel) {
		this.mipanel = mipanel;
	}
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   返回选择的是第几种方式
	*创建日期：2011-04-14
	*参数
	*@return 返回选择的是第几种方式
	 * @throws Exception 
	*/
	public int getRes(){
		if(!isOK){
			return -1;
		}
		return 0;
	}
	/**
	 * 设置DLG名称
	 */
	@Override
	public String getTitle() {
		return thetitle;
	}
	/**
	 * 布局DLGPanel
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
	 * 按钮[确定]
	 * 
	 * @return
	 */
	private UIButton getBoOK() {
		if (boOK == null) {
			try {
				boOK = new UIButton();
				boOK.setName("m_btOK");
				boOK.setText("确定");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boOK;
	}
	/**
	 * 按钮[取消]
	 * 
	 * @return
	 */
	private UIButton getBoCancel() {
		if (boCancel == null) {
			try {
				boCancel = new UIButton();
				boCancel.setName("m_btCancel");
				boCancel.setText("取消");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boCancel;
	}
	private UIButton getDeleteBtn() {
		if (boDelete == null) {
			try {
				boDelete = new UIButton("删除");
				boDelete.setName("m_btDelete");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boDelete;
	}

	/**
	 * DLG按钮组
	 * 
	 * @return
	 */
	private UIPanel getPanelButton() {
		if (panelButtons == null) {
			try {
				panelButtons = new UIPanel();
				panelButtons.setName("PanelButton");
				panelButtons.setLayout(new FlowLayout());
				panelButtons.add(getBoOK(), getBoOK().getName());
				panelButtons.add(getBoCancel(), getBoCancel().getName());
				panelButtons.add(getDeleteBtn(),getDeleteBtn().getName());

			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return panelButtons;
	}
	private void initConnections() throws java.lang.Exception {
		getBoOK().addActionListener(eventHandler);
		getBoCancel().addActionListener(eventHandler);
		getDeleteBtn().addActionListener(eventHandler);
	}
	/**
	 * 按钮监听内部类
	 * 
	 * @author jieely 2008-8-21
	 */
	class EventHandler implements java.awt.event.ActionListener {
		final DataformatDLG this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				DataformatPanel dfp=(DataformatPanel) this$0.getMipanel();
				Map<String,DataformatdefBVO[]> desmap = dfp.getDmap();
				Map<String,DipDatarecSpecialTab[]> specialmap=dfp.getSpecialmap();
				List<DipDatarecSpecialTab> specialvos=new ArrayList<DipDatarecSpecialTab>();
				String sysSideBussinessCode[]=dfp.getSysSideBussinessCode();
//				if(dfp.getRightBackFormat()!=null){
//					DipDatarecSpecialTab backvos[]=new DipDatarecSpecialTab[3];
//					for(int i=0;i<backvos.length;i++){
//						backvos[i]=new DipDatarecSpecialTab();
//					}
//					dfp.getRightBackFormat().getBillModel().getBodyValueVOs(backvos);
//					for(int i=0;i<backvos.length;i++){
//						specialvos.add(backvos[i]);
//					}
//				}
//				if(dfp.getRightXmlFormat()!=null){
//					DipDatarecSpecialTab backvos[]=new DipDatarecSpecialTab[3];
//					for(int i=0;i<backvos.length;i++){
//						backvos[i]=new DipDatarecSpecialTab();
//					}
//					dfp.getRightXmlFormat().getBillModel().getBodyValueVOs(backvos);
//					for(int i=0;i<backvos.length;i++){
//						specialvos.add(backvos[i]);
//					}
//				}
//				if(dfp.getRightXTFormat()!=null){
//					DipDatarecSpecialTab backvos[]=new DipDatarecSpecialTab[3];
//					for(int i=0;i<backvos.length;i++){
//						backvos[i]=new DipDatarecSpecialTab();
//					}
//					dfp.getRightXTFormat().getBillModel().getBodyValueVOs(backvos);
//					for(int i=0;i<backvos.length;i++){
//						specialvos.add(backvos[i]);
//					}
//				}
				
				
				
				String[] pks=dfp.getPks();
				String[] lx=dfp.getLx();
				if(!dfp.ischeckpass()){
					return;
				}
				DataformatdefHVO[] hvos=dfp.getHvos();
				if(specialmap!=null){
					DipDatarecSpecialTab xtvos[]=specialmap.get(DataformatPanel.SPECIAL_XT);
					if(xtvos!=null&&xtvos.length>0){
						for(int i=0;i<xtvos.length;i++){
							if(sysSideBussinessCode!=null){
								for(int w=0;w<sysSideBussinessCode.length;w++){
									
									if(xtvos[i].getNodenumber()==1&&xtvos[i].getName().equals(DataformatPanel.XTBZ)&&xtvos[i].getValue()!=null&&xtvos[i].getValue().equals(sysSideBussinessCode[0])){
										xtvos[i].setValue("");
									}
									if(xtvos[i].getNodenumber()==2&&xtvos[i].getName().equals(DataformatPanel.ZDBZ)&&xtvos[i].getValue()!=null&&xtvos[i].getValue().equals(sysSideBussinessCode[1])){
										xtvos[i].setValue("");
									}
									if(xtvos[i].getNodenumber()==3&&xtvos[i].getName().equals(DataformatPanel.YWBZ)&&xtvos[i].getValue()!=null&&xtvos[i].getValue().equals(sysSideBussinessCode[2])){
										xtvos[i].setValue("");
									}
								}
							}
							xtvos[i].setPk_datarec_h(hvo.getPk_datarec_h());
							xtvos[i].setIs_xtfixed(new UFBoolean(true));
							specialvos.add(xtvos[i]);
						}
					}
					if(dfp.getBackcon()!=null&&dfp.getBackcon().booleanValue()){
						DipDatarecSpecialTab backvos[]=specialmap.get(DataformatPanel.SPECIAL_BACK);
						
						if(backvos!=null&&backvos.length>0){
							for(int i=0;i<backvos.length;i++){
								if(sysSideBussinessCode!=null){
									for(int w=0;w<sysSideBussinessCode.length;w++){
										
										if(backvos[i].getNodenumber()==1&&backvos[i].getName().equals(DataformatPanel.XTBZ)&&backvos[i].getValue()!=null&&backvos[i].getValue().equals(sysSideBussinessCode[0])){
											backvos[i].setValue("");
										}
										if(backvos[i].getNodenumber()==2&&backvos[i].getName().equals(DataformatPanel.ZDBZ)&&backvos[i].getValue()!=null&&backvos[i].getValue().equals(sysSideBussinessCode[1])){
											backvos[i].setValue("");
										}
										if(backvos[i].getNodenumber()==3&&backvos[i].getName().equals(DataformatPanel.YWBZ)&&backvos[i].getValue()!=null&&backvos[i].getValue().equals(sysSideBussinessCode[2])){
											backvos[i].setValue("");
										}
									}
								}
								backvos[i].setPk_datarec_h(hvo.getPk_datarec_h());
								backvos[i].setIs_back(new UFBoolean(true));
								specialvos.add(backvos[i]);
							}
						}
					}
					if(hvos!=null&&hvos[0]!=null&&hvos[0].getFiletype()!=null&&hvos[0].getFiletype()==1){
						DipDatarecSpecialTab xmlvos[]=specialmap.get(DataformatPanel.SPECIAL_XML);
						if(xmlvos!=null&&xmlvos.length>0){
							for(int i=0;i<xmlvos.length;i++){
								xmlvos[i].setPk_datarec_h(hvo.getPk_datarec_h());
								xmlvos[i].setIs_xml(new UFBoolean(true));
								specialvos.add(xmlvos[i]);
							}
						}
					}
				}
				
				IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				if(hvos!=null&&hvos.length>0){
					if(hvos[0].getMessflowdef().equals("1")){
						Iterator<String> keys=desmap.keySet().iterator();
						while(keys.hasNext()){
							String keyi=keys.next();
							if(keyi.endsWith("r")){
								DataformatdefBVO[] bvo=desmap.get(keyi);
								if(bvo!=null&&bvo.length>0){
									for(DataformatdefBVO bvoi:bvo){
										if(bvoi.getCorreskind()==null||bvoi.getCorreskind().length()<=0){
											MessageDialog.showErrorDlg(DataformatDLG.this,  "提示", "字段对照必须录入对照字段！");
											return;
										}
									}
								}
							}
						}
					}
				}
				/*在修改了数据来源类型后，在修改格式定义后保存时候，会按照insert处理，会在新插入一条数据格式定义主表记录，这样一pk_datarec_h会对应多条Dip_Dataformatdef_h主表记录，所以在保存时候先删除记录*/
				if(hvo!=null&&hvo.getPk_datarec_h()!=null){
					String s1="delete  from  dip_dataformatdef_b bb where bb.pk_dataformatdef_h in(select hh.pk_dataformatdef_h from dip_dataformatdef_h hh  where hh.pk_datarec_h='"+hvo.getPk_datarec_h()+"')";
					String s2="delete  from  Dip_Dataformatdef_h where pk_datarec_h='"+hvo.getPk_datarec_h()+"'"; 
					List<String> list=new ArrayList<String>();
					list.add(s1);
					list.add(s2);
					iqf.exectEverySql(list);
				}
				 
				iqf.saveDataFormat(this$0.hvo.getPrimaryKey(),desmap, pks, lx, hvos,specialvos);
				/*if(desmap!=null){
					if(pks!=null&&pks.length>0){
						for(int i=0;i<pks.length;i++){
							DataformatdefHVO hvo=hvos[i];
							if(hvos[i].getPrimaryKey()==null||hvos[i].getPrimaryKey().length()<=0){
							
							}
						}
						
					}
				}*/
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}else if(e.getSource()==getDeleteBtn()){
				getMipanel().onBoDelete();
			}
		};
		EventHandler() {
			this$0 = DataformatDLG.this;
			// super();
		}

	}
	public void onOK() {
		isOK=true;
		this.close();
	}
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		DataformatDLG a=new DataformatDLG(jp,new DipDatarecHVO());
		a.showModal();
//		System.out.println(a.getRes()+"");
	}
}
