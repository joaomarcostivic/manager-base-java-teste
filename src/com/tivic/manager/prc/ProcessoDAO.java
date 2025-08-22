package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProcessoDAO{

	public static int insert(Processo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Processo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("PRC_PROCESSO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdProcesso()<=0)
				objeto.setCdProcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_PROCESSO (CD_PROCESSO,"+
			                                  "CD_TIPO_PROCESSO,"+
			                                  "CD_ORGAO_JUDICIAL,"+
			                                  "CD_COMARCA,"+
			                                  "CD_TIPO_SITUACAO,"+
			                                  "CD_ADVOGADO,"+
			                                  "CD_ADVOGADO_CONTRARIO,"+
			                                  "CD_ORGAO,"+
			                                  "CD_GRUPO_PROCESSO,"+
			                                  "NR_PROCESSO,"+
			                                  "LG_CLIENTE_AUTOR,"+
			                                  "TXT_OBJETO,"+
			                                  "TXT_OBSERVACAO,"+
			                                  "DT_DISTRIBUICAO,"+
			                                  "DT_AUTUACAO,"+
			                                  "TXT_SENTENCA,"+
			                                  "DT_SENTENCA,"+
			                                  "ST_PROCESSO,"+
			                                  "DT_SITUACAO,"+
			                                  "VL_PROCESSO,"+
			                                  "VL_ACORDO,"+
			                                  "LG_TAXA_PAPEL,"+
			                                  "LG_TAXA_PAGA,"+
			                                  "NR_ANTIGO,"+
			                                  "DT_ULTIMO_ANDAMENTO,"+
			                                  "QT_MAX_DIAS,"+
			                                  "VL_SENTENCA,"+
			                                  "TP_INSTANCIA,"+
			                                  "DT_ATUALIZACAO,"+
			                                  "TP_PERDA,"+
			                                  "CD_ADVOGADO_TITULAR,"+
			                                  "CD_OFICIAL_JUSTICA,"+
			                                  "CD_TIPO_PEDIDO,"+
			                                  "CD_TIPO_OBJETO,"+
			                                  "CD_RESPONSAVEL_ARQUIVO,"+
			                                  "DT_CADASTRO,"+
			                                  "TP_SENTENCA,"+
			                                  "CD_TRIBUNAL,"+
			                                  "CD_JUIZO,"+
			                                  "NR_JUIZO,"+
			                                  "CD_CIDADE,"+
			                                  "QT_MEDIA_DIAS,"+
			                                  "DT_PRAZO,"+
			                                  "LG_PRAZO,"+
			                                  "NM_CONTEINER1,"+
			                                  "NM_CONTEINER2,"+
			                                  "NM_CONTEINER3,"+
			                                  "ST_LIMINAR,"+
			                                  "ST_ARQUIVO,"+
			                                  "DT_REPASSE,"+
			                                  "CD_CENTRO_CUSTO,"+
			                                  "DT_ATUALIZACAO_EDIT,"+
			                                  "DT_ATUALIZACAO_EDI,"+
			                                  "ST_ATUALIZACAO_EDI,"+
			                                  "CD_USUARIO_CADASTRO,"+
			                                  "TP_AUTOS,"+ 
			                                  "NR_INTERNO,"+
			                                  "CD_GRUPO_TRABALHO,"+
			                                  "CD_PROCESSO_PRINCIPAL,"+
			                                  "ID_PROCESSO,"+
			                                  "CD_JUIZ,"+
			                                  "CD_SISTEMA_PROCESSO,"+
			                                  "TP_RITO,"+
			                                  "TP_REPASSE,"+ 
			                                  "DT_INATIVACAO,"+
			                                  "LG_IMPORTANTE"+//66
			                                  ") " +
			                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			                                  + "	   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			                                  + "	   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			                                  + "	   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			                                  + "	   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			                                  + "	   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			                                  + "	   ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdProcesso());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoProcesso());
			if(objeto.getCdOrgaoJudicial()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrgaoJudicial());
			if(objeto.getCdComarca()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdComarca());
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoSituacao());
			if(objeto.getCdAdvogado()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAdvogado());
			if(objeto.getCdAdvogadoContrario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAdvogadoContrario());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOrgao());
			if(objeto.getCdGrupoProcesso()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdGrupoProcesso());
			pstmt.setString(10,objeto.getNrProcesso());
			pstmt.setInt(11,objeto.getLgClienteAutor());
			pstmt.setString(12,objeto.getTxtObjeto());
			pstmt.setString(13,objeto.getTxtObservacao());
			if(objeto.getDtDistribuicao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtDistribuicao().getTimeInMillis()));
			if(objeto.getDtAutuacao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtAutuacao().getTimeInMillis()));
			pstmt.setString(16,objeto.getTxtSentenca());
			if(objeto.getDtSentenca()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtSentenca().getTimeInMillis()));
			pstmt.setInt(18,objeto.getStProcesso());
			if(objeto.getDtSituacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtSituacao().getTimeInMillis()));
			pstmt.setDouble(20,objeto.getVlProcesso());
			pstmt.setDouble(21,objeto.getVlAcordo());
			pstmt.setInt(22,objeto.getLgTaxaPapel());
			pstmt.setInt(23,objeto.getLgTaxaPaga());
			pstmt.setString(24,objeto.getNrAntigo());
			if(objeto.getDtUltimoAndamento()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtUltimoAndamento().getTimeInMillis()));
			pstmt.setInt(26,objeto.getQtMaxDias());
			pstmt.setDouble(27,objeto.getVlSentenca());
			pstmt.setInt(28,objeto.getTpInstancia());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(29, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(29,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(30,objeto.getTpPerda());
			pstmt.setInt(31,objeto.getCdAdvogadoTitular());
			pstmt.setInt(32,objeto.getCdOficialJustica());
			pstmt.setInt(33,objeto.getCdTipoPedido());
			pstmt.setInt(34,objeto.getCdTipoObjeto());
			pstmt.setInt(35,objeto.getCdResponsavelArquivo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(36, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(36,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(37,objeto.getTpSentenca());
			if(objeto.getCdTribunal()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdTribunal());
			if(objeto.getCdJuizo()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdJuizo());
			pstmt.setString(40,objeto.getNrJuizo());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(41, Types.INTEGER);
			else
				pstmt.setInt(41,objeto.getCdCidade());
			pstmt.setInt(42,objeto.getQtMediaDias());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(43, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(43,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			pstmt.setInt(44,objeto.getLgPrazo());
			pstmt.setString(45,objeto.getNmConteiner1());
			pstmt.setString(46,objeto.getNmConteiner2());
			pstmt.setString(47,objeto.getNmConteiner3());
			pstmt.setInt(48,objeto.getStLiminar());
			pstmt.setInt(49,objeto.getStArquivo());
			if(objeto.getDtRepasse()==null)
				pstmt.setNull(50, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(50,new Timestamp(objeto.getDtRepasse().getTimeInMillis()));
			pstmt.setInt(51,objeto.getCdCentroCusto());
			if(objeto.getDtAtualizacaoEdit()==null)
				pstmt.setNull(52, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(52,new Timestamp(objeto.getDtAtualizacaoEdit().getTimeInMillis()));
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(53, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(53,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(54,objeto.getStAtualizacaoEdi());
			if(objeto.getCdUsuarioCadastro()==0)
				pstmt.setNull(55, Types.INTEGER);
			else
				pstmt.setInt(55,objeto.getCdUsuarioCadastro());
			pstmt.setInt(56,objeto.getTpAutos());
			pstmt.setString(57,objeto.getNrInterno());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(58, Types.INTEGER);
			else
				pstmt.setInt(58,objeto.getCdGrupoTrabalho());
			if(objeto.getCdProcessoPrincipal()==0)
				pstmt.setNull(59, Types.INTEGER);
			else
				pstmt.setInt(59,objeto.getCdProcessoPrincipal());
			pstmt.setString(60,objeto.getIdProcesso());
			if(objeto.getCdJuiz()==0)
				pstmt.setNull(61, Types.INTEGER);
			else
				pstmt.setInt(61,objeto.getCdJuiz());
			if(objeto.getCdSistemaProcesso()==0)
				pstmt.setNull(62, Types.INTEGER);
			else
				pstmt.setInt(62,objeto.getCdSistemaProcesso());
			pstmt.setInt(63,objeto.getTpRito());
			pstmt.setInt(64,objeto.getTpRepasse());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(65, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(65,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setInt(66, objeto.getLgImportante());
			
			pstmt.executeUpdate();
			return objeto.getCdProcesso();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Processo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Processo objeto, int cdProcessoOld) {
		return update(objeto, cdProcessoOld, null);
	}

	public static int update(Processo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Processo objeto, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_PROCESSO SET CD_PROCESSO=?,"+
												      		   "CD_TIPO_PROCESSO=?,"+
												      		   "CD_ORGAO_JUDICIAL=?,"+
												      		   "CD_COMARCA=?,"+
												      		   "CD_TIPO_SITUACAO=?,"+
												      		   "CD_ADVOGADO=?,"+
												      		   "CD_ADVOGADO_CONTRARIO=?,"+
												      		   "CD_ORGAO=?,"+
												      		   "CD_GRUPO_PROCESSO=?,"+
												      		   "NR_PROCESSO=?,"+
												      		   "LG_CLIENTE_AUTOR=?,"+
												      		   "TXT_OBJETO=?,"+
												      		   "TXT_OBSERVACAO=?,"+
												      		   "DT_DISTRIBUICAO=?,"+
												      		   "DT_AUTUACAO=?,"+
												      		   "TXT_SENTENCA=?,"+
												      		   "DT_SENTENCA=?,"+
												      		   "ST_PROCESSO=?,"+
												      		   "DT_SITUACAO=?,"+
												      		   "VL_PROCESSO=?,"+
												      		   "VL_ACORDO=?,"+
												      		   "LG_TAXA_PAPEL=?,"+
												      		   "LG_TAXA_PAGA=?,"+
												      		   "NR_ANTIGO=?,"+
												      		   "DT_ULTIMO_ANDAMENTO=?,"+
												      		   "QT_MAX_DIAS=?,"+
												      		   "VL_SENTENCA=?,"+
												      		   "TP_INSTANCIA=?,"+
												      		   "DT_ATUALIZACAO=?,"+
												      		   "TP_PERDA=?,"+
												      		   "CD_ADVOGADO_TITULAR=?,"+
												      		   "CD_OFICIAL_JUSTICA=?,"+
												      		   "CD_TIPO_PEDIDO=?,"+
												      		   "CD_TIPO_OBJETO=?,"+
												      		   "CD_RESPONSAVEL_ARQUIVO=?,"+
												      		   "DT_CADASTRO=?,"+
												      		   "TP_SENTENCA=?,"+
												      		   "CD_TRIBUNAL=?,"+
												      		   "CD_JUIZO=?,"+
												      		   "NR_JUIZO=?,"+
												      		   "CD_CIDADE=?,"+
												      		   "QT_MEDIA_DIAS=?,"+
												      		   "DT_PRAZO=?,"+
												      		   "LG_PRAZO=?,"+
												      		   "NM_CONTEINER1=?,"+
												      		   "NM_CONTEINER2=?,"+
												      		   "NM_CONTEINER3=?,"+
												      		   "ST_LIMINAR=?,"+
												      		   "ST_ARQUIVO=?,"+
												      		   "DT_REPASSE=?,"+
												      		   "CD_CENTRO_CUSTO=?,"+
												      		   "DT_ATUALIZACAO_EDIT=?,"+
												      		   "DT_ATUALIZACAO_EDI=?,"+
												      		   "ST_ATUALIZACAO_EDI=?,"+
												      		   "TP_AUTOS=?,"+ 
												      		   "NR_INTERNO=?,"+
												      		   "CD_GRUPO_TRABALHO=?,"+ 
												      		   "CD_PROCESSO_PRINCIPAL=?,"+
												      		   "ID_PROCESSO=?,"+
												      		   "CD_JUIZ=?,"+
												      		   "CD_SISTEMA_PROCESSO=?, "+
												      		   "TP_RITO=?,"+
												      		   "TP_REPASSE=?,"+ 
												      		   "DT_INATIVACAO=?,"+
												      		   "LG_IMPORTANTE=?"+//65
												      		   " WHERE CD_PROCESSO=?");
			pstmt.setInt(1,objeto.getCdProcesso());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoProcesso());
			if(objeto.getCdOrgaoJudicial()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrgaoJudicial());
			if(objeto.getCdComarca()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdComarca());
			if(objeto.getCdTipoSituacao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoSituacao());
			if(objeto.getCdAdvogado()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAdvogado());
			if(objeto.getCdAdvogadoContrario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAdvogadoContrario());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOrgao());
			if(objeto.getCdGrupoProcesso()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdGrupoProcesso());
			pstmt.setString(10,objeto.getNrProcesso());
			pstmt.setInt(11,objeto.getLgClienteAutor());
			pstmt.setString(12,objeto.getTxtObjeto());
			pstmt.setString(13,objeto.getTxtObservacao());
			if(objeto.getDtDistribuicao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtDistribuicao().getTimeInMillis()));
			if(objeto.getDtAutuacao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtAutuacao().getTimeInMillis()));
			pstmt.setString(16,objeto.getTxtSentenca());
			if(objeto.getDtSentenca()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtSentenca().getTimeInMillis()));
			pstmt.setInt(18,objeto.getStProcesso());
			if(objeto.getDtSituacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtSituacao().getTimeInMillis()));
			pstmt.setDouble(20,objeto.getVlProcesso());
			pstmt.setDouble(21,objeto.getVlAcordo());
			pstmt.setInt(22,objeto.getLgTaxaPapel());
			pstmt.setInt(23,objeto.getLgTaxaPaga());
			pstmt.setString(24,objeto.getNrAntigo());
			if(objeto.getDtUltimoAndamento()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtUltimoAndamento().getTimeInMillis()));
			pstmt.setInt(26,objeto.getQtMaxDias());
			pstmt.setDouble(27,objeto.getVlSentenca());
			pstmt.setInt(28,objeto.getTpInstancia());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(29, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(29,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(30,objeto.getTpPerda());
			pstmt.setInt(31,objeto.getCdAdvogadoTitular());
			pstmt.setInt(32,objeto.getCdOficialJustica());
			pstmt.setInt(33,objeto.getCdTipoPedido());
			pstmt.setInt(34,objeto.getCdTipoObjeto());
			pstmt.setInt(35,objeto.getCdResponsavelArquivo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(36, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(36,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(37,objeto.getTpSentenca());
			if(objeto.getCdTribunal()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdTribunal());
			if(objeto.getCdJuizo()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdJuizo());
			pstmt.setString(40,objeto.getNrJuizo());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(41, Types.INTEGER);
			else
				pstmt.setInt(41,objeto.getCdCidade());
			pstmt.setInt(42,objeto.getQtMediaDias());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(43, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(43,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			pstmt.setInt(44,objeto.getLgPrazo());
			pstmt.setString(45,objeto.getNmConteiner1());
			pstmt.setString(46,objeto.getNmConteiner2());
			pstmt.setString(47,objeto.getNmConteiner3());
			pstmt.setInt(48,objeto.getStLiminar());
			pstmt.setInt(49,objeto.getStArquivo());
			if(objeto.getDtRepasse()==null)
				pstmt.setNull(50, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(50,new Timestamp(objeto.getDtRepasse().getTimeInMillis()));
			pstmt.setInt(51,objeto.getCdCentroCusto());
			if(objeto.getDtAtualizacaoEdit()==null)
				pstmt.setNull(52, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(52,new Timestamp(objeto.getDtAtualizacaoEdit().getTimeInMillis()));
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(53, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(53,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(54,objeto.getStAtualizacaoEdi());
			pstmt.setInt(55,objeto.getTpAutos());
			pstmt.setString(56, objeto.getNrInterno());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(57, Types.INTEGER);
			else
				pstmt.setInt(57,objeto.getCdGrupoTrabalho());
			if(objeto.getCdProcessoPrincipal()==0)
				pstmt.setNull(58, Types.INTEGER);
			else
				pstmt.setInt(58,objeto.getCdProcessoPrincipal());
			pstmt.setString(59,objeto.getIdProcesso());
			if(objeto.getCdJuiz()==0)
				pstmt.setNull(60, Types.INTEGER);
			else
				pstmt.setInt(60,objeto.getCdJuiz());
			if(objeto.getCdSistemaProcesso()==0)
				pstmt.setNull(61, Types.INTEGER);
			else
				pstmt.setInt(61,objeto.getCdSistemaProcesso());
			pstmt.setInt(62,objeto.getTpRito());
			pstmt.setInt(63,objeto.getTpRepasse());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(64, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(64,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setInt(65,objeto.getLgImportante());
			pstmt.setInt(66, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProcesso) {
		return delete(cdProcesso, null);
	}

	public static int delete(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_PROCESSO WHERE CD_PROCESSO=?");
			pstmt.setInt(1, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Processo get(int cdProcesso) {
		return get(cdProcesso, null);
	}

	public static Processo get(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO WHERE CD_PROCESSO=?");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Processo(rs.getInt("CD_PROCESSO"), 
						rs.getInt("CD_TIPO_PROCESSO"), 
						rs.getInt("CD_ORGAO_JUDICIAL"), 
						rs.getInt("CD_COMARCA"), 
						rs.getInt("CD_TIPO_SITUACAO"), 
						rs.getInt("CD_ADVOGADO"), 
						rs.getInt("CD_ADVOGADO_CONTRARIO"), 
						rs.getInt("CD_ORGAO"), 
						rs.getInt("CD_GRUPO_PROCESSO"), 
						rs.getInt("LG_CLIENTE_AUTOR"), 
						rs.getString("TXT_OBJETO"), 
						rs.getString("TXT_OBSERVACAO"), 
						(rs.getTimestamp("DT_DISTRIBUICAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_DISTRIBUICAO").getTime()),
						(rs.getTimestamp("DT_AUTUACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_AUTUACAO").getTime()),
						rs.getString("TXT_SENTENCA"),
						(rs.getTimestamp("DT_SENTENCA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_SENTENCA").getTime()),
						rs.getInt("ST_PROCESSO"), 
						(rs.getTimestamp("DT_SITUACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_SITUACAO").getTime()),
						rs.getDouble("VL_PROCESSO"), 
						rs.getDouble("VL_ACORDO"), 
						rs.getInt("LG_TAXA_PAPEL"), 
						rs.getInt("LG_TAXA_PAGA"), 
						rs.getString("NR_ANTIGO"), 
						(rs.getTimestamp("DT_ULTIMO_ANDAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ULTIMO_ANDAMENTO").getTime()),
						rs.getInt("QT_MAX_DIAS"), 
						rs.getDouble("VL_SENTENCA"), 
						rs.getInt("TP_INSTANCIA"), 
						(rs.getTimestamp("DT_ATUALIZACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ATUALIZACAO").getTime()),
						rs.getInt("TP_PERDA"), 
						rs.getInt("CD_ADVOGADO_TITULAR"), 
						rs.getInt("CD_OFICIAL_JUSTICA"), 
						rs.getInt("CD_TIPO_PEDIDO"), 
						rs.getInt("CD_TIPO_OBJETO"), 
						rs.getInt("CD_RESPONSAVEL_ARQUIVO"), 
						(rs.getTimestamp("DT_CADASTRO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_CADASTRO").getTime()),
						rs.getInt("TP_SENTENCA"), 
						rs.getString("NR_JUIZO"), 
						rs.getInt("CD_CIDADE"), 
						rs.getInt("QT_MEDIA_DIAS"), 
						(rs.getTimestamp("DT_PRAZO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_PRAZO").getTime()),
						rs.getInt("LG_PRAZO"),
						rs.getString("NM_CONTEINER1"),
						rs.getString("NM_CONTEINER2"),
						rs.getString("NM_CONTEINER3"),
						rs.getInt("ST_LIMINAR"),
						rs.getInt("ST_ARQUIVO"),
						(rs.getTimestamp("DT_REPASSE")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_REPASSE").getTime()),
						rs.getInt("CD_CENTRO_CUSTO"),
						(rs.getTimestamp("DT_ATUALIZACAO_EDIT")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ATUALIZACAO_EDIT").getTime()),
						(rs.getTimestamp("DT_ATUALIZACAO_EDI")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ATUALIZACAO_EDI").getTime()),
						rs.getInt("ST_ATUALIZACAO_EDI"),
						rs.getInt("CD_TRIBUNAL"),
						rs.getInt("CD_JUIZO"),
						rs.getString("NR_PROCESSO"),
						rs.getInt("CD_USUARIO_CADASTRO"),
						rs.getInt("TP_AUTOS"),
						rs.getString("NR_INTERNO"),
						rs.getInt("CD_GRUPO_TRABALHO"),
						rs.getInt("CD_PROCESSO_PRINCIPAL"),
						rs.getString("ID_PROCESSO"),
						rs.getInt("CD_JUIZ"),
						rs.getInt("CD_SISTEMA_PROCESSO"),
						rs.getInt("TP_RITO"),
						rs.getInt("TP_REPASSE"),
						(rs.getTimestamp("DT_INATIVACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_INATIVACAO").getTime()),
						rs.getInt("LG_IMPORTANTE"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM PRC_PROCESSO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
