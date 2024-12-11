package uiGame

import android.content.Context
import android.media.MediaPlayer
import com.example.myapplication.R

class MusicManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_music)
            mediaPlayer?.isLooping = true
        }
        mediaPlayer?.start()
    }

    fun stopMusic() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()
                }
                mediaPlayer?.release()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } finally {
                mediaPlayer = null
            }
        }
    }

    fun setVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume) // Valores de 0.0f (mudo) a 1.0f (máximo)
    }
}
