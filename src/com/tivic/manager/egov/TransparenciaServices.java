package com.tivic.manager.egov;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;
import java.util.ArrayList;
import java.text.DecimalFormat;
import javax.servlet.http.HttpSession;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;


public class TransparenciaServices {

	public static void registraAcesso(HttpServletRequest request)	{
		Connection connect = Conexao.conectar();
		try	{
			//
			Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
			String nmLogin  = usuario!=null ? usuario.getNmLogin() : "";
			String nrIP     = request.getRemoteAddr();
			//
			String nmPagina = request.getServletPath();
			if(nmPagina.indexOf("/")>=0)
				nmPagina = nmPagina.substring(nmPagina.lastIndexOf("/")+1);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO log_acesso (cd_acesso,dt_acesso,nm_pagina,nm_login,nr_ip_origem) VALUES (?,?,?,?,?)");
			pstmt.setInt(1, Conexao.getSequenceCode("log_acesso"));
			pstmt.setTimestamp(2, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setString(3, nmPagina);
			pstmt.setString(4, nmLogin);
			pstmt.setString(5, nrIP);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAcessos(ArrayList<ItemComparator> crt)	{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT CAST(dt_acesso AS DATE) AS dt_acesso, COUNT(DISTINCT nr_ip_origem) AS qt_acesso "+
                                                               "FROM log_acesso " +
                                                               "GROUP BY 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Carrega as informações do arquivo que contém as despesas
	 */
	public static Result loadDespesa(byte[] fileDespesa, int cdUsuario, boolean republicar)	{
		Connection connect = Conexao.conectar();
		try	{
			BufferedReader reader = fileDespesa==null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileDespesa)));
			String line = "";
			PreparedStatement pstmtUndOrc = connect.prepareStatement("SELECT * FROM grl_unidade_orcamentaria " +
					                                                  "WHERE nm_unidade_orcamentaria = ?");
			PreparedStatement pstmtCredor = connect.prepareStatement("SELECT * FROM grl_credor " +
                    												  "WHERE nm_credor = ?");
			PreparedStatement pstmtInsert = connect.prepareStatement(
					"INSERT INTO adm_despesa (cd_despesa,cd_unidade_orcamentaria,cd_credor,dt_publicacao,dt_etapa,tp_etapa,nr_processo,"+
                    "                             nr_compra,txt_bem_servico,vl_despesa,nm_funcao,nm_subfuncao,nm_natureza_despesa,nm_fonte_recurso," +
                    "                             nr_etapa,ds_modalidade,cd_arquivo,nr_registro) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			/*
			 * Laço que percorre todo o arquivo
			 */
			Result result = new Result(1);
			int qtEmp = 0, qtLiq = 0, qtPag = 0;
			float vlEmpenhos = 0, vlLiquidacoes = 0, vlPagamentos = 0;
			int cdArquivo = 0, nrRegistro = 0, cdTipoArquivo = 0;
			boolean verificouCabecalho = false;
			String dtArquivo = null;
			while(reader!=null && (line = reader.readLine())!=null)	{
				// CABEÇALHO
				if(line.trim().length()>0 && line.substring(0, 1).equals("0"))	{
					String tpArquivo = line.substring(1,8);
					if(!tpArquivo.equals("DESPESA"))	{
						reader.close();
						return new Result(-1, "Tipo de arquivo incorreto!");
					}
					dtArquivo = line.substring(8,18)+" "+line.substring(18, 26);
					// Tipo de Arquivo
					ResultSet rs = connect.prepareStatement("SELECT * FROM grl_tipo_arquivo WHERE nm_tipo_arquivo = \'DESPESA\'").executeQuery();
					if(!rs.next())	{
						cdTipoArquivo = Conexao.getSequenceCode("grl_tipo_arquivo");
						PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_arquivo (cd_tipo_arquivo,nm_tipo_arquivo) VALUES (?,?)");
						pstmt.setInt(1, cdTipoArquivo);
						pstmt.setString(2, "DESPESA");
						pstmt.executeUpdate();
					}
					else
						cdTipoArquivo = rs.getInt("cd_tipo_arquivo");

					verificouCabecalho = true;
					continue;
				}
				if(line.trim().length()<=0 || !line.substring(0, 1).equals("1"))
					continue;
				//
				if(!verificouCabecalho)
					new Result(-2, "Informações do cabeçalho do arquivo não encontrada!");
				//
				nrRegistro++;
				String nmUnidadeOrc     = line.substring(1, 51).trim();  // OK: Unidade Orçamentária
				String dtPublicacao     = line.substring(51, 61).trim();      // OK: dtEmissao
				String dtEtapa          = line.substring(61, 71).trim();      // OK: dtVencimento
				String tpEtapa          = line.substring(71, 74).trim();      // EMP - Empenho, LIQ - Liquidação, PAG - Pagamento
				String nrProcesso       = line.substring(74, 104).trim();      // Nº do Processo administrativo
				String nrCompra         = line.substring(104, 134).trim();     // Número da Licitação ou Compra Direta
				String dsBemServico     = line.substring(134, 1134).trim();   // Nome do produto ou serviço adquirido
				String nmCredor         = line.substring(1134, 1284).trim();  //
				String nrCpfCnpj        = line.substring(1284, 1302).trim();
				String vlDespesa        = line.substring(1302, 1323).trim();
				String nmFuncao         = line.substring(1323, 1473).trim();
				String nmSubFuncao      = line.substring(1473, 1623).trim();
				String nmNaturezaDespesa  = line.substring(1623, 1773).trim();
				String nmFonteRecurso     = line.substring(1773, 1823).trim();
				String nrEtapa            = line.substring(1823, 1953).trim();  // Número do empenho, liquidação ou pagamento
				String tpModalidadeCompra = line.substring(1953, 1983).trim(); // Modalidade da licitação, dispensa ou inexebilidade
				vlDespesa = vlDespesa.replaceAll("[\\.]", "").replaceAll("[,]", ".");
				// Verifica se é a primeira linha de registro, testa se já existem lançamentos para esse mesmo dia de movimento
				if((qtEmp+qtLiq+qtPag)==0)	{
					System.out.println("Verificando na primeira linha");
					// Verifica a existência do arquivo
					PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_despesa " +
							                         				   "WHERE tp_etapa "+(tpEtapa.equals("PAG")?" = 2 ":" IN (0,1) ")+
							                         				   "  AND dt_etapa = ?");
					pstmt.setTimestamp(1, Util.convStringTimestamp(dtEtapa+" 00:00:00"));
					ResultSet rs = pstmt.executeQuery();
					if(rs.next())	{
						// Se encontrou e não é uma republicação
						if(!republicar)
							return new Result(-2, "O arquivo com "+(tpEtapa.equals("PAG")?"os pagamentos":"os empenhos e liquidações")+" do dia "+dtEtapa+" já foi importado!");
					}
					else if(republicar)
						return new Result(-2, "O arquivo com "+(tpEtapa.equals("PAG")?"os pagamentos":"os empenhos e liquidações")+" do dia "+dtEtapa+" não foi localizado! Republicação não permitida!");
					if(republicar)
						deleteArquivo(rs.getInt("cd_arquivo"), connect);
					// Grava Arquivo
					cdArquivo = Conexao.getSequenceCode("grl_arquivo");
					pstmt = connect.prepareStatement("INSERT INTO grl_arquivo (cd_arquivo,cd_tipo_arquivo,dt_arquivamento,nm_arquivo,cd_usuario,st_arquivo,dt_criacao) VALUES (?,?,?,?,?,?,?)");
					pstmt.setInt(1, cdArquivo);
					pstmt.setInt(2, cdTipoArquivo);
					pstmt.setTimestamp(3, new Timestamp(new java.util.GregorianCalendar().getTimeInMillis()));
					pstmt.setString(4, "DESPESA "+dtArquivo);
					pstmt.setInt(5, cdUsuario);
					pstmt.setInt(6, 0 /*Pendente*/);
					pstmt.setTimestamp(7, Util.convStringTimestamp(dtArquivo));
					pstmt.executeUpdate();
					result.addObject("DT_CRIACAO", Util.convStringTimestamp(dtArquivo));
				}
				// Unidade Orçamentária
				int cdUndOrc = 0;
				pstmtUndOrc.setString(1, nmUnidadeOrc);
				ResultSet rs = pstmtUndOrc.executeQuery();
				if(!rs.next())	{
					cdUndOrc = Conexao.getSequenceCode("grl_unidade_orcamentaria");
					PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_unidade_orcamentaria(cd_unidade_orcamentaria,nm_unidade_orcamentaria,id_unidade_orcamentaria) " +
							                                           " VALUES (?,?,?)");
					pstmt.setInt(1, cdUndOrc);
					pstmt.setString(2, nmUnidadeOrc);
					pstmt.setString(3, "");
					pstmt.executeUpdate();
				}
				else
					cdUndOrc = rs.getInt("cd_unidade_orcamentaria");
				// CREDOR
				int cdCredor = 0;
				pstmtCredor.setString(1, nmCredor);
				rs = pstmtCredor.executeQuery();
				if(!rs.next())	{
					nrCpfCnpj = nrCpfCnpj.replaceAll("[\\.]", "");
					nrCpfCnpj = nrCpfCnpj.replaceAll("[\\-]", "");
					nrCpfCnpj = nrCpfCnpj.replaceAll("[\\/]", "");
					cdCredor = Conexao.getSequenceCode("grl_credor");
					// Pessoa
					PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_credor (cd_credor,nm_credor,nr_cpf_cnpj,tp_pessoa) " +
							                                           " VALUES (?,?,?,?)");
					pstmt.setInt(1, cdCredor);
					pstmt.setString(2, nmCredor);
					pstmt.setString(3, nrCpfCnpj);
					pstmt.setInt(4, nrCpfCnpj.length()<=11?PessoaServices.TP_FISICA:PessoaServices.TP_JURIDICA);
					pstmt.executeUpdate();
				}
				else
					cdCredor = rs.getInt("cd_credor");
				// INSERINDO
				int cdContaPagar = Conexao.getSequenceCode("adm_despesa");
				pstmtInsert.setInt(1, cdContaPagar);
				pstmtInsert.setInt(2, cdUndOrc);
				pstmtInsert.setInt(3, cdCredor);
				pstmtInsert.setTimestamp(4, Util.convStringTimestamp(dtPublicacao+" 00:00:00")); // dtPublicacao
				pstmtInsert.setTimestamp(5, Util.convStringTimestamp(dtEtapa+" 00:00:00")); // dtEtapa
				if(tpEtapa.equals("EMP"))	{
					qtEmp++;
					vlEmpenhos += Float.parseFloat(vlDespesa);
					pstmtInsert.setInt(6, 0/*EMP*/);
				}
				else if(tpEtapa.equals("LIQ"))	{
					qtLiq++;
					vlLiquidacoes += Float.parseFloat(vlDespesa);
					pstmtInsert.setInt(6, 1/*LIQ*/);
				}
				else if(tpEtapa.equals("PAG"))	{
					qtPag++;
					vlPagamentos += Float.parseFloat(vlDespesa);
					pstmtInsert.setInt(6, 2/*PAG*/);
				}
				pstmtInsert.setString(7, nrProcesso);
				pstmtInsert.setString(8, nrCompra);
				pstmtInsert.setString(9, dsBemServico); //
				pstmtInsert.setFloat(10, Float.parseFloat(vlDespesa)); //
				pstmtInsert.setString(11, nmFuncao); //
				pstmtInsert.setString(12, nmSubFuncao); //
				pstmtInsert.setString(13, nmNaturezaDespesa); //
				pstmtInsert.setString(14, nmFonteRecurso); //
				pstmtInsert.setString(15, nrEtapa); //
				pstmtInsert.setString(16, tpModalidadeCompra); //
				pstmtInsert.setInt(17, cdArquivo);
				pstmtInsert.setInt(18, nrRegistro);
				pstmtInsert.executeUpdate();
				result.addObject("DT_MOVIMENTO", Util.convStringTimestamp(dtEtapa+" 00:00:00"));
			}
			// Armazenando informações do usuário
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			result.addObject("usuario", usuario!=null ? usuario.getNmLogin() :"Usuário desconhecido");
			// Log - empenhos
			ResultSetMap rsmLog = new ResultSetMap();
			HashMap<String,Object> reg = new HashMap<String,Object>();
			if(qtEmp > 0)	{
				String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlEmpenhos);
				reg.put("DS_LOG", qtEmp+" empenhos "+(republicar?"re":"")+"importados com sucesso, totalizando R$ "+dsValor+";");
				rsmLog.addRegister(reg);
			}
			// Log - liquidações
			if(qtLiq > 0)	{
				String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlLiquidacoes);
				reg = new HashMap<String,Object>();
				reg.put("DS_LOG", qtLiq+" liquidações "+(republicar?"re":"")+"importadas com sucesso, totalizando R$ "+dsValor+";");
				rsmLog.addRegister(reg);
			}
			// Log - pagamentos
			if(qtPag > 0)	{
				String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlPagamentos);
				reg = new HashMap<String,Object>();
				reg.put("DS_LOG", qtPag+" pagamentos "+(republicar?"re":"")+"importados com sucesso, totalizando R$ "+dsValor+";");
				rsmLog.addRegister(reg);
			}
			// Total
			reg = new HashMap<String,Object>();
			reg.put("DS_LOG", (qtEmp+qtLiq+qtPag)+" registro(s) "+(republicar?"re":"")+"importados no total.");
			rsmLog.addRegister(reg);
			reader.close();
			//
			result.addObject("rsmLog", rsmLog);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar importar despesas", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Carrega as informações do arquivo que contém as despesas
	 */
	public static Result loadReceita(byte[] fileReceita, int cdUsuario, boolean republicar)	{
		Connection connect = Conexao.conectar();
		try	{
			// Carregando arquivo
			BufferedReader reader = fileReceita==null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileReceita)));
			String line = "";
			PreparedStatement pstmtUndGst = connect.prepareStatement("SELECT * FROM grl_unidade_gestora WHERE id_unidade_gestora = ?");
			PreparedStatement pstmtInsert = connect.prepareStatement(
					"INSERT INTO adm_receita (cd_receita,cd_unidade_gestora,dt_publicacao,dt_etapa,tp_receita,tp_modalidade,"+
                    "                         txt_descricao,vl_receita,nm_fonte_recurso,nm_natureza_receita,ds_destinacao,cd_arquivo,nr_registro) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			/*
			 * Laço que percorre todo o arquivo
			 * */
			Result result = new Result(1);
			int qtLancamentos = 0, qtReceitas = 0;
			float vlLancamentos = 0, vlReceitas = 0;
			int cdArquivo = 0, nrRegistro = 0;
			boolean verificouCabecalho = false;
			while(reader!=null && (line = reader.readLine())!=null)	{
				// PRIMEIRA LINHA
				if(line.trim().length()>0 && line.substring(0, 1).equals("0"))	{
					String tpArquivo = line.substring(1,8);
					if(!tpArquivo.equals("RECEITA")) {
						reader.close();
						return new Result(-1, "Tipo de arquivo incorreto!");
					}
					String dtArquivo = line.substring(8,18)+" "+line.substring(18, 26);
					// Tipo de Arquivo
					int cdTipoArquivo = 0;
					ResultSet rs = connect.prepareStatement("SELECT * FROM grl_tipo_arquivo WHERE nm_tipo_arquivo = \'RECEITA\'").executeQuery();
					if(!rs.next())	{
						cdTipoArquivo = Conexao.getSequenceCode("grl_tipo_arquivo");
						PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_arquivo (cd_tipo_arquivo,nm_tipo_arquivo) VALUES (?,?)");
						pstmt.setInt(1, cdTipoArquivo);
						pstmt.setString(2, "RECEITA");
						pstmt.executeUpdate();
					}
					else
						cdTipoArquivo = rs.getInt("cd_tipo_arquivo");
					// Verifica a existência do arquivo
					PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo " +
							                                           "WHERE dt_criacao = ? " +
							                                           "  AND cd_tipo_arquivo = "+cdTipoArquivo);
					pstmt.setTimestamp(1, Util.convStringTimestamp(dtArquivo));
					rs = pstmt.executeQuery();
					if(rs.next())	{
						// Se encontrou e não é uma republicação
						if(!republicar)
							return new Result(-2, "Arquivo já importado! Data de Criação: "+dtArquivo);
					}
					else if(republicar)
						return new Result(-2, "O arquivo com as receitas desse dia não foi localizado! Republicação não permitida!");
					if(republicar)
						deleteArquivo(rs.getInt("cd_arquivo"), connect);
					// Arquivo
					cdArquivo = Conexao.getSequenceCode("grl_arquivo");
					pstmt = connect.prepareStatement("INSERT INTO grl_arquivo (cd_arquivo,cd_tipo_arquivo,dt_arquivamento,nm_arquivo,cd_usuario,st_arquivo,dt_criacao) VALUES (?,?,?,?,?,?,?)");
					pstmt.setInt(1, cdArquivo);
					pstmt.setInt(2, cdTipoArquivo);
					pstmt.setTimestamp(3, new Timestamp(new java.util.GregorianCalendar().getTimeInMillis()));
					pstmt.setString(4, "RECEITA "+dtArquivo);
					pstmt.setInt(5, cdUsuario);
					pstmt.setInt(6, 0 /*Pendente*/);
					pstmt.setTimestamp(7, Util.convStringTimestamp(dtArquivo));
					pstmt.executeUpdate();
					result.addObject("DT_CRIACAO", Util.convStringTimestamp(dtArquivo));
					verificouCabecalho = true;
					continue;
				}
				if(line.trim().length()<=0 || !line.substring(0, 1).equals("1"))
					continue;
				//
				if(!verificouCabecalho)
					new Result(-2, "Informações do cabeçalho do arquivo não encontrada!");
				//
				nrRegistro++;
				String idUnidadeGestora = line.substring(1, 21).trim();       // Unidade Gestora -> Empresa/Filial
				String dtPublicacao     = line.substring(21, 31).trim();      // dtEmissao
				String dtEtapa          = line.substring(31, 41).trim();      // dtVencimento
				String tpReceita        = line.substring(41, 45).trim();      // ORC - Orçamentária, NORC - Não Orçamentária
				String tpModalidade     = line.substring(45, 48).trim();      // LAN - Lançamento, REC - Receita
				String dsReceita        = line.substring(48, 1048).trim();    // Observação (1000 Caracteres)
				String vlReceita        = line.substring(1048, 1069).trim();  // vlConta (21 posições já com formatação)
				String nmFonteRecurso   = line.substring(1069, 1219).trim();  //  150
				String nmNaturezaReceita = line.substring(1219, 1369).trim(); //  150
				String dsDestinacao      = line.substring(1369, 1519).trim(); // Histórico (150)
				vlReceita = vlReceita.replaceAll("[\\.]", "").replaceAll("[,]", ".");
				// Unidade Gestora
				int cdUndGestora = 0;
				pstmtUndGst.setString(1, idUnidadeGestora);
				ResultSet rs = pstmtUndGst.executeQuery();
				if(!rs.next())	{
					cdUndGestora = Conexao.getSequenceCode("grl_unidade_gestora");
					PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_unidade_gestora (cd_unidade_gestora,nm_unidade_gestora,id_unidade_gestora) " +
							                                           " VALUES (?,?,?)");
					pstmt.setInt(1, cdUndGestora);
					pstmt.setString(2, "UNIDADE "+idUnidadeGestora);
					pstmt.setString(3, idUnidadeGestora);
					pstmt.executeUpdate();
				}
				else
					cdUndGestora = rs.getInt("cd_unidade_gestora");
				// INSERINDO
				int cdReceita = Conexao.getSequenceCode("adm_receita");
				pstmtInsert.setInt(1, cdReceita);
				pstmtInsert.setInt(2, cdUndGestora);
				pstmtInsert.setTimestamp(3, Util.convStringTimestamp(dtPublicacao+" 00:00:00")); // dtEmissão
				pstmtInsert.setTimestamp(4, Util.convStringTimestamp(dtEtapa+" 00:00:00")); // dtVencimento
				// Tipo de Receita
				if(tpReceita.equals("NORC"))
					pstmtInsert.setInt(5, 1); // Orçamentária
				else
					pstmtInsert.setInt(5, 0); // Não Orçamentária
				// Modalidade
				if(tpModalidade.equals("LAN"))	{
					qtLancamentos++;
					vlLancamentos += Float.parseFloat(vlReceita);
					pstmtInsert.setInt(6, 0); // Lançamento
				}
				else {
					qtReceitas++;
					vlReceitas += Float.parseFloat(vlReceita);
					pstmtInsert.setInt(6, 1); // Recebimento
				}
				pstmtInsert.setString(7, dsReceita); //
				pstmtInsert.setFloat(8, Float.parseFloat(vlReceita)); //
				pstmtInsert.setString(9, nmFonteRecurso); //
				pstmtInsert.setString(10, nmNaturezaReceita); //
				pstmtInsert.setString(11, dsDestinacao); //
				pstmtInsert.setInt(12, cdArquivo);
				pstmtInsert.setInt(13, nrRegistro);
				pstmtInsert.executeUpdate();
				result.addObject("DT_MOVIMENTO", Util.convStringTimestamp(dtEtapa+" 00:00:00"));
			}
			// Armazenando informações do usuário
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			result.addObject("usuario", usuario!=null ? usuario.getNmLogin() :"Usuário desconhecido");
			// Log - lançamento
			ResultSetMap rsmLog = new ResultSetMap();
			HashMap<String,Object> reg = new HashMap<String,Object>();
			if(qtLancamentos > 0)	{
				String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlLancamentos);
				reg.put("DS_LOG", qtLancamentos+" registros de lançamento importados com sucesso, totalizando R$ "+dsValor+";");
				rsmLog.addRegister(reg);
			}
			// Log - receita
			if(qtReceitas > 0)	{
				reg = new HashMap<String,Object>();
				String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(vlReceitas);
				reg.put("DS_LOG", qtReceitas+" registros de receitas importados com sucesso, totalizando R$ "+dsValor+";");
				rsmLog.addRegister(reg);
			}
			// Total
			reg = new HashMap<String,Object>();
			reg.put("DS_LOG", (qtLancamentos+qtReceitas)+" registros importados no total.");
			rsmLog.addRegister(reg);
			//
			result.addObject("rsmLog", rsmLog);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar importar receitas!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Retorna todas as unidades orçamentárias
	 */
	public static ResultSetMap getAllUnidadeOrcamentaria()	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_unidade_orcamentaria").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Retorna todas as unidades orçamentárias
	 */
	public static ResultSetMap getAllUnidadeGestora()	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_unidade_gestora").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Retorna todas as unidades orçamentárias
	 */
	public static ResultSetMap getAllTipoArquivo()	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_tipo_arquivo").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	/*
	 * Retorna todas as modalidades de compras
	 */
	public static ResultSetMap getAllModalidadeCompra()	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT DISTINCT ds_modalidade FROM adm_despesa " +
					                                         "WHERE ds_modalidade <> \'\' AND ds_modalidade IS NOT NULL").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/*
	 * Retorna todas as modalidades de compras
	 */
	public static ResultSetMap getDates(int tpReceitaDespesa)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT DISTINCT dt_etapa FROM "+(tpReceitaDespesa==0?" adm_receita ":" adm_despesa ")+" ORDER BY dt_etapa").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findDespesa(ArrayList<ItemComparator> criterios) {
		return findDespesa(criterios, null);
	}

	public static ResultSetMap findDespesa(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_unidade_orcamentaria, C.nm_credor FROM adm_despesa A " +
				           "LEFT OUTER JOIN grl_unidade_orcamentaria B ON (A.cd_unidade_orcamentaria = B.cd_unidade_orcamentaria) "+
				           "LEFT OUTER JOIN grl_credor               C ON (A.cd_credor = C.cd_credor) " +
				           "JOIN grl_arquivo D ON (A.cd_arquivo = D.cd_arquivo)" +
				           "WHERE 1 = 1", "ORDER BY tp_etapa, dt_etapa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null, false);
	}

	public static ResultSetMap findReceita(ArrayList<ItemComparator> criterios) {
		return findReceita(criterios, null);
	}

	public static ResultSetMap findReceita(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_unidade_gestora FROM adm_receita A " +
				           "LEFT OUTER JOIN grl_unidade_gestora B ON (A.cd_unidade_gestora = B.cd_unidade_gestora) " +
						   "JOIN grl_arquivo C ON (A.cd_arquivo = C.cd_arquivo)" +
						   "WHERE 1 = 1", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null, false);
	}

	public static Object loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha) {
		return loginAndUpdateSession(session, nmLogin, nmSenha, null);
	}

	public static Object loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			// remove atributos da sessao
			session.removeAttribute("usuario");
			session.removeAttribute("user");

			int cdUsuario = login(nmLogin, nmSenha, connection);
			if (cdUsuario<=0)
				return -1;
			else {
				Usuario usuario = UsuarioDAO.get(cdUsuario, connection);

				usuario.setNmSenha("");

				session.setAttribute("usuario", usuario);
				session.setAttribute("user", usuario);

				return cdUsuario;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int login(String nmLogin, String nmSenha) {
		return login(nmLogin, nmSenha, null);
	}

	public static int login(String nmLogin, String nmSenha, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario " +
															   "WHERE nm_login	 = ? " +
															   "  AND st_usuario = 1");
			pstmt.setString(1, nmLogin);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				if(nmSenha.equals(rs.getString("NM_SENHA")))
					return rs.getInt("CD_USUARIO");
				else
					return -2;
			}
			else
				return -1;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return -3;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insertUsuario(String nmLogin, String nmSenha, int tpUsuario, int stUsuario) {
		return insertUsuario(nmLogin, nmSenha, tpUsuario, stUsuario, null);
	}

	public static int insertUsuario(String nmLogin, String nmSenha, int tpUsuario, int stUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("seg_usuario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario (cd_usuario,"+
			                                  "nm_login,"+
			                                  "nm_senha,"+
			                                  "tp_usuario,"+
			                                  "st_usuario) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,nmLogin);
			pstmt.setString(3,nmSenha);
			pstmt.setInt(4,tpUsuario);
			pstmt.setInt(5,stUsuario);
			pstmt.executeUpdate();
			return code;
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

	public static int updateUsuario(int cdUsuario, String nmLogin, String nmSenha, int tpUsuario, int stUsuario) {
		return updateUsuario(cdUsuario, nmLogin, nmSenha, tpUsuario, stUsuario, null);
	}

	public static int updateUsuario(int cdUsuario, String nmLogin, String nmSenha, int tpUsuario, int stUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario SET cd_usuario=?,"+
												      		   "nm_login=?,"+
												      		   "nm_senha=?,"+
												      		   "tp_usuario=?,"+
												      		   "st_usuario=? WHERE cd_usuario=?");
			pstmt.setInt(1,cdUsuario);
			pstmt.setString(2,nmLogin);
			pstmt.setString(3,nmSenha);
			pstmt.setInt(4,tpUsuario);
			pstmt.setInt(5,stUsuario);
			pstmt.setInt(6, cdUsuario);
			pstmt.executeUpdate();
			return 1;
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

	public static int deleteUsuario(int cdUsuario) {
		return deleteUsuario(cdUsuario, null);
	}

	public static int deleteUsuario(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario WHERE cd_usuario=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.executeUpdate();
			return 1;
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

	public static ResultSetMap getAllUsuario() {
		return getAllUsuario(null);
	}

	public static ResultSetMap getAllUsuario(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario");
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

	public static ResultSetMap findArquivo(int cdTipoArquivo, int stArquivo, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return findArquivo(cdTipoArquivo, stArquivo, tpData, dtInicial, dtFinal, null);
	}

	public static ResultSetMap findArquivo(int cdTipoArquivo, int stArquivo, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			if(dtInicial!=null)	{
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
			}
			if(dtFinal!=null)	{
				dtFinal.set(Calendar.HOUR, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
			}
			String[] tipoData = new String[] {"A.dt_criacao", "A.dt_arquivamento", "A.dt_publicacao"};
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_login, C.nm_tipo_arquivo " +
					                                                     "FROM grl_arquivo A " +
					                                                     "LEFT OUTER JOIN seg_usuario      B ON (A.cd_usuario = B.cd_usuario) " +
					                                                     "LEFT OUTER JOIN grl_tipo_arquivo C ON (A.cd_tipo_arquivo = C.cd_tipo_arquivo) " +
					                                                     "WHERE 1=1 "+
					                                                     (cdTipoArquivo>0 ? " AND A.cd_tipo_arquivo = "+cdTipoArquivo : "")+
					                                                     (stArquivo>=0 ? " AND A.st_arquivo = "+stArquivo : "")+
					                                                     (tpData>=0 && tpData<3? " AND "+tipoData[tpData]+" BETWEEN ? AND ? " : "")+
					                                                     (tpData==3 ? " AND (EXISTS (SELECT * FROM adm_receita A1 " +
					                                                     		      "              WHERE A1.cd_arquivo = A.cd_arquivo " +
					                                                     		      "                AND A1.dt_etapa BETWEEN ? AND ?) " +
					                                                     		      "  OR  EXISTS (SELECT * FROM adm_despesa A1 " +
					                                                     		      "              WHERE A1.cd_arquivo = A.cd_arquivo " +
					                                                     		      "                AND A1.dt_etapa BETWEEN ? AND ?)) " : "")+
					                                                     " ORDER BY nm_tipo_arquivo, dt_criacao");
			if(tpData >= 0){
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				if(tpData==3)	{
					pstmt.setTimestamp(3, new Timestamp(dtInicial.getTimeInMillis()));
					pstmt.setTimestamp(4, new Timestamp(dtFinal.getTimeInMillis()));
				}
			}
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				ResultSet rs = null;
				/*
				 * RECEITAS
				 */
				if(rsm.getString("nm_tipo_arquivo")!=null && rsm.getString("nm_tipo_arquivo").equals("RECEITA"))	{
					rs = connect.prepareStatement("SELECT DISTINCT dt_etapa FROM adm_receita " +
							  					  "WHERE cd_arquivo = "+rsm.getInt("cd_arquivo")).executeQuery();

					if(rs.next())
						rsm.setValueToField("DT_ETAPA", rs.getTimestamp("dt_etapa"));
					// Conteúdo
					int qtTotal = 0;
					String dsLog = "";
					ResultSetMap rsmLog = new ResultSetMap();
					rs = connect.prepareStatement("SELECT tp_modalidade, COUNT(*) AS qt_total, SUM(vl_receita) AS vl_total FROM adm_receita " +
												  "WHERE cd_arquivo = "+rsm.getInt("cd_arquivo")+
												  " GROUP BY tp_modalidade").executeQuery();
					while(rs.next())	{
						String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(rs.getFloat("vl_total"));
						String dsTexto = rs.getInt("qt_total")+(rs.getInt("tp_modalidade")==0?" lançamentos ":" recebimentos ")+" totalizando R$ "+dsValor+"; ";
						dsLog += dsTexto;
						HashMap<String,Object> reg = new HashMap<String,Object>();
						reg.put("DS_LOG", dsTexto);
						rsmLog.addRegister(reg);
						qtTotal += rs.getInt("qt_total");
					}
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("DS_LOG", (qtTotal)+" registros importados no total.");
					rsmLog.addRegister(reg);
					rsm.setValueToField("RSM_LOG", rsmLog);
					rsm.setValueToField("DS_LOG", dsLog);
				}
				/*
				 * DESPESAS
				 */
				else if(rsm.getString("nm_tipo_arquivo")!=null && rsm.getString("nm_tipo_arquivo").equals("DESPESA"))	{
					rs = connect.prepareStatement("SELECT DISTINCT dt_etapa FROM adm_despesa " +
							                      "WHERE cd_arquivo = "+rsm.getInt("cd_arquivo")).executeQuery();
					if(rs.next())
						rsm.setValueToField("DT_ETAPA", rs.getTimestamp("dt_etapa"));
					// Conteúdo
					ResultSetMap rsmLog = new ResultSetMap();
					int qtTotal = 0;
					String dsLog = "";
					String[] tipoEtapa = new String[] {"empenhos","liquidações","pagamentos"};
					rs = connect.prepareStatement("SELECT tp_etapa, COUNT(*) AS qt_total, SUM(vl_despesa) AS vl_total FROM adm_despesa " +
												  "WHERE cd_arquivo = "+rsm.getInt("cd_arquivo")+
												  " GROUP BY tp_etapa").executeQuery();
					while(rs.next())	{
						String dsValor = new DecimalFormat("#,##0.00;-#,##0.00", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(rs.getFloat("vl_total"));
						String dsTexto = rs.getInt("qt_total")+" "+(tipoEtapa[rs.getInt("tp_etapa")])+" totalizando R$ "+dsValor+"; ";
						dsLog += dsTexto;
						HashMap<String,Object> reg = new HashMap<String,Object>();
						reg.put("DS_LOG", dsTexto);
						rsmLog.addRegister(reg);
						qtTotal += rs.getInt("qt_total");
					}
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("DS_LOG", (qtTotal)+" registros importados no total.");
					rsmLog.addRegister(reg);
					rsm.setValueToField("DS_LOG", dsLog);
					rsm.setValueToField("RSM_LOG", rsmLog);
				}
			}
			rsm.beforeFirst();
			return rsm;
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

	public static int publicarArquivo(int cdArquivo) {
		return publicarArquivo(cdArquivo, null);
	}

	public static int publicarArquivo(int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_arquivo SET st_arquivo = 1, dt_publicacao = ? WHERE cd_arquivo = "+cdArquivo);
			pstmt.setTimestamp(1, new Timestamp(new java.util.GregorianCalendar().getTimeInMillis()));
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

	public static int deleteArquivo(int cdArquivo) {
		return deleteArquivo(cdArquivo, null);
	}

	public static int deleteArquivo(int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Excluindo receitas
			connect.prepareStatement("DELETE FROM adm_receita WHERE cd_arquivo="+cdArquivo).executeUpdate();
			// Excluindo despesas
			connect.prepareStatement("DELETE FROM adm_despesa WHERE cd_arquivo="+cdArquivo).executeUpdate();
			// Excluindo arquivo
			return connect.prepareStatement("DELETE FROM grl_arquivo WHERE cd_arquivo="+cdArquivo).executeUpdate();
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
}
