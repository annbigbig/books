package back.controller.general.mail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import back.controller.base.BaseAction;
import back.model.Mail;

@SuppressWarnings("serial")
public class QueryMails extends BaseAction{
	//select * from mails,folders where mails.folderId=folders.id AND workerName='annbigbig' AND subject like '%tes%' limit 0,200;
	
	//mails資料表裡可以讓你搜尋的欄位名稱
	enum ColumnsName {
		id,_from,_to,cc,bcc,contentType,subject,sentDate,bodyMessage;
	}
	
	//input
	String searchColumn = "";	//搜尋的欄位名稱
	String searchValue = "";	//搜尋的值
	String orderColumn = "";	//排序的欄位名稱
	String orderType = "";		//排序方法
	int pageNumber = 1;			//頁碼
	int pageSize = 20;			//每頁顯示列數
	
	//output
	int totalRows = -1;			//符合條件的列數
	int totalPages = -1;		//總頁數
	List<Mail> mails = null;	//本頁的郵件列表
	
	public String execute(){
		debug();
		setWorker(wrapSessionWorker());
		mails = getMailDao().queryMails("mails,folders",getWhere(), getOrder(), getPageNumber(), getPageSize());
		return SUCCESS;
	}
	
	public void debug(){
		System.out.println("searchColumn=[" + searchColumn + "]");
		System.out.println("searchValue=[" + searchValue + "]");
		System.out.println("orderColumn=[" + orderColumn + "]");
		System.out.println("orderType=[" + orderType + "]");
	}
	
	public String getWhere(){
		String where = "where mails.folderId=folders.id AND workerName='" + getWorker().getName() + "'";
		if((searchColumn.length()>0)&&(searchValue.length()>0)){
			//避免SQL注入，剪短searchValue
			if(searchValue.length()>10){
				searchValue = searchValue.substring(0, 10);	//從index0拿10個字元
			}
			switch(ColumnsName.valueOf(searchColumn)){
				case id:
					where += " AND mails.id like '%" + searchValue + "%'";
					break;
				case _from:
					where += " AND _from like '%" + searchValue + "%'";
					break;
				case _to:
					where += " AND _to like '%" + searchValue + "%'";
					break;
				case cc:
					where += " AND cc like '%" + searchValue + "%'";
					break;
				case bcc:
					where += " AND bcc like '%" + searchValue + "%'";
					break;
				case contentType:
					where += " AND contentType like '%" + searchValue + "%'";
					break;
				case subject:
					where += " AND subject like '%" + searchValue + "%'";
					break;
				case sentDate:
					where += " AND sentDate like '%" + searchValue + "%'";
					break;
				case bodyMessage:
					where += " AND bodyMessage like '%" +  searchValue + "%'";
					break;
			}
		}
		System.out.println("[where]=" + where);
		return where;
	}
	
	//SELECT * FROM mails where id<99 order by id asc LIMIT 0,100
	public String getOrder(){
		String order = "";
		if(orderColumn.length()>0 && orderType.length()>0){
			switch(ColumnsName.valueOf(orderColumn)){
				case id:
					order += "order by mails.id ";
					break;
				case _from:
					order += "order by _from ";
					break;
				case _to:
					order += "order by _to ";
					break;
				case cc:
					order += "order by cc ";
					break;
				case bcc:
					order += "order by bcc ";
					break;
				case contentType:
					order += "order by contentType ";
					break;
				case subject:
					order += "order by subject ";
					break;
				case sentDate:
					order += "order by sentDate ";
					break;
				case bodyMessage:
					order += "order by bodyMessage ";
			}
			if(orderType.equals("desc")){
				order += "desc";
			}else{
				order += "asc";
			}
		}
		//預設值以郵件id作升冪排序
		if(order.length()==0){
			order = "order by mails.id asc";
		}
		System.out.println("[order]=" + order);
		return order;
	}
	
