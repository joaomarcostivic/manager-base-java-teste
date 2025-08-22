/*** 
* Descri��o.: formata um campo do formul�rio de 
* acordo com a m�scara informada... 
* Par�metros: - objForm (o Objeto Form) 
* - strField (string contendo o nome 
* do textbox) 
* - sMask (mascara que define o 
* formato que o dado ser� apresentado, 
* usando o algarismo "9" para 
* definir n�meros e o s�mbolo "!" para 
* qualquer caracter... 
* - evtKeyPress (evento) 
* Uso.......: <input type="textbox" 
* name="xxx"..... 
* onkeypress="return txtBoxFormat(document.rcfDownload, 'str_cep', '99999-999', event);"> 
* Observa��o: As m�scaras podem ser representadas como os exemplos abaixo: 
* CEP -> 99.999-999 
* CPF -> 999.999.999-99 
* CNPJ -> 99.999.999/9999-99 
* Data -> 99/99/9999 
* Tel Resid -> (99) 999-9999 
* Tel Cel -> (99) 9999-9999 
* Processo -> 99.999999999/999-99 
* C/C -> 999999-! 
* E por a� vai... 
***/
function fieldFormat(field, sMask, evtKeyPress) {
     var i, nCount, sValue, fldLen, mskLen,bolMask, sCod;
	
	 var nTecla;
     if(document.all) { // Internet Explorer
       nTecla = evtKeyPress.keyCode; 
	 }
     else{ // Nestcape
       nTecla = evtKeyPress.which;
     }
     sValue = field.value;

     // Limpa todos os caracteres de formata��o que
     // j� estiverem no campo.
     sValue = sValue.toString().replace( "-", "" );
     sValue = sValue.toString().replace( ":", "" );
     sValue = sValue.toString().replace( "-", "" );
     sValue = sValue.toString().replace( ".", "" );
     sValue = sValue.toString().replace( ".", "" );
     sValue = sValue.toString().replace( "/", "" );
     sValue = sValue.toString().replace( "/", "" );
     sValue = sValue.toString().replace( "(", "" );
     sValue = sValue.toString().replace( "(", "" );
     sValue = sValue.toString().replace( ")", "" );
     sValue = sValue.toString().replace( ")", "" );
     sValue = sValue.toString().replace( " ", "" );
     sValue = sValue.toString().replace( " ", "" );
     fldLen = sValue.length;
     mskLen = sMask.length;

     i = 0;
     nCount = 0;
     sCod = "";
     mskLen = fldLen;

     while (i <= mskLen) {
       bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/") || (sMask.charAt(i) == ":"))
       bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " "))

       if (bolMask) {
         sCod += sMask.charAt(i);
         mskLen++; }
       else {
         sCod += sValue.charAt(nCount);
         nCount++;
       }

       i++;
     }

     field.value = sCod;
	 if (nTecla == 0)
	 	return true;
     if (nTecla == 8){
		field.value=field.value.substring(0, field.value.length-1)
		return true;
	 }
		
	 if (sMask.charAt(i-1) == "9") { // apenas n�meros...
	    return ((nTecla > 47) && (nTecla < 58)); } // n�meros de 0 a 9
	 else  // qualquer caracter...
	    return true;
}

function txtBoxFormat(objForm, strField, sMask, evtKeyPress) {
     return fieldFormat(objForm[strField], sMask, evtKeyPress);
}
  
function txtFormat(sValue, sMask) {
     var i, nCount, fldLen, mskLen, bolMask, sCod;

     // Limpa todos os caracteres de formata��o que
     // j� estiverem no campo.
     sValue = sValue.toString().replace( "-", "" );
     sValue = sValue.toString().replace( ":", "" );
     sValue = sValue.toString().replace( "-", "" );
     sValue = sValue.toString().replace( ".", "" );
     sValue = sValue.toString().replace( ".", "" );
     sValue = sValue.toString().replace( "/", "" );
     sValue = sValue.toString().replace( "/", "" );
     sValue = sValue.toString().replace( "(", "" );
     sValue = sValue.toString().replace( "(", "" );
     sValue = sValue.toString().replace( ")", "" );
     sValue = sValue.toString().replace( ")", "" );
     sValue = sValue.toString().replace( " ", "" );
     sValue = sValue.toString().replace( " ", "" );
     fldLen = sValue.length;
     mskLen = sMask.length;

     i = 0;
     nCount = 0;
     sCod = "";
     mskLen = fldLen;

     while (i <= mskLen) {
       bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/") || (sMask.charAt(i) == ":"))
       bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " "))

       if (bolMask) {
         sCod += sMask.charAt(i);
         mskLen++; }
       else {
         sCod += sValue.charAt(nCount);
         nCount++;
       }
       i++;
     }
     return sCod;
}