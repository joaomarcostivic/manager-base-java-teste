<meta http-equiv="pragma" content="no-cache">
<%@page import="java.util.*" %>
<%@page import = "org.rawsocket.jspupload.*" %>
<%@page import="sol.util.RequestUtilities"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form" compress="false" />
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script>
function btOkOnClick() {
	document.form.cdAcao.value = '1';
	document.form.submit();
}
</script>
</head>
<%
	try {
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.upload();
		int cdAcao = RequestUtilities.getParameterAsInteger(request, "cdAcao", 0, fileUpload);
		String idSession = RequestUtilities.getParameterAsString(request, "idSession", "", fileUpload, true);
		String idContentLoad = RequestUtilities.getParameterAsString(request, "idContentLoad", "", fileUpload, true);
		if (cdAcao==1) {
			HashMap mimeParts = fileUpload.getMimeParts();
			MimeBodyPart part = (MimeBodyPart)mimeParts.get("srcImage");;
			byte[] bytesImage = part==null ? null : part.getBytes();
			session.setAttribute(idSession, bytesImage);
			%>
				<script>
					var contentLoad = parent.document.getElementById('<%=idContentLoad%>');
					if (contentLoad!= null) {
						if (contentLoad.src)
							contentLoad.src = 'preview_imagem.jsp?idSession=<%=idSession%>';
					}
					parent.closeWindow('jLoadImagem');
				</script>
			<%
		}
%>
<body class="body" style="padding:2px 4px 2px 4px">
<form action="load_imagem.jsp" method="post" enctype="multipart/form-data" name="form" target="_self">
  <input name="idSession" id="idSession" type="hidden" value="<%=idSession%>">
  <input name="cdAcao" id="cdAcao" type="hidden" value="0">
  <input name="idContentLoad" id="idContentLoad" type="hidden" value="<%=idContentLoad%>">
  <div>
    <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px">Informe o nome do arquivo:</label>
  </div>
  <div style="float:left">
    <input type="file" id="srcImage" name="srcImage" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px; border:1px solid"/>
  </div>
  <div>
    <button type="button" name="btOk" id="btOk" style="width:90px; height:16px; padding:0px; border:1px solid #000000; cursor:pointer" onClick="btOkOnClick()">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right"><img src="/sol/imagens/check_13.gif"></td>
        <td nowrap style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px">Carregar Imagem </td>
      </tr>
    </table>
    </button>
  </div>
</form>
</body>
<%
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>
</html>