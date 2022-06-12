package bluescreen9.minecraft.bukkit.errorlogin;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Tone {
				public static void yes(Player player) {
					if (Main.ErrorLogin.getConfig().getBoolean("tone")) {
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1000.0F, 1.0F);
					}
				}
				
				public static void no(Player player) {
					if (Main.ErrorLogin.getConfig().getBoolean("tone")) {
						player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1000.0F, 1.0F);
					}
				}
}
