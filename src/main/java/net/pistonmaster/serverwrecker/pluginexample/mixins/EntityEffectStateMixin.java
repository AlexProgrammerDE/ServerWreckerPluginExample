/*
 * ServerWrecker
 *
 * Copyright (C) 2023 ServerWrecker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package net.pistonmaster.serverwrecker.pluginexample.mixins;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import net.pistonmaster.serverwrecker.pluginexample.ExampleServerExtension;
import net.pistonmaster.serverwrecker.protocol.ExecutorManager;
import net.pistonmaster.serverwrecker.protocol.bot.model.EffectData;
import net.pistonmaster.serverwrecker.protocol.bot.state.EntityEffectState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityEffectState.class)
public class EntityEffectStateMixin {
    @Inject(method = "getEffect", at = @At(value = "HEAD"), cancellable = true)
    private void returnJumpBoost(Effect effect, CallbackInfoReturnable<Optional<EffectData>> cir) {
        var botConnection = ExecutorManager.BOT_CONNECTION_THREAD_LOCAL.get();
        if (botConnection != null
            && effect == Effect.JUMP_BOOST
            && botConnection.settingsHolder().get(ExampleServerExtension.HackJumpBoostSettings.HACK_JUMP_BOOST)) {
            cir.setReturnValue(Optional.of(new EffectData(
                effect,
                botConnection.settingsHolder().get(ExampleServerExtension.HackJumpBoostSettings.JUMP_BOOST_LEVEL),
                1,
                false,
                false
            )));
        }
    }
}
