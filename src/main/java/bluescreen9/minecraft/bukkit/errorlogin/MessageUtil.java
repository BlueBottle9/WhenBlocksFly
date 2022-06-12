package bluescreen9.minecraft.bukkit.errorlogin;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageUtil {
				public static String dispose(String data) {
					return ChatColor.translateAlternateColorCodes('&', data);
				}
				
				public static String dispose(String data,Player player) {
					Date date = Data.getLoginDate(player);
					if (date != null) {
						Date now = new Date();
						long time = now.getTime() - date.getTime();
						long d = time / 86400000;
						long h = time / 3600000;
						long min = time / 60000;
						long s = time / 1000;
						String str = dispose(data.replaceAll("<%playername>", player.getName()).replaceAll("<%logindate>", 
								new SimpleDateFormat(Main.ErrorLogin.getConfig().getString("date-format")).format(date)).replaceAll("<%login-d>",
										d + "").replaceAll("<%login-h>", h + "").replaceAll("<%login-min>", min + "").replaceAll("<%login-s>", s + ""));
						return str;
					}
					return dispose(data.replaceAll("<%playername>", player.getName()));
				}
				
				@SuppressWarnings("deprecation")
				public static void sendTitle(Player player,String maintitle,String subtitle,int fadein,int stay,int fadeout) {
							player.sendTitle(maintitle, subtitle, fadein, stay, fadeout);
				}
}
