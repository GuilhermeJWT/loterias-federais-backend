# Loterias - Análise Inteligente de Jogos

Sistema completo para análise de jogos da Lotofacil, com integração à API da Caixa, persistência de dados e interface web moderna.

## 🚀 Funcionalidades

### Principais Funcionalidades
- **Integração API Caixa**: Busca automática de dados da Lotofacil via API oficial
- **Persistência de Dados**: Armazenamento de todos os sorteios no PostgreSQL
- **Análise de Jogos**: Verificação de acertos de um jogo contra o histórico completo
- **Gerador de Jogos**: Criação de jogos baseados em diferentes estratégias:
  - Números mais sorteados
  - Jogos balanceados (quentes e frios)
  - Jogos aleatórios
  - Jogos com sequências
- **Interface Web Moderna**: Dashboard responsivo com Bootstrap 5
- **Estatísticas**: Análise de frequência de números e padrões

### Estratégias de Análise
- **Análise Completa**: Verifica acertos contra todos os sorteios históricos
- **Análise Parcial**: Verifica acertos apenas nos últimos X sorteios
- **Filtragem por Acertos**: Mostra apenas jogos com 11+ acertos

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 4.1.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Cloud OpenFeign** (integração API)
- **RestTemplate** (cliente HTTP genérico)
- **Spring Validation** (validação de dados)
- **Configuration Properties** (configuração dinâmica)
- **Thymeleaf** (templates)
- **Bootstrap 5** (interface)
- **Docker** (PostgreSQL)
- **Maven**

## 🎯 Arquitetura Dinâmica

O sistema foi projetado para ser extensível e suportar múltiplos jogos de loteria sem duplicação de código:

### Configuração Centralizada
- URLs de APIs configuradas no `application.yaml`
- Configurações específicas por jogo (quantidade de dezenas, máximo número, etc.)
- Cliente genérico `LoteriaApiClient` para todos os jogos

### Jogos Configurados
- **Lotofacil**: 15 dezenas, números 1-25
- **Mega-Sena**: 6 dezenas, números 1-60 (preparado)
- **Lotomania**: 20 dezenas, números 1-100 (preparado)
- **Quina**: 5 dezenas, números 1-80 (preparado)

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Docker e Docker Compose (para PostgreSQL)
- Git

## 🔧 Instalação e Configuração

### 1. Clonar o Repositório

```bash
git clone <repository-url>
cd loterias
```

### 2. Configurar Banco de Dados

#### Opção A: Usar Docker (Recomendado)

```bash
# Iniciar PostgreSQL
docker-compose up -d

# Verificar se o container está rodando
docker ps
```

#### Opção B: PostgreSQL Local

Certifique-se de ter PostgreSQL instalado e crie o banco:

```sql
CREATE DATABASE loterias;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE loterias TO postgres;
```

### 4. Compilar e Executar

```bash
# Compilar projeto
./mvnw clean install

# Executar aplicação
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 🎮 Como Usar

### Interface Web

1. Acesse `http://localhost:8080`
2. **Análise de Jogo**:
   - Digite 15 números separados por vírgula ou espaço
   - Opcionalmente, especifique quantos últimos sorteios analisar
   - Clique em "Analisar Jogo"
   - Veja os resultados com acertos detalhados

3. **Gerador de Jogos**:
   - Escolha a quantidade de jogos a gerar
   - Selecione a base de análise (últimos X sorteios)
   - Escolha a estratégia:
     - **Mais Sorteados**: Prioriza números que mais apareceram
     - **Balanceados**: Combina números quentes e frios
     - **Aleatórios**: Gera jogos completamente aleatórios
     - **Sequências**: Inclui pequenas sequências de números

### API REST

#### Endpoints Lotofacil

- `GET /lotofacil/concurso/{concurso}` - Buscar concurso específico
- `GET /lotofacil/todos` - Buscar todos os sorteios
- `GET /lotofacil/ultimos/{quantidade}` - Buscar últimos X sorteios
- `GET /lotofacil/total` - Contar total de sorteios

#### Endpoints API Caixa

- `GET /lotofacil/api/ultimo` - Buscar último concurso da API (sem salvar)
- `GET /lotofacil/api/concurso/{concurso}` - Buscar concurso específico da API (sem salvar)
- `POST /lotofacil/api/salvar-ultimo` - Buscar e salvar último concurso
- `POST /lotofacil/api/salvar/{concurso}` - Buscar e salvar concurso específico

