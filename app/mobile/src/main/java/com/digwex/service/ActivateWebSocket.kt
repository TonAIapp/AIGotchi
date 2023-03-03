package com.digwex.service

import android.util.Log
import com.digwex.Config
import com.digwex.api.model.ActivateRequestJson
import com.digwex.api.model.ActivateResponseJson
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ActivateWebSocket(mUrl: String,
                        private val httpClient: OkHttpClient,
                        private val mGson: Gson,
                        private val mCallback: ActivateCallback) : WebSocketListener() {

  interface ActivateCallback {
    fun onActivateSuccess(id: Int, token: String, offset: Int)

    fun receivePin(pin: String)
  }

  private val mWebSocketRequest: okhttp3.Request = okhttp3.Request.Builder()
    .url("$mUrl/activate")
    .build()

  private var mWebSocket: WebSocket? = null
  private var mIsConnected: Boolean = false
  private var mIsConnecting: Boolean = false
  private var mIsDestroy: Boolean = false

  fun start() {
    if (mIsConnected || mIsConnecting) {
      return
    }

    mIsConnecting = true
    mWebSocket = httpClient.newWebSocket(mWebSocketRequest, this)
  }

  override fun onOpen(webSocket: WebSocket, response: Response?) {
    //println(DEBUG, BindWsClient.class, "Opened");

    println("open")

    mIsConnecting = false
    mIsConnected = true
    mWebSocket = webSocket
    send()
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable,
                         response: Response?) {
    mIsConnecting = false
    mIsConnected = false

    println("fail ${Log.getStackTraceString(t)}")

    //println(ERROR, BindWsClient.class, Log.getStackTraceString(e));

    try {
      Thread.sleep(5000)
    } catch (ignore: Exception) {
      //e1.printStackTrace();
    }
    reconnect()
  }

  override fun onClosed(webSocket: WebSocket, code: Int, reason: String?) {
    println("closed")
    mIsConnecting = false
    mIsConnected = false

    if (code == 1000) return
    reconnect()
  }

  override fun onMessage(webSocket: WebSocket, text: String?) {
    println(text)
    if (text != null) {
      try {
        val response: ActivateRequestJson? = mGson.fromJson(text, ActivateRequestJson::class.java)
        if (response?.pin != null) {
          mCallback.receivePin(response.pin)
          com.digwex.components.log.Log.println(Log.DEBUG, ActivateWebSocket::class.java, "Receive: $text")
          return
        }

        val activate = mGson.fromJson(text, ActivateResponseJson::class.java)
        if (activate != null) {
          mCallback.onActivateSuccess(activate.id, activate.access_token, activate.offset)
          com.digwex.components.log.Log.println(Log.DEBUG, ActivateWebSocket::class.java, "Receive: $text")
          mIsDestroy = true
          disconnect()
        }
      } catch (ignore: Exception) {
      }
    }
  }

  private fun reconnect() {
    if (mIsDestroy) return
    disconnect()
    start()
  }

  private fun disconnect() {
    if (!mIsConnected) {
      return
    }

    if (mIsConnecting) {
      mIsConnecting = false
    }

    if (mWebSocket != null) {
      try {
        mWebSocket!!.close(1000, null)
        mWebSocket!!.cancel()
      } catch (ignore: Exception) {
        /* ignored */
      } finally {
        mWebSocket = null
      }
    }
  }

  private fun send() {
    if (mWebSocket == null) {
      reconnect()
      return
    }

    try {
      val model = ActivateRequestJson()
      model.pin = Config.pin
      model.platform = "android"
      val requestString = mGson.toJson(model)
      com.digwex.components.log.Log.println(Log.DEBUG, ActivateWebSocket::class.java, "Sending request: %s",
        requestString)
      mWebSocket!!.send(requestString)
    } catch (ignored: Exception) {
      //com.digwex.components.log.Log.printException(ignored)
      return
    }
  }

}