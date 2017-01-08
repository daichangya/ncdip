package nc.impl.dip.pub;

import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.dip.pub.IServiceInputDataRec;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.webservice.dip.pub.DipWebserviceUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class ServiceInputDataRec implements IServiceInputDataRec {
	/**
	 * @desc ���ݲ���prop��ȡwebservice������
	 * ���ص��ַ����ĸ�ʽ��������ע�͵���
	<?xml version="1.0" encoding="UTF-8"?>
	<root>
		<head>
			<retcode>0:�ɹ���1:ʧ��</retcode>
			<retmsg>������Ϣ</retmsg>
			<retcount>������Ŀ��</retcount>
			<rettag>���صı�ǩ</rettag>
		</head>
		<body>
			<data>d1,d2,d3</data>
			<data>d1,d2,d3</data>
			<data>d1,d2,d3</data>
		</body>
	</root>
//	*@param propΪһ���ַ������м��Զ��Ÿ����������������Ϊ4����ô����ϵͳ��վ�㣬ҵ��Ψһ�����������������Ϊ6����ô����ϵͳվ�㣬ҵ��Ψһ������ȡ���������ڼ���ȡ��
//	 * */
//	public String callData1111(String prop) {
//		Document rootDoc = DocumentHelper.createDocument();
//		Element root=rootDoc.addElement("root");
//		Element head=root.addElement("head");
//		if(prop==null||!(prop.split(",").length==4||prop.split(",").length==6)){
//			head.addElement("retcode").setText("1");
//			head.addElement("retmsg").setText("������������ȷ");
//			return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//		}
//		Integer countrow=-1;
//		Integer rownum=-1;
//		String proptemp="";
//		if(prop.split(",").length==6){
//			String[] props=prop.split(",");
//			countrow=Integer.parseInt(props[4]);
//			rownum=Integer.parseInt(props[5]);
//			for(int i=0;i<4;i++){
//				proptemp=proptemp+props[i]+",";
//			}
//			if(proptemp.length()>0){
//				proptemp=proptemp.substring(0,proptemp.length()-1);
//			}
//		}
//		List<DipDatarecHVO> hvo=null;
//		try {
//			hvo=(List<DipDatarecHVO>) getBaseDAO().retrieveByClause(DipDatarecHVO.class, "ioflag='���' and sourcetype='0001AA1000000003493X' and (sourceparam='"+prop+"' or sourceparam like '"+proptemp+"%')");
//		} catch (DAOException e) {
//			e.printStackTrace();
//		}
//		if(hvo==null||hvo.size()<1){
//			head.addElement("retcode").setText("1");
//			head.addElement("retmsg").setText("û���ҵ���Ӧ�����ͬ������");
//			return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//		}
//
//		DipDatarecHVO vo = hvo.get(0);
//			// 0��˳��1���ֶζ���
//			// �����ֶ�
//				
//				String phyname = vo.getDataname();// �������
//				// ������Դ����(dataSourse)
//				String sourceurl = vo.getSourcecon();
//				//�ļ���������
//				
//				// �洢����
//				String memorytable = vo.getMemorytable();
//				DataformatdefHVO fhvo = null;
//				List<DataformatdefHVO> listfhvo = null;
//				try {
//					listfhvo = (List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and nvl(dr,0)=0");
//				} catch (DAOException e4) {
//					e4.printStackTrace();
//				}
//				if(listfhvo==null||listfhvo.size()<1){
//					head.addElement("retcode").setText("1");
//					head.addElement("retmsg").setText("û���ҵ���Ӧ�ĸ�ʽ����Ķ����ʽ");
//					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//				}
//				DipSysregisterHVO xt = null;
//				try {
//					xt = (DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, vo.getPk_xt());
//				} catch (DAOException e3) {
//					e3.printStackTrace();
//				}
//				if(xt==null){
//					head.addElement("retcode").setText("1");
//					head.addElement("retmsg").setText("û���ҵ���Ӧ��ϵͳ");
//					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//				}
//				DipDatadefinitHVO ddfhvo = null;
//				try {
//					ddfhvo = (DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, memorytable);
//				} catch (DAOException e2) {
//					e2.printStackTrace();
//				}
//				if(ddfhvo==null){
//					head.addElement("retcode").setText("1");
//					head.addElement("retmsg").setText("û���ҵ���Ӧ�����ݶ���");
//					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//				}
//				String xtbz=xt.getExtcode();//ϵͳ��־
//				String zdzj=ddfhvo.getDeploycode();
//				String zdbz="";//վ���־
//				if(zdzj==null||zdzj.length()<=0){
//					zdbz=xt.getDef_str_1();
//				}else{
//					DipSysregisterBVO sbvo = null;
//					try {
//						sbvo = (DipSysregisterBVO) getBaseDAO().retrieveByPK(DipSysregisterBVO.class, zdzj);
//					} catch (DAOException e) {
//						e.printStackTrace();
//					}
//					if(sbvo==null){
//						head.addElement("retcode").setText("1");
//						head.addElement("retmsg").setText("û���ҵ���Ӧ��վ��");
//						return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//					}
//					zdbz=sbvo.getSitecode();
//				}
//				String ywbz=ddfhvo.getBusicode();//ҵ���־
//				List<String> ziduan=null;
//				StringBuffer sql=new StringBuffer();
//				List<DataformatdefBVO> listbvos=null;
//				fhvo=listfhvo.get(0);
//				try {
//					listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+fhvo.getPrimaryKey()+"' order by code");
//				} catch (DAOException e1) {
//					e1.printStackTrace();
//				}
//				if(listbvos==null||listbvos.size()<=0){
//					head.addElement("retcode").setText("1");
//					head.addElement("retmsg").setText("û���ҵ���Ӧ�ĸ�ʽ����ĸ�ʽ");
//					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//				}
//				int i=0;
//				ziduan=new ArrayList<String>();
//				Map<String,DataformatdefBVO> bmap=new HashMap<String, DataformatdefBVO>();
//				for(DataformatdefBVO bvoi:listbvos){
//					if(bvoi.getVdef2().equals("ҵ������")){
//						ziduan.add(bvoi.getDatakind().toUpperCase()+i);
//						sql.append(bvoi.getDatakind().toUpperCase()+" "+bvoi.getDatakind().toUpperCase()+i+",");
//					}else if(bvoi.getVdef2().equals("�Զ���")||bvoi.getVdef2().equals("��־ͷ")||bvoi.getVdef2().equals("��־β")){
//						ziduan.add((bvoi.getDatakind()==null?"kg":bvoi.getDatakind()).toUpperCase()+i);
//						sql.append("'"+(bvoi.getDatakind()==null?"":bvoi.getDatakind()).toUpperCase()+"' "+(bvoi.getDatakind()==null?"kg":bvoi.getDatakind()).toUpperCase()+i+",");
//					}else if(bvoi.getVdef2().equals("�̶���־")){
//						if(bvoi.getVdef3().equals("ϵͳ��־")){
//							ziduan.add("xtbz_".toUpperCase()+i);
//							sql.append("'"+xtbz+"' "+"xtbz_"+i+",");
//						}else if(bvoi.getVdef3().equals("վ���־")){
//							ziduan.add("zdbz_".toUpperCase()+i);
//							sql.append("'"+zdbz+"' "+"zdbz_"+i+",");
//						}else if(bvoi.getVdef3().equals("ҵ���־")){
//							ziduan.add("ywbz_".toUpperCase()+i);
//							sql.append("'"+ywbz+"' "+"ywbz_"+i+",");
//						}
//					}else if(bvoi.getVdef2().equals("ʱ�亯��")){
//						ziduan.add("sjhs_".toUpperCase()+i);
//						sql.append("'"+bvoi.getDatakind()+"' "+"sjhs_"+i+",");
//					}else if(bvoi.getVdef2().equals("��¼������")){
//						ziduan.add("jlshs_".toUpperCase()+i);
//						sql.append("'"+bvoi.getDatakind()+"' "+"jlshs_"+i+",");
//					}else if(bvoi.getVdef2().equals("��������")){
//						ziduan.add("zjhs_".toUpperCase()+i);
//						sql.append("'"+bvoi.getDatakind()+"' "+"zjhs_"+i+",");
//					}
//					bmap.put(ziduan.get(i), (DataformatdefBVO) bvoi.clone());
//					i++;
//				}
//				String count="";
//				try {
//					count=iqf.queryfield("select count(*) from "+phyname);
//				} catch (BusinessException e1) {
//					e1.printStackTrace();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				} catch (DbException e1) {
//					e1.printStackTrace();
//				}
//				String querysql="";
//				if(countrow==-1){
//					querysql="select "+sql.substring(0,sql.length()-1)+" from "+phyname;
//				}else{
//					String sqltemp=sql.substring(0,sql.length()-1);
//					String[] sqls=sqltemp.split(",");
//					String sqlt="";
//					for(int t=0;t<sqls.length;t++){
//						sqlt=sqlt+(sqls[t].indexOf(" ")>0?sqls[t].split(" ")[1]:sqls[t])+",";
//					}
//					querysql="select "+sqlt.substring(0,sqlt.length()-1) +" from ( "
//                    +	"select "+sql+"rownum rwn  from "+phyname+" where rownum<="+((rownum+1)*countrow)
//                    +" )  where rwn>"+rownum*countrow;
//				}
//				List<HashMap> list=new ArrayList();
//				try{
//					list = iqf.queryListMapSingleSql(querysql);
//				}catch(Exception e){
//					head.addElement("retcode").setText("1");
//					head.addElement("retmsg").setText("��ѯ���ݳ���"+e.getMessage());
//					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//				}
//				if(list!=null&&list.size()>0){
//					rootDoc= writetoXmlFile(list,ziduan,bmap,fhvo,Integer.parseInt(count==null||count.length()<=0?"0":count));
//				}else{
//					head.addElement("retcode").setText("0");
//					head.addElement("retmsg").setText("û�в鵽����");
//					head.addElement("retcount").setText("0");
//					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//				}
//		return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
//	}
	/**
	 * <?xml version="1.0" encoding="UTF-8"?>
	<root>
		<head>
			<retcode>0:�ɹ���1:ʧ��</retcode>
			<retmsg>������Ϣ</retmsg>
			<retcount>������Ŀ��</retcount>
			<rettag>���صı�ǩ</rettag>
		</head>
		<body>
			<data>d1,d2,d3</data>
			<data>d1,d2,d3</data>
			<data>d1,d2,d3</data>
		</body>
	</root>
	 * */
	/***
	 * @param list ��ѯ�����Ľ����
	 * @param ziduan �ֶ�
	 * @param bmap �ֶκ����ݸ�ʽ�����map
	 * @param fhvo ��ʽ����ı�ͷ
	 * @param s �ܵļ�¼��
	 * */
//	public Document writetoXmlFile(List<HashMap> list,List<String> ziduan,Map<String,DataformatdefBVO> bmap,DataformatdefHVO fhvo,int s){
//		String filtype=fhvo.getMessflowdef();
//		Document rootDoc = DocumentHelper.createDocument();
//		Element root=rootDoc.addElement("root");
//		Element head=root.addElement("head");
//		head.addElement("retcode").setText("0");
//		head.addElement("retmsg").setText("�ɹ�");
//		head.addElement("retcount").setText(""+list.size());
//		Element body=root.addElement("body");
//		String bq="";
//		for(int i=0;i<list.size();i++){
//			String data="";
//			for(int j=0;j<ziduan.size();j++){
//				String ziduani=ziduan.get(j);
//				String value=((list.get(i).get(ziduan.get(j))==null?"":list.get(i).get(ziduan.get(j))))+"";
//				DataformatdefBVO bvoi=bmap.get(ziduani);
//				ziduani=ziduani.toLowerCase();
//				if(ziduani.startsWith("jlshs_")){
//					value=s+"";
//				}else if(ziduani.startsWith("sjhs_")){
//					value=new SimpleDateFormat(value).format(new Date());
//				}else if(ziduani.startsWith("zjhs_")){
//					try {
//						value=new QueryFieldImpl().getOID();
//					} catch (NamingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				String text=filtype.equals("0")?bvoi.getDatakind():bvoi.getCorreskind();
//				data=data+value+",";
//				if(i==0){
//					bq=bq+text+",";
//				}
//			}
//			if(data!=null&&data.length()>0){
//				data=data.substring(0,data.length()-1);
//			}
//			body.addElement("data").setText(data);
//		}
//		head.addElement("rettag").setText(bq!=null&&bq.length()>0?bq.substring(0,bq.length()-1):"");
//		return rootDoc;
//	}

	/**
	 * 
	 * @param prop
	 */
	public String callData(String prop){
		//����Դ���û������������ֶ�����ip���ڼ�ҳ��ÿҳ��������
        //datasource,usercode,tablename,pk|code|name,5,1000
		
		Document rootDoc = DocumentHelper.createDocument();
		Element root=rootDoc.addElement("root");
		Element head=root.addElement("head");
		if(prop==null||!(prop.split(",").length==7)){
			head.addElement("retcode").setText("1");
			head.addElement("retmsg").setText("������������ȷ");
			return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
		}
		Boolean flag=false;
		String datasource="";
		String usercode="";
		String tablename="";
		String fields="";
		String ip="";
		int pageSize=0;
		int currentPage=0;
		if(prop.split(",").length==7){
			String[] props=prop.split(",");
			 datasource=props[0];
			 usercode=props[1];
			 tablename=props[2];
			 fields=props[3];
			 fields=fields.replace("|", ",");
			 ip=props[4];
			 currentPage=Integer.parseInt(props[5]);
			 pageSize=Integer.parseInt(props[6]);
		}
		Object obj=DipWebserviceUtil.validate(datasource, ip, usercode);
		if(obj!=null){
			if(obj instanceof Boolean){
			  flag=(Boolean) obj;
			}else{
				head.addElement("retcode").setText("1");
				head.addElement("retmsg").setText("��ѯ���ݳ���:"+obj.toString());
				return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
			}	
		}
		if(flag){
			if(DipWebserviceUtil.validateResources(usercode, datasource, DipWebserviceUtil.DIP_ROLEANDTABLE_B, tablename)){
				String sql="select "+fields +" from ( "
                +	"select "+fields+" ,rownum rwn  from "+tablename+" where rownum<="+((currentPage+1)*pageSize)
                +" )  where rwn>"+currentPage*pageSize;
				try {
					List<Map> list = (List<Map>)new BaseDAO(datasource).executeQuery(sql, new MapListProcessor());
					if(list!=null&&list.size()>0){
						rootDoc=writeListtoXmlFile(list, fields);
					}else{
						head.addElement("retcode").setText("1");
						head.addElement("retmsg").setText("�����Ѿ��������û������");
						return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
					}
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					head.addElement("retcode").setText("1");
					head.addElement("retmsg").setText("��ѯ���ݳ���"+e.getMessage());
					return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
				}
			}else{
				head.addElement("retcode").setText("1");
				head.addElement("retmsg").setText("��ѯ���ݳ���"+"�û�"+usercode+"û�б�"+tablename+"�ķ���Ȩ��");
				return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
			}
		}
		return "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+rootDoc.getRootElement().asXML();
	}
	
	/***
	 * @param fieldsListValue ��ѯ�����Ľ����
	 * @param fields �ֶ�
	 * */
	public Document writeListtoXmlFile(List<Map> fieldsListValue,String fields){
		Document rootDoc = DocumentHelper.createDocument();
		Element root=rootDoc.addElement("root");
		Element head=root.addElement("head");
		head.addElement("retcode").setText("0");
		head.addElement("retmsg").setText("�ɹ�");
		head.addElement("retcount").setText(""+fieldsListValue.size());
		Element body=root.addElement("body");
		String fieldsArry[]=fields.split(",");
		for(int i=0;i<fieldsListValue.size();i++){
			StringBuffer data=new StringBuffer();
			for(int j=0;j<fieldsArry.length;j++){
				String field=fieldsArry[j];
				String value=fieldsListValue.get(i).get(field.toLowerCase())==null?"":fieldsListValue.get(i).get(field.toLowerCase()).toString();
				field=field.toLowerCase();
				data.append(value+",");
			}
			body.addElement("data").setText(data.substring(0,data.length()-1));
		}
		head.addElement("rettag").setText(fields);
		return rootDoc;
	}
	
}
