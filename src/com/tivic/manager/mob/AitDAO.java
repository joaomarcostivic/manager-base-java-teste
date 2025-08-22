package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AitDAO{

	public static int insert(Ait objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ait objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_ait", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAit(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait (cd_ait,"+
			                                  "cd_infracao,"+
			                                  "cd_agente,"+
			                                  "cd_usuario,"+
			                                  "dt_infracao,"+
			                                  "ds_observacao,"+
			                                  "ds_local_infracao,"+
			                                  "nr_ait,"+
			                                  "vl_velocidade_permitida,"+
			                                  "vl_velocidade_aferida,"+
			                                  "vl_velocidade_penalidade,"+
			                                  "nr_placa,"+
			                                  "ds_ponto_referencia,"+
			                                  "lg_auto_assinado,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "cd_cidade,"+
			                                  "dt_digitacao,"+
			                                  "cd_equipamento,"+
			                                  "vl_multa,"+
			                                  "lg_enviado_detran,"+
			                                  "st_ait,"+
			                                  "cd_evento_equipamento,"+
			                                  "dt_sincronizacao,"+
			                                  "nr_tentativa_sincronizacao,"+
			                                  "cd_bairro,"+
			                                  "cd_usuario_exclusao,"+
			                                  "dt_vencimento,"+
			                                  "tp_natureza_autuacao,"+
			                                  "dt_movimento,"+
			                                  "tp_status,"+
			                                  "tp_arquivo,"+
			                                  "cd_ocorrencia,"+
			                                  "ds_parecer,"+
			                                  "nr_erro,"+
			                                  "st_entrega,"+
			                                  "nr_processo,"+
			                                  "dt_registro_detran,"+
			                                  "st_recurso,"+
			                                  "lg_advertencia,"+
			                                  "nr_controle,"+
			                                  "nr_renainf,"+
			                                  "nr_sequencial,"+
			                                  "dt_ar_nai,"+
			                                  "nr_notificacao_nai,"+
			                                  "nr_notificacao_nip,"+
			                                  "tp_cancelamento,"+
			                                  "lg_cancela_movimento,"+
			                                  "dt_cancelamento_movimento,"+
			                                  "nr_remessa,"+
			                                  "nr_codigo_barras,"+
			                                  "nr_remessa_registro,"+
			                                  "dt_emissao_nip,"+
			                                  "dt_resultado_defesa,"+
			                                  "dt_atualizacao,"+
			                                  "dt_resultado_jari,"+
			                                  "dt_resultado_cetran,"+
			                                  "lg_advertencia_jari,"+
			                                  "lg_advertencia_cetran,"+
			                                  "lg_notrigger,"+
			                                  "st_detran,"+
			                                  "lg_erro,"+
			                                  "txt_observacao,"+
			                                  "cd_movimento_atual,"+
			                                  "cd_ait_origem,"+
			                                  "nr_fator_nic,"+
			                                  "lg_detran_febraban,"+
			                                  "nr_placa_antiga,"+
			                                  "cd_especie_veiculo,"+
			                                  "cd_marca_veiculo,"+
			                                  "cd_cor_veiculo,"+
			                                  "cd_tipo_veiculo,"+
			                                  "cd_categoria_veiculo,"+
			                                  "nr_renavan,"+
			                                  "nr_ano_fabricacao,"+
			                                  "nr_ano_modelo,"+
			                                  "nm_proprietario,"+
			                                  "tp_documento,"+
			                                  "nr_documento,"+
			                                  "nm_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "uf_cnh_condutor,"+
			                                  "tp_cnh_condutor,"+
			                                  "cd_marca_autuacao,"+
			                                  "nm_condutor_autuacao,"+
			                                  "nm_proprietario_autuacao,"+
			                                  "nr_cnh_autuacao,"+
			                                  "uf_cnh_autuacao,"+
			                                  "nr_documento_autuacao,"+
			                                  "tp_pessoa_proprietario,"+
			                                  "cd_banco,"+
			                                  "sg_uf_veiculo,"+
			                                  "ds_logradouro,"+
			                                  "ds_nr_imovel,"+
			                                  "nr_cep,"+
			                                  "nr_ddd,"+
			                                  "nr_telefone,"+
			                                  "ds_endereco_condutor,"+
			                                  "nr_cpf_proprietario,"+
			                                  "nr_cpf_cnpj_proprietario,"+
			                                  "nm_complemento,"+
			                                  "ds_bairro_condutor,"+
			                                  "nr_imovel_condutor,"+
			                                  "ds_complemento_condutor,"+
			                                  "cd_cidade_condutor,"+
			                                  "nr_cep_condutor,"+
			                                  "dt_primeiro_registro,"+
			                                  "nr_cpf_condutor,"+
			                                  "cd_veiculo,"+
			                                  "cd_endereco,"+
			                                  "cd_proprietario,"+
			                                  "cd_condutor,"+
			                                  "cd_endereco_condutor,"+
			                                  "dt_notificacao_devolucao,"+
			                                  "tp_origem,"+
			                                  "lg_publicar_nai_diario_oficial,"+
			                                  "tp_convenio," + 
			                                  "id_ait," +
			                                  "cd_talao," +
			                                  "dt_afericao," +
			                                  "nr_lacre," +
			                                  "nr_inventario_inmetro," +
			                                  "dt_prazo_defesa,"+
			                                  "cd_logradouro_infracao, " + 
			                                  "cd_cidade_proprietario," + 
			                                  "cd_convenio,"+
			                                  "txt_cancelamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			pstmt.setString(6,objeto.getDsObservacao());
			pstmt.setString(7,objeto.getDsLocalInfracao());
			pstmt.setInt(8,objeto.getNrAit());
			
			if(objeto.getVlVelocidadePermitida()==null)
				pstmt.setNull(9, Types.DOUBLE);
			else
				pstmt.setDouble(9,objeto.getVlVelocidadePermitida());
			
			if(objeto.getVlVelocidadeAferida()==null)
				pstmt.setNull(10, Types.DOUBLE);
			else
				pstmt.setDouble(10,objeto.getVlVelocidadeAferida());
			
			if(objeto.getVlVelocidadePenalidade()==null)
				pstmt.setNull(11, Types.DOUBLE);
			else
				pstmt.setDouble(11,objeto.getVlVelocidadePenalidade());
			
			pstmt.setString(12,objeto.getNrPlaca());
			pstmt.setString(13,objeto.getDsPontoReferencia());
			pstmt.setInt(14,objeto.getLgAutoAssinado());
			
			if(objeto.getVlLatitude()==null)
				pstmt.setNull(15, Types.DOUBLE);
			else
				pstmt.setDouble(15,objeto.getVlLatitude());
			
			if(objeto.getVlLongitude()==null)
				pstmt.setNull(16, Types.DOUBLE);
			else
				pstmt.setDouble(16,objeto.getVlLongitude());
			
			if(objeto.getCdCidade()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdCidade());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEquipamento());
			
			if(objeto.getVlMulta()==null)
				pstmt.setNull(20, Types.DOUBLE);
			else
				pstmt.setDouble(20,objeto.getVlMulta());
			
			pstmt.setInt(21,objeto.getLgEnviadoDetran());
			pstmt.setInt(22,objeto.getStAit());
			if(objeto.getCdEventoEquipamento()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdEventoEquipamento());
			if(objeto.getDtSincronizacao()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtSincronizacao().getTimeInMillis()));
			pstmt.setInt(25,objeto.getNrTentativaSincronizacao());
			if(objeto.getCdBairro()==0)
				pstmt.setNull(26, Types.INTEGER);
			else
				pstmt.setInt(26,objeto.getCdBairro());
			if(objeto.getCdUsuarioExclusao()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdUsuarioExclusao());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(28, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(28,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			pstmt.setInt(29,objeto.getTpNaturezaAutuacao());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(31,objeto.getTpStatus());
			pstmt.setInt(32,objeto.getTpArquivo());
			pstmt.setInt(33,objeto.getCdOcorrencia());
			pstmt.setString(34,objeto.getDsParecer());
			pstmt.setString(35,objeto.getNrErro());
			pstmt.setInt(36,objeto.getStEntrega());
			pstmt.setString(37,objeto.getNrProcesso());
			if(objeto.getDtRegistroDetran()==null)
				pstmt.setNull(38, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(38,new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(39,objeto.getStRecurso());
			if(objeto.getLgAdvertencia()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getLgAdvertencia());
			pstmt.setString(41,objeto.getNrControle());
			pstmt.setString(42,objeto.getNrRenainf());
			pstmt.setInt(43,objeto.getNrSequencial());
			if(objeto.getDtArNai()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtArNai().getTimeInMillis()));
			pstmt.setInt(45,objeto.getNrNotificacaoNai());
			pstmt.setInt(46,objeto.getNrNotificacaoNip());
			pstmt.setInt(47,objeto.getTpCancelamento());
			pstmt.setInt(48,objeto.getLgCancelaMovimento());
			if(objeto.getDtCancelamentoMovimento()==null)
				pstmt.setNull(49, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(49,new Timestamp(objeto.getDtCancelamentoMovimento().getTimeInMillis()));
			pstmt.setInt(50,objeto.getNrRemessa());
			pstmt.setString(51,objeto.getNrCodigoBarras());
			pstmt.setInt(52,objeto.getNrRemessaRegistro());
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(53, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(53,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			if(objeto.getDtResultadoDefesa()==null)
				pstmt.setNull(54, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(54,new Timestamp(objeto.getDtResultadoDefesa().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(55, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(55,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			if(objeto.getDtResultadoJari()==null)
				pstmt.setNull(56, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(56,new Timestamp(objeto.getDtResultadoJari().getTimeInMillis()));
			if(objeto.getDtResultadoCetran()==null)
				pstmt.setNull(57, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(57,new Timestamp(objeto.getDtResultadoCetran().getTimeInMillis()));
			pstmt.setInt(58,objeto.getLgAdvertenciaJari());
			pstmt.setInt(59,objeto.getLgAdvertenciaCetran());
			pstmt.setInt(60,objeto.getLgNotrigger());
			pstmt.setInt(61,objeto.getStDetran());
			pstmt.setInt(62,objeto.getLgErro());
			pstmt.setString(63,objeto.getTxtObservacao());
			pstmt.setInt(64,objeto.getCdMovimentoAtual());
			if(objeto.getCdAitOrigem()==0)
				pstmt.setNull(65, Types.INTEGER);
			else
				pstmt.setInt(65,objeto.getCdAitOrigem());
			pstmt.setInt(66,objeto.getNrFatorNic());
			pstmt.setInt(67,objeto.getLgDetranFebraban());
			pstmt.setString(68,objeto.getNrPlacaAntiga());
			pstmt.setInt(69,objeto.getCdEspecieVeiculo());
			pstmt.setInt(70,objeto.getCdMarcaVeiculo());
			pstmt.setInt(71,objeto.getCdCorVeiculo());
			pstmt.setInt(72,objeto.getCdTipoVeiculo());
			pstmt.setInt(73,objeto.getCdCategoriaVeiculo());
			pstmt.setString(74,objeto.getNrRenavan());
			pstmt.setString(75,objeto.getNrAnoFabricacao());
			pstmt.setString(76,objeto.getNrAnoModelo());
			pstmt.setString(77,objeto.getNmProprietario());
			pstmt.setInt(78,objeto.getTpDocumento());
			pstmt.setString(79,objeto.getNrDocumento());
			pstmt.setString(80,objeto.getNmCondutor());
			pstmt.setString(81,objeto.getNrCnhCondutor());
			pstmt.setString(82,objeto.getUfCnhCondutor());
			if (objeto.getTpCnhCondutor() == TipoCnhEnum.NAO_INFORMADO.getKey())
				pstmt.setNull(83, Types.INTEGER);
			else
				pstmt.setInt(83, objeto.getTpCnhCondutor());
			pstmt.setInt(84,objeto.getCdMarcaAutuacao());
			pstmt.setString(85,objeto.getNmCondutorAutuacao());
			pstmt.setString(86,objeto.getNmProprietarioAutuacao());
			pstmt.setString(87,objeto.getNrCnhAutuacao());
			pstmt.setString(88,objeto.getUfCnhAutuacao());
			pstmt.setString(89,objeto.getNrDocumentoAutuacao());
			pstmt.setInt(90,objeto.getTpPessoaProprietario());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(91, Types.INTEGER);
			else
				pstmt.setInt(91,objeto.getCdBanco());
			pstmt.setString(92,objeto.getSgUfVeiculo());
			pstmt.setString(93,objeto.getDsLogradouro());
			pstmt.setString(94,objeto.getDsNrImovel());
			pstmt.setString(95,objeto.getNrCep());
			pstmt.setString(96,objeto.getNrDdd());
			pstmt.setString(97,objeto.getNrTelefone());
			pstmt.setString(98,objeto.getDsEnderecoCondutor());
			pstmt.setString(99,objeto.getNrCpfProprietario());
			pstmt.setString(100,objeto.getNrCpfCnpjProprietario());
			pstmt.setString(101,objeto.getNmComplemento());
			pstmt.setString(102,objeto.getDsBairroCondutor());
			pstmt.setString(103,objeto.getNrImovelCondutor());
			pstmt.setString(104,objeto.getDsComplementoCondutor());
			if(objeto.getCdCidadeCondutor()==0)
				pstmt.setNull(105, Types.INTEGER);
			else
				pstmt.setInt(105,objeto.getCdCidadeCondutor());
			pstmt.setString(106,objeto.getNrCepCondutor());
			if(objeto.getDtPrimeiroRegistro()==null)
				pstmt.setNull(107, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(107,new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			pstmt.setString(108,objeto.getNrCpfCondutor());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(109, Types.INTEGER);
			else
				pstmt.setInt(109,objeto.getCdVeiculo());
			pstmt.setInt(110,objeto.getCdEndereco());
			pstmt.setInt(111,objeto.getCdProprietario());
			pstmt.setInt(112,objeto.getCdCondutor());
			pstmt.setInt(113,objeto.getCdEnderecoCondutor());
			if(objeto.getDtNotificacaoDevolucao()==null)
				pstmt.setNull(114, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(114,new Timestamp(objeto.getDtNotificacaoDevolucao().getTimeInMillis()));
			pstmt.setInt(115,objeto.getTpOrigem());
			pstmt.setInt(116,objeto.getLgPublicarNaiDiarioOficial());
			pstmt.setInt(117,objeto.getTpConvenio());
			pstmt.setString(118,objeto.getIdAit());
			if(objeto.getCdTalao()==0)
				pstmt.setNull(119, Types.INTEGER);
			else
				pstmt.setInt(119,objeto.getCdTalao());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(120, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(120,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setString(121,objeto.getNrLacre());
			pstmt.setString(122,objeto.getNrInventarioInmetro());
			if(objeto.getDtPrazoDefesa()==null)
				pstmt.setNull(123, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(123,new Timestamp(objeto.getDtPrazoDefesa().getTimeInMillis()));
			if(objeto.getCdLogradouroInfracao()==0)
				pstmt.setNull(124, Types.INTEGER);
			else
				pstmt.setInt(124,objeto.getCdLogradouroInfracao());
			if(objeto.getCdCidadeProprietario()==0)
				pstmt.setNull(125, Types.INTEGER);
			else
				pstmt.setInt(125,objeto.getCdCidadeProprietario());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(126, Types.INTEGER);
			else
				pstmt.setInt(126,objeto.getCdConvenio());
			pstmt.setString(127,objeto.getTxtCancelamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ait objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Ait objeto, int cdAitOld) {
		return update(objeto, cdAitOld, null);
	}

	public static int update(Ait objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Ait objeto, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait SET cd_ait=?,"+
												      		   "cd_infracao=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_usuario=?,"+
												      		   "ds_observacao=?,"+
												      		   "ds_local_infracao=?,"+
												      		   "nr_ait=?,"+
												      		   "vl_velocidade_permitida=?,"+
												      		   "vl_velocidade_aferida=?,"+
												      		   "vl_velocidade_penalidade=?,"+
												      		   "nr_placa=?,"+
												      		   "ds_ponto_referencia=?,"+
												      		   "lg_auto_assinado=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+
												      		   "cd_cidade=?,"+
												      		   "dt_digitacao=?,"+
												      		   "cd_equipamento=?,"+
												      		   "vl_multa=?,"+
												      		   "lg_enviado_detran=?,"+
												      		   "st_ait=?,"+
												      		   "cd_evento_equipamento=?,"+
												      		   "dt_sincronizacao=?,"+
												      		   "nr_tentativa_sincronizacao=?,"+
												      		   "cd_bairro=?,"+
												      		   "cd_usuario_exclusao=?,"+
												      		   "dt_vencimento=?,"+
												      		   "tp_natureza_autuacao=?,"+
												      		   "dt_movimento=?,"+
												      		   "tp_status=?,"+
												      		   "tp_arquivo=?,"+
												      		   "cd_ocorrencia=?,"+
												      		   "ds_parecer=?,"+
												      		   "nr_erro=?,"+
												      		   "st_entrega=?,"+
												      		   "nr_processo=?,"+
												      		   "dt_registro_detran=?,"+
												      		   "st_recurso=?,"+
												      		   "lg_advertencia=?,"+
												      		   "nr_controle=?,"+
												      		   "nr_renainf=?,"+
												      		   "nr_sequencial=?,"+
												      		   "dt_ar_nai=?,"+
												      		   "nr_notificacao_nai=?,"+
												      		   "nr_notificacao_nip=?,"+
												      		   "tp_cancelamento=?,"+
												      		   "lg_cancela_movimento=?,"+
												      		   "dt_cancelamento_movimento=?,"+
												      		   "nr_remessa=?,"+
												      		   "nr_codigo_barras=?,"+
												      		   "nr_remessa_registro=?,"+
												      		   "dt_emissao_nip=?,"+
												      		   "dt_resultado_defesa=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "dt_resultado_jari=?,"+
												      		   "dt_resultado_cetran=?,"+
												      		   "lg_advertencia_jari=?,"+
												      		   "lg_advertencia_cetran=?,"+
												      		   "lg_notrigger=?,"+
												      		   "st_detran=?,"+
												      		   "lg_erro=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_movimento_atual=?,"+
												      		   "cd_ait_origem=?,"+
												      		   "nr_fator_nic=?,"+
												      		   "lg_detran_febraban=?,"+
												      		   "nr_placa_antiga=?,"+
												      		   "cd_especie_veiculo=?,"+
												      		   "cd_marca_veiculo=?,"+
												      		   "cd_cor_veiculo=?,"+
												      		   "cd_tipo_veiculo=?,"+
												      		   "cd_categoria_veiculo=?,"+
												      		   "nr_renavan=?,"+
												      		   "nr_ano_fabricacao=?,"+
												      		   "nr_ano_modelo=?,"+
												      		   "nm_proprietario=?,"+
												      		   "tp_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "nm_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "uf_cnh_condutor=?,"+
												      		   "tp_cnh_condutor=?,"+
												      		   "cd_marca_autuacao=?,"+
												      		   "nm_condutor_autuacao=?,"+
												      		   "nm_proprietario_autuacao=?,"+
												      		   "nr_cnh_autuacao=?,"+
												      		   "uf_cnh_autuacao=?,"+
												      		   "nr_documento_autuacao=?,"+
												      		   "tp_pessoa_proprietario=?,"+
												      		   "cd_banco=?,"+
												      		   "sg_uf_veiculo=?,"+
												      		   "ds_logradouro=?,"+
												      		   "ds_nr_imovel=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_ddd=?,"+
												      		   "nr_telefone=?,"+
												      		   "ds_endereco_condutor=?,"+
												      		   "nr_cpf_proprietario=?,"+
												      		   "nr_cpf_cnpj_proprietario=?,"+
												      		   "nm_complemento=?,"+
												      		   "ds_bairro_condutor=?,"+
												      		   "nr_imovel_condutor=?,"+
												      		   "ds_complemento_condutor=?,"+
												      		   "cd_cidade_condutor=?,"+
												      		   "nr_cep_condutor=?,"+
												      		   "dt_primeiro_registro=?,"+
												      		   "nr_cpf_condutor=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_endereco=?,"+
												      		   "cd_proprietario=?,"+
												      		   "cd_condutor=?,"+
												      		   "cd_endereco_condutor=?,"+
												      		   "dt_notificacao_devolucao=?,"+
												      		   "tp_origem=?,"+
												      		   "lg_publicar_nai_diario_oficial=?,"+
												      		   "tp_convenio=?," + 
												      		   "id_ait=?," + 
												      		   "cd_talao=?," + 
												      		   "dt_afericao=?," + 
												      		   "nr_lacre=?, " +
												      		   "nr_inventario_inmetro=?, "+
												      		   "dt_prazo_defesa=?,"+
												      		   "cd_logradouro_infracao=?,"+
												      		   "cd_cidade_proprietario=?,"+
												      		   "txt_cancelamento=? WHERE cd_ait=?");
			pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			pstmt.setString(5,objeto.getDsObservacao());
			pstmt.setString(6,objeto.getDsLocalInfracao());
			pstmt.setInt(7,objeto.getNrAit());
			
			if(objeto.getVlVelocidadePermitida()==null)
				pstmt.setNull(8, Types.DOUBLE);
			else
				pstmt.setDouble(8,objeto.getVlVelocidadePermitida());
			
			if(objeto.getVlVelocidadeAferida()==null)
				pstmt.setNull(9, Types.DOUBLE);
			else
				pstmt.setDouble(9,objeto.getVlVelocidadeAferida());
			
			if(objeto.getVlVelocidadePenalidade()==null)
				pstmt.setNull(10, Types.DOUBLE);
			else
				pstmt.setDouble(10,objeto.getVlVelocidadePenalidade());
			
			pstmt.setString(11,objeto.getNrPlaca());
			pstmt.setString(12,objeto.getDsPontoReferencia());
			pstmt.setInt(13,objeto.getLgAutoAssinado());
			
			if(objeto.getVlLatitude()==null)
				pstmt.setNull(14, Types.DOUBLE);
			else
				pstmt.setDouble(14,objeto.getVlLatitude());
			
			if(objeto.getVlLongitude()==null)
				pstmt.setNull(15, Types.DOUBLE);
			else
				pstmt.setDouble(15,objeto.getVlLongitude());
			
			if(objeto.getCdCidade()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdCidade());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEquipamento());
			
			if(objeto.getVlMulta()==null)
				pstmt.setNull(19, Types.DOUBLE);
			else
				pstmt.setDouble(19,objeto.getVlMulta());
			
			
			pstmt.setInt(20,objeto.getLgEnviadoDetran());
			pstmt.setInt(21,objeto.getStAit());
			if(objeto.getCdEventoEquipamento()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdEventoEquipamento());
			if(objeto.getDtSincronizacao()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtSincronizacao().getTimeInMillis()));
			pstmt.setInt(24,objeto.getNrTentativaSincronizacao());
			if(objeto.getCdBairro()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdBairro());
			if(objeto.getCdUsuarioExclusao()==0)
				pstmt.setNull(26, Types.INTEGER);
			else
				pstmt.setInt(26,objeto.getCdUsuarioExclusao());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(27, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(27,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			pstmt.setInt(28,objeto.getTpNaturezaAutuacao());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(29, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(29,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(30,objeto.getTpStatus());
			pstmt.setInt(31,objeto.getTpArquivo());
			pstmt.setInt(32,objeto.getCdOcorrencia());
			pstmt.setString(33,objeto.getDsParecer());
			pstmt.setString(34,objeto.getNrErro());
			pstmt.setInt(35,objeto.getStEntrega());
			pstmt.setString(36,objeto.getNrProcesso());
			if(objeto.getDtRegistroDetran()==null)
				pstmt.setNull(37, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(37,new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(38,objeto.getStRecurso());
			pstmt.setInt(39,objeto.getLgAdvertencia());
			pstmt.setString(40,objeto.getNrControle());
			pstmt.setString(41,objeto.getNrRenainf());
			pstmt.setInt(42,objeto.getNrSequencial());
			if(objeto.getDtArNai()==null)
				pstmt.setNull(43, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(43,new Timestamp(objeto.getDtArNai().getTimeInMillis()));
			pstmt.setInt(44,objeto.getNrNotificacaoNai());
			pstmt.setInt(45,objeto.getNrNotificacaoNip());
			pstmt.setInt(46,objeto.getTpCancelamento());
			pstmt.setInt(47,objeto.getLgCancelaMovimento());
			if(objeto.getDtCancelamentoMovimento()==null)
				pstmt.setNull(48, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(48,new Timestamp(objeto.getDtCancelamentoMovimento().getTimeInMillis()));
			pstmt.setInt(49,objeto.getNrRemessa());
			pstmt.setString(50,objeto.getNrCodigoBarras());
			pstmt.setInt(51,objeto.getNrRemessaRegistro());
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(52, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(52,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			if(objeto.getDtResultadoDefesa()==null)
				pstmt.setNull(53, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(53,new Timestamp(objeto.getDtResultadoDefesa().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(54, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(54,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			if(objeto.getDtResultadoJari()==null)
				pstmt.setNull(55, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(55,new Timestamp(objeto.getDtResultadoJari().getTimeInMillis()));
			if(objeto.getDtResultadoCetran()==null)
				pstmt.setNull(56, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(56,new Timestamp(objeto.getDtResultadoCetran().getTimeInMillis()));
			pstmt.setInt(57,objeto.getLgAdvertenciaJari());
			pstmt.setInt(58,objeto.getLgAdvertenciaCetran());
			pstmt.setInt(59,objeto.getLgNotrigger());
			pstmt.setInt(60,objeto.getStDetran());
			pstmt.setInt(61,objeto.getLgErro());
			pstmt.setString(62,objeto.getTxtObservacao());
			pstmt.setInt(63,objeto.getCdMovimentoAtual());
			if(objeto.getCdAitOrigem()==0)
				pstmt.setNull(64, Types.INTEGER);
			else
				pstmt.setInt(64,objeto.getCdAitOrigem());
			pstmt.setInt(65,objeto.getNrFatorNic());
			pstmt.setInt(66,objeto.getLgDetranFebraban());
			pstmt.setString(67,objeto.getNrPlacaAntiga());
			pstmt.setInt(68,objeto.getCdEspecieVeiculo());
			pstmt.setInt(69,objeto.getCdMarcaVeiculo());
			pstmt.setInt(70,objeto.getCdCorVeiculo());
			pstmt.setInt(71,objeto.getCdTipoVeiculo());
			pstmt.setInt(72,objeto.getCdCategoriaVeiculo());
			pstmt.setString(73,objeto.getNrRenavan());
			pstmt.setString(74,objeto.getNrAnoFabricacao());
			pstmt.setString(75,objeto.getNrAnoModelo());
			pstmt.setString(76,objeto.getNmProprietario());
			pstmt.setInt(77,objeto.getTpDocumento());
			pstmt.setString(78,objeto.getNrDocumento());
			pstmt.setString(79,objeto.getNmCondutor());
			pstmt.setString(80,objeto.getNrCnhCondutor());
			pstmt.setString(81,objeto.getUfCnhCondutor());
			if (objeto.getTpCnhCondutor() == TipoCnhEnum.NAO_INFORMADO.getKey())
				pstmt.setNull(82, Types.INTEGER);
			else
				pstmt.setInt(82, objeto.getTpCnhCondutor());
			pstmt.setInt(83,objeto.getCdMarcaAutuacao());
			pstmt.setString(84,objeto.getNmCondutorAutuacao());
			pstmt.setString(85,objeto.getNmProprietarioAutuacao());
			pstmt.setString(86,objeto.getNrCnhAutuacao());
			pstmt.setString(87,objeto.getUfCnhAutuacao());
			pstmt.setString(88,objeto.getNrDocumentoAutuacao());
			pstmt.setInt(89,objeto.getTpPessoaProprietario());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(90, Types.INTEGER);
			else
				pstmt.setInt(90,objeto.getCdBanco());
			pstmt.setString(91,objeto.getSgUfVeiculo());
			pstmt.setString(92,objeto.getDsLogradouro());
			pstmt.setString(93,objeto.getDsNrImovel());
			pstmt.setString(94,objeto.getNrCep());
			pstmt.setString(95,objeto.getNrDdd());
			pstmt.setString(96,objeto.getNrTelefone());
			pstmt.setString(97,objeto.getDsEnderecoCondutor());
			pstmt.setString(98,objeto.getNrCpfProprietario());
			pstmt.setString(99,objeto.getNrCpfCnpjProprietario());
			pstmt.setString(100,objeto.getNmComplemento());
			pstmt.setString(101,objeto.getDsBairroCondutor());
			pstmt.setString(102,objeto.getNrImovelCondutor());
			pstmt.setString(103,objeto.getDsComplementoCondutor());
			if(objeto.getCdCidadeCondutor()==0)
				pstmt.setNull(104, Types.INTEGER);
			else
				pstmt.setInt(104,objeto.getCdCidadeCondutor());
			pstmt.setString(105,objeto.getNrCepCondutor());
			if(objeto.getDtPrimeiroRegistro()==null)
				pstmt.setNull(106, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(106,new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			pstmt.setString(107,objeto.getNrCpfCondutor());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(108, Types.INTEGER);
			else
				pstmt.setInt(108,objeto.getCdVeiculo());
			pstmt.setInt(109,objeto.getCdEndereco());
			pstmt.setInt(110,objeto.getCdProprietario());
			pstmt.setInt(111,objeto.getCdCondutor());
			pstmt.setInt(112,objeto.getCdEnderecoCondutor());
			if(objeto.getDtNotificacaoDevolucao()==null)
				pstmt.setNull(113, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(113,new Timestamp(objeto.getDtNotificacaoDevolucao().getTimeInMillis()));
			pstmt.setInt(114,objeto.getTpOrigem());
			pstmt.setInt(115,objeto.getLgPublicarNaiDiarioOficial());
			pstmt.setInt(116,objeto.getTpConvenio());
			pstmt.setString(117,objeto.getIdAit());
			if(objeto.getCdTalao()==0)
				pstmt.setNull(118, Types.INTEGER);
			else
				pstmt.setInt(118,objeto.getCdTalao());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(119, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(119,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setString(120,objeto.getNrLacre());
			pstmt.setString(121,objeto.getNrInventarioInmetro());
			if(objeto.getDtPrazoDefesa()==null)
				pstmt.setNull(122, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(122,new Timestamp(objeto.getDtPrazoDefesa().getTimeInMillis()));			
			if(objeto.getCdLogradouroInfracao()==0)
				pstmt.setNull(123, Types.INTEGER);
			else
				pstmt.setInt(123,objeto.getCdLogradouroInfracao());
			if(objeto.getCdCidadeProprietario()==0)
				pstmt.setNull(124, Types.INTEGER);
			else
				pstmt.setInt(124,objeto.getCdCidadeProprietario());
			pstmt.setString(125,objeto.getTxtCancelamento());
			pstmt.setInt(126, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! AitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.out.println("Erro! AitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit) {
		return delete(cdAit, null);
	}

	public static int delete(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait WHERE cd_ait=?");
			pstmt.setInt(1, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ait get(int cdAit) {
		return get(cdAit, null);
	}

	public static Ait get(int cdAit, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			if (lgBaseAntiga)
			{
				pstmt = connect.prepareStatement("SELECT * FROM ait WHERE codigo_ait=?");
				pstmt.setInt(1, cdAit);
				rs = pstmt.executeQuery();
				if(rs.next()){
					
					return new Ait(rs.getInt("codigo_ait"),
					    rs.getInt("cod_infracao"),
						rs.getInt("cod_agente"),
						rs.getInt("cod_usuario"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_infracao"),
						Integer.parseInt(rs.getString("nr_ait").replaceAll("[^\\d]", "")),
						rs.getDouble("vl_velocidade_permitida"),
						rs.getDouble("vl_velocidade_aferida"),
						rs.getDouble("vl_velocidade_penalidade"),
						rs.getString("nr_placa"),
						rs.getString("ds_ponto_referencia"),
						rs.getInt("lg_auto_assinado"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cod_municipio"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("cd_equipamento"),
						rs.getDouble("vl_multa"),
						rs.getInt("lg_enviado_detran"),
						rs.getInt("st_ait"),
						rs.getInt("cd_evento_equipamento"),
						
						(rs.getTimestamp("dt_sincronizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_sincronizacao").getTime()),
						
						//Não tem esse campo na base antiga assim foi adicionado 0
						//rs.getInt("nr_tentativa_sincronizacao"),
						0,
								
						
						rs.getInt("cod_bairro"),
						rs.getInt("cd_usuario_exclusao"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						rs.getInt("tp_natureza_autuacao"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("tp_status"),
						rs.getInt("tp_arquivo"),
						rs.getInt("cod_ocorrencia"),
						rs.getString("ds_parecer"),
						rs.getString("nr_erro"),
						rs.getInt("st_entrega"),
						rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"),
						rs.getInt("lg_advertencia"),
						rs.getString("nr_controle"),
						rs.getString("nr_renainf"),
						rs.getInt("nr_sequencial"),
						(rs.getTimestamp("dt_ar_nai")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ar_nai").getTime()),
						rs.getInt("nr_notificacao_nai"),
						rs.getInt("nr_notificacao_nip"),
						rs.getInt("tp_cancelamento"),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento_movimento").getTime()),
						rs.getInt("nr_remessa"),
						rs.getString("nr_codigo_barras"),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_emissao_nip")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_nip").getTime()),
						(rs.getTimestamp("dt_resultado_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_defesa").getTime()),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						(rs.getTimestamp("dt_resultado_jari")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_jari").getTime()),
						(rs.getTimestamp("dt_resultado_cetran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_cetran").getTime()),
						(rs.getTimestamp("dt_prazo_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo_defesa").getTime()),
						rs.getInt("lg_advertencia_jari"),
						rs.getInt("lg_advertencia_cetran"),
						rs.getInt("lg_notrigger"),
						rs.getInt("st_detran"),
						rs.getInt("lg_erro"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_movimento_atual"),
						rs.getInt("cd_ait_origem"),
						rs.getInt("nr_fator_nic"),
						rs.getInt("lg_detran_febraban"),
						rs.getString("nr_placa_antiga"),
						rs.getInt("cod_especie"),
						rs.getInt("cod_marca"),
						rs.getInt("cod_cor"),
						rs.getInt("cod_tipo"),
						rs.getInt("cod_categoria"),
						rs.getString("cd_renavan"),
						rs.getString("ds_ano_fabricacao"),
						rs.getString("ds_ano_modelo"),
						rs.getString("nm_proprietario"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getInt("tp_cnh_condutor"),
						rs.getInt("cod_marca_autuacao"),
						rs.getString("nm_condutor_autuacao"),
						rs.getString("nm_proprietario_autuacao"),
						rs.getString("nr_cnh_autuacao"),
						rs.getString("uf_cnh_autuacao"),
						rs.getString("nr_documento_autuacao"),
						rs.getInt("tp_pessoa_proprietario"),
						rs.getInt("cod_banco"),
						rs.getString("uf_veiculo"),
						rs.getString("ds_logradouro"),
						rs.getString("ds_nr_imovel"),
						rs.getString("nr_cep"),
						rs.getString("nr_ddd"),
						rs.getString("nr_telefone"),
						rs.getString("ds_endereco_condutor"),
						rs.getString("nr_cpf_proprietario"),
						rs.getString("nr_cpf_cnpj_proprietario"),
						rs.getString("nm_complemento"),
						rs.getString("ds_bairro_condutor"),
						rs.getString("nr_imovel_condutor"),
						rs.getString("ds_complemento_condutor"),
						rs.getInt("cd_municipio_condutor"),
						rs.getString("nr_cep_condutor"),
						(rs.getTimestamp("dt_primeiro_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						rs.getString("nr_cpf_condutor"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_proprietario"),
						rs.getInt("cd_condutor"),
						rs.getInt("cd_endereco_condutor"),
						(rs.getTimestamp("dt_notificacao_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao_devolucao").getTime()),
						rs.getInt("tp_origem"),
						0,
						rs.getInt("tp_convenio"),
						rs.getString("nr_ait"),
						0,
						null,
						null,
						null,
						0,
						0,
						0,
						0,
						rs.getString("txt_cancelamento"));
				}
				else
					return null;
			}
			else
			{
				pstmt = connect.prepareStatement("SELECT * FROM mob_ait WHERE cd_ait=?");
				pstmt.setInt(1, cdAit);
				rs = pstmt.executeQuery();
				if(rs.next()){
				
					return new Ait(rs.getInt("cd_ait"),
					    rs.getInt("cd_infracao"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_infracao"),
						rs.getInt("nr_ait"),
						rs.getDouble("vl_velocidade_permitida"),
						rs.getDouble("vl_velocidade_aferida"),
						rs.getDouble("vl_velocidade_penalidade"),
						rs.getString("nr_placa"),
						rs.getString("ds_ponto_referencia"),
						rs.getInt("lg_auto_assinado"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cd_cidade"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("cd_equipamento"),
						rs.getDouble("vl_multa"),
						rs.getInt("lg_enviado_detran"),
						rs.getInt("st_ait"),
						rs.getInt("cd_evento_equipamento"),
						(rs.getTimestamp("dt_sincronizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_sincronizacao").getTime()),
						rs.getInt("nr_tentativa_sincronizacao"),
						rs.getInt("cd_bairro"),
						rs.getInt("cd_usuario_exclusao"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						rs.getInt("tp_natureza_autuacao"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("tp_status"),
						rs.getInt("tp_arquivo"),
						rs.getInt("cd_ocorrencia"),
						rs.getString("ds_parecer"),
						rs.getString("nr_erro"),
						rs.getInt("st_entrega"),
						rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"),
						rs.getInt("lg_advertencia"),
						rs.getString("nr_controle"),
						rs.getString("nr_renainf"),
						rs.getInt("nr_sequencial"),
						(rs.getTimestamp("dt_ar_nai")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ar_nai").getTime()),
						rs.getInt("nr_notificacao_nai"),
						rs.getInt("nr_notificacao_nip"),
						rs.getInt("tp_cancelamento"),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento_movimento").getTime()),
						rs.getInt("nr_remessa"),
						rs.getString("nr_codigo_barras"),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_emissao_nip")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_nip").getTime()),
						(rs.getTimestamp("dt_resultado_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_defesa").getTime()),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						(rs.getTimestamp("dt_resultado_jari")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_jari").getTime()),
						(rs.getTimestamp("dt_resultado_cetran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_cetran").getTime()),
						(rs.getTimestamp("dt_prazo_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo_defesa").getTime()),
						rs.getInt("lg_advertencia_jari"),
						rs.getInt("lg_advertencia_cetran"),
						rs.getInt("lg_notrigger"),
						rs.getInt("st_detran"),
						rs.getInt("lg_erro"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_movimento_atual"),
						rs.getInt("cd_ait_origem"),
						rs.getInt("nr_fator_nic"),
						rs.getInt("lg_detran_febraban"),
						rs.getString("nr_placa_antiga"),
						rs.getInt("cd_especie_veiculo"),
						rs.getInt("cd_marca_veiculo"),
						rs.getInt("cd_cor_veiculo"),
						rs.getInt("cd_tipo_veiculo"),
						rs.getInt("cd_categoria_veiculo"),
						rs.getString("nr_renavan"),
						rs.getString("nr_ano_fabricacao"),
						rs.getString("nr_ano_modelo"),
						rs.getString("nm_proprietario"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getInt("tp_cnh_condutor"),
						rs.getInt("cd_marca_autuacao"),
						rs.getString("nm_condutor_autuacao"),
						rs.getString("nm_proprietario_autuacao"),
						rs.getString("nr_cnh_autuacao"),
						rs.getString("uf_cnh_autuacao"),
						rs.getString("nr_documento_autuacao"),
						rs.getInt("tp_pessoa_proprietario"),
						rs.getInt("cd_banco"),
						rs.getString("sg_uf_veiculo"),
						rs.getString("ds_logradouro"),
						rs.getString("ds_nr_imovel"),
						rs.getString("nr_cep"),
						rs.getString("nr_ddd"),
						rs.getString("nr_telefone"),
						rs.getString("ds_endereco_condutor"),
						rs.getString("nr_cpf_proprietario"),
						rs.getString("nr_cpf_cnpj_proprietario"),
						rs.getString("nm_complemento"),
						rs.getString("ds_bairro_condutor"),
						rs.getString("nr_imovel_condutor"),
						rs.getString("ds_complemento_condutor"),
						rs.getInt("cd_cidade_condutor"),
						rs.getString("nr_cep_condutor"),
						(rs.getTimestamp("dt_primeiro_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						rs.getString("nr_cpf_condutor"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_proprietario"),
						rs.getInt("cd_condutor"),
						rs.getInt("cd_endereco_condutor"),
						(rs.getTimestamp("dt_notificacao_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao_devolucao").getTime()),
						rs.getInt("tp_origem"),
						rs.getInt("lg_publicar_nai_diario_oficial"),
						rs.getInt("tp_convenio"),
						rs.getString("id_ait"),
						rs.getInt("cd_talao"),
						(rs.getTimestamp("dt_afericao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao").getTime()),
						rs.getString("nr_lacre"),
						rs.getString("nr_inventario_inmetro"),
						rs.getInt("cd_logradouro_infracao"),
						rs.getInt("cd_cidade_proprietario"),
						rs.getInt("cd_convenio"),
						rs.getInt("cd_pais"),
						rs.getString("txt_cancelamento"));	
				}
				else{
					return null;
			}
		}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Ait> getList() {
		return getList(null);
	}

	public static ArrayList<Ait> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Ait> list = new ArrayList<Ait>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Ait obj = AitDAO.get(rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

