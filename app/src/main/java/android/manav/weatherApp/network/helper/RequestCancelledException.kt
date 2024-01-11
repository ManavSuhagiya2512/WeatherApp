package android.p.weatherApp.network.helper

import org.chromium.net.CronetException

class RequestCancelledException(message: String = "The request was canceled!", cause: Throwable? = null) :
    CronetException(message, cause)