package nc.vo.dip.recformat;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;

public class DipRecformatVO extends SuperVO
{
  public String code;
  public String str_def_1;
  public String str_def_3;
  public String ts;
  public String pk_recformat;
  public Integer dr;
  public String str_def_2;
  public String recvalue;
  public String name;
  public static final String CODE = "code";
  public static final String STR_DEF_1 = "str_def_1";
  public static final String STR_DEF_3 = "str_def_3";
  public static final String TS = "ts";
  public static final String PK_RECFORMAT = "pk_recformat";
  public static final String DR = "dr";
  public static final String STR_DEF_2 = "str_def_2";
  public static final String RECVALUE = "recvalue";
  public static final String NAME = "name";

  public String getCode()
  {
    return this.code;
  }

  public void setCode(String newCode)
  {
    this.code = newCode;
  }

  public String getStr_def_1()
  {
    return this.str_def_1;
  }

  public void setStr_def_1(String newStr_def_1)
  {
    this.str_def_1 = newStr_def_1;
  }

  public String getStr_def_3()
  {
    return this.str_def_3;
  }

  public void setStr_def_3(String newStr_def_3)
  {
    this.str_def_3 = newStr_def_3;
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

  public String getPk_recformat()
  {
    return this.pk_recformat;
  }

  public void setPk_recformat(String newPk_recformat)
  {
    this.pk_recformat = newPk_recformat;
  }

  public Integer getDr()
  {
    return this.dr;
  }

  public void setDr(Integer newDr)
  {
    this.dr = newDr;
  }

  public String getStr_def_2()
  {
    return this.str_def_2;
  }

  public void setStr_def_2(String newStr_def_2)
  {
    this.str_def_2 = newStr_def_2;
  }

  public String getRecvalue()
  {
    return this.recvalue;
  }

  public void setRecvalue(String newRecvalue)
  {
    this.recvalue = newRecvalue;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String newName)
  {
    this.name = newName;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (this.pk_recformat == null) {
      errFields.add(new String("pk_recformat"));
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
    return "pk_recformat";
  }

  public String getTableName()
  {
    return "dip_recformat";
  }

  public DipRecformatVO()
  {
  }

  public DipRecformatVO(String newPk_recformat)
  {
    this.pk_recformat = newPk_recformat;
  }

  public String getPrimaryKey()
  {
    return this.pk_recformat;
  }

  public void setPrimaryKey(String newPk_recformat)
  {
    this.pk_recformat = newPk_recformat;
  }

  public String getEntityName()
  {
    return "dip_recformat";
  }
}