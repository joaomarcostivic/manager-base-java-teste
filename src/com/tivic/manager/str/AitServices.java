package com.tivic.manager.str;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.AgenteServices;
import com.tivic.manager.mob.AitEvento;
import com.tivic.manager.mob.AitEventoServices;
import com.tivic.manager.mob.AitSyncServices;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoArquivoServices;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.EventoEquipamentoServices;
import com.tivic.manager.mob.TalonarioServices;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.TipoEventoServices;
import com.tivic.manager.mob.ait.SituacaoAitEnum;
import com.tivic.manager.mob.ait.StConsistenciaAitEnum;
import com.tivic.manager.mob.tipoevento.enums.IdTipoEventoEnum;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.str.ait.AitDuplicidadeDatabaseFactory;
import com.tivic.manager.str.ait.IAitVerificadorDuplicidade;
import com.tivic.manager.str.sync.logs.FlexSyncLog;
import com.tivic.manager.str.sync.logs.instance.FlexAitLog;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.report.ReportServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AitServices {
	
	// Constantes do tipo de competencia da aplicacao da multa
	public static final int COMPETENCIA_MUNICIPAL = 0;
	public static final int COMPETENCIA_ESTADUAL = 1;
	
	public static final int ST_CANCELADO = 0;
	public static final int ST_CONFIRMADO   = 1;
	public static final int ST_PENDENTE_CONFIRMACAO   = 2;
	
	private static final int ERR_AIT_DUPLICADA = -1550; 
	
	public static Result sync(ArrayList<Ait> aits) {

		boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
		if(str2mob) {
			return AitSyncServices.sync(toMob(aits));
		}
		
		return sync(aits, null);
	}
	
	public static Result sync(ArrayList<Ait> aits, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			try {
				FlexSyncLog flexSyncLog = new FlexSyncLog();
				FlexAitLog flexAitLog = new FlexAitLog();
				flexAitLog.setData(aits);
				
				flexSyncLog.addInstance(flexAitLog);
				
				flexSyncLog.write();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
			
//			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
//			boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
			
			if(aits.size()>1) {
				for (Ait ait : aits) {
					for (Ait aitCheck : aits) {
						if(ait!=aitCheck && ait.getNrAit()==aitCheck.getNrAit()) {
							aits.remove(ait);
							break;
						}
					}
				}
			}
			
			ArrayList<Ait> aitsRetorno = new ArrayList<Ait>();
//			ArrayList<Ait> aitsDuplicadas = new ArrayList<Ait>();
//			ArrayList<Ait> aitsErro = new ArrayList<Ait>();
			
			// divide as ait em lote 
			int done = 0;
			int left = aits.size();
			int lote = aits.size();
			
			if(Util.getConfManager().getProps().getProperty("AIT_LOTE_SYNC") != null)
				lote = Integer.parseInt(Util.getConfManager().getProps().getProperty("AIT_LOTE_SYNC"));
			
			int retorno = 0;
			for (Ait ait: aits) {
				
				done++;
				left--;
				
				Result r = sync(ait, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					//aitsErro.add(ait);
					break;
				}
//				else if(r.getCode()==2) {
//					//aitsDuplicadas.add(ait);
//					continue;
//				}
				else {
					ait.setCdAit(r.getCode());
					//aitsRetorno.add(ait);
				}
								
				if(done % lote == 0 || left < lote) {
									
					connect.commit();
					Conexao.desconectar(connect);
					
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					//XXX
					System.out.println("\t\t"+done+" COMMIT");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
//			else if (isConnectionNull)
//				connect.commit();		
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (aits.size() == aitsRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar AITs.");
			
//			r.addObject("AITS", aitsRetorno);
//			r.addObject("AITS_ERRO", aitsErro);
//			r.addObject("AITS_DUPLICADAS", aitsDuplicadas);
			
			//XXX:
			System.out.println(r);
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AITs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result syncItem(Ait ait) {
		return sync(ait, null);
	}
	
	public static Result sync(Ait ait, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo AIT...");
			System.out.println("\tNr. AIT: "+ait.getNrAit());
			System.out.println("\tPlaca: "+ait.getNrPlaca());
			System.out.println("\tAgente: "+com.tivic.manager.str.AgenteDAO.get(ait.getCdAgente(), connect).getNmAgente());
			System.out.println("\tInfracao: "+(ait.getCdInfracao() != 0 ? InfracaoDAO.get(ait.getCdInfracao(), connect).getNrCodDetran() : "Sem infração, possívelmente cancelada."));
			System.out.println("\tLocalizacao: ["+ait.getVlLatitude()+", "+ait.getVlLongitude()+"]");
			System.out.println("\tSituação. AIT: "+ait.getStAit());
			
			int retorno = 0;
			decidirConsistencia(ait);
			
			Ait aitDuplicada = verificarDuplicidade(ait, connect); 
			
			if(aitDuplicada != null) System.out.println("Diagnostico: AIT Duplicada...");
			
			boolean   gctPadrao  = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO");
			Talonario talonario  = TalonarioDAO.get(ait.getCdTalao(), connect);
			int nrAit = 0;
			//ID_AIT
			if(gctPadrao) {					
				//XXX:
				System.out.println("\tCD_TALAO = "+ait.getCdTalao());
				System.out.println("\tID_AIT = "+ait.getIdAit());
				
				if(talonario != null) {
					nrAit = ait.getNrAit();
					String sgTalao = checkSiglaTalao(talonario) + (aitDuplicada != null ? "D" : ""); 
					ait.setIdAit(!sgTalao.trim().equals("") ? sgTalao + Util.fillNum(ait.getNrAit(), 10 - sgTalao.length()) : String.valueOf(ait.getNrAit()));
					ait.setStAit(aitDuplicada != null ? AitServices.ST_CANCELADO : ait.getStAit());
					System.out.println("\tSG_TALAO = "+talonario.getSgTalao());
				}
			}
						
			if(aitDuplicada != null && verificarDuplicidade(ait, connect) != null) {
				System.out.println("Diagnostico: AIT já sincronizada...");
				retorno = 1;
			} else {
				retorno = insert(ait, connect);
				ait.setNrAit(nrAit != 0 ? nrAit : ait.getNrAit());
				
				if(retorno > 0) {
					System.out.println("Diagnostico: AIT Recebida...");
					
					ait.setCdAit(retorno);
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
				
				if(retorno<=0)
					Conexao.rollback(connect);
				else if (isConnectionNull)
					connect.commit();
			}
			
			Result result = new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar AITs.");
			result.addObject("AIT", ait);
			
			return result;
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AIT ");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Ait verificarDuplicidade(Ait ait, Connection connect) throws Exception {
		IAitVerificadorDuplicidade verificador = new AitDuplicidadeDatabaseFactory().verificador();
		return verificador.findByNrAit(ait, connect).get();
	}
	
	public static int insert(Ait objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
			boolean lgGctPadrao = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO");
			
			int code = Conexao.getSequenceCode("ait", connect);

			objeto.setCdAit(code);
			
			String sql = "INSERT INTO str_ait (cd_ait,"+
                    "cd_infracao,"+
                    "cd_agente,"+
                    "cd_usuario,"+
                    "cd_especie,"+
                    "cd_marca,"+
                    "cd_cor,"+
                    "cd_tipo,"+
                    "dt_infracao,"+
                    "cd_categoria,"+
                    "ds_observacao,"+
                    "ds_local_infracao,"+
                    "nr_renavan,"+
                    "ds_ano_fabricacao,"+
                    "ds_ano_modelo,"+
                    "nm_proprietario,"+
                    "tp_documento,"+
                    "nr_documento,"+
                    "nr_ait,"+
                    "nm_condutor,"+
                    "nr_cnh_condutor,"+
                    "uf_cnh_condutor,"+
                    "vl_velocidade_permitida,"+
                    "vl_velocidade_aferida,"+
                    "vl_velocidade_penalidade,"+
                    "nr_placa,"+
                    "ds_ponto_referencia,"+
                    "lg_auto_assinado,"+
                    "vl_longitude,"+
                    "vl_latitude,"+
                    "cd_cidade,"+
                    "tp_cnh_condutor,"+
                    "dt_digitacao,"+
                    "vl_multa,"+
                    "st_ait,"+
                    "lg_enviado_detran) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			if(lgBaseAntiga) {
				sql = "INSERT INTO AIT (codigo_ait,"+
	                    "cod_infracao,"+
	                    "cod_agente,"+
	                    "cod_usuario,"+
	                    "cod_especie,"+
	                    "cod_marca,"+
	                    "cod_cor,"+
	                    "cod_tipo,"+
	                    "dt_infracao,"+
	                    "cod_categoria,"+
	                    "ds_observacao,"+
	                    "ds_local_infracao,"+
	                    "cd_renavan,"+
	                    "ds_ano_fabricacao,"+
	                    "ds_ano_modelo,"+
	                    "nm_proprietario,"+
	                    "tp_documento,"+
	                    "nr_documento,"+
	                    "nr_ait,"+
	                    "nm_condutor,"+
	                    "nr_cnh_condutor,"+
	                    "uf_cnh_condutor,"+
	                    "vl_velocidade_permitida,"+
	                    "vl_velocidade_aferida,"+
	                    "vl_velocidade_penalidade,"+
	                    "nr_placa,"+
	                    "ds_ponto_referencia,"+
	                    "lg_auto_assinado,"+
	                    "vl_longitude,"+
	                    "vl_latitude,"+
	                    "cod_municipio,"+
	                    "tp_cnh_condutor,"+
	                    "dt_digitacao,"+
	                    "cd_equipamento,"+
	                    "tp_convenio,"+
	                    
	                    //CAMPOS QUE GRAVAM O AIT EXATAMENTE COMO NO MOMENTO DA AUTUACAO
	                    "COD_MARCA_AUTUACAO,"+
	                    "NM_CONDUTOR_AUTUACAO,"+
	                    "NM_PROPRIETARIO_AUTUACAO,"+
	                    "NR_CNH_AUTUACAO,"+
	                    "UF_CNH_AUTUACAO,"+
	                    "NR_DOCUMENTO_AUTUACAO,"+

	                    // 
	                    "VL_MULTA,"+
	                    "ST_AIT," +
	                    "LG_ENVIADO_DETRAN,"+ 
	                    "COD_TALAO,"+
	                    "nr_cpf_cnpj_proprietario,"+
	                    "nr_cpf_condutor,"+
	                    "cod_ocorrencia"+
	                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			}
						
			PreparedStatement pstmt = connect.prepareStatement(sql);
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
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEspecie());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMarca());
			if(objeto.getCdCor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCor());
			if(objeto.getCdTipo()<=0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else {
				objeto.getDtInfracao().setTimeZone(TimeZone.getTimeZone("GMT"));
				pstmt.setTimestamp(9, new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			}
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCategoria());
			pstmt.setString(11, objeto.getDsObservacao() == null ? "" : Util.retirarAcentos(objeto.getDsObservacao().toUpperCase()));
			
			/* TODO
			 * Tratar a informaÃ§Ã£o getDsLocalInfracao para limitar a quantidade de caracteres,
			 * jÃ¡ que o limite de caracteres aceitos pelo campo na tabela Ã© 100.
			 * */
			objeto.setDsLocalInfracao(Util.retirarAcentos(objeto.getDsLocalInfracao()));
			pstmt.setString(12, objeto.getDsLocalInfracao().length() > 100 ? 
								objeto.getDsLocalInfracao().toUpperCase().substring(0, 99) : 
								objeto.getDsLocalInfracao().toUpperCase());
			pstmt.setLong(13, new BigDecimal(objeto.getNrRenavan()).longValue());
			pstmt.setString(14,objeto.getDsAnoFabricacao());
			pstmt.setString(15,objeto.getDsAnoModelo());
			if(objeto.getNmProprietario() == null)
				pstmt.setNull(16, Types.VARCHAR);
			else
				pstmt.setString(16,objeto.getNmProprietario().toUpperCase());
			pstmt.setInt(17,objeto.getTpDocumento());
			pstmt.setString(18,objeto.getNrDocumento());			
			
			if(lgGctPadrao){
				String nrAit = objeto.getIdAit()!=null ? objeto.getIdAit() : String.valueOf(objeto.getNrAit());
				pstmt.setString(19, nrAit);
			}
			else{
				pstmt.setInt(19,objeto.getNrAit());
			}			
			
			pstmt.setString(20,objeto.getNmCondutor() != null ? objeto.getNmCondutor().toUpperCase() : null);
			pstmt.setString(21,objeto.getNrCnhCondutor());
			pstmt.setString(22,objeto.getUfCnhCondutor());
			pstmt.setFloat(23,objeto.getVlVelocidadePermitida());
			pstmt.setFloat(24,objeto.getVlVelocidadeAferida());
			pstmt.setFloat(25,objeto.getVlVelocidadePenalidade());
			pstmt.setString(26,objeto.getNrPlaca());
			pstmt.setString(27, objeto.getDsPontoReferencia() == null ? "" : Util.retirarAcentos(objeto.getDsPontoReferencia().toUpperCase()));
			pstmt.setInt(28,objeto.getLgAutoAssinado());
			pstmt.setFloat(29,objeto.getVlLongitude());
			pstmt.setFloat(30,objeto.getVlLatitude());
			if(objeto.getCdCidade()<=0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdCidade());
			
			if(objeto.getTpCnhCondutor()==-1)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getTpCnhCondutor());
			
			pstmt.setTimestamp(33, new Timestamp(System.currentTimeMillis()));
			
			if(objeto.getCdEquipamento()<=0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdEquipamento());
			

			pstmt.setInt(35,objeto.getTpConvenio());
			
			/**
			 * CAMPOS QUE GRAVAM O AIT EXATAMENTE COMO NO MOMENTO DA AUTUACAO
			 */
			//CD_MARCA_AUTUACAO
			if(objeto.getCdMarca()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdMarca());

			//NM_CONDUTOR_AUTUACAO - VARCHAR(100)
			objeto.setNmCondutor(objeto.getNmCondutor() != null ? Util.retirarAcentos(objeto.getNmCondutor()) : null);
			if(objeto.getNmCondutor() == null)
				pstmt.setNull(37, Types.VARCHAR);
			else
				pstmt.setString(37,objeto.getNmCondutor().toUpperCase());
			
			//NM_PROPRIETARIO_AUTUACAO - VARCHAR(100)
			objeto.setNmProprietario(objeto.getNmProprietario() != null ? Util.retirarAcentos(objeto.getNmProprietario()) : null);
			if(objeto.getNmProprietario() == null)
				pstmt.setNull(38, Types.VARCHAR);
			else
				pstmt.setString(38,objeto.getNmProprietario().toUpperCase());
			
			//NR_CNH_AUTUACAO - VARCHAR(20)
			pstmt.setString(39,objeto.getNrCnhCondutor());
			
			//UF_CNH_AUTUACAO - VAARCHAR(2)
			pstmt.setString(40,objeto.getUfCnhCondutor());
			
			//NR_DOCUMENTO_AUTUACAO - VARCHAR(30)
			pstmt.setString(41,objeto.getNrDocumento());

			
			//VALOR DA INFRACAO NO MOMENTO DA AUTUACAO
			//Infracao infracao = InfracaoDAO.get(objeto.getCdInfracao(), connect);
			Infracao infracao = InfracaoServices.getVigente(objeto.getCdInfracao(), connect);
			if(infracao != null && infracao.getVlInfracao() > 0) {
                pstmt.setFloat(42, infracao.getVlInfracao());
            } else {
                if(isAitPendenteOuCancelado(objeto)) {
                    pstmt.setNull(42, Types.FLOAT);
                } else {
                    throw new Exception("O valor da multa para uma infração válida é obrigatório.");
                }
            }
				
			//System.out.println("> AIT "+objeto.getNrAit()+"\tInfração: "+infracao.getNrCodDetran()+", R$ "+Util.formatCurrency((double) infracao.getVlInfracao()));		
			//ST_AIT
			pstmt.setInt(43, objeto.getStAit());
			
			//LG_ENVIADO_DETRAN
			pstmt.setInt(44, 0);
			
			//LG_ENVIADO_DETRAN
			pstmt.setInt(45, objeto.getCdTalao());
			
			pstmt.setString(46, objeto.getNrCpfCnpjProprietario());
			
			pstmt.setString(47, objeto.getNrCpfCondutor());
			
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(48, Types.INTEGER);
			else
				pstmt.setInt(48,objeto.getCdOcorrencia());
			
			pstmt.executeUpdate();
			
			//INSERIR IMAGENS
			if(objeto.getImagens()!=null && objeto.getImagens().size()>0) {
				
				for (AitImagem aitImg : objeto.getImagens()) {
					System.out.println("Comprimindo imagem...");
					aitImg.setBlbImagem(ImagemServices.compress(aitImg.getBlbImagem()));
				}
				
				code = AitImagemServices.save(objeto.getCdAit(), objeto.getImagens(), connect).getCode();
			}
			
			if(code<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveRadarAit(com.tivic.manager.str.Ait ait, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result;
			com.tivic.manager.mob.Agente agente;
			
			if(Util.isStrBaseAntiga())
				agente = AgenteServices.getBaseAntiga(ait.getCdAgente(), connect);
			else 
				agente = AgenteDAO.get(ait.getCdAgente(), connect);
			
			if(agente == null)
				return new Result(-1, "O agente informado não existe");
			
			Talonario talao = TalonarioServices.toStrTalonario(TalonarioServices.getTalaoMonitoramento(agente, connect));
			
			ait.setNrAit(talao.getNrUltimoAit() + 1);
			ait.setStAit(StConsistenciaAitEnum.ST_CONSISTENTE.getKey());
			result = com.tivic.manager.str.AitServices.sync(ait, connect);

			if(result.getCode() > 0){
				talao.setNrUltimoAit(ait.getNrAit());
				int update = TalonarioDAO.update(talao, connect);
				if(update > 0)
					connect.commit();
			}
			
			
			return result;			
		} catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static Result validarSync(ArrayList<String> aits) {
		return validarSync(aits, null);
	}
	
	public static Result validarSync(ArrayList<String> aits, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {

			boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
			if(str2mob) {
				return AitSyncServices.validarSync(aits);
			}
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
			
			ArrayList<String> aitsNaoEncontradas = new ArrayList<String>();
			
			for (String nrAit : aits) {
				
				ResultSet rs = connect.prepareStatement(
						lgBaseAntiga ? 
							"SELECT * FROM AIT WHERE NR_AIT = " + (lgGctPadrao? "'" + nrAit + "'" : nrAit ) : 
							"SELECT * FROM str_ait WHERE nr_ait = " + nrAit
					).executeQuery();
					
				if(!rs.next()) {
					//aitsNaoEncontradas.add(nrAit);
				}
			}
			
			Result r = new Result(1);
			r.setCode(aitsNaoEncontradas.size()>0 ? -1 : 1);
			r.setMessage(aitsNaoEncontradas.size()>0 ? aitsNaoEncontradas.size()+" AIT(s) não foram validada(s)." : "");
			r.addObject("AITS", aitsNaoEncontradas);
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-2, "Erro ao validar AITs");
		}
	}
	
	public static boolean isAitPendenteOuCancelado(Ait ait) {
		return Arrays.asList(AitServices.ST_PENDENTE_CONFIRMACAO, AitServices.ST_CANCELADO).contains(ait.getStAit());
	}
	
	public static void decidirConsistencia(Ait ait) throws ValidacaoException {
		if(ait.getStAit() != StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey()) {
			int orgaoOptanteConsistencia = ParametroServices.getValorOfParametroAsInteger("LG_OBRIGAR_CONSISTENCIA_AIT", -1);
			if(orgaoOptanteConsistencia == -1) {
				throw new ValidacaoException("O parâmetro LG_OBRIGAR_CONSISTENCIA_AIT não foi configurado.");
			}
			ait.setStAit(orgaoOptanteConsistencia != 1 
				    ? StConsistenciaAitEnum.ST_CONSISTENTE.getKey() 
				    : StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey());
		}
	}
	
	public static ResultSetMap getAitsByNrPlacaRenavan(String nrPlaca, int nrRenavan){
		return getAitsByNrPlacaRenavan(nrPlaca, nrRenavan, null);
	}

	public static ResultSetMap getAitsByNrPlacaRenavan(String nrPlaca, int nrRenavan, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			if(nrPlaca!=null && !nrPlaca.equals(""))
				criterios.add(new ItemComparator("NR_PLACA", nrPlaca, ItemComparator.LIKE, Types.VARCHAR));
			
			if(nrRenavan!=0)
				criterios.add(new ItemComparator("CD_RENAVAN", String.valueOf(nrRenavan), ItemComparator.EQUAL, Types.INTEGER));
			
			
			ResultSetMap rsmAits = find(criterios, connect);
			
			return rsmAits;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getAitsByNrPlacaRenavan: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca o AIT a partir do número.
	 * 
	 * @param nrAit
	 * @return {@link Ait}
	 * 
	 * @author mauriciocordeiro
	 * @since 2019-09-16
	 * 
	 * @disclaimer retorna apenas o cd_ait
	 */
	public static Ait get(String nrAit){
		return get(nrAit, null);
	}
	/**
	 * Busca o AIT a partir do número.
	 * 
	 * @param nrAit
	 * @param connect {@link Connection}
	 * @return {@link Ait}
	 * 
	 * @author mauriciocordeiro
	 * @since 2019-09-16
	 * 
	 * @disclaimer retorna apenas o cd_ait
	 */
	public static Ait get(String nrAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("NR_AIT", nrAit, ItemComparator.LIKE, Types.VARCHAR));
			
			ResultSetMap rsm = find(criterios, connect);
			
			if(rsm.next()) {
				Ait ait = new Ait();
				ait.setCdAit(rsm.getInt("codigo_ait"));
				
				return ait;
				
			} else {
				return null;
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.get: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static ResultSetMap getAllMovimentosByCdAit(int cdAit){
		return getMovimentosByCdAit(cdAit, null);
	}

	public static ResultSetMap getAllMovimentosByCdAit(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM AIT_MOVIMENTO where codigo_ait = " + cdAit+""
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getMovimentosByCdAit: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getMovimentosByCdAit(int cdAit){
		return getMovimentosByCdAit(cdAit, null);
	}

	public static ResultSetMap getMovimentosByCdAit(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM AIT_MOVIMENTO where codigo_ait = " + cdAit+" AND tp_status in ( 7, 8, 9, 10, 11, 12, 51, 52, 53  )"
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getMovimentosByCdAit: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		
		String sql = "SELECT * "
				+ "FROM AIT A "
				+ "LEFT OUTER JOIN INFRACAO B ON (A.cod_infracao = B.cod_infracao) "
				+ "LEFT OUTER JOIN MARCA_MODELO C ON (A.cod_marca = C.cod_marca) "
				+ "LEFT OUTER JOIN COR D ON (A.cod_cor = D.cod_cor) ";
		
		return Search.find(lgBaseAntiga ? sql : "SELECT * FROM str_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Registra a imagem gerada via monitoramento no AIT (str_ait_imagem)
	 * @param cdAit código da AIT
	 * @param cdEvento código do evento (contém a imagem)
	 * @return {@link Result}
	 * 
	 * @see #salvarAitImagem(int, int, Connection)
	 * 
	 * @category STR
	 * 
	 * @author Mauricio
	 * @since 13/08/2018
	 */
	public static Result salvarAitImagem(int cdAit, byte[] blbImagem) {
		return salvarAitImagem(cdAit, blbImagem, null);
	}
	
	/**
	 * Registra a imagem gerada via monitoramento no AIT (str_ait_imagem)
	 * @param cdAit cÃ³digo da AIT
	 * @param blbImagem Imagem de um Evento
	 * @param connect
	 * @return {@link Result}
	 * 
	 * @category STR
	 * 
	 * @author Mauricio
	 * @since 13/08/2018
	 */
	public static Result salvarAitImagem(int cdAit, byte[] blbImagem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			AitImagem aitImagem = new AitImagem(0, blbImagem, 0, cdAit);
			ArrayList<AitImagem> imagens = new ArrayList<>();
			imagens.add(aitImagem);
			
			if(AitImagemServices.save(cdAit, imagens, connect).getCode()<0) {
				if(isConnectionNull)
					connect.rollback();
				return new Result(-2, "Erro ao gravar AitImagem.");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.salvarAitImagem: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Sincronia do Radar Server com o banco local do municipio
	 * @return
	 */
	public static Result syncRadarServer() {
		return syncRadarServer(null, null, null);
	}
	
	public static Result syncRadarServer(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return syncRadarServer(dtInicial, dtFinal, null);
	}

	/**
	 * Sincronia do Radar Server com o banco local do municipio
	 * @return
	 */
	public static Result syncRadarServer(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		ManagerLog managerLog = new ManagerLog();
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			
			boolean lgBaseAntiga =  Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
						
			String driver = Util.getConfManager().getProps().getProperty("RADAR_DRIVER");
			String url = Util.getConfManager().getProps().getProperty("RADAR_DBPATH");
			String login = Util.getConfManager().getProps().getProperty("RADAR_LOGIN");
			String senha = Util.getConfManager().getProps().getProperty("RADAR_PASS");
			
			String nmOrgaoAutuador = Util.getConfManager().getProps().getProperty("RADAR_ORGAO_AUTUADOR");
			
			if(driver==null || url==null || login==null || senha==null || nmOrgaoAutuador==null) {
				LogUtils.debug("Impossível sincronizar AITs com o servidor do radar. Valores de configurações inexistentes.");
				return new Result(-2, "Impossível sincronizar AITs com o servidor do radar. Valores de configurações inexistentes.");
			}
			
			
			Class.forName(driver).newInstance();
	  		DriverManager.setLoginTimeout(Conexao.CONNECTION_TIMEOUT);
	  		Connection connectRS = DriverManager.getConnection(url, login, senha);
	  		
			String sql = "SELECT A.*, B.*, B2.id_tipo_evento, C.nr_cod_detran, D.nr_matricula, E.nm_marca, E.nm_modelo, F.nm_cidade, G.nm_tipo_veiculo, H.ds_especie, I.nm_cor "
					+ " FROM mob_ait A " 
					+ " JOIN mob_ait_evento B1 ON (A.cd_ait = B1.cd_ait) "
					+ " JOIN mob_evento_equipamento B ON (B1.cd_evento = B.cd_evento) "	 
					+ " JOIN mob_tipo_evento B2 ON (B2.cd_tipo_evento = B.cd_tipo_evento) "	 
					+ " JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao) "
					+ " JOIN mob_agente D ON (A.cd_agente = D.cd_agente) "
					+ " JOIN fta_marca_modelo E ON (A.cd_marca_veiculo = E.cd_marca) "
					+ " LEFT OUTER JOIN grl_cidade F ON (A.cd_cidade = F.cd_cidade) "
					+ " LEFT OUTER JOIN fta_tipo_veiculo G ON (A.cd_tipo_veiculo = G.cd_tipo_veiculo) " 
					+ " LEFT OUTER JOIN fta_especie_veiculo H ON (A.cd_especie_veiculo = H.cd_especie) " 
					+ " LEFT OUTER JOIN fta_cor I ON (A.cd_cor_veiculo = I.cd_cor) " 
					+ " where B.nm_orgao_autuador = ? "
					+ "   and B.st_evento = " + EventoEquipamentoServices.ST_EVENTO_CONFIRMADO
					+ "   and A.dt_sincronizacao is null "
					+ "   and (A.nr_tentativa_sincronizacao <= 3 or A.nr_tentativa_sincronizacao is null) ";
			

			if(dtInicial!=null && dtFinal!=null) {
				sql+= " and A.dt_infracao >= ? and A.dt_infracao <= ?";
			}
			
			sql+=" order by A.nr_ait";
						
			PreparedStatement pstmtRS = connectRS.prepareStatement(sql);
						
			pstmtRS.setString(1, nmOrgaoAutuador);
			
			if(dtInicial!=null && dtFinal!=null) {
				pstmtRS.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
				pstmtRS.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			}
			ResultSetMap rsmAitsRS = new ResultSetMap(pstmtRS.executeQuery());
			
			if(rsmAitsRS.size()==0) {
				LogUtils.debug("Não foi necessário sincronizar AITs com o servidor do radar. Nenhum encontrado.");
				return new Result(-3, "Não foi necessário sincronizar AITs com o servidor do radar. Nenhum encontrado.");
			}
			 
			Result r = null;
			
			int qtAitsInseridas = 0;
			if(rsmAitsRS.size()>0) {
				LogUtils.debug("Sincronizando "+rsmAitsRS.size()+" AIT(s) encontrados no servidor do radar.");
				
				while(rsmAitsRS.next()) {
					
					try {
					
						if (isConnectionNull) {
							connect = Conexao.conectar();
							connect.setAutoCommit(false);
						}
						
						managerLog.info("Registro", (rsmAitsRS.getPosition()+1)+ "/"+rsmAitsRS.size()+" [AIT: "+rsmAitsRS.getString("NR_AIT")+"]");
						
						ResultSet rs1 = connect.prepareStatement(
								lgBaseAntiga ? 
									"SELECT * FROM AIT WHERE NR_AIT = " + (lgGctPadrao? "'" + rsmAitsRS.getString("NR_AIT") + "'" : rsmAitsRS.getString("NR_AIT") ) : 
									"SELECT * FROM str_ait WHERE nr_ait = " + rsmAitsRS.getString("NR_AIT")
							).executeQuery();
							
						if(rs1.next()) {
							LogUtils.debug("AitServices.syncRadarServer: AIT Duplicada... "+rsmAitsRS.getString("NR_AIT"));
							
							pstmtRS = connectRS.prepareStatement("UPDATE mob_ait SET nr_tentativa_sincronizacao=? WHERE cd_ait=?");
							pstmtRS.setInt(1, rsmAitsRS.getInt("NR_TENTATIVA_SINCRONIZACAO")+1);
							pstmtRS.setInt(2, rsmAitsRS.getInt("CD_AIT"));
							pstmtRS.executeUpdate();
							
							continue;
						}
						
						Equipamento equipamento = EquipamentoServices.getByIdEquipamento(rsmAitsRS.getString("NM_EQUIPAMENTO"), connect); 
						if(equipamento==null) {
							String msg = "Evento não contém um equipamento válido ou registrado no banco local:\n"
									+ "N. AIT.\t"+rsmAitsRS.getInt("NR_AIT")+"\n"
									+ "Placa\t"+rsmAitsRS.getString("NR_PLACA")+"\n"
									+ "Equip.\t"+rsmAitsRS.getString("NM_EQUIPAMENTO")+"\n"
									+ "Tp. Evt.\t"+rsmAitsRS.getString("ID_TIPO_EVENTO")+"\n"
									+ "Dt. Evt.\t"+Util.convCalendarString(rsmAitsRS.getGregorianCalendar("DT_EVENTO"));
							LogUtils.debug(msg);
							Conexao.rollback(connect);
							return new Result(-3, msg);
						}
						
						GregorianCalendar dtAfericaoMinutoMais = rsmAitsRS.getGregorianCalendar("dt_conclusao");
						dtAfericaoMinutoMais.add(Calendar.MINUTE, 1);
						GregorianCalendar dtAfericaoMinutoMenos = rsmAitsRS.getGregorianCalendar("dt_conclusao");
						dtAfericaoMinutoMenos.add(Calendar.MINUTE, -1);
						ResultSet rs2 = connect.prepareStatement(
								"SELECT * FROM mob_evento_equipamento "
							  + "	WHERE nr_placa = '"+rsmAitsRS.getString("NR_PLACA")+"'"
							  + "     AND cd_equipamento = " + equipamento.getCdEquipamento() 
							  + "     AND dt_conclusao <= '" + Util.formatDate(dtAfericaoMinutoMais, "yyyy-MM-dd HH:mm:ss") + "'"
							  + "     AND dt_conclusao >= '" + Util.formatDate(dtAfericaoMinutoMenos, "yyyy-MM-dd HH:mm:ss") + "'").executeQuery();
	
						if(rs2.next()) {
							LogUtils.debug("AitServices.syncRadarServer: Evento possivelmente duplicado... "+rsmAitsRS.getString("CD_EVENTO"));
							continue;
						}
						
						Infracao infracao = InfracaoServices.getVigenteByNrCodDetran(rsmAitsRS.getInt("NR_COD_DETRAN"), connect);
						if(infracao==null) {
							String msg = "AIT não contém uma infração válida ou registrada no banco local:\n"
									+ "N. AIT.\t"+rsmAitsRS.getInt("NR_AIT")+"\n"
									+ "Placa\t"+rsmAitsRS.getString("NR_PLACA")+"\n"
									+ "Equip.\t"+rsmAitsRS.getString("NM_EQUIPAMENTO")+"\n"
									+ "Tp. Evt.\t"+rsmAitsRS.getString("ID_TIPO_EVENTO")+"\n"
									+ "Infração\t"+rsmAitsRS.getString("NR_COD_DETRAN")+"\n"
									+ "Dt. Infração\t"+Util.convCalendarString(rsmAitsRS.getGregorianCalendar("DT_INFRACAO"));
							LogUtils.debug(msg);
							Conexao.rollback(connect);
							return new Result(-3, msg);
						}
						
						Agente agente = com.tivic.manager.str.AgenteServices.getByNrMatricula(rsmAitsRS.getString("NR_MATRICULA"), connect);
						if(agente==null) {
							String msg = "AIT não contém um agente válido ou registrado no banco local:\n"
									+ "N. AIT.\t"+rsmAitsRS.getInt("NR_AIT")+"\n"
									+ "Placa\t"+rsmAitsRS.getString("NR_PLACA")+"\n"
									+ "Equip.\t"+rsmAitsRS.getString("NM_EQUIPAMENTO")+"\n"
									+ "Tp. Evt.\t"+rsmAitsRS.getString("ID_TIPO_EVENTO")+"\n"
									+ "Agente\t"+rsmAitsRS.getString("NR_MATRICULA")+"\n"
									+ "Infração\t"+rsmAitsRS.getString("NR_COD_DETRAN")+"\n"
									+ "Dt. Infração\t"+Util.convCalendarString(rsmAitsRS.getGregorianCalendar("DT_INFRACAO"));
							LogUtils.debug(msg);
							Conexao.rollback(connect);
							return new Result(-4, msg);
						}
						
						MarcaModelo marcaVeiculo = MarcaModeloServices.getByMarcaModelo(rsmAitsRS.getString("NM_MARCA"), rsmAitsRS.getString("NM_MODELO"), connect);
						if(marcaVeiculo==null) {
							String msg = "AIT não contém uma marca válida ou registrada no banco local:\n"
									+ "N. AIT.\t"+rsmAitsRS.getInt("NR_AIT")+"\n"
									+ "Placa\t"+rsmAitsRS.getString("NR_PLACA")+"\n"
									+ "Equip.\t"+rsmAitsRS.getString("NM_EQUIPAMENTO")+"\n"
									+ "Tp. Evt.\t"+rsmAitsRS.getString("ID_TIPO_EVENTO")+"\n"
									+ "Agente\t"+rsmAitsRS.getString("NR_MATRICULA")+"\n"
									+ "Infração\t"+rsmAitsRS.getString("NR_COD_DETRAN")+"\n"
									+ "Dt. Infração\t"+Util.convCalendarString(rsmAitsRS.getGregorianCalendar("DT_INFRACAO"))+"\n"
									+ "Marca\t'"+rsmAitsRS.getString("NM_MARCA")+"'\n"
									+ "Modelo\t'"+rsmAitsRS.getString("NM_MODELO")+"'";
							LogUtils.debug(msg);
							continue;
						}
						
						CategoriaVeiculo categoria = CategoriaVeiculoServices.getByNmCategoria(rsmAitsRS.getString("NM_CATEGORIA"), connect);
						Usuario usuario = UsuarioServices.getByNmLogin("RADAR_SYNC", connect);
						EspecieVeiculo especieVeiculo = EspecieVeiculoServices.getByDsEspecie(rsmAitsRS.getString("DS_ESPECIE"), connect);
						Cor corVeiculo = CorServices.getByNmCor(rsmAitsRS.getString("NM_COR"), connect);
						TipoVeiculo tipoVeiculo = TipoVeiculoServices.getByNmTipo(rsmAitsRS.getString("NM_TIPO_VEICULO"), connect);
						Cidade cidade = CidadeServices.getByNmCidade(rsmAitsRS.getString("NM_CIDADE"), connect);
						
						TipoEvento tipoEvento =  TipoEventoServices.getByIdTipoEvento(rsmAitsRS.getString("ID_TIPO_EVENTO"), connect);
						if(tipoEvento==null) {
							String msg = "Evento não contém um tipo de evento válido ou registrado no banco local:\n"
									+ "N. AIT.\t"+rsmAitsRS.getInt("NR_AIT")+"\n"
									+ "Placa\t"+rsmAitsRS.getString("NR_PLACA")+"\n"
									+ "Equip.\t"+rsmAitsRS.getString("NM_EQUIPAMENTO")+"\n"
									+ "Tp. Evt.\t"+rsmAitsRS.getString("ID_TIPO_EVENTO")+"\n"
									+ "Dt. Evt.\t"+Util.convCalendarString(rsmAitsRS.getGregorianCalendar("DT_EVENTO"));
							LogUtils.debug(msg);
							Conexao.rollback(connect);
							return new Result(-3, msg);
						}
						
						
						EventoEquipamento evento = new EventoEquipamento();
						evento.setCdEvento(0);
						evento.setCdEquipamento(equipamento.getCdEquipamento());
						evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());
						evento.setDtEvento(rsmAitsRS.getGregorianCalendar("DT_EVENTO"));
						evento.setNmOrgaoAutuador(rsmAitsRS.getString("NM_ORGAO_AUTUADOR"));
						evento.setNmEquipamento(rsmAitsRS.getString("NM_EQUIPAMENTO"));
						evento.setDsLocal(rsmAitsRS.getString("DS_LOCAL"));
						evento.setIdIdentificacaoInmetro(rsmAitsRS.getString("ID_IDENTIFICACAO_INMETRO"));
						evento.setDtAfericao(rsmAitsRS.getGregorianCalendar("DT_AFERICAO"));
						evento.setNrPista(rsmAitsRS.getInt("NR_PISTA"));
						evento.setDtConclusao(rsmAitsRS.getGregorianCalendar("DT_CONCLUSAO"));
						evento.setVlLimite(rsmAitsRS.getInt("VL_LIMITE"));
						evento.setVlVelocidadeTolerada(rsmAitsRS.getInt("VL_VELOCIDADE_TOLERADA"));
						evento.setVlMedida(rsmAitsRS.getInt("VL_MEDIDA"));
						evento.setVlConsiderada(rsmAitsRS.getInt("VL_CONSIDERADA"));
						evento.setNrPlaca(rsmAitsRS.getString("NR_PLACA"));
						evento.setLgTempoReal(rsmAitsRS.getInt("LG_TEMPO_REAL"));
						evento.setTpVeiculo(rsmAitsRS.getInt("TP_VEICULO"));
						evento.setVlComprimentoVeiculo(rsmAitsRS.getInt("VL_COMPRIMENTO_VEICULO"));
						evento.setIdMedida(rsmAitsRS.getInt("ID_MEDIDA"));
						evento.setNrSerial(rsmAitsRS.getString("NR_SERIAL"));
						evento.setNmModeloEquipamento(rsmAitsRS.getString("NM_MODELO_EQUIPAMENTO"));
						evento.setNmRodovia(rsmAitsRS.getString("NM_RODOVIA"));
						evento.setNmUfRodovia(rsmAitsRS.getString("NM_UF_RODOVIA"));
						evento.setNmKmRodovia(rsmAitsRS.getString("NM_KM_RODOVIA"));
						evento.setNmMetrosRodovia(rsmAitsRS.getString("NM_METROS_RODOVIA"));
						evento.setNmSentidoRodovia(rsmAitsRS.getString("NM_SENTIDO_RODOVIA"));
						evento.setIdCidade(rsmAitsRS.getInt("ID_CIDADE"));
						evento.setNmMarcaEquipamento(rsmAitsRS.getString("NM_MARCA_EQUIPAMENTO"));
						evento.setTpEquipamento(rsmAitsRS.getInt("TP_EQUIPAMENTO"));
						evento.setNrPista(rsmAitsRS.getInt("NR_PISTA"));
						evento.setDtExecucaoJob(rsmAitsRS.getGregorianCalendar("DT_EXECUCAO_JOB"));
						evento.setIdUuid(rsmAitsRS.getString("ID_UUID"));
						evento.setTpRestricao(rsmAitsRS.getInt("TP_RESTRICAO"));
						evento.setTpClassificacao(rsmAitsRS.getInt("TP_CLASSIFICACAO"));
						evento.setVlPermanencia(rsmAitsRS.getInt("VL_PERMANENCIA"));
						evento.setVlSemaforoVermelho(rsmAitsRS.getInt("VL_SEMAFORO_VERMELHO"));
						evento.setStEvento(rsmAitsRS.getInt("ST_EVENTO"));
						evento.setCdMotivoCancelamento(rsmAitsRS.getInt("CD_MOTIVO_CANCELAMENTO"));
						evento.setTxtEvento(rsmAitsRS.getString("TXT_EVENTO"));
						r = EventoEquipamentoServices.save(evento, null, connect);
						
						if(r.getCode()>0) {
							
							evento.setCdEvento( ((EventoEquipamento)r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());
							
							pstmtRS = connectRS.prepareStatement("SELECT * FROM mob_evento_arquivo A, grl_arquivo B "
									+ " WHERE A.cd_arquivo = B.cd_arquivo "
									+ " AND A.cd_evento = ? "
									+ " AND A.tp_arquivo = " + EventoArquivoServices.TP_ARQUIVO_FOTO);
							pstmtRS.setInt(1, rsmAitsRS.getInt("CD_EVENTO"));
							
							ResultSet rsEventoArquivosRS = pstmtRS.executeQuery();
							
							while(rsEventoArquivosRS.next()) {
								Arquivo arquivo = new Arquivo();
								arquivo.setCdArquivo(0);
								arquivo.setNmArquivo(rsEventoArquivosRS.getString("NM_ARQUIVO"));
								arquivo.setBlbArquivo(rsEventoArquivosRS.getBytes("BLB_ARQUIVO"));
								arquivo.setDtArquivamento(Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_ARQUIVAMENTO")));
								arquivo.setDtCriacao(Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_CRIACAO")));
								r = ArquivoServices.save(arquivo, null, connect);
								
								if(r.getCode() > 0) {
									arquivo.setCdArquivo( ((Arquivo)r.getObjects().get("ARQUIVO")).getCdArquivo() );
									
									EventoArquivo eventoArquivo = new EventoArquivo();
									
									eventoArquivo.setCdEvento(evento.getCdEvento());
									eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
									eventoArquivo.setTpArquivo(rsEventoArquivosRS.getInt("TP_ARQUIVO"));
									eventoArquivo.setIdArquivo(rsEventoArquivosRS.getString("ID_ARQUIVO"));
									eventoArquivo.setTpEventoFoto(rsEventoArquivosRS.getInt("TP_EVENTO_FOTO"));
									eventoArquivo.setTpFoto(rsEventoArquivosRS.getInt("TP_FOTO"));
									eventoArquivo.setDtArquivo(Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_ARQUIVO")));
									r = EventoArquivoServices.save(eventoArquivo, null, connect); 
									
									if(r.getCode() <= 0)
										break;
								}
								else 
									break;
							}
						}
	
						if(r.getCode()>0) {
							
							int code = Conexao.getSequenceCode("ait", connect);
							if (code <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao receber código de ait");
							}
							
							pstmt = connect.prepareStatement("INSERT INTO AIT (codigo_ait,"+
				                    "cod_infracao,"+
				                    "cod_agente,"+
				                    "cod_usuario,"+
				                    "cod_especie,"+
				                    "cod_marca,"+
				                    "cod_cor,"+
				                    "cod_tipo,"+
				                    "dt_infracao,"+
				                    "cod_categoria,"+
				                    "ds_observacao,"+
				                    "ds_local_infracao,"+
				                    "cd_renavan,"+
				                    "ds_ano_fabricacao,"+
				                    "ds_ano_modelo,"+
				                    "nm_proprietario,"+
				                    "tp_documento,"+
				                    "nr_documento,"+
				                    "nr_ait,"+
				                    "nm_condutor,"+
				                    "nr_cnh_condutor,"+
				                    "uf_cnh_condutor,"+
				                    "vl_velocidade_permitida,"+
				                    "vl_velocidade_aferida,"+
				                    "vl_velocidade_penalidade,"+
				                    "nr_placa,"+
				                    "ds_ponto_referencia,"+
				                    "lg_auto_assinado,"+
				                    "vl_longitude,"+
				                    "vl_latitude,"+
				                    "cod_municipio,"+
				                    "tp_cnh_condutor,"+
				                    "dt_digitacao,"+
				                    "cd_equipamento,"+
				                    "tp_convenio,"+
				                    "COD_MARCA_AUTUACAO,"+
				                    "NM_CONDUTOR_AUTUACAO,"+
				                    "NM_PROPRIETARIO_AUTUACAO,"+
				                    "NR_CNH_AUTUACAO,"+
				                    "UF_CNH_AUTUACAO,"+
				                    "NR_DOCUMENTO_AUTUACAO,"+
				                    "VL_MULTA,"+
				                    "LG_ENVIADO_DETRAN,"+
				                    "ST_AIT, "+
				                    "DT_SINCRONIZACAO, "+
				                    "CD_EVENTO_EQUIPAMENTO," +
				                    "DT_AFERICAO, "+
				                    "NR_LACRE, "+
				                    "NR_INVENTARIO_INMETRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
							
							pstmt.setInt(1, code);
							pstmt.setInt(2, infracao.getCdInfracao());
							pstmt.setInt(3, agente.getCdAgente());
							if(usuario==null || usuario.getCdUsuario()==0)
								pstmt.setNull(4, Types.INTEGER);
							else
								pstmt.setInt(4, usuario.getCdUsuario()); 
							if(especieVeiculo==null || especieVeiculo.getCdEspecie()==0)
								pstmt.setNull(5, Types.INTEGER);
							else
								pstmt.setInt(5, especieVeiculo.getCdEspecie());
							pstmt.setInt(6, marcaVeiculo.getCdMarca());
							if(corVeiculo==null || corVeiculo.getCdCor()==0)
								pstmt.setNull(7, Types.INTEGER);
							else
								pstmt.setInt(7, corVeiculo.getCdCor());
							if(tipoVeiculo==null || tipoVeiculo.getCdTipo()<=0)
								pstmt.setNull(8, Types.INTEGER);
							else
								pstmt.setInt(8, tipoVeiculo.getCdTipo());
							pstmt.setTimestamp(9, rsmAitsRS.getTimestamp("DT_INFRACAO"));
							if(categoria==null || categoria.getCdCategoria()==0)
								pstmt.setNull(10, Types.INTEGER);
							else
								pstmt.setInt(10, categoria.getCdCategoria());
							pstmt.setString(11, rsmAitsRS.getString("DS_OBSERVACAO") == null ? "" : rsmAitsRS.getString("DS_OBSERVACAO").toUpperCase());
	
							String dsLocalInfracao = rsmAitsRS.getString("DS_LOCAL_INFRACAO") == null ? "" : rsmAitsRS.getString("DS_LOCAL_INFRACAO");
							pstmt.setString(12, dsLocalInfracao.length() > 100 ? 
												dsLocalInfracao.toUpperCase().substring(0, 99) : 
												dsLocalInfracao.toUpperCase());
							if(rsmAitsRS.getString("NR_RENAVAN")==null || rsmAitsRS.getString("NR_RENAVAN").equals(""))
								pstmt.setNull(13, Types.INTEGER);
							else	
								pstmt.setInt(13, new Integer(rsmAitsRS.getString("NR_RENAVAN")));
							pstmt.setString(14, rsmAitsRS.getString("NR_ANO_FABRICACAO"));
							pstmt.setString(15, rsmAitsRS.getString("NR_ANO_MODELO"));
							pstmt.setString(16, rsmAitsRS.getString("NM_PROPRIETARIO") == null ? "" : rsmAitsRS.getString("NM_PROPRIETARIO").toUpperCase());
							pstmt.setInt(17, rsmAitsRS.getInt("TP_DOCUMENTO"));
							pstmt.setString(18, rsmAitsRS.getString("NR_DOCUMENTO"));			
							
							if(lgGctPadrao)
								pstmt.setString(19, rsmAitsRS.getString("NR_AIT"));
							else
								pstmt.setInt(19, rsmAitsRS.getInt("NR_AIT"));		
							
							pstmt.setString(20, rsmAitsRS.getString("NM_CONDUTOR") == null ? "" : rsmAitsRS.getString("NM_CONDUTOR").toUpperCase());
							pstmt.setString(21, rsmAitsRS.getString("NR_CNH_CONDUTOR"));
							pstmt.setString(22, rsmAitsRS.getString("UF_CNH_CONDUTOR"));
							
							if (tipoEvento.getIdTipoEvento().equals(IdTipoEventoEnum.VAL.getKey())) {
							    pstmt.setFloat(23, rsmAitsRS.getFloat("VL_VELOCIDADE_PERMITIDA"));
							    pstmt.setFloat(24, rsmAitsRS.getFloat("VL_VELOCIDADE_AFERIDA"));
							    pstmt.setFloat(25, rsmAitsRS.getFloat("VL_VELOCIDADE_PENALIDADE"));	
							} else {
							    pstmt.setFloat(23, 0.0f);
							    pstmt.setFloat(24, 0.0f);
							    pstmt.setFloat(25, 0.0f);
							}
							
							pstmt.setString(26, rsmAitsRS.getString("NR_PLACA"));
							pstmt.setString(27, rsmAitsRS.getString("DS_PONTO_REFERENCIA") == null ? "" : rsmAitsRS.getString("DS_PONTO_REFERENCIA").toUpperCase());
							pstmt.setInt(28, rsmAitsRS.getInt("LG_AUTO_ASSINADO"));
							pstmt.setFloat(29, rsmAitsRS.getFloat("VL_LONGITUDE"));
							pstmt.setFloat(30, rsmAitsRS.getFloat("VL_LATITUDE"));
							
							if(cidade == null || cidade.getCdCidade()<=0)
								pstmt.setNull(31, Types.INTEGER);
							else
								pstmt.setInt(31, cidade.getCdCidade());
							
							if(rsmAitsRS.getInt("TP_CNH_CONDUTOR")==-1)
								pstmt.setNull(32, Types.INTEGER);
							else
								pstmt.setInt(32, rsmAitsRS.getInt("TP_CNH_CONDUTOR"));
							
							pstmt.setTimestamp(33, new Timestamp(System.currentTimeMillis()));
							pstmt.setInt(34, equipamento.getCdEquipamento());
							
							pstmt.setInt(35, rsmAitsRS.getInt("tp_convenio"));
							
							pstmt.setInt(36, marcaVeiculo.getCdMarca());
							pstmt.setString(37, rsmAitsRS.getString("NM_CONDUTOR") == null ? "" : rsmAitsRS.getString("NM_CONDUTOR").toUpperCase());
							pstmt.setString(38, rsmAitsRS.getString("NM_PROPRIETARIO") == null ? "" : rsmAitsRS.getString("NM_PROPRIETARIO").toUpperCase());
							pstmt.setString(39, rsmAitsRS.getString("NR_CNH_CONDUTOR"));
							pstmt.setString(40, rsmAitsRS.getString("UF_CNH_CONDUTOR"));
							pstmt.setString(41, rsmAitsRS.getString("NR_DOCUMENTO"));
							if(infracao==null || infracao.getVlInfracao()<0)
								pstmt.setNull(42, Types.FLOAT);
							else
								pstmt.setFloat(42, infracao.getVlInfracao());
							pstmt.setInt(43, 0); //lgEnviadoDetran
							
							pstmt.setInt(44, ST_PENDENTE_CONFIRMACAO);
							pstmt.setTimestamp(45, new Timestamp(System.currentTimeMillis())); //dtSincronizacao
							pstmt.setInt(46, evento.getCdEvento()); //cdEventoEquipamento
	
							if(rsmAitsRS.getTimestamp("DT_AFERICAO")==null)
								pstmt.setNull(47, Types.TIMESTAMP);
							else
								pstmt.setTimestamp(47, rsmAitsRS.getTimestamp("DT_AFERICAO"));
							pstmt.setString(48, rsmAitsRS.getString("NR_LACRE"));
							pstmt.setString(49, rsmAitsRS.getString("NR_INVENTARIO_INMETRO"));
							pstmt.executeUpdate();
							
							r = new Result(code);
							
							if(r.getCode()>0) {
								
								qtAitsInseridas++;
								
								pstmtRS = connectRS.prepareStatement("SELECT * FROM mob_ait_imagem "
										+ " WHERE cd_ait = ? ");
								pstmtRS.setInt(1, rsmAitsRS.getInt("CD_AIT"));
								
								ResultSet rsAitImagensRS = pstmtRS.executeQuery();
								
								while(rsAitImagensRS.next()) {
									
									AitImagem imagem = new AitImagem(0, //cdImagem, 
											rsAitImagensRS.getBytes("BLB_IMAGEM"),
											0,//blbImagem 
											code); //cdAit, 
									r = new Result(AitImagemDAO.insert(imagem, connect));
								}
							}
							
							if(r.getCode()>0) {
								pstmtRS = connectRS.prepareStatement("UPDATE mob_ait SET dt_sincronizacao=?,"+
							      		   "nr_tentativa_sincronizacao=? WHERE cd_ait=?");
								pstmtRS.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
								pstmtRS.setInt(2, rsmAitsRS.getInt("NR_TENTATIVA_SINCRONIZACAO")+1);
								pstmtRS.setInt(3, rsmAitsRS.getInt("CD_AIT"));
								pstmtRS.executeUpdate();
							}
	
							if (evento != null && evento.getCdEvento() > 0) {
								AitEvento aitEvento = new AitEvento(code, evento.getCdEvento());
								r = AitEventoServices.save(aitEvento, null, connect);
								if(r.getCode() <= 0) {
									break;
								}
							}
	
						}
						
						if(r!=null && r.getCode() <= 0)
							Conexao.rollback(connect);
						else if (isConnectionNull){
							String msg = "AIT inserido com sucesso:\n"
									+ "N. AIT.\t"+rsmAitsRS.getInt("NR_AIT")+"\n"
									+ "Placa\t"+rsmAitsRS.getString("NR_PLACA")+"\n"
									+ "Equip.\t"+rsmAitsRS.getString("NM_EQUIPAMENTO")+"\n"
									+ "Tp. Evt.\t"+rsmAitsRS.getString("ID_TIPO_EVENTO")+"\n"
									+ "Agente\t"+rsmAitsRS.getString("NR_MATRICULA")+"\n"
									+ "Infração\t"+rsmAitsRS.getString("NR_COD_DETRAN")+"\n"
									+ "Dt. Infração\t"+Util.convCalendarString(rsmAitsRS.getGregorianCalendar("DT_INFRACAO"))+"\n"
									+ "Marca\t'"+rsmAitsRS.getString("NM_MARCA")+"'\n"
									+ "Modelo\t'"+rsmAitsRS.getString("NM_MODELO")+"'";
							LogUtils.debug(msg);
							
							connect.commit();
						}
						
					} catch(Exception e) {
						Conexao.rollback(connect);
						e.printStackTrace(System.out);
						return null;
					} finally {
						if (isConnectionNull)
							Conexao.desconectar(connect);
					}
				}	
			}
				
			connectRS.close();
			managerLog.info("Resultado final", qtAitsInseridas + "/" + rsmAitsRS.size()+" ait(s) sincronizados com o servidor do radar.");
			return new Result(1, qtAitsInseridas + "/" + rsmAitsRS.size()+" ait(s) sincronizados com o servidor do radar.");
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.syncRadarServer: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.syncRadarServer: " + e);
			return null;
		}
	}
	
	public static Ait get(int cdAit, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			Ait ait = null;
			
			if(Util.isStrBaseAntiga()) {
				PreparedStatement ps = connection.prepareStatement("SELECT * FROM ait WHERE codigo_ait = ?");
				ps.setInt(1, cdAit);
				
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					ait = new Ait(rs.getInt("codigo_ait"),
							rs.getInt("cod_infracao"),
							rs.getInt("cod_agente"),
							rs.getInt("cod_usuario"),
							rs.getInt("cod_especie"),
							rs.getInt("cod_marca"),
							rs.getInt("cod_cor"),
							rs.getInt("cod_tipo"),
							(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
							rs.getInt("cod_categoria"),
							rs.getString("ds_observacao"),
							rs.getString("ds_local_infracao"),
							rs.getString("cd_renavan"),
							rs.getString("ds_ano_fabricacao"),
							rs.getString("ds_ano_modelo"),
							rs.getString("nm_proprietario"),
							rs.getInt("tp_documento"),
							rs.getString("nr_documento"),
							Integer.parseInt(rs.getString("nr_ait").replaceAll("[^\\d]", "")),
							rs.getString("nm_condutor"),
							rs.getString("nr_cnh_condutor"),
							rs.getString("uf_cnh_condutor"),
							rs.getFloat("vl_velocidade_permitida"),
							rs.getFloat("vl_velocidade_aferida"),
							rs.getFloat("vl_velocidade_penalidade"),
							rs.getString("nr_placa"),
							rs.getString("ds_ponto_referencia"),
							rs.getInt("lg_auto_assinado"),
							rs.getFloat("vl_longitude"),
							rs.getFloat("vl_latitude"),
							rs.getInt("cod_municipio"),
							rs.getInt("cd_equipamento"),
							rs.getInt("tp_cnh_condutor"),
							rs.getInt("tp_convenio"),
							rs.getString("nr_ait"),
							0,
							rs.getString("nr_cpf_cnpj_proprietario"),
							rs.getString("nr_cpf_condutor"),
							null);
				}
				
			} else {
				ait = AitDAO.get(cdAit, connection);
			}
				
			return ait;
			
		} catch(Exception e) {
			System.out.println("Erro! AitServices.get.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	protected static boolean hasAit(int cdAit, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
			}
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			if(Util.isStrBaseAntiga()) {
				crt.add(new ItemComparator("codigo_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			} else {
				crt.add(new ItemComparator("cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			}
			ResultSetMap rsm = com.tivic.manager.mob.AitServices.find(crt, connection);
			
			return (rsm.getLines().size() <= 0);
		} catch(Exception e) {
			System.out.println("Erro! AitServices.hasAit.");
			e.printStackTrace(System.out);
			
			return false;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static Result getFormularioAndamento(int cdAit, ResultSetMap rsm, ResultSetMap rsmProcessos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String imgLogo64 = null;
			String imgLogo264 = null;
			String dsTitulo1 = null;
			String dsTitulo2 = null;
			String dsTitulo3 = null;
			
			int cdMunicipioAutuador = 0;
			String cdOrgaoAutuante = null;
			String nmMunicipio = null;
			String cdMunicipio = null;
			String nmUF = null;
			String nmModelo = null;
			String cdRenavan = null;
			String dsEspecie = null;
			String nmCategoria = null;
			String dsLocal = null;
			String dsPontoReferencia = null;
			float vlMulta = 0;
			String nrCnhCondutor = null;
			String ufCnhCondutor = null;
			String cdInfracao = null;
			String dsInfracao = null;
			String ufVeiculo = null;
			
			String nrCpfCondutor = null;
			String nrCpfProprietario = null;
			String nmCondutor = null;
			String nmProprietario = null;
			
			if(rsm.next()) {
				nmMunicipio = rsm.getString("nm_municipio");
				nmUF = rsm.getString("nm_uf");
				ufVeiculo = rsm.getString("nm_uf");
				nmModelo = rsm.getString("nm_modelo");
				cdRenavan = rsm.getString("cd_renavan");
				dsEspecie = rsm.getString("ds_especie");
				nmCategoria = rsm.getString("nm_categoria");
				dsLocal = rsm.getString("ds_local_infracao");
				dsPontoReferencia = rsm.getString("ds_ponto_referencia");				
				vlMulta = rsm.getFloat("vl_multa");
				nrCnhCondutor = rsm.getString("nr_cnh_condutor");
				ufCnhCondutor = rsm.getString("uf_cnh_condutor");
				cdInfracao = rsm.getString("cod_infracao");
				dsInfracao = rsm.getString("ds_infracao2");
				cdMunicipio = rsm.getString("cod_municipio");
			}
			if(Util.isStrBaseAntiga()) {
				PreparedStatement ps = connect
						.prepareStatement("SELECT nm_orgao_autuador, nm_suborgao, nm_departamento, img_logo_orgao, img_logo_departamento, cd_municipio_autuador,"
								+ "cd_orgao_autuante FROM parametro");
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					dsTitulo1 = rs.getString("nm_orgao_autuador");
					dsTitulo2 = rs.getString("nm_suborgao");
					dsTitulo3 = rs.getString("nm_departamento");
					
					imgLogo64 = Base64.getEncoder().encodeToString(rs.getBytes("img_logo_orgao"));
					imgLogo264 = Base64.getEncoder().encodeToString(rs.getBytes("img_logo_departamento"));
					
					cdMunicipioAutuador = rs.getInt("cd_municipio_autuador");
					cdOrgaoAutuante = rs.getString("cd_orgao_autuante");
				}
			} else {
				dsTitulo1 = ParametroServices.getValorOfParametro("NM_ORGAO_AUTUADOR", connect);
				dsTitulo2 = ParametroServices.getValorOfParametro("NM_SUBORGAO", connect);
				dsTitulo3 = ParametroServices.getValorOfParametro("NM_DEPARTAMENTO", connect);
				
				imgLogo64 = ParametroServices.getValorOfParametro("IMG_LOGO_ORGAO", connect);
				imgLogo264 = ParametroServices.getValorOfParametro("IMG_LOGO_DEPARTAMENTO", connect);
				
				cdMunicipioAutuador = ParametroServices.getValorOfParametroAsInteger("CD_MUNICIPIO_AUTUADOR", 0, 0, connect);
				cdOrgaoAutuante = ParametroServices.getValorOfParametro("CD_ORGAO_AUTUANTE", connect);
			}
			
			Ait ait = get(cdAit, connect);
			if(ait == null) {
				return new Result(-2, "Erro! AitServices.getFormularioProcesso: AIT nulo.");
			}
			
			Infracao infracao = InfracaoServices.getVigente(ait.getCdInfracao(), connect);
			if(infracao == null) {
				return new Result(-3, "Erro! AitServices.getFormularioProcesso: Infração nula.");
			}
			String sql = "SELECT A.*, C.nr_cod_detran, D.nr_matricula, E.nm_marca, E.nm_modelo, F.nm_municipio, G.nm_tipo, H.ds_especie, I.nm_cor "
					+ " FROM ait A " 
					+ " JOIN infracao C ON (A.cod_infracao = C.cod_infracao) "
					+ " JOIN agente D ON (A.cod_agente = D.cod_agente) "
					+ " JOIN marca_modelo E ON (A.cod_marca = E.cod_marca) "
					+ " LEFT OUTER JOIN municipio F ON (A.cod_municipio = F.cod_municipio) "
					+ " LEFT OUTER JOIN tipo_veiculo G ON (A.cod_tipo = G.cod_tipo) " 
					+ " LEFT OUTER JOIN especie_veiculo H ON (A.cod_especie = H.cod_especie) " 
					+ " LEFT OUTER JOIN cor I ON (A.cod_cor = I.cod_cor) " 
					+ " where A.codigo_ait = ?";
			
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setInt(1, ait.getCdAit());
			ResultSet rs = ps.executeQuery();
			rs = ps.executeQuery();
			if(rs.next()) {
				nmModelo = rs.getString("nm_modelo");
				nrCnhCondutor = rs.getString("nr_cnh_condutor");
				ufCnhCondutor = rs.getString("uf_cnh_condutor");
				nrCpfCondutor = rs.getString("nr_cpf_condutor");

				nrCpfProprietario = rs.getString("nr_cpf_proprietario");
				
				if((nrCpfProprietario == null || nrCpfProprietario.trim().equals("")) && rs.getString("nr_cpf_cnpj_proprietario") != null) {
					nrCpfProprietario = rs.getString("nr_cpf_cnpj_proprietario").length() <= 14 ?
										Util.formatCpf( rs.getString("nr_cpf_cnpj_proprietario")) : 
										Util.formatCnpj( rs.getString("nr_cpf_cnpj_proprietario"));
									  	
				}

				nmCondutor = rs.getString("nm_condutor");
				nmProprietario = rs.getString("nm_proprietario");
				dsLocal = rs.getString("ds_local_infracao");
				dsPontoReferencia = rs.getString("ds_ponto_referencia");
				nmMunicipio = rs.getString("nm_municipio");	
			}
			
			String nmReport = "mob/andamentos_ait";
						
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("IMG_LOGO", imgLogo64);
			params.put("IMG_LOGO_2", imgLogo264);
			params.put("DS_TITULO_1", dsTitulo1);
			params.put("DS_TITULO_2", dsTitulo2);
			params.put("DS_TITULO_3", dsTitulo3);
			params.put("NM_PROPRIETARIO", nmProprietario);
			params.put("NR_PLACA", ait.getNrPlaca());
			params.put("NR_INFRACAO_DETRAN", Integer.toString(infracao.getNrCodDetran()));
			params.put("NR_AIT", Integer.toString(ait.getNrAit()));
			params.put("NR_CPF_PROPRIETARIO", nrCpfProprietario);
			params.put("NM_CONDUTOR", nmCondutor);
			params.put("NR_CPF_CONDUTOR", nrCpfCondutor);
			params.put("NR_CNH_CONDUTOR", nrCnhCondutor);
			params.put("UF_CNH_CONDUTOR", ufCnhCondutor);
			params.put("DS_DT_PROCESSO", Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
			params.put("DS_ENDERECO_CONDUTOR", dsLocal);
			params.put("NM_CIDADE", "Vitória da Conquista");
			
			params.put("CD_MUNICIPIO_AUTUADOR", cdMunicipioAutuador);
			params.put("CD_ORGAO_AUTUANTE", cdOrgaoAutuante	);
			params.put("NM_MUNICIPIO", nmMunicipio);
			params.put("NM_MUNICIPIO_AUTUADOR", CidadeServices.getByCodMunicipio(cdMunicipioAutuador, connect).getNmCidade());
			params.put("CD_MUNICIPIO", cdMunicipio);
			params.put("NM_UF", nmUF);
			params.put("NR_PLACA", ait.getNrPlaca());
			params.put("NM_MODELO", nmModelo);
			params.put("CD_RENAVAN", cdRenavan);
			params.put("DS_ESPECIE", dsEspecie);
			params.put("NM_CATEGORIA", nmCategoria);
			params.put("DS_LOCAL", dsLocal);
			params.put("VL_MULTA", Util.formatCurrency(Double.valueOf(vlMulta)));
			params.put("DS_PONTO_REFERENCIA", dsPontoReferencia);
			
			params.put("NR_CNH_CONDUTOR", nrCnhCondutor);
			params.put("UF_CNH_CONDUTOR", ufCnhCondutor);
			params.put("CD_INFRACAO", cdInfracao);
			params.put("DS_INFRACAO", dsInfracao);
			params.put("DT_INFRACAO", Util.formatDate(ait.getDtInfracao(), "dd/MM/yyyy"));
			params.put("HR_INFRACAO", Util.formatDate(ait.getDtInfracao(), "hh:mm"));
			params.put("UF_VEICULO", ufVeiculo);
		
			
			ResultSetMap rsmImpressao = new ResultSetMap();
			
			sql = "SELECT A.*"
					+ " FROM ait_movimento A " 
					+ " where A.codigo_ait = ?"
					+ " ORDER BY nr_movimento ASC";
			
			ps = connect.prepareStatement(sql);
			ps.setInt(1, ait.getCdAit());
			ResultSet rsMovimentos = ps.executeQuery();
			while(rsMovimentos.next()) {
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NR_MOVIMENTO", rsMovimentos.getInt("nr_movimento"));
				register.put("NM_SITUACAO", getSituacao(rsMovimentos.getInt("tp_status")));
				register.put("NR_PROCESSO", rsMovimentos.getString("nr_processo"));
				register.put("LG_REGISTRADO", rsMovimentos.getBoolean("lg_enviado_detran"));

				if(rsMovimentos.getTimestamp("dt_registro_detran") != null)
					register.put("DT_REGISTRO", Util.formatDateTime(rsMovimentos.getTimestamp("dt_registro_detran").getTime(), "dd/MM/yyyy"));
				
				if(rsMovimentos.getTimestamp("dt_movimento") != null)
					register.put("DT_SITUACAO", Util.formatDateTime(rsMovimentos.getTimestamp("dt_movimento").getTime(), "dd/MM/yyyy"));
				
				rsmImpressao.addRegister(register);
			}
			byte[] print = ReportServices.getPdfReport(nmReport, params, rsmImpressao);
			
			return new Result(1, "Sucesso.", "BLB_ARQUIVO", print);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getFormularioProcesso: " + e);
			return new Result(-1, "Erro! AitServices.getFormularioProcesso");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getFormularioProcesso(int cdAit, int tpProcesso, ProcessoRequerente requerente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			String imgLogo64 = null;
			String imgLogo264 = null;
			String dsTitulo1 = null;
			String dsTitulo2 = null;
			String dsTitulo3 = null;
			
			if(Util.isStrBaseAntiga()) {
				PreparedStatement ps = connect
						.prepareStatement("SELECT nm_orgao_autuador, nm_suborgao, nm_departamento, img_logo_orgao, img_logo_departamento FROM parametro");
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					dsTitulo1 = rs.getString("nm_orgao_autuador");
					dsTitulo2 = rs.getString("nm_suborgao");
					dsTitulo3 = rs.getString("nm_departamento");
					
					imgLogo64 = Base64.getEncoder().encodeToString(rs.getBytes("img_logo_orgao"));
					imgLogo264 = Base64.getEncoder().encodeToString(rs.getBytes("img_logo_departamento"));
				}
			} else {
				dsTitulo1 = ParametroServices.getValorOfParametro("NM_ORGAO_AUTUADOR", connect);
				dsTitulo2 = ParametroServices.getValorOfParametro("NM_SUBORGAO", connect);
				dsTitulo3 = ParametroServices.getValorOfParametro("NM_DEPARTAMENTO", connect);
				
				imgLogo64 = ParametroServices.getValorOfParametro("IMG_LOGO_ORGAO", connect);
				imgLogo264 = ParametroServices.getValorOfParametro("IMG_LOGO_DEPARTAMENTO", connect);
			}
			
			Ait ait = get(cdAit, connect);
			if(ait == null) {
				return new Result(-2, "Erro! AitServices.getFormularioProcesso: AIT nulo.");
			}
			
			Infracao infracao = InfracaoServices.getVigente(ait.getCdInfracao(), connect);
			if(infracao == null) {
				return new Result(-3, "Erro! AitServices.getFormularioProcesso: Infração nula.");
			}
			
			String nmReport = "";
			
			ResultSetMap rsm = new ResultSetMap();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("IMG_LOGO", imgLogo64);
			params.put("IMG_LOGO_2", imgLogo264);
			params.put("DS_TITULO_1", dsTitulo1);
			params.put("DS_TITULO_2", dsTitulo2);
			params.put("DS_TITULO_3", dsTitulo3);
			params.put("TP_REQUERENTE", requerente.getTpRequerente());
			params.put("NM_PROPRIETARIO", requerente.getNmRequerente());
			params.put("NR_PLACA", ait.getNrPlaca());
			params.put("NR_INFRACAO_DETRAN", Integer.toString(infracao.getNrCodDetran()));
			params.put("NR_AIT", Integer.toString(ait.getNrAit()));
			params.put("NR_TELEFONE_PROPRIETARIO", requerente.getNrTelefone1Requerente());
			params.put("NR_CPF_PROPRIETARIO", requerente.getNrCpfRequerente());
			params.put("NR_RG_PROPRIETARIO", requerente.getNrRgRequerente());
			params.put("NM_CONDUTOR", requerente.getNmCondutor());
			params.put("NR_RG_CONDUTOR", requerente.getNrRgCondutor());
			params.put("NR_CPF_CONDUTOR", requerente.getNrCpfCondutor());
			params.put("NR_CNH_CONDUTOR", requerente.getNrCnhCondutor());
			params.put("UF_CNH_CONDUTOR", requerente.getUfCnhCondutor());
			params.put("NR_TELEFONE_CONDUTOR", requerente.getNrTelefone1Condutor());
			params.put("DS_DT_PROCESSO", Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
			params.put("DS_ENDERECO_CONDUTOR", requerente.getDsEnderecoCondutor());
			params.put("NM_CIDADE", "Vitória da Conquista");
			
			//XXX
			System.out.println(ait.getDtInfracao());
			System.out.println(Util.formatDate(ait.getDtInfracao(), "dd/MM/yyyy"));
			System.out.println(Util.formatCurrency(Double.valueOf(infracao.getVlInfracao())));
			
			switch (tpProcesso) {
			case ProcessoServices.TP_APRESENTAR_CONDUTOR:
				nmReport = "mob/apresentacao_condutor";
				
				break;
			case ProcessoServices.TP_DEFESA_PREVIA:
				nmReport = "mob/defesa_previa";
				
				if(requerente.getTxtRequisicao()==null || requerente.getTxtRequisicao().trim().equals("")) {
					int nrLine = 1;
					while(nrLine <= 30) {
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NR_LINHA", nrLine);
						rsm.addRegister(register);
						nrLine++;
					}
				} else {
					params.put("TXT_REQUISICAO", requerente.getTxtRequisicao());
				}

				
				break;
			case ProcessoServices.TP_JARI:
				nmReport = "mob/jari";
				
				if(requerente.getTxtRequisicao()==null || requerente.getTxtRequisicao().trim().equals("")) {
					int nrLine = 1;
					while(nrLine <= 30) {
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NR_LINHA", nrLine);
						rsm.addRegister(register);
						nrLine++;
					}
				} else {
					params.put("TXT_REQUISICAO", requerente.getTxtRequisicao());
				}

				
				break;
			case ProcessoServices.TP_REEMBOLSO:
				nmReport = "mob/reembolso";

				params.put("DS_VL_INFRACAO", Util.formatCurrency(Double.valueOf(infracao.getVlInfracao())));
				params.put("DS_DT_INFRACAO", Util.formatDate(ait.getDtInfracao(), "dd/MM/yyyy"));
				params.put("TXT_REQUISICAO", requerente.getTxtRequisicao());
				
				break;
			default:
				break;
			}
			
			byte[] print = ReportServices.getPdfReport(nmReport, params, rsm);
			
			return new Result(1, "Sucesso.", "BLB_ARQUIVO", print);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getFormularioProcesso: " + e);
			return new Result(-1, "Erro! AitServices.getFormularioProcesso");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getSituacao(int tpSituacao) {
//		System.out.println("tpSituacao ==> "+tpSituacao);
		switch(tpSituacao){
			case 0:
			    return "CADASTRO JULGADO";
			case 1:
			    return "CADASTRO IMPRESSO";
			case -1:
			    return "CADASTRO CANCELADO";
			case 2:
			    return "REGISTRO DE INFRA��O";
			case 3:
			    return "NAI ENVIADA";
			case 4:
			    return "AR NAI";
			case 5:
			    return "NIP ENVIADA";
			case 6:
			    return "AR NIP";
			case 7:
			    return "DEFESA EM JULGAMENTO";
			case 8:
			    return "DEFESA DEFERIDA";
			case 9:
			    return "DEFESA INDEFERIDA";
			case 10:
			    return "RECURSO JARI - EM JULGAMENTO";
			case 11:
			    return "JARI COM PROVIMENTO";
			case 12:
			    return "JARI SEM PROVIMENTO";
			case 51:
			    return "RECURSO CETRAN - EM JULGAMENTO";
			case 52:
			    return "RECURSO CETRAN DEFERIDO";
			case 53:
			    return "RECURSO CETRAN INDEFERIDO";
			case 16:
			    return "PENALIDADE SUSPENSA JARI";
			case 17:
			    return "REGISTRO DE MULTA CANCELADO";
			case 25:
			    return "INFRA��O CANCELADA";
			case 18:
			    return "PENALIDADE REATIVADA JARI";
			case 19:
			    return "TRANSFER�NCIA DE PONTUA��O";
			case 20:
			    return "CANCELAMENTO PONTUA��O";
			case 21:
			    return "REGISTRA PONTUA��O";
			case 22:
			    return "SUSPENDE PENALIDADE/PONTUA��O";
			case 23:
			    return "REATIVA��O MULTA/PONTUA��O";
			case 24:
			    return "MULTA PAGA";
			case 26:
			    return "MULTA CANCELADA";
			case 27:
			    return "MUDAN�A DE VENCIMENTO (NIP)";
			case 29:
			    return "DEVOLU��O DE PAGAMENTO";
			case 30:
			    return "REATIVA��O";
			case 31:
			    return "MULTA PAGA EM OUTRA UF";
			case 33:
			    return "REEMBOLSO SOLICITADO";
			case 34:
			    return "REEMBOLSO NEGADO";
			case 35:
			    return "RECALCULO NIC";
			case 36:
			    return "ATUALIZA��O DE DADOS";
			case 162:
			    return "PENALIDADE SUSPENSA CETRAN";
			case 182:
			    return "PENALIDADE REATIVADA CETRAN";
			default:
				return "N�O LOCALIZADO";
		}
	}
	
	private static ArrayList<com.tivic.manager.mob.Ait> toMob(ArrayList<Ait> in) {
		ArrayList<com.tivic.manager.mob.Ait> out = new ArrayList<com.tivic.manager.mob.Ait>();
		
		for (Ait ait : in) {
			out.add(toMob(ait));
		}
		
		return out;
	}
	
	private static com.tivic.manager.mob.Ait toMob(Ait ait) {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			String strIn = mapper.writeValueAsString(ait);
			
			com.tivic.manager.mob.Ait out = mapper.readValue(strIn, com.tivic.manager.mob.Ait.class);
			
			//TODO: correção de parser			
			out.setCdMarcaAutuacao(ait.getCdMarca());
			out.setCdMarcaVeiculo(ait.getCdMarca());
			out.setCdEspecieVeiculo(ait.getCdEspecie());
			out.setCdTipoVeiculo(ait.getCdTipo());
			out.setCdCategoriaVeiculo(ait.getCdCategoria());
			out.setCdCorVeiculo(ait.getCdCor());
			
			out.setNrAnoFabricacao(ait.getDsAnoFabricacao());
			out.setNrAnoModelo(ait.getDsAnoModelo());
			
//			out.setNmCondutor(ait.getNmCondutor());
//			out.setNrCnhCondutor(ait.getNrCnhCondutor());
//			out.setUfCnhCondutor(ait.getUfCnhCondutor());
			
			if(ait.getNmCondutor() != null && !ait.getNmCondutor().trim().equals(""))
				out.setNmCondutorAutuacao(ait.getNmCondutor());	
			out.setNrCnhAutuacao(ait.getNrCnhCondutor());
			out.setUfCnhAutuacao(ait.getUfCnhCondutor());
			
			out.setTpCnhCondutor(ait.getTpCnhCondutor());
			
			out.setTpConvenio(ait.getTpConvenio());
			
			out.setNrDocumento(ait.getNrDocumento());
			out.setNrDocumentoAutuacao(ait.getNrDocumento());
			out.setTpDocumento(ait.getTpDocumento());
						
			ArrayList<com.tivic.manager.mob.AitImagem> imagens = new ArrayList<com.tivic.manager.mob.AitImagem>();
			for (AitImagem img : ait.getImagens()) {
				imagens.add(new com.tivic.manager.mob.AitImagem(0, 0, img.getBlbImagem(), 0, 0));
			}
			out.setImagens(imagens);
			out.setCdLogradouroInfracao(ait.getCdLogradouroInfracao());
			
//			System.out.println("AIT-IN =========================================================================");
//			System.out.println(ait.toString().replaceAll(",", ",\n"));
//			System.out.println("================================================================================");
//			System.out.println(out.toString().replaceAll(",", ",\n"));
//			System.out.println("AIT-OUT ========================================================================");
			
			return out;
		} catch(JsonParseException jpe) {
			jpe.printStackTrace(System.out);
			return null;
		} catch(IOException ioe) {
			ioe.printStackTrace(System.out);
			return null;
		}
	}
	
	private static String checkSiglaTalao(Talonario talonario) {
		if(talonario.getSgTalao() != null)
			return talonario.getSgTalao();
		
		return "";
	}
	
}