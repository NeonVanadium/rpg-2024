package game.characters;

public enum Gender {
  MAN, WOMAN, SOMETHING_ELSE;

  public static Gender getFromString(String s) {
    String string = s.trim();
    if (string.equals("MAN") || string.equals("MALE")) {
      return Gender.MAN;
    } else if (string.equals("WOMAN") || string.equals("FEMALE")) {
      return Gender.WOMAN;
    } else {
      return Gender.SOMETHING_ELSE;
    }
  }
}
