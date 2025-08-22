package com.tivic.manager.adm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class OrcamentoCategoriaServices {

	public static Result save(OrcamentoCategoria orcamentoCategoria){
		return save(orcamentoCategoria, null, null);
	}

	public static Result save(OrcamentoCategoria orcamentoCategoria, AuthData authData){
		return save(orcamentoCategoria, authData, null);
	}

	public static Result save(OrcamentoCategoria orcamentoCategoria, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(orcamentoCategoria==null)
				return new Result(-1, "Erro ao salvar. OrcamentoCategoria é nulo");

			int retorno;
			OrcamentoCategoria tmpOrcamentoCat = OrcamentoCategoriaDAO.get( orcamentoCategoria.getCdOrcamento(),
																			orcamentoCategoria.getCdCategoriaEconomica(),
																			orcamentoCategoria.getCdCompetencia());
			if( tmpOrcamentoCat == null ){
				retorno = OrcamentoCategoriaDAO.insert(orcamentoCategoria, connect);
				orcamentoCategoria.setCdOrcamento(retorno);
			}
			else {
				retorno = OrcamentoCategoriaDAO.update(orcamentoCategoria, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORCAMENTOCATEGORIA", orcamentoCategoria);
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
	
	public static Result save(Orcamento orcamento, ArrayList<OrcamentoCategoria> orcamentoCategoriaList){
		return save(orcamento, orcamentoCategoriaList, null, null);
	}
	
	public static Result save(Orcamento orcamento, ArrayList<OrcamentoCategoria> orcamentoCategoriaList, AuthData authData){
		return save(orcamento, orcamentoCategoriaList, authData, null);
	}
	
	public static Result save(Orcamento orcamento, ArrayList<OrcamentoCategoria> orcamentoCategoriaList, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(orcamentoCategoriaList==null)
				return new Result(-1, "Erro ao salvar. orcamentoCategoriaList é nulo");
			
			Result r;
			ArrayList<Integer> orcamentoCatKeys = new ArrayList<Integer>();
			for(int i=0;i<orcamentoCategoriaList.size();i++){
				r = save(orcamentoCategoriaList.get(i), authData, connect);
				if( r.getCode() <= 0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar planejamento.");
				}
				orcamentoCatKeys.add( ((OrcamentoCategoria) r.getObjects().get("ORCAMENTOCATEGORIA")).getCdCategoriaEconomica() );
			}
			OrcamentoServices.save(orcamento, null, connect);
			connect.prepareStatement(
					" DELETE FROM ADM_ORCAMENTO_CATEGORIA "+
					" WHERE CD_ORCAMENTO = "+orcamento.getCdOrcamento()+
					" AND CD_CATEGORIA_ECONOMICA NOT IN ( "+Util.join(orcamentoCatKeys)+" ) "
				).execute();
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1,"Planejamento registrado com sucesso.");
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
	
	public static Result remove(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia){
		return remove(cdOrcamento, cdCategoriaEconomica, cdCompetencia, false, null, null);
	}
	public static Result remove(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia, boolean cascade){
		return remove(cdOrcamento, cdCategoriaEconomica, cdCompetencia, cascade, null, null);
	}
	public static Result remove(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia, boolean cascade, AuthData authData){
		return remove(cdOrcamento, cdCategoriaEconomica, cdCompetencia, cascade, authData, null);
	}
	public static Result remove(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OrcamentoCategoriaDAO.delete(cdOrcamento, cdCategoriaEconomica, cdCompetencia, connect);
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
	
	public static ResultSetMap getAllByOrcamento(int cdOrcamento ) {
		return getAllByOrcamento(cdOrcamento, null);
	}

	public static ResultSetMap getAllByOrcamento(int cdOrcamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsmCategoriaEconomica = CategoriaEconomicaServices.getAllHierarquia();
			rsmCategoriaEconomica.beforeFirst();
			setOrcamentoCategoria(cdOrcamento, rsmCategoriaEconomica, connect);
			return rsmCategoriaEconomica;	
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaServices.getAllByExercicio: " + e);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap setOrcamentoCategoria( int cdOrcamento,  ResultSetMap rsm ){
		return setOrcamentoCategoria( cdOrcamento, rsm, null);
	}
	private static ResultSetMap setOrcamentoCategoria( int cdOrcamento, ResultSetMap rsm, Connection connect ){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			while( rsm.next() ){
				rsm.setValueToField("ORCAMENTO",
						 new ResultSetMap(
									connect.prepareStatement(
												" SELECT * FROM ctb_competencia A "+
												" LEFT JOIN adm_orcamento_categoria B on "+
												"					( A.cd_competencia = B.cd_competencia "+
												"                        AND A.cd_exercicio = B.cd_exercicio "+
												"                        AND B.cd_categoria_economica = "+
																				rsm.getInt("CD_CATEGORIA_ECONOMICA")+
												"                        AND ( B.cd_orcamento = "+cdOrcamento+
												"                                  OR                       "+
												"							   B.cd_orcamento is null       "+							
												"							 )  						    "+							
												"                   ) 										"+
												" LEFT JOIN adm_orcamento C on ( B.cd_orcamento = C.cd_orcamento ) "+
												" ORDER BY A.DT_INICIAL "
											).executeQuery()));
				
				if( rsm.getRegister().containsKey("subResultSetMap") ){
					((ResultSetMap) rsm.getRegister().get("subResultSetMap")).beforeFirst();
					setOrcamentoCategoria(cdOrcamento, (ResultSetMap) rsm.getRegister().get("subResultSetMap"), connect );
				}
			}
			return rsm;	
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaServices.getAllByExercicio: " + e);
			return null;
		}finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_orcamento_categoria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_orcamento_categoria", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}