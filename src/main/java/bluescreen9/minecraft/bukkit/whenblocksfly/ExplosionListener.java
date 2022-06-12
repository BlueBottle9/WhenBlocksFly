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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

public class ExplosionListener implements Listener{
					@EventHandler
					public static void onExplosion(ExplosionPrimeEvent event) {
						Location central = event.getEntity().getLocation();
						List<Block> blocks = new ArrayList<Block>();
						int yield = (int) event.getRadius();
						for (int x =-yield;x < yield;x++) {
							for (int y =-yield;y < yield;y++) {
								for (int z =-yield;z < yield;z++) {
									Location loc = central.clone().add(x, y, z);
									if (loc.distance(central) > yield) {
										continue;
									}
									Block block = loc.getBlock();
									Material type = block.getType();
									if (type.equals(Material.AIR) || type.equals(Material.CHEST) || type.equals(Material.BEDROCK) || type.equals(Material.TNT) || type.equals(Material.BARRIER) || type.equals(Material.END_PORTAL) || type.equals(Material.END_PORTAL_FRAME)) {
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
					
					@EventHandler
					public void onEntityDamage(EntityDamageEvent event) {
						if (event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
							event.setCancelled(true);
						}
					}
}
