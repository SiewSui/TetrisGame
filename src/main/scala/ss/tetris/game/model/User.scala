package ss.tetris.game.model

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import java.time.LocalDateTime
import ss.tetris.game.util.Database
import scalikejdbc._
import scala.util.{Failure, Success, Try}

class User(val usernameS: String) extends Database {
  def this() = this(null)


  var username = new StringProperty(usernameS)
  var score = IntegerProperty(0)
  var time = ObjectProperty[LocalDateTime](LocalDateTime.now)

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into tetris_user (username, score, time) values
          (${username.value}, ${score.value}, ${time.value})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
          update tetris_user
          set
          score = ${score.value},
          time = ${time.value}
          where username = ${username.value}
        """.update.apply()
      })
    }
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from tetris_user where  username = ${username.value}
      """.map(rs => rs.string("username")).single.apply()
    } match {
      case Some(_) => true
      case None => false
    }
  }

  def updateScoreAndTime(newScore: Int): Try[Int] = {
    if (newScore > score.value) {
      score.value = newScore
      time.value = LocalDateTime.now
      save()
    } else {
      Success(0) // Return success with no rows updated if the new score is not higher
    }
  }
}

object User extends Database {
  def apply(usernameS: String, scoreI: Int, timeS: LocalDateTime): User = {
    new User(usernameS) {
      score.value = scoreI
      time.value = timeS
    }
  }

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      sql"""
          create table tetris_user (
          id int not null generated always as identity (start with 1, increment by 1),
          username varchar(255) not null primary key,
          score int,
          time timestamp
        )
      """.execute.apply()
    }
  }

  def getAllUsers: List[User] = {
    DB readOnly { implicit session =>
      sql"select * from tetris_user order by score desc , time asc".map(rs => User(
        rs.string("USERNAME"),
        rs.int("SCORE"),
        rs.localDateTime("TIME")
      )).list.apply()
    }
  }
}
