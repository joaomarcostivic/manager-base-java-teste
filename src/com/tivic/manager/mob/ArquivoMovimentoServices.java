package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.util.ResultSetMapper;


public class ArquivoMovimentoServices {

	public static Result save(ArquivoMovimento arquivoMovimento){
		return save(arquivoMovimento, null, null);
	}

	public static Result save(ArquivoMovimento arquivoMovimento, AuthData authData){
		return save(arquivoMovimento, authData, null);
	}

	public static Result save(ArquivoMovimento arquivoMovimento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(arquivoMovimento==null)
				return new Result(-1, "Erro ao salvar. ArquivoMovimento é nulo");

			int retorno;
			if(arquivoMovimento.getCdArquivoMovimento() == 0){
				retorno = ArquivoMovimentoDAO.insert(arquivoMovimento, connect);
				arquivoMovimento.setCdMovimento(retorno);
			}
			else {
				retorno = ArquivoMovimentoDAO.update(arquivoMovimento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ARQUIVOMOVIMENTO", arquivoMovimento);
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
	public static Result remove(ArquivoMovimento arquivoMovimento) {
		return remove(arquivoMovimento.getCdArquivoMovimento(),  arquivoMovimento.getCdMovimento(), arquivoMovimento.getCdAit());
	}
	public static Result remove(int cdArquivoMovimento, int cdMovimento, int cdAit){
		return remove(cdArquivoMovimento, cdMovimento, cdAit, false, null, null);
	}
	public static Result remove(int cdArquivoMovimento, int cdMovimento, int cdAit, boolean cascade){
		return remove(cdArquivoMovimento, cdMovimento, cdAit, cascade, null, null);
	}
	public static Result remove(int cdArquivoMovimento, int cdMovimento, int cdAit, boolean cascade, AuthData authData){
		return remove(cdArquivoMovimento, cdMovimento, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdArquivoMovimento, int cdMovimento, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ArquivoMovimentoDAO.delete(cdArquivoMovimento, cdMovimento, cdAit, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_arquivo_movimento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArquivoMovimento getUltimoArquivoMovimento(int cdAit, int cdMovimento) {
		return getUltimoArquivoMovimento(cdAit, cdMovimento, null);
	}

	public static ArquivoMovimento getUltimoArquivoMovimento(int cdAit, int cdMovimento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_arquivo_movimento WHERE cd_ait = ? and cd_movimento = ? ORDER BY cd_arquivo_movimento DESC LIMIT 1");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdMovimento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				List<ArquivoMovimento> arquivosMovimento = new ResultSetMapper<ArquivoMovimento>(rsm, ArquivoMovimento.class).toList();
				return arquivosMovimento.get(0);
			}
			
			return null;
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArquivoMovimento getUltimoArquivoMovimentoStatus(int cdAit, int tpStatus) {
		return getUltimoArquivoMovimentoStatus(cdAit, tpStatus, null);
	}

	public static ArquivoMovimento getUltimoArquivoMovimentoStatus(int cdAit, int tpStatus, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_arquivo_movimento WHERE cd_ait = ? AND tp_status = ? ORDER BY cd_arquivo_movimento DESC LIMIT 1");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, tpStatus);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				List<ArquivoMovimento> arquivosMovimento = new ResultSetMapper<ArquivoMovimento>(rsm, ArquivoMovimento.class).toList();
				return arquivosMovimento.get(0);
			}
			
			return null;
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find(
				  " SELECT ARQ_MOV.*, AIT_MOV.*, AIT.*, ERRO.* FROM mob_arquivo_movimento ARQ_MOV "
				+ "	JOIN mob_ait_movimento AIT_MOV ON (ARQ_MOV.cd_movimento = AIT_MOV.cd_movimento AND ARQ_MOV.cd_ait = AIT_MOV.cd_ait) "
				+ "	JOIN mob_ait AIT ON (ARQ_MOV.cd_ait = AIT.cd_ait) "
				+ " JOIN mob_erro_retorno ERRO ON ( ERRO.nr_erro = LPAD(AIT_MOV.nr_erro, 4, \'0\'))", " ORDER BY ARQ_MOV.dt_arquivo DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
