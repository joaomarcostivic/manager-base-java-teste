_cssTable = 'grid';
_cssHeader = 'noScrollTop header';
_cssSelector = 'noScrollLeft selector';
_cssCell = 'gridLine';
_cssLine = 'gridLine';
_cssLineSelected = 'gridLineSelected';
_cssArrow = 'gridColumnArrow';

_imgDown = 'imagens/grid_down.gif';
_imgUp = 'imagens/grid_up.gif';
_imgSearch = 'imagens/grid_search.gif';
_NONE = 0;
_DATE = 1;
_DATETIME = 2;
_TIME = 3;
_FLOAT = 4;
_CURRENCY = 5;
_CPF = 6;
_CNPJ = 7;
/* colums is Array of = [label, columnName, displayMask, groupByThisColumn] */
function Grid(name, div, rsm, colums, onClickFunction, showCheckBox)	{
	var table = document.createElement("TABLE");
	table.id = name;
	table.cellSpacing = 0;
	table.className   = _cssTable;
	
	var tbody = document.createElement("TBODY");
	
	/* Header */
	var tr = document.createElement("TR");
	tr.id = name+'_tr_header';
	for(l=0; l<colums.length; l++) {
		if(l==0){
			var newCell = document.createElement('TD');
			if(showCheckBox)	{
				newCell.innerHTML = '<input type="checkbox" id="cb_header" name="cb_header" onClick="changeAllState('+name+', this.checked)"/>';
				newCell.width=18;
				newCell.className = _cssHeader;
			}
			else	{
				newCell.innerHTML = '<img src="'+_imgSearch+'" width="15" height="15"/>';
				newCell.width=18;
				newCell.className = _cssHeader;
				newCell.onclick = function(){
					localizar(this);
				}
			}
			tr.appendChild(newCell);
		}
		var td = document.createElement('TD');
		td.className = _cssHeader;
		td.setAttribute("nowrap","nowrap");
		td.noWrap  ="true";
		td.appendChild(document.createTextNode(colums[l][0]));
		td.onclick = function(){
			ordenaTabela(this);
			return false;
		}
		var txt = getInnerText(td);
        td.innerHTML = txt+'<span class="'+_cssArrow+'" style="color:#666666; font-weight:bold; font-size:8;">&nbsp;&nbsp;&nbsp;</span>';
		td.setAttribute("nowrap","nowrap");

		tr.appendChild(td);
	}
	tbody.appendChild(tr);
	
	/* Cria linhas com registro */
	if(rsm != null){
		var valueColumnGrouped = '';
		for(i=0;i<rsm.lines.length; i++)	{
			// Criando linha
			var tr = document.createElement("tr");
			tr.id = name+'_tr_'+i;
			tr.onclick = function(){
				selectTR(this);
				if(onClickFunction)
					onClickFunction();
			}
			tr.className = _cssCell;
			tr.register  = rsm.lines[i];
			
			// Criando células
			for(l=0; l<colums.length; l++) {
				if(l==0){
					var newCell = document.createElement('td');
					if(showCheckBox)	{
						newCell.innerHTML = '<input type="checkbox" id="cb_'+table.id+'_'+i+'" name="cb_'+table.id+'_'+i+'"/>';
						newCell.width=18;
					}
					tr.appendChild(newCell);
				}
				var td = document.createElement('td');
				td.setAttribute("nowrap","nowrap");
				td.noWrap="true";
				td.style.padding="0 8 0 0";
				if(colums[l][1] in rsm.lines[i] && rsm.lines[i][colums[l][1]]!=null && rsm.lines[i][colums[l][1]]!='null')	{
					var value = rsm.lines[i][colums[l][1]];
					if(colums[l].length>3 && colums[l][3] && colums[l][2]!=_CURRENCY)	{
						if(value==valueColumnGrouped)
							value = '';
						else
							valueColumnGrouped = value;
					}
					if(colums[l].length>2)	{
						if(colums[l][2]==_DATE)	{
							value=value.split(' ');
							value=value[0];
						}
						// Time
						if(colums[l][2]==_TIME)	{
							value=value.split(' ');
							value=value[1];
							if(value.lastIndexOf(':')>3)
								value=value.substr(0,value.lastIndexOf(':'));
						}
						// Currency
						if(colums[l][2]==_CURRENCY)	{
							td.setAttribute("align","right");
							value = formatNumber(value, 2);
						}
						// Float
						if(colums[l][2]==_FLOAT)	{
							td.setAttribute("align","right");
							if(colums[l].length>3)
								value = formatNumber(value, colums[l][3]);
							else
								value = formatNumber(value, 2);
						}
						// Cpf
						if(colums[l][2]==_CPF)	
							value = txtFormat(value, '999.999.999-99');
						// Cpf
						if(colums[l][2]==_CNPJ)	
							value = txtFormat(value, '99.999.999/9999-99');
					};
					td.appendChild(document.createTextNode(value));
				}
				tr.appendChild(td);
				tbody.appendChild(tr);
			}
		}
	}
	
	table.appendChild(tbody);
	/*table.onblur = function(){
		document.selectedTable=null;
	}
	table.onclick = function(){
		if(table.focus)
			table.focus();
	}*/
	
	table.onkeydown = processTableKeys;
	/*table.oncontextmenu = gridContextMenu;*/
	this._table=table;
	
	if(div)	
		this.plotAt(div);
}
Grid.prototype.plotAt = function(div){
	this.plotAt2(div,true,true);
}
Grid.prototype.plotAt2 = function(div,allowWidth,allowHeight){
	div.innerHTML	 = '';
	if(allowWidth)
		div.style.width	 = div.parentNode.offsetWidth - (div.parentNode.offsetWidth-div.offsetWidth);
	if(allowHeight)
		div.style.height = div.parentNode.offsetHeight - (div.parentNode.offsetHeight-div.offsetHeight);
	div.appendChild(this._table);
}
Grid.prototype.addLine = function(values)	{
	var tr = document.createElement("tr");
	tr.id = name+'_tr_'+this._table.rows.length;
	/*tr.onclick = function(){
		selectTR(this);
		if(onClickFunction)
			onClickFunction();
	}*/
	tr.className = _cssCell;
	tr.register  = values;
	var newCell = document.createElement('td');
	tr.appendChild(newCell);
	for(i=0; i<values.length;i++)	{
		var td = document.createElement('td');
		td.setAttribute("nowrap","nowrap");
		td.noWrap="true";
		td.style.padding="0 8 0 0";
		td.appendChild(document.createTextNode(values[i][0]));
		if(values[i].length>1)	{
			td.setAttribute("align", values[i][1]);
		}
		tr.appendChild(td);
	}
	this._table.tBodies[0].appendChild(tr);
}
/* SELECAO LINHAS */
function selectTR(tr)	{
	var table = getParent(tr, "TABLE");
	if(table && tr){
		if(table.selectedRow){
			table.selectedRow.className = _cssLine
			table.selectedRow.onmouseover = tr.onmouseover;
			table.selectedRow.onmouseout  = tr.onmouseout;
		}
		tr.className=_cssLineSelected;
		tr.onmouseover = "";
		tr.onmouseout  = "";		
		table.selectedRow=tr;
		if(document.selectedTable && table.focus && document.selectedTable!=table)
			table.focus();
		document.selectedTable=table;
	}
}
function unSelectTR(grid)	{
	if(grid._table.selectedRow!=null)	{
		grid._table.selectedRow.className = _cssLine;
		grid._table.selectedRow = null;
	}
}
/* BUSCA */
pos = 0;
lastWord = '';
function changeAllState(table, checked)	{
	for(x=0; x<table.rows.length-1; x++)	{
		document.getElementById("cb_"+table.id+"_"+x).checked = checked;
	}
}
function localizar(table, txt){
	var divJanela;
	if(document.getElementById('searchWindow'))	
		divJanela = document.getElementById('searchWindow');
	else{
		divJanela = document.createElement("div");
		divJanela.id = "searchWindow";
		divJanela.style.position = "absolute";
		divJanela.style.width  = 140;
		divJanela.style.height = 10;
		divJanela.style.border = "outset #FFFFFF 1px";
		divJanela.style.backgroundColor = "#CCCCCC";
		divJanela.style.padding = 2;
		divJanela.style.fontFamily = 'Verdana, Arial, Helvetica, sans-serif';
		divJanela.style.fontSize = 9;
		
		var field = document.createElement("INPUT");
		field.type = "text";
		field.id = "searchField";
		field.value = lastWord;
		field.style.width = 100;
		field.style.height = 20;
		field.style.border = "solid #666666 1px";
		field.style.fontSize = 10;
		field.onkeydown = processFindKeys;
		
		var btSearch = document.createElement("BUTTON");
		btSearch.id = "searchButton";
		btSearch.style.width = 20;
		btSearch.style.height = 20;
		btSearch.style.marginLeft = 1;
		btSearch.style.border = "solid #666666 1px";
		btSearch.innerHTML = '<img src="'+_imgSearch+'" width="15" height="15"/>';
		btSearch.onclick = function(){
			findit(document.getElementById('searchField').value, table);
		}
	
		var btClose = document.createElement("BUTTON");
		btClose.id = "searchButtonClose";
		btClose.style.width = 10;
		btClose.style.height = 20;
		btClose.style.marginLeft = 1;
		btClose.style.border = "solid #666666 1px";
		btClose.innerHTML = '&times;';
		btClose.onclick = function(){
			var tmp = document.getElementById('searchWindow');
			tmp.parentNode.removeChild(tmp);
		}
		
		divJanela.appendChild(field);
		divJanela.appendChild(btSearch);
		divJanela.appendChild(btClose);
		divJanela.appendChild(document.createElement("BR"));
		var divTxt = document.createElement("DIV");
		divTxt.id='searchTxt';
		divJanela.appendChild(divTxt);
		table.appendChild(divJanela);
		
		divJanela.style.left = table.scrollLeft;
		divJanela.style.top = divJanela.scrollTop+20;
	}
	
	if(txt && txt!=''){
		document.getElementById('searchTxt').innerHTML=txt
	}
	
	document.getElementById('searchField').select();
}

