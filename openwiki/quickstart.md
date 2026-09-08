---
type: "Reference"
title: "Quickstart"
openwiki_generated: true
verified:
  - by: openwiki/0.5.0
    at: 2026-09-07T22:46:42.500Z
sources:
  - id: openwiki-source-5617d4b443aa7ea3d8fa4fcc
    resource: repo://engine-core/pom.xml
  - id: openwiki-source-7df8d76706759c7bf0d798be
    resource: repo://engine-core/src/main/java/com/engine/core/services/AuthService.java
  - id: openwiki-source-7588d564576eb2ce47df29a4
    resource: repo://engine-spring-starter/pom.xml
  - id: openwiki-source-8824ed3698f2cc7fdcc334e7
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/config/AuthControllerConfiguration.java
  - id: openwiki-source-adde509158bc4ae30339f9e5
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/config/EngineSecurityBeansConfiguration.java
  - id: openwiki-source-e7cf5c508d7b6d152eb9bf7b
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/config/EngineSecurityProperties.java
  - id: openwiki-source-07a111ca94922c0a12a51e54
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/config/EngineWebSecurityAutoConfiguration.java
  - id: openwiki-source-ee0eae126de214e0958c4aac
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/config/SecurityConfig.java
  - id: openwiki-source-168295d5b03551ac77dc6801
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/controllers/AuthController.java
  - id: openwiki-source-3ae2303b27ea751422d61dbf
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/EngineAutoConfiguration.java
  - id: openwiki-source-c1ccae559d85b5a3e7bb26f6
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/EngineSecurityProperties.java
  - id: openwiki-source-6bf36413381110b179e03a43
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/security/JwtAuthenticationFilter.java
  - id: openwiki-source-5e621c9e06b617dc2780151f
    resource: repo://engine-spring-starter/src/main/java/com/engine/starter/security/JwtServiceAdapter.java
  - id: openwiki-source-0d856304b5892d49fdbbdb9b
    resource: repo://engine-spring-starter/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
  - id: openwiki-source-2355f81d7cf522f8dbdaabd4
    resource: repo://pom.xml
  - id: openwiki-source-23775c3de52f3ab95a13cb8b
    resource: repo://README.md
generated: { by: "openwiki/0.5.0", at: "2026-09-07T22:46:42.500Z" }
---


Auth Infra is a two-module Maven reactor: `engine-core` owns framework-free authentication orchestration and ports, while `engine-spring-starter` depends on it and supplies Spring Boot auto-configuration, web/security wiring, JWT adapters, and JPA types. Start at the root with:

```bash
mvn clean install
```

The reactor targets Java 17 and manages Spring Boot 3.2.5 dependencies. There is no separate `engine-spring-adapter` Maven module in the current reactor; that name in the README is not a module map to follow.

## Choose the task, then read the boundary

| If you need to… | Start here | Then follow |
|---|---|---|
| Understand ownership, add a port, or replace an adapter | [Core–Starter System Boundaries](architecture/system-boundaries.md) | [Authentication Domain and Port Contracts](concepts/auth-domain-and-ports.md) |
| Integrate the starter or change HTTP/Spring Security behavior | [Spring Boot Starter Integration](integrations/spring-boot-starter.md) | [Configuration, Persistence, and Production Operation](operations/configuration-and-persistence.md) |
| Change registration, refresh, request authentication, or logout semantics | [Registration, Refresh, and Request Authentication Workflows](workflows/token-lifecycle.md) | [Authentication Domain and Port Contracts](concepts/auth-domain-and-ports.md) |
| Configure secrets, public routes, persistence, or deployment | [Configuration, Persistence, and Production Operation](operations/configuration-and-persistence.md) | [Validation Strategy and Known Integration Gaps](testing/validation-and-known-gaps.md) |
| Make a security-sensitive change or prepare a release | [Validation Strategy and Known Integration Gaps](testing/validation-and-known-gaps.md) | the workflow and integration pages implicated by the change |

## Maven module map

| Module | Role | What a safe change preserves |
|---|---|---|
| root `auth-infra` | Parent POM and reactor. | Both modules remain buildable together under the root-managed Java and Boot versions. |
| `engine-core` | Plain Java domain objects, port interfaces, and `AuthService` orchestration. It declares no dependencies of its own. | Core code depends on port contracts, not Spring or persistence implementations. |
| `engine-spring-starter` | Depends on `engine-core`; brings Spring Boot web, security, JPA, H2, and JJWT dependencies. | Framework adapters stay outside core and are assembled through the starter's registered auto-configurations. |

