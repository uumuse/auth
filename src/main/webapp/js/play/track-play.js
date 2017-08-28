$("head").append("<script type=\"text/javascript\" src=\"/js/play/KukePlayer.js\"></script>");
var curIndex = 0;

function TrackPlayer(){
	this.init();
	this.collectMp3();
}

TrackPlayer.prototype = new KukePlayer();


TrackPlayer.prototype.show_loading = function(id, prevId){
	$("#total").empty().append("00:00");
	$("#played").empty().append("00:00");
}

TrackPlayer.prototype.show_playing = function (id, prevId){
	if(!prevId){
		prevId = $(".song-info").find("dl:visible").attr("id");
	}
	$("dl#" + prevId).hide();
	$("dl#" + id).show();
	
	$(".song-play").hide();
	$(".song-pause").show();
}

TrackPlayer.prototype.show_pausing = function(id, prevId){
	$(".song-play").show();
	$(".song-pause").hide();
}

TrackPlayer.prototype.collectMp3 = function(id, prevId){
	var player = this;
	$(".track-info").each(function(i){
		$(this).attr("serial", i);
		var l_code = $(this).attr("l_code");
		var item_code = $(this).attr("item_code");
		
		var array = new Array();
		array.push(l_code);
		array.push(item_code);
		player.item_l_code_array.push(array);
		if(l_code === lcode){
			curIndex = player.item_l_code_array.length - 1;
		}
	});
	if(curIndex == 0){
		$(".track-info").first().parent().show();
	}
}

TrackPlayer.prototype.progressBarReset = function(id, prevId){
	
}

TrackPlayer.prototype.progressBarScroll = function(current_time, total_time){
	var totalTime = parseInt(total_time);
	var t_m = totalTime / 60;
	t_m = Math.floor(t_m);
	t_m = t_m + "";
	if(t_m.length < 2){
		t_m = "0" + t_m;
	}
	
	var t_s = totalTime % 60;
	t_s = t_s + "";
	if(t_s.length < 2){
		t_s = "0" + t_s;
	}
	
	$("#total").empty().append(t_m + ":" + t_s);
	
	var time = parseInt(current_time);
	var m = time / 60;
	m = Math.floor(m);
	m = "" + m;
	if(m.length < 2){
		m = "0" + m;
	}
	
	var s = time % 60;
	s = Math.ceil(s);
	s = s + "";
	if(s.length < 2){
		s = "0" + s;
	}
	var t = m + ":" + s;
	$("#played").empty().append(t);
}