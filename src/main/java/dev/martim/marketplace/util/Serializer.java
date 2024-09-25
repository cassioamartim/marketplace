package dev.martim.marketplace.util;

import lombok.SneakyThrows;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTReadLimiter;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Serializer {

    @SneakyThrows
    public static byte[] serialize(ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);

        // Serializa o ItemStack para NBTTagCompound
        NBTTagCompound nbtTag = new NBTTagCompound();
        nmsItem.b(nbtTag);

        // Converte NBTTagCompound para bytes
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        NBTCompressedStreamTools.a(nbtTag, output);

        return output.toByteArray();
    }

    @SneakyThrows
    public static ItemStack deserialize(byte[] data) {
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        NBTTagCompound compound = NBTCompressedStreamTools.a(input, NBTReadLimiter.a());

        // Converte o NBTTagCompound para ItemStack
        net.minecraft.world.item.ItemStack nmsStack = net.minecraft.world.item.ItemStack.a(compound);

        ItemStack stack = CraftItemStack.asBukkitCopy(nmsStack);

        if (stack.getItemMeta() instanceof LeatherArmorMeta leatherMeta) {
            System.out.println("Cor do item restaurada: " + leatherMeta.getColor());
        }

        return stack;
    }
}
