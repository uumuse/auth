$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");

function appTrackPlayer(){
	this.init();
	this.collectMp3();
}

appTrackPlayer.prototype = new KukePlayer();

appTrackPlayer.prototype.show_loading = function(){
//	$(".tt-loading").show();
//	$(".tt-play").hide();
//	$(".tt-pause").hide();
}

appTrackPlayer.prototype.show_playing = function (){
	$("#pause").hide();
	$("#play").show();
}

appTrackPlayer.prototype.show_pausing = function(){
	$("#pause").show();
	$("#play").hide();
}

appTrackPlayer.prototype.unUseedUrl = function(){
	tips("分享的歌曲已过期");
}

appTrackPlayer.prototype.collectMp3 = function(){
	
}

appTrackPlayer.prototype.progressBarReset = function(id){
	$(".comSt08").css("left","0%");
	$("#starttime").html("00:00");
	$("#endtime").html($("#timing").val());
}

appTrackPlayer.prototype.progressBarScroll = function(current_time, total_time){
	$(".comSt08").css("left",current_time/total_time*100 + "%");
	$("#starttime").html(getTheTime(current_time));
	$("#endtime").html(getTheTime(total_time-current_time));
}

function getTheTime(time){
	var num = parseInt(time);
	if(num >= 60){
		return getFormatNum(Math.floor(num/60))+":"+getFormatNum(num%60);
	}else{
		return "00:"+getFormatNum(num);
	}
}
function getFormatNum(num){
	if(num <= 9){
		return "0"+num;
	}else{
		return num;
	}
}