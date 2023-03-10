package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.util.HttpUtilities;
import net.minecraft.world.level.EnumGamemode;

public class CommandPublish {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(IChatBaseComponent.translatable("commands.publish.failed"));
    private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED = new DynamicCommandExceptionType((object) -> {
        return IChatBaseComponent.translatable("commands.publish.alreadyPublished", object);
    });

    public CommandPublish() {}

    public static void register(CommandDispatcher<CommandListenerWrapper> commanddispatcher) {
        commanddispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.commands.CommandDispatcher.literal("publish").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(4);
        })).executes((commandcontext) -> {
            return publish((CommandListenerWrapper) commandcontext.getSource(), HttpUtilities.getAvailablePort());
        })).then(net.minecraft.commands.CommandDispatcher.argument("port", IntegerArgumentType.integer(0, 65535)).executes((commandcontext) -> {
            return publish((CommandListenerWrapper) commandcontext.getSource(), IntegerArgumentType.getInteger(commandcontext, "port"));
        })));
    }

    private static int publish(CommandListenerWrapper commandlistenerwrapper, int i) throws CommandSyntaxException {
        if (commandlistenerwrapper.getServer().isPublished()) {
            throw CommandPublish.ERROR_ALREADY_PUBLISHED.create(commandlistenerwrapper.getServer().getPort());
        } else if (!commandlistenerwrapper.getServer().publishServer((EnumGamemode) null, false, i)) {
            throw CommandPublish.ERROR_FAILED.create();
        } else {
            commandlistenerwrapper.sendSuccess(IChatBaseComponent.translatable("commands.publish.success", i), true);
            return i;
        }
    }
}