processFindKeys = function(evt) {
    evt = (evt) ? evt : ((event) ? event : null);
    if (evt) {
		switch(evt.keyCode){
			case 13: /*enter*/
				  document.getElementById("searchButton").click();
			  break;
			case 27: /*esc*/
				  document.getElementById("searchButtonClose").click();
			  break;
		}
    }
}

function findit(word, table) {
	if(lastWord != word){
		lastWord = word;
		pos=0;
	}

	if (word == '') {
		return;
	}
	if (document.all) {
		var found = false;
		var text = document.body.createTextRange();
		for (var i=0; i<=pos && (found=text.findText(word)) != false; i++) {
			text.moveStart("character", 1);
			text.moveEnd("textedit");
		}
		if (found) {
			text.moveStart("character", -1);
			text.findText(word);
			text.select();
			text.scrollIntoView();
			pos++;
			var linhaSelecionada = getParent(text.parentElement(), "TR");
			linhaSelecionada.click();
			document.body.removeChild(document.getElementById('search'));
		}
		else {
			if (pos == '0')
				localizar(table, 'Não encontrado.');
			else
				localizar(table, 'Sem mais ocorrências.');
			pos=0;
		}
	}
	else if (document.layers) {
		find(word,false);
	}
}

/* MENU DE CONTEXTO*/

