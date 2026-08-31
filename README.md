# API Gestão de Fila de Atendimento

API para gerenciamento de filas de atendimento, desenvolvida em Java com Spring Boot. O projeto começou pela modelagem do domínio em Java puro, antes da camada de aplicação, garantindo que a lógica de negócio fosse sólida, coesa e testável de forma isolada.

## Status do projeto

**v1 — em desenvolvimento.** A camada de domínio (Java puro) está completa. A camada de API (REST) e a persistência ainda estão em construção.

## Sobre o projeto

O sistema simula o funcionamento de uma fila de atendimento: um cliente entra na fila e recebe uma senha, um atendente chama o próximo da fila, e o atendimento pode ser finalizado ou cancelado a qualquer momento. Toda a lógica de negócio foi construída primeiro em Java puro (sem depender do framework), para reforçar boas práticas de orientação a objetos antes de expor tudo via API.

## Estrutura do projeto

```
com.gestaodeatendimento
├── core/
│   ├── model/          → Entidades de domínio (Cliente, Atendimento, StatusAtendimento)
│   ├── service/         → Lógica de negócio (FilaService, GeradorDeSenha)
│   └── exception/       → Exceções de domínio customizadas
└── api/                  → Camada de aplicação (controllers, DTOs, configuração) — em construção
```

## Domínio

- **Cliente**: representa a pessoa que entra na fila (id, nome, horário de chegada). Classe imutável.
- **Atendimento**: representa o "ticket" de um cliente na fila, com número de senha, status e horários de entrada/início/fim. Expõe métodos de transição de estado (`iniciarAtendimento`, `finalizar`, `cancelar`) em vez de setters genéricos.
- **StatusAtendimento**: enum com os estados possíveis (`AGUARDANDO`, `EM_ATENDIMENTO`, `FINALIZADO`, `CANCELADO`).
- **FilaService**: orquestra toda a lógica de negócio — entrar na fila, chamar o próximo, finalizar, cancelar, consultar posição e listar a fila atual.
- **GeradorDeSenha**: responsável por gerar números de senha sequenciais e únicos.
- **Exceções de domínio**: `FilaVaziaException` e `AtendimentoNaoEncontradoException`, lançadas em situações específicas do negócio, em vez de erros genéricos.

## Decisões técnicas

- Uso de `Queue` (FIFO) para representar a fila de espera, respeitando a ordem natural de chegada.
- Uso de `Map` para busca rápida de atendimentos em andamento por número de senha.
- Encapsulamento de transições de estado dentro da própria entidade `Atendimento`, evitando setters genéricos que permitiriam estados inválidos.
- `equals`/`hashCode` de entidades baseados apenas no identificador único (`id` ou `numeroSenha`), já que representam identidade, não valor.

## Tecnologias

- Java
- Spring Boot
- Maven

## Próximos passos

- [ ] Camada de API REST (controllers e DTOs)
- [ ] Tratamento de erros HTTP para as exceções de domínio
- [ ] Persistência com JPA e banco de dados
- [ ] Testes automatizados (JUnit)
- [ ] Autenticação/autorização

## Como rodar

```bash
./mvnw spring-boot:run
```

> Observação: a camada de API e a persistência ainda estão em desenvolvimento; por enquanto, a lógica de domínio pode ser validada isoladamente via testes manuais em Java puro.

