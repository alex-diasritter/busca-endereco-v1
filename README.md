# Busca Endereço API

Aplicação web para busca de endereços por CEP, com autenticação de usuários e histórico de consultas.

## 🚀 Funcionalidades

- 🔒 Autenticação segura com JWT
- 🔍 Busca de endereços por CEP
- 📝 Histórico de consultas
- 👤 Cadastro e login de usuários
- 🛡️ Validações de segurança
- 📊 Armazenamento em banco de dados

## 🛠️ Tecnologias

### Backend
- Java com Spring Boot
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL (Banco de dados principal)
- OpenFeign para integração com API de CEP
- Maven para gerenciamento de dependências

### Frontend
- HTML5, CSS3 e JavaScript puro
- Fetch API para requisições HTTP
- Local Storage para armazenamento do token JWT

## 🚀 Como executar

### Pré-requisitos
- Java 11 ou superior
- Maven
- Docker
- Docker Compose

### Executando a aplicação com Docker (Recomendado)

1. **Requisitos**
   - Docker
   - Docker Compose

2. **Iniciar a aplicação**
   ```bash
   # Na raiz do projeto, execute:
   docker-compose up --build
   ```
   
   Isso irá:
   - Construir as imagens do backend e frontend
   - Iniciar o banco de dados PostgreSQL
   - Configurar automaticamente todas as dependências
   - Configurar uma rede docker

   A aplicação estará disponível em:
   - Frontend: `http://localhost:80`
   - Backend (API): `http://localhost:8080`

## 📝 Uso

1. **Cadastro**
   - Acesse a página de registro
   - Preencha os dados necessários
   - Faça login com suas credenciais

2. **Busca de CEP**
   - Faça login na aplicação
   - Informe o CEP desejado no campo de busca
   - Visualize os dados do endereço
   - Todas as consultas são salvas no histórico

## 🔒 Segurança

- Autenticação baseada em JWT com Spring Security
- Senhas armazenadas com hash seguro
- Proteção contra ataques CSRF
- Validação de entrada em todas as requisições

## 📦 Estrutura do Projeto

```
busca-endereco-v1/
├── backend/                           # Código-fonte do backend (Spring Boot)
│   ├── src/main/java/com/alex/buscacep/
│   │   ├── controller/               # Controladores REST
│   │   │   ├── AuthenticationController.java  # Autenticação de usuários
│   │   │   ├── EnderecoController.java        # Busca de CEP
│   │   ├── domain/
│   │   │   ├── dtos/                # Objetos de transferência de dados
│   │   │   │   ├── request/          # DTOs de requisição
│   │   │   │   └── response/         # DTOs de resposta
│   │   │   └── models/               # Entidades do domínio
│   │   ├── infra/
│   │   │   ├── client/               # Clientes HTTP (Feign)
│   │   │   ├── repositories/         # Repositórios JPA
│   │   │   ├── security/             # Configurações de segurança
│   │   │   └── service/              # Serviços da aplicação
│   │   └── BuscacepApplication.java  # Classe principal
│   └── src/main/resources/           # Arquivos de configuração
├── frontend/                         # Código-fonte do frontend
│   ├── index.html                    # Página de login
│   ├── register.html                 # Página de cadastro
│   ├── app.js                        # Lógica principal
│   └── styles.css                    # Estilos
└── docker-compose.yml                # Configuração Docker
```
```

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---