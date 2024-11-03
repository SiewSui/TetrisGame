package ss.tetris.game.util
import scalikejdbc._
import ss.tetris.game.model.User
trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  //protocal
  val dbURL = "jdbc:derby:TetrisDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  // only the 1sr connection will create account
  ConnectionPool.singleton(dbURL, "tetris", "game")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession
  // provide type for all sigleton object


}
//import all and it will initialise it
object Database extends Database{

  def setupDB() = {
    if (!hasDBInitialize) {
      User.initializeTable()
    }
  }
  def hasDBInitialize : Boolean = {

    DB getTable "tetris_user" match {
      case Some(x) => true
      case None => false
    }

  }
}

