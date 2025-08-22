/********* CONSTRÓI VEICULO ***********/

var rbgCor = [];

//ED = [{qtPneusE: n, qtPneusD: n}, ...]
//ET = [{qtPneusE: n, qtPneusD: n}, ...]
function constroiVeiculo(ED, ET){
	limparEixos();
	for(var i=0; i<ED.length; i++)
		inserirEixo('dianteiro', ED[i].qtPneusE, ED[i].qtPneusD);
	
	for(var i=0; i<ET.length; i++)
		inserirEixo('traseiro', ET[i].qtPneusE, ET[i].qtPneusD);
}

function constroiReboque(ED, ET){
	limparEixosReboque();
	for(var i=0; i<ED.length; i++)
		inserirEixo('dianteiro', ED[i].qtPneusE, ED[i].qtPneusD, true);
	
	for(var i=0; i<ET.length; i++)
		inserirEixo('traseiro', ET[i].qtPneusE, ET[i].qtPneusD, true);
}

function inserirEixo(pos, qtPneusE, qtPneusD, isReboque){
	var posicao = (pos==null || pos.toLowerCase() == 'dianteiro')? $('eixosDianteiros' + ((isReboque)?'Reboque':'')) : $('eixosTraseiros' + ((isReboque)?'Reboque':''));
	var eixo = constroiEixo( ((pos==null || pos.toLowerCase() == 'dianteiro')?'ED'+((isReboque)?'R':''):'ET'+((isReboque)?'R':'')) + (posicao.childNodes.length+1), 
							 !qtPneusE?1:qtPneusE, 
							 !qtPneusD?1:qtPneusD);
	posicao.appendChild(eixo);
	return eixo;
}
function limparEixos(){
	$('eixosDianteiros').innerHTML='';
	$('eixosTraseiros').innerHTML='';
}
function limparEixosReboque(){
	$('eixosDianteirosReboque').innerHTML='';
	$('eixosTraseirosReboque').innerHTML='';
}

function constroiEixo(id, qtPneusE, qtPneusD){
	var eixo = document.createElement("div");
	    eixo.id = id;
		eixo.className = 'eixo';
		
	var barra = document.createElement("div");
	    barra.className = 'barra';
	
	var conjuntoEsquerdo = document.createElement("div");
		conjuntoEsquerdo.id = id+'CE';
	    conjuntoEsquerdo.className = 'conjuntoEsquerdo';
		
	for(var i=0; i<qtPneusE; i++){
		var pneuE = document.createElement("div");
			pneuE.id = conjuntoEsquerdo.id+'P'+(i+1);
			pneuE.className = 'pneu';
			pneuE.onmouseover = function(){
				if($(this.id+'Hint'))
					$(this.id+'Hint').style.display='block';
			}
			pneuE.onmouseout = function(){
				if($(this.id+'Hint'))
					$(this.id+'Hint').style.display='none';
			}
			
		conjuntoEsquerdo.appendChild(pneuE);
	}
		
	var conjuntoDireito = document.createElement("div");
		conjuntoDireito.id = id+'CD';
	    conjuntoDireito.className = 'conjuntoDireito';

	for(var i=qtPneusD; i>0; i--){
		var pneuD = document.createElement("div");
			pneuD.id = conjuntoDireito.id+'P'+(i);
			pneuD.className = 'pneu';
			pneuD.onmouseover = function(){
				if($(this.id+'Hint'))
					$(this.id+'Hint').style.display='block';
			}
			pneuD.onmouseout = function(){
				if($(this.id+'Hint'))
					$(this.id+'Hint').style.display='none';
			}
		conjuntoDireito.appendChild(pneuD);
	}
	
	eixo.appendChild(barra);
	eixo.appendChild(conjuntoEsquerdo);
	eixo.appendChild(conjuntoDireito);
	return eixo;
}

function appendHint(id, txt){
	if($(id)){
		var hint = document.createElement("div");
		hint.id = $(id).id+'Hint';
		hint.className = 'hint';
		hint.innerHTML = txt;
			
		var hintArrow = document.createElement("div");
		hintArrow.className = 'hintArrow';
		
		hint.appendChild(hintArrow);
		$(id).appendChild(hint);
	}
}


