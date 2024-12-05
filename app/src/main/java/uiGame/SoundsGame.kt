package uiGame

import android.content.Context
import android.media.SoundPool
import androidx.annotation.RawRes

class SoundManager(context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(5).build()
    private val soundMap = mutableMapOf<Int, Int>() // Map para los sonidos cargados
    private val loadedSounds = mutableSetOf<Int>() // Set para verificar si el sonido está listo

    init {
        // Listener para verificar cuando un sonido esté cargado
        soundPool.setOnLoadCompleteListener { _, soundId, status ->
            if (status == 0) { // 0 significa que el sonido se cargó correctamente
                loadedSounds.add(soundId)
            }
        }
    }

    fun loadSound(@RawRes resId: Int, context: Context) {
        val soundId = soundPool.load(context, resId, 1)
        soundMap[resId] = soundId
    }

    fun playSound(@RawRes resId: Int) {
        val soundId = soundMap[resId]
        if (soundId != null && loadedSounds.contains(soundId)) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        } else {
            // El sonido no está listo; puedes registrar un log o manejar este caso
            println("Sound $resId not ready")
        }
    }

    fun release() {
        soundPool.release()
    }
}
