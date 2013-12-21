package warlockjk.compressedBuilding.utils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.blocks.Blocks;

public class PlacementUtil {
	
	private boolean sneaking = false;
	private byte[] orientationArr = new byte[6];
	private byte originalCount = 0;
	private byte newCount = 0;
//	private byte radius = 1;
//	private byte upCount = 0;
//	private byte downCount = 0;
	
	public PlacementUtil() {
	}
	
	/**
	 * @param id The id of the block to be placed
	 * @param sizeFactor1 Modifies the size of what's placed, 1 would keep it at the default 3 TODO Implement this
	 * @param sizeFactor2 Modifies the size of what's placed, 1 would keep it at the default 3 TODO Implement this
	 */
	public void placeBlocks(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, int id, double sizeFactor1, double sizeFactor2) {
		if (!world.isRemote) {
			sneaking = player.isSneaking();
			orientationArr[0] = -1;//Starting of i
			orientationArr[1] = 2;//One greater than the ending of i
			orientationArr[2] = -1;//Starting of k
			orientationArr[3] = 2;//One greater than the ending of k
			orientationArr[4] = 1;//The offset of various directions depending on where it is used below
			
			/**
			 * Used to determine which direction to orient it when the player is sneaking
			 * 1 means East-West
			 * 2 means North-South
			 */
			orientationArr[5] = 1;
			
			sideBasedLogic(side);//Manipulates where the loops below start and end for proper orientation based on the side
			orientationLogic(world, x, y, z, side);//Changes the east-west vs north-south orientation
			if(!world.getBlockMaterial(x, y, z).isSolid()) {
				//nonSolidLogic(side);//Makes things work as you would think they would with grass and such
				orientationArr[0] = -1;
				orientationArr[1] = 2;
				orientationArr[2] = -1;
				orientationArr[3] = 2;
				orientationArr[4] = 0;
			}
/**			if(sneaking) {
				verticalDisplacementLogic(world, x, y, z);
			}*/
			for (int i = orientationArr[0]; i < orientationArr[1]; i++) {
				for(int j = orientationArr[2]; j < orientationArr[3]; j++) {
					if (sneaking) {
						if (orientationArr[5] == 1) {
							if (world.isAirBlock(x + i, y + j, z + orientationArr[4]) || !world.getBlockMaterial(x + i, y + j, z + orientationArr[4]).isSolid() || world.getBlockId(x + i, y + j, z + orientationArr[4]) == Blocks.squareCobble.blockID) {
								world.setBlock(x + i, y + j, z + orientationArr[4], id);
							}else{
								spawnCompensation(world, id, 1, x, y, z);
							}
						}else{
							if (world.isAirBlock(x + orientationArr[4], y + j, z + i) || !world.getBlockMaterial(x + orientationArr[4], y + j, z + i).isSolid() || world.getBlockId(x + orientationArr[4], y + j, z + i) == Blocks.squareCobble.blockID) {
								world.setBlock(x + orientationArr[4], y + j, z + i, id);
							}else{
								spawnCompensation(world, id, 1, x, y, z);
							}
						}
					}else{
						if (world.isAirBlock(x + i, y + orientationArr[4], z + j) || !world.getBlockMaterial(x + i, y + orientationArr[4], z + j).isSolid() || world.getBlockId(x + i, y + orientationArr[4], z + j) == Blocks.squareCobble.blockID) {
							world.setBlock(x + i, y + orientationArr[4], z + j, id);
						}else{
							spawnCompensation(world, id, 1, x, y, z);
						}
					}
				}
			}
		}
	}
	
	private void sideBasedLogic(int side) {
		if (side == 1) {//Top
			if (sneaking) {
				orientationArr[2] = 1;
				orientationArr[3] = 4;
				orientationArr[4] = 0;
				orientationArr[5] = 1;//This is the default and shouldn't need to be changed, but it's good to be safe if it doesn't make any significant impact on resources
			}else{
				orientationArr[4] = 1;
			}
		}else if (side == 0) {//Bottom
			if (sneaking) {
				orientationArr[2] = -3;
				orientationArr[3] = 0;
				orientationArr[4] = 0;
				orientationArr[5] = 1;
			}else{
				orientationArr[4] = -1;
			}
		}else if (side == 5) {//The more positive x side
			if (sneaking) {
				orientationArr[0] = 1;
				orientationArr[1] = 4;
				orientationArr[4] = 0;
				orientationArr[5] = 1;
			}else{
				orientationArr[0] = 1;
				orientationArr[1] = 4;
				orientationArr[4] = 0;
			}
		}else if (side == 2) {//The more negative z side
			if (sneaking) {
				orientationArr[0] = -3;
				orientationArr[1] = 0;
				orientationArr[4] = 0;
				orientationArr[5] = 2;
			}else{
				orientationArr[2] = -3;
				orientationArr[3] = 0;
				orientationArr[4] = 0;
			}
		}else if (side == 3) {//The more positive z side
			if (sneaking) {
				orientationArr[0] = 1;
				orientationArr[1] = 4;
				orientationArr[4] = 0;
				orientationArr[5] = 2;
			}else{
				orientationArr[2] = 1;
				orientationArr[3] = 4;
				orientationArr[4] = 0;
			}
		}else if (side == 4) {//The more negative x side
			if (sneaking) {
				orientationArr[0] = -3;
				orientationArr[1] = 0;
				orientationArr[4] = 0;
				orientationArr[5] = 1;
			}else{
				orientationArr[0] = -3;
				orientationArr[1] = 0;
				orientationArr[4] = 0;
			}
		}
	}
	
