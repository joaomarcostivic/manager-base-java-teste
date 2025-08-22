package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFaturamentoDAO{

	public static int insert(RegraFaturamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFaturamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_regra_faturamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegraFaturamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_regra_faturamento (cd_regra_faturamento,"+
			                                  "cd_tipo_andamento,"+
			                                  "cd_tipo_prazo,"+
			                                  "cd_produto_servico,"+
			                                  "cd_tipo_processo,"+
			                                  "cd_orgao,"+
			                                  "cd_cliente,"+
			                                  "cd_grupo_processo,"+
			                                  "cd_area_direito,"+
			                                  "cd_estado,"+
			                                  "tp_fato_gerador,"+
			                                  "tp_natureza_evento,"+
			                                  "tp_parte_cliente,"+
			                                  "tp_instancia,"+
			                                  "tp_segmento,"+
			                                  "tp_ponto_base,"+
			                                  "qt_maxima,"+
			                                  "qt_apos_encerramento,"+
			                                  "vl_limite_inferior,"+
			                                  "vl_limite_superior,"+
			                                  "vl_km_deslocamento,"+
			                                  "vl_km_maxima,"+
			                                  "vl_servico,"+
			                                  "cd_categoria_economica,"+
			                                  "cd_centro_custo,"+
			                                  "tp_aplicacao_grupo,"+
			                                  "nm_regra_faturamento,"+
			                                  "st_regra_faturamento,"+
			                                  "cd_cidade,"+
			                                  "cd_grupo_trabalho,"+
			                                  "lg_preposto,"+
			                                  "cd_processo,"+
			                                  "nr_juizo,"+
			                                  "cd_juizo,"+
			                                  "vl_preposto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAndamento());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoPrazo());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoProcesso());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdOrgao());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCliente());
			if(objeto.getCdGrupoProcesso()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdGrupoProcesso());
			if(objeto.getCdAreaDireito()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdAreaDireito());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEstado());
			pstmt.setInt(11,objeto.getTpFatoGerador());
			pstmt.setInt(12,objeto.getTpNaturezaEvento());
			pstmt.setInt(13,objeto.getTpParteCliente());
			pstmt.setInt(14,objeto.getTpInstancia());
			pstmt.setInt(15,objeto.getTpSegmento());
			pstmt.setInt(16,objeto.getTpPontoBase());
			pstmt.setInt(17,objeto.getQtMaxima());
			pstmt.setInt(18,objeto.getQtAposEncerramento());
			pstmt.setDouble(19,objeto.getVlLimiteInferior());
			pstmt.setDouble(20,objeto.getVlLimiteSuperior());
			pstmt.setDouble(21,objeto.getVlKmDeslocamento());
			pstmt.setDouble(22,objeto.getVlKmMaxima());
			pstmt.setDouble(23,objeto.getVlServico());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdCentroCusto());
			pstmt.setInt(26,objeto.getTpAplicacaoGrupo());
			pstmt.setString(27,objeto.getNmRegraFaturamento());
			pstmt.setInt(28,objeto.getStRegraFaturamento());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdCidade());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdGrupoTrabalho());
			pstmt.setInt(31,objeto.getLgPreposto());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdProcesso());
			pstmt.setString(33,objeto.getNrJuizo());
			if(objeto.getCdJuizo()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdJuizo());
			pstmt.setDouble(35,objeto.getVlPreposto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFaturamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(RegraFaturamento objeto, int cdRegraFaturamentoOld) {
		return update(objeto, cdRegraFaturamentoOld, null);
	}

	public static int update(RegraFaturamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(RegraFaturamento objeto, int cdRegraFaturamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_regra_faturamento SET cd_regra_faturamento=?,"+
												      		   "cd_tipo_andamento=?,"+
												      		   "cd_tipo_prazo=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_tipo_processo=?,"+
												      		   "cd_orgao=?,"+
												      		   "cd_cliente=?,"+
												      		   "cd_grupo_processo=?,"+
												      		   "cd_area_direito=?,"+
												      		   "cd_estado=?,"+
												      		   "tp_fato_gerador=?,"+
												      		   "tp_natureza_evento=?,"+
												      		   "tp_parte_cliente=?,"+
												      		   "tp_instancia=?,"+
												      		   "tp_segmento=?,"+
												      		   "tp_ponto_base=?,"+
												      		   "qt_maxima=?,"+
												      		   "qt_apos_encerramento=?,"+
												      		   "vl_limite_inferior=?,"+
												      		   "vl_limite_superior=?,"+
												      		   "vl_km_deslocamento=?,"+
												      		   "vl_km_maxima=?,"+
												      		   "vl_servico=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "cd_centro_custo=?,"+
												      		   "tp_aplicacao_grupo=?,"+
												      		   "nm_regra_faturamento=?,"+
												      		   "st_regra_faturamento=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_grupo_trabalho=?,"+
												      		   "lg_preposto=?,"+
												      		   "cd_processo=?,"+
												      		   "nr_juizo=?,"+
												      		   "cd_juizo=?,"+
												      		   "vl_preposto=? WHERE cd_regra_faturamento=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAndamento());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoPrazo());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoProcesso());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdOrgao());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCliente());
			if(objeto.getCdGrupoProcesso()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdGrupoProcesso());
			if(objeto.getCdAreaDireito()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdAreaDireito());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEstado());
			pstmt.setInt(11,objeto.getTpFatoGerador());
			pstmt.setInt(12,objeto.getTpNaturezaEvento());
			pstmt.setInt(13,objeto.getTpParteCliente());
			pstmt.setInt(14,objeto.getTpInstancia());
			pstmt.setInt(15,objeto.getTpSegmento());
			pstmt.setInt(16,objeto.getTpPontoBase());
			pstmt.setInt(17,objeto.getQtMaxima());
			pstmt.setInt(18,objeto.getQtAposEncerramento());
			pstmt.setDouble(19,objeto.getVlLimiteInferior());
			pstmt.setDouble(20,objeto.getVlLimiteSuperior());
			pstmt.setDouble(21,objeto.getVlKmDeslocamento());
			pstmt.setDouble(22,objeto.getVlKmMaxima());
			pstmt.setDouble(23,objeto.getVlServico());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdCentroCusto());
			pstmt.setInt(26,objeto.getTpAplicacaoGrupo());
			pstmt.setString(27,objeto.getNmRegraFaturamento());
			pstmt.setInt(28,objeto.getStRegraFaturamento());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdCidade());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdGrupoTrabalho());
			pstmt.setInt(31,objeto.getLgPreposto());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdProcesso());
			pstmt.setString(33,objeto.getNrJuizo());
			if(objeto.getCdJuizo()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdJuizo());
			pstmt.setDouble(35,objeto.getVlPreposto());
			pstmt.setInt(36, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraFaturamento) {
		return delete(cdRegraFaturamento, null);
	}

	public static int delete(int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_regra_faturamento WHERE cd_regra_faturamento=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFaturamento get(int cdRegraFaturamento) {
		return get(cdRegraFaturamento, null);
	}

	public static RegraFaturamento get(int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_faturamento WHERE cd_regra_faturamento=?");
			pstmt.setInt(1, cdRegraFaturamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFaturamento(rs.getInt("cd_regra_faturamento"),
						rs.getInt("cd_tipo_andamento"),
						rs.getInt("cd_tipo_prazo"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_tipo_processo"),
						rs.getInt("cd_orgao"),
						rs.getInt("cd_cliente"),
						rs.getInt("cd_grupo_processo"),
						rs.getInt("cd_area_direito"),
						rs.getInt("cd_estado"),
						rs.getInt("tp_fato_gerador"),
						rs.getInt("tp_natureza_evento"),
						rs.getInt("tp_parte_cliente"),
						rs.getInt("tp_instancia"),
						rs.getInt("tp_segmento"),
						rs.getInt("tp_ponto_base"),
						rs.getInt("qt_maxima"),
						rs.getInt("qt_apos_encerramento"),
						rs.getDouble("vl_limite_inferior"),
						rs.getDouble("vl_limite_superior"),
						rs.getDouble("vl_km_deslocamento"),
						rs.getDouble("vl_km_maxima"),
						rs.getDouble("vl_servico"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("cd_centro_custo"),
						rs.getInt("tp_aplicacao_grupo"),
						rs.getString("nm_regra_faturamento"),
						rs.getInt("st_regra_faturamento"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_grupo_trabalho"),
						rs.getInt("lg_preposto"),
						rs.getInt("cd_processo"),
						rs.getString("nr_juizo"),
						rs.getInt("cd_juizo"),
						rs.getDouble("vl_preposto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_faturamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFaturamento> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFaturamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFaturamento> list = new ArrayList<RegraFaturamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFaturamento obj = RegraFaturamentoDAO.get(rsm.getInt("cd_regra_faturamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFaturamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_regra_faturamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}