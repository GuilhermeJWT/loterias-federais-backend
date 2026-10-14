# Loterias - Análise da Lotofácil

Aplicação Java para análise de jogos da Lotofácil da Caixa Econômica Federal.

## 🎯 Funcionalidades

- **CRUD de Concursos**: Buscar, salvar e gerenciar concursos da Lotofácil
- **Integração com API da Caixa**: Importação automática de resultados oficiais
- **Análise de Jogos**: Verificar acertos contra histórico de sorteios
- **Geração de Jogos**: Criar jogos baseados em números mais sorteados
- **Inteligência Artificial**: Sugestões inteligentes baseadas em padrões
- **Estatísticas**: Frequência de números e tendências
- **Dashboard**: Interface visual completa com gráficos

## 🚀 Como Executar

### Pré-requisitos

- Java 17
- Maven
- PostgreSQL

### Banco de Dados

```bash
docker-compose up -d
```

### Executar Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📡 API Endpoints

### CRUD de Concursos

- `GET /lotofacil/concurso/{concurso}` - Buscar concurso específico
- `GET /lotofacil/todos` - Buscar todos os concursos
- `GET /lotofacil/ultimos/{quantidade}` - Buscar últimos X concursos
- `GET /lotofacil/total` - Contar total de sorteios
- `GET /lotofacil/ultimo` - Buscar último concurso

### Integração com API da Caixa

- `GET /lotofacil/api/ultimo` - Buscar último concurso na API
- `GET /lotofacil/api/concurso/{concurso}` - Buscar concurso específico na API
- `POST /lotofacil/api/salvar-ultimo` - Salvar último concurso da API
- `POST /lotofacil/api/salvar/{concurso}` - Salvar concurso específico da API

### Análise de Jogos

- `POST /lotofacil/analise/jogo` - Analisar jogo contra histórico

### Geração de Jogos

- `GET /lotofacil/analise/gerar/mais-sorteados` - Gerar jogos mais sorteados

### Inteligência Artificial

- `GET /lotofacil/sugestao-inteligente` - Gerar sugestão inteligente

### Estatísticas

- `GET /lotofacil/frequencia` - Frequência de números

## 🎨 Tecnologias

- Java 17
- Spring Boot 4.1.1
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Lombok
- RestTemplate
- Bootstrap 5
- Chart.js

## � Configuração

As configurações estão em `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres

loterias:
  api:
    base-url: https://servicebus2.caixa.gov.br/portaldeloterias/api
    jogos:
      lotofacil:
        nome: Lotofacil
        endpoint: lotofacil
        quantidade-dezenas: 15
        maximo-numero: 25
```

## 📄 Licença

Este projeto é para fins educacionais e de análise.