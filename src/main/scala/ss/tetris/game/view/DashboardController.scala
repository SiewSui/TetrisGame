package ss.tetris.game.view
import scalafx.application.Platform
import scalafx.scene.control.{Alert, Button, TextField}
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafxml.core.macros.sfxml
import ss.tetris.game.MyApp
import ss.tetris.game.MyApp.{showDashboard, showGameBoard, showHowToPlayPage, showLeaderboard, showSettingPage, showWelcomePage, userData}

@sfxml
class DashboardController(
                           private val usernameField: TextField
                         ) {

  def handleProceed(event: ActionEvent): Unit = {
    val username = usernameField.text.value.trim
    if (username.isEmpty) {
      val infoAlert = new Alert(AlertType.Warning) {
        title = "Input Error"
        contentText = "Username cannot be empty."
      }
      infoAlert.showAndWait()
    } else {
      MyApp.username = username
      showDashboard()
    }
  }

  def handleStartGame(action: ActionEvent): Unit = {
    showGameBoard()
  }

  def handleHowToPlayButton(action: ActionEvent): Unit = {
    showHowToPlayPage()

  }

  def handleLeaderboardButton(action: ActionEvent): Unit = {
    showLeaderboard()
  }

  def handleBacktoWelcomePage(action: ActionEvent): Unit = {
    showWelcomePage()
  }

  def handleBacktoDashboard(action: ActionEvent): Unit = {
    showDashboard()
  }

  def handleSetting(): Unit = {
    showSettingPage(() => {}
    )
  }

  def handleClose(action: ActionEvent): Unit = {
    // Exit the application
    Platform.exit()
  }
}

