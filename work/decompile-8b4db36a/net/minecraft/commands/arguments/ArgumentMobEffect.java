package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICompletionProvider;
import net.minecraft.core.IRegistry;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.effect.MobEffectList;

public class ArgumentMobEffect implements ArgumentType<MobEffectList> {

    private static final Collection<String> EXAMPLES = Arrays.asList("spooky", "effect");
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_EFFECT = new DynamicCommandExceptionType((object) -> {
        return IChatBaseComponent.translatable("effect.effectNotFound", object);
    });

    public ArgumentMobEffect() {}

    public static ArgumentMobEffect effect() {
        return new ArgumentMobEffect();
    }

    public static MobEffectList getEffect(CommandContext<CommandListenerWrapper> commandcontext, String s) {
        return (MobEffectList) commandcontext.getArgument(s, MobEffectList.class);
    }

    public MobEffectList parse(StringReader stringreader) throws CommandSyntaxException {
        MinecraftKey minecraftkey = MinecraftKey.read(stringreader);

        return (MobEffectList) IRegistry.MOB_EFFECT.getOptional(minecraftkey).orElseThrow(() -> {
            return ArgumentMobEffect.ERROR_UNKNOWN_EFFECT.create(minecraftkey);
        });
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
        return ICompletionProvider.suggestResource((Iterable) IRegistry.MOB_EFFECT.keySet(), suggestionsbuilder);
    }

    public Collection<String> getExamples() {
        return ArgumentMobEffect.EXAMPLES;
    }
}
