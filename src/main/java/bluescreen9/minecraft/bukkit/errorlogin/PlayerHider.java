package bluescreen9.minecraft.bukkit.errorlogin;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerHider extends BukkitRunnable{
				protected static PlayerHider Hider;
			
				protected static ArrayList<Player> hiders = new ArrayList<Player>();
				
				@Override
				public void run() {
					
					for (Player player:Main.ErrorLogin.getServer().getOnlinePlayers()) {
						for (Player p:hiders) {
							if (hiders.contains(p)) {
								player.hidePlayer(Main.ErrorLogin, p);
							}
						}
				}
					}
				
				public static void showPlayer(Player player) {
					hiders.remove(player);
					for (Player p:Main.ErrorLogin.getServer().getOnlinePlayers()) {
						for (int i = 0;i < 5;i++) {
							p.showPlayer(Main.ErrorLogin, player);
						}
					}
				}
				
				public static void hidePlayer(Player player) {
							hiders.add(player);
				}
}
