package ann.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeTool {
	private static DatetimeTool instance = null;
	public static DatetimeTool getInstance(){
		if(instance==null) instance = new DatetimeTool();
		return instance;
	}
	
	public Date now(){
		Date now = new Date();
		return now;
	}
	
	public String nowStr(){
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		return formatter.format(now);
	}
	
	public String standardFormat(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		return formatter.format(date);
	}
}
