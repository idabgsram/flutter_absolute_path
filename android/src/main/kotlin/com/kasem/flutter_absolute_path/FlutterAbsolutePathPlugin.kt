package com.kasem.flutter_absolute_path

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.util.Log
import android.content.pm.ProviderInfo
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import java.security.Provider


public class FlutterAbsolutePathPlugin: FlutterPlugin, MethodCallHandler {

    private var channel : MethodChannel? = null
    private lateinit var context: Context

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
          val plugin = FlutterAbsolutePathPlugin()
          plugin.setupChannel(registrar.messenger(), registrar.context())
        }
      }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        setupChannel(binding.binaryMessenger, binding.applicationContext)
        context = binding.applicationContext
      }
    
      override fun onDetachedFromEngine(p0: FlutterPlugin.FlutterPluginBinding) {
        teardownChannel();
      }
    
      fun setupChannel(messenger: BinaryMessenger, context: Context) {
        channel = MethodChannel(messenger, "flutter_absolute_path")
        channel?.setMethodCallHandler(this)
      }
    
      private fun teardownChannel() {
        channel?.setMethodCallHandler(null)
        channel = null
      }

      override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when {
            call.method == "getAbsolutePath" -> {
                val uriString = call.argument<Any>("uri") as String
                val uri = Uri.parse(uriString)
                result.success(FileDirectory.getAbsolutePath(this.context, uri))
            }
            else -> result.notImplemented()
        }
    }
}
