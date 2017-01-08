package nc.impl.dip.pub;

import java.io.IOException;
 

public class PfxxClient {
	static HTTPClient client;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			client = new HTTPClient("http://127.0.0.1");
			client.getConnection();
			BackMsgVO msg = client.doPost("%xmlstr%");
			System.out.println(msg.getResultDesc());
			System.out.println(msg.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
