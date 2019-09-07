package uz.lee.way.play.api

import khttp.post
import khttp.get
import org.json.JSONArray
import org.json.JSONObject

class SarkorApi {
    private lateinit var cookie: String
    private lateinit var categories: JSONArray

    fun login() {
        val response = post(
            url = "http://sarkor.tv/api/user/login",
            headers = mapOf("Content-Type" to "application/json"),
            json = mapOf("login" to "ge-7181307", "password" to "7181307")
        )
        val obj = response.jsonObject
        if (obj.optString("status") != "ok") {
            throw RuntimeException("Result.login: $obj")
        }
        cookie = response.headers["Set-Cookie"].toString()
    }

    fun getPlaylists(): JSONArray {
        println("cookie: $cookie")
        val resp = post(
            url = "http://sarkor.tv/api/playlist/get",
            headers = mapOf("Content-Type" to "application/json", "Cookie" to cookie)
        )
        val obj = resp.jsonObject
        if (obj.optString("status") != "ok") {
            throw RuntimeException("Result.getPlaylists: $obj")
        }
        categories = obj.optJSONObject("result").optJSONArray("categories")
        return obj.optJSONObject("result").getJSONArray("channels")

    }

    fun getUrlPlaylist(id: Int): String {
        val resp = get(
            url = "http://sarkor.tv/api/channel/play/$id",
            headers = mapOf("Cookie" to cookie)
        )
        val obj = resp.jsonObject
        if (obj.optString("status") != "ok") {
            throw RuntimeException("Result.getPlaylists: $obj")
        }
        return obj.getJSONObject("result").getString("url")
    }

    fun findCategoryName(str: String): String {
        val split = str.split(" ")
        if (split.size <= 1) {
            return "Мои каналы"
        }
        val s = split[1]
        val category = categories.filter {
            (it as JSONObject).optString("name") == s
        }
        if (category.isEmpty()) {
            return "Мои каналы"
        }
        return (category[0] as JSONObject).optString("title")
    }
}