package ss.tetris.game.model

import scalafx.scene.paint.Color

object TetrisColor {

  // List of colors
  private val colors: List[Color] = List(
    Color.rgb(255, 99, 71),   // Tomato
    Color.rgb(144, 238, 144), // Light Green
    Color.rgb(65, 105, 225),  // Royal Blue
    Color.rgb(147, 112, 219), // Medium Purple
    Color.rgb(188, 143, 143), // Rosy Brown
    Color.rgb(255,140,0),     // dark orange
    Color.rgb(218,165,32)     // Golden rod
  )


  // Method to get a random color from the list
  def randomColor(): Color = colors(scala.util.Random.nextInt(colors.length))
}
