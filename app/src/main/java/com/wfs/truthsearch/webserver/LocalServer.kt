package com.wfs.truthsearch.webserver

import android.content.Context
import android.util.Log
import com.wfs.truthsearch.models.getBookFromAssets
import com.wfs.truthsearch.utils.AppCloser
import fi.iki.elonen.NanoHTTPD
import java.io.File
import com.wfs.truthsearch.utils.PreferenceManager
import com.wfs.truthsearch.utils.extractPayload
import com.wfs.truthsearch.utils.validatePayload
import java.io.IOException

class LocalServer(private val cacheDir: File, private val context: Context) : NanoHTTPD(8080) {

    private val tag = "ServerStatus"
    override fun serve(session: IHTTPSession): Response {
        Log.d(tag, "Serve: ${session.uri}")

        return try {
            when {
                session.uri == "/" -> {
                    // Handle the root path for pings
                    newFixedLengthResponse(Response.Status.OK, "text/plain", "Ping OK")
                }

                session.uri == "/mark" -> {
                    val contentLength = session.headers["content-length"]?.toIntOrNull() ?: 0

                    if (contentLength <= 0) {
                        Log.e(tag, "Invalid or missing Content-Length")
                        return newFixedLengthResponse(
                            Response.Status.BAD_REQUEST,
                            "text/plain",
                            "Invalid or missing Content-Length"
                        )
                    }

                    val requestBody = try {
                        val buffer = ByteArray(contentLength)
                        session.inputStream.read(buffer, 0, contentLength)
                        String(buffer, Charsets.UTF_8)
                    } catch (e: IOException) {
                        Log.e(tag, "Error reading input stream", e)
                        return newFixedLengthResponse(
                            Response.Status.INTERNAL_ERROR,
                            "text/plain",
                            "Error reading input stream"
                        )
                    }

                    if (requestBody.isBlank()) {
                        Log.e(tag, "Empty request body")
                        return newFixedLengthResponse(
                            Response.Status.BAD_REQUEST,
                            "text/plain",
                            "Request body is empty"
                        )
                    }

                    Log.d(tag, "Received payload: $requestBody")

                    if (!validatePayload(requestBody)) {
                        Log.e(tag, "Invalid payload: $requestBody")
                        return newFixedLengthResponse(
                            Response.Status.BAD_REQUEST,
                            "text/plain",
                            "Invalid payload format"
                        )
                    }

                    // Save the payload as a bookmark
                    val (payload, version) = extractPayload(requestBody)
                    val versionEntry = PreferenceManager.versionMap.entries.first { version.contains(it.key) }
                    PreferenceManager.saveString(versionEntry.value, payload)

                    Log.w(tag, "saved: ${versionEntry.value} $payload")
                    Log.e(tag, "Saved: ${PreferenceManager.getString(versionEntry.value)}")

                    newFixedLengthResponse(
                        Response.Status.OK,
                        "text/plain",
                        "Payload received successfully"
                    )
                }

                session.uri == "/exit" -> {
                    AppCloser.closeApp()
                    newFixedLengthResponse(
                        Response.Status.OK, "text/html", """
                    <html>
                    <body>
                        <script>
                            window.location.href = "app://com.wfs.truthsearch";
                        </script>
                        <p>If the app doesn’t open, <a href="app://com.wfs.truthsearch">click here to return to the app.</a></p>
                    </body>
                    </html>
                """.trimIndent()
                    )
                }

                session.uri == "/close" -> {
                    Log.d(tag, "close browser")
                    newFixedLengthResponse(
                        Response.Status.OK, "text/html", """
                    <html>
                    <body>
                        <script>
                            window.location.href = "app://com.wfs.truthsearch";
                        </script>
                        <p>If the app doesn’t open, <a href="app://com.wfs.truthsearch">click here to return to the app.</a></p>
                    </body>
                    </html>
                """.trimIndent()
                    )
                }

                session.uri.endsWith(".css") -> {
                    // Handle CSS files
                    val requestedPath = session.uri.trimStart('/')
                    val file = File(cacheDir, requestedPath)
                    if (file.exists()) {
                        Log.d(tag, "Serving CSS: ${file.name}")
                        val fileInputStream = file.inputStream()
                        newFixedLengthResponse(
                            Response.Status.OK,
                            "text/css", // Set correct MIME type for CSS
                            fileInputStream,
                            file.length()
                        )
                    } else {
                        Log.e(tag, "CSS File not found: $requestedPath")
                        newFixedLengthResponse(
                            Response.Status.NOT_FOUND,
                            "text/plain",
                            "CSS File not found"
                        )
                    }
                }

                else -> {
                    // Serve static files from cache
                    val requestedPath = session.uri.trimStart('/')
                    val file = File(cacheDir, requestedPath)
                    if (file.exists()) {
                        Log.d(tag, "Serving: ${file.name}")
                        val fileInputStream = file.inputStream()
                        newFixedLengthResponse(
                            Response.Status.OK,
                            "text/html",
                            fileInputStream,
                            file.length()
                        )
                    } else {
                        Log.e(tag, "File not found: $requestedPath")
                        newFixedLengthResponse(
                            Response.Status.NOT_FOUND,
                            "text/plain",
                            "File not found"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error while serving request: ${e.message}")
            newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                "text/plain",
                "Internal Server Error"
            )
        }
    }


}
