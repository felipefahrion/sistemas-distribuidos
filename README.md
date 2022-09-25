# Trabalho Prático 1

<div style="text-align: justify">

    O primeiro trabalho prático da disciplina tem como objetivo aprofundar o conhecimento relacionado aos tópicos abordados até o momento, que incluem o modelo cliente/servidor e P2P utilizando programação com Sockets.
</div>

<div style="text-align: justify">

    A sua tarefa consiste na implementação de um sistema P2P básico, que deve ser organizado como uma arquitetura centralizada, onde o controle de toda a aplicação (lógica e estado) é concentrado em um computador servidor. Um único programa deve ser utilizado, e o mesmo pode ser configurado em um dos dois modos de operação (servidor / peer). Para isso, pode-se passar essa informação como parâmetro durante a carga do programa, juntamente com outras informações de configuração. As seguintes funcionalidades devem ser implementadas:
</div>

<div style="text-align: justify">

    - [x] Os peers devem se registrar no servidor P2P para poderem resalizar a troca de informações.
</div>

<div style="text-align: justify">

    - [x] Durante o registro, um cliente informa os recursos disponíveis. Os recursos são definidos por um diretório com diversos arquivos, que serão abertos um a um e terão sua hash calculada. Para cada arquivo, forneça ao servidor o seu nome e sua hash (metadados).
</div>

<div style="text-align: justify">

    - [x] O servidor associa os metadados de cada recurso em uma estrutura de dados adequada, juntamente com informações sobre a origem de cada recurso (identificação do peer, como IP e porta).
</div>

<div style="text-align: justify">

    - [x] Os peers podem solicitar uma lista de recursos (nomes e hashes) ou um recurso específico ao servidor. É necessária a definição de um critério de busca, e não apenas a lista completa dos recursos.
</div>

<div style="text-align: justify">

    - [ ] Ao solicitar um recurso ao servidor, o cliente recebe a metadados apenas, incluindo sua localização (outro peer). O peer deve então realizar a comunicação P2P para transferência do recurso.
</div>

<div style="text-align: justify">

    - [x] O servidor é responsável por manter a estrutura da rede e do estado dos peers. Para isso, os peers devem enviar mensagens periódicas ao servidor (a cada 10 segundos). Caso um peer deixe de enviar uma mensagem, o mesmo deve ser removido e seus metadados excluídos.
</div>

<div style="text-align: justify">

    Para o desenvolvimento, é sugerido que os alunos utilizem uma rede com topologia definida, e que sejam  realizados testes com um número suficiente de máquinas (pelo menos 3) ou VMs.
</div>

<div style="text-align: justify">

    O trabalho deve ser realizado em grupos de 2 ou 3 integrantes. Qualquer linguagem de programação pode ser utilizada, desde que as abstrações para comunicação entre processos sejam e equivalentes aos exemplos apresentados em sala de aula (modelo de comunicação utilizando Sockets). Para a entrega é solicitado o código fonte da aplicação. O trabalho será apresentado em sala de aula pelo grupo no dia 04/10.
</div>
# Como rodar

- Ter Java instalado e configurado.

```
make
```

Rodar o servidor: 

```
java p2pServer <ip_address>
```

Por default a execução do servidor já ocorre na porta 9000

Para rodar um peer: 


- Registry: 

```
java p2pPeer 127.0.0.1 "registry pipo docs/a1.txt,docs/a2.txt,docs/a3.txt" 9001
```

- Query: 

```
query a1.txt 192.168.68.101
```
