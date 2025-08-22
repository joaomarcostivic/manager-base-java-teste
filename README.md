# Sistema etransito (legado)

### Descrição

Core legado do sistema etransito da TIVIC

### Instalações necessárias

1. JDK 1.8
2. Tomcat 8.5
3. Eclipse 2019-12 (Java EE)

### Ambiente de desenvolvimento

1. Na IDE Eclipse, vá em _File_ > _New_ > _Dynamic Web Project_.
2. Defina o nome e caminho do projeto, lembrando de definir o _Tomcat 8_ no _Target runtime_ ([Imagem 1](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/instalacao/001.png)). Em seguida, clique no botão _Next_.
3. Defina o _Default output folder_, por padrão `web\WEB-INF\classes` ([Imagem 2](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/instalacao/002.png)). Em seguida, clique no botão _Next_.
4. Defina o _Content directory_ como `web`. Em seguida, clique no botão _Finish_ ([Imagem 3](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/instalacao/003.png)).
5. Clique com o botão direito no projeto recém criado e vá em _Properties_ > _Validation_ e selecione _Enable project specific settings_ e _Suspend all validators_ ([Imagem 4](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/instalacao/004.png)).
6. Também em _Properties_, vá em _Project Facets_ e selecione a versão `1.8` do Java ([Imagem 5](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/instalacao/005.png)).
7. Ainda em _Properties_, altere o _encoding_ para `UTF-8`([Imagem 6](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/instalacao/006.png))
8. Fazer uma cópia do arquivo manager.conf e colocar na pasta src/com/tivic/manager/conf/

### Execução - Ambiente Tomcat

1. Fazer um arquivo [etransito.xml](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/contexto/etransito.xml)
2. Colocar arquivo dentro do Tomcat em: conf\Catalina\localhost
3. Após reiniciar o Tomcat, acessar o sistema através do navegador: `http://localhost:8080/etransito/`

### Execução - Ambiente Docker

1. Criar uma pasta dentro do servidor de produção com a seguinte hierarquia:
```
--etransito
    --contexto
    --conf
```
2. Criar uma cópia do executável da aplicação (pasta web) do projeto, renomea-la com o nome do contexto e inseri-la na pasta "etransito"
3. Criar um arquivo [Dockerfile](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/docker/Dockerfile), fazendo as alterações necessárias
4. Criar um arquivo [docker-compose.yml](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/docker/docker-compose.yml), fazendo as alterações necessárias
5. Criar um arquivo etransito.xml e coloca-lo na pasta "contexto", com a seguinte configuração:
```xml
<Context docBase="/usr/local/etransito"
         antiResourceLocking="false" 
         antiJARLocking="false" 
         reloadable="true">
</Context>
```
6. Criar um arquivo [manager.conf](https://github.com/tivic-dev/docs/blob/master/resources/Readme/manager-base-java/conf/manager.conf) e coloca-lo na pasta "conf", fazendo as alterações necessárias
7. Executar o seguinte comando: `docker-compose up -d`
8. Após o término, acessar o sistema no navegador: `http://localhost:<PORT>/` (onde PORT será escolhido no arquivo docker-compose.yml)

### API

Para consultar os recursos disponíveis na API - v2, acesse: `http://localhost:8080/etransito/swagger`.
