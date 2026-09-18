# API Gestão de Fila de Atendimento

API para gerenciamento de filas de atendimento, desenvolvida em Java com Spring Boot. O projeto começou pela modelagem do domínio em Java puro, antes da camada de aplicação, garantindo que a lógica de negócio fosse sólida, coesa e testável de forma isolada.

## Status do projeto

**v1 — concluída.** Domínio completo, API REST funcional, persistência real com MySQL e tratamento de erros HTTP implementados e testados. A v2 está planejada com testes automatizados, CI/CD e outras melhorias (veja "Próximos passos").

## Sobre o projeto

O sistema simula o funcionamento de uma fila de atendimento: um cliente entra na fila e recebe uma senha, um atendente chama o próximo da fila, e o atendimento pode ser finalizado ou cancelado a qualquer momento. Toda a lógica de negócio foi construída primeiro em Java puro (sem depender do framework), para reforçar boas práticas de orientação a objetos antes de expor tudo via API.

## Estrutura do projeto

```
com.gestaodeatendimento
├── core/
│   ├── model/          → Entidades JPA (Cliente, Atendimento, StatusAtendimento)
│   ├── service/         → Lógica de negócio (FilaService)
│   ├── repository/      → Repositórios JPA (ClienteRepository, AtendimentoRepository)
│   └── exception/       → Exceções de domínio customizadas
└── api/
    ├── dto/             → Objetos de entrada/saída da API (EntrarNaFilaRequest, AtendimentoResponse, PosicaoResponse)
    ├── controller/       → FilaController, expondo a lógica de negócio via endpoints REST
    └── exception/        → GlobalExceptionHandler, mapeando exceções de domínio para respostas HTTP
```

## Domínio

- **Cliente**: representa a pessoa que entra na fila (id, nome, horário de chegada). Entidade JPA.
- **Atendimento**: representa o "ticket" de um cliente na fila, com número de senha, status e horários de entrada/início/fim. Expõe métodos de transição de estado (`iniciarAtendimento`, `finalizar`, `cancelar`) em vez de setters genéricos. Entidade JPA, relacionada a `Cliente` via `@ManyToOne`.
- **StatusAtendimento**: enum com os estados possíveis (`AGUARDANDO`, `EM_ATENDIMENTO`, `FINALIZADO`, `CANCELADO`), persistido como String no banco.
- **FilaService**: orquestra toda a lógica de negócio — entrar na fila, chamar o próximo, finalizar, cancelar, consultar posição e listar a fila atual — usando os repositórios JPA em vez de estruturas em memória.
- **Exceções de domínio**: `FilaVaziaException` e `AtendimentoNaoEncontradoException`, lançadas em situações específicas do negócio e mapeadas para respostas HTTP adequadas.

## Persistência

Dados armazenados em MySQL via Spring Data JPA/Hibernate. `Cliente` e `Atendimento` são entidades JPA com chave primária autogerada pelo banco (`@GeneratedValue`). A "fila" não é mais uma estrutura em memória: é uma consulta ao banco filtrando atendimentos com `status = AGUARDANDO`, ordenados por horário de entrada (`findByStatusOrderByHorarioEntradaAsc`), preservando o comportamento FIFO. Os dados persistem entre reinicializações da aplicação — validado manualmente reiniciando a aplicação e confirmando que a fila permanece íntegra.

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

## Tratamento de erros HTTP

Um `GlobalExceptionHandler` (`@RestControllerAdvice`) intercepta as exceções de domínio e as converte em respostas HTTP apropriadas, em vez do erro 500 genérico padrão do Spring:

- `FilaVaziaException` → **400 Bad Request**
- `AtendimentoNaoEncontradoException` → **404 Not Found**

Ambos os cenários foram testados via Postman e retornam o status e a mensagem corretos.

## Decisões técnicas

- Uso de query method (`findByStatusOrderByHorarioEntradaAsc`) para representar a fila FIFO diretamente via banco de dados, sem precisar manter uma estrutura em memória paralela.
- Encapsulamento de transições de estado dentro da própria entidade `Atendimento`, evitando setters genéricos que permitiriam estados inválidos.
- `equals`/`hashCode` de entidades baseados apenas no identificador único (`id` ou `numeroSenha`), já que representam identidade, não valor.
- Separação entre DTOs de entrada (mutáveis, com construtor vazio, pensados para o Jackson desserializar JSON) e DTOs de saída (imutáveis, montados pelo próprio código a partir do domínio).
- Injeção de dependência via construtor no `FilaController` e no `FilaService`, em vez de instanciar dependências manualmente.
- Tratamento de erros centralizado (`@RestControllerAdvice`) em vez de `try/catch` espalhado pelos controllers.

## Testes do domínio (Java puro)

Antes de integrar com Spring, toda a lógica de negócio do `FilaService` foi validada isoladamente através de uma classe de teste manual (`TesteFilaManual`), executada via `main()` independente do framework, cobrindo entrada na fila, ordem FIFO, transições de estado e as exceções de domínio.

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- MySQL
- Maven

## Próximos passos (v2)

- [ ] Testes automatizados (JUnit)
- [ ] CI com GitHub Actions, rodando os testes a cada push
- [ ] Bean Validation nos DTOs de entrada
- [ ] Uso mais completo do Lombok (reduzir boilerplate em entidades e DTOs)
- [ ] Documentação da API com Swagger/OpenAPI
- [ ] Containerização com Docker
- [ ] Autenticação/autorização

## Visão futura (fora do escopo da v1 e v2)

Uma ideia em estudo para uma versão futura, bem mais adiante, é adaptar o conceito de fila para o contexto da área da saúde — por exemplo, ordenar o atendimento por **gravidade/prioridade clínica** em vez de estritamente FIFO (semelhante a um sistema de triagem hospitalar). Essa é apenas a idealização final do projeto a longo prazo.

## Como rodar

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta `8080` por padrão. Requer um banco MySQL configurado em `application.properties`.
