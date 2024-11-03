package ss.tetris.game.view

import scalafx.scene.control.{Slider}
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml
import ss.tetris.game.util.MediaPlayerManager

@sfxml
class SettingsController(
                          private val volumeSlider: Slider,
                        ) {

  // Method to mute the media player
  def handleMute(action: ActionEvent): Unit = {
    MediaPlayerManager.mute()
  }

  // Method to mute the media player
  def handleUnmute(action: ActionEvent): Unit = {
    MediaPlayerManager.unmute()
  }

  def handleVolume(): Unit = {
    // Initialize the slider with the current volume
    volumeSlider.value = MediaPlayerManager.volume * 100

    // Add listener to update volume when slider is moved
    volumeSlider.value.onChange {
      MediaPlayerManager.volume = volumeSlider.value.doubleValue() / 100
    }

  }
}
