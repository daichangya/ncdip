package nc.ui.dip.dlg.warntime;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.WindowConstants;

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
public class WarningTime extends javax.swing.JPanel implements ActionListener{
	private DipWarningsetDayTimeVO dtvo;
	JRadioButton[] dwm;
	JRadioButton[] fo;
	private JRadioButton day;//每天
	private JRadioButton month;//按月
	private JRadioButton week;//按周
	private JRadioButton jpp;//周期发生
	private JRadioButton jpo;//只发生一次
	private JSpinner pl;//选的频率间隔
	private JSpinner time;
	private JTextField os;//只发生一次的秒
	private JTextField om;//只发生一次的
	private JTextField oh;
	private JTextField eh;
	private JTextField em;
	private JTextField es;
	private JTextField ss;
	private JTextField sm;
	private JTextField sh;
	private JProgressBar jProgressBar4;
	private JLabel jLabel9;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel5;
	private JLabel jLabel4;
	private JCheckBox d1;
	private JCheckBox d2;
	private JCheckBox d3;
	private JCheckBox d4;
	private JCheckBox d5;
	private JCheckBox d6;
	private JCheckBox d7;
	private JCheckBox d8;
	private JCheckBox d9;
	private JCheckBox d10;
	private JCheckBox d11;
	private JCheckBox d12;
	private JCheckBox d13;
	private JCheckBox d14;
	private JCheckBox d15;
	private JCheckBox d16;
	private JCheckBox d17;
	private JCheckBox d18;
	private JCheckBox d19;
	private JCheckBox d20;
	private JCheckBox d21;
	private JCheckBox d22;
	private JCheckBox d23;
	private JCheckBox d24;
	private JCheckBox d25;
	private JCheckBox d26;
	private JCheckBox d27;
	private JCheckBox d28;
	private JCheckBox d29;
	private JCheckBox d30;
	private JCheckBox d31;
	private JCheckBox w1;
	private JCheckBox w2;
	private JCheckBox w3;
	private JCheckBox w4;
	private JCheckBox w5;
	private JCheckBox w6;
	private JCheckBox w7;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JProgressBar jProgressBar1;
	private JProgressBar jProgressBar3;
	private JProgressBar jProgressBar2;

