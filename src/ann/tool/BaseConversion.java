package ann.tool;

public class BaseConversion {
	private static BaseConversion instance = null;
	public static BaseConversion getInstance(){
		if(instance==null) instance = new BaseConversion();
		return instance;
	}
	
	//十進位轉成二進位之後，第offset個字元是否為1，注意:offset從1開始
	public boolean isOne(int decimalValue,int offset,boolean showDebugMessage){
		boolean result = false;
		String binaryStr = binaryString(decimalValue,showDebugMessage);
		
		//確定offset的值在合理範圍，不能超過二進位字串的長度，也不能小於1
		if(offset<1){
			offset=1;
		}
		if(offset>binaryStr.length()){
			offset = binaryStr.length();
		}
		if(binaryStr.substring(offset-1, offset).equals("1")){
			result = true;
		}
		//debug
		if(showDebugMessage){
			String msg = "[BaseConversion.isOne()] ";
			msg += "decimalValue=" + decimalValue;
			msg += "\t binaryStr=" + binaryStr;
			msg += "\t 二進制字串的第" + offset + "個字是" + (result ? "1":"0");
			System.out.println(msg);
		}
		return result;
	}
	
	//傳入一個十進位整數，返回二進制字串(注意:只處理0-255之間的十進位整數)
	public String binaryString(int decimalValue,boolean showDebugMessage){
		String tempStr = "";
		String binaryString = "";
		//確認傳入的十進位整數在合理範圍
		if(decimalValue<0){
			decimalValue = 0;
		}
		if(decimalValue>255){
			decimalValue = 255;
		}
		
		int q = decimalValue;
		for(int i=0;i<8;i++){
			int r = q%2;
			tempStr += r;
			q=q/2;
		}
		//此時產生的tempStr是反過來的順序，要將它倒著寫才是正確結果
		for(int i=tempStr.length()-1;i>=0;i--){
			binaryString += tempStr.substring(i, i+1);
		}
		
		//debug
		if(showDebugMessage){
			String msg = "[BaseConversion.binaryString()] ";
			msg += "decimalValue=" + decimalValue;
			msg += "\t tempStr=" + tempStr;
			msg += "\t binaryString=" + binaryString;
			System.out.println(msg);
		}
		return binaryString;
	}
}
