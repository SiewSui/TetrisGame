package ss.tetris.game.model

object Tetromino {

  val IShape = List(
    //vertical rotation
    List(Array(0, 0), Array(0, 1), Array(0, 2), Array(0, 3)),
    //horizontal rotation
    List(Array(0, 0), Array(1, 0), Array(2, 0), Array(3, 0))
  )

  val LShape = List(
    //Rotation 0
    List(Array(0, 0), Array(0, 1), Array(0, 2), Array(1, 2)),
    //Rotation 90
    List(Array(0, 1), Array(0, 0), Array(1, 0), Array(2, 0)),
    //Rotation 180
    List(Array(0, 0), Array(1, 0), Array(1, 1), Array(1, 2)),
    //Rotation 270
    List(Array(0, 1), Array(1, 1), Array(2, 1), Array(2, 0))
  )

  val JShape = List(
    //Rotation 0
    List(Array(1, 0), Array(0, 0), Array(0, 1), Array(0, 2)),
    //Rotation 90
    List(Array(0, 0), Array(1, 0), Array(2, 0), Array(2, 1)),
    //Rotation 180
    List(Array(0, 2), Array(1, 2), Array(1, 1), Array(1, 0)),
    //Rotation 270
    List(Array(0, 0), Array(0, 1), Array(1, 1), Array(2, 1))
  )

  val TShape = List(
    //Rotation 0
    List(Array(1, 0), Array(0, 1), Array(1, 1), Array(1, 2)),
    //Rotation 90
    List(Array(1, 0), Array(0, 1), Array(1, 1), Array(2, 1)),
    //Rotation 180
    List(Array(0, 0), Array(0, 1), Array(1, 1), Array(0, 2)),
    //Rotation 270
    List(Array(0, 0), Array(1, 0), Array(2, 0), Array(1, 1))
  )

  val OShape = List(
    //Rotation 0
    List(Array(0, 0), Array(0, 1), Array(1, 0), Array(1, 1)),
    List(Array(0, 0), Array(0, 1), Array(1, 0), Array(1, 1))
  )

  val SShape = List(
    //Rotation 0
    List(Array(0, 0), Array(0, 1), Array(1, 1), Array(1, 2)),
    //Rotation 90
    List(Array(0, 1), Array(1, 1), Array(1, 0), Array(2, 0))
  )

  val ZShape= List(
    //Rotation 0
    List(Array(1, 0), Array(1, 1), Array(0, 1), Array(0, 2)),
    //Rotation 90
    List(Array(0, 0), Array(1, 0), Array(1, 1), Array(2, 1))
  )
}