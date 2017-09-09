package com.christchurchcitylibraries.maze.gui;

import com.christchurchcitylibraries.maze.tileentity.MazeCreatorTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MazeCreatorInventoryHandler extends Container {

	private MazeCreatorTileEntity mcte;

	public MazeCreatorInventoryHandler(IInventory playerInv, MazeCreatorTileEntity mcte) {
		this.mcte = mcte;

		// Tile Entity, Slot 0-2, Slot IDs 0-2
		for (int y = 0; y < 3; ++y) {
			this.addSlotToContainer(new SingleItemSlot(mcte, y, 8, 17 + y * 18));
		}

		// Player Inventory, Slot 3-29, Slot IDs 3-29
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 142 + i * 18));
			}
		}

		// Player Inventory, Slot 0-8, Slot IDs 30-38
		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 200));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {

		ItemStack previous = null;
		Slot slot = (Slot) this.inventorySlots.get(fromSlot);

		if (slot != null && slot.getHasStack()) {
			ItemStack current = slot.getStack();
			previous = current.copy();

			if (fromSlot < 3) {
				// From TE Inventory to Player Inventory
				if (!this.mergeItemStack(current, 3, 38, true)) {
					return null;
				}
			} else {
				// From Player Inventory to TE Inventory
				if (current.getItem() instanceof Item || !this.mergeItemStack(current, 0, 3, false)) {
					return null;
				}
			}

			if (current.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (current.stackSize == previous.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(playerIn, current);
		}
		return previous;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
		boolean success = false;
		int index = startIndex;

		if (useEndIndex)
			index = endIndex - 1;

		Slot slot;
		ItemStack stackinslot;
		if (stack.isStackable()) {
			while (stack.stackSize > 0 && (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex)) {
				slot = (Slot) this.inventorySlots.get(index);
				stackinslot = slot.getStack();

				if (stackinslot != null && stackinslot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == stackinslot.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, stackinslot)) {
					int l = stackinslot.stackSize + stack.stackSize;
					int maxsize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());

					if (l <= maxsize) {
						stack.stackSize = 0;
						stackinslot.stackSize = l;
						slot.onSlotChanged();
						success = true;
					} else if (stackinslot.stackSize < maxsize) {
						stack.stackSize -= stack.getMaxStackSize() - stackinslot.stackSize;
						stackinslot.stackSize = stack.getMaxStackSize();
						slot.onSlotChanged();
						success = true;
					}
				}
				if (useEndIndex) {
					--index;
				} else {
					++index;
				}
			}
		}
		if (stack.stackSize > 0) {
			if (useEndIndex) {
				index = endIndex - 1;
			} else {
				index = startIndex;
			}
			while (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex && stack.stackSize > 0) {
				slot = (Slot) this.inventorySlots.get(index);
				stackinslot = slot.getStack();

				// Forge: Make sure to respect isItemValid in the slot.
				if (stackinslot == null && slot.isItemValid(stack)) {
					if (stack.stackSize < slot.getSlotStackLimit()) {
						slot.putStack(stack.copy());
						stack.stackSize = 0;
						success = true;
						break;
					} else {
						ItemStack newstack = stack.copy();
						newstack.stackSize = slot.getSlotStackLimit();
						slot.putStack(newstack);
						stack.stackSize -= slot.getSlotStackLimit();
						success = true;
					}
				}
				if (useEndIndex) {
					--index;
				} else {
					++index;
				}
			}
		}
		return success;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.mcte.isUseableByPlayer(playerIn);
	}
}
