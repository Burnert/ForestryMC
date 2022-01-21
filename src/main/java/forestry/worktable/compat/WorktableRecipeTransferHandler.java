package forestry.worktable.compat;

import java.util.List;
import java.util.Map;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.core.utils.RecipeUtils;
import forestry.worktable.gui.ContainerWorktable;
import forestry.worktable.inventory.CraftingInventoryForestry;
import forestry.worktable.recipes.MemorizedRecipe;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
class WorktableRecipeTransferHandler implements IRecipeTransferHandler<ContainerWorktable, CraftingRecipe> {
	@Override
	public Class<ContainerWorktable> getContainerClass() {
		return ContainerWorktable.class;
	}

	@Override
	public Class<CraftingRecipe> getRecipeClass() {
		return CraftingRecipe.class;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(ContainerWorktable container, CraftingRecipe recipe, IRecipeLayout recipeLayout, Player player, boolean maxTransfer, boolean doTransfer) {
		if (doTransfer) {
			Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();

			CraftingInventoryForestry inventory = new CraftingInventoryForestry(container);

			for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : guiIngredients.entrySet()) {
				int recipeSlot = entry.getKey();
				List<ItemStack> allIngredients = entry.getValue().getAllIngredients();
				if (!allIngredients.isEmpty()) {
					if (recipeSlot != 0) { // skip the output slot
						ItemStack firstIngredient = allIngredients.get(0);
						inventory.setItem(recipeSlot - 1, firstIngredient);
					}
				}
			}

			List<CraftingRecipe> matchingRecipes = RecipeUtils.findMatchingRecipes(inventory, player.level);
			if (!matchingRecipes.isEmpty()) {
				MemorizedRecipe memorizedRecipe = new MemorizedRecipe(inventory, matchingRecipes);
				container.sendWorktableRecipeRequest(memorizedRecipe);
			}
		}

		return null;
	}
}
