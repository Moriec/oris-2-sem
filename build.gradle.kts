plugins {
    id("java")
    id("application")
    id("war")
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
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework:spring-jdbc:${springVersion}")
    implementation("org.springframework:spring-orm:${springVersion}")
    implementation("org.springframework:spring-context-support:${springVersion}")
    implementation("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")
    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("org.postgresql:postgresql:$postgreVersion")
    implementation("org.freemarker:freemarker:$freemarkerVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("org.springframework.data:spring-data-jpa:${springDataVersion}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerVersion}")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}

tasks.test {
    useJUnitPlatform()
}
