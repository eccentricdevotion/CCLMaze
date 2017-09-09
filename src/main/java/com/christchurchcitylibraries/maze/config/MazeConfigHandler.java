package com.christchurchcitylibraries.maze.config;

import java.io.File;
import java.util.Random;

import com.christchurchcitylibraries.maze.item.EnumDyeColor;

import net.minecraftforge.common.config.Configuration;

public class MazeConfigHandler {

	public static Configuration config;
	// public static Configuration questionsAndAnswers;

	// Options
	public static String FloorBlock;
	public static String FloorColour;
	public static String BaseBlock;
	public static String BaseColour;
	public static String WallBlock;
	public static String WallColour;
	public static int Attempts;
	public static boolean EnableTiming;
	public static int DoorCloseTime;
	public static int TeleportX;
	public static int TeleportY;
	public static int TeleportZ;

	private static String[] colours = new String[16];

	// Q&As
	public static Question[] questions = new Question[10];
	// 10 question & answer categories
	private static String[] categories = { "Q&A_1", "Q&A_2", "Q&A_3", "Q&A_4", "Q&A_5", "Q&A_6", "Q&A_7", "Q&A_8", "Q&A_9", "Q&A_10" };
	// 10 questions
	public static String[] defaultQuestions = new String[10];
	private static String question1;
	private static String question2;
	private static String question3;
	private static String question4;
	private static String question5;
	private static String question6;
	private static String question7;
	private static String question8;
	private static String question9;
	private static String question10;
	private static Object[] questionObjs;
	// 10 answer sets
	private static String[] defaultAnswers = { "Answer A", "Answer B", "Answer C", "Answer D" };
	private static String[] answers1;
	private static String[] answers2;
	private static String[] answers3;
	private static String[] answers4;
	private static String[] answers5;
	private static String[] answers6;
	private static String[] answers7;
	private static String[] answers8;
	private static String[] answers9;
	private static String[] answers10;
	private static Object[] answerObjs;
	// 10 correct answers
	private static String correct1;
	private static String correct2;
	private static String correct3;
	private static String correct4;
	private static String correct5;
	private static String correct6;
	private static String correct7;
	private static String correct8;
	private static String correct9;
	private static String correct10;
	private static Object[] correctObjs;

	private static String[] randomAnswers = { "A", "B", "C", "D" };
	private static Random random = new Random();

	public static void initOptions(File file) {
		for (int i = 0; i < 16; i++) {
			colours[i] = EnumDyeColor.values()[i].getName().toUpperCase();
		}
		questionObjs = new Object[] { question1, question2, question3, question4, question5, question6, question7, question8, question9, question10 };
		answerObjs = new Object[] { answers1, answers2, answers3, answers4, answers5, answers6, answers7, answers8, answers9, answers10 };
		correctObjs = new Object[] { correct1, correct2, correct3, correct4, correct5, correct6, correct7, correct8, correct9, correct10 };
		for (int i = 0; i < 10; i++) {
			defaultQuestions[i] = "This is question number " + (i + 1);
		}
		config = new Configuration(file);
		syncConfigOptions();
	}

	public static void syncConfigOptions() {

		String category;

		category = "Creation";
		config.addCustomCategoryComment(category, "Maze creation options");
		FloorBlock = config.getString("FloorBlock", category, "cclmaze:maze_floor", "The default block used for maze floors if none is placed in the Maze Creator GUI.");
		FloorColour = config.getString("FloorColour", category, "WHITE", "The default colour used for maze floors (value is ignored if block can't be coloured).", colours);
		BaseBlock = config.getString("BaseBlock", category, "cclmaze:maze_wall", "The default block used for the maze wall base if none is placed in the Maze Creator GUI.");
		BaseColour = config.getString("BaseColour", category, "GREEN", "The default colour used for the maze base (value is ignored if block can't be coloured).", colours);
		WallBlock = config.getString("WallBlock", category, "cclmaze:maze_wall", "The default block used for the maze walls if none is placed in the Maze Creator GUI.");
		WallColour = config.getString("WallColour", category, "LIME", "The default colour used for maze walls (value is ignored if block can't be coloured).", colours);

		category = "Quizzes";
		config.addCustomCategoryComment(category, "Quiz options");
		Attempts = config.getInt("Attempts", category, 2, 1, 10, "The maximum number of attempts a player can make to answer a question. If they exceed this, they are teleported back to the previous door. Set to 0 to disable.");
		EnableTiming = config.getBoolean("EnableTiming", category, true, "Whether players are timed when they go through the maze.");
		DoorCloseTime = config.getInt("DoorCloseTime", category, 5, 1, 20, "The amount of time (in seconds) before a door closes behind a player.");
		TeleportX = config.getInt("TeleportX", category, 0, -30000, 30000, "The x-coordinate for the location the player teleports to when they complete the Maze Quiz.");
		TeleportY = config.getInt("TeleportY", category, 64, -30000, 30000, "The y-coordinate for the location the player teleports to when they complete the Maze Quiz.");
		TeleportZ = config.getInt("TeleportZ", category, 0, -30000, 30000, "The z-coordinate for the location the player teleports to when they complete the Maze Quiz.");

		for (int q = 0; q < 10; q++) {
			category = categories[q];
			config.addCustomCategoryComment(category, "A quiz question, multi-choice answers and the correct answer.");
			questionObjs[q] = config.getString("Question", category, defaultQuestions[q], "This is a question that players will answer to solve the Maze Quiz.");
			answerObjs[q] = config.getStringList("Answers", category, defaultAnswers, "These are the multi-choice answers a player can choose from.");
			correctObjs[q] = config.getString("CorrectAnswer", category, randomAnswers[random.nextInt(4)], "This is the correct answer.");
			Question question = new Question(questionObjs[q], answerObjs[q], correctObjs[q]);
			questions[q] = question;
		}
		config.save();
	}

	public static void syncQuestions() {
		for (int q = 0; q < 10; q++) {
			Question question = questions[q];
			config.get("q&a_" + (q + 1), "Question", defaultQuestions[q]).setValue(question.getQuestion());
			config.get("q&a_" + (q + 1), "Answers", defaultAnswers).set(question.getAnswers());
			config.get("q&a_" + (q + 1), "CorrectAnswer", randomAnswers[random.nextInt(4)]).setValue(question.getCorrect());
		}
	}
}
