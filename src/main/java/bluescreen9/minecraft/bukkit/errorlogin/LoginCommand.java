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

public class LoginCommand implements TabExecutor{

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,@NotNull String label, @NotNull String[] args) {
		return new ArrayList<String>();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,@NotNull String[] args) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "only players can execute this command");
			}
			Player player = (Player) sender;
			try {
				if (args.length != 1) {
					player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.wronggrammar"), player));
					Tone.no(player);
					return true;
				}
				Data.login(player, args[0]);
			} catch (Error | Exception e) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.unkownerror"), player));
				e.printStackTrace();
				Tone.no(player);
			}
			
		return true;
	}
					
}
