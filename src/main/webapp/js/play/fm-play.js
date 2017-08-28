$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");

function FmPlayer(){
	this.init();
	this.collectMp3();
}

FmPlayer.prototype = new KukePlayer();

FmPlayer.prototype.show_loading = function(id, prevId){
	
}

FmPlayer.prototype.show_playing = function (id, prevId){
	$("#mh-playlist-" + prevId).hide();
	$("#mh-playlist-" + id).show();
	
	$(".mh-pic-play").hide();
	$(".mh-pic-pause").show();
	
	if(prevId){
		$("#play-" + prevId).show();
		$("#pause-" + prevId).hide();
	}
	$("#play-" + id).hide();
	$("#pause-" + id).show();
}

FmPlayer.prototype.show_pausing = function(id, prevId){
	$(".mh-pic-play").show();
	$(".mh-pic-pause").hide();
	$("#play-" + id).show();
	$("#pause-" + id).hide();
}


FmPlayer.prototype.collectMp3 = function(){
	var player = this;
	$(".mh-playlist").each(function(i){
		$(this).find(".play-contr").attr("serial", i);
		var l_code = $(this).attr("l_code");
		var item_code = $(this).attr("item_code");
		
		var array = new Array();
		array.push(l_code);
		array.push(item_code);
		player.item_l_code_array.push(array);
	});
}

FmPlayer.prototype.progressBarReset = function(id, prevId){
	
}

FmPlayer.prototype.progressBarScroll = function(current_time, total_time){
	
}