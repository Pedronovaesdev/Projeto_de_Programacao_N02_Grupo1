# Comandos cURL para Windows - API de Usuários

## Base URL
```
http://localhost:8080
```

## 1. Criar Usuário (POST /user)

### Criar um Estudante (Windows PowerShell)
```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d "{\"name\": \"João Silva\", \"email\": \"joao.silva@email.com\", \"password\": \"senha123\", \"phone\": \"11999887766\", \"specialty\": null, \"birthDate\": \"1995-05-15\", \"role\": \"STUDENT\", \"accountValue\": 0.0, \"registration\": \"2024001\", \"teacherRegistration\": null}"
```

### Criar um Instrutor (Windows PowerShell)
```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d "{\"name\": \"Maria Santos\", \"email\": \"maria.santos@email.com\", \"password\": \"senha456\", \"phone\": \"11888776655\", \"specialty\": \"Programação Java\", \"birthDate\": \"1985-08-20\", \"role\": \"INSTRUCTOR\", \"accountValue\": 5000.0, \"registration\": null, \"teacherRegistration\": \"PROF2024001\"}"
```

### Criar um Administrador (Windows PowerShell)
```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d "{\"name\": \"Carlos Admin\", \"email\": \"carlos.admin@email.com\", \"password\": \"admin123\", \"phone\": \"11777665544\", \"specialty\": \"Gestão de Sistemas\", \"birthDate\": \"1980-12-10\", \"role\": \"ADMIN\", \"accountValue\": 10000.0, \"registration\": null, \"teacherRegistration\": \"ADM2024001\"}"
```

## 2. Buscar Usuário por Email (GET /user)

```powershell
curl -X GET "http://localhost:8080/user?email=joao.silva@email.com" -H "Content-Type: application/json"
```

## 3. Listar Todos os Usuários (GET /user/all)

```powershell
curl -X GET http://localhost:8080/user/all -H "Content-Type: application/json"
```

## 4. Atualizar Usuário (PUT /user/{id})

```powershell
curl -X PUT http://localhost:8080/user/1 -H "Content-Type: application/json" -d "{\"name\": \"João Silva Atualizado\", \"email\": \"joao.silva.novo@email.com\", \"password\": \"novaSenha123\", \"phone\": \"11999887799\", \"specialty\": \"Desenvolvimento Web\", \"birthDate\": \"1995-05-15\", \"role\": \"STUDENT\", \"accountValue\": 100.0, \"registration\": \"2024001\", \"teacherRegistration\": null}"
```

## 5. Atualizar Role do Usuário (PATCH /user/{id}/role)

### Promover usuário para INSTRUCTOR
```powershell
curl -X PATCH "http://localhost:8080/user/1/role?role=INSTRUCTOR" -H "Content-Type: application/json"
```

### Promover usuário para ADMIN
```powershell
curl -X PATCH "http://localhost:8080/user/1/role?role=ADMIN" -H "Content-Type: application/json"
```

## 6. Deletar Usuário por Email (DELETE /user)

```powershell
curl -X DELETE "http://localhost:8080/user?email=joao.silva@email.com" -H "Content-Type: application/json"
```

## 7. Deletar Usuário por ID (DELETE /user/{id})

```powershell
curl -X DELETE http://localhost:8080/user/1 -H "Content-Type: application/json"
```

---

## Alternativa: Usando arquivos JSON (Recomendado para Windows)

### Criar arquivo JSON para estudante
Crie um arquivo `estudante.json`:
```json
{
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "password": "senha123",
  "phone": "11999887766",
  "specialty": null,
  "birthDate": "1995-05-15",
  "role": "STUDENT",
  "accountValue": 0.0,
  "registration": "2024001",
  "teacherRegistration": null
}
```

### Usar o arquivo com curl:
```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d @estudante.json
```

### Criar arquivo JSON para instrutor
Crie um arquivo `instrutor.json`:
```json
{
  "name": "Maria Santos",
  "email": "maria.santos@email.com",
  "password": "senha456",
  "phone": "11888776655",
  "specialty": "Programação Java",
  "birthDate": "1985-08-20",
  "role": "INSTRUCTOR",
  "accountValue": 5000.0,
  "registration": null,
  "teacherRegistration": "PROF2024001"
}
```

### Usar o arquivo com curl:
```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d @instrutor.json
```

### Criar arquivo JSON para admin
Crie um arquivo `admin.json`:
```json
{
  "name": "Carlos Admin",
  "email": "carlos.admin@email.com",
  "password": "admin123",
  "phone": "11777665544",
  "specialty": "Gestão de Sistemas",
  "birthDate": "1980-12-10",
  "role": "ADMIN",
  "accountValue": 10000.0,
  "registration": null,
  "teacherRegistration": "ADM2024001"
}
```

### Usar o arquivo com curl:
```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d @admin.json
```

## Testando com PowerShell (Alternativa ao curl)

### Criar usuário usando Invoke-RestMethod:
```powershell
$body = @{
    name = "João Silva"
    email = "joao.silva@email.com"
    password = "senha123"
    phone = "11999887766"
    specialty = $null
    birthDate = "1995-05-15"
    role = "STUDENT"
    accountValue = 0.0
    registration = "2024001"
    teacherRegistration = $null
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/user" -Method POST -Body $body -ContentType "application/json"
```

### Listar todos os usuários:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/user/all" -Method GET -ContentType "application/json"
```

### Buscar usuário por email:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/user?email=joao.silva@email.com" -Method GET -ContentType "application/json"
```

## Notas para Windows:

1. **Escape de aspas**: No Windows cmd/PowerShell, use `\"` para escapar aspas dentro de strings JSON
2. **Arquivos JSON**: A abordagem mais limpa é criar arquivos .json e usar `-d @arquivo.json`
3. **PowerShell nativo**: Use `Invoke-RestMethod` como alternativa ao curl
4. **Quebras de linha**: No Windows, evite quebras de linha nos comandos curl ou use `^` no cmd

## Comando Corrigido para seu teste:

```powershell
curl -X POST http://localhost:8080/user -H "Content-Type: application/json" -d "{\"name\": \"João Silva\", \"email\": \"joao.silva@email.com\", \"password\": \"senha123\", \"phone\": \"11999887766\", \"specialty\": null, \"birthDate\": \"1995-05-15\", \"role\": \"STUDENT\", \"accountValue\": 0.0, \"registration\": \"2024001\", \"teacherRegistration\": null}"
```