For a core behavior change, trace the port contract into every default or JPA adapter before editing the service. For a starter change, trace from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` into the imported configuration and then into the consuming application's override points. This is more reliable than treating package scanning or README endpoint lists as the integration contract.

## Actual integration surface: verify before relying on it

The auto-configuration imports register `EngineAutoConfiguration`, `AuthControllerConfiguration`, `EngineSecurityBeansConfiguration`, and `EngineWebSecurityAutoConfiguration`. `EngineAutoConfiguration` supplies in-memory user, refresh-token, blacklist, audit, and password ports when missing, and constructs `AuthService` only when all six ports—including `TokenServicePort`—are available. The web-only security beans configuration supplies the token service, JWT filter, `UserDetailsService`, and JPA-oriented adapters conditionally; the web-security configuration imports the `SecurityFilterChain`.

The controller actually exposes `POST /auth/register`, `POST /auth/refresh`, and `GET /auth/health`. It does **not** declare the README-advertised `/auth/login` or `/auth/logout` endpoints. `/auth/**` is public by default, while the installed security chain is stateless, runs the JWT filter before username/password authentication, requires authentication for other requests, and reserves `/admin/**` for `ADMIN`. Review the integration page before adding an endpoint: route publication, security policy, exception mapping, and service support are separate concerns.

> **Current-source warning:** do not assume the starter is plug-and-play in a production web application. The imported configurations have ordering and type inconsistencies: the initial auto-configuration registers in-memory ports before the later JPA adapter conditions are evaluated; the `JwtServiceAdapter` returns a UUID refresh token even though `AuthService.refresh` extracts a signed-token subject; and two different `EngineSecurityProperties` classes exist in different packages. Treat the detailed operations and validation pages as required reading before selecting persistence or token behavior.

## Safest reading order for a change

1. **Establish the boundary.** Read [Core–Starter System Boundaries](architecture/system-boundaries.md), then identify whether the change belongs in `AuthService`/a port or in a Spring adapter.
2. **Follow the state transition.** For anything involving credentials or tokens, read [Registration, Refresh, and Request Authentication Workflows](workflows/token-lifecycle.md). Preserve its checks: registration rejects an existing email, refresh requires a stored unexpired, unrevoked token and revokes it before issuance, and request authentication consults the blacklist.
3. **Trace the runtime assembly.** Read [Spring Boot Starter Integration](integrations/spring-boot-starter.md) from the imports file through bean conditions, controller, filter, and chain. Confirm what is registered in the target application rather than inferring it from a class in the repository.
4. **Check operational ownership.** Read [Configuration, Persistence, and Production Operation](operations/configuration-and-persistence.md) for property binding, signing key/expiry behavior, state durability, adapter overrides, JPA discovery, and public-path exposure.
5. **Define regression evidence before editing.** Finish with [Validation Strategy and Known Integration Gaps](testing/validation-and-known-gaps.md). The repository has no automated test sources, so add focused coverage for the changed branch and manually exercise the affected HTTP and security paths.

## Minimal operator and integrator checklist

- Supply a non-null `engine.security.jwt-secret` and deliberate access and refresh expirations; JWT signing derives its HMAC key directly from that value.
- Decide whether each state owner—users, refresh tokens, blacklisted access tokens, and audit records—may be in memory. Defaults are process-local; durability and cleanup are an application responsibility unless a correctly selected persistent adapter supplies them.
- Override port beans intentionally and verify conditional bean selection. In particular, inspect the resolved `TokenServicePort`, repositories, `UserDetailsService`, JWT filter, and `SecurityFilterChain` in a real web context.
- Test the actual controller contract, not the README: registration, refresh, and health are the exposed starter routes. Add application endpoints/adapters for login or logout if those workflows are required.
- For protected-request tests, cover no bearer header, malformed/invalid token, blacklisted token, valid token, and the `/admin/**` authority rule; ensure a refresh token can be parsed by the selected token service before accepting rotation as working.

This page is a routing map. Detailed behavior, configuration, and known defects live in the linked pages so changes can be reviewed at the boundary they affect.
