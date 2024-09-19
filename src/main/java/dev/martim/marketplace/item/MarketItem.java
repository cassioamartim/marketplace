package dev.martim.marketplace.item;

import dev.martim.marketplace.manager.Manager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;

import java.lang.reflect.Method;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MarketItem {

    private final int id = Manager.getMarketData().list().size() + 1;

    private final UUID author;
    private final String data;

    private final int price;
    private final long createdAt = System.currentTimeMillis();

    public org.bukkit.inventory.ItemStack deserialize() {
        NBTTagCompound compound = new NBTTagCompound();

        // Using reflection to get Compound
        try {
            Method method = NBTTagCompound.class.getDeclaredMethod("a", String.class);
            compound = (NBTTagCompound) method.invoke(null, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemStack nmsStack = ItemStack.a(compound);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }
}
