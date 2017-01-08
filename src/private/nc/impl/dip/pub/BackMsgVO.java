package nc.impl.dip.pub;

import java.io.Serializable;

public class BackMsgVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2816194130590424003L;
	public String success;
	public String unitcode;
	/**
	 * 存储在 SERVER 端的结果文件
	 */
	public String resultFile;
	/**
	 * 对应NC的单据PK，一般为空，需要自行添加
	 */
	public String billPk;
	/**
	 * 存储在 XX_IDCONTRASTPK 中
	 */
	public String bdocId;
	/**
	 * 备份在 SERVER 端的文件
	 */
	public String xmlFile;
	/**
	 * 返回代码，参考 TResult.RESULT_OK
	 */
	public String resultCode;
	/**
	 * 执行结果描述
	 */
	public String resultDesc;
	/**
	 * 内容
	 */
	public String content;
	
	/**
	 * 存储在 XX_IDCONTRASTPK 中
	 */
	public String getBdocId() {
		return bdocId;
	}
	/**
	 * 存储在 XX_IDCONTRASTPK 中
	 */
	public void setBdocId(String bdocId) {
		this.bdocId = bdocId;
	}
	/**
	 *  对应NC的单据PK，一般为空，需要自行添加
	 */
	public String getBillPk() {
		return billPk;
	}
	/**
	 *  对应NC的单据PK，一般为空，需要自行添加
	 */
	public void setBillPk(String billPk) {
		this.billPk = billPk;
	}
	/**
	 * 内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 返回代码，参考 TResult.RESULT_OK
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * 返回代码，参考 TResult.RESULT_OK
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * 执行结果描述
	 */
	public String getResultDesc() {
		return resultDesc;
	}
	/**
	 * 执行结果描述
	 */
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	/**
	 * 存储在 SERVER 端的结果文件
	 */
	public String getResultFile() {
		return resultFile;
	}
	/**
	 * 存储在 SERVER 端的结果文件
	 */
	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}
	/**
	 * 备份在 SERVER 端的文件
	 */
	public String getXmlFile() {
		return xmlFile;
	}
	/**
	 * 备份在 SERVER 端的文件
	 */
	public void setXmlFile(String xmlFile) {
		this.xmlFile = xmlFile;
	}
	public String isSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getUnitcode() {
		return unitcode;
	}
	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}
}
