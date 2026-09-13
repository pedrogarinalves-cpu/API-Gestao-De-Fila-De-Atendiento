# API Gestão de Fila de Atendimento

API para gerenciamento de filas de atendimento, desenvolvida em Java com Spring Boot. O projeto começou pela modelagem do domínio em Java puro, antes da camada de aplicação, garantindo que a lógica de negócio fosse sólida, coesa e testável de forma isolada.

## Status do projeto

**v1 — em desenvolvimento.** A camada de domínio (Java puro) está completa e validada por testes manuais. A camada de API (REST) já está implementada e a aplicação sobe com sucesso. Os endpoints ainda serão testados via Postman. Persistência ainda não implementada.

## Sobre o projeto

O sistema simula o funcionamento de uma fila de atendimento: um cliente entra na fila e recebe uma senha, um atendente chama o próximo da fila, e o atendimento pode ser finalizado ou cancelado a qualquer momento. Toda a lógica de negócio foi construída primeiro em Java puro (sem depender do framework), para reforçar boas práticas de orientação a objetos antes de expor tudo via API.

## Estrutura do projeto

```
com.gestaodeatendimento
├── core/
│   ├── model/          → Entidades de domínio (Cliente, Atendimento, StatusAtendimento)
│   ├── service/         → Lógica de negócio (FilaService, GeradorDeSenha)
│   └── exception/       → Exceções de domínio customizadas
└── api/
    ├── dto/             → Objetos de entrada/saída da API (EntrarNaFilaRequest, AtendimentoResponse, PosicaoResponse)
    └── controller/       → FilaController, expondo a lógica de negócio via endpoints REST
```

## Domínio

- **Cliente**: representa a pessoa que entra na fila (id, nome, horário de chegada). Classe imutável.
- **Atendimento**: representa o "ticket" de um cliente na fila, com número de senha, status e horários de entrada/início/fim. Expõe métodos de transição de estado (`iniciarAtendimento`, `finalizar`, `cancelar`) em vez de setters genéricos.
- **StatusAtendimento**: enum com os estados possíveis (`AGUARDANDO`, `EM_ATENDIMENTO`, `FINALIZADO`, `CANCELADO`).
- **FilaService**: orquestra toda a lógica de negócio — entrar na fila, chamar o próximo, finalizar, cancelar, consultar posição e listar a fila atual. Anotado com `@Service` para ser gerenciado pelo Spring.
- **GeradorDeSenha**: responsável por gerar números de senha sequenciais e únicos.
- **Exceções de domínio**: `FilaVaziaException` e `AtendimentoNaoEncontradoException`, lançadas em situações específicas do negócio, em vez de erros genéricos.

## API REST

O `FilaController` expõe os seguintes endpoints, mapeando diretamente as operações do `FilaService`:

| Método | Endpoint | Ação |
|---|---|---|
| POST | `/fila` | Cliente entra na fila (recebe nome, retorna atendimento com senha) |
| POST | `/fila/proximo` | Chama o próximo atendimento da fila |
| PUT | `/fila/{numeroSenha}/finalizar` | Finaliza um atendimento em andamento |
| DELETE | `/fila/{numeroSenha}` | Cancela um atendimento |
| GET | `/fila/{numeroSenha}/posicao` | Consulta a posição de um atendimento na fila |
| GET | `/fila` | Lista todos os atendimentos aguardando na fila |

DTOs de entrada e saída foram usados para não expor as classes de domínio diretamente na API, mantendo o contrato da API independente da estrutura interna do domínio.

## Decisões técnicas

- Uso de `Queue` (FIFO) para representar a fila de espera, respeitando a ordem natural de chegada.
- Uso de `Map` para busca rápida de atendimentos em andamento por número de senha.
- Encapsulamento de transições de estado dentro da própria entidade `Atendimento`, evitando setters genéricos que permitiriam estados inválidos.
- `equals`/`hashCode` de entidades baseados apenas no identificador único (`id` ou `numeroSenha`), já que representam identidade, não valor.
- Separação entre DTOs de entrada (mutáveis, com construtor vazio, pensados para o Jackson desserializar JSON) e DTOs de saída (imutáveis, montados pelo próprio código a partir do domínio).
- Injeção de dependência via construtor no `FilaController`, em vez de instanciar o `FilaService` manualmente.

## Testes do domínio (Java puro)

Antes de integrar com Spring, toda a lógica de negócio do `FilaService` foi validada isoladamente através de uma classe de teste manual (`TesteFilaManual`), executada via `main()` independente do framework. O teste cobriu:

- Entrada de múltiplos clientes na fila e geração sequencial de senhas
- Ordem de atendimento respeitando FIFO (primeiro a entrar, primeiro a ser chamado)
- Transições de estado (`AGUARDANDO` → `EM_ATENDIMENTO` → `FINALIZADO`/`CANCELADO`)
- Atualização correta da fila após cada operação
- Lançamento correto das exceções de domínio (`AtendimentoNaoEncontradoException` para senha inexistente, `FilaVaziaException` para fila vazia)

Todos os cenários testados retornaram o comportamento esperado, sem erros em tempo de execução.

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Maven

## Próximos passos

- [ ] Testar os endpoints REST via Postman
- [ ] Tratamento de erros HTTP para as exceções de domínio (ex: 404 para atendimento não encontrado, 400 para fila vazia)
- [ ] Persistência com JPA e banco de dados (MySQL)
- [ ] Testes automatizados (JUnit)
- [ ] Autenticação/autorização

## Visão futura (fora do escopo da v1)

Uma ideia em estudo para uma versão futura, bem mais adiante da v1 atual, é adaptar o conceito de fila para o contexto da área da saúde — por exemplo, ordenar o atendimento por **gravidade/prioridade clínica** em vez de estritamente FIFO (semelhante a um sistema de triagem hospitalar). Essa é apenas a idealização final do projeto a longo prazo e não faz parte do escopo atual nem da v1.

## Como rodar

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta `8080` por padrão.

> Observação: a persistência ainda não foi implementada; os dados existem apenas em memória durante a execução da aplicação.
