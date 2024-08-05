package view.window;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

class AudioManager implements LineListener {
  private static final String TEXT_BLIP_FILE = "resources/audio/textblip.wav";
  private static final String CONFIRM_BLIP_FILE = "resources/audio/confirm.wav";
  private static final boolean TYPE_BLIPS = false, CONTINUE_BLIPS = true; // whether to use these sounds

  private AudioInputStream typeStream, confirmStream;
  Clip typeClip;
  Clip confirmClip;

  public AudioManager() {
    try {
      typeStream = AudioSystem.getAudioInputStream(new File(TEXT_BLIP_FILE).toURI().toURL());
      typeClip = AudioSystem.getClip();
      confirmStream = AudioSystem.getAudioInputStream(new File(CONFIRM_BLIP_FILE).toURI().toURL());
      confirmClip = AudioSystem.getClip();
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
    if (TYPE_BLIPS) playClip(typeClip, typeStream);
  }

  public void playUserEnter() {
    if (CONTINUE_BLIPS) playClip(confirmClip, confirmStream);
  }

  private void playClip(Clip c, AudioInputStream s) {
    try {
      if (!c.isOpen()) {
        c.open(s);
      }
      if (!c.isActive()) {
        c.setMicrosecondPosition(0);
        c.start();
      }
    } catch (Exception ex) {
      System.out.println("ERROR playing audio! " + ex.getMessage());
    }
  }

  public void signalDoneTyping() {

  }

  private void closeBlip() {
    try {
      typeClip.close();
    } catch (Exception ex) {
      System.out.println("ERROR closing audio file! " + ex.getMessage());
    }
  }
}
