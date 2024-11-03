package ss.tetris.game.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  implicit class RichString(val s: String) extends AnyVal {
    def parseLocalDateTime: LocalDateTime = LocalDateTime.parse(s, formatter)
  }

  implicit class RichLocalDateTime(val dt: LocalDateTime) extends AnyVal {
    def formatLocalDateTime: String = dt.format(formatter)
  }
}
