package bluescreen9.minecraft.bukkit.errorlogin;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CommonEventsListener implements Listener{
						@EventHandler
						public void onPlayerJoin(PlayerJoinEvent event) {
						
							new BukkitRunnable() {
								
								@Override
								public void run() {
									Player player = event.getPlayer();
									player.setAllowFlight(true);
										PlayerHider.hidePlayer(player);
										
										if (Main.ErrorLogin.getConfig().getBoolean("auto-login.enable")) {
											if (Data.isRegistered(player)) {
												if (Main.ErrorLogin.getConfig().getLong("auto-login.allow-time(min)") * 60000 > (new Date().getTime() - Data.getLoginDate(player).getTime())) {
													if (Main.ErrorLogin.getConfig().getBoolean("login-point.enable") && (!Main.ErrorLogin.getConfig().getBoolean("do-not-tp-to-loginpoint"))) {
														Data.OriginalLocs.put(player.getUniqueId().toString(), player.getLocation());
														player.teleport(Data.loginPoint);
													}
													new BukkitRunnable() {
														@Override
														public void run() {
															Data.autoLogin(player);
														}
													}.runTaskLater(Main.ErrorLogin, 10L);
													return;
												}
											}
										}
										
										if (Main.ErrorLogin.getConfig().getBoolean("login-point.enable")) {
											Data.OriginalLocs.put(player.getUniqueId().toString(), player.getLocation());
											player.teleport(Data.loginPoint);
										}
										
										if (!Data.isRegistered(player)) {
											player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.passwordsafety"), player));
											player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.message"),player));
											if (Main.ErrorLogin.getConfig().getBoolean("prompts.title")) {
												MessageUtil.sendTitle(player, MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.title.maintitle"), player),
														MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.title.subtitle"), player),
														0, Integer.MAX_VALUE, 0);
												new BukkitRunnable() {
													Player p = player;
													@Override
													public void run() {
														if (Data.isRegistered(p)) {
															cancel();
															return;
														}
														player.sendMessage(
																MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.message"), player));
													}
												}.runTaskTimer(Main.ErrorLogin, Main.ErrorLogin.getConfig().getLong("prompts.message.timer-delay(tick)"), Main.ErrorLogin.getConfig().getLong("prompts.message.timer-delay(tick)"));
											}
											return;
										}
										
										if (!Data.isLogined(player)) {
											player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.message"),player));
											if (Main.ErrorLogin.getConfig().getBoolean("prompts.title")) {
												MessageUtil.sendTitle(player, MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.title.maintitle"), player),
														MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.title.subtitle"), player),
														0, Integer.MAX_VALUE, 0);
												new BukkitRunnable() {
													Player p = player;
													@Override
													public void run() {
														if (Data.isLogined(p)) {
															cancel();
															return;
														}
														player.sendMessage(
																MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.message"), player));
													}
												}.runTaskTimer(Main.ErrorLogin, Main.ErrorLogin.getConfig().getLong("prompts.message.timer-delay(tick)"), Main.ErrorLogin.getConfig().getLong("prompts.message.timer-delay(tick)"));
											}
											return;
										}

								}
							}.runTaskLater(Main.ErrorLogin, 20L);
						
						}
						
						@EventHandler
						public void onPlayerQuit(PlayerQuitEvent event) {
								if (Main.ErrorLogin.getConfig().getBoolean("delay-entering-message")) {
									Data.OriginalLocs.remove(event.getPlayer().getUniqueId().toString());
								}
								Data.quit(event.getPlayer());
						}
}
