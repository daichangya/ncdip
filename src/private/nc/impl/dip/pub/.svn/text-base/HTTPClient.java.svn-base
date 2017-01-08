package nc.impl.dip.pub;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class HTTPClient {
	String URL ;
	HttpURLConnection con;
	
	public HTTPClient(String url) throws IOException{
		URL = url;
		getConnection();
	}
	
	public void getConnection() throws IOException{
		if(con==null){
			URL url = new URL(URL);
//			System.out.println("\n...curr [URL] "+url.toString());
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("content-type", "text/xml");// 其中 text/xml;charset=utf-8,gb2312 的 charset 可以省略
			con.setRequestMethod("POST");
	    }
	}
	
	public BackMsgVO doPost(String doc) throws Exception{
		postStr(doc);
		BackMsgVO msg = getResponse();
		return msg;
	}
	
	public BackMsgVO doPost(Document doc) throws Exception{
		postDoc(doc);
		BackMsgVO msg = getResponse();
		return msg;
	}
	
	public void postStr(String input) throws UnsupportedEncodingException, IOException{
		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(),"gb2312");
		writer.write(input);
		writer.flush();
		writer.close();
	}
	
	public void postDoc(Document doc) throws UnsupportedEncodingException, IOException{
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("gb2312");
		OutputStream ot=con.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(ot, "gb2312");
		XMLWriter writer = new XMLWriter(osw,format);
		writer.write(doc);
		osw.close();
		writer.close();
	}
	public String getResponseDoc() throws Exception{
		StringBuffer sb = new StringBuffer();
		InputStream inputStream = null;
		try {
			inputStream = con.getInputStream();
			byte[] tempBytes = new byte[2048];
			int count = 0;
			while((count = inputStream.read(tempBytes)) > 0 ){
				sb.append(new String(tempBytes,0,count,"utf-8"));
			}
		} catch (IOException e) {
			throw e;
		} finally{
			try{
				if(inputStream!=null){
					inputStream.close();
				}
			}catch(Exception e){
				throw e;
			}
		}
		
	    String resp=sb.toString();
//	    System.out.println(resp);
//	    Document doc = DocumentHelper.parseText(resp);
//	    doc.setXMLEncoding("utf-8");
	    return resp;
	}
	
	public BackMsgVO getResponse() throws Exception{
		StringBuffer sb = new StringBuffer();
		InputStream inputStream = null;
		try {
			inputStream = con.getInputStream();
			byte[] tempBytes = new byte[2048];
			int count = 0;
			while((count = inputStream.read(tempBytes)) > 0 ){
				sb.append(new String(tempBytes,0,count,"utf-8"));
			}
		} catch (IOException e) {
			throw e;
		} finally{
			try{
				if(inputStream!=null){
					inputStream.close();
				}
			}catch(Exception e){
				throw e;
			}
		}
		
	    String resp=sb.toString();
	    System.out.println(resp);
	    Document doc = DocumentHelper.parseText(resp);
//	    doc.setXMLEncoding("utf-8");
	    return buildResultVO(doc);
	}
	
	private BackMsgVO buildResultVO(Document doc){
//		ArrayList<BackM> re = new ArrayList<TResult>();
		BackMsgVO msgvo = new BackMsgVO();
		Element root = doc.getRootElement();
//		String resultFile = root.attributeValue("filename");
		Element sendresult = root.element("sendresult");
		msgvo.setBdocId(sendresult.element("bdocid").getText());
		msgvo.setResultCode(sendresult.element("resultcode").getText());
		msgvo.setResultDesc(sendresult.element("resultdescription").getText());
		msgvo.setContent(sendresult.element("content").getText());
		msgvo.setSuccess(root.attributeValue("successful"));
//		System.out.println(msgvo.isSuccess()+" ... "+root.attributeValue("receiver"));
		String receiver = root.attributeValue("receiver");
		if(receiver!=null&&receiver.length()>=4){
			msgvo.setUnitcode(root.attributeValue("receiver").substring(0, 4));
		}else{
			msgvo.setUnitcode(receiver);
		}
		
		msgvo.setXmlFile(root.attributeValue("filename"));
		return msgvo;
	}
}
