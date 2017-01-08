package nc.pub.dip.startup;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.vo.pub.BusinessException;

public class MyTask extends TimerTask {
	private static final int C_SCHEDULE_HOUR = 11;

	private static boolean isRunning = false;

	private ServletContext context = null;

	public MyTask(ServletContext context) {
		this.context = context;
	}

	public void run() {
		Calendar cal = Calendar.getInstance();
		if (!isRunning) {
			if (C_SCHEDULE_HOUR == Calendar.HOUR_OF_DAY) {
				isRunning = true;
				context.log("开始执行指定任务");

				// TODO 添加自定义的详细任务，以下只是示例
				context.log("正在执行~~~~~~~~~~~~~~");
				nc.itf.dip.pub.IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				try {
					String ss=iqf.queryfield("select unitname from bd_corp where pk_corp='1056'");
					context.log(ss+"-----------------------------------");
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
				/*AlertMain a=new AlertMain();
				try {
					a.executeTask(null);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				isRunning = false;
				context.log("指定任务执行结束");
			}
		} else {
			context.log("上一次任务执行还未结束");
		}
	}
}
