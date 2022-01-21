package forestry.core.recipes.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;

public abstract class ForestryRecipeCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<T> {
	private final IDrawable background;
	private final String localizedName;

	public ForestryRecipeCategory(IDrawable background, String unlocalizedName) {
		this.background = background;
		this.localizedName = new TranslatableComponent(unlocalizedName).getString();
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent(localizedName);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		recipe.setIngredients(ingredients);
	}
}
