package nc.vo.dip.voucher;

import nc.vo.dip.in.RowDataVO;

public class VoucherVO {
	String id;
	String glorgbookcode;
	boolean checkpass;
	RowDataVO[] srcVo;
	VoucherHVO parent;
	VoucherDetailVO[] children;
	
	public VoucherDetailVO[] getChildren() {
		return children;
	}
	public void setChildren(VoucherDetailVO[] children) {
		this.children = children;
	}
	public VoucherHVO getParent() {
		return parent;
	}
	public void setParent(VoucherHVO parent) {
		this.parent = parent;
	}
	public RowDataVO[] getSrcVo() {
		return srcVo;
	}
	public void setSrcVo(RowDataVO[] srcVo) {
		this.srcVo = srcVo;
	}
	public String getGlorgbookcode() {
		return glorgbookcode;
	}
	public void setGlorgbookcode(String glorgbookcode) {
		this.glorgbookcode = glorgbookcode;
	}
	public boolean isCheckpass() {
		return checkpass;
	}
	public void setCheckpass(boolean checkpass) {
		this.checkpass = checkpass;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
