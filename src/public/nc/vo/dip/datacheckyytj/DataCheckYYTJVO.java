package nc.vo.dip.datacheckyytj;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class DataCheckYYTJVO extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6429639017056804150L;
	public String pksys;//ϵͳ����
	public String syscode;//ϵͳ����
	public String sysecode;//�ⲿϵͳ����
	public String sysname;//ϵͳ����
	public String datatype;//ҵ������
	public String pkbus;//ҵ������
	public String buscode;//ҵ�����
	public String busname;//ҵ������
	public String pkvh;//����У�����������
	public String pkvb;//����У��ĸ�������
	public String vcode;//����У��ĸ������
	public String vclass;//���ݽ��޵ĸ����У����
	public String vector;//����У�������ʽ
	public String pkv;//����У��ע�������
	public String vtype;//����У������
	public String vdesc;//����У�����������ͨ��У���õ�
	public String verifycon;//ͨ������У���У�����
	public String rwlx;//��������
	public Integer dr;//
	public String ts;//
	
	
	public String getRwlx() {
		return rwlx;
	}

	public void setRwlx(String rwlx) {
		this.rwlx = rwlx;
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 *
	 * ��������:2011-6-30
	 */
	public DataCheckYYTJVO() {

		super();	
	}    

	public String getBuscode() {
		return buscode;
	}

	public void setBuscode(String buscode) {
		this.buscode = buscode;
	}

	public String getBusname() {
		return busname;
	}

	public void setBusname(String busname) {
		this.busname = busname;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getPkbus() {
		return pkbus;
	}

	public void setPkbus(String pkbus) {
		this.pkbus = pkbus;
	}

	public String getPksys() {
		return pksys;
	}

	public void setPksys(String pksys) {
		this.pksys = pksys;
	}

	public String getPkv() {
		return pkv;
	}

	public void setPkv(String pkv) {
		this.pkv = pkv;
	}

	public String getPkvb() {
		return pkvb;
	}

	public void setPkvb(String pkvb) {
		this.pkvb = pkvb;
	}

	public String getPkvh() {
		return pkvh;
	}

	public void setPkvh(String pkvh) {
		this.pkvh = pkvh;
	}

	public String getSysecode() {
		return sysecode;
	}

	public void setSysecode(String sysecode) {
		this.sysecode = sysecode;
	}

	public String getVclass() {
		return vclass;
	}

	public void setVclass(String vclass) {
		this.vclass = vclass;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getVdesc() {
		return vdesc;
	}

	public void setVdesc(String vdesc) {
		this.vdesc = vdesc;
	}

	public String getVector() {
		return vector;
	}

	public void setVector(String vector) {
		this.vector = vector;
	}

	public String getVerifycon() {
		return verifycon;
	}

	public void setVerifycon(String verifycon) {
		this.verifycon = verifycon;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getSyscode() {
		return syscode;
	}
	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public Object getTs() {
		if(ts==null){
			return null;
		}else{
			if(IContrastUtil.VERSION.equals("nc502")){
				  return new UFDateTime(ts);
			  }else if(IContrastUtil.VERSION.equals("nc507")){
				  return ts;  
			  }
		}
		return ts;
	} 
	public void setTs(String ts) {
		this.ts = ts;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pkvb";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_alldatatype";
	}
	/**
	 * ���ض����ʶ,����Ψһ��λ����.
	 *
	 * ��������:2011-6-30
	 * @return String
	 */
	public String getPrimaryKey() {

		return pkvb;

	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����.
	 *
	 * ��������:2011-6-30
	 * @param newPk_dip_datacanzhao  String    
	 */
	public void setPrimaryKey(String pkvb) {

		pkvb = pkvb; 

	} 

	/**
	 * ������ֵ�������ʾ����.
	 *
	 * ��������:2011-6-30
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "v_dip_alldatatype"; 

	} 

}
