package nc.vo.dip.view;

import nc.vo.pub.SuperVO;


public class VDipCrerefVO extends SuperVO {

	/**
	 * 凭证模板的引用和复制的参照
	 *
create or replace view v_dip_creref as (
--凭证模板的引用和复制的参照
select d.syscode, d.sysname, d.memorytable, c.unitcode, c.unitname,h.name, h.fpk ,b.pk_datachange_b,b.dr
  from dip_datachange_h h
  left join dip_datachange_b b on h.pk_datachange_h = b.pk_datachange_h
  left join bd_corp c on b.orgcode = c.pk_corp
  left join dip_datadefinit_h d on h.busidata = d.pk_datadefinit_h
 where c.pk_corp is not null
   and d.syscode is not null
   and b.tempexist = '已定义'
   and nvl(h.dr, 0) = 0
   and nvl(b.dr, 0) = 0
   and nvl(c.dr, 0) = 0
   and nvl(d.dr, 0) = 0
   )
	 */
	private static final long serialVersionUID = 7822266910309509697L;

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_datachange_b";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_creref";
	}
	private String syscode;
	private String sysname;
	private String memorytable;
	private String unitcode;
	private String unitname;
	private String name;
	private String fpk;
	private String pk_datachange_b;

	public String getFpk() {
		return fpk;
	}

	public void setFpk(String fpk) {
		this.fpk = fpk;
	}

	public String getMemorytable() {
		return memorytable;
	}

	public void setMemorytable(String memorytable) {
		this.memorytable = memorytable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk_datachange_b() {
		return pk_datachange_b;
	}

	public void setPk_datachange_b(String pk_datachange_b) {
		this.pk_datachange_b = pk_datachange_b;
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

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

}
