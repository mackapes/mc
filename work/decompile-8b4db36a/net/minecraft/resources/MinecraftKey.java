package net.minecraft.resources;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.ResourceKeyInvalidException;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.util.ChatDeserializer;
import org.apache.commons.lang3.StringUtils;

public class MinecraftKey implements Comparable<MinecraftKey> {

    public static final Codec<MinecraftKey> CODEC = Codec.STRING.comapFlatMap(MinecraftKey::read, MinecraftKey::toString).stable();
    private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(IChatBaseComponent.translatable("argument.id.invalid"));
    public static final char NAMESPACE_SEPARATOR = ':';
    public static final String DEFAULT_NAMESPACE = "minecraft";
    public static final String REALMS_NAMESPACE = "realms";
    protected final String namespace;
    protected final String path;

    protected MinecraftKey(String[] astring) {
        this.namespace = StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0];
        this.path = astring[1];
        if (!isValidNamespace(this.namespace)) {
            throw new ResourceKeyInvalidException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ":" + this.path);
        } else if (!isValidPath(this.path)) {
            throw new ResourceKeyInvalidException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ":" + this.path);
        }
    }

    public MinecraftKey(String s) {
        this(decompose(s, ':'));
    }

    public MinecraftKey(String s, String s1) {
        this(new String[]{s, s1});
    }

    public static MinecraftKey of(String s, char c0) {
        return new MinecraftKey(decompose(s, c0));
    }

    @Nullable
    public static MinecraftKey tryParse(String s) {
        try {
            return new MinecraftKey(s);
        } catch (ResourceKeyInvalidException resourcekeyinvalidexception) {
            return null;
        }
    }

    @Nullable
    public static MinecraftKey tryBuild(String s, String s1) {
        try {
            return new MinecraftKey(s, s1);
        } catch (ResourceKeyInvalidException resourcekeyinvalidexception) {
            return null;
        }
    }

    protected static String[] decompose(String s, char c0) {
        String[] astring = new String[]{"minecraft", s};
        int i = s.indexOf(c0);

        if (i >= 0) {
            astring[1] = s.substring(i + 1, s.length());
            if (i >= 1) {
                astring[0] = s.substring(0, i);
            }
        }

        return astring;
    }

    public static DataResult<MinecraftKey> read(String s) {
        try {
            return DataResult.success(new MinecraftKey(s));
        } catch (ResourceKeyInvalidException resourcekeyinvalidexception) {
            return DataResult.error("Not a valid resource location: " + s + " " + resourcekeyinvalidexception.getMessage());
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof MinecraftKey)) {
            return false;
        } else {
            MinecraftKey minecraftkey = (MinecraftKey) object;

            return this.namespace.equals(minecraftkey.namespace) && this.path.equals(minecraftkey.path);
        }
    }

    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public int compareTo(MinecraftKey minecraftkey) {
        int i = this.path.compareTo(minecraftkey.path);

        if (i == 0) {
            i = this.namespace.compareTo(minecraftkey.namespace);
        }

        return i;
    }

    public String toDebugFileName() {
        return this.toString().replace('/', '_').replace(':', '_');
    }

    public String toLanguageKey() {
        return this.namespace + "." + this.path;
    }

    public String toShortLanguageKey() {
        return this.namespace.equals("minecraft") ? this.path : this.toLanguageKey();
    }

    public String toLanguageKey(String s) {
        return s + "." + this.toLanguageKey();
    }

    public static MinecraftKey read(StringReader stringreader) throws CommandSyntaxException {
        int i = stringreader.getCursor();

        while (stringreader.canRead() && isAllowedInResourceLocation(stringreader.peek())) {
            stringreader.skip();
        }

        String s = stringreader.getString().substring(i, stringreader.getCursor());

        try {
            return new MinecraftKey(s);
        } catch (ResourceKeyInvalidException resourcekeyinvalidexception) {
            stringreader.setCursor(i);
            throw MinecraftKey.ERROR_INVALID.createWithContext(stringreader);
        }
    }

    public static boolean isAllowedInResourceLocation(char c0) {
        return c0 >= '0' && c0 <= '9' || c0 >= 'a' && c0 <= 'z' || c0 == '_' || c0 == ':' || c0 == '/' || c0 == '.' || c0 == '-';
    }

    private static boolean isValidPath(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (!validPathChar(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidNamespace(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (!validNamespaceChar(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean validPathChar(char c0) {
        return c0 == '_' || c0 == '-' || c0 >= 'a' && c0 <= 'z' || c0 >= '0' && c0 <= '9' || c0 == '/' || c0 == '.';
    }

    private static boolean validNamespaceChar(char c0) {
        return c0 == '_' || c0 == '-' || c0 >= 'a' && c0 <= 'z' || c0 >= '0' && c0 <= '9' || c0 == '.';
    }

    public static boolean isValidResourceLocation(String s) {
        String[] astring = decompose(s, ':');

        return isValidNamespace(StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0]) && isValidPath(astring[1]);
    }

    public static class a implements JsonDeserializer<MinecraftKey>, JsonSerializer<MinecraftKey> {

        public a() {}

        public MinecraftKey deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return new MinecraftKey(ChatDeserializer.convertToString(jsonelement, "location"));
        }

        public JsonElement serialize(MinecraftKey minecraftkey, Type type, JsonSerializationContext jsonserializationcontext) {
            return new JsonPrimitive(minecraftkey.toString());
        }
    }
}
