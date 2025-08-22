package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class BicoDAO{

	public static int insert(Bico objeto) {
		return insert(objeto, null);
	}

	public static int insert(Bico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_bico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_bico (cd_bico,"+
			                                  "id_bico,"+
			                                  "nr_ordem,"+
			                                  "st_bico,"+
			                                  "dt_instalacao,"+
			                                  "dt_exclusao,"+
			                                  "dt_cadastro,"+
			                                  "dt_ultima_alteracao,"+
			                                  "nr_casas_inteiras,"+
			                                  "nr_casas_decimais,"+
			                                  "qt_encerrante_inicial,"+
			                                  "vl_encerrante_inicial,"+
			                                  "txt_observacao,"+
			                                  "cd_bomba,"+
			                                  "cd_tanque," +
			                                  "dt_fabricacao," +
			                                  "cd_tabela_preco," +
			                                  "cd_turno_instalacao," +
			                                  "nr_endereco_automacao," +
			                                  "nr_bico_automacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdBico());
			pstmt.setInt(3,objeto.getNrOrdem());
			pstmt.setInt(4,objeto.getStBico());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getDtExclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtExclusao().getTimeInMillis()));
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			pstmt.setInt(9,objeto.getNrCasasInteiras());
			pstmt.setInt(10,objeto.getNrCasasDecimais());
			pstmt.setFloat(11,objeto.getQtEncerranteInicial());
			pstmt.setFloat(12,objeto.getVlEncerranteInicial());
			pstmt.setString(13,objeto.getTxtObservacao());
			if(objeto.getCdBomba()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdBomba());
			if(objeto.getCdTanque()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTanque());
			if(objeto.getDtFabricacao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtFabricacao().getTimeInMillis()));
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTabelaPreco());
			if(objeto.getCdTurnoInstalacao()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTurnoInstalacao());
			pstmt.setInt(19,objeto.getNrEnderecoAutomacao());
			pstmt.setString(20,objeto.getNrBicoAutomacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Bico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Bico objeto, int cdBicoOld) {
		return update(objeto, cdBicoOld, null);
	}

	public static int update(Bico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Bico objeto, int cdBicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_bico SET cd_bico=?,"+
												      		   "id_bico=?,"+
												      		   "nr_ordem=?,"+
												      		   "st_bico=?,"+
												      		   "dt_instalacao=?,"+
												      		   "dt_exclusao=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_ultima_alteracao=?,"+
												      		   "nr_casas_inteiras=?,"+
												      		   "nr_casas_decimais=?,"+
												      		   "qt_encerrante_inicial=?,"+
												      		   "vl_encerrante_inicial=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_bomba=?,"+
												      		   "cd_tanque=?," +
												      		   "dt_fabricacao=?," +
												      		   "cd_tabela_preco=?," +
												      		   "cd_turno_instalacao=?," +
												      		   "nr_endereco_automacao=?," +
												      		   "nr_bico_automacao=? WHERE cd_bico=?");
			pstmt.setInt(1,objeto.getCdBico());
			pstmt.setString(2,objeto.getIdBico());
			pstmt.setInt(3,objeto.getNrOrdem());
			pstmt.setInt(4,objeto.getStBico());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getDtExclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtExclusao().getTimeInMillis()));
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			pstmt.setInt(9,objeto.getNrCasasInteiras());
			pstmt.setInt(10,objeto.getNrCasasDecimais());
			pstmt.setFloat(11,objeto.getQtEncerranteInicial());
			pstmt.setFloat(12,objeto.getVlEncerranteInicial());
			pstmt.setString(13,objeto.getTxtObservacao());
			if(objeto.getCdBomba()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdBomba());
			if(objeto.getCdTanque()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTanque());
			if(objeto.getDtFabricacao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtFabricacao().getTimeInMillis()));
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTabelaPreco());
			if(objeto.getCdTurnoInstalacao()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTurnoInstalacao());
			pstmt.setInt(19,objeto.getNrEnderecoAutomacao());
			pstmt.setString(20,objeto.getNrBicoAutomacao());
			pstmt.setInt(21, cdBicoOld!=0 ? cdBicoOld : objeto.getCdBico());
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

	public static int delete(int cdBico) {
		return delete(cdBico, null);
	}

	public static int delete(int cdBico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_bico WHERE cd_bico=?");
			pstmt.setInt(1, cdBico);
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

	public static Bico get(int cdBico) {
		return get(cdBico, null);
	}

	public static Bico get(int cdBico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bico WHERE cd_bico=?");
			pstmt.setInt(1, cdBico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Bico(rs.getInt("cd_bico"),
						rs.getString("id_bico"),
						rs.getInt("nr_ordem"),
						rs.getInt("st_bico"),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						(rs.getTimestamp("dt_exclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_exclusao").getTime()),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_ultima_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultima_alteracao").getTime()),
						rs.getInt("nr_casas_inteiras"),
						rs.getInt("nr_casas_decimais"),
						rs.getFloat("qt_encerrante_inicial"),
						rs.getFloat("vl_encerrante_inicial"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_bomba"),
						rs.getInt("cd_tanque"),
						(rs.getTimestamp("dt_fabricacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fabricacao").getTime()),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_turno_instalacao"),
						rs.getInt("nr_endereco_automacao"),
						rs.getString("nr_bico_automacao"));
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM pcb_bico").executeQuery());
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
		String sql = "SELECT DISTINCT(BI.*), PS.nm_produto_servico AS nm_combustivel, PS.cd_produto_servico AS cd_combustivel, TP.nm_tabela_preco, PSP.vl_preco, PSE.qt_precisao_custo " +
				     "FROM pcb_bico BI " +
					 "JOIN pcb_bombas                 BO ON(BI.cd_bomba = BO.cd_bomba) " +
					 "JOIN pcb_tanque                  T ON(BI.cd_tanque = T.cd_tanque) " +
					 "JOIN grl_produto_servico        PS ON(T.cd_produto_servico = PS.cd_produto_servico) " +
					 "JOIN alm_local_armazenamento    LA ON(T.cd_tanque = LA.cd_local_armazenamento) " +
					 "JOIN grl_produto_servico_empresa PSE ON(LA.cd_empresa = PSE.cd_empresa AND T.cd_produto_servico = PSE.cd_produto_servico) "+
					 "LEFT OUTER JOIN adm_tabela_preco           TP ON(BI.cd_tabela_preco = TP.cd_tabela_preco) "+
					 "LEFT OUTER JOIN adm_produto_servico_preco PSP ON(PS.cd_produto_servico = PSP.cd_produto_servico " +
					 "															AND PSP.cd_tabela_preco = TP.cd_tabela_preco " +
					 "															AND PSP.dt_termino_validade IS NULL) "+
					 "WHERE 1=1 ";
		ResultSetMap rsm = Search.find(sql, "ORDER BY BI.nr_ordem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}
	
}
