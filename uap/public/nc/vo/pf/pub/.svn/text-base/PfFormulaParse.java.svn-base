package nc.vo.pf.pub;

import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 平台公式解析器
 * 
 * @author 童志杰 2001-8-17
 * @modifier 雷军 2005-1-25 应cch要求,修改isFormulaRight()方法
 * @modifier 王少伟 2005-5-26 效率优化
 */
public class PfFormulaParse {

	private PfFormulaParse instance = null;

	/** 公式 */
	private String m_strFormula;

	/** 未转换前的原始公式 */
	private String m_oldFormula;

	/** 传人公式的数据 */
	private AggregatedValueObject m_voAggvo;

	// 2002-06-06
	/** 传入的成本VO数据 */
	private CircularlyAccessibleValueObject[] m_costVos = null;

	private String m_strFeatureField = null;

	/** AggregatedValueObject的子表行号 */
	private int m_iLineNumber = 0;

	/** 解析后的公式变量列表 */
	private FormVarVO[] m_aryFormVars;

	private FormulaParse fp = null;

	private boolean isField = false;

	public PfFormulaParse() {
		super();
		getFp();
	}

	private PfFormulaParse getInstance() {
		if (instance == null) {
			instance = new PfFormulaParse();
		}
		return instance;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:06:17)
	 * 
	 * @param formula
	 *                java.lang.String
	 */
	public PfFormulaParse(String formula, AggregatedValueObject dataVO,
			CircularlyAccessibleValueObject[] costVo, String featureField, int lineNumber) {
		super();
		getFp();
		setM_strFormula(formula);
		setM_oldFormula(formula);
		setM_voAggvo(dataVO);
		setM_costVos(costVo);
		setM_strFeatureField(featureField);
		setM_iLineNumber(lineNumber);
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:06:17)
	 * 
	 * @param formula
	 *                java.lang.String
	 */
	public PfFormulaParse(String formula, AggregatedValueObject dataVO, int lineNumber) {
		super();
		getFp();
		setM_strFormula(formula);
		setM_oldFormula(formula);
		setM_voAggvo(dataVO);
		setM_iLineNumber(lineNumber);
		initialize();
	}

	/**
	 * 根据类型转换Object。 创建日期：(01-8-21 13:54:32)
	 */
	public static Object convertReturnType(String formula, String value, int type)
			throws PfFormException {
		try {
			switch (type) {
				case IDapType.INTEGER:
					if (value == null)
						return new Integer(0);
					else
						return new Integer(value);
				case IDapType.STRING:
					return value;

				case IDapType.UFBOOLEAN:
					if (value == null)
						return new nc.vo.pub.lang.UFBoolean(true);
					else
						return new nc.vo.pub.lang.UFBoolean(value);

				case IDapType.UFDATE:
					if (value == null)
						return null;
					else
						return new nc.vo.pub.lang.UFDate(value);

				case IDapType.UFDOUBLE:
					if (value == null)
						return new nc.vo.pub.lang.UFDouble(0);
					else
						return new nc.vo.pub.lang.UFDouble(value);

				default:
					break;

			}
		} catch (Exception e) {
			throw new PfFormException("公式运算后返回值类型转换错误.", formula, PfFormException.CALCULATE_ERROR);
		}
		return null;
	}

