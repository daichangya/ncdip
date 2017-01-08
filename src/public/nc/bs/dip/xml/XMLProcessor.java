package nc.bs.dip.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;

import nc.bs.dao.DAOException;
import nc.bs.dip.in.AbstractDataProcessor;
import nc.bs.dip.in.ValueTranslator;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.xml.XMLDataVO;
import nc.vo.pub.AggregatedValueObject;

import org.dom4j.DocumentException;

public class XMLProcessor extends AbstractDataProcessor{
	File[] files;

	/**
	 * @desc 
	 * @param File[] file 文件路径
	 * @param AggregatedValueObject datarcv 数据接收定义的聚合vo
	 * @param Hashtable<String, DipDatadefinitBVO> datadef <就是表体的enamel，表体vo>，对于主键加一条<"#PKFIELD#",vo>
	 * @param String[] checkers 校验类数据，可以传空，后边有做判断
	 * */
	public XMLProcessor(File[] file, AggregatedValueObject datarcv, Hashtable<String, DipDatadefinitBVO> datadef, String[] checkers) {
		super(file, datarcv, datadef, checkers);
		files = file;
	}

	@Override
	public void execute() {
		int mapType = (Integer)getRcvConfig().getParentVO().getAttributeValue("datamaptype");//对照方式
		HashMap mapVOs = (HashMap)getRcvConfig().getParentVO().getAttributeValue("map");//对照表
		String table = (String)getRcvConfig().getParentVO().getAttributeValue("tablename");//存储表
		String pkfield = (String)getFieldKey().get("#PKFIELD#").getEname();
		
		XMLReader reader = new XMLReader();
		for(int i=0;i<files.length;i++){
			try {
				XMLDataVO data = reader.read(files[i]);
				RowDataVO[] rowVOs = new RowDataVO[data.getDataSize()];
				for(int j=0;j<data.getDataSize();j++){
					rowVOs[j] = new RowDataVO();
					rowVOs[j].setTableName(table);
					rowVOs[j].setPKField(pkfield);
					
					String[] field = data.getFieldKeys();//xml默认存储ename
					
					for(int k=0;k<field.length;k++){
//						String key = mapType == PubDataVO.FIELD_MATCH_KEY?field[k]:Integer.toString(data.getKeyIndex(field[k]));
						Object value = data.getRowData(j).getAttributeValue(field[k]); 
						String fieldi=mapType==0?mapVOs.get(k+1+"").toString():mapVOs.get(field[k]).toString();
						String type=getFieldKey().get(fieldi).getType();
						Integer len=getFieldKey().get(fieldi).getLength();
						if(len!=null){
							value = ValueTranslator.translate((String)value, type, len);
						}
						if(type.toLowerCase().equals("integer")){
							value=Integer.parseInt(value.toString());
						}
						rowVOs[j].setAttributeValue(fieldi, value);
					}
				}
				rowVOs = checkData(rowVOs);
				savData(rowVOs);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public RowDataVO[] rexecute() throws DocumentException {

		int mapType = (Integer)getRcvConfig().getParentVO().getAttributeValue("datamaptype");//对照方式
		HashMap mapVOs = (HashMap)getRcvConfig().getParentVO().getAttributeValue("map");//对照表
		String table = (String)getRcvConfig().getParentVO().getAttributeValue("tablename");//存储表
		String pkfield = (String)getFieldKey().get("#PKFIELD#").getEname();
		
		XMLReader reader = new XMLReader();
		XMLDataVO data = reader.read(files[0]);
		RowDataVO[] rowVOs = new RowDataVO[data.getDataSize()];
		for(int j=0;j<data.getDataSize();j++){
			rowVOs[j] = new RowDataVO();
			rowVOs[j].setTableName(table);
			rowVOs[j].setPKField(pkfield);
			
			String[] field = data.getFieldKeys();//xml默认存储ename
			
			for(int k=0;k<field.length;k++){
				Object value = data.getRowData(j).getAttributeValue(field[k]); 
				String fieldi=mapType==0?mapVOs.get(k+1+"").toString():mapVOs.get(field[k]).toString();
				String type=getFieldKey().get(fieldi).getType();
				Integer len=getFieldKey().get(fieldi).getLength();
				if(len!=null){
					value = ValueTranslator.translate((String)value, type, len);
				}
				if(type.toLowerCase().equals("integer")){
					value=Integer.parseInt(value.toString());
				}
				rowVOs[j].setAttributeValue(fieldi, value);
			}
		}
		return rowVOs;
	
	}

}
