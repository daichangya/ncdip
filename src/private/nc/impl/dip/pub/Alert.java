package nc.impl.dip.pub;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IAlert;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
//预警执行类
public class Alert implements IAlert{
	/**
	 * @author wyd
	 * @desc 重写equals方法，如果Alert的mybillvo是一样的，那么返回true，否则是fasle
	 * @return 如果Alert的mybillvo是一样的，那么返回true，否则是fasle
	 * */
	@Override
	public boolean equals(Object arg0) {
		boolean ret=false;
		try{
			if(!SJUtil.isNull(arg0)){
				ret=getMybillvo().equals(((Alert)arg0).getMybillvo());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}

	private MyBillVO mybillvo=null;
/**
 * @author wyd
 * @desc 构造方法
 * @param nc.vo.dip.warningset.MyBillVO 预警设置vo
 * */
	public Alert(MyBillVO mybillvo){
		this.mybillvo=mybillvo;
	}
	public Alert(String hpk){
		super();
		MyBillVO vo=new MyBillVO();
		BaseDAO bd=getBaseDAO();
		try {
			DipWarningsetVO hvo=(DipWarningsetVO) bd.retrieveByPK(DipWarningsetVO.class, hpk);
			List bvo=(List) bd.retrieveByClause(DipWarningsetBVO.class, "pk_warningset='"+hpk+"' and nvl(dr,0)=0");
			vo.setParentVO(hvo);
			vo.setChildrenVO(SJUtil.isNull(bvo)?null:((DipWarningsetBVO[])bvo.toArray(new DipWarningsetBVO[bvo.size()])));
			this.mybillvo=vo;
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	BaseDAO bd=null;
	private BaseDAO getBaseDAO(){
		if(bd==null){
			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}
	public Alert(DipWarningsetVO hvo){
		super();
		if(SJUtil.isNull(hvo)||SJUtil.isNull(hvo.getPrimaryKey())){
			return;
		}
		MyBillVO vo=new MyBillVO();
		BaseDAO bd=getBaseDAO();
		vo.setParentVO(hvo);
		List bvo;
		try {
			bvo = (List) bd.retrieveByClause(DipWarningsetBVO.class, "pk_warningset='"+hvo.getPrimaryKey()+"' and nvl(dr,0)=0");
			vo.setParentVO(hvo);
			vo.setChildrenVO(SJUtil.isNull(bvo)?null:((DipWarningsetBVO[])bvo.toArray(new DipWarningsetBVO[bvo.size()])));
			this.mybillvo=vo;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	public MyBillVO getMybillvo() {
		return mybillvo;
	}

	public void setMybillvo(MyBillVO mybillvo) {
		this.mybillvo = mybillvo;
	}
	
	/**
	 * @author wyd
	 *@desc 预警的执行方
	 * */
	public boolean doAlert(){
		Logger.debug(Thread.currentThread().getName()+"-------start");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()+"-------end");
		return false;
	}
	
}
