package ss.tetris.game.view

import ss.tetris.game.model.User
import ss.tetris.game.MyApp
import scalafx.scene.control.{TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.StringProperty
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import ss.tetris.game.MyApp.{showDashboard, userData}
import ss.tetris.game.util.DateTimeUtil.RichLocalDateTime

import java.time.format.DateTimeFormatter

@sfxml
class LeaderboardController(
                             private val userTable: TableView[User],
                             private val rankingColumn: TableColumn[User, String],
                             private val usernameColumn: TableColumn[User, String],
                             private val scoreColumn: TableColumn[User, String],
                             private val dateColumn: TableColumn[User, String]
                           ) {

  // Initialize Table View display contents model
  private val sortedUsers = MyApp.userData.sortBy(_.score.value).reverse.take(10)
  userTable.items = ObservableBuffer(sortedUsers: _*)

  // Initialize ranking column's cell values
  rankingColumn.cellValueFactory = { cellData =>
    val rank = sortedUsers.indexOf(cellData.value) + 1
    new StringProperty(rank.toString)
  }

  // Initialize columns' cell values
  usernameColumn.cellValueFactory = { _.value.username }

  // Convert IntegerProperty to String for display in TableColumn
  scoreColumn.cellValueFactory = { cellData =>
    new StringProperty(cellData.value.score.value.toString)
  }

  // Convert ObjectProperty[LocalDateTime] to String for display in TableColumn
  dateColumn.cellValueFactory = { cellData =>
    new StringProperty(cellData.value.time.value.formatLocalDateTime)
  }

  def handleBacktoDashboard(action: ActionEvent): Unit = {
    showDashboard()
  }
}

