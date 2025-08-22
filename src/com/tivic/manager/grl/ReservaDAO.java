package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ReservaDAO{

	public static int insert(Reserva objeto) {
		return insert(objeto, null);
	}

	public static int insert(Reserva objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_reserva", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdReserva(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_reserva (cd_reserva,"+
			                                  "dt_reserva,"+
			                                  "dt_validade,"+
			                                  "tp_reserva,"+
			                                  "st_reserva,"+
			                                  "cd_usuario,"+
			                                  "cd_atendimento,"+
			                                  "cd_referencia,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "txt_reserva,"+
			                                  "cd_pessoa,"+
			                                  "cd_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtReserva()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtReserva().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpReserva());
			pstmt.setInt(5,objeto.getStReserva());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAtendimento());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdReferencia());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEmpresa());
			pstmt.setString(11,objeto.getTxtReserva());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdResponsavel());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReservaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Reserva objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Reserva objeto, int cdReservaOld) {
		return update(objeto, cdReservaOld, null);
	}

	public static int update(Reserva objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Reserva objeto, int cdReservaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_reserva SET cd_reserva=?,"+
												      		   "dt_reserva=?,"+
												      		   "dt_validade=?,"+
												      		   "tp_reserva=?,"+
												      		   "st_reserva=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_atendimento=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "txt_reserva=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_responsavel=? WHERE cd_reserva=?");
			pstmt.setInt(1,objeto.getCdReserva());
			if(objeto.getDtReserva()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtReserva().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpReserva());
			pstmt.setInt(5,objeto.getStReserva());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAtendimento());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdReferencia());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEmpresa());
			pstmt.setString(11,objeto.getTxtReserva());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdResponsavel());
			pstmt.setInt(14, cdReservaOld!=0 ? cdReservaOld : objeto.getCdReserva());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReserva) {
		return delete(cdReserva, null);
	}

	public static int delete(int cdReserva, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_reserva WHERE cd_reserva=?");
			pstmt.setInt(1, cdReserva);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Reserva get(int cdReserva) {
		return get(cdReserva, null);
	}

	public static Reserva get(int cdReserva, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_reserva WHERE cd_reserva=?");
			pstmt.setInt(1, cdReserva);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Reserva(rs.getInt("cd_reserva"),
						(rs.getTimestamp("dt_reserva")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_reserva").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						rs.getInt("tp_reserva"),
						rs.getInt("st_reserva"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_atendimento"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getString("txt_reserva"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_responsavel"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_reserva");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReservaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_reserva", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
