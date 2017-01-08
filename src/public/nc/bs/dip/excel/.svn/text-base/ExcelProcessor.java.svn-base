package nc.bs.dip.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import nc.bs.dao.DAOException;
import nc.bs.dip.in.AbstractDataProcessor;
import nc.bs.dip.in.ValueTranslator;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.excel.ExcelDataVO;
import nc.vo.dip.in.PubDataVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.AggregatedValueObject;

public class ExcelProcessor extends AbstractDataProcessor{
	
	File[] file;

	/**
	 * @desc 
	 * @param File[] file 文件路径
	 * @param AggregatedValueObject datarcv 数据定义的聚合vo
	 * @param Hashtable<String, DipDatadefinitBVO> datadef <就是表体的enamel，表体vo>，对于主键加一条<"#PKFIELD#",vo>
	 * @param String[] checkers 校验类数据，可以传空，后边有做判断
	 * */
	public ExcelProcessor(File[] files, AggregatedValueObject datarcv, Hashtable<String, DipDatadefinitBVO> datadef, String[] checkers) {
		super(files, datarcv, datadef, checkers);
		file = files;
	}

	public void execute(){
		int mapType = (Integer)getRcvConfig().getParentVO().getAttributeValue("datamaptype");//对照方式
		HashMap mapVOs = (HashMap)getRcvConfig().getParentVO().getAttributeValue("map");//对照表
		String table = (String)getRcvConfig().getParentVO().getAttributeValue("tablename");//存储表
		String pkfield = (String)getFieldKey().get("#PKFIELD#").getEname();
		
		try {			
			for (int f = 0; f < file.length; f++) {
				File xls = file[f];
				ExcelDataVO data = new ExcelReader().read(xls);
				RowDataVO[] rowVOs = new RowDataVO[data.getDataSize()];
				for (int i = 0; i < data.getDataSize(); i++) {
					rowVOs[i] = new RowDataVO();
					rowVOs[i].setTableName(table);
					rowVOs[i].setPKField(pkfield);
					
					String[] fields = data.getRowData(i).getAttributeNames();

					for (short j = 0; j < fields.length; j++) {
						String colno = fields[j];
						String key = mapType == PubDataVO.FIELD_MATCH_KEY ? data.getTitlemap().get(colno) : colno;
						if(mapType==0){
							if (mapVOs.containsKey(key)) { //对照表里包含
								Object value = data.getRowData(i).getAttributeValue(colno);
								String field=data.getTitlemap().get(colno);
								Integer len= getFieldKey().get(field).getLength();
								String type= getFieldKey().get(field).getType();
								if(!(type.toLowerCase().equals("integer"))){
									value = ValueTranslator.translate((String)value,type,len);
								}
								rowVOs[i].setAttributeValue((String) mapVOs.get(key), value);
							}else if (!mapVOs.containsKey(key) && getFieldKey().containsKey(data.getTitlemap().get(colno))){
								Object value = data.getRowData(i).getAttributeValue(colno);
								String type= getFieldKey().get(data.getTitlemap().get(colno)).getType();
								Integer len=getFieldKey().get(data.getTitlemap().get(colno)).getLength();
								if(!(type.toLowerCase().equals("integer"))){
									value = ValueTranslator.translate((String)value,type, len);
								}
								rowVOs[i].setAttributeValue(data.getTitlemap().get(colno), value);
							}
						}else{
							//key是第几列是什么excel字段
							if (mapVOs.containsKey(key)) { //对照表里包含
								Object value = data.getRowData(i).getAttributeValue(colno);
								String field=mapVOs.get(key).toString();
								Integer len= getFieldKey().get(field).getLength();
								String type= getFieldKey().get(field).getType();
								if(!(type.toLowerCase().equals("integer"))){
									value = ValueTranslator.translate((String)value,type,len);
								}
								rowVOs[i].setAttributeValue((String) mapVOs.get(key), value);
							}/*else if (!mapVOs.containsKey(key) && getFieldKey().containsKey(data.getTitlemap().get(colno))){
								Object value = data.getRowData(i).getAttributeValue(colno);
								String type= getFieldKey().get(data.getTitlemap().get(colno)).getType();
								Integer len=getFieldKey().get(data.getTitlemap().get(colno)).getLength();
								if(!(type.toLowerCase().equals("integer"))){
									value = ValueTranslator.translate((String)value,type, len);
								}
								rowVOs[i].setAttributeValue(data.getTitlemap().get(colno), value);
							}*/
						}
					}
				}
				
				rowVOs = checkData(rowVOs);
				savData(rowVOs);
			}			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//保存数据库失败
		}
	}

	@Override
	public RowDataVO[] rexecute() throws FileNotFoundException, IOException  {

		int mapType = (Integer)getRcvConfig().getParentVO().getAttributeValue("datamaptype");//对照方式
		HashMap mapVOs = (HashMap)getRcvConfig().getParentVO().getAttributeValue("map");//对照表
		String table = (String)getRcvConfig().getParentVO().getAttributeValue("tablename");//存储表
		String pkfield = (String)getFieldKey().get("#PKFIELD#").getEname();
		
		File xls = file[0];
		ExcelDataVO data = new ExcelReader().read(xls);
		RowDataVO[] rowVOs = new RowDataVO[data.getDataSize()];
		for (int i = 0; i < data.getDataSize(); i++) {
			rowVOs[i] = new RowDataVO();
			rowVOs[i].setTableName(table);
			rowVOs[i].setPKField(pkfield);
			
			String[] fields = data.getRowData(i).getAttributeNames();

			for (short j = 0; j < fields.length; j++) {
				String colno = fields[j];
				String key = mapType == PubDataVO.FIELD_MATCH_KEY ? data.getTitlemap().get(colno) : colno;
				Object value = data.getRowData(i).getAttributeValue(colno);
				String field=mapType==0?data.getTitlemap().get(colno):mapVOs.get(key).toString();
				Integer len= getFieldKey().get(field).getLength();
				String type= getFieldKey().get(field).getType();
				if(!(type.toLowerCase().equals("integer"))){
					value = ValueTranslator.translate((String)value,type,len);
				}
				rowVOs[i].setAttributeValue((String) mapVOs.get(key), value);
			}
		}
			
			return rowVOs;
			
	
	}
}
