package nc.bs.dip.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.dip.in.AbstractDataProcessor;
import nc.bs.dip.in.ValueTranslator;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.in.PubDataVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.AggregatedValueObject;

public class TXTProcessor extends AbstractDataProcessor{
		File[] files;

		/**
		 * @desc 
		 * @param File[] file 文件路径
		 * @param AggregatedValueObject datarcv 数据定义的聚合vo
		 * @param Hashtable<String, DipDatadefinitBVO> datadef <就是表体的enamel，表体vo>，对于主键加一条<"#PKFIELD#",vo>
		 * @param String[] checkers 校验类数据，可以传空，后边有做判断
		 * */
		public TXTProcessor(File[] file, AggregatedValueObject datarcv, Hashtable<String, DipDatadefinitBVO> datadef, String[] checkers) {
			super(file, datarcv, datadef, checkers);
			files = file;
		}

		@Override
		public void execute() {
			int mapType = (Integer)getRcvConfig().getParentVO().getAttributeValue("datamaptype");//对照方式
			HashMap mapVOs = (HashMap)getRcvConfig().getParentVO().getAttributeValue("map");//对照表
			String table = (String)getRcvConfig().getParentVO().getAttributeValue("tablename");//存储表
			String pkfield = (String)getFieldKey().get("#PKFIELD#").getEname();
			
			for(int i=0;i<files.length;i++){
					TxtDataVO txtdvo=null;
					try {
						txtdvo = read(files[i]);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(txtdvo==null){
						continue;
					}

					RowDataVO[] rowVOs = new RowDataVO[txtdvo.getDataSize()];
					for(int j=0;j<rowVOs.length;j++){
						rowVOs[j] = new RowDataVO();
						rowVOs[j].setTableName(table);
						rowVOs[j].setPKField(pkfield);
						
						String[] fields = txtdvo.getFieldName();//.getRowData(i).getAttributeNames();
						
						for(int k=0;k<fields.length;k++){
							String colno = fields[k];
							String key = mapType == PubDataVO.FIELD_MATCH_KEY ? mapVOs.get(colno).toString() : colno;
							if(mapType==0){
								Object value = txtdvo.getRowData(j).getAttributeValue(txtdvo.getTitlemap().get(colno));
								String filedi=mapVOs.get(k+1+"").toString();
								String type= getFieldKey().get(filedi).getType();
								Integer len=getFieldKey().get(filedi).getLength();
								if(len!=null){
									value = ValueTranslator.translate((String)value, type, len);
								}
								rowVOs[j].setAttributeValue(filedi, value);
							}else{
								Object value = txtdvo.getRowData(j).getAttributeValue(txtdvo.getTitlemap().get(colno));
								String type= getFieldKey().get(key).getType();
								Integer len=getFieldKey().get(key).getLength();
								if(len!=null){
									value = ValueTranslator.translate((String)value,type, len);
								}
								rowVOs[j].setAttributeValue(key, value);
							}
						}
						
					}
					rowVOs = checkData(rowVOs);
					try {
						savData(rowVOs);
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		public TxtDataVO read(File file) throws FileNotFoundException, IOException{
			List<String> list=new ArrayList<String>();
			FileReader fr = null ;
			BufferedReader br = null;
			try{
				fr = new FileReader(file.getPath()); 
				br = new BufferedReader(fr);
				String line="";
				while ((line = br.readLine()) != null){
					list.add(line);
				}
			
			}finally{
				if(br!=null){
					br.close();
				}
				if(fr!=null){
					fr.close();
				}
			}
			if(list==null||list.size()<=1){
				return null;
			}
			
			TxtDataVO dataVo = new TxtDataVO();
			dataVo.setStartCol(0);
			dataVo.setRowCount(list.size()-1);//去掉标题行
			String[] title=list.get(0).split(",");
			dataVo.setColCount(title.length);
			dataVo.setFirstDataRow(1);
			dataVo.setFirstDataCol(0);
			
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for(short i=0;i<title.length;i++){
				titlemap.put(title[i], Short.toString(i));
			}
			dataVo.setTitlemap(titlemap);
			dataVo.setFieldName(title);
			for(int i=1;i<list.size();i++){
				String[] val=list.get(i).split(",");
				for(short j=0;j<val.length;j++){
					dataVo.setCellData(i-1, j, val[j]);
				}
			}

			return dataVo;
		}

		@Override
		public RowDataVO[] rexecute() throws FileNotFoundException, IOException  {

			int mapType = (Integer)getRcvConfig().getParentVO().getAttributeValue("datamaptype");//对照方式
			HashMap mapVOs = (HashMap)getRcvConfig().getParentVO().getAttributeValue("map");//对照表
			String table = (String)getRcvConfig().getParentVO().getAttributeValue("tablename");//存储表
			String pkfield = (String)getFieldKey().get("#PKFIELD#").getEname();
			
			TxtDataVO txtdvo= read(files[0]);

			RowDataVO[] rowVOs = new RowDataVO[txtdvo.getDataSize()];
			for(int j=0;j<rowVOs.length;j++){
				rowVOs[j] = new RowDataVO();
				rowVOs[j].setTableName(table);
				rowVOs[j].setPKField(pkfield);
				String[] fields = txtdvo.getFieldName();
				for(int k=0;k<fields.length;k++){
					String colno = fields[k];
					String key = mapType == PubDataVO.FIELD_MATCH_KEY ? mapVOs.get(colno).toString() : colno;
					Object value = txtdvo.getRowData(j).getAttributeValue(txtdvo.getTitlemap().get(colno));
					String filedi=mapType==0?mapVOs.get(k+1+"").toString():key;
					String type= getFieldKey().get(filedi).getType();
					Integer len=getFieldKey().get(filedi).getLength();
					if(len!=null){
						value = ValueTranslator.translate((String)value, type, len);
					}
					rowVOs[j].setAttributeValue(filedi, value);
				}
				
			}
			return rowVOs;
		
		}
	}
