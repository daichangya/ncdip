package nc.ui.dip.effectdef;

/**
 * 
 * 抽象业务代理类的缺省实现
 * 
 * @author 何冰
 * @version tempProject version
 */
public class MyDelegator extends AbstractMyDelegator {

	/**
	 * 
	 * 
	 * 该方法用于获取查询条件，用户可以对其进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {
		return super.getBodyCondition(bodyClass, key);
	}

}