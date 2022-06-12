package bluescreen9.minecraft.bukkit.whenblocksfly;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	protected static Plugin WhenBlocksFly;
					@Override
					public void onEnable() {
						WhenBlocksFly = Main.getPlugin(Main.class);
						getServer().getPluginManager().registerEvents(new ExplosionListener(), WhenBlocksFly);
					}
}