var gridContextMenuOptions = [[_imgSearch, 'Localizar', function(){ localizar(document.selectedTable); }]];

function gridContextMenu(e){
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY){
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY){
		posx = e.clientX + document.body.scrollLeft;
		posy = e.clientY + document.body.scrollTop;
	}
	
	var divMenu;
	if(document.getElementById('gridContextMenu'))	
		divMenu = document.getElementById('gridContextMenu');
	else{
		divMenu = document.createElement("div");
		divMenu.id = "gridContextMenu";
		divMenu.style.position = "absolute";
		divMenu.style.width = 138;
		divMenu.style.height = 10;
		divMenu.style.border = "outset #FFFFFF 1px";
		divMenu.style.backgroundColor = "#CCCCCC";
		divMenu.style.padding = 2;
		divMenu.style.fontFamily = 'Verdana, Arial, Helvetica, sans-serif';
		divMenu.style.fontSize = 9;
		divMenu.style.cursor = 'default';
		
		divMenu.onblur = function(){
			document.body.removeChild(document.getElementById('gridContextMenu'));
		}
		
		for(var i=0; i<gridContextMenuOptions.length; i++){
			var img = document.createElement("IMG");
			img.src = gridContextMenuOptions[i][0];
			var rotulo = document.createTextNode(gridContextMenuOptions[i][1]);
			var option = document.createElement("div");
			option.appendChild(img);
			option.appendChild(rotulo);
			option.onclick = gridContextMenuOptions[i][2];
			
			divMenu.appendChild(option);
		}
		
		var eBody = document.getElementsByTagName('body').item(0);
		eBody.appendChild(divMenu);
	}
	divMenu.style.left = posx;
	divMenu.style.top = posy;
	
	return false;
}

