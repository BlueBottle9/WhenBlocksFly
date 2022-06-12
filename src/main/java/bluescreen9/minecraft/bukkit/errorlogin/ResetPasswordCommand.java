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

public class ResetPasswordCommand implements TabExecutor{

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
				if (!Main.ErrorLogin.getConfig().getBoolean("allow-reset-password")) {
					player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.resetpassword.disabled"), player));
					Tone.no(player);
					return true;
				}
				if (args.length != 2) {
					player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.resetpassword.wronggramar"), player));
					Tone.no(player);
					return true;
				}
				Data.resetPassword(player, args[0], args[1]);
			} catch (Error | Exception e) {
				player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.resetpassword.unknowerror"), player));
				e.printStackTrace();
				Tone.no(player);
			}
		return true;
	}
				
}
