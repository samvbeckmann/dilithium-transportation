package com.qkninja.network.item;

import com.qkninja.network.client.model.ModelCrystal;
import com.qkninja.network.handler.DistanceHandler;
import com.qkninja.network.reference.Names;
import com.qkninja.network.tileentity.TileEntityTransporter;
import com.qkninja.network.utility.ResourceLocationHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

/**
 * Defines the Core item.
 *
 * @author QKninja
 */
public class ItemItemCore extends ItemNetwork implements INetworkModCore
{
    public ItemItemCore()
    {
        super();
        setUnlocalizedName(Names.Items.ITEM_CORE);
    }

    @Override
    public void onNexusImportCall(TileEntityTransporter nexus)
    {
        if (nexus.getStackInSlot(0) == null)
        {
            TileEntity tile = nexus.getAttachedTileEntity();
            if (tile != null && tile instanceof IInventory)
            {
                IInventory inv = (IInventory) tile;
                if (inv instanceof ISidedInventory)
                {
                    ISidedInventory sided = (ISidedInventory) inv;
                    for (int slot : sided.getAccessibleSlotsFromSide(nexus.getBlockMetadata()))
                    {
                        if (inv.getStackInSlot(slot) != null)
                        {
                            nexus.setInventorySlotContents(0, inv.decrStackSize(slot, 1));
                            return;
                        }
                    }

                } else
                {
                    for (int i = 0; i < inv.getSizeInventory(); i++)
                    {
                        if (inv.getStackInSlot(i) != null)
                        {
                            nexus.setInventorySlotContents(0, inv.decrStackSize(i, 1));
                            return;
                        }
                    }
                }
            }
        }
        attemptTeleport(nexus);
    }

    @Override
    public void onNexusExportCall(TileEntityTransporter nexus)
    {
        if (nexus.getStackInSlot(0) != null)
        {
            TileEntity tile = nexus.getAttachedTileEntity();
            if (tile != null && tile instanceof IInventory)
            {
                IInventory inv = (IInventory) tile;
                ItemStack remainder = TileEntityHopper.func_145889_a(inv, nexus.getStackInSlot(0), nexus.getBlockMetadata());
                if (remainder == null)
                {
                    nexus.setInventorySlotContents(0, null);
                    return;
                }
            }
        }
        attemptTeleport(nexus);
    }

    @Override
    public void onNexusNeutralCall(TileEntityTransporter nexus)
    {
        attemptTeleport(nexus);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(this);
    }

    @Override
    public ModelBase getModel()
    {
        return new ModelCrystal();
    }

    @Override
    public ResourceLocation getTexture()
    {
        return ResourceLocationHelper.getResourceLocation(Names.Models.CRYSTAL);
    }

    private boolean attemptTeleport(TileEntityTransporter nexus)
    {
        if (nexus.getStackInSlot(0) != null)
        {
            Collections.sort(nexus.getExportLocations());
            for (DistanceHandler loc : nexus.getExportLocations())
            {
                TileEntity tile = nexus.getWorldObj().getTileEntity(loc.getX(), loc.getY(), loc.getZ());
                if (tile instanceof TileEntityTransporter) // Should always be true
                {
                    TileEntityTransporter transporter = (TileEntityTransporter) tile;
                    if (transporter.getStackInSlot(0) == null)
                    {
                        transporter.setInventorySlotContents(0, nexus.getStackInSlot(0));
                        nexus.decrStackSize(0, 1);
                        transporter.resetCounter();
                        nexus.resetCounter();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}