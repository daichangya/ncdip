package nc.vo.dip.exp;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class DataExpVO extends SuperVO
{
  public String vdef4;
  public String type;
  public UFBoolean flag;
  public String vdef7;
  public Integer dr;
  public String vdef3;
  public String vdef9;
  public String pk_dip_dataexp;
  public String vdef8;
  public String vdef2;
  public String exptab;
  public String ts;
  public String exppro;
  public String vdef6;
  public String vdef10;
  public String pk_contdata_h;
  public String vdef1;
  public String vdef5;
  public static final String VDEF4 = "vdef4";
  public static final String TYPE = "type";
  public static final String FLAG = "flag";
  public static final String VDEF7 = "vdef7";
  public static final String DR = "dr";
  public static final String VDEF3 = "vdef3";
  public static final String VDEF9 = "vdef9";
  public static final String PK_DIP_DATAEXP = "pk_dip_dataexp";
  public static final String VDEF8 = "vdef8";
  public static final String VDEF2 = "vdef2";
  public static final String EXPTAB = "exptab";
  public static final String TS = "ts";
  public static final String EXPPRO = "exppro";
  public static final String VDEF6 = "vdef6";
  public static final String VDEF10 = "vdef10";
  public static final String PK_CONTDATA_H = "pk_contdata_h";
  public static final String VDEF1 = "vdef1";
  public static final String VDEF5 = "vdef5";

  public String getVdef4()
  {
    return this.vdef4;
  }

  public void setVdef4(String newVdef4)
  {
    this.vdef4 = newVdef4;
  }

  public String getType()
  {
    return this.type;
  }

  public void setType(String newType)
  {
    this.type = newType;
  }

  public UFBoolean getFlag()
  {
    return this.flag;
  }

  public void setFlag(UFBoolean newFlag)
  {
    this.flag = newFlag;
  }

  public String getVdef7()
  {
    return this.vdef7;
  }

  public void setVdef7(String newVdef7)
  {
    this.vdef7 = newVdef7;
  }

  public Integer getDr()
  {
    return this.dr;
  }

  public void setDr(Integer newDr)
  {
    this.dr = newDr;
  }

  public String getVdef3()
  {
    return this.vdef3;
  }

  public void setVdef3(String newVdef3)
  {
    this.vdef3 = newVdef3;
  }

  public String getVdef9()
  {
    return this.vdef9;
  }

  public void setVdef9(String newVdef9)
  {
    this.vdef9 = newVdef9;
  }

  public String getPk_dip_dataexp()
  {
    return this.pk_dip_dataexp;
  }

  public void setPk_dip_dataexp(String newPk_dip_dataexp)
  {
    this.pk_dip_dataexp = newPk_dip_dataexp;
  }

  public String getVdef8()
  {
    return this.vdef8;
  }

  public void setVdef8(String newVdef8)
  {
    this.vdef8 = newVdef8;
  }

  public String getVdef2()
  {
    return this.vdef2;
  }

  public void setVdef2(String newVdef2)
  {
    this.vdef2 = newVdef2;
  }

  public String getExptab()
  {
    return this.exptab;
  }

  public void setExptab(String newExptab)
  {
    this.exptab = newExptab;
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

  public void setTs(String newTs)
  {
    this.ts = newTs;
  }

  public String getExppro()
  {
    return this.exppro;
  }

  public void setExppro(String newExppro)
  {
    this.exppro = newExppro;
  }

  public String getVdef6()
  {
    return this.vdef6;
  }

  public void setVdef6(String newVdef6)
  {
    this.vdef6 = newVdef6;
  }

  public String getVdef10()
  {
    return this.vdef10;
  }

  public void setVdef10(String newVdef10)
  {
    this.vdef10 = newVdef10;
  }

  public String getPk_contdata_h()
  {
    return this.pk_contdata_h;
  }

  public void setPk_contdata_h(String newPk_contdata_h)
  {
    this.pk_contdata_h = newPk_contdata_h;
  }

  public String getVdef1()
  {
    return this.vdef1;
  }

  public void setVdef1(String newVdef1)
  {
    this.vdef1 = newVdef1;
  }

  public String getVdef5()
  {
    return this.vdef5;
  }

  public void setVdef5(String newVdef5)
  {
    this.vdef5 = newVdef5;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (this.pk_dip_dataexp == null) {
      errFields.add(new String("pk_dip_dataexp"));
    }

    StringBuffer message = new StringBuffer();
    message.append("下列字段不能为空:");
    if (errFields.size() > 0) {
      String[] temp = (String[])errFields.toArray(new String[0]);
      message.append(temp[0]);
      for (int i = 1; i < temp.length; ++i) {
        message.append(",");
        message.append(temp[i]);
      }
      throw new NullFieldException(message.toString());
    }
  }

  public String getParentPKFieldName()
  {
    return null;
  }

  public String getPKFieldName()
  {
    return "pk_dip_dataexp";
  }

  public String getTableName()
  {
    return "dip_dataexp";
  }

  public DataExpVO()
  {
  }

  public DataExpVO(String newPk_dip_dataexp)
  {
    this.pk_dip_dataexp = newPk_dip_dataexp;
  }

  public String getPrimaryKey()
  {
    return this.pk_dip_dataexp;
  }

  public void setPrimaryKey(String newPk_dip_dataexp)
  {
    this.pk_dip_dataexp = newPk_dip_dataexp;
  }

  public String getEntityName()
  {
    return "dip_dataexp";
  }
}