package ss.tetris.game.util
import scalafx.scene.media.{Media, MediaPlayer}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object MediaPlayerManager {

  // Store all MediaPlayer instances in a ListBuffer
  private val mediaPlayers: ListBuffer[MediaPlayer] = ListBuffer()
  private var _volume: Double = 1.0 // Default to 100% volume


  //Method to load and store media player instances

  def resourceAudio(path: String): MediaPlayer = {
    val resourcePath = s"/sounds/$path.mp3"
    val resource = getClass.getResource(resourcePath)
    if (resource == null) throw new IllegalArgumentException(s"Audio not found: $path")
    val media = new Media(resource.toString)
    val mediaPlayer = new MediaPlayer(media)
    mediaPlayers += mediaPlayer
    mediaPlayer
  }

  def mute(): Unit = {
    mediaPlayers.foreach(_.setMute(true))
  }

  def unmute(): Unit = {
    mediaPlayers.foreach(_.setMute(false))
  }


  //   Sets the volume for all media players
  //   The volume level set (0.0 to 1.0)

  def volume_=(volume: Double): Unit = {
    _volume = volume
    mediaPlayers.foreach(_.volume = volume)
  }


  //   Gets the current volume level
  //   return The current volume level
  def volume: Double = _volume
}

