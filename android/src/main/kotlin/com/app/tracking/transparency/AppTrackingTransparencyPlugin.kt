package com.app.tracking.transparency

import android.app.Activity
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlin.concurrent.thread

class AppTrackingTransparencyPlugin() : FlutterPlugin, ActivityAware, MethodCallHandler {
    private var activity: Activity? = null

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        val channel = MethodChannel(binding.binaryMessenger, "app_tracking_transparency")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (activity == null) {
            result.success(4)
            return
        }
        val a = activity!!
        when (call.method) {
            "getAdvertisingIdentifier" -> thread {
                try {
                    val id = AdvertisingIdClient.getAdvertisingIdInfo(a).id
                    a.runOnUiThread {
                        result.success(id)
                    }
                } catch (e: Exception) {
                    a.runOnUiThread {
                        result.success(null)
                    }
                }
            }
            "getTrackingAuthorizationStatus" -> thread {
                try {
                    val isLimitAdTrackingEnabled =
                        AdvertisingIdClient.getAdvertisingIdInfo(a).isLimitAdTrackingEnabled
                    a.runOnUiThread {
                        result.success(if(isLimitAdTrackingEnabled) 2 else 3)
                    }
                } catch (e: Exception) {
                    a.runOnUiThread {
                        result.success(0)
                    }
                }
            }
            "requestTrackingAuthorization" -> thread {
                try {
                    val isLimitAdTrackingEnabled =
                        AdvertisingIdClient.getAdvertisingIdInfo(a).isLimitAdTrackingEnabled
                    a.runOnUiThread {
                        result.success(if(isLimitAdTrackingEnabled) 2 else 3)
                    }
                } catch (e: Exception) {
                    a.runOnUiThread {
                        result.success(0)
                    }
                }
            }
            else -> result.notImplemented()
        }
    }
}
