package nc.bs.dip.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

import nc.bs.dip.voucher.DataChangeProcessor;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.voucher.FreeValueVO;
import nc.vo.dip.voucher.VoucherDetailVO;
import nc.vo.dip.voucher.VoucherHVO;
import nc.vo.dip.voucher.VoucherVO;
import nc.vo.pub.BusinessException;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

public class XMLWriter {
	int numberCount=1;
	String filedir;
	public XMLWriter(String outPath){
		filedir = outPath;

//		filedir = filedir.replaceAll("\\", "/");
		filedir = (filedir.endsWith("/")?filedir:filedir+"/");
		File dir = new File(filedir);
		dir.mkdir();
		dir.exists();
	}

	public RetMessage write(VoucherVO[] vos,String sender,String str,int k) throws IOException{
		
		boolean isStart=false;//是否启用了精度控制。
		int count=0;//转换后输出精度是几位。
		if(str!=null&&!"".equals(str)){
			if("Y".equals(str)){
				isStart=true;
				count=k;
			}
		}
		if(vos == null || vos.length == 0 || vos[0] == null){
			return new RetMessage(true,null);
		}
		int su=0;
		int fa=0;
	
		Document doc = DocumentFactory.getInstance().createDocument();
		Element itf = null;

		for(VoucherVO vo: vos){
			if(vo == null){
				continue;
			}

			if(itf == null){
				itf = doc.addElement("ufinterface");
				createUFITF(vo, itf,sender);
			}
			VoucherHVO hvo = vo.getParent();
			Element voucher = itf.addElement("voucher");
			String id=vo.getParent().getCompany()+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())+new DecimalFormat("00000").format(numberCount++);
			voucher.addAttribute("id", id);

			Element head = voucher.addElement("voucher_head");
			String[] field = hvo.getAttributeNames();
			for(int i=0;i<field.length;i++){
				Object value = hvo.getAttributeValue(field[i]);
				if(value == null){
					value = "";
				}
//				//liyunzhe 修改 ，voucher_id为固定值 0
//				if("voucher_id".equals(field[i])){
//					head.addElement(field[i]).setText(0+"");
//				}else if("posting_date".equals(field[i])){
//					head.addElement(field[i]).setText(head.elementText("prepareddate"));
//				}else{
					head.addElement(field[i]).setText(value.toString());	
//				}
				
			}

			Element body = voucher.addElement("voucher_body");
			VoucherDetailVO[] bvos = vo.getChildren();
			for(VoucherDetailVO bvo: bvos){
				Element entry = body.addElement("entry");

				field = bvo.getAttributeNames();
				for(int i=0;i<field.length;i++){
					if(!field[i].equals("freevalue")){
						Object value = bvo.getAttributeValue(field[i]);
						if(value == null){
							value = "";
						}
						if(!field[i].equals("abstract_m")){
							if(isStart&&("primary_debit_amount".equals(field[i])||"natural_debit_currency".equals(field[i])||"primary_credit_amount".equals(field[i])||"natural_credit_currency".equals(field[i])||"secondary_debit_amount".equals(field[i])||"secondary_credit_amount".equals(field[i]))){
								value=round(value.toString(), count);
							}
								entry.addElement(field[i]).setText(value!=null?value.toString().trim():"");
							
							
						}else{
							entry.addElement("abstract").setText(value!=null?value.toString().trim():"");
						}
					}else{
						Element ass = entry.addElement("auxiliary_accounting");
						FreeValueVO[] free = bvo.getFreevalue();
						for(int j=0;j<free.length;j++){
							Element item = ass.addElement("item");
							item.addAttribute("name", free[j].getAssType());
							item.setText(free[j].getAssValue()!=null?free[j].getAssValue().trim():"");
						}
					}
				}
			}
			String filePath = "";
			if(vos[0].isCheckpass()){
				filePath = filedir + "send/" + vos[0].getParent().getCompany() + "/" + id + ".xml";
				File myfile = new File(filedir + "send/" + vos[0].getParent().getCompany() + "");
				if(!myfile.exists()) {
					myfile.mkdirs();
				}
			}else{
				filePath = filedir + "error/" + vos[0].getParent().getCompany() + "/" + id + ".xml";
				File myfile = new File(filedir + "error/" + vos[0].getParent().getCompany() + "");
				if(!myfile.exists()) {
					myfile.mkdirs();
				}
			}
			org.dom4j.io.XMLWriter out =null;
			FileOutputStream ots=null;
			try{
				new File(filePath).createNewFile();
				OutputFormat format = OutputFormat.createPrettyPrint();
				//format.set
				ots=new FileOutputStream(filePath);
				out = new org.dom4j.io.XMLWriter(ots,format);
				SJUtil.PrintLog("d:/aa/a", "","  准备写xml"+"  "+doc);
				out.write(doc);
				SJUtil.PrintLog("d:/aa/a", "","  完成写xml"+"  "+doc);
				SJUtil.PrintLog("d:/aa/a", "","  完成写xml"+" filepath :"+filePath);
				if(vos[0].isCheckpass()){
					su++;
				}else{
					fa++;
				}
				
				/*if(vos[0].isCheckpass()){
					SJUtil.PrintLog("d:/aa/a", "","  转换xml成功个数  ++前 ischeckpass");
					SJUtil.PrintLog("d:/aa/a", "","  转换xml成功个数  ++前"+DataChangeProcessor.success+"");
					DataChangeProcessor.success++;
					SJUtil.PrintLog("d:/aa/a", "","  转换xml成功个数  ++后"+DataChangeProcessor.success+"");
				}else {
					SJUtil.PrintLog("d:/aa/a", "","  转换xml失败个数  ++前 ……ischeckpass");
					SJUtil.PrintLog("d:/aa/a", "","  转换xml失败个数  ++前"+DataChangeProcessor.fail+"");
					DataChangeProcessor.fail++;
					SJUtil.PrintLog("d:/aa/a", "","  转换xml失败个数  ++后"+DataChangeProcessor.fail+"");
				}*/
				SJUtil.PrintLog("d:/aa/a", ""," 写XML结束 ");
			}catch (IOException e) {
				e.printStackTrace();
				SJUtil.PrintLog("d:/aa/a", "","  写xml出错 io异常"+"  "+e.getMessage()+" ");
				throw new IOException("未能在指定路径下正确生成文件：【文件路径】"+filePath);
			}finally{
				if(ots!=null){
					ots.close();
					SJUtil.PrintLog("d:/aa/a", ""," 关ots ");
				}
				if(out!=null){
					out.close();
					SJUtil.PrintLog("d:/aa/a", ""," 关out ");
				}
			}
			
		}
		RetMessage ret=new RetMessage(true,"成功");
		ret.setFa(fa);
		ret.setSu(su);
		return ret;

	}

	private void createUFITF(VoucherVO vo, Element e,String sender) {
		e.addAttribute("proc", "add");
		e.addAttribute("sender", sender+"");
		//roottag="voucher" billtype="billtype" replace="Y" receiver="0101@0101-0002" sender="1003" isexchange="Y" filename="凭证样本数据文件.xml" proc="add" operation="req"
		e.addAttribute("roottag", "voucher");
		e.addAttribute("billtype", "gl");
		e.addAttribute("replace", "Y");
		e.addAttribute("isexchange", "Y");
//		String book="";
//		try {
//			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
////			String sql="select unitcode from bd_corp where pk_corp='"+vo.getParent().getCompany()+"'";
////			String unitname=iqf.queryfield(sql);
//			String sql=" select glorgbookcode from bd_glorgbook where  pk_glbook='"+vo.getGlorgbookcode()+"' and glorgbookcode like '"+vo.getParent().getCompany()+"%'";
//			book=iqf.queryfield(sql);
//		} catch (BusinessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (DbException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		//liyunzhe 更改
		e.addAttribute("receiver", vo.getParent().getCompany()/*+"@"+vo.getGlorgbookcode()*/);
	}
	
	/**
	 * 四舍五入方法。按照指定的小数位数去四舍五入。
	 * @param value
	 * @param count
	 * @return
	 */
	private String round(String value, int count){
		if(count<=0||count>8){
			return value;
		}
		BigDecimal b = new BigDecimal(value);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, count, BigDecimal.ROUND_HALF_UP).toString();	
	}
}
