document.write("<script language='javascript' src='"+wwwHeadUrl+"/js/search-auto-complete.js'></script>");
//搜索提交
function submitSearch(){
	var keyWord = $.trim($("#keyWord").val());
	if(keyWord == ''){
		return;
	}else{
		$("#searchForm").submit();
	}
};

$(function(){
	//--------------------------------------搜索-------------------------------------------------
	//联想搜索-----------------------------------------------------------------
	var flag = false;
	var index = -1;
	var $searchList = $("#searchList");
	var timer;
	
 	$(document).on("keyup", "#keyWord", function(e){
 		var keyWord = $.trim($(this).val());
 		if(e.keyCode == 13){
 			if(index == -1){
 				return;
 			}
 			var $links = $searchList.find('a');
 			if($links.length){
 				$links.eq(index).trigger('mousedown');
 			}
 			return;
		}
 		else if(e.keyCode == 38 || e.keyCode == 40){
			var $links = $searchList.find('a');
			if(!$links.length){
				return;s
			}
			if(e.keyCode == 38){
				index--;
				if(index< 0){
					index = $links.length -1;
				}
			}
			else{
				index++;
				if(index >= $links.length){
					index = 0;
				}
			}
			$links.removeClass('selected').eq(index).addClass('selected');
		}
 		else{ 
			index = -1;
			if(timer){
				clearTimeout(timer);
			}
			
			if(keyWord.length){
				timer = setTimeout(function(){
					searchList(keyWord,wwwHeadUrl);
				}, 500);
			}
			else{
				$("#searchList").hide();
			}
		}
	}); 

	//联想搜索  - 点击搜索
 	$(document).on("mousedown", ".searchClick", function(){
		var keyWord = $.trim($(this).html());
		var searchType = $(this).attr("searchType");
		
		if(keyWord == ""){
			return false;
		}else{
			$("#keyWord").val(keyWord);
			$("#searchType").val(searchType);
			$("#searchForm").submit();
		}
	}); 
 	
	//隐藏联想搜索框
  	$("#keyWord").bind("blur",function(){
		$("#searchList").slideUp("slow");
	});
  	
 	$(document).on("keydown", "#keyWord", function(event){
 		var temp = event || window.event;
 		if(temp.keyCode == 13){     
 			submitSearch();
		}
	}); 
 	
 	$(document).on("keydown", "#searchForm", function(event){
 		var temp = event || window.event;
 		if(temp.keyCode == 13){     
 			return false;
		}
 	});
	
	//搜索
	$(document).on("submit", "#searchForm", function(){
		var keyWord = $.trim($("#keyWord").val());
		return !!keyWord.length;
	});
	
	//隐藏联想搜索框
  	$("#keyWord").bind("blur",function(){
		$("#searchList").slideUp("slow");
	}); 
});
