graph TD
    Client[Mobil GUI / İstemci] -->|REST / JSON| Gateway[API Gateway - Kong]
    
    Gateway -->|/api/users| UserService[User Service]
    Gateway -->|/api/cargo| CargoService[Cargo Service]
    
    UserService -->|JDBC| UserDB[(PostgreSQL - Users)]
    
    CargoService -->|JDBC| CargoDB[(PostgreSQL - Cargos)]
    CargoService -->|NoSQL| RedisCache[(Redis - Anlık Takip/Cache)]
