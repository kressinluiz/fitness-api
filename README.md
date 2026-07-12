# Fitness App

[![CI](https://github.com/kressinluiz/fitness-api/actions/workflows/ci.yml/badge.svg)](https://github.com/kressinluiz/fitness-api/actions/workflows/ci.yml)

Uma API REST em Spring Boot para gerenciar exercícios, treinos e planos de treino agendados — construída com arquitetura em camadas, PostgreSQL, migrations com Flyway, testes de integração com Testcontainers e pipeline de CI no GitHub Actions.

## Visão Geral

A API modela três recursos principais:

- **Exercícios (Exercises)** — definições reutilizáveis de exercício (nome, categoria, grupo muscular).
- **Treinos (Workouts)** — uma rotina nomeada, composta por um ou mais exercícios, cada um com um conjunto de repetições/carga.
- **Planos de Treino (Workout Plans)** — um agendamento vinculado a um treino, recorrente (dias da semana específicos) ou baseado em datas específicas.

## Arquitetura

O projeto segue uma arquitetura em camadas clássica, mantendo a camada web desacoplada do modelo de persistência:

```
Controller  →  Service  →  Repository  →  Banco de Dados
    ↑             ↓
   DTO   ←──   Mapper   ←──→   Entity
```

- **Controllers** (`controller/`) tratam apenas questões de HTTP — roteamento, status codes e documentação OpenAPI. Nenhuma regra de negócio vive aqui.
- **Services** (`service/`) contêm as regras de negócio e orquestram a persistência.
- **Entities** (`entity/`) garantem suas próprias invariantes (ex: um `Exercise` não pode ser criado com nome vazio, um `ExerciseSet` não pode ter zero repetições) — a validação não é só via anotação, ela faz parte do próprio modelo de domínio.
- **DTOs + Mappers** (`dto/`, `mapper/`) garantem que entidades nunca sejam expostas diretamente pela API, e que atualizações parciais (`PATCH`) sejam tratadas explicitamente.
- **Exceções customizadas + um handler global** (`exception/`) traduzem erros de domínio (ex: `ExerciseNotFoundException`, `BusinessException`) em respostas HTTP consistentes.
- **Migrations com Flyway** (`resources/db/migration/`) versionam o schema do PostgreSQL, em vez de depender do DDL automático do Hibernate em qualquer ambiente além da validação local.

### Modelo de Domínio

```
Exercise ──< ExercisePlan >── Workout ──< WorkoutPlan >── WorkoutDate ──< ScheduleEntry
                │
                └──< ExerciseSet
```

- Um `Workout` agrupa múltiplos `ExercisePlan`s (um exercício + seus sets) daquela rotina.
- Um `WorkoutPlan` vincula um `Workout` a um `WorkoutDate`, que é do tipo `RECURRING` (com `ScheduleEntry`s semanais) ou `SPECIFIC_DATES`.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 26 |
| Framework | Spring Boot 4.1.0 (Spring Web MVC, Spring Data JPA, Bean Validation) |
| Banco de dados | PostgreSQL 17 |
| Migrations | Flyway |
| Documentação de API | springdoc-openapi (Swagger UI) |
| Testes | JUnit 5, Testcontainers (PostgreSQL real nos testes de integração) |
| Containerização | Docker (build multi-stage), Docker Compose |
| CI | GitHub Actions |
| Build | Maven |

## Como Executar

### Opção 1 — Docker Compose (API + banco, tudo em containers)

```bash
docker-compose up --build
```

Isso builda a imagem da API (via `Dockerfile` multi-stage) e sobe a API junto do PostgreSQL, aguardando o banco ficar saudável (`healthcheck`) antes de iniciar a aplicação. A API sobe em `http://localhost:8080` usando o profile `prod`.

### Opção 2 — Ambiente de desenvolvimento com hot-reload

```bash
docker-compose -f docker-compose.dev.yml up --build
```

Usa o `Dockerfile.dev`, monta o código-fonte como volume e ativa o `spring-boot-devtools`, então alterações no código reiniciam a aplicação automaticamente. O `docker/entrypoint.sh` aguarda o PostgreSQL responder antes de iniciar o Maven.

### Opção 3 — Apenas o banco em container, aplicação local
É necessário configurar o application.properties com as seguintes informações:

```application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fitness
spring.datasource.username=fitness
spring.datasource.password=fitness
```

Para iniciar a aplicação:
```bash
docker-compose up -d postgres
./mvnw clean package
./mvnw spring-boot:run
```

Em qualquer uma das opções, o Flyway roda automaticamente na inicialização.

### Explorar a API

Com a aplicação em execução, o Swagger UI fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

## Configuração e Profiles

A aplicação usa profiles do Spring para separar configuração por ambiente — nenhuma credencial fica hardcoded no código-fonte:

| Profile | Uso | Configuração |
|---|---|---|
| `dev` | Desenvolvimento local com hot-reload | `application-dev.properties` |
| `prod` | Execução via Docker Compose "de produção" | `application-prod.properties` |
| `test` | Testes de integração (Testcontainers) | `src/test/resources/application-test.properties` |

Tanto `dev` quanto `prod` leem a conexão do banco a partir de variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>
export SPRING_DATASOURCE_USERNAME=<usuario>
export SPRING_DATASOURCE_PASSWORD=<senha>
```

Os valores `fitness`/`fitness` usados nos arquivos `docker-compose.yml` e `docker-compose.dev.yml` são credenciais de desenvolvimento local, não segredos de produção — em qualquer ambiente real de deploy, essas variáveis devem vir do gerenciador de segredos da plataforma, nunca de um arquivo versionado.

## Endpoints da API

### Exercícios — `/exercises`

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/exercises` | Criar um exercício |
| `GET` | `/exercises` | Listar todos os exercícios |
| `GET` | `/exercises/{id}` | Buscar um exercício específico |
| `PATCH` | `/exercises/{id}` | Atualizar parcialmente um exercício |
| `DELETE` | `/exercises/{id}` | Excluir um exercício |

**Exemplo de requisição de criação:**

```json
{
  "name": "Bench Press",
  "description": "Exercício de peitoral",
  "category": "Força",
  "muscleGroup": "Peitoral"
}
```

### Treinos — `/workouts`

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/workouts` | Criar um treino |
| `GET` | `/workouts` | Listar todos os treinos |
| `GET` | `/workouts/{id}` | Buscar um treino específico |
| `PATCH` | `/workouts/{id}` | Atualizar parcialmente um treino |
| `DELETE` | `/workouts/{id}` | Excluir um treino |

**Exemplo de requisição de criação:**

```json
{
  "name": "Treino de Corpo Inteiro",
  "description": "Uma rotina completa",
  "exercisePlans": [
    {
      "exerciseId": 1,
      "sets": [
        { "reps": 8, "weight": 60.0 }
      ]
    }
  ]
}
```

### Planos de Treino — `/planner`

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/planner` | Criar um plano de treino |
| `GET` | `/planner` | Listar todos os planos de treino |
| `GET` | `/planner/{id}` | Buscar um plano de treino específico |
| `PATCH` | `/planner/{id}` | Atualizar parcialmente um plano de treino |
| `DELETE` | `/planner/{id}` | Excluir um plano de treino |

**Exemplo de requisição de criação (agendamento recorrente):**

```json
{
  "workoutId": 1,
  "workoutDate": {
    "scheduleType": "RECURRING",
    "scheduleEntries": [
      { "weekDay": 1, "dateTime": "2026-07-13T07:00:00-03:00" }
    ]
  }
}
```

O campo `scheduleType` aceita `RECURRING` ou `SPECIFIC_DATES`.

Os schemas completos de requisição/resposta de cada endpoint — incluindo restrições de validação e respostas de erro — estão disponíveis no Swagger UI com a aplicação em execução.

## Estrutura do Projeto

```
.
├── .github/workflows/ci.yml      Pipeline de CI (testes + build)
├── docker/entrypoint.sh           Aguarda o Postgres antes de iniciar (ambiente dev)
├── Dockerfile                     Build multi-stage para produção (imagem final roda como usuário não-root)
├── Dockerfile.dev                 Imagem de desenvolvimento com hot-reload
├── docker-compose.yml             API + Postgres, profile `prod`
├── docker-compose.dev.yml         API + Postgres com volume montado, profile `dev`
│
src/main/java/com/kressin/fitness_app/
├── controller/   Controllers REST — apenas camada HTTP
├── dto/          Records de requisição/resposta
├── entity/       Entidades JPA com invariantes autovalidadas
├── mapper/       Tradução entre DTOs, entidades e commands
├── repository/   Repositórios Spring Data JPA
├── service/      Regras de negócio
└── exception/    Exceções de domínio + handler global de exceções

src/main/resources/
├── application.properties
├── application-dev.properties
├── application-prod.properties
└── db/migration/   Scripts de migration do Flyway (V1 … V7)

src/test/resources/
└── application-test.properties
```

## Testes

A suíte de testes cobre tanto testes unitários (services, mappers) quanto testes de integração que sobem uma instância real de PostgreSQL via Testcontainers — nenhum H2 ou banco mockado é usado na camada de integração.

```bash
./mvnw test
```

## Licença
Distribuído sob a licença MIT. Veja [LICENSE](https://opensource.org/licenses/MIT) para mais detalhes.
