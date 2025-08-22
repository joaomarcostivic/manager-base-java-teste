package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoControleDAO{

	public static int insert(EmpreendimentoControle objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoControle objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[1];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			int code = Conexao.getSequenceCode("mcr_empreendimento_controle", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_controle (cd_empreendimento,"+
			                                  "lg_controle_formal,"+
			                                  "lg_livro_caixa,"+
			                                  "lg_controle_estoque,"+
			                                  "lg_conta_receber,"+
			                                  "lg_receita_despesa,"+
			                                  "lg_conta_pagar,"+
			                                  "lg_outro_controle,"+
			                                  "nm_outro_controle,"+
			                                  "txt_sistema_organizacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2,objeto.getLgControleFormal());
			pstmt.setInt(3,objeto.getLgLivroCaixa());
			pstmt.setInt(4,objeto.getLgControleEstoque());
			pstmt.setInt(5,objeto.getLgContaReceber());
			pstmt.setInt(6,objeto.getLgReceitaDespesa());
			pstmt.setInt(7,objeto.getLgContaPagar());
			pstmt.setInt(8,objeto.getLgOutroControle());
			pstmt.setString(9,objeto.getNmOutroControle());
			pstmt.setString(10,objeto.getTxtSistemaOrganizacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoControle objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoControle objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_controle SET lg_controle_formal=?,"+
			                                  "lg_livro_caixa=?,"+
			                                  "lg_controle_estoque=?,"+
			                                  "lg_conta_receber=?,"+
			                                  "lg_receita_despesa=?,"+
			                                  "lg_conta_pagar=?,"+
			                                  "lg_outro_controle=?,"+
			                                  "nm_outro_controle=?,"+
			                                  "txt_sistema_organizacao=? WHERE cd_empreendimento=?");
			pstmt.setInt(1,objeto.getLgControleFormal());
			pstmt.setInt(2,objeto.getLgLivroCaixa());
			pstmt.setInt(3,objeto.getLgControleEstoque());
			pstmt.setInt(4,objeto.getLgContaReceber());
			pstmt.setInt(5,objeto.getLgReceitaDespesa());
			pstmt.setInt(6,objeto.getLgContaPagar());
			pstmt.setInt(7,objeto.getLgOutroControle());
			pstmt.setString(8,objeto.getNmOutroControle());
			pstmt.setString(9,objeto.getTxtSistemaOrganizacao());
			pstmt.setInt(10,objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento) {
		return delete(cdEmpreendimento, null);
	}

	public static int delete(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_controle WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoControle get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static EmpreendimentoControle get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_controle WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoControle(rs.getInt("cd_empreendimento"),
						rs.getInt("lg_controle_formal"),
						rs.getInt("lg_livro_caixa"),
						rs.getInt("lg_controle_estoque"),
						rs.getInt("lg_conta_receber"),
						rs.getInt("lg_receita_despesa"),
						rs.getInt("lg_conta_pagar"),
						rs.getInt("lg_outro_controle"),
						rs.getString("nm_outro_controle"),
						rs.getString("txt_sistema_organizacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_controle");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoControleDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_controle", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
