package bluescreen9.minecraft.bukkit.errorlogin;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;



@SuppressWarnings("deprecation")
public class PlayerLimiter implements Listener{
				@EventHandler
				public void onPlayerMove(PlayerMoveEvent event) {
					if (!Data.isLogined(event.getPlayer())) {
						event.setCancelled(true);
					}
				}
				
				@EventHandler
				public void onPlayerHurt(EntityDamageEvent event) {
					if (event.getEntity() instanceof Player) {
						if (!Data.isLogined((Player) event.getEntity())) {
							event.setCancelled(true);
						}
					}
				}
				
				@EventHandler
				public void onPlayerInteract(PlayerInteractEvent event) {
					if (!Data.isLogined(event.getPlayer())) {
						event.setCancelled(true);
					}
				}
				
				@EventHandler
				public void onPlayerDropItem(PlayerDropItemEvent event) {
					if (!Data.isLogined(event.getPlayer())) {
						event.setCancelled(true);
					}
				}
				
				@EventHandler
				public void onPlayerChat(AsyncPlayerChatEvent event) {
					Player player = event.getPlayer();
					if (!Data.isLogined(player)) {
						event.setCancelled(true);
						if (Data.isRegistered(player)) {
							Tone.no(player);
							player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.warning"), player));
						}else {
							Tone.no(player);
							player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.warning"), player));
						}
					}
				}
				
				@EventHandler
				public void onPlayerRunCommand(PlayerCommandPreprocessEvent event) {
						Player player = event.getPlayer();
						if (!Data.isLogined(player)) {
							if (!(event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register"))) {
								event.setCancelled(true);
									if (Data.isRegistered(player)) {
										Tone.no(player);
										player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.warning"), player));
									}else {
										Tone.no(player);
										player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.warning"), player));
									}
							}
						}
				}
				
				@EventHandler
				public void onMonsterTarget(EntityTargetLivingEntityEvent event) {
						if (event.getTarget() instanceof Player) {
							Player player = (Player) event.getTarget();
							if (!Data.isLogined(player)) {
								event.setCancelled(true);
							}
						}
				}
}
