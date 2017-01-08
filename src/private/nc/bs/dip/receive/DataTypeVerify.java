package nc.bs.dip.receive;

import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.impl.dip.pub.EsbMapUtilVO;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.util.dip.sj.CheckMessage;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.datarecD.ArgDataRecDVO;

public class DataTypeVerify implements ICheckPlugin{

	public CheckMessage doCheck(Object... ob) {
		// TODO Auto-generated method stub
		//integer 
		//number
		//char,varchar,nvarcher
		//long

		// TODO Auto-generated method stub
		CheckMessage checkmsg=new CheckMessage();
		String msg=(String) ob[0];
//		ArgDataRecDVO dvo=(ArgDataRecDVO) ob[1];
		EsbMapUtilVO esbvo=(EsbMapUtilVO) ob[1];
		List<DataformatdefBVO> formatbvoList=esbvo.getData();
		List<DipDatadefinitBVO> defbvoList=esbvo.getTypeddb();
		String separator=esbvo.getBj();//分隔符号 例如 ##
//		##databegin##系统标准0##站点标志##业务标志##1##2##3##4##5##dataend##
		for(int i=4;i<formatbvoList.size();i++){
			DataformatdefBVO bvo=formatbvoList.get(i);
			if(bvo.getVdef2()!=null&&"业务数据".equals(bvo.getVdef2())){//占位符号
			String str=	msg.split(separator)[i+1];//实际的字段值。
			String ename=bvo.getDatakind();//实际字段在表中的ename
			String type=null;
			  for(int j=0;j<defbvoList.size();j++){
				  if(ename.equals(defbvoList.get(j).getEname())){
					 type=defbvoList.get(j).getType(); 
					 break;
				  };
			  
			  }
			  
			  if(type==null){
				  checkmsg.setSuccessful(false);
				  checkmsg.setMessage("不满足数据类型匹配校验，数据库中"+ename+"数据类型为空");
				  break;
			  }else if(type!=null&&type.length()==0){
				  checkmsg.setSuccessful(false);
				  checkmsg.setMessage("不满足数据类型匹配校验，数据库中"+ename+"数据类型为空");
				  break;
			  }
			  Logger.debug("----esb接收数据类型校验---实际字段值 ="+str+", 实际字段在表中的ename="+ename+",type==="+type);  
			  
			  
			  
			  if(str!=null){
				  
				  if("INTEGER".equals(type.toUpperCase())){
				    	String pattern = "[0-9]*";
				    	if(str.matches(pattern)){
				    		//数据类型匹配。
				    		checkmsg.setSuccessful(true);
				    		continue;
				    		
				    	}else{
				    	 checkmsg.setSuccessful(false);
				    	 checkmsg.setMessage("不满足数据类型匹配校验！");
				    	 break;
				    	}
				    	
				    }else if(type.toUpperCase().indexOf("CHAR")>=0){
				    	checkmsg.setSuccessful(true);
				    	continue;
				    }else if("NUMBER".equals(type.toUpperCase())){
				    	if(str.indexOf(".")>=1){
				    		String[] num=str.split(".");
				    		if(num.length==2){
				    			String pattern="[0-9]*";
				    			if(num[0].matches(pattern)&&num[1].matches(pattern)){
				                    //数据类型匹配成功		。
				    				checkmsg.setSuccessful(true);
				    				continue;
				    			}else{
				    				checkmsg.setSuccessful(false);
					    			checkmsg.setMessage("不满足数据类型匹配校验！");
					    			break;
				    			}
				    		}else{
				    			//不成功
				    			checkmsg.setSuccessful(false);
				    			checkmsg.setMessage("不满足数据类型匹配校验！");
				    			break;
				    		}
				    			
				    		
				    	}
				    }
			  }
				
			}
		}
		
		return checkmsg;
	
	}
	
}
