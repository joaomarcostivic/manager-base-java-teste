package com.tivic.manager.ptc.fix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class FixDefesaPrevia {

	public static void main(String[] args) {
		try {
			InicializationBeans.init(new InjectApplicationBuilder());
			fixDefesaPrevia();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	/**
	 * Migração dos dados de Defesa Prévia
	 * 
	 * @since 13/05/2022
	 * @author Paulo Lima
	 * @throws IOException 
	 * @return {@link Optional<String>}
	 * @throws Exception 
	 */
	public static void  fixDefesaPrevia() throws Exception {
		Connection conn = Conexao.conectar();
				
		try {
			int cdDefesa = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA",0,0);
			conn.setAutoCommit(false);
			String queryDocumento = 
					"SELECT A.* " +
					"FROM ptc_documento A " +
					"WHERE A.cd_tipo_documento = "+ cdDefesa +
					"  AND EXISTS (SELECT cd_documento FROM grl_formulario_atributo WHERE cd_documento = A.cd_documento)";
			
			ResultSetMap rsmDocumento = new ResultSetMap(conn.prepareStatement(queryDocumento).executeQuery());
			
			while (rsmDocumento.next()) {
				int cdDocumento = rsmDocumento.getInt("cd_documento");
				
				HashMap<String, Object> atributosValor = getFormularioByDocumento(cdDocumento, conn);			
				
				if(atributosValor == null)
					continue;
				
				System.out.println("######### Processando Documento " + rsmDocumento.getString("NR_DOCUMENTO") + " ###########");
				Documento documento = DocumentoDAO.get(cdDocumento, conn);
				documento.setNmRequerente(atributosValor.get("nmRequerente") != null ?
						String.valueOf(atributosValor.get("nmRequerente")) :
						"");
				documento.setTxtObservacao(atributosValor.get("txtRequisicao") != null ?
						String.valueOf(atributosValor.get("txtRequisicao")) :
						"");
				DocumentoDAO.update(documento, conn);
			}
			
			conn.commit();
		} catch (SQLException e) {
			Conexao.rollback(conn);
			System.out.println(e.getMessage());
			
		} finally {
			Conexao.desconectar(conn);
		}
	}
	
	private static HashMap<String, Object> getFormularioByDocumento(int cdDocumento, Connection conn) {
		try {
			String queryForm = 
					"SELECT cd_documento, nm_formulario, nm_atributo, txt_atributo_valor " +
					"FROM grl_formulario A, grl_formulario_atributo B, grl_formulario_atributo_valor C " +
					"WHERE A.cd_formulario = B.cd_formulario " +
					"AND B.cd_formulario_atributo = C.cd_formulario_atributo " +
					"AND C.cd_documento = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(queryForm);
			pstmt.setInt(1, cdDocumento);
			
			HashMap<String, Object> atributosValor = new HashMap<String, Object>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.size() == 0) {
				return null;
			}
			
			while(rsm.next()) {
				atributosValor.put(rsm.getString("NM_ATRIBUTO"), rsm.getString("TXT_ATRIBUTO_VALOR"));
			}
			
			return atributosValor;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}