#### Endpoints de Análise

- `POST /analise/jogo` - Analisar jogo
  ```json
  {
    "jogo": "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15",
    "ultimosSorteios": 100
  }
  ```

#### Endpoints de Geração

- `GET /analise/gerar/mais-sorteados?quantidadeJogos=5&ultimosSorteios=100`
- `GET /analise/gerar/balanceados?quantidadeJogos=5&ultimosSorteios=100`
- `GET /analise/gerar/aleatorios?quantidadeJogos=5`
- `GET /analise/gerar/sequencias?quantidadeJogos=5&ultimosSorteios=100`

#### Endpoints de Importação

- `POST /importacao/concursos?concursoInicial=1&concursoFinal=100` - Importar concursos específicos
- `POST /importacao/todos` - Importar todos os concursos (automático)
- `POST /importacao/lotofacil-completa` - Importar Lotofacil completa (1 a 3784)
- `POST /importacao/ultimos?quantidade=50` - Importar últimos X concursos
- `POST /importacao/faltantes` - Importar apenas concursos faltantes
- `GET /importacao/status` - Verificar status da importação
- `GET /importacao/ultimo-concurso-salvo` - Verificar último concurso salvo
- `GET /importacao/total-concursos-salvos` - Verificar total de concursos salvos

### Importação Completa da Lotofacil

Para importar todos os 3784 sorteios da Lotofacil:

```bash
curl -X POST "http://localhost:8080/importacao/lotofacil-completa"
```

**Importante:**
- O processo usa rate limiting (500ms entre requisições)
- Faz pausas a cada 50 concursos (5 segundos)
- Pula concursos que já existem no banco
- Tempo estimado: ~30-40 minutos para 3784 concursos
- Verifique os logs para progresso em tempo real

## 📊 Estrutura do Projeto

```
src/main/java/br/com/systemgs/loterias/
├── config/          # Configurações adicionais
├── controller/      # Controllers REST e Web
├── dto/            # DTOs para API e UI
├── model/          # Entidades JPA
├── repository/     # Repositories JPA
├── service/        # Lógica de negócio
├── client/         # Clientes Feign para API Caixa
└── util/           # Utilitários de análise
```

## 🔍 Funcionalidades Extras

### Estatísticas Avançadas
- Frequência de cada número (1-25)
- Análise de dias da semana com mais sorteios
- Números "quentes" e "frios"
- Padrões de sequências

### Recomendações Inteligentes
- Sugestão de números baseados em tendências
- Análise de probabilidade
- Simulação de apostas

## 🐛 Troubleshooting

### PostgreSQL não conecta
```bash
# Verificar se Docker está rodando
docker ps

# Reiniciar container
docker-compose restart

# Ver logs
docker-compose logs postgres
```

### Erro de compilação
```bash
# Limpar e recompilar
./mvnw clean install -U
```

### API da Caixa não responde
- Verifique sua conexão com a internet
- A API da Caixa pode ter rate limiting
- Tente novamente após alguns minutos

## 📝 Próximas Melhorias

- [ ] Suporte para outros jogos (Mega-Sena, Quina, etc.)
- [ ] Gráficos e visualizações avançadas
- [ ] Exportação de relatórios (PDF, Excel)
- [ ] Sistema de alertas e notificações
- [ ] Autenticação de usuários
- [ ] Histórico de análises do usuário
- [ ] Machine Learning para previsões

## 📄 Licença

Este projeto é para fins educacionais e de análise pessoal.

## ⚠️ Aviso Legal

Este sistema é apenas para fins de análise e entretenimento. Não garante ganhos em loterias. Jogue com responsabilidade.

## 👨‍💻 Desenvolvimento

### Adicionar Novos Jogos

1. Criar entidade/model para o novo jogo
2. Criar repository correspondente
3. Implementar service específico
4. Criar controller para o novo jogo
5. Adicionar cliente Feign para API do jogo
6. Atualizar interface web

### Executar Testes

```bash
./mvnw test
```

### Build de Produção

```bash
./mvnw clean package -Pprod
java -jar target/loterias-1.0.0.jar
```

---

Desenvolvido com ❤️ para análise inteligente de loterias