# Desafio Santander — API (Spring Boot + OAuth2/JWT + Caffeine + H2)

## Requisitos
- Java 17+
- Maven 3.9+
- Docker (opcional para execução em container)

## Geração de Chaves RSA (JWT)
Gere as chaves e substitua/gere os arquivos private.pem e public.pem em `src/main/resources/keypair/`:

```bash
openssl genrsa -out src/main/resources/keypair/private.pem 2048
openssl rsa -in src/main/resources/keypair/private.pem -pubout -out src/main/resources/keypair/public.pem
```

## Executando (Local)
Dentro do diretório raiz do projeto no terminal, execute o seguinte comando:
```bash
mvn spring-boot:run
```
- Swagger: `http://localhost:8080/swagger-ui.html`  
- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:desafio`)

## Executando (Docker Compose)
Dentro do diretório raiz do projeoto no terminal, execute o seguinte comando:
```bash
docker compose up --build -d
```

## Autenticação (OAuth2 + JWT simplificado)
1. **Obter token**:
   Foi configurado um usuário padrão na aplicação, mas o fluxo completo de autenticação OAuth2 é seguido para a geração e validação do JWT.
* Username: admin
* password: 12345
```bash
curl -s -X POST http://localhost:8080/auth/token   -H 'Content-Type: application/json'   -d '{"username":"admin","password":"12345"}'
```
Resposta:
```json
{
  "access_token": "<JWT>",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

2. **Usar o token**: enviar `Authorization: Bearer <JWT>` nos endpoints protegidos.

## Endpoints
### 1) Cadastrar Agência
`POST /desafio/cadastrar`
```bash
curl -X POST http://localhost:8080/desafio/cadastrar   -H "Authorization: Bearer <JWT>"   -H "Content-Type: application/json"   -d '{"posX":10,"posY":-5}'
```
Resposta (exemplo):
```json
{
  "id": 1,
  "posX": 10.0,
  "posY": -5.0,
  "nome": "AGENCIA_1"
}
```

### 2) Distâncias
`GET /desafio/distancia?posX=-10&posY=5`
```bash
curl -s "http://localhost:8080/desafio/distancia?posX=-10&posY=5"   -H "Authorization: Bearer <JWT>"
```
Resposta (exemplo):
```json
[
  {"agencia_id":"2","distancia":2.2360679775},
  {"agencia_id":"1","distancia":10.0}
]
```

## Cache (Caffeine)
- Cache **local** denominado `agencia`.
- **Máximo de 10 objetos** (LRU).
- **Expiração por acesso**: 5 minutos após o último acesso.
- O método `buscarPorIdCache` popula o cache; o cálculo de distâncias usa esse método para garantir a política de cache.

## Observações
- Política de cache alinhada às instruções do desafio: itens representam **agências**; limite 10; expiração 5 min após acesso.
- O Token gerado no endpoint de autenticação tem validade de uma hora.
- JWT emitido e validado localmente, com chaves RSA.
- H2 em memória para facilitar execução.
