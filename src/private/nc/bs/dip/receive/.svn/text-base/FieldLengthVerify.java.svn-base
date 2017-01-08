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

public class FieldLengthVerify implements ICheckPlugin{
	


	public CheckMessage doCheck(Object... ob) {
		// TODO Auto-generated method stub

		
		
		CheckMessage checkmsg=new CheckMessage();
		String msg=(String) ob[0];
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
			Integer length=null;
			  for(int j=0;j<defbvoList.size();j++){
				  if(ename.equals(defbvoList.get(j).getEname())){
					 type=defbvoList.get(j).getType();
					 length=defbvoList.get(j).getLength();
					 break;
				  };
			  
			  }
			  Logger.debug("----esb接收数据字段长度校验---实际字段值 ="+str+", 实际字段在表中的ename="+ename+",type==="+type+" length===="+length);		  
			  if(str!=null){
				  
				  if("INTEGER".equals(type.toUpperCase())){
				    	String pattern = "[0-9]*";
				    	if(str.matches(pattern)){
				    		//数据类型匹配。
				    		checkmsg.setSuccessful(true);
				    	}else{
				    	 //数据类型不匹配。	
				    		checkmsg.setSuccessful(false);
				    		checkmsg.setMessage("不满足数据类型长度校验！");
				    	    break;
				    	}
				    	
				    }else if(type.toUpperCase().indexOf("CHAR")>=0){
				    	if(str.length()<=length){
				    		checkmsg.setSuccessful(true);
				    	}else{
				    		//匹配不成功。
				    		checkmsg.setSuccessful(false);
				    		checkmsg.setMessage("不满足数据类型长度校验！");
				    	    break;
				    	};
				    	
				    }else if("NUMBER".equals(type.toUpperCase())){
				    	if(str.indexOf(".")>=1){
				    		String[] num=str.split(".");
				    		if(num.length==2){
				    			String pattern="[0-9]*";
				    			if(num[0].matches(pattern)&&num[1].matches(pattern)){
				                    //数据类型匹配成功		。	
				    				if(num[0].length()<=12&&num[1].length()<=8){
				    					//长度匹配成功。
				    					checkmsg.setSuccessful(true);
				    				}else{
				    					//长度匹配不成功。
				    					checkmsg.setSuccessful(false);
							    		checkmsg.setMessage("不满足数据类型长度校验！");
							    	    break;
				    				}
				    			}else{
				    				//数据类型匹配不成功。
				    				checkmsg.setSuccessful(false);
						    		checkmsg.setMessage("不满足数据类型长度校验！");
						    	    break;
				    			}
				    		}else{
				    			checkmsg.setSuccessful(false);
					    		checkmsg.setMessage("不满足数据类型长度校验！");
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
////		##databegin##系统标准0##站点标志##业务标志##1##2##3##4##5##dataend##
//	//	String separator=null;//分隔符号 例如 ##
//		String palaceholder=null;//占位符 例如 ok
//		String syscode=null;//系统标志
//		String zdbz=null;//站点标志
//		String busicode=dvo.getYw();//业务标志
//		String begin=dvo.getBeginFlag();//databegin
//		String end=dvo.getEndFlag();//dataend
//		dvo.getJsl();//head+","+syscode+","+sysname+","+busicode+","+end
//		Map<String,String> fieldType=dvo.getFiledType(); //<ename,type>
//		Map<String,Integer> fieldLen=dvo.getFieldLen();//<ename,length>
//		Map<Integer,String> mapb=dvo.getBmap();//<code,ename>
//		String esbStr[]=msg.split(separator);
//		for(int i=0;i<mapb.values().size();i++){
//			int k=5+i;
//			String ename=mapb.get(k);//这个是数据库中存data的第一个英文字段名。
//		    String type=fieldType.get(ename);//这个是ename的字段类型。integer，char，data等等。
//		    Integer length=fieldLen.get(ename);//这个是ename的字段类型的长度。
//		    String str=esbStr[k+1];//实际接收的这个ename要存的值。
//			 if(str!=null){
//				  if("INTEGER".equals(type.toUpperCase())){
//				    	String pattern = "[0-9]*";
//				    	if(str.matches(pattern)){
//				    		//数据类型匹配。
//				    		checkmsg.setSuccessful(true);
//				    	}else{
//				    	 //数据类型不匹配。	
//				    		checkmsg.setSuccessful(false);
//				    		checkmsg.setMessage("不满足数据类型长度校验！");
//				    	    break;
//				    	}
//				    	
//				    }else if(type.toUpperCase().indexOf("CHAR")>=0){
//				    	if(str.length()<=length){
//				    		checkmsg.setSuccessful(true);
//				    	}else{
//				    		//匹配不成功。
//				    		checkmsg.setSuccessful(false);
//				    		checkmsg.setMessage("不满足数据类型长度校验！");
//				    	    break;
//				    	};
//				    	
//				    }else if("NUMBER".equals(type.toUpperCase())){
//				    	if(str.indexOf(".")>=1){
//				    		String[] num=str.split(".");
//				    		if(num.length==2){
//				    			String pattern="[0-9]*";
//				    			if(num[0].matches(pattern)&&num[1].matches(pattern)){
//				                    //数据类型匹配成功		。	
//				    				if(num[0].length()<=20&&num[1].length()<=8){
//				    					//长度匹配成功。
//				    					checkmsg.setSuccessful(true);
//				    				}else{
//				    					//长度匹配不成功。
//				    					checkmsg.setSuccessful(false);
//							    		checkmsg.setMessage("不满足数据类型长度校验！");
//							    	    break;
//				    				}
//				    			}else{
//				    				//数据类型匹配不成功。
//				    				checkmsg.setSuccessful(false);
//						    		checkmsg.setMessage("不满足数据类型长度校验！");
//						    	    break;
//				    			}
//				    		}else{
//				    			checkmsg.setSuccessful(false);
//					    		checkmsg.setMessage("不满足数据类型长度校验！");
//					    	    break;
//				    		}
//				    			
//				    		
//				    	}
//				    }
//			  }
		 
		    
		//return null;
//	}
//}
