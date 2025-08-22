package com.tivic.manager.mob;

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

public class DeclaranteEnderecoServices {

	public static Result save(DeclaranteEndereco boatDeclaranteEndereco){
		return save(boatDeclaranteEndereco, null, null);
	}

	public static Result save(DeclaranteEndereco boatDeclaranteEndereco, AuthData authData){
		return save(boatDeclaranteEndereco, authData, null);
	}

	public static Result save(DeclaranteEndereco boatDeclaranteEndereco, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatDeclaranteEndereco==null)
				return new Result(-1, "Erro ao salvar. BoatDeclaranteEndereco é nulo");

			int retorno;
			if(DeclaranteEnderecoDAO.get(boatDeclaranteEndereco.getCdDeclarante(), boatDeclaranteEndereco.getCdCidade(), connect) == null){
				retorno = DeclaranteEnderecoDAO.insert(boatDeclaranteEndereco, connect);
//				boatDeclaranteEndereco.setCdDeclarante(retorno);
			}
			else {
				retorno = DeclaranteEnderecoDAO.update(boatDeclaranteEndereco, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATDECLARANTEENDERECO", boatDeclaranteEndereco);
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
	public static Result remove(DeclaranteEndereco boatDeclaranteEndereco) {
		return remove(boatDeclaranteEndereco.getCdDeclarante(), boatDeclaranteEndereco.getCdCidade());
	}
	public static Result remove(int cdDeclarante, int cdCidade){
		return remove(cdDeclarante, cdCidade, false, null, null);
	}
	public static Result remove(int cdDeclarante, int cdCidade, boolean cascade){
		return remove(cdDeclarante, cdCidade, cascade, null, null);
	}
	public static Result remove(int cdDeclarante, int cdCidade, boolean cascade, AuthData authData){
		return remove(cdDeclarante, cdCidade, cascade, authData, null);
	}
	public static Result remove(int cdDeclarante, int cdCidade, boolean cascade, AuthData authData, Connection connect){
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
			retorno = DeclaranteEnderecoDAO.delete(cdDeclarante, cdCidade, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_declarante_endereco");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static DeclaranteEndereco get(int cdDeclarante) {
		return get(cdDeclarante, null);
	}

	public static DeclaranteEndereco get(int cdDeclarante, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			DeclaranteEndereco endereco = null;
			
			pstmt = connect.prepareStatement("SELECT * FROM mob_declarante_endereco WHERE cd_declarante = "+cdDeclarante);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				endereco = new DeclaranteEndereco(cdDeclarante, 
						rsm.getInt("cd_cidade"),//cdCidade, 
						rsm.getString("nr_cep"), //nrCep, 
						rsm.getString("nm_logradouro"), //nmLogradouro, 
						rsm.getString("nr_endereco"), //nrEndereco, 
						rsm.getString("nm_bairro"), //nmBairro, 
						rsm.getString("ds_complemento") //dsComplemento
					);
			}
			
			return endereco;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoServices.getAll: " + e);
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
		return Search.findAndLog("SELECT * FROM mob_declarante_endereco", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
