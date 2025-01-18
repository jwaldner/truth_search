package com.wfs.truthsearch.webserver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.wfs.truthsearch.BuildConfig
import com.wfs.truthsearch.SharedViewModel
import com.wfs.truthsearch.models.Topic
import com.wfs.truthsearch.utils.AppCloser
import fi.iki.elonen.NanoHTTPD
import java.io.File
import com.wfs.truthsearch.utils.PreferenceManager
import com.wfs.truthsearch.utils.extractPayload
import com.wfs.truthsearch.utils.validatePayload
import java.io.IOException

import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.json.JSONObject
import java.security.Security



class LocalServer(
    private val cacheDir: File,
    private val context: Context,
    private val ssl: Boolean = false
    // private val sharedViewModel: SharedViewModel
    ) : NanoHTTPD(8080) {

    private val tag = "ServerStatus"



    // Load keystore and configure SSL
    init {
        Log.d(tag, "SSL: ${PreferenceManager.getBool(PreferenceManager.KEY_PREFS_SSL)}")

        if (ssl) {
            val keystorePassword = BuildConfig.KEYSTORE_PASSWORD

            // Copy keystore file to the cache directory
            copyKeystoreFile(context)

            // Access the keystore file from cacheDir
            val keystoreFile = File(context.cacheDir, "bibles/server.keystore")

            // Load keystore and configure SSL
            Security.addProvider(BouncyCastleProvider())
            Log.d(tag, "BouncyCastleProvider initialized: ${Security.getProvider("BC") != null}")

            val keystore = loadKeystore(keystoreFile, keystorePassword.toCharArray())
            val sslContext = createSslContext(keystore, keystorePassword.toCharArray())

            makeSecure(sslContext.serverSocketFactory, null)
        }
    }

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

                    // Parse verses from the payload
                    val verses = parseVersesFromPayload(requestBody) // Use your existing logic to parse the JSON payload

                    val default = "${verses.count()} verse${if (verses.count() == 1) "" else "s"}"

                    Log.d(tag, "added ${default}")

                    val intent = Intent(Topic.ACTION_TOPIC_UPDATE).apply {
                        putExtra("topic_title", default)
                        putExtra("topic_description", "")
                        putExtra("topic_notes", "")
                        putStringArrayListExtra("topic_verses", ArrayList(verses))
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    Log.d("TopicBroadcast","intent created in ${tag} for ${default}" )


                    return newFixedLengthResponse(
                        Response.Status.OK,
                        "text/plain",
                        "Verses received and added as a topic"
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

    private fun copyKeystoreFile(context: Context) {
        val assetFilePath = "bibles/server.keystore"
        val targetFile = File(context.filesDir.path + "/bibles/server.keystore")

        if (!targetFile.exists()) {
            targetFile.parentFile?.mkdirs() // Ensure the parent directories exist
            context.assets.open(assetFilePath).use { inputStream ->
                targetFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    private fun loadKeystore(file: File, password: CharArray): KeyStore {
        val keyStore = KeyStore.getInstance("PKCS12")
        Log.d(tag, "exists: ${file.exists()} can read ${file.name}  ${file.canRead()}"  )
        return try {
            file.inputStream().use { inputStream ->
                keyStore.load(inputStream, password)
            }
            keyStore
        } catch (e: Exception) {
            Log.e(tag, "Failed to load keystore: ${e.message}", e)
            throw e // Re-throw the exception to avoid suppressing the error
        }
    }


    private fun createSslContext(keystore: KeyStore, password: CharArray): SSLContext {
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keystore, password)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagerFactory.keyManagers, null, null)
        return sslContext
    }

    private fun parseVersesFromPayload(payload: String): List<String> {
        // Example: Extract "selectedVerses" from JSON payload
        val jsonObject = JSONObject(payload)
        val selectedVerses = jsonObject.getJSONArray("selectedVerses")
        val verses = mutableListOf<String>()
        for (i in 0 until selectedVerses.length()) {
            verses.add(selectedVerses.getString(i))
        }
        return verses
    }


}
