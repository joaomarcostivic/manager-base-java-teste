package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendedorDAO{

	public static int insert(Empreendedor objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Empreendedor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[1];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_pessoa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = Conexao.getSequenceCode("mcr_empreendedor", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendedor (cd_pessoa,"+
			                                  "tp_moradia,"+
			                                  "lg_hipoteca,"+
			                                  "nr_tempo_reside_local,"+
			                                  "vl_moradia,"+
			                                  "nm_outro_tipo,"+
			                                  "nr_tempo_reside_municipio,"+
			                                  "txt_parecer,"+
			                                  "nr_pessoas_renda,"+
			                                  "vl_alimentacao,"+
			                                  "vl_saude,"+
			                                  "vl_educacao,"+
			                                  "vl_vestuario,"+
			                                  "vl_aluguel,"+
			                                  "vl_agua,"+
			                                  "vl_transporte,"+
			                                  "vl_dividas,"+
			                                  "vl_diversao,"+
			                                  "vl_outros,"+
			                                  "txt_experiencia_anterior,"+
			                                  "nm_superior,"+
			                                  "txt_curso_profissionalizante,"+
			                                  "txt_comportamento,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getTpMoradia());
			pstmt.setInt(3,objeto.getLgHipoteca());
			pstmt.setInt(4,objeto.getNrTempoResideLocal());
			pstmt.setFloat(5,objeto.getVlMoradia());
			pstmt.setString(6,objeto.getNmOutroTipo());
			pstmt.setInt(7,objeto.getNrTempoResideMunicipio());
			pstmt.setString(8,objeto.getTxtParecer());
			pstmt.setInt(9,objeto.getNrPessoasRenda());
			pstmt.setFloat(10,objeto.getVlAlimentacao());
			pstmt.setFloat(11,objeto.getVlSaude());
			pstmt.setFloat(12,objeto.getVlEducacao());
			pstmt.setFloat(13,objeto.getVlVestuario());
			pstmt.setFloat(14,objeto.getVlAluguel());
			pstmt.setFloat(15,objeto.getVlAgua());
			pstmt.setFloat(16,objeto.getVlTransporte());
			pstmt.setFloat(17,objeto.getVlDividas());
			pstmt.setFloat(18,objeto.getVlDiversao());
			pstmt.setFloat(19,objeto.getVlOutros());
			pstmt.setString(20,objeto.getTxtExperienciaAnterior());
			pstmt.setString(21,objeto.getNmSuperior());
			pstmt.setString(22,objeto.getTxtCursoProfissionalizante());
			pstmt.setString(23,objeto.getTxtComportamento());
			pstmt.setString(24,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Empreendedor objeto) {
		return update(objeto, null);
	}

	public static int update(Empreendedor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendedor SET tp_moradia=?,"+
			                                  "lg_hipoteca=?,"+
			                                  "nr_tempo_reside_local=?,"+
			                                  "vl_moradia=?,"+
			                                  "nm_outro_tipo=?,"+
			                                  "nr_tempo_reside_municipio=?,"+
			                                  "txt_parecer=?,"+
			                                  "nr_pessoas_renda=?,"+
			                                  "vl_alimentacao=?,"+
			                                  "vl_saude=?,"+
			                                  "vl_educacao=?,"+
			                                  "vl_vestuario=?,"+
			                                  "vl_aluguel=?,"+
			                                  "vl_agua=?,"+
			                                  "vl_transporte=?,"+
			                                  "vl_dividas=?,"+
			                                  "vl_diversao=?,"+
			                                  "vl_outros=?,"+
			                                  "txt_experiencia_anterior=?,"+
			                                  "nm_superior=?,"+
			                                  "txt_curso_profissionalizante=?,"+
			                                  "txt_comportamento=?,"+
			                                  "txt_observacao=? WHERE cd_pessoa=?");
			pstmt.setInt(1,objeto.getTpMoradia());
			pstmt.setInt(2,objeto.getLgHipoteca());
			pstmt.setInt(3,objeto.getNrTempoResideLocal());
			pstmt.setFloat(4,objeto.getVlMoradia());
			pstmt.setString(5,objeto.getNmOutroTipo());
			pstmt.setInt(6,objeto.getNrTempoResideMunicipio());
			pstmt.setString(7,objeto.getTxtParecer());
			pstmt.setInt(8,objeto.getNrPessoasRenda());
			pstmt.setFloat(9,objeto.getVlAlimentacao());
			pstmt.setFloat(10,objeto.getVlSaude());
			pstmt.setFloat(11,objeto.getVlEducacao());
			pstmt.setFloat(12,objeto.getVlVestuario());
			pstmt.setFloat(13,objeto.getVlAluguel());
			pstmt.setFloat(14,objeto.getVlAgua());
			pstmt.setFloat(15,objeto.getVlTransporte());
			pstmt.setFloat(16,objeto.getVlDividas());
			pstmt.setFloat(17,objeto.getVlDiversao());
			pstmt.setFloat(18,objeto.getVlOutros());
			pstmt.setString(19,objeto.getTxtExperienciaAnterior());
			pstmt.setString(20,objeto.getNmSuperior());
			pstmt.setString(21,objeto.getTxtCursoProfissionalizante());
			pstmt.setString(22,objeto.getTxtComportamento());
			pstmt.setString(23,objeto.getTxtObservacao());
			pstmt.setInt(24,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa) {
		return delete(cdPessoa, null);
	}

	public static int delete(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendedor WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Empreendedor get(int cdPessoa) {
		return get(cdPessoa, null);
	}

	public static Empreendedor get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendedor WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Empreendedor(rs.getInt("cd_pessoa"),
						rs.getInt("tp_moradia"),
						rs.getInt("lg_hipoteca"),
						rs.getInt("nr_tempo_reside_local"),
						rs.getFloat("vl_moradia"),
						rs.getString("nm_outro_tipo"),
						rs.getInt("nr_tempo_reside_municipio"),
						rs.getString("txt_parecer"),
						rs.getInt("nr_pessoas_renda"),
						rs.getFloat("vl_alimentacao"),
						rs.getFloat("vl_saude"),
						rs.getFloat("vl_educacao"),
						rs.getFloat("vl_vestuario"),
						rs.getFloat("vl_aluguel"),
						rs.getFloat("vl_agua"),
						rs.getFloat("vl_transporte"),
						rs.getFloat("vl_dividas"),
						rs.getFloat("vl_diversao"),
						rs.getFloat("vl_outros"),
						rs.getString("txt_experiencia_anterior"),
						rs.getString("nm_superior"),
						rs.getString("txt_curso_profissionalizante"),
						rs.getString("txt_comportamento"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendedor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendedorDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendedor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
