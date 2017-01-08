package nc.ui.pub.tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;

import nc.ui.pub.beans.UIDialog;
import nc.ui.sm.login.LoginPanelRes;

/**
 * bannerÕº±Í
 * @author ÷‹…∆±£
 * Created on 2005-3-16
 */
public class BannerDialog extends UIDialog {
	private BannerLabel lbBanner = null;
	private boolean bStop = false;
	
	public BannerDialog(Container owner) {
		super(owner);
		init(null);
	}

	public BannerDialog(Frame owner) {
		super(owner);
		init(owner);
	}
	
	public BannerDialog(Dialog owner) {
		super(owner);
		init(owner);
	}
	
	private void init(Window owner){
		setTitle(LoginPanelRes.getUFSoft());
		setDefaultCloseOperation(UIDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setSize(250,120);
		if (owner != null){
			setLocationRelativeTo(owner);
		}else{
			Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((ds.width-250)/2,(ds.height-120)/2);
		}
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getBannerLabel(),BorderLayout.CENTER);		
	}
	
	private BannerLabel getBannerLabel(){
		if (lbBanner == null){
			lbBanner= new BannerLabel();
			lbBanner.setHorizontalAlignment(BannerLabel.CENTER);
		}
		return lbBanner;
	}

	public void start(){
		new Thread(new Runnable(){
	 		public void run(){
//	 			try {
//	 				Thread.sleep(1500);
//				} catch (Exception e) {
//				}
	 			if (!bStop){
	 				setVisible(true);
	 			}
	 		}
		}).start();
	}
	public void end(){
		bStop = true;
		setVisible(false);
		dispose();
	}
	
	public void setStartText(String startText) {
		getBannerLabel().setText(startText);
	}
	
	public void setEndText(String endText) {
		getBannerLabel().setText(endText);
	}
	protected void processWindowEvent(WindowEvent e) {
		
	}
}
