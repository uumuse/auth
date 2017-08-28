$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");

function RecordPlayer(){
	this.init();
	this.collectMp3();
}

RecordPlayer.prototype = new KukePlayer();

RecordPlayer.prototype.show_loading = function(id, prevId){
	
}


RecordPlayer.prototype.show_playing = function (id, prevId){
	$(".mh-pic-play").hide();
	$(".mh-pic-pause").show();
	if(prevId){
		$("#play-" + prevId).show();
		$("#pause-" + prevId).hide();
	}
	$("#play-" + id).hide();
	$("#pause-" + id).show();
}

RecordPlayer.prototype.show_pausing = function(id, prevId){
	$(".mh-pic-play").show();
	$(".mh-pic-pause").hide();
	$("#play-" + id).show();
	$("#pause-" + id).hide();
}

RecordPlayer.prototype.collectMp3 = function(){
	var player = this;
	$(".mh-playlist").each(function(i){
		$(this).find(".play-contr").attr("serial", i);
		var l_code = $(this).attr("l_code");
		var item_code = $(this).attr("item_code");
		$(this).find(".mh-pro").find("em").append((i+1) + ".");
		
		var array = new Array();
		array.push(l_code);
		array.push(item_code);
		player.item_l_code_array.push(array);
	});
}

RecordPlayer.prototype.progressBarReset = function(id, prevId){
	
}

RecordPlayer.prototype.progressBarScroll = function(current_time, total_time){
	
}