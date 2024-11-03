package ss.tetris.game

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import scalafx.stage.{Modality, Stage, StageStyle}
import scalafx.scene.image.Image
import scalafx.scene.media.MediaPlayer
import ss.tetris.game.model.User
import ss.tetris.game.util.{Database, MediaPlayerManager}

object MyApp extends JFXApp {
  Database.setupDB()
  val userData = new ObservableBuffer[User]()

  // Initialize user data
  userData ++= User.getAllUsers

  var username: String = ""

  val sound: MediaPlayer = MediaPlayerManager.resourceAudio("ThemeSoundTrack")

  val sound3: MediaPlayer = MediaPlayerManager.resourceAudio("TetrisGame")

  val rootResource = getClass.getResource("view/RootLayout.fxml")

  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Tetris"
    // Set the application icon
    icons += new Image(getClass.getResourceAsStream("/images/GameIcon.png"))
    scene = new Scene {
      root = roots
    }
  }

  def showWelcomePage(): Unit = {
    val resource = getClass.getResource("view/WelcomePage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    sound.play()
  }


  def showDashboard(): Unit = {
    val resource = getClass.getResource("view/Dashboard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    sound.play()
  }

  def showGameBoard(): Unit = {
    val resource = getClass.getResource("view/GameBoard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    userData.clear()
    sound.stop()
    sound3.play()

  }

  def showHowToPlayPage(): Unit = {
    val resource = getClass.getResource("view/HowToPlayPage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showLeaderboard(): Unit = {
    val resource = getClass.getResource("view/Leaderboard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showSettingPage(onClose: () => Unit): Unit = {
    val resource = getClass.getResource("view/Settings.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    val settingsStage = new Stage()
    settingsStage.setTitle("Settings")
    val scene = new Scene(roots)
    settingsStage.setScene(scene)
    // Make the settings window modal, blocking interactions with other windows until it is closed
    settingsStage.initModality(Modality.ApplicationModal)
    // Show the settings window and set a listener to handle its closure
    settingsStage.setOnHidden(_ => onClose())
    settingsStage.show()
  }


  stage.setResizable(false)
  showWelcomePage()
}
