package com.tivic.manager.acd;

import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.TipoTransporte;
import com.tivic.manager.fta.TipoTransporteDAO;
import com.tivic.manager.geo.Camada;
import com.tivic.manager.geo.CamadaDAO;
import com.tivic.manager.geo.TipoCamada;
import com.tivic.manager.geo.TipoCamadaDAO;
import com.tivic.manager.grl.AlergiaServices;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.Escolaridade;
import com.tivic.manager.grl.EscolaridadeDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.TipoDocumentacao;
import com.tivic.manager.grl.TipoDocumentacaoDAO;
import com.tivic.manager.grl.TipoEndereco;
import com.tivic.manager.grl.TipoEnderecoDAO;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.grl.TipoNecessidadeEspecial;
import com.tivic.manager.grl.TipoNecessidadeEspecialDAO;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.VinculoDAO;
import com.tivic.manager.seg.Modulo;
import com.tivic.manager.seg.ModuloDAO;
import com.tivic.manager.seg.SistemaDAO;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioModulo;
import com.tivic.manager.seg.UsuarioModuloDAO;
import com.tivic.manager.seg.UsuarioModuloEmpresa;
import com.tivic.manager.seg.UsuarioModuloEmpresaDAO;
import com.tivic.manager.srh.TipoAdmissao;
import com.tivic.manager.srh.TipoAdmissaoDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class EducacensoServices {
	
	public static Result importEducacenso(byte[] arquivoImportacao){
		return null;
	}
	
	public static String gerarEducacenso()	{
		return registroEscola00()+
		       registroEscola10()+
		       registroTurma20()+
		       registroDocente30()+
		       registroDocente40()+
		       registroDocente50()+
		       registroDocente51()+
		       registroAluno60()+
		       registroAluno70()+
		       registroAluno80();
	}
	
	private static String registroEscola00()	{
		// Tipo de registro			2	N	1	2	Obrigatório - Informe 00
		// Numero da linha			10	N	3	12	Obrigatório
		// Código da Escola - INEP	8	N	13	20	Obrigatório - Código atribuído pelo INEP
		// Sit. de Funcionamento	1	N	21	21	"Obrigatório
		// Início do Ano Letivo		8	DATA	22	29	
		// Término Ano Letivo 		8	DATA	30	37	
		// Nome da Escola			100	X	38	137	"Obrigatório independente da situação de funcionamento da escola
		// CEP						8	X	138	145	Obrigatório independente da situação de funcionamento da escola
		// Endereço					100	X	146	245	"Obrigatório independente da situação de funcionamento da escola
		// Endereço Numero			10	X	246	255	Não obrigatório
		// Complemento 				20	X	256	275	Não obrigatório
		// Bairro					50	X	276	325	Não obrigatório
		// UF						2	N	326	327	Obrigatório independente da situação de funcionamento da escola
		// Município				7	N	328	334	Obrigatório independente da situação de funcionamento da escola
		// Distrito					2	N	335	336	Obrigatório independente da situação de funcionamento da escola
		// DDD						2	X	337	338	Não obrigatório
		// Telefone					8	X	339	346	Não obrigatório
		// Telefone Público 		1	8	X	347	354	
		// Telefone Público 		2	8	X	355	362	
		// FAX						8	X	363	370	
		// E-mail					50	X	371	420	Não obrigatório
		// Código Órgão Regional	5	X	421	425	Não obrigatório
		// Dep. Administrativa		1	N	426	426	"Obrigatório independente da situação de funcionamento da escola
		// Localização/Zona Escola	1	N	427	427	"Obrigatório independente da situação de funcionamento da escola
		// CNPJ Unidade Executora 	14	X	428	441	Não obrigatório mas só poderá ser preenchido quando a situação de funcionamento da escola for "Em Atividade", o campo 22 (dependência administrativa) for igual a "1" (federal) ou "2" (estadual) ou '3" (municipal) ou o campo 22 (dependência administrativa) for igual a "4" (Privada) e o campo 25 (categoria da escola privada) for igual a "4" (filantrôpica)
		// Cat. Escola Privada 		1	N	442	442	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade"" e o campo 22 (dependência administrativa) for igual a ""4"" (privada)
		// Conv. Poder Público		1	N	443	443	"Não obrigatório mas só poderá ser preenchido quando a situação de funcionamento da escola for ""Em Atividade"", o campo 22 (dependência administrativa) for igual a ""4"" (Privada)
		// Número no CNAS			15	X	444	458	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade"", o campo 22 (dependência administrativa) for igual a ""4"" (Privada) e o campo 25 (categoria da escola privada) for igual a ""4"" (filantrópica)
		// Número CEBAS 			15	X	459	473	
		// Mantenedora (Empresas)	1	N	474	474	Empresa, grupos empresariais do setor privado ou pessoa física.    
		// 	    (Sind)				1	N	475	475	Sindicatos de trabalhadores ou patronais, associações, cooperativas e sistemas S.
		// 	    (ONG)				1	N	476	476	Organização não governamental  ONG  internacional ou nacional.
		// 	    (Sem Fins)			1	N	477	477	Instituições sem fins lucrativos
		// CNPJ da Escola Privada	14	X	478	491	Não obrigatório mas só poderá ser preenchido quando a situação de funcionamento da escola for "Em Atividade" e o campo 22 (dependência administrativa) for igual a "4" (Privada)
		// Reg./Credenciamento 		1	N	492	492	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		return "00";
	}
	private static String registroEscola10()	{
		// Tipo de registro	2	N	1	2	Obrigatório - Informe 10	Deverá ser obrigatoriamente preenchido com o valor 10.
		// Número da linha	10	N	3	12	Obrigatório	Deverá ser obrigatoriamente preenchido com o número sequencial da linha no arquivo.
		// Código da Escola - INEP	8	N	13	20	Obrigatório - Código atribuído pelo INEP	Deverá possuir os 8 dígitos informados.
		// Nome do Diretor		100	X	21	120	"Obrigatório independente da situação de funcionamento da escola
		// Número CPF Diretor	11	X	121	131	Obrigatório independente da situação de funcionamento da escola	"Deverá ser obrigatoriamente um CPF válido.
		// Cargo Diretor		80	X	132	211	"Obrigatório independente da situação de funcionamento da escola
		// E-mail Diretor		50	X	212	261	Não obrigatório	"Poderá ser preenchido independente da situação de funcionamento da escola.
		// Local Funcionamento8a	1	N	262	262	Prédio Escolar 	
		// Local Funcionamento8b	1	N	263	263	Templo/Igreja	
		// Local Funcionamento8c	1	N	264	264	Salas de empresa	
		// Local Funcionamento8d	1	N	265	265	Casa do Professor	
		// Local Funcionamento8e	1	N	266	266	Salas em Outra Escola	
		// Local Funcionamento8f	1	N	267	267	Galpão/Rancho/Paiol/Barracão	
		// Local Funcionamento8g	1	N	268	268	Unidade de Internação/Prisional	
		// Local Funcionamento8h	1	N	269	269	Outros	
		// Prédio Compartilhado 	1	N	270	270	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Código da Escola 1	8	N	271	278	As validações de situação de funcionamento e existência do código INEP da escola com a qual compartilha serão feitas no momento do processamento do arquivo no INEP.	Quando informado, deverá possuir os 8 dígitos preenchidos.
		// Código da Escola 2	8	N	279	286	As validações de situação de funcionamento e existência do código INEP da escola com a qual compartilha serão feitas no momento do processamento do arquivo no INEP.	Quando informado, deverá possuir os 8 dígitos preenchidos.
		// Código da Escola 3	8	N	287	294	As validações de situação de funcionamento e existência do código INEP da escola com a qual compartilha serão feitas no momento do processamento do arquivo no INEP.	Quando informado, deverá possuir os 8 dígitos preenchidos.
		// Código da Escola 4	8	N	295	302	As validações de situação de funcionamento e existência do código INEP da escola com a qual compartilha serão feitas no momento do processamento do arquivo no INEP.	Quando informado, deverá possuir os 8 dígitos preenchidos.
		// Código da Escola 5	8	N	303	310	As validações de situação de funcionamento e existência do código INEP da escola com a qual compartilha serão feitas no momento do processamento do arquivo no INEP.	Quando informado, deverá possuir os 8 dígitos preenchidos.
		// Código da Escola 6	8	N	311	318	As validações de situação de funcionamento e existência do código INEP da escola com a qual compartilha serão feitas no momento do processamento do arquivo no INEP.	Quando informado, deverá possuir os 8 dígitos preenchidos.
		// Água Consumida 		1	N	319	319	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Abastecimento Água 12a	1	N	320	320	Rede Pública      	
		// Abastecimento Água 12b	1	N	321	321	Poço Artesiano   	
		// Abastecimento Água 12c	1	N	322	322	Cacimba/Cisterna/Poço 	
		// Abastecimento Água 12d	1	N	323	323	Fonte/Rio/Igarapé/Riacho/Córrego  	
		// Abastecimento Água 12e	1	N	324	324	Inexistente  	
		// Energia Elétrica 13a	1	N	325	325	Rede Pública	
		// Energia Elétrica 13b	1	N	326	326	Gerador	
		// Energia Elétrica 13c	1	N	327	327	Outros (Energia alternativa) Ex: Eólica, Solar e etc.	
		// Energia Elétrica 13d	1	N	328	328	Inexistente	
		// Esgoto Sanitário 14a	1	N	329	329	Rede Pública	
		// Esgoto Sanitário 14b	1	N	330	330	Fossa	
		// Esgoto Sanitário 14c	1	N	331	331	Inexistente	
		// Destinação do Lixo 15a	1	N	332	332	Coleta Periódica	
		// Destinação do Lixo 15b	1	N	333	333	Queima	
		// Destinação do Lixo 15c	1	N	334	334	Joga em outra área	
		// Destinação do Lixo 15d	1	N	335	335	Recicla	
		// Destinação do Lixo 15e	1	N	336	336	Enterra	
		// Destinação do Lixo 15f	1	N	337	337	Outros	
		// Dependências 16a	1	N	338	338	Diretoria	
		// Dependências 16b	1	N	339	339	Sala de Professores	
		// Dependências 16c	1	N	340	340	Laboratório de informática	
		// Dependências 16d	1	N	341	341	Laboratório de ciências	
		// Dependências 16e	1	N	342	342	Sala de recursos multifuncionais para atendimento educacional especializado - AEE	
		// Dependências 16f	1	N	343	343	Quadra de Esportes	
		// Dependências 16g	1	N	344	344	Cozinha	
		// Dependências 16h	1	N	345	345	Biblioteca 	
		// Dependências 16i	1	N	346	346	Sala de leitura	
		// Dependências 16j	1	N	347	347	Parque Infantil	
		// Dependências 16k	1	N	348	348	Berçário	
		// Dependências 16l	1	N	349	349	Sanitário fora do prédio	
		// Dependências 16m	1	N	350	350	Sanitário dentro do prédio	
		// Dependências 16n	1	N	351	351	Sanitário adequado à educação Infantil	
		// Dependências 16o	1	N	352	352	Sanitário adequado a alunos com deficiência ou mobilidade reduzida.	
		// Dependências 16p	1	N	353	353	Dependências e vias adequadas a alunos com deficiência ou mobilidade reduzida.	
		// Dependências 16q	1	N	354	354	Nenhuma das dependências relacionadas	
		// Número Salas Aula 	4	N	355	358	Obrigatório quando a situação de funcionamento da escola for "Em Atividade" e o campo 8a (prédio escolar) for igual a "1" (sim)	"Deverá ser obrigatoriamente preenchido com valor diferente de 0000 quando a situação de funcionamento da escola for Em Atividade e o local de funcionamento da escola for ""Prédio Escolar"".
		// Total Salas Aula 	4	N	359	362	Obrigatório quando a situação de funcionamento da escola for "Em Atividade"	"Deverá ser obrigatoriamente preenchido com valor diferente de 0000 quando a situação de funcionamento da escola for Em Atividade.
		// Equip. Existentes 19a	1	N	363	363	Aparelho de Televisão	
		// Equip. Existentes 19b	1	N	364	364	Videocassete	
		// Equip. Existentes 19c	1	N	365	365	DVD	
		// Equip. Existentes 19d	1	N	366	366	Antena Parabólica	
		// Equip. Existentes 19e	1	N	367	367	Copiadora	
		// Equip. Existentes 19f	1	N	368	368	Retroprojetor	
		// Equip. Existentes 19g	1	N	369	369	Impressora	
		// Computadores		1	N	370	370	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Quantidade Computadores 4	N	371	374	Obrigatório quando a situação de funcionamento da escola for "Em Atividade" e o campo 20 (computadores) for igual a "1" (possui)	"Deverá ser obrigatoriamente preenchido com valor diferente de 0000 quando a situação de funcionamento da escola for Em Atividade e a escola possuir computadores.
		// Comp. Administrativo 	4	N	375	378	Não obrigatório mas só poderá ser preenchido quando a situação de funcionamento da escola for "Em Atividade" e o campo 20 (computadores) for igual a "1" (possui)	"Poderá ser preenchido com valor diferente de 0000 quando a situação de funcionamento da escola for Em Atividade e a escola possuir computadores.
		// Comp. para Uso Alunos	4	N	379	382	Não obrigatório mas só poderá ser preenchido quando a situação de funcionamento da escola for "Em Atividade" e o campo 20 (computadores) for igual a "1" (possui)	"Poderá ser preenchido com valor diferente de 0000 quando a situação de funcionamento da escola for Em Atividade e a escola possuir computadores.
		// Acesso à internet	1	N	383	383	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade"" e o campo 20 (computadores) for igual a ""1"" (possui)
		// Banda Larga		1	N	384	384	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade"", o campo 20 (computadores) for igual a ""1"" (possui) e o campo 20d (acesso à internet) for igual a ""1"" (sim)
		// Total de funcionários 	4	N	385	388	Obrigatório quando a situação de funcionamento da escola for "Em Atividade"	"Deverá ser obrigatoriamente preenchido com valor diferente de 0000 quando a situação de funcionamento da escola for Em Atividade.
		// Alimentação Escolar 	1	N	389	389	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Atend.Especializado 	1	N	390	390	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Atividade Complementar	1	N	391	391	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Mod. Ativ. Complem.25a	1	N	392	392	Regular	
		// Mod. Ativ. Complem.25b	1	N	393	393	Especial	
		// Mod. Ativ. Complem.25c	1	N	394	394	Educação de Jovens e Adultos	
		// Etapas 26a		1	N	395	395	Ensino Regular  - Etapa - (Educação Infantil - Creche)	"Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26b		1	N	396	396	Ensino Regular  - Etapa - (Educação Infantil  - Pré-escola)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26c		1	N	397	397	Ensino Regular  -  Etapa - (Ensino Fundamental 8 anos.)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26d		1	N	398	398	Ensino Regular  -  Etapa - (Ensino Fundamental 9 anos.)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26e		1	N	399	399	Ensino Regular  -  Etapa - (Ensino Médio. - Médio)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26f		1	N	400	400	Ensino Regular  -  Etapa - (Ensino Médio. - Médio Integrado)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26g		1	N	401	401	Ensino Regular  -  Etapa - (Ensino Médio. - Médio Normal/Magisterio)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26h		1	N	402	402	Ensino Regular  -  Etapa - (Ensino Médio. - Médio Educação Profissional)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26i		1	N	403	403	Educação Especial - Etapa - (Educação Infantil - Creche)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26j		1	N	404	404	Educação Especial - Etapa - (Educação Infantil  - Pré-escola)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26k		1	N	405	405	Educação Especial - Etapa - (Ensino Fundamental 8 anos.)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26l		1	N	406	406	Educação Especial -  Etapa - (Ensino Fundamental 9 anos.)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26m		1	N	407	407	Educação Especial - Etapa - (Ensino Médio. - Médio)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26n		1	N	408	408	Educação Especial -  Etapa - (Ensino Médio. - Médio Integrado)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26o		1	N	409	409	Educação Especial - Etapa - (Ensino Médio. - Médio Normal/Magisterio)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26p		1	N	410	410	Educação Especial -  Etapa - (Ensino Médio. - Médio Educação Profissional)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26q		1	N	411	411	Educação Especial -  Etapa - (EJA - Ensino Fundamental)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26r		1	N	412	412	Educação Especial -  Etapa - (EJA - Ensino Médio)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26s		1	N	413	413	Educação de Jovens e Adultos -  Etapa - (EJA - Ensino Fundamental)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Etapas 26t		1	N	414	414	Educação de Jovens e Adultos -  Etapa - (EJA - Ensino Médio)	Deverá ser preenchido de acordo com a Tabela de Combinações Modalidades x Etapas
		// Ensino Fund. ciclos	1	N	415	415	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Localiz. Diferenciada 	1	N	416	416	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Mat. Did. Esp. 29a	1	N	417	417	Não Utiliza	
		// Mat. Did. Esp. 29b	1	N	418	418	Quilombola	
		// Mat. Did. Esp. 29c	1	N	419	419	Indígena	
		// Educação Indígena	1	N	420	420	"Obrigatório quando a situação de funcionamento da escola for ""Em Atividade""
		// Língua do Ensino 31a	1	N	421	421	Língua Indígena	
		// Língua do Ensino 31b	1	N	422	422	Língua Portuguesa	
		// Código Língua Indígena	5	N	423	427	Obrigatório quando a situação de funcionamento da escola for "Em Atividade", o campo 30 (educação indígena) for igual a "1" (sim) e o campo 31a (língua indígena) for igual a "1" (sim)	"Deverá ser obrigatoriamente preenchido de acordo com a Tabela de Língua Indígena quando a situação de funcionamento for Em Atividade, a escola oferecer educação indígena e a língua em que o ensino é ministrado for Língua Indígena.
		return "10";
	}
	private static String registroTurma20()	{
		// Tipo de registro			2	N	1	2	Obrigatório - Informe 20
		// Numero da linha			10	N	3	12	Obrigatório
		// Código da Escola - INEP	8	N	13	20	Obrigatório - Código atribuído pelo INEP
		// Código da Turma - INEP	10	N	21	30	Não obrigatório
		// Código Turma na Escola	20	X	31	50	Obrigatório
		// Nome da Turma			80	X	51	130	"Obrigatório
		// Horário Inicial-Hora		2	X	131	132	"Obrigatório
		// Horário Inicial-Minuto	2	X	133	134	"Obrigatório
		// Horário Final - Hora		2	X	135	136	"Obrigatório
		// Horário Final - Minuto	2	X	137	138	"Obrigatório
		// Tipo de Atendimento		1	N	139	139	"Obrigatório
		// Freq.Sem.Ativ.Compl/AEE	1	N	140	140	"Obrigatório quando o campo 8 (tipo de atendimento) for igual a ""4"" e ""5"" (atendimento educacional especializado - AEE)(atividade complementar)
		// Código do Atividade 1	5	N	141	145	Obrigatório quando o campo 8 (tipo de atendimento) for igual a "4" (atividade complementar)
		// Código do Atividade 2	5	N	146	150	Não obrigatório
		// Código do Atividade 3	5	N	151	155	Não obrigatório
		// Código do Atividade 4	5	N	156	160	Não obrigatório
		// Código do Atividade 5	5	N	161	165	Não obrigatório
		// Código do Atividade 6	5	N	166	170	Não obrigatório
		// Tipo de AEE 16a			1	N	171	171	Sistema Braile
		// Tipo de AEE 16b			1	N	172	172	Atividades da vida autônoma
		// Tipo de AEE 16c			1	N	173	173	Recursos para alunos com baixa visão
		// Tipo de AEE 16d			1	N	174	174	Desenvolvimento de processos mentais
		// Tipo de AEE 16e			1	N	175	175	Orientação e mobilidade
		// Tipo de AEE 16f			1	N	176	176	Língua Brasileira de Sinais
		// Tipo de AEE 16g			1	N	177	177	Comunicação alternativa e aumentativa
		// Tipo de AEE 16h			1	N	178	178	Atividades de enriquecimento curricular
		// Tipo de AEE 16i			1	N	179	179	Soroban
		// Tipo de AEE 16j			1	N	180	180	Informática acessível
		// Tipo de AEE 16k			1	N	181	181	Língua Portuguesa na modalidade escrita
		// Modalidade				1	N	182	182	"Obrigatório quando o campo 8 (tipo de atendimento) for diferente de ""4"" (atividade complementar) e ""5"" (atendimento educacional especializado - AEE)
		// Etapa de Ensino			2	N	183	184	Obrigatório quando o campo 8 (tipo de atendimento) for diferente de "4" (atividade complementar) e "5" (atendimento educacional especializado - AEE)
		// Código Curso Prof.		8	N	185	192	"Obrigatório quando o campo 17 (modalidade) for igual a ""1"" (regular) e o campo 18 (etapa de ensino) for igual a ""30"" ou ""31"" ou ""32"" ou ""33"" ou ""34"" ou 39"" ou ""40""
		// Disciplinas 20a			1	N	193	193	1-Química
		// Disciplinas 20b			1	N	194	194	2-Física
		// Disciplinas 20c			1	N	195	195	3-Matemática
		// Disciplinas 20d			1	N	196	196	4-Biologia
		// Disciplinas 20e			1	N	197	197	5-Ciências
		// Disciplinas 20f			1	N	198	198	6-Língua / Literatura Portuguesa
		// Disciplinas 20g			1	N	199	199	7-Lingua / Literatura Estrangeira - Inglês
		// Disciplinas 20h			1	N	200	200	8-Lingua / Literatura Estrangeira - Espanhol
		// Disciplinas 20i			1	N	201	201	9-Lingua / Literatura Estrangeira - Outra
		// Disciplinas 20j			1	N	202	202	10-Artes (Educação Artística, Teatro, Dança, Música, Artes Plásticas e outras)
		// Disciplinas 20k			1	N	203	203	11-Educação Física
		// Disciplinas 20l			1	N	204	204	12-História
		// Disciplinas 20m			1	N	205	205	13-Geografia
		// Disciplinas 20n			1	N	206	206	14-Filosofia
		// Disciplinas 20o			1	N	207	207	15-Estudos Sociais/Sociologia
		// Disciplinas 20p			1	N	208	208	16-Informática/Computação
		// Disciplinas 20q			1	N	209	209	17-Disciplinas Profissionalizantes
		// Disciplinas 20r			1	N	210	210	20-Disciplinas Voltadas ao Atendimento de Necessidades Especiais (Disciplinas pedagógicas) 
		// Disciplinas 20s			1	N	211	211	21-Disciplinas Voltadas à Diversidade Sócio-Cultural (Disciplinas pedagógicas)                  
		// Disciplinas 20t			1	N	212	212	23-Libras 
		// Disciplinas 20u			1	N	213	213	25 - Disciplinas pedagógicas 
		// Disciplinas 20v			1	N	214	214	26 - Ensino Religioso
		// Disciplinas 20w			1	N	215	215	27 - Lingua indígena
		// Disciplinas 20x			1	N	216	216	99 - Outras Disciplinas		
		return "20";
	}
    private static String registroDocente30()	{
    	return "30";
    }
    private static String registroDocente40()	{
    	return "40";
    }
    private static String registroDocente50()	{
    	return "50";
    }
    private static String registroDocente51()	{
    	return "51";
    }
    private static String registroAluno60()	{
    	return "60";
    }
    private static String registroAluno70()	{
    	return "70";
    }
    private static String registroAluno80()	{
    	return "80";
    }

	public static sol.util.Result importarEducacenso(String nmArquivo)	{
		nmArquivo = "C:/projetos/documents/Escola do Futuro/iuiu/29246547.txt";
		RandomAccessFile raf = null;
		String line = "";
		try	{
			raf = new RandomAccessFile(nmArquivo, "rw");
			//int qtEscolas = 0;
			String lastLine = "";
			while((line = raf.readLine())!=null)	{
				if(line.subSequence(0, 1).equals("0"))
					lastLine = line;
				else if(line.subSequence(0, 1).equals("1"))
					importDadosEscola(lastLine, line);
			}
			if(raf != null)
				raf.close();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		return new sol.util.Result(0);
	}
	
	private static int importDadosEscola(String txtLinha, String txtLinha2)	{
		Connection connect = Conexao.conectar();
		try	{
			// LINHA 1
			String nrInep = txtLinha.substring(12, 20).trim(); 
			//String stEscola = txtLinha.substring(20, 21).trim();
			String nmEscola = txtLinha.substring(37, 137).trim();
			//String nrCep = txtLinha.substring(137, 145).trim();
			//String nmLogradouro = txtLinha.substring(145, 245).trim();
			//String nrEndereco = txtLinha.substring(245, 255).trim();
			//String nmComplemento = txtLinha.substring(255, 275).trim();
			//String nmBairro = txtLinha.substring(275, 325).trim();
			//String sgEstado = txtLinha.substring(325, 327).trim();
			//String nmMunicipio = txtLinha.substring(327, 334).trim();
			//String nmDistrito = txtLinha.substring(334, 336).trim();
			String nrDDD = txtLinha.substring(336, 338).trim();
			String[] nrTelefone = new String[] {txtLinha.substring(338, 346).trim(), txtLinha.substring(346, 354).trim(), txtLinha.substring(354, 362).trim()};
			String nrFax = txtLinha.substring(362, 370).trim();
			String nmEmail = txtLinha.substring(370, 420).trim();
			String nrOrgaoRegional = txtLinha.substring(420, 425).trim();
			int tpDependenciaAdministrativa = 0;
			try	{
				tpDependenciaAdministrativa = Integer.parseInt(txtLinha.substring(425, 426).trim());
			}catch(Exception e){};
			int tpLocalizacao 			   = 0;
			try	{
				tpLocalizacao 			   = Integer.parseInt(txtLinha.substring(426, 427).trim());
			}catch(Exception e){};
			String nrCnpjMantenedora 		   = txtLinha.substring(427, 441).trim();
			int tpCategoriaPrivada 		   	   = 0;
			try	{
				tpCategoriaPrivada 		   	   = Integer.parseInt(txtLinha.substring(441, 442).trim());
			}catch(Exception e){};
			int tpConvenioPublico 		   = 0;
			try	{
				tpConvenioPublico = Integer.parseInt(txtLinha.substring(442, 443).trim());
			}catch(Exception e){};
			String nrCnas 					   = txtLinha.substring(443, 458).trim();
			String nrCebas 					   = txtLinha.substring(458, 472).trim();
			//String[] tpMantenedora 			   = new String[] {txtLinha.substring(473, 474).trim(), txtLinha.substring(474, 475).trim(), 
			//		                                           txtLinha.substring(475, 476).trim(), txtLinha.substring(476, 477).trim()};
			String nrCnpjPrivada               = txtLinha.substring(477, 491).trim();
			int stRegulamentacao            = 0;
			try	{
				stRegulamentacao 		   	   = Integer.parseInt(txtLinha.substring(491).trim());
			}catch(Exception e){};
			// Dados do diretor
			int cdDiretor    = 0;
			String nmDiretor      = txtLinha2.substring(20,120).trim();
			String nrCpfDiretor   = txtLinha2.substring(120,131).trim();
			String nmEmailDiretor = txtLinha2.substring(211,261).trim();
			ResultSet rs = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE nm_pessoa = \'"+nmDiretor+"\'").executeQuery();
			if(rs.next())
				cdDiretor = rs.getInt("cd_pessoa");
			else	{
				PessoaFisica diretor = new PessoaFisica(0/*cdPessoa*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmDiretor,null/*nrTelefone1*/,null/*nrTelefone2*/,
														null/*nrCelular*/,null/*nrFax*/,nmEmailDiretor,new GregorianCalendar()/*dtCadastro*/,
														PessoaServices.TP_FISICA,null/*imgFoto*/,1/*stCadastro*/,null/*nmUrl*/,null/*nmApelido*/,
														null/*txtObservacao*/,0/*lgNotificacao*/,null/*idPessoa*/,0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,
														null/*dtChegadaPais*/,0/*cdNaturalidade*/,0/*cdEscolaridade*/,null/*dtNascimento*/,nrCpfDiretor,
														null/*sgOrgaoRg*/,null/*nmMae*/,null/*nmPai*/,0/*tpSexo*/,0/*stEstadoCivil*/,null/*nrRg*/,
														null/*nrCnh*/,null/*dtValidadeCnh*/,null/*dtPrimeiraHabilitacao*/,0/*tpCategoriaHabilitacao*/,
														0/*tpRaca*/,0/*lgDeficienteFisico*/,null/*nmFormaTratamento*/,0/*cdEstadoRg*/,null/*dtEmissaoRg*/,
														null/*blbFingerprint*/,
														0 /*cdConjuge*/, 0 /*qtMembrosFamilia*/, 0 /*vlRendaFamiliarPerCapta*/, 0 /*tpNacionalidade*/,0 /*tpFiliacaoPai*/);
				cdDiretor = PessoaFisicaDAO.insert(diretor, connect);
			}
			/* Local de funcionamento
			String[] tpLocalFuncionamento = new String[] {txtLinha2.substring(261, 262), txtLinha2.substring(262, 263), 
	                									  txtLinha2.substring(263, 264), txtLinha2.substring(264, 265),
	                									  txtLinha2.substring(265, 266), txtLinha2.substring(266, 267),
	                									  txtLinha2.substring(267, 268), txtLinha2.substring(268, 269)};*/
			// Complemento
			int lgPredioCompartilhado = 0;
			try	{
				lgPredioCompartilhado = Integer.parseInt(txtLinha2.substring(269,270).trim());
			}catch(Exception e){};
			/*
			String[] nrInepCompartilhamento = new String[] {txtLinha2.substring(270, 278), txtLinha2.substring(278, 286), 
					  										txtLinha2.substring(286, 294), txtLinha2.substring(294, 302),
					  										txtLinha2.substring(302, 310), txtLinha2.substring(310, 318)};*/
			int stAguaConsumida       = Integer.parseInt(txtLinha2.substring(318,319).trim());
			/*
			String[] arAbastecimentoAgua = new String[] {txtLinha2.substring(319, 320), txtLinha2.substring(320, 321), 
														 txtLinha2.substring(321, 322), txtLinha2.substring(322, 323),
														 txtLinha2.substring(323, 324)};*/
			int tpAbastecimentoAgua = 0;
			/*
			String[] arFornecimentoEnergia = new String[] {txtLinha2.substring(324, 325), txtLinha2.substring(325, 326), 
					 									   txtLinha2.substring(326, 327), txtLinha2.substring(327, 328)};*/
			int tpFornecimentoEnergia = 0;
			//String[] arEsgotoSanitario = new String[] {txtLinha2.substring(328, 329), txtLinha2.substring(329, 330), txtLinha2.substring(330, 331)};
			int tpEsgotoSanitario = 0;
			/*
			String[] arDestinoLixo = new String[] {txtLinha2.substring(331, 332), txtLinha2.substring(332, 333), 
												   txtLinha2.substring(333, 334), txtLinha2.substring(334, 335),
												   txtLinha2.substring(335, 336), txtLinha2.substring(336, 337)};*/
			int tpDestinoLixo = 0;
			// Dependencias da escola
			/*
			String[] arDependencia = new String[] {txtLinha2.substring(337, 338), txtLinha2.substring(338, 339), 
												   txtLinha2.substring(339, 340), txtLinha2.substring(340, 341),
												   txtLinha2.substring(341, 342), txtLinha2.substring(342, 343),
												   txtLinha2.substring(343, 344), txtLinha2.substring(344, 345),
												   txtLinha2.substring(345, 346), txtLinha2.substring(346, 347),
												   txtLinha2.substring(347, 348), txtLinha2.substring(348, 349),
												   txtLinha2.substring(349, 350), txtLinha2.substring(350, 351),
												   txtLinha2.substring(351, 352), txtLinha2.substring(352, 353),
												   txtLinha2.substring(353, 354)};*/
			int qtSalaAula        = Integer.parseInt(txtLinha2.substring(354, 358).trim());;
			int qtSalaAulaExterna = Integer.parseInt(txtLinha2.substring(358, 362).trim());;
			/*
			String[] tpEquipamento = new String[] {txtLinha2.substring(362, 363), txtLinha2.substring(363, 364), 
					   							   txtLinha2.substring(364, 365), txtLinha2.substring(365, 366),
					   							   txtLinha2.substring(366, 367), txtLinha2.substring(367, 368),
					   							   txtLinha2.substring(368, 369)};*/
			int qtComputadorAdministrativo = Integer.parseInt(txtLinha2.substring(374, 378).trim());
			int qtComputadorAluno          = Integer.parseInt(txtLinha2.substring(378, 382).trim());
			int lgInternet 				   = Integer.parseInt(txtLinha2.substring(382, 383).trim());
			int lgBandaLarga               = Integer.parseInt(txtLinha2.substring(383, 384).trim());
			int nrFuncionarios             = Integer.parseInt(txtLinha2.substring(384, 388).trim());
			int lgAlimentacaoEscolar       = Integer.parseInt(txtLinha2.substring(388, 389).trim());
			int tpAtendimentoEspecializado = Integer.parseInt(txtLinha2.substring(389, 390).trim());
			int tpAtividadeComplementar    = Integer.parseInt(txtLinha2.substring(390, 391).trim());
			//String[] arModalidadeEnsino    = new String[] {txtLinha2.substring(391, 392), txtLinha2.substring(392, 393), txtLinha2.substring(393, 394)};
			int tpModalidadeEnsino         = 0;
			/*String[] arEtapas = new String[] {txtLinha2.substring(394, 395), txtLinha2.substring(395, 396), 
											   txtLinha2.substring(396, 397), txtLinha2.substring(397, 398),
											   txtLinha2.substring(398, 399), txtLinha2.substring(399, 400),
											   txtLinha2.substring(400, 401), txtLinha2.substring(401, 402),
											   txtLinha2.substring(402, 403), txtLinha2.substring(403, 404),
											   txtLinha2.substring(404, 405), txtLinha2.substring(405, 406),
											   txtLinha2.substring(406, 407), txtLinha2.substring(407, 408),
											   txtLinha2.substring(408, 409), txtLinha2.substring(409, 410),
											   txtLinha2.substring(410, 411), txtLinha2.substring(411, 412),
											   txtLinha2.substring(412, 413), txtLinha2.substring(413, 414)};*/
			int lgEnsinoFundamentalCiclo   = Integer.parseInt(txtLinha2.substring(414, 415).trim());
			int tpLocalizacaoDiferenciada  = Integer.parseInt(txtLinha2.substring(415, 416).trim());
			//String[] arMaterialEspecifico  = new String[] {txtLinha2.substring(416, 417), txtLinha2.substring(417, 418), txtLinha2.substring(418, 419)};
			int tpMaterialEspecifico       = 0;
			int lgEducacaoIndigena         = Integer.parseInt(txtLinha2.substring(419, 420).trim());;
			//String[] arLingua              = new String[] {txtLinha2.substring(420, 421), txtLinha2.substring(421, 422)};
			int tpLingua                   = 0;
			int tpEducacaoInfantil         = 0;
			int nrAnosEnsinoFundalmental   = 0;
			int tpEnsinoMedio              = 0;
			int tpEducacaoJovemAdulto      = 0;
			String nrLinguaIndigena        = txtLinha2.substring(422, 427).trim();
			int stInstituicaoPublica       = 0;
			int tpFormaOcupacao            = 0;
			int lgModalidadeRegular        = 0;
			int lgModalidadeEspecial       = 0;
			int lgModalidadeEJA            = 0;
			int lgMaterialDidaticoIndigena   = 0;
			int lgMaterialDidaticoQuilombola = 0;
			int lgBrasilAlfabetizado         = 0;
			int lgEspacoComunidade 			 = 0;
			int lgFormacaoAlternancia        = 0;
			int cdLinguaIndigena             = 0;
			int lgLinguaMinistradaIndigena   = 0;
			int lgLinguaMinistradaPortuguesa = 0;
			Instituicao instituicao = new Instituicao(0/*cdInstituicao*/,0/*cdPessoaSuperior*/,0/*cdPais*/,nmEscola,nrDDD+nrTelefone[0],nrDDD+nrTelefone[1],
										  nrDDD+nrTelefone[2],nrFax,nmEmail,new GregorianCalendar()/*dtCadastro*/,PessoaServices.TP_JURIDICA,
										  null/*imgFoto*/,1/*stCadastro*/,null/*nmUrl*/,null/*nmApelido*/,null/*txtObservacao*/,0/*lgNotificacao*/,
										  null/*idPessoa*/,0/*cdClassificacao*/,0/*cdFormaDivulgacao*/,null/*dtChegadaPais*/,nrCnpjPrivada+nrCnpjMantenedora,
										  nmEscola/*RazaoSocial*/,null/*nrInscricaoEstadual*/,null/*nrInscricaoMunicipal*/,nrFuncionarios, 
										  new GregorianCalendar()/*dtInicioAtividade*/,0/*cdNaturezaJuridica*/,0/*tpEmpresa*/,null/*dtTerminoAtividade*/,
										  0/*lgMatriz*/,null/*imgLogomarca*/,null/*idEmpresa*/,0/*cdTabelaCatEconomica*/,
										  /* DADOS INSTITUIÇÃO */
										  null/*nrSecretario*/,null/*nrDiretor*/,null/*nmDiarioOficial*/,null/*nmResolucao*/,null/*nmParecer*/,
										  0/*qtMinutosMatutino*/,0/*qtMinutosVespertino*/,0/*qtMinutosNoturno*/,0/*nrVagasTeorica*/,0/*nrVagasPratica*/,
										  0/*vlHoraAula*/,cdDiretor,0/*cdCoordenador*/,0/*cdViceDiretor*/,0/*cdSecretario*/,0/*cdTesoureiro*/,0/*cdAdministrador*/, null, null, 0, nrInep);
			InstituicaoEducacenso educacenso = new InstituicaoEducacenso(0/*cdInstituicao*/,nrInep,nrOrgaoRegional,null/*nmOrgaoRegional*/,
					tpDependenciaAdministrativa,tpLocalizacao,
					nrCnpjMantenedora,tpCategoriaPrivada,tpConvenioPublico,nrCnas,nrCebas,stRegulamentacao,
					lgPredioCompartilhado,stAguaConsumida,tpAbastecimentoAgua,tpFornecimentoEnergia,tpEsgotoSanitario,
					tpDestinoLixo,qtSalaAula,qtSalaAulaExterna,qtComputadorAdministrativo,qtComputadorAluno,
					lgInternet,lgBandaLarga,lgAlimentacaoEscolar,tpAtendimentoEspecializado,tpAtividadeComplementar,
					tpModalidadeEnsino,lgEnsinoFundamentalCiclo,tpEducacaoInfantil,nrAnosEnsinoFundalmental,
					tpEnsinoMedio,tpEducacaoJovemAdulto,tpLocalizacaoDiferenciada,tpMaterialEspecifico,
					lgEducacaoIndigena,tpLingua,nrLinguaIndigena,stInstituicaoPublica, tpFormaOcupacao,
					lgModalidadeRegular,lgModalidadeEspecial, lgModalidadeEJA, lgMaterialDidaticoIndigena, lgMaterialDidaticoQuilombola,
					lgBrasilAlfabetizado, lgEspacoComunidade, lgFormacaoAlternancia, cdLinguaIndigena, lgLinguaMinistradaIndigena, lgLinguaMinistradaPortuguesa, 0, nrFuncionarios, null, null, null, null, null, null, 0, 0, 0, 0, 0);
			System.out.println(instituicao);
			System.out.println(educacenso);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
		return 1;	
	}
	
	@SuppressWarnings("unused")
	private static int importDadosTurma(String txtLinha)	{
		Connection connect = Conexao.conectar();
		try	{
			// LINHA 1
			String nrInepEscola = txtLinha.substring(12, 20).trim(); 
			String nrInepTurma  = txtLinha.substring(20, 30).trim();
			//String idTurma      = txtLinha.substring(30, 50).trim();
			String nmTurma      = txtLinha.substring(50, 130).trim();
			int tpAtendimento   = Integer.parseInt(txtLinha.substring(138, 139).trim());			
			int qtDiasSemanaAtividade  = Integer.parseInt(txtLinha.substring(139, 140).trim());
			/*
			String[] arAtivComplementar = new String[] {txtLinha.substring(140, 145), txtLinha.substring(145, 150), 
					   									txtLinha.substring(150, 155), txtLinha.substring(155, 160),
					   									txtLinha.substring(160, 165), txtLinha.substring(165, 170)};
			*/
			// Atendimento Especializado
			ArrayList<String> alAtendEspec = new ArrayList<String>();
			for(int i=170; i<181; i++)
				alAtendEspec.add(txtLinha.substring(i, i+1));
			//
			int tpModalidade = Integer.parseInt(txtLinha.substring(181, 182).trim());
			// Atendimento Especializado
			ArrayList<String> alDisciplinas = new ArrayList<String>();
			for(int i=193; i<216; i++)
				alDisciplinas.add(txtLinha.substring(i, i+1));
			/*
			 * Cadastrando turma
			 */
			int cdInstituicao = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso WHERE nr_inep = \'"+nrInepEscola+"\'").executeQuery();
			if(rs.next())
				cdInstituicao = rs.getInt("cd_instituicao");
			int cdMatriz = 0, cdPeriodoLetivo = 0;
			int cdCurso  = 0, cdCursoPeriodo  = 0, tpTurno = -1; 
			String idTurma = "";
			int tpEducacaoInfantil = 0;
			int lgMaisEduca = 0;
			GregorianCalendar dtAbertura = null, dtConclusao = null;
			Turma turma = new Turma(0,cdMatriz,cdPeriodoLetivo,nmTurma,dtAbertura,dtConclusao,tpTurno,0/*cdCategoriaMensalidade*/,
									0/*cdCategoriaMatricula*/,1/*stTurma*/,cdInstituicao,cdCurso,
									0/*cdTabelaPreco*/,0/*qtVagas*/,cdCursoPeriodo,nrInepTurma,qtDiasSemanaAtividade,
									tpAtendimento,tpModalidade, nmTurma, tpEducacaoInfantil, lgMaisEduca, 0, 0, 0);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
		return 1;	
	}

	public static Result init()	{
		Connection connect = Conexao.conectar();
		try	{
			
			connect.setAutoCommit(false);
			/*
			 *  Tipos de Endereço
			 */
			String[] tiposEndereco = new String[] {"Residencial",
					  							  "Trabalho",
					  							  "Família", 
					  							  "Comercial"};
			for(int i=0; i<tiposEndereco.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_tipo_endereco WHERE nm_tipo_endereco = \'"+(tiposEndereco[i])+"\'").executeQuery();
				if(!rs.next())	{
					TipoEndereco tipoEndereco = new TipoEndereco(0,tiposEndereco[i]);
					if(TipoEnderecoDAO.insert(tipoEndereco, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Endereço");
					}
				}
			}

			String[] tiposLogradouro = new String[] {"Vila",
					 "Largo",
					 "Travessa",
					 "Viela",
					 "Loteamento",
					 "Pátio",
					 "Viaduto",
					 "Área",
					 "Via",
					 "Aeroporto",
					 "Vereda",
					 "Distrito",
					 "Vale",
					 "Núcleo",
					 "Trevo",
					 "Fazenda",
					 "Trecho",
					 "Estrada",
					 "Sítio",
					 "Feira",
					 "Setor",
					 "Morro",
					 "Rua",
					 "Chácara",
					 "Rodovia",
					 "Residencial",
					 "Avenida",
					 "Colônia",
					 "Recanto",
					 "Quadra",
					 "Praça",
					 "Condomínio",
					 "Passarela",
					 "Parque",
					 "Esplanada",
					 "Lagoa",
					 "Favela",
					 "Ladeira",
					 "Lago",
					 "Conjunto",
					 "Jardim",
					 "Estação",
					 "Campo",
					 "Alameda"};

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
																 "FROM grl_tipo_logradouro " +
																 "WHERE nm_tipo_logradouro = ?");
			for(int i=0; i<tiposLogradouro.length; i++)	{
				pstmt.setString(1, tiposLogradouro[i]);
				if(!pstmt.executeQuery().next())	{
					TipoLogradouro tpLogradouro = new TipoLogradouro(0, tiposLogradouro[i], null);
					TipoLogradouroDAO.insert(tpLogradouro, connect);
				}
			}
			
			/*
			 *  Tipos de Mantenedora
			 */
			String[] mantenedoras = new String[] {"Empresa, grupos empresariais do setor privado ou pessoa física",
					  							  "Sind. (trab./patronais), associações, cooperativas",
					  							  "ONG Internacional ou Nacional", 
					  							  "Instituições sem fins lucrativos", 
					  							  "Sistema S (Sesi, Senai, Sesc, outros)"};
			for(int i=0; i<mantenedoras.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_tipo_mantenedora WHERE id_tipo_mantenedora = \'0"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					TipoMantenedora tipoMantenedora = new TipoMantenedora(0,mantenedoras[i],String.valueOf("0" + (i+1)));
					if(TipoMantenedoraDAO.insert(tipoMantenedora, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Mantenedora");
					}
				}
			}
			/*
			 * Locais de Funcionamento 
			 */
			String[] locaisFuncionamentos = new String[] {"Prédio Escolar",
														  "Templo/Igreja",
														  "Salas de empresa",
														  "Casa do Professor",
														  "Salas em Outra Escola",
														  "Galpão/Rancho/Paiol/Barracão",
														  "Unidade de Atendimento Socioeducativo",
														  "Unidade de Internação/Prisional",
														  "Outros"};
			for(int i=0; i<locaisFuncionamentos.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_local_funcionamento WHERE id_local_funcionamento = \'0"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					LocalFuncionamento local = new LocalFuncionamento(0,locaisFuncionamentos[i],String.valueOf("0" + (i+1)));
					if(LocalFuncionamentoDAO.insert(local, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Locais de Funcionamento");
					}
				}
			}
			/*
			 * Abastecimento de Agua
			 */
			String[] abastecimentoAgua = new String[] {"Rede pública",
													   "Poço artesiano",
													   "Cacimba/cisterna/poço",
													   "Fonte/rio/igarapé/riacho/córrego",
													   "Inexistente"};
			for(int i=0; i<abastecimentoAgua.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_abastecimento_agua WHERE id_abastecimento_agua = \'0"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					AbastecimentoAgua local = new AbastecimentoAgua(0,abastecimentoAgua[i],String.valueOf("0" + (i+1)));
					if(AbastecimentoAguaDAO.insert(local, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Abastecimento de Agua");
					}
				}
			}
			/*
			 * Abastecimento de Energia
			 */
			String[] abastecimentoEnergia = new String[] {"Rede pública",
													   	  "Gerador",
													   	  "Outros (energias alternativas)",
													   	  "Inexistente"};
			for(int i=0; i<abastecimentoEnergia.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_abastecimento_energia WHERE id_abastecimento_energia = \'0"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					AbastecimentoEnergia local = new AbastecimentoEnergia(0,abastecimentoEnergia[i],String.valueOf("0" + (i+1)));
					if(AbastecimentoEnergiaDAO.insert(local, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Abastecimento de Energia");
					}
				}
			}
			/*
			 * Esgoto Sanitário
			 */
			String[] esgotoSanitario = new String[] {"Rede pública",
												   	 "Fossa",
												   	 "Inexistente"};
			for(int i=0; i<esgotoSanitario.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_esgoto_sanitario WHERE id_esgoto_sanitario = \'0"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					EsgotoSanitario local = new EsgotoSanitario(0,esgotoSanitario[i],String.valueOf("0" + (i+1)));
					if(EsgotoSanitarioDAO.insert(local, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Esgoto Sanitario");
					}
				}
			}
			/*
			 * Destinação do Lixo
			 */
			String[] destinacaoLixo = new String[] {"Coleta periódica",
												   	"Queima",
												   	"Joga em outra área",
												   	"Recicla",
												   	"Enterra",
												   	"Outros"};
			for(int i=0; i<destinacaoLixo.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_destinacao_lixo WHERE id_destinacao_lixo = \'0"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					DestinacaoLixo local = new DestinacaoLixo(0,destinacaoLixo[i],String.valueOf("0" + (i+1)));
					if(DestinacaoLixoDAO.insert(local, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Destinação de Lixo");
					}
				}
			}
			/*
			 * Tipos de Dependências
			 */
			String[] tiposDependencias = new String[] {"Diretoria",
													   "Sala de Professores",
													   "Sala de Secretaria",
													   "Laboratório de informática",
													   "Laboratório de ciências",
													   "Sala de recursos multifuncionais - AEE", 
													   "Quadra de Esportes coberta",
													   "Quadra de Esportes descoberta",
													   "Cozinha",
													   "Biblioteca",
													   "Sala de Leitura", 
													   "Parque Infantil",
													   "Berçário",
													   "Banheiro fora do prédio",
													   "Banheiro dentro do prédio",
													   "Banheiro adequado a educação infantil",
													   "Banheiro adequado a alunos com deficiência",
													   "Dependências adequadas a alunos com deficiência",
													   "Banheiro com chuveiro",
													   "Refeitório",
													   "Despensa",
													   "Almoxarifado",
													   "Auditório",
													   "Pátio coberto",
													   "Pátio descoberto",
													   "Alojamento de aluno",
													   "Alojamento de professor",
													   "Área verde",
													   "Lavanderia",
													   "Nenhuma das dependências relacionadas",
													   "Sala de Aula"};
			for(int i=0; i<tiposDependencias.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_tipo_dependencia WHERE id_tipo_dependencia = \'"+Util.fillNum(i+1, 2)+"\'").executeQuery();
				if(!rs.next())	{
					TipoDependencia tipoDependencia = new TipoDependencia(0,tiposDependencias[i],Util.fillNum(i+1, 2));
					if(TipoDependenciaDAO.insert(tipoDependencia, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Dependencia");
					}
				}
			}
			/*
			 *  Tipos de Equipamentos
			 */
			String[] equipamentos = new String[] {"Aparelho de Televisão",
												  "Videocassete",
												  "DVD",
												  "Antena Parabólica",
												  "Copiadora",
												  "Retroprojetor",
												  "Impressora",
												  "Aparelho de som",
												  "Data show",
												  "Fax",
												  "Máquina Fotográfica/Filmadora",
												  "Computadores"};
			for(int i=0; i<equipamentos.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento WHERE id_tipo_equipamento = \'"+Util.fillNum(i+1, 2)+"\'").executeQuery();
				if(!rs.next())	{
					TipoEquipamento tipoEquipamento = new TipoEquipamento(0,equipamentos[i],Util.fillNum(i+1, 2));
					if(TipoEquipamentoDAO.insert(tipoEquipamento, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Equipamento");
					}
				}
			}
			
			/*
			 *  Tipos de Periodo
			 */
			String[] periodo = new String[] {"Período Letivo",
												  "Período de Matrícula",
												  "Exames Finais",
												  "Férias Coletivas",
												  "Recesso"};
			for(int i=0; i<periodo.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_tipo_periodo WHERE id_tipo_periodo = \'"+Util.fillNum(i+1, 2)+"\'").executeQuery();
				if(!rs.next())	{
					TipoPeriodo tipoPeriodo = new TipoPeriodo(0,periodo[i],TipoPeriodoServices.ST_ATIVO, Util.fillNum(i+1, 2));
					if(TipoPeriodoDAO.insert(tipoPeriodo, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Periodo");
					}
				}
			}
			
			/*
			 *  Tipos de Etapas
			 */
			ResultSetMap rsmTiposEtapa = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_tipo_etapa").executeQuery());
			if(!rsmTiposEtapa.next()){
				//Educação Infantil e Ensino Fundamental Especial (8 e 9 anos) Multietapa
				TipoEtapa tipoEtapa = new TipoEtapa(0,"Educação Infantil e Ensino Fundamental Especial (8 e 9 anos)","27", 0, 1, 0, 0/*cdEtapaPosterior*/, "EIEF89");
				int cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				if(cdTipoEtapa <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir Tipo Etapa");
				}
				//Multietapa
				Curso curso = new Curso(0, 0, "Multietapa", null, null, null, null, 0, "111", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				int cdCurso = CursoDAO.insert(curso, connect);
				if(cdCurso <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir Curso");
				}
				CursoEtapa cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "56", cdTipoEtapa);
				int cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				if(cdCursoEtapa <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir Curso Etapa");
				}
				
				//Educação Infantil e Ensino Fundamental (8 e 9 anos) Multietapa
				tipoEtapa = new TipoEtapa(0,"Educação Infantil e Ensino Fundamental (8 e 9 anos)","26", 1, 0, 0, 0/*cdEtapaPosterior*/, "EIF89");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Multietapa
				curso = new Curso(0, 0, "Multietapa", null, null, null, null, 0, "110", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "56", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Profissional Especial Mista
				tipoEtapa = new TipoEtapa(0,"Educação Profissional Especial Mista","25", 0, 1, 0, 0/*cdEtapaPosterior*/, "EPEM");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Subsequente
				curso = new Curso(0, 0, "Subsequente", null, null, null, null, 0, "109", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "64", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Concomitantemente
				curso = new Curso(0, 0, "Concomitantemente", null, null, null, null, 0, "108", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "64", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Profissional Mista
				tipoEtapa = new TipoEtapa(0,"Educação Profissional Mista","24", 1, 0, 0, 0/*cdEtapaPosterior*/, "EPM");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Subsequente
				curso = new Curso(0, 0, "Subsequente", null, null, null, null, 0, "107", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "64", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Concomitantemente
				curso = new Curso(0, 0, "Concomitantemente", null, null, null, null, 0, "106", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "64", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação EJA - Ensino Médio
				tipoEtapa = new TipoEtapa(0,"Educação - EJA - Ensino Médio","23", 0, 0, 1, 0/*cdEtapaPosterior*/, "EEJM");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Médio semipresencial
				curso = new Curso(0, 0, "Médio", null, null, null, null, 0, "105", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "48", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Médio presencial
				curso = new Curso(0, 0, "Médio", null, null, null, null, 0, "104", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "45", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Profissional Médio semipresencial
				curso = new Curso(0, 0, "Profissional Médio", null, null, null, null, 0, "103", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "63", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Profissional Médio presencial
				curso = new Curso(0, 0, "Profissional Médio", null, null, null, null, 0, "102", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "62", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação EJA - Ensino Fundamental - Projovem Urbano
				tipoEtapa = new TipoEtapa(0,"Educação EJA - Ensino Fundamental Projovem (urbano)","22", 0, 0, 1, 0/*cdEtapaPosterior*/, "EEJFPU");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Anos iniciais e finais semipresencial
				curso = new Curso(0, 0, "Projovem (urbano)", null, null, null, null, 0, "101", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "65", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação EJA - Ensino Fundamental
				tipoEtapa = new TipoEtapa(0,"Educação - EJA - Ensino Fundamental","21", 0, 0, 1, 0/*cdEtapaPosterior*/, "EJF");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Profissional semipresencial
				curso = new Curso(0, 0, "Profissional", null, null, null, null, 0, "100", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "61", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Profissional presencial
				curso = new Curso(0, 0, "Profissional", null, null, null, null, 0, "99", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "60", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais e finais semipresencial
				curso = new Curso(0, 0, "Anos iniciais e finais", null, null, null, null, 0, "98", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "58", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais e finais presencial
				curso = new Curso(0, 0, "Anos iniciais e finais", null, null, null, null, 0, "97", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "51", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos finais semipresencial
				curso = new Curso(0, 0, "Anos finais", null, null, null, null, 0, "96", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "47", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais semipresencial
				curso = new Curso(0, 0, "Anos iniciais", null, null, null, null, 0, "95", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "46", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos finais presencial
				curso = new Curso(0, 0, "Anos finais", null, null, null, null, 0, "94", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "44", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais presencial
				curso = new Curso(0, 0, "Anos iniciais", null, null, null, null, 0, "93", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "43", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				
				//Educação Especial EJA - Ensino Médio
				tipoEtapa = new TipoEtapa(0,"Educação Especial - EJA - Ensino Médio","20", 0, 1, 1, 0/*cdEtapaPosterior*/, "EEEJM");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Médio semipresencial
				curso = new Curso(0, 0, "Médio", null, null, null, null, 0, "92", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "48", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Médio presencial
				curso = new Curso(0, 0, "Médio", null, null, null, null, 0, "91", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "45", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				
				//Educação Especial EJA - Ensino Fundamental
				tipoEtapa = new TipoEtapa(0,"Educação Especial - EJA - Ensino Fundamental","19", 0, 1, 1, 0/*cdEtapaPosterior*/, "EEEJF");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Profissional semipresencial
				curso = new Curso(0, 0, "Profissional", null, null, null, null, 0, "90", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "61", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Profissional presencial
				curso = new Curso(0, 0, "Profissional", null, null, null, null, 0, "89", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "60", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais e finais semipresencial
				curso = new Curso(0, 0, "Anos iniciais e finais", null, null, null, null, 0, "88", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "58", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais e finais presencial
				curso = new Curso(0, 0, "Anos iniciais e finais", null, null, null, null, 0, "87", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "51", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos finais semipresencial
				curso = new Curso(0, 0, "Anos finais", null, null, null, null, 0, "86", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "47", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais semipresencial
				curso = new Curso(0, 0, "Anos iniciais", null, null, null, null, 0, "85", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "46", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos finais presencial
				curso = new Curso(0, 0, "Anos finais", null, null, null, null, 0, "84", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "44", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Anos iniciais presencial
				curso = new Curso(0, 0, "Anos iniciais", null, null, null, null, 0, "83", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "43", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				
				//Educação Especial - Ensino Médio - Educação Profissional
				tipoEtapa = new TipoEtapa(0,"Educação Especial - Ensino Médio - Educação Profissional","18", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEMP");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Subsequente
				curso = new Curso(0, 0, "Subsequente", null, null, null, null, 0, "82", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "40", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Concomitantemente
				curso = new Curso(0, 0, "Concomitantemente", null, null, null, null, 0, "81", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "39", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Profissional Médio semipresencial
				curso = new Curso(0, 0, "Educação Profissional", null, null, null, null, 0, "80", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_SEMIPRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "63", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Profissional Médio presencial
				curso = new Curso(0, 0, "Educação Profissional", null, null, null, null, 0, "79", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "62", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial - Ensino Médio - Integrado
				tipoEtapa = new TipoEtapa(0,"Educação Especial - Ensino Médio - Normal/Magistério","17", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEMN");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//4 serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "78", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 12, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "38", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "77", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 11, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "37", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "76", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 10, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "36", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "75", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 9, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "35", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial - Ensino Médio - Integrado
				tipoEtapa = new TipoEtapa(0,"Educação Especial - Ensino Médio - Integrado","16", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEMI");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Não seriada
				curso = new Curso(0, 0, "Não seriada", null, null, null, null, 0, "74", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "34", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "73", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 12, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "33", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "72", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 11, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "32", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "71", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 10, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "31", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "70", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 9, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "30", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial - Ensino Médio - Médio
				tipoEtapa = new TipoEtapa(0,"Educação Especial - Ensino Médio - Médio","15", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEM");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Não seriada
				curso = new Curso(0, 0, "Não seriada", null, null, null, null, 0, "69", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "29", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "68", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 12, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "28", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "67", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 11, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "27", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "66", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 10, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "26", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "65", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 9, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "25", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial - Ensino Fundamental 9 anos
				tipoEtapa = new TipoEtapa(0,"Educação Especial - Ensino Fundamental 9 anos","14", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEF9");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Correção de Fluxo
				curso = new Curso(0, 0, "Correção de Fluxo", null, null, null, null, 0, "64", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "23", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Multi
				curso = new Curso(0, 0, "Multi", null, null, null, null, 0, "63", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "22", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//9 Ano
				curso = new Curso(0, 0, "9 ano", null, null, null, null, 0, "62", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 8, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "41", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//8 Ano
				curso = new Curso(0, 0, "8 ano", null, null, null, null, 0, "61", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 7, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "21", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//7 Ano
				curso = new Curso(0, 0, "7 ano", null, null, null, null, 0, "60", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 6, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "20", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//6 Ano
				curso = new Curso(0, 0, "6 ano", null, null, null, null, 0, "59", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 5, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "19", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//5 Ano
				curso = new Curso(0, 0, "5 ano", null, null, null, null, 0, "58", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 4, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "18", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 Ano
				curso = new Curso(0, 0, "4 ano", null, null, null, null, 0, "57", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 3, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "17", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 Ano
				curso = new Curso(0, 0, "3 ano", null, null, null, null, 0, "56", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 2, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "16", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 Ano
				curso = new Curso(0, 0, "2 ano", null, null, null, null, 0, "55", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "15", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 Ano
				curso = new Curso(0, 0, "1 ano", null, null, null, null, 0, "54", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 0, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "14", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial - Ensino Fundamental 8 anos
				tipoEtapa = new TipoEtapa(0,"Educação Especial - Ensino Fundamental 8 anos","13", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEF8");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Correção de Fluxo
				curso = new Curso(0, 0, "Correção de Fluxo", null, null, null, null, 0, "53", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "13", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Multi
				curso = new Curso(0, 0, "Multi", null, null, null, null, 0, "52", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "12", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//8 Serie
				curso = new Curso(0, 0, "8 serie", null, null, null, null, 0, "51", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 8, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "11", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//7 Serie
				curso = new Curso(0, 0, "7 serie", null, null, null, null, 0, "50", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 7, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "10", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//6 Serie
				curso = new Curso(0, 0, "6 serie", null, null, null, null, 0, "49", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 6, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "9", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//5 Serie
				curso = new Curso(0, 0, "5 serie", null, null, null, null, 0, "48", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 5, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "8", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 Serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "47", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 4, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "7", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 Serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "46", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 3, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "6", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 Serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "45", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 2, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "5", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 Serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "44", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "4", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial Infantil - Pré-escola (4 e 5 anos)
				tipoEtapa = new TipoEtapa(0,"Educação Especial Infantil - Pré-escola (4 e 5 anos)","12", 0, 1, 0, 0/*cdEtapaPosterior*/, "EEIP");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				curso = new Curso(0, 0, "Pré-escola (4 e 5 anos)", null, null, null, null, 0, "43", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "2", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Especial Infantil - Creche (0 a 3 anos)
				tipoEtapa = new TipoEtapa(0,"Educação Especial Infantil - Creche (0 a 3 anos)","11", 0, 1, 0, cdTipoEtapa, "EEIC");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				curso = new Curso(0, 0, "Creche (0 a 3 anos)", null, null, null, null, 0, "42", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "1", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Médio - Educação Profissional
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Médio - Educação Profissional","10", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERMP");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Subsequente
				curso = new Curso(0, 0, "Subsequente", null, null, null, null, 0, "41", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "40", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Concomitantemente
				curso = new Curso(0, 0, "Concomitantemente", null, null, null, null, 0, "40", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "39", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Médio - Integrado
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Médio - Normal/Magistério","9", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERMN");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//4 serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "39", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 12, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "38", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "38", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 11, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "37", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "37", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 10, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "36", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "36", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 9, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "35", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Médio - Integrado
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Médio - Integrado","8", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERMI");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Não seriada
				curso = new Curso(0, 0, "Não seriada", null, null, null, null, 0, "35", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "34", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "34", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 12, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "33", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "33", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 11, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "32", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "32", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 10, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "31", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "31", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 9, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "30", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Médio - Médio
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Médio - Médio","7", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERM");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Não seriada
				curso = new Curso(0, 0, "Não seriada", null, null, null, null, 0, "30", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "29", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "29", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 12, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "28", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "28", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 11, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "27", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "27", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 10, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "26", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "26", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 9, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "25", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Fundamental 8 e 9 anos
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Fundamental 8 e 9 anos","6", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERF89");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				curso = new Curso(0, 0, "Multi 8 e 9 anos", null, null, null, null, 0, "25", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 0, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "24", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Fundamental 9 anos
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Fundamental 9 anos","5", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERF9");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Correção de Fluxo
				curso = new Curso(0, 0, "Correção de Fluxo", null, null, null, null, 0, "24", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "23", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Multi
				curso = new Curso(0, 0, "Multi", null, null, null, null, 0, "23", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "22", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//9 Ano
				curso = new Curso(0, 0, "9 ano", null, null, null, null, 0, "22", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 8, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "41", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//8 Ano
				curso = new Curso(0, 0, "8 ano", null, null, null, null, 0, "21", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 7, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "21", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//7 Ano
				curso = new Curso(0, 0, "7 ano", null, null, null, null, 0, "20", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 6, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "20", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//6 Ano
				curso = new Curso(0, 0, "6 ano", null, null, null, null, 0, "19", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 5, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "19", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//5 Ano
				curso = new Curso(0, 0, "5 ano", null, null, null, null, 0, "18", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 4, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "18", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 Ano
				curso = new Curso(0, 0, "4 ano", null, null, null, null, 0, "17", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 3, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "17", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 Ano
				curso = new Curso(0, 0, "3 ano", null, null, null, null, 0, "16", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 2, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "16", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 Ano
				curso = new Curso(0, 0, "2 ano", null, null, null, null, 0, "15", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "15", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 Ano
				curso = new Curso(0, 0, "1 ano", null, null, null, null, 0, "14", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 0, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "14", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Regular - Ensino Fundamental 8 anos
				tipoEtapa = new TipoEtapa(0,"Educação Regular - Ensino Fundamental 8 anos","4", 1, 0, 0, 0/*cdEtapaPosterior*/, "ERF8");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				//Correção de Fluxo
				curso = new Curso(0, 0, "Correção de Fluxo", null, null, null, null, 0, "13", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "13", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//Multi
				curso = new Curso(0, 0, "Multi", null, null, null, null, 0, "12", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "12", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//8 Serie
				curso = new Curso(0, 0, "8 serie", null, null, null, null, 0, "11", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 8, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "11", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//7 Serie
				curso = new Curso(0, 0, "7 serie", null, null, null, null, 0, "10", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 7, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "10", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//6 Serie
				curso = new Curso(0, 0, "6 serie", null, null, null, null, 0, "09", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 6, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "9", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//5 Serie
				curso = new Curso(0, 0, "5 serie", null, null, null, null, 0, "08", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 5, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "8", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//4 Serie
				curso = new Curso(0, 0, "4 serie", null, null, null, null, 0, "07", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 4, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "7", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//3 Serie
				curso = new Curso(0, 0, "3 serie", null, null, null, null, 0, "06", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 3, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "6", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//2 Serie
				curso = new Curso(0, 0, "2 serie", null, null, null, null, 0, "05", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 2, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "5", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				//1 Serie
				curso = new Curso(0, 0, "1 serie", null, null, null, null, 0, "04", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, 1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "4", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Infantil - Unificada (0 a 5 anos)
				tipoEtapa = new TipoEtapa(0,"Educação Infantil - Unificada (0 a 5 anos)","3", 1, 0, 0, 0/*cdEtapaPosterior*/, "EIU");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				curso = new Curso(0, 0, "Unificada (0 a 5 anos)", null, null, null, null, 0, "03", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "3", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Infantil - Pré-escola (4 e 5 anos)
				tipoEtapa = new TipoEtapa(0,"Educação Infantil - Pré-escola (4 e 5 anos)","2", 1, 0, 0, 0/*cdEtapaPosterior*/, "EIP");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				curso = new Curso(0, 0, "Pré-escola (4 e 5 anos)", null, null, null, null, 0, "02", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, 0/*cdCursoEtapaPosterior*/, "2", cdTipoEtapa);
				cdCursoEtapa = CursoEtapaDAO.insert(cursoEtapa, connect);
				
				//Educação Infantil - Creche (0 a 3 anos)
				tipoEtapa = new TipoEtapa(0,"Educação Infantil - Creche (0 a 3 anos)","1", 1, 0, 0, cdTipoEtapa, "EIC");
				cdTipoEtapa = TipoEtapaDAO.insert(tipoEtapa, connect);
				curso = new Curso(0, 0, "Creche (0 a 3 anos)", null, null, null, null, 0, "01", null, 0, 0, 0, null, 0, null, 0, 0, 0, null, null, null, null, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0, 0, 0, CursoServices.TP_PRESENCIAL, -1, 0, 0);
				cdCurso = CursoDAO.insert(curso, connect);
				cursoEtapa = new CursoEtapa(0, cdCurso, cdCursoEtapa, "1", cdTipoEtapa);
				CursoEtapaDAO.insert(cursoEtapa, connect);
			}
			
			//Import a tabela de Linguas Indigenas
//			Result resultado = EducacensoImport.importLinguasIndigenasFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				return resultado;
//			}
//			
			//Import a tabela de Atividades complementares
//			resultado = EducacensoImport.importAtividadeComplementarFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				return resultado;
//			}
//			
			/*
			 * Atendimento Especializado
			 */
			String[] atendimentoEspecializado = new String[] {"Sistema Braile",
															  "Ensino de uso ópticos e não ópticos",
															  "Desenvolvimento de processos mentais",
															  "Orientação e mobilidade",
															  "Língua Brasileira de Sinais",
															  "Comunicação alternativa e aumentativa",
															  "Atividades de enriquecimento curricular",
															  "Soroban",
															  "Informática acessível",
															  "Língua Portuguesa na modalidade escrita",
															  "Autonomia no ambiente escolar"};
			for(int i=0; i<atendimentoEspecializado.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_atendimento_especializado WHERE id_atendimento_especializado = \'"+(i+1)+"\'").executeQuery();
				if(!rs.next())	{
					AtendimentoEspecializado atendimentoEsp = new AtendimentoEspecializado(0,atendimentoEspecializado[i],String.valueOf(i+1));
					if(AtendimentoEspecializadoDAO.insert(atendimentoEsp, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Atendimento Especializado");
					}
				}
			}
			
			/*
			 * Áreas de Conhecimento
			 */
			String[][] areasConhecimento = new String[][] {{InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_NATUREZA,"Ciências da Natureza"},
													 {InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_MATEMATICA,"Matemática"},
													 {InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS,"Língua Portuguesa, Língua Estrangeira, Educação Artística e Educação Física"},
													 {InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS,"Ciências Humanas"},
					                                 {InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS,"Outras áreas"}};
			for(int i=0; i<areasConhecimento.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_produto_servico A, acd_disciplina B " +
						                                "WHERE A.cd_produto_servico = B.cd_disciplina " +
						                                "  AND id_produto_servico = \'"+areasConhecimento[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					AreaConhecimento areaConhecimento = new AreaConhecimento(0, areasConhecimento[i][1], 0/*cdAreaConhecimentoSuperior*/, areasConhecimento[i][0]);	
					if(AreaConhecimentoDAO.insert(areaConhecimento, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Áreas de Conhecimento");
					}
				}
			}
			
			/*
			 * Disciplinas
			 */
			String[][] disciplinas = new String[][] {{"1","Química", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_NATUREZA, "QUI"},
													 {"2","Física", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_NATUREZA, "FIS"},
													 {"3","Matemática", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_MATEMATICA, "MAT"},
													 {"4","Biologia", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_NATUREZA, "BIO"},
													 {"5","Ciências", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_NATUREZA, "CIE"},
					                                 {"6","Língua /Literatura Portuguesa", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LING PORT"},
					                                 {"7","Língua /Literatura estrangeira - Inglês", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LING ING"},
					                                 {"8","Língua /Literatura estrangeira - Espanhol", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LING ESP"},
					                                 {"30","Língua /Literatura estrangeira - Francês", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LING FRA"},
					                                 {"9","Língua /Literatura estrangeira - outra", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LING OUT"},
					                                 {"10","Artes (Educação Artística, Teatro, Dança, Música, Artes Plásticas e outras)", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "ART"},
					                                 {"11","Educação Física", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "ED FIS"},
					                                 {"12","História", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS, "HIS"},
					                                 {"13","Geografia", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS, "GEO"},
					                                 {"14","Filosofia", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS, "FIL"},
					                                 {"28","Estudos Sociais", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS, "EST SOC"},
					                                 {"29","Sociologia", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS, "SOC"},
					                                 {"16","Informática/Computação", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "INF"},
					                                 {"17","Disciplinas profissionalizantes", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "DIS PROF"},
					                                 {"20","Disciplinas voltadas ao atendimento de necessidades especiais (disciplina pedagógica)", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "DIS AEE"},
					                                 {"21","Disciplinas voltadas à diversidade sócio-cultural (disciplina pedagógica)", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "DIS DIV"},
					                                 {"23","Libras", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LIB"},
					                                 {"25","Disciplinas pedagógicas", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "DIS PED"},
					                                 {"26","Ensino religioso", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "ENS REL"},
					                                 {"27","Lingua indígena", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_LINGUAGENS, "LING IND"},
					                                 {"99","Outras Disciplinas", InstituicaoEducacensoServices.TP_AREA_CONHECIMENTO_OUTRAS, "OUT"}};
			for(int i=0; i<disciplinas.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_produto_servico A, acd_disciplina B " +
						                                "WHERE A.cd_produto_servico = B.cd_disciplina " +
						                                "  AND id_produto_servico = \'"+disciplinas[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					Disciplina disciplina = new Disciplina(0/*cdProdutoServico*/,0/*cdCategoriaEconomica*/,disciplinas[i][1]/*nmProdutoServico*/,null/*txtProdutoServico*/,
														   null/*txtEspecificacao*/,null/*txtDadoTecnico*/,null/*txtPrazoEntrega*/,ProdutoServicoServices.TP_SERVICO/*tpProdutoServico*/,
														   disciplinas[i][0]/*idProdutoServico*/,disciplinas[i][3]/*sgProdutoServico*/,0/*cdClassificacaoFiscal*/,
														   0/*cdFabricante*/,0/*cdMarca*/,null/*nmModelo*/, 0/*cdNcm*/, null/*nrReferencia*/,0 /*cdCategoriaReceita*/, 0/*cdCategoriaDespesa*/, 
														   0/*vlServico*/,0/*cdDisciplinaTeoria*/, 0/*gnDisciplina (Teórica)*/, AreaConhecimentoServices.getById(disciplinas[i][2], connect).getCdAreaConhecimento(), 0/*tpClassificacao*/);	
					if(DisciplinaDAO.insert(disciplina, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Disciplinas");
					}
				}
			}
			
//			resultado = EducacensoImport.importPaisFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				Conexao.rollback(connect);
//				System.out.println("Erro: " + resultado.getMessage());
//				return resultado;
//			}
//			
//			resultado = EducacensoImport.importEstadoFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				Conexao.rollback(connect);
//				System.out.println("Erro: " + resultado.getMessage());
//				return resultado;
//			}
//			
//			resultado = EducacensoImport.importCidadeFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				Conexao.rollback(connect);
//				System.out.println("Erro: " + resultado.getMessage());
//				return resultado;
//			}
//			
//			resultado = EducacensoImport.importDistritoFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				Conexao.rollback(connect);
//				System.out.println("Erro: " + resultado.getMessage());
//				return resultado;
//			}
//			
//			resultado = EducacensoImport.importFormacaoSuperiorFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				Conexao.rollback(connect);
//				System.out.println("Erro: " + resultado.getMessage());
//				return resultado;
//			}
//			
//			Result resultado = EducacensoImport.importIesFromCSV(connect);
//			if(resultado.getCode() <= 0){
//				Conexao.rollback(connect);
//				System.out.println("Erro: " + resultado.getMessage());
//				return resultado;
//			}
			
			/*
			 * Formação do Professor
			 */
			String[][] formacao = new String[][] {{"01","Específico para Creche (0 a 3 anos)"},
													 {"02","Específico para Pré-Escola (4 a 5 anos)"},
													 {"03","Específico para anos iniciais do ensino fundamental"},
													 {"04","Específico para anos finais do ensino fundamental"},
													 {"05","Específico para ensino médio"},
													 {"06","Específico para educação de jovens e adultos"},
													 {"07","Específico para educação especial"},
					                                 {"08","Específico para Educação Indígena"},
					                                 {"09","Específico para Educação do campo"},
					                                 {"10","Específico para educação ambiental"},
					                                 {"11","Específico para educação em direitos humanos"},
					                                 {"12","Gênero e diversidade sexual"},
					                                 {"13","Direitos de criança e adolescente"},
					                                 {"14","Educação para as relações etnicorraciais e História e cultura Afro-Brasileira e Africana"},
					                                 {"15","Outros"}};
			for(int i=0; i<formacao.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_formacao_curso " +
						                                "WHERE id_ocde = \'"+formacao[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					FormacaoCurso formacaoCurso = new FormacaoCurso(0, 0, formacao[i][1], formacao[i][0], FormacaoCursoServices.TP_GRAU_LICENCIATURA, 0);	
					if(FormacaoCursoDAO.insert(formacaoCurso, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Formação do Docente");
					}
				}
			}
			
			/*
			 * Função
			 */
			String[][] funcoes = new String[][] {{"01","Professor"},
													 {"02","Auxiliar"},
													 {"03","Monitor"},
													 {"04","Tradutor de LIBRAS"}};
			for(int i=0; i<funcoes.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM srh_funcao " +
						                                "WHERE id_funcao = \'"+funcoes[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					com.tivic.manager.srh.Funcao funcao = new com.tivic.manager.srh.Funcao(0, 0/*cdEmpresa*/, funcoes[i][1], funcoes[i][0]);	
					if(com.tivic.manager.srh.FuncaoDAO.insert(funcao, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Função");
					}
				}
			}
			
			/*
			 * Tipo Documentação
			 */
			String[][] tipoDocumentacao = new String[][] {{"01","NIS"},
														  {"02","RG"},
														  {"03","Passaporte"},
														  {"04","Registro de Nascimento"},
														  {"05","Registro de Casamento"},
														  {"06","CPF"},
														  {"07","SUS"}};
			for(int i=0; i<tipoDocumentacao.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_tipo_documentacao " +
						                                "WHERE id_tipo_documentacao = \'"+tipoDocumentacao[i][0]+"\'").executeQuery();
				if(!rs.next())	{
//					TipoDocumentacao tipoDoc = new TipoDocumentacao(0, tipoDocumentacao[i][1], tipoDocumentacao[i][0]);
					TipoDocumentacao tipoDoc = new TipoDocumentacao(0, tipoDocumentacao[i][1], null, 0, tipoDocumentacao[i][0]);
					if(TipoDocumentacaoDAO.insert(tipoDoc, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Documentação");
					}
				}
			}
			
			/*
			 * Tipo de Deficiência
			 */
			String[][] tipoNecessidadeEspecial = new String[][] { {"01","Cegueira"},
														  {"02","Baixa Visão"},
														  {"03","Surdez"},
														  {"04","Deficiência Auditiva"},
														  {"05","Surdocegueira"},
														  {"06","Deficiência Física"},
														  {"07","Deficiência Intelectual"},
														  {"08","Deficiência Multipla"},
														  {"09","Autismo Infantil"},
														  {"10","Sindrome de Asperger"},
														  {"11","Sindrome de Rett"},
														  {"12","Transtorno Desintegrativo"},
														  {"13","Superdotação"}};
			for(int i=0; i<tipoNecessidadeEspecial.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_tipo_necessidade_especial " +
						                                "WHERE id_tipo_necessidade_especial = \'"+tipoNecessidadeEspecial[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					TipoNecessidadeEspecial tipoNecessidadeEspecial1 = new TipoNecessidadeEspecial(0, tipoNecessidadeEspecial[i][1], tipoNecessidadeEspecial[i][0], 0);	
					if(TipoNecessidadeEspecialDAO.insert(tipoNecessidadeEspecial1, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Deficiência");
					}
				}
			}
			
			/*
			 * Tipo de Recurso
			 */
			String[][] tipoRecurso = new String[][] { {"01","Auxílio Ledor"},
														  {"02","Auxílio Transcrição"},
														  {"03","Guia Intérprete"},
														  {"04","Intérprete de Libras"},
														  {"05","Deficiência Auditiva"},
														  {"06","Prova Ampliada Fonte 16"},
														  {"07","Prova Ampliada Fonte 20"},
														  {"08","Prova Ampliada Fonte 24"},
														  {"09","Prova em Braille"}};
			for(int i=0; i<tipoRecurso.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_prova " +
						                                "WHERE id_tipo_recurso = \'"+tipoRecurso[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					TipoRecursoProva tipoDoc = new TipoRecursoProva(0, tipoRecurso[i][1], tipoRecurso[i][0]);	
					if(TipoRecursoProvaDAO.insert(tipoDoc, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Recurso para fazer a prova");
					}
				}
			}
			
			/*
			 * Tipo de transporte
			 */
			String[][] tipoTransporte = new String[][] { {"01","Van"},
														  {"02","Microônibus"},
														  {"03","Ônibus"},
														  {"04","Bicicleta"},
														  {"05","Transporte de tração animal"},
														  {"06","Outros rodoviários"},
														  {"07","Embarcação até 5 pessoas"},
														  {"08","Embarcação de 5 até 15 pessoas"},
														  {"09","Embarcação de 15 até 35 pessoas"},
														  {"10","Embarcação a partir de 35 pessoas"},
														  {"11","Metrô"}};
			for(int i=0; i<tipoTransporte.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM fta_tipo_transporte " +
						                                "WHERE id_tipo_transporte = \'"+tipoTransporte[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					TipoTransporte tipoDoc = new TipoTransporte(0, tipoTransporte[i][1], tipoTransporte[i][0]);	
					if(TipoTransporteDAO.insert(tipoDoc, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Transporte");
					}
				}
			}
			
			/*
			 * Tipo de ocorrência
			 */
			String[][] tipoOcorrencia = new String[][] { {"01","Admissão"},
														  {"02","Ligação"},
														  {"03","Email Enviado"},
														  {"04","Transferência"},
														  {"05","Atribuição"},
														  {"06","Conclusão"},
														  {"07","Reabertura"},
														  {"08","Mudança de fase"},
														  {"09","Relevância"},
														  {"10","Aluno não possui documento"},
														  {"11","Escola não possui informação de documento do aluno"},
														  {"12","Solicitação de transferência"},
														  {"13","Conclusão de transferência"},
														  {"14","Solicitação de quadro de vagas"},
														  {"15","Confirmação de Matrícula"},
														  {"16","Remanejamento de Aluno"}};
			for(int i=0; i<tipoOcorrencia.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_tipo_ocorrencia " +
						                                "WHERE id_tipo_ocorrencia = \'"+tipoOcorrencia[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					com.tivic.manager.grl.TipoOcorrencia tipoDoc = new com.tivic.manager.grl.TipoOcorrencia(0, tipoOcorrencia[i][1], tipoOcorrencia[i][0], 0);	
					if(com.tivic.manager.grl.TipoOcorrenciaDAO.insert(tipoDoc, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Tipo de Ocorrência");
					}
				}
			}
			
			
//			String[] escolaridade = new String[] {"Fundamental Incompleto",
//												  "Fundamental Completo",
//												  "Ensino Médio Normal/Magistério",
//												  "Ensino Médio Normal/Magistério Indígena",
//												  "Ensino Médio",
//												  "Ensino Superior"};
//			
//			pstmt = connect.prepareStatement("SELECT * " +
//									 "FROM grl_escolaridade " +
//									 "WHERE id_escolaridade = ?");
//			for(int i=0; i<escolaridade.length; i++)	{
//				pstmt.setString(1, "" + (i+1));
//				if(!pstmt.executeQuery().next())	{
//					Escolaridade esc = new Escolaridade(0, escolaridade[i], "" + (i+1));
//					EscolaridadeDAO.insert(esc, connect);
//				}
//			}
			
			/*
			 * Tipo de alergia
			 */
			String[][] tipoAlergia = new String[][] { {"01","Poeira", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"02","Plantas", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"03","Amoxilina", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"04","Picada de Inseto", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"05","Abacaxi", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"06","Ovos", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"07","Dipirona", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"08","Buscopan", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"09","Prazil", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"10","Pêlo de Animal", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"11","Mofo", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"12","Lã", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"13","Macarrão", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"14","Salsicha", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"15","Remédios", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"16","Gelo", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"17","Corante", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"18","Gluten", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"19","Pelúcia", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"20","Amendoim", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"21","Leite e seus derivados", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"22","Gelo", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"23","Frutos do Mar", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"24","Milho", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"25","Nescau", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"26","Ibuprofeno", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"27","Chocolate", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"28","Dermatite", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"29","Salgadinho de Milho", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"30","Carne de Porco", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"31","Sabão", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"32","Gelado", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"33","Suco artificial", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"34","Suor", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"35","Cheiro Forte", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"36","Banana", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"37","Binotal", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"38","Cefalexina", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"39","Fectrin", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"40","Diclofenaco", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"41","Alimentos", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"42","Picada de Abelha", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"43","Tecido", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"44","Fumaça", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"45","Maracujá", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA},
													  {"46","Corticoide", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"47","Nimesulida", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"48","AAS", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"49","Sabonete", "" + AlergiaServices.TP_ALERGIA_OUTROS},
													  {"50","Mebendazol", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"51","Própolis", "" + AlergiaServices.TP_ALERGIA_MEDICAMENTOS},
													  {"52","Refrigerante", "" + AlergiaServices.TP_ALERGIA_ALIMENTICIA}
													};
			for(int i=0; i<tipoAlergia.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_alergia " +
						                                "WHERE id_alergia = \'"+tipoAlergia[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					com.tivic.manager.grl.Alergia tpAlergia = new com.tivic.manager.grl.Alergia(0, tipoAlergia[i][1], tipoAlergia[i][0], Integer.parseInt(tipoAlergia[i][2]));	
					if(com.tivic.manager.grl.AlergiaDAO.insert(tpAlergia, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Alergias");
					}
				}
			}
			
			
//			/*
//			 * Orgãos Emissores
//			 */
//			String[][] orgaosEmissores = new String[][] {{"SSP", "Secretaria de Seguranca Publica"},
//													 {"ABNC","Academia Brasileira de Neurocirurgia"},
//													 {"CGPI/DUREX/DPF","Coordenacao Geral de Policia de Imigracao da Policia Federal"},
//													 {"CGPI","Coordenacao-Geral de Privilegios e Imunidades"},
//													 {"CGPMAF","Coordenadoria Geral de Policia Maritima, Aeronautica e de Fronteiras"},
//													 {"CNIG","Conselho Nacional de Imigracao"},
//													 {"CNT","Carteira Nacional de Habilitacao"},
//					                                 {"COREN","Conselho Regional de Enfermagem"},
//					                                 {"CRA","Conselho Regional de Administracao"},
//					                                 {"CRAS","Conselho Regional de Assistentes Sociais"},
//					                                 {"CRB","Conselho Regional de Biblioteconomia"},
//					                                 {"CRC","Conselho Regional de Contabilidade"},
//					                                 {"CRE","Conselho Regional de Estatistica"},
//					                                 {"CREA","Conselho Regional de Engenharia Arquitetura e Agronomia"},
//					                                 {"CRECI","Conselho Regional de Corretores de Imoveis"},
//					                                 {"CREFIT","Conselho Regional de Fisioterapia e Terapia Ocupacional"},
//					                                 {"CRF","Conselho Regional de Farmacia"},
//					                                 {"CRM","Conselho Regional de Medicina"},
//					                                 {"CRMV","Conselho Regional de Medicina Veterinaria"},
//					                                 {"OMB","Ordem dos Musicos do Brasil"},
//					                                 {"CRN","Conselho Regional de Nutricao"},
//					                                 {"CRO","Conselho Regional de Odontologia"},
//					                                 {"CRPRE","Conselho Regional de Profissionais de Relacoes Publicas"},
//					                                 {"CRP","Conselho Regional de Psicologia"},
//					                                 {"CRQ","Conselho Regional de Quimica"},
//					                                 {"CRRC","Conselho Regional de Representantes Comerciais"},
//					                                 {"OAB","Ordem dos Advogados do Brasil"},
//					                                 {"CSC","Carteira Sede Carpina de Pernambuco"},
//					                                 {"CTPS","Carteira de Trabalho e Previdencia Social"},
//					                                 {"DIC","Diretoria de Identificacao Civil"},
//					                                 {"DIREX","Diretoria-Executiva"},
//					                                 {"DPMAF","Divisao de Policia Maritima, Area e de Fronteiras"},
//					                                 {"DPT","Departamento de Policia Tecnica Geral"},
//					                                 {"DST","Programa Municipal DST/Aids"},
//					                                 {"FGTS","Fundo de Garantia do Tempo de Servico"},
//					                                 {"FIPE","Fundacao Instituto de Pesquisas Economicas"},
//					                                 {"FLS","Fundacao Lyndolpho Silva"},
//					                                 {"GOVGO","Governo do Estado de Goias"},
//					                                 {"I CLA","Carteira de Identidade Classista"},
//					                                 {"IFP","Instituto Felix Pacheco"},
//					                                 {"IGP","Instituto Geral de Pericias"},
//					                                 {"IICCECF/RO","Instituto de Identificacao Civil e Criminal Engracia da Costa Francisco de Rondonia"},
//					                                 {"IIMG","Inter-institutional Monitoring Group"},
//					                                 {"IML","Instituto Medico-Legal"},
//					                                 {"IPC","Indice de Precos ao Consumidor"},
//					                                 {"IPF","Instituto Pereira Faustino"},
//					                                 {"MAE","Ministerio da Aeronautica"},
//					                                 {"MEX","Ministerio do Exercito"},
//					                                 {"MMA","Ministerio da Marinha"},
//					                                 {"PCMG","Policia Civil do Estado de Minas Gerais"},
//					                                 {"PMMG","Policia Militar do Estado de Minas Gerais"},
//					                                 {"DPF","Policia Federal"},
//					                                 {"POM","Policia Militar"},
//					                                 {"SDS","Secretaria de Defesa Social (Pernambuco)"},
//					                                 {"SNJ","Secretaria Nacional de Justica / Departamento de Estrangeiros"},
//					                                 {"SECC","Secretaria de Estado da Casa Civil"},
//					                                 {"SEJUSP","Secretaria de Estado de Justica e Seguranca Publica - Mato Grosso"},
//					                                 {"EST","Carteira de Estrangeiro"},
//					                                 {"SESP","Secretaria de Estado da Seguranca Publica do Parana"},
//					                                 {"SJS","Secretaria da Justica e Seguranca"},
//					                                 {"SJTC","Secretaria da Justica do Trabalho e Cidadania"},
//					                                 {"SJTS","Secretaria da Justica do Trabalho e Seguranca"},
//					                                 {"SPTC","Secretaria de Policia Tecnico-Cientifica"}};
//			for(int i=0; i<orgaosEmissores.length; i++)	{
//				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_orgao_emissor " +
//						                                "WHERE id_orgao_emissor = \'"+orgaosEmissores[i][0]+"\'").executeQuery();
//				if(!rs.next())	{
//					OrgaoEmissor orgaoEmissor = new OrgaoEmissor(0, orgaosEmissores[i][1], orgaosEmissores[i][0]);	
//					if(OrgaoEmissorDAO.insert(orgaoEmissor, connect) <= 0){
//						Conexao.rollback(connect);
//						return new Result(-1, "Erro ao cadastrar Orgão Emissor");
//					}
//				}
//			}
//			
			
			/*
			 * Area de conhecimento da Formação do Professor
			 */
			String[][] formacaoAreaConhecimentoArray = new String[][] {{"01","Educação"},
													 {"02","Humanidades e Artes"},
													 {"03","Ciências Sociais, Negócios e Direitos"},
													 {"04","Ciências, Matemática e Computação"},
													 {"05","Engenharia, Produção e Construção"},
													 {"06","Agricultura e Veterinária"},
													 {"07","Saúde e Bem-estar Social"},
					                                 {"08","Serviços"},
					                                 {"09","Outros"}};
			for(int i=0; i<formacaoAreaConhecimentoArray.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM acd_formacao_area_conhecimento " +
						                                "WHERE id_area = \'"+formacaoAreaConhecimentoArray[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					FormacaoAreaConhecimento formacaoAreaConhecimento = new FormacaoAreaConhecimento(0, formacaoAreaConhecimentoArray[i][1], formacaoAreaConhecimentoArray[i][0]);	
					if(FormacaoAreaConhecimentoDAO.insert(formacaoAreaConhecimento, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Área de Conhecimento da Formação do Docente");
					}
				}
			}
			
			
			/*
			 * Tipo de Admissao - Censo
			 */
			String[][] tipoAdmissaoArray = new String[][] {{"01","Concursado/efetivo/estável"},
													 {"02","Contrato temporário"},
													 {"03","Contrato terceirizado"},
													 {"04","Contrato CLT"}};
			for(int i=0; i<tipoAdmissaoArray.length; i++)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM srh_tipo_admissao " +
						                                "WHERE id_tipo_admissao = \'"+tipoAdmissaoArray[i][0]+"\'").executeQuery();
				if(!rs.next())	{
					TipoAdmissao tipoAdmissao = new TipoAdmissao(0, tipoAdmissaoArray[i][1], tipoAdmissaoArray[i][0], 0, 1);	
					if(TipoAdmissaoDAO.insert(tipoAdmissao, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar tipo de admissão");
					}
				}
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_matriz", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFind = EmpresaDAO.find(criterios, connect);
			int cdEmpresa = 0;
			if(!rsmFind.next()){
				Instituicao instituicao = new Instituicao(0, 0, 0, "SECRETARIA DE EDUCAÇÃO", null, null, null, null, null, Util.getDataAtual(), PessoaServices.TP_JURIDICA, null, 
														PessoaServices.ST_ATIVO, null, null, null, 0, 
														"00", 0, 0, null, null, "SECRETARIA DE EDUCAÇÃO", null, null, 0, null, 0, 0/*tpEmpresa*/, null, 1, null, 
														"00", 0, null, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, 0, null);
				
				PessoaEndereco endereco = new PessoaEndereco(0, 0, null, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, 0, 1);
				
				if(InstituicaoServices.save(instituicao, null/*educacenso*/, endereco, connect).getCode() <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar instituição Secretaria");
				}	
				
				cdEmpresa = instituicao.getCdInstituicao();
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date date = formatter.parse("01/01/" + String.valueOf(Util.getDataAtual().get(Calendar.YEAR)-1));
				GregorianCalendar dtInicioLetivo = new GregorianCalendar();
				dtInicioLetivo.setTime(date);
				date = formatter.parse("31/12/" + String.valueOf(Util.getDataAtual().get(Calendar.YEAR)-1));
				GregorianCalendar dtFimLetivo = new GregorianCalendar();
				dtFimLetivo.setTime(date);
				InstituicaoPeriodo instituicaoPeriodo = new InstituicaoPeriodo(0, cdEmpresa, String.valueOf(Util.getDataAtual().get(Calendar.YEAR)-1), dtInicioLetivo, dtFimLetivo, 0, InstituicaoPeriodoServices.ST_ATUAL, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 0);
				
				if(InstituicaoPeriodoServices.save(instituicaoPeriodo, 1, connect).getCode() <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir relação entre instituição e periodo");
				}
				
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_INSTITUICAO_SECRETARIA_MUNICIPAL").getCdParametro(), String.valueOf(cdEmpresa), null, connect);
				
				TipoCamada tipoCamada = new TipoCamada(0, "EDUCAÇÃO", "Camada da área de educação", "EDU");
				tipoCamada.setCdTipo(TipoCamadaDAO.insert(tipoCamada, connect));
				if(tipoCamada.getCdTipo() <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Tipo de Camada principal");
				}
				
				Camada camada = new Camada(0, "ESCOLAS MUNICIPAIS", "Camada com as escolas do município", tipoCamada.getCdTipo(), 0, null, 0, 0);
				
				camada.setCdCamada(CamadaDAO.insert(camada, connect));
				if(camada.getCdCamada() <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Camada principal");
				}
				
			}
			else{
				cdEmpresa = rsmFind.getInt("cd_empresa");
				
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_INSTITUICAO_SECRETARIA_MUNICIPAL").getCdParametro(), String.valueOf(cdEmpresa), null, connect);
			}
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_login", "tivic", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmUsuario = UsuarioDAO.find(criterios, connect);
			int cdUsuario = 0;
			if(rsmUsuario.next())
				cdUsuario = rsmUsuario.getInt("cd_usuario");
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_sistema", "MMng", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmSistema = SistemaDAO.find(criterios, connect);
			int cdSistema = 0;
			if(rsmSistema.next()){
				cdSistema = rsmSistema.getInt("cd_sistema");
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_modulo", "acd", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmModulo = ModuloDAO.find(criterios, connect);
			int cdModulo = 0;
			if(rsmModulo.next()){
				cdModulo = rsmModulo.getInt("cd_modulo");
			}
			
			Modulo modulo = ModuloDAO.get(cdModulo, cdSistema, connect);
			modulo.setLgAtivo(1);
			if(ModuloDAO.update(modulo, connect) <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar Modulo");
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_usuario", "" + cdUsuario, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_modulo", "" + cdModulo, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_sistema", "" + cdSistema, ItemComparator.EQUAL, Types.INTEGER));
			if(!UsuarioModuloDAO.find(criterios, connect).next()){
				UsuarioModulo usuarioModulo = new UsuarioModulo(cdUsuario, cdModulo, cdSistema, 1);
				if(UsuarioModuloDAO.insert(usuarioModulo, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Usuario Modulo");
				}
			}
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_usuario", "" + cdUsuario, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_modulo", "" + cdModulo, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_sistema", "" + cdSistema, ItemComparator.EQUAL, Types.INTEGER));
			if(!UsuarioModuloEmpresaDAO.find(criterios, connect).next()){
				UsuarioModuloEmpresa usuarioModuloEmpresa = new UsuarioModuloEmpresa(cdUsuario, cdEmpresa, cdModulo, cdSistema);
				if(UsuarioModuloEmpresaDAO.insert(usuarioModuloEmpresa, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Usuario Modulo Empresa");
				}
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_vinculo", "PROFESSOR", ItemComparator.EQUAL, Types.VARCHAR));
			int cdVinculo = 0;
			ResultSetMap rsmVinculo = VinculoDAO.find(criterios, connect);
			if(!rsmVinculo.next()){
				Vinculo vinculo = new Vinculo(0, "PROFESSOR", 0, 0, 0, 0);
				cdVinculo = VinculoDAO.insert(vinculo, connect);
				if(cdVinculo <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Vinculo Professor");
				}
			}
			else{
				cdVinculo = rsmVinculo.getInt("cd_vinculo");
			}
			ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_VINCULO_PROFESSOR").getCdParametro(), String.valueOf(cdVinculo), null, connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_vinculo", "ALUNO", ItemComparator.EQUAL, Types.VARCHAR));
			rsmVinculo = VinculoDAO.find(criterios, connect);
			if(!rsmVinculo.next()){
				Vinculo vinculo = new Vinculo(0, "ALUNO", 0, 0, 0, 0);
				cdVinculo = VinculoDAO.insert(vinculo, connect);
				if(cdVinculo <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Vinculo Aluno");
				}
			}
			else{
				cdVinculo = rsmVinculo.getInt("cd_vinculo");
			}
			ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_VINCULO_ALUNO").getCdParametro(), String.valueOf(cdVinculo), null, connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_vinculo", "INSTITUIÇÃO ACADÊMICA", ItemComparator.EQUAL, Types.VARCHAR));
			rsmVinculo = VinculoDAO.find(criterios, connect);
			if(!rsmVinculo.next()){
				Vinculo vinculo = new Vinculo(0, "INSTITUIÇÃO ACADÊMICA", 0, 0, 0, 0);
				cdVinculo = VinculoDAO.insert(vinculo, connect);
				if(cdVinculo <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Vinculo Instituição Acadêmica");
				}
			}
			else{
				cdVinculo = rsmVinculo.getInt("cd_vinculo");
			}
			ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_VINCULO_INSTITUICAO_ACADEMICA").getCdParametro(), String.valueOf(cdVinculo), null, connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_vinculo", "CONDUTOR", ItemComparator.EQUAL, Types.VARCHAR));
			rsmVinculo = VinculoDAO.find(criterios, connect);
			if(!rsmVinculo.next()){
				Vinculo vinculo = new Vinculo(0, "CONDUTOR", 0, 0, 0, 0);
				cdVinculo = VinculoDAO.insert(vinculo, connect);
				if(cdVinculo <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar Vinculo Condutor");
				}
			}
			else{
				cdVinculo = rsmVinculo.getInt("cd_vinculo");
			}
			ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_VINCULO_CONDUTOR").getCdParametro(), String.valueOf(cdVinculo), null, connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_tipo_documentacao", "Registro de Nascimento", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmTipoDocumentacao = TipoDocumentacaoDAO.find(criterios, connect);
			if(rsmTipoDocumentacao.next()){
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_TIPO_DOCUMENTACAO_NASCIMENTO").getCdParametro(), String.valueOf(rsmTipoDocumentacao.getInt("cd_tipo_documentacao")), null, connect);
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_tipo_documentacao", "Registro de Casamento", ItemComparator.EQUAL, Types.VARCHAR));
			rsmTipoDocumentacao = TipoDocumentacaoDAO.find(criterios, connect);
			if(rsmTipoDocumentacao.next()){
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_TIPO_DOCUMENTACAO_CASAMENTO").getCdParametro(), String.valueOf(rsmTipoDocumentacao.getInt("cd_tipo_documentacao")), null, connect);
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_tipo_periodo", InstituicaoEducacensoServices.TP_PERIODO_LETIVO, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmTipoPeriodo = TipoPeriodoDAO.find(criterios, connect);
			if(rsmTipoPeriodo.next()){
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_TIPO_PERIODO_LETIVO").getCdParametro(), String.valueOf(rsmTipoPeriodo.getInt("cd_tipo_periodo")), null, connect);
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_tipo_dependencia", "Sala de Aula", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmTipoDependencia = TipoDependenciaDAO.find(criterios, connect);
			if(rsmTipoDependencia.next()){
				ParametroServices.updateValueOfParametro(ParametroServices.getByName("CD_TIPO_DEPENDENCIA_SALA").getCdParametro(), String.valueOf(rsmTipoDependencia.getInt("cd_tipo_dependencia")), null, connect);
			}
			
			connect.commit();
			
			return new Result(1);
		}
		catch(Exception e)	{
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro geral");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}