	public void spawnCompensation(World world, int id, int count, int x, int y, int z) {
		EntityItem entityItem = new EntityItem(world, (double)x, (double)y, (double)z, new ItemStack(id, count, 0));
		world.spawnEntityInWorld(entityItem);
	}
	
/**	public void addItemsToInventory(EntityPlayer player, int id, int count) {
		if (count > 0) {
			itemSlot = getInventorySlotContainItem(player, id);
			if (itemSlot != -1) {
				if (player.inventory.mainInventory[itemSlot].getMaxStackSize() - player.inventory.mainInventory[itemSlot].stackSize >= count) {
					player.inventory.mainInventory[itemSlot].stackSize += count;
				}else{
					if (!player.inventory.addItemStackToInventory(new ItemStack(id, count - (player.inventory.mainInventory[itemSlot].getMaxStackSize() - player.inventory.mainInventory[itemSlot].stackSize), 0))) {
						//if it doesn't work, then drop the items on the ground by the player
					}
					player.inventory.mainInventory[itemSlot].stackSize += 
							player.inventory.mainInventory[itemSlot].getMaxStackSize() - player.inventory.mainInventory[itemSlot].stackSize;
				}
			}else{
				
			}
		}
	}*/
	
/**	public int getInventorySlotContainItem(EntityPlayer player, int id) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].itemID == id) {
                return i;
            }
        }
        return -1;
	}*/
	
	private void orientationLogic(World world,int x,int y,int z, int side) {
		if (side == 0 || side == 1) {
			originalCount = 0;		
			for (int n = orientationArr[0]; n < orientationArr[1]; n++) {
				if(orientationArr[5] == 1) {
					if(!(world.isAirBlock(x + n, y, z + orientationArr[4]) || !world.getBlockMaterial(x + n, y, z + orientationArr[4]).isSolid())){
						originalCount++;
					}
				}else{
					if(!(world.isAirBlock(x + orientationArr[4], y, z + n) || !world.getBlockMaterial(x + orientationArr[4], y, z + n).isSolid())) {
						originalCount++;
					}
				}
			}
			newCount = 0;
			for (int n = orientationArr[0]; n < orientationArr[1]; n++) {
				if(orientationArr[5] == 1) {//It might look like this is the same as above, but I switched the if statements so it does the opposite
					if(!world.isAirBlock(x + orientationArr[4], y, z + n)) {
						newCount++;
					}
				}else{
					if(!world.isAirBlock(x + n, y, z + orientationArr[4])){
						newCount++;
					}
				}
			}
			if (newCount > originalCount) {
				if (orientationArr[5] == 1) {
					orientationArr[5] = 2;
				}else{
					orientationArr[5] = 1;
				}
			}
		}
	}
	
/**	private void nonSolidLogic(int side) {
			if(side == 5 || side == 3) {
				if (!sneaking && side == 3) {
					orientationArr[2] = 0;
					orientationArr[3] = 3;
				}else{
					orientationArr[0] = 0;
					orientationArr[1] = 3;
				}
			}else if (side == 2 || side == 4) {
				if (!sneaking && side == 2) {
					orientationArr[2] = -2;
					orientationArr[3] = 1;
				}else{
					orientationArr[0] = -2;
					orientationArr[1] = 1;
				}
			}
			orientationArr[4] = 0;
	}*/
	
/**	private void verticalDisplacementLogic(World world,int x, int y, int z) {
		radius = (byte)((Math.abs(orientationArr[1] - orientationArr[0]) - 1) / 2);
		downCount = 0;
		originalCount = 0;
		upCount = 0;
		for (int k = -1; k < 2; k++) {
			for (int n = -1; n < 2; n++) {
				if(n == 0) continue;
				for (int i = -radius; i <= radius; i++) {
					if (orientationArr[5] == 1) {
						if (world.isAirBlock(x + i, y + (n * radius) + k, z + orientationArr[4]) || !world.getBlockMaterial(x + i, y + (n * radius) + k, z + orientationArr[4]).isSolid()) {
							if(k == -1) {
								downCount++;
							}else if(k == 0) {
								originalCount++;
							}else if (k == 1){
								upCount++;
							}
						}
					}else{
						if (world.isAirBlock(x + orientationArr[4], y + (n * radius) + k, z + i) || !world.getBlockMaterial(x + orientationArr[4], y + (n * radius) + k, z + i).isSolid()) {
							if(k == -1) {
								downCount++;
							}else if(k == 0) {
								originalCount++;
							}else if (k == 1){
								upCount++;
							}
						}
					}
				}
			}
		}
		if(upCount > originalCount) {
			if(downCount > upCount) {
				orientationArr[2]--;
				orientationArr[3]--;
			}else{
				orientationArr[2]++;
				orientationArr[3]++;
			}
		}else if(downCount > originalCount) {
			orientationArr[2]--;
			orientationArr[3]--;
		}
	}*/
}