package org.billthefarmer.currency.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import org.billthefarmer.currency.composition.composed
import org.billthefarmer.currency.presentation.model.MainViewModel
import org.billthefarmer.currency.presentation.view.main.Main
import org.billthefarmer.currency.presentation.view.main.MainViewComposition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val composition: MainViewComposition = composed(Main)
        setContent {
            composition.Compose(model = MainViewModel())
        }
    }

}