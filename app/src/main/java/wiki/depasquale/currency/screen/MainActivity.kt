package wiki.depasquale.currency.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import wiki.depasquale.currency.composition.composed
import wiki.depasquale.currency.presentation.view.main.Main
import wiki.depasquale.currency.presentation.view.main.MainViewComposition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val composition: MainViewComposition = composed(Main)
        setContent {
            composition.Compose(model = MainViewModel())
        }
    }

}