package mx.ipn.cic.geo.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import java.util.*

// Pasos para crear la aplicación desde cero.
// 1.- Crear proyecto en Android Studio (Empty Activity).
// 2.- Definir icono para el juego.
// 3.- Quitar action bar del MainActivity.
// 4.- Copiar imágenes ficha amarilla y ficha roja.
// 5.- Diseño del layout XML (Introducción GridLayout & GridLayout AndroidX).
// 6.- Implementación del método dropIn, asignarlo a cada una de las fichas (Animaciones).
// 7.- Uso del atributo de clase jugador_actual, para alternar entre ficha.
// 8.- Definición del array para llevar el control de los tiros en las casillas.
// 9.- Agregar tag a cada imageView para representar el índice del arreglo tablero.
// 10.- Agregar el uso de posicion_tiro para saber si esa casilla ya está ocupada por alguna ficha o no.
// 11.- Definir el arreglo posiciones_ganadoras.
// 12.- Buscar si hay una combinación ganadora posible.
// 13.- Diseñar el layout del mensaje de que hay un jugador ganador.
// 14.- Mostrar mensaje en caso de que haya algún ganador.
// 15.- Implementar método playAgain e inicializar las variables.
// 16.- Atributo para saber si el juego está activo o no.
// 17.- Implementar sección para determinar si hay un empate.

class MainActivity : AppCompatActivity() {
   // Para llevar el control del jugador actual.
   // 0 -> ficha amarilla, 1 -> ficha roja.
   private val jugadorAmarillo = 0
   private val jugadorRojo = 1
   private val vacio = 2

   private var jugadorActual = jugadorAmarillo

   // Permitirá conocer si una casilla ya está ocupada por alguna ficha o no.
   private val tablero = intArrayOf(vacio, vacio, vacio, vacio, vacio, vacio, vacio, vacio, vacio)

   // Definir la combinación de posiciones ganadoras.
   private val posicionesGanadoras = arrayOf(
      intArrayOf(0, 1, 2),
      intArrayOf(3, 4, 5),
      intArrayOf(6, 7, 8),
      intArrayOf(0, 3, 6),
      intArrayOf(1, 4, 7),
      intArrayOf(2, 5, 8),
      intArrayOf(0, 4, 8),
      intArrayOf(2, 4, 6)
   )
   private var juegoActivo = true

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

      // Código para ocultar el action bar.
      this.supportActionBar?.hide()
   }

   fun dropIn(view: View) {
      val counter: ImageView = view as ImageView

      // Recuperar la posición de la ficha.
      val posicionTiro: Int = counter.tag.toString().toInt()
      // Si la casilla no está vacía, no se puede tirar en ella.
      if (tablero[posicionTiro] != vacio || !juegoActivo) return
      // Indicar que un jugador acaba de tirar en esta casilla.
      tablero[posicionTiro] = jugadorActual
      counter.translationY = -1000f
      jugadorActual = if (jugadorActual == jugadorAmarillo) {
         counter.setImageResource(R.drawable.ficha_amarilla)
         jugadorRojo
      } else {
         counter.setImageResource(R.drawable.ficha_roja)
         jugadorAmarillo
      }
      counter.animate().translationYBy(1000f).rotation(360f).duration = 300

      // Determinar si hay un ganador.
      for (values in posicionesGanadoras) {
         if (tablero[values[0]] != vacio &&
            tablero[values[0]] == tablero[values[1]] &&
            tablero[values[1]] == tablero[values[2]]
         ) {
            val textoGanador: String = if (jugadorActual == jugadorAmarillo)
               "El ganador es el jugador rojo"
            else
               "El ganador es el jugador amarillo"
            showLinearLayout(textoGanador)
         } else {
            if (Arrays.stream(tablero).noneMatch { x -> x == vacio })
               showLinearLayout("Juego Empatado")
         }
      }
   }

   private fun showLinearLayout(mensaje: String) {
      // Mostrar el layout.
      val txtViewMensaje = findViewById<TextView>(R.id.txtViewGanador)
      txtViewMensaje.text = mensaje
      val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutMensaje)
      linearLayout.visibility = View.VISIBLE
      juegoActivo = false
   }

   fun playAgain(view: View) {
      // Ocular el layout.
      val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutMensaje)
      linearLayout.visibility = View.INVISIBLE
      // Que el primero en tirar sea el jugador que ganó.
      jugadorActual = if (jugadorActual == jugadorAmarillo) jugadorRojo else jugadorAmarillo
      Arrays.fill(tablero, vacio)
      // Borrar todas las imágenes de las fichas.
      val gridLayout: GridLayout = findViewById(R.id.gridLayoutTablero)
      for (i in 0 until gridLayout.childCount) {
         (gridLayout.getChildAt(i) as ImageView).setImageResource(0)
      }
      juegoActivo = true
   }
}