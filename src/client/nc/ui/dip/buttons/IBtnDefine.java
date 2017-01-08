 package nc.ui.dip.buttons;

import nc.ui.dip.buttons.folder.AddFolderBtn;
import nc.ui.dip.buttons.folder.DeleteFolderBtn;
import nc.ui.dip.buttons.folder.EditFolderBtn;
import nc.ui.dip.buttons.folder.FolderManageBtn;
import nc.ui.dip.buttons.folder.MoveFolderBtn;

/*
 * �ýӿ����ڴ�Ű�ť����
 */
public interface IBtnDefine {
	
	//�������������ֹͣ ��ť����
	public final static int Start=102;
	public final static int Stop=103;
	
	//���ݶ��壺������
	public final static int CreateTable=104;
	
	//����ͬ����ͬ��������У�顢Ԥ��
	public final static int Synch=105;
	public final static int DataValidate=106;
	public final static int EarlyWarning=107;
	
	//Ԥ�������������á����ð�ť
	public final static int Disable=108;
	public final static int Enable=109;
	
	//���ݼӹ����ӹ���ť�����ư�ť�����ư�ť
	public final static int DataProce=110;
	
	//�������̣�����ִ�С�Ԥ����ť
	public final static int ProcessFlow=111;
	public final static int YuJing=112;
	
	//����ת����ģ�塢ת����ť
	public final static int Model=113;
	public final static int Conversion=114;
	
	//ƾ֤ģ�嶨�� ����ת��   �����Ӱ�����ء���ť��ƾ֤�ϲ�����
	public final static int AddEffectFactor=115;
	public final static int CREDENCE=133;
	public final static int HBSET=134;
	
	//���ݽ��ն��壺�����ݸ�ʽ����������У�顱����ģ�����á���ť
	public final static int DataStyle=116;
	public final static int DataCheck=117;
	public final static int ModelSZ=118;

	//Ԥ��ʱ�䰴ť
	public final static int WARNTIME=119;
	
	//���ݶ���ά����У���顢��������ģ�嵼�� ��ť
	public final static int VALIDATECHECK=120;
	public final static int DATACLEAR=121;
	public final static int EXPORTMODEL=122;
	
	//���ݼӹ������ư�ť�����ư�ť
	
	public final static int Movedup=123;
	public final static int Moveddown=124;
	
	//2011-5-14 ���ݶ���ά���� ����ϵͳ��ѯ���塢������ϵͳ��ѯ����
	public final static int CONTSYSQUERY=125;
	public final static int BCONTSYSQUERY=126;
	//����ά���ڵ㣺���水ť
	public final static int CONTSAVE=127;
	//���ս��ά��
	public final static int contresut = 129;
	
	
	//��������� ��ѯ
	public final static int DATALOOKQUERY=128;
	//���ݷ��ͣ����Ͱ�ť
	public final static int DATASEND=130;
	public final static int ADD=131;
	public final static int DELETE=132;
	
	//���ݶ���ά��:����
	public final static int SET=133;

	public final static int edit = 134;
	
	//����ת����ճ��ģ�塢ϵͳģ��
	public final static int PASTEMODEL=135;
	public final static int SYSMODEL=136;
	
	//����ESB���ͽ���
	public final static int TESTESBREC=137;
	public final static int TESTESBSEND=138;
	
	//���ݷ��� ǰ̨����
	public final static int SENDFORM=139;
	//����ͬ�� ǰ̨ͬ��
	public final static int TBFORM=140;
	//���ݶ���ά��  ����У�顣
	public final static int DATACHECK=141;
	//����
	public final static int MBZH=142;
	//���ݽ��ն���ĸ�ʽ����
	public final static int JSDYDATAFORMAT=143;
	
	
	//2011-7-20
	public final static int CONTROL=144;//����
	public final static int CLEARCACHE=145;//������
	
	//�������̵��в���
	public final static int LCLINE=146;
	//2011-7-13 ����ת�� �� ���Ӱ�����ء�ƾ֤�ϲ����á���¼�ϲ����ð�ť���ϵ�һ���У���3��ҳǩ
	public final static int LCLineadd=147;
	public final static int LCLinedel=148;
	public final static int LCLinecopy=149;
	public final static int LCLineins=150;
	public final static int LCLinepast=151;
	
	public final static int CRESET=152; //CreSetBtn;
	
	//���ݼӹ����У���鰴ť
	public final static int DataProcessCheck=153;//DataProcessCheckBtn
	
	//����Դע��Ĳ�������
	public final static int TESTCONNECT=154;//TestConnectBtn
	
	public final static int Clean=155;//CleanBtn
	
	
	public final static int folderManageBtn=FolderManageBtn.FOLDERMANAGE;
	public final static int addFolderBtn=AddFolderBtn.ADDFOLDERBTN;
	public final static int editFolderBtn=EditFolderBtn.EditFolder;
	public final static int delFolderBtn=DeleteFolderBtn.DeleteFolder;
	public final static int dealFile=DealFileBtn.DealFileBtn;
	public final static int uploadFile=UpLoadFileBtn.UpLoadFileBtn;

	
	public final static int autoContData=AutoContDataBtn.autoContDataBtn;
	public final static int initUFOENV=InitUFOENVBtn.initUFOENVBtn;
	public final static int moveFolderBtn=MoveFolderBtn.MOVEFOLDERBTN;
	
	public final static int ACTION_SET=165;
	public final static int FAST_SET=166;
	public final static int RELATION_SET=167;
}