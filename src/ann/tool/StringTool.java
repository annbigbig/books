package ann.tool;

public class StringTool {
	private static StringTool instance = null;
	public static StringTool getInstance(){
		if(instance==null) instance = new StringTool();
		return instance;
	}
	
	//傳入一個整數，返回字串型態
	public String convert(int value){
		String str = "";
		str = Integer.toString(value);
		return str;
	}
	
	//傳入一個字串，如果長度小於n，則在最前面補上0
	public String fillzero(String str,int n){
		String result = "";
		int length = str.length();
		for(int i=0;i<(n-length);i++){
			result += "0";
		}
		result += str;
		return result;
	}
	
	//傳入一個格式為[xxx,ooo,xxx]的字串，將其分開轉存成一個字串陣列
	public String[] getWorkerNames(String workers){
		return workers.split(",");
	}
}
