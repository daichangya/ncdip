package nc.util.dip.sj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 消息类
 * */
public class CheckMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 428755196054925402L;
	
	// 返回消息
	private String message;
	// 返回是否成功
	private boolean successful;
//	//成功的记录数
//	private Integer amount_suc;
//	//失败的记录数
//	private Integer amount_err;
//	//每行的错误消息
//	private Map<Integer,String> errmsg;
//	//校验名称
//	private String checkname;
	
	private Object[] validData;
	
	private List errList;
	
	private HashMap map;
	/*在同步所有后缀类型的文件时候校验表头是否正确*/
	private boolean isAllPrexCheckTile;
	
	public Object[] getValidData() {
		return validData;
	}
	public void setValidData(Object[] validData) {
		this.validData = validData;
	}
//	/**
//	 * @desc 得到具体的错误消息，返回一个map，key是行号，value是具体每行消息
//	 * */
//	public Map<Integer, String> getErrmsg() {
//		return errmsg;
//	}
//	public void setErrmsg(Map<Integer, String> errmsg) {
//		this.errmsg = errmsg;
//	}
	/**
	 * @desc 得到错误消息
	 * */
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
//	/**
//	 * 返回失败的记录数
//	 * */
//	public Integer getAmount_err() {
//		return amount_err;
//	}
//	public void setAmount_err(Integer amount_err) {
//		this.amount_err = amount_err;
//	}
//	/**
//	 * 返回成功的记录数
//	 * */
//	public Integer getAmount_suc() {
//		return amount_suc;
//	}
//	public void setAmount_suc(Integer amount_suc) {
//		this.amount_suc = amount_suc;
//	}
//	/**
//	 * 返回校验名称
//	 * */
//	public String getCheckname() {
//		return checkname;
//	}
//	public void setCheckname(String checkname) {
//		this.checkname = checkname;
//	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	public List getErrList() {
		return errList;
	}
	public void setErrList(List errList) {
		this.errList = errList;
	}
	public HashMap getMap() {
		return map;
	}
	public void setMap(HashMap map) {
		this.map = map;
	}
	public boolean isAllPrexCheckTile() {
		return isAllPrexCheckTile;
	}
	public void setAllPrexCheckTile(boolean isAllPrexCheckTile) {
		this.isAllPrexCheckTile = isAllPrexCheckTile;
	}
	
	
}
