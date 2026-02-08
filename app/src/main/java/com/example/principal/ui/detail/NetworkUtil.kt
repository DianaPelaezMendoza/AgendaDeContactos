package com.example.principal.ui.detail

/**
 * Comprueba si hay conexión a Internet.
 *
 * Esta función utiliza el `ConnectivityManager` para determinar si
 * el dispositivo tiene acceso a Internet. Requiere el permiso
 * [Manifest.permission.ACCESS_NETWORK_STATE].
 *
 * @param context Contexto de la aplicación o actividad.
 * @return `true` si hay conexión a Internet, `false` en caso contrario.
 */


import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun NetworkUtil(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities =
        connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
