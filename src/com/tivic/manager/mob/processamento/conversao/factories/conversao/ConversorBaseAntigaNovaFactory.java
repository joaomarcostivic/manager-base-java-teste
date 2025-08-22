package com.tivic.manager.mob.processamento.conversao.factories.conversao;

import com.tivic.manager.adapter.base.antiga.agente.AgenteRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.ait.AitRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.aitImagem.AitImagemRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.aitmovimento.AitMovimentoRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.cidade.CidadeRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.especieveiculo.EspecieVeiculoRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.infracao.InfracaoRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.infracaoobservacao.InfracaoObservacaoRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.marcamodelo.MarcaModeloRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.ocorrencia.OcorrenciaRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.parametro.ParametroOldRepositoryDAO;
import com.tivic.manager.adapter.base.antiga.talonario.TalonarioRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.tipoveiculo.TipoVeiculoRepositoryOldDAO;
import com.tivic.manager.adapter.base.antiga.usuario.UsuarioRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.fta.especieveiculo.EspecieVeiculoRepository;
import com.tivic.manager.fta.especieveiculo.EspecieVeiculoRepositoryDAO;
import com.tivic.manager.fta.marcamodelo.MarcaModeloRepository;
import com.tivic.manager.fta.marcamodelo.MarcaModeloRepositoryDAO;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepository;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepositoryDAO;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.cidade.CidadeRepositoryDAO;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.mob.agente.AgenteRepositoryDAO;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.AitRepositoryDAO;
import com.tivic.manager.mob.ait.sync.builders.ParametroSyncDTOBuilder;
import com.tivic.manager.mob.ait.sync.builders.ParametroSyncDTOBuilderBase;
import com.tivic.manager.mob.ait.sync.builders.ParametroSyncDTOBuilderOld;
import com.tivic.manager.mob.aitimagem.repositories.AitImagemRepositoryDAO;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepositoryDAO;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.infracao.InfracaoRepositoryDAO;
import com.tivic.manager.mob.infracao.observacao.InfracaoObservacaoRepository;
import com.tivic.manager.mob.infracao.observacao.InfracaoObservacaoRepositoryDAO;
import com.tivic.manager.mob.ocorrencia.OcorrenciaRepository;
import com.tivic.manager.mob.ocorrencia.OcorrenciaRepositoryDAO;
import com.tivic.manager.mob.talonario.TalonarioRepository;
import com.tivic.manager.mob.talonario.TalonarioRepositoryDAO;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.manager.seg.usuario.repositories.UsuarioRepositoryDAO;

public class ConversorBaseAntigaNovaFactory {

    private final boolean isBaseAntiga;
    
    public ConversorBaseAntigaNovaFactory() {
    	isBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
    }

    private <T> T getRepository(T baseAntiga, T baseNova) {
        return isBaseAntiga ? baseAntiga : baseNova;
    }

    public AitRepository getAitRepository() throws Exception {
        return getRepository(new AitRepositoryOldDAO(), new AitRepositoryDAO());
    }

    public AitMovimentoRepository getAitMovimentoRepository() throws Exception {
        return getRepository(new AitMovimentoRepositoryOldDAO(), new AitMovimentoRepositoryDAO());
    }

    public IAitImagemRepository getAitImagemRepository() throws Exception {
        return getRepository(new AitImagemRepositoryOldDAO(), new AitImagemRepositoryDAO());
    }

    public IUsuarioRepository getUsuarioRepository() throws Exception {
        return getRepository(new UsuarioRepositoryOldDAO(), new UsuarioRepositoryDAO());
    }

    public AgenteRepository getAgenteRepository() throws Exception {
        return getRepository(new AgenteRepositoryOldDAO(), new AgenteRepositoryDAO());
    }

    public TalonarioRepository getTalonarioRepository() throws Exception {
        return getRepository(new TalonarioRepositoryOldDAO(), new TalonarioRepositoryDAO());
    }

    public IParametroRepository getParametroRepository() throws Exception {
        return getRepository(new ParametroOldRepositoryDAO(), new ParametroRepositoryDAO());
    }
    
    public InfracaoRepository getInfracaoRepository() throws Exception {
        return getRepository(new InfracaoRepositoryOldDAO(), new InfracaoRepositoryDAO());
    }
    
    public OcorrenciaRepository getOcorrenciaRepository() throws Exception {
        return getRepository(new OcorrenciaRepositoryOldDAO(), new OcorrenciaRepositoryDAO());
    }
    
    public EspecieVeiculoRepository getEspecieVeiculoRepository() throws Exception {
        return getRepository(new EspecieVeiculoRepositoryOldDAO(), new EspecieVeiculoRepositoryDAO());
    }
    
    public MarcaModeloRepository getMarcaModeloRepository() throws Exception {
        return getRepository(new MarcaModeloRepositoryOldDAO(), new MarcaModeloRepositoryDAO());
    }
    
    public InfracaoObservacaoRepository getInfracaoObservacaoRepository() throws Exception {
        return getRepository(new InfracaoObservacaoRepositoryOldDAO(), new InfracaoObservacaoRepositoryDAO());
    }
    
    public ParametroSyncDTOBuilderBase getParametroSyncDTOBuilder() throws Exception {
        return getRepository(new ParametroSyncDTOBuilderOld(), new ParametroSyncDTOBuilder());
    }
    
    public CidadeRepository getCidadeRepository() throws Exception {
        return getRepository(new CidadeRepositoryOldDAO(), new CidadeRepositoryDAO());
    }
    
    public TipoVeiculoRepository getTipoVeiculoRepository() throws Exception {
    	return getRepository(new TipoVeiculoRepositoryOldDAO(), new TipoVeiculoRepositoryDAO());
    }
}