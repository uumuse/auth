$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");

function TrackPlayer(){
	this.init();
	this.collectMp3();
}

TrackPlayer.prototype = new KukePlayer();

TrackPlayer.prototype.show_loading = function(){
//	$(".tt-loading").show();
	$(".cd-play").hide();
	$(".cd-pause").hide();
}


TrackPlayer.prototype.show_playing = function (){
//	$(".tt-loading").hide();
	$(".cd-pause").show();
	$(".cd-play").hide();
}

TrackPlayer.prototype.show_pausing = function(){
	$(".cd-play").show();
	$(".cd-pause").hide();
}

TrackPlayer.prototype.collectMp3 = function(){
	
}

//TrackPlayer.prototype.progressBarReset = function(id){
//	$(".tt-scroll").find("span").css("width", "0%");
//}

//TrackPlayer.prototype.progressBarScroll = function(current_time, total_time){
//	$(".tt-scroll").find("span").css("width", current_time/total_time*100 + "%");
//}