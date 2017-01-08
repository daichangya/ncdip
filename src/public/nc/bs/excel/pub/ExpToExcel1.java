package nc.bs.excel.pub;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import nc.ui.pub.beans.MessageDialog;
import nc.vo.dip.in.RowDataVO;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
	public class ExpToExcel1 
	{
		private WritableWorkbook   book = null; 
		private WritableSheet   sheet = null;
		private String path = "";
		private String name = "";
		private String[] sheetName=null;
		private String names;
		private String arrayfilenames[];
		private String nameTemp = "";
		private RowDataVO [] vos = null;
		private List<RowDataVO []> vosList=null;
		private int fieldNO=0;
	    public ExpToExcel1(String filePath,String[] sheetName,String[] fileNames,List<RowDataVO []> list,String fileTemp) throws IOException{
//	    	super();
	    	path = filePath;
	    	this.sheetName = sheetName;
	    	arrayfilenames = fileNames;
	    	nameTemp = fileTemp;
//	    	vos = vo;
	    	vosList=list;
	    	
			
		}
	    public boolean create(){
	        /**********�ж��ļ��Ƿ����********/
	    	if(!(sheetName!=null&&vosList!=null&&arrayfilenames!=null&&sheetName.length==vosList.size()&&sheetName.length==arrayfilenames.length)){
	    		MessageDialog.showErrorDlg(null, "����", "��֯�������ݴ��󣬲�������");
	    		return false;
	    	}
	    	boolean flag=false;
	        try {
	            File file = new File(path);
	            
//	            /**********�ж��ļ��Ƿ����********/
//	            if(file.exists()){
//	                File filetemp = new File(nameTemp);//��ʱ�ļ�
//	                Workbook wb = Workbook.getWorkbook(file);
//	                book = Workbook.createWorkbook(filetemp, wb);//��wb�����ռ���������
//	                int wb_sheet_length = wb.getSheets().length;
//	                /**********���new sheet***/
//	                sheet = book.createSheet(name, wb_sheet_length+1);//sheet���ֲ������Ѿ����ڵ���ͬ����������
//	                /**************д��������*******/
//	                createToExcel(names,vos);
//	                book.write();
//	                book.close();
//	                wb.close();
//	                /**********ɾ��file ���޸�file����********/
//	                file.delete();
//	                filetemp.renameTo(file);//�ļ���������
//	                
//	            }else{
	               
					try {
						book = Workbook.createWorkbook(file);
					} catch (Exception e) {
						MessageDialog.showErrorDlg(null, "����", e.getMessage());
						// TODO: handle exception
					}	                
					/** **********����������************ */
	                for(int i=0;i<sheetName.length;i++){
	                	name=sheetName[i];
	                	vos=vosList.get(i);
	                	names=arrayfilenames[i];
	                	if(name!=null){
	                		sheet = book.createSheet(name, i);	
	                	}
	                	if(vos!=null&&names!=null){
	                		/**************д������*******/
			                createToExcel(names,vos);	
	                	}
		                	
	                }
	                
	                book.write();
	                book.close();
	               flag=true;
//	            }
	        } catch (Exception e){
	            e.printStackTrace();
	            flag=false;
	        }
	        return flag;
	    }
	    private void createToExcel(String fileNames,RowDataVO [] vo)throws IOException{
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
	    private void excelLine(RowDataVO [] vo) throws RowsExceededException, WriteException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
	    	if(vo == null || vo.length == 0)
	    		return ;
	    	Label lines = null;
	    	for(int i = 0; i<vo.length;i++){
	    		for(int j=1;j<=fieldNO;j++){
	    			Object ob=vo[i].getAttributeValue((j-1)+"");
	    			if(ob!=null){
	    				lines = new  Label(j-1,i+1,ob.toString()); 
	    				sheet.addCell(lines); 
	    			}
	    		}
	    	}
	    }
	    private void close() throws IOException, WriteException{
	    	book.write();
			book.close();
	    }
	}



