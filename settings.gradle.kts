import me.champeau.gradle.igp.gitRepositories
import org.eclipse.jgit.api.Git
import java.io.FileInputStream
import java.util.Properties

include(":skill")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // not using version catalog because it is not available in settings.gradle.kts
    id("me.champeau.includegit") version "0.1.6"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

val localProperties = Properties().apply {
    try {
        load(FileInputStream(File(rootDir, "local.properties")))
    } catch (e: Throwable) {
        println("Warning: can't read local.properties: $e")
    }
}

if (localProperties.getOrDefault("useLocalDicioLibraries", "") == "true") {
    includeBuild("../dicio-numbers") {
        dependencySubstitution {
            substitute(module("git.included.build:dicio-numbers"))
                .using(project(":numbers"))
        }
    }

} else {
    // if the repo has already been cloned, the gitRepositories plugin is buggy and doesn't
    // fetch the remote repo before trying to checkout the commit (in case the commit has changed),
    // so we need to do it manually
    val file = File("$rootDir/checkouts/dicio-numbers")
    if (file.isDirectory) {
        Git.open(file).fetch().call()
    }

    gitRepositories {
        include("dicio-numbers") {
            uri.set("https://github.com/Stypox/dicio-numbers")
            // use the dummy version, except when specified differently in local.properties
            val overrideCommit = localProperties.getOrDefault("dicioNumbersCommit", "").toString()
            if (overrideCommit.isBlank()) {
                tag.set("dummy-3")
            } else {
                commit.set(overrideCommit)
            }
            autoInclude.set(false)
            includeBuild("") {
                dependencySubstitution {
                    substitute(module("git.included.build:dicio-numbers"))
                        .using(project(":numbers"))
                }
            }
        }
    }
}
