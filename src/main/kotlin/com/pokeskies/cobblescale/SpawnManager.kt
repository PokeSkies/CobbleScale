package com.pokeskies.cobblescale

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.pokeskies.cobblescale.config.ConfigManager
import com.pokeskies.cobblescale.utils.Utils
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object SpawnManager {
    fun init() {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe { event ->
            if (!ConfigManager.CONFIG.enabled) return@subscribe
            val entity = event.ctx.cause.entity ?: return@subscribe
            if (entity is ServerPlayer) {
                Utils.printDebug("Pokemon ${event.entity.pokemon.species.resourceIdentifier.asString()} has appeared nearby ${entity.name.string} at the level ${event.entity.pokemon.level}!")
                val party = Cobblemon.storage.getParty(entity)

                if (party.occupied() <= 0) {
                    Utils.printDebug("${entity.name.string} doesn't have any Pokemon to check the level with")
                    return@subscribe
                }

                // Check if any blacklists are present
                if (ConfigManager.CONFIG.enabledDimensions.isNotEmpty() && !ConfigManager.CONFIG.enabledDimensions.contains(event.ctx.world.dimension().location().toString())) {
                    Utils.printDebug("Dimension ${event.ctx.world.dimension().location().asString()} is not enabled and other dimensions are present! Leaving this one alone...")
                    return@subscribe
                }
                if (ConfigManager.CONFIG.blacklistedDimensions.contains(event.ctx.world.dimension().location().toString())) {
                    Utils.printDebug("Dimension ${event.ctx.world.dimension().location().asString()} is blacklisted! Leaving this one alone...")
                    return@subscribe
                }
                if (ConfigManager.CONFIG.blacklistedPokemon.contains(event.entity.pokemon.species.resourceIdentifier.asString())) {
                    Utils.printDebug("Pokemon ${event.entity.pokemon.species.resourceIdentifier.asString()} is blacklisted! Leaving this one alone...")
                    return@subscribe
                }
                if (ConfigManager.CONFIG.blacklistedBiomes.contains(event.ctx.biomeName.asString())) {
                    Utils.printDebug("Biome ${event.ctx.biomeName.asString()} is blacklisted! Leaving this one alone...")
                    return@subscribe
                }

                val playerAverage = ConfigManager.CONFIG.scaleMode.calculation.invoke(party)
                if (playerAverage == null) {
                    Utils.printDebug("An error occurred while calculating the average level of ${entity.name.string}'s party! Unsure how that happened...")
                    return@subscribe
                }

                Utils.printDebug("The ${ConfigManager.CONFIG.scaleMode} level of ${entity.name.string}'s party is $playerAverage!")
                var newLevel = playerAverage + ConfigManager.CONFIG.levelOffset.getRandomOffset(event.ctx.world.random)
                Utils.printDebug("After applying a  random level offset, we are at level $newLevel")
                ConfigManager.CONFIG.biomeScaling[event.ctx.biomeName.asString()]?.let {
                    newLevel = (newLevel * it).toInt()
                    Utils.printDebug("Found a biome scale for ${event.ctx.biomeName.asString()}. After applying the biome scaling, we are at level $newLevel")
                }

                newLevel = newLevel.coerceIn(ConfigManager.CONFIG.minLevel, ConfigManager.CONFIG.maxLevel)
                Utils.printDebug("After clamping to the min and max levels, we are now spawning at level $newLevel")

                event.entity.pokemon.level = newLevel
                event.entity.pokemon.initialize()
            }
        }
    }
}
