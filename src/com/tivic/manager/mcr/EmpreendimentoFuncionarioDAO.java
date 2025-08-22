package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoFuncionarioDAO{

	public static int insert(EmpreendimentoFuncionario objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoFuncionario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_funcionario");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mcr_empreendimento_funcionario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_funcionario (cd_empreendimento,"+
			                                  "cd_funcionario,"+
			                                  "nm_funcionario,"+
			                                  "lg_familiar,"+
			                                  "lg_remunerado,"+
			                                  "lg_carteira_assinada,"+
			                                  "vl_salario,"+
			                                  "tp_sexo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmFuncionario());
			pstmt.setInt(4,objeto.getLgFamiliar());
			pstmt.setInt(5,objeto.getLgRemunerado());
			pstmt.setInt(6,objeto.getLgCarteiraAssinada());
			pstmt.setFloat(7,objeto.getVlSalario());
			pstmt.setInt(8,objeto.getTpSexo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoFuncionario objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoFuncionario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_funcionario SET nm_funcionario=?,"+
			                                  "lg_familiar=?,"+
			                                  "lg_remunerado=?,"+
			                                  "lg_carteira_assinada=?,"+
			                                  "vl_salario=?,"+
			                                  "tp_sexo=? WHERE cd_empreendimento=? AND cd_funcionario=?");
			pstmt.setString(1,objeto.getNmFuncionario());
			pstmt.setInt(2,objeto.getLgFamiliar());
			pstmt.setInt(3,objeto.getLgRemunerado());
			pstmt.setInt(4,objeto.getLgCarteiraAssinada());
			pstmt.setFloat(5,objeto.getVlSalario());
			pstmt.setInt(6,objeto.getTpSexo());
			pstmt.setInt(7,objeto.getCdEmpreendimento());
			pstmt.setInt(8,objeto.getCdFuncionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento, int cdFuncionario) {
		return delete(cdEmpreendimento, cdFuncionario, null);
	}

	public static int delete(int cdEmpreendimento, int cdFuncionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_funcionario WHERE cd_empreendimento=? AND cd_funcionario=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdFuncionario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoFuncionario get(int cdEmpreendimento, int cdFuncionario) {
		return get(cdEmpreendimento, cdFuncionario, null);
	}

	public static EmpreendimentoFuncionario get(int cdEmpreendimento, int cdFuncionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_funcionario WHERE cd_empreendimento=? AND cd_funcionario=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdFuncionario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoFuncionario(rs.getInt("cd_empreendimento"),
						rs.getInt("cd_funcionario"),
						rs.getString("nm_funcionario"),
						rs.getInt("lg_familiar"),
						rs.getInt("lg_remunerado"),
						rs.getInt("lg_carteira_assinada"),
						rs.getFloat("vl_salario"),
						rs.getInt("tp_sexo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_funcionario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFuncionarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_funcionario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