	/**
	 * 根据主键从detailVos中找到具体的VO, 再从该VO中找出字段值 创建日期：(2002-6-6 10:14:46)
	 */
	private static CircularlyAccessibleValueObject findFieldValue(
			CircularlyAccessibleValueObject[] detailVos, String strPrimaryKey) {
		try {
			for (int i = 0; i < detailVos.length; i++) {
				if (detailVos[i].getPrimaryKey().equals(strPrimaryKey)) { return detailVos[i]; }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getAggvo() {
		return m_voAggvo;
	}

	/**
	 * 返回公式固定值 创建日期：(2002-6-6 8:58:50)
	 */
	private static Object getConstValue(String strFormula, int returnType) throws PfFormException {
		int intHIndex = strFormula.indexOf("@");
		int intBIndex = strFormula.indexOf("#");
		int intCIndex = strFormula.indexOf("$");
		int intBDPk = strFormula.indexOf("getBDPk(");
		if (intHIndex >= 0 || intBIndex >= 0 || intCIndex >= 0 || intBDPk >= 0) {
			return null;
		} else {
			if (strFormula.startsWith("\"")) {
				strFormula = strFormula.substring(1, strFormula.length() - 1);
				return convertReturnType(strFormula, strFormula, returnType);
			} else {
				return convertReturnType(strFormula, strFormula, returnType);
			}
		}
	}

	/**
	 * 得到字段的前缀数据 增加字段处理,如果为字段直接返回值,不用进行公式解析 创建日期：(2002-6-5 15:20:10)
	 * 
	 * @return java.lang.String
	 */
	private Object getFieldValue(String strFormula, AggregatedValueObject dataVo,
			CircularlyAccessibleValueObject[] costVo, String attField, int lineNumber, int returnType)
			throws PfFormException {

		String strFirst = strFormula.substring(0, 1);
		int len = strFormula.length();
		int intSecond = strFormula.indexOf(strFirst, 1);
		String strField = strFormula.substring(1, len - 2);
		try {
			if (intSecond + 1 == len) {

				Object retValue = null;
				// 获取字段
				if (strFirst.equals("@")) {
					// 表头
					isField = true;
					retValue = dataVo.getParentVO().getAttributeValue(strField);
				} else if (strFirst.equals("#")) {
					// 表体
					if (costVo == null) {
						isField = true;
						retValue = dataVo.getChildrenVO()[lineNumber].getAttributeValue(strField);
					} else {
						isField = true;
						String primaryKey = (String) costVo[lineNumber].getAttributeValue(attField);
						retValue = findFieldValue(dataVo.getChildrenVO(), primaryKey).getAttributeValue(
								strField);
					}
				} else {
					// 其它
					isField = true;
					if (costVo == null)
						return null;
					else
						retValue = costVo[lineNumber].getAttributeValue(strField);
				}
				//
				if (retValue instanceof String && returnType != IDapType.STRING) {
					return convertReturnType(strFormula, (String) retValue, returnType);
				} else {
					return retValue;
				}
			} else {
				isField = false;
			}
		} catch (Exception ex) {
			throw new PfFormException("VO中不存在公式中定义的变量:" + strField, strFormula,
					PfFormException.DATA_ERROR);
		}
		return null;
	}

	/**
	 * 指定返回类型获得结果。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return java.lang.Object
	 */
	public java.lang.Object getFormResult(int returnType) throws PfFormException {
		return getFormValue(returnType);
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFormula() {
		return m_strFormula;
	}

	/**
	 * 返回公式解析的结果 
	 * @return java.lang.Object
	 */
	private Object getFormValue(int returnType) throws PfFormException {

		if (m_voAggvo == null) { throw new PfFormException("传人公式的AggregatedValueObject为null!:",
				m_oldFormula, PfFormException.DATA_ERROR); }

		if (getFormula() == null || getFormula().trim().length() == 0)
			return null;

		// 获得变量值
		FormVarVO[] fvo = getFormVars();
		if (fvo != null) {
			for (int i = 0; i < fvo.length; i++) {
				getFp().addVariable(fvo[i].getRealVarName(), fvo[i].getVarValue());
			}
		}
		Object value = null;
		try {
			Object[][] rs = getFp().getValueOArray();
			int h = rs == null ? 0 : rs.length;
			int w = h > 0 ? (rs[h - 1] == null ? 0 : rs[h - 1].length) : 0;
			if (h > 0 && w > 0) {
				value = rs[h - 1][w - 1];
			}
			if (getFp().getError() != null)
				throw new PFBusinessException(getFp().getError());
		} catch (Exception e) {
			throw new PfFormException("公式计算错误!:" + m_oldFormula + ",原因:" + e.getMessage(),
					PfFormException.CALCULATE_ERROR);
		}

		return value;

	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return nc.vo.pf.pub.FormVarVO[]
	 */
	public nc.vo.pf.pub.FormVarVO[] getFormVars() throws PfFormException {

		VarryVO vo = getFp().getVarry();
		FormVarVO[] varvo = null;

		if (vo != null && vo.getVarry() != null && vo.getVarry().length > 0) {
			varvo = new FormVarVO[vo.getVarry().length];
			for (int i = 0; i < varvo.length; i++) {
				CircularlyAccessibleValueObject cvo = null;
				varvo[i] = new FormVarVO();
				// 变量
				varvo[i].setVarName(vo.getVarry()[i]);
				// 表志
				varvo[i].setHeadData(FormulaProc.isHeadVar(vo.getVarry()[i]));
				// 类型
				varvo[i].getVarType(); // 获得变量的类型

				// 得到转换后的属性值
				String strVarName = varvo[i].getVarName();

				if (varvo[i].getHeadData().equals("H")) {
					cvo = getAggvo().getParentVO();
				} else if (varvo[i].getHeadData().equals("B")) {
					if (m_costVos == null) {
						cvo = getAggvo().getChildrenVO()[getLineNumber()];
					} else {
						String primaryKey = (String) m_costVos[getLineNumber()]
								.getAttributeValue(m_strFeatureField);
						cvo = findFieldValue(getAggvo().getChildrenVO(), primaryKey);
					}
				} else if (varvo[i].getHeadData().equals("C")) {
					if (m_costVos != null)
						cvo = m_costVos[getLineNumber()];
				} else {
					throw new PfFormException("VO中不存在公式中定义的变量:" + strVarName, m_oldFormula,
							PfFormException.DATA_ERROR);
				}

				// String[] allNames = cvo.getAttributeNames();
				// Vector v = new Vector();
				// for (int j = 0; j < allNames.length; j++) {
				// v.addElement(allNames[j]);
				// }

				// if (!v.contains(strVarName))
				// throw new PfFormException(
				// "VO中不存在公式中定义的变量:" + strVarName,
				// m_oldFormula,
				// PfFormException.DATA_ERROR);
				Object value = null;
				if (cvo != null)
					value = cvo.getAttributeValue(strVarName);

				if (value == null)
					value = "";

				varvo[i].setVarValue(value);

			}

			m_aryFormVars = varvo;
		}

		return m_aryFormVars;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-21 9:35:00)
	 * 
	 * @return nc.ui.pub.formulaparse.FormulaParse
	 */
	public FormulaParse getFp() {
		if (fp == null) {
			fp = getFormulaParse();
		}
		return fp;
	}

	public static FormulaParse getFormulaParse() {
		FormulaParse parser = new FormulaParse();
		/* add by st 2003-10-20 for selfdefine func */
		parser.setSelfMethod("nc.vo.dap.vouchtemp.DapSelfFormulaFunc", "getBDPk", String.class,
				new Class[] { String.class, String.class, String.class });
		/**/
		parser.setSelfMethod("nc.vo.dap.vouchtemp.DapSelfFormulaFunc", "getHL", UFDouble.class,
				new Class[] { String.class, String.class, UFDate.class, String.class });
		// fp.setExpress(getFormula());
		// ytq 增加如下函数 getYMLastDay,getDataByKey
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getGenPK", String.class,
				new Class[] { });	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "cmonth", String.class,
				new Class[] { });	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "cyear", String.class,
				new Class[] { });	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "initEnv", String.class,
				new Class[] { String.class,String.class,String.class,String.class ,String.class});
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "initEnvHrFunction", String.class,
				new Class[] { String.class,String.class,String.class,String.class});
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getYMLastDay", String.class,
				new Class[] { String.class, String.class});	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getCurAccountYear", String.class,
				new Class[] {  String.class });	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getCurAccountMonth", String.class,
				new Class[] {  String.class });	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getDataByKey", String.class,
				new Class[] { String.class,String.class, Object.class});	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "querySqlByDesign", String.class,
				new Class[] { String.class,String.class});	
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "random", String.class,
				new Class[] { Object.class});
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "defGetColValue", String.class,
				new Class[] { String.class,String.class,String.class,String.class});
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "trim", String.class,
				new Class[] { Object.class});
		parser.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "formatDef", String.class,
				new Class[] { Object.class, Object.class});
		return parser;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-21 10:41:37)
	 * 
	 * @return int
	 */
	public int getLineNumber() {
		return m_iLineNumber;
	}

	/**
	 * 静态调用公式解析 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return java.lang.Object
	 */
	public static Object getParseValue(String formula, AggregatedValueObject dataVO,
			CircularlyAccessibleValueObject[] costVo, String attrField, int lineNumber, int returnType)
			throws PfFormException {

		if (formula == null)
			return null;
		// fgj2002-06-06****************************
		// 判断固定值
		PfFormulaParse pfParser = new PfFormulaParse();
		Object constValue = getConstValue(formula, returnType);
		if (constValue != null) { return constValue; }
		// 判断字段值
		Object fieldValue = pfParser.getFieldValue(formula, dataVO, costVo, attrField, lineNumber,
				returnType);
		if (pfParser.isField) { return fieldValue; }
		// fgj2002-06-06****************************
		pfParser.setM_strFormula(formula);
		pfParser.setM_oldFormula(formula);
		pfParser.setM_voAggvo(dataVO);
		pfParser.setM_costVos(costVo);
		pfParser.setM_strFeatureField(null);
		pfParser.setM_iLineNumber(lineNumber);
		pfParser.initialize();

		Object o = pfParser.getFormResult(returnType);
		return o;

	}

	/**
	 * 静态调用公式解析 如果成本VO不为空,行号指定为成本数据的行号,否则指定为单据行号 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return java.lang.Object
	 */
	public static Object getParseValue(String formula, AggregatedValueObject dataVO, int lineNumber,
			int returnType) throws PfFormException {

		return getParseValue(formula, dataVO, null, null, lineNumber, returnType);

	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-21 10:03:57)
	 */
	private void initialize() {
		m_strFormula = FormulaProc.replaceFormVar(m_strFormula);
		String[] eArray = getFormula().split(";");
		for (int i = 0; i < eArray.length; i++) {
			eArray[i] = eArray[i].trim();
		}
		fp.setExpressArray(eArray);
	}

	/**
	 * 判断一个表达式是否公式解析能够识别的公式。 公式以@#开头。 创建日期：(01-8-17 10:51:46)
	 */
	public static boolean isFormula(String express) {
		if (express == null || express.trim().length() == 0)
			return true;
		return isFormulaRight(FormulaProc.replaceFormVar(express));

	}

	/**
	 * 判断一个表达式是否公式解析能够识别的公式, 公式以STR_HEAD,STR_BODY开头。 创建日期：(01-8-17 10:51:46)
	 */
	public static boolean isFormulaRight(String express) {
		FormulaParse parser = getFormulaParse();
		parser.setExpress(express);
		boolean bRight = parser.checkExpress(express);
		if (!bRight)
			return false;

		String[] names = parser.getVarry().getVarry();

		if (names == null)
			return true;

		for (int i = 0; i < names.length; i++) {
			if (!names[i].startsWith(FormulaProc.STR_VOBODY)
					|| !names[i].endsWith(FormulaProc.STR_VOBODY)) {
				if (!names[i].startsWith(FormulaProc.STR_VOHEAD)
						|| !names[i].endsWith(FormulaProc.STR_VOHEAD)) {
					if (!names[i].startsWith(FormulaProc.STR_VOCOST)
							|| !names[i].endsWith(FormulaProc.STR_VOCOST))
						return false;
				}
			}

			if (names[i].indexOf(FormulaProc.STR_VOBODY, 1) != names[i]
					.lastIndexOf(FormulaProc.STR_VOBODY))
				return false;
			if (names[i].indexOf(FormulaProc.STR_VOHEAD, 1) != names[i]
					.lastIndexOf(FormulaProc.STR_VOHEAD))
				return false;

			if (names[i].indexOf(FormulaProc.STR_VOCOST, 1) != names[i]
					.lastIndexOf(FormulaProc.STR_VOCOST))
				return false;
		}

		return true;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-30 15:17:45)
	 * 
	 * @return boolean
	 * @param str
	 *                java.lang.String
	 */
	/*
	 * private boolean isNumber(String str) { try { Double.valueOf(str); }
	 * catch (Exception e) { return false; } return true; }
	 */

	/**
	 * 当没有变量时,自动判断常量的数据类型。
	 */
	public int judgTypeAutomatically(String value) {
		int type = IDapType.STRING;
		// UFDouble
		try {
			UFDouble v = new UFDouble(value);
			type = IDapType.UFDOUBLE;
			return type;
		} catch (Exception e) {
		}

		// 整型
		try {
			Integer v = new Integer(value);
			type = IDapType.INTEGER;
			return type;
		} catch (Exception e) {
		}

		// 日期型

		try {
			if (UFDate.getValidUFDateString(value) == null)
				throw new Exception("UFDate construct exception.");
			type = IDapType.UFDATE;
			return type;
		} catch (Exception e) {
		}

		// Date Time型
		try {
			if (UFDateTime.getValidUFDateTimeString(value) == null)
				throw new Exception("UFDateTime construct exception.");
			type = IDapType.UFDATETIME;
			return type;
		} catch (Exception e) {
		}

		// String

		return type;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @param newAggvo
	 *                nc.vo.pub.AggregatedValueObject
	 */
	public void setAggvo(nc.vo.pub.AggregatedValueObject newAggvo) {
		m_voAggvo = newAggvo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @param newFormula
	 *                java.lang.String
	 */
	public void setFormula(java.lang.String newFormula) {
		m_strFormula = newFormula;
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-17 11:04:46)
	 * 
	 * @param newFormVars
	 *                nc.vo.pf.pub.FormVarVO[]
	 */
	public void setFormVars(nc.vo.pf.pub.FormVarVO[] newFormVars) {
		m_aryFormVars = newFormVars;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-21 9:35:00)
	 * 
	 * @param newFp
	 *                nc.bs.pub.formulaparse.FormulaParse
	 */
	public void setFp(FormulaParse newFp) {
		fp = newFp;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-8-21 10:41:37)
	 * 
	 * @param newLineNumber
	 *                int
	 */
	public void setLineNumber(int newLineNumber) {
		m_iLineNumber = newLineNumber;
	}

	/**
	 * @return 返回 m_iLineNumber。
	 */
	public int getM_iLineNumber() {
		return m_iLineNumber;
	}

	/**
	 * @param lineNumber
	 *                要设置的 m_iLineNumber。
	 */
	public void setM_iLineNumber(int lineNumber) {
		m_iLineNumber = lineNumber;
	}

	/**
	 * @return 返回 m_oldFormula。
	 */
	public String getM_oldFormula() {
		return m_oldFormula;
	}

	/**
	 * @param formula
	 *                要设置的 m_oldFormula。
	 */
	public void setM_oldFormula(String formula) {
		m_oldFormula = formula;
	}

	/**
	 * @return 返回 m_strFormula。
	 */
	public String getM_strFormula() {
		return m_strFormula;
	}

	/**
	 * @param formula
	 *                要设置的 m_strFormula。
	 */
	public void setM_strFormula(String formula) {
		m_strFormula = formula;
	}

	/**
	 * @return 返回 m_voAggvo。
	 */
	public AggregatedValueObject getM_voAggvo() {
		return m_voAggvo;
	}

	/**
	 * @param aggvo
	 *                要设置的 m_voAggvo。
	 */
	public void setM_voAggvo(AggregatedValueObject aggvo) {
		m_voAggvo = aggvo;
	}

	/**
	 * @return 返回 m_costVos。
	 */
	public CircularlyAccessibleValueObject[] getM_costVos() {
		return m_costVos;
	}

	/**
	 * @param vos
	 *                要设置的 m_costVos。
	 */
	public void setM_costVos(CircularlyAccessibleValueObject[] vos) {
		m_costVos = vos;
	}

	/**
	 * @return 返回 m_strFeatureField。
	 */
	public String getM_strFeatureField() {
		return m_strFeatureField;
	}

	/**
	 * @param featureField
	 *                要设置的 m_strFeatureField。
	 */
	public void setM_strFeatureField(String featureField) {
		m_strFeatureField = featureField;
	}

	/**
	 * 静态调用公式解析 创建日期：(01-8-17 11:04:46)
	 * 
	 * @return java.lang.Object
	 */
	public Object getParseValueFip(String formula, AggregatedValueObject dataVO, int lineNumber,
			int returnType) throws PfFormException {

		if (formula == null)
			return null;
		// fgj2002-06-06****************************
		// 判断固定值

		Object constValue = getConstValue(formula, returnType);
		if (constValue != null) { return constValue; }
		// 判断字段值
		Object fieldValue = getInstance().getFieldValue(formula, dataVO, null, null, lineNumber,
				returnType);
		if (getInstance().isField) { return fieldValue; }
		// fgj2002-06-06****************************
		getInstance().getFp();
		getInstance().setM_strFormula(formula);
		getInstance().setM_oldFormula(formula);
		getInstance().setM_voAggvo(dataVO);
		getInstance().setM_costVos(null);
		getInstance().setM_strFeatureField(null);
		getInstance().setM_iLineNumber(lineNumber);
		getInstance().initialize();

		Object o = getInstance().getFormResult(returnType);
		return o;
	}
}
