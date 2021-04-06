package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemMagicWeapon;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemWeapon;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.item.itemtype.base.ItemJob;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.task.MagicTask;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.tanokun.tanorpg.TanoRPG.PX;

public class LeftClickEventListener implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            GamePlayer gamePlayer = GamePlayerManager.getPlayer(e.getPlayer().getUniqueId());
            if (!ItemManager.isExists(ItemManager.getID(e.getPlayer().getEquipment().getItemInMainHand())))
                return;
            Item item = ItemManager.getItem(gamePlayer.getPlayer().getEquipment().getItemInMainHand());
            if (!gamePlayer.isProper(e.getPlayer().getEquipment().getItemInMainHand())) {
                if (item instanceof ItemWeapon || item instanceof ItemMagicWeapon) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(PX + "§c対応していない武器です");
                    return;
                }
                return;
            }
            if (!(item instanceof ItemMagicWeapon)) return;
            if (gamePlayer.getPlayer().hasMetadata("cooltime_magic")) {
                e.setCancelled(true);
                return;
            }
            gamePlayer.getPlayer().setMetadata("cooltime_magic", new FixedMetadataValue(TanoRPG.getPlugin(), true));
            final int[] cool = {(int) Math.round(((ItemJob)ItemManager.getItem(
                    ItemManager.getID(e.getPlayer().getEquipment().getItemInMainHand()))).getCoolTime()
                    * ((100 - gamePlayer.getStatus(StatusType.CT_ATK).getLevel()) / 100))};

            new BukkitRunnable() {
                @Override
                public void run() {
                    cool[0] -= 1;
                    if (cool[0] <= 0) {
                        gamePlayer.getPlayer().removeMetadata("cooltime_magic", TanoRPG.getPlugin());
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 0, 1L);
            new MagicTask(GamePlayerManager.getPlayer(e.getPlayer().getUniqueId())).runTaskTimerAsynchronously(TanoRPG.getPlugin(), 2, 1);
        }
    }

    @EventHandler
    public void onClickSkill(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getItem().getItemMeta().getDisplayName() == null) return;
            if (e.getItem().getItemMeta().getDisplayName().contains("§bスキル習得書: ")){
                String[] skillName = e.getItem().getItemMeta().getDisplayName().split("§6");
                if (!SkillManager.isExists(skillName[1])) return;
                Skill skill = SkillManager.getSkillName(skillName[1]);
                GamePlayer player = GamePlayerManager.getPlayer(e.getPlayer().getUniqueId());
                if (!skill.getJobs().contains(player.getJob())){
                    player.getPlayer().sendMessage(PX + "§c職業が違うため習得できません");
                    return;
                }
                if (player.getLEVEL().getLEVEL() < skill.getLvl()){
                    player.getPlayer().sendMessage(PX + "§cレベルが足りないため習得できません");
                    return;
                }
                TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);

                player.addSkill(skillName[1]); e.getPlayer().sendMessage(PX + "スキル「" + skillName[1] + "」を習得しました");
                ItemStack item = e.getItem();
                item.setAmount(item.getAmount() - 1);
            }
        }
    }
}
