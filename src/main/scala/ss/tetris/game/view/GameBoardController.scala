package ss.tetris.game.view

import scalafx.Includes._
import scalafx.scene.control.{Label}
import scalafx.scene.layout.{AnchorPane, GridPane}
import scalafx.scene.media.{MediaPlayer}
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import scalafx.animation._
import scala.util.{Failure, Random, Success}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.application.Platform
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.paint.Color
import ss.tetris.game.MyApp
import ss.tetris.game.MyApp.{sound3, showDashboard, showSettingPage, userData}
import ss.tetris.game.model.{TetrisColor, Tetromino, User}
import ss.tetris.game.util.MediaPlayerManager

@sfxml
class GameBoardController(tetris: AnchorPane, tetrisBoard: GridPane,
                          nextPieceBoard: GridPane,
                          score: Label, showPaused: Label) {

  // hold the score of the game
  var scores = 0
  var finalScore = 0

  // hold all the tetrominos
  val tetromino: List[List[List[Array[Int]]]] = List(
    Tetromino.IShape,
    Tetromino.JShape,
    Tetromino.LShape,
    Tetromino.TShape,
    Tetromino.OShape,
    Tetromino.ZShape,
    Tetromino.SShape
  )

  // hold the piece with the orientation
  var currentTetromino: List[List[Array[Int]]] = List()

  // hold currentPiece data
  var currentPiece: List[Array[Int]] = List()

  // hold the color of the tetromino
  var color: Color = _   // Variable to store the color of the current piece
  var nextPieceColor: Color = _ // Variable to store the color of the next piece

  // hold the current orientation number
  var currentZ: Int = 0

  // hold the currentY
  var currentY: Int = 0

  // hold the currentX
  var currentX: Int = 5

  // hold nextPiece data
  var nextPiece: List[List[Array[Int]]] = List()

  // a virtual board that holds all the diePiece data
  var board = Array.ofDim[Int](20, 10)

  // whether gameOver or not
  var gameOver = false

  // the soundtrack of the game
  val sound2: MediaPlayer = MediaPlayerManager.resourceAudio( "TetrisDrop")
  val sound4: MediaPlayer = MediaPlayerManager.resourceAudio( "FinishGame")
  val sound5: MediaPlayer = MediaPlayerManager.resourceAudio( "RotateTetris")
  val sound6: MediaPlayer = MediaPlayerManager.resourceAudio( "Clearline")

  // Initialize the board
  for (row <- 0 until 20) {
    for (col <- 0 until 10) {
      board(row)(col) = 0
    }
  }

  // Create rectangles for the game board and next piece board
  def createRectangles(rows: Int, cols: Int, board: GridPane): List[List[Rectangle]] = {
    // Create rectangles for each cell
    val rectangles = (0 until rows).map { row =>
      (0 until cols).map { col =>
        new Rectangle {
          width = 27
          height = 27
          fill = "white"
        }
      }.toList
    }.toList

    // Add rectangles to the GridPane
    rectangles.zipWithIndex.foreach { case (rowRects, rowIdx) =>
      rowRects.zipWithIndex.foreach { case (rect, colIdx) =>
        board.add(rect, colIdx, rowIdx)
      }
    }
    rectangles
  }

  // Create rectangles for the game board and next piece board
  val rectangles = createRectangles(20, 10, tetrisBoard)
  val nextPieceRectangles = createRectangles(4, 4, nextPieceBoard)

  // Initialize key press state
  var pause = false
  var leftPressed = false
  var rightPressed = false
  var upPressed = false
  var downPressed = false
  var spacePressed = false

  // Animation timer to control game loop
  var time = 0L
  val timer: AnimationTimer = AnimationTimer(t => {

    // Handle next piece (get from nextPiece)
    if (nextPiece.isEmpty) {
      nextPieceColor = TetrisColor.randomColor
      nextPiece = randomPiece()
      for (a <- 0 until nextPiece(0).size) {
        nextPieceRectangles(nextPiece(0)(a)(1))(nextPiece(0)(a)(0)+1).fill = nextPieceColor
      }
    }

    // Handle current piece
    if (currentPiece.isEmpty) {
      checkRows()
      val tmpPiece = nextPiece
      currentTetromino = tmpPiece
      color = nextPieceColor // Use the next piece's color for the current piece
      for (a <- 0 until nextPiece(0).size) {
        nextPieceRectangles(nextPiece(0)(a)(1))(nextPiece(0)(a)(0)+1).fill = "white"
      }
      nextPiece = List()
      currentZ = 0
      currentX = 4
      currentY = 0
      currentPiece = currentTetromino(0)

      if (checkGameOver()) {
        gameOver = true
        timer.stop
        finalScore = scores
        saveUserData()
        Platform.runLater {
          val alert = new Alert(AlertType.Information) {
            title = "Game Over"
            headerText = s"You've reached the top of the grid, and the game has ended. Your final score is $finalScore"
            contentText = "Don't give up! Try again to beat your high score!"
          }

          sound3.stop()
          sound4.play()
          alert.showAndWait()
          showDashboard()
        }
      }
      // Paint the current piece on the board
      for (a <- 0 until currentPiece.size) {
        rectangles(currentPiece(a)(1)+currentY)(currentPiece(a)(0)+currentX).fill = color
      }
    }

    // call the keypress method
    handleKeyPress()

    // Move down every second
    if ((t - time) > 1e+9) {
      move(0,1)
      time = t
    }
  })

  timer.start

  // Handle key events for controlling the game
  def handleKeyEvent(): Unit = {
    tetris.requestFocus()
    tetris.onKeyPressed = (e: KeyEvent) => {
      e.code match {
        case KeyCode.Left => leftPressed = true
        case KeyCode.Right => rightPressed = true
        case KeyCode.Up => upPressed = true
        case KeyCode.Down => downPressed = true
        case KeyCode.Space => spacePressed = true
        case KeyCode.P =>
          handlePauseGame()
        case _ =>
      }
    }
  }

  // the method after a key is pressed
  def handleKeyPress() {
    if (leftPressed) {
      move(-1, 0)
      leftPressed = false
    }
    if (rightPressed) {
      move(1, 0)
      rightPressed = false
    }
    if (upPressed) {
      if (isRotatable()) {
        sound5.play()
        sound5.stop()
        clearPieceFromBoard(currentPiece, currentX, currentY)
        currentPiece = rotate()
        paintPieceToBoard(currentPiece, currentX, currentY)
      }
      upPressed = false
    }
    if (downPressed) {
      move(0, 1)
      downPressed = false
    }
    if (spacePressed) {
      do {
        sound2.play()
        sound2.stop()
        move(0,1)
      }
      while (!currentPiece.isEmpty)
      spacePressed = false
    }
  }

  // Rotate the current piece
  def rotate(): List[Array[Int]] = {
    if (currentZ + 1 >= currentTetromino.size) {
      currentZ = 0
      currentTetromino(currentZ)
    } else {
      currentZ += 1
      currentTetromino(currentZ)
    }
  }

  // Check if the piece can be rotated
  def isRotatable(): Boolean = {
    val piece = currentTetromino((currentZ + 1) % currentTetromino.size)
    for (a <- 0 until piece.size) {
      // check whether it hits the side
      if ((piece(a)(0) + currentX) >= 10 || (piece(a)(0) + currentX) < 0) {
        return false
      }

      // check whether it hits the bottom
      if ((piece(a)(1) + currentY) >= 20) {
        return false
      }

      // check whether it is occupied
      if ((board((piece(a)(1) + currentY))(piece(a)(0) + currentX) == 1)) {
        return false
      }
    }
    true
  }
  // Check for game over condition
  def checkGameOver(): Boolean = {
    for (a <- 0 until currentPiece.size) {
      if (collisionDetection(currentPiece(a),currentX,currentY)) {
        return true
      }
    }
    for (row <- 0 until 2) {
      for (col <- 0 until 10) {
        if (board(row)(col) == 1) {
          return true
        }
      }
    }
    false
  }

  // Paint the current piece onto the virtual board
  def paintBoard(currentX: Int, currentY: Int) {
    for (a <- 0 until currentPiece.size) {
      val tmpCol = currentPiece(a)(0) + currentX
      val tmpRow = currentPiece(a)(1) + currentY
      board(tmpRow)(tmpCol) = 1
    }
    refreshGameBoard()
  }

  // Check and clear completed rows
  def checkRows() {
    for (row <- 0 until board.size) {
      if (isRowComplete(row)) {
        sound6.play()
        sound6.stop()
        clearRow(row)
        rowDrop(row)
        refreshGameBoard()
      }
    }
  }

  // Clear a row and add score
  def clearRow(row: Int) {
    // turn everything on that row to 0
    for (col <- 0 until board(row).size) {
      board(row)(col) = 0
    }
    addScore(10)
  }

  // Drop rows above the cleared row
  def rowDrop(row: Int) {
    for (rows <- row to 0 by -1) {
      for (col <- 0 until board(rows).size) {
        if (board(rows)(col) == 1) {
          board(rows+1)(col) = 1
          board(rows)(col) = 0
        }
      }
    }
  }

  // Check if a row is complete
  def isRowComplete(row: Int): Boolean = {
    for (col <- 0 until board(row).size) {
      if (board(row)(col) == 0) {
        return false
      }
    }
    return true
  }

  // add score
  def addScore(point: Int) {
    scores += point
    score.setText(scores.toString)
  }

  // Check if a piece collides with the board
  def collisionDetection(piece: Array[Int], currentX: Int, currentY: Int): Boolean = {
    if (board(piece(1)+currentY)(piece(0)+currentX) == 1) {
      return true
    }
    return false
  }

  // Refresh the game board display
  def refreshGameBoard() = {
    // row
    for (row <- 0 until 20) {
      // column
      for (col <- 0 until 10) {
        // if it occupy, fill blue, else white
        if (board(row)(col) == 1) {
          rectangles(row)(col).fill = Color.rgb(100, 149, 237)
        } else {
          rectangles(row)(col).fill = "white"
        }
      }
    }
  }

  // return a random tetromino
  def randomPiece(): List[List[Array[Int]]] = {
    tetromino(Random.nextInt(tetromino.size))
  }

  // move method the piece
  // pass x & y to move
  def move(x: Int, y: Int): Unit = {

    for (a <- 0 until currentPiece.size) {
      // check if it hits the side of the board
      if (currentPiece(a)(0) + currentX + x >= 10 || currentPiece(a)(0) + currentX + x < 0) {
        return
      }
      // check for collisions
      if (collisionDetection(currentPiece(a),(currentX + x),(currentY))) {
        return
      }

      // check if it hits the bottom of the board
      if ((currentPiece(a)(1) + currentY + y) > 19) {
        paintBoard(currentX + x,currentY)
        currentPiece = List()
        return
      }

      // check whether it collides the other pieces
      if (collisionDetection(currentPiece(a),(currentX + x),(currentY + y))) {
        paintBoard(currentX + x,currentY)
        currentPiece = List()
        return
      }
    }

    clearPieceFromBoard(currentPiece,currentX,currentY)
    currentX += x
    currentY += y
    paintPieceToBoard(currentPiece,currentX,currentY)
  }

  def clearPieceFromBoard(piece: List[Array[Int]], currentX: Int, currentY: Int) = {
    // paint the board back to white according to the piece
    for (a <- 0 until piece.size) {
      rectangles(piece(a)(1)+currentY)(piece(a)(0)+currentX).fill = "white"
    }
  }

  def paintPieceToBoard(piece: List[Array[Int]], currentX: Int, currentY: Int) = {
    // paint the board back blue according to the piece
    for (a <- 0 until piece.size) {
      rectangles(piece(a)(1)+currentY)(piece(a)(0)+currentX).fill = color
    }
  }

  def saveUserData(): Unit = {
    val user = new User(MyApp.username) // Use the global username from MyApp
    user.updateScoreAndTime(finalScore)
    MyApp.userData ++= User.getAllUsers
  }

  def handlePauseGame(): Unit = {
    if (!pause && !gameOver) {
      timer.stop()
      showPaused.setText("Game Paused!")
      pause = true
    } else if (pause && !gameOver) {
      timer.start()
      showPaused.setText("")
      pause = false
    }
  }

  def handleSettings(): Unit = {
    handlePauseGame()
    showSettingPage(() => {
      if (pause && !gameOver) {
        timer.start()
        showPaused.setText("")
        pause = false
      }
    })
  }
}