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
	private String type;//����
	private String key;//��
	private List<DataformatdefBVO> data;//������
	private String bj;//�ָ���
	private String bjesc;//�ָ��� �����Ҫת���ת�壬����Ҫת��ͺ�bjһ�� 
	private String tablename;//�洢����
	private List<String> checkname;//����У��ķ����б�
	private List<DipDatadefinitBVO> typeddb;//���ݶ���ĸ����б�
	private VDipDsreceiveVO sysvo;
	private String pk_datarec;
	private String esc;//ת���ַ�����   ���ڽ���esb����ʱ����뵽���ݿ�ʱ���滻ת���ַ��õġ�
	private String mergemark;//�ϲ��ָ���� 
	private String mergemarkesc;//�ϲ��ָ����ת�� �������Ҫת���ת�壬����Ҫת��ͺ�mergemarkһ�� 
	private String mergeStyle;// �ϲ�����  0��ʾ����ģʽ��1��ʾ��¼�ϲ���2��ʾ��ȫ�ϲ�
	private Integer delaytime;//�ӳ�ʱ��
	private Integer mergeCount;//�ϲ�����
	private String  endflag;//������־
	
	
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
		bvo.setActive("���ݽ���");
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
