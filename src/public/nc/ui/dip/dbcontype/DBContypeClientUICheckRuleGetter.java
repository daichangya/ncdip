package nc.ui.dip.dbcontype;
import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

public class DBContypeClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {


	/**
	 * <b> ǰ̨У�����Getter�� </b>
	 *
	 * <p>
	 *     �ڴ˴���Ӵ����������Ϣ
	 * </p>
	 *
	 *
	 * @author author
	 * @version tempProject 1.0
	 */

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.dbcontype.DBConTypeClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}


}
