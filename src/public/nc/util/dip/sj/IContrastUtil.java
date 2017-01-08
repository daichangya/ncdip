package nc.util.dip.sj;

import java.util.HashMap;
import java.util.Map;

import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.sysregister.DipSysregisterBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;

public class IContrastUtil {
	//VERSION��ֵ������nc502����nc507��
	public final static String VERSION="nc507";
	public final static String DATAPROCESS_ERR="����";
	public final static String DATAPROCESS_HINT="��ʾ";
	public final static String DESIGN_CON="ncdip";
	
	public final static Integer WH_ADD=0;//���ݶ���ά��������
	public final static Integer WH_REMOVE=1;//���ݶ���ά����ɾ��
	
	public final static String TABMONSTA_ON="jxz";//��״̬��ص�������
	public final static String TABMONSTA_FIN="finash";//��״̬��ص��������
	
	
	public final static String YWLX_DY="dy";//���ݶ���
	public final static String YWLX_JS="js";//���ݽ���
	public final static String YWLX_TB="tb";//����ͬ��
	public final static String YWLX_DZ="dz";//���ݶ��ն���
	public final static String YWLX_JG="jg";//���ݼӹ�
	public final static String YWLX_ZH="zh";//����ת��
	public final static String YWLX_FS="fs";//���ݷ���
	public final static String YWLX_ESBSEND="esbsend";//�ļ�����
	public final static String YWLX_LC="lc";//��������
	public final static String YWLX_SJQX="sjqx";//����Ȩ�޶���
	public final static String YWLX_TYZH="tyzh";//ͨ��ת����
	public final static String YWLX_TYZHXML="tyzhxml";//ͨ��XMLת����
	public final static String YWLX_ZTYZ="ztyz";//״̬Ԥ��
	public final static String SYSCODE="SYS001";//��д�����ݶ����ļ���
	public final static String SYSNAME="ϵͳ�ļ���";//��д�����ݶ����ļ���
	
	public final static String LOG_ALL="ALL";//add by zhw 2012-05-18 DIP��¼��־����
	public final static String LOG_ERROR="ERROR";//add by zhw 2012-05-18 DIP��¼��־����
	public final static String LOG_SUCESS="SUCESS";//add by zhw 2012-05-18 DIP��¼��־����
	
	public final static String ESCS="$,(,),*,+,.,[,],?,\\,^,{,},|";//ת���ַ�
	



//add by liyunzhe 2012-06-06 start   ��Դ���ͻ��������޸�

	/**
	 *0001AA10000000021R3Y    ��ʽ�ļ�
	 */
	public final static String DATAORIGIN_GSWJ="0001AA10000000021R3Y";
	/**
	 * 0001AA1000000001NY7F    �м��
	 */
	public final static String DATAORIGIN_ZJB="0001AA1000000001NY7F";
	/**
	 * 0001AA10000000013SVH    ����ץȡ
	 */
	public final static String DATAORIGIN_ZDZQ="0001AA10000000013SVH";
	/**
	 * 0001AA10000000013SVI    ��Ϣ����
	 */
	public final static String DATAORIGIN_XXZX="0001AA10000000013SVI";
	/**
	 * 0001AA1000000003493X    webserviceץȡ	
	 */
	public final static String DATAORIGIN_WEBSERVICEZQ="0001AA1000000003493X";
//	add by liyunzhe 2012-06-06 end
	/** ���ݼӹ����͵��� ���� 0001AA1000000002G4UD������������ж��*/
	public final static String PROCESSSTYLE_SJXZ="0001AA1000000002G4UD";
	/** ���ݼӹ����͵��� ����0001AA1000000002TJVJ �������� �Զ���*/
	public final static String PROCESSSTYLE_ZDY="0001AA1000000002TJVJ";
	/** ���ݼӹ����͵��� ����0001AA1000000003HBMO �������� ״̬Ԥ��*/
	public final static String PROCESSSTYLE_ZTYZ="0001AA1000000003HBMO";
	/** �ظ����ݿ��� 0001AA10000000018UZ1  �����Ѵ������� */
	public final static String REPEATDATA_CONTROL_HULUE="0001AA10000000018UZ1";
	/** �ظ����ݿ��� 0001ZZ10000000018K6M �����Ѵ������� */
	public final static String REPEATDATA_CONTROL_FUGAI="0001ZZ10000000018K6M";
	
	
    /**	1	01	������Ϣ�ṹ	0001BB100000000JJOKC*/
	public final static String DATASTYLE_BASEINF="0001BB100000000JJOKC";
	/**	2	02	ҵ����Ϣ�ṹ	0001BB100000000JJOKM*/
	public final static String DATASTYLE_BUSINESSINF="0001BB100000000JJOKM";
	/**		3	03	ϵͳ��Ϣ�ṹ	0001BB100000000JKANO*/
	public final static String DATASTYLE_SYSTEMINF="0001BB100000000JKANO";
	/**		4	04	�ӹ���Ϣ�ṹ	0001BB100000000JKANP*/
	public final static String DATASTYLE_PROCESSINF="0001BB100000000JKANP";
	
