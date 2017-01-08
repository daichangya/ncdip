package nc.bs.dip.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import nc.vo.dip.xml.XMLDataVO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLReader {
	public XMLDataVO read(File file) throws DocumentException{
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(file);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
	    Iterator it = root.elementIterator();
	    
	    XMLDataVO data = new XMLDataVO();
	    int row = 0;
	    while(it.hasNext()){
	    	Element head = (Element)it.next();
	    	List list = head.elements();
	    	if(list == null || list.size() == 0){
	    		continue;
	    	}
	    	for(int i=0;i<list.size();i++){
	    		data.readNode(i, row, (Element)list.get(i));
	    	}
	    	row++;
	    }
	    
	    return data;
	}
}
