package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip sound;
    FloatControl fc;
    URL url;

    public Sound(String file) {

        try {

            url = getClass().getResource(file);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            sound = AudioSystem.getClip();
            sound.open(ais);
            fc = (FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN);
            changeVolume(-2f);
        } catch (Exception e) {
        }
    }

    public void play() {
        sound.start();
    }

    public void loop() {
        sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        sound.stop();
    }

    public void replay() {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            sound = AudioSystem.getClip();
            sound.open(ais);
        } catch (Exception e) {
        }
        play();
    }

    public void playMusic() {
        play();
        loop();
    }

    public void changeVolume(float volume) {
        fc.setValue(volume);
    }
}
