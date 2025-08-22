package com.tivic.manager.crm;

import sol.util.Result;
import java.sql.*;
import java.util.*;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;


@DestinationConfig(enabled = false)
public class VoipServices {
	public static Result click2Call(String nrCpf, String nmPessoa, String nmEmail, String nrDDD, String nrTelefone)	{
		Connection connect = Conexao.conectar();
		try	{
			/*
			 * Verifica disponibilidade do CALL Center (Feriados e Horários) 
			 */
			// Horário
			com.tivic.manager.srh.TabelaHorario tabHorario = com.tivic.manager.srh.TabelaHorarioServices.getTabelaClick2Call();
			GregorianCalendar now = new GregorianCalendar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM srh_horario " +
					                                           "WHERE cd_tabela_horario = "+tabHorario.getCdTabelaHorario()+
					                                           "  AND nr_dia_semana     = "+(now.get(Calendar.DAY_OF_WEEK)));
			sol.dao.ResultSetMap rsTabHorario = new sol.dao.ResultSetMap(pstmt.executeQuery());
			boolean lgDisponivel = false;
			while(rsTabHorario.next())	{
				GregorianCalendar hrEntrada = rsTabHorario.getGregorianCalendar("hr_entrada"); 
				GregorianCalendar hrSaida   = rsTabHorario.getGregorianCalendar("hr_saida");
				float minEntrada = (hrEntrada.get(Calendar.HOUR_OF_DAY) * 60) + hrEntrada.get(Calendar.MINUTE);
				float minSaida   = (hrSaida.get(Calendar.HOUR_OF_DAY) * 60) + hrSaida.get(Calendar.MINUTE);
				float minNow     = (now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE);
				if(minNow>=minEntrada && minNow<=minSaida)	{
					lgDisponivel = true;
					break;
				}
			}
			if(!lgDisponivel)
				return new Result(-1, "Prezado cliente, nosso horário de atendimento é de segunda-feira à sexta-feira das 08:00 às 20:00 horas!");
			// Feriado
			if(FeriadoServices.isFeriado(new GregorianCalendar()))
				return new Result(-1, "Prezado cliente hoje é feriado em nosso município! Por favor entre em contato no próximo dia útil " +
						              "no horário das 08:00 às 20:00!");
			/*
			 * Verifica se DDD é aceito 
			 */
			if(connect.prepareStatement("SELECT * FROM crm_regra WHERE tp_regra = "+RegraServices._DDD).executeQuery().next())	{
				String sql = "SELECT * FROM crm_regra " +
							 "WHERE tp_regra = "+RegraServices._DDD+
							 "  AND nm_regra = \'"+nrDDD+"\'";
				if(!connect.prepareStatement(sql).executeQuery().next())
					return new Result(-1, "Ligações oriundas dessa localidade (DDD) não são permitidas!");
			}
			/*
			 * Valida telefone e CPF 
			 */
			String nrCelular = "";
			nrTelefone 	= nrTelefone==null ? "" : nrTelefone.replaceAll("[\\.-]", "");
			nrCpf 		= nrCpf==null      ? "" : nrCpf.replaceAll("[\\.-]", "");

			if(nrTelefone.length()<8)
				return new Result(-1, "Número de telefone inválido! ");
			
			if(nrTelefone.substring(0, 1).equals("8") || nrTelefone.substring(0, 1).equals("9"))	{
				nrCelular  = nrTelefone;
				nrTelefone = null;
			}
			/*
			 * Verifica cadastro 
			 */
			PessoaFisica pessoa = PessoaFisicaServices.getByCpf(nrCpf, connect);
			if(pessoa==null)	{
				int code = Conexao.getSequenceCode("grl_pessoa", connect); 
				pstmt = connect.prepareStatement(
							"INSERT INTO grl_pessoa (cd_pessoa,nm_pessoa,nr_telefone1,nr_celular,nm_email,dt_cadastro,gn_pessoa,st_cadastro) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setInt(1, code);
				pstmt.setString(2, nmPessoa);
				pstmt.setString(3, nrTelefone!=null ? nrDDD+nrTelefone : null);
				pstmt.setString(4, nrCelular!=null ? nrDDD+nrCelular : null);
				pstmt.setString(5, nmEmail);
				pstmt.setTimestamp(6, new Timestamp(new GregorianCalendar().getTimeInMillis()));
				pstmt.setInt(7, PessoaServices.TP_FISICA);
				pstmt.setInt(8, 1/*Ativo*/);
				pstmt.executeUpdate();
				// Inserindo pessoa física
				pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa,nr_cpf) VALUES (?, ?)");
				pstmt.setInt(1, code);
				pstmt.setString(2, nrCpf);
				pstmt.executeUpdate();
				pessoa = PessoaFisicaDAO.get(code, connect);
			}
			else	{
				// Email
				if(pessoa.getNmEmail()==null || pessoa.getNmEmail().equals(""))
					pessoa.setNmEmail(nmEmail);
				// Telefone
				if((pessoa.getNrTelefone1()==null || pessoa.getNrTelefone1().equals("")) && nrTelefone!=null)
					pessoa.setNrTelefone1(nrDDD+nrTelefone);
				// Celular
				if((pessoa.getNrCelular()==null || pessoa.getNrCelular().equals("")) && nrCelular!=null)
					pessoa.setNrCelular(nrDDD+nrCelular);
				pstmt = connect.prepareStatement("UPDATE grl_pessoa SET nm_email = ?, nr_telefone1 = ?, nr_celular = ? " +
						                         " WHERE cd_pessoa = ?");
				pstmt.setString(1, pessoa.getNmEmail());
				pstmt.setString(2, pessoa.getNrTelefone1());
				pstmt.setString(3, pessoa.getNrCelular());
				pstmt.setInt(4, pessoa.getCdPessoa());
				pstmt.executeUpdate();
			}
			/*
			 * Registra ocorrência 
			 */
			// Verifica tipo de ocorrência
			int cdTipoOcorrencia = 0;
			ResultSet rs = connect.prepareStatement("SELECT * from grl_tipo_ocorrencia WHERE nm_tipo_ocorrencia = \'CLICK2CALL\'").executeQuery();
			if(rs.next())
				cdTipoOcorrencia = rs.getInt("cd_tipo_ocorrencia");
			else	{
				com.tivic.manager.grl.TipoOcorrencia tipoOcorrencia = new com.tivic.manager.grl.TipoOcorrencia(0, "CLICK2CALL");
				cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaDAO.insert(tipoOcorrencia, connect);
			}
			// Registrando ocorrencia
			Ocorrencia ocorrencia = new Ocorrencia(0/*cdOcorrencia*/,pessoa.getCdPessoa(),
													"Usou click to call telefone/celular nº ("+nrDDD+")"+(nrCelular!=null ? nrCelular : nrTelefone), 
													new GregorianCalendar(), cdTipoOcorrencia, 
													com.tivic.manager.grl.OcorrenciaServices.ST_CONCLUIDO, 0 /*cdSistema*/, 0 /*cdUsuario*/,  
													0 /*cdAtendimento*/, 0 /*cdAgendamento*/, 0 /*cdCentral*/, 0 /*cdAtendente*/);
			int cdOcorrencia = OcorrenciaDAO.insert(ocorrencia, connect);
			if(cdOcorrencia <= 0)
				return new Result(cdOcorrencia, "Não foi possível registrar ocorrência!");
			// Retorno
			return new Result(1);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar fazer conexão telefonica!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}
