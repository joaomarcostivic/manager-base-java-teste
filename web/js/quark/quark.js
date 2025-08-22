/**
 *	jQuery Quark v1.0 plugin
 *	
 *	@copyright      2016 TIViC - Tecnologia, Informação e Inovação.
 *	@author         Edgard Hufelande
 *	@documentation  http://www.tivic.com.br/Quark/doc
 *	@description    TODO	
 */

var protocol    = window.location.protocol;
var pathname    = window.location.pathname.split('/');
var host        = window.location.host;
var nmRoot      = protocol + '//' + host + '/' + pathname[1] + '/';

var Quark = {
	
	initialized: false,
	
	initialize: function() {
		this.initialized = true;
	},
	
	init: function(){
		
		Date.prototype.getMonthName = function() {
		    var month_names = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
		    return month_names[this.getMonth()];
		}

		Date.prototype.getMonthAbbr = function() {
		    var month_abbrs = ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'];
		    return month_abbrs[this.getMonth()];
		}

		Date.prototype.getDayFull = function() {
		    var days_full = ['Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado'];
		    return days_full[this.getDay()];
		};

		Date.prototype.getDayAbbr = function() {
			var days_abbr = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab'];
		    return days_abbr[this.getDay()];
		};

		Date.prototype.getDayOfYear = function() {
		    var onejan = new Date(this.getFullYear(), 0, 1);
		    return Math.ceil((this - onejan) / 86400000);
		};

		Date.prototype.getDaySuffix = function() {
		    var d = this.getDate();
		    var sfx = ["th", "st", "nd", "rd"];
		    var val = d % 100;
		    return (sfx[(val - 20) % 10] || sfx[val] || sfx[0]);
		};

		Date.prototype.getWeekOfYear = function() {
		    var onejan = new Date(this.getFullYear(), 0, 1);
		    return Math.ceil((((this - onejan) / 86400000) + onejan.getDay() + 1) / 7);
		};

		Date.prototype.isLeapYear = function() {
		    var yr = this.getFullYear();

		    if ((parseInt(yr) % 4) == 0) {
		        if (parseInt(yr) % 100 == 0) {
		            if (parseInt(yr) % 400 != 0) {
		                return false;
		            }
		            if (parseInt(yr) % 400 == 0) {
		                return true;
		            }
		        }
		        if (parseInt(yr) % 100 != 0) {
		            return true;
		        }
		    }
		    if ((parseInt(yr) % 4) != 0) {
		        return false;
		    }
		};

		Date.prototype.getMonthDayCount = function() {
		    var month_day_counts = [31, this.isLeapYear() ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		    return month_day_counts[this.getMonth()];
		}
		
		
		this.initialize();
		
		return this;	
	},
	
    isMobile: {    	
        Android: function() {
            return navigator.userAgent.match(/Android/i);
        },
        BlackBerry: function() {
            return navigator.userAgent.match(/BlackBerry/i);
        },
        iOS: function() {
            return navigator.userAgent.match(/iPhone|iPad|iPod/i);
        },
        Opera: function() {
            return navigator.userAgent.match(/Opera Mini/i);
        },
        Windows: function() {
            return navigator.userAgent.match(/IEMobile/i);
        },
        Any: function() {
            return (Quark.isMobile.Android() || Quark.isMobile.BlackBerry() || Quark.isMobile.iOS() || Quark.isMobile.Opera() || Quark.isMobile.Windows());
        }
    },

    ajaxCaller: function(id, options) {
        try {
        	
        	if(!this.initialized) {
        		this.init();
        	}
        	
//            if (window.localStorage.getItem("nm_host") === null || window.localStorage.getItem("nm_host") === "") {
//                Quark.load('./config.html', {
//                    title: 'Configurações Administrativas'
//                });
//                return false;
//            }

            var loading  = this.onLoading;
            var ajaxData = this.getDataArguments(id, options['args']);
            var servlet  = this.getUrlContextServlet(host);
            var alert    = null;
            var ajaxResult;
            
            if (typeof id === 'undefined') {
                throw "É necessário informar o id referente ao ajax que será chamado.";
            }

            if (options == null || typeof options.args === 'undefined') {
                options.args = [];
            }

            if (!options.noLoading) {
                loading.show();
            }
            
            var ajax = $.ajax({
                url: nmRoot + '/quark/',
                dataType: 'text',
                data: ajaxData,
                async: true,
                beforeSend: function() {
                    if (alert) { 
                        alert.trigger('close');
                    }
                },
                success: function(data) {
                	if (alert) {
                        alert.remove();
                    }
                    if (data) {
                        var response = eval("(" + data + ")");
                    }
                    if (options && options.callback && options.callback instanceof Function) {
                        options.callback(response);
                    } else {
                        return response;
                    }

                    if (!options.noLoading) {
                        loading.hide();
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                	if (typeof this.tryCount !== "number") {
                        this.tryCount = 1;
                    }
                    if (!(xhr.status >= 200 && xhr.status <= 307)) {

                        alert = Quark.alert('body', {
                            icon: 'fa fa-spinner fa-pulse fa-3x fa-fw',
                            message: 'Tivemos um problema na conexão, tentaremos novamente em: <span class="timer"></span>',
                            callbackParameters: [this]
                        }, function(request) {
                            Quark.timer(40, $('.timer'), onTimerSucess, request);
                        });

                        function onTimerSucess(r) {
                            $.ajax(r);
                            return;
                        }

                        return;
                    } else {
                        Quark.alert('body', {
                            icon: 'fa fa-spinner fa-pulse fa-3x fa-fw',
                            message: '#40000',
                        });
                    }
                }
            });

        } catch (e) {
        	console.log(e);
        }
    },

    get: function(parameter) {
        var results = new RegExp('[\?&]' + parameter + '=([^&#]*)').exec(window.location.href);
        if (results == null) {
            return null;
        } else {
            return decodeURIComponent(results[1]) || 0;
        }
    },

    post: function() {

    },

    removeUrlParameter: function(parameter) {
        var results = new RegExp('[\?&]' + parameter + '=([^&#]*)').exec(window.location.href);
        if (results == null) {
            return null;
        } else {
            return results[1] || 0;
        }
    },

    getUrlPath: function(host) {
        try {
            return window.location.protocol + "//" + (host == null ? window.location.host : host) + "/" + window.location.pathname;
        } catch (e) {
            console.log(e);
        }
    },

    getUrlContext: function(host) {
        try {
            return window.location.protocol + "//" + (host == null ? window.location.host : host) + "/" + window.location.pathname.split("/")[1];
        } catch (e) {
            console.log(e);
        }
    },

    getUrlContextServlet: function(host) {
        try {
            return window.location.protocol + "//" + (host == null ? window.location.host : host) + "/" + window.location.pathname.split("/")[1] + "/quark/";
        } catch (e) {
            console.log(e);
        }
    },

    getDataArguments: function(id, args) {
        try {
            var data = ['id=' + id, '&args=('];

            if (args) {
                for (i = 0; i < args.length; i++) {

                    data.push('const ');
                    data.push(args[i].value + ':');
                    data.push(args[i].type);

                    if (i != (args.length - 1))
                        data.push(', ');

                }
            }

            data.push(')');

            return data.join("");
        } catch (e) {
            console.log(e);
        }
    },

    removeUrlParameters: function() {
        var url = window.location.toString();
        window.history.pushState('', document.title, url.substring(0, url.indexOf("?")));
    },

    callback: function(data) {
        return data;
    },

    onLoading: {
        show: function() {
            var eLoading = $("<div />")
                .addClass("quark-loading")
                .css({
                    'background-color': 'rgba(255, 255, 255, .7)',
                    'position': 'fixed',
                    'top': '0px',
                    'left': '0px',
                    'z-index': '9998'
                })
                .css({
                    'width': '100%',
                    'height': '100%'
                });

            var spinner = $("<i />")
                .addClass("fa fa-cog fa-spin")
                .css({
                    'margin': '-30px -35px',
                    'position': 'absolute',
                    'font-size': '70px',
                    'top': '50%',
                    'left': '50%'
                });

            spinner.prependTo(eLoading);
            eLoading.prependTo('body');
            eLoading.addClass('animated bounceIn');
        },
        hide: function() {
            $(".quark-loading").fadeOut('fast', function() {
                $(this).remove();
            })
        }
    },

    /**
     * Chamar página de exibição
     * 
     * @author Edgard Hufelande
     * @param id
     * @param options
     * @returns Bootstrap Modal
     */

    load: function(url, options) {
    	
    	if (options && options.parameters) {
            var data = ['?'];

            var i = 0;
            for (k in options.parameters) {
                if (i != Object.keys(options.parameters).length - 1) {
                    data.push(k + "=" + options.parameters[k] + "&");
                } else {
                    data.push(k + "=" + options.parameters[k]);
                }
                i++;
            }
        }
    	
    	this.alert($("body"), {
    		ajax: {
    			url: url,
    			parameters: options.parameters
    		},    		
    	}, null);
    	
        return $(this);
    },
    /**
     * Cria um alerta para exibição na tela
     * 
     * @author Edgard Hufelande
     * @param id
     * @param options
     * @returns Bootstrap Modal
     */

    alert: function(id, options, callback) {

        var ajaxContent;
        var message = options.message;
        var icon    = options.icon;

        if (typeof id === 'undefined') {
            id = $("body");
        }

        if (options === null && !options.message) {
            throw "É necessária a mensagem que será informada.";
            return false;
        }

        if (options === null && !options.icon) {
            throw "É necessário o icone.";
            return false;
        }

        if (options.timer > 0) {
            message = [options.message].join('');
        }

        if (options.icon) {
            icon = $("<div />").addClass('row').append(
        				$("<i />").addClass(options.icon + " text-muted").css('font-size', '5em')
            		).append($("<hr />"));
        }

        var modal = $("<div />").attr({
        	'id': 'messageScreenModal',
            tabindex: -1,
            role: 'dialog',
        }).addClass("modal fade").append(
            $("<div />").addClass("modal-dialog modal-sm").append(
                $("<div />").addClass("modal-content").append(
                    $("<div />").addClass("modal-body text-center").append().css({
                        'height': options.fitHeight ? $(window).height() * 0.85 : 'auto',
                    })
                ).append(
                    $("<div />").addClass("modal-footer")
                )
            )
        );
        
        if(options.ajax){
    		var data = options.ajax.parameters;
        	$('.modal-body', modal).load(options.ajax.url, function(response){
        	});
        } else {
        	$('.modal-body', modal).append(
                $("<div />").css({
                    height: '100%'
                }).html(message).prepend(icon)
            );
        }
        
        $(window).on('resize', function() {
        	if(options.fitHeight) {
        		$(".modal-body", modal).css('height', $(window).height() * 0.85);
        	}
        });

        $(id).append(modal);

        if (options.timer > 0) {
            setTimeout(function() {
                modal.modal('hide');
            }, options.timer);
        }

        modal.bind('close', function() {
            modal.modal('hide').data('bs.modal', null);
        });

        if (!(options.buttons instanceof Array)) {
            $(".modal-footer", modal).append(
                $("<button />").addClass("btn btn-danger btn-sm").html("Fechar").bind('click', function() {
                    modal.modal('hide').on('hidden', function () {
                        $(this).remove();
                    });
                })
            );
        } else {
            var handle = function(fn, apply) {
                return function() {
                    fn.apply(null, apply);
                }
            }
            for (i = 0; i < options.buttons.length; i++) {
                var button = options.buttons[i];
                var btnObj = $("<button />")
                    .addClass("btn btn-" + button.type)
                    .html(button.label)
                    .prepend(" ")
                    .prepend($("<i />").addClass(button.icon));

                if (button.click instanceof Function) {
                    btnObj.on('click tap', handle(button.click, [modal]));
                }

                $(".modal-footer", modal).append(btnObj);
            }
        }

        if (callback instanceof Function) {
            if (options.callbackParameters instanceof Array) {
                callback.apply(null, options.callbackParameters);
            } else {
                callback(options.callbackParameters);
            }
        }

        modal.modal();

        return modal;

    },

    /**
     * Remover um elemento nav referente ao Quark.menuList
     * @author Edgard Hufelande
     * @param element
     */

    capitalize: function(str) {
        var tokens = str.split(" ").filter(function(t) {
            return t != "";
        });
        var res = [];
        var i;
        var len;
        var component;
        for (i = 0, len = tokens.length; i < len; i++) {
            component = tokens[i];
            if (tokens[i].length != 2) {
                res.push(component.substring(0, 1).toUpperCase());
                res.push(component.substring(1).toLowerCase());
                res.push(" "); // put space back in
            } else {
                res.push(component.substring(0, 1).toLowerCase());
                res.push(component.substring(1).toLowerCase());
                res.push(" "); // put space back in
            }
        }
        return res.join("");
    },

    eval: function(str) {
        return eval("(" + str + ")");
    },

    format: {
        phone: function(n) {
            n = n.replace(/\D/g, "");
            n = n.replace(/^(\d\d)(\d)/g, "($1) $2");
            n = n.replace(/(\d{4})(\d)/, "$1-$2");
            return n;
        }
    },

    date: {
        getDate: function(date) {
            if (date) {
                return new Date(date);
            } else {
                return new Date();
            }
        },
        getYear: function() {
            if (date) {
                var date = new Date(date)
                return date.getYear();
            } else {
                var date = new Date()
                return date.getYear();
            }
        },
        getFullYear: function() {
            if (date) {
                var date = new Date(date)
                return date.getFullYear();
            } else {
                var date = new Date()
                return date.getFullYear();
            }
        },
        parse: function(value) {
            try {
                var parts = (value + '').split(' ');
                var dateParts = (parts[0] + '').split('/');
                var hourParts;
                if (parts[1])
                    hourParts = (parts[1] + '').split(':');
                if (!hourParts) {
                    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
                } else if (hourParts.length > 2) {
                    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0], hourParts[0], hourParts[1], hourParts[2]);
                } else {
                    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0], hourParts[0], hourParts[1]);
                }
            } catch (e) {
                return null;
            }
        },
        format: function(dt, dateFormat) {
        	try {
	        	if(!this.initialized) {
	        		Quark.init();
	        	}
	        	
	            // break apart format string into array of characters
	            dateFormat = dateFormat.split("");
	
	            var date = dt.getDate(),
	                month = dt.getMonth(),
	                hours = dt.getHours(),
	                minutes = dt.getMinutes(),
	                seconds = dt.getSeconds();
	            // get all date properties ( based on PHP date object functionality )
	            var date_props = {
	                d: date < 10 ? '0' + date : date,
	                D: dt.getDayAbbr(),
	                j: dt.getDate(),
	                l: dt.getDayFull(),
	                S: dt.getDaySuffix(),
	                w: dt.getDay(),
	                z: dt.getDayOfYear(),
	                W: dt.getWeekOfYear(),
	                F: dt.getMonthName(),
	                m: month < 10 ? '0' + (month + 1) : month + 1,
	                M: dt.getMonthAbbr(),
	                n: month + 1,
	                t: dt.getMonthDayCount(),
	                L: dt.isLeapYear() ? '1' : '0',
	                Y: dt.getFullYear(),
	                y: dt.getFullYear() + ''.substring(2, 4),
	                a: hours > 12 ? 'pm' : 'am',
	                A: hours > 12 ? 'PM' : 'AM',
	                g: hours % 12 > 0 ? hours % 12 : 12,
	                G: hours > 0 ? hours : "12",
	                h: hours % 12 > 0 ? hours % 12 : 12,
	                H: hours,
	                i: minutes < 10 ? '0' + minutes : minutes,
	                s: seconds < 10 ? '0' + seconds : seconds
	            };

	            var date_string = "";
	            for (var i = 0; i < dateFormat.length; i++) {
	                var f = dateFormat[i];
	                if (f.match(/[a-zA-Z]/g)) {
	                    date_string += date_props[f] ? date_props[f] : '';
	                } else {
	                    date_string += f;
	                }
	            }
	
	            return date_string;
        	} catch (e) {
                throw (e);
                return e;
        	}
        }
    },

    /**
     * Aplicando delay de execução no código
     * 
     * @author  Edgard Hufelande 
     * @param   milliseconds
     * @returns sleep
     * 
     */
    sleep: function(milliseconds) {
        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
            if ((new Date().getTime() - start) > milliseconds) {
                break;
            }
        }
    },

    /**
     * Criar um menu com icones
     * @author Edgard Hufelande
     * @param element
     */
    menuList: {
        create: function(element, options) {
            Quark.menuList.removeAll();

            if (!element) {
                alert('menuList.create: É necessário informar o elemento no qual será inserido o menuList.');
                return false;
            }

            element = element ? element : document.body;
            options.list = options.list ? options.list : {};

            var elementMenu = $("<nav />").css({
                '-webkit-animation-delay': '0s',
                'animation-duration': '0.5s'
            }).addClass("horizontal menuList animated bounceInRight").bind('click tap touchstart', function(e) {
                e.stopPropagation();
            });

            var ulMenu = $("<ul />");

            for (var i in options.list) {
                var list = options.list[i];
                var liMenu = $("<li />");
                var aMenu = $("<a />");
                var iconMenu;

                if (list.icon) {
                    iconMenu = $("<i /> ").addClass(list.icon).css({
                        'font-size': '30px'
                    });
                }

                if (list.href) {
                    aMenu.attr('href', list.href);
                }

                if (list.click && typeof list.click === 'function') {
                    aMenu.bind('click', element.data(), list.click);
                    aMenu.bind("click", function(e) {
                        Quark.menuList.removeAll();
                    });
                }

                if (list.label) {
                    aMenu.html((iconMenu ? "<br /> " : "") + list.label).css({
                        'font-size': '7px'
                    }).addClass('text-center');
                }

                aMenu.prepend(iconMenu);
                liMenu.append(aMenu);
                ulMenu.append(liMenu);

                i++;
            }

            ulMenu.appendTo(elementMenu);

            elementMenu.appendTo(element).show();

            elementMenu.css({
                position: 'absolute',
                top: element.offset().top - 200,
                right: 0
            });

            $(document).bind("click", function(e) {
                if (!$(e.currentTarget).hasClass("menuList")) {
                    Quark.menuList.removeAll();
                }
            });

            return elementMenu;
        },

        /**
         * Remover um elemento nav referente ao Quark.menuList
         * @author Edgard Hufelande
         * @param element
         */
        remove: function(element) {
            if ($(element) && $(element).hasClass('menuList')) {
                alert('menuList.create: É necessário informar qual menuList será removido.');
                return false;
            }

            $(element).hasClass('menuList').remove();
        },

        /**
         * Remover todos os elementos nav referente ao Quark.menuList
         * @author Edgard Hufelande
         */
        removeAll: function() {

            if ($(".menuList").length > 0) {
                $(".menuList").remove();
            }
        }
    },

    math: {
        getPercentage: function(val1, val2, withSymbol) {
            return ((val1 / val2) * 100).toFixed(2) + (withSymbol === true ? "%" : "");
        }
    },

    timer: function(duration, el, callback, args) {
        var timer = duration,
            minutes, seconds;
        setInterval(function() {
            minutes = parseInt(timer / 60, 10)
            seconds = parseInt(timer % 60, 10);

            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;

            el.text(minutes + ":" + seconds);

            if (--timer < 0) {
                if (callback instanceof Function) {
                    if (args instanceof Array) {
                        callback.apply(null, args);
                    } else {
                        callback(args);
                    }
                }
                callback = null;
                timer = duration;
            }
        }, 1000);

        return el;
    },

    listView: function(element, array) {

        for (list in array) {
            var ul = $(document.createElement('ul')).addClass('list-view');

            for (i in array[list]) {
                var li = $(document.createElement('li')).addClass('list-view-item');
                ul.append(li);
            }
        }

        ul.append(element);

    },

    localStorage: {
        setItem: function(key, value) {
            if (value instanceof Array || value instanceof Object) {
                value = JSON.stringify(value);
                return window.localStorage.setItem(key, value);
            } else {
                return window.localStorage.setItem(key, value);
            }
        },
        getItem: function(key) {
            if (window.localStorage.getItem(key) !== null) {
                return window.localStorage.getItem(key);
            }
        },
        removeItem: function(key) {
            if (window.localStorage.getItem(key))
                return window.localStorage.removeItem(key);
        },
        removeAll: function() {
            for (i = 0; i<window.localStorage.length; i++) {
            	if(window.localStorage.key(i)){
                    var key = window.localStorage.key(i);
                    window.localStorage.removeItem(key);
            	}
            }
        }
    },

    sessionStorage: {
        setItem: function(key, value) {
            if (value instanceof Array || value instanceof Object) {
                value = JSON.stringify(value);
                return window.sessionStorage.setItem(key, value);
            } else {
                return window.sessionStorage.setItem(key, value);
            }
        },
        getItem: function(key) {
            if (window.sessionStorage.getItem(key))
                return (JSON.parse(window.sessionStorage.getItem(key)) ? JSON.parse(window.sessionStorage.getItem(key)) : window.sessionStorage.getItem(key));
        },
        removeItem: function(key) {
            if (window.sessionStorage.getItem(key))
                return sessionStorage.removeItem(key);
        },
        removeAll: function() {
            for (i = 0; window.sessionStorage.length; i++) {
                var key = sessionStorage.key(i);
                sessionStorage.removeItem(key);
            }
        }
    }
};