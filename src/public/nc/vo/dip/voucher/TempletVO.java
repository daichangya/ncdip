package nc.vo.dip.voucher;

import nc.vo.dip.credence.CredenceBVO;
import nc.vo.dip.credence.CredenceHVO;

/**
 * @author Administrator
 * @Ä£°åÊý¾Ý
 */
public class TempletVO {
	CredenceHVO parent;
	CredenceBVO[] children;
	
	public CredenceBVO[] getChildren() {
		return children;
	}
	public void setChildren(CredenceBVO[] children) {
		this.children = children;
	}
	public CredenceHVO getParent() {
		return parent;
	}
	public void setParent(CredenceHVO parent) {
		this.parent = parent;
	}
	
}
