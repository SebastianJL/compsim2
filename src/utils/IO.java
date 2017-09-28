package utils;

public class IO {
    public static <T> void print(T obj) { System.out.println(obj); }

    public static void print(double[] a) { System.out.println(toString(a)); }

    public static <T> void print(T[] array) { System.out.println(toString(array));}

    private static <T> String toString(T[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i=0; i<array.length-1; i++) {
            sb.append(array[i]).append(",\n");
        }
        sb.append(array[array.length-1]).append("]");
        return sb.toString();
    }

    public static String toString(double[] a) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i=0; i<a.length-1; i++) {
            sb.append(a[i]).append(", ");
        }
        sb.append(a[a.length-1]).append("]");
        return sb.toString();
    }
}
