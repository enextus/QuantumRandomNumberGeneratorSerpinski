//
//        -2,147,483,648 до -715,827,882 (Integer.MIN_VALUE до Integer.MIN_VALUE / 3)
//
//        -715,827,881 до 715,827,881 (Integer.MIN_VALUE / 3 + 1 до Integer.MAX_VALUE / 3)
//
//        715,827,882 до 2,147,483,647 (Integer.MAX_VALUE / 3 * 2 до Integer.MAX_VALUE)
//
//        1,431,655,767
//        1,431,655,763
//        1,431,655,766
//
//        1,431,655,767 + 1,431,655,763 + 1,431,655,766 = 4,294,967,296
//
package org.ThreeDotsSierpinski;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

class DotController extends JPanel {
    private String errorMessage = null; // Для хранения сообщения об ошибке
    public static final int SMALL_FONT_SIZE = 32;
    public static final int LARGE_FONT_SIZE = 78;

    private final String duplicateMessage = "";
    public static final float RANGETIMEFLOAT = 90f;
    public static final float TRANSPARENCYFLOAT = 0.85f;
    public static final float DARKNESSFLOAT = 1 - TRANSPARENCYFLOAT;
    private static final int SIZE = 925;
    public static final int DELAY_TIME = 1000;
    public static final int DOTWIDTH = 7;
    public static final int DOTHEIGHT = 7;
    Point dot;
    private final List<Dot> dots; //  list of dots
    private final RandomNumberProvider randomNumberProvider;
    private int dotCounter; // counter of the number of dots

    public DotController() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        dot = new Point(SIZE / 2, SIZE / 2);
        dots = new ArrayList<>();
        randomNumberProvider = new RandomNumberProvider();
        dotCounter = 0;
        int duplicateNumber = 0;
        setBackground(new Color(176, 224, 230));
    }

    public int getDotCounter() {
        return dotCounter;
    }

    private long toMillis(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (errorMessage != null) { // Проверяем, есть ли сообщение об ошибке
            // Отрисовка фона ошибки или самого сообщения об ошибке
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.RED);
            g.drawString(errorMessage, 10, 20); // Выводим сообщение об ошибке
        } else {

        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        //super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for (Dot dot : dots) {
            long diffInNanoTime = System.nanoTime() - dot.getCreationNanoTime();
            long diffInSeconds = diffInNanoTime / (DELAY_TIME * 1_000_000);  // convert nanoseconds to seconds
            float alpha = 1f - Math.min(TRANSPARENCYFLOAT, diffInSeconds / RANGETIMEFLOAT);

            alpha = Math.max(alpha, DARKNESSFLOAT);
            Color c = new Color(0, 0, 0, (int) (alpha * 255));

            if (alpha <= 1 - TRANSPARENCYFLOAT)
                c = new Color(0.0f, 0.0f, 0.0f, alpha);

            g2d.setColor(c);
            g2d.fillOval(dot.getPoint().x, dot.getPoint().y, DOTWIDTH, DOTHEIGHT);
        }


        if (!dots.isEmpty()) {
            g2d.setColor(new Color(255, 0, 0)); // bright red
            Dot lastDot = dots.get(dots.size() - 1);
            g2d.fillOval(lastDot.point.x, lastDot.point.y, DOTWIDTH, DOTHEIGHT);
        }

        Font myFont1 = new Font("Sans Serif", Font.ITALIC, SMALL_FONT_SIZE);
        Font myFont2 = new Font("Sans Serif", Font.ITALIC, LARGE_FONT_SIZE);


        int alpha1 = 128;
        int alpha2 = 64;

        g2d.setFont(myFont1);
        g2d.setColor(new Color(105, 105, 105, alpha1));  // blue text with adjusted transparency

        String text = "Dots count     ";
        int textX = SIZE - 50; // adjust these values to place the text in the desired location
        int textY = SIZE - 120;

        g2d.drawString(text, textX, textY); // "Counter  "

        g2d.setFont(myFont2);
        g2d.setColor(new Color(105, 105, 105, alpha2));  // dark gray text with adjusted transparency
        String counter = String.valueOf(getDotCounter()); // get the counter value as a string
        int counterX = textX + g2d.getFontMetrics(myFont1).stringWidth(text); // place the counter right after the text
        g2d.drawString(counter, counterX, textY);

        g2d.setFont(myFont1);
        g2d.setColor(new Color(105, 105, 105, alpha1));  // blue text with adjusted transparency
        int textYNew = textY - 200; // place the new text 200 pixels above the old one

        String text3 = "Last duplicates value    "; // the last value from a set of duplicate values
        g2d.drawString(text3, textX, textYNew);

        g2d.setFont(myFont2);
        g2d.setColor(new Color(105, 105, 105, alpha2));  // dark gray text with adjusted transparency
        String duplicateNumberStr = String.valueOf(RandomNumberProvider.lastDuplicateNumber); // get the duplicateNumber value as a string
        int duplicateNumberX = textX + g2d.getFontMetrics(myFont1).stringWidth(text3); // place the duplicateNumber right after the text3
        g2d.drawString(duplicateNumberStr, duplicateNumberX, textYNew); // print the duplicateNumber

        g2d.setFont(myFont1);
        g2d.setColor(new Color(105, 105, 105, alpha1));  // blue text with adjusted transparency
        int textYNew2 = textYNew - 100; // place the new text 100 pixels above the old one

        String text4 = "Duplicate Numbers Count    "; // the count of duplicate numbers
        g2d.drawString(text4, textX, textYNew2);

        g2d.setFont(myFont1);
        g2d.setColor(new Color(105, 105, 105, alpha1));  // blue text with adjusted transparency

        String text2 = "Number    ";
        int textX2 = SIZE - 50; // adjust these values to place the text in the desired location
        int textY2 = SIZE - 30;

        g2d.drawString(text2, textX2, textY2);

        g2d.setFont(myFont2);
        g2d.setColor(new Color(105, 105, 105, alpha2));  // dark gray text with adjusted transparency
        String value = String.valueOf(randomNumberProvider.getNextRandomNumber()); // get the dice value as a string
        int valueX = textX2 + g2d.getFontMetrics(myFont1).stringWidth(text2); // place the value right after the text
        g2d.drawString(value, valueX, textY2);
        }
    }

    public void moveDot() {
        try {
            int roll = randomNumberProvider.getNextRandomNumber();

            if (roll <= Integer.MIN_VALUE / 3 || roll > Integer.MAX_VALUE / 3 * 2) {
                dot.x = dot.x / 2;
                dot.y = dot.y / 2;
            } else if (roll <= Integer.MAX_VALUE / 3) {
                dot.x = SIZE / 2 + dot.x / 2;
                dot.y = dot.y / 2;
            } else {
                dot.x = dot.x / 2;
                dot.y = SIZE / 2 + dot.y / 2;
            }

            dots.add(new Dot(new Point(dot.x, dot.y)));
            dotCounter++;

            repaint();

        } catch (Exception e) { // Перехватываем исключение
            errorMessage = "Error: Cannot connect to Random Number Provider."; // Сохраняем сообщение об ошибке
            repaint(); // Перерисовываем компонент для отображения сообщения об ошибке
        }
    }
}