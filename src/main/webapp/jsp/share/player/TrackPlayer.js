$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");

function TrackPlayer(){
	this.init();
	this.collectMp3();
}

TrackPlayer.prototype = new KukePlayer();

TrackPlayer.prototype.show_loading = function(){
//	$(".tt-loading").show();
//	$(".tt-play").hide();
//	$(".tt-pause").hide();
}

TrackPlayer.prototype.show_playing = function (){
	$("#playbutn").hide();
	$("#pausebutn").show();
}

TrackPlayer.prototype.show_pausing = function(){
	$("#playbutn").show();
	$("#pausebutn").hide();
}

TrackPlayer.prototype.collectMp3 = function(){
	
}

TrackPlayer.prototype.unUseedUrl = function(){
	$(".comTcBg").show();
	$(".comSite602").show();
}

TrackPlayer.prototype.progressBarReset = function(id){
//	$(".comSt08").css("left","0%");
//	$("#starttime").html("00:00");
//	$("#endtime").html($("#timing").val());
}

TrackPlayer.prototype.progressBarScroll = function(current_time, total_time){
//	$(".comSt08").css("left",current_time/total_time*100 + "%");
//	$("#starttime").html(getTheTime(current_time));
//	$("#endtime").html(getTheTime(total_time-current_time));
}