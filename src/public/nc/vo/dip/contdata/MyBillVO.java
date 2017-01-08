package nc.vo.dip.contdata;



import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;


/**
 * 
 * 单子表/单表头/单表体聚合VO
 *参照树卡片多子表，重写方所用法
 * 创建日期:2011-5-12
 * @author 张龙超
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO /* implements IExAggVO*/ {


	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return null;
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (DipContdataVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		/*if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new DipDataproceBVO[0]));
		}*/
		super.setChildrenVO(null);
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((DipContdataVO)parent);
	}


	
	/*

	*//** 字段描述 *//*
	//private static final long serialVersionUID = 7585725489091667339L;

	// 用于装载多子表数据的HashMap
	private HashMap hmChildVOs = new HashMap();

	*//**
	 * 返回多个子表的编码 必须与单据模版的页签编码对应 创建日期：2008-4-4
	 * 
	 * @return String[]
	 *//*
	public String[] getTableCodes() {

		return new String[] { "dip_contdata_b", "dip_contdata_b2" };

	}

	*//**
	 * 返回多个子表的中文名称 创建日期：2008-4-4
	 * 
	 * @return String[]
	 *//*
	public String[] getTableNames() {

		return new String[] { "数据对照定义多子表--子表一", "数据对照定义多子表--子表二", };
	}

	*//**
	 * 取得所有子表的所有VO对象 创建日期：2008-4-4
	 * 
	 * @return CircularlyAccessibleValueObject[]
	 *//*
	public CircularlyAccessibleValueObject[] getAllChildrenVO() {

		ArrayList al = new ArrayList();
		for (int i = 0; i < getTableCodes().length; i++) {
			CircularlyAccessibleValueObject[] cvos = getTableVO(getTableCodes()[i]);
			if (cvos != null)
				al.addAll(Arrays.asList(cvos));
		}

		return (SuperVO[]) al.toArray(new SuperVO[0]);
	}

	*//**
	 * 返回每个子表的VO数组 创建日期：2008-4-4
	 * 
	 * @return CircularlyAccessibleValueObject[]
	 *//*
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {

		return (CircularlyAccessibleValueObject[]) hmChildVOs.get(tableCode);
	}

	*//**
	 * 
	 * 创建日期：2008-4-4
	 * 
	 * @param SuperVO
	 *            item
	 * @param String
	 *            id
	 *//*
	public void setParentId(SuperVO item, String id) {
	}

	*//**
	 * 为特定子表设置VO数据 创建日期：2008-4-4
	 * 
	 * @param String
	 *            tableCode
	 * @para CircularlyAccessibleValueObject[] vos
	 *//*
	public void setTableVO(String tableCode, CircularlyAccessibleValueObject[] vos) {

		hmChildVOs.put(tableCode, vos);
	}

	*//**
	 * 缺省的页签编码 创建日期：2008-4-4
	 * 
	 * @return String
	 *//*
	public String getDefaultTableCode() {

		return getTableCodes()[0];
	}

	*//**
	 * 
	 * 创建日期：2008-4-4
	 * 
	 * @param String
	 *            tableCode
	 * @param String
	 *            parentId
	 * @return SuperVO[]
	 *//*
	public SuperVO[] getChildVOsByParentId(String tableCode, String parentId) {

		return null;
	}

	*//**
	 * 
	 * 创建日期：2008-4-4
	 * 
	 * @return HashMap
	 *//*
	public HashMap getHmEditingVOs() throws Exception {

		return null;
	}

	*//**
	 * 
	 * 创建日期:2008-4-4
	 * 
	 * @param SuperVO
	 *            item
	 * @return String
	 *//*
	public String getParentId(SuperVO item) {

		return null;
	}
*/}
