import org.gradle.jvm.tasks.Jar
import net.minecraftforge.gradle.user.UserBaseExtension

val modBaseName: String by extra
val forgeVersion: String by extra
val mappingVersion: String by extra
val Project.minecraft: UserBaseExtension
    get() = extensions.getByName<UserBaseExtension>("minecraft")

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://files.minecraftforge.net/maven")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT") {
            isChanging = true
        }
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
    }
}

plugins {
    java
}

apply {
    plugin("net.minecraftforge.gradle.forge")
    plugin("kotlin")
}

version = "1.0.0"
group = "me.ikexing"

repositories {
    mavenCentral()
    maven(url = "https://maven.thiakil.com")
    maven(url = "https://maven.blamejared.com")
    maven(url = "https://maven.shadowfacts.net")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")

    implementation("net.shadowfacts:Forgelin:1.8.4")
    implementation("vazkii.botania:Botania:r1.10-364.5")
    implementation("com.azanor.baubles:Baubles:1.12-1.5.2")
    implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.1.20.681")
}

configure<UserBaseExtension> {
    version = forgeVersion
    runDir = "run"

    mappings = mappingVersion
    makeObfSourceJar = false
}

tasks.withType<Jar> {
    inputs.properties += "version" to project.version
    inputs.properties += "mcversion" to project.minecraft.version

    baseName = modBaseName

    filesMatching("/mcmod.info") {
        expand(
            mapOf(
                "version" to project.version, "mcversion" to project.minecraft.version
            )
        )
    }
}
//
//runClient {
//    args("--username", "dev_player")
//}
//
