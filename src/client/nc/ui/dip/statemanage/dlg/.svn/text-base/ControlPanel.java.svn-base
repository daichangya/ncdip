package nc.ui.dip.statemanage.dlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.pub.lang.UFDateTime;

public class ControlPanel extends JPanel implements ActionListener{
	private JScrollPane jTabeP = null;
//	private DipDataproceHVO vo;
	private ControlVO controlVO;
	private BillListPanel blp;
	
	String fpk;
	String ywlx;
	String pktabnames;
	
	public ControlPanel(){
		super();
	}

	public ControlPanel(String fpk,String ywlx,String pktabnames){
		this.fpk=fpk;
		this.ywlx=ywlx;
		this.pktabnames=pktabnames;
		initGUI();
	}

	/**
	 * @desc 业务数据的字段选择
	 **/
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public BillListPanel getJpField(){
		if(blp==null){
			blp=new BillListPanel();
			blp.loadTemplet("H4H8Hd", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
			blp.getBodyTable().setSortEnabled(false);
			try{
				String olddata=pktabnames;//原始数据：中文名称
//				String tabname=vo.getFirstdata();//原始表名
				String[] old=olddata.split(",");
//				String[] tname=tabname.split(",");

				//查询条件
				String oldstr = "'asd'";
				for(int i=0;i<old.length;i++){
					oldstr =oldstr+",'"+old[i]+"'";
				}

				ControlVO cvo=new ControlVO();
				//清数据 not in 
				HYPubBO_Client.deleteByWhereClause(ControlVO.class, " pk_bus='"+fpk+"' and bustype='"+ywlx+"' and nvl(dr,0)=0 and tabcname not in("+oldstr+")");

				ControlVO[] vos=(ControlVO[]) HYPubBO_Client.queryByCondition(ControlVO.class, " pk_bus='"+fpk+"' and bustype='"+ywlx+"' and nvl(dr,0)=0 and tabcname in("+oldstr+")");
				int v=vos.length;
				//查询修改前与修改后的有相同的值，把它替换成空，不再插入
				for (int k=0;k<v;k++){
					olddata = olddata.replaceAll(vos[k].getTabcname().toString(), "");
				}					

				if(null!=olddata || olddata.length()>0){
					old=olddata.split(",");
					for(int j=0;j<old.length;j++){
						if (null !=old[j] && old[j].length()>0){
							cvo.setTabname("");//表名
							cvo.setTabcname(old[j]);//中文名称:存主键
							cvo.setBustype(ywlx);
							cvo.setPk_bus(fpk);
							cvo.setDr(0);
							HYPubBO_Client.insert(cvo);
						}
					}
					vos=(ControlVO[]) HYPubBO_Client.queryByCondition(ControlVO.class, " pk_bus='"+fpk+"' and bustype='"+ywlx+"' and nvl(dr,0)=0  and tabcname in("+oldstr+")");
				}
				blp.setBodyValueVO(vos);
				//设置界面加载出来时为可编辑状态
				int row=vos.length;
				for(int i=0;i<row;i++){
					blp.getBodyBillModel().setCellEditable(i, "vdef_clzt", true);
					blp.getBodyBillModel().setCellEditable(i, "vdef_jszt", true);
				}
				/*UIRefPane uir=(UIRefPane)blp.getBodyItem("tabname").getComponent();
				DataDefinitRefModel model2=new DataDefinitRefModel();
				model2.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h <>'"+vo.getPrimaryKey()+"' and pk_sysregister_h='"+vo.getFpk()+"' and nvl(dr,0)=0 and dip_datadefinit_h.iscreatetab='Y'");//取消原始表名只能参照自定义限制条件 2011-06-21 zlc
				uir.setRefModel(model2);
				int selected=blp.getBodyTable().getSelectedRow();
				blp.getBodyBillModel().execEditFormulas(selected);*/

				//执行显示公式
				blp.getBodyBillModel().execLoadFormula();

			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return blp;
	}

	/**
	 * @desc 格式的设置
	 * */
	private void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(920, 388));
		this.setLayout(null);
		this.add(getTabbedPane());
	}
	private JScrollPane getTabbedPane() {
		if (jTabeP == null) {
			try {
				jTabeP = new JScrollPane();
				jTabeP.setBounds(12, 25, 920, 345);
				jTabeP.setName("FiledTabbedPane");
				jTabeP.setViewportView(getJpField());

			} catch (Exception ivjExc) {
				ivjExc.printStackTrace();
			}
		}
		return jTabeP;
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClickedaaa");		
	}

	public void mouseEntered(MouseEvent e) {
		System.out.println("mouseEnteredaaa");		
	}

	public void mouseExited(MouseEvent e) {
		System.out.println("mouseExitedaaa");		
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("mousePressedaaa");			
	}

	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleasedaaa");				
	}

	public int getDecimalFromItem(int row, BillItem item) {
		return 0;
	}

	public int getDecimalFromSource(int row, Object okValue) {
		return 0;
	}

	public int getDecimalType() {
		return 0;
	}

	public String getSource() {
		return null;
	}

	public boolean isTarget(BillItem item) {
		return false;
	}
}
