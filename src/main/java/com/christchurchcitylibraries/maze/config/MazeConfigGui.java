package com.christchurchcitylibraries.maze.config;

import java.util.ArrayList;
import java.util.List;

import com.christchurchcitylibraries.maze.CCLMaze;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;

public class MazeConfigGui extends GuiConfig {

	public MazeConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), CCLMaze.MODID, false, false, StatCollector.translateToLocal("config.CCLMaze.title"));
		titleLine2 = StatCollector.translateToLocal("config.CCLMaze.title2");
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements(GuiScreen parent) {

		List<IConfigElement> list = new ArrayList<IConfigElement>();
		// adds sections declared in MazeConfigHandler. toLowerCase() is used because
		// the configuration class automatically does this, so we must too.
		list.add(new ConfigElement(MazeConfigHandler.config.getCategory("Creation".toLowerCase())));
		list.add(new ConfigElement(MazeConfigHandler.config.getCategory("Quizzes".toLowerCase())));
		for (int i = 1; i < 11; i++) {
			list.add(new ConfigElement(MazeConfigHandler.config.getCategory("q&a_" + i)));
		}
		return list;
	}
}
