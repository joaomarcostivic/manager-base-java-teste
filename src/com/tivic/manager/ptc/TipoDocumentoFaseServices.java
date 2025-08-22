package com.tivic.manager.ptc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class TipoDocumentoFaseServices {
	
	public static int TP_ASSOCIACAO_FASE_TIPO_DOCUMENTO = 0;
	public static int TP_ASSOCIACAO_TIPO_DOCUMENTO_FASE = 1;
	
	public static int LG_OBRIGATORIO_NAO = 0;
	public static int LG_OBRIGATORIO_SIM = 1;

	public static Result save(ArrayList<TipoDocumentoFase> arrayTipoDocumentoFases){
		Result retorno = null;
		for (TipoDocumentoFase tipoDocumentoFase : arrayTipoDocumentoFases) {
			retorno = save(tipoDocumentoFase, null, null);
			if(retorno==null)
				return new Result(-1, "Erro ao salvar. TipoDocumentoFase é nulo");
		}
		
		return new Result(1, "Array de Tipos de documentos Salvos com sucesso!");
	}

	public static Result save(TipoDocumentoFase tipoDocumentoFase){
		return save(tipoDocumentoFase, null, null);
	}
	
	public static Result save(TipoDocumentoFase tipoDocumentoFase, AuthData authData){
		return save(tipoDocumentoFase, authData, null);
	}

	public static Result save(TipoDocumentoFase tipoDocumentoFase, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoDocumentoFase==null)
				return new Result(-1, "Erro ao salvar. TipoDocumentoFase é nulo");

			int retorno;
			
			/**
			 * Valida a existência dos registros passados
			 */
			ResultSetMap rsmValidacao = new ResultSetMap( 
					connect.prepareStatement("SELECT * FROM ptc_tipo_documento_fase " +
						                     " WHERE cd_tipo_documento = "+ tipoDocumentoFase.getCdTipoDocumento() +
						                     "   AND cd_fase = "+tipoDocumentoFase.getCdFase() +
						                     "   AND tp_associacao = " + tipoDocumentoFase.getTpAssociacao()).executeQuery());
			//Update
			if( rsmValidacao.next()){
				retorno = TipoDocumentoFaseDAO.update(tipoDocumentoFase, connect);
			}else {
				retorno = TipoDocumentoFaseDAO.insert(tipoDocumentoFase, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODOCUMENTOFASE", tipoDocumentoFase);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(TipoDocumentoFase tipoDocumentoFase) {
		return remove(tipoDocumentoFase.getCdTipoDocumento(), tipoDocumentoFase.getCdFase());
	}
	public static Result remove(int cdTipoDocumento, int cdFase){
		return remove(cdTipoDocumento, cdFase, false, null, null);
	}
	public static Result remove(int cdTipoDocumento, int cdFase, boolean cascade){
		return remove(cdTipoDocumento, cdFase, cascade, null, null);
	}
	public static Result remove(int cdTipoDocumento, int cdFase, boolean cascade, AuthData authData){
		return remove(cdTipoDocumento, cdFase, cascade, authData, null);
	}
	public static Result remove(int cdTipoDocumento, int cdFase, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = TipoDocumentoFaseDAO.delete(cdTipoDocumento, cdFase, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_documento_fase");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM ptc_tipo_documento_fase A " +
						   "LEFT OUTER JOIN ptc_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento) " +
						   "LEFT OUTER JOIN gpn_tipo_documento C ON (B.cd_tipo_documento = C.cd_tipo_documento) " +
						   "LEFT OUTER JOIN ptc_fase 		   D ON (A.cd_fase = D.cd_fase) ", " ORDER BY A.nr_ordem ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}