package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BicoEncerranteDAO{

	public static int insert(BicoEncerrante objeto) {
		return insert(objeto, null);
	}

	public static int insert(BicoEncerrante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_bico_encerrante (cd_conta,"+
			                                  "cd_fechamento,"+
			                                  "cd_bico,"+
			                                  "qt_encerrante_inicial,"+
			                                  "qt_encerrante_final,"+
			                                  "vl_afericao,"+
			                                  "qt_litros,"+
			                                  "vl_preco,"+
			                                  "vl_total," +
			                                  "cd_tanque," +
			                                  "cd_combustivel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFechamento());
			if(objeto.getCdBico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdBico());
			pstmt.setFloat(4,objeto.getQtEncerranteInicial());
			pstmt.setFloat(5,objeto.getQtEncerranteFinal());
			pstmt.setFloat(6,objeto.getVlAfericao());
			pstmt.setFloat(7,objeto.getQtLitros());
			pstmt.setFloat(8,objeto.getVlPreco());
			pstmt.setFloat(9,objeto.getVlTotal());
			if(objeto.getCdTanque()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTanque());
			if(objeto.getCdCombustivel()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCombustivel());
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

	public static int update(BicoEncerrante objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(BicoEncerrante objeto, int cdContaOld, int cdFechamentoOld, int cdBicoOld) {
		return update(objeto, cdContaOld, cdFechamentoOld, cdBicoOld, null);
	}

	public static int update(BicoEncerrante objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(BicoEncerrante objeto, int cdContaOld, int cdFechamentoOld, int cdBicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_bico_encerrante SET cd_conta=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_bico=?,"+
												      		   "qt_encerrante_inicial=?,"+
												      		   "qt_encerrante_final=?,"+
												      		   "vl_afericao=?,"+
												      		   "qt_litros=?,"+
												      		   "vl_preco=?,"+
												      		   "vl_total=?," +
												      		   "cd_tanque=?," +
												      		   "cd_combustivel=? WHERE cd_conta=? AND cd_fechamento=? AND cd_bico=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdFechamento());
			pstmt.setInt(3,objeto.getCdBico());
			pstmt.setFloat(4,objeto.getQtEncerranteInicial());
			pstmt.setFloat(5,objeto.getQtEncerranteFinal());
			pstmt.setFloat(6,objeto.getVlAfericao());
			pstmt.setFloat(7,objeto.getQtLitros());
			pstmt.setFloat(8,objeto.getVlPreco());
			pstmt.setFloat(9,objeto.getVlTotal());
			if(objeto.getCdTanque()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTanque());
			if(objeto.getCdCombustivel()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCombustivel());
			pstmt.setInt(12, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(13, cdFechamentoOld!=0 ? cdFechamentoOld : objeto.getCdFechamento());
			pstmt.setInt(14, cdBicoOld!=0 ? cdBicoOld : objeto.getCdBico());
			return pstmt.executeUpdate();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdFechamento, int cdBico) {
		return delete(cdConta, cdFechamento, cdBico, null);
	}

	public static int delete(int cdConta, int cdFechamento, int cdBico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_bico_encerrante WHERE cd_conta=? AND cd_fechamento=? AND cd_bico=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdBico);
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

	public static BicoEncerrante get(int cdConta, int cdFechamento, int cdBico) {
		return get(cdConta, cdFechamento, cdBico, null);
	}

	public static BicoEncerrante get(int cdConta, int cdFechamento, int cdBico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_encerrante WHERE cd_conta=? AND cd_fechamento=? AND cd_bico=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdBico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BicoEncerrante(rs.getInt("cd_bico"),
						rs.getFloat("qt_encerrante_inicial"),
						rs.getFloat("qt_encerrante_final"),
						rs.getFloat("vl_afericao"),
						rs.getFloat("qt_litros"),					
						rs.getFloat("vl_preco"),
						rs.getFloat("vl_total"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_tanque"),
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_encerrante");
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT BE.*, B.*, T.*, LA.nm_local_armazenamento AS nm_tanque, " +
				     "       PS.nm_produto_servico AS nm_combustivel, PSE.qt_precisao_custo " +
				     "FROM pcb_bico_encerrante BE " +
					 "JOIN pcb_bico                    B   ON (B.cd_bico              = BE.cd_bico) " +
					 "JOIN pcb_tanque                  T   ON (BE.cd_tanque           = T.cd_tanque) " +
					 "JOIN alm_local_armazenamento     LA  ON (T.cd_tanque            = LA.cd_local_armazenamento) " +
					 "JOIN grl_produto_servico         PS  ON (BE.cd_combustivel      = PS.cd_produto_servico) " +
					 "JOIN grl_produto_servico_empresa PSE ON (PS.cd_produto_servico  = PSE.cd_produto_servico " +
					 "                                     AND LA.cd_empresa          = PSE.cd_empresa) " +
					 "WHERE 1=1 ";		
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
