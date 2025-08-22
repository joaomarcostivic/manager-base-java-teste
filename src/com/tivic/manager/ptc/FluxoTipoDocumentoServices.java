package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FluxoTipoDocumentoServices {
	public static Result save(FluxoTipoDocumento tipoDocumento){
		return save(tipoDocumento, null);
	}
	
	public static Result save(FluxoTipoDocumento tipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoDocumento==null)
				return new Result(-1, "Erro ao salvar. Tipo de Documento é nulo");
			
			// Verifica se já existe tipo de documento no fluxo
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_fluxo_tipo_documento A " +
					                                "WHERE A.cd_fluxo = " +tipoDocumento.getCdFluxo()+
					   								"  AND A.cd_tipo_documento ="+tipoDocumento.getCdTipoDocumento()).executeQuery();
			if(rs.next())
				return new Result(-1, "Este tipo de documento já foi incluído no fluxo.");
			
			int retorno = FluxoTipoDocumentoDAO.insert(tipoDocumento, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FLUXOTIPODOCUMENTO", tipoDocumento);
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
	
	public static Result remove(int cdFluxo, int cdTipoDocumento){
		return remove(cdFluxo, cdTipoDocumento, false, null);
	}
	
	public static Result remove(int cdFluxo, int cdTipoDocumento, boolean cascade){
		return remove(cdFluxo, cdTipoDocumento, cascade, null);
	}
	
	public static Result remove(int cdFluxo, int cdTipoDocumento, boolean cascade, Connection connect){
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
				retorno = FluxoTipoDocumentoDAO.delete(cdFluxo, cdTipoDocumento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFluxo(int cdFluxo) {
		return getAllByFluxo(cdFluxo, null);
	}

	public static ResultSetMap getAllByFluxo(int cdFluxo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_fluxo_tipo_documento A " +
					" JOIN ptc_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento)" +
					" WHERE cd_fluxo = "+cdFluxo+
					" ORDER BY B.nm_tipo_documento").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM ptc_fluxo_tipo_documento A " +
				" JOIN ptc_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento)", "ORDER BY B.nm_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
