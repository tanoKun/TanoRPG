package com.github.tanokun.tanorpg.util

import com.github.tanokun.tanorpg.TanoRPG
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable
import java.util.stream.Stream

fun bindHologramAtLocationAndEntity(text: String?, location: Location?, entity: Entity, hy: Double, ticks: Int, offsetX: Double, offsetY: Double, offsetZ: Double) {
    val hologram = HologramsAPI.createHologram(TanoRPG.plugin, location)
    hologram.appendTextLine(text)
    object : BukkitRunnable() {
        var time: Long = 0
        override fun run() {
            time++
            val loc = entity.location
            loc.add(0.0, hy, 0.0)
            hologram.teleport(loc.add(offsetX, offsetY, offsetZ))
            if (time > ticks) {
                hologram.delete()
                cancel()
            }
        }
    }.runTaskTimer(TanoRPG.plugin, 1L, 1L)
}

fun bindHologramAtLocation(texts: Array<String?>, location: Location, ticks: Int, offsetX: Double, offsetY: Double, offsetZ: Double) {
    val hologram = HologramsAPI.createHologram(TanoRPG.plugin, location)
    val copy = arrayOf(location.clone())
    Stream.of(*texts).forEach { s: String? -> hologram.appendTextLine(s) }
    object : BukkitRunnable() {
        var time: Long = 0
        override fun run() {
            time++
            copy[0] = location.clone()
            hologram.teleport(copy[0].add(offsetX, offsetY, offsetZ))
            if (time > ticks) {
                hologram.delete()
                cancel()
            }
        }
    }.runTaskTimer(TanoRPG.plugin, 1L, 1L)
}