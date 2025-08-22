package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class OcorrenciaServices {

	public static final int ST_PENDENTE = 0;
	public static final int ST_CONCLUIDO = 1;
	public static final int ST_CANCELADO = 2;

	public static final String[] situacaoOcorrencia = {"Pendente", "Concluído", "Cancelado"};

	public static Result save(Ocorrencia ocorrencia){
		return save(ocorrencia, null);
	}

	public static Result save(Ocorrencia ocorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrencia==null)
				return new Result(-1, "Erro ao salvar. Ocorrencia é nulo");

			int retorno;
			if(ocorrencia.getCdOcorrencia()==0){
				retorno = OcorrenciaDAO.insert(ocorrencia, connect);
				ocorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaDAO.update(ocorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIA", ocorrencia);
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
	
	public static int delete(int cdOcorrencia) {
		return delete(cdOcorrencia, null);
	}

	public static int delete(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (com.tivic.manager.adm.OcorrenciaDAO.delete(cdOcorrencia, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (com.tivic.manager.agd.OcorrenciaDAO.delete(cdOcorrencia, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (com.tivic.manager.crm.OcorrenciaDAO.delete(cdOcorrencia, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (com.tivic.manager.fta.OcorrenciaDAO.delete(cdOcorrencia, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (OcorrenciaDAO.delete(cdOcorrencia, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT DISTINCT A.*, " +
				"B.nm_tipo_ocorrencia,  " +
				"C.nm_pessoa, C.nr_telefone1, C.nr_telefone2, C.nr_celular, " +
				"D.nm_login " +
				"FROM grl_pessoa_empresa E, grl_ocorrencia A " +
				"LEFT OUTER JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia) " +
				"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
				"LEFT OUTER JOIN seg_usuario D ON (A.cd_usuario = D.cd_usuario) " +
				"WHERE (C.cd_pessoa = E.cd_pessoa)",
				criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static Ocorrencia insert(Ocorrencia ocorrencia) {
		return insert(ocorrencia, null);
	}

	public static Ocorrencia insert(Ocorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			ocorrencia.setDtOcorrencia(new GregorianCalendar());
			int cdOcorrencia = OcorrenciaDAO.insert(ocorrencia, connection);
			if (cdOcorrencia <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			ocorrencia.setCdOcorrencia(cdOcorrencia);
			return ocorrencia;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Ocorrencia update(Ocorrencia ocorrencia) {
		return update(ocorrencia, null);
	}

	public static Ocorrencia update(Ocorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (ocorrencia.getDtOcorrencia()==null)
				return null;

			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdRetorno = OcorrenciaDAO.update(ocorrencia, connection);

			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			return ocorrencia;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result gerarRelatorioOcorrencia(ArrayList<ItemComparator> criterios){
		return gerarRelatorioOcorrencia(criterios, null);
	}
	public static Result gerarRelatorioOcorrencia(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			
			rsm = find(criterios);
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
			
	} catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_ocorrencia WHERE cd_pessoa = " + cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.getAllByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.getAllByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
