package nc.vo.dip.view;
/**
 * @author WYD
 * @DESC ���ݶ��壬ϵͳע�ᣬ���ݽ��յ�VO
 * */
import nc.vo.pub.SuperVO;

public class VDipDsreceiveVO extends SuperVO{
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_datarec_h";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_dsreceive";
	}
	
	public String def_str_1;//վ���־���û�У�������ֶδ���
	public String pk_datarec_h;
	public String rcode;//ͬ���������
	public String recname;//ͬ����������
	public String sourcetype;//������Դ����
	public String pk_sysregister_h;//ϵͳ��������
	public String bjpk;//ϵͳ �������
	public String bj;//ϵͳ ���
	public String repeatdata;//�ظ����ݿ�������
	public String constatus;//����������
	public String endstatus;//�����������
	public String pk_sysregister_b;//ϵͳվ������
	public String code;//ϵͳ����
	public String extcode;//��ϵͳ����
	public String extname;//��ϵͳ����
	public String sitecode;//վ�����
	public String sitename;//վ������
	public String pk_datadefinit_h;//���ݶ�������
	public String busicode;//ҵ���ʶ
	public String syscode;//���ݶ������
	public String sysname;//���ݶ�������
	public String memorytable;//���ݶ������
	public String backcon;//�Ƿ��ִ��־
//	public String backerrinfo;//��ִ������
//	public String bz1;//��ִ��ʼ��־
//	public String bz2;//��ִ���ݱ�־
//	public String bz3;//��ִ������־
	public String backmsr;//��ִ��Ϣ����������
	public String isesc;//�Ƿ�ת��
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExtcode() {
		return extcode;
	}

	public void setExtcode(String extcode) {
		this.extcode = extcode;
	}

	public String getExtname() {
		return extname;
	}

	public void setExtname(String extname) {
		this.extname = extname;
	}

	public String getMemorytable() {
		return memorytable;
	}

	public void setMemorytable(String memorytable) {
		this.memorytable = memorytable;
	}

	public String getPk_datadefinit_h() {
		return pk_datadefinit_h;
	}

	public void setPk_datadefinit_h(String pk_datadefinit_h) {
		this.pk_datadefinit_h = pk_datadefinit_h;
	}

	public String getPk_sysregister_b() {
		return pk_sysregister_b;
	}

	public void setPk_sysregister_b(String pk_sysregister_b) {
		this.pk_sysregister_b = pk_sysregister_b;
	}

	public String getPk_sysregister_h() {
		return pk_sysregister_h;
	}

	public void setPk_sysregister_h(String pk_sysregister_h) {
		this.pk_sysregister_h = pk_sysregister_h;
	}

	public String getSitecode() {
		return sitecode;
	}

	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
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

	public String getBj() {
		return bj;
	}

	public void setBj(String bj) {
		this.bj = bj;
	}

	public String getBjpk() {
		return bjpk;
	}

	public void setBjpk(String bjpk) {
		this.bjpk = bjpk;
	}

	public String getConstatus() {
		return constatus;
	}

	public void setConstatus(String constatus) {
		this.constatus = constatus;
	}

	public String getEndstatus() {
		return endstatus;
	}

	public void setEndstatus(String endstatus) {
		this.endstatus = endstatus;
	}

	public String getPk_datarec_h() {
		return pk_datarec_h;
	}

	public void setPk_datarec_h(String pk_datarec_h) {
		this.pk_datarec_h = pk_datarec_h;
	}

	public String getRcode() {
		return rcode;
	}

	public void setRcode(String rcode) {
		this.rcode = rcode;
	}

	public String getRecname() {
		return recname;
	}

	public void setRecname(String recname) {
		this.recname = recname;
	}

	public String getRepeatdata() {
		return repeatdata;
	}

	public void setRepeatdata(String repeatdata) {
		this.repeatdata = repeatdata;
	}

	public String getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(String sourcetype) {
		this.sourcetype = sourcetype;
	}

	public String getBusicode() {
		return busicode;
	}

	public void setBusicode(String busicode) {
		this.busicode = busicode;
	}

	public String getDef_str_1() {
		return def_str_1;
	}

	public void setDef_str_1(String def_str_1) {
		this.def_str_1 = def_str_1;
	}

	public String getBackcon() {
		return backcon;
	}

	public void setBackcon(String backcon) {
		this.backcon = backcon;
	}
	
	
	/*public String getBackerrinfo() {
		return backerrinfo;
	}

	public void setBackerrinfo(String backerrinfo) {
		this.backerrinfo = backerrinfo;
	}

	public String getBz1() {
		return bz1;
	}

	public void setBz1(String bz1) {
		this.bz1 = bz1;
	}

	public String getBz2() {
		return bz2;
	}

	public void setBz2(String bz2) {
		this.bz2 = bz2;
	}

	public String getBz3() {
		return bz3;
	}

	public void setBz3(String bz3) {
		this.bz3 = bz3;
	}*/

	public String getIsesc() {
		return isesc;
	}

	public void setIsesc(String isesc) {
		this.isesc = isesc;
	}

	public String getBackmsr() {
		return backmsr;
	}

	public void setBackmsr(String backmsr) {
		this.backmsr = backmsr;
	}
	
	

}
