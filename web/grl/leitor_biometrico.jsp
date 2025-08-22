<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
</head>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.ConfManager" %>
<%@page import="com.tivic.manager.util.Util" %>
<%@page import="java.util.*" %>
<%
	try {
		int cdPessoa = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
		
		ConfManager conf = Util.getConfManager();
		HashMap dbInfo = conf.getResourceOfDbUsed();
		Properties props = conf.getProps();

		String driver 	= (String)dbInfo.get("DRIVER");
		String dbserver = (String)props.get("DBSERVER");
		String login 	= (String)dbInfo.get("LOGIN");
		String pass 	= (String)dbInfo.get("PASS");
%>
<body style="margin:0 3px 3px 3px; background-color:Menu">
<applet code="dotBiometric.applet.FormMain"
    archive="/sol/gadgets/dotBiometric/SignedFingerprintSDKJava.jar,/sol/gadgets/dotBiometric/postgresql.jar,/sol/gadgets/dotBiometric/dotBiometric.jar"
    width="300" height="380">
    <param name="DRIVER" value="<%=driver%>" />
    <param name="PATH" value="jdbc:postgresql://<%=dbserver%>/dotManager" />
    <param name="USER" value="<%=login%>" />
    <param name="PASSWORD" value="<%=pass%>" />
    <param name="CD_PESSOA" value="<%=cdPessoa%>" />
</applet>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
