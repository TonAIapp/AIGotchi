package com.digwex.utils

import android.util.Log.INFO
import com.digwex.components.log.Log

object RootUtils {
  val isRoot: Boolean
    get() {
      try {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "pm"))
        return true
      } catch (ex: Exception) {
        Log.println(INFO, RootUtils::class.java,
          "Check root exception: %s", ex.message)
      }

      return false
    }

  private fun execSu(command: String){
    Log.println(INFO, RootUtils::class.java, "Exec command: %s", command)
    val proc: Process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
    proc.waitFor()
  }

  fun tryExecSu(command: String) {
    try {
      execSu(command)
    }catch (ex: Exception){
      Log.println(INFO, RootUtils::class.java, "SU excec exception: %s", ex.message)
    }
  }
}