package nc.bs.dip.fun;

import java.io.Serializable;
import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;

import nc.ui.pub.beans.MessageDialog;
import nc.util.dip.sj.LRUCache;
import nc.vo.pub.BusinessException;

public class FormmulaCache implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 676756746765L;
	public static  LRUCache<String, String> cache=null;
	public static FormmulaCache object=null;
	public  int k=0;//缓存大小。
	public static int count=0;
	public String formmulaFlag=null;//是否启用公式缓存。yes表示启用公式缓存，no表示不启用公式缓存。
	private String[] blackName=null;//公式缓存黑名单
	private IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public static FormmulaCache init(){
		if(object==null){
		 object=new FormmulaCache();
		}
		return object;
	}
	public  void initcache(){
		
		String sql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000007' and nvl(dr,0)=0 ";
		try {
			String count=iq.queryfield(sql);
			if(count!=null&&count.length()>0){
				String regex="[0-9]*";
				boolean flag=count.matches(regex);
				if(!flag){
					//MessageDialog.showErrorDlg(null, "错误", "缓存数量大小参数填写错误！");
					k=100;
				}
				k=Integer.parseInt(count.trim());	
			}
			
			if(!(k>0)){
				//MessageDialog.showErrorDlg(null, "错误", "缓存数量大小参数填写错误！");
				k=100;
			}
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void initCacheBlackName(){
		String sql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000010' and nvl(dr,0)=0 ";
		try {
			String value=iq.queryfield(sql);
			
			if(value!=null&&value.length()>0){
				value=value.toLowerCase();
				if(value.contains(",")){
					blackName=value.split(",");
				}else{
					blackName=new String[1];
					blackName[0]=value;
				}
				
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String initCacheFormmulaYesOrNo(){
		String sql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000009' and nvl(dr,0)=0 ";
		try {
			String value=iq.queryfield(sql);
			if(value!=null&&value.length()>0&&value.trim().equals("true")){
				formmulaFlag="yes";
			}else{
				formmulaFlag="no";
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formmulaFlag;
	}
	
	public  LRUCache<String, String> getInstance(){
		if(cache==null){
			initcache();
			initCacheBlackName();
			initCacheFormmulaYesOrNo();
			cache=new LRUCache<String, String>(k);
		}else{
			return cache;
		}
		return cache;
	}
	public  void put(String key,String value){
			if(blackName!=null&&key!=null&&!key.equals("")){
				boolean ff=comp(blackName, key.toLowerCase());
				if(!ff){
					getInstance().put(key, value);
					if(count<k){
						count++;	
					}
					
				}
			}
	}
	
	private boolean comp(String[] ss,String key){
		boolean ff=false;
		for(int i=0;i<ss.length;i++){
			if(key.equals(ss[i])||key.startsWith(ss[i])){
				 ff=true;//如果key中包含黑名单函数，则不把公式加入缓存。
				 break;
			}
			
		}
		return ff;
	}
	public  String get(String key){
		return getInstance().get(key);
	}
	public  void clear(){
		if(getInstance()!=null){
			cache=null;	
		}
		count=0;
		formmulaFlag=null;
		blackName=null;
	}
	
	public int getCount(){
		return count;
	}
	  
}
