package com.klaxon.client

import android.net.ConnectivityManager
import android.content.Context
import java.net.{SocketAddress, UnknownHostException, InetAddress}
import com.klaxon.remote.common.io.socket.SocketClient
import scala.util.{Failure, Success}
import java.io.IOException

/**
 * <p>date 3/5/14 </p>
 * @author klaxon
 */
object ConnectionHelper {

  def client(socketAddress: SocketAddress) = {
    try {
      new SocketClient(socketAddress)
    } catch {
      case e: IOException => null
    }
  }

  def isNetworkConnected(context: Context): Boolean = {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE).asInstanceOf[ConnectivityManager]
    val networkInfo = connectivityManager.getActiveNetworkInfo

    networkInfo != null
  }

  def hostFrom(hostString: String): InetAddress = {
    try {
      InetAddress.getByName(hostString)
    } catch {
      case e: UnknownHostException => return null
    }
  }

}
