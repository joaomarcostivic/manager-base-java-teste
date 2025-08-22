package com.tivic.manager.ptc.fix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class FixResultadoDefesa {
	public static void main(String[] args) {
		try {
			InicializationBeans.init(new InjectApplicationBuilder());
			fixResultadoDefesaPrevia();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	/**
	 * Migração dos dados de Resultado de Defesa Prévia
	 * 
	 * @since 16/05/2022
	 * @author Paulo Lima
	 * @throws IOException 
	 * @return {@link Optional<String>}
	 * @throws Exception 
	 */
	public static void  fixResultadoDefesaPrevia() throws Exception {
		Connection conn = Conexao.conectar();
				
		try {
			int cdDefesa = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA",0,0);
			int cdOcorrenciaDeferida = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA_DOCUMENTO",0,0);
			int cdOcorrenciaIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INDEFERIDA_DOCUMENTO",0,0);
			int cdFaseDeferida = ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDA",0,0);
			int cdFaseIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INDEFERIDA",0,0);

			conn.setAutoCommit(false);
			String queryDocumento = 
					"SELECT * FROM ptc_documento A" + 
					" WHERE A.cd_tipo_documento = "+ cdDefesa + 
					" AND (A.cd_fase = "+ cdFaseDeferida +" OR A.cd_fase = " + cdFaseIndeferida + ")" + 
					" AND NOT EXISTS (SELECT * FROM ptc_documento_ocorrencia B WHERE A.cd_documento = B.cd_documento) order by A.cd_documento desc";
			
			ResultSetMap rsmDocumento = new ResultSetMap(conn.prepareStatement(queryDocumento).executeQuery());
			
			while (rsmDocumento.next()) {
				System.out.println("######### Processando Documento " + rsmDocumento.getString("NR_DOCUMENTO") + " ###########");
				
				Documento documento = DocumentoDAO.get(rsmDocumento.getInt("cd_documento"), conn);
				DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia();
				ocorrencia.setCdDocumento(documento.getCdDocumento());
				ocorrencia.setCdTipoOcorrencia(documento.getCdFase() == cdFaseDeferida ? cdOcorrenciaDeferida : cdOcorrenciaIndeferida);
				ocorrencia.setCdUsuario(documento.getCdUsuario());
				ocorrencia.setDtOcorrencia(documento.getDtProtocolo());
				DocumentoOcorrenciaDAO.insert(ocorrencia, conn);
			}
			
			conn.commit();
		} catch (SQLException e) {
			Conexao.rollback(conn);
			System.out.println(e.getMessage());
			
		} finally {
			Conexao.desconectar(conn);
		}
	}
}
