package nc.ui.dip.datadefinit;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipDatalookVO;

public class WaTreeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -3867639768969365077L;

    private String nodename = null;

	public WaTreeRenderer(ImageIcon leaficon, String nodename) {
		super();
		this.leafIcon = leaficon;
		this.nodename = nodename;
	}
	public WaTreeRenderer(){
		
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		if(value!=null){
			if(value instanceof VOTreeNode){
		VOTreeNode node = (VOTreeNode) value;
//		BDVoTreeNode voNode = null;
//		Object userObject = null;
		DipDatadefinitHVO hvo = null;

		if(node!=null&&node.getData()!=null){
//			if(node.getParent() instanceof VOTreeNode){
//				VOTreeNode vonode=(VOTreeNode) node.getParent();
//				if(vonode!=null&&vonode.getData()!=null){
					if(node.getData() instanceof DipDatadefinitHVO){
						hvo=(DipDatadefinitHVO) node.getData();
						if(hvo.getPk_sysregister_h()!=null&&hvo.getPk_sysregister_h().length()>0)
						{
//							1	01	基础信息结构	0001BB100000000JJOKC
//							2	02	业务信息结构	0001BB100000000JJOKM
//							3	03	系统信息结构	0001BB100000000JKANO
//							4	04	加工信息结构	0001BB100000000JKANP
							//liyunzhe modify 2012-06-13 修改数据类型基础档案比较
							if(hvo.getDatatype()!=null&&!hasFocus){
								if(hvo.getDatatype().equals(IContrastUtil.DATASTYLE_BASEINF)){//基础信息结构
									setForeground(Color.black);
								}else if(hvo.getDatatype().equals(IContrastUtil.DATASTYLE_BUSINESSINF)){//业务信息结构
									setForeground(Color.blue);
								}else if(hvo.getDatatype().equals(IContrastUtil.DATASTYLE_PROCESSINF)){//加工信息结构
									setForeground(Color.gray);
								}else if(hvo.getDatatype().equals(IContrastUtil.DATASTYLE_SYSTEMINF)){//系统信息结构
									setForeground(Color.green);
								}
							}else if(hvo.getDatatype()!=null&&hasFocus){
								setForeground(Color.white);
							}
						}
				}
					if(node.getData() instanceof DipDatalookVO){
						DipDatalookVO i=(DipDatalookVO) node.getData();
						if(i!=null&&i.getDatatype()!=null&&i.getPk_datadefinit_h()!=null&&i.getPk_datalook()!=null&&i.getPk_datalook().length()>0)
						{
//							1	01	基础信息结构	0001BB100000000JJOKC
//							2	02	业务信息结构	0001BB100000000JJOKM
//							3	03	系统信息结构	0001BB100000000JKANO
//							4	04	加工信息结构	0001BB100000000JKANP
							if(i.getDatatype()!=null&&!hasFocus){
								if(i.getDatatype().equals(IContrastUtil.DATASTYLE_BASEINF)){//基础信息结构
									setForeground(Color.black);
								}else if(i.getDatatype().equals(IContrastUtil.DATASTYLE_BUSINESSINF)){//业务信息结构
									setForeground(Color.blue);
								}else if(i.getDatatype().equals(IContrastUtil.DATASTYLE_PROCESSINF)){//加工信息结构
									setForeground(Color.gray);
								}else if(i.getDatatype().equals(IContrastUtil.DATASTYLE_SYSTEMINF)){//系统信息结构
									setForeground(Color.green);
								}
							}else if(i.getDatatype()!=null&&hasFocus){
								setForeground(Color.white);
							}
						}
				
					}
		}
			}
		}
		return this;
	}

	/*protected int nodeType(Object value) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		BDVoTreeNode voNode = null;
		Object userObject = null;
		WaclassHeaderVO waclassvo = null;

		userObject = node.getUserObject();
		
//		=====================TODO modify by jieely 	
		if(userObject instanceof WaclassHeaderVO)
		{
			if(((WaclassHeaderVO)userObject).getDocname() != null && ((WaclassHeaderVO)userObject).getDocname().equals("defdoc"))
				return IS_TRUNK;
		}
//		=====================END 2010-12-15
		
		if (node instanceof BDVoTreeNode) {
			voNode = (BDVoTreeNode) node;
			if (voNode != null) {
				waclassvo = (WaclassHeaderVO) voNode.getVo();
			}
		}

		if (node != null && node.isRoot()) {
			return IS_ROOT;
		}


		if (nodename != null && nodename.equals("薪资类别") && WaGlobal.isGroup()) { -=notranslate=- 
		 if(waclassvo != null && waclassvo.getIsealflag() == 1){
			return IS_SEALED;
		} else {
			return IS_LEAF;
		}

		} else  {
			if (((userObject instanceof String) && (userObject != null && userObject.equals(GROUP)))
					|| (waclassvo != null && waclassvo.getVwaclassname().equals(GROUP) && node.getParent() != null
					&& ((DefaultMutableTreeNode)node.getParent()).isRoot())) {
				return IS_GROUP;
			} else if (((userObject instanceof String) && (userObject != null && userObject.equals(CORP)))
					|| (waclassvo != null && waclassvo.getVwaclassname().equals(CORP) && node.getParent() != null
							&& ((DefaultMutableTreeNode)node.getParent()).isRoot())) {
				return IS_CORP;
			}else if(waclassvo != null && waclassvo.getIsealflag() == 1){
				return IS_SEALED;
			} else {
				return IS_LEAF;
			}
		}
	}*/
}

