package com.github.tanokun.tanorpg.game.entity.boss.inv;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.boss.BossActiveEntity;
import com.github.tanokun.tanorpg.game.entity.boss.BossEntity;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.MobsUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CheckEnterMenu implements InventoryProvider {
    private String id;

    public CheckEnterMenu(String id) {
        this.id = id;
    }

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(false)
                .provider(this)
                .size(3, 9)
                .title("§cボス部屋に行きますか?")
                .id("checkEnterMenu")
                .update(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        BossEntity bossEntity = TanoRPG.getPlugin().getEntityManager().getBossManager().getBoss(id);

        contents.set(1, 2, ClickableItem.of(ItemUtilsKt.createItem(Material.GREEN_WOOL, "§bボスと戦う", 1, true), e -> {
            TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
            contents.inventory().close(player);

            player.setMetadata("boss", new FixedMetadataValue(TanoRPG.getPlugin(), true));
            player.teleport(bossEntity.getTeleport());

            BossActiveEntity bossActiveEntity = (BossActiveEntity) MobsUtilsKt.getActiveEntity(bossEntity.spawn());
            bossActiveEntity.getBossEntity().getBossActiveEntity().getJoin().add(player);
            TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId()).setBossEntity(bossEntity);
        }));

        contents.set(1, 6, ClickableItem.of(ItemUtilsKt.createItem(Material.RED_WOOL, "§bまた今度にする", 1, true), e -> {
            TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
            contents.inventory().close(player);
        }));
    }
}
