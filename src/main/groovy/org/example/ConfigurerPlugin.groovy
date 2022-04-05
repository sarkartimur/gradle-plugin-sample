package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigurerPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.configure(project, configurer())

        project.subprojects configurer()
    }

    private Closure<Void> configurer() {
        return {
            apply plugin: 'java'
            apply plugin: 'org.springframework.boot'
            apply plugin: 'io.spring.dependency-management'
            apply plugin: 'io.freefair.lombok'
            apply plugin: 'com.google.cloud.tools.jib'

            repositories {
                mavenLocal()
                mavenCentral()
            }
        }
    }
}
