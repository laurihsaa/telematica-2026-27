package pit2027;

import java.util.Scanner;

public final class KeyBoard {
    private static final Scanner SCANNER = new Scanner(System.in);

    private KeyBoard() {
    }

    public static int readInt() {
        String value = SCANNER.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            System.out.println("Introduzca un número entero válido.");
            return Integer.MAX_VALUE;
        }
    }

    public static char readChar() {
        String value = SCANNER.nextLine();
        if (value.isEmpty()) {
            return Character.MAX_VALUE;
        }
        return value.charAt(0);
    }

    public static String readString() {
        return SCANNER.nextLine();
    }
}
