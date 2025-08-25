import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val SPRING_DOC = "1.7.0"
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.0"
    id("org.jetbrains.kotlin.kapt") version "1.7.0"
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("nu.studer.jooq") version "8.2.1"
    id("org.flywaydb.flyway") version "7.1.1"
}

repositories {
    mavenCentral()
}

// Define your versions
val sqliteVersion = "3.39.3.0"
val jooqVersion = "3.17.5"

dependencies {
    // Kotlin stdlib
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Spring Boot ⭐
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // SQLite JDBC
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    jooqGenerator("org.xerial:sqlite-jdbc:$sqliteVersion")

    // JAXB dependencies for JOOQ codegen (Jakarta API)
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    jooqGenerator("org.glassfish.jaxb:jaxb-runtime:3.0.2")

    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-ui:${Versions.SPRING_DOC}")

    // JOOQ
    implementation("org.jooq:jooq:$jooqVersion")
    jooqGenerator("org.jooq:jooq-codegen:$jooqVersion")

    implementation("org.jetbrains.kotlin:kotlin-test-junit5:1.7.0")

    // Testing dependencies
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

// Configure jooq code generation
jooq {
    version.set(jooqVersion)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.sqlite.JDBC"
                    url = "jdbc:sqlite:${project.projectDir}/demoapp.sqlite"  // Use absolute path
                }
                generator.apply {
                    database.apply {
                        name = "org.jooq.meta.sqlite.SQLiteDatabase"
                        excludes = "flyway_schema_history|sqlite_sequence"
                    }
                    target.apply {
                        packageName = "com.tts.demo.db.jooq.generated"
                        directory = "${project.projectDir}/src/main/kotlin/"
                    }
                }
            }
        }
    }
}

// Flyway config to create/populate the DB - USE SAME DB AS JOOQ
flyway {
    url = "jdbc:sqlite:${project.projectDir}/demoapp.sqlite"  // Same as JOOQ config
}

// Make IDE recognize generated sources
sourceSets {
    main {
        java.srcDir("build/generated-src/jooq/main")  // Fixed path to match JOOQ config
    }
}

// Java & Kotlin compatibility
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// Task to run JOOQ code generation after Flyway migration
tasks.named("generateJooq") {
    dependsOn("flywayMigrate")
}

// Clean up tasks
tasks.register("jooq-cleanup", Delete::class) {
    doLast {
        mkdir("${project.buildDir}")
    }
    delete("${project.projectDir}/src/main/kotlin/com/tts/demo/db/jooq/generated")
    delete("${project.projectDir}/demoapp.sqlite")
}

// Remember to run:
// ./gradlew flywayMigrate
// ./gradlew generateJooq
// to create & populate your SQLite db, then generate JOOQ code