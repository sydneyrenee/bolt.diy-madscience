package utils

import kotlinx.coroutines.await
import org.w3c.dom.get
import kotlin.js.Promise

external fun require(module: String): dynamic

val childProcess = require("child_process")

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

fun callNodeJsFunction(modulePath: String, functionName: String, vararg args: Any): Promise<Any> {
    return Promise { resolve, reject ->
        val module = require(modulePath)
        val function = module[functionName]
        if (function != null) {
            function.call(module, *args, { error: dynamic, result: dynamic ->
                if (error != null) {
                    reject(error)
                } else {
                    resolve(result)
                }
            })
        } else {
            reject(Exception("Function $functionName not found in module $modulePath"))
        }
    }
}
