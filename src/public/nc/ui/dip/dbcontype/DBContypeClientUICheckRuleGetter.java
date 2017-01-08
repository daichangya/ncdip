package nc.ui.dip.dbcontype;
import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

public class DBContypeClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {


	/**
	 * <b> 前台校验类的Getter类 </b>
	 *
	 * <p>
	 *     在此处添加此类的描述信息
	 * </p>
	 *
	 *
	 * @author author
	 * @version tempProject 1.0
	 */

	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.dbcontype.DBConTypeClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return null;
	}


}