	public String getLinkButtons(){
		String linkButtons = "";
		//根目錄[/annbookstore]
		String contextPath = ServletActionContext.getRequest().getContextPath();
		String nameSpace = "/back/general";
		String actionName = "/queryMails.action";
		boolean hasSearchColumn = (searchColumn.length()>0);
		boolean hasSearchValue = (searchValue.length()>0);
		String paramSearchColumn = "&searchColumn=" + searchColumn;
		String paramSearchValue = "&searchValue=" + searchValue;
		boolean hasOrderColumn = (orderColumn.length()>0);
		boolean hasOrderType = ((orderType.equals("desc"))||(orderType.equals("asc")));
		String paramOrderColumn = "&orderColumn=" + orderColumn;
		String paramOrderType = "&orderType=" + orderType;
		
		//第一頁
		if(pageNumber<=1){
			linkButtons += "[第一頁]";
		}else{
			linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=1";
			linkButtons += (hasSearchColumn ? paramSearchColumn : "");
			linkButtons += (hasSearchValue ? paramSearchValue : "");
			linkButtons += (hasOrderColumn ? paramOrderColumn : "");
			linkButtons += (hasOrderType ? paramOrderType : "");
			linkButtons += "\">[第一頁]</a>";
		}
		
		//上十頁
		if(pageNumber<=10){
			linkButtons += "[上十頁]";
		}else{
			linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=" + (pageNumber-10);
			linkButtons += (hasSearchColumn ? paramSearchColumn : "");
			linkButtons += (hasSearchValue ? paramSearchValue : "");
			linkButtons += (hasOrderColumn ? paramOrderColumn : "");
			linkButtons += (hasOrderType ? paramOrderType : "");
			linkButtons += "\">[上十頁]</a>";
		}
		
		//上一頁
		if(pageNumber<=1){
			linkButtons += "[上一頁]";
		}else{
			linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=" + (pageNumber-1);
			linkButtons += (hasSearchColumn ? paramSearchColumn : "");
			linkButtons += (hasSearchValue ? paramSearchValue : "");
			linkButtons += (hasOrderColumn ? paramOrderColumn : "");
			linkButtons += (hasOrderType ? paramOrderType : "");
			linkButtons += "\">[上一頁]</a>";
		}
		
		//頁碼按鈕 1 [2] 3 4 5 6 7 8 9 10
		//先找出數列最左邊的數字
		int startIndex = 0;
		if(pageNumber%10==0){
			startIndex = pageNumber-9;
		}else{
			startIndex = (pageNumber-(pageNumber%10))+1;
		}
		if(startIndex<1) startIndex = 1;
		for(int i=startIndex;i<startIndex+10;i++){
			if(i>totalPages) break;
			if(i==pageNumber){
				linkButtons += "[" + i + "]";
			}else{
				linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=" + i;
				linkButtons += (hasSearchColumn ? paramSearchColumn : "");
				linkButtons += (hasSearchValue ? paramSearchValue : "");
				linkButtons += (hasOrderColumn ? paramOrderColumn : "");
				linkButtons += (hasOrderType ? paramOrderType : "");
				linkButtons += "\">[" + i + "]</a>";
			}
		}
		
		//下一頁
		if(pageNumber>=totalPages){
			linkButtons += "[下一頁]";
		}else{
			linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=" + (pageNumber+1);
			linkButtons += (hasSearchColumn ? paramSearchColumn : "");
			linkButtons += (hasSearchValue ? paramSearchValue : "");
			linkButtons += (hasOrderColumn ? paramOrderColumn : "");
			linkButtons += (hasOrderType ? paramOrderType : "");
			linkButtons += "\">[下一頁]</a>";
		}
		
		//下十頁
		if(pageNumber+10>totalPages){
			linkButtons += "[下十頁]";
		}else{
			linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=" + (pageNumber+10);
			linkButtons += (hasSearchColumn ? paramSearchColumn : "");
			linkButtons += (hasSearchValue ? paramSearchValue : "");
			linkButtons += (hasOrderColumn ? paramOrderColumn : "");
			linkButtons += (hasOrderType ? paramOrderType : "");
			linkButtons += "\">[下十頁]</a>";
		}
		
		//最後頁
		if(pageNumber>=totalPages){
			linkButtons += "[最後頁]";
		}else{
			linkButtons += "<a href=\"" + contextPath + nameSpace + actionName + "?pageNumber=" + totalPages;
			linkButtons += (hasSearchColumn ? paramSearchColumn : "");
			linkButtons += (hasSearchValue ? paramSearchValue : "");
			linkButtons += (hasOrderColumn ? paramOrderColumn : "");
			linkButtons += (hasOrderType ? paramOrderType : "");
			linkButtons += "\">[最後頁]</a>";
		}
		
		//[目前頁碼/總頁數]
		linkButtons += "[" + pageNumber + "/" + totalPages + "]";
		
		return linkButtons;
	}
	
	public String getSearchColumn() {
		return searchColumn;
	}
	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public int getPageNumber() {
		//再次確認頁面在合理的範圍內
		if(pageNumber<1){
			pageNumber = 1;
		}
		//
		int pages = getTotalPages();
		if(pageNumber>pages){
			pageNumber = pages;
		}
		System.out.println("[pageNumber]=" + pageNumber);
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		System.out.println("[pageSize]=" + pageSize);
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	//總列數要調用MailDao才知道
	public int getTotalRows() {
		if(totalRows<0){
			totalRows = getMailDao().countTotalRows("mails,folders",getWhere());
		}
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	//總頁數是由總列數計算而來
	public int getTotalPages() {
		int rows = getTotalRows();
		if(totalPages<0){
			if(rows%pageSize==0){
				//整除
				totalPages = (rows / pageSize);
			}else{
				//有餘數
				totalPages = (rows / pageSize) + 1;
			}
		}
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<Mail> getMails() {
		return mails;
	}
	public void setMails(List<Mail> mails) {
		this.mails = mails;
	}
	
}