	ButtonGroup bg=new ButtonGroup();
	ButtonGroup bg1=new ButtonGroup();
	private JCheckBox[] jcbday=new JCheckBox[31];
	private JCheckBox[] jcbweek=new JCheckBox[7];

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new WarningTime(null));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public WarningTime(DipWarningsetDayTimeVO dtvo) {
		super();
		initGUI();
		this.dtvo=dtvo;
		setDTVO();
	}
	private void setDTVO(){
		if(SJUtil.isNull(dtvo)){
			dtvo=new DipWarningsetDayTimeVO();
		}else{
			String swartime_vo=dtvo.getWarntime();
			if(!SJUtil.isNull(swartime_vo)&&swartime_vo.length()==8){
				this.oh.setText(swartime_vo.substring(0,2));
				this.om.setText(swartime_vo.substring(3, 5));
				this.os.setText(swartime_vo.substring(6,8));
			}
			String starttime_vo=dtvo.getPeriodstart();
			if(!SJUtil.isNull(starttime_vo)&&starttime_vo.length()==8){
				this.sh.setText(starttime_vo.substring(0,2));
				this.sm.setText(starttime_vo.substring(3, 5));
				this.ss.setText(starttime_vo.substring(6,8));
			}
			String endtime_vo=dtvo.getPeriodend();
			if(!SJUtil.isNull(endtime_vo)&&endtime_vo.length()==8){
				this.eh.setText(endtime_vo.substring(0,2));
				this.em.setText(endtime_vo.substring(3, 5));
				this.es.setText(endtime_vo.substring(6,8));
			}
			String dwm_vo=dtvo.getDwm();
			if(dwm_vo.equals("0")){
				day.setSelected(true);
			}else if(dwm_vo.equals("1")){
				week.setSelected(true);
			}else{
				month.setSelected(true);
			}
			String pl_vo=dtvo.getFo();
			if(pl_vo.equals("0")){
				jpp.setSelected(true);
			}else{
				jpo.setSelected(true);
			}
			Integer sj_vo=dtvo.getPeriodtime();

//			private JSpinner pl;//选的频率间隔
//			private JSpinner time;
			if(sj_vo%60==0){
				time.getModel().setValue("时");
				pl.getModel().setValue(sj_vo/60+"");
			}else{
				time.getModel().setValue("分");
				pl.getModel().setValue(sj_vo+"");
			}
			try {
				for(int i=0;i<jcbday.length;i++){
					Method m = DipWarningsetDayTimeVO.class.getMethod("getD"+(i+1));
						jcbday[i].setSelected(((UFBoolean)m.invoke(dtvo)).booleanValue());
				}
				for(int i=0;i<jcbweek.length;i++){
	
					Method m = DipWarningsetDayTimeVO.class.getMethod("getW"+(i+1));
						jcbweek[i].setSelected(((UFBoolean)m.invoke(dtvo)).booleanValue());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public DipWarningsetDayTimeVO getDTVO(){
		String swartime="";
		String[] times = new String[3];
		times[0]=this.oh.getText();
		times[1]=this.om.getText();
		times[2]=this.os.getText();
		for(int i=0;i<3;i++){
			if(SJUtil.isNull(times[i])){
				times[i]="00";
			}else if(times[i].length()==1){
				times[i]="0"+times[i];
			}
		}
		swartime=times[0]+":"+times[1]+":"+times[2];
		dtvo.setWarntime(swartime);

		times[0]=this.sh.getText();
		times[1]=this.sm.getText();
		times[2]=this.ss.getText();
		for(int i=0;i<3;i++){
			if(SJUtil.isNull(times[i])){
				times[i]="00";
			}else if(times[i].length()==1){
				times[i]="0"+times[i];
			}
		}
		swartime=times[0]+":"+times[1]+":"+times[2];
		dtvo.setPeriodstart(swartime);
		
		times[0]=this.eh.getText();
		times[1]=this.em.getText();
		times[2]=this.es.getText();
		for(int i=0;i<3;i++){
			if(SJUtil.isNull(times[i])){
				times[i]="00";
			}else if(times[i].length()==1){
				times[i]="0"+times[i];
			}
		}
		swartime=times[0]+":"+times[1]+":"+times[2];
		dtvo.setPeriodend(swartime);
		
		String dwm="0";
		if(week.isSelected()){
			dwm="1";
		}else if(month.isSelected()){
			dwm="2";
		}
		dtvo.setDwm(dwm);
		String pl_vo="0";//dtvo.getFo();
		if(jpp.isSelected()){
			pl_vo="0";
		}else if(jpo.isSelected()){
			pl_vo="1";
		}
		dtvo.setFo(pl_vo);

		Integer cd=pl.getModel().getValue()==null?0:Integer.parseInt(pl.getModel().getValue().toString());
		if(time.getModel().getValue().equals("时")){
			dtvo.setPeriodtime(cd*60);
		}else{
			dtvo.setPeriodtime(cd);
		}
		try {
			for(int i=0;i<jcbday.length;i++){
				Method m = DipWarningsetDayTimeVO.class.getMethod("setD"+(i+1),new Class[] { UFBoolean.class });
				m.invoke(dtvo,new Object[]{new UFBoolean(jcbday[i].isSelected())});
			}
			for(int i=0;i<jcbweek.length;i++){
				Method m = DipWarningsetDayTimeVO.class.getMethod("setW"+(i+1),new Class[] { UFBoolean.class });
				m.invoke(dtvo,new Object[]{new UFBoolean(jcbweek[i].isSelected())});
			}
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
			this.setPreferredSize(new java.awt.Dimension(621, 399));
			this.setLayout(null);
			
			{
				day = new JRadioButton();
				this.add(day);
				day.setLayout(null);
				day.setText("\u6bcf\u5929");//每天
				day.setBounds(50, 7, 88, 21);
			}
			{
				week = new JRadioButton();
				this.add(week);
				week.setText("\u6bcf\u5468");//每周
				week.setBounds(187, 8, 88, 17);
			}
			{
				month = new JRadioButton();
				this.add(month);
				month.setText("\u6bcf\u6708");//每月
				month.setBounds(336, 5, 88, 22);
			}
			{
				jProgressBar1 = new JProgressBar();
				this.add(jProgressBar1);
				jProgressBar1.setBounds(38, 2, 499, 31);
			}
			{
				jpp = new JRadioButton();
				this.add(jpp);
				jpp.setText("\u5468\u671f\u53d1\u751f");
				jpp.setBounds(44, 57, 77, 19);
			}
			{
				jpo = new JRadioButton();
				this.add(jpo);
				jpo.setText("\u53ea\u53d1\u751f\u4e00\u6b21");
				jpo.setBounds(44, 106, 104, 19);
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("\u5f00\u59cb\u65f6\u95f4");
				jLabel1.setBounds(127, 46, 60, 16);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("\u7ed3\u675f\u65f6\u95f4");
				jLabel2.setBounds(125, 82, 60, 12);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("\u5468\u671f\u9891\u7387");
				jLabel3.setBounds(330, 59, 62, 15);
			}
			{
				SpinnerListModel TimeModel = 
					new SpinnerListModel(
							new String[] { "时", "分"  });
				time = new JSpinner();
				this.add(time);
				time.setModel(TimeModel);
				time.setBounds(455, 55, 36, 21);
				time.getEditor().setToolTipText("\u65f6\uff0c\u5206\uff0c\u79d2");
				time.setFocusable(false);
			}
			{
				w1 = new JCheckBox();
				this.add(w1);
				w1.setText("\u661f\u671f\u4e00");
				w1.setBounds(50, 146, 82, 19);
			}
			{
				w2 = new JCheckBox();
				this.add(w2);
				w2.setText("\u661f\u671f\u4e8c");
				w2.setBounds(168, 146, 83, 17);
			}
			{
				w3 = new JCheckBox();
				this.add(w3);
				w3.setText("\u661f\u671f\u4e09");
				w3.setBounds(302, 146, 74, 19);
			}
			{
				w4 = new JCheckBox();
				this.add(w4);
				w4.setText("\u661f\u671f\u56db");
				w4.setBounds(425, 146, 83, 19);
			}
			{
				w5 = new JCheckBox();
				this.add(w5);
				w5.setText("\u661f\u671f\u4e94");
				w5.setBounds(50, 182, 82, 19);
			}
			{
				w6 = new JCheckBox();
				this.add(w6);
				w6.setText("\u661f\u671f\u516d");
				w6.setBounds(168, 182, 83, 19);
			}
			{
				w7 = new JCheckBox();
				this.add(w7);
				w7.setText("\u661f\u671f\u65e5");
				w7.setBounds(302, 182, 86, 19);
			}
			{
				jProgressBar3 = new JProgressBar();
				this.add(jProgressBar3);
				jProgressBar3.setBounds(38, 143, 499, 73);
			}
			{
				d1 = new JCheckBox();
				this.add(d1);
				d1.setText("1");
				d1.setBounds(50, 242, 43, 19);
			}
			{
				d2 = new JCheckBox();
				this.add(d2);
				d2.setText("2");
				d2.setBounds(93, 242, 45, 19);
			}
			{
				d3 = new JCheckBox();
				this.add(d3);
				d3.setText("3");
				d3.setBounds(138, 242, 48, 19);
			}
			{
				d4 = new JCheckBox();
				this.add(d4);
				d4.setText("4");
				d4.setBounds(186, 242, 46, 19);
			}
			{
				d5 = new JCheckBox();
				this.add(d5);

				d5.setText("5");
				d5.setBounds(232, 242, 49, 19);
			}
			{
				d6 = new JCheckBox();
				this.add(d6);
				d6.setText("6");
				d6.setBounds(281, 242, 39, 19);
			}
			{
				d7 = new JCheckBox();
				this.add(d7);
				d7.setText("7");
				d7.setBounds(329, 242, 37, 19);
			}
			{
				d8 = new JCheckBox();
				this.add(d8);
				d8.setText("8");
				d8.setBounds(380, 242, 46, 19);
			}
			{
				d9 = new JCheckBox();
				this.add(d9);
				d9.setText("9");
				d9.setBounds(427, 242, 46, 16);
			}
			{
				d10 = new JCheckBox();
				this.add(d10);
				d10.setText("10");
				d10.setBounds(475, 242, 56, 17);
			}
			{
				d11 = new JCheckBox();
				this.add(d11);
				d11.setText("11");
				d11.setBounds(50, 267, 43, 19);
			}
			{
				d12 = new JCheckBox();
				this.add(d12);
				d12.setText("12");
				d12.setBounds(93, 267, 43, 19);
			}
			{
				d13 = new JCheckBox();
				this.add(d13);
				d13.setText("13");
				d13.setBounds(137, 267, 49, 19);
			}
			{
				d14 = new JCheckBox();
				this.add(d14);
				d14.setText("14");
				d14.setBounds(187, 267, 45, 19);
			}
			{
				d15 = new JCheckBox();
				this.add(d15);
				d15.setText("15");
				d15.setBounds(232, 267, 49, 19);
			}
			{
				d16 = new JCheckBox();
				this.add(d16);
				d16.setText("16");
				d16.setBounds(281, 268, 47, 18);
			}
			{
				d17 = new JCheckBox();
				this.add(d17);
				d17.setText("17");
				d17.setBounds(328, 268, 53, 16);
			}
			{
				d18 = new JCheckBox();
				this.add(d18);
				d18.setText("18");
				d18.setBounds(381, 267, 46, 19);
			}
			{
				d19 = new JCheckBox();
				this.add(d19);
				d19.setText("19");
				d19.setBounds(427, 268, 46, 19);
			}
			{
				d20 = new JCheckBox();
				this.add(d20);
				d20.setText("20");
				d20.setBounds(475, 268, 52, 15);
			}
			{
				d21 = new JCheckBox();
				this.add(d21);
				d21.setText("21");
				d21.setBounds(50, 292, 42, 19);
			}
			{
				d22 = new JCheckBox();
				this.add(d22);
				d22.setText("22");
				d22.setBounds(92, 292, 44, 19);
			}
			{
				d23 = new JCheckBox();
				this.add(d23);
				d23.setText("23");
				d23.setBounds(136, 292, 50, 19);
			}
			{
				d24 = new JCheckBox();
				this.add(d24);
				d24.setText("24");
				d24.setBounds(186, 292, 46, 19);
			}
			{
				d25 = new JCheckBox();
				this.add(d25);
				d25.setText("25");
				d25.setBounds(232, 292, 49, 19);
			}
			{
				d26 = new JCheckBox();
				this.add(d26);
				d26.setText("26");
				d26.setBounds(281, 292, 47, 19);
			}
			{
				d27 = new JCheckBox();
				this.add(d27);
				d27.setText("27");
				d27.setBounds(328, 292, 53, 19);
			}
			{
				d28 = new JCheckBox();
				this.add(d28);
				d28.setText("28");
				d28.setBounds(381, 292, 46, 19);
			}
			{
				d29 = new JCheckBox();
				this.add(d29);
				d29.setText("29");
				d29.setBounds(427, 295, 48, 19);
			}
			{
				d30 = new JCheckBox();
				this.add(d30);
				d30.setText("30");
				d30.setBounds(475, 294, 52, 17);
			}
			{
				d31 = new JCheckBox();
				this.add(d31);
				d31.setText("31");
				d31.setBounds(50, 319, 42, 19);
			}
			{
				sh = new JTextField();
				this.add(sh);
				sh.setBounds(187, 43, 24, 22);
			}
			{
				sm = new JTextField();
				this.add(sm);
				sm.setBounds(217, 43, 24, 22);
			}
			{
				ss = new JTextField();
				this.add(ss);
				ss.setBounds(250, 43, 24, 22);
			}
			{
				eh = new JTextField();
				this.add(eh);
				eh.setBounds(187, 77, 24, 22);
			}
			{
				em = new JTextField();
				this.add(em);
				em.setBounds(217, 77, 25, 22);
			}
			{
				es = new JTextField();
				this.add(es);
				es.setBounds(250, 77, 25, 22);
			}
			{
				oh = new JTextField();
				this.add(oh);
				oh.setBounds(160, 104, 22, 22);
			}
			{
				om = new JTextField();
				this.add(om);
				om.setBounds(191, 104, 24, 22);
			}
			{
				os = new JTextField();
				this.add(os);
				os.setBounds(223, 104, 22, 22);
			}
			{
				SpinnerListModel plModel = 
					new SpinnerListModel(
							new String[] { "1", "2" , "3" , "4" , "5" , "6" , "7" ,"8","9","10","11", "12" , "13" , "14" , "15" , "16" , "17" ,"18","19","20","21","22","23","24"});
				pl = new JSpinner();
				this.add(pl);
				pl.setModel(plModel);
				pl.setBounds(400, 55, 39, 22);
			}
			{
				jProgressBar2 = new JProgressBar();
				this.add(jProgressBar2);
				jProgressBar2.setBounds(38, 238, 499, 111);
			}
			{
				jLabel4 = new JLabel();
				this.add(jLabel4);
				jLabel4.setText(":");
				jLabel4.setBounds(211, 47, 12, 15);
			}
			{
				jLabel5 = new JLabel();
				this.add(jLabel5);
				jLabel5.setText(":");
				jLabel5.setBounds(244, 43, 9, 22);
			}
			{
				jLabel6 = new JLabel();
				this.add(jLabel6);
				jLabel6.setText(":");
				jLabel6.setBounds(211, 76, 9, 24);
			}
			{
				jLabel7 = new JLabel();
				this.add(jLabel7);
				jLabel7.setText(":");
				jLabel7.setBounds(245, 75, 10, 27);
			}
			{
				jLabel8 = new JLabel();
				this.add(jLabel8);
				jLabel8.setText(":");
				jLabel8.setBounds(186, 105, 8, 22);
			}
			{
				jLabel9 = new JLabel();
				this.add(jLabel9);
				jLabel9.setText(":");
				jLabel9.setBounds(217, 104, 7, 20);
			}
			{
				jProgressBar4 = new JProgressBar();
				this.add(jProgressBar4);
				jProgressBar4.setBounds(38, 37, 499, 98);
			}
//			fo=new JRadioButton[2];
//			fo[0]=jpp;
//			fo[1]=jpo;
			jpo.setSelected(true);
			bg.add(jpp);
			bg.add(jpo);
			bg1.add(day);
			bg1.add(month);
			bg1.add(week);
			day.setSelected(true);
			String date=new SimpleDateFormat("yyyymmddHHmmss").format(new Date());
			String dateh=date.substring(8,10);
			String datef=date.substring(10,12);
			String dates=date.substring(12,14);
			oh.setText(dateh);
			om.setText(datef);
			os.setText(dates);
			jcbday[0]=d1;
			jcbday[1]=d2;
			jcbday[2]=d3;
			jcbday[3]=d4;
			jcbday[4]=d5;
			jcbday[5]=d6;
			jcbday[6]=d7;
			jcbday[7]=d8;
			jcbday[8]=d9;
			jcbday[9]=d10;
			jcbday[10]=d11;
			jcbday[11]=d12;
			jcbday[12]=d13;
			jcbday[13]=d14;
			jcbday[14]=d15;
			jcbday[15]=d16;
			jcbday[16]=d17;
			jcbday[17]=d18;
			jcbday[18]=d19;
			jcbday[19]=d20;
			jcbday[20]=d21;
			jcbday[21]=d22;
			jcbday[22]=d23;
			jcbday[23]=d24;
			jcbday[24]=d25;
			jcbday[25]=d26;
			jcbday[26]=d27;
			jcbday[27]=d28;
			jcbday[28]=d29;
			jcbday[29]=d30;
			jcbday[30]=d31;
			jcbweek[0]=w1;
			jcbweek[1]=w2;
			jcbweek[2]=w3;
			jcbweek[3]=w4;
			jcbweek[4]=w5;
			jcbweek[5]=w6;
			jcbweek[6]=w7;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
