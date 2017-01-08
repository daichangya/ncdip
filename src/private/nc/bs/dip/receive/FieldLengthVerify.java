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
		String separator=esbvo.getBj();//�ָ����� ���� ##
//		##databegin##ϵͳ��׼0##վ���־##ҵ���־##1##2##3##4##5##dataend##
		for(int i=4;i<formatbvoList.size();i++){
			DataformatdefBVO bvo=formatbvoList.get(i);
			if(bvo.getVdef2()!=null&&"ҵ������".equals(bvo.getVdef2())){//ռλ����
			String str=	msg.split(separator)[i+1];//ʵ�ʵ��ֶ�ֵ��
			String ename=bvo.getDatakind();//ʵ���ֶ��ڱ��е�ename
			String type=null;
			Integer length=null;
			  for(int j=0;j<defbvoList.size();j++){
				  if(ename.equals(defbvoList.get(j).getEname())){
					 type=defbvoList.get(j).getType();
					 length=defbvoList.get(j).getLength();
					 break;
				  };
			  
			  }
			  Logger.debug("----esb���������ֶγ���У��---ʵ���ֶ�ֵ ="+str+", ʵ���ֶ��ڱ��е�ename="+ename+",type==="+type+" length===="+length);		  
			  if(str!=null){
				  
				  if("INTEGER".equals(type.toUpperCase())){
				    	String pattern = "[0-9]*";
				    	if(str.matches(pattern)){
				    		//��������ƥ�䡣
				    		checkmsg.setSuccessful(true);
				    	}else{
				    	 //�������Ͳ�ƥ�䡣	
				    		checkmsg.setSuccessful(false);
				    		checkmsg.setMessage("�������������ͳ���У�飡");
				    	    break;
				    	}
				    	
				    }else if(type.toUpperCase().indexOf("CHAR")>=0){
				    	if(str.length()<=length){
				    		checkmsg.setSuccessful(true);
				    	}else{
				    		//ƥ�䲻�ɹ���
				    		checkmsg.setSuccessful(false);
				    		checkmsg.setMessage("�������������ͳ���У�飡");
				    	    break;
				    	};
				    	
				    }else if("NUMBER".equals(type.toUpperCase())){
				    	if(str.indexOf(".")>=1){
				    		String[] num=str.split(".");
				    		if(num.length==2){
				    			String pattern="[0-9]*";
				    			if(num[0].matches(pattern)&&num[1].matches(pattern)){
				                    //��������ƥ��ɹ�		��	
				    				if(num[0].length()<=12&&num[1].length()<=8){
				    					//����ƥ��ɹ���
				    					checkmsg.setSuccessful(true);
				    				}else{
				    					//����ƥ�䲻�ɹ���
				    					checkmsg.setSuccessful(false);
							    		checkmsg.setMessage("�������������ͳ���У�飡");
							    	    break;
				    				}
				    			}else{
				    				//��������ƥ�䲻�ɹ���
				    				checkmsg.setSuccessful(false);
						    		checkmsg.setMessage("�������������ͳ���У�飡");
						    	    break;
				    			}
				    		}else{
				    			checkmsg.setSuccessful(false);
					    		checkmsg.setMessage("�������������ͳ���У�飡");
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
////		##databegin##ϵͳ��׼0##վ���־##ҵ���־##1##2##3##4##5##dataend##
//	//	String separator=null;//�ָ����� ���� ##
//		String palaceholder=null;//ռλ�� ���� ok
//		String syscode=null;//ϵͳ��־
//		String zdbz=null;//վ���־
//		String busicode=dvo.getYw();//ҵ���־
//		String begin=dvo.getBeginFlag();//databegin
//		String end=dvo.getEndFlag();//dataend
//		dvo.getJsl();//head+","+syscode+","+sysname+","+busicode+","+end
//		Map<String,String> fieldType=dvo.getFiledType(); //<ename,type>
//		Map<String,Integer> fieldLen=dvo.getFieldLen();//<ename,length>
//		Map<Integer,String> mapb=dvo.getBmap();//<code,ename>
//		String esbStr[]=msg.split(separator);
//		for(int i=0;i<mapb.values().size();i++){
//			int k=5+i;
//			String ename=mapb.get(k);//��������ݿ��д�data�ĵ�һ��Ӣ���ֶ�����
//		    String type=fieldType.get(ename);//�����ename���ֶ����͡�integer��char��data�ȵȡ�
//		    Integer length=fieldLen.get(ename);//�����ename���ֶ����͵ĳ��ȡ�
//		    String str=esbStr[k+1];//ʵ�ʽ��յ����enameҪ���ֵ��
//			 if(str!=null){
//				  if("INTEGER".equals(type.toUpperCase())){
//				    	String pattern = "[0-9]*";
//				    	if(str.matches(pattern)){
//				    		//��������ƥ�䡣
//				    		checkmsg.setSuccessful(true);
//				    	}else{
//				    	 //�������Ͳ�ƥ�䡣	
//				    		checkmsg.setSuccessful(false);
//				    		checkmsg.setMessage("�������������ͳ���У�飡");
//				    	    break;
//				    	}
//				    	
//				    }else if(type.toUpperCase().indexOf("CHAR")>=0){
//				    	if(str.length()<=length){
//				    		checkmsg.setSuccessful(true);
//				    	}else{
//				    		//ƥ�䲻�ɹ���
//				    		checkmsg.setSuccessful(false);
//				    		checkmsg.setMessage("�������������ͳ���У�飡");
//				    	    break;
//				    	};
//				    	
//				    }else if("NUMBER".equals(type.toUpperCase())){
//				    	if(str.indexOf(".")>=1){
//				    		String[] num=str.split(".");
//				    		if(num.length==2){
//				    			String pattern="[0-9]*";
//				    			if(num[0].matches(pattern)&&num[1].matches(pattern)){
//				                    //��������ƥ��ɹ�		��	
//				    				if(num[0].length()<=20&&num[1].length()<=8){
//				    					//����ƥ��ɹ���
//				    					checkmsg.setSuccessful(true);
//				    				}else{
//				    					//����ƥ�䲻�ɹ���
//				    					checkmsg.setSuccessful(false);
//							    		checkmsg.setMessage("�������������ͳ���У�飡");
//							    	    break;
//				    				}
//				    			}else{
//				    				//��������ƥ�䲻�ɹ���
//				    				checkmsg.setSuccessful(false);
//						    		checkmsg.setMessage("�������������ͳ���У�飡");
//						    	    break;
//				    			}
//				    		}else{
//				    			checkmsg.setSuccessful(false);
//					    		checkmsg.setMessage("�������������ͳ���У�飡");
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
