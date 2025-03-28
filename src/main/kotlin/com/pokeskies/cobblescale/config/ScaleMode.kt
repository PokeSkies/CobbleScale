package com.pokeskies.cobblescale.config

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore

enum class ScaleMode(val calculation: (party: PlayerPartyStore) -> Int?) {
    HIGHEST({ party -> party.maxOfOrNull { it.level } }),
    AVERAGE({ party -> party.sumOf { it.level } / party.occupied() }),
    MEDIAN({ party -> party.sortedBy { it.level }[party.occupied() / 2].level }),
}
