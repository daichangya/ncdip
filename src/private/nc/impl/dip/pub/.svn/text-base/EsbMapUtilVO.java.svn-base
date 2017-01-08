package nc.impl.dip.pub;

import java.io.Serializable;
import java.util.List;

import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.tabstatus.DipTabstatusVO;
import nc.vo.dip.tabstatus.TabstatusBVO;
import nc.vo.dip.view.VDipDsreceiveVO;

public class EsbMapUtilVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6862934706632094640L;
	private String type;//类型
	private String key;//大串
	private List<DataformatdefBVO> data;//数据项
	private String bj;//分割标记
	private String bjesc;//分割标记 如果需要转义就转义，不需要转义就和bj一样 
	private String tablename;//存储表名
	private List<String> checkname;//数据校验的方法列表
	private List<DipDatadefinitBVO> typeddb;//数据定义的附表列表
	private VDipDsreceiveVO sysvo;
	private String pk_datarec;
	private String esc;//转义字符参数   是在接收esb数据时候插入到数据库时候，替换转义字符用的。
	private String mergemark;//合并分割符号 
	private String mergemarkesc;//合并分割符号转义 ，如果需要转义就转义，不需要转义就和mergemark一样 
	private String mergeStyle;// 合并类型  0表示正常模式，1表示记录合并，2表示完全合并
	private Integer delaytime;//延迟时间
	private Integer mergeCount;//合并条数
	private String  endflag;//结束标志
	
	
	public String getPk_datarec() {
		return pk_datarec;
	}
	public void setPk_datarec(String pk_datarec) {
		this.pk_datarec = pk_datarec;
	}
	public DipTabstatusVO getThvo(){
		DipTabstatusVO hvo=new DipTabstatusVO();
		hvo.setTablecode(sysvo.getPk_datadefinit_h());
		hvo.setSysname(sysvo.getExtname());
		hvo.setSyscode(sysvo.getPk_sysregister_h());
		hvo.setTabname(sysvo.getSysname());
		hvo.setStabname(sysvo.getMemorytable());
		return hvo;
	}
	public TabstatusBVO getTbvo(){
		TabstatusBVO bvo=new TabstatusBVO();
		bvo.setActive("数据接收");
		return bvo;
	}
	
	public VDipDsreceiveVO getSysvo() {
		return sysvo;
	}
	public void setSysvo(VDipDsreceiveVO sysvo) {
		this.sysvo = sysvo;
	}
	public List<String> getCheckname() {
		return checkname;
	}
	public void setCheckname(List<String> checkname) {
		this.checkname = checkname;
	}
	public String getBj() {
		return bj;
	}
	public void setBj(String bj) {
		this.bj = bj;
	}
	public List<DataformatdefBVO> getData() {
		return data;
	}
	public void setData(List<DataformatdefBVO> data) {
		this.data = data;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<DipDatadefinitBVO> getTypeddb() {
		return typeddb;
	}
	public void setTypeddb(List<DipDatadefinitBVO> typeddb) {
		this.typeddb = typeddb;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getEsc() {
		return esc;
	}
	public void setEsc(String esc) {
		this.esc = esc;
	}
	public Integer getDelaytime() {
		return delaytime;
	}
	public void setDelaytime(Integer delaytime) {
		this.delaytime = delaytime;
	}
	public Integer getMergeCount() {
		return mergeCount;
	}
	public void setMergeCount(Integer mergeCount) {
		this.mergeCount = mergeCount;
	}
	public String getMergemark() {
		return mergemark;
	}
	public void setMergemark(String mergemark) {
		this.mergemark = mergemark;
	}
	public String getMergeStyle() {
		return mergeStyle;
	}
	public void setMergeStyle(String mergeStyle) {
		this.mergeStyle = mergeStyle;
	}
	public String getMergemarkesc() {
		return mergemarkesc;
	}
	public void setMergemarkesc(String mergemarkesc) {
		this.mergemarkesc = mergemarkesc;
	}
	public String getBjesc() {
		return bjesc;
	}
	public void setBjesc(String bjesc) {
		this.bjesc = bjesc;
	}
	public String getEndflag() {
		return endflag;
	}
	public void setEndflag(String endflag) {
		this.endflag = endflag;
	}
	
	
}