	/** ���ݽӿ�ƽ̨д�����ֵ�ʱ���������ļ���������   ���ݽӿ�ƽ̨*/
	public final static String DATADICTFATHER_NAME="���ݽӿ�ƽ̨";
	/** ���ݽӿ�ƽ̨д�����ֵ�ʱ���������ļ��б�����  sjjkpt*/
	public final static String DATADICTFATHER_CODE="sjjkpt";
	/** ���ݽӿ�ƽ̨д�����ֵ�ʱ��ڶ�����ļ��������� �ӿ�ƽ̨*/
	public final static String DATADICTFORDER_NAME="�ӿ�ƽ̨";
	/** ���ݽӿ�ƽ̨д�����ֵ�ʱ��ڶ�����ļ��б����� jkpt*/
	public final static String DATADICTFORDER_CODE="jkpt";
	
	/**����ͬ����������ץȡ������ʾ��������*/
	public final static String DATAORIGIN_ZDZQ_PARAMETAR="����Դ,�û���,����";
	/**����ͬ��������Ϣ���� �ļ��� �����������ʾ��������*/
	public final static String DATAORIGIN_XXZX_IN_FILE_PARAMETAR="�ļ�����";
	/**����ͬ�������ʽ�ļ�������ʾ��������*/
	public final static String DATAORIGIN_GSWJ_PARAMETAR="�ļ�����";
	/**����ͬ������ �м�� ������ʾ��������*/
	public final static String DATAORIGIN_ZJB_PARAMETAR="����Դ,�û���,����";
	/**����ͬ������ webservicץȡ ������ʾ��������*///ϵͳ��ʶ��վ���־��ҵ���ʶ��Ψһ��ʶ,ȡ������,�ڼ���ȡ��
//	public final static String DATAORIGIN_WEBSERVICEZQ_PARAMETAR="ϵͳ��ʶ,վ���־,ҵ���ʶ,Ψһ��ʶ,ȡ������,�ڼ���ȡ��";
	public final static String DATAORIGIN_WEBSERVICEZQ_PARAMETAR="����Դ,�û���,����";
	/**webserviceץȡ��ͬ��ʱ���ҳ��С */
	public final static int DATAORIGIN_WEBSERVICEZQ_PAGESIZE=5000;
	
	
	
