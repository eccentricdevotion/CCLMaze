package com.christchurchcitylibraries.maze.config;

import java.io.File;

import com.christchurchcitylibraries.maze.item.EnumDyeColor;

import net.minecraftforge.common.config.Configuration;

public class MazeConfigHandler {

	public static Configuration config;
	public static Configuration doors;

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
	private static String[] answers1 = { "Goodbye", "Bad", "A type of seafood", "Chicken" };
	private static String[] answers2 = { "Mouse", "Cat", "Sheep", "Dog" };
	private static String[] answers3 = { "Father", "Mother", "Children", "Grandma" };
	private static String[] answers4 = { "Lake", "River", "Sea", "Rain" };
	private static String[] answers5 = { "Water", "Big", "Grass", "Lunch" };
	private static String[] answers6 = { "Hand", "Foot", "Nose", "Tongue" };
	private static String[] answers7 = { "House", "Toilet", "Skyscraper", "Garage" };
	private static String[] answers8 = { "Spirit", "Water", "Clouds", "Air" };
	private static String[] answers9 = { "Come here", "Stay away", "Hurry up", "To delay" };
	private static String[] answers10 = { "Water", "Sky", "Wind", "Land" };
	private static String[][] answerObjs;
	// 10 correct answers
	private static String correct1 = "B";
	private static String correct2 = "D";
	private static String correct3 = "C";
	private static String correct4 = "A";
	private static String correct5 = "B";
	private static String correct6 = "D";
	private static String correct7 = "B";
	private static String correct8 = "A";
	private static String correct9 = "D";
	private static String correct10 = "C";
	private static String[] correctObjs;

	public static void initOptions(File file) {
		for (int i = 0; i < 16; i++) {
			colours[i] = EnumDyeColor.values()[i].getName().toUpperCase();
		}
		questionObjs = new Object[] { question1, question2, question3, question4, question5, question6, question7, question8, question9, question10 };
		answerObjs = new String[][] { answers1, answers2, answers3, answers4, answers5, answers6, answers7, answers8, answers9, answers10 };
		correctObjs = new String[] { correct1, correct2, correct3, correct4, correct5, correct6, correct7, correct8, correct9, correct10 };
		defaultQuestions[0] = "What is the English translation of the Maori word 'kino'?";
		defaultQuestions[1] = "What is the English translation of the Maori word 'kuri'?";
		defaultQuestions[2] = "What is the English translation of the Maori word 'tamariki'?";
		defaultQuestions[3] = "What is the English translation of the Maori word 'roto'?";
		defaultQuestions[4] = "What is the English translation of the Maori word 'nui'?";
		defaultQuestions[5] = "What is the English translation of the Maori word 'arero'?";
		defaultQuestions[6] = "What is the English translation of the Maori word 'paku'?";
		defaultQuestions[7] = "What is the English translation of the Maori word 'wairua'?";
		defaultQuestions[8] = "What is the English translation of the Maori word 'taihoa'?";
		defaultQuestions[9] = "What is the English translation of the Maori word 'hau'?";
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
			answerObjs[q] = config.getStringList("Answers", category, answerObjs[q], "These are the multi-choice answers a player can choose from.");
			correctObjs[q] = config.getString("CorrectAnswer", category, correctObjs[q], "This is the correct answer.");
			Question question = new Question(questionObjs[q], answerObjs[q], correctObjs[q]);
			questions[q] = question;
		}
		config.save();
	}

	public static void syncQuestions() {
		for (int q = 0; q < 10; q++) {
			Question question = questions[q];
			config.get("q&a_" + (q + 1), "Question", defaultQuestions[q]).setValue(question.getQuestion());
			config.get("q&a_" + (q + 1), "Answers", answerObjs[q]).set(question.getAnswers());
			config.get("q&a_" + (q + 1), "CorrectAnswer", correctObjs[q]).setValue(question.getCorrect());
		}
	}

	public static void initDoors(File file) {
		doors = new Configuration(file);
	}

	public static void addDoor(float fx, float fy, float fz, int iyaw) {
		// count current number of categories
		int c = doors.getCategoryNames().size() + 1;
		String category = "door_" + c;
		doors.addCustomCategoryComment(category, "A teleport location in front of a maze quiz start door.");
		doors.getFloat("x", category, fx, -30000, 30000, "This is the x co-ordinate of the location.");
		doors.getFloat("y", category, fy, 0, 256, "This is the y co-ordinate of the location.");
		doors.getFloat("z", category, fz, -30000, 30000, "This is the z co-ordinate of the location.");
		doors.getInt("yaw", category, iyaw, 0, 360, "This is the direction of the player when facing the door.");
		doors.save();
	}
}
