package ann.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {
	private static RegexTool instance = null;
	public static RegexTool getInstance(){
		if(instance==null) instance = new RegexTool();
		return instance;
	}
	
	//傳入的text是否與樣式patternStr符合？
	//例:text:[/annbookstore/back/mis/index.action]
	//patternStr:[.*/back/mis/.*]
	public boolean match(String text,String patternStr){
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

	//傳入一個HTML標簽，例[<div id='hey'></div>]
	//返回:[&lt;div id='hey'&gt;&lt;/div&gt;]
	public String escapeHtml(String original){
		String escStr = original; 
		escStr = escStr.replaceAll("[<]", "&lt;");
		escStr = escStr.replaceAll("[>]", "&gt;");
		return escStr;
	}
}
