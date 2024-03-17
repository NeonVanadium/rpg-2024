package view.window;

import java.io.File;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;

class AudioManager implements LineListener {
  private static final String TEXT_BLIP_FILE = "resources/audio/textblip.wav";
  private InputStream inputStream;
  private AudioInputStream audioStream;
  private DataLine.Info info;
  Clip audioClip;

  public AudioManager() {
    try {
      File f = new File(TEXT_BLIP_FILE);
      audioStream = AudioSystem.getAudioInputStream(f.toURI().toURL());

      audioClip = AudioSystem.getClip();
    } catch (Exception e) {
      System.out.println("ERROR initializing audio manager! " + e);
    }
  }

  @Override
  public void update(LineEvent event) {
    if (LineEvent.Type.START == event.getType()) {
      System.out.println("Playback started.");
    } else if (LineEvent.Type.STOP == event.getType()) {
      System.out.println("Playback completed.");
      closeBlip();
    }
  }

  public void playBlip() {
    try {
      if (!audioClip.isOpen()) {
        audioClip.open(audioStream);
      }
      if (!audioClip.isActive()) {
        audioClip.setMicrosecondPosition(0);
        audioClip.start();
      }
    } catch (Exception ex) {
      System.out.println("ERROR playing audio! " + ex.getMessage());
    }
  }

  public void signalDoneTyping() {

  }

  private void closeBlip() {
    try {
      audioClip.close();
    } catch (Exception ex) {
      System.out.println("ERROR closing audio file! " + ex.getMessage());
    }
  }
}
