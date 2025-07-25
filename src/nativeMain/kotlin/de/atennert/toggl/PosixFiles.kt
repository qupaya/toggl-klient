package de.atennert.toggl

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.*

@ExperimentalForeignApi
object PosixFiles {
    private const val READ_BUFFER_SIZE = 100

    fun exists(path: String): Boolean {
        return access(path, F_OK) == 0
    }

    fun readLines(path: String, consumer: (String) -> Unit) {
        val filePointer = fopen(path, "r") ?: return

        var bufferString = ""

        ByteArray(READ_BUFFER_SIZE).usePinned { entry ->
            while (fgets(entry.addressOf(0), READ_BUFFER_SIZE, filePointer) != null) {
                bufferString += entry.get()
                    .takeWhile { it > 0 }
                    .fold("") {acc, b -> acc + b.toInt().toChar()}

                if (bufferString.endsWith('\n') || feof(filePointer) != 0) {
                    consumer(bufferString)
                    bufferString = ""
                }
            }
        }

        fclose(filePointer)
    }
}