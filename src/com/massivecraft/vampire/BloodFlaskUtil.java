package com.massivecraft.vampire;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.UPlayer;

public class BloodFlaskUtil
{
	private final static String COLOR_RED = ChatColor.RED.toString();
	public final static String BLOOD_FLASK_NAME = COLOR_RED + "Blood Flask";
	public final static String BLOOD_FLASK_AMOUNT_SUFFIX = COLOR_RED + " unit(s) of blood.";
	public final static String BLOOD_FLASK_VAMPIRIC_TRUE = COLOR_RED + "The blood is vampiric.";
	public final static String BLOOD_FLASK_VAMPIRIC_FALSE = COLOR_RED + "The blood is not vampiric.";
	public final static PotionEffect BLOOD_FLASK_CUSTOM_EFFECT = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0);
	
	public static ItemStack createBloodFlask(double amount, boolean isVampiric)
	{
		// Create a new item stack of material potion ...
		ItemStack ret = new ItemStack(Material.POTION);
		
		// ... and convert the isVampiric boolean into a string ...
		String metaVampiric = isVampiric ? BLOOD_FLASK_VAMPIRIC_TRUE : BLOOD_FLASK_VAMPIRIC_FALSE;
		
		// ... create the item lore ...
		List<String> lore = MUtil.list(
			Double.toString(amount) + BLOOD_FLASK_AMOUNT_SUFFIX,
			metaVampiric
		);
		
		// ... and set the item meta ...
		PotionMeta meta = InventoryUtil.createMeta(ret);
		meta.setDisplayName(BLOOD_FLASK_NAME);
		meta.setLore(lore);
		meta.addCustomEffect(BLOOD_FLASK_CUSTOM_EFFECT, false);
		ret.setItemMeta(meta);
		
		// ... finally, return the result.
		return ret;
	}
	
	public static boolean isBloodFlaskVampiric(ItemStack item)
	{
		List<String> lore = InventoryUtil.getLore(item);
		String stringBoolean = lore.get(1);
		return BLOOD_FLASK_VAMPIRIC_TRUE.equals(stringBoolean);
	}
	
	public static double getBloodFlaskAmount(ItemStack item)
	{
		if (item == null) return 0D;
		List<String> lore = InventoryUtil.getLore(item);
		if (lore.isEmpty()) return 0D;
		
		String amountLoreString = lore.get(0);
		String amountString[] = amountLoreString.split(" ");
		String amountStr = amountString[0].substring(0, 3);
		double amount = Double.parseDouble(amountStr);
		
		return amount;
	}
	
	public static boolean isBloodFlask(ItemStack item)
	{
		return InventoryUtil.isDisplayName(item, BLOOD_FLASK_NAME);
	}
	
	public static boolean playerHoldsGlassBottle(Player player)
	{
		ItemStack item = InventoryUtil.getWeapon(player);
		if (item == null) return false;
		if (item.getType() != Material.GLASS_BOTTLE) return false;
		return true;
	}
	
	public static void playerConsumeGlassBottle(Player player)
	{
		ItemStack item = InventoryUtil.getWeapon(player);
		if (item == null) return;
		if (item.getType() != Material.GLASS_BOTTLE) return;
		int amount = item.getAmount();
		if (amount > 1)
		{
			item.setAmount(amount - 1);
		}
		else
		{
			player.getInventory().setItemInMainHand(null);
		}
	}
	
	public static void fillBottle(double amount, UPlayer uplayer)
	{
		BloodFlaskUtil.playerConsumeGlassBottle(uplayer.getPlayer());
		PlayerInventory playerInventory = uplayer.getPlayer().getInventory();
		playerInventory.addItem(BloodFlaskUtil.createBloodFlask(amount, uplayer.isVampire()));
	}
	
}
