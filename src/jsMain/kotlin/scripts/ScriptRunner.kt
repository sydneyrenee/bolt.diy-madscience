package scripts

import kotlinx.coroutines.await
import org.w3c.dom.get
import kotlin.js.Promise

external fun require(module: String): dynamic

val childProcess = require("child_process")

fun executeScript(script: String, args: List<String> = emptyList()): Promise<String> {
    return Promise { resolve, reject ->
        val command = "$script ${args.joinToString(" ")}"
        childProcess.exec(command) { error, stdout, stderr ->
            if (error != null) {
                reject(error)
            } else {
                resolve(stdout.toString())
            }
        }
    }
}

fun executePythonScript(scriptPath: String, args: List<String> = emptyList()): Promise<String> {
    return executeScript("python $scriptPath", args)
}

fun executeNodeJsScript(scriptPath: String, args: List<String> = emptyList()): Promise<String> {
    return executeScript("node $scriptPath", args)
}

fun executeNodeJsCode(code: String): Promise<String> {
    return Promise { resolve, reject ->
        childProcess.exec(code) { error, stdout, stderr ->
            if (error != null) {
                reject(error)
            } else {
                resolve(stdout.toString())
            }
        }
    }
}
