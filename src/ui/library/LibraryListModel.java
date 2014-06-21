package ui.library;

public class LibraryListModel {

	private String songTitle;
	private boolean playing;
	private boolean editMode;

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getSongTitle() {

		return this.songTitle;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isInEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}
