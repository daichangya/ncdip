package nc.vo.dip.view;

import nc.vo.pub.SuperVO;

public class VDipSysdefinitVO extends SuperVO{
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_datadefinit_h";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_sysdefinit";
	}
	
	public String pk_sysregister_h;//ϵͳ��������
	public String pk_sysregister_b;//ϵͳվ������
	public String code;//ϵͳ����
	public String extcode;//��ϵͳ����
	public String extname;//��ϵͳ����
	public String sitecode;//վ�����
	public String sitename;//վ������
	public String pk_datadefinit_h;//���ݶ�������
	public String syscode;//���ݶ������
	public String sysname;//���ݶ�������
	public String memorytable;//���ݶ������
	
	
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
	
	

}
