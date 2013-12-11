<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查詢郵件[成功頁]</title>
<script src="<%=request.getContextPath()%>/jquery/jquery-1.9.1.js"></script>
<script src="<%=request.getContextPath()%>/jquery/jquery-ui-1.10.3.custom.js"></script>
<script src="<%=request.getContextPath()%>/jquery/jquery.validate.js"></script>
<script>
$.validator.setDefaults();
  
$().ready(function() {
	// validate signup form on keyup and submit
	$("#pageNumberForm").validate({
		rules: {
			pageNumber: {
				required: true,
				range: [1,<s:property value="totalPages"/>]
			}
		},
		messages: {
			pageNumber: {
				required: "請輸入頁碼",
				range: "請輸入{0}和{1}之間的值"
			}
		}
	});
	
	//定義進度條
	var progressbar = $( "#progressbar" ),
    progressLabel = $( ".progress-label" );

  progressbar.progressbar({
    value: false,
    change: function() {
      progressLabel.text( progressbar.progressbar( "value" ) + "%" );
    },
    complete: function() {
      progressLabel.text( "Complete!" );
    }
  });

  function progress() {
		var val = progressbar.progressbar( "value" ) || 0;
	    progressbar.progressbar( "value", val + 1 );

	    if ( val < 99 ) {
	      setTimeout( progress, 100 );
	    }
  }

	
	
	$(".showMail").click(function(e){
		//禁止超連結的預設行為
		e.preventDefault();
		
		//啟用進度條
	  progressbar.progressbar("value",0);
	  setTimeout( progress, 1000 );
		
		//a標簽的父節點的父節點裡找第一個td包著的文字
		var mailId = $(this).parent().parent().find("td:first-child").text();
	   	$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/back/general/mailXML.action",
			  data: { mail_id: mailId},
			  success: function(xml){
				  var id = $(xml).find("Mail:first").find("id:first").text();
				  var from = $(xml).find("Mail:first").find("from").text();
				  var to = $(xml).find("Mail:first").find("to").text();
				  var cc = $(xml).find("Mail:first").find("cc").text();
				  var bcc = $(xml).find("Mail:first").find("bcc").text();
				  var contentType = $(xml).find("Mail:first").find("contentType").text();
				  var subject = $(xml).find("Mail:first").find("subject").text();
				  var sentDate = $(xml).find("Mail:first").find("sentDate").text();
				  
				  $("#id").text(id);	//注意html()和text()方法的微妙差別，後者text()方法不會把這個<ka.shu@e-benk.com.tw>當成HTML來解析
				  $("#from").text(from);
				  $("#to").text(to);
				  $("#cc").text(cc);
				  $("#bcc").text(bcc);
				  $("#contentType").text(contentType);
				  $("#subject").text(subject);
				  $("#sentDate").text(sentDate);
				  
				  $("#attachments").empty();
				  $(xml).find("Attachment").each(function(){
					  var attId = $(this).find("id").text();
					  var filename = $(this).find("filename").text();
					  $('<a>',{
						    text: filename,
						    title: '[' + filename + ']',
						    href: '<%=request.getContextPath()%>/back/general/download?id='+attId
						}).appendTo('#attachments');
					  $("<br>").appendTo("#attachments");
					  //alert('attId=' + attId + "  filename=" + filename);
				  });
				  //停用進度條
				  progressbar.progressbar("value",99);
				  
			  },
			  error: function(xhr,textStatus,errorThrown){
				  alert(textStatus);
			  },
			  dataType: "xml"
		});
	   	$("#bodyMessage").load("<%=request.getContextPath()%>/back/general/bodyMessage.action",{mail_id:mailId});
	});
	
	$(".xxxshowMail").click(function(e){
		e.preventDefault();
		//a標簽的父節點的父節點裡找第一個td包著的文字
		var mailId = $(this).parent().parent().find("td:first-child").text();
		var iframe = $("#myFrame");
		$(iframe).attr("src","<%=request.getContextPath()%>/back/general/bodyMessage?mail_id=" + mailId);
	});
	
	//$("#phone").mask("9999-999-999");
	//預設顯示第一封信的內容，觸發本頁裡第一個<a class='showMail'...的超連結
	$("a.showMail:first").trigger("click");
	
});
</script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/standard.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ui-lightness/jquery-ui-1.10.3.custom.css"/>
<style>
  .ui-progressbar {
    position: relative;
  }
  .progress-label {
    position: absolute;
    left: 50%;
    top: 4px;
    font-weight: bold;
    text-shadow: 1px 1px 0 #fff;
  }
  </style>
