#  Auth Infra

**A Plug-and-Play Authentication Infrastructure for Spring Boot**

Auth Infra is a modular authentication engine built using Hexagonal Architecture (Ports & Adapters) and exposed as a Spring Boot Starter. It eliminates repetitive authentication boilerplate so you can focus on business logic instead of authentication plumbing.

[![JitPack](https://jitpack.io/v/ayushy738/auth-infra.svg)](https://jitpack.io/#ayushy738/auth-infra)

---

##  Project Goal

Modern backend applications repeatedly reimplement the same authentication infrastructure:

- Configure Spring Security
- Write JWT generation & validation
- Manage refresh tokens
- Handle logout via token revocation
- Implement password encoding
- Wire everything with dependency injection

**This leads to:**
- Duplicate code
- Security mistakes
- Slower MVP development
- Inconsistent implementations across projects

**Auth Infra solves this by providing a reusable, configurable authentication engine that works out-of-the-box.**

> 1. Add one dependency
> 2. Configure three properties
> 3. Get production-ready JWT authentication

---

##  Features

| Feature | Description |
|---------|-------------|
|  **JWT-Based Authentication** | Stateless access tokens with HMAC signing and configurable expiration |
|  **Refresh Token Rotation** | Secure refresh flow with automatic token replacement |
|  **Token Blacklist** | Immediate token invalidation for secure logout |
|  **Password Security** | BCrypt password encoding and secure matching |
|  **Audit Logging** | Track login success/failure with extensible logging interface |
|  **Fully Configurable** | Customize secret key, token expiration, and more |
|  **Plug & Play** | In-memory defaults included, no database required |

---

##  Architecture Overview

Auth Infra follows **Clean Architecture** principles with framework-independent core logic.

```
Application
    ↓
Spring Boot Auto Configuration
    ↓
Auth Infra Starter
    ↓
Core Engine (Pure Java)
```

### Module Structure

```
auth-infra/
├── engine-core              # Pure Java domain & ports
├── engine-spring-adapter    # Optional runtime app module
└── engine-spring-starter    # Spring Boot auto-config starter
```

### Design Principles

-  Framework-independent core logic
-  Dependency inversion (ports & adapters)
-  Default implementations with override capability
-  Auto-configured infrastructure
-  Minimal required setup

---

##  Installation

### Step 1 — Add JitPack Repository

Add the following to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Step 2 — Add Dependency

```xml
<dependency>
    <groupId>com.github.ayushy738.auth-infra</groupId>
    <artifactId>engine-spring-starter</artifactId>
    <version>v1.0.3</version>
</dependency>
```


---

##  Configuration

Add the following to your `application.yml`:

```yaml
engine:
  security:
    jwt-secret: your-very-long-secure-secret-key
    access-expiration: 900000        # 15 minutes
    refresh-expiration: 604800000    # 7 days
```

**That's it.** No additional setup required.

---

##  Usage

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/register` | Register new user |
| `POST` | `/auth/login` | Authenticate user |
| `POST` | `/auth/refresh` | Refresh access token |
| `POST` | `/auth/logout` | Invalidate token |

### Protecting Routes

Create a controller:

```java
@RestController
public class TestController {

    @GetMapping("/secure")
    public String secure() {
        return "Authenticated access";
    }
}
```

Access protected routes using:

```
Authorization: Bearer <access_token>
```

---

##  Default Implementations

Auth Infra provides **default in-memory implementations** for:

- `UserRepositoryPort`
- `RefreshTokenRepositoryPort`
- `TokenBlacklistPort`
- `AuditLogPort`
- `TokenServicePort`
- `PasswordEncoderPort`

All are registered using `@ConditionalOnMissingBean`, meaning:

> **If you provide your own implementation → it automatically overrides the default.**

---

##  Customizing Persistence

To replace in-memory storage with a database:

```java
@Bean
public UserRepositoryPort userRepositoryPort() {
    return new JpaUserRepositoryAdapter();
}
```

The starter will automatically use your implementation.

---

##  How It Works

### Security Flow

1. **User registers** → Password is encoded using BCrypt
2. **Access + refresh tokens** are generated
3. **Protected routes** validate access token
4. **Logout** blacklists access token
5. **Refresh endpoint** rotates tokens securely

### Core Layer

- Pure Java
- No Spring dependencies
- Contains domain logic and service layer

### Starter Layer

- Auto-configures beans
- Registers default adapters
- Wires `AuthService`
- Loads configuration properties

---

##  Development

### Build Project

```bash
mvn clean install
```

### Publish New Version

```bash
git tag v1.0.X
git push origin v1.0.X
```

JitPack builds automatically.

---

##  Use Cases

-  Rapid MVP development
-  Hackathon backend setup
-  Microservice authentication base
-  Educational clean architecture example
-  Internal platform standardization

---

##  Production Considerations

For production environments:

- Replace in-memory storage with persistent database adapters
-  Use strong secret key (at least 256 bits)
-  Use HTTPS only
-  Configure token expiration according to security policy
-  Add rate limiting if required
-  Implement proper monitoring and alerting

---

##  Why This Project Matters

Auth Infra demonstrates:

- Clean Architecture principles
- Modular system design
- Dependency inversion
- Framework-level thinking
- Spring Boot auto-configuration
- Reusable backend infrastructure

**This is not just an authentication system.**  
**It is an authentication infrastructure component.**

---

##  Author

**Ayush Yadav**  
Backend & Infrastructure Engineering

---

##  License

This project is licensed under the [MIT License](LICENSE).

---

##  Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

##  Support

If you have any questions or need help, please:

- Open an issue on GitHub
- Check the documentation
- Reach out to the maintainer

---

<div align="center">

** If you find this project useful, please consider giving it a star! ⭐**

Made with ❤️ by Ayush Raj Yadav

</div>