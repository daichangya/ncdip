package nc.vo.pub.ldap;

import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParseXML {
	// ����һ��Properties �����������ֵ
	private Properties props;

	public Properties getProps() {
		return this.props;
	}
	

	public void parse(String filename) throws Exception {
		// �����ǵĽ���������
		ConfigParser handler = new ConfigParser();
		// ��ȡSAX��������
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		// ��ȡSAX����
		SAXParser parser = factory.newSAXParser();
		try {
			// ���������ͽ�������xml��ϵ����,��ʼ����
			parser.parse(filename, handler);
			// ��ȡ�����ɹ��������
			props = handler.getProps();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
}
