package nc.ui.dip.tyzhq.iniufoenv;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JComboBox;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;

import nc.vo.framework.rsa.Encode;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.fi.uforeport.NCFuncForUFO;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;

public class InitUFOJPanel extends javax.swing.JPanel {
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel4;
	private JTextField usercodefield;
//	private JTextField passwordfield;
	private JTextField unitcodeField;
//	private JLabel jLabel5;
	private JLabel jLabel3;
//	private JTextField desing;
	private JComboBox accountlist;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new InitUFOJPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public InitUFOJPanel() {
		super();
		initGUI();
	}
	Map<String,String> map=ClientEnvDef.getAccountMap();
	private void initGUI() {
		Map<String,Integer> mapi=new HashMap<String, Integer>();
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("\u5e10    \u5957");
				jLabel1.setBounds(52, 42, 72, 15);
			}
			{
				
				String key="";
				int i=0;
				if(map!=null&&map.size()>0){
					Iterator<String> it=map.keySet().iterator();
					while(it.hasNext()){
						String ikey=it.next();
						key=key+ikey+",";
						mapi.put(map.get(ikey).split("\\|")[0], i);
						i++;
					}
					key=key.substring(0,key.length()-1);
				}
				DefaultComboBoxModel accountlistModel = 
					new DefaultComboBoxModel(
							key.split(","));
				accountlist = new JComboBox();
				this.add(accountlist);
				accountlist.setModel(accountlistModel);
				accountlist.setBounds(163, 36, 171, 21);
			}
		/*	{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("\u6570 \u636e \u6e90");
				jLabel2.setBounds(52, 79, 72, 15);
			}*/
			/*{
				desing = new JTextField();
				this.add(desing);
				desing.setBounds(163, 75, 171, 22);
			}*/
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("\u516c\u53f8\u7f16\u7801");
				jLabel3.setBounds(52, 113, 72, 15);
			}
			{
				jLabel4 = new JLabel();
				this.add(jLabel4);
				jLabel4.setText("\u7528 \u6237 \u540d");
				jLabel4.setBounds(52, 148, 72, 15);
			}
//			{
//				jLabel5 = new JLabel();
//				this.add(jLabel5);
//				jLabel5.setText("\u5bc6    \u7801");
//				jLabel5.setBounds(52, 186, 72, 15);
//			}
			{
				unitcodeField = new JTextField();
				this.add(unitcodeField);
				unitcodeField.setBounds(163, 107, 171, 22);
			}
//			{
//				passwordfield = new JTextField();
//				this.add(passwordfield);
//				passwordfield.setBounds(163, 180, 171, 22);
//			}
			{
				usercodefield = new JTextField();
				this.add(usercodefield);
				usercodefield.setBounds(163, 147, 171, 22);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ClientEnvDef.getCalenv()!=null){
			String[] fu=ClientEnvDef.getCalenv();
//			 String[] calEnv = {"ncdip", //数据源名称
//					  /*ClientEnvironment.getInstance().getAccount().getAccountCode()*/"02", //账套
//					  ClientEnvironment.getInstance().getCorporation().getUnitcode(), //单位
//					  "2011-04-01", //日期
//					  ClientEnvironment.getInstance().getUser().getUserCode(), //用户
//					  "1", //密码
//						"2011-04-01",
//				ClientEnvironment.getInstance().getLanguage()//语言类型
//				    };
			accountlist.setSelectedIndex(mapi.get(fu[1]));
//			desing.setText(fu[0]);
			unitcodeField.setText(fu[2]);
		}else{
			accountlist.setSelectedIndex(mapi.get(ClientEnvironment.getInstance().getAccount().getAccountCode()));
		}
		
	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public String[] getCalenvFromPanel(){
		String[] ff=new String[8];
		String acc=map.get(accountlist.getSelectedItem().toString());
		ff[0]=acc!=null&&acc.length()>0?acc.split("\\|")[1]:null;
		ff[1]=acc!=null&&acc.length()>0?acc.split("\\|")[0]:null;
		ff[2]=unitcodeField.getText();
		ff[3]=ClientEnvironment.getInstance().getDate().toString();
		ff[4]=usercodefield.getText();
		String password=null;
		try {
			password=iqf.queryfield("select user_password from sm_user where user_code='"+ff[4]+"'",ff[0]);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Encode ecode=new Encode();
		ff[5]=ecode.decode(password==null?"":password);
		ff[6]=ff[3];
		ff[7]=ClientEnvironment.getInstance().getLanguage();
		return ff;
	}
}
