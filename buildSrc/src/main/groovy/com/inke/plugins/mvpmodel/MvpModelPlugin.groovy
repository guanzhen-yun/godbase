package com.inke.plugins.mvpmodel

import org.gradle.api.Plugin
import org.gradle.api.Project

class MvpModelPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.create("mvpModel", MvpModelTask)
    }
}