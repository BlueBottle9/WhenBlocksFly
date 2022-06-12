package bluescreen9.minecraft.bukkit.errorlogin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterCommand implements TabExecutor{

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		return new ArrayList<String>();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command");
			return true;
		}
		Player player = (Player) sender;
		try {
			if (args.length != 2) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.wronggrammar"), player));
				Tone.no(player);
				return true;
			}
			if (Data.isLogined(player)) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.alreadylogined"), player));
				Tone.no(player);
				return true;
			}
			
			if (Data.isRegistered(player)) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.alreadyregistered"), player));
				Tone.no(player);
				return true;
			}
			
			String password = args[0];
			String confirmPassowrd = args[1];
			
			if (!password.equals(confirmPassowrd)) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.inconsistencypassword"), player));
				Tone.no(player);
				return true;
			}
			
			if (!Data.isSecurePassword(password, player)) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.insecurepassword"), player));
				Tone.no(player);
				return true;
			}
			
			Data.register(player, password);
		} catch (Error | Exception e) {
			player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.unknowerror"), player));
			e.printStackTrace();
			Tone.no(player);
		}
		
		return true;
	}

}