	//ÿ�����͵�PK�ֶ�
	private static Map<String,String> pkMap=new HashMap<String,String>();
	public static Map getPkMap(){
		if(pkMap==null||pkMap.size()<=0){
			pkMap=new HashMap<String,String>();
			pkMap.put(YWLX_DY, "pk_datadefinit_h");
			pkMap.put(YWLX_JS, "pk_datarec_h");
			pkMap.put(YWLX_TB, "pk_datasynch");
			pkMap.put(YWLX_DZ, "pk_contdata_h");
			pkMap.put(YWLX_JG, "pk_dataproce_h");
			pkMap.put(YWLX_ZH, "pk_datachange_h");
			pkMap.put(YWLX_FS, "pk_datasend");
			pkMap.put(YWLX_ESBSEND, "pk_esbsend");
			pkMap.put(YWLX_LC, "pk_processflow_h");
			pkMap.put(YWLX_TYZH, "pk_tyzhq_h");
			pkMap.put(YWLX_SJQX, "pk_contdata_h");
			pkMap.put(YWLX_ZTYZ, "pk_statemanage_h");
			pkMap.put(YWLX_TYZHXML, "pk_tyxml_h");
		}
		return pkMap;
	}
	//ÿ�����͵ĸ������ֶ�
	private static Map<String,String> fpkMap=new HashMap<String,String>();
	public static Map getFpkMap(){

		if(fpkMap==null||fpkMap.size()<=0){
			fpkMap=new HashMap<String,String>();
					fpkMap.put(YWLX_DY, "PK_SYSREGISTER_H".toLowerCase());
					fpkMap.put(YWLX_LC, "fpk");
					fpkMap.put(YWLX_JS, "fpk");
					fpkMap.put(YWLX_TB, "fpk");
					fpkMap.put(YWLX_ZH, "fpk");
					fpkMap.put(YWLX_JG, "fpk");
					fpkMap.put(YWLX_FS, "pk_sys");
					fpkMap.put(YWLX_DZ, "pk_sysregister_h");
					fpkMap.put(YWLX_TYZH, "fpk");
					fpkMap.put(YWLX_ESBSEND, "pk_sys");
					fpkMap.put(YWLX_SJQX, "pk_sysregister_h");
					fpkMap.put(YWLX_ZTYZ, "fpk");
					fpkMap.put(YWLX_TYZHXML, "fpk");
		}
		return fpkMap;
	}
	//ÿ�����͵����ݱ������ֶ�
	private static Map<String,String> tabpkMap=new HashMap<String,String>();
	public static Map getTabpkMap(){
		if(tabpkMap==null||tabpkMap.size()<=0){
			tabpkMap=new HashMap<String,String>();
					tabpkMap.put(YWLX_DY, "memorytable");
					tabpkMap.put(YWLX_LC, "");
					tabpkMap.put(YWLX_JS, "memorytable");
					tabpkMap.put(YWLX_TB, "datasite");
					tabpkMap.put(YWLX_ZH, "busidata");
					tabpkMap.put(YWLX_JG, "firsttab");
					//2011-5-30
					tabpkMap.put(YWLX_FS, "filepath");
					tabpkMap.put(YWLX_DZ, "contabcode");
					tabpkMap.put(YWLX_TYZH, "sourcetab");
					tabpkMap.put(YWLX_ESBSEND, "filepath");
					tabpkMap.put(YWLX_TYZHXML, "sourcetab");
		}
		return tabpkMap;
	}
	private static Map<String,String> tabNameMap=new HashMap<String,String>();
	public static Map getTabNameMap(){

		if(tabNameMap==null||tabNameMap.size()<=0){
			tabNameMap=new HashMap<String,String>();
					tabNameMap.put(YWLX_DY, "dip_datadefinit_h");
					tabNameMap.put(YWLX_LC, "dip_processflow_h");
					tabNameMap.put(YWLX_JS, "dip_datarec_h");
					tabNameMap.put(YWLX_TB, "dip_datasynch");
					tabNameMap.put(YWLX_ZH, "dip_datachange_h");
					tabNameMap.put(YWLX_JG, "dip_dataproce_h");
					tabNameMap.put(YWLX_FS, "dip_datasend");
					tabNameMap.put(YWLX_DZ, "dip_contdata");
					tabNameMap.put(YWLX_TYZH, "dip_tyzhq_h");
					tabNameMap.put(YWLX_ESBSEND, "dip_esbsend");
					tabNameMap.put(YWLX_SJQX, "dip_adcontdata");
					tabNameMap.put(YWLX_ZTYZ, "dip_statemanage_h");
					tabNameMap.put(YWLX_TYZHXML, "dip_tyxml_h");
		}
		return tabNameMap;
	}
		//���ƴ���״̬
/*	private static Map<String,String> tabStartMap=new HashMap<String,String>();
	public static Map getTabStartMap(){

		if(tabStartMap==null||tabStartMap.size()<=0){
			tabStartMap=new HashMap<String,String>();
					tabStartMap.put(YWLX_DY, "dispostatus");
					tabStartMap.put(YWLX_LC, "");
					tabStartMap.put(YWLX_JS, "constatus");
					tabStartMap.put(YWLX_TB, "constatus");
					tabStartMap.put(YWLX_ZH, "constatus");
					tabStartMap.put(YWLX_JG, "constatus");
					tabStartMap.put(YWLX_FS, "contorlstatus");
		}
		return tabStartMap;
	
	}*/
		//���ƽ���״̬
	/*private static Map<String,String> tabEndMap=new HashMap<String,String>();
	public static Map getTabEndMap(){
		if(tabEndMap==null||tabEndMap.size()<=0){
			tabEndMap=new HashMap<String,String>();
				tabEndMap.put(YWLX_DY, "endstatus");
				tabEndMap.put(YWLX_LC, "");
				tabEndMap.put(YWLX_JS, "endstatus");
				tabEndMap.put(YWLX_TB, "endstatus");
				tabEndMap.put(YWLX_ZH, "endstatus");
				tabEndMap.put(YWLX_JG, "endstatus");
				tabEndMap.put(YWLX_FS, "endstatus");
		}
		return tabEndMap;
	}*/
//		ҵ������
	private static Map<String,String> tabName=new HashMap<String,String>();
	public static Map getTabName(){
		if(tabName==null||tabName.size()<=0){
			tabName=new HashMap<String,String>();
					tabName.put(YWLX_DY, "���ݶ���");
					tabName.put(YWLX_LC, "��������");
					tabName.put(YWLX_JS, "���ݽ��ն���");
					tabName.put(YWLX_TB, "����ͬ��");
					tabName.put(YWLX_ZH, "����ת��");
					tabName.put(YWLX_JG, "���ݼӹ�");
					tabName.put(YWLX_FS, "���ݷ���");
					tabName.put(YWLX_DZ, "���ݶ���");
					tabName.put(YWLX_TYZH, "ͨ��ת��������");
					tabName.put(YWLX_ESBSEND, "�ļ�����");
					tabName.put(YWLX_TYZHXML, "ͨ��XMLת��");
		}
		return tabName;
	}
		//����ҵ�����͵������ֶ�
	private static Map<String,String> ywName=new HashMap<String,String>();
	public static Map getYwName(){
		if(ywName==null||ywName.size()<=0){
			ywName=new HashMap<String,String>();
				ywName.put(YWLX_DY, "sysname");//���ݶ���
				ywName.put(YWLX_LC, "name");//��������
				ywName.put(YWLX_JS, "recname");//���ݶ��ն���
				ywName.put(YWLX_TB, "name");//����ͬ��
				ywName.put(YWLX_ZH, "name");//����ת��
				ywName.put(YWLX_JG, "name");//���ݼӹ�
				ywName.put(YWLX_FS, "name");//���ݷ���
				ywName.put(YWLX_DZ, "name");//���ݷ���
				ywName.put(YWLX_TYZH, "name");//���ݷ���
				ywName.put(YWLX_ESBSEND, "name");//���ݷ���
				ywName.put(YWLX_SJQX, "name");
				ywName.put(YWLX_ZTYZ, "name");
				ywName.put(YWLX_TYZHXML, "name");
			
		}
		return ywName;
	}
//		����ҵ�����͵ı����ֶ�
	private static Map<String,String> ywbmName=new HashMap<String,String>();
	public static Map getYwbmName(){
		if(ywbmName==null||ywbmName.size()<=0){
			 ywbmName=new HashMap<String,String>();
						ywbmName.put(YWLX_DY, "syscode");//���ݶ���
						ywbmName.put(YWLX_LC, "code");//��������
						ywbmName.put(YWLX_JS, "syscode");//���ݶ��ն���
						ywbmName.put(YWLX_TB, "code");//����ͬ��
						ywbmName.put(YWLX_ZH, "code");//����ת��
						ywbmName.put(YWLX_JG, "code");//���ݼӹ�
						ywbmName.put(YWLX_FS, "code");//���ݷ���
						ywbmName.put(YWLX_DZ, "code");//���ݷ���
						ywbmName.put(YWLX_TYZH, "code");//���ݷ���
						ywbmName.put(YWLX_ESBSEND, "code");//���ݷ���
						ywbmName.put(YWLX_SJQX, "code");
						ywbmName.put(YWLX_ZTYZ, "code");
						ywbmName.put(YWLX_TYZHXML, "code");
		}
		return ywbmName;
	}


	
	
