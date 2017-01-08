package nc.bs.excel.pub;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
public class ExpToExcel 
{
	
	private WritableWorkbook   book = null; 
	private WritableSheet   sheet = null;
	public String path = "";
	public String name = "";
	public String names;
	public String nameTemp = "";
	public ExpExcelVO [] vos = null;
	private int fieldNO=0;
    public ExpToExcel(String filePath,String sheetName,String fileNames,ExpExcelVO [] vo,String fileTemp) throws IOException{
//    	super();
    	path = filePath;
    	name = sheetName;
    	names = fileNames;
    	nameTemp = fileTemp;
    	vos = vo;
    	init();
		
	}
    public void init(){
        /**********�ж��ļ��Ƿ����********/
        try {
            File file = new File(path);
            
//            /**********�ж��ļ��Ƿ����********/
//            if(file.exists()){
//                File filetemp = new File(nameTemp);//��ʱ�ļ�
//                Workbook wb = Workbook.getWorkbook(file);
//                book = Workbook.createWorkbook(filetemp, wb);//��wb�����ռ���������
//                int wb_sheet_length = wb.getSheets().length;
//                /**********���new sheet***/
//                sheet = book.createSheet(name, wb_sheet_length+1);//sheet���ֲ������Ѿ����ڵ���ͬ����������
//                /**************д��������*******/
//                createToExcel(names,vos);
//                book.write();
//                book.close();
//                wb.close();
//                /**********ɾ��file ���޸�file����********/
//                file.delete();
//                filetemp.renameTo(file);//�ļ���������
//                
//            }else{
               
                book = Workbook.createWorkbook(file);
                /** **********����������************ */
                sheet = book.createSheet(name, 0);
                /**************д������*******/
                createToExcel(names,vos);
                book.write();
                book.close();
//            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createToExcel(String fileNames,ExpExcelVO [] vo)throws IOException{
    	try {
			excelLab(fileNames);
			excelLine(vo);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
    	
    }
    /**
     * ����excle����ͷ
     * */
	private void excelLab(String fileNames) throws RowsExceededException, WriteException{
		if("".equals(fileNames) || fileNames==null)
			return ;
		String [] labName = fileNames.split(",");
		fieldNO=labName.length;
		for(int i = 0;i < labName.length;i++){
			Label lab = new Label(i,0,labName[i]);
			sheet.addCell(lab);
		}
	}
	/**
     * ����excle�ĵ�Ԫ��
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
     * */
    private void excelLine(ExpExcelVO [] vo) throws RowsExceededException, WriteException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
    	if(vo == null || vo.length == 0)
    		return ;
    	Label lines = null;
    	for(int i = 0; i<vo.length;i++){
    		for(int j=1;j<=fieldNO;j++){
//    			Method mset=ExpExcelVO.class.getMethod("getLine"+j);
//    			Object ob=mset.invoke(vo[i]);
    			Object ob=vo[i].getAttributeValue("line"+j);
    			if(ob!=null){
    				lines = new  Label(j-1,i+1,ob.toString()); 
    				sheet.addCell(lines); 
    			}
    		}
    		/*if(vo[i].getLine1()!=null){
				lines = new  Label(0,i+1,vo[i].getLine1()); 
				sheet.addCell(lines); 
			}
    		if(vo[i].getLine2()!=null){
    			lines = new  Label(1,i+1,vo[i].getLine2()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine3()!=null){
    			lines = new  Label(2,i+1,vo[i].getLine3());
    			sheet.addCell(lines); 
    		}
    			 
    		if(vo[i].getLine4()!=null){
    			lines = new  Label(3,i+1,vo[i].getLine4()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine5()!=null){
    			lines = new  Label(4,i+1,vo[i].getLine5()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine6()!=null){
    			lines = new  Label(5,i+1,vo[i].getLine6()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine7()!=null){
    			lines = new  Label(6,i+1,vo[i].getLine7()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine8()!=null){
    			lines = new  Label(7,i+1,vo[i].getLine8()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine9()!=null){
    			lines = new  Label(8,i+1,vo[i].getLine9());
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine10()!=null){
    			lines = new  Label(9,i+1,vo[i].getLine10()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine11()!=null){
    			lines = new  Label(10,i+1,vo[i].getLine11()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine12()!=null){
    			lines = new  Label(11,i+1,vo[i].getLine12()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine13()!=null){
    			lines = new  Label(12,i+1,vo[i].getLine13()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine14()!=null){
    			lines = new  Label(13,i+1,vo[i].getLine14()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine15()!=null){
    			lines = new  Label(14,i+1,vo[i].getLine15()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine16()!=null){
    			lines = new  Label(15,i+1,vo[i].getLine16());
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine17()!=null){
    			lines = new  Label(16,i+1,vo[i].getLine17()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine18()!=null){
    			lines = new  Label(17,i+1,vo[i].getLine18()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine19()!=null){
    			lines = new  Label(18,i+1,vo[i].getLine19()); 
    			sheet.addCell(lines); 
    		}
    			
    		if(vo[i].getLine20()!=null){
    			lines = new  Label(19,i+1,vo[i].getLine20()); 
    			sheet.addCell(lines); 
    		}*/
    	}
    }
    private void close() throws IOException, WriteException{
    	book.write();
		book.close();
    }
}

