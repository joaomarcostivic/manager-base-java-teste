/* Classe HashMap */
function HashMap(){
	this.register=new Array();
}

function HashMap_put(key, value){
	this.register.push(new Array(key,value));
}
HashMap.prototype.put = HashMap_put;

function HashMap_getValue(key){
	var i=0;
	for(i=0; i<this.register.length; i++){
		if(this.register[i][0]==key.toUpperCase()){
			return this.register[i][1];
		}
	}
	return null;
}
HashMap.prototype.getValue = HashMap_getValue;

function HashMap_toString(register){
	str='';
	var i=0;
	for(i=0; i<this.register.length; i++){
		str+='['+this.register[i][0]+']='+this.register[i][1]+', ';
	}
	return str;
}
HashMap.prototype.toString = HashMap_toString;
/* fim classe HashMap*/