function clearVehicle(){
	refreshVehicle();
	refreshReboque();
	setDisplayNrPlaca();
}

function enableReboque(){
	var disabled = ($('tpReboque').value==0);
	$('tpCarga').disabled=disabled;
	$('tpCarga').className=(disabled)?'disabledField':'field';
	$('nrCapacidadeReboque').disabled=disabled;
	$('nrCapacidadeReboque').className=(disabled)?'disabledField':'field';
	$('tpEixoDianteiroReboque').disabled=disabled;
	$('tpEixoDianteiroReboque').className=(disabled)?'disabledField':'field';
	$('tpEixoTraseiroReboque').disabled=disabled;
	$('tpEixoTraseiroReboque').className=(disabled)?'disabledField':'field';
	$('qtEixosDianteirosReboque').disabled=disabled;
	$('qtEixosDianteirosReboque').className=(disabled)?'disabledField':'field';
	$('qtEixosTraseirosReboque').disabled=disabled;
	$('qtEixosTraseirosReboque').className=(disabled)?'disabledField':'field';
	
	refreshReboque();
	
}

function refreshVehicle(){
	displayVehicle($('qtEixosDianteiros').value, $('tpEixoDianteiro').value, 
			   $('qtEixosTraseiros').value, $('tpEixoTraseiro').value);

}
function refreshReboque(){
	var disabled = ($('tpReboque').value==0);
	$('reboqueViewport').style.display = (disabled)?'none':'block';

	$('qtEixosDianteirosReboque').value= ($('qtEixosDianteirosReboque').value==0)?1:$('qtEixosDianteirosReboque').value;
	$('qtEixosTraseirosReboque').value= ($('qtEixosTraseirosReboque').value==0)?1:$('qtEixosTraseirosReboque').value;
	displayReboque($('qtEixosDianteirosReboque').value, $('tpEixoDianteiroReboque').value, 
			   $('qtEixosTraseirosReboque').value, $('tpEixoTraseiroReboque').value);
}
function setDisplayNrPlaca(){
	if($('nrPlaca').value != ''){
		$('nrPlaca').value = $('nrPlaca').value.toUpperCase();
		$('displayPlaca').innerHTML = $('nrPlaca').value;
	}
	else
		$('displayPlaca').innerHTML = '&nbsp;';
}

function setDisplayCor(){
	if($('nmCor'))
		$('colorReference').style.backgroundColor = rbgCor[$('nmCor').selectedIndex];
}

function displayVehicle(qtEixosDianteiros, tpEixoDianteiro, qtEixosTraseiros, tpEixoTraseiro){
	var eixosDianteiros = []; 
	var eixosTraseiros = []; 
	for(var i=0; i<qtEixosDianteiros; i++)
		eixosDianteiros.push({qtPneusE: (tpEixoDianteiro==0)?1:2, qtPneusD: (tpEixoDianteiro==0)?1:2});
	for(var i=0; i<qtEixosTraseiros; i++)
		eixosTraseiros.push({qtPneusE: (tpEixoTraseiro==0)?1:2, qtPneusD: (tpEixoTraseiro==0)?1:2});
	constroiVeiculo(eixosDianteiros,eixosTraseiros);
}
function displayReboque(qtEixosDianteiros, tpEixoDianteiro, qtEixosTraseiros, tpEixoTraseiro){
	var eixosDianteiros = []; 
	var eixosTraseiros = []; 
	for(var i=0; i<qtEixosDianteiros; i++)
		eixosDianteiros.push({qtPneusE: (tpEixoDianteiro==0)?1:2, qtPneusD: (tpEixoDianteiro==0)?1:2});
	for(var i=0; i<qtEixosTraseiros; i++)
		eixosTraseiros.push({qtPneusE: (tpEixoTraseiro==0)?1:2, qtPneusD: (tpEixoTraseiro==0)?1:2});
	constroiReboque(eixosDianteiros,eixosTraseiros);
}
