package com.example.ringtuneandwallpaper.utility

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
class DurationConverter {
    @TypeConverter
    fun fromDuration(duration: Duration?): Long? {
        return duration?.toMillis()
    }

    @TypeConverter
    fun toDuration(millis: Long?): Duration? {
        return millis?.let { Duration.ofMillis(it) }
    }
}