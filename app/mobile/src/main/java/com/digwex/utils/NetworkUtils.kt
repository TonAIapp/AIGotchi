package com.digwex.utils

import java.net.NetworkInterface
import java.util.*
import kotlin.experimental.and
class NetworkUtils {
    companion object {
        /**
         * Convert byte arrayOrientation to hex string
         * @param bytes toConvert
         * @return hexValue
         */
        @JvmStatic
        fun bytesToHex(bytes: ByteArray): String {
            val sbuf = StringBuilder()
            for (idx in bytes.indices) {
                val intVal = bytes[idx] and 0xff.toByte()
                if (intVal < 0x10) sbuf.append("0")
                sbuf.append(Integer.toHexString(intVal.toInt()).toUpperCase())
            }
            return sbuf.toString()
        }

        /**
         * Get utf8 byte arrayOrientation.
         * @param str which to be converted
         * @return  arrayOrientation of NULL if error was found
         */
        @JvmStatic
        fun getUTF8Bytes(str: String): ByteArray? {
            return try {
                str.toByteArray(charset("UTF-8"))
            } catch (ex: Exception) {
                null
            }

        }

        /**
         * Returns MAC address of the given interface name.
         * @param interfaceName eth0, wlan0 or NULL=use first interface
         * @return  mac address or empty string
         */
        @JvmStatic
        fun getMACAddress(interfaceName: String?): String {
            try {
                val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    if (interfaceName != null) {
                        if (!intf.name.equals(interfaceName, ignoreCase = true)) continue
                    }
                    val mac = intf.hardwareAddress ?: return ""
                    val buf = StringBuilder()
                    for (aMac in mac) buf.append(String.format("%02X:", aMac))
                    if (buf.isNotEmpty()) buf.deleteCharAt(buf.length - 1)
                    return buf.toString()
                }
            } catch (ignored: Exception) {
            }
            return ""
        }

        /**
         * Get IP address from first non-localhost interface
         * @param useIPv4   true=return ipv4, false=return ipv6
         * @return  address or empty string
         */
        @JvmStatic
        fun getIPAddress(useIPv4: Boolean): String {
            try {
                val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs = Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress) {
                            val sAddr = addr.hostAddress
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            val isIPv4 = sAddr.indexOf(':') < 0

                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) sAddr.toUpperCase()
                                    else sAddr.substring(0, delim).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
            // for now eat exceptions
            return ""
        }
    }
}
