domain="kuke.com";
function KukePlayer(){
	this.audio;
	this.s_l_code;
	this.prev_s_l_code;
	this.item_l_code_array = new Array();
	this.current_i = -1;
}


KukePlayer.prototype.init = function(playerId){
	if(!playerId){
		playerId = "playerxEnvXYXWHbCc";
	}
	var player = this;
	this.audio = document.getElementById(playerId);
	this.audio.autoplay = false;
	this.audio.isLoadedmetadata = false;
	this.audio.touchstart = false;
	this.audio.audio = true;
	
	this.audio.addEventListener("loadstart", function(){
		if(player.s_l_code){
			player.show_loading(player.s_l_code, player.prev_s_l_code);
		}
	},false);
	
	this.audio.addEventListener("play",function(){
		player.show_playing(player.s_l_code, player.prev_s_l_code);
	},false);
	
	this.audio.addEventListener("playing",function(){
		player.show_playing(player.s_l_code, player.prev_s_l_code);
	},false);
	
	this.audio.addEventListener("timeupdate", function(){
		var total_time = player.audio.duration;
		var current_time = player.audio.currentTime;
		if(total_time>0){
			player.progressBarScroll(current_time, total_time);
	  	}
	},false);
	
	this.audio.addEventListener("ended",function(){
		player.next_play();
	},false);
}

KukePlayer.play = function(player, i){
//	debugger;
	player.current_i = i;
	var item = player.item_l_code_array[player.current_i];
	var l_code = item[0];
	var item_code = item[1];
	var c = item[2];//分享时的长整型时间
	
	player.show_loading(l_code);
	
	player.setMedia(l_code, item_code,c,function(info){
		if(info.flag == true){
			var play_con=info.data;
			player.audio.src = play_con.url;
			player.audio.play();
			
			player.prev_s_l_code = player.s_l_code;
			player.s_l_code = l_code;
			
			player.progressBarReset(player.prev_s_l_code);
		}else{
			player.unUseedUrl();
		}
	});
}

KukePlayer.prototype.setMedia = function(l_code, item_code, c,callback){
	var url = "/kuke/share/getSharePlayUrl?lcode=" + l_code + "&item_code=" + item_code+"&c="+c;//new Date().getTime();
	jQuery.ajax({
		url: url,
		dataType: 'JSON',
		async: false,
		timeout: 30000,
		success: function(data, textStatus){
			jQuery.globalEval(callback(data));
		},
		error: function(XMLHttpRequest,textStatus,errorThrown){
			//alert("textStatus="+textStatus+"\nerrorThrown="+errorThrown);
		}
	});
}

KukePlayer.prototype.mp3_play = function(index){
	if(index == null){
		if(this.current_i == -1){
			KukePlayer.play(this, 0);
		}
		else{
			this.mp3_recover();
		}
	}
	else{
		KukePlayer.play(this, index);
	}
}

KukePlayer.prototype.mp3_pause = function(){
	this.audio.pause();
	this.show_pausing(this.s_l_code);
}

KukePlayer.prototype.mp3_recover = function(){
	this.audio.play();
	this.show_playing(this.s_l_code);
}

KukePlayer.prototype.next_play = function(){
	if(this.current_i == this.item_l_code_array.length - 1){
		this.current_i = -1;
	}
	this.current_i++;
	this.mp3_play(this.current_i);
}

KukePlayer.prototype.show_loading = function(id, prevId){
	
}

KukePlayer.prototype.show_playing = function (id, prevId){
	
}

KukePlayer.prototype.show_pausing = function(id, prevId){
	
}

KukePlayer.prototype.collectMp3 = function(id, prevId){
	
}

KukePlayer.prototype.progressBarReset = function(id, prevId){
	
}

KukePlayer.prototype.unUseedUrl = function(id, prevId){
	
}

KukePlayer.prototype.progressBarScroll = function(current_time, total_time){
	
}