package com.sharelinkapp.wifidirect

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class WifiDirectModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private var manager: WifiP2pManager? = null
    private var channel: WifiP2pManager.Channel? = null
    private var receiver: WifiDirectBroadcastReceiver? = null
    private val intentFilter = IntentFilter()

    override fun getName() = "WifiDirectModule"

    init {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    @ReactMethod
    fun initialize(promise: Promise) {
        try {
            manager = reactContext.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
            channel = manager?.initialize(reactContext, reactContext.mainLooper, null)

            receiver = WifiDirectBroadcastReceiver(manager!!, channel!!, this)
            reactContext.registerReceiver(receiver, intentFilter)

            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("INIT_FAILED", e.message)
        }
    }

    @ReactMethod
    fun createGroup(promise: Promise) {
        manager?.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                promise.resolve(true)
            }

            override fun onFailure(reasonCode: Int) {
                promise.reject("CREATE_GROUP_FAILED", "Reason code: $reasonCode")
            }
        })
    }

    @ReactMethod
    fun removeGroup(promise: Promise) {
        manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() = promise.resolve(true)
            override fun onFailure(reasonCode: Int) =
                promise.reject("REMOVE_GROUP_FAILED", "Reason code: $reasonCode")
        })
    }

    @ReactMethod
    fun discoverPeers(promise: Promise) {
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() = promise.resolve(true)
            override fun onFailure(reasonCode: Int) =
                promise.reject("DISCOVER_FAILED", "Reason code: $reasonCode")
        })
    }

    @ReactMethod
    fun connectToPeer(deviceAddress: String, promise: Promise) {
        val config = WifiP2pConfig().apply {
            deviceAddress = deviceAddress
        }
        manager?.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() = promise.resolve(true)
            override fun onFailure(reasonCode: Int) =
                promise.reject("CONNECT_FAILED", "Reason code: $reasonCode")
        })
    }

    fun onPeersAvailable(peers: WifiP2pDeviceList) {
        val peerArray = Arguments.createArray()
        for (device: WifiP2pDevice in peers.deviceList) {
            val peerMap = Arguments.createMap()
            peerMap.putString("deviceName", device.deviceName)
            peerMap.putString("deviceAddress", device.deviceAddress)
            peerArray.pushMap(peerMap)
        }
        sendEvent("onPeersChanged", peerArray)
    }

    fun onConnectionInfoAvailable(groupOwnerAddress: String, isGroupOwner: Boolean) {
        val map = Arguments.createMap()
        map.putString("groupOwnerAddress", groupOwnerAddress)
        map.putBoolean("isGroupOwner", isGroupOwner)
        sendEvent("onConnectionChanged", map)
    }

    private fun sendEvent(eventName: String, params: Any?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    @ReactMethod
    fun addListener(eventName: String) { /* required for RN event emitter */ }

    @ReactMethod
    fun removeListeners(count: Int) { /* required for RN event emitter */ }
}