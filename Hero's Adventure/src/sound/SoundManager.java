package sound;

public class SoundManager {

    public Sound backgroundMusic;
    public Sound battleMusic;
    public Sound dungeonMusic;
    public Sound attack;
    public Sound level_up;
    public Sound button_success, button_failed;
    public Sound ladder_slide;
    public Sound player_death;
    public Sound victory;
    public Sound dialogue;

    public SoundManager() {

        backgroundMusic = new Sound("/sound/background_music.wav");
        battleMusic = new Sound("/sound/battle_music.wav");
        dungeonMusic = new Sound("/sound/dungeon_music.wav");
        attack = new Sound("/sound/attack.wav");
        level_up = new Sound("/sound/level_up.wav");
        button_success = new Sound("/sound/button_success.wav");
        button_failed = new Sound("/sound/button_failed.wav");
        ladder_slide = new Sound("/sound/ladder_slide.wav");
        player_death = new Sound("/sound/player_death.wav");
        victory = new Sound("/sound/victory.wav");
        dialogue = new Sound("/sound/dialogue.wav");
    }
}
