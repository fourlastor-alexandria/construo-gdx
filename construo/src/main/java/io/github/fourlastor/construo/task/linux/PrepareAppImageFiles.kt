package io.github.fourlastor.construo.task.linux

import io.github.fourlastor.construo.ConstruoPlugin
import io.github.fourlastor.construo.Architecture
import io.github.fourlastor.construo.task.BaseTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class PrepareAppImageFiles @Inject constructor(
    private val fileSystemOperations: FileSystemOperations,
): BaseTask() {

    @get:Input abstract val targetName: Property<String>
    @get:Input abstract val architecture: Property<Architecture>
    @get:InputDirectory abstract val jpackageImageBuildDir: DirectoryProperty
    @get:InputDirectory abstract val templateAppDir: DirectoryProperty
    @get:InputFile abstract val icon: RegularFileProperty
    @get:OutputDirectory abstract val outputDir: DirectoryProperty

    @TaskAction
    fun run() {
        fileSystemOperations.copy {
            it.from(jpackageImageBuildDir.get().dir("${targetName.get()}-linux-${architecture.get().arch}")) {
                it.into(ConstruoPlugin.APP_DIR_NAME)
            }
            // TODO remove /Game.AppDir.Template from this
            it.from(templateAppDir.get().dir("Game.AppDir.Template")) {
                it.into(ConstruoPlugin.APP_DIR_NAME)
            }
            if (icon.isPresent) {
                it.from(icon.get()) {
                    it.into(ConstruoPlugin.APP_DIR_NAME)
                }
            }
            it.into(outputDir.dir(architecture.get().arch))
        }
    }
}