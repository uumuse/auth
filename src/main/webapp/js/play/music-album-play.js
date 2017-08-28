$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");

function MusicAlbumPlayer(){
	this.init();
	this.collectMp3();
}

MusicAlbumPlayer.prototype = new KukePlayer();

MusicAlbumPlayer.prototype.show_loading = function(id, prevId){
	
}


MusicAlbumPlayer.prototype.show_playing = function (id, prevId){
	$(".kp-play").hide();
	$(".kp-pause").show();
	if(prevId){
		$("#cur-" + prevId).hide();
		$("#play-" + prevId).show();
		$("#pause-" + prevId).hide();
	}
	$("#cur-" + id).show();
	$("#play-" + id).hide();
	$("#pause-" + id).show();
}

MusicAlbumPlayer.prototype.show_pausing = function(id, prevId){
	$(".kp-play").show();
	$(".kp-pause").hide();
	$("#play-" + id).show();
	$("#pause-" + id).hide();
}

MusicAlbumPlayer.prototype.collectMp3 = function(){
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
MusicAlbumPlayer.prototype.progressBarReset = function(id){
	
}

MusicAlbumPlayer.prototype.progressBarScroll = function(current_time, total_time){
	
}