# CatWalk

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Paper](https://img.shields.io/badge/Paper-1.21.4+-blue.svg)](https://papermc.io/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-green.svg)](https://gradle.org/)

**CatWalk** is a REST API gateway and communication bridge for Minecraft (Paper) servers. Built on top of [Javalin](https://javalin.io/), it allows you to expose server endpoints, stream console output via WebSockets, and route API requests across multiple servers using a database-backed message broker architecture.

It is designed as a successor and modern rewrite of [ServerTap](https://github.com/ServerTap/ServerTap).

---

## 🚀 Modes of Operation

CatWalk can be deployed in three modes (configured in `config.yml` via `hub.enabled`):

| Mode | Configuration | Description |
|---|---|---|
| **Standalone** | `hub.enabled: "standalone"` | Runs as a direct REST API on the server. Does not require a database. |
| **Hub Gateway** | `hub.enabled: true` | Serves as the single public entry point for all API requests. Discovers and routes requests to backend servers via database polling. |
| **Backend Server** | `hub.enabled: false` | Polls the database for incoming requests routed from the Hub, executes them locally on the Paper server, and posts responses back. |

---

## ✨ Features

- **Automatic Routing**: Hub gateways dynamically discover backend servers, their registered addons, and paths, creating unified proxy endpoints (e.g. `/v1/servers/{serverId}/...`).
- **Dynamic Addon API**: Third-party plugins can register custom routes and HTTP handlers at runtime using the `CatWalkWebserverService` service.
- **REST & OpenAPI**: Out-of-the-box OpenAPI 3.0 specification generation with Swagger UI served at `/swagger`.
- **WebSocket Console**: Live server console log streaming.
- **Security**: Built-in API key auth (`useKeyAuth`), whitelisted/blocked path rules, SSL/TLS configurations, and CORS controls.

---

## ⚡ Quick Start

### Prerequisites
- **Java 25** or higher
- **PaperMC 1.21.4** or higher
- **MariaDB / MySQL** (Required for Hub and Backend modes; not needed for Standalone)

### 1. Build from Source
```bash
git clone https://github.com/ikeepcalm/catwalk
cd catwalk
./gradlew shadowJar
```

### 2. Install & Configure
1. Copy the generated JAR from `build/libs/CatWalk-*.jar` to your server's `plugins/` directory.
2. Start the server once to generate the default configuration, then stop it.
3. Configure `plugins/CatWalk/config.yml`.

#### Configuration Example (Hub / Backend Mode)
```yaml
port: 4567
key: 'your-secure-api-key'

hub:
  enabled: true # true for gateway hub, false for backend server
  server-id: "lobby" # unique identifier for this server

database:
  host: "localhost"
  port: 3306
  name: "catwalk_network"
  username: "catwalk"
  password: "your_password"
```

For **Standalone** mode:
```yaml
hub:
  enabled: "standalone"
  server-id: "my-standalone-server"
```

4. Start your server. CatWalk will automatically initialize the database schema (if not in Standalone mode) and start the web server on the specified port.

---

## 🛠️ Development

### Local Test Server
Run a local Paper development server pre-loaded with CatWalk:
```bash
./gradlew runServer
```
The test server files will be located in the `run/` directory.

### Running Tests
```bash
./gradlew test
```

---

## 🙏 Acknowledgments

- **PaperMC Team** for the Minecraft server platform.
- **Javalin** for the lightweight web framework.
- **ServerTap** for providing the original inspiration and codebase reference.

---

## 📞 Support & Links

- **Issues**: [GitHub Issues](https://github.com/ikeepcalm/catwalk/issues)
- **Discussions**: [GitHub Discussions](https://github.com/ikeepcalm/catwalk/discussions)
- **Documentation**: [Wiki](https://github.com/ikeepcalm/catwalk/wiki)

---

Licensed under the [MIT License](LICENSE).