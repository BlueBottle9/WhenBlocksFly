package bluescreen9.minecraft.bukkit.errorlogin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import bluescreen9.minecraft.bukkit.langutil.Lang;

public class Main extends JavaPlugin{
					protected static Plugin ErrorLogin;
					protected static Lang Language;
					private static Thread DataUpdater;
					private static boolean updating = true;
					
					@SuppressWarnings("deprecation")
					@Override
					public void onEnable() {
							ErrorLogin = Main.getPlugin(Main.class);
							saveDefaultConfig();
							reloadConfig();
							Language = new Lang(ErrorLogin);
							Language.copyDeafultLangFile();
							Language.loadLanguages();
							Language.setDeafultLang(getConfig().getString("default-language"));
							
							if (getConfig().getBoolean("player-kicks.server-reload")){
								for (Player p:getServer().getOnlinePlayers()) {
									p.kickPlayer(MessageUtil.dispose(Language.getLang(p, "prompt.server.reload"), p));
								}
							}
							
							if (getConfig().getBoolean("login-point.enable")) {
								Data.loginPoint = new Location(getServer().getWorld(getConfig().getString("login-point.point.world")), getConfig().getDouble("login-point.point.X"), getConfig().getDouble("login-point.point.Y"), getConfig().getDouble("login-point.point.Z"));
							}
							
							getCommand("login").setExecutor(new LoginCommand());
							getCommand("login").setTabCompleter(new LoginCommand());
							getCommand("register").setExecutor(new RegisterCommand());
							getCommand("register").setTabCompleter(new RegisterCommand());
							getCommand("resetpassword").setExecutor(new ResetPasswordCommand());
							getCommand("resetpassword").setTabCompleter(new ResetPasswordCommand());
							
							getServer().getPluginManager().registerEvents(new CommonEventsListener(), ErrorLogin);
							getServer().getPluginManager().registerEvents(new PlayerLimiter(), ErrorLogin);
							
							Data.loadData();
							
							DataUpdater = new Thread() {
								@Override
								public void run() {
									while(updating) {
										try {
											sleep(500L);
										} catch (Exception e) {
											e.printStackTrace();
										}
										Data.updateData();
									}
								}
							};
							PlayerHider.Hider = new PlayerHider();
							PlayerHider.Hider.runTaskTimer(ErrorLogin, 10L, 10L);
							DataUpdater.start();
					}
					
					@Override
					public void onDisable() {
						PlayerHider.Hider.cancel();	
						PlayerHider.Hider = null;
							updating = false;
							DataUpdater = null;
							Data.saveData();
					}
}
