package com.tivic.manager.acd;

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

public class InstituicaoEducacensoArquivoRegistroServices {

	public static Result save(InstituicaoEducacensoArquivoRegistro instituicaoEducacensoArquivoRegistro){
		return save(instituicaoEducacensoArquivoRegistro, null, null);
	}

	public static Result save(InstituicaoEducacensoArquivoRegistro instituicaoEducacensoArquivoRegistro, AuthData authData){
		return save(instituicaoEducacensoArquivoRegistro, authData, null);
	}

	public static Result save(InstituicaoEducacensoArquivoRegistro instituicaoEducacensoArquivoRegistro, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoEducacensoArquivoRegistro==null)
				return new Result(-1, "Erro ao salvar. InstituicaoEducacensoArquivoRegistro é nulo");

			int retorno;
			if(instituicaoEducacensoArquivoRegistro.getCdInstituicao()==0){
				retorno = InstituicaoEducacensoArquivoRegistroDAO.insert(instituicaoEducacensoArquivoRegistro, connect);
				instituicaoEducacensoArquivoRegistro.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoEducacensoArquivoRegistroDAO.update(instituicaoEducacensoArquivoRegistro, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOEDUCACENSOARQUIVOREGISTRO", instituicaoEducacensoArquivoRegistro);
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
	public static Result remove(InstituicaoEducacensoArquivoRegistro instituicaoEducacensoArquivoRegistro) {
		return remove(instituicaoEducacensoArquivoRegistro.getCdInstituicao(), instituicaoEducacensoArquivoRegistro.getCdPeriodoLetivo(), instituicaoEducacensoArquivoRegistro.getCdRegistro());
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro){
		return remove(cdInstituicao, cdPeriodoLetivo, cdRegistro, false, null, null);
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro, boolean cascade){
		return remove(cdInstituicao, cdPeriodoLetivo, cdRegistro, cascade, null, null);
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro, boolean cascade, AuthData authData){
		return remove(cdInstituicao, cdPeriodoLetivo, cdRegistro, cascade, authData, null);
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro, boolean cascade, AuthData authData, Connection connect){
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
			retorno = InstituicaoEducacensoArquivoRegistroDAO.delete(cdInstituicao, cdPeriodoLetivo, cdRegistro, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso_arquivo_registro");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_educacenso_arquivo_registro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}