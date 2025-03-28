package com.pokeskies.cobblescale.utils

import com.pokeskies.cobblescale.CobbleScale
import net.minecraft.network.chat.Component

object TextUtils {
    fun toNative(text: String): Component {
        return CobbleScale.INSTANCE.adventure.toNative(CobbleScale.MINI_MESSAGE.deserialize(text))
    }

    fun toComponent(text: String): net.kyori.adventure.text.Component {
        return CobbleScale.MINI_MESSAGE.deserialize(text)
    }
}
