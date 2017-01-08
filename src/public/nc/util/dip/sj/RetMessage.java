package nc.util.dip.sj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息类
 * */
public class RetMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1604841092016972247L;
	public RetMessage(){}
	public RetMessage(boolean issucc,String msg){
		super();
		setIssucc(issucc);
		setMessage(msg);
		
	}
	public RetMessage(boolean issucc,String msg,List<String> errlist){
		super();
		setIssucc(issucc);
		setMessage(msg);
		setErrlist(errlist);
		
	}
	public RetMessage(boolean issucc,String msg,Integer su,Integer fa){
		super();
		setIssucc(issucc);
		setMessage(msg);
		setSu(su);
		setFa(fa);
		
	}
	public RetMessage(boolean issucc,String msg,Integer su,int exit,Integer fa){
		super();
		setIssucc(issucc);
		setMessage(msg);
		setSu(su);
		setFa(fa);
		setExit(exit);
	}
	
	/**
	 * 
	 */
	/**
	 * 返回消息
	 * */
	private String message;
	/**
	 * 返回是否成功
	 * */
	private boolean issucc;
	private Integer su;//成功
	private Integer fa;//失败
	private Integer hulue;//忽略
	private Integer fugai;//覆盖
	private Integer insert;//插入
	private int exit;
	private List errlist;
	private List<String> filename=new ArrayList<String>();
	private Object value;
	public void setFileName(String filenames){
		if(filename==null){
			filename=new ArrayList<String>();
		}
		if(filenames!=null&&!filename.contains(filenames)){
			filename.add(filenames);
		}
	}
	public void setFielListName(List<String> fileListname){
		this.filename=fileListname;
	}
	
	public List<String> getfilename(){
		return filename;
	}
	public List getErrlist() {
		return errlist;
	}
	public void setErrlist(List errlist) {
		this.errlist = errlist;
	}
	public int getExit() {
		return exit;
	}
	public void setExit(int exit) {
		this.exit = exit;
	}
	public Integer getFa() {
		return fa;
	}
	public void setFa(Integer fa) {
		this.fa = fa;
	}
	public Integer getSu() {
		return su;
	}
	public void setSu(Integer su) {
		this.su = su;
	}
	public boolean getIssucc() {
		return issucc;
	}
	public void setIssucc(boolean issucc) {
		this.issucc = issucc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Integer getFugai() {
		return fugai;
	}
	public void setFugai(Integer fugai) {
		this.fugai = fugai;
	}
	public Integer getHulue() {
		return hulue;
	}
	public void setHulue(Integer hulue) {
		this.hulue = hulue;
	}
	public Integer getInsert() {
		return insert;
	}
	public void setInsert(Integer insert) {
		this.insert = insert;
	}
	
	
	
}
