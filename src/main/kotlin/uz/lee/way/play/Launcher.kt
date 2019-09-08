package uz.lee.way.play

import uz.lee.way.play.api.SarkorApi
import java.io.File
import java.lang.RuntimeException

fun main(args: Array<String>) {
    if(args.size < 2){
        throw RuntimeException("Args not found")
    }
    val api = SarkorApi().apply { login(args[0], args[1]) }
    val channels = api.getPlaylists()
    val file = genFile()
    val channelLength = channels.length()

    for (i in 0 until channelLength) {
        val channel = channels.getJSONObject(i)
        val id = channel.getInt("id")
        val group = api.findCategoryName(channel.getString("categories"))
        val title = channel.getString("title")
        val url = api.getUrlPlaylist(id)
        println("id: $id, category: $group, title: $title")
        file.appendText("#EXTINF:-1 group-title=\"$group\",$title\n")
        file.appendText(url)
        if (i != channelLength - 1) {
            file.appendText("\n")
        }
    }
}

fun genFile(): File {
    val file = File("playlist/tv.m3u")
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    file.writeText("#EXTM3U\n")
    return file
}


fun testFile() {
    val file = File("playlist/tv.m3u")
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    file.writeText("#EXTM3U\n")
    file.appendText("#EXTINF:-1,Test\n")
    file.appendText("#EXTINF:-1,Test2\n")
    println("finished")

}