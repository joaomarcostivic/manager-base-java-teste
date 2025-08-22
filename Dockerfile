FROM openjdk:8-jdk AS builder

# Defina as variáveis de ambiente no contêiner
ENV CATALINA_OUT /usr/local/tomcat/logs/catalina.out

# Instalar o Apache Ant
RUN apt-get update && apt-get install -y ant

# Definir o diretório de trabalho
WORKDIR /app

# Copiar os arquivos do projeto para o contêiner
COPY . .

# Criar diretório para o arquivo de configuração (caso não exista)
RUN mkdir -p src/com/tivic/manager/conf

# Copiar o arquivo de configuração (se estiver fora do contexto atual)
COPY manager.conf src/com/tivic/manager/conf/

# Executar o build com ant usando a biblioteca ecj
RUN ant -lib web/WEB-INF/compile/ecj-4.6.1.jar build

# Empacotar a aplicação como WAR
RUN mkdir -p deploy && \
    cd web && \
    jar -cvf ../deploy/manager-base-java.war *

# Segunda etapa, usando Tomcat para executar a aplicação
FROM tomcat:8.5-jre8

# Remover aplicativos padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Configurar o Tomcat com as propriedades solicitadas
# RUN sed -i 's/unpackWARs="true"/unpackWARs="true" antiResourceLocking="false" antiJARLocking="false" reloadable="true"/' /usr/local/tomcat/conf/server.xml

# Definir uma variável de ambiente para o contexto da aplicação (padrão: ROOT)
ENV APP_CONTEXT=ROOT

# Script de inicialização para configurar o contexto da aplicação dinamicamente
COPY entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

# Copiar o arquivo WAR para a pasta webapps do Tomcat
COPY --from=builder /app/deploy/manager-base-java.war /usr/local/tomcat/webapps/

# Prepara os diretórios de trabalho
RUN mkdir -p /tivic/work/ && chmod 767 -R /tivic/work/

# Expor a porta 8080
EXPOSE 8080

# Usar o script de inicialização personalizado
ENTRYPOINT ["entrypoint.sh"]