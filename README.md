
# Enterprise Identity & Data Governance Platform Showcase

# 1. Project Overview
This project is a high-performance distributed Identity & Access Management (IAM) and Data Governance platform inspired by large-scale enterprise systems. It implements a robust Role-Based Access Control (RBAC) model designed to manage organizational hierarchies, user permissions, and comprehensive system auditing.

**Disclaimer:** This project is a personal reference implementation inspired by industry best practices and is not affiliated with any past or present employers.

# 2. Business Context
**Organizations increasingly struggle with:**
- Enforcing data access policies across multiple teams and applications
- Tracking and auditing who accessed what data, when, and for what purpose
- Ensuring compliance with regulatory standards (GDPR, SOX, HIPAA)

**This platform solves these challenges by:**
- Centralizing user identity management
- Managing roles, permissions, and hierarchical access control
- Enforcing data access policies at runtime
- Providing auditable logs for all user actions

# 3. Key Technical Features
## 3.1. Robust RBAC & Organizational Hierarchy
- **Departmental Management:** Implements tree-based structures for organizing complex departmental hierarchies.
- **Granular Permission Control:** Supports fine-grained Access Control Lists (ACLs) categorized by functional modules.
- **Constraint Validation:** Employs strictly typed parameter objects for data integrity across all entities.

## 3.2. Enterprise Auditing & Logging
- **State Tracking:** Captures "Before" and "After" snapshots for all entity modifications to support security compliance.
- **Automated Log Management:** Implements a professional logging strategy with automated rotation, timestamping, and GZ compression for long-term storage.


# 4. Architecture & Technical Stack

## 4.1 Architectural Design
The project follows a clean, layered architecture to ensure maintainability and scalability:
- **Presentation (Controller Layer):** Spring MVC Controllers handling HTTP and Swagger annotations.
- **Business Logic (Service Layer):** Encapsulates business logic, validation, and audit logging.
- **Data Access (DAO/MapperLayer):** MyBatis XML Mappers for optimized SQL execution and flexible data modeling.
- **Security Context:** Custom RequestHolder to manage thread-local user sessions.
- using different POJOs (Model, DTO, Bean, Param) help decouple the database layer from the UI layer


```azure
+----------------------+       +----------------------+
|  Client / Frontend   | <---> |  Spring Boot API     |
+----------------------+       +----------------------+
                                   |
                                   v
                          +----------------------+
                          |  Service Layer       |
                          | - Role Management    |
                          | - Permission Checks  |
                          | - Audit Logging      |
                          +----------------------+
                                   |
                   +----------------+----------------+
                   |                                 |
          +----------------+               +----------------+
          |  MySQL DB      |               |  Redis Cache    |
          | - Users        |               | - Session Mgmt  |
          | - Roles        |               | - Hot Data      |
          | - Permissions  |               +----------------+
          | - Audit Logs   |
          +----------------+

```

## 4.2 Tech Stack
- **Programming:** Java 17, SQL, JavaScript, jQuery, Ajax
- **Core Framework:** Spring Boot 3.2.6.
- **Persistence:** MyBatis 3.0.5 with high-performance XML-based mapping
- **Caching:** Redis (Jedis) for session and permission caching.
- **Utilities:** Google Guava, Lombok, Jackson.
- **Validation:** Hibernate Validator (JSR-303).
- **Testing:** JUnit 5, Mockito, MockMvc, and AssertJ.
- **Logging:** SLF4J with Logback-spring configuration for environment-aware logging

# 5. Project Configuration & Quality Assurance
## 5.1 Environment Management
The platform utilizes Spring Profiles for seamless transitions between development, testing, and production environments:
- **application-dev.yml:** Local development settings.
- **application-test.yml:** Specialized configurations for automated test runs.
- **application-prod.yml:** Hardened settings for production-grade deployments.

## 5.2 Professional Logging Strategy
System logs are meticulously managed and stored in the logs/ directory:
- **Active Logs:** Real-time tracking in `data-governance-platform-info.log`.
- **Archived Logs:** Historic logs are automatically rotated, timestamped, and compressed (e.g., `info-2026-02-19.0.log.gz`) to optimize disk space while maintaining an audit trail.

