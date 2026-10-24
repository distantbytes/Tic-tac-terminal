plugins {
    kotlin("jvm") version "1.8.22"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}


group = "page.codeberg.distantbytes"
version = "4.6.0"

repositories {
    mavenCentral()
}

dependencies {
    // 在这里添加你的项目依赖
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(11)
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}

application {
    mainClass.set("page.codeberg.distantbytes.tictacterminal.TicTacToeKt") // 替换为你的主类
}

tasks.shadowJar {
    archiveClassifier.set("all")
    // 生成 xxx-all.jar，避免覆盖普通 jar
    mergeServiceFiles()
    // 合并 META-INF/services，避免多依赖时 SPI 冲突
}

tasks.test {
    useJUnitPlatform()
}