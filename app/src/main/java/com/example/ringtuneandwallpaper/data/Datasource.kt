package com.example.ringtuneandwallpaper.data

import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.Ringtone
import com.example.ringtuneandwallpaper.model.Wallpaper

class Datasource {
    fun loadRingtone(): List<Ringtone>{
        return listOf(
            Ringtone(R.raw.morgan),
            Ringtone(R.raw.alala_sayang),
            Ringtone(R.raw.bagaikan_langit),
            Ringtone(R.raw.bengali),
            Ringtone(R.raw.khai_bahar),
            Ringtone(R.raw.kisah_cinta_kita),
            Ringtone(R.raw.rintihan_rindu_wanymmmmmmmmmmmmmmmmmmmmmmmmmmmmm),
            Ringtone(R.raw.sinner_like_me),
            Ringtone(R.raw.siti_bilang_cuti),
            Ringtone(R.raw.ziggy_zagga)
        )
    }

    fun loadWallpaper(): List<Wallpaper>{
        return listOf(
            Wallpaper(R.raw.pexels_iriser_1707215),
            Wallpaper(R.raw.pexels_arunbabuthomas_1156684),
            Wallpaper(R.raw.pexels_bertellifotografia_799443),
            Wallpaper(R.raw.pexels_eberhardgross_1366919),
            Wallpaper(R.raw.pexels_eberhardgross_1624496),
            Wallpaper(R.raw.pexels_iamtausifhossain9321_1226302),
            Wallpaper(R.raw.pexels_ollivves_1433052),
            Wallpaper(R.raw.pexels_rahulp9800_1212487),
            Wallpaper(R.raw.pexels_rpnickson_2486168),
            Wallpaper(R.raw.pexels_todd_trapani_488382_1535162)
        )
    }
}