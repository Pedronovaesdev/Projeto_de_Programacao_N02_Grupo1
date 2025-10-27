# Comandos cURL para Testar a API de Usuários

## Base URL
```
http://localhost:8080
```

## 1. Criar Usuário (POST /user)

### Criar um Estudante
```bash
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "password": "senha123",
    "phone": "11999887766",
    "specialty": null,
    "birthDate": "1995-05-15",
    "role": "STUDENT",
    "registration": "2024001",
    "teacherRegistration": null
  }'
```

### Criar um Instrutor
```bash
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Santos",
    "email": "maria.santos@email.com",
    "password": "senha456",
    "phone": "11888776655",
    "specialty": "Programação Java",
    "birthDate": "1985-08-20",
    "role": "INSTRUCTOR",
    "registration": null,
    "teacherRegistration": "PROF2024001"
  }'
```

### Criar um Administrador
```bash
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos Admin",
    "email": "carlos.admin@email.com",
    "password": "admin123",
    "phone": "11777665544",
    "specialty": "Gestão de Sistemas",
    "birthDate": "1980-12-10",
    "role": "ADMIN",
    "registration": null,
    "teacherRegistration": "ADM2024001"
  }'
```

## 2. Buscar Usuário por Email (GET /user)

```bash
curl -X GET "http://localhost:8080/user?email=joao.silva@email.com" \
  -H "Content-Type: application/json"
```

## 3. Listar Todos os Usuários (GET /user/all)

```bash
curl -X GET http://localhost:8080/user/all \
  -H "Content-Type: application/json"
```

## 4. Atualizar Usuário (PUT /user/{id})

```bash
curl -X PUT http://localhost:8080/user/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Atualizado",
    "email": "joao.silva.novo@email.com",
    "password": "novaSenha123",
    "phone": "11999887799",
    "specialty": "Desenvolvimento Web",
    "birthDate": "1995-05-15",
    "role": "STUDENT",
    "registration": "2024001",
    "teacherRegistration": null
  }'
```

## 5. Atualizar Role do Usuário (PATCH /user/{id}/role)

### Promover usuário para INSTRUCTOR
```bash
curl -X PATCH "http://localhost:8080/user/1/role?role=INSTRUCTOR" \
  -H "Content-Type: application/json"
```

### Promover usuário para ADMIN
```bash
curl -X PATCH "http://localhost:8080/user/1/role?role=ADMIN" \
  -H "Content-Type: application/json"
```

## 6. Deletar Usuário por Email (DELETE /user)

```bash
curl -X DELETE "http://localhost:8080/user?email=joao.silva@email.com" \
  -H "Content-Type: application/json"
```

## 7. Deletar Usuário por ID (DELETE /user/{id})

```bash
curl -X DELETE http://localhost:8080/user/1 \
  -H "Content-Type: application/json"
```

## Sequência de Testes Recomendada

1. **Primeiro, crie alguns usuários:**
   - Execute os 3 comandos POST para criar um estudante, instrutor e admin

2. **Liste todos os usuários:**
   - Execute o GET /user/all para ver todos os usuários criados

3. **Busque um usuário específico:**
   - Execute o GET /user com email de um dos usuários criados

4. **Atualize um usuário:**
   - Use o ID retornado na listagem para executar o PUT

5. **Atualize apenas o role:**
   - Execute o PATCH para alterar o role de um usuário

6. **Delete um usuário:**
   - Execute um dos comandos DELETE

## Notas Importantes

- **IDs**: Os IDs são gerados automaticamente. Use os IDs retornados nas consultas GET para os comandos PUT, PATCH e DELETE por ID.
- **Emails únicos**: Cada usuário deve ter um email único.
- **Roles disponíveis**: STUDENT, INSTRUCTOR, ADMIN
- **Campos obrigatórios**: name, email, password, phone, birthDate, registration (para estudantes), role
- **Campos opcionais**: specialty, teacherRegistration

## Exemplo de Resposta de Sucesso
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "phone": "11999887766",
  "birthDate": "1995-05-15",
  "registrionDate": "2024-01-15",
  "lastAccess": "2024-01-15T10:30:00",
  "registration": "2024001",
  "specialty": null,
  "teacherRegistration": null,
  "role": "STUDENT"
}
```

## Testando Erros

### Tentar criar usuário com email duplicado:
```bash
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Outro João",
    "email": "joao.silva@email.com",
    "password": "senha789",
    "phone": "11888999777",
    "birthDate": "1990-01-01",
    "role": "STUDENT",
    "registration": "2024002"
  }'
```

### Buscar usuário que não existe:
```bash
curl -X GET "http://localhost:8080/user?email=naoexiste@email.com" \
  -H "Content-Type: application/json"
```