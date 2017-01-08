package nc.bs.dip.log;

import java.util.regex.Pattern;
import nc.bs.logging.Log;

public class DipErroLogParam {

	 public DipErroLogParam()
	    {
	    }

	    public static final Log log = Log.getInstance("diperror");
	    public static long timeThreshold = 500L;
	    public static Pattern clientFilter;
}
