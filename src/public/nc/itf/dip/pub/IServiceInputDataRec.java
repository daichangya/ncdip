package nc.itf.dip.pub;
//发部成webservice，数据同步那里调用
public interface IServiceInputDataRec {
	/**
	 * @author wyd
	 * @desc 取webserviec
	 * @return 执行预警成功与否
	 * */
	public String callData(String prop);
}
