package com.christchurchcitylibraries.maze.config;

public class Question {

	private String question;
	private String[] answers;
	private String correct;

	public Question(Object question, Object answers, Object correct) {
		this.question = (String) question;
		this.answers = (String[]) answers;
		this.correct = (String) correct;
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public String getCorrect() {
		return correct;
	}
}
