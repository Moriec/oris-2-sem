import java.util.Properties

plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.2"
}

group = "com.vinogradov"
version = "1.0-SNAPSHOT"

val springVersion: String by project
val jakartaVersion: String by project
val hibernateVersion: String by project
val postgreVersion: String by project
val freemarkerVersion: String by project
val lombokVersion: String by project
val hikariVersion: String by project
val springDataVersion: String by project
val swaggerVersion: String by project
val jacksonVersion: String by project
val springSecurityVersion: String by project


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("jakarta.servlet:jakarta.servlet-api:${jakartaVersion}")
    implementation("org.postgresql:postgresql:${postgreVersion}")

    implementation("org.springframework:spring-context-support:${springVersion}")
    implementation("org.springframework.security:spring-security-taglibs")

    implementation("org.springframework.boot:spring-boot-starter-freemarker")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("javax.mail:javax.mail-api:1.6.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerVersion}")

    implementation("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.postgresql:postgresql:${postgreVersion}")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
}

tasks.test {
    useJUnitPlatform()
}

val props = Properties()
props.load(file("src/main/resources/db/liquibase.properties").inputStream())

println("DEBUG props: change-lop-file=${props["change-lop-file"]}, url=${props["url"]}")

liquibase {
    activities.register("main") {
        arguments = mapOf(
        "changeLogFile" to props["change-lop-file"],
        "url" to props["url"].toString(),
        "username" to props["username"].toString(),
            "password" to props["password"].toString(),
            "driver" to props["driver-class-name"]
        )
    }
}