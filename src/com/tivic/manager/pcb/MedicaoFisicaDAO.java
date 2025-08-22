package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MedicaoFisicaDAO{

	public static int insert(MedicaoFisica objeto) {
		return insert(objeto, null);
	}

	public static int insert(MedicaoFisica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_medicao_fisica (cd_conta,"+
			                                  "cd_fechamento,"+
			                                  "cd_tanque,"+
			                                  "vl_regua,"+
			                                  "qt_volume,"+
			                                  "qt_estoque_escritural,"+
			                                  "qt_capacidade," +
			                                  "cd_combustivel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFechamento());
			if(objeto.getCdTanque()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTanque());
			pstmt.setFloat(4,objeto.getVlRegua());
			pstmt.setFloat(5,objeto.getQtVolume());
			pstmt.setFloat(6,objeto.getQtEstoqueEscritural());
			pstmt.setFloat(7,objeto.getQtCapacidade());
			if(objeto.getCdCombustivel()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdCombustivel());
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

	public static int update(MedicaoFisica objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MedicaoFisica objeto, int cdContaOld, int cdFechamentoOld, int cdTanqueOld) {
		return update(objeto, cdContaOld, cdFechamentoOld, cdTanqueOld, null);
	}

	public static int update(MedicaoFisica objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MedicaoFisica objeto, int cdContaOld, int cdFechamentoOld, int cdTanqueOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_medicao_fisica SET cd_conta=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_tanque=?,"+
												      		   "vl_regua=?,"+
												      		   "qt_volume=?,"+
												      		   "qt_estoque_escritural=?,"+
												      		   "qt_capacidade=?," +
												      		   "cd_combustivel=? WHERE cd_conta=? AND cd_fechamento=? AND cd_tanque=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdFechamento());
			pstmt.setInt(3,objeto.getCdTanque());
			pstmt.setFloat(4,objeto.getVlRegua());
			pstmt.setFloat(5,objeto.getQtVolume());
			pstmt.setFloat(6,objeto.getQtEstoqueEscritural());
			pstmt.setFloat(7,objeto.getQtCapacidade());
			if(objeto.getCdCombustivel()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdCombustivel());
			pstmt.setInt(9, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(10, cdFechamentoOld!=0 ? cdFechamentoOld : objeto.getCdFechamento());
			pstmt.setInt(11, cdTanqueOld!=0 ? cdTanqueOld : objeto.getCdTanque());
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

	public static int delete(int cdConta, int cdFechamento, int cdTanque) {
		return delete(cdConta, cdFechamento, cdTanque, null);
	}

	public static int delete(int cdConta, int cdFechamento, int cdTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_medicao_fisica WHERE cd_conta=? AND cd_fechamento=? AND cd_tanque=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdTanque);
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

	public static MedicaoFisica get(int cdConta, int cdFechamento, int cdTanque) {
		return get(cdConta, cdFechamento, cdTanque, null);
	}

	public static MedicaoFisica get(int cdConta, int cdFechamento, int cdTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_medicao_fisica WHERE cd_conta=? AND cd_fechamento=? AND cd_tanque=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdTanque);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MedicaoFisica(rs.getInt("cd_conta"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_tanque"),
						rs.getInt("vl_regua"),
						rs.getInt("qt_volume"),
						rs.getFloat("qt_estoque_escritural"),
						rs.getInt("qt_capacidade"),
						rs.getInt("cd_combustivel"));
			}
			return null;
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_medicao_fisica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoFisicaDAO.getAll: " + e);
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
		String sql = "SELECT MF.*, LA.nm_local_armazenamento AS nm_tanque, PS.nm_produto_servico AS nm_combustivel, TT.qt_capacidade " +
					 "FROM pcb_medicao_fisica MF " +
					 "JOIN pcb_tanque              T  ON(T.cd_tanque = MF.cd_tanque)" + 
				     "JOIN alm_local_armazenamento LA ON(LA.cd_local_armazenamento = T.cd_tanque) " +
					 "JOIN grl_produto_servico     PS ON(PS.cd_produto_servico     = MF.cd_combustivel) " +
					 "JOIN grl_produto_servico_empresa PSE ON(PS.cd_produto_servico = PSE.cd_produto_servico AND LA.cd_empresa = PSE.cd_empresa)" +
					 "JOIN pcb_tipo_tanque         TT ON(T.cd_tipo_tanque = TT.cd_tipo_tanque) " +
					 "WHERE 1=1 ";
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
