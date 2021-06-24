(function($){
	function saveRange(target){
		var opts = $.data(target, 'texteditor').options;
		opts.selectedRange = null;
		if (window.getSelection){
			var sel = window.getSelection();
			if (sel.getRangeAt && sel.rangeCount){
				var range = sel.getRangeAt(0);
				var pnode = range.commonAncestorContainer.parentNode;
				if ($(pnode).closest('.texteditor')[0] == $(target).parent()[0]){
					opts.selectedRange = range;
				}
			}
		} else if (document.selection && document.selection.createRange){
			var range = document.selection.createRange();
			var pnode = range.parentElement();
			if ($(pnode).closest('.texteditor')[0] == $(target).parent()[0]){
				opts.selectedRange = range;
			}
		}
	}

	function restoreRange(target){
		var opts = $.data(target, 'texteditor').options;
		var range = opts.selectedRange;
		if (window.getSelection){
			var sel = window.getSelection();
			sel.removeAllRanges();
			if (range){
				sel.addRange(range);
			}
		} else if (document.selection){
			document.selection.empty();
			if (range && range.select){
				range.select();
			}
		}
	}

	function insertContent(target, html){
		var opts = $.data(target, 'texteditor').options;
		if (opts.selectedRange){
			if (window.getSelection){
				opts.selectedRange.collapse(false);
				opts.selectedRange.insertNode($(html)[0]);
			} else if (document.selection && opts.selectedRange.select){
				opts.selectedRange.collapse(false);
				opts.selectedRange.pasteHTML($(html)[0]);
			}
		}
	}

	function updateToolbar(target){
		var opts = $.data(target, 'texteditor').options;
		opts.dlgToolbar.find('.l-btn').each(function(){
			var cmd = $(this).attr('cmd');
			if (document.queryCommandState(cmd)){
				$(this).linkbutton('select');
			} else {
				$(this).linkbutton('unselect');
			}
		});
		opts.dlgToolbar.find('.combobox-f').each(function(){
			var cmd = $(this).attr('cmd');
			var value = String(document.queryCommandValue(cmd)||'');
			value = value.replace(/['"]/g,'').toLowerCase();
			var copts = $(this).combo('options');
			var onChange = copts.onChange;
			copts.onChange = function(){};
			var data = $(this).combobox('getData');
			if ($.easyui.indexOfArray(data, copts.valueField, value) >= 0){
				$(this).combobox('setValue', value);
			} else {
				$(this).combobox('clear');
			}
			copts.onChange = onChange;
		});
	}

	function buildEditor(target){
		var opts = $.data(target, 'texteditor').options;
		$(opts.dlgToolbar).panel('clear').remove();
		opts.dlgToolbar = $('<div></div>');
		for(var i=0; i<opts.toolbar.length; i++){
			var tool = opts.toolbar[i];
			if (tool == '-'){
				$('<span class="dialog-tool-separator"></span>').appendTo(opts.dlgToolbar);
			} else {
				var cmd = opts.commands[tool];
				if (cmd){
					cmd.type = cmd.type || 'linkbutton';
					cmd.plain = cmd.plain || true;
					var btn = $('<a href="javascript:;"></a>').appendTo(opts.dlgToolbar);
					btn.attr('cmd', tool);
					btn[cmd.type](cmd);
				}
			}
		}
		$(target).dialog($.extend({}, opts, {
			toolbar: opts.dlgToolbar
		}));
		$(target).attr('contenteditable', true);
		var input = $(target).dialog('dialog').children('.texteditor-value');
		if (!input.length){
			input = $('<textarea class="texteditor-value" style="display:none"></textarea>').insertAfter(target);
		}
		input.attr('name', opts.name || $(target).attr('name'));
		$(target).unbind('.texteditor').bind('mouseup.texteditor keyup.texteditor',function(){
			saveRange(target);
			updateToolbar(target);
		}).bind('blur.texteditor', function(e){
			input.val($(target).html());
		});
		input.val($(target).html());
	}

	$.fn.texteditor = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.texteditor.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.dialog(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'texteditor');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'texteditor', {
					options: $.extend({}, $.fn.texteditor.defaults, $.fn.texteditor.parseOptions(this), options)
				});
			}
			buildEditor(this);
		});
	};

	$.fn.texteditor.methods = {
		options: function(jq){
			return jq.data('texteditor').options;
		},
		execCommand: function(jq, cmd){
			return jq.each(function(){
				var a = cmd.split(' ');
				var c = a.shift();
				restoreRange(this);
				document.execCommand(c, false, a.join(' ')||null);
				updateToolbar(this);
				saveRange(this);
			});
		},
		getEditor: function(jq){
			return jq.closest('.texteditor').children('.texteditor-body');
		},
		insertContent: function(jq, html){
			return jq.each(function(){
				insertContent(this, html);
				$(this).dialog('dialog').children('.texteditor-value').val($(this).html());
			});
		},
		destroy: function(jq){
			return jq.each(function(){
				var opts = $(this).texteditor('options');
				$(opts.dlgToolbar).panel('clear');
				$(this).dialog('destroy');
			});
		},
		getValue: function(jq){
			return jq.dialog('dialog').children('.texteditor-value').val();
		},
		setValue: function(jq, html){
			return jq.each(function(){
				$(this).html(html);
				$(this).dialog('dialog').children('.texteditor-value').val($(this).html());
			});
		}
	};

	$.fn.texteditor.parseOptions = function(target){
		return $.extend({}, $.fn.dialog.parseOptions(target), {

		});
	};

	$.fn.texteditor.defaults = $.extend({}, $.fn.dialog.defaults, {
		title: null,
		cls: 'texteditor',
		bodyCls: 'texteditor-body',
		draggable: false,
		shadow: false,
		closable: false,
		inline: true,
		border: 'thin',
		name: '',
		toolbar: ['bold','italic','strikethrough','underline','-','justifyleft','justifycenter','justifyright','justifyfull','-','insertorderedlist','insertunorderedlist','outdent','indent','-','formatblock','fontname','fontsize'],
		commands: {
			'bold': {
				type: 'linkbutton',
				iconCls: 'icon-bold',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','bold');
				}
			},
			'italic': {
				type: 'linkbutton',
				iconCls: 'icon-italic',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','italic');
				}
			},
			'strikethrough': {
				type: 'linkbutton',
				iconCls: 'icon-strikethrough',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','strikethrough');
				}
			},
			'underline': {
				type: 'linkbutton',
				iconCls: 'icon-underline',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','underline');
				}
			},
			'justifyleft': {
				type: 'linkbutton',
				iconCls: 'icon-justifyleft',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','justifyleft');
				}
			},
			'justifycenter': {
				type: 'linkbutton',
				iconCls: 'icon-justifycenter',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','justifycenter');
				}
			},
			'justifyright': {
				type: 'linkbutton',
				iconCls: 'icon-justifyright',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','justifyright');
				}
			},
			'justifyfull': {
				type: 'linkbutton',
				iconCls: 'icon-justifyfull',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','justifyfull');
				}
			},
			'insertorderedlist': {
				type: 'linkbutton',
				iconCls: 'icon-insertorderedlist',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','insertorderedlist');
				}
			},
			'insertunorderedlist': {
				type: 'linkbutton',
				iconCls: 'icon-insertunorderedlist',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','insertunorderedlist');
				}
			},
			'outdent': {
				type: 'linkbutton',
				iconCls: 'icon-outdent',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','outdent');
				}
			},
			'indent': {
				type: 'linkbutton',
				iconCls: 'icon-indent',
				onClick: function(){
					$(this).texteditor('getEditor').texteditor('execCommand','indent');
				}
			},
			'formatblock': {
				type: 'combobox',
				width: 100,
				prompt: 'Font Format',
				editable: false,
				panelHeight: 'auto',
				data: [
					{value: 'p',text:'p'},
					{value: 'pre',text:'pre'},
					{value: 'h6',text:'h6'},
					{value: 'h5',text:'h5'},
					{value: 'h4',text:'h4'},
					{value: 'h3',text:'h3'},
					{value: 'h2',text:'h2'},
					{value: 'h1',text:'h1'}
				],
				formatter: function(row){
					return '<'+row.value+' style="margin:0;padding:0">'+row.text+'</'+row.value+'>';
				},
				onChange: function(value){
					$(this).texteditor('getEditor').texteditor('execCommand','formatblock '+value);
				}
			},
			'fontname': {
				type: 'combobox',
				width: 100,
				prompt: 'Font Family',
				editable: false,
				panelHeight: 'auto',
				data: [
					{value:'arial',text:'Arial'},
					{value:'comic sans ms',text:'Comic Sans'},
					{value:'courier new',text:'Courier New'},
					{value:'georgia',text:'Georgia'},
					{value:'helvetica',text:'Helvetica'},
					{value:'impact',text:'Impact'},
					{value:'times new roman',text:'Times'},
					{value:'trebuchet ms',text:'Trebuchet'},
					{value:'verdana',text:'Verdana'}
				],
				formatter: function(row){
					return '<font face="'+row.value+'">'+row.text+'</font>';
				},
				onChange: function(value){
					$(this).texteditor('getEditor').texteditor('execCommand','fontname '+value);
				}
			},
			'fontsize': {
				type: 'combobox',
				width: 100,
				prompt: 'Font Size',
				editable: false,
				panelHeight: 'auto',
				data: [
					{value:1,text:'Size 1'},
					{value:2,text:'Size 2'},
					{value:3,text:'Size 3'},
					{value:4,text:'Size 4'},
					{value:5,text:'Size 5'},
					{value:6,text:'Size 6'}
				],
				formatter: function(row){
					return '<font size="'+row.value+'">'+row.text+'</font>';
				},
				onChange: function(value){
					$(this).texteditor('getEditor').texteditor('execCommand','fontsize '+value);
				}
			}
		}

	});

	$.parser.plugins.push('texteditor');
})(jQuery);
