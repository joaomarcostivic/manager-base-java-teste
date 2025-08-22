package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoCustoFixoDAO{

	public static int insert(EmpreendimentoCustoFixo objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoCustoFixo objeto, Connection connect){
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
			int code = Conexao.getSequenceCode("mcr_empreendimento_custo_fixo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_custo_fixo (cd_empreendimento,"+
			                                  "vl_mao_obra,"+
			                                  "vl_mao_obra_sem_custos,"+
			                                  "vl_agua_luz,"+
			                                  "vl_veiculo,"+
			                                  "vl_contador,"+
			                                  "vl_retirada,"+
			                                  "vl_manutencao,"+
			                                  "vl_aluguel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setFloat(2,objeto.getVlMaoObra());
			pstmt.setFloat(3,objeto.getVlMaoObraSemCustos());
			pstmt.setFloat(4,objeto.getVlAguaLuz());
			pstmt.setFloat(5,objeto.getVlVeiculo());
			pstmt.setFloat(6,objeto.getVlContador());
			pstmt.setFloat(7,objeto.getVlRetirada());
			pstmt.setFloat(8,objeto.getVlManutencao());
			pstmt.setFloat(9,objeto.getVlAluguel());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoCustoFixo objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoCustoFixo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_custo_fixo SET vl_mao_obra=?,"+
			                                  "vl_mao_obra_sem_custos=?,"+
			                                  "vl_agua_luz=?,"+
			                                  "vl_veiculo=?,"+
			                                  "vl_contador=?,"+
			                                  "vl_retirada=?,"+
			                                  "vl_manutencao=?,"+
			                                  "vl_aluguel=? WHERE cd_empreendimento=?");
			pstmt.setFloat(1,objeto.getVlMaoObra());
			pstmt.setFloat(2,objeto.getVlMaoObraSemCustos());
			pstmt.setFloat(3,objeto.getVlAguaLuz());
			pstmt.setFloat(4,objeto.getVlVeiculo());
			pstmt.setFloat(5,objeto.getVlContador());
			pstmt.setFloat(6,objeto.getVlRetirada());
			pstmt.setFloat(7,objeto.getVlManutencao());
			pstmt.setFloat(8,objeto.getVlAluguel());
			pstmt.setInt(9,objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.update: " +  e);
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
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_custo_fixo WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoCustoFixo get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static EmpreendimentoCustoFixo get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_custo_fixo WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoCustoFixo(rs.getInt("cd_empreendimento"),
						rs.getFloat("vl_mao_obra"),
						rs.getFloat("vl_mao_obra_sem_custos"),
						rs.getFloat("vl_agua_luz"),
						rs.getFloat("vl_veiculo"),
						rs.getFloat("vl_contador"),
						rs.getFloat("vl_retirada"),
						rs.getFloat("vl_manutencao"),
						rs.getFloat("vl_aluguel"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_custo_fixo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoFixoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_custo_fixo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