/* ORDENACAO */
var SORT_COLUMN_INDEX;
/*
function ordenacaoInit() {
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.className+' ').indexOf(cssTabela) != -1) && (thisTbl.id)) {
            habilitarOrdenacao(thisTbl);
        }
    }
}
*/
function habilitarOrdenacao(table) {
	
	var firstRow;
	if (table.rows && table.rows.length > 0) {
        firstRow = table.rows[0];
		
		for (var i=1;i<table.rows.length;i++){
			table.rows[i].className=_cssLine;
			table.rows[i].onclick = function(){
				selectTR(this);
			}
		}
    }
    if (!firstRow) return;
    
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        cell.onclick = function(){
			ordenaTabela(this);
			return false;
		}
		var txt = getInnerText(cell);
        cell.innerHTML = txt+'<span class="'+_cssArrow+'" style="color:#666666; font-weight:bold; font-size:8;">&nbsp;&nbsp;&nbsp;</span>';
		cell.setAttribute("nowrap","nowrap");
		cell.className=_cssHeader;
    }	
}

function processTableKeys(evt) {
    evt = (evt) ? evt : ((event) ? event : null);
    if (evt) {
		switch(evt.keyCode){
			case 40: /*seta p/ baixo*/
				  var table = document.selectedTable;
				  if(table)
					selectTR(table.rows[table.selectedRow.rowIndex+1]);
			  break;
			case 38: /*seta p/ cima */
				  var table = document.selectedTable;
				  if(table && table.selectedRow.rowIndex-1>0)
					selectTR(table.rows[table.selectedRow.rowIndex-1]);
			  break;
		    case 70: /*q */
				  if(evt.ctrlKey && evt.shiftKey)
				  	var table = document.selectedTable;
				  if(table)
					localizar(table);
			  break;
		}
    }
}

function ordenaTabela(cell) {
    var span;
    for (var ci=0;ci<cell.childNodes.length;ci++) {
        if (cell.childNodes[ci].tagName && cell.childNodes[ci].tagName.toLowerCase() == 'span') span = cell.childNodes[ci];
    }
    var spantext = getInnerText(span);
    var td = cell;
    var column = td.cellIndex;
    var table = getParent(td,'TABLE');
    
    if (table.rows.length <= 1) return;
    var itm = getInnerText(table.rows[1].cells[column]);
    ordenaFunction = ordenaCaseInsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) ordenaFunction = ordenaData;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) ordenaFunction = ordenaData;
    if (itm.match(/^[£$]/)) ordenaFunction = ordenaFlutuante;
    if (itm.match(/^[\d\.]+$/)) ordenaFunction = ordenaNumerico;
    SORT_COLUMN_INDEX = column;
    var firstRow = new Array();
    var newRows = new Array();
    for (i=0;i<table.rows[0].length;i++) { firstRow[i] = table.rows[0][i]; }
    for (j=1;j<table.rows.length;j++) { newRows[j-1] = table.rows[j]; }

    newRows.sort(ordenaFunction);

    if (span.getAttribute("sortdir") == 'down') {
        ARROW = '<img src="'+_imgDown+'" width="13" height= "13"/>';
        newRows.reverse();
        span.setAttribute('sortdir','up');
    } else {
        ARROW = '<img src="'+_imgUp+'" width="13" height= "13"/>';
        span.setAttribute('sortdir','down');
    }
    
    for (i=0;i<newRows.length;i++) { if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) table.tBodies[0].appendChild(newRows[i]);}
    for (i=0;i<newRows.length;i++) { if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) table.tBodies[0].appendChild(newRows[i]);}
    
    var allspans = document.getElementsByTagName("span");
    for (var ci=0;ci<allspans.length;ci++) {
        if (allspans[ci].className == _cssArrow) {
            if (getParent(allspans[ci],"table") == getParent(cell,"table")) {
                allspans[ci].innerHTML = '&nbsp;&nbsp;&nbsp;';
            }
        }
    }
        
    span.innerHTML = ARROW;
}