	//����������֮��Ӧ�Ĳ��յ�map
	private static Map<String ,String> docRefMap=new HashMap<String, String>();
	public static Map getDocRefMap(){
		if(docRefMap==null||docRefMap.size()<=0){
			docRefMap=new HashMap<String, String>();
			docRefMap.put("dip_datacheckregist", "<nc.ui.bd.ref.DataCheckRefModel>");//����У��ע����
			docRefMap.put("dip_statusregist", "<nc.ui.bd.ref.StatusRegistRefModel>");//����״̬ע��
			docRefMap.put("dip_sourceregist", "<nc.ui.bd.ref.SourceRegistRefModel>");//����Դע��
			docRefMap.put("dip_recstatus", "<nc.ui.bd.ref.RecStatusRefModel>");//���ݽ���״̬ά��
			docRefMap.put("dip_recformatdef_h","<nc.ui.bd.ref.RecFormatRefModel>");//���ݽ��ո�ʽά��
			docRefMap.put("dip_datastyle", "<nc.ui.bd.ref.DataStyleRefModel>");//��������
			docRefMap.put("dip_dataorigin","<nc.ui.bd.ref.DataOriginRefModel>");//������Դע��
			docRefMap.put("dataconsult","<nc.ui.bd.ref.DataConsultRefModel>");//�������
			docRefMap.put("dip_recserver", "<nc.ui.bd.ref.RecserverPathRefModel>");//NC���շ�����ע��
			docRefMap.put("dip_dataurl", "<nc.ui.bd.ref.DataUrlRefModel>");//����ͬ��URLע��
			docRefMap.put("dip_filepath", "<nc.ui.bd.ref.FilePathModel>");//�ļ�����·��ע��
			docRefMap.put("dip_filepath", "<nc.ui.bd.ref.FilePathRefModel>");//�ļ�����·��ע��
			docRefMap.put("dip_filepath", "<nc.ui.bd.ref.GSWJRefModel>");//�ļ�����·��ע��
			docRefMap.put("dip_messagelogo", "<nc.ui.bd.ref.MessageLogoRefModel>");//��Ϣ��־
			docRefMap.put("dip_messtype","<nc.ui.bd.ref.MessTypeRefModel>");//��Ϣ����
			docRefMap.put("dip_msr","<nc.ui.bd.ref.MessageRefModel>");//��Ϣ������ע��
			docRefMap.put("dip_repeatdata","<nc.ui.bd.ref.RepedtDataRefModel>");//�ظ����ݿ���
			docRefMap.put("dip_obtainsign","<nc.ui.bd.ref.ObtainSignRefModel>");//��ȡ��־
			docRefMap.put("dip_processstyle", "<nc.ui.bd.ref.ProcessStyleRefModel>");//�ӹ����͵���
			docRefMap.put("dip_returnmess_h","<nc.ui.bd.ref.ReturnMsgRefModel>");//��ִ�������
			docRefMap.put("dip_taskregister","<nc.ui.bd.ref.TaskRegisterRefMode>");//��������ע��
			docRefMap.put("dip_dbcontype","<nc.ui.bd.ref.ZJBRefModel>");//���ݿ������м��
			docRefMap.put("dip_dataurl","<nc.ui.bd.ref.ZDZQRefModel>");//����ͬ��URLע��
			docRefMap.put("dip_msr","<nc.ui.bd.ref.ZDJSRefModel>");//��Ϣ������ע��
		}
		return docRefMap;
	}
	
	public static String getCodeRepeatHint(String name,String code){
		return name+"��"+code+"��"+"�Ѿ����ڣ�";
	}
	public static String[] getSysSideBussinessCode(String pk_xt,String pk_datadefint_h){
		String str[]=new String[]{"","",""};
		try {
			DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_xt);
			DipDatadefinitHVO datadefinthvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk_datadefint_h);
			if(syshvo!=null){
				str[0]=syshvo.getExtcode();//ϵͳ��־
				str[1]=syshvo.getDef_str_1();//�̶���־
			}
			if(datadefinthvo!=null){
				if(datadefinthvo.getIsdeploy()!=null&&datadefinthvo.getIsdeploy().booleanValue()&&datadefinthvo.getDeploycode()!=null&&datadefinthvo.getDeploycode().trim().length()>0){
					String pk_deycode=datadefinthvo.getDeploycode();
					DipSysregisterBVO bvo=(DipSysregisterBVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterBVO.class,pk_deycode);
					str[1]=bvo.getSitecode();//վ���־ 
				}
				str[2]=datadefinthvo.getBusicode();//ҵ���־
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return str;	
	}
}
