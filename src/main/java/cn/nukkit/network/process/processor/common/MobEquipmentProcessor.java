package cn.nukkit.network.process.processor.common;

import cn.nukkit.Player;
import cn.nukkit.PlayerHandle;
import cn.nukkit.Server;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.network.process.DataPacketProcessor;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

/**
 * @author LT_Name
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MobEquipmentProcessor extends DataPacketProcessor<MobEquipmentPacket> {

    public static final MobEquipmentProcessor INSTANCE = new MobEquipmentProcessor();
    private static final int MAX_FAILED = 15;
    @Override
    public void handle(@NotNull PlayerHandle playerHandle, @NotNull MobEquipmentPacket pk) {
        Player player = playerHandle.player;
        
        if (!player.spawned || !player.isAlive()) {
            return;
        }

        Inventory inv = player.getWindowById(pk.windowId);

        if (inv == null) {
            player.getServer().getLogger().debug(player.getName() + " has no open container with window ID " + pk.windowId);
            playerHandle.setFailedMobEquipmentPacket(playerHandle.getFailedMobEquipmentPacket() + 1);
            if (playerHandle.getFailedMobEquipmentPacket() > MAX_FAILED) {
                log.warn("{} Too many failed MobEquipmentPacket", player.getName());
                //player.close("", "Too many failed packets");
                player.sendMessage("You are kicked from the server because of Too many failed packets");
                Server.getInstance().dispatchCommand(player,"lobby");
            }
            return;
        }

        Item item = inv.getItem(pk.hotbarSlot);

        if (!item.equals(pk.item)) {
            player.getServer().getLogger().debug(player.getName() + " tried to equip " + pk.item + " but have " + item + " in target slot");
            playerHandle.setFailedMobEquipmentPacket(playerHandle.getFailedMobEquipmentPacket() + 1);
            if (playerHandle.getFailedMobEquipmentPacket() > MAX_FAILED) {
                log.warn("{} Too many failed MobEquipmentPacket", player.getName());
                //player.close("", "Too many failed packets");
                player.sendMessage("You are kicked from the server because of Too many failed packets");
                Server.getInstance().dispatchCommand(player,"lobby");
            }
            inv.sendContents(player);
            return;
        }

        if (inv instanceof PlayerInventory) {
            ((PlayerInventory) inv).equipItem(pk.hotbarSlot);
        }

        player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, false);
    }

    @Override
    public int getPacketId() {
        return ProtocolInfo.toNewProtocolID(ProtocolInfo.MOB_EQUIPMENT_PACKET);
    }

    @Override
    public Class<? extends DataPacket> getPacketClass() {
        return MobEquipmentPacket.class;
    }

    @Override
    public boolean isSupported(int protocol) {
        return protocol >= ProtocolInfo.v1_1_0;
    }
}
