package model;

import java.io.Serializable;
/*
 * ENUM for displaying possible outcomes when the user selects a song.
 * 
 * Programmers: Cynthia Shlezar and Patrick Kelly
 */
public enum SongSelection implements Serializable {
	NOT_ENOUGH_CREDIT, NO_PLAYS_REMAINING_SONG, NO_PLAYS_REMAINING_USER, SUCCESS, NOT_LOGGED_IN, SONG_NOT_EXIST, FAILURE;
}
