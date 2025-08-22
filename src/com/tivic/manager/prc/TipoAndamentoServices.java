package com.tivic.manager.prc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

public class TipoAndamentoServices {
	
	public static final int ST_CADASTRO_INATIVO = 0;
	public static final int ST_CADASTRO_ATIVO = 1;

	public static Result save(TipoAndamento tipoAndamento){
		return save(tipoAndamento, null, null);
	}

	public static Result save(TipoAndamento tipoAndamento, AuthData authData){
		return save(tipoAndamento, authData, null);
	}

	public static Result save(TipoAndamento tipoAndamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoAndamento==null)
				return new Result(-1, "Erro ao salvar. TipoAndamento é nulo");

			int retorno;
			if(tipoAndamento.getCdTipoAndamento()==0){
				retorno = TipoAndamentoDAO.insert(tipoAndamento, connect);
				tipoAndamento.setCdTipoAndamento(retorno);
			}
			else {
				retorno = TipoAndamentoDAO.update(tipoAndamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOANDAMENTO", tipoAndamento);
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
	public static Result remove(TipoAndamento tipoAndamento) {
		return remove(tipoAndamento.getCdTipoAndamento());
	}
	public static Result remove(int cdTipoAndamento){
		return remove(cdTipoAndamento, false, null, null);
	}
	public static Result remove(int cdTipoAndamento, boolean cascade){
		return remove(cdTipoAndamento, cascade, null, null);
	}
	public static Result remove(int cdTipoAndamento, boolean cascade, AuthData authData){
		return remove(cdTipoAndamento, cascade, authData, null);
	}
	public static Result remove(int cdTipoAndamento, boolean cascade, AuthData authData, Connection connect){
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
			
			PreparedStatement pstmt = connect.prepareStatement(
					  " UPDATE prc_tipo_andamento SET cd_tipo_andamento_superior = NULL "
					+ "	WHERE cd_tipo_andamento_superior = "+cdTipoAndamento);
			retorno = pstmt.executeUpdate();
			
			if(!cascade || retorno>=0)
				retorno = TipoAndamentoDAO.delete(cdTipoAndamento, connect);
			
			if(retorno<0){
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
	
	
	public static TipoAndamento get(int cdTipoAndamento) {
		return TipoAndamentoDAO.get(cdTipoAndamento);
	}
	
	public static ResultSetMap getAllAtivos() {
		return getAll(-1, 1, null);
	}
	
	public static ResultSetMap getAllAtivos(Connection connect) {
		return getAll(-1, 1, connect);
	}

	public static ResultSetMap getAll() {
		return getAll(-1, -1, null);
	}
	
	public static ResultSetMap getAll(int stTipoAndamento) {
		return getAll(stTipoAndamento, -1, null);
	}
	
	public static ResultSetMap getAll(int stTipoAndamento, int stCadastro) {
		return getAll(stTipoAndamento, stCadastro, null);
	}

	public static ResultSetMap getAll(int stTipoAndamento, int stCadastro, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_situacao,"
											 + " C.nm_tipo_andamento AS nm_tipo_andamento_superior "
											 + " FROM prc_tipo_andamento A " 
											 + " LEFT OUTER JOIN prc_tipo_situacao B ON (B.cd_tipo_situacao = A.cd_tipo_situacao) "
											 + " LEFT OUTER JOIN prc_tipo_andamento C ON (A.cd_tipo_andamento_superior = C.cd_tipo_andamento)"
											 + " WHERE 1=1 " 
											 + (stTipoAndamento==-1 ? "" :	
												 " AND  ((A.ST_TIPO_ANDAMENTO is null OR A.ST_TIPO_ANDAMENTO = 0) OR A.ST_TIPO_ANDAMENTO = " + (stTipoAndamento > -1 ? stTipoAndamento : 1)+")") 
											 + (stCadastro>-1 ? " AND A.st_cadastro="+stCadastro : "")
					                         + " ORDER BY A.st_cadastro DESC, A.nm_tipo_andamento");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getHierarquia() {
		return getHierarquia(-1, -1, null);
	}
	
	public static ResultSetMap getHierarquia(int stTipoAndamento, int stCadastro) {
		return getHierarquia(stTipoAndamento, stCadastro, null, null);
	}
	
	public static ResultSetMap getHierarquia(int stTipoAndamento, int stCadastro, String nmTipoAndamento) {
		return getHierarquia(stTipoAndamento, stCadastro, nmTipoAndamento, null);
	}
	
	public static ResultSetMap getHierarquia(int stTipoAndamento, int stCadastro, String nmTipoAndamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			if(nmTipoAndamento!=null)
				nmTipoAndamento = Util.limparAcentos(nmTipoAndamento.toUpperCase(), null);
			
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_situacao "
											 + " FROM prc_tipo_andamento A " 
											 + " LEFT OUTER JOIN prc_tipo_situacao B ON (B.cd_tipo_situacao = A.cd_tipo_situacao) "
											 + " WHERE 1=1 AND A.cd_tipo_andamento_superior IS NULL"
											 + (stTipoAndamento==-1 ? "" :	
												 " AND  ((A.ST_TIPO_ANDAMENTO is null OR A.ST_TIPO_ANDAMENTO = 0) OR A.ST_TIPO_ANDAMENTO = " + (stTipoAndamento > -1 ? stTipoAndamento : 1)+")") 
											 + (stCadastro>-1 ? " AND A.st_cadastro="+stCadastro : "")
											 + (nmTipoAndamento==null ? "" : 
												 " AND (TRANSLATE(A.nm_tipo_andamento, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+nmTipoAndamento+"%'"
											 +   "      OR EXISTS (SELECT Z.* "
											 + "				   FROM prc_tipo_andamento Z"
											 + " 				   WHERE A.cd_tipo_andamento = Z.cd_tipo_andamento_superior"
											 + " 				   AND TRANSLATE(Z.nm_tipo_andamento, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+nmTipoAndamento+"%')"
											 + "       )")
					                         + " ORDER BY A.st_cadastro DESC, A.nm_tipo_andamento");
			
			ResultSetMap rsmRoot = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmRoot.next()) {
				pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_situacao,"
						 + " C.nm_tipo_andamento AS nm_tipo_andamento_superior "
						 + " FROM prc_tipo_andamento A " 
						 + " LEFT OUTER JOIN prc_tipo_situacao B ON (B.cd_tipo_situacao = A.cd_tipo_situacao) "
						 + " LEFT OUTER JOIN prc_tipo_andamento C ON (A.cd_tipo_andamento_superior = C.cd_tipo_andamento)"
						 + " WHERE 1=1 AND A.cd_tipo_andamento_superior="+rsmRoot.getInt("cd_tipo_andamento") 
						 + (stTipoAndamento==-1 ? "" :	
							 " AND  ((A.ST_TIPO_ANDAMENTO is null OR A.ST_TIPO_ANDAMENTO = 0) OR A.ST_TIPO_ANDAMENTO = " + (stTipoAndamento > -1 ? stTipoAndamento : 1)+")") 
						 + (stCadastro>-1 ? " AND A.st_cadastro="+stCadastro : "")
						 + (nmTipoAndamento!=null ? " AND TRANSLATE(A.nm_tipo_andamento, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+nmTipoAndamento+"%'" : "")
                         + " ORDER BY A.st_cadastro DESC, A.nm_tipo_andamento");
				
				ResultSetMap rsmLeaf = new ResultSetMap(pstmt.executeQuery());
				if(rsmLeaf.next()) {
					rsmLeaf.beforeFirst();
					
					rsmRoot.setValueToField("RSM_TIPO_ANDAMENTO_FOLHA", rsmLeaf);
				}
			}
			
			return rsmRoot;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAndamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_tipo_andamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}