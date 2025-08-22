<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
function initMarca(){
	FormFactory.createQuickForm('jGrupoMarca', {
		caption: 'Cadastro e Manutenção de Grupos de Marcas', width: 305, height: 225, unitSize: '%',
		id: 'adm_marca', classDAO: 'com.tivic.manager.bpm.GrupoMarcaDAO', keysFields: ['cd_grupo'],	
		noTitle: true,
		constructorFields: [{name: 'cd_grupo', type: 'int', reference:'cd_grupo'},
		                    {name: 'nm_grupo', type: 'java.lang.String', reference:'nm_grupo'}
		],
		gridFields: [{name: 'cd_grupo', label: 'ID'}],
		editFields: [{name: 'nm_grupo', label: 'Nome', line: 1, width:50, maxLength:50}],
		gridOptions: {columns: [{reference: 'cd_grupo', label: 'Ordem'},
		                        {reference: 'nm_grupo', label: 'Nome do Grupo'}],
		                        strippedLines: true, columnSeparator: false, lineSeparator: false},
        lines: [[{reference: 'nm_grupo', label: 'Nome', width:100, maxLength:50}]]
	});
	document.getElementById('jGrupoMarca').style.left = '0px';
}

</script>
</head>
<body class="body" onload="initMarca();" style="left:-10px;">
</body>
</html>
