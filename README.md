Aplicação para encurtar URL.

# Linguagem Utilizada
- Java

# Principais Bibliotecas Utilizadas
- Spring Boot
- Redis
- Commons-lang3
- Junit (Testes unitários e integrados)
- Junitbenchmarks (Para testes de performance)

A lista completa e respectivas poderá ser observada em build.gralde na raíz do projeto.

# Automação de Compilação 
- Gradle 5+ (https://gradle.org/releases/)

# Ferramenta para desenvolvimento - Opcional
- Spring Tool Suite 4 (STS)

# Instalação
- 1 - Baixe o projeto do Github e importe-o como projeto Gradle para o STS ou Eclipse.
- 2 - Ao importar, o Eclipse/STS irá realizar o download das dependências do projeto. Certifique-se que não possui FIREWALL para possibilitar o download.
- 3 - Por padrão, a execução está configurada para a porta 8080, caso deseje alterar, modifique os arquivos application.properties no projeto.
- 4 - Em seguida verificaremos se as dependências estão corretas, clique com o botão direito sobre a classe do pacote br.com.rezende.shortener.app.URLTransformApplication > run as.. > Spring Boot Application e clique em Run.
Se aparecer a mensagem de autorização de excução do Redis significa que o projeto está compilando e poderá ser executado.

- Para encutar uma url, enviar um POST para o endereço: http://localhost:8080/tinier com o Body do tipo "application/json":
{
	"url": "URL PARA ENCURTAR"
}

Como resposta deverá ter um endereço semelhante: localhost:8080/bt2a0

Ao acessar o endereço localhost:8080/bt2a0 no browser, o mesmo deverá redirecionar para o endereço da URL que foi encurtada.

Exemplo:
POST:
{
 "url": "https://www.facebook.com/photo.php?fbid=1400530880098368&set=gm.2464020093769913&type=3&eid=ARAgyR4ejpShRsmH4R-Z0KJKwF6P04DLNdTmh_B1fVkqz9xCB25w-eStieBqUfLmJkjjEHYptV9DnXK8"
}

Resposta:
localhost:8080/bt2a0

Ao acessar o endereço: localhost:8080/bt2a0, redirecionado para: https://www.facebook.com/photo.php?fbid=1400530880098368&set=gm.2464020093769913&type=3&eid=ARAgyR4ejpShRsmH4R-Z0KJKwF6P04DLNdTmh_B1fVkqz9xCB25w-eStieBqUfLmJkjjEHYptV9DnXK8

# Build
Para executar o build, tenha certeza que o gradle está instalado corretamente. Para verificar a versão instalada execute o comando no Prompt: 
- gradle -v

Para o build, execução dos testes unitários e integrados:
- gradle clean build

Após a execução, e saída de status: BUILD SUCCESSFUL, o relatório de testes poderá ser visualizado no diretório:
- <PROJECT_HOME>/build/reports/tests/test/index.html

- Informações de Performance no relatório de testes botão "Standard output". Ao final informações configuradas para mensurar dados analiticos de tempo, memória, dentre outras. 

E o relatório de cobertura no diretório:
- <PROJECT_HOME>/build/reports/jacocoHtml/index.html
- <PROJECT_HOME>/build/reports/jacoco/test/jacocoTestReport.csv
- <PROJECT_HOME>/build/reports/jacoco/test/jacocoTestReport.xml

# Observação
Ao interromper a execução da aplicação, verifique se o Java e o Redis foram interrompidos corretamente, caso estejam em execução, elimine o(s) processo(s).
Esta etapa será obrigatório para a reexecução do projeto.

Testes de integração estão utilizando o Junitbenchmarks.
