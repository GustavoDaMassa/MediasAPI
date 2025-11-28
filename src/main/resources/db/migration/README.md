# Database Migrations com Flyway

Este diretório contém todas as migrations de banco de dados da aplicação MediasAPI, gerenciadas pelo Flyway.

## 📁 Estrutura de Migrations

```
db/migration/
├── V1__baseline_schema.sql          # Schema inicial (baseline)
├── V2__example_add_column.sql       # Exemplo de migration futura
└── README.md                         # Esta documentação
```

## 📋 Convenção de Nomenclatura

As migrations seguem o padrão Flyway:

```
V{VERSION}__{DESCRIPTION}.sql
```

- **V**: Prefixo obrigatório (versioned migration)
- **{VERSION}**: Número da versão (1, 2, 3, 1.1, 2.1, etc.)
- **__**: Dois underscores separando versão e descrição
- **{DESCRIPTION}**: Descrição em snake_case
- **.sql**: Extensão do arquivo

### Exemplos Válidos:
- ✅ `V1__baseline_schema.sql`
- ✅ `V2__add_user_phone_column.sql`
- ✅ `V3__create_audit_table.sql`
- ✅ `V2.1__add_index_on_email.sql`

### Exemplos Inválidos:
- ❌ `V1_baseline.sql` (um underscore só)
- ❌ `v2__add_column.sql` (V minúsculo)
- ❌ `V2-add-column.sql` (hífens na descrição)
- ❌ `baseline.sql` (sem versão)

## 🔄 Como as Migrations Funcionam

### 1. Inicialização
Quando a aplicação inicia pela primeira vez:
- Flyway cria a tabela `flyway_schema_history`
- Se o banco já tem dados, executa **baseline** (marca como V1)
- Aplica migrations pendentes em ordem de versão

### 2. Execução de Migrations
- Migrations são executadas **uma única vez** por banco de dados
- Flyway calcula checksum (hash) de cada migration
- Se o checksum mudar, Flyway detecta alteração e falha (proteção)
- Migrations bem-sucedidas são registradas em `flyway_schema_history`

### 3. Validação
- No startup, Flyway valida todas as migrations aplicadas
- Compara checksums para detectar modificações
- Garante que o schema está sincronizado com as migrations

## 📝 Como Criar uma Nova Migration

### Passo 1: Criar o Arquivo
```bash
# Criar migration para adicionar coluna
touch src/main/resources/db/migration/V2__add_user_phone_column.sql
```

### Passo 2: Escrever o SQL
```sql
-- V2__add_user_phone_column.sql

-- Add phone column to users table
ALTER TABLE users ADD COLUMN phone VARCHAR(20);

-- Create index for phone lookups
CREATE INDEX idx_users_phone ON users(phone);

-- Update existing records with default value
UPDATE users SET phone = 'NOT_PROVIDED' WHERE phone IS NULL;
```

### Passo 3: Testar Localmente
```bash
# Rebuild do projeto
./mvnw clean install

# Ou rodar com Docker
docker compose down && docker compose up --build
```

### Passo 4: Verificar Aplicação
```sql
-- Conectar ao banco e verificar
SELECT * FROM flyway_schema_history;

-- Deve mostrar V2 aplicada
```

## 🚨 Regras Importantes

### ❌ NUNCA FAÇA:
1. **Modificar migration já aplicada em produção**
   - Flyway detecta mudança no checksum e FALHA
   - Crie uma nova migration V3 ao invés de modificar V2

2. **Deletar migrations aplicadas**
   - Flyway espera encontrar todas as migrations registradas
   - Se deletar, aplicação não sobe

3. **Usar ddl-auto=update ou create**
   - Agora usamos `ddl-auto=validate`
   - Flyway gerencia TODAS as mudanças de schema

4. **Fazer mudanças direto no banco**
   - Sempre crie migrations para mudanças
   - Mantém histórico e permite replicação

### ✅ SEMPRE FAÇA:
1. **Teste migrations em ambiente local primeiro**
   ```bash
   docker compose down -v  # Remove volumes
   docker compose up --build  # Testa do zero
   ```

2. **Use transações quando possível**
   ```sql
   -- Flyway executa em transação por padrão
   -- Rollback automático se falhar
   ```

3. **Adicione comentários descritivos**
   ```sql
   -- Description: Add support for phone authentication
   -- Author: Gustavo Henrique
   -- Date: 2025-11-28
   -- Ticket: JIRA-123
   ```

4. **Considere backward compatibility**
   ```sql
   -- Adicionar coluna como NULLABLE primeiro
   ALTER TABLE users ADD COLUMN status VARCHAR(20);

   -- Popular dados
   UPDATE users SET status = 'ACTIVE';

   -- Em migration futura, tornar NOT NULL
   -- V3__make_status_not_null.sql
   ALTER TABLE users MODIFY COLUMN status VARCHAR(20) NOT NULL;
   ```

## 🔍 Monitoramento e Debug

### Verificar Migrations Aplicadas
```sql
SELECT
    installed_rank,
    version,
    description,
    type,
    script,
    installed_on,
    execution_time,
    success
FROM flyway_schema_history
ORDER BY installed_rank;
```

### Logs do Flyway
```bash
# Ver logs da aplicação
docker compose logs -f mediasapi | grep -i flyway

# Buscar por erros de migration
docker compose logs mediasapi | grep -i "migration.*failed"
```

### Troubleshooting

#### Erro: "Validate failed: Migration checksum mismatch"
**Causa:** Migration foi modificada após ser aplicada

**Solução:**
```bash
# Opção 1: Reverter modificação da migration
git checkout HEAD -- src/main/resources/db/migration/V2__*.sql

# Opção 2: Reparar (APENAS EM DEV!)
# Conectar ao banco e executar:
# DELETE FROM flyway_schema_history WHERE version = '2';
# docker compose restart mediasapi
```

#### Erro: "Found non-resolved migration"
**Causa:** Migration pendente mas banco está marcado como versão posterior

**Solução:**
```bash
# Verificar todas as migrations no diretório
ls -la src/main/resources/db/migration/

# Comparar com flyway_schema_history
# Garantir sequência correta de versões
```

## 🌐 Deploy em Produção

### Checklist antes do Deploy:
- [ ] Todas as migrations testadas localmente
- [ ] Migrations testadas em staging
- [ ] Backup do banco de produção realizado
- [ ] Migration é backward compatible (app antiga funciona)
- [ ] CI/CD validou migrations
- [ ] Estimativa de tempo de execução conhecida

### Estratégia de Deploy:
1. **Backup automático** antes do deploy
2. **Aplicar migrations** antes de subir nova versão da app
3. **Validar schema** com `ddl-auto=validate`
4. **Rollback plan** definido (pode precisar de migration reversa)

### Exemplo de Migration Reversa:
```sql
-- V4__remove_phone_column.sql
-- Reverte mudanças da V2

ALTER TABLE users DROP COLUMN phone;
DROP INDEX idx_users_phone ON users;
```

## 📊 Histórico de Migrations

| Versão | Descrição | Data | Autor |
|--------|-----------|------|-------|
| V1 | Baseline schema (users, course, projection, assessment) | 2025-11-28 | System |

## 🔗 Referências

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Migration Naming](https://flywaydb.org/documentation/concepts/migrations#naming)
- [Best Practices](https://flywaydb.org/documentation/usage/bestpractices)
