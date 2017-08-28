(function($){  
                var _prcData = {  
                    percent:"100%",  
                    categoryName:"",  
                    categoryInnerText:""  
                };  
                var _prcProgress  = function(ele,opt){  
                    this.$element = ele,  
                    this.defaults = {  
                        borderSize:1,  
                        borderColor:'#e7e7e7',  
                        borderStyle:"solid",  
                        width:200,  
                        height:18,  
                        categoryWidth:60,  
                        data:_prcData,  
                        prcBarDefClass:"orange",  
                        prcBarBackColor:"",  
                        onClick:null  
                    },  
                    this.options = $.fn.extend({},this.defaults,opt)  
                }  
  
                _prcProgress.prototype = {  
                    _render:function(){  
                        var g = this, p = this.options;  
                        g.prcProg = $(g.$element);  
                        var prcRenderHtmlArr = [];  
                        prcRenderHtmlArr.push(' <span class="prcCategory"></span>');  
                        prcRenderHtmlArr.push(' <div class="graph">');  
                        prcRenderHtmlArr.push('     <span class="categoryText"></span>');  
                        prcRenderHtmlArr.push('     <span class="prcBar"></span>');  
                        prcRenderHtmlArr.push('     </div>');  
                        var prcRenderHtml = prcRenderHtmlArr.join('');  
                        //给元素添加样式  
                        g.prcProg.addClass("prcWarp");  
                        //添加元素到页面  
                        g.prcProg.html(prcRenderHtml);  
                        g.prcProg.prcCategory = $(".prcCategory:first",g.prcProg);  
                        g.prcProg.graph = $(".graph:first",g.prcProg);  
                        g.prcProg.categoryText = $(".categoryText:first",g.prcProg);  
                        g.prcProg.prcBar = $(".prcBar:first",g.prcProg);  
  
                    },_init:function(){  
                        this._render();  
                        var g = this, p = this.options;  
                        var prcData = p.data;  
                        var prcWrapWidth = 0;  
                        var prcWrapHeight = 0;  
                        var borWdith = 0 ;  
                        var graphWidth = 0;  
                        var graphHeight = 0;  
                        var cateWidth = 0;  
                        //百分比条设置边框样式  
                        g.prcProg.graph.css({"border-width ":p.borderSize,"border-style":p.borderStyle,"border-color ":p.borderColor});  
                        if(p.borderStyle != "none"){  
                            if(typeof p.borderSize == "number"){  
                                borWidth = 2 * p.borderSize;  
                            }else{  
                                borWidth = 2 * parseInt(p.borderSize);  
                            }  
                        }  
                        if(typeof p.width == "number"){  
                            graphWidth = p.width;  
                        }else{  
                            graphWidth = parseFloat(p.width);  
                        }  
                        if(typeof p.categoryWidth == "number"){  
                            cateWidth = p.categoryWidth;  
                        }else{  
                            cateWidth = parseFloat(categoryWidth);  
  
                        }  
  
                        if(typeof p.height == "number"){  
                            graphHeight = p.height;  
                        }else{  
                            graphHeight = parseFloat(p.height);  
                        }  
                        g.prcProg.graph.height(graphHeight);  
                        g.prcProg.prcCategory.width(cateWidth);  
                        g.prcProg.graph.width(graphWidth);  
                        //IE不加2会变形  
                        prcWrapWidth = borWidth + g.prcProg.graph.outerWidth(true) + g.prcProg.prcCategory.outerWidth(true) + 2 ;  
                        prcWrapHeight =  borWidth + g.prcProg.graph.outerHeight(true) ;  
                        g.prcProg.height(prcWrapHeight);  
                        g.prcProg.width(prcWrapWidth);  
                        //设置百分比条的背景  
                        if(p.prcBarBackColor){  
                            g.prcProg.prcBar.css("background",p.prcBarBackColor);  
                        }else{  
                            g.prcProg.prcBar.addClass(p.prcBarDefClass);  
                        }  
  
                        this._setData(prcData);  
                        g.prcProg.graph.click(function(){  
                            //如果有绑定的单击事件  
                            if(p.onClick){  
                                g.prcProg.graph.css("cursor","pointer");  
                                //将传入的data传参给单击回调函数  
                                p.onClick.call(p.onClick,prcData);  
                            }  
                        });  
                    },_setData:function(prcData){  
                        var g = this, p = this.options;  
                        if(!prcData)return;  
                        var percent = 0.0;  
                        if(typeof prcData.percent == "string"){  
                            var percentStr = prcData.percent ;  
                            if(percentStr.indexOf("%") != -1){  
                                percentStr = percentStr.substring(0,percentStr.lastIndexOf("%"));  
                                percent = (percentStr * 1) / 100;  
                            }else{  
                                //转换字符串为数字类型  
                                percent = percentStr * 1;  
                            }  
  
  
                        }else if(typeof prcData.percent == "number"){  
                            percent = prcData.percent;  
                        }  
                        if(typeof percent == "number" && !isNaN(percent)){  
                            if(percent >= 1){  
                                percent = 1;  
                            }  
                            if(percent <= 0 ){  
                                percent = 0;  
                            }  
                            //设置百分比条的宽度  
                            g.prcProg.prcBar.width(g.prcProg.graph.width() * percent);  
                        }  
                        var cInnerText = g.parseNumPercent(percent)+"%";  
                        if(prcData.categoryInnerText){  
                            cInnerText = prcData.categoryInnerText + ":" + cInnerText;  
                        }  
                        g.prcProg.categoryText.text(cInnerText);  
                        var categoryStr  = "";  
                        if(prcData.categoryName){  
                            categoryStr = prcData.categoryName + ":";  
                        }  
                        g.prcProg.prcCategory.text(categoryStr);  
                    },percentNum:function(num, num2) {  
                        return (Math.round(num / num2 * 10000) / 100.00); //小数点后两位百分比  
                        //转换小数位包含两位小数的数字  
                    },parseNumPercent:function(num){  
                        return(Math.round(num * 10000) / 100.00);  
                    }  
  
  
  
                }  
                $.fn.prcProgress = function(options){  
                    var prcProg = new _prcProgress(this,options);  
                    prcProg._init();  
                }  
            })(jQuery);  