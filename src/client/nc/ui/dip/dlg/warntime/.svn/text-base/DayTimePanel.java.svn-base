package nc.ui.dip.dlg.warntime;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;

import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;


import nc.util.dip.sj.SJUtil;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
import nc.vo.pub.lang.UFBoolean;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DayTimePanel extends javax.swing.JPanel {
	private DipWarningsetDayTimeVO dtvo;
	private JLabel time;
	private JProgressBar jProgressBar1;
	private JLabel jLabel2;
	private JLabel day;
	private JLabel jLabel1;
//	private JLabel week;
//	private JProgressBar jProgressBar3;
	private JProgressBar jProgressBar2;
	//是否启用--------------------------------
//	private JCheckBox isonweek;
	private JCheckBox isonday;
	//时间--------------------------------------------
	private JTextField timeh;
	private JTextField timem;
	private JTextField times;
	//日
	JCheckBox[] jcbday;
	//周
//	JCheckBox[] jcbweek;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new DayTimePanel(null));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/*public DayTimePanel() {
		super();
		initGUI();
	}*/
	public DayTimePanel(DipWarningsetDayTimeVO dtvo){
		super();
		initGUI();
		this.dtvo=dtvo;
		setDTVO();
	}
	private void setDTVO(){
		if(SJUtil.isNull(dtvo)){
			dtvo=new DipWarningsetDayTimeVO();
		}else{
			String swartime=dtvo.getWarntime();
			if(!SJUtil.isNull(swartime)&&swartime.length()==8){
				this.timeh.setText(swartime.substring(0,2));
				this.timem.setText(swartime.substring(3, 5));
				this.times.setText(swartime.substring(6,8));
			}
//			this.isonday.setSelected(dtvo.getIsonday().booleanValue());
//			this.isonweek.setSelected(dtvo.getIsonweek().booleanValue());
			try {
				for(int i=0;i<jcbday.length;i++){
					Method m = DipWarningsetDayTimeVO.class.getMethod("getD"+(i+1));
					jcbday[i].setSelected(((UFBoolean)m.invoke(dtvo)).booleanValue());
				}
				/*for(int i=0;i<jcbweek.length;i++){
					Method m = DipWarningsetDayTimeVO.class.getMethod("getW"+(i+1));
					jcbweek[i].setSelected(((UFBoolean)m.invoke(dtvo)).booleanValue());
				}*/
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	public DipWarningsetDayTimeVO getDTVO(){
		String swartime="";
		String[] times = new String[3];
		times[0]=this.timeh.getText();
		times[1]=this.timem.getText();
		times[2]=this.times.getText();
		for(int i=0;i<3;i++){
			if(SJUtil.isNull(times[i])){
				times[i]="00";
			}else if(times[i].length()==1){
				times[i]="0"+times[i];
			}
		}
		swartime=times[0]+":"+times[1]+":"+times[2];
		dtvo.setWarntime(swartime);
//		dtvo.setIsonday(new UFBoolean(this.isonday.isSelected()));
//		dtvo.setIsonweek(new UFBoolean(this.isonweek.isSelected()));
		try {
			for(int i=0;i<jcbday.length;i++){
				Method m = DipWarningsetDayTimeVO.class.getMethod("setD"+(i+1),new Class[] { UFBoolean.class });
				m.invoke(dtvo,new Object[]{new UFBoolean(jcbday[i].isSelected())});
			}
		/*	for(int i=0;i<jcbweek.length;i++){
				Method m = DipWarningsetDayTimeVO.class.getMethod("setW"+(i+1),new Class[] { UFBoolean.class });
				m.invoke(dtvo,new Object[]{new UFBoolean(jcbweek[i].isSelected())});
			}*/
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return dtvo;
	}
	private void initGUI() {
		try {
			jcbday=new JCheckBox[31];
//			jcbweek=new JCheckBox[7];
			this.setLayout(null);
			this.setPreferredSize(new java.awt.Dimension(485, 351));
			{
				time = new JLabel();
				this.add(time);
				time.setText("\u9884\u8b66\u89e6\u53d1\u65f6\u95f4");
				time.setBounds(52, 20, 100, 21);
			}
			{
				timeh = new JTextField();
				timeh.setDocument(new NumberDocument(timeh,"h"));
				this.add(timeh);
				timeh.setBounds(169, 17, 24, 25);
			}
			{
				day = new JLabel();
				this.add(day);
				day.setText("\u65e5");
				day.setBounds(26, 59, 15, 32);
			}
			for(int i=0;i<31;i++){
				jcbday[i]=new JCheckBox();
				this.add(jcbday[i]);
				jcbday[i].setText((i+1)+"");
			}
				jcbday[0] .setBounds(26, 97, 39, 19);
				jcbday[1].setBounds(67, 94, 36, 22);
				jcbday[2].setBounds(109, 98, 40, 19);
				jcbday[3].setBounds(149, 97, 44, 19);
				jcbday[4].setBounds(193, 99, 43, 16);
				jcbday[5].setBounds(236, 100, 47, 15);
				jcbday[6].setBounds(283, 96, 42, 22);
				jcbday[7].setBounds(326, 97, 40, 22);
				jcbday[8].setBounds(366, 97, 39, 23);
				jcbday[9].setBounds(405, 96, 43, 22);
				jcbday[10].setBounds(26, 123, 39, 19);
				jcbday[11].setBounds(67, 123, 41, 19);
				jcbday[12].setBounds(108, 122, 41, 21);
				jcbday[13].setBounds(149, 121, 44, 21);
				jcbday[14].setBounds(193, 122, 43, 23);
				jcbday[15].setBounds(236, 124, 45, 19);
				jcbday[16].setBounds(282, 124, 44, 19);
				jcbday[17].setBounds(326, 122, 40, 24);
				jcbday[18].setBounds(366, 126, 40, 19);
				jcbday[19].setBounds(406, 126, 44, 19);
				jcbday[20].setBounds(26, 149, 40, 19);
				jcbday[21].setBounds(66, 149, 40, 19);
				jcbday[22].setBounds(108, 149, 41, 19);
				jcbday[23].setBounds(149, 149, 44, 19);
				jcbday[24].setBounds(193, 149, 43, 19);
				jcbday[25].setBounds(236, 149, 46, 19);
				jcbday[26].setBounds(282, 149, 44, 19);
				jcbday[27].setBounds(326, 148, 40, 22);
				jcbday[28].setBounds(366, 150, 40, 19);
				jcbday[29].setBounds(406, 150, 44, 19);
				jcbday[30].setBounds(26, 180, 41, 17);
			/*	
			{
				week = new JLabel();
				this.add(week);
				week.setText("\u5468");
				week.setBounds(26, 222, 22, 26);
			}
			String [] we={"一","二","三","四","五","六","日"};
			for(int i=0;i<7;i++){
				jcbweek[i]=new JCheckBox();
				this.add(jcbweek[i]);
				jcbweek[i].setText("星期"+we[i]);
			}
			jcbweek[0].setBounds(26, 254, 93, 19);
			jcbweek[1].setBounds(143, 254, 96, 19);
			jcbweek[2].setBounds(251, 249, 89, 27);
			jcbweek[3].setBounds(366, 254, 76, 18);
			jcbweek[4].setBounds(26, 285, 81, 21);
			jcbweek[5].setBounds(143, 285, 93, 22);
			jcbweek[6].setBounds(251, 285, 89, 20);*/
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("\uff1a");
				jLabel1.setBounds(195, 19, 16, 21);
			}
			{
				timem = new JTextField(2);

				timem.setDocument(new NumberDocument(timem,"m"));
				this.add(timem);
				timem.setBounds(208, 17, 24, 25);
			}
			{
				times = new JTextField(2);
				times.setDocument(new NumberDocument(times,"s"));
				this.add(times);
				times.setBounds(248, 17, 24, 25);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("\uff1a");
				jLabel2.setBounds(235, 18, 16, 21);
			}
			{
				jProgressBar1 = new JProgressBar();
				this.add(jProgressBar1);
				jProgressBar1.setBounds(20, 11, 439, 42);
			}
			{
				isonday = new JCheckBox();
				this.add(isonday);
				isonday.setText("\u662f\u5426\u542f\u7528\u65e5\u671f\u9884\u8b66");
				isonday.setBounds(61, 66, 147, 19);
			}
		/*	{
				isonweek = new JCheckBox();
				this.add(isonweek);
				isonweek.setText("\u662f\u5426\u542f\u7528\u5468\u9884\u8b66");
				isonweek.setBounds(60, 227, 147, 19);
			}*/
			{
				jProgressBar2 = new JProgressBar();
				this.add(jProgressBar2);
				jProgressBar2.setBounds(21, 61, 436, 146);
			}
			/*{
				jProgressBar3 = new JProgressBar();
				this.add(jProgressBar3);
				jProgressBar3.setBounds(21, 218, 436, 109);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
