package com.tivic.manager.util.ddl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.grl.ParametroValorDAO;
import com.tivic.manager.util.XmlServices;

import sol.util.Result;

public class DDLCore {

	public void executar(String caminhoArquivo) throws SQLException, Exception {
		System.out.println("Atualizando banco de dados...");
		Connection connect = Conexao.conectar();
		int cdParametro = getCdParametroRelease(connect);
		PreparedStatement pstmt = createStatementeRelease(cdParametro, connect);
		List<?> itens = getItensFromXml(caminhoArquivo);
		for(int i=0; i<itens.size(); i++) {
			 Element e = (Element)itens.get(i);
		     String idRelease 		= e.getAttributeValue("id");
		     List<?> ddls 	 	    = e.getChildren("DDL");
			 if(!releaseJaExecutado(pstmt, idRelease))	{
	    		 for(int l=0; l<ddls.size(); l++) {
			    	 String ddl    = ((Element)ddls.get(l)).getText();
			    	 executarDdl(e, ddl, connect);
		    	 }
				 gravarParametroValorRelease(cdParametro, e, ddls, connect);
		     }
	    	 else
	    		 System.out.println("Release: "+idRelease+" ja executado!");
		}
	}

	private void gravarParametroValorRelease(int cdParametro, Element e, List<?> ddls, Connection connect) throws Exception {
		ParametroValor pValue = new ParametroValor(cdParametro,0 /*cdValor*/,0 /*cdOpcao*/,0 /*cdEmpresa*/,0 /*cdPessoa*/,
				("Release "+e.getAttributeValue("id")+"\n"+
			 	 "Autor: "+e.getAttributeValue("autor")+"\n"+
				 "Data: "+e.getAttributeValue("date")+"\n"+
				 "Descrição: "+e.getAttributeValue("description")+"\n"+
				 "DDL´s: "+ddls).getBytes(),
				 e.getAttributeValue("id") /*vlInicial*/, e.getAttributeValue("date") /*vlFinal*/);
		int cdValor = ParametroValorDAO.insert(pValue, connect);
		if(cdValor <= 0)
			throw new Exception("Erro ao gravar parametro valor de release");
	}

	private int getCdParametroRelease(Connection connection) throws SQLException{
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = \'RELEASES\'");
		ResultSet rs            = pstmt.executeQuery();
		int cdParametro         = rs.next() ? rs.getInt("cd_parametro") : 0;
		if(cdParametro <= 0){
			cdParametro = ParametroDAO.insert(new Parametro(0, 0, "RELEASES", ParametroServices.STRING, ParametroServices.COMBO_BOX,
				      "Controle de Releases", "", 0/*cdPessoa*/, ParametroServices.TP_GERAL, 0, 0, ParametroServices.NIVEL_ACESSO_OPERADOR));

		}
		return cdParametro;
	}
	
	private PreparedStatement createStatementeRelease(int cdParametro, Connection connect) throws SQLException {
		PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro_valor " +
				                         "WHERE cd_parametro = "+cdParametro+
				                         "  AND vl_inicial   = ?");
		return pstmt;
	}
	

	private List<?> getItensFromXml(String caminhoArquivo) {
		Result ret   = XmlServices.loadJdomXML("file://localhost/"+XmlServices.class.getResource(caminhoArquivo).getPath().substring(1));
		Document doc = (Document)ret.getObjects().get("doc");
		Element root = doc.getRootElement();
		return root.getChildren("release");
	}

	private boolean releaseJaExecutado(PreparedStatement pstmt, String idRelease) throws SQLException {
		pstmt.setString(1, idRelease);
   	 	ResultSet rs = pstmt.executeQuery();
   	 	return rs.next();
	}

	private void executarDdl(Element e, String ddl, Connection connection) {
		System.out.println("***************************** EXECUTANDO DDL *****************************\n" +
                 "\tRelease "+e.getAttributeValue("id")+"\n"+
                 "\tAutor: "+e.getAttributeValue("autor")+"\n"+
                 "\tData: "+e.getAttributeValue("date")+"\n"+
                 "\tDescrição: "+e.getAttributeValue("description")+"\n"+
                 "\tDDL: "+ddl);
		try	{
			connection.prepareStatement(ddl).executeUpdate();
		}
		catch(Exception erro){
			erro.printStackTrace(System.out);
		}
	}

}
