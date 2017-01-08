/*
 * Created on 2005-6-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.vo.pub.formulaset;

import java.util.Arrays;
import java.util.List;

/**
 * @author cch
 * @nopublish
 *
 * 数据库函数统一定义,便于使用查找
 */
public class DBFunctionDefination
{
    private static DBFunctionDefination dbfuncitons = new DBFunctionDefination();
    /**
     * 数据库函数列表  FIXME
     */    
    public final static String[] POST_CONVERT_FUNCTIONS = new String[] {
            "GETCOLVALUE","GETCOLVALUE2", 
            "GETCOLNMV","GETCOLNMV2",
            "CVS", "CVN", "GETCOLVALUERES","GETDATABYKEY" };
    //ytq 增加一个函数 GETDATABYKEY 2011-05-15
    protected List m_keywords = null;
    
    public List getKeyWords()
    {
        if(m_keywords == null)
            m_keywords = Arrays.asList(POST_CONVERT_FUNCTIONS);
        return m_keywords;
    }
    
    public static DBFunctionDefination getInstance()
    {
        return dbfuncitons;
    }
    
   
}
