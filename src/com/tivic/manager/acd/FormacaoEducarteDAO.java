package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FormacaoEducarteDAO{

	public static int insert(FormacaoEducarte objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormacaoEducarte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_formacao_educarte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_formacao_educarte (cd_formacao,"+
			                                  "cd_modalidade,"+
			                                  "cd_instituicao,"+
			                                  "cd_circulo,"+
			                                  "cd_instrutor,"+
			                                  "tp_turno,"+
			                                  "nr_dia_semana,"+
			                                  "lg_planejamento,"+
			                                  "lg_oficina,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getCdModalidade());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getCdCirculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCirculo());
			if(objeto.getCdInstrutor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdInstrutor());
			pstmt.setInt(6,objeto.getTpTurno());
			pstmt.setString(7,objeto.getNrDiaSemana());
			pstmt.setInt(8,objeto.getLgPlanejamento());
			pstmt.setInt(9,objeto.getLgOficina());
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormacaoEducarte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormacaoEducarte objeto, int cdFormacaoOld) {
		return update(objeto, cdFormacaoOld, null);
	}

	public static int update(FormacaoEducarte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormacaoEducarte objeto, int cdFormacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_formacao_educarte SET cd_formacao=?,"+
												      		   "cd_modalidade=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_circulo=?,"+
												      		   "cd_instrutor=?,"+
												      		   "tp_turno=?,"+
												      		   "nr_dia_semana=?,"+
												      		   "lg_planejamento=?,"+
												      		   "lg_oficina=?,"+
												      		   "txt_observacao=? WHERE cd_formacao=?");
			pstmt.setInt(1,objeto.getCdFormacao());
			pstmt.setString(2,objeto.getCdModalidade());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getCdCirculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCirculo());
			if(objeto.getCdInstrutor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdInstrutor());
			pstmt.setInt(6,objeto.getTpTurno());
			pstmt.setString(7,objeto.getNrDiaSemana());
			pstmt.setInt(8,objeto.getLgPlanejamento());
			pstmt.setInt(9,objeto.getLgOficina());
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.setInt(11, cdFormacaoOld!=0 ? cdFormacaoOld : objeto.getCdFormacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormacao) {
		return delete(cdFormacao, null);
	}

	public static int delete(int cdFormacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_formacao_educarte WHERE cd_formacao=?");
			pstmt.setInt(1, cdFormacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormacaoEducarte get(int cdFormacao) {
		return get(cdFormacao, null);
	}

	public static FormacaoEducarte get(int cdFormacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_educarte WHERE cd_formacao=?");
			pstmt.setInt(1, cdFormacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormacaoEducarte(rs.getInt("cd_formacao"),
						rs.getString("cd_modalidade"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_circulo"),
						rs.getInt("cd_instrutor"),
						rs.getInt("tp_turno"),
						rs.getString("nr_dia_semana"),
						rs.getInt("lg_planejamento"),
						rs.getInt("lg_oficina"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_educarte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FormacaoEducarte> getList() {
		return getList(null);
	}

	public static ArrayList<FormacaoEducarte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FormacaoEducarte> list = new ArrayList<FormacaoEducarte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FormacaoEducarte obj = FormacaoEducarteDAO.get(rsm.getInt("cd_formacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoEducarteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_formacao_educarte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}