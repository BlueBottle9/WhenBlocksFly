package bluescreen9.minecraft.bukkit.whenblocksfly;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class ExplosionListener implements Listener{
					@EventHandler
					public void onExplosionEntity(EntityExplodeEvent event) {
						List<Block> blocks = new ArrayList<Block>();
						blocks.addAll(event.blockList());
							if (blocks.isEmpty()) {
							return;
							}
							int basicPower = (int)(Math.pow(( ((double)blocks.size()) / ((3D/4D) * Math.PI)),(1D/3D)));
							if (basicPower < 0) {
								basicPower = 0;
							}
							int power = basicPower + Main.WhenBlocksFly.getConfig().getInt("explosion-power-off");
							explodeBlocks(event.getLocation(), power);
							remove(event.blockList());
					}
					
					@EventHandler
					public void onExplosionBlock(BlockExplodeEvent event) {
						List<Block> blocks = new ArrayList<Block>();
						blocks.addAll(event.blockList());
							if (blocks.isEmpty()) {
							return;
							}
							int basicPower = (int)(Math.pow(( ((double)blocks.size()) / ((3D/4D) * Math.PI)),(1D/3D)));
							if (basicPower < 0) {
								basicPower = 0;
							}
							int power = basicPower + Main.WhenBlocksFly.getConfig().getInt("explosion-power-off");
							explodeBlocks(event.getBlock().getLocation(), power);
							remove(event.blockList());
					}
					
					@EventHandler
					public void onEntityDamage(EntityDamageEvent event) {
						if (event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
							event.setCancelled(true);
						}
					}
					
					public static void explodeBlocks(Location central,int radius) {
						List<Block> blocks = new ArrayList<Block>();
						for (int x =-radius;x < radius;x++) {
							for (int y =-radius;y < radius;y++) {
								for (int z =-radius;z < radius;z++) {
									Location loc = central.clone().add(x, y, z);
									if (loc.distance(central) > radius) {
										continue;
									}
									Block block = loc.getBlock();
									Material type = block.getType();
									if (Main.ImperviousBlocks.contains(type)) {
										continue;
									}
									blocks.add(block);
								}
							}
						}
						if (blocks!= null) {
							if (!blocks.isEmpty()) {
								World world = central.getWorld(); 
								for (Block block:blocks) {
									FallingBlock fallingBlock = world.spawnFallingBlock(block.getLocation(), block.getBlockData());
									fallingBlock.setDropItem(true);
									Vector vector = block.getLocation().clone().subtract(central).toVector().normalize();
									vector.setX(vector.getX()+(Math.random()/2-0.25));
									vector.setY(vector.getY()+(Math.random()/2-0.25));
									vector.setZ(vector.getZ()+(Math.random()/2-0.25));
									double factor=Math.min(1,1/block.getLocation().distance(central));
									fallingBlock.setHurtEntities(true);
									
									vector = vector.multiply(factor);
									fallingBlock.setVelocity(vector);
									block.setType(Material.AIR);
								}
							}
						}
					}
					
					public static void remove(List<Block> blocks) {
							for (Block block:blocks) {
								if (Main.ImperviousBlocks.contains(block.getType())) {
									continue;
								} 
								blocks.remove(block);
							}
					}
}
