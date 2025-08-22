addEvent(window, "load", ordenacaoInit);

var SORT_COLUMN_INDEX;

var cssTabela = "tabelaOrdenavel";
var cssSeta = "setaColuna"

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

function habilitarOrdenacao(table) {
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    if (!firstRow) return;
    
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        cell.onclick = function(){
			ordenaTabela(this);
			return false;
		}
		var txt = getInnerText(cell);
        cell.innerHTML = txt+'<span class="'+cssSeta+'">&nbsp;&nbsp;&nbsp;</span>';
		cell.setAttribute("nowrap","nowrap");
    }
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

function ordenaTabela(cell) {
    var span;
    for (var ci=0;ci<cell.childNodes.length;ci++) {
        if (cell.childNodes[ci].tagName && cell.childNodes[ci].tagName.toLowerCase() == 'span') span = cell.childNodes[ci];
    }
    var spantext = getInnerText(span);
    var td = cell;
    var column = td.cellIndex;
    var table = getParent(td,'TABLE');
	if (table.rows && table.rows.length <= 1) 
		return;
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
        ARROW = '<img src="imagens/down.gif" width="13" height="13" />';
        newRows.reverse();
        span.setAttribute('sortdir','up');
    } else {
        ARROW = '<img src="imagens/up.gif" width="13" height="13" />';
        span.setAttribute('sortdir','down');
    }
    
    for (i=0;i<newRows.length;i++) { if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) table.tBodies[0].appendChild(newRows[i]);}
    for (i=0;i<newRows.length;i++) { if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) table.tBodies[0].appendChild(newRows[i]);}
    
    var allspans = document.getElementsByTagName("span");
    for (var ci=0;ci<allspans.length;ci++) {
        if (allspans[ci].className == cssSeta) {
            if (getParent(allspans[ci],"table") == getParent(cell,"table")) {
                allspans[ci].innerHTML = '&nbsp;&nbsp;&nbsp;';
            }
        }
    }
        
    span.innerHTML = ARROW;
}

function getParent(el, pTagName) {
	if (el == null) return null;
	else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())
		return el;
	else
		return getParent(el.parentNode, pTagName);
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


function addEvent(elm, evType, fn, useCapture){
  if (elm.addEventListener){
    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent){
    var r = elm.attachEvent("on"+evType, fn);
    return r;
  } else {
    alert("Handler nao pode ser removido");
  }
} 
