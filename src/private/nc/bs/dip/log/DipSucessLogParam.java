package nc.bs.dip.log;

import java.util.regex.Pattern;
import nc.bs.logging.Log;

public class DipSucessLogParam {

	 public DipSucessLogParam()
	    {
	    }

	    public static final Log log = Log.getInstance("dipsucess");
	    public static long timeThreshold = 500L;
	    public static Pattern clientFilter;
}
