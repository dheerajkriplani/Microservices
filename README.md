# Microservices

Spring Boot microservices demo covering **Eureka Server** (service registry), **OpenFeign** (declarative client) and **RestClient** (load-balanced HTTP client).

## Tech Stack

- Java 21 (min 17 )
- Spring Boot 4.1.1
- Spring Cloud 2025.1.3

## Services

| Service | Port | Role |
|---|---|---|
| registry_service | 8761 | Eureka Server |
| quiz_service | 8081 | Calls other services using Feign and RestClient |
| question_service | 8082 | Called by quiz_service through Feign |
| report_service | 8083 | Called by quiz_service through Feign / RestClient |
| api_gateway | 9090 | Single entry point, routes requests to the services |

Start order: `registry_service` first, then the other services.

---

## 1. Eureka Server (registry_service)

### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```

Spring Cloud version management (add to every service that uses Spring Cloud):

```xml
<properties>
    <java.version>21</java.version>
    <spring-cloud.version>2025.1.3</spring-cloud.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Main class

```java
@SpringBootApplication
@EnableEurekaServer
public class RegistryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryServiceApplication.class, args);
    }
}
```

### application.properties

```properties
spring.application.name=registry-service
server.port=8761

# disable as client
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Dashboard: http://localhost:8761

---

## 2. Register Services with Eureka (Eureka Client)

Used by `quiz_service`, `question_service` and `report_service`.

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

### application.properties

```properties
spring.application.name=quiz-service
server.port=8081

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

The service name in `spring.application.name` is the name other services use to call it.

---

## 3. Feign Client (quiz_service)

### Dependency

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### Enable Feign

```java
@SpringBootApplication
@EnableFeignClients
public class QuizServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizServiceApplication.class, args);
    }
}
```

### Feign interfaces

```java
// load-balanced through Eureka, using the service name
@FeignClient(name = "question-service")
public interface QuestionClient {

    @GetMapping("/question/getByQuizId/{quizId}")
    List<Question> getQuestionsOfQuiz(@PathVariable Long quizId);
}
```

```java
@FeignClient(name = "report-service")
public interface ReportClient {

    @GetMapping("/report/getByQuizId/{quizId}")
    Report getReportOfQuiz(@PathVariable Long quizId);
}
```

Without Eureka, you can call a fixed URL instead:

```java
@FeignClient(url = "http://localhost:8082", name = "question-client")
```

### Usage

```java
@Autowired
private QuestionClient questionClient;

quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
```

---

## 4. RestClient (quiz_service)

`RestClient` is part of Spring Web, so no extra dependency is needed. `spring-cloud-starter-loadbalancer` (see section 2) is required for `@LoadBalanced`.

### Config

```java
@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
```

### application.properties

```properties
# with @LoadBalanced, use the Eureka service name instead of localhost
report.service.url=http://report-service/report
```

### Usage

```java
@Service
public class ReportRestClient {

    @Autowired
    private RestClient restClient;

    @Value("${report.service.url}")
    private String reportServiceUrl;

    public Report getReportOfQuiz(Long quizId) {
        return restClient.get()
                .uri(reportServiceUrl + "/getByQuizId/{quizId}", quizId)
                .retrieve()
                .body(Report.class);
    }
}
```

---

## 5. API Gateway (api_gateway)

### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway-server-webmvc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

Add the Spring Cloud `dependencyManagement` block from section 1.

### Main class

```java
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

### application.properties

```properties
spring.application.name=api_gateway
server.port=9090

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

spring.cloud.gateway.server.webmvc.routes[0].id=quiz-service
spring.cloud.gateway.server.webmvc.routes[0].uri=lb://quiz-service
spring.cloud.gateway.server.webmvc.routes[0].predicates[0]=Path=/quiz/**

spring.cloud.gateway.server.webmvc.routes[1].id=question-service
spring.cloud.gateway.server.webmvc.routes[1].uri=lb://question-service
spring.cloud.gateway.server.webmvc.routes[1].predicates[0]=Path=/question/**

spring.cloud.gateway.server.webmvc.routes[2].id=report-service
spring.cloud.gateway.server.webmvc.routes[2].uri=lb://report-service
spring.cloud.gateway.server.webmvc.routes[2].predicates[0]=Path=/report/**
```

`lb://service-name` tells the gateway to look the service up in Eureka and load-balance across its instances.

Example calls through the gateway:

- `GET http://localhost:9090/quiz/getAll`
- `GET http://localhost:9090/question/getByQuizId/1`
- `GET http://localhost:9090/report/getByQuizId/1`

---

## Feign vs RestClient

| | Feign | RestClient |
|---|---|---|
| Style | Declarative interface | Fluent code |
| Extra dependency | `spring-cloud-starter-openfeign` | None (part of Spring Web) |
| Annotation to enable | `@EnableFeignClients` | `@LoadBalanced` on the bean |
| Service discovery | Uses `name = "service-name"` | Uses `http://service-name/...` in the URL |

---

## Run

1. Start `registry_service` and open http://localhost:8761
2. Start `question_service`, `report_service` and `quiz_service`
3. Start `api_gateway`
4. Confirm all services show as registered on the Eureka dashboard
5. Call `GET http://localhost:9090/quiz/getAll` (through the gateway) or `GET http://localhost:8081/quiz/getAll` (directly)