## 5.3 Multi-Tiered Testing Strategy
This project demonstrates a "test-first" engineering mindset with high test coverage. Reliability is enforced through a multi-tiered testing strategy:
- **Unit Tests:** Comprehensive mocking of service layers using Mockito. Intensive use of JUnit 5 and Mockito to isolate business logic.
- **Integration Tests:** Data-layer testing using @MybatisTest and in-memory H2 databases.
- **Web Layer Testing:** Utilizing MockMvc to verify endpoint security and response formats.
- **Bean Validation:** Independent testing of complex JSR-303 constraints in DTO/Param objects.
- **Concurency Testing TODO:** Conduct stress testing with JMeter and perform JVM performance tuning to ensure stability under high concurrency

# 6. How to Run
## Way 1. Local Execution
1. Clone repository: git clone https://github.com/yourusername/enterprise-identity-platform.git

2. Configure: Set up database and Redis in application.yml.

3. Run: mvn spring-boot:run
4. Monitoring: Access Druid at http://localhost:8082/druid/ (User: druid, Pin: 123456).
5. API UI: Explore via http://localhost:8082/swagger-ui.html.


## Quick Tips for Windows Users
`mvnw.cmd` is a Maven Wrapper script for Windows. This script is a brilliant little tool designed to let us run a Maven project without actually having Maven installed on your machine.
The Maven Wrapper is a way to bundle a specific version of Maven with the project. This ensures that every developer on your team (and your CI/CD pipeline) uses the exact same version of Maven, avoiding the "it works on my machine" headache.

###  Common Use Cases

| Goal                   | Command               |
|------------------------|-----------------------|
| Build the project      | mvnw clean install    |
| Run a Spring Boot app  | mvnw spring-boot:run  |
| Check Maven version    | mvnw -v               |


- **Permission Issues:** Sometimes the script might not run if your execution policy is strict, though .cmd files usually bypass the hurdles that .sh or .ps1 files face.

- **Missing Java:** If you get the error JAVA_HOME not found, you'll need to set your environment variable to point to your JDK folder (e.g., C:\Program Files\Java\jdk-17).

- **Proxy Issues:** If you are behind a corporate firewall and mvnw fails to download Maven, you may need to configure proxy settings in your .mvn/wrapper/maven-wrapper.properties file.

### 🛠 Troubleshooting mvnw Errors
If you run `mvnw clean install` or `./mvnw clean install` and encounter the following error:

Error: Could not find or load main class org.apache.maven.wrapper.MavenWrapperMain

**Why this happens:**
The mvnw script exists, but the essential .mvn/wrapper/maven-wrapper.jar file is missing. This often happens if .jar files are included in your .gitignore or if the folder wasn't included in the project copy.

**The Fix:** <br>
If you have Maven installed locally, run this command in your project root to regenerate the wrapper files: `mvn wrapper:wrapper`

**Note:** To prevent this in the future, ensure your .gitignore allows the wrapper JAR:
```azure
# Allow the Maven Wrapper executable
!.mvn/wrapper/maven-wrapper.jar
```

## Way 2. Running the Platform with Docker
This project is fully containerized to ensure a consistent runtime environment across development and production stages.

**1. Prerequisites**
- Docker Engine 20.10+ and Docker Compose V2.
- JDK 17 (The Docker multi-stage build will handle the compilation, so you don't need Maven installed locally).

**2. Standard Deployment**

Run the following command in the project root to build the images and start the cluster in detached mode:
```azure
# Build images and start all services (App, MySQL, Redis)
docker compose up --build -d
```

**3. Service Verification**
Once the containers are running, you can verify their health and connectivity:
- **API & Service Health:** Access the Swagger UI. If this page loads and allows you to "Try it out" on an endpoint (e.g., /sys/user/list.json), the Spring context and Service layer are healthy. 
URL: http://localhost:8082/swagger-ui.html

- **Database & Connection Pool:** Access the Druid Monitor Console. This provides real-time statistics on your connection pool and SQL execution.
 URL: http://localhost:8082/druid/index.html (User: druid / Pass: 123456)

- **Redis Status:** Test the cache connectivity. Use the Docker CLI to ping the cache:
```azure
docker compose exec redis redis-cli ping
```