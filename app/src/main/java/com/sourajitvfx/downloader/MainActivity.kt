package com.sourajitvfx.downloader
import android.app.Activity
import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executors
class MainActivity:Activity(){
 private lateinit var web:WebView
 private val executor=Executors.newSingleThreadExecutor()
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);web=WebView(this);web.settings.javaScriptEnabled=true;web.settings.domStorageEnabled=true;web.webViewClient=WebViewClient();web.addJavascriptInterface(Bridge(),"AndroidBridge");setContentView(web);web.loadUrl("file:///android_asset/index.html")}
 inner class Bridge{
 @JavascriptInterface fun clipboardText():String=try{val c=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;val t=c.primaryClip?.getItemAt(0)?.coerceToText(this@MainActivity)?.toString()?: "";if(t.startsWith("http://")||t.startsWith("https://"))t else ""}catch(e:Exception){""}
 @JavascriptInterface fun fetchInfo(input:String){executor.execute{val r=try{require(input.startsWith("https://")||input.startsWith("http://")){"Enter a valid link"};val u="https://api.sayan-nexuswork.workers.dev/alldown?url="+URLEncoder.encode(input,"UTF-8");val c=URL(u).openConnection() as HttpURLConnection;c.connectTimeout=20000;c.readTimeout=30000;val code=c.responseCode;val body=(if(code in 200..299)c.inputStream else c.errorStream).bufferedReader().use{it.readText()};c.disconnect();if(code !in 200..299)error("API HTTP "+code);JSONObject().put("ok",true).put("data",body).toString()}catch(e:Exception){JSONObject().put("ok",false).put("error",e.message?:"API error").toString()};runOnUiThread{web.evaluateJavascript("window.onApiResult("+JSONObject.quote(r)+")",null)}}}
 @JavascriptInterface fun download(link:String,name:String,mime:String){runOnUiThread{try{val u=Uri.parse(link);require(u.scheme=="https"||u.scheme=="http");val safe=name.replace(Regex("[^A-Za-z0-9._-]"),"_");val req=DownloadManager.Request(u).setTitle(safe).setDescription("VFX DOWNLOADER").setMimeType(mime).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,safe);(getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(req);Toast.makeText(this@MainActivity,"Download started",Toast.LENGTH_LONG).show()}catch(e:Exception){Toast.makeText(this@MainActivity,e.message?:"Download failed",Toast.LENGTH_LONG).show()}}}
 }
 override fun onDestroy(){web.destroy();executor.shutdown();super.onDestroy()}
}