function ordenaData(a,b) {
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa.length == 10) {
        dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
    } else {
        yr = aa.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
    }
    if (bb.length == 10) {
        dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
    } else {
        yr = bb.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
    }
    if (dt1==dt2) return 0;
    if (dt1<dt2) return -1;
    return 1;
}

function ordenaFlutuante(a,b) { 
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    return parseFloat(aa) - parseFloat(bb);
}

function ordenaNumerico(a,b) { 
    aa = parseFloat(getInnerText(a.cells[SORT_COLUMN_INDEX]));
    if (isNaN(aa)) aa = 0;
    bb = parseFloat(getInnerText(b.cells[SORT_COLUMN_INDEX])); 
    if (isNaN(bb)) bb = 0;
    return aa-bb;
}

function ordenaCaseInsensitive(a,b) {
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function ordenaDefault(a,b) {
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

/* FUNCOES UTEIS */
function getTableContentInText(table){
	var text = '';
	if (table.rows && table.rows.length > 0) {
     	for (var i=0;i<table.rows.length;i++){
			if(i!=0)
				text+='\n';
			var row = table.rows[i];
			for (var j=0;j<row.cells.length;j++){
				if(j!=0)
					text+='\t';
				var cell = row.cells[j];
				text += getInnerText(cell);
			}
		}
    }
	return text;
}

function copyClip(text){
	if (window.clipboardData)
		window.clipboardData.setData("Text", text);
	else if (window.netscape) { 
		netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
		var clip = Components.classes['@mozilla.org/widget/clipboard;1']
					 .createInstance(Components.interfaces.nsIClipboard);
		if (!clip) return;
		var trans = Components.classes['@mozilla.org/widget/transferable;1']
					  .createInstance(Components.interfaces.nsITransferable);
		if (!trans) return;
		trans.addDataFlavor('text/unicode');
		var str = new Object();
		var len = new Object();
		var str = Components.classes["@mozilla.org/supports-string;1"]
					.createInstance(Components.interfaces.nsISupportsString);
		var copytext=text;
		str.data=copytext;
		trans.setTransferData("text/unicode",str,copytext.length*2);
		var clipid=Components.interfaces.nsIClipboard;
		if (!clip) return false;		
		clip.setData(trans,null,clipid.kGlobalClipboard);	
	}
	return false;
}

function getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") { return el };
	if (el.innerText) return el.innerText;
	var str = "";
	
	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				str += getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}

function getParent(el, pTagName) {
	if (el == null) return null;
	else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())
		return el;
	else
		return getParent(el.parentNode, pTagName);
}

function addEvent(elm, evType, fn, useCapture){
  if (elm.addEventListener){
    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent){
    var r = elm.attachEvent("on"+evType, fn);
    return r;
  } else {
    ;/*alert("Handler nao encontrado");*/
  }
} 

function getAbsolutePos(el) {
	var SL = 0, ST = 0;
	var is_div = /^div$/i.test(el.tagName);
	if (is_div && el.scrollLeft)
		SL = el.scrollLeft;
	if (is_div && el.scrollTop)
		ST = el.scrollTop;
	var r = { x: el.offsetLeft - SL, y: el.offsetTop - ST };
	if (el.offsetParent) {
		var tmp = getAbsolutePos(el.offsetParent);
		r.x += tmp.x;
		r.y += tmp.y;
	}
	return r;
}

String.PAD_LEFT  = 0;
String.PAD_RIGHT = 1;
String.PAD_BOTH  = 2;

String.prototype.pad = function(size, pad, side) {
  var str = this, append = "", size = (size - str.length);
  var pad = ((pad != null) ? pad : " ");
  if ((typeof size != "number") || ((typeof pad != "string") || (pad == ""))) {
    throw new Error("Parametros errados para o metodo String.pad()!");
  }
  if (side == String.PAD_BOTH) {
    str = str.pad((Math.floor(size / 2) + str.length), pad, String.PAD_LEFT);
    return str.pad((Math.ceil(size / 2) + str.length), pad, String.PAD_RIGHT);
  }
  while ((size -= pad.length) > 0) {
    append += pad;
  }
  append += pad.substr(0, (size + pad.length));
  return ((side == String.PAD_LEFT) ? append.concat(str) : str.concat(append));
}

