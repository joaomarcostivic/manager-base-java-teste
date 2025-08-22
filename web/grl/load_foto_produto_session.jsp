<meta http-equiv="pragma" content="no-cache">
<%@page import="java.util.*" %>
<%@page import = "org.rawsocket.jspupload.*" %>
<%@page import="sol.util.RequestUtilities"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
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
		HashMap<?,?> mimeParts = fileUpload.getMimeParts();
		MimeBodyPart part = (MimeBodyPart)mimeParts.get("imgFoto");;
		byte[] imgFoto = part==null ? null : part.getBytes();
		session.setAttribute("imgFoto", imgFoto);
		%>
			<script>
				parent.btnSaveFotoProdutoOnClick();
			</script>
		<%
%>
<body class="body">
</body>
<%
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>
</html>