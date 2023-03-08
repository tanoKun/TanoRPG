package com.github.tanokun.tanorpg.util.image

import org.bukkit.Location
import org.bukkit.util.Vector
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.ParticleData
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class ImageParticles(image: BufferedImage, scanQuality: Int) {
    var ratio: Double = 0.2
    private val particles: HashMap<Vector, Color> = HashMap()
    private val clearence = 300

    init {
        val height = image.height
        val width = image.width
        val sensitivity = abs(scanQuality)
        for(x in 0 until width step sensitivity) {
            for (y in 0 until height step sensitivity) {
                val rgb = image.getRGB(x, y)
                if (-rgb <= clearence) continue
                val javaColor = Color(rgb)
                val vector = Vector(width - 1 - x, height - 1 - y, 0)
                particles[vector] = javaColor
            }
        }
    }

    private fun rotateAroundAxisX(v: Vector, angle_temp: Double): Vector {
        val angle = Math.toRadians(angle_temp)

        val y: Double
        val z: Double
        val cos: Double = cos(angle)
        val sin: Double = sin(angle)
        y = v.y * cos - v.z * sin
        z = v.y * sin + v.z * cos

        return v.setY(y).setZ(z)
    }

    private fun rotateAroundAxisY(v: Vector, angle_temp: Double): Vector {
        var angle = -angle_temp
        angle = Math.toRadians(angle)

        val x: Double
        val z: Double
        val cos: Double = cos(angle)
        val sin: Double = sin(angle)
        x = v.x * cos + v.z * sin
        z = v.x * -sin + v.z * cos

        return v.setX(x).setZ(z)
    }

    fun getParticles(location: Location, pitch: Double, yaw: Double, format: Function<Color, ParticleData>): MutableMap<Location, ParticleData> {
        val map: MutableMap<Location, ParticleData> = HashMap()
        for (note in particles) {
            val difference = note.key.clone().multiply(ratio)
            rotateAroundAxisX(difference, pitch)
            rotateAroundAxisY(difference, yaw)
            map[location.clone().add(difference)] = format.apply(note.value)
        }
        return map
    }

    fun getParticles(location: Location): MutableMap<Location, ParticleData> {
        val r: MutableMap<Location, ParticleData> = getParticles(location, location.pitch.toDouble(), location.yaw.toDouble()) color@{
            val color = RegularColor(it)
            color.effect = ParticleEffect.REDSTONE
            return@color color
        }
        return r
    }

    fun getParticles(location: Location, format: (t: Color) -> ParticleData): MutableMap<Location, ParticleData> {
        return getParticles(location, location.pitch.toDouble(), location.yaw.toDouble(), format)
    }
}

