package com.example.ringtuneandwallpaper.utility

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.example.ringtuneandwallpaper.R

fun NavController.navigateForward(
    directions: NavDirections
){
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    this.navigate(directions, navOptions)
}

fun NavController.navigateBack(
    directions: NavDirections
){
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left)
        .setExitAnim(R.anim.slide_out_right)
        .setPopEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_left)
        .build()

    this.navigate(directions, navOptions)
}