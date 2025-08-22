package com.tivic.manager.evt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.admin.Util;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class OcorrenciaServices {
		
	public static final String[] tpOcorrencia = {
			"Entrada", // 0
			"Saída", // 1
	};

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
	public static Result remove(float cdOcorrencia, int cdPessoa, int cdEvento){
		return remove(cdOcorrencia, cdPessoa, cdEvento, false, null);
	}
	public static Result remove(float cdOcorrencia, int cdPessoa, int cdEvento, boolean cascade){
		return remove(cdOcorrencia, cdPessoa, cdEvento, cascade, null);
	}
	public static Result remove(float cdOcorrencia, int cdPessoa, int cdEvento, boolean cascade, Connection connect){
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
			retorno = OcorrenciaDAO.delete(cdOcorrencia, cdPessoa, cdEvento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM evt_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllOcorrenciasGroupByPessoa(ArrayList<ItemComparator> criterios) {
		return getAllOcorrenciasGroupByPessoa(criterios, null);
	}
	
	public static ResultSetMap getAllOcorrenciasGroupByPessoa(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			ResultSetMap rsm = EventoPessoaServices.getAllPessoas(criterios, connect);

			while(rsm.next()){				
				rsm.setValueToField("NM_PESSOA", rsm.getString("NM_PESSOA").toUpperCase());
				if(rsm.getInt("QT_FREQUENCIA") < 3){
					rsm.setValueToField("LG_APTA", 0);
					rsm.setValueToField("NM_SITUACAO_CERTIFICADO", "NÃO APTO");
				} else {
					rsm.setValueToField("LG_APTA", 1);
					rsm.setValueToField("NM_SITUACAO_CERTIFICADO", "APTO");
				}
			}
			
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_PESSOA");
			rsm.orderBy(orderBy);
			
			rsm.beforeFirst();
			
			return rsm;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM evt_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
