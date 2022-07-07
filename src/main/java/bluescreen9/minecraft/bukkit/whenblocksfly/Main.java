package bluescreen9.minecraft.bukkit.whenblocksfly;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public static ArrayList<Material> ImperviousBlocks = new ArrayList<Material>();
	protected static Plugin WhenBlocksFly;
					@Override
					public void onEnable() {
						WhenBlocksFly = Main.getPlugin(Main.class);
						getServer().getPluginManager().registerEvents(new ExplosionListener(), WhenBlocksFly);
						saveDefaultConfig();
						reloadConfig();
						ImperviousBlocks.add(Material.AIR);
						for (String str:getConfig().getStringList("impervious-blocks")) {
								ImperviousBlocks.add(Material.valueOf(str.toUpperCase()));
						}
					}
}
