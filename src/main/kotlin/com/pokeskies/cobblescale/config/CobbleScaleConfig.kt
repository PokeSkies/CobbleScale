package com.pokeskies.cobblescale.config

import com.google.gson.annotations.SerializedName
import net.minecraft.util.RandomSource

class CobbleScaleConfig(
    var debug: Boolean = false,
    val enabled: Boolean = true,
    @SerializedName("level_offset")
    val levelOffset: LevelOffset = LevelOffset(),
    @SerializedName("min_level")
    val minLevel: Int = 5,
    @SerializedName("max_level")
    val maxLevel: Int = 100,
    @SerializedName("scale_mode")
    val scaleMode: ScaleMode = ScaleMode.HIGHEST,
    @SerializedName("blacklisted_pokemon")
    val blacklistedPokemon: List<String> = listOf("cobblemon:mewtwo", "cobblemon:mew"),
    @SerializedName("blacklisted_biomes")
    val blacklistedBiomes: List<String> = listOf(),
    @SerializedName("blacklisted_dimensions")
    val blacklistedDimensions: List<String> = listOf(),
    @SerializedName("biome_scaling")
    val biomeScaling: Map<String, Double> = mapOf(
        "minecraft:mountains" to 1.25,
        "minecraft:plains" to 1.0,
        "minecraft:desert" to 1.1,
    ),
    @SerializedName("enabled_dimensions")
    val enabledDimensions: List<String> = listOf("minecraft:overworld"),
) {
    class LevelOffset(
        val min: Int = 0,
        val max: Int = 0,
    ) {
        fun getRandomOffset(random: RandomSource): Int {
            return random.nextIntBetweenInclusive(min, max)
        }
    }

    override fun toString(): String {
        return "CobbleScaleConfig(debug=$debug, enabled=$enabled, levelOffset=$levelOffset, minLevel=$minLevel, " +
                "maxLevel=$maxLevel, scaleMode=$scaleMode, blacklistedPokemon=$blacklistedPokemon, " +
                "blacklistedBiomes=$blacklistedBiomes, blacklistedDimensions=$blacklistedDimensions, " +
                "biomeScaling=$biomeScaling, enabledDimensions=$enabledDimensions)"
    }
}
