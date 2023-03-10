package net.minecraft.network.protocol.game;

import net.minecraft.core.IRegistry;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.Item;

public class PacketPlayOutSetCooldown implements Packet<PacketListenerPlayOut> {

    private final Item item;
    private final int duration;

    public PacketPlayOutSetCooldown(Item item, int i) {
        this.item = item;
        this.duration = i;
    }

    public PacketPlayOutSetCooldown(PacketDataSerializer packetdataserializer) {
        this.item = (Item) packetdataserializer.readById(IRegistry.ITEM);
        this.duration = packetdataserializer.readVarInt();
    }

    @Override
    public void write(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeId(IRegistry.ITEM, this.item);
        packetdataserializer.writeVarInt(this.duration);
    }

    public void handle(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.handleItemCooldown(this);
    }

    public Item getItem() {
        return this.item;
    }

    public int getDuration() {
        return this.duration;
    }
}
