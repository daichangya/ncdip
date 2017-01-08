package nc.vo.dip.view;

import nc.vo.pub.SuperVO;


public class VDipTabAFileVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1522473518742298337L;


	/**
	 * ƾ֤ģ������ú͸��ƵĲ���
	 *
create or replace view v_dip_creref as (
--ƾ֤ģ������ú͸��ƵĲ���
select d.syscode, d.sysname, d.memorytable, c.unitcode, c.unitname,h.name, h.fpk ,b.pk_datachange_b,b.dr
  from dip_datachange_h h
  left join dip_datachange_b b on h.pk_datachange_h = b.pk_datachange_h
  left join bd_corp c on b.orgcode = c.pk_corp
  left join dip_datadefinit_h d on h.busidata = d.pk_datadefinit_h
 where c.pk_corp is not null
   and d.syscode is not null
   and b.tempexist = '�Ѷ���'
   and nvl(h.dr, 0) = 0
   and nvl(b.dr, 0) = 0
   and nvl(c.dr, 0) = 0
   and nvl(d.dr, 0) = 0
   )
	 */
//	private static final long serialVersionUID = 7822266910309509697L;

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_tableafile";
	}
	private String code;//���ݶ��������·���ı���
	private String name;//���ݶ��������·��������
	private String pk;//���ݶ������·��������
	private String type;//FILE /PATH
	private String des;//�����Ӣ�ı�����·��
	private String pksys;//ϵͳ����
	private String scode;//ϵͳ����
	private String extcode;//�ⲿϵͳ����
	private String sname;//ϵͳ����


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExtcode() {
		return extcode;
	}

	public void setExtcode(String extcode) {
		this.extcode = extcode;
	}

	public String getPksys() {
		return pksys;
	}

	public void setPksys(String pksys) {
		this.pksys = pksys;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}


}
