package com.kuke.core.base;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import com.kuke.util.PageInfo;

public class PageTag extends TagSupport{
	private static final long serialVersionUID = -8211255562263213862L;
	private Integer dist = 5;
	private PageInfo page;
	private String action;
	private String formId;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			int currentPage = page.getCurrentPage();//当前页
			int pageCount = page.getPageCount();//总共几页
			int pageSize = page.getPageSize();//每页数量
			
			StringBuilder pagingHtml = new StringBuilder();
			
			pagingHtml.append("<div class=\"comSite81 c\">");
			if(pageCount != 1){
				if(currentPage == 1){
					pagingHtml.append("<div class=\"comSite82 fl\">");
				}else{
					pagingHtml.append("<a href=\"javascript:;\" id=\"prevPage\" class=\"comSt69 fl\"></a>");
					pagingHtml.append("<div class=\"comSite82 fl\">");
					pagingHtml.append("<a href=\"javascript:;\" id=\"firstPage\">首页</a>");
				}
				
				int half = dist/2;
				int startIndex = currentPage > half? (currentPage - half) : 1;
				int endIndex = startIndex + dist-1 > pageCount? pageCount : startIndex + dist-1;
				if(endIndex - startIndex < dist-1){
					startIndex = endIndex - (dist-1) > 0? endIndex - (dist-1) : 1;
				}
				if(currentPage > half + 1){
					pagingHtml.append("<em>...</em>");
				}
				
				for(int i=startIndex; i <= endIndex; i++){
					if(currentPage == i){
						pagingHtml.append("<a id='"+i+"' class='comOn10'>" + i + "</a>");
					}else{
						pagingHtml.append("<a href=\"javascript:void(0)\" id='"+i+"' class='paging'>" + i + "</a>");
					}
				}
				
				if(endIndex < pageCount){
					pagingHtml.append("<em>...</em>");
				}
				
				if((currentPage == pageCount) || pageCount == 0){
					pagingHtml.append("</div>");
				}
				else{
					pagingHtml.append("<a href=\"javascript:;\" id=\"lastPage\">末页</a>");
					pagingHtml.append("</div>");
					pagingHtml.append("<span id=\"nextPage\" class=\"comSt69 comSt70 fl\"></span>");
				}
			}
			pagingHtml.append("</div>\n");
			//pagingHtml.append("<p class=\"page-info\">共计<span>" + totalRow + "</span>条记录，本页显示<span>" + pageSize + "</span>条</p>");
			//pagingHtml.append("</div>\n");
			
			if(StringUtils.isBlank(formId)){
				pagingHtml.append("<form id=\"pagingForm\" action=\""+action+"\" method=\"post\">\n");
				pagingHtml.append("	<input type=\"hidden\" id=\"pSizeInput\" name=\"pageSize\" value=\""+pageSize+"\"/>\n");
				pagingHtml.append("	<input type=\"hidden\" id=\"pageNo\" name=\"currentPage\" value=\"\"/>\n");
				pagingHtml.append("</form>\n");
				formId = "pagingForm";
			}
			
			pagingHtml.append("<script type=\"text/javascript\">                                           \n");
			pagingHtml.append("$(document).ready(function(){                                               \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 var pageSize = $(\"<input type=\\\"hidden\\\" id=\\\"pageSize\\\" name=\\\"pageSize\\\" value=\\\""+pageSize+"\\\"/>\" ); \n");
			pagingHtml.append("	 form.append(pageSize);                                                    \n");
			
			pagingHtml.append("$(\".paging\").one(\"click\",function(){                                    \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 var indexVal = $(this).attr(\"id\");                                      \n");
			pagingHtml.append("	 var index = $(\"<input type=\\\"hidden\\\" name=\\\"currentPage\\\"/>\");      \n");
			pagingHtml.append("	 index.attr(\"value\",indexVal);                                           \n");
			pagingHtml.append("	 form.append(index);                                                       \n");
			pagingHtml.append("	 form.submit();                                                            \n");
			pagingHtml.append("});                                                                         \n");
			
			pagingHtml.append("$(\".pageSize\").one(\"click\", function(){                              \n");
			pagingHtml.append("	 var value=$(this).attr(\"id\");                                        \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 form.find(\"#pageSize\").val(value)                                       \n");
			pagingHtml.append("	 var index = $(\"<input type=\\\"hidden\\\" name=\\\"currentPage\\\" value=\\\"1\\\"/>\");      \n");
			pagingHtml.append("	 form.append(index);                                                       \n");
			pagingHtml.append("	 form.submit();                                                            \n");
			pagingHtml.append("});                                                                         \n");
			
			pagingHtml.append("$(\"#firstPage\").one(\"click\",function(){                                    \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 var index = $(\"<input type=\\\"hidden\\\" name=\\\"currentPage\\\" value=\\\""+ 1 +"\\\"/>\");      \n");
			pagingHtml.append("	 form.append(index);                                                       \n");
			pagingHtml.append("	 form.submit();                                                            \n");
			pagingHtml.append("});                                                                         \n");
			
			int prevPage = currentPage > 1? currentPage - 1 : currentPage;
			pagingHtml.append("$(\"#prevPage\").one(\"click\",function(){                                    \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 var index = $(\"<input type=\\\"hidden\\\" name=\\\"currentPage\\\" value=\\\""+ prevPage +"\\\"/>\");      \n");
			pagingHtml.append("	 form.append(index);                                                       \n");
			pagingHtml.append("	 form.submit();                                                            \n");
			pagingHtml.append("});                                                                         \n");
			
			int nextPage = currentPage < pageCount? currentPage + 1 : currentPage;
			pagingHtml.append("$(\"#nextPage\").one(\"click\",function(){                                    \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 var index = $(\"<input type=\\\"hidden\\\" name=\\\"currentPage\\\" value=\\\""+ nextPage +"\\\"/>\");      \n");
			pagingHtml.append("	 form.append(index);                                                       \n");
			pagingHtml.append("	 form.submit();                                                            \n");
			pagingHtml.append("});                                                                         \n");
			
			pagingHtml.append("$(\"#lastPage\").one(\"click\",function(){                                    \n");
			pagingHtml.append("	 var form = $(\"#"+this.formId+"\");                                       \n");
			pagingHtml.append("	 var index = $(\"<input type=\\\"hidden\\\" name=\\\"currentPage\\\" value=\\\""+ pageCount +"\\\"/>\");      \n");
			pagingHtml.append("	 form.append(index);                                                       \n");
			pagingHtml.append("	 form.submit();                                                            \n");
			pagingHtml.append("});                                                                         \n");
			
			pagingHtml.append(" });                                                                        \n");
			pagingHtml.append("</script>                                                                   \n");
			
			
			JspWriter writer = this.pageContext.getOut();
			writer.write(pagingHtml.toString());
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.page = null;
		this.action = null;
		this.formId = null;
	}

	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
	
}
