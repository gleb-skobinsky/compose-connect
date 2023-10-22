package common.util

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage

suspend fun loadImage() {
    val storageRef = Firebase.storage.reference
    val image = storageRef.child("profile_me.jpg")
    val url = image.getDownloadUrl()
    println(url)
}