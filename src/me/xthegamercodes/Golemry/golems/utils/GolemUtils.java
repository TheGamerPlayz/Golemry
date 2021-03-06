package me.xthegamercodes.Golemry.golems.utils;

import java.lang.reflect.Constructor;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemType;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.World;

public class GolemUtils {

	public static final String[] HELP = {
			"&e- - - - - - - - - - - - - - -",
			"&6Summon Golem:",
			"&5 - Breeder Golem &r: &5Red Rose/Poppy",
			"&c - Guard Golem &r: &cDiamond Sword",
			"&2 - Harvester Golem &r: &2Diamond Hoe",
			"&8 - Miner Golem &r: &8Diamond Pickaxe",
			"&0 - Smith Golem &r: &0Charcoal",
			"&e- - - - - - - - - - - - - - -"
			};

	public static net.minecraft.server.v1_8_R3.Entity getNMSEntity(Entity entity) {
		if(entity == null) {
			return null;
		}

		return ((CraftEntity) entity).getHandle();
	}

	public static Location toLocation(org.bukkit.World world, BlockPosition blockposition) {
		Location location = new Location(world, blockposition.getX(), blockposition.getY(), blockposition.getZ());
//		if(location.getX() < 0) {
//			location.add(1, 0, 0);
//		}
//		if(location.getZ() < 0) {
//			location.add(0, 0, 1);
//		}
		return location;
	}

	public static Vec3D randomPosition(EntityGolem paramEntity, int wanderRange) {
		Vec3D paramVec3D = null;

		Random localRandom = paramEntity.bc();
		int i = 0;
		int j = 0;
		int k = 0;
		int m = 0;
		float f1 = -99999.0F;
		int n;
		if(paramEntity.ck()) {
			double d1 = paramEntity.ch().c(MathHelper.floor(paramEntity.locX), MathHelper.floor(paramEntity.locY), MathHelper.floor(paramEntity.locZ)) + 4.0D;
			double d2 = paramEntity.ci() + wanderRange;
			n = d1 < d2 * d2 ? 1 : 0;
		}
		else {
			n = 0;
		}
		for(int i1 = 0; i1 < 10; i1++) {
			int i2 = localRandom.nextInt(2 * wanderRange) - wanderRange;
			int i3 = localRandom.nextInt(2 * wanderRange) - wanderRange;
			int i4 = localRandom.nextInt(2 * wanderRange) - wanderRange;
			if((paramVec3D == null) || (i2 * paramVec3D.a + i4 * paramVec3D.c >= 0.0D)) {
				if((paramEntity.ck()) && (wanderRange > 1)) {
					BlockPosition center = new BlockPosition(paramEntity);
					if(paramEntity.locX > center.getX()) {
						i2 -= localRandom.nextInt(wanderRange / 2);
					}
					else {
						i2 += localRandom.nextInt(wanderRange / 2);
					}
					if(paramEntity.locZ > center.getZ()) {
						i4 -= localRandom.nextInt(wanderRange / 2);
					}
					else {
						i4 += localRandom.nextInt(wanderRange / 2);
					}
				}
				i2 += MathHelper.floor(paramEntity.locX);
				i3 += MathHelper.floor(paramEntity.locY);
				i4 += MathHelper.floor(paramEntity.locZ);

				BlockPosition localBlockPosition = new BlockPosition(i2, i3, i4);
				if((n == 0) || (paramEntity.e(localBlockPosition))) {
					float f2 = paramEntity.a(localBlockPosition);
					if(f2 > f1) {
						f1 = f2;
						j = i2;
						k = i3;
						m = i4;
						i = 1;
					}
				}
			}
		}
		if(i != 0) {
			return new Vec3D(j, k, m);
		}
		return null;
	}

	public static World getWorld(org.bukkit.World world) {
		return ((CraftWorld) world).getHandle();
	}

	public static double getScore(net.minecraft.server.v1_8_R3.ItemStack equipment) {
		if(equipment == null) {
			return 2.0;
		}
		else if(equipment.getItem() == Items.DIAMOND_PICKAXE) {
			return 0.25;
		}
		else if(equipment.getItem() == Items.GOLDEN_PICKAXE) {
			return 0.30;
		}
		else if(equipment.getItem() == Items.IRON_PICKAXE) {
			return 0.50;
		}
		else if(equipment.getItem() == Items.STONE_PICKAXE) {
			return 0.75;
		}
		else if(equipment.getItem() == Items.WOODEN_PICKAXE) {
			return 0.90;
		}

		return 0;
	}

	public static GolemType getGolemType(Material material, short damage) {
		for(GolemType type : GolemType.values()) {
			if(type.getSummoningItem() == material) {
				if(type.getDamage() == damage) {
					return type;
				}
			}
		}

		return null;
	}

	public static EntityGolem createGolem(World world, GolemType type) {

		try {
			Constructor<?> ctor = type.getGolemClass().getConstructor(World.class);
			return (EntityGolem) ctor.newInstance(world);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("-------------------------");
			System.out.println("Please report this error to the developer of this plugin.");
			System.out.println("-------------------------");
		}
		
		return null;
	}

	public static String getGolemName(EntityZombie entity) {
		NBTTagCompound nbt = new NBTTagCompound();
		entity.b(nbt);
		return nbt.getString("GolemryType");
	}

	@SuppressWarnings("deprecation")
	public static ItemStack[] buildItem(String string) {
		if(string.length() == 0) {
			return new ItemStack[9];
		}
		String[] items = string.split(" ");
		ItemStack[] itemstack = new ItemStack[9];

		int n = 0;
		for(String item : items) {
			String[] data = item.split("-");

			int itemID = Integer.valueOf(data[0]);
			int amount = Integer.valueOf(data[1]);
			short damage = (short) ((int) Integer.valueOf(data[2]));
			itemstack[n++] = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(itemID, amount, damage));
		}

		return itemstack;
	}

	public static boolean canCreate(Player player, GolemType type) {
		String golemname = type.toString().toLowerCase();
		
		return player.hasPermission("Golemry.create." + golemname);
	}

}
