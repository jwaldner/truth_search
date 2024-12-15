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
            when (session.uri) {
                "/" -> {
                    // Handle the root path for pings
                    newFixedLengthResponse(Response.Status.OK, "text/plain", "Ping OK")
                }

                "/mark" -> {
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
                        // Read only the specified number of bytes from the input stream
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
                    Log.d(tag, requestBody)


                    val (payload, version) = extractPayload(requestBody)
                    val versionEntry = PreferenceManager.versionMap.entries.first { version.contains(it.key) }
                    PreferenceManager.saveString(versionEntry.value, payload)

                    Log.w(tag,  "saved: ${versionEntry.value}  $payload")
                    Log.e(tag, "Saved: ${PreferenceManager.getString(versionEntry.value)}")

                    return newFixedLengthResponse(
                        Response.Status.OK,
                        "text/plain",
                        "Payload received successfully"
                    )
                }

                "/exit" -> {
                    fun handleCloseRequest() {
                        // Trigger the app close signal
                        Log.w(tag, "handle App Close triggered by browser: ${session.uri}")
                        AppCloser.closeApp()
                    }

                    // Serve the /exit endpoint
                    Log.w(tag, "serve the exit endpoint: ${session.uri}")
                    handleCloseRequest()
                    newFixedLengthResponse(
                        Response.Status.OK, "text/html", """
                        <html>
                        <body>
                            <script>
                                // Redirect to the app using the custom scheme
                                window.location.href = "app://com.wfs.truthsearch";
                            </script>
                            <p>If the app doesn’t open, <a href="app://com.wfs.truthsearch">click here to return to the app.</a></p>
                        </body>
                        </html>
                    """.trimIndent()
                    )
                }

                "/close" -> {
                    Log.d(tag, "close browser")
                    // this works
                    //Log.d(tag, "clear ${PreferenceManager.KEY_PLACE_HOLDER}" )
                    // Serve the /close endpointPoint
                    newFixedLengthResponse(
                        Response.Status.OK, "text/html", """
                        <html>
                        <body>
                            <script>
                                // Redirect to the app using the custom scheme
                                window.location.href = "app://com.wfs.truthsearch";
                            </script>
                            <p>If the app doesn’t open, <a href="app://com.wfs.truthsearch">click here to return to the app.</a></p>
                        </body>
                        </html>
                    """.trimIndent()
                    )
                }
                
                else -> {
                    // Serve static files from cache
                    val requestedPath = session.uri.trimStart('/')
                    val file = File(cacheDir, requestedPath)
                    val versionEntry = PreferenceManager.versionMap.entries.first { requestedPath.contains(it.key) }

                    if (file.exists()) {
                        val fileLength = file.length()
                        val fileInputStream = file.inputStream()
                        val placeHolder = getBookFromAssets(context, versionEntry.key, file.name)!!.id
                        // Save to preferences
                        PreferenceManager.saveString(
                            versionEntry.value,
                            placeHolder
                        )
                        Log.d(tag, "saved ${versionEntry.value}: ${placeHolder}")

                        newFixedLengthResponse(
                            Response.Status.OK,
                            "text/html",
                            fileInputStream,
                            fileLength
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
