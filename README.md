# API de Gerenciamento de Filas de Atendimento

![CI Status](https://shields.io)

Sistema robusto para simulação e gerenciamento de filas de atendimento, desenvolvido em **Java 17** com **Spring Boot**. O projeto priorizou a modelagem do domínio em Java puro antes da camada de aplicação, garantindo uma lógica de negócio sólida, coesa e testável de forma isolada.

---

## 🚀 Status do Projeto

*   **v1 (Concluída):** Domínio estruturado • API REST funcional • Persistência em MySQL • Tratamento de erros HTTP customizado.
*   **v2 (Concluída/Branch `v2-melhorias`):** Testes automatizados • CI com GitHub Actions • Bean Validation • Lombok • Documentação interativa com Swagger/OpenAPI.
*   **v3 (Planejada):** Containerização com Docker e expansão do domínio.

---

## 📋 Sobre o Projeto

O sistema reproduz digitalmente o fluxo de uma fila física:
1. Um cliente entra na fila e recebe uma senha numérica.
2. O atendente chama a próxima senha disponível (regra FIFO).
3. O atendimento pode ser iniciado, finalizado ou cancelado a qualquer momento.

Toda a lógica central foi construída primeiro em Java puro para reforçar os pilares da Orientação a Objetos e o encapsulamento, blindando o domínio contra dependências diretas do framework.

---

## 📂 Estrutura do Projeto

```text
com.gestaodeatendimento
├── core/
│   ├── model/          → Entidades JPA (Cliente, Atendimento, StatusAtendimento)
│   ├── service/         → Lógica de negócio (FilaService)
│   ├── repository/      → Repositórios JPA (ClienteRepository, AtendimentoRepository)
│   └── exception/       → Exceções de domínio customizadas
└── api/
    ├── dto/             → Objetos de entrada/saída da API (EntrarNaFilaRequest, etc.)
    ├── controller/       → FilaController (Endpoints REST)
    └── exception/        → GlobalExceptionHandler (Mapeamento de erros HTTP)
```

---

## 🧠 Arquitetura e Domínio

*   **Cliente:** Entidade JPA com dados de identificação e horário de chegada. Utiliza Lombok com `@EqualsAndHashCode` restrito estritamente ao ID.
*   **Atendimento:** O "ticket" do cliente. Controla o número da senha, status e carimbos de data/hora. **Não possui setters genéricos**; expõe métodos explícitos de transição de estado (`iniciarAtendimento()`, `finalizar()`, `cancelar()`).
*   **StatusAtendimento:** Enum contendo os estados `AGUARDANDO`, `EM_ATENDIMENTO`, `FINALIZADO` e `NCANCELADO`. Persistido como `String` no banco de dados.
*   **FilaService:** Componente central que orquestra as operações de negócio utilizando os repositórios JPA.
*   **Exceções de Domínio:** `FilaVaziaException` e `AtendimentoNaoEncontradoException` isolam erros de negócio de erros de infraestrutura.

---

## 🗄️ Persistência e Comportamento FIFO

Os dados são armazenados em um banco de dados **MySQL** via **Spring Data JPA/Hibernate**. 
A semântica de fila (First-In, First-Out) é garantida diretamente no nível do banco por meio do Query Method:
```java
findByStatusOrderByHorarioEntradaAsc(StatusAtendimento status);
```
Isso dispensa o uso de estruturas voláteis em memória e preserva o estado da fila entre reinicializações da aplicação.

---

## 🛣️ API REST (Endpoints)

| Método | Endpoint | Ação |
| :--- | :--- | :--- |
| **POST** | `/fila` | Cliente entra na fila (recebe nome, retorna senha) |
| **POST** | `/fila/proximo` | Chama o próximo atendimento da fila |
| **PUT** | `/fila/{numeroSenha}/finalizar` | Finaliza um atendimento em andamento |
| **DELETE** | `/fila/{numeroSenha}` | Cancela um atendimento específico |
| **GET** | `/fila/{numeroSenha}/posicao` | Consulta a posição atual do atendimento na fila |
| **GET** | `/fila` | Lista todos os atendimentos que estão aguardando |

> **Nota:** DTOs (Data Transfer Objects) isolam as entidades de domínio da camada externa. Os DTOs de entrada são mutáveis para desserialização do Jackson, enquanto os DTOs de saída são estritamente imutáveis.

---

## 🛡️ Validação e Tratamento de Erros

*   **Bean Validation:** O `EntrarNaFilaRequest` valida a entrada usando `@NotBlank` no nome do cliente. Falhas geram automaticamente um status `400 Bad Request` detalhado.
*   **Global Exception Handler:** Um `@RestControllerAdvice` intercepta exceções do núcleo da aplicação e as converte nos códigos HTTP adequados:
    *   `FilaVaziaException` ➡️ `400 Bad Request`
    *   `AtendimentoNaoEncontradoException` ➡️ `404 Not Found`

---

## 🧪 Testes e Qualidade

### Testes do Domínio (Java Puro)
Antes do acoplamento com o Spring, as regras foram validadas via `TesteFilaManual` usando um método `main()` isolado, assegurando o comportamento esperado do negócio.

### Testes Automatizados (v2)
A suíte conta com **10 testes unitários** usando **JUnit 5** e **Mockito**, isolando o `FilaService` da camada de dados por meio de mocks. Cenários cobertos:
*   Sucesso e falha na transição de estados (`chamarProximo`, `finalizarAtendimento`, `cancelarAtendimento`).
*   Cálculo exato de índice posicional na fila.
*   Garantia de que refatorações com Lombok não quebraram contratos de negócio.

---

## ⚙️ Integração Contínua (CI)

O fluxo do **GitHub Actions** (`.github/workflows/ci.yml`) é disparado a cada push ou Pull Request nas branches `main` e `v2-melhorias`. O pipeline:
1. Executa o checkout do código.
2. Configura o ambiente Java 17.
3. Roda a suíte com `./mvnw test`.

*Nota: O teste padrão `ApplicationTests` gerado pelo inicializador foi removido para evitar que a build falhasse no ambiente de CI por falta de um banco de dados MySQL ativo.*

---

## 🛠️ Tecnologias Utilizadas

*   **Java 17**
*   **Spring Boot 3.x** (Data JPA, Web, Bean Validation)
*   **MySQL** & **Hibernate**
*   **Lombok**
*   **Springdoc OpenAPI / Swagger** (Disponível em `/swagger-ui.html`)
*   **JUnit 5** & **Mockito**
*   **GitHub Actions**
*   **Maven**

---

## 🔮 Próximos Passos (v3)

- [ ]  **Containerização:** Criação de `Dockerfile` e `docker-compose.yml` para unificar o ecossistema da aplicação e do banco MySQL.
- [ ]  **Evolução do Modelo:** Inclusão de campo descritivo/triagem no `Atendimento` (ex: consulta de rotina, retorno de exames, emergência).

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
*   **Java 17** instalado localmente.
*   Banco de dados **MySQL** configurado de acordo com as propriedades do arquivo `src/main/resources/application.properties`.

### Inicializando a Aplicação
Para rodar o servidor de desenvolvimento, execute o comando abaixo na raiz do projeto:
```bash
./mvnw spring-boot:run
```
> A aplicação subirá na porta **8080** por padrão.

### Acessando a Documentação
Com a aplicação em execução, a interface interativa do Swagger estará disponível no endereço:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧪 Como Rodar os Testes Automatizados

Para executar toda a suíte de testes unitários (JUnit 5 + Mockito) e validar as regras de negócio, utilize o comando:
```bash
./mvnw test
```

---

## 🔭 Visão Futura (Escopo de Longo Prazo)

Uma ideia em estudo para versões futuras — bem além do escopo da v3 — envolve a **adaptação do conceito de fila para a área da saúde**. 

O objetivo é evoluir o algoritmo para ordenar o atendimento por **gravidade e prioridade clínica**, substituindo a lógica estrita de ordem de chegada (FIFO) por um sistema baseado em triagem hospitalar (como o Protocolo de Manchester). O campo de descrição planejado para a v3 servirá como fundação para essa mudança, fornecendo o contexto inicial de cada paciente.

```bash
./mvnw test
```
