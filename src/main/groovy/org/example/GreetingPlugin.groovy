package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.getPlugins().apply("java")
        project.getPlugins().apply("org.springframework.boot")
        project.getPlugins().apply("io.spring.dependency-management")
        project.getPlugins().apply("io.freefair.lombok")
        project.getPlugins().apply("com.google.cloud.tools.jib")

        project.subprojects {
            apply plugin: 'java'
            apply plugin: 'org.springframework.boot'
            apply plugin: 'io.spring.dependency-management'
            apply plugin: 'io.freefair.lombok'
            apply plugin: 'com.google.cloud.tools.jib'
        }
    }
}
