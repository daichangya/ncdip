package nc.bs.dip.log;

import java.util.regex.Pattern;
import nc.bs.logging.Log;

public class DipAllLogParam {

	 public DipAllLogParam()
	    {
	    }

	    public static final Log log = Log.getInstance("dipall");
	    public static long timeThreshold = 500L;
	    public static Pattern clientFilter;
}
