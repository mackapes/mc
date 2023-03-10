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
import net.minecraft.world.item.enchantment.Enchantment;

public class ArgumentEnchantment implements ArgumentType<Enchantment> {

    private static final Collection<String> EXAMPLES = Arrays.asList("unbreaking", "silk_touch");
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENCHANTMENT = new DynamicCommandExceptionType((object) -> {
        return IChatBaseComponent.translatable("enchantment.unknown", object);
    });

    public ArgumentEnchantment() {}

    public static ArgumentEnchantment enchantment() {
        return new ArgumentEnchantment();
    }

    public static Enchantment getEnchantment(CommandContext<CommandListenerWrapper> commandcontext, String s) {
        return (Enchantment) commandcontext.getArgument(s, Enchantment.class);
    }

    public Enchantment parse(StringReader stringreader) throws CommandSyntaxException {
        MinecraftKey minecraftkey = MinecraftKey.read(stringreader);

        return (Enchantment) IRegistry.ENCHANTMENT.getOptional(minecraftkey).orElseThrow(() -> {
            return ArgumentEnchantment.ERROR_UNKNOWN_ENCHANTMENT.create(minecraftkey);
        });
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
        return ICompletionProvider.suggestResource((Iterable) IRegistry.ENCHANTMENT.keySet(), suggestionsbuilder);
    }

    public Collection<String> getExamples() {
        return ArgumentEnchantment.EXAMPLES;
    }
}
