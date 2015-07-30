package com.qkninja.network.item;

import com.qkninja.network.client.model.ModelUpgrade;
import com.qkninja.network.reference.ConfigValues;
import com.qkninja.network.reference.Names;
import com.qkninja.network.tileentity.TileEntityTransporter;
import com.qkninja.network.utility.ResourceLocationHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Defines the Red LED ring item, used as a speed upgrade.
 *
 * @author QKninja
 */
public class ItemRedLED extends UpgradeBase
{
    public ItemRedLED()
    {
        super(Names.Items.RED_LED,
                new ModelUpgrade(),
                ResourceLocationHelper.getResourceLocation(Names.Models.RED_LED));
    }

    @Override
    public void onAdded(TileEntityTransporter te)
    {
        te.setDelay(1);
    }

    @Override
    public void onRemoved(TileEntityTransporter te)
    {
        te.setDelay(ConfigValues.transportDelay);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(this);
    }
}
