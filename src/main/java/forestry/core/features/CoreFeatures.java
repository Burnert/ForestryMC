package forestry.core.features;

import java.util.ArrayList;

import forestry.core.config.Config;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import forestry.core.blocks.EnumResourceType;
import forestry.core.config.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CoreFeatures {
	private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();

	public static void registerOres() {
		// Read config
		boolean bGenApatite = Config.generateApatiteOre;
		boolean bGenCopper = Config.generateCopperOre;
		boolean bGenTin = Config.generateTinOre;

		if (bGenApatite)
			overworldOres.add(register("apatite_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CoreBlocks.RESOURCE_ORE.get(EnumResourceType.APATITE).defaultState(), 36)).range(4).squared().count((56))));

		if (bGenCopper)
			overworldOres.add(register("copper_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CoreBlocks.RESOURCE_ORE.get(EnumResourceType.COPPER).defaultState(), 6)).range(20).squared().count(32)));

		if (bGenTin)
			overworldOres.add(register("tin_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CoreBlocks.RESOURCE_ORE.get(EnumResourceType.TIN).defaultState(), 6)).range(20).squared().count(16)));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void gen(BiomeLoadingEvent event) {
		if (event.getCategory() == Biome.Category.NETHER || event.getCategory() == Biome.Category.THEEND) {
			return;
		}

		BiomeGenerationSettingsBuilder generation = event.getGeneration();
		for (ConfiguredFeature<?, ?> ore : overworldOres) {
			if (ore != null) {
				generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
			}
		}
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Constants.MOD_ID + ":" + name, configuredFeature);
	}
}
