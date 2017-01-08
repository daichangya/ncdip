package nc.impl.dip.sjjgimpl;
import java.sql.SQLException;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IDataProcess;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.pub.ClientEnvironment;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;


/**
 * ����ж�ز����
 * ���ߣ��뽨��
 * ʱ�䣺2011-07-02
 * */
public class DataProcessSjxzImpl implements IDataProcess {

	public RetMessage dataprocess(String hpk, String sql, String tablename,String oldtablename) throws Exception {
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		if(oldtablename.contains(",")){
			String oldtables[]=oldtablename.split(",");
			if(oldtables!=null&&oldtables.length>0){
				for(int i=0;i<oldtables.length;i++){
					process(sql,oldtables[i], iqf);
				}	
			}
		}else{
			process(sql, oldtablename, iqf);
		}
		return new RetMessage(true,"����ж�سɹ���");
	}
	public void process(String sql,String oldtablename,IQueryField iqf) throws Exception{
//		��һ�������Դ��׷�ӱ��ݣ��ѵ�ǰ����������ݣ����뵽���ݱ�Ȼ����յ�ǰ��
		if("DIP_BAK1".equals(sql)){
			sql = "select 1 from user_tables where table_name = '"+oldtablename+"_BAK'";
			String tablebak = iqf.queryfield(sql);
			//��ѯ���ݱ��Ƿ��Ѵ���
			if("1".equals(tablebak)){
				//ֱ�ӱ�������
				sql = "insert into "+oldtablename+"_BAK select * from "+oldtablename+"";
				iqf.exesqlCommit(sql);
			}else{
				//����������������
				sql = "create table "+oldtablename+"_BAK as select * from "+oldtablename+" ";
				iqf.exesqlCommit(sql);
			}

			sql = "delete from "+oldtablename;
			iqf.exesqlCommit(sql);
		}
		/*��ж�ر��ݵı�����Ϊdip_bak��������ʱ����ĸ�ʽ������һ��Դ˱���
		 * 2011-06-21
		 * zlc*/
		if("DIP_BAK_TS".equals(sql)){
//			UFDate date = new UFDate(new Date());
//			UFTime ts=new UFTime(new Time(0));//2011-06-21 
			UFDateTime da=ClientEnvironment.getInstance().getServerTime();
//			da.getDay();
//			if(da.getDay()<){}
	      	//String ss=""+da.getYear()+da.getMonth()+da.getDay()+da.getHour()+da.getMinute();
	      	String year=(""+da.getYear()).substring(2, 4).trim();
	      	String month="";
	      	String day="";
	      	String hour="";
	      	String minute="";
	      	if(da.getMonth()<10){
	      		month=(""+0+da.getMonth()).trim();
	      	}else{
	      		month=(""+da.getMonth()).trim();
	      	}
	      	if(da.getDay()<10){
	      		day=(""+0+da.getDay()).trim();
	      	}else{
	      		day=(""+da.getDay()).trim();

	      	}
	      	if(da.getHour()<10){
	      		hour=(""+0+da.getHour()).trim();
	      	}else{
	      		hour=(""+da.getHour()).trim();
	      	}
	      	if(da.getMinute()<10){
	      		minute=(""+0+da.getMinute()).trim();
	      	}else{
	      		minute=(""+da.getMinute()).trim();
	      	}
	      	String ss=year+month+day+hour+minute;
			//String ss=da.toString().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
			//ss=ss.trim();
//			String str=da.toString();
//			System.out.println(str+"**********************************************************************8");
			
			//�ڶ���������ѵ�ǰ������ݲ��뵽�´����ġ���ǰ����ʱ������ı��У�Ȼ����յ�ǰ��
			String tablebakname = "";
			tablebakname =oldtablename+ss.trim();
			sql = "select 1 from user_tables where table_name = '"+tablebakname+"'";
			String tablebak = iqf.queryfield(sql);
			if("1".equals(tablebak)){
				sql = "drop table "+tablebakname;
				iqf.exesqlCommit(sql);
			}

			//���ݱ�
			sql = "create table "+tablebakname+" as select * from "+oldtablename+" ";
			iqf.exesqlCommit(sql);
			//��յ�ǰ��
			sql = "delete from "+oldtablename;
			iqf.exesqlCommit(sql);
		}
	}
	
}