</head>
<body>
    <div id="wrapper">
        <div id="headerwrap">
        	<div id="header">
            	<p><h1>安安線上書局後台管理系統</h1></p>
        	</div>
        </div>

        <div id="navigationwrap">
        	<div id="navigation">
            	<p>
            		後台首頁&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		<s:url id="misIndex" namespace="/back/mis" action="index" method="execute"/>
            		<s:a href="%{misIndex}">資訊部</s:a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		<a href="<%=request.getContextPath()%>/back/sales/index!execute">銷售部</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		倉管部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		採購部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		物流部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		客服部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		財務部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	</p>
        	</div>
        </div>

        <div id="contentliquid">
			<div id="contentwrap">
        		<div id="content_up">
					<p>
							<s:if test="%{mails!=null}">
								<!-- 共取出了<s:property value="mails.size()"/>封信件<br>　-->
								<table>
									<!-- 排序按鈕設定 -->
									<s:url var="ascId" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">id</s:param>
										<s:param name="orderType">asc</s:param>
									</s:url>
									<s:url var="descId" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">id</s:param>
										<s:param name="orderType">desc</s:param>
									</s:url>
									<s:url var="ascFrom" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">_from</s:param>
										<s:param name="orderType">asc</s:param>
									</s:url>
									<s:url var="descFrom" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">_from</s:param>
										<s:param name="orderType">desc</s:param>
									</s:url>
									<s:url var="ascSubject" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">subject</s:param>
										<s:param name="orderType">asc</s:param>
									</s:url>
									<s:url var="descSubject" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">subject</s:param>
										<s:param name="orderType">desc</s:param>
									</s:url>
									<s:url var="ascSentDate" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">sentDate</s:param>
										<s:param name="orderType">asc</s:param>
									</s:url>
									<s:url var="descSentDate" namespace="/back/general" action="queryMails.action">
										<s:param name="searchColumn">${searchColumn}</s:param>
										<s:param name="searchValue">${searchValue}</s:param>
										<s:param name="orderColumn">sentDate</s:param>
										<s:param name="orderType">desc</s:param>
									</s:url>					
									<tr>
										<th>
											<s:if test="%{orderColumn=='id' && orderType=='asc'}">▲</s:if>
											<s:else><s:a href="%{ascId}">△</s:a></s:else>
											id
											<s:if test="%{orderColumn=='id' && orderType=='desc'}">▼</s:if>
											<s:else><s:a href="%{descId}">▽</s:a></s:else>
										</th>
										<th>
											<s:if test="%{orderColumn=='_from' && orderType=='asc'}">▲</s:if>
											<s:else><s:a href="%{ascFrom}">△</s:a></s:else>										
											from
											<s:if test="%{orderColumn=='_from' && orderType=='desc'}">▼</s:if>
											<s:else><s:a href="%{descFrom}">▽</s:a></s:else>
										</th>
										<th>
											<s:if test="%{orderColumn=='subject' && orderType=='asc'}">▲</s:if>
											<s:else><s:a href="%{ascSubject}">△</s:a></s:else>
											subject
											<s:if test="%{orderColumn=='subject' && orderType=='desc'}">▼</s:if>
											<s:else><s:a href="%{descSubject}">▽</s:a></s:else>
										</th>
										<th>
											<s:if test="%{orderColumn=='sentDate' && orderType=='asc'}">▲</s:if>
											<s:else><s:a href="%{ascSentDate}">△</s:a></s:else>
											sentDate
											<s:if test="%{orderColumn=='sentDate' && orderType=='desc'}">▼</s:if>
											<s:else><s:a href="%{descSentDate}">▽</s:a></s:else>
										</th>
									</tr>
								<s:iterator value="mails" status="mail">
									<tr>
										<td>
											<s:property value="id"/>
										</td>
										<td>
											<s:property value="from"/>
										</td>
										<td>
											<s:url namespace="/back/general" action="myXML" var="myurl"/>
											<s:a href="%{myurl}" cssClass="showMail">
												<s:property value="subject"/>
											</s:a>
										</td>
										<td>
											<s:property value="sentDate"/>
										</td>
									</tr>
								</s:iterator>
								</table>
								<!-- 頁碼按鈕 -->
								<s:property value="linkButtons" escape="false"/>
								<form id="pageNumberForm" action="<%=request.getContextPath()%>/back/general/queryMails.action" method="post" style="margin:0px;display: inline">
									跳到<input name="pageNumber" type="text" value="${pageNumber}" size="3" maxlength="3"/>
									<input type="hidden" name="searchColumn" value="${searchColumn}"/>
									<input type="hidden" name="searchValue" value="${searchValue}"/>
									<input type="hidden" name="orderColumn" value="${orderColumn}"/>
									<input type="hidden" name="orderType" value="${orderType}"/>
									<input type="submit" value="頁"/>
								</form>
								<!-- 搜尋表單 -->
								<form id="searchForm" action="<%=request.getContextPath()%>/back/general/queryMails.action" method="post">
									欄位:
									<select name="searchColumn">
										<option value="" <s:if test="%{searchColumn==null || searchColumn==''}"> selected</s:if>>請選擇要搜尋的欄位...</option>
										<option value="id"<s:if test="%{searchColumn=='id'}"> selected</s:if>>郵件編號[id]</option>
										<option value="_from"<s:if test="%{searchColumn=='_from'}"> selected</s:if>>寄件人[_from]</option>
										<option value="_to"<s:if test="%{searchColumn=='_to'}"> selected</s:if>>收件人[_to]</option>
										<option value="cc"<s:if test="%{searchColumn=='cc'}"> selected</s:if>>副本[cc]</option>
										<option value="bcc"<s:if test="%{searchColumn=='bcc'}"> selected</s:if>>密件副本[bcc]</option>
										<option value="subject"<s:if test="%{searchColumn=='subject'}"> selected</s:if>>信件主旨[subject]</option>
										<option value="sentDate"<s:if test="%{searchColumn=='sentDate'}"> selected</s:if>>寄達時間[sentDate]</option>
										<option value="bodyMessage"<s:if test="%{searchColumn=='bodyMessage'}"> selected</s:if>>信件內容[bodyMessage]</option>
									</select>
									<input name="searchValue" type="text" size="10" maxlength="10" value="${searchValue}">
									<input type="submit" value="搜尋信件">
								</form>
							</s:if>
							<s:else>
								收件夾裡沒有任何信件
							</s:else>
					</p>
        		</div>
        		<!-- 內容[下] -->
				<div id="content_down">
					<!-- <iframe id="myFrame" width="600" height="50"></iframe> -->
					<!-- 進度條 -->
					<div id="progressbar"><div class="progress-label">Loading...</div></div>
					<ul>
						<li id="id"></li>
						<li id="from"></li>
						<li id="to"></li>
						<li id="cc"></li>
						<li id="bcc"></li>
						<li id="contentType"></li>
						<li id="subject"></li>
						<li id="sentDate"></li>
					</ul>
					<div id="bodyMessage"></div>
					<span id="attachments"></span>
				</div>
			</div>
		</div>

        <div id="leftcolumnwrap">
        	<div id="left_up">
				<p>
					<a href="<%=request.getContextPath()%>/back/general/queryMails.action">收件夾
				<s:if test="%{worker!=null && worker.inbox!=null}">
					(<s:property value="worker.inbox.mailsCount"/>)
				</s:if>
					</a>
				</p>
            	<p>您好:
            	<s:if test="%{worker!=null && worker.name!=null}">
            		<s:property value="worker.name"/>
            	</s:if>
            	<s:else>
            		訪客
            	</s:else>
            	<s:a namespace="/back" href="logout">(登出)</s:a>
            	</p>
        	</div>
			<div id="left_down">
				<p>Click Me</p>
				<p>Fxxk Me</p>
				<p>oh yes</p>
				<p><a href="<%=request.getContextPath()%>/back/general/takeLongTime.action">測試execAndWait</a></p>
			</div>
        </div>

        <div id="footerwrap">
        	<div id="footer">
            	<p>This is the Footer</p>
        	</div>
        </div>
    </div>
</body>
</html>