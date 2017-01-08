package nc.impl.dip.ini;

import nc.bs.framework.common.NCLocator;
import nc.bs.sm.accountmanage.IUpdateAccount;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.FolderObject;
import nc.vo.pub.core.ObjectNode;
//nc.impl.dip.ini.InsertDicUpgradeImpl
public class InsertDicUpgradeImpl implements IUpdateAccount {

	public void doAfterUpdateData(String oldVersion, String newVersion)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void doBeforeUpdateDB(String oldVersion, String newVersion)
			throws Exception {
		String[] id=new String[]{"sjjkpt","dip","jkpt"};
		String[] parentid=new String[]{"-1Datadict","sjjkpt","sjjkpt"};
		String[] disname=new String[]{"数据接口平台","基础数据","接口平台"};
//		insert into pub_datadict (DISPLAY, DR, GUID, ID, KIND, PARENTGUID, PROP, TS)
//		values ('基础数据', 0, 'dip', 'dip', 'Folder', 'sjjkpt', null, '2011-08-18 16:30:36')
//		values ('接口平台', 0, 'jkpt', 'jkpt', 'Folder', 'sjjkpt', null, '2011-06-28 20:51:29')
//		values ('数据接口平台', 0, 'sjjkpt', 'sjjkpt', 'Folder', '-1Datadict', null, '2011-08-18 16:30:21')

		IBizObjStorage storage = (IBizObjStorage) NCLocator.getInstance().lookup(
				IBizObjStorage.class.getName());
		for(int i=0;i<3;i++){
		ObjectNode node=new FolderNode();
		FolderObject fo=new FolderObject();
		fo.setNode(node);
		fo.setDisplayName(disname[i]);
		fo.setGUID(id[i]);
		fo.setID(id[i]);
		fo.setKind("Folder");
		fo.setParentGUID(parentid[i]);


		storage.saveObject(IContrastUtil.DESIGN_CON, "nc.bs.pub.ddc.datadict.DatadictStorage", node, fo);
		}

	}

	public void doBeforeUpdateData(String oldVersion, String newVersion)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
