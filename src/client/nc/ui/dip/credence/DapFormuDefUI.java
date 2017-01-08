package nc.ui.dip.credence;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import nc.bs.logging.Logger;
import nc.ui.dip.dlg.accountqj.AccountqjDLG;
import nc.ui.dip.dlg.accountqj.DataUrlDLG;
import nc.ui.dip.tyzhq.iniufoenv.ParamInputDlg;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.fi.uforeport.NCFuncForUFO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dap.factor.DapDefItemVO;
import nc.vo.dip.consttab.DipConsttabVO;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.util.DipNcIufoModuleVO;
import nc.vo.fi.uforeport.IFuncNCBatch;
import nc.vo.framework.rsa.Encode;
import nc.vo.pf.pub.FormulaProc;
import nc.vo.pub.billtype.DefitemVO;

/**
 * 公式向导界面。
 *
 * @author：贾玉淼 2001-5-11 17:07:33
 * <p>修改人：雷军 2005-1-27 应wangxy要求,增加一个公式"getHL(,,,)"
 */
public class DapFormuDefUI extends UIDialog implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5587427813551864985L;
	private UIPanel ivjFieldSelectPanel = null;
	private UIPanel ivjFormulaPanel = null;
	private UIPanel ivjMainPanel = null;
	private UIPanel ivjOperatorPanel = null;
	private UIPanel ivjOptButtonPanel = null;
	private UIPanel ivjOptQKButtonPanel = null;
	private JPanel ivjUIDialogContentPane = null;
	private UIButton ivjCancelButton = null;
	private UIButton ivjCfmButton = null;
	private UIButton ivjCheckButton = null;
	private UIButton ivjClearButton = null;
	private UITextArea ivjFormulaTArea = null;
	private UIButton ivjANDButton = null;
	private UIButton ivjBiggerButton = null;
	private UIButton ivjDivButton = null;
	private UIButton ivjEqualButton = null;
	private UITabbedPane ivjTablledPane = null;
	private UIList ivjFieldList = null;
	private UIList ivjFieldListBody = null;

	private UIList ivjFormulaList = null;
	private UIButton ivjLeftPrthButton = null;
	private UIButton ivjLIKEButton = null;
	private UIButton ivjMultButton = null;
	private UIButton ivjNOTButton = null;
	private UIButton ivjYWBButton = null;
	private UIButton ivjORButton = null;
	private UIButton ivjPlusButton = null;
	private UIButton ivjReduceButton = null;
	private UIButton ivjRightPrthButton = null;
	private UIButton ivjSmallerButton = null;
	private UIScrollPane ivjFieldScrollPane = null;

	private UIScrollPane ivjFormulaScrollPane = null;
	private UIScrollPane ivjFormulaTAreaScrollPanel = null;
	//定义公式字符串
	//记录光标所在位置
	private int m_caretPosi = 0;

	private UITextArea ivjHintLabel = null;
	//保存所有的单据项
	private DapDefItemVO[] m_allBillItems = null;
	private DapDefItemVO[] m_headerBillItems = null;
	private DapDefItemVO[] m_bodyBillItems = null;

	//基本档案
	private nc.ui.pub.beans.UIButton m_btnBaseDoc;
	//外部系统基本档案
	private nc.ui.pub.beans.UIButton m_btnWb;

	//固定值类型pk
	private String m_sRefType;  //  @jve:decl-index=0:

	//应wsw要求增加的两个字段
	private UITextField matchText = null;
	private UIButton btnSearch = null;
	private String pk_sys;
	public  int OK=0;//如果点击确认按钮为1
	
	public void setPk_sys(String ss){
		this.pk_sys=ss;
		
	}
	public String getPk_sys(){
		return pk_sys;
	}
	private String pk_table;
    
	private  String tablename="";
	
	Object[] listItems = { "iif(  ,  ,  )"/*@des 0*/,
			"indexOf(  ,  )"/*@des 1*/,
			"left(  ,  )"/*@des 2*/,
			"length( )",/*@des 3*/
			"right(  ,  )",/*@des 4*/
			"mid(  ,  ,  )", /*@des 5*/
			"abs( )",/*@des 6*/
			"round(  ,  )", /*@des 7*/
			"int( )",/*@des 8*/
			"sgn( )", /*@des 9*/
//			"getHL(  ,  ,  ,  )",/*@des 10*/
			"getYMLastDay(  ,  )",/*@des 10*/
			"getCurAccountYear(  )",
			"getCurAccountMonth(  )",
			"cyear()",
			"cmonth()",
			"getGenPK(  )",/*@des 10-1@*/
//			"execuFBySD(  ,  ,  )",/*@des 10-2@*/
			"initEnv(  ,  ,  ,  ,  )",/*@des 10-2@*/
			"getDataByKey(  ,  ,  )",/*@des 11*/
			"querySqlByDesign(  ,  )",/*@des 11-1*/
			"defGetColValue(  ,  ,  ,  )", /*@des 12*/
			"random(  )",/*@des 13*/
			"trim(  )",/*@des 14*/
			"formatDef( , )",/*@des 15*/
			"tonumber( )",/*@des 16*/
			"tostring( )"/*@des 17*/,
			"getPKServlet( )"
	};
	
	String[] intrudse={ "iif(condition,thenvalue,elsevalue)判断条件condition是否满足,如果满足返回第一个值thenvalue,如果不满足返回第二个值elsevalue,比如iif(var1==\"Y\",\"Open\",\"Close\").iif函数支持嵌套比如:iif(var1==\"Y\",iif(var2!=\"C\",\"Stop\",\"Run\"),\"Close\")"/*@des 0*/,
			"indexOf(String st1, String st2) 判断字符串st1中第一个字符串st2所在的位置,比如lastIndexOf(\"HI,UAP2006,UAP\",\"UAP\")返回【3】."/*@des 1*/,
			"left(String st, index) 求字符串st左边前index个字符组成的字符串,比如left(\"0123456\",2)返回【01】"/*@des 2*/,
			"length(String st) 求字符串st的长度,比如length(\"0123456\")返回【7】",/*@des 3*/
			"right(String st, int index) 求字符串st右边前index个字符组成的字符串,比如right(\"0123456\",2)返回【23456】",/*@des 4*/
			"mid(String st, int start, int end) 求字符串st左边前第start个字符至第end个字符之间的字符串,mid(\"0123456\",2,4)返回【23】", /*@des 5*/
			"abs(num)求数num的绝对值,比如abs(-100)返回【100】",/*@des 6*/
			"round(double num, int index) 对num保留index位小数(四舍五入)", /*@des 7*/
			"int(数字或者字符串) 将变量转换为int类型",/*@des 8*/
			"sgn(num) 当数num大于0时,返回1,等于0时,返回0,小于0时返回-1", /*@des 9*/
//			"getHL(  ,  ,  ,  )",/*@des 10*/
			"getYMLastDay(String year ,String month) 返回year年month月的最后一天,比如getYMLastDay(\"2011\",\"8\")返回【2011-08-31】",/*@des 11*/
			"getCurAccountYear(String pk) 根据选择的会计期间方案（pk）返回当前的会计年",/*@des 11*/
			"getCurAccountMonth(String pk) 根据选择的会计期间方案（pk）返回当前的会计月",/*@des 11*/
			"cyear() 返回当前年",/*@des 11*/
			"cmonth() 返回当前月",/*@des 11*/
			"getGenPK(  ) 自动生成一个主键",/*@des 10-1*/
//			"execuFBySD(ServletPK,ServletName,Formula) 通过servlet，执行公式Formula。",/*@des 10-2*/
			"initEnv(pkservlet,accountcode,unitcode,usercode,func) 初始化业务函数执行的环境，如果第一个参数为【\"\"】的时候,函数在本地执行，否则调用远程通过Servlet执行。其中参数:pkservlet servlet远程调用主键；accountcode 帐套编码；unitcode 公司编码；usercode 用户编码；func 业务函数",/*@des 10-2*/
			"getDataByKey(tname1.fild1,tname2.fild2,value)根据关键字找对应对照数据,tname1.fild1为对照系统的有引用关系的字段,tname2.fild2为返回的被对照系统的tname2表的fild2字段,value为tname1.fild1的取值",/*@des 12*/
			"querySqlByDesign(design,sql)连接数据源design取sql语句的查询结果,如果design传null或者空，则取ncdip数据源，否则取指定数据源；sql语句注意查出来的是一个字段的一个值。如果查出一个字段有多个值那么取第一个值",/*@des 12*/
			"defGetColValue(tablename,fieldname,pkfield,pkvalue)根据主键从数据库查询特定字段的值,其功能类似SQL语句:select fieldname from tablename where pkfield = pkvalue 从这条SQL语句可以看出各个参数的含义." ,/*@des 13*/
			"random(num) 自动生成一个num位的随机数，返回的类型为字符串，例如random(5)返回【23419】" ,/*@des 13*/
			"trim(str)将str的所有空格都去掉",/*@des 14*/
			"formatDef(Object obj,Object num)将obj（必须是小数或者数字），格式化成num(num大于0，小于等于8)位小数位，如果字符串小数位大于num，大于的都舍去，如果小于的补0",/*@des 15*/
			"toNumber(String st) 将字符串st转换为本解析器可识别的数字,比如toNumber(45.0)将返回一个数字型45.0,经过转化后可参与各种数值计算",/*@des 16*/
			"toString(obj) 将对象obj转换为本解析器可识别的字符串形式"/*@des 17*/,
			"getPKServlet(String pk) 根据选择的数据同步url注册来返回servlet主键"
	};
	int invindex=0;
	private int getInvIndex(){
		return invindex;
	}
	NCFuncForUFO fu=ClientEnvDef.getNCFuncForUFO();
	/**
	 * DapFormuDefUI 构造子注解。
	 * @param parent java.awt.Container
	 */
	public DapFormuDefUI(Container parent,String pk_table,String pk_sys) {
		super(parent);
		try {
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk_table);
			if(hvo.getMemorytable()!=null&&hvo.getMemorytable().length()>0){
				tablename=hvo.getMemorytable();
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
		setPk_table(pk_table);
		setPk_sys(pk_sys);
		initialize();
		if(fu==null&&!ClientEnvironment.getInstance().getCorporation().getUnitcode().equals("0001")){
			Encode code = new Encode();
		 String[] calEnv = {"ncdip",
                 //数据源名称
				  ClientEnvironment.getInstance().getAccount().getAccountCode(), //账套
				  ClientEnvironment.getInstance().getCorporation().getUnitcode(), //单位
				  "2011-04-01", //日期
				  ClientEnvironment.getInstance().getUser().getUserCode(), //用户
				  code.decode(ClientEnvironment.getInstance().getUser().getUserPassword()), //密码
			"2011-04-01",
			ClientEnvironment.getInstance().getLanguage()//语言类型
			    };
			fu=new NCFuncForUFO();
			fu.setCalEnv(calEnv);
			ClientEnvDef.setCalenv(calEnv);
			ClientEnvDef.setNCFuncForUFO(fu);
		}
		for(int i=0;i<intrudse.length;i++){
			if(intrudse[i].startsWith("initEnv")){
				setIntrudse(i);
				break;
			}
		}
	}
private void setIntrudse(int i){
	this.invindex=i;
}
/**
 * liyunzhe modify ,增加参数是为了在导入时候做校验。
 */
public boolean ischeckpass(String  formulaString){
	String form="";
	boolean showErrorMessage=true;
	if(formulaString.trim().length()>1){
	 form=formulaString;
	 showErrorMessage=false;
	}else{
		form=getFormulaTArea().getText().trim()	;
	}

	form=form.replace(" ", "");
	form=FormulaProc.replaceFormVar(form);
	String fourm=form;
	if(fourm.indexOf("-b")>=0){//判断是否含有业务函数
		//如果含有业务函数，判断业务函数的开始结束标志的个数是否相等
		int countb=(fourm+"a").split("-b").length;
		int counte=(fourm+"a").split("-e").length;
		if(countb!=counte){
			showErrorMessageDilog(showErrorMessage);
			return false;
		}
		//如果业务函数里边嵌套函数，判断函数的个数是否相等
		if(fourm.indexOf("(")>=0){
			  countb=(fourm+"a").split("\\(").length;
			  counte=(fourm+"a").split("\\)").length;
			if(countb!=counte){
				showErrorMessageDilog(showErrorMessage);
				return false;
			}
		}
		//如果替换完的公式仍然包含业务函数
		while(fourm.indexOf("-b")>=0){
			int romindex=fourm.indexOf("-b")+2;
			int endindex=0;
			String temp=fourm.substring(romindex);
			int count1=0;
			//如果业务函数里边嵌套业务函数，这种情况没有测试，不能保证正确
			while(temp!=null&&temp.indexOf("-b")>=0&&temp.indexOf("-e")>=0&&temp.indexOf("-b")<temp.indexOf("-e")){
				romindex=temp.indexOf("-b")+romindex;
				temp=form.substring(romindex+2);
				count1=count1+2;
			}
			int end=temp.indexOf("-e");
			endindex=romindex+end;
			String f=fourm.substring(romindex,endindex);
			//得到的业务函数整个内容，包括函数名和函数参数
			String ftt=f;
			int ff=f.indexOf("(");
			int fe=f.lastIndexOf(")");
		    String ft=f.replace("'", "\"").substring(ff+1,fe);
		    //如果业务函数里边的参数是由函数构成的
		    if(ft.indexOf("(")>0){
		    	//判断函数的左右括号是否相同
		    	  countb=(ft+"a").split("\\(").length;
				  counte=(ft+"a").split("\\)").length;
				if(countb!=counte){
					showErrorMessageDilog(showErrorMessage);
					return false;
				}
				//循环，直到里边不包含函数
		    	while(ft.indexOf("(")>0){
			    	String tempft=ft;
			    	int fbindex=ft.indexOf("(");
			    	int fromind=0;
			    	int count=0;
			    	//如果函数嵌套函数
				    while(tempft.length()>=fbindex+1&&tempft.indexOf("(",fbindex+1)>=0&&tempft.indexOf(")",fbindex+1)>=0&&tempft.indexOf("(",fbindex+1)<tempft.indexOf(")",fbindex+1)){
				    	tempft=tempft.substring(fbindex+1);
				    	fromind=fromind+fbindex+1;
				    	count++;
				    }
			    	int fteindex=tempft.indexOf(")")+fromind;
			    	fromind=fromind==0?fbindex:fromind;
			    	String lfstr=ft.substring(0,fteindex);
			    	lfstr=lfstr.substring(0,lfstr.lastIndexOf("("));
			    	int from=(lfstr.lastIndexOf(",")<lfstr.lastIndexOf("(")?lfstr.lastIndexOf("("):lfstr.lastIndexOf(","))+1;
			    	//最里层的函数
			    	String temform=ft.substring(from,fteindex+1);
			    	if (!nc.vo.pf.pub.PfFormulaParse.isFormula(temform)) {
			    		showErrorMessageDilog(showErrorMessage);
						return false;
					}
			    	//把参数里边的函数都替换成“”
			    	ft=ft.replace(temform, "\"\"");
		    	}
		    }
		    //把业务函数替换成“”
		    fourm=fourm.replace("-b"+ftt+"-e", "\"\"");
		}
		if (!nc.vo.pf.pub.PfFormulaParse.isFormula(fourm)) {
			showErrorMessageDilog(showErrorMessage);
			return false;
		}
	}else{
		String strFormula = formulaAnalyze(fourm);
		if (!nc.vo.pf.pub.PfFormulaParse.isFormula(strFormula)) {
			showErrorMessageDilog(showErrorMessage);
			return false;
		}
	}
	return true;
}
	private void showErrorMessageDilog(boolean showErrorMessage){
		if(showErrorMessage){
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000039")/*@res "公式错误"*/,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000040")/*@res "定义的公式有误，请重新检查！"*/);
		}
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getANDButton())) {
			getFormulaTArea().insert("&&", m_caretPosi);
		}
		if (e.getSource().equals(getYWBButton())) {
			getFormulaTArea().insert("\""+tablename+"\"", m_caretPosi);
		}
		if (e.getSource().equals(getBiggerButton())) {
			getFormulaTArea().insert(">", m_caretPosi);
		}
		if (e.getSource().equals(getCancelButton())) {
			this.closeCancel();
		}
		if (e.getSource().equals(getCfmButton())) {
		
			if (!ischeckpass("")) {
				return;
			} else {
				OK=1;
				this.closeOK();
			}
		}
		if (e.getSource().equals(getCheckButton())) {
			if(ischeckpass("")){
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000041")/*@res "公式正确"*/,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000042")/*@res "定义的公式完全正确！"*/);
			}
		}
		if (e.getSource().equals(getClearButton())) {
			getFormulaTArea().setText(null);
			m_caretPosi = 0;
		}
		if (e.getSource().equals(getDivButton())) {
			getFormulaTArea().insert("/", m_caretPosi);
		}
		if (e.getSource().equals(getEqualButton())) {
			getFormulaTArea().insert("=", m_caretPosi);
		}
		if (e.getSource().equals(getLeftPrthButton())) {
			getFormulaTArea().insert("(", m_caretPosi);
		}
		if (e.getSource().equals(getLIKEButton())) {
			getFormulaTArea().insert("LIKE", m_caretPosi);
		}
		if (e.getSource().equals(getMultButton())) {
			getFormulaTArea().insert("*", m_caretPosi);
		}
		if (e.getSource().equals(getNOTButton())) {
			getFormulaTArea().insert("!()", m_caretPosi);
		}
		if (e.getSource().equals(getORButton())) {
			getFormulaTArea().insert("||", m_caretPosi);
		}
		if (e.getSource().equals(getPlusButton())) {
			getFormulaTArea().insert("+", m_caretPosi);
		}
		if (e.getSource().equals(getReduceButton())) {
			getFormulaTArea().insert("-", m_caretPosi);
		}
		if (e.getSource().equals(getRightPrthButton())) {
			getFormulaTArea().insert(")", m_caretPosi);
		}
		if (e.getSource().equals(getSmallerButton())) {
			getFormulaTArea().insert("<", m_caretPosi);
		}
		if(e.getSource().equals(getBtnWb())){
			WbDlg dlg=new WbDlg(this,true,getPk_sys());
			dlg.show();
		}
		/**
		 * 进行过滤
		 */
		if (e.getSource().equals(getBtnSearch())) {
			doFilter();
		}
	}

	String _special = null;
	int _index = 0;

	/**
	 * 进行单据项目过滤
	 * 
	 */
	void doFilter() {
		//
		String special = getMatchText().getText();
		if (special == null || special.length() == 0) {
			//do nothing
			return;
		}

		Pattern p = Pattern.compile(special);
		UIList aim = getTabbedPane().getSelectedIndex() == 0 ? getFieldList() : getFieldListBody();
		int count = aim.getModel().getSize();

		int currentSelect = aim.getSelectedIndex() + 1;

		boolean _isSame = special.equalsIgnoreCase(_special);
		boolean _selectSame = currentSelect == _index;

		int begin = 0;

		if (_isSame && _selectSame) {
			begin = _index;
		} else {
			if (_isSame) {
				begin = currentSelect == -1 ? 0 : currentSelect;
			} else {
				begin = 0;
			}
		}
		_special = special;

		for (int i = begin; i < count; i++) {
			DapDefItemVO selectObject = (DapDefItemVO) aim.getModel().getElementAt(i);
			String itemName = selectObject.getItemname();
			Matcher m = p.matcher(itemName);
			if (m.find()) {
				aim.setSelectedIndex(i);
				aim.scrollRectToVisible(aim.getCellBounds(i, i));
				_index = i + 1;
				break;
			}
		}
	}

	/**
	 * 分析公式成为公式处理，从单据项名称转换成属性名称。
	 * 创建日期：(2001-6-8 15:52:03)
	 * @return java.lang.String
	 * @param formula java.lang.String
	 */
	public String formulaAnalyze(String formula) {
		String formulaProc = formula;
		/*add by st 2003-10-21*/
		/*formulaProc = BdInputDlg.formulaAnalyze(formulaProc);
		end 
		if (formulaProc != null) {
			if (m_allBillItems != null && m_allBillItems.length != 0) {
				for (int i = 0; i < m_allBillItems.length; i++) {
					int flag = 0;
					do {
						int situ = formulaProc.indexOf("@" + m_allBillItems[i].getItemname().trim()
								+ m_allBillItems[i].getItemtype().toString().trim() + "@");
						if (situ != -1) {
							int firt = situ;
							int end = situ
									+ ("@" + m_allBillItems[i].getItemname().trim() + m_allBillItems[i].getItemtype().toString().trim() + "@")
											.length();
							//问题出现在这里
							formulaProc = formulaProc.substring(0, firt) + "@" + m_allBillItems[i].getAttrname().trim()
									+ m_allBillItems[i].getItemtype().toString().trim() + "@" + formulaProc.substring(end);
						}
						flag = situ;
					} while (flag != -1);
//					flag = 0;
//					do {
//						int situ = formulaProc.indexOf("#" + m_allBillItems[i].getItemname().trim()
//								/*+ m_allBillItems[i].getItemtype().toString().trim() + "#");*/
//						if (situ != -1) {
//							int firt = situ;
//							int end = situ
//									+ ("#" + m_allBillItems[i].getItemname().trim() /*+ m_allBillItems[i].getItemtype().toString().trim()*/ + "#")
//											.length();
//							formulaProc = formulaProc.substring(0, firt) + "#" + m_allBillItems[i].getAttrname().trim()
//									/*+ m_allBillItems[i].getItemtype().toString().trim()*/ + "#" + formulaProc.substring(end);
//						}
//						flag = situ;
//					} while (flag != -1);
//				}
//			}
//		}*/
		return formulaProc;
	}


	/**
	 * 返回 UIButton182 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getANDButton() {
		if (ivjANDButton == null) {
			try {
				ivjANDButton = new nc.ui.pub.beans.UIButton();
				ivjANDButton.setName("ANDButton");
				ivjANDButton.setText("AND");
				ivjANDButton.setBounds(234, 0, 38, 20);
				ivjANDButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				
				// user code begin {1}
				ivjANDButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjANDButton;
	}
    /**
     * 外系统基本档案
     * 2011-5-19
     * lx
     */
	public nc.ui.pub.beans.UIButton getBtnWb(){
		if(m_btnWb==null){
			m_btnWb=new UIButton();
			m_btnWb.setName("m_btnWb");
			m_btnWb.setText("外系统基本档案");
			m_btnWb.setBounds(220, 0, 100, 20);
			m_btnWb.setMargin(new java.awt.Insets(2,4,2,4));
			m_btnWb.addActionListener(this);
		}
		return m_btnWb;
	}
	
	
	
	
	
	/**
	 * 得到基本档案录入参照
	 * 创建日期：(2003-10-21 09:38:26)
	 * @author：Administrator
	 * @return nc.ui.pub.beans.UIButton
	 */
	public nc.ui.pub.beans.UIButton getBaseDocButton() {
		if (m_btnBaseDoc == null) {
			m_btnBaseDoc = new UIButton();
			m_btnBaseDoc.setName("BaseDocButton");
			m_btnBaseDoc.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000043")/*@res "基本档案"*/);
			m_btnBaseDoc.setBounds(160, 0, 60, 20);
			m_btnBaseDoc.setMargin(new java.awt.Insets(2, 4, 2, 4));
			// user code begin {1}
			m_btnBaseDoc.addActionListener(this);
			// user code end
		}
		return m_btnBaseDoc;
	}


	/**
	 * 返回 UIButton14 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBiggerButton() {
		if (ivjBiggerButton == null) {
			try {
				ivjBiggerButton = new nc.ui.pub.beans.UIButton();
				ivjBiggerButton.setName("BiggerButton");
				ivjBiggerButton.setText(">");
				ivjBiggerButton.setBounds(80, 0, 20, 20);
				ivjBiggerButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjBiggerButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBiggerButton;
	}

	/**
	 * 返回所有的单据项。
	 * 创建日期：(2001-6-8 15:19:20)
	 * @return String[]
	 */
	public DapDefItemVO[] getBillItems() {
		return m_allBillItems;
	}

	/**
	 * 返回 CancelButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getCancelButton() {
		if (ivjCancelButton == null) {
			try {
				ivjCancelButton = new nc.ui.pub.beans.UIButton();
				ivjCancelButton.setName("CancelButton");
				ivjCancelButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
				ivjCancelButton.setBounds(75, 0, 70, 22);
				ivjCancelButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjCancelButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCancelButton;
	}

	/**
	 * 返回 CfmButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getCfmButton() {
		if (ivjCfmButton == null) {
			try {
				ivjCfmButton = new nc.ui.pub.beans.UIButton();
				ivjCfmButton.setName("CfmButton");
				ivjCfmButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);
				ivjCfmButton.setBounds(0, 0, 70, 22);
				ivjCfmButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjCfmButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCfmButton;
	}

	/**
	 * 返回 CheckButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getCheckButton() {
		if (ivjCheckButton == null) {
			try {
				ivjCheckButton = new nc.ui.pub.beans.UIButton();
				ivjCheckButton.setName("CheckButton");
				ivjCheckButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000045")/*@res "检查"*/);
				ivjCheckButton.setBounds(0, 0, 40, 20);
				ivjCheckButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjCheckButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCheckButton;
	}

	/**
	 * 返回 ClearButton 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getClearButton() {
		if (ivjClearButton == null) {
			try {
				ivjClearButton = new nc.ui.pub.beans.UIButton();
				ivjClearButton.setName("ClearButton");
				ivjClearButton.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000040")/*@res "清空"*/);
				ivjClearButton.setBounds(40, 0, 40, 20);
				ivjClearButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
				// user code begin {1}
				ivjClearButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjClearButton;
	}

	/**
	 * 返回 UIButton11 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getDivButton() {
		if (ivjDivButton == null) {
			try {
				ivjDivButton = new nc.ui.pub.beans.UIButton();
				ivjDivButton.setName("DivButton");
				ivjDivButton.setText("÷");
				ivjDivButton.setBounds(20, 0, 20, 20);
				ivjDivButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjDivButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjDivButton;
	}

	/**
	 * 返回 UIButton16 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getEqualButton() {
		if (ivjEqualButton == null) {
			try {
				ivjEqualButton = new nc.ui.pub.beans.UIButton();
				ivjEqualButton.setName("EqualButton");
				ivjEqualButton.setText("=");
				ivjEqualButton.setBounds(120, 0, 20, 20);
				ivjEqualButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjEqualButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjEqualButton;
	}

	/**
	 * 返回 FieldList 特性值。
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIList getFieldList() {
		if (ivjFieldList == null) {
			try {
				ivjFieldList = new nc.ui.pub.beans.UIList();
				ivjFieldList.setName("FieldList");
				//ivjFieldList.setBorder(new javax.swing.border.EtchedBorder());
//				ivjFieldList.setBounds(5, 5, 107, 146);
				ivjFieldList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				// user code begin {1}
				ivjFieldList.addMouseListener(this);


				//ivjFieldList.ad
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldList;
	}

	/**
	 * 返回 FieldList 特性值。
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIList getFieldListBody() {
		if (ivjFieldListBody == null) {
			try {
				ivjFieldListBody = new nc.ui.pub.beans.UIList();
				ivjFieldListBody.setName("FieldListBody");
				//ivjFieldList.setBorder(new javax.swing.border.EtchedBorder());
				ivjFieldListBody.setBounds(0, 0, 107, 146);
				ivjFieldListBody.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				// user code begin {1}
				ivjFieldListBody.addMouseListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldListBody;
	}

	/**
	 * 容纳字段，根据表头、表体进行分组
	 * @return
	 */
	private UITabbedPane getTabbedPane() {
		if (ivjTablledPane == null) {
			try {
				ivjTablledPane = new UITabbedPane();
				ivjTablledPane.setName("FiledTabbedPane");
				ivjTablledPane.addTab("业务数据", getFieldScrollPane());
                                ivjTablledPane.addTab("常量数据", getCLJpane());
				ivjTablledPane.addTab("本表结构", getSelfB());
//				ivjTablledPane.addTab("子表", getFieldScrollPaneBody());
				ivjTablledPane.addTab("NC系统表",getNCB());
				ivjTablledPane.addTab("业务函数",getYWHS());
//				ivjTablledPane.addTab("外部系统表", getFieldScrollPaneBody());
				ivjTablledPane.addTab("本系统表", getWbXTB());
				ivjTablledPane.addTab("关联表", getGlB());
				ivjTablledPane.add("函数",getFormulaScrollPane());
				
			} catch (Exception ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTablledPane;
	}
	UITabbedPane cljp=null;
	private UITabbedPane getCLJpane(){
		if(cljp==null){
			try {
				cljp = new UITabbedPane();
				cljp.setName("cljp");
				cljp.addTab("返回值", getCLzdScrollPane());
				cljp.addTab("返回公式", getCLgsPane1());
			} catch (Exception ivjExc) {
				handleException(ivjExc);
			}
		}
		return cljp;
	}
	private BillCardPanel mipanel=null;
	private BillCardPanel getCLgsPane1() {
			if(mipanel==null){
				mipanel=new BillCardPanel();
				mipanel.loadTemplet("H4H1H1Hd", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
				mipanel.getBillTable().setSortEnabled(false);
				DipConsttabVO[] msvos = null;
				try {
					msvos = (DipConsttabVO[]) HYPubBO_Client.queryByCondition(DipConsttabVO.class, "nvl(dr,0)=0");
				} catch (UifException e) {
					e.printStackTrace();
				}
				mipanel.getBillTable().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseExited(MouseEvent mouseevent) {
					}
					@Override
					public void mouseReleased(MouseEvent mouseevent) {
					}
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2){
							int row=mipanel.getBillTable().getSelectedRow();
							String ins="";
							if(row>=0){
								ins=(String) mipanel.getBodyValueAt(row, "pk_consttab");
								getFormulaTArea().insert("getValueByConst(\""+ins+"\")",m_caretPosi);
							}
						}
					}
					public void mousePressed(MouseEvent mouseevent) {

					}
				});
				if(msvos!=null&&msvos.length>0){
				mipanel.getBillModel().setBodyDataVO(msvos);
				mipanel.getBillModel().setEnabled(false);
				}
			}
			return mipanel;
	}
	private BillCardPanel mipanel1=null;
	private BillCardPanel getCLzdScrollPane() {
		if(mipanel1==null){
			mipanel1=new BillCardPanel();
			mipanel1.loadTemplet("H4H1H1Hd", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			mipanel1.getBillTable().setSortEnabled(false);
			DipConsttabVO[] msvos = null;
			try {
				msvos = (DipConsttabVO[]) HYPubBO_Client.queryByCondition(DipConsttabVO.class, "nvl(dr,0)=0");
			} catch (UifException e) {
				e.printStackTrace();
			}
			mipanel1.getBillTable().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent mouseevent) {
				}
				@Override
				public void mouseReleased(MouseEvent mouseevent) {
				}
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2){
						int row=mipanel1.getBillTable().getSelectedRow();
						String ins="";
						if(row>=0){
							ins=(String) mipanel1.getBodyValueAt(row, "descs");
							getFormulaTArea().insert(ins,m_caretPosi);
						}
					}
				}
				public void mousePressed(MouseEvent mouseevent) {

				}
			});
			if(msvos!=null&&msvos.length>0){
				mipanel1.getBillModel().setBodyDataVO(msvos);
				mipanel1.getBillModel().setEnabled(false);
			}
		}
		return mipanel1;
}
	JPanel jwbxtb;

    DefaultListModel leftModel = new DefaultListModel();
    DefaultListModel rightMOdel = new DefaultListModel();
    JList leftList = new JList(leftModel);
    JList rightList = new JList(rightMOdel);

	private JPanel getWbXTB(){
		if(jwbxtb==null){
			jwbxtb=new JPanel();
			jwbxtb.setLayout(null);
			jwbxtb.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			jwbxtb.add(jScrollPane1);
			jScrollPane1.setBounds(3, 3, 274, 128);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane jScrollPane2 = new JScrollPane();
			jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jwbxtb.add(jScrollPane2);
			jScrollPane2.setBounds(3, 133, 274, 290);
			try {
				DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_xt='"+getPk_sys()+"' and (isfolder is null or isfolder='N')");// and  Pk_Datadefinit_h<>'"+getPk_table()+"'");
				if(hvo!=null&&hvo.length>0){
					int i=0;
					wbmap=new HashMap<String, String>();
					String[] str=new String[hvo.length];
					for(DipDatadefinitHVO hvoi:hvo){
						str[i]=hvoi.getMemorytable()+" <"+hvoi.getSysname()+">";
						
						wbmap.put(hvoi.getMemorytable()+" <"+hvoi.getSysname()+">", hvoi.getPrimaryKey());
						i++;
					}
					ListModel jList1Model = 
						new DefaultComboBoxModel(str);
					leftList.setModel(jList1Model);
				leftList.addMouseListener(this);
				}
				jScrollPane1.setViewportView(leftList);
				{
					rightList = new JList();
					rightList.addMouseListener(this);
				}
				jScrollPane2.setViewportView(rightList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jwbxtb;
	}
	JPanel selfb=null;
	JList selfl=null;
	private JPanel getSelfB(){
		if(selfb==null){
			selfb=new JPanel();
			selfb.setLayout(null);
			selfb.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			selfb.add(jScrollPane1);
			jScrollPane1.setBounds(3, 3, 274, 420);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			try {
				{
					
					selfl = new JList();
					DipDatadefinitBVO[] hvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " Pk_Datadefinit_h='"+getPk_table()+"'");
					if(hvo!=null&&hvo.length>0){
						String[] str=new String[hvo.length];
						int i=0;
//						ncmap=new HashMap<String, String>();
						for(DipDatadefinitBVO vo:hvo){
							str[i]=vo.getEname().toUpperCase()+" <"+vo.getCname()+">";
//							ncmap.put(vo.getMemorytable()+" <"+vo.getSysname()+">", vo.getPrimaryKey());
							i++;
						}
						ListModel jList1Model = 
							new DefaultComboBoxModel(str);
						selfl.setModel(jList1Model);
					}
					selfl.addMouseListener(this);
				}
				jScrollPane1.setViewportView(selfl);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return selfb;
	}
	JPanel ywhs=null;

	DefaultListModel ywleftModel = new DefaultListModel();
    DefaultListModel ywrightMOdel = new DefaultListModel();
    JList ywleftList = new JList(ywleftModel);
    JList ywrightList = new JList(ywrightMOdel);
    Map<String, String[]> ywhsmap=new HashMap<String, String[]>();
    Map<String, String> ywhsdescmap=new HashMap<String, String>();
	private JPanel getYWHS(){

		if(ywhs==null){
			ywhs=new JPanel();
			ywhs.setLayout(null);
			ywhs.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			ywhs.add(jScrollPane1);
			jScrollPane1.setBounds(3, 3, 274, 128);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane jScrollPane2 = new JScrollPane();
			jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ywhs.add(jScrollPane2);
			jScrollPane2.setBounds(3, 133, 274, 290);
			try {
				DipNcIufoModuleVO[] hvo=(DipNcIufoModuleVO[]) HYPubBO_Client.queryByCondition(DipNcIufoModuleVO.class, "nvl(dr,0)=0");
				if(hvo!=null&&hvo.length>0){
					String[] str=new String[hvo.length+1];
					for(int i=0;i<hvo.length;i++){
						String classname=hvo[i].getModule_name();
						str[i]=classname;
						String clas=hvo[i].getModule_class();
						IFuncNCBatch b=(IFuncNCBatch) Class.forName(clas).newInstance();
						int c=b.getModuleCount();
						String md=b.getModuleDesc(c);
						nc.vo.fi.uforeport.UfoSimpleObject[] cc=b.getFuncList(c);
						if(cc!=null&&cc.length>0){
							String[] funname=new String[cc.length];
							for(int j=0;j<cc.length;j++){
								funname[j]=cc[j].getName();
								ywhsdescmap.put(funname[j], b.getFuncDesc(funname[j])+"\n"+b.getFuncForm(funname[j]));
							}
							ywhsmap.put(classname, funname);
						}
					}
					str[str.length-1]="HR通用函数";
					ywhsmap.put("HR通用函数", new String[]{"HR_TOTAL"});
					ListModel jList1Model = 
						new DefaultComboBoxModel(str);
					ywleftList.setModel(jList1Model);
					ywleftList.addMouseListener(this);
				}
				jScrollPane1.setViewportView(ywleftList);
				{
					ywrightList = new JList();
					ywrightList.addMouseListener(this);
				}
				jScrollPane2.setViewportView(ywrightList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ywhs;
	
	}
	JPanel glb=null;
	JList gll=null;
	JList glr=null;
	JList glm=null;
	Map<String ,DipDatadefinitBVO> glmap=null;
	private JPanel getGlB(){
		if(glb==null){
			glb=new JPanel();
			glb.setLayout(null);
			glb.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(3, 3, 274, 83);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			glb.add(jScrollPane1);
			JScrollPane jScrollPane2 = new JScrollPane();
			jScrollPane2.setBounds(3, 88, 274, 70);
			jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			glb.add(jScrollPane2);
			JScrollPane jScrollPane3 = new JScrollPane();
			jScrollPane3.setBounds(3, 160, 274, 263);
			jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane3.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			glb.add(jScrollPane3);
			try {
				{
					gll = new JList();
					DipDatadefinitBVO[] hvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " isquote='Y' and nvl(dr,0)=0 and pk_datadefinit_h='"+getPk_table()+"'");
					if(hvo!=null&&hvo.length>0){
						String[] str=new String[hvo.length];
						int i=0;
						glmap=new HashMap<String, DipDatadefinitBVO>();
						for(DipDatadefinitBVO vo:hvo){
							str[i]=vo.getEname()+" <"+vo.getCname()+">";
							glmap.put(vo.getEname()+" <"+vo.getCname()+">", vo);
							i++;
						}
						ListModel jList1Model = 
							new DefaultComboBoxModel(str);
						gll.setModel(jList1Model);
					}
					gll.addMouseListener(this);
				}
				jScrollPane1.setViewportView(gll);
				{
					glm = new JList();
					glm.addMouseListener(this);
				}
				jScrollPane2.setViewportView(glm);
				{
					glr = new JList();
					glr.addMouseListener(this);
				}
				jScrollPane3.setViewportView(glr);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return glb;
	}
	
	
	JPanel jncb;

    DefaultListModel ncleftModel = new DefaultListModel();
    DefaultListModel ncrightMOdel = new DefaultListModel();
    JList ncleftList = new JList(ncleftModel);
    JList ncrightList = new JList(ncrightMOdel);
    Map<String,String> ncmap;
    Map<String,String> wbmap;
	private JPanel getNCB(){
		if(jncb==null){
			jncb=new JPanel();
			jncb.setLayout(null);
			jncb.setPreferredSize(new java.awt.Dimension(307, 300));
			JScrollPane jScrollPane1 = new JScrollPane();
			jncb.add(jScrollPane1);
			jScrollPane1.setBounds(3, 3, 274, 128);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane jScrollPane2 = new JScrollPane();
			jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jncb.add(jScrollPane2);
			jScrollPane2.setBounds(3, 133, 274, 290);
			try {
				{
					ncleftList = new JList();
					DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, " pk_xt='0001AA1000000001XQ1B' and (isfolder is null or isfolder='N')");
					if(hvo!=null&&hvo.length>0){
						String[] str=new String[hvo.length];
						int i=0;
						ncmap=new HashMap<String, String>();
						for(DipDatadefinitHVO vo:hvo){
							str[i]=vo.getMemorytable()+" <"+vo.getSysname()+">";
							ncmap.put(vo.getMemorytable()+" <"+vo.getSysname()+">", vo.getPrimaryKey());
							i++;
						}
						ListModel jList1Model = 
							new DefaultComboBoxModel(str);
						ncleftList.setModel(jList1Model);
					}
					ncleftList.addMouseListener(this);
				}
				jScrollPane1.setViewportView(ncleftList);
				{
					ncrightList = new JList();
					ncrightList.addMouseListener(this);
				}
				jScrollPane2.setViewportView(ncrightList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jncb;
	}
	/**
	 * 返回 FieldScrollPane 特性值。
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getFieldScrollPane() {
		if (ivjFieldScrollPane == null) {
			try {
				ivjFieldScrollPane = new nc.ui.pub.beans.UIScrollPane();
				ivjFieldScrollPane.setName("FieldScrollPane");
				ivjFieldScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				ivjFieldScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getFieldScrollPane().setViewportView(getFieldList());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldScrollPane;
	}


	/**
	 * 返回 FieldSelectPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getFieldSelectPanel() {
		if (ivjFieldSelectPanel == null) {
			try {
				ivjFieldSelectPanel = new nc.ui.pub.beans.UIPanel();
				ivjFieldSelectPanel.setName("FieldSelectPanel");
				ivjFieldSelectPanel.setLayout(getFieldSelectPanelGridLayout());
				ivjFieldSelectPanel.setBounds(3,3, 283, 470);
//				ivjFieldSelectPanel.add(getFormulaScrollPane(), getFormulaScrollPane().getName());
				ivjFieldSelectPanel.add(getTabbedPane(), getTabbedPane().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFieldSelectPanel;
	}

	/**
	 * 返回 FieldSelectPanelGridLayout 特性值。
	 * @return java.awt.GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.GridLayout getFieldSelectPanelGridLayout() {
		java.awt.GridLayout ivjFieldSelectPanelGridLayout = null;
		try {
			/* 创建部件 */
			ivjFieldSelectPanelGridLayout = new java.awt.GridLayout();
			ivjFieldSelectPanelGridLayout.setHgap(10);
			ivjFieldSelectPanelGridLayout.setColumns(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjFieldSelectPanelGridLayout;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2002-06-21 10:11:19)
	 * @return nc.ui.pub.beans.UIRefPane
	 */

	/**
	 * 得到固定值参照类型pk。
	 * 创建日期：(2003-10-21 15:37:15)
	 * @author：Administrator
	 * @return java.lang.String
	 */
	public java.lang.String getFixRefType() {
		return m_sRefType;
	}

	/**
	 * 获取取数公式。
	 * 创建日期：(2001-5-15 16:10:30)
	 * @return java.lang.String
	 */
	public String getFormula() {
		return getFormulaTArea().getText();
	}

	/**
	 * 返回 FormulaList 特性值。
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getFormulaList() {
		if (ivjFormulaList == null) {
			try {
				ivjFormulaList = new nc.ui.pub.beans.UIList();
				ivjFormulaList.setName("FormulaList");
				ivjFormulaList.setBounds(0, 0, 400, 400);
				ivjFormulaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				ivjFormulaList.setListData(listItems);
				ivjFormulaList.addMouseListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaList;
	}

	/**
	 * 返回 FormulaPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getFormulaPanel() {
		if (ivjFormulaPanel == null) {
			try {
				ivjFormulaPanel = new nc.ui.pub.beans.UIPanel();
				ivjFormulaPanel.setName("FormulaPanel");
				ivjFormulaPanel.setBorder(new javax.swing.border.CompoundBorder());
				ivjFormulaPanel.setLayout(new java.awt.GridLayout());
				ivjFormulaPanel.setBounds(290, 7, 510, 335);
				ivjFormulaPanel.add(getFormulaTAreaScrollPanel(), getFormulaTAreaScrollPanel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaPanel;
	}

	/**
	 * 返回 FormulaScrollPane 特性值。
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getFormulaScrollPane() {
		if (ivjFormulaScrollPane == null) {
			try {
				ivjFormulaScrollPane = new nc.ui.pub.beans.UIScrollPane();
				ivjFormulaScrollPane.setName("FormulaScrollPane");
				ivjFormulaScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				ivjFormulaScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getFormulaScrollPane().setViewportView(getFormulaList());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaScrollPane;
	}

	/**
	 * 返回 FormulaTArea 特性值。
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextArea getFormulaTArea() {
		if (ivjFormulaTArea == null) {
			try {
				ivjFormulaTArea = new nc.ui.pub.beans.UITextArea();
				ivjFormulaTArea.setName("FormulaTArea");
				ivjFormulaTArea.setBorder(new javax.swing.border.EtchedBorder());
				ivjFormulaTArea.setBounds(0, 0, 171, 124);
				// user code begin {1}
				ivjFormulaTArea.setLineWrap(true);
				ivjFormulaTArea.setWrapStyleWord(true);
				ivjFormulaTArea.addCaretListener(new CaretListener() {
					public void caretUpdate(CaretEvent e) {
						m_caretPosi = e.getMark();
					}
				});
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaTArea;
	}

	/**
	 * 返回 FormulaTAreaScrollPanel 特性值。
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getFormulaTAreaScrollPanel() {
		if (ivjFormulaTAreaScrollPanel == null) {
			try {
				ivjFormulaTAreaScrollPanel = new nc.ui.pub.beans.UIScrollPane();
				ivjFormulaTAreaScrollPanel.setName("FormulaTAreaScrollPanel");
				getFormulaTAreaScrollPanel().setViewportView(getFormulaTArea());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormulaTAreaScrollPanel;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-8-21 9:46:30)
	 * @return nc.ui.pub.beans.UILabel
	 */
	public UITextArea getHintLabel() {
		if (ivjHintLabel == null) {
			try {
				ivjHintLabel = new UITextArea();
				ivjHintLabel.setEditable(false);
				ivjHintLabel.setName("HintLabel");
				ivjHintLabel.setLineWrap(true);
				ivjHintLabel.setWrapStyleWord(true);
				ivjHintLabel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000047")/*@res "注意：手工输入字符串时，请一定使用英文双引号！"*/);
//				ivjHintLabel.setBounds(1, 558, 342, 22);
				ivjHintLabel.setBounds(290, 375, 510, 98);
				//ivjHintLabel.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				//ivjHintLabel.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjHintLabel;
	}

	/**
	 * 返回 UIButton17 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getLeftPrthButton() {
		if (ivjLeftPrthButton == null) {
			try {
				ivjLeftPrthButton = new nc.ui.pub.beans.UIButton();
				ivjLeftPrthButton.setName("LeftPrthButton");
				ivjLeftPrthButton.setText("(");
				ivjLeftPrthButton.setBounds(140, 0, 20, 20);
				ivjLeftPrthButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjLeftPrthButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLeftPrthButton;
	}

	/**
	 * 返回 UIButton181 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getLIKEButton() {
		if (ivjLIKEButton == null) {
			try {
				ivjLIKEButton = new nc.ui.pub.beans.UIButton();
				ivjLIKEButton.setName("LIKEButton");
				ivjLIKEButton.setText("LIKE");
				ivjLIKEButton.setBounds(196, 0, 38, 20);
				ivjLIKEButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjLIKEButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLIKEButton;
	}

	/**
	 * 返回 MainPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getMainPanel() {
		if (ivjMainPanel == null) {
			try {
				ivjMainPanel = new nc.ui.pub.beans.UIPanel();
				ivjMainPanel.setName("MainPanel");
				ivjMainPanel.setLayout(null);
				getMainPanel().add(getOptButtonPanel(), getOptButtonPanel().getName());
				getMainPanel().add(getOptQKButtonPanel(), getOptQKButtonPanel().getName());//清空按钮
				getMainPanel().add(getFormulaPanel(), getFormulaPanel().getName());
				getMainPanel().add(getOperatorPanel(), getOperatorPanel().getName());
				getMainPanel().add(getFieldSelectPanel(), getFieldSelectPanel().getName());
				getMainPanel().add(getHintLabel());
//				JProgressBar jProgressBar1 = new JProgressBar();
//				jProgressBar1.setBounds(290, 475, 510, 2);
//				getMainPanel().add(jProgressBar1);
				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjMainPanel;
	}
	/**
	 * 返回 OptButtonPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getOptQKButtonPanel() {
		if (ivjOptQKButtonPanel == null) {
			try {
				ivjOptQKButtonPanel = new nc.ui.pub.beans.UIPanel();
				ivjOptQKButtonPanel.setName("ivjOptQKButtonPanel");
				ivjOptQKButtonPanel.setLayout(null);
				ivjOptQKButtonPanel.setBounds(720, 350, 100, 22);
				getOptQKButtonPanel().add(getCheckButton(), getCheckButton().getName());
				getOptQKButtonPanel().add(getClearButton(), getClearButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOptQKButtonPanel;
	}
	/**
	 * 返回 UIButton1 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getMultButton() {
		if (ivjMultButton == null) {
			try {
				ivjMultButton = new nc.ui.pub.beans.UIButton();
				ivjMultButton.setName("MultButton");
				ivjMultButton.setText("×");
				ivjMultButton.setBounds(0, 0, 20, 20);
				ivjMultButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjMultButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMultButton;
	}
	/**
	 * 返回 UIButton184 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getYWBButton() {
		if (ivjYWBButton == null) {
			try {
				ivjYWBButton = new nc.ui.pub.beans.UIButton();
				ivjYWBButton.setName("YWBButton");
				ivjYWBButton.setText("业务表名");
				ivjYWBButton.setBounds(342, 0, 70, 20);
				ivjYWBButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjYWBButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjYWBButton;
	}

	/**
	 * 返回 UIButton184 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getNOTButton() {
		if (ivjNOTButton == null) {
			try {
				ivjNOTButton = new nc.ui.pub.beans.UIButton();
				ivjNOTButton.setName("NOTButton");
				ivjNOTButton.setText("NOT");
				ivjNOTButton.setBounds(307, 0, 35, 20);
				ivjNOTButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjNOTButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjNOTButton;
	}

	/**
	 * 返回 OperatorPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getOperatorPanel() {
		if (ivjOperatorPanel == null) {
			try {
				ivjOperatorPanel = new nc.ui.pub.beans.UIPanel();
				ivjOperatorPanel.setName("OperatorPanel");
				ivjOperatorPanel.setLayout(null);
				ivjOperatorPanel.setBounds(290, 350, 440, 20);
				getOperatorPanel().add(getMultButton(), getMultButton().getName());
				getOperatorPanel().add(getDivButton(), getDivButton().getName());
				getOperatorPanel().add(getPlusButton(), getPlusButton().getName());
				getOperatorPanel().add(getReduceButton(), getReduceButton().getName());
				getOperatorPanel().add(getBiggerButton(), getBiggerButton().getName());
				getOperatorPanel().add(getSmallerButton(), getSmallerButton().getName());
				getOperatorPanel().add(getEqualButton(), getEqualButton().getName());
				getOperatorPanel().add(getLeftPrthButton(), getLeftPrthButton().getName());
				getOperatorPanel().add(getRightPrthButton(), getRightPrthButton().getName());
				getOperatorPanel().add(getLIKEButton(), getLIKEButton().getName());
				getOperatorPanel().add(getANDButton(), getANDButton().getName());
				getOperatorPanel().add(getORButton(), getORButton().getName());
				getOperatorPanel().add(getNOTButton(), getNOTButton().getName());
				getOperatorPanel().add(getYWBButton(), getYWBButton().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjOperatorPanel;
	}

	/**
	 * 返回 OptButtonPanel 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getOptButtonPanel() {
		if (ivjOptButtonPanel == null) {
			try {
				ivjOptButtonPanel = new nc.ui.pub.beans.UIPanel();
				ivjOptButtonPanel.setName("OptButtonPanel");
				ivjOptButtonPanel.setLayout(null);
				ivjOptButtonPanel.setBounds(350, 485, 150, 22);
				getOptButtonPanel().add(getCfmButton(), getCfmButton().getName());
				getOptButtonPanel().add(getCancelButton(), getCancelButton().getName());
//				getOptButtonPanel().add(getCheckButton(), getCheckButton().getName());
//				getOptButtonPanel().add(getClearButton(), getClearButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOptButtonPanel;
	}

	/**
	 * 返回 UIButton183 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getORButton() {
		if (ivjORButton == null) {
			try {
				ivjORButton = new nc.ui.pub.beans.UIButton();
				ivjORButton.setName("ORButton");
				ivjORButton.setText("OR");
				ivjORButton.setBounds(272, 0, 35, 20);
				ivjORButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjORButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjORButton;
	}

	/**
	 * 返回 UIButton12 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getPlusButton() {
		if (ivjPlusButton == null) {
			try {
				ivjPlusButton = new nc.ui.pub.beans.UIButton();
				ivjPlusButton.setName("PlusButton");
				ivjPlusButton.setText("+");
				ivjPlusButton.setBounds(40, 0, 20, 20);
				ivjPlusButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjPlusButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPlusButton;
	}

	/**
	 * 返回 UIButton13 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getReduceButton() {
		if (ivjReduceButton == null) {
			try {
				ivjReduceButton = new nc.ui.pub.beans.UIButton();
				ivjReduceButton.setName("ReduceButton");
				ivjReduceButton.setText("-");
				ivjReduceButton.setBounds(60, 0, 20, 20);
				ivjReduceButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjReduceButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjReduceButton;
	}

	/**
	 * 返回 UIButton18 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getRightPrthButton() {
		if (ivjRightPrthButton == null) {
			try {
				ivjRightPrthButton = new nc.ui.pub.beans.UIButton();
				ivjRightPrthButton.setName("RightPrthButton");
				ivjRightPrthButton.setText(")");
				ivjRightPrthButton.setBounds(160, 0, 20, 20);
				ivjRightPrthButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjRightPrthButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRightPrthButton;
	}

	/**
	 * 返回 UIButton15 特性值。
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getSmallerButton() {
		if (ivjSmallerButton == null) {
			try {
				ivjSmallerButton = new nc.ui.pub.beans.UIButton();
				ivjSmallerButton.setName("SmallerButton");
				ivjSmallerButton.setText("<");
				ivjSmallerButton.setBounds(100, 0, 20, 20);
				ivjSmallerButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
				// user code begin {1}
				ivjSmallerButton.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSmallerButton;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.GridLayout());
				getUIDialogContentPane().add(getMainPanel(), getMainPanel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// System.out.println("--------- 未捕捉到的异常 ---------");
		Logger.error(exception);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("DapFormuDefUI");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(815, 550);
			setResizable(true);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000048")/*@res "公式定义"*/);
			setContentPane(getUIDialogContentPane());
//			getFieldList();//ytq 增加
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
	Map<String, String> cmap=null;
	public void mouseClicked(MouseEvent e) {
		if(e.getSource().equals(ywrightList)){
			String hsname=ywrightList.getSelectedValue().toString();
			if(e.getClickCount()==1){
				if(ywhsdescmap.containsKey(hsname)){
					getHintLabel().setText(ywhsdescmap.get(hsname));
			}
			}
			String ins=null;
			if(e.getClickCount()==2){
				if(fu==null){
					MessageDialog.showErrorDlg(this, "没有初始化环境", "请初始化环境！");
					return;
				}
				if(hsname.equals("HR_TOTAL")){/*
					WizardDialog aWizardDialog = new WizardDialog(this);
					 aWizardDialog.setModal(true);
					 aWizardDialog.show();
				      Insets insets = aWizardDialog.getInsets();
//				      aWizardDialog.setSize(aWizardDialog.getWidth() + insets.left + insets.right, aWizardDialog.getHeight() + insets.top + insets.bottom);
//				      aWizardDialog.setVisible(true);
*/				}else{
					boolean haref=fu.hasReferDlg(hsname);
					if(haref){
						String ss=fu.doFuncRefer(hsname);
						if(ss!=null&&ss.length()>0){
							ss=("-b"+hsname+"("+ss+")-e").replaceAll("'", "\"");
							ins=ss;
						}
					}else{
						ParamInputDlg dg=new ParamInputDlg(this,hsname);
						dg.showModal();
						String res=dg.getResults();
						if(res!=null&&res.length()>0){
							ins="-b"+res+"-e";
						}
					}
				}
				if(ins!=null&&ins.length()>0){

					getFormulaTArea().insert("initEnv( , , , ,"+ins+")",m_caretPosi);
					getHintLabel().setText(intrudse[getInvIndex()]);
				}
			}
		}else if(e.getSource().equals(ywleftList)&&e.getClickCount()==1){
			
				

				String[] hs=ywhsmap.get(ywleftList.getSelectedValue().toString());
						ListModel lm = 
							new DefaultComboBoxModel(hs);
						ywrightList.setModel(lm);
			

		}else if (e.getSource().equals(getFormulaList()) && e.getClickCount() == 2) {
			UIList list = (UIList) e.getSource();
			String val=list.getSelectedValue().toString();
			if(val.startsWith("getCurAccountMonth")||val.startsWith("getCurAccountYear")){
				AccountqjDLG dlg=new AccountqjDLG(this);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null&&ret.length()>0){
					val=val.replace(" ", "");
					getFormulaTArea().insert(val.substring(0,val.length()-1)+"\""+ret+"\")", m_caretPosi);
				}
			}if(val.startsWith("getPKServlet")){
				DataUrlDLG dlg=new DataUrlDLG(this);
				dlg.showModal();
//				MessageDialog.showErrorDlg(this, "11", "22");
				String ret=dlg.getRes();
				getFormulaTArea().insert("\""+ret+"\"", m_caretPosi);
			}else{
				getFormulaTArea().insert(val, m_caretPosi);
			}
		}else if(e.getSource().equals(getFormulaList())&&e.getClickCount()==1){
			getHintLabel().setText(intrudse[getFormulaList().getSelectedIndex()]);
		}else{
			getHintLabel().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101201", "UPP101201-000047")/*@res "注意：手工输入字符串时，请一定使用英文双引号！"*/);
		}
		boolean fromHead = e.getSource().equals(getFieldList());
		boolean fromBody = e.getSource().equals(getFieldListBody());
		boolean ncleft=e.getSource().equals(ncleftList);
		boolean ncriget=e.getSource().equals(ncrightList);
		boolean wbleft=e.getSource().equals(leftList);
		boolean wbrigtht=e.getSource().equals(rightList);
		boolean self=e.getSource().equals(selfl);
		boolean gl=e.getSource().equals(gll);
		boolean gm=e.getSource().equals(glm);
		boolean gr=e.getSource().equals(glr);

		if(gl&&e.getClickCount()==1){
			DipDatadefinitBVO bvo=(DipDatadefinitBVO) glmap.get(gll.getSelectedValue().toString());
			cmap=new HashMap<String, String>();
			String glbpk=bvo.getQuotetable();
			try{
				DipContdataVO[] cvos=(DipContdataVO[])HYPubBO_Client.queryByCondition(DipContdataVO.class, "nvl(dr,0)=0 and contcolcode='"+glbpk+"'");
				if(cvos!=null&&cvos.length>0){
					String key="";
					for(DipContdataVO cvo:cvos){
						key=key+"'"+cvo.getExtetabcode()+"',";
					}
					if(key!=null&&key.length()>0){
						key=key.substring(0, key.length()-1);
						DipDatadefinitHVO[] hvos=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "Pk_Datadefinit_h in("+key+") and nvl(dr,0)=0");
						if(hvos!=null&&hvos.length>0){
							String[] str=new String[hvos.length];
							int i=0;
							for(DipDatadefinitHVO hvoi:hvos){
								str[i]=hvoi.getMemorytable()+" <"+hvoi.getSysname()+">";
								cmap.put(str[i], hvoi.getPrimaryKey());
								i++;
							}
	
							ListModel lm = 
								new DefaultComboBoxModel(str);
							glm.setModel(lm);
						}
					}
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}else if(gl&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+tablename+"."+gll.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
		}
		if(gm&&e.getClickCount()==1){
			if(cmap.containsKey(glm.getSelectedValue())){
				String key=cmap.get(glm.getSelectedValue());
				if(key!=null&&key.length()>0){
					DipDatadefinitBVO[] bvos=null;
					try {
						bvos = (DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "Pk_Datadefinit_h ='"+key+"' and nvl(dr,0)=0");
					} catch (UifException e1) {
						e1.printStackTrace();
					}
					if(bvos!=null&&bvos.length>0){
						String [] str=new String[bvos.length];
						int i=0;
						for(DipDatadefinitBVO bvo:bvos){
							str[i]=bvo.getEname()+" <"+bvo.getCname()+">";
							i++;
						}
						ListModel lm = 
							new DefaultComboBoxModel(str);
						glr.setModel(lm);
					}
				}
			}
		}else if(gm&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+glm.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
		}
		
		if(gr&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+glm.getSelectedValue().toString().split(" <")[0]+"."+glr.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
		}
		
		if(self&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+tablename+"."+selfl.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
		}
		if(ncleft&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+ncleftList.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
			
		}else if(ncleft&&e.getClickCount()==1){
			String pk=ncmap.get(ncleftList.getSelectedValue().toString());
			try {
				DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "Pk_Datadefinit_h='"+pk+"'");
				if(bvos!=null&&bvos.length>0){
					String [] str=new String[bvos.length];
					int i=0;
					for(DipDatadefinitBVO bvo:bvos){
						str[i]=bvo.getEname()+" <"+bvo.getCname()+">";
						i++;
					}
					ListModel lm = 
						new DefaultComboBoxModel(str);
					ncrightList.setModel(lm);
				}
			} catch (UifException e1) {
				e1.printStackTrace();
			}
		}
		if(wbleft&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+leftList.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
			
		}else if(wbleft&&e.getClickCount()==1){
			String pk=wbmap.get(leftList.getSelectedValue().toString());
			try {
				DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "Pk_Datadefinit_h='"+pk+"'");
				if(bvos!=null&&bvos.length>0){
					String [] str=new String[bvos.length];
					int i=0;
					for(DipDatadefinitBVO bvo:bvos){
						str[i]=bvo.getEname()+" <"+bvo.getCname()+">";
						i++;
					}
					ListModel lm = 
						new DefaultComboBoxModel(str);
					rightList.setModel(lm);
				}
			} catch (UifException e1) {
				e1.printStackTrace();
			}
		}
		if(wbrigtht&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+leftList.getSelectedValue().toString().split(" <")[0]+"."+rightList.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
		}
		if(ncriget&&e.getClickCount()==2){
			getFormulaTArea().insert("\""+ncleftList.getSelectedValue().toString().split(" <")[0]+"."+ncrightList.getSelectedValue().toString().split(" <")[0]+"\"",m_caretPosi);
		}
		
		
		if ((fromHead || fromBody) && e.getClickCount() == 2) {

			UIList list = (UIList) e.getSource();
			DapDefItemVO selectObject = (DapDefItemVO) list.getSelectedValue();
			DefitemVO selectedItem = selectObject.getDefItems();
			if (selectedItem.getHeadflag().equals(new nc.vo.pub.lang.UFBoolean("Y"))) {
				getFormulaTArea().insert("@" + selectedItem.getAttrname().trim().toUpperCase() + /*selectedItem.getItemtype().toString().trim() +*/ "@",
						m_caretPosi);
			} else {
				getFormulaTArea().insert("#" + selectedItem.getAttrname().trim() + /*selectedItem.getItemtype().toString().trim() +*/ "#",
						m_caretPosi);
			}
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-6-8 15:11:41)
	 * @param items nc.vo.dap.vouchtemp.DefitemVO[]
	 */
	public void setBillItems(DefitemVO[] items) {
		m_allBillItems = DapDefItemVO.toArray(items);
		int len = m_allBillItems == null ? 0 : m_allBillItems.length;
		if (len == 0) {
			MessageDialog.showWarningDlg(new java.awt.Container(), nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("101201", "UPP101201-000049")/*@res "单据项查询结果错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("101201",
					"UPP101201-000050")/*@res "没有任何对应的单据项"*/);
			return;
		}

		List<DapDefItemVO> header = new ArrayList<DapDefItemVO>(len);
		List<DapDefItemVO> body = new ArrayList<DapDefItemVO>(len);
		for (int i = 0; i < len; i++) {
			if (m_allBillItems[i].getHeadflag().booleanValue() == true) {
				header.add(m_allBillItems[i]);
			} else {
				body.add(m_allBillItems[i]);
			}
		}
		m_headerBillItems = new DapDefItemVO[0];
		m_headerBillItems = header.toArray(m_headerBillItems);

		m_bodyBillItems = new DapDefItemVO[0];
		m_bodyBillItems = body.toArray(m_bodyBillItems);

		getFieldList().setListData(m_headerBillItems);
		getFieldListBody().setListData(m_bodyBillItems);

	}



	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-5-22 16:16:47)
	 * @param formula java.lang.String
	 */
	public void setFormula(String formula) {
		getFormulaTArea().setText(formula);
	}


	/**
	 * This method initializes matchText	
	 * 	
	 * @return nc.ui.pub.beans.UITextField	
	 */
	private UITextField getMatchText() {
		if (matchText == null) {
			matchText = new UITextField();
			matchText.setBounds(new Rectangle(322, 570, 125, 20));
		}
		return matchText;
	}

	/**
	 * This method initializes btnSearch	
	 * 	
	 * @return nc.ui.pub.beans.UIButton	
	 */
	private UIButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new UIButton();
			btnSearch.setText("定位");
			btnSearch.setBounds(new Rectangle(459, 570, 31, 20));
			btnSearch.setMargin(new java.awt.Insets(2, 4, 2, 4));
			btnSearch.addActionListener(this);
		}
		return btnSearch;
	}
	public String getPk_table() {
		return pk_table;
	}
	public void setPk_table(String pk_table) {
		this.pk_table = pk_table;
	}
} //  @jve:decl-index=0:visual-constraint="10,10"