package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CirculoServices {
	
	public static final int TP_CIRCULO = 0;
	public static final int TP_NUCLEO = 1;
	public static final int TP_DISTRIBUICAO_MERENDA = 2;
	public static final String[] tpCirculo = {"Círculo", "Núcleo", "Distribuição Merenda"};

	public static Result save(Circulo circulo){
		return save(circulo, null);
	}

	public static Result save(Circulo circulo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(circulo==null)
				return new Result(-1, "Erro ao salvar. Circulo nulo");

			int retorno;
			if(circulo.getCdCirculo()==0){
				retorno = CirculoDAO.insert(circulo, connect);
				circulo.setCdCirculo(retorno);
			}
			else {
				retorno = CirculoDAO.update(circulo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CIRCULO", circulo);
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

	public static Result remove(int cdCirculo){
		return remove(cdCirculo, false, null);
	}
	public static Result remove(int cdCirculo, boolean cascade){
		return remove(cdCirculo, cascade, null);
	}
	public static Result remove(int cdCirculo, boolean cascade, Connection connect){
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
			retorno = CirculoDAO.delete(cdCirculo, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está¡ vinculado a outros e não pode ser excluído!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_circulo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllCirculos(int lgUrbana) {
		return getAllCirculos(lgUrbana, null);
	}

	public static ResultSetMap getAllCirculos(int lgUrbana, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_circulo WHERE tp_circulo IN ("+TP_CIRCULO+", "+TP_NUCLEO+")");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(lgUrbana == 1){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("CD_CIRCULO", 999);
				register.put("NM_CIRCULO", "URBANAS");
				rsm.addRegister(register);
				
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAllCirculos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAllCirculos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getCirculo(int cdCirculo) {
		return getCirculo(cdCirculo, null);
	}

	public static Result getCirculo(int cdCirculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			if(cdCirculo == 999){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("CD_CIRCULO", 999);
				register.put("NM_CIRCULO", "URBANAS");
				return new Result(1, "Sucesso ao buscar circulo", "CIRCULO", register);
			}
			
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_circulo WHERE cd_circulo = "+cdCirculo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()){
				
				String nmInstituicao = "";
				String nmGestor = "";
				String nmViceDiretor = "";
				String nmCoordenador = "";
				String nmSecretario = "";
				
				ResultSetMap rsmInstituicoes = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_circulo WHERE cd_circulo = " + rsm.getInt("cd_circulo")).executeQuery());
				int x = 0;
				while(rsmInstituicoes.next()){
					Result result = InstituicaoServices.getResumo(rsmInstituicoes.getInt("cd_instituicao"), connect);
					HashMap<String, Object> register = (HashMap<String, Object>)result.getObjects().get("INSTITUICAO_RESUMO");
					
					if(register == null){
						rsmInstituicoes.deleteRow();
						if(x == 0)
							rsmInstituicoes.beforeFirst();
						else
							rsmInstituicoes.previous();
						continue;
					}
					
					nmInstituicao += register.get("NM_INSTITUICAO") + ", ";
					nmGestor = String.valueOf((register.get("NM_GESTOR") != null ? register.get("NM_GESTOR") : ""));
					nmViceDiretor = String.valueOf((register.get("NM_VICE_DIRETOR") != null ? register.get("NM_VICE_DIRETOR") : ""));
					nmCoordenador = String.valueOf((register.get("NM_COORDENADOR") != null ? register.get("NM_COORDENADOR") : ""));
					nmSecretario = String.valueOf((register.get("NM_SECRETARIO") != null ? register.get("NM_SECRETARIO") : ""));
					
					x++;
				}
				
				rsm.setValueToField("NM_INSTITUICAO", nmInstituicao.substring(0, nmInstituicao.length()-2));
				rsm.setValueToField("NM_GESTOR", nmGestor);
				rsm.setValueToField("NM_VICE_DIRETOR", nmViceDiretor);
				rsm.setValueToField("NM_COORDENADOR", nmCoordenador);
				rsm.setValueToField("NM_SECRETARIO", nmSecretario);
				
			}
			
			
			return new Result(1, "Sucesso ao buscar circulo", "CIRCULO", rsm.getRegister());
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getCirculo: " + sqlExpt);
			return new Result(-1, "Erro ao buscar circulo");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getCirculo: " + e);
			return new Result(-1, "Erro ao buscar circulo");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean pertenceCirculo(int cdCirculo, int cdInstituicao) {
		return pertenceCirculo(cdCirculo, cdInstituicao, null);
	}

	public static boolean pertenceCirculo(int cdCirculo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_circulo WHERE cd_circulo = " + cdCirculo + " AND cd_instituicao = " + cdInstituicao);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm.next();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.pertenceCirculo: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTpCirculo(int tpCirculo) {
		return getAllByTpCirculo(tpCirculo, null);
	}

	public static ResultSetMap getAllByTpCirculo(int tpCirculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_circulo WHERE tp_circulo = ?");
			pstmt.setInt(1, tpCirculo);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getModalidadeByTpCirculo(int tpCirculo) {
		return getModalidadeByTpCirculo(tpCirculo, null);
	}

	public static ResultSetMap getModalidadeByTpCirculo(int tpCirculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
				"SELECT * FROM acd_circulo A, alm_circulo_modalidade B " + 
				"WHERE A.tp_circulo = ? " + 
				"AND A.cd_circulo = B.cd_circulo "
			);
			pstmt.setInt(1, tpCirculo);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoServices.getAll: " + e);
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
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_circulo" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect == null);
	    return _rsm;
	}

}